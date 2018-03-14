package com.bilibili.servlet.info;


import com.bilibili.Video.Video;
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

@WebServlet("/info/getVideoById")
public class GetVideoById extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        int videoId = Integer.parseInt(req.getParameter("videoId"));
        Video video = VideoDao.getVideoById(videoId);
        JSONObject job = null;
        if(video != null) {
            job = JSONObject.fromObject(video);
            job.put("result", "success");
        }
        else if (video == null) {
            job = new JSONObject();
            job.put("result", "error");
        }
        JsonUtil.writeResponse(resp, job.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
