package com.will.picviewer.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.will.picviewer.decoder.bean.ArticleObject;

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
}
