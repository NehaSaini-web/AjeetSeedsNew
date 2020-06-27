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
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;

import java.util.List;

public class CropAdapter extends ArrayAdapter<CropMasterTable.CropMasterModel> {
    Context context;
    int resourceId;
    List<CropMasterTable.CropMasterModel> items;

    public CropAdapter(@NonNull Context context, int resourceId, List<CropMasterTable.CropMasterModel> items) {
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
        CropMasterTable.CropMasterModel crop = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textview1);
            name.setText(crop.description);
        return view;
    }

    @Nullable
    @Override
    public CropMasterTable.CropMasterModel getItem(int position) {
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
            CropMasterTable.CropMasterModel crop = (CropMasterTable.CropMasterModel) resultValue;
            return crop.description;
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