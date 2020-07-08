package com.example.ajeetseeds.ui.inspection;

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
import java.util.ArrayList;
import java.util.List;

public class CropConditionAdapter extends ArrayAdapter<String>{
        private Context context;
        private int resourceId;
        private List<String> items, tempItems, suggestions;

        public CropConditionAdapter(@NonNull Context context, int resourceId, List<String> items) {
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
                String district = getItem(position);
                TextView name = (TextView) view.findViewById(R.id.textview1);
                name.setText(district);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        @Nullable
        @Override
        public String getItem(int position) {
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
               String district = (String) resultValue;
                return district;
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence != null) {
                    suggestions.clear();
                    for (String district : tempItems) {
                        if (district.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
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
                ArrayList<String> tempValues = (ArrayList<String>) filterResults.values;
                if (filterResults != null && filterResults.count > 0) {
                    clear();
                    for (String districtObj : tempValues) {
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
