package com.bilibili.servlet.info;

import com.bilibili.user.User;
import com.bilibili.dao.UserDao;
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

@WebServlet("/info/getExtraInfo")
public class GetExtraInfo extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String jwt = req.getHeader("jwt");
        String username = req.getParameter("username");
        JSONObject job = new JSONObject();
        if(!SecretUtil.checkJwt(jwt)) {
            job.put("result", "signError");
            JsonUtil.writeResponse(resp, job.toString());
            return;
        }
        User user = UserDao.getExtraInfo(username);
        job.put("sign", user.getUserSign());
        job.put("signInDate", user.getSignInDate());
        job.put("email", user.getEmail());
        job.put("isAdmin", user.isAdmin());
        job.put("photoUrl", user.getPhotoUrl());
        JsonUtil.writeResponse(resp, job.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
