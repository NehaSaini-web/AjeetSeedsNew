package com.example.ajeetseeds.ui.inspection.create_inspection;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.R;

import java.util.List;

public class InspectionLineListAdapter extends BaseAdapter {
    private List<InspectionModel.Inspection_Line> listdata;
    private Activity activity;
    private String selected_lot;

    public InspectionLineListAdapter(Activity activity, List<InspectionModel.Inspection_Line> listdata, String selected_lot) {
        super();
        this.listdata = listdata;
        this.activity = activity;
        this.selected_lot = selected_lot;
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
        convertView = inflater.inflate(R.layout.inspection_listview, null);
        TextView tv_production_lot_no = convertView.findViewById(R.id.tv_production_lot_no);
        TextView tv_variety_no = convertView.findViewById(R.id.tv_variety_no);
        TextView tv_Status = convertView.findViewById(R.id.tv_Status);
        tv_production_lot_no.setText(listdata.get(position).production_lot_no);
        tv_variety_no.setText(listdata.get(position).variety_no);
        tv_Status.setText(getInspection(listdata.get(position)));
        if (selected_lot.equalsIgnoreCase(listdata.get(position).production_lot_no)) {
            convertView.setBackgroundColor(Color.parseColor("#D5F5E3"));
        }
        return convertView;

    }

    public String getInspection(InspectionModel.Inspection_Line inspection_line) {
        if (inspection_line.crop_code.equalsIgnoreCase("FCROP") && inspection_line.item_croptype.equalsIgnoreCase("Improved")) {
            if (inspection_line.inspection_1 == 0) {
                return "Pending";
            } else {
                if (inspection_line.inspection_4 == 0) {
                    return "INS 1 Done";
                } else {
                    if (inspection_line.inspection_qc == 0) {
                        return "INS 4 Done";
                    } else {
                        return "INS QC Done";
                    }
                }
            }
        } else {
            if (inspection_line.inspection_1 == 0) {
                return "Pending";
            } else {
                if (inspection_line.inspection_2 == 0) {
                    return "INS 1 Done";
                } else {
                    if (inspection_line.inspection_3 == 0) {
                        return "INS 2 Done";
                    } else {
                        if (inspection_line.inspection_4 == 0) {
                            return "INS 3 Done";
                        } else {
                            if (inspection_line.inspection_qc == 0) {
                                return "INS 4 Done";
                            } else {
                                return "INS QC Done";
                            }
                        }
                    }
                }
            }
        }

    }

}
