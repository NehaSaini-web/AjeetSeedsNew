package com.example.ajeetseeds.ui.eventManagement.createEvent;

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

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<DistrictMasterTable.DistrictMaster> {
    Context context;
    int resourceId;
    List<DistrictMasterTable.DistrictMaster> items;

    public DistrictAdapter(@NonNull Context context, int resourceId, List<DistrictMasterTable.DistrictMaster> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
        DistrictMasterTable.DistrictMaster district = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textview1);
            name.setText(district.name);
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
        return customerFilter;
    }

    private Filter customerFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            DistrictMasterTable.DistrictMaster district = (DistrictMasterTable.DistrictMaster) resultValue;
            return district.name;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = items;
            filterResults.count = items.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notifyDataSetChanged();
        }
    };
}