package com.example.ajeetseeds.ui.travel.addExpanse;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseModel;

import java.util.List;

public class TravelExpenseListAdapter extends BaseAdapter {
    List<TravelLineExpenseModel> listdata;
    Activity activity;

    public TravelExpenseListAdapter(Activity activity, List<TravelLineExpenseModel> listdata) {
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
        convertView = inflater.inflate(R.layout.expense_travel_listview, null);
        TextView tv_date = convertView.findViewById(R.id.tv_date);
        TextView tv_from_loc = convertView.findViewById(R.id.tv_from_loc);
        TextView tv_to_loc = convertView.findViewById(R.id.tv_to_loc);
        TextView tv_Departure = convertView.findViewById(R.id.tv_Departure);
        TextView tv_Arrival = convertView.findViewById(R.id.tv_Arrival);
        TextView tv_Fare = convertView.findViewById(R.id.tv_Fare);
        TextView tv_mode_of_travel = convertView.findViewById(R.id.tv_mode_of_travel);
        TextView tv_Loading = convertView.findViewById(R.id.tv_Loading);

        TextView tv_fuelVehicleExpanse = convertView.findViewById(R.id.tv_fuelVehicleExpanse);
        TextView tv_Daily_Express = convertView.findViewById(R.id.tv_Daily_Express);
        TextView tv_VehicleRepairing = convertView.findViewById(R.id.tv_VehicleRepairing);
        TextView tv_local_convance = convertView.findViewById(R.id.tv_local_convance);
        TextView tv_Other_Expenses = convertView.findViewById(R.id.tv_Other_Expenses);
        TextView tv_Total_Expanse = convertView.findViewById(R.id.tv_Total_Expanse);

        tv_date.setText(listdata.get(position).date);
        try {
            CityMasterTable cityMasterTable = new CityMasterTable(activity);
            cityMasterTable.open();
            listdata.get(position).from_loc_name = cityMasterTable.fetchCityName(listdata.get(position).from_loc);
            listdata.get(position).to_loc_name = cityMasterTable.fetchCityName(listdata.get(position).to_loc);
            cityMasterTable.close();
        } catch (Exception e) {

        } finally {
            tv_from_loc.setText(listdata.get(position).from_loc_name);
            tv_to_loc.setText(listdata.get(position).to_loc_name);
        }
        tv_Departure.setText(listdata.get(position).departure);
        tv_Arrival.setText(listdata.get(position).arrival);
        tv_Fare.setText(listdata.get(position).fare);
        tv_mode_of_travel.setText(listdata.get(position).mode_of_travel);
        tv_Loading.setText(listdata.get(position).loading_in_any);

        tv_fuelVehicleExpanse.setText(listdata.get(position).fuel_vehicle_expance);
        tv_Daily_Express.setText(listdata.get(position).daily_express);
        tv_VehicleRepairing.setText(listdata.get(position).vehicle_repairing);
        tv_local_convance.setText(listdata.get(position).local_convance);
        tv_Other_Expenses.setText(listdata.get(position).other_expenses);
        tv_Total_Expanse.setText(listdata.get(position).total_amount_calulated);
        return convertView;

    }

}
