package com.bilibili.servlet.manage;

import com.bilibili.dao.VideoDao;
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

/**
 *  参数: jwt, videoId
 */

@WebServlet("/manage/deleteVideo")
public class DeleteVideo extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String id = req.getParameter("videoId");
        String jwt = req.getHeader("jwt");
        String username = new Token(jwt).getPlayloadMap().get("username");
        JSONObject job = new JSONObject();
        if(SecretUtil.checkJwt(jwt) && SecretUtil.videoToUser(username, id)) {
            if(VideoDao.deleteVideo(id)) {
                job.put("result", "success");
            }
            else {
                job.put("result", "error");
            }
        }
        JsonUtil.writeResponse(resp, job.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
