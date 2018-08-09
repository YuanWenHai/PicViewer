package com.will.picviewer.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.will.picviewer.decoder.bean.ArticleObject;
import com.will.picviewer.network.NetworkServer;

/**
 * created  by will on 2018/7/23 16:31
 */
public class SPHelper {
    private static final String SP_NAME = "com.will";
    private static SPHelper mInstance;
    private SharedPreferences sp;

    private SPHelper(Context context){
        sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public static SPHelper getInstance(Context context){
        if(mInstance == null){
            synchronized(SPHelper.class){
                if(mInstance == null){
                    mInstance = new SPHelper(context);
                }
            }
        }
        return mInstance;
    }

    public void setArticleAsFavorited(ArticleObject object, boolean b){
        sp.edit().putBoolean(object.toString(),b).apply();
    }
    public boolean isArticleFavorited(ArticleObject object){
        return sp.getBoolean(object.toString(),false);
    }
    private static final String CURRENT_SERVER = "server";
    public void setCurrentServer(String server){
        sp.edit().putString(CURRENT_SERVER,"https://"+server+"/").apply();
    }
    public String getCurrentServer(){
        return  sp.getString(CURRENT_SERVER, NetworkServer.getDefaultServer());
    }
}
