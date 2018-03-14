package com.bilibili.dao;

import com.bilibili.Video.Video;
import com.bilibili.util.FileUtil;
import com.bilibili.util.JdbcUtil;
import com.bilibili.util.SecretUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static javax.management.timer.Timer.ONE_WEEK;

public class VideoDao {

    private static Video getRandomVideo(String sql) {

        Video video = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                video = new Video(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getRandomVideo Error");
        } finally {
            JdbcUtil.close(connection, preparedStatement, resultSet);
        }

        return video;

    }

    public static Set<Video> getSpreadVideoSet() {
        Set<Video> spreadVideo = new HashSet<>();
        while (spreadVideo.size() < 5) {
            spreadVideo.add(getAllTypeRandomVideo());
        }
        return spreadVideo;
    }

    public static Video getAllTypeRandomVideo() {
        String sql = "SELECT * FROM videoInfo WHERE type = ? AND success = 'y' ORDER BY rand() LIMIT 1";
        return getRandomVideo(sql);
    }
    public static Video getOneTypeRandomVideo(String type) {
        String sql = "SELECT * FROM videoInfo WHERE type = ? AND success = 'y' ORDER BY rand() LIMIT 1";
        return getRandomVideo(sql);
    }

    public static Set<Video> getPartitionInfoSet(String type) {
        Set<Video> partitionSet = new HashSet<>();
        while (partitionSet.size() < 10) {
            partitionSet.add(getOneTypeRandomVideo(type));
        }
        return partitionSet;
    }


