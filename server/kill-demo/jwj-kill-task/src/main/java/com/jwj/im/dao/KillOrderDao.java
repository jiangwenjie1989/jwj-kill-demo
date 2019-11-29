package com.jwj.im.dao;


import com.jwj.domain.KillOrder;
import com.jwj.im.repository.KillOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @ClassName : KillOrderDao  //类名
 * @Description : 订单dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-28 14:30  //时间
 */
@Repository
public class KillOrderDao {

    @Autowired
    private KillOrderRepository killOrderRepository;

    public KillOrder save(KillOrder killOrder) {
        return killOrderRepository.save(killOrder);
    }

    public Optional<KillOrder> findByUserId(Long userId) {
        return killOrderRepository.findByUserId(userId);
    }
}
