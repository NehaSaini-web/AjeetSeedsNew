package com.example.ajeetseeds.ui.travel.addExpanse;

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
import com.example.ajeetseeds.sqlLite.event.EventTypeMaster;

import java.util.List;

public class EventTypeChieldAdapter extends ArrayAdapter<EventTypeMaster.EventTypeMasterModel> {
    Context context;
    int resourceId;
    List<EventTypeMaster.EventTypeMasterModel> items;

    public EventTypeChieldAdapter(@NonNull Context context, int resourceId, List<EventTypeMaster.EventTypeMasterModel> items) {
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
        EventTypeMaster.EventTypeMasterModel event_type = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textview1);
            name.setText(event_type.event_type);
        return view;
    }

    @Nullable
    @Override
    public EventTypeMaster.EventTypeMasterModel getItem(int position) {
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
            EventTypeMaster.EventTypeMasterModel eventdata = (EventTypeMaster.EventTypeMasterModel) resultValue;
            return eventdata.event_type;
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