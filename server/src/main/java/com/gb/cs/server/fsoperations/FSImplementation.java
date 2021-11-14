package com.gb.cs.server.fsoperations;

import com.gb.cs.common.CommandType;
import com.gb.cs.common.FileMessage;
import com.gb.cs.server.clientLogic.Client;
import io.netty.channel.socket.SocketChannel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FSImplementation implements FSManager<Client> {
    private Path clientsRoot;

    public FSImplementation(Path clientsRoot) {
        this.clientsRoot = clientsRoot;
        if (!Files.exists(clientsRoot)) {
            try {
                Files.createDirectory(clientsRoot);
            } catch (IOException e) {
                throw new RuntimeException("Не удается создать ROOT директорию");
            }
        }
    }

    @Override
    public void createHome(Client cl) {
        Path homeDir = Paths.get(clientsRoot.toString(), cl.getHomeDir().toString());
        if (Files.notExists(homeDir)) {
            try {
                Files.createDirectory(homeDir);
            } catch (IOException e) {
                throw new RuntimeException("Не удается создать домашнюю директорию для клиента: " + cl.getHomeDir());
            }
        }
    }

    @Override
    public FileMessage listFile(Client client) throws IOException {
        Path homeDir = Paths.get(clientsRoot.toString(), client.getHomeDir().toString()).normalize(); //хомяк нужен для контроля, чтобы не пустить клиента на выше его домашней директории
        Path curDir = absPath(client,null);
        System.out.println("LIST FILES cur dir: " + curDir);
        List<String> filesList;

        filesList = Files.list(curDir)
                .map((e) -> e.getFileName().toString())
                .collect(Collectors.toList());
        if ( !curDir.equals(homeDir)) {
            System.out.println("not EQ");
            filesList.add(0, "..");
        }

        return  new FileMessage(CommandType.LIST_FILES).fillItems(filesList);
    }


    @Override
    public FileMessage getFile(Client client, List<String> files) throws IOException {
        FileMessage fileMessage = null;
        for (String file : files) {
            Path filePath = Paths.get(absPath(client, file).toString());
            long size = Files.size(filePath);
            if (size < 1500){
                fileMessage= new FileMessage(Arrays.asList(file), size, Files.readAllBytes(filePath),0);
            } else {
                System.out.println("not realised yet");
                return fileMessage;
            }
        }
        return fileMessage;
    }

    public boolean isDir(Client client, String request) {
        if ( request.equals("..")) return true;

        Path req = absPath(client,request);
        System.out.println("FS IMPL isDir :" +req);
        return Files.isDirectory(req);
    }

    public void chDir(Client cl, String d) {
        System.out.println("FS IMPL  chDir:" + d);
        if ( d.equals("..")){
            Path relative = absPath(cl,d).normalize();
            cl.changeDir(clientsRoot.relativize(relative).toString());
        } else {
            cl.changeDir(d);
        }
        System.out.println("FS IMPL chDir() :" +cl.getAbsPAth());
    }

    private Path absPath(Client cl, String f){
        Path p = (f==null)? Paths.get(clientsRoot.toString(),cl.getAbsPAth().toString()) : Paths.get(clientsRoot.toString(),cl.getAbsPAth().toString(),f);
        return p;
    }

}
