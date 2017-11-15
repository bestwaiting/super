package com.bestwaiting.netty.nettynio.client;

import com.bestwaiting.netty.NIOConstants;
import com.bestwaiting.netty.nettynio.codec.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by bestwaiting on 17/9/6.
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
    private int counter;
    byte[] req;

    public NettyClientHandler() {
        req = NIOConstants.REQEST_STR.getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf firstMessage;
//        for (int i = 0; i < 100; i++) {
//            firstMessage = Unpooled.buffer(req.length);
//            firstMessage.writeBytes(req);
//            ctx.writeAndFlush(firstMessage);
//        }
        for (int i = 0; i < 100; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("name" + i);
            userInfo.setAge(i);
            ctx.writeAndFlush(userInfo);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] resp = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(resp);
//        String body = new String(resp, "UTF-8");
//        System.out.println("Now is " + body + "; the counter is " + ++counter);
        System.out.println("client receive msg is " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
