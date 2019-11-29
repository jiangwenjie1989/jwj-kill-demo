package com.jwj.im.config;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
/**
 * @ClassName : RedisContainerConfig  //类名
 * @Description : Redis消息监听者容器  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-15 15:45  //时间
 */
@Configuration
public class RedisContainerConfig {

    @Value("${channel.order.name}")
    private  String orderChannel;

    @Value("${channel.user.name}")
    private  String userChannel;

    @Value("${channel.goods.name}")
    private  String goodsChannel;

    @Value("${channel.im.name}")
    private  String imChannel;

    /**
     * redis消息监听器容器 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean // 相当于xml中的bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅了一个叫chat 的通道
        container.addMessageListener(listenerAdapter, new PatternTopic(orderChannel));
        container.addMessageListener(listenerAdapter, new PatternTopic(userChannel));
        container.addMessageListener(listenerAdapter, new PatternTopic(goodsChannel));
        container.addMessageListener(listenerAdapter, new PatternTopic(imChannel));
        // 这个container 可以添加多个 messageListener
        
        /**
         * 如果不定义线程池，每一次消费都会创建一个线程，浪费开销
         */
//        ThreadFactory factory = new ThreadFactoryBuilder()
//                .setNameFormat("redis-listener-pool-%d").build();
//        Executor executor = new ThreadPoolExecutor(
//                1,//线程池
//                1,// 最大线程数
//                5L,// 闲置线程存活时间
//                TimeUnit.SECONDS,// 时间单位
//                new LinkedBlockingQueue<>(1000),// 线程队列
//                factory);
//        container.setTaskExecutor(executor);
        
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisMsg receiver) {
        // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        // 也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

}
