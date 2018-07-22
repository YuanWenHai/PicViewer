package com.will.picviewer.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.will.picviewer.decoder.bean.PicObject;

import java.util.ArrayList;
import java.util.List;

public class PicPagerAdapter extends FragmentPagerAdapter {
    private List<PicObject> data = new ArrayList<>();
    public PicPagerAdapter(FragmentManager manager,List<PicObject> items){
        super(manager);
        data.addAll(items);
    }
    @Override
    public Fragment getItem(int position) {
        return PicFragment.getInstance(data.get(position));
    }

    @Override
    public int getCount() {
        return data.size();
    }

}
