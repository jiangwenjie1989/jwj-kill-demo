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
import com.jwj.im.redis.redisson.RedissLockUtil;
import com.jwj.im.response.SimpleResponse;
import com.jwj.im.service.KillService;
import com.jwj.im.syse.HttpCodeE;
import com.jwj.im.syse.SysRespStatusE;
import com.jwj.im.until.DateUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : KillServiceImpl  //类名
 * @Description : 秒杀实现  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-28 14:35  //时间
 */
@Service
public class KillServiceImpl implements KillService {

    @Autowired
    private KillOrderDao killOrderDao;

    @Autowired
    private KillActivityGoodsDao killActivityGoodsDao;

    @Value("${channel.order.name}")
    private  String orderChannel;

    @Autowired
    private RedisStringCacheSupport redisString;

    @Override
    @Transactional
    public SimpleResponse robGoodsByOptimismLock(Long userId, Long killActivityId) {
        SimpleResponse resp = new SimpleResponse();
        KillActivityGoods killActivityGoods=killActivityGoodsDao.findById(killActivityId);
        if (killActivityGoods == null) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该秒杀活动不存在！");
        }
        Optional<KillOrder> opt=killOrderDao.findByUserId(userId);
        if(opt.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "您已经抢到该商品！");
        }
        Integer goodsNum = killActivityGoods.getGoodsNum();
        if(goodsNum>0){
            saveOrderAndUpdateNum(userId, killActivityId, killActivityGoods);
        }else {
            killActivityGoods.setGoodsNum(0);
            killActivityGoodsDao.save(killActivityGoods);
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该商品已经抢完！");
        }
        return resp;
    }


    @Override
    @Transactional
    public SimpleResponse robGoodsByAopLock(Long userId, Long killActivityId) {
        SimpleResponse resp = new SimpleResponse();
        KillActivityGoods killActivityGoods=killActivityGoodsDao.findById(killActivityId);
        if (killActivityGoods == null) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该秒杀活动不存在！");
        }
        Optional<KillOrder> opt=killOrderDao.findByUserId(userId);
        if(opt.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "您已经抢到该商品！");
        }
        Integer goodsNum = killActivityGoods.getGoodsNum();
        if(goodsNum>0){
            saveOrderAndUpdateNum(userId, killActivityId, killActivityGoods);
        }else {
            killActivityGoods.setGoodsNum(0);
            killActivityGoodsDao.save(killActivityGoods);
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该商品已经抢完！");
        }
        return resp;
    }

    @Override
    @Transactional
    public SimpleResponse robGoodsByRedissonLock(Long userId, Long killActivityId) {
        SimpleResponse resp = new SimpleResponse();
        boolean res=false;
        try {
            //尝试获取锁，最多等待3秒，上锁以后10秒自动解锁（实际项目中如果用Redisson推荐这种，以防出现死锁）
            res = RedissLockUtil.tryLock(killActivityId+"", TimeUnit.SECONDS, 1, 1);
            if(res){
                KillActivityGoods killActivityGoods=killActivityGoodsDao.findById(killActivityId);
                if (killActivityGoods == null) {
                    return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该秒杀活动不存在！");
                }
                Optional<KillOrder> opt=killOrderDao.findByUserId(userId);
                if(opt.isPresent()){
                    return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "您已经抢到该商品！");
                }
                Integer goodsNum = killActivityGoods.getGoodsNum();
                if(goodsNum>0){
                    saveOrderAndUpdateNum(userId, killActivityId, killActivityGoods);
                }else {
                    killActivityGoods.setGoodsNum(0);
                    killActivityGoodsDao.save(killActivityGoods);
                    return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该商品已经抢完！");
                }
            }else{
                return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "人数过多，请再次尝试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                RedissLockUtil.unlock(killActivityId+"");
            }
        }
        return resp;
    }

    @Override
    public SimpleResponse robGoodsByRedisPush(Long userId, Long killActivityId) {
        SimpleResponse resp = new SimpleResponse();
        //缓存key
        String keyHash = RedisCacheKeys.user_Rob_Goods_Key + killActivityId;
        String hashCached = redisString.getHashCached(keyHash, userId+"");
        if( hashCached != null && !"".equals(hashCached) ){//判断是否已抢过
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "人数过多，请再次尝试！");
        }

        KillActivityGoods killActivityGoods=killActivityGoodsDao.findById(killActivityId);
        if (killActivityGoods == null) {
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该秒杀活动不存在！");
        }
        Optional<KillOrder> opt=killOrderDao.findByUserId(userId);
        if(opt.isPresent()){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "您已经抢到该商品！");
        }
        Integer goodsNum = killActivityGoods.getGoodsNum();
        if(goodsNum<=0){
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "该商品已经抢完！");
        }
        //缓冲队列
        String keyQueue= RedisCacheKeys.Rob_Goods_Activity_Queue_Key + killActivityId;
        long size = redisString.sCard(keyQueue);
        if(size<=goodsNum.longValue()){
            RedisMassgae redisMassgae = new RedisMassgae();
            redisMassgae.setChannel(orderChannel);
            redisMassgae.setSendType(0);
            Message message = new Message();
            message.setText(userId+"#"+killActivityId);
            redisMassgae.setMessage(message);
            redisString.sendMessage(orderChannel, JSON.toJSONString(redisMassgae));
            return resp;
        }else{
            return resp.setReturnErrMsg(resp, HttpCodeE.数据验证不通过.value, SysRespStatusE.失败.getDesc(), "人数过多，请再次尝试！");
        }
    }




    /**
     * 修改库存数保存订单
     * @param userId
     * @param killActivityId
     * @param killActivityGoods
     */
    private void saveOrderAndUpdateNum(Long userId, Long killActivityId, KillActivityGoods killActivityGoods) {
        killActivityGoods.setGoodsNum(killActivityGoods.getGoodsNum() - 1);
        killActivityGoodsDao.save(killActivityGoods);
        KillOrder killOrder = new KillOrder();
        killOrder.setActivityId(killActivityId);
        killOrder.setUserId(userId);
        killOrder.setCreateTime(DateUntil.getNowTimestamp());
        killOrder.setStatus(1);
        killOrderDao.save(killOrder);
    }
}
