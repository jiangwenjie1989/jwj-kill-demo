package com.jwj.im.config;

import com.alibaba.fastjson.JSON;
import com.jwj.im.common.Message;
import com.jwj.im.common.RedisCacheKeys;
import com.jwj.im.common.RedisMassgae;

import com.jwj.im.redis.RedisStringCacheSupport;
import com.jwj.im.service.KillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName : TestRedisListener  //类名
 * @Description : 测试  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-14 19:37  //时间
 */
@Component
public class ChannelRedisListener implements RedisMsg {

    @Value("${channel.order.name}")
    private  String orderChannel;

    @Value("${channel.user.name}")
    private  String userChannel;

    @Value("${channel.goods.name}")
    private  String goodsChannel;

    @Value("${channel.im.name}")
    private  String imChannel;

    @Autowired
    private RedisStringCacheSupport redisString;


    @Autowired
    private MyWebSocket myWebSocket;

    @Override
    public void receiveMessage(String massgae){
        RedisMassgae redisMassgae = JSON.parseObject(massgae, RedisMassgae.class);
        Message messageData = redisMassgae.getMessage();
        if(redisMassgae.getChannel().equals(orderChannel)){

            if(redisMassgae.getSendType()==0){
                String text = messageData.getText();
                String[] split = text.split("#");
                String userId=split[0];
                String killActivityId=split[1];
                //缓存队列key
                String keyQueue= RedisCacheKeys.Rob_Goods_Activity_Queue_Key + killActivityId;
                redisString.sAdd(keyQueue, messageData.getText());

            }
        }else if(redisMassgae.getChannel().equals(userChannel)){
            System.out.println("完成用户逻辑");
        }else if(redisMassgae.getChannel().equals(goodsChannel)){
            System.out.println("完成商品逻辑");
        }else if(redisMassgae.getChannel().equals(imChannel)){
            Integer sendType = redisMassgae.getSendType();
            if(sendType.equals(1)){//点对点
                myWebSocket.sendMessageToUser(JSON.toJSONString(messageData),messageData.getToUserId());
            }else if(sendType.equals(2)){//广播
                myWebSocket.broadcast(JSON.toJSONString(messageData));
            }
        }
    }
}
