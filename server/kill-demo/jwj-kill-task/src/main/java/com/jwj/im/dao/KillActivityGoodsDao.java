package com.jwj.im.dao;


import com.jwj.domain.KillActivityGoods;
import com.jwj.im.repository.KillActivityGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName : KillActivityGoodsDao  //类名
 * @Description : 秒杀活动dao  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-28 14:31  //时间
 */
@Repository
public class KillActivityGoodsDao {

    @Autowired
    private KillActivityGoodsRepository killActivityGoodsRepository;


    public KillActivityGoods findById(Long killActivityId) {
        return killActivityGoodsRepository.findOne(killActivityId);
    }

    public KillActivityGoods save(KillActivityGoods killActivityGoods) {
        return killActivityGoodsRepository.save(killActivityGoods);
    }
}
