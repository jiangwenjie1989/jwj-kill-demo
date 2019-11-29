package com.jwj.im.common;

import lombok.Data;

/**
 * @ClassName : RedisMassgae  //类名
 * @Description : 消息类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-15 17:14  //时间
 */
@Data
public class RedisMassgae {

    //频道
    private String channel;

    //发送类型 0表示内部消息 1表示点对点发送 2表示广播
    private Integer sendType;

    //发送内容
    private Message message;

}
