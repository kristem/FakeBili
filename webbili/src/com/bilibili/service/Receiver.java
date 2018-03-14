package com.bilibili.service;

import com.bilibili.Video.Video;
import com.bilibili.Video.VideoUpProgress;
import com.bilibili.dao.UserDao;
import com.bilibili.dao.VideoDao;
import com.bilibili.token.Token;
import com.bilibili.util.ImgUtil;
import com.bilibili.util.LoginUtil;
import com.bilibili.util.SecretUtil;
import com.bilibili.util.VideoUtil;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

import static com.bilibili.Video.Video.*;
import static com.bilibili.dao.UserDao.*;
import static com.bilibili.servlet.RefreshTokenServlet.JWT;
import static com.bilibili.servlet.HasUserServlet.*;

public class Receiver {
    private JSONObject requestJson;
    private int flag;
    private JSONObject responseJson;
    private Token token;
    private HttpServletRequest req;
    private String path;

    public Receiver() {
        this.requestJson = null;
        responseJson = new JSONObject();
    }

    public Receiver(String jsonStr, HttpServletRequest req) {
        this.requestJson = JSONObject.fromObject(jsonStr);
        this.req = req;
        responseJson = new JSONObject();
    }

    public Receiver(String jsonStr) {
        this.requestJson = JSONObject.fromObject(jsonStr);
        responseJson = new JSONObject();
    }

    public Receiver(String jsonStr, String path) {
        this.path = path;
        this.requestJson = JSONObject.fromObject(jsonStr);
        responseJson = new JSONObject();
    }

    public Receiver(HttpServletRequest req) {
        this.req = req;
        responseJson = new JSONObject();
    }

    public Receiver(HttpServletRequest req, String path) {
        this.req = req;
        this.path = path;
        responseJson = new JSONObject();
    }

    public String getResponse() {
        return responseJson.toString();
    }
    public static final String FORMAT_ERROR = "formatError";

    private void errorString() {
            responseJson.put(RESULT, FORMAT_ERROR);
    }

    public static final String AUTHOR = "author";
    public final static int TOKEN_OVERTIME_SECOND = 604800;
    public static final String SUCCESS = "success";
    public static final String DELETE = "delete";
    public static final String PASSWORD_ERROR = "passwordError";
    public static final String REQUEST_ERROR = "requestError";
    public static final String CODE_ERROR = "codeError";
    public static final String CODE = "code";

    public void login() {
        if (SecretUtil.isSecret(requestJson)) {
            String username = requestJson.getString(USERNAME);
            String password = requestJson.getString(PASSWORD);
            System.out.println("用户名: " + username);
            String code = requestJson.getString(CODE);
            if (username != null && password != null && code != null) {
                if(!req.getSession().getAttribute("rand").toString().equals(code)) {
                    responseJson.put(RESULT, CODE_ERROR);
                }
                else if (LoginUtil.isPass(username, password)) {
                    Token token = new Token();
                    token.setSub(AUTHOR);
                    token.setTime(TOKEN_OVERTIME_SECOND);
                    token.setData(username);

                    responseJson.put(RESULT, SUCCESS);
                    responseJson.put(JWT, token.getToken());
                } else {
                    responseJson.put(RESULT, PASSWORD_ERROR);
                }
            } else {
                responseJson.put(RESULT, REQUEST_ERROR);
            }
        } else {
            errorString();
        }
    }

    public static final String TOKEN_OVERTIME = "tokenOvertime";
    public static final String TOKEN_ERROR = "tokenError";

