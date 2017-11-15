package com.bestwaiting.netty.nettynio.client;

import com.bestwaiting.netty.NIOConstants;
import com.bestwaiting.netty.nettynio.codec.MessagePackDecoder;
import com.bestwaiting.netty.nettynio.codec.MessagePackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by bestwaiting on 17/9/6.
 */
public class NettyClientTest {
    public void connect(String host, int port) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            socketChannel.pipeline().addLast("messagepack decoder", new MessagePackDecoder());
//
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("messagepack encoder", new MessagePackEncoder());

                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 等待客户端链路关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = NIOConstants.PORT;
        new NettyClientTest().connect("127.0.0.1", port);
    }
}
