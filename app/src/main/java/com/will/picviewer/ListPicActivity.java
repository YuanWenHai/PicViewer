package com.will.picviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.will.picviewer.base.BaseActivity;
import com.will.picviewer.decoder.HtmlDecoder;
import com.will.picviewer.decoder.bean.ArticleObject;
import com.will.picviewer.decoder.bean.PicObject;
import com.will.picviewer.file.FileHelper;
import com.will.picviewer.listPic.ListPicAdapter;
import com.will.picviewer.network.NetworkHelper;
import com.will.picviewer.sp.SPHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListPicActivity extends BaseActivity {

    private ArticleObject articleObject;
    private List<PicObject> items;
    private RecyclerView picListView;
    private Toolbar toolbar;
    private ListPicAdapter adapter;

    private boolean isFavoriteChecked;
    private volatile int downloadCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        articleObject = (ArticleObject)(getIntent().getSerializableExtra("title"));
        initializeView();
        getImageList();
    }
    private void initializeView(){
        picListView = findViewById(R.id.activity_image_list_recycler_view);
        adapter = new ListPicAdapter();
        picListView.setAdapter(adapter);
        picListView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.activity_image_list_toolbar);
        toolbar.setTitle(articleObject.getTitle());
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
        NetworkHelper.getInstance().getHtml(SPHelper.getInstance(this).getCurrentServer()+ articleObject.getLink(), new NetworkHelper.NetworkHelperHtmlCallback() {
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
                    final String filePath = externalCacheDir.getPath() + File.separator + articleObject.getTitle() + File.separator + fileName;
                    FileHelper.saveBytesToDisk(filePath,bytes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setSubtitle(downloadCount+"/"+items.size());
                            adapter.addItem(new File(filePath));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_pic_toolbar_menu,menu);
        setFavoriteMenuIconChecked( SPHelper.getInstance(this).isArticleFavorited(articleObject));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_list_pic_favorite){
            setFavoriteMenuIconChecked(!isFavoriteChecked);
            SPHelper.getInstance(this).setArticleAsFavorited(articleObject,!isFavoriteChecked);
        }else if(item.getItemId() == R.id.menu_list_pic_open_browser){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SPHelper.getInstance(this).getCurrentServer()+ articleObject.getLink()));
            startActivity(browserIntent);
        }
        return true;
    }

    private void setFavoriteMenuIconChecked(boolean which){
        toolbar.getMenu().findItem(R.id.menu_list_pic_favorite).setIcon(which?R.drawable.favorite_holo_dark_no_trim:R.drawable.favorite_border_holo_dark_no_trim);
    }
}
