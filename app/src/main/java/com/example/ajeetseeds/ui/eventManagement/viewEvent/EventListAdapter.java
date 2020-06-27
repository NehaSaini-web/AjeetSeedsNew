package com.example.ajeetseeds.ui.eventManagement.viewEvent;

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
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;

import java.util.List;

public class EventListAdapter extends BaseAdapter {
    List<EventManagementTable.EventManagemantModel> listdata;
    Activity activity;

    public EventListAdapter(Activity activity, List<EventManagementTable.EventManagemantModel> listdata) {
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
        convertView = inflater.inflate(R.layout.view_event_listview, null);
        TextView event_code = convertView.findViewById(R.id.event_code);
        TextView event_date = convertView.findViewById(R.id.event_date);
        TextView event_desc = convertView.findViewById(R.id.event_desc);
        TextView event_status = convertView.findViewById(R.id.event_status);
        TextView event_type = convertView.findViewById(R.id.event_type);
        TextView event_budget = convertView.findViewById(R.id.event_budget);
        TextView crop_name = convertView.findViewById(R.id.crop_name);
        TextView variety_name = convertView.findViewById(R.id.variety_name);
        TextView state_name = convertView.findViewById(R.id.state_name);
        TextView district_name = convertView.findViewById(R.id.district_name);
        TextView taluka_name = convertView.findViewById(R.id.taluka_name);
        TextView village = convertView.findViewById(R.id.village);

        ImageView approve_order = convertView.findViewById(R.id.approve_order);
        ImageView reject_order = convertView.findViewById(R.id.reject_order);
        approve_order.setVisibility(View.GONE);
        reject_order.setVisibility(View.GONE);
        event_code.setText(listdata.get(position).event_code.equalsIgnoreCase("0")?"Local-"+listdata.get(position).android_event_code:listdata.get(position).event_code);
        event_desc.setText(listdata.get(position).event_desc);
        event_type.setText(listdata.get(position).event_type);
        event_budget.setText(listdata.get(position).event_budget);
        event_date.setText(DateUtilsCustome.getDateOnly(listdata.get(position).event_date));
        event_status.setText(listdata.get(position).created_on!=null?(DateUtilsCustome.getDate_Time(listdata.get(position).created_on) + " " + listdata.get(position).status):listdata.get(position).status);
        event_status.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        try {
            CropMasterTable cropMasterTable = new CropMasterTable(activity);
            cropMasterTable.open();
            listdata.get(position).crop_name = cropMasterTable.getCropName(listdata.get(position).crop);
            cropMasterTable.close();
            CropItemMasterTable cropItemMasterTable = new CropItemMasterTable(activity);
            cropItemMasterTable.open();
            listdata.get(position).variety_name = cropItemMasterTable.getCropItemname(listdata.get(position).variety);
            cropItemMasterTable.close();
            StateMasterTable stateMasterTable = new StateMasterTable(activity);
            stateMasterTable.open();
            listdata.get(position).state_name = stateMasterTable.getStateName(listdata.get(position).state);
            stateMasterTable.close();
            DistrictMasterTable districtMasterTable = new DistrictMasterTable(activity);
            districtMasterTable.open();
            listdata.get(position).district_name = districtMasterTable.fetchDistrictName(listdata.get(position).district);
            districtMasterTable.close();
            TalukaMasterTable talukaMasterTable = new TalukaMasterTable(activity);
            talukaMasterTable.open();
            listdata.get(position).taluka_name = talukaMasterTable.getTalukaName(listdata.get(position).taluka);
            talukaMasterTable.close();
        } catch (Exception e) {
        } finally {
            crop_name.setText(listdata.get(position).crop_name);
            variety_name.setText(listdata.get(position).variety_name);
            state_name.setText(listdata.get(position).state_name);
            district_name.setText(listdata.get(position).district_name);
            taluka_name.setText(listdata.get(position).taluka_name);
        }
        village.setText(listdata.get(position).village);
        return convertView;

    }

}
