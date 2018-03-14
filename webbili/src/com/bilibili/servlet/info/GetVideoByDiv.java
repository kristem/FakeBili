package com.bilibili.servlet.info;

import com.bilibili.dao.VideoDao;
import com.bilibili.util.JsonUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/info/getVideoByDiv")
public class GetVideoByDiv extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String div1 = req.getParameter("div1");
        String div2 = req.getParameter("div2");
        JSONObject jsonObject = VideoDao.getVideoByDiv(div1, div2);
        if(jsonObject != null) {
            jsonObject.put("result", "success");
        }
        else {
            jsonObject.put("result", "error");
        }
        JsonUtil.writeResponse(resp, jsonObject.toString());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
