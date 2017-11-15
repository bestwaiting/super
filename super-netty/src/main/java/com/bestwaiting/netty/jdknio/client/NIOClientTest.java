package com.bestwaiting.netty.jdknio.client;


import com.bestwaiting.netty.NIOConstants;

/**
 * Created by bestwaiting on 17/9/5.
 */
public class NIOClientTest {

    public static void main(String[] args) {
        int port = NIOConstants.PORT;
        new Thread(new NIOClientHandle("", port), "client-001").start();
    }
}
