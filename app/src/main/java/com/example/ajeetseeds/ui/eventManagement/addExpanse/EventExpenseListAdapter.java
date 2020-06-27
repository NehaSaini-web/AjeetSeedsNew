package com.example.ajeetseeds.ui.eventManagement.addExpanse;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.event.EventManagementExpenseLineTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.event.EventTypeMaster;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;

import java.util.List;

public class EventExpenseListAdapter extends BaseAdapter {
    List<EventManagementExpenseLineTable.EventManagementExpenseLineModel> listdata;
    Activity activity;

    public EventExpenseListAdapter(Activity activity, List<EventManagementExpenseLineTable.EventManagementExpenseLineModel> listdata) {
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
        convertView = inflater.inflate(R.layout.expense_event_listview, null);
        TextView expenseType = convertView.findViewById(R.id.expenseType);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView rate = convertView.findViewById(R.id.rate);
        TextView amount = convertView.findViewById(R.id.amount);
        if (listdata.get(position).expense_type_name == null || listdata.get(position).expense_type_name.equalsIgnoreCase("")) {
            EventTypeMaster eventTypeMaster = new EventTypeMaster(activity);
            eventTypeMaster.open();
            listdata.get(position).expense_type_name = eventTypeMaster.fetchNameById(listdata.get(position).expense_type);
            eventTypeMaster.close();
        }
        expenseType.setText(listdata.get(position).expense_type_name);
        quantity.setText(listdata.get(position).quantity);
        rate.setText(listdata.get(position).rate_unit_cost);
        amount.setText(listdata.get(position).amount);
        return convertView;

    }

}