    public void RefreshToken(String jwt) {
        if (SecretUtil.isSecret(requestJson)) {
            Token originToken = new Token(jwt);
            if (originToken.isToken()) {
                if (originToken.notOverDue()) {
                    String username = originToken.getPlayloadMap().get("username");
                    Token newToken = new Token();
                    newToken.setSub(username);
                    newToken.setData(username);
                    newToken.setTime(TOKEN_OVERTIME_SECOND);
                    Map<String, String> dataMap = newToken.getPlayloadMap();
                    responseJson.put(USERNAME, username);
                    responseJson.put(UserDao.ID, dataMap.get(UserDao.ID));
                    responseJson.put("isAdmin", dataMap.get("isAdmin"));
                    responseJson.put(LEVEL, dataMap.get(LEVEL));
                    responseJson.put(COIN, dataMap.get(COIN));
                    responseJson.put(BIG_VIP,  dataMap.get(BIG_VIP));
                    responseJson.put(PHOTOURL, dataMap.get(PHOTOURL));
                    responseJson.put("userSign", dataMap.get("userSign"));
                    responseJson.put(RESULT, SUCCESS);
                    responseJson.put(JWT, newToken.getToken());
                } else {
                    responseJson.put(RESULT, TOKEN_OVERTIME);
                }
            } else {
                responseJson.put(RESULT, TOKEN_ERROR);
            }
        } else {
            errorString();
        }
    }

    public static void main(String[] args) {
        Token t = new Token("963beb3afb5d58b15e07860c6a44c2fa0baef085bf21f27e202a5f0bb02f9807.eydzdWInOidhdXRob3InLCdjb21tZW50TnVtYmVyJzonMCcsJ25iZic6JzE1MTk3MzQ1MDYnLCd1cE51bWJlcic6JzAnLCdsZXZlbCc6JzEnLCdpc3MnOidiaWxpYmlsaS5jb20nLCdleHAnOicxNTIwMzM5MzA2JywnaWF0JzonMTUxOTczNDUwNicsJ2VtYWlsJzonNDQ3MzE3OTA3QHFxLmNvbScsJ3VzZXJuYW1lJzonYWRtaW4nLCdiaWdWaXAnOicwJywnY29pbic6JzAnfQ==.1482f3788794d6d5fd556ed916710930b703f5437b04b554764ce993d9bdb0d3");
        System.out.println(t.notOverDue());
    }

    public void Register() {
        if (SecretUtil.isSecret(requestJson)) {
            String username = requestJson.getString(USERNAME);
            String email = requestJson.getString(EMAIL);
            String password = requestJson.getString(PASSWORD);
            String code = requestJson.getString(CODE);
            if(!req.getSession().getAttribute("rand").equals(code)) {
                responseJson.put(RESULT, CODE_ERROR);
            }
            else if (!LoginUtil.hasUser(username)) {
                UserDao.insertNewUser(username, password, email);
                Token token = new Token();
                token.setSub(AUTHOR);
                token.setTime(TOKEN_OVERTIME_SECOND);
                token.setData(username);
                responseJson.put(RESULT, SUCCESS);
                responseJson.put(JWT, token.getToken());
            } else {
                responseJson.put(RESULT, USER_EXIST);
            }
        } else {
            errorString();
        }
    }

    public static final String INFO="info";
    public static final String RANK="rank";
    public static final String DATA="data";
    public static final String SPREAD="spread";
    public static final String VIDEOID="videoId";

