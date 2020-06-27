package com.example.ajeetseeds.ui.dailyActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ajeetseeds.Model.dailyActivity.DailyActivityModel;
import com.example.ajeetseeds.R;

import java.util.ArrayList;
import java.util.List;

public class DailyActivityAdapter extends BaseAdapter {
    List<DailyActivityModel.DailyActivityLines> listdata;
    Activity activity;

    public DailyActivityAdapter(Activity activity, List<DailyActivityModel.DailyActivityLines> listdata) {
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
        convertView = inflater.inflate(R.layout.daily_activity_line_listview, null);
        TextView tv_farmer_name = convertView.findViewById(R.id.tv_farmer_name);
        TextView tv_district = convertView.findViewById(R.id.tv_district);
        TextView tv_village = convertView.findViewById(R.id.tv_village);

        TextView tv_ajeet_crop_and_verity = convertView.findViewById(R.id.tv_ajeet_crop_and_verity);
        TextView tv_ajeet_crop_age = convertView.findViewById(R.id.tv_ajeet_crop_age);
        TextView tv_ajeet_fruits_per = convertView.findViewById(R.id.tv_ajeet_fruits_per);
        TextView tv_ajeet_pest = convertView.findViewById(R.id.tv_ajeet_pest);
        TextView tv_ajeet_disease = convertView.findViewById(R.id.tv_ajeet_disease);

        TextView tv_check_crop_and_verity = convertView.findViewById(R.id.tv_check_crop_and_verity);
        TextView tv_check_crop_age = convertView.findViewById(R.id.tv_check_crop_age);
        TextView tv_check_fruits_per = convertView.findViewById(R.id.tv_check_fruits_per);
        TextView tv_check_pest = convertView.findViewById(R.id.tv_check_pest);
        TextView tv_check_disease = convertView.findViewById(R.id.tv_check_disease);

        tv_farmer_name.setText(listdata.get(position).farmer_name);
        tv_district.setText(listdata.get(position).district);
        tv_village.setText(listdata.get(position).village);

        tv_ajeet_crop_and_verity.setText(listdata.get(position).ajeet_crop_and_verity);
        tv_ajeet_crop_age.setText(listdata.get(position).ajeet_crop_age);
        tv_ajeet_fruits_per.setText(listdata.get(position).ajeet_fruits_per);
        tv_ajeet_pest.setText(listdata.get(position).ajeet_pest);
        tv_ajeet_disease.setText(listdata.get(position).ajeet_disease);

        tv_check_crop_and_verity.setText(listdata.get(position).check_crop_and_variety);
        tv_check_crop_age.setText(listdata.get(position).check_crop_age);
        tv_check_fruits_per.setText(listdata.get(position).check_fruits_per);
        tv_check_pest.setText(listdata.get(position).check_pest);
        tv_check_disease.setText(listdata.get(position).check_disease);

        //todo manage arrows of list
        ImageView ajeetArrow = convertView.findViewById(R.id.ajeetArrow);
        TableRow ajeetTableRowSection1 = convertView.findViewById(R.id.ajeetTableRowSection1);
        TableRow ajeetTableRowSection2 = convertView.findViewById(R.id.ajeetTableRowSection2);
        ajeetArrow.setOnClickListener(view -> {
            if (ajeetTableRowSection1.getVisibility() == View.GONE) {
                ajeetTableRowSection1.setVisibility(View.VISIBLE);
                ajeetTableRowSection2.setVisibility(View.VISIBLE);
                ajeetArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                ajeetTableRowSection1.setVisibility(View.GONE);
                ajeetTableRowSection2.setVisibility(View.GONE);
                ajeetArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        ImageView checkArrow = convertView.findViewById(R.id.check_arraw);
        TableRow checkTableRowSection1 = convertView.findViewById(R.id.checkTableRowSection1);
        TableRow checkTableRowSection2 = convertView.findViewById(R.id.checkTableRowSection2);
        checkArrow.setOnClickListener(view -> {
            if (checkTableRowSection1.getVisibility() == View.GONE) {
                checkTableRowSection1.setVisibility(View.VISIBLE);
                checkTableRowSection2.setVisibility(View.VISIBLE);
                checkArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                checkTableRowSection1.setVisibility(View.GONE);
                checkTableRowSection2.setVisibility(View.GONE);
                checkArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });
        return convertView;

    }

}
