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
 * 活动商品表(kill_activity_goods)
 * 
 * @author jwj
 * @version 1.0.0 2019-11-28
 */
@Data
@Entity
@Table(name = "kill_activity_goods")
public class KillActivityGoods implements java.io.Serializable {

    private static final long serialVersionUID = 1172517527946267631L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 19)
    private Long id;

    /** 商品名称 */
    @Column(name = "goods_name", nullable = true, length = 200)
    private String goodsName;

    /** 商品总数量 */
    @Column(name = "goods_num", nullable = true, length = 10)
    private Integer goodsNum;

    /** 创建时间 */
    @Column(name = "create_time", nullable = true, length = 19)
    private Timestamp createTime;

    /** 版本号 */
    @Column(name = "version", nullable = true, length = 19)
    private Long version;


}