    /**
     *  上传视频.
     */
    public void upVideo() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 500);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(1024 * 1024 * 300);
        List<FileItem> items = null;
        Token token = new Token(req.getHeader("jwt"));
        String upUser = token.getPlayloadMap().get("username");
        String userId = token.getPlayloadMap().get("id");
        try {
            items = upload.parseRequest(new ServletRequestContext(req));
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
        try {
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    long sizeInBytes = item.getSize();
                    String videoPath = path + "video";
                    Video video = new Video(fileName, sizeInBytes, upUser, path, userId);

                    InputStream in = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    String sessionName = req.getHeader("jwt") + fileName;
                    VideoUpProgress.put(sessionName + "Size", sizeInBytes);
                    VideoUpProgress.put(sessionName + "Del", "false");
                    VideoUpProgress.put(sessionName + "Id", video.getId());
                    int len = 0;
                    long progress = 0;

                    File dirMaker = new File(videoPath + "/" + video.getId());
                    if(!dirMaker.mkdirs()) {
                        System.out.println("创建文件夹出错");
                    }
                    fileName = videoPath + "/" + video.getId() + "/" + fileName;

                    OutputStream out = new FileOutputStream(fileName);
                    boolean delFlag = false;

                    while ((len = in.read(buffer)) != -1) {
                        if(VideoUpProgress.get(sessionName + "Del").equals("true")) {
                            VideoDao.stopVideo(String.valueOf(video.getId()));
                            delFlag = true;
                            break;
                        }
                        out.write(buffer, 0, len);
                        progress += len;
                        VideoUpProgress.put(sessionName + "Progress", progress);
                    }

                    VideoUpProgress.remove(sessionName + "Size");
                    VideoUpProgress.remove(sessionName + "Progress");

                    out.close();
                    in.close();

                    if(delFlag) {
                        new File(fileName).delete();
                        responseJson.put(RESULT, DELETE);
                    }
                    else {
                        responseJson.put("url", fileName);
                        responseJson.put(RESULT, SUCCESS);
                    }
                    responseJson.put(Video.ID, video.getId());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upCover() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 20);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(1024 * 1024 * 5);
        List<FileItem> items = null;
        String videoId = null;
        try {
            items = upload.parseRequest(new ServletRequestContext(req));
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
        String filename = null;
        try {
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("utf-8");
                    if(name.equals("filename")){
                        filename = value;
                    }
                }
                else {
                    String key = req.getHeader("jwt") + filename + "Id";
                    videoId = VideoUpProgress.get(key).toString();
                    String coverPath = path + "video";

                    InputStream in = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    File dirMaker = new File(coverPath + "/" + videoId + "/img");
                    if(!dirMaker.mkdirs()) {
                        System.out.println("创建文件夹出错");
                    }
                    String fileName = coverPath + "/" + videoId + "/img/cover.jpg";

                    System.out.println(fileName);

                    OutputStream out = new FileOutputStream(fileName);

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                    out.close();
                    in.close();

                    ImgUtil.cutImg(fileName, coverPath + "/" + videoId + "/img/cover_mini.jpg", 100, 168);
                    ImgUtil.cutImg(fileName, coverPath + "/" + videoId + "/img/cover.jpg", 600, 960);

                    String coverUrl = "/video/" + videoId + "/img";
                    VideoDao.setCoverUrl(videoId, coverUrl);
                    responseJson.put("url", coverUrl + "/cover_mini.jpg");
                    responseJson.put(RESULT, SUCCESS);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void VideoInfoUpdata() {
        if (SecretUtil.isSecret(requestJson)) {
            String id = requestJson.getString(VIDEOID);
            String div = requestJson.getString("div");
            String title = requestJson.getString(TITLE);
            String summary = requestJson.getString(SUMMARY);
            String[] divs = div.split("_");
            if(!VideoUtil.setTimeLength(requestJson.getString("videoUrl"), id)) {
                responseJson.put(RESULT, "formatError");
                return;
            }
            VideoDao.updataVideoInfo(title, summary, divs[0], divs[1], id);
            responseJson.put(RESULT, SUCCESS);
        } else {
            errorString();
        }
    }

    public void upHeadImg(String id, String username) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 20);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(1024 * 1024 * 5);
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(new ServletRequestContext(req));
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        try {
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String headPath = path + "img/user/" + id;
                    InputStream in = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    System.out.println(headPath);

                    File dirMaker = new File(headPath);
                    if (!dirMaker.mkdirs()) {
                        System.out.println("创建文件夹出错");
                    }
                    String fileName = headPath + "/head.jpg";

                    System.out.println(fileName);

                    OutputStream out = new FileOutputStream(fileName);

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                    out.close();
                    in.close();

                    ImgUtil.cutImg(headPath + "/head.jpg", headPath + "/head_mini.jpg", 180, 180);

                    UserDao.upHeadImg(username, "/img/user/" + id + "/head_mini.jpg");
                    responseJson.put(RESULT, SUCCESS);
                    responseJson.put("url", "/img/user/" + id + "/head_mini.jpg");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}