package com.bilibili.command;


import com.bilibili.service.Receiver;

public abstract class Command {
    protected Receiver receiver = null;

    public abstract void exectue();

    public String getResponseJson() {
        return receiver.getResponse();
    }
}
