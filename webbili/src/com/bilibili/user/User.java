package com.bilibili.user;

import java.sql.ResultSet;
import java.sql.SQLException;


public class User {
    private String email;
    private String userSign;
    private String signInDate;
    private boolean isAdmin;
    private String username;
    private String photoUrl;
    private boolean bigVip;
    private int level;
    private int coin;
    private int id;

    public User(ResultSet resultSet) throws SQLException{
        this.email = resultSet.getString("email");
        this.userSign = resultSet.getString("userSign");
        this.signInDate = resultSet.getString("signInDate");
        this.isAdmin = resultSet.getBoolean("isAdmin");
        this.photoUrl = resultSet.getString("photoUrl");
    }

    public User() {

    }

    public void setFullInfo(ResultSet resultSet) throws SQLException{
        this.userSign = resultSet.getString("userSign");
        this.signInDate = resultSet.getString("signInDate");
        this.isAdmin = resultSet.getBoolean("isAdmin");
        this.username = resultSet.getString("username");
        this.photoUrl = resultSet.getString("photoUrl");
        this.bigVip = resultSet.getBoolean("bigVip");
        this.level = resultSet.getInt("level");
        this.coin = resultSet.getInt("coin");
        this.id = resultSet.getInt("id");
        this.email = resultSet.getString("email");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isBigVip() {
        return bigVip;
    }

    public void setBigVip(boolean bigVip) {
        this.bigVip = bigVip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getSignInDate() {
        return signInDate;
    }

    public void setSignInDate(String signInDate) {
        this.signInDate = signInDate;
    }

}
