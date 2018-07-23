package com.will.picviewer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ImageListView extends ScrollView {

    private LinearLayout linearLayout;
    ImageListView(Context context){
        super(context);
        initialize();
    }

    public ImageListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ImageListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(){
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        addView(linearLayout);
    }
    private ImageView makeImageView(int imageWidth,int imageHeight){
        float ratio = (float)linearLayout.getWidth()/(float)imageWidth;
        ImageView imageView = new ImageView(getContext());
        imageView.setAdjustViewBounds(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//new LinearLayout.LayoutParams(linearLayout.getWidth(),(int)(imageHeight*ratio));
        imageView.setLayoutParams(params);
        return imageView;
    }
    public void addImage(Bitmap image){
        ImageView imageView = makeImageView(image.getWidth(),image.getHeight());
        imageView.setImageBitmap(image);
        linearLayout.addView(imageView);
    }
    public void addImage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        ImageView imageView = makeImageView(options.outWidth,options.outHeight);
        linearLayout.addView(imageView);

    }

}
