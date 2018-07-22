package com.will.picviewer.file;

import android.os.Environment;

public class FilePath {
    private static final String APP_DIRECTORY = "/.PicViewer/";

    public static String getAppPath(){
        return  Environment.getExternalStorageDirectory().getPath() + APP_DIRECTORY;
    }
}
