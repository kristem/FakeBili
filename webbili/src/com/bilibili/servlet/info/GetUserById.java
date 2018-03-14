package com.bilibili.servlet.info;

import com.bilibili.dao.UserDao;
import com.bilibili.user.User;
import com.bilibili.util.JsonUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/info/getUserById")
public class GetUserById extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        int userId = Integer.parseInt(req.getParameter("userId"));
        User user = UserDao.getUserById(userId);
        JSONObject jsonObject = null;
        if(user != null) {
            jsonObject = JSONObject.fromObject(user);
            jsonObject.put("result", "success");
        }
        else {
            jsonObject = new JSONObject();
            jsonObject.put("result", "error");
        }
        JsonUtil.writeResponse(resp, jsonObject.toString());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
