package com.example.myapplication;

import java.util.concurrent.Callable;

/**
 * Callable接口实例 计算累加值大小并返回
 */
public class CallableDemo implements Callable<Integer> {
    private int sum;
    @Override
    public Integer call() throws Exception {
        System.out.println("Callable子线程开始计算啦！===="+Thread.currentThread().getName());
        Thread.sleep(3000);

        for(int i=0 ;i<5000;i++){
            sum=sum+i;
        }
        System.out.println("Callable子线程计算结束！===="+Thread.currentThread().getName());
        return sum;
    }
}