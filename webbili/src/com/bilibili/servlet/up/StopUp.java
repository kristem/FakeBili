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
import static com.bilibili.servlet.HasUserServlet.RESULT;

@WebServlet("/up/stopUp")
public class StopUp extends HttpServlet {
    public static final String SUCCESS = "success";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String key = req.getHeader("jwt") + req.getParameter("filename");
        VideoUpProgress.put(key + "Del","true");
        JSONObject json = new JSONObject();
        json.put(RESULT, SUCCESS);
        json.put("id", VideoUpProgress.get(key +"Id"));
        JsonUtil.writeResponse(resp, json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
