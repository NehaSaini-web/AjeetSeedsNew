package com.example.ajeetseeds.ui.order_creation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;

import java.util.List;

public class CartProductDetailListAdapter extends BaseAdapter {
    List<CropItemMasterTable.CropItemMasterModel> listdata;
    Activity activity;

    public interface OnItemClickListener {
        void onItemClick(int position, String flag);
    }

    private final OnItemClickListener listener;

    public CartProductDetailListAdapter(Activity activity, List<CropItemMasterTable.CropItemMasterModel> listdata, OnItemClickListener listener) {
        super();
        this.listdata = listdata;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.cart_product_detail_listview, null);
        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        TextView itemDesc = convertView.findViewById(R.id.itemDesc);
        TextView item_code = convertView.findViewById(R.id.item_code);
        TextView sum_of_calculatedQty = convertView.findViewById(R.id.sum_of_calculatedQty);
        Button add_qty_button = convertView.findViewById(R.id.add_qty_button);
        Button remove_qty_button = convertView.findViewById(R.id.remove_qty_button);

        itemDesc.setText(listdata.get(position).item_desc);
        item_code.setText("Pack Size : " + listdata.get(position).fg_pack_size);
        if (listdata.get(position).image_url.contains("no_image_placeholder")) {
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(convertView)
                .load(StaticDataForApp.globalurl + listdata.get(position).image_url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.gray3)
                .into(itemImage);

        add_qty_button.setOnClickListener(view -> {
            listener.onItemClick(position, "Add");
        });
        remove_qty_button.setOnClickListener(view -> {
            listener.onItemClick(position, "Remove");
        });
        sum_of_calculatedQty.setText(String.valueOf(listdata.get(position).total_buy_qty));
        return convertView;

    }

}
