package com.bilibili.command;


import com.bilibili.service.Receiver;

public class RegisterCommand extends Command {

    public RegisterCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.Register();
    }

}
