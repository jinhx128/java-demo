package com.luoyu.java.time;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;

@Slf4j
public class Time {

    public static void main(String[] args) throws Exception {
        log.info("测试开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        test1();
        log.info("测试结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * 耗时计算
     */
    public static void test1() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(5000);
        stopWatch.stop();
        log.info("耗时计算：{}", (double) stopWatch.getTime()/1000 + "s");
        stopWatch.reset();
        stopWatch.start();
        Thread.sleep(3000);
        stopWatch.stop();
        log.info("耗时计算：{}", (double) stopWatch.getTime()/1000 + "s");
    }

}
