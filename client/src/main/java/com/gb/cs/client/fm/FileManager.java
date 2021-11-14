package com.gb.cs.client.fm;

import com.gb.cs.client.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileManager {
    Controller ctrl;
    private String clientsRoot;
    private String curDir;

    public FileManager(String clientsRoot, Controller ctrl) {
        this.clientsRoot = clientsRoot;
        this.ctrl = ctrl;
        initialize(clientsRoot);

    }

    private void initialize(String dir) {
        Path home = Paths.get(dir);
        try {
            if (Files.notExists(home)) {
                chDir(Files.createDirectory(home).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * имя параметра обманывает и сюда передается не логин, а clientsRoot + login
     */
    public void createClientHome(String login){
        initialize(login);
    }

    public void writeFile(byte[] file,String name) {
        try {
            Path elt = Paths.get(curDir, name);
            File fsfile = elt.toFile();
            FileOutputStream fos = new FileOutputStream(fsfile);
            fos.write(file, 0, file.length);
            fos.close();
            chDir(null);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void chDir(String dir) {

        Path pDir =(dir ==null)? Paths.get(curDir):Paths.get(dir);
        try {
            if (Files.isReadable(pDir)) {
                curDir=pDir.toString();
                ctrl.fillPane(Files.list(pDir)
                        .map((e) -> e.getFileName().toString())
                        .collect(Collectors.toList())
                        ,false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
