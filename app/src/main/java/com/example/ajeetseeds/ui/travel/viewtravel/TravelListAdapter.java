package com.example.ajeetseeds.ui.travel.viewtravel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.TravelHeaderTable;

import java.util.List;

public class TravelListAdapter extends BaseAdapter {
    List<TravelHeaderTable.TravelHeaderModel> listdata;
    Activity activity;

    public TravelListAdapter(Activity activity, List<TravelHeaderTable.TravelHeaderModel> listdata) {
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
        TextView tv_app_budget_title = convertView.findViewById(R.id.tv_app_budget_title);
        TextView tv_app_budget = convertView.findViewById(R.id.tv_app_budget);
        if(listdata.get(position).status.equalsIgnoreCase("PENDING")){
            tv_app_budget_title.setVisibility(View.GONE);
            tv_app_budget.setVisibility(View.GONE);
        }else{
            tv_app_budget_title.setVisibility(View.VISIBLE);
            tv_app_budget.setVisibility(View.VISIBLE);
            tv_app_budget.setText(listdata.get(position).approve_budget);
        }

        ImageView approve_order = convertView.findViewById(R.id.approve_order);
        ImageView reject_order = convertView.findViewById(R.id.reject_order);
        approve_order.setVisibility(View.GONE);
        reject_order.setVisibility(View.GONE);

        travel_code.setText(listdata.get(position).travelcode.equalsIgnoreCase("0") ? "Local-" + listdata.get(position).android_travelcode : listdata.get(position).travelcode);
        try {
            if(listdata.get(position).from_loc_name==null ||listdata.get(position).from_loc_name.equalsIgnoreCase("")) {
                DistrictMasterTable districtMasterTable = new DistrictMasterTable(activity);
                districtMasterTable.open();
                listdata.get(position).from_loc_name = districtMasterTable.fetchDistrictName(listdata.get(position).from_loc);
                listdata.get(position).to_loc_name = districtMasterTable.fetchDistrictName(listdata.get(position).to_loc);
                districtMasterTable.close();
            }
        } catch (Exception e) {
        }
        tv_from_loc.setText(listdata.get(position).from_loc_name);
        tv_to_loc.setText(listdata.get(position).to_loc_name);
        tv_start_date.setText(listdata.get(position).start_date);
        tv_end_date.setText(listdata.get(position).end_date);
        tv_Created.setText(listdata.get(position).created_by);
        tv_travel_reson.setText(listdata.get(position).travel_reson);
        tv_expense_budget.setText(listdata.get(position).expense_budget);
        tv_travel_status.setText(listdata.get(position).created_on != null ? (DateUtilsCustome.getDate_Time(listdata.get(position).created_on) + " " + listdata.get(position).status) : listdata.get(position).status);
        tv_travel_status.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        return convertView;

    }

}
