package com.example.ajeetseeds.ui.travel.approveTravel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ajeetseeds.Model.travel.SyncTravelDetailModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;

import java.util.List;

public class TravelListAdapter extends BaseAdapter {
    List<SyncTravelDetailModel> listdata;
    Activity activity;
    public interface OnItemClickListener {
        void onItemClick(SyncTravelDetailModel selectedeventData, int position, String flag);
    }
    private final OnItemClickListener listener;
    public TravelListAdapter(Activity activity, List<SyncTravelDetailModel> listdata, OnItemClickListener listener) {
        super();
        this.listdata = listdata;
        this.activity = activity;
        this.listener=listener;
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
        convertView = inflater.inflate(R.layout.view_travel_listview, null);
        TextView travel_code = convertView.findViewById(R.id.travel_code);
        TextView tv_from_loc = convertView.findViewById(R.id.tv_from_loc);
        TextView tv_to_loc = convertView.findViewById(R.id.tv_to_loc);
        TextView tv_start_date = convertView.findViewById(R.id.tv_start_date);
        TextView tv_end_date = convertView.findViewById(R.id.tv_end_date);
        TextView tv_travel_reson = convertView.findViewById(R.id.tv_travel_reson);
        TextView tv_expense_budget = convertView.findViewById(R.id.tv_expense_budget);
        TextView tv_travel_status = convertView.findViewById(R.id.tv_travel_status);
        TextView tv_Created = convertView.findViewById(R.id.tv_Created);

        ImageView approve_order = convertView.findViewById(R.id.approve_order);
        ImageView reject_order = convertView.findViewById(R.id.reject_order);
        approve_order.setVisibility(View.VISIBLE);
        reject_order.setVisibility(View.VISIBLE);

        travel_code.setText(listdata.get(position).travelcode);
        try {
            DistrictMasterTable districtMasterTable = new DistrictMasterTable(activity);
            districtMasterTable.open();
            listdata.get(position).from_loc_name = districtMasterTable.fetchDistrictName(listdata.get(position).from_loc);
            listdata.get(position).to_loc_name = districtMasterTable.fetchDistrictName(listdata.get(position).to_loc);
            districtMasterTable.close();
        } catch (Exception e) {
        }
        tv_from_loc.setText(listdata.get(position).from_loc_name);
        tv_to_loc.setText(listdata.get(position).to_loc_name);
        tv_start_date.setText(listdata.get(position).start_date);
        tv_end_date.setText(listdata.get(position).end_date);
        tv_Created.setText(listdata.get(position).created_by);
        tv_travel_reson.setText(listdata.get(position).travel_reson);
        tv_expense_budget.setText(listdata.get(position).expense_budget);
        tv_travel_status.setText(listdata.get(position).created_on != null ? (DateUtilsCustome.getDate_Time(listdata.get(position).created_on) + " " + listdata.get(position).STATUS) : listdata.get(position).STATUS);
        approve_order.setOnClickListener(view -> {
            listener.onItemClick(listdata.get(position), position, "approve");
        });
        reject_order.setOnClickListener(view -> {
            listener.onItemClick(listdata.get(position), position, "reject");
        });
        return convertView;

    }

}
