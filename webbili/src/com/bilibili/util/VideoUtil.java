package com.bilibili.util;

import com.bilibili.dao.VideoDao;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;

public class VideoUtil {
    public static boolean setTimeLength(String url, String videoId) {
        File video = new File(url);
        Encoder encoder = new Encoder();
        long length = 0;
        try {
            MultimediaInfo multimediaInfo = encoder.getInfo(video);
            length = multimediaInfo.getDuration();
        } catch (EncoderException e) {
            return false;
        }
        int sec = (int)(length/1000);
        int min = sec/60;
        int hour = min/60;
        sec = sec % 60;
        min = min % 60;
        VideoDao.setTime(hour, min, sec, videoId);
        return true;
    }
}
