package com.gb.cs.client;

import com.gb.cs.client.handlers.InboundClientHandler;
import com.gb.cs.common.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyNetwork<Message> implements Network<Message> {
    private Controller ctrl;
    private Bootstrap bootstrap;
    private String server;
    private int port;
    private EventLoopGroup workELG;
    private SocketChannel socket;


    public NettyNetwork(String server, int port,  Controller ctrl) {
        this.ctrl = ctrl;
        this.server = server;
        this.port = port;
        bootstrap = new Bootstrap();
        workELG = new NioEventLoopGroup(1);
        connect();

    }


    private void connect() {
        try {
            configureBootstrap();
            ChannelFuture future = bootstrap.connect(server, port).sync();
            Thread t = new Thread(()->{
                try {
                    System.out.println("Daemon thread started");
                    future.channel().closeFuture().sync();
                    System.out.println("Thread STOPED");
                } catch (Exception exception){
                    exception.printStackTrace();
                } finally {
                    workELG.shutdownGracefully();
                }

            });
            t.setDaemon(true);
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message msg) {
        socket.writeAndFlush(msg);
    }

    private void configureBootstrap() {
        bootstrap.channel(NioSocketChannel.class)
                .group(workELG)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socket = socketChannel;
                        socketChannel.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new InboundClientHandler(ctrl)
                        );
                    }
                });
    }
}
