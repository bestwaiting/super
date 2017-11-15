package com.bestwaiting.netty.nettynio.server;

import com.bestwaiting.netty.NIOConstants;
import com.bestwaiting.netty.nettynio.codec.MessagePackDecoder;
import com.bestwaiting.netty.nettynio.codec.MessagePackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by bestwaiting on 17/9/6.
 */
public class NettyServerTest {

    public void bind(int port) throws Exception {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, NIOConstants.BUFFER_SIZE)
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = NIOConstants.PORT;
        new NettyServerTest().bind(port);
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast("frameDecoder",
                    new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
            socketChannel.pipeline().addLast("messagepack decoder", new MessagePackDecoder());

            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
            socketChannel.pipeline().addLast("messagepack encoder", new MessagePackEncoder());

            socketChannel.pipeline().addLast(new NettyServerHandler());
        }
    }
}