    public static Set<Video> getPartitionRankSet(String type, long nowTime) {
        Set<Video> partitionSet = new TreeSet<>();
        String sql = "SELECT video.*,user.*,SUM(video.views+video.coin*70) AS temp " +
                "FROM video,user WHERE time > ? AND type = ? AND video.authorId=user.id AND success = 'y' " +
                "GROUP BY video.id ORDER BY temp DESC LIMIT 7";
        try {
            Connection connection = JdbcUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, nowTime - ONE_WEEK);
            pstmt.setString(2, type);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Video video = new Video(resultSet);
                partitionSet.add(video);
            }
            while (partitionSet.size() < 7) {
                partitionSet.add(getAllTypeRandomVideo());
            }
            resultSet.close();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getPaetitionRankSet Error");
        }
        return partitionSet;
    }

    public static Video getVideoInfo(int id) {
        String sql = "SELECT video.*,user.* FROM video,user WHERE video.id = ? AND video.authorId = user.id AND success = 'y'";
        Video video = null;
        try {
            Connection connection = JdbcUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                video = new Video(resultSet);
            }
            resultSet.close();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return video;
    }

    public static int initVideo(String name, long size, String upUser, String path, int upUserId) {
        String sql_01 = "INSERT INTO video(videoName, videoSize, upUser, upUserId) VALUE(?,?,?,?) ";
        String sql_02 = "SELECT @@IDENTITY AS return_id";
        String sql_03 = "UPDATE video SET realPath = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int id = -1;

        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql_01);

            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, size);
            preparedStatement.setString(3, upUser);
            preparedStatement.setInt(4, upUserId);

            preparedStatement.execute();

            preparedStatement = connection.prepareStatement(sql_02);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                id = Integer.parseInt(resultSet.getString("return_id"));
            }

            preparedStatement = connection.prepareStatement(sql_03);
            preparedStatement.setString(1, path + "video/" + id);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();

        }
        catch (SQLException e) {
            System.out.println("初始视频时错误");
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(connection, preparedStatement);
        }

        return id;
    }

    public static void stopVideo(String videoId) {
        String sql = "DELETE FROM video " +
                "WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(videoId));
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("删除视频时抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static void updataVideoInfo(String title, String summary, String fatherDiv,
                                       String div, String videoId) {
        String sql = "UPDATE video SET title = ?, summary = ?, fatherDiv = ?, sonDiv = ?" +
                ", simpleUrl = ?, coverUrl = ?, videoUrl = ?" +
                " WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, summary);
            preparedStatement.setString(3, fatherDiv);
            preparedStatement.setString(4, div);
            preparedStatement.setString(5, "/video/" + videoId + "/img");
            preparedStatement.setString(6, "/video/" + videoId + "/img");
            preparedStatement.setString(7, "/video/" + videoId);

            preparedStatement.setInt(8, Integer.parseInt(videoId));
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("更新视频信息时抛错误！");
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static boolean deleteVideo(String videoId) {
        String realPath = null;

        String sql_01 = "SELECT realPath FROM video WHERE id = ?";
        String sql_02 = "DELETE FROM video WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql_01);
            preparedStatement.setInt(1, Integer.parseInt(videoId));

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                realPath = resultSet.getString("realPath");
            }
            else {
                return false;
            }

            //删除本地的视频
            if(!FileUtil.deleteDir(new File(realPath))) {
                return false;
            }

            //删除数据库中的信息
            preparedStatement = connection.prepareStatement(sql_02);
            preparedStatement.setInt(1, Integer.parseInt(videoId));
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return true;
    }

    public static boolean hasPic(String id) {
        String sql = "SELECT id FROM video WHERE id = ? AND coverUrl IS NOT NULL";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(id));

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                System.out.println("ok");
                return true;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return false;
    }

    public static void setCoverUrl(String videoId, String url) {
        String sql = "UPDATE video SET coverUrl = ?, simpleUrl = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, url + "/cover.jpg");
            preparedStatement.setString(2, url + "/cover_mini.jpg");
            preparedStatement.setInt(3, Integer.parseInt(videoId));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static void setTime(int hour, int min, int sec, String videoId) {
        String sql = "UPDATE video SET hour = ?, min = ?, sec = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, hour);
            preparedStatement.setInt(2, min);
            preparedStatement.setInt(3, sec);
            preparedStatement.setInt(4, Integer.parseInt(videoId));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static String getByTime() {
        String sql = "SELECT * from video WHERE hour IS NOT NULL ORDER BY videoDate DESC, id DESC LIMIT 50";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;;
        JSONArray allVideo = new JSONArray();
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Video video = new Video(resultSet);
                allVideo.add(JSONObject.fromObject(video).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return allVideo.toString();
    }

    public static void upHits(int videoId) {
        String sql = "UPDATE video SET hits = hits + 1 WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, videoId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
    }

    public static String getDivCnt () {
        String sql = "SELECT fatherDiv, COUNT(*) AS nums " +
                "FROM video GROUP BY fatherDiv";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;;
        JSONObject job = new JSONObject();
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                job.put(resultSet.getString("fatherDiv"),
                        resultSet.getString("nums"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return job.toString();
    }

    public static Video getVideoById(int id) {
        String sql = "SELECT * from video WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Video video = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                video = new Video(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement);
        }
        return video;
    }
    public static String getVideoByUserId(int upUserId) {
        String sql = "SELECT * from video WHERE upUserId = ?";
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Video video=null;
        JSONArray jsonArray = new JSONArray();
        JSONObject temp = null;
        try {
            connection=JdbcUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,upUserId);
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                video=new Video(resultSet);
                temp = JSONObject.fromObject(video);
                jsonArray.add(temp.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(connection,preparedStatement);
        }
        return jsonArray.toString();
    }

    public static String getVideoByKeyTitle(String keyTitle){
        String sql = "SELECT * from video WHERE title LIKE ? ORDER BY id DESC";
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Video video=null;
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=null;
        try {
            connection=JdbcUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+keyTitle+"%");
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                video=new Video(resultSet);
                jsonObject=JSONObject.fromObject(video);
                jsonArray.add(jsonObject.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(connection,preparedStatement);
        }
        return jsonArray.toString();

    }

    public static JSONObject getVideoByDiv(String div_01, String div_02){
        String sql = "SELECT * from video WHERE sonDiv = ?  ORDER BY hits DESC";
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Video video=null;
        JSONArray jsonArray_01=new JSONArray();
        JSONArray jsonArray_02=new JSONArray();
        JSONObject jsonObject=null;
        try {
            connection=JdbcUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,div_01);
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                video = new Video(resultSet);
                jsonObject=JSONObject.fromObject(video);
                jsonArray_01.add(jsonObject.toString());
            }
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,div_02);
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                video = new Video(resultSet);
                jsonObject=JSONObject.fromObject(video);
                jsonArray_02.add(jsonObject.toString());
            }
            jsonObject.put("div1", jsonArray_01.toString());
            jsonObject.put("div2", jsonArray_02.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(connection,preparedStatement);
        }
        return jsonObject;

    }

}
