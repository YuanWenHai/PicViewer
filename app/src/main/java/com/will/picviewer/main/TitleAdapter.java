package com.will.picviewer.main;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.will.picviewer.R;
import com.will.picviewer.decoder.bean.ArticleObject;

import java.util.ArrayList;
import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder>{

    private List<ArticleObject> data = new ArrayList<>();
    private TitleItemClickCallback callback;
    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        ArticleObject object =  data.get(position);
        holder.author.setText(object.getAuthor());
        holder.title.setText(object.getTitle());
        if(object.getTime().isEmpty()){
            holder.time.setText("置顶 ");
            holder.time.setTextColor(Color.RED);
        }else{
            holder.time.setText(object.getTime());
            holder.time.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<ArticleObject> items){
        data.addAll(items);
    }
    public void clearItems(){
        data.clear();
        notifyDataSetChanged();
    }
    public void setOnItemClickCallback(TitleItemClickCallback callback){
        this.callback = callback;
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView title,author,time;
        TitleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.activity_title_list_item_title);
            author = itemView.findViewById(R.id.activity_title_list_item_author);
            time = itemView.findViewById(R.id.activity_title_list_item_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null){
                        callback.onClick(data.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
    public interface TitleItemClickCallback{
        void onClick(ArticleObject object);
    }
}
