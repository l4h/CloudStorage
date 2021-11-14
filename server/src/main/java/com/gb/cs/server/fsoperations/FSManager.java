package com.gb.cs.server.fsoperations;

import com.gb.cs.common.FileMessage;
import com.gb.cs.server.clientLogic.Client;

import java.io.IOException;
import java.util.List;

public interface FSManager<T extends Client> {

    FileMessage listFile(T client) throws IOException;

    FileMessage getFile(T client, List<String> files) throws IOException;

    boolean isDir(T client, String request);

    void createHome(Client client);

    void chDir(T Client, String dir);

}
