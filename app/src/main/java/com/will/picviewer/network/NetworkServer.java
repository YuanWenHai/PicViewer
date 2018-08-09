package com.will.picviewer.network;

import android.content.Context;

import com.will.picviewer.sp.SPHelper;

public class NetworkServer {

    private static String SERVER_MAINLAND = "https://cl.b9x.win/";
    private static String PIC_LIST_URL = "thread0806.php?fid=16&search=&page=";
    public static String getDefaultServer(){
        return SERVER_MAINLAND;
    }
    public static String getDaguarreTitleListUrl(Context context,int pageIndex){
        return SPHelper.getInstance(context).getCurrentServer() + PIC_LIST_URL + pageIndex;
    }
}
