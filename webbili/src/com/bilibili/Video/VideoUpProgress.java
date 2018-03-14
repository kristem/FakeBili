package com.bilibili.Video;

import java.util.Hashtable;

//记录当前视频上传进度
public class VideoUpProgress {
    private static Hashtable<Object, Object> progress = new Hashtable<>();
    public static void put(Object video, Object percent) {
        progress.put(video, percent);
    }
    public static Object get(Object video) {
        return progress.get(video);
    }
    public static void remove(Object video) {
        progress.remove(video);
    }
}
