package com.gb.cs.client.handlers;

import com.gb.cs.client.Controller;
import com.gb.cs.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InboundClientHandler extends SimpleChannelInboundHandler<Message> {

    private Controller ctrl;

    public InboundClientHandler(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("Client connected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        switch (msg.getCommand()) {
            case AUTH:
                AuthMess auth = (AuthMess) msg;
                if (auth.getAuthStatus().equals(AuthStatus.LOGIN_OK)) {
                    System.out.println("Login_OK");
                    ctrl.setLogin(auth.getLogin());
                }
                break;
            case LIST_FILES:
                FileMessage list = (FileMessage) msg;
                ctrl.fillPane(list.getElements(),true);
                break;
            case WRITE_FILE:
                FileMessage filemsg = (FileMessage) msg;
                ctrl.writeFile(filemsg.readData(),filemsg.getElements().get(0));
                break;

            default:
                System.out.println("Default: " + msg.getCommand() + msg.toString());
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Exce[tion");
        super.exceptionCaught(ctx, cause);
    }


}
