package com.bilibili.command;


import com.bilibili.service.Receiver;

public class RefreshTokenCommand extends Command {
    private String jwt;

    public RefreshTokenCommand(Receiver receiver, String jwt) {
        this.receiver = receiver;
        this.jwt = jwt;
    }

    @Override
    public void exectue() {
        receiver.RefreshToken(jwt);
    }

}
