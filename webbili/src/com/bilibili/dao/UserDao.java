package com.bilibili.dao;

import com.bilibili.user.User;
import com.bilibili.util.JdbcUtil;
import com.bilibili.util.JsonUtil;
import com.bilibili.util.SecretUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {
    public static final String COMMENTNUMBER = "commentNumber";
    public static final String PHOTOURL = "photoUrl";
    public static final String UPNUMBER = "upNumber";
    public static final String USERNAME = "username";
    public static final String BIG_VIP = "bigVip";
    public static final String EMAIL = "email";
    public static final String LEVEL = "level";
    public static final String COIN = "coin";
    public static final String ID = "id";
    public static final String PASSWORD="password";
    public static final String UTF8 = "UTF-8";
    /**
     * 初始头像的位置
     */
    private static final String initPhotoUrl = "/img/user/noface.gif";
    private static final String PHOTO_URL = "photoUrl";


    /**
     * 获得某个用户的信息
     *
     * @param username     用户名
     * @return 用户的信息Map
     */
    public static Map<String, String> getUserInfo(String username) {

        Map<String, String> info = null;
        String sql = "SELECT id,photoUrl,coin,level,upNumber,bigVip,isAdmin,userSign FROM user WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                info = new HashMap<>();
                info.put(USERNAME, username);
                info.put(UPNUMBER, resultSet.getString(UPNUMBER));
                info.put(ID, resultSet.getString(ID));
                info.put(BIG_VIP, resultSet.getString(BIG_VIP));
                info.put(COIN, resultSet.getString(COIN));
                info.put(PHOTO_URL, resultSet.getString(PHOTO_URL));
                info.put(LEVEL, resultSet.getString(LEVEL));
                info.put("isAdmin", resultSet.getString("isAdmin"));
                info.put("userSign", resultSet.getString("userSign"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getUserInfo抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement, resultSet);
        }

        return info;

    }

    /**
     * 获得某个用户的id
     *
     * @param username     用户名
     * @return 用户的id
     */
    public static int getUserid(String username) {

        int id = -1;
        String sql = "SELECT id FROM user WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getUserId抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement, resultSet);
        }

        return id;

    }

    /**
     * 获得用户的邮箱
     *
     * @param username 用户名
     * @return 用户的邮箱
     */
    public static String getUserEmail(int username) {

        String nickname = null;
        String sql = "SELECT email FROM user WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nickname = resultSet.getString(EMAIL);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getUserNickname抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement, resultSet);
        }

        return nickname;

    }

    /**
     * 获得用户的密码
     *
     * @param username     用户名
     * @return 用户的已加密的密码
     */
    public static String getUserPass(String username) {

        String password = null;
        String sql = "SELECT password FROM user WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString(PASSWORD);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getUserPass抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement, resultSet);
        }

        return password;

    }

    /**
     * 新用户的注册
     *
     * @param username     用户名
     * @param password     密码
     * @param email        邮箱
     */
    public static void insertNewUser(String username, String password, String email) {

        String sql = "INSERT INTO user(username,password,email,photoUrl,bigVip,coin,level,commentNumber,upNumber) VALUE(?,?,?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        System.out.println("新用户: 用户名 = " + username + ", 邮箱 = " + email);
        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, SecretUtil.encoderHs256(password));
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, initPhotoUrl);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7,1);
            preparedStatement.setInt(8,0);
            preparedStatement.setInt(9,0);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("insertNewUser抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }

    }

    public static User getExtraInfo(String username) {

        String sql = "SELECT userSign, signInDate, email, isAdmin, photoUrl FROM user WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                user = new User(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查找信息时抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return user;
    }

    public static void updateBaseInfo (String email, String sign, String username) {
        String sql = "UPDATE user SET email = ?, userSign = ? WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, sign);
            preparedStatement.setString(3, username);

            preparedStatement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static void upHeadImg (String username, String url) {
        String sql = "UPDATE user SET photoUrl = ? WHERE username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, url);
            preparedStatement.setString(2, username);

            preparedStatement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static User getUserById(int userId) {
        String sql = "SELECT email,username,level,photoUrl,userSign,id,bigVip,coin,signInDate,isAdmin FROM user WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                User user = new User();
                user.setFullInfo(resultSet);
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return null;
    }

    public static boolean putSQL(String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

