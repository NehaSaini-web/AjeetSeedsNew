package com.example.ajeetseeds.ui.inspection.inspectiontwo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ajeetseeds.Model.inspection.InspectionOneModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;

import java.util.List;

public class InspectionTwoLineListAdapter extends BaseAdapter {
    private List<InspectionOneModel.InspectionTwoLines> listdata;
    private Activity activity;

    public InspectionTwoLineListAdapter(Activity activity, List<InspectionOneModel.InspectionTwoLines> listdata) {
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
        convertView = inflater.inflate(R.layout.inspection_two_add_line_listview, null);
        TextView tv_date_of_inspection = convertView.findViewById(R.id.tv_date_of_inspection);
        TextView tv_crop_condition = convertView.findViewById(R.id.tv_crop_condition);
        TextView tv_net_area = convertView.findViewById(R.id.tv_net_area);
        TextView tv_suggestion_to_grower = convertView.findViewById(R.id.tv_suggestion_to_grower);
        tv_date_of_inspection.setText(DateUtilsCustome.getDateMMMDDYYYY(listdata.get(position).date_of_inspection));
        tv_crop_condition.setText(listdata.get(position).crop_condition);
        tv_net_area.setText(listdata.get(position).net_area);
        tv_suggestion_to_grower.setText(listdata.get(position).suggestion_to_grower);
        return convertView;

    }

}
