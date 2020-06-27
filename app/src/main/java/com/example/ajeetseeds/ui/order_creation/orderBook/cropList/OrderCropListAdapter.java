package com.example.ajeetseeds.ui.order_creation.orderBook.cropList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class OrderCropListAdapter extends RecyclerView.Adapter<OrderCropListAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(CropMasterTable.CropMasterModel item);
    }

    private final OnItemClickListener listener;
    private List<CropMasterTable.CropMasterModel> dataModelList;
    private Context mContext;

    // View holder class whose objects represent each list item

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImageView;
        public TextView titleTextView;
        public TextView subTitleTextView;
        public MaterialCardView cardview_section;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.card_title);
            subTitleTextView = itemView.findViewById(R.id.card_subtitle);
            cardview_section = itemView.findViewById(R.id.cardview_section);
        }

        public void bindData(CropMasterTable.CropMasterModel dataModel, Context context) {
            if (!dataModel.image_url.contains("no_image_placeholder")) {
                cardImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            Glide.with(context)
                    .load(StaticDataForApp.globalurl + dataModel.image_url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.color.gray3)
                    .into(cardImageView);
            titleTextView.setText(dataModel.description);
            subTitleTextView.setText(dataModel.code);
            cardview_section.setOnClickListener(view -> {
                listener.onItemClick(dataModel);
            });
        }
    }

    public OrderCropListAdapter(List<CropMasterTable.CropMasterModel> modelList, Context context, OnItemClickListener listener) {
        dataModelList = modelList;
        mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_listview, parent, false);
        // Return a new view holder

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position

        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items

        return dataModelList.size();
    }
}