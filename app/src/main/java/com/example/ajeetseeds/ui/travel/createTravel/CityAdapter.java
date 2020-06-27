package com.example.ajeetseeds.ui.travel.createTravel;

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
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends ArrayAdapter<CityMasterTable.CityMasterModel> {
    Context context;
    int resourceId;
    List<CityMasterTable.CityMasterModel> items, tempItems, suggestions;

    public CityAdapter(@NonNull Context context, int resourceId, List<CityMasterTable.CityMasterModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<CityMasterTable.CityMasterModel>(items);
        suggestions = new ArrayList<CityMasterTable.CityMasterModel>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
        }
        CityMasterTable.CityMasterModel event_type = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.textview1);
        name.setText(event_type.name + " (" + event_type.code + ")");
        return view;
    }

    @Nullable
    @Override
    public CityMasterTable.CityMasterModel getItem(int position) {
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
            CityMasterTable.CityMasterModel eventdata = (CityMasterTable.CityMasterModel) resultValue;
            return eventdata.name;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CityMasterTable.CityMasterModel item : tempItems) {
                    if (item.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CityMasterTable.CityMasterModel> filterList = (ArrayList<CityMasterTable.CityMasterModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CityMasterTable.CityMasterModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}