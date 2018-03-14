package com.bilibili.servlet.info;

import com.bilibili.Video.VideoUpProgress;
import com.bilibili.dao.UserDao;
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

@WebServlet("/info/updateBaseInfo")
public class UpdateBaseInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String email = req.getParameter("email");
        String sign = req.getParameter("sign");
        String jwt = req.getHeader("jwt");
        JSONObject job = new JSONObject();
        Token token = new Token(jwt);
        if(!SecretUtil.checkJwt(jwt)){
            job.put("result", "signError");
            JsonUtil.writeResponse(resp, job.toString());
            return;
        }
        String filename = req.getParameter("filename");
        VideoUpProgress.remove(jwt + filename + "Id");
        UserDao.updateBaseInfo(email, sign, token.getPlayloadMap().get("username"));
        job.put("result", "success");
        JsonUtil.writeResponse(resp, job.toString());
    }
}
