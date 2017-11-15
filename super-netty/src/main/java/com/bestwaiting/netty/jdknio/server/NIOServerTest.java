package com.bestwaiting.netty.jdknio.server;

import com.bestwaiting.netty.NIOConstants;

/**
 * Created by bestwaiting on 17/9/5.
 */
public class NIOServerTest {

    public static void main(String[] args) {
        int port = NIOConstants.PORT;
        new Thread(new NIOServerHandle(port), "server-001").start();
    }
}
