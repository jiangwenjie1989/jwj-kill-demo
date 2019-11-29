package com.jwj.domain;/*
 * Welcome to use the TableGo Tools.
 * 
 * http://www.tablego.cn
 * 
 * http://vipbooks.iteye.com
 * http://blog.csdn.net/vipbooks
 * http://www.cnblogs.com/vipbooks
 * 
 * Author: bianj
 * Email: tablego@qq.com
 * Version: 6.6.6
 */

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 秒杀活动订单表(kill_order)
 * 
 * @author jwj
 * @version 1.0.0 2019-11-28
 */
@Data
@Entity
@Table(name = "kill_order")
public class KillOrder implements java.io.Serializable {

    private static final long serialVersionUID = 6395817971414131927L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 活动id */
    @Column(name = "activity_id", nullable = true, length = 19)
    private Long activityId;

    /** 用户id */
    @Column(name = "user_id", nullable = true, length = 19)
    private Long userId;

    /** 创建时间 */
    @Column(name = "create_time", nullable = true, length = 19)
    private Timestamp createTime;

    /** 状态 1待支付 2代发货 */
    @Column(name = "status", nullable = true, length = 10)
    private Integer status;

}