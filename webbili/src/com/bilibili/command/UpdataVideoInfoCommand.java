package com.bilibili.command;


import com.bilibili.service.Receiver;

public class UpdataVideoInfoCommand extends Command {

    public UpdataVideoInfoCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.VideoInfoUpdata();
    }

}
