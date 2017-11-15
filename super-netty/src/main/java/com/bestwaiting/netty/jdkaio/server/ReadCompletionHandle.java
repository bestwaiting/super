package com.bestwaiting.netty.jdkaio.server;

import com.bestwaiting.netty.NIOConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * Created by bestwaiting on 17/9/5.
 */
@Slf4j
public class ReadCompletionHandle implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandle(AsynchronousSocketChannel channel) {
        if (this.channel == null) {
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String reqStr = new String(body, "UTF-8");
            System.out.println("server receive the request:" + reqStr);
            String resp = NIOConstants.REQEST_STR.equals(reqStr) ? new Date().toString() : NIOConstants.RESPONSE_ERROR;
            doWrite(resp);
        } catch (Exception e) {
            log.error("read error...", e.getMessage());
        }
    }

    private void doWrite(String content) {
        if (StringUtils.isEmpty(content))
            return;
        byte[] writeBytes = content.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
        writeBuffer.put(writeBytes);
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    channel.write(buffer, buffer, this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    log.error("write error...", e.getMessage());
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            log.error("read failed...", e.getMessage());
        }
    }
}
