

drop table if exists kill_activity_goods;

/*==============================================================*/
/* Table: kill_activity_goods                                   */
/*==============================================================*/
create table kill_activity_goods
(
   id                   bigint not null auto_increment,
   goods_name           varchar(200) comment '商品名称',
   goods_num            int comment '商品总数量',
   create_time          timestamp NULL DEFAULT NULL COMMENT '创建时间',
   version              bigint comment '版本号',
   primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

alter table kill_activity_goods comment '活动商品表';


drop table if exists kill_order;

/*==============================================================*/
/* Table: kill_order                                            */
/*==============================================================*/
create table kill_order
(
   id                   bigint not null auto_increment,
   activity_id          bigint comment '活动id',
   user_id              bigint comment '用户id',
   create_time          timestamp NULL DEFAULT NULL COMMENT '创建时间',
   status               int comment '状态 1待支付 2代发货',
   primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

alter table kill_order comment '秒杀活动订单表';
