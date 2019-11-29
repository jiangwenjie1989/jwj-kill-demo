package com.jwj.im.controller;

import com.jwj.im.common.SwggerCommonTags;
import com.jwj.im.config.annotation.CacheLock;
import com.jwj.im.config.annotation.CacheParam;
import com.jwj.im.response.SimpleResponse;
import com.jwj.im.service.KillService;
import com.jwj.im.syse.HttpCodeE;
import com.jwj.im.syse.SysRespStatusE;
import com.jwj.im.until.id.IdUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : KillController  //类名
 * @Description : 秒杀控制层  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-28 14:28  //时间
 */
@RestController
@RequestMapping("/kill/api/robGoodsActivity")
public class KillController {

    @Autowired
    private KillService killService;

    /**
     * 通过aop利用redis分布式锁完成   锁的过期时间为1秒  拦截器类名称：LockMethodInterceptor
     * 该接口立刻响应结果，业务场景用户如果没抢到可以一直点击，直到商品库存数量为0
     * 例如：我库存有100个商品，那么锁的过期时间是1秒，就相当于这些商品需要100秒的时间才能抢完
     * @param userId
     * @param killActivityId
     * @return
     * @throws Exception
     */
    @CacheLock(prefix = "kill:robGoodsByAopLock:robGoods")
    @ApiOperation(value = "秒杀商品-通过aop利用redis分布式锁完成", httpMethod = "POST", tags = SwggerCommonTags.KILL_ACTIVITY_MODULE)
    @RequestMapping(value = "/robGoodsByAopLock.do", method = RequestMethod.POST)
    public SimpleResponse robGoodsByAopLock(
            @ApiParam(required = false, name = "userId", value = "用户ID(必填)")
            @RequestParam(defaultValue = "0") Long userId,

            @CacheParam(name = "killActivityId")
            @ApiParam(required = false, name = "killActivityId", value = "秒杀活动id(必填)")
            @RequestParam(defaultValue = "0") Long killActivityId) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        //为了方便测试写死
        killActivityId=1L;
        userId= IdUtils.getId();
        if ( userId == 0 || killActivityId==0 ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数有误！");
        }
        return killService.robGoodsByAopLock(userId, killActivityId);
    }



//    @ApiOperation(value = "秒杀商品-通过RedissonLock分布式锁完成", httpMethod = "POST", tags = SwggerCommonTags.KILL_ACTIVITY_MODULE)
//    @RequestMapping(value = "/robGoodsByRedissonLock.do", method = RequestMethod.POST)
//    public SimpleResponse robGoodsByRedissonLock(
//            @ApiParam(required = false, name = "userId", value = "用户ID(必填)")
//            @RequestParam(defaultValue = "0") Long userId,
//
//            @ApiParam(required = false, name = "killActivityId", value = "秒杀活动id(必填)")
//            @RequestParam(defaultValue = "0") Long killActivityId) throws Exception {
//        SimpleResponse resp = new SimpleResponse();
//        //为了方便测试写死
//        killActivityId=1L;
//        userId= IdUtils.getId();
//        if ( userId == 0 || killActivityId==0 ) {
//            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数有误！");
//        }
//        return killService.robGoodsByRedissonLock(userId, killActivityId);
//    }


    @ApiOperation(value = "秒杀商品-通过Redis队列以及-消息发布与订阅完成", httpMethod = "POST", tags = SwggerCommonTags.KILL_ACTIVITY_MODULE)
    @RequestMapping(value = "/robGoodsByRedisPush.do", method = RequestMethod.POST)
    public SimpleResponse robGoodsByRedisPush(
            @ApiParam(required = false, name = "userId", value = "用户ID(必填)")
            @RequestParam(defaultValue = "0") Long userId,

            @ApiParam(required = false, name = "killActivityId", value = "秒杀活动id(必填)")
            @RequestParam(defaultValue = "0") Long killActivityId) throws Exception {
        SimpleResponse resp = new SimpleResponse();
        //为了方便测试写死
        killActivityId=1L;
        userId= IdUtils.getId();
        if ( userId == 0 || killActivityId==0 ) {
            return resp.setReturnErrMsg(resp, HttpCodeE.参数有误.value, SysRespStatusE.失败.getDesc(), "参数有误！");
        }
        return killService.robGoodsByRedisPush(userId, killActivityId);
    }



}
