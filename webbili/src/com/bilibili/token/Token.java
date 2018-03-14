package com.bilibili.token;

import com.bilibili.dao.UserDao;
import com.bilibili.util.SecretUtil;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.bilibili.dao.UserDao.EMAIL;

/**
{
  'typ': 'JWT',
  'alg': 'HS256'
}
sub: "user"
iss: JWT的签发者
ist: token签发日期
exp: token过期时间
sd: token被处理的开始日期
jti：JWT ID
*/

public class Token {
    private String header;
    private Map<String, String> playloadMap;
    private String playloadStr;
    private String signature;

    public Token() {
        playloadMap = new HashMap<>();
        this.header = "963beb3afb5d58b15e07860c6a44c2fa0baef085bf21f27e202a5f0bb02f9807";
        this.playloadMap.put("iss", "bilibili.com");
    }

    public Token(String token) {
        String[] tokens = token.split("\\.");
        if (tokens.length == 3) {
            this.header = tokens[0];
            this.playloadStr = tokens[1];
            byte[] deByte = Base64.getDecoder().decode(tokens[1]);
            String deStr = null;
            try {
                deStr = new String(deByte,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSONObject.fromObject(deStr);
            Set<String> set = jsonObject.keySet();
            playloadMap = new HashMap<>();
            for (String key : set) {
                playloadMap.put(key, jsonObject.getString(key));
            }
            this.signature = tokens[2];
        }
    }

    public Map<String, String> getPlayloadMap() {
        return this.playloadMap;
    }

    public String getEmail() {
        return this.playloadMap.get(EMAIL);
    }

    public void setData(String username) {
        Map<String, String> userInfo = UserDao.getUserInfo(username);
        for (Map.Entry<String, String> entry : userInfo.entrySet()) {
            this.playloadMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void setSub(String sub) {
        playloadMap.put("sub", sub);
    }

    public void setTime(long time) {
        long nowTime = new Date().getTime() / 1000;
        playloadMap.put("sd", String.valueOf((long)Math.ceil(nowTime)));
        playloadMap.put("ist", String.valueOf(nowTime));
        playloadMap.put("exp", String.valueOf((long)Math.ceil(nowTime + time)));
    }

    public String getToken() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String> entry : playloadMap.entrySet()) {
            sb.append("'").append(entry.getKey()).append("':'")
                    .append(entry.getValue()).append("',");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        this.playloadStr = sb.toString();
        String base64Playload = null;
        try {
            base64Playload = Base64.getEncoder().encodeToString(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String signature = this.header + "." + base64Playload;
        this.signature = signature;

        String hs256Signature = SecretUtil.encoderHs256(signature);
        return this.header + "." + base64Playload + "." + hs256Signature;
    }


    public boolean isToken() {
        if (this.header != null && this.playloadStr != null && this.signature != null) {
            String userSignature = SecretUtil.encoderHs256(header + "." + playloadStr);
            if (this.signature.equals(userSignature)) {
                return true;
            }
        }
        return false;
    }

    public boolean notOverDue() {
        long tokenExpires = Long.parseLong(playloadMap.get("exp"));
        long tokenStartDate = Long.parseLong(playloadMap.get("sd"));
        long nowTime = (long) Math.ceil(new Date().getTime() / 1000);
        return tokenExpires >= nowTime && nowTime >= tokenStartDate;
    }


    public static void main(String[] args) {
        Token token = new Token();
        token.setData("user");
        token.setSub("user");
        token.setTime(10000L);
        Token t = new Token(token.getToken());
    }
}
