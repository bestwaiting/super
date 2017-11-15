package com.bestwaiting.netty.jdkaio.server;

import com.bestwaiting.netty.NIOConstants;

/**
 * Created by bestwaiting on 17/9/5.
 */
public class AIOServerTest {
    public static void main(String[] args) {
        int port = NIOConstants.PORT;
        new Thread(new AIOServerHandle(port), "server-001").start();

    }
}
