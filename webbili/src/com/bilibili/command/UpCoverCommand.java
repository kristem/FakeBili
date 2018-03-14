package com.bilibili.command;


import com.bilibili.service.Receiver;

public class UpCoverCommand extends Command {

    public UpCoverCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.upCover();
    }

}
