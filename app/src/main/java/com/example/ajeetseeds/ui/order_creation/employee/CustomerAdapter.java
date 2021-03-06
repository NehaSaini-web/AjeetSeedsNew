package com.example.ajeetseeds.ui.order_creation.employee;

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
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ArrayAdapter<CustomerMasterTable.CustomerMasterModel> {
    Context context;
    int resourceId;
    List<CustomerMasterTable.CustomerMasterModel> items, tempItems, suggestions;

    public CustomerAdapter(@NonNull Context context, int resourceId, List<CustomerMasterTable.CustomerMasterModel> items) {
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
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
        }
        CustomerMasterTable.CustomerMasterModel customer = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.textview1);
        name.setText(customer.name +" ( "+customer.no+" )");
        return view;
    }

    @Nullable
    @Override
    public CustomerMasterTable.CustomerMasterModel getItem(int position) {
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
            CustomerMasterTable.CustomerMasterModel customer = (CustomerMasterTable.CustomerMasterModel) resultValue;
            return customer.name  +" ( "+customer.no+" )";
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (CustomerMasterTable.CustomerMasterModel itemobject : tempItems) {
                    if (itemobject.name.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(itemobject);
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
            ArrayList<CustomerMasterTable.CustomerMasterModel> tempValues = (ArrayList<CustomerMasterTable.CustomerMasterModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (CustomerMasterTable.CustomerMasterModel districtObj : tempValues) {
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