package com.bilibili.servlet.info;

import com.bilibili.dao.VideoDao;
import com.bilibili.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bilibili.dao.UserDao.UTF8;

@WebServlet("/info/getDivCnt")
public class GetDivCnt extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(UTF8);
        req.setCharacterEncoding(UTF8);
        JsonUtil.writeResponse(resp,VideoDao.getDivCnt());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
