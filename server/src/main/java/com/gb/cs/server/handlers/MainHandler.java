package com.gb.cs.server.handlers;

import com.gb.cs.server.ServerApp;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.beans.binding.StringBinding;

import java.nio.charset.StandardCharsets;

public class MainHandler extends ChannelInboundHandlerAdapter {
    private ServerApp serverApp;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Новое подключение!");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        System.out.println(buf.toString());
        ByteBuf outBuf = ctx.alloc().buffer();
        outBuf.writeBytes(buf.toString().getBytes(StandardCharsets.UTF_8));
//        outBuf.retain();
        ctx.writeAndFlush(outBuf);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    public MainHandler(ServerApp serverApp) {
        this.serverApp=serverApp;
    }
}
