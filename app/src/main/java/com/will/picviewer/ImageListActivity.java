package com.will.picviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.will.picviewer.base.BaseActivity;
import com.will.picviewer.decoder.HtmlDecoder;
import com.will.picviewer.decoder.bean.PicObject;
import com.will.picviewer.decoder.bean.TitleObject;
import com.will.picviewer.file.FileHelper;
import com.will.picviewer.network.NetworkHelper;
import com.will.picviewer.network.NetworkServer;
import com.will.picviewer.view.ImageListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends BaseActivity {

    private TitleObject titleObject;
    private List<PicObject> items;
    private ImageListView imageListView;
    private Toolbar toolbar;
    private volatile int downloadCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        titleObject = (TitleObject)(getIntent().getSerializableExtra("title"));
        initializeView();
        getImageList();
    }
    private void initializeView(){
        imageListView = findViewById(R.id.activity_image_list_view);
        toolbar = findViewById(R.id.activity_image_list_toolbar);
        toolbar.setTitle(titleObject.getTitle());
        toolbar.setSubtitle("0/0");
        toolbar.setNavigationIcon(R.drawable.back_arraw_holo_dark_no_trim);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getImageList(){
        NetworkHelper.getInstance().getHtml(NetworkServer.getDefaultServer()+titleObject.getLink(), new NetworkHelper.NetworkHelperHtmlCallback() {
            @Override
            public void onSuccess(String html) {
                items =  HtmlDecoder.getInstance().decodePicFormHtml(html);
                loadImages();
            }

            @Override
            public void onFailure() {

            }
        });
    }
    private void loadImages(){
        if(checkWriteStoragePermission()){
            List<String> urls = new ArrayList<>();
            for(PicObject object : items){
                urls.add(object.getLink());
            }
            NetworkHelper.getInstance().downloadPics(urls, new NetworkHelper.NetworkHelperDownloadCallback() {
                @Override
                public void onSuccess(String fileName, final byte[] bytes) {
                    File externalCacheDir = getExternalCacheDir();
                    final String filePath = externalCacheDir.getPath() + File.separator + titleObject.getTitle() + File.separator + fileName;
                    FileHelper.saveBytesToDisk(filePath,bytes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setSubtitle(downloadCount+"/"+items.size());
                            //imageListView.addImage(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                            imageListView.addImage(filePath);
                            downloadCount++;
                        }
                    });
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
}
