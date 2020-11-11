package com.example.ajeetseeds.ui.order_creation.orderBook.crop_item_List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.ui.travel.approveTravel.ApproveTravelDetailFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

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
                constraint = "";
            }
            // if (constraint != null && constraint.length() > 0) {
            List<CropItemMasterTable.CropItemMasterModel> filterList = new ArrayList<>();
            for (int i = 0; i < mStringFilterList.size(); i++) {
                if (CropItemFragment.globale_crop_item_category == null || CropItemFragment.globale_crop_item_category.equalsIgnoreCase("")) {
                    if ((mStringFilterList.get(i).item_no.toUpperCase())
                            .contains(constraint.toString().toUpperCase()) || (mStringFilterList.get(i).item_desc.toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                } else {
                    if (((mStringFilterList.get(i).item_no.toUpperCase())
                            .contains(constraint.toString().toUpperCase()) || (mStringFilterList.get(i).item_desc.toUpperCase())
                            .contains(constraint.toString().toUpperCase())) && mStringFilterList.get(i).crop.equalsIgnoreCase(CropItemFragment.globale_crop_item_category)) {
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
        subTitleTextView.setText(dataModel.item_no + " (" + dataModel.crop + ") Pack Size : " + dataModel.fg_pack_size);
        add_qty_button.setOnClickListener(view -> {
            listener.onItemClick(dataModel.item_no, "Add");
        });
        remove_qty_button.setOnClickListener(view -> {
            listener.onItemClick(dataModel.item_no, "Remove");
        });
        sum_of_calculatedQty.setText(String.valueOf(dataModel.total_buy_qty));
        sum_of_calculatedQty.setOnClickListener(view -> {
            manualAmountEnput(dataModel);
        });
        return itemView;

    }

    void manualAmountEnput(CropItemMasterTable.CropItemMasterModel dataModel){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setTitle("Manual Packet " + dataModel.name);
        builder.setIcon(R.drawable.approve_order_icon);
        LinearLayout parentVertical = new LinearLayout(activity);
        LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        parentVertical.setLayoutParams(parentVerticalParams);
        parentVertical.setOrientation(LinearLayout.VERTICAL);
        TextView enterReson_tv = new TextView(activity);
        enterReson_tv.setText("Please Enter Packets");
        enterReson_tv.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        enterReson_tv.setPadding(10, 30, 0, 0);
        EditText approvebudget = new EditText(activity);
        approvebudget.setInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
        parentVertical.addView(enterReson_tv);
        parentVertical.addView(approvebudget);
        parentVertical.setPadding(40, 20, 40, 10);
        builder.setView(parentVertical);

        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                listener.onItemClick(dataModel.item_no, "ManualEnter_"+approvebudget.getText().toString());
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}
