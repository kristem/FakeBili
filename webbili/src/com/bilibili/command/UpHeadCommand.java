package com.bilibili.command;

import com.bilibili.service.Receiver;

public class UpHeadCommand extends Command{
    private String username;
    private String id;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UpHeadCommand(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.upHeadImg(id, username);
    }
}
