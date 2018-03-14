package com.bilibili.servlet.up;

import com.bilibili.Video.VideoUpProgress;
import com.bilibili.util.JsonUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/up/getProgress")
public class GetUpProgress extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String key = req.getHeader("jwt") + req.getParameter("filename");
        Object size = VideoUpProgress.get(key + "Size");
        size = size == null ? 100 : size;
        Object progress = VideoUpProgress.get(key + "Progress");
        progress = progress == null ? 0 : progress;
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("progress", progress);
        JsonUtil.writeResponse(resp, json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
