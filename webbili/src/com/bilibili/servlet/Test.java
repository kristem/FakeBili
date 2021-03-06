package com.bilibili.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.bilibili.dao.UserDao.UTF8;

public class Test extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        req.getInputStream(),UTF8
                )
        );
        String str = null;
        while((str = reader.readLine()) != null){
            System.out.println(str);
        }
    }
}
