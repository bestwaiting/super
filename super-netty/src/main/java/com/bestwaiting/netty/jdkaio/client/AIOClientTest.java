package com.bestwaiting.netty.jdkaio.client;

import com.bestwaiting.netty.NIOConstants;

/**
 * Created by bestwaiting on 17/9/5.
 */
public class AIOClientTest {
    public static void main(String[] args) {
        int port = NIOConstants.PORT;
        new Thread(new AIOClientHandle("", port), "client-001").start();

    }
}
