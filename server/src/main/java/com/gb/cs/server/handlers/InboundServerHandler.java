package com.gb.cs.server.handlers;

import com.gb.cs.common.*;
import com.gb.cs.server.ServerApp;
import com.gb.cs.server.clientLogic.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

public class InboundServerHandler extends SimpleChannelInboundHandler<Message> {
    private ServerApp server;

    public InboundServerHandler(ServerApp server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChannelActive");
        ctx.writeAndFlush(CommandType.AUTH);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        Message message = msg;
        switch (msg.getCommand()) {
            case AUTH:
                AuthMess auth = (AuthMess) message;
                System.out.println("Auth");
                if (server.getAuth().loginSuccess(auth.getCred())) {
                    Client cl = new Client((SocketChannel) ctx.channel(), "cloudman1");
                    server.addClient((SocketChannel) ctx.channel(), cl);
                    server.getFsManager().createHome(cl);
                    ctx.writeAndFlush(auth.setAuthOk(AuthStatus.LOGIN_OK));
                    ctx.writeAndFlush(server.getFsManager().listFile(cl));
                } else
                    ctx.writeAndFlush(auth.setAuthOk(AuthStatus.LOGIN_FIELD));
                break;
            case LIST_FILES:
                FileMessage fileList = (FileMessage) message;

                //server.getClient()

            case READ_FILE:
                System.out.println("READ FILE");
                FileMessage fMsg = (FileMessage) message;
                List<String> elements = fMsg.getElements();
                Client cl = server.getClient((SocketChannel) ctx.channel());

                if (elements.size() > 0) {
                    if (server.getFsManager().isDir(cl, elements.get(0))) {
                        server.getFsManager().chDir(cl, elements.get(0));
                        ctx.writeAndFlush(server.getFsManager().listFile(cl));
                    } else {
                        ctx.writeAndFlush(server.getFsManager().getFile(cl,elements));
                    }
                }

                break;
            default:
                System.out.println("InboundServerHandler switch default");
                break;
        }
    }


}
