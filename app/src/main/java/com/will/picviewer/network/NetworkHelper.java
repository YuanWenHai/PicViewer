package com.will.picviewer.network;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkHelper {

    private static NetworkHelper mInstance;

    private OkHttpClient mClient;
    private Handler mHandler = new Handler(Looper.myLooper());

    private NetworkHelper(){
        mClient = new OkHttpClient();
        mClient.dispatcher().setMaxRequests(20);
    }
    public static NetworkHelper getInstance(){
        if(mInstance == null){
            synchronized(NetworkHelper.class){
                if(mInstance == null){
                    mInstance = new NetworkHelper();
                }
            }
        }
        return mInstance;
    }


    public void getHtml(String link, final NetworkHelperHtmlCallback callback){
        final Request request = new Request.Builder().url(link).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure( Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String html = new String(response.body().bytes(),"gbk");

                response.close();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(html);
                    }
                });
            }
        });
    }
    public void downloadPics(List<String>  urls, final NetworkHelperDownloadCallback callback){
        Request request;
        OkCallback okCallback;
        for(String url : urls){
            request = new Request.Builder().url(url).build();
            okCallback = new OkCallback(callback);
            mClient.newCall(request).enqueue(okCallback);
        }
    }
    class OkCallback implements Callback{
        private NetworkHelperDownloadCallback callback;


        public OkCallback(NetworkHelperDownloadCallback callback){
            this.callback = callback;
        }
        @Override
        public void onFailure(Call call, IOException e) {
            callback.onFailure();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.code() == 200){
                String url = call.request().url().toString();
                String picName = url.substring(url.lastIndexOf(File.separator)+1,url.length());
                callback.onSuccess(picName,response.body().bytes());
                response.close();
                return;
            }
            callback.onFailure();
        }
    }
    public interface NetworkHelperHtmlCallback {
        void onSuccess(String html);
        void onFailure();
    }
    public interface NetworkHelperDownloadCallback{
        void onSuccess(String fileName,byte[] bytes);
        void onFailure();
    }
}
