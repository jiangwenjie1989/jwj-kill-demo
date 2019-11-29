package com.jwj.im.config;


/**
 * @ClassName : RedisMsg  //类名
 * @Description : 定义接受信息的接口  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-15 16:48  //时间
 */
public interface RedisMsg {
    /**
     * 接受信息
     * @param redisMassgae
     */
    public void receiveMessage(String redisMassgae);


}
