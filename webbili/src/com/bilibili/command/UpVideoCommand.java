package com.bilibili.command;

import com.bilibili.service.Receiver;

public class UpVideoCommand extends Command{
    public UpVideoCommand(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.upVideo();
    }
}
