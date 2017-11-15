package com.bestwaiting.netty.jdknio.client;

import com.bestwaiting.netty.NIOConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bestwaiting on 17/9/5.
 */
@Slf4j
public class NIOClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean flag = false;

    public NIOClientHandle(String host, int port) {
        this.host = host == "" ? "127.0.0.01" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            log.error("init error...", e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            log.error("connect error...", e.getMessage());
            System.exit(1);
        }

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
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("select error...", e.getMessage());
                System.exit(1);
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                log.error("selector close error...", e.getMessage());
            }
        }
    }

    private void handleIO(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isValid()) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();
            if (selectionKey.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    // 连接失败
                    log.error("connect fail...");
                    System.exit(1);
                }
            }
            if (selectionKey.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Now is " + body);
                    this.flag = true;
                } else if (readBytes < 0) {
                    selectionKey.cancel();
                    sc.close();
                } else {
                    // 读到0字节，忽略
                    System.out.println("read bytes size is 0");
                }
            }
        }
    }

    /**
     * 尝试连接服务器
     * 1.连接成功后注册监控读通道；发送请求
     * 2.连接未果，注册监控ACK消息
     *
     * @throws Exception
     */
    private void doConnect() throws Exception {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

    }

    private void doWrite(SocketChannel socketChannel) throws Exception {
        byte[] req = NIOConstants.REQEST_STR.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("send request time succeed...");
        }
    }
}
