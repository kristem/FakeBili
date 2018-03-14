package com.bilibili.Video;


import com.bilibili.dao.VideoDao;
import com.bilibili.util.JdbcUtil;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Video {
    private int id;
    private String videoName;
    private String simpleUrl;
    private String coverUrl;
    private String summary;
    private String sonDiv;
    private String fatherDiv;
    private int coinNum;
    private String upUser;
    private int commentNumber;
    private int barrageNumber;
    private long size;
    private int hour;
    private int sec;
    private int min;
    private String videoUrl;
    private String title;
    private boolean isPass;
    private String videoDate;
    private int upUserId;
    private int hits;

    public static final String VIDEONAME = "videoName";
    public static final String ID = "id";
    public static final String SONDIV = "sonDiv";
    public static final String FATHERDIV = "fatherDiv";
    public static final String UPUSER = "upUser";
    public static final String BARRAGENUMBER = "barrageNumber";
    public static final String COINNUM = "coinNum";
    public static final String SIMPLEURL = "simpleUrl";
    public static final String COVERURL = "coverUrl";
    public static final String SUMMARY = "summary";
    public static final String HOUR = "hour";
    public static final String MIN = "min";
    public static final String SEC = "sec";
    public static final String VIDEOSIZE = "videoSize";
    public static final String TITLE = "title";
    public static final String VIDEOURL = "videoUrl";
    public static final String ISPASS = "isPass";
    public static final String VIDEODATE = "videoDate";
    public static final String UPUSERID = "upUserId";
    public static final String HITS = "hits";


    public Video() {
    }

    public Video(String name, long size, String upUser, String path, String userId) {
        this.videoName = name;
        this.size = size;
        this.upUser = upUser;
        this.upUserId = Integer.parseInt(userId);
        this.id = VideoDao.initVideo(name, size, upUser, path, upUserId);
    }

    public Video(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getInt(ID));
        this.setVideoName(resultSet.getString(VIDEONAME));
        this.setCoinNum(resultSet.getInt(COINNUM));
        this.setSimpleUrl(resultSet.getString(SIMPLEURL));
        this.setCoverUrl(resultSet.getString(COVERURL));
        this.setSummary(resultSet.getString(SUMMARY));
        this.setUpUser(resultSet.getString(UPUSER));
        this.setBarrageNumber(resultSet.getInt(BARRAGENUMBER));
        this.setSonDiv(resultSet.getString(SONDIV));
        this.setFatherDiv(resultSet.getString(FATHERDIV));
        this.setMin(resultSet.getInt(MIN));
        this.setHour(resultSet.getInt(HOUR));
        this.setSec(resultSet.getInt(SEC));
        this.setSize(resultSet.getInt(VIDEOSIZE));
        this.setTitle(resultSet.getString(TITLE));
        this.setVideoUrl(resultSet.getString(VIDEOURL));
        this.setPass(resultSet.getBoolean(ISPASS));
        this.setVideoDate(resultSet.getString(VIDEODATE));
        this.setUpUserId(resultSet.getInt(UPUSERID));
        this.setHits(resultSet.getInt(HITS));
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setUpUserId(int upUserId) {
        this.upUserId = upUserId;
    }

    public String getSonDiv() {
        return sonDiv;
    }

    public void setSonDiv(String sonDiv) {
        this.sonDiv = sonDiv;
    }

    public String getUpUser() {
        return upUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getSimpleUrl() {
        return simpleUrl;
    }

    public void setSimpleUrl(String simpleUrl) {
        this.simpleUrl = simpleUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getFatherDiv() {
        return fatherDiv;
    }

    public void setFatherDiv(String fatherDiv) {
        this.fatherDiv = fatherDiv;
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getUpUserId() {
        return upUserId;
    }

    public void setUpUser(String upUser) {
        this.upUser = upUser;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getBarrageNumber() {
        return barrageNumber;
    }

    public void setBarrageNumber(int barrageNumber) {
        this.barrageNumber = barrageNumber;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", videoName='" + videoName + '\'' +
                ", simpleUrl='" + simpleUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", sonDiv='" + sonDiv + '\'' +
                ", fatherDiv='" + fatherDiv + '\'' +
                ", coinNum=" + coinNum +
                ", upUser='" + upUser + '\'' +
                ", commentNumber=" + commentNumber +
                ", barrageNumber=" + barrageNumber +
                ", size=" + size +
                ", hour=" + hour +
                ", sec=" + sec +
                ", min=" + min +
                ", videoUrl='" + videoUrl + '\'' +
                ", title='" + title + '\'' +
                ", isPass=" + isPass +
                ", videoDate='" + videoDate + '\'' +
                '}';
    }

    public static void main(String[] args) {
    }
}
