package com.bestwaiting.netty.nettynio.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by bestwaiting on 17/9/8.
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        System.out.println("encoder is " + msg);
        // 序列化
        byte[] raw = messagePack.write(msg);
        out.writeBytes(raw);
    }
}
