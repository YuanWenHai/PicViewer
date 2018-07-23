package com.will.picviewer.listPic;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.will.picviewer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * created  by will on 2018/7/23 10:27
 */
public class ListPicAdapter extends RecyclerView.Adapter<ListPicAdapter.ImageViewHolder>{

    private List<File> items = new ArrayList<>();
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(holder.imageView).load(items.get(position)).into(holder.imageView);
        //Picasso.get().load(items.get(position)).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(File file){
        items.add(file);
        notifyItemInserted(items.size()-1);
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.item_pic_list_image_view);
        }
    }
}
