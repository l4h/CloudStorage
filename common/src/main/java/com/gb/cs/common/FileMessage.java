package com.gb.cs.common;

import java.util.List;

public class FileMessage  implements Message {
    private CommandType command;
    private List<String> elements;
    private byte[] file;
    private long messageNo;
    private long size;

    public FileMessage(CommandType command, String currentDir) {
        this.command = command;
    }

    public FileMessage(CommandType command) {
        this.command = command;
    }

    public FileMessage(List<String> elements, long size, byte[] data, long messageNo) {
        command = CommandType.WRITE_FILE;
        this.elements = elements;
        this.size = size;
        this.file = data;
        this.messageNo = messageNo;
    }

    public FileMessage fillItems(List<String> elements) {
        this.elements = elements;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    public byte[] readData(){
        return file;
    }

    public long getSize() {
        return size;
    }

    @Override
    public CommandType getCommand() {
        return command;
    }
}
