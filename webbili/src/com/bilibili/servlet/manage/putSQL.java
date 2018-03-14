package com.bilibili.servlet.manage;

import com.bilibili.dao.UserDao;
import com.bilibili.util.JsonUtil;
import com.bilibili.util.SecretUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/manage/putSQL")
public class putSQL extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String jwt = req.getHeader("jwt");
        JSONObject jsonObject = new JSONObject();
        if(!SecretUtil.isAdmin(jwt)) {
            jsonObject.put("result", "permissionError");
            JsonUtil.writeResponse(resp, jsonObject.toString());
            return;
        }
        String sql = req.getParameter("sql");
        if(UserDao.putSQL(sql)) {
            jsonObject.put("result", "success");
        }
        else {
            jsonObject.put("result", "sqlError");
        }
        JsonUtil.writeResponse(resp, jsonObject.toString());
    }
}
