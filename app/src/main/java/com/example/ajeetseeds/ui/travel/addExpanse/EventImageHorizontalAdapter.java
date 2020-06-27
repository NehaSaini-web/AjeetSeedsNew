package com.example.ajeetseeds.ui.travel.addExpanse;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;

import java.util.List;

public class EventImageHorizontalAdapter extends RecyclerView.Adapter<EventImageHorizontalAdapter.MyViewHolder> {
    private List<String> imageUrlList;
    Activity activity;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;

        MyViewHolder(View view) {
            super(view);
            imgView = view.findViewById(R.id.imgView);
        }
    }

    public EventImageHorizontalAdapter(Activity activity, List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_display_horizontaly_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(activity.getApplicationContext())
                .load(StaticDataForApp.globalurl + "/" + imageUrlList.get(position))
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.gray3)
                .into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }
}
