package com.bilibili.command;


import com.bilibili.service.Receiver;

public class LoginCommand extends Command {

    public LoginCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exectue() {
        receiver.login();
    }

}
