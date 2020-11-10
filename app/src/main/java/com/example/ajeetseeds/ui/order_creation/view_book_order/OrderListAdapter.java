package com.example.ajeetseeds.ui.order_creation.view_book_order;

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
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    List<OrderBookHeader.OrderBookHeaderModel> listdata;
    Activity activity;

    public OrderListAdapter(Activity activity, List<OrderBookHeader.OrderBookHeaderModel> listdata) {
        super();
        this.listdata = listdata;
        this.activity = activity;
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
        TextView order_Status_tv = convertView.findViewById(R.id.order_Status_tv);

        ImageView approve_order = convertView.findViewById(R.id.approve_order);
        ImageView reject_order = convertView.findViewById(R.id.reject_order);
        approve_order.setVisibility(View.GONE);
        reject_order.setVisibility(View.GONE);
        order_Status_tv.setVisibility(View.VISIBLE);
        order_Status_tv.setText(listdata.get(position).order_status);
        order_no.setText(listdata.get(position).order_no.equalsIgnoreCase("0")?"Local-"+listdata.get(position).android_order_no:listdata.get(position).order_no);
        int sumOfQty = 0;
        try {
            OrderBookLine orderBookLine = new OrderBookLine(activity);
            orderBookLine.open();
            List<OrderBookLine.OrderBookLineModel> orderlineList = orderBookLine.fetch(listdata.get(position).android_order_no);
            orderBookLine.close();
            for (OrderBookLine.OrderBookLineModel orderItem : orderlineList) {
                sumOfQty += Integer.parseInt(orderItem.qty);
            }
        } catch (Exception e) {
        }
        String customer_name = "";
        try {
            CustomerMasterTable customerMasterTable = new CustomerMasterTable(activity);
            customerMasterTable.open();
            customer_name = customerMasterTable.fetchCustomerName(listdata.get(position).customer_no);
            customerMasterTable.close();
        } catch (Exception e) {
        }
        order_detail.setText("Packet : " + sumOfQty+((customer_name==null || customer_name.equalsIgnoreCase(""))?"":" , Cust. : "+customer_name));
        try {
            created_on.setText(DateUtilsCustome.getDate_Time(listdata.get(position).updated_on));
        } catch (Exception e) {
        }
        if ("".contains("no_image_placeholder")) {
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(convertView)
                .load((StaticDataForApp.globalurl + listdata.get(position).image_url))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.gray3)
                .into(itemImage);
        return convertView;

    }

}
