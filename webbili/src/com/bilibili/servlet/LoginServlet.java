package com.bilibili.servlet;

import com.bilibili.command.Command;
import com.bilibili.command.LoginCommand;
import com.bilibili.util.JsonUtil;
import com.bilibili.service.Receiver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

/**
 *  Json参数:
    username:   用户名
    password:   密码
    email:      邮箱
    code:       小写的验证码
 */

@WebServlet("/sign/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("用户登入请求 -- ");
        response.setCharacterEncoding(UTF8);
        request.setCharacterEncoding(UTF8);
        String jsonStr = JsonUtil.getJsonStr(request.getInputStream());
        Receiver receiver = new Receiver(jsonStr, request);
        Command command = new LoginCommand(receiver);
        command.exectue();

        JsonUtil.writeResponse(response, command.getResponseJson());
    }
}
