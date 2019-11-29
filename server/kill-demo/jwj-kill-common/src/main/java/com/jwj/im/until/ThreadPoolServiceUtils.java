package com.jwj.im.until;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : ThreadPoolServiceUtils  //类名
 * @Description : 单例获取线程池  懒汉式//描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-10-15 16:19  //时间
 */
public class ThreadPoolServiceUtils {

    private static final int DEFAULT_CORE_SIZE = 10;
    private static final int MAX_QUEUE_SIZE = 100;
    private volatile static ThreadPoolExecutor executor;

    // 获取单例的线程池对象
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (ThreadPoolServiceUtils.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,// 核心线程数
                            MAX_QUEUE_SIZE, // 最大线程数
                            Integer.MAX_VALUE, // 闲置线程存活时间
                            TimeUnit.MILLISECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                            Executors.defaultThreadFactory()// 线程工厂
                    );
                }
            }
        }
        return executor;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    // 从线程队列中移除对象
    public void cancel(Runnable runnable) {
        if (executor != null) {
            executor.getQueue().remove(runnable);
        }
    }

    public static void shutdown(ThreadPoolExecutor executor) {
        if (executor != null) {
            executor.shutdown();
        }
    }

}
