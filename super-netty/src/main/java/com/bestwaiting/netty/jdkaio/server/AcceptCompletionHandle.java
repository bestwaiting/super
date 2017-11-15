package com.bestwaiting.netty.jdkaio.server;

import com.bestwaiting.netty.NIOConstants;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by bestwaiting on 17/9/5.
 */
@Slf4j
public class AcceptCompletionHandle implements CompletionHandler<AsynchronousSocketChannel, AIOServerHandle> {
    @Override
    public void completed(AsynchronousSocketChannel result, AIOServerHandle attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(NIOConstants.BUFFER_SIZE);
        result.read(byteBuffer, byteBuffer, new ReadCompletionHandle(result));
    }


    @Override
    public void failed(Throwable exc, AIOServerHandle attachment) {
        log.error("accept error...", exc.getMessage());
        attachment.countDownLatch.countDown();
    }
}
