package com.bilibili.servlet.up;

import com.bilibili.command.Command;
import com.bilibili.command.UpdataVideoInfoCommand;
import com.bilibili.dao.VideoDao;
import com.bilibili.service.Receiver;
import com.bilibili.util.JsonUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/up/upVideoInfo")
public class UpdataInfo extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        String jsonStr = JsonUtil.getJsonStr(req.getInputStream());

        //检测是否有封面
        JSONObject job = JSONObject.fromObject(jsonStr);
        String videoId = job.getString("videoId");
        if(!VideoDao.hasPic(videoId)) {
            job = new JSONObject();
            job.put("result", "imgError");
            JsonUtil.writeResponse(resp, job.toString());
            return;
        }
        Receiver receiver = new Receiver(jsonStr);
        Command command = new UpdataVideoInfoCommand(receiver);
        command.exectue();

        JsonUtil.writeResponse(resp, receiver.getResponse());
    }
}
