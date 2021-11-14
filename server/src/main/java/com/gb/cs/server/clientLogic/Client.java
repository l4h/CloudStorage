package com.gb.cs.server.clientLogic;

import io.netty.channel.socket.SocketChannel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Client {

    final private SocketChannel socket;
    final private String login;
    final private Path homeDir;
    private Path currentDir;

    public Client(SocketChannel socket, String login) {
        this.socket = socket;
        this.login = login;
        this.homeDir = Paths.get(login);
        currentDir = homeDir;
    }

    public Path getCurrentDir() {
        return currentDir;
    }

    public Path getHomeDir() {
        return homeDir;
    }

    public Path getAbsPAth(){
        return (currentDir.equals(homeDir))? homeDir : Paths.get(homeDir.toString(),currentDir.toString());
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public void changeDir(String toDir) {
        currentDir = Paths.get(toDir);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(login, client.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
