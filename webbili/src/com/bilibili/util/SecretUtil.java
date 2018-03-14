package com.bilibili.util;

import com.bilibili.token.Token;
import net.sf.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecretUtil {
    private static final String SECRET = "Bilibili";
    private static final String INSTANCE="HmacSHA256";

    public static boolean isSecret(JSONObject data) {
        return true;
    }

    public static String encoderHs256(String message) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance(INSTANCE);
            SecretKeySpec secret_key = new SecretKeySpec(SECRET.getBytes(), INSTANCE);
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            StringBuilder hs = new StringBuilder();
            String stmp;
            for (int n = 0; bytes!=null && n < bytes.length; n++) {
                stmp = Integer.toHexString(bytes[n] & 0XFF);
                if (stmp.length() == 1)
                    hs.append('0');
                hs.append(stmp);
            }
            hash = hs.toString().toLowerCase();
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 " + e.getMessage());
        }
        return hash;
    }

    public static boolean checkJwt(String jwt) {
        Token token = new Token(jwt);
        if(token.isToken() && token.notOverDue()) {
            return true;
        }
        return false;
    }

    public static boolean videoToUser(String username, String videoId) {
        String sql = "SELECT upUser FROM video WHERE id = ?";
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, Integer.parseInt(videoId));

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                if(resultSet.getString("upUser").equals(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAdmin(String jwt) {
        Token token = new Token(jwt);
        if(token.isToken() && token.notOverDue()) {
            if(token.getPlayloadMap().get("isAdmin").equals("1")) {
                return true;
            }
        }
        return false;
    }
}