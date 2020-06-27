package com.example.ajeetseeds.ui.dailyActivity.addLine;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends ArrayAdapter<DistrictMasterTable.DistrictMaster> {
    private Context context;
    private int resourceId;
    private  List<DistrictMasterTable.DistrictMaster> items, tempItems, suggestions;

    public DistrictAdapter(@NonNull Context context, int resourceId, List<DistrictMasterTable.DistrictMaster> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            DistrictMasterTable.DistrictMaster district = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textview1);
            name.setText(district.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public DistrictMasterTable.DistrictMaster getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return districtFilter;
    }

    private Filter districtFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            DistrictMasterTable.DistrictMaster district = (DistrictMasterTable.DistrictMaster) resultValue;
            return district.name;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (DistrictMasterTable.DistrictMaster district : tempItems) {
                    if (district.name.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(district);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<DistrictMasterTable.DistrictMaster> tempValues = (ArrayList<DistrictMasterTable.DistrictMaster>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (DistrictMasterTable.DistrictMaster districtObj : tempValues) {
                    add(districtObj);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}