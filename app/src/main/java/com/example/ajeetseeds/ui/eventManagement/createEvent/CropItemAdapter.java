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
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;

import java.util.List;

public class CropItemAdapter extends ArrayAdapter<CropItemMasterTable.CropItemMasterModel> {
    Context context;
    int resourceId;
    List<CropItemMasterTable.CropItemMasterModel> items;

    public CropItemAdapter(@NonNull Context context, int resourceId, List<CropItemMasterTable.CropItemMasterModel> items) {
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
        CropItemMasterTable.CropItemMasterModel cropitem = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textview1);
            name.setText(cropitem.name+" ("+cropitem.crop+")");
        return view;
    }

    @Nullable
    @Override
    public CropItemMasterTable.CropItemMasterModel getItem(int position) {
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
            CropItemMasterTable.CropItemMasterModel cropitem = (CropItemMasterTable.CropItemMasterModel) resultValue;
            return cropitem.name +" ("+cropitem.crop+")";
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