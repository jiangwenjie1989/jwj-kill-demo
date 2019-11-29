package com.jwj.im.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : Message  //类名
 * @Description : 消息  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-12 19:20  //时间
 */
@Data
public class Message {

    @ApiModelProperty(value = "发送人id")
    private String fromUserId;
    
    @ApiModelProperty(value = "发送人名称")
    private String fromUsername;

    @ApiModelProperty(value = "接收人id")
    private String toUserId;
    
    @ApiModelProperty(value = "接收人名称")
    private String toUsername;

    @ApiModelProperty(value = "群id")
    private String groupId;

    @ApiModelProperty(value = "内容")
    private String text;

    @ApiModelProperty(value = "总人数")
    private Integer peopleNum;

    @ApiModelProperty(value = "发送类型 1表示点对点发送 2表示广播")
    private Integer sendType;
    
    @ApiModelProperty(value = "消息类型 1上线消息 2下线消息")
    private Integer messageType;
    

    
  

}
