package com.example.ajeetseeds.ui.dailyActivity.viewDailyActivity;

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
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;

import java.util.List;

public class DailyActivityListAdapter extends BaseAdapter {
    List<DailyActivityHeader.DailyActivityHeaderModel> listdata;
    Activity activity;

    public DailyActivityListAdapter(Activity activity, List<DailyActivityHeader.DailyActivityHeaderModel> listdata) {
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
        convertView = inflater.inflate(R.layout.daily_activity_view_listview, null);
        TextView activity_no = convertView.findViewById(R.id.activity_no);
        TextView contact_no_tv = convertView.findViewById(R.id.contact_no_tv);
        TextView created_on = convertView.findViewById(R.id.created_on);
        TextView order_collected_tv=convertView.findViewById(R.id.order_collected_tv);
        TextView paymentCollected=convertView.findViewById(R.id.paymentCollected);
        order_collected_tv.setText("Order Collected : "+listdata.get(position).order_collected);
        paymentCollected.setText(listdata.get(position).payment_collected);
        activity_no.setText(listdata.get(position).activity_no);
        contact_no_tv.setText(listdata.get(position).contact_no +(listdata.get(position).contact_no1.equalsIgnoreCase("0")?"":" , "+listdata.get(position).contact_no1) );
        try {
            created_on.setText(DateUtilsCustome.getDate_Time(listdata.get(position).updated_on));
        } catch (Exception e) {
        }
        return convertView;

    }

}
