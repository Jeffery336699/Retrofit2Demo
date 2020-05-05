package com.example.myapplication;

import com.example.myapplication.zeren.AbstractLogger;
import com.example.myapplication.zeren.ChainPatternDemo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {



        testZeren();

        assertEquals(4, 2 + 2);
    }

    public void testUnion(){
        System.out.println("laile ");
        int flag=0;
        flag|=2;
        System.out.println(flag);

        int flag1=0;
        flag1|=1;
        System.out.println(flag1);
    }


    public void testZeren(){
        ChainPatternDemo chainPatternDemo = new ChainPatternDemo();
        AbstractLogger loggerChain = chainPatternDemo.getChainOfLoggers();

//        loggerChain.logMessage(AbstractLogger.INFO, "This is an information.");

//        loggerChain.logMessage(AbstractLogger.DEBUG,"This is a debug level information.");

        loggerChain.logMessage(AbstractLogger.ERROR,"This is an error information.");
    }
}