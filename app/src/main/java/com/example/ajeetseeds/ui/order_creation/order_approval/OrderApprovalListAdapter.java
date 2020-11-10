package com.example.ajeetseeds.ui.order_creation.order_approval;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

import java.util.List;

public class OrderApprovalListAdapter extends BaseAdapter {
    List<OrderApprovalModel> listdata;
    Activity activity;

    public interface OnItemClickListener {
        void onItemClick(List<OrderApprovalModel.OrderLineModel> selectedOrderLine, int position, String flag);
    }

    private final OnItemClickListener listener;

    public OrderApprovalListAdapter(Activity activity, List<OrderApprovalModel> listdata, OnItemClickListener listener) {
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
        convertView = inflater.inflate(R.layout.order_approval_listview, null);
        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        TextView order_no = convertView.findViewById(R.id.order_no);
        TextView order_detail = convertView.findViewById(R.id.order_detail);
        TextView created_on = convertView.findViewById(R.id.created_on);

        ImageView approve_order = convertView.findViewById(R.id.approve_order);
        ImageView reject_order = convertView.findViewById(R.id.reject_order);

        order_no.setText(listdata.get(position).order_no);
        String customer_name = "";
        try {
            CustomerMasterTable customerMasterTable = new CustomerMasterTable(activity);
            customerMasterTable.open();
            customer_name = customerMasterTable.fetchCustomerName(listdata.get(position).customer_no);
            customerMasterTable.close();
        } catch (Exception e) {
        }
        order_detail.setText("Packet : " +  listdata.get(position).sum_of_qty+((customer_name==null || customer_name.equalsIgnoreCase(""))?"":" , Cust. : "+customer_name)
        );
        try {
            created_on.setText(DateUtilsCustome.getDate_Time(listdata.get(position).updated_on));
        } catch (Exception e) {
        }
        if (listdata.get(position).orderline.get(0).image_url != null && listdata.get(position).orderline.get(0).image_url.contains("no_image_placeholder")) {
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(convertView)
                .load(listdata.get(position).orderline.size() > 0 ? (StaticDataForApp.globalurl + listdata.get(position).orderline.get(0).image_url) : "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.gray3)
                .into(itemImage);

        approve_order.setOnClickListener(view -> {
            listener.onItemClick(listdata.get(position).orderline, position, "approve_order");
        });
        reject_order.setOnClickListener(view -> {
            listener.onItemClick(listdata.get(position).orderline, position, "reject_order");
        });
        convertView.setOnClickListener(view -> {
            listener.onItemClick(listdata.get(position).orderline, position, "ClickOnItem");
        });
        return convertView;

    }

}
