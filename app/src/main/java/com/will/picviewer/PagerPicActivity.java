package com.will.picviewer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.will.picviewer.base.BaseActivity;
import com.will.picviewer.decoder.HtmlDecoder;
import com.will.picviewer.decoder.bean.PicObject;
import com.will.picviewer.decoder.bean.TitleObject;
import com.will.picviewer.file.FileHelper;
import com.will.picviewer.file.FilePath;
import com.will.picviewer.network.NetworkHelper;
import com.will.picviewer.network.NetworkServer;
import com.will.picviewer.pagerPic.PicFragment;
import com.will.picviewer.pagerPic.PicPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PagerPicActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TitleObject titleObject;
    private android.support.v7.widget.Toolbar mToolbar;
    private PicPagerAdapter mPicPagerAdapter;
    private List<PicObject> items;
    private volatile int downloadedCount =1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        titleObject = (TitleObject)(getIntent().getSerializableExtra("title"));
        initializeView();
        loadItems();
    }



    private void initializeView(){
        mViewPager = findViewById(R.id.activity_pic_view_pager);
        mToolbar = findViewById(R.id.activity_pic_toolbar);
        mToolbar.setTitle(titleObject.getTitle());
        mToolbar.setSubtitle("0/0");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.back_arraw_holo_dark_no_trim);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolbar.setSubtitle(position+1+"/"+items.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void loadItems(){

        NetworkHelper.getInstance().getHtml(NetworkServer.getDefaultServer()+titleObject.getLink(), new NetworkHelper.NetworkHelperHtmlCallback() {
            @Override
            public void onSuccess(String html) {
                items =  HtmlDecoder.getInstance().decodePicFormHtml(html);
                mPicPagerAdapter = new PicPagerAdapter(getSupportFragmentManager(),items);
                mViewPager.setAdapter(mPicPagerAdapter);
                mToolbar.setSubtitle("1/"+items.size());
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pic_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pic_toolbar_save:
                if(checkWriteStoragePermission()){
                    int currentIndex = mViewPager.getCurrentItem();
                    String picName = items.get(currentIndex).getLink().substring(items.get(currentIndex).getLink().lastIndexOf("/")+1,items.get(currentIndex).getLink().length());
                    Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.activity_pic_view_pager + ":" + mViewPager.getCurrentItem());
                    ((PicFragment)currentFragment).savePic(picName);
                }
                break;
            case R.id.menu_pic_toolbar_download_all:
                if(checkWriteStoragePermission()){
                    List<String> urls = new ArrayList<>();
                    for(PicObject object : items){
                        urls.add(object.getLink());
                    }
                    NetworkHelper.getInstance().downloadPics(urls, new NetworkHelper.NetworkHelperDownloadCallback() {
                        @Override
                        public void onSuccess(String fileName, byte[] bytes) {
                            if(downloadedCount == items.size()){
                                mViewPager.post(new Runnable() {
                                    @Override
                                    public void run(){
                                        Toast.makeText(PagerPicActivity.this, "download completely", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //Log.e("download:",fileName);
                            String filePath = FilePath.getAppPath() + items.get(0).getTitle() + File.separator + fileName;
                            FileHelper.saveBytesToDisk(filePath,bytes);
                            downloadedCount++;
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
                break;
            case R.id.menu_pic_toolbar_open_browser:
                break;
        }
        return true;
    }

}
