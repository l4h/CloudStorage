package com.gb.cs.server;

import com.gb.cs.server.clientLogic.Client;
import com.gb.cs.server.fsoperations.FSImplementation;
import com.gb.cs.server.fsoperations.FSManager;
import com.gb.cs.server.handlers.InboundServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ServerApp {
    private Auth auth;
    private FSManager fsManager;
    private Path clientsRoot;
    private ServerApp serverApp;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Map<SocketChannel, Client> clients;
    public ServerApp(int port) {
        clientsRoot = Paths.get("E:\\ClientsRoot");
        fsManager = new FSImplementation(clientsRoot);
        serverApp = this;
        auth = new SimpleAuthService();
        clients = new HashMap<>();
        //менеджеры потоков
        bossGroup = new NioEventLoopGroup(1); //ассептер
        workGroup = new NioEventLoopGroup();          //для обработки сетевого взаимодействия

        startServer(port);
    }

    public static void main(String[] args) {
        new ServerApp(8189);
    }

    public FSManager getFsManager() {
        return fsManager;
    }

    private void startServer(int port) {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("SERVER CHANNEL HANDLER:" + socketChannel);
                            socketChannel.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new InboundServerHandler(serverApp));
                        }
                    });
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public void addClient(SocketChannel socket,Client cl) {
        clients.put(socket, cl);
    }

    public Client getClient(SocketChannel channel) {
        return clients.get(channel);
    }

    public int countClients() {
        return clients.size();
    }

    public Path getClientsRoot() {
        return clientsRoot;
    }
}
