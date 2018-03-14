package com.bilibili.servlet;

import com.bilibili.command.Command;
import com.bilibili.command.RefreshTokenCommand;
import com.bilibili.util.JsonUtil;
import com.bilibili.service.Receiver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

/**
 *   Json参数:
     jwt:   jwt字符串
 */

@WebServlet("/sign/isLogin")
public class RefreshTokenServlet extends HttpServlet {
    public static final String JWT="jwt";
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF8);
        response.setCharacterEncoding(UTF8);
        String tokenStr = request.getHeader(JWT);
        Receiver receiver = new Receiver();
        Command command = new RefreshTokenCommand(receiver, tokenStr);
        command.exectue();

        JsonUtil.writeResponse(response, command.getResponseJson());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
