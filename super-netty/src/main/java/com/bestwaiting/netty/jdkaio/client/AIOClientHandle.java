package com.bestwaiting.netty.jdkaio.client;

import com.bestwaiting.netty.NIOConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bestwaiting on 17/9/5.
 */
@Slf4j
public class AIOClientHandle implements Runnable, CompletionHandler<Void, AIOClientHandle> {
    private String host;
    private int port;
    private AsynchronousSocketChannel client;
    private CountDownLatch countDownLatch;

    public AIOClientHandle(String host, int port) {
        this.host = host == "" ? "127.0.0.01" : host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception e) {
            log.error("init error...", e.getMessage());
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AIOClientHandle attachment) {
        doWrite(NIOConstants.REQEST_STR);
    }

    private void doWrite(String content) {
        if (StringUtils.isEmpty(content))
            return;
        byte[] writeBytes = content.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
        writeBuffer.put(writeBytes);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    client.write(buffer, buffer, this);
                } else {
                    doRead();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("write fail...", e.getMessage());
                }
            }
        });
    }

    private void doRead() {
        ByteBuffer readBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
        client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                String body;
                try {
                    body = new String(bytes, "UTF-8");
                    System.out.println("Now is " + body);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    log.error("read error...", e.getMessage());
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer byteBuffer) {
                try {
                    client.close();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    log.error("read fail...", e.getMessage());
                }

            }
        });
    }

    @Override
    public void failed(Throwable exc, AIOClientHandle attachment) {
        exc.printStackTrace();
        try {
            client.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            log.error("connect fail...", e.getMessage());
        }
    }
}
