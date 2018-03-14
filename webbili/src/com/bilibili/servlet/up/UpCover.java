package com.bilibili.servlet.up;

import com.bilibili.command.Command;
import com.bilibili.command.UpCoverCommand;
import com.bilibili.command.UpVideoCommand;
import com.bilibili.service.Receiver;
import com.bilibili.token.Token;
import com.bilibili.util.JsonUtil;
import com.bilibili.util.SecretUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/up/upCover")
public class UpCover extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        JSONObject job = new JSONObject();
        String jwt = req.getHeader("jwt");
        if(!SecretUtil.checkJwt(jwt)){
            job.put("result", "signError");
            JsonUtil.writeResponse(resp, job.toString());
            return;
        }
        String path = this.getClass().getClassLoader().getResource("../../").getPath();
        Receiver receiver = new Receiver(req, path);
        Command command = new UpCoverCommand(receiver);
        command.exectue();

        JsonUtil.writeResponse(resp, receiver.getResponse());
    }
}
