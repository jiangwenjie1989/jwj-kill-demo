package com.jwj.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.jwj.domain.KillActivityGoods;
import com.jwj.domain.KillOrder;
import com.jwj.im.common.Message;
import com.jwj.im.common.RedisCacheKeys;
import com.jwj.im.common.RedisMassgae;
import com.jwj.im.dao.KillActivityGoodsDao;
import com.jwj.im.dao.KillOrderDao;
import com.jwj.im.redis.RedisStringCacheSupport;
import com.jwj.im.until.DateUntil;
import com.jwj.im.service.KillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.Set;

/**
 * @ClassName : KillServiceImpl  //类名
 * @Description :   //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-29 11:10  //时间
 */
@Service
public class KillServiceImpl implements KillService {

    @Autowired
    private KillOrderDao killOrderDao;

    @Autowired
    private KillActivityGoodsDao killActivityGoodsDao;

    @Value("${channel.im.name}")
    private String imChannel;

    @Autowired
    private RedisStringCacheSupport redisString;



    @Override
    @Transactional
    public void handleRobGoodsToQueueTask() {
        //暂时写死
        String keyQueue= RedisCacheKeys.Rob_Goods_Activity_Queue_Key + "1";

        Set<String> sMembers = redisString.sMembers(keyQueue);
        if(!CollectionUtils.isEmpty(sMembers)){
            for (String st : sMembers) {
                String[] split = st.split("#");
                String userId=split[0];
                String killActivityId=split[1];
                //缓存key
                String hashKey = RedisCacheKeys.user_Rob_Goods_Key + killActivityId;


                KillActivityGoods killActivityGoods=killActivityGoodsDao.findById(Long.parseLong(killActivityId));

                //判断是否抢完
                if (killActivityGoods.getGoodsNum()>0) {
                    redisString.putHashCached(hashKey, userId+"", killActivityId);
                    KillOrder killOrder = new KillOrder();
                    killOrder.setActivityId(Long.parseLong(killActivityId));
                    killOrder.setUserId(Long.parseLong(userId));
                    killOrder.setCreateTime(DateUntil.getNowTimestamp());
                    killOrder.setStatus(1);
                    KillOrder killOrderDb = killOrderDao.save(killOrder);

                    killActivityGoods.setGoodsNum(killActivityGoods.getGoodsNum() - 1);
                    killActivityGoodsDao.save(killActivityGoods);

                    RedisMassgae redisMassgae = new RedisMassgae();
                    redisMassgae.setChannel(imChannel);
                    redisMassgae.setSendType(2);

                    Message message = new Message();
                    redisMassgae.setMessage(message);
                    message.setFromUserId(userId);
                    message.setMessageType(2);
                    message.setText("恭喜用户userId="+userId+"抢到了商品"+killActivityGoods.getGoodsName()+"订单主键id="+killOrderDb.getId());
                    redisString.sendMessage(imChannel, JSON.toJSONString(redisMassgae));


                }else{
                    killActivityGoods.setGoodsNum(0);
                    killActivityGoodsDao.save(killActivityGoods);
                }

            }
            redisString.deleteCached(keyQueue);
        }
    }
}
