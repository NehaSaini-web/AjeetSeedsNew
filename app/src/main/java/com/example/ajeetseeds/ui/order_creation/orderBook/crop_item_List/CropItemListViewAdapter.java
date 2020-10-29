package com.example.ajeetseeds.ui.order_creation.orderBook.crop_item_List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CropItemListViewAdapter extends BaseAdapter implements Filterable {
    List<CropItemMasterTable.CropItemMasterModel> mStringFilterList;
    ValueFilter valueFilter;

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() <= 0) {
                constraint="";
            }
           // if (constraint != null && constraint.length() > 0) {
                List<CropItemMasterTable.CropItemMasterModel> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if(CropItemFragment.globale_crop_item_category==null ||CropItemFragment.globale_crop_item_category.equalsIgnoreCase("")){
                        if ((mStringFilterList.get(i).item_no.toUpperCase())
                                .contains(constraint.toString().toUpperCase()) || (mStringFilterList.get(i).item_desc.toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            filterList.add(mStringFilterList.get(i));
                        }
                    }else{
                        if (((mStringFilterList.get(i).item_no.toUpperCase())
                                .contains(constraint.toString().toUpperCase()) || (mStringFilterList.get(i).item_desc.toUpperCase())
                                .contains(constraint.toString().toUpperCase()))&&mStringFilterList.get(i).crop.equalsIgnoreCase(CropItemFragment.globale_crop_item_category)) {
                            filterList.add(mStringFilterList.get(i));
                        }
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
//            } else {
//                results.count = mStringFilterList.size();
//                results.values = mStringFilterList;
//            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            dataModelList = (List<CropItemMasterTable.CropItemMasterModel>) results.values;
            notifyDataSetChanged();
        }

    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(String item_no, String flag);
    }

    private final OnItemClickListener listener;
    private List<CropItemMasterTable.CropItemMasterModel> dataModelList;
    private Activity activity;

    public CropItemListViewAdapter(List<CropItemMasterTable.CropItemMasterModel> modelList, Activity activity, OnItemClickListener listener) {
        dataModelList = modelList;
        this.mStringFilterList = modelList;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        itemView = inflater.inflate(R.layout.crop_item_listview, null);
        ImageView cardImageView = itemView.findViewById(R.id.imageView);
        TextView titleTextView = itemView.findViewById(R.id.card_title);
        TextView subTitleTextView = itemView.findViewById(R.id.card_subtitle);
        Button add_qty_button = itemView.findViewById(R.id.add_qty_button);
        Button remove_qty_button = itemView.findViewById(R.id.remove_qty_button);
        TextView sum_of_calculatedQty = itemView.findViewById(R.id.sum_of_calculatedQty);
        MaterialCardView card_filter_hide_show = itemView.findViewById(R.id.card_filter_hide_show);

        CropItemMasterTable.CropItemMasterModel dataModel = dataModelList.get(position);
        if (!dataModel.image_url.contains("no_image_placeholder")) {
            cardImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(activity)
                .load(StaticDataForApp.globalurl + dataModel.image_url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.gray3)
                .into(cardImageView);
        titleTextView.setText(dataModel.item_desc);
        subTitleTextView.setText(dataModel.item_no+" ( "+dataModel.crop+" )");
        add_qty_button.setOnClickListener(view -> {
            listener.onItemClick(dataModel.item_no, "Add");
        });
        remove_qty_button.setOnClickListener(view -> {
            listener.onItemClick(dataModel.item_no, "Remove");
        });
        sum_of_calculatedQty.setText(String.valueOf(dataModel.total_buy_qty));
        return itemView;

    }

}
