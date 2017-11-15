package com.bestwaiting.dubbo.consumer;

import com.bestwaiting.dubbo.api.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by bestwaiting on 17/11/14.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring-dubbo.xml"});
        context.start();

        TestService testService = (TestService) context.getBean("testService");
        String hello = testService.dubboTest("bestwaiting");
        System.out.println(hello);
        System.in.read();
    }
}
