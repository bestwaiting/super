package com.bestwaiting.netty.nettynio.server;

import com.bestwaiting.netty.nettynio.codec.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by bestwaiting on 17/9/6.
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] req = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(req);
//        String body = new String(req, "UTF-8");
//        System.out.println("server receive is " + body + "; the counter is " + ++counter);
//        String resp = NIOConstants.REQEST_STR.equals(body) ? new Date().toString() : NIOConstants.RESPONSE_ERROR;
//        ByteBuf respBuf = Unpooled.copiedBuffer(resp.getBytes());
//        ctx.write(respBuf);
        List<UserInfo> userInfos = (List<UserInfo>) msg;
        System.out.println("server receive is " + userInfos);
        ctx.writeAndFlush(userInfos);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将消息发送队列中的消息写入到socketChannel中发送给对方
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
