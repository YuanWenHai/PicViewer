package com.will.picviewer.pager;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.will.picviewer.R;
import com.will.picviewer.decoder.bean.PicObject;
import com.will.picviewer.file.FileHelper;
import com.will.picviewer.file.FilePath;

import java.io.File;

public class PicFragment extends Fragment {

    private PicObject item;
    public static PicFragment getInstance(PicObject object){
        PicFragment fragment = new PicFragment();
        Bundle args = new Bundle();
        args.putSerializable("item",object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        item = (PicObject)args.getSerializable("item");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager,container,false);
        PhotoView imageView = view.findViewById(R.id.fragment_view_pager_image_view);

        final RequestCreator  picassoRequestCreator = Picasso.get().load(item.getLink());
        picassoRequestCreator.into(imageView);
        return view;
    }
    public void savePic(final String fileName){
        Picasso.get().load(item.getLink()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String filePath = FilePath.getAppPath() + item.getTitle() + File.separator + fileName;
                if(FileHelper.saveBitmapToDisk(filePath,bitmap)){
                    Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "save failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }
}
