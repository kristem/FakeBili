package com.bilibili.util;


import java.io.File;

public class FileUtil {
    public static Boolean deleteDir(File dir) {
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            for(File file : files) {
                if(!deleteDir(file)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
