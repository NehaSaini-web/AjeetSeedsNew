package com.example.ajeetseeds.ui.inspection.create_inspection;

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

import com.example.ajeetseeds.Model.inspection.LocationModel;
import com.example.ajeetseeds.R;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<LocationModel> {
    Context context;
    int resourceId;
    List<LocationModel> items;

    public LocationAdapter(@NonNull Context context, int resourceId, List<LocationModel> items) {
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
        LocationModel data = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.textview1);
        name.setText(data.location_name+" ("+data.location_code+")");
        return view;
    }

    @Nullable
    @Override
    public LocationModel getItem(int position) {
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
            LocationModel data = (LocationModel) resultValue;
            return data.location_name+" ("+data.location_code+")";
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
