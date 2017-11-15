package com.bestwaiting.netty.jdknio.server;

import com.bestwaiting.netty.NIOConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bestwaiting on 17/9/5.
 */
@Slf4j
public class NIOServerHandle implements Runnable {
    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean flag;

    public NIOServerHandle(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server is starting...port is" + port);
        } catch (Exception e) {
            log.error("server start error...", e.getMessage());
            System.exit(1);
        }
    }

    public void stop() {
        this.flag = true;
    }

    @Override
    public void run() {
        while (!flag) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleIO(key);
                    } catch (Exception ex) {
                        log.error("handleIO error");
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                log.error("select error");
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                log.error("selector close error");
            }
        }
    }

    private void handleIO(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isValid()) {
            // 处理接入的新请求
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            if (selectionKey.isReadable()) {
                SocketChannel sc = (SocketChannel) selectionKey.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("servet receive the order is " + body);
                    String currentTime = NIOConstants.REQEST_STR.equals(body) ? new Date().toString() : "req error";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    selectionKey.cancel();
                    sc.close();
                } else {
                    // 0字节，忽略处理
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String content) throws Exception {
        if (StringUtils.isEmpty(content))
            return;
        byte[] rep = content.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(rep.length);
        writeBuffer.put(rep);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("send response time succeed..." + content);
        }
    }
}
