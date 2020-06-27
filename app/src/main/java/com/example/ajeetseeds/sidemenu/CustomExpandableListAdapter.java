package com.example.ajeetseeds.sidemenu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajeetseeds.R;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private Map<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       Map<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        bindGroupImage(listTitle, (ImageView) convertView.findViewById(R.id.gropIcon));
        ImageView arrow_up_down = (ImageView) convertView.findViewById(R.id.arrow_up_down);
        //todo group arrow set
        if (isExpanded) {
            arrow_up_down.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            arrow_up_down.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
        }
        return convertView;
    }

    /* access modifiers changed from: 0000 */
    public void bindGroupImage(String listTitle, ImageView gropIcon) {
        if (listTitle.contentEquals("Daily Activity")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_transfer_within_a_station_black_24dp));
        } else if (listTitle.contentEquals("Order Creation")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_multiline_chart_black_24dp));
        } else if (listTitle.contentEquals("Event")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_supervisor_account_black_24dp));
        } else if (listTitle.contentEquals("TA/DA Bill")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_directions_bike_black_24dp));
        } else if (listTitle.contentEquals("Manifest")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_local_shipping_black_24dp));
        } else if (listTitle.contentEquals("Reports")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_pie_chart_black_24dp));
        } else if (listTitle.contentEquals("App Setting")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_phonelink_setup_black_24dp));
        }
        else if (listTitle.contentEquals("Inspection")) {
            gropIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_verified_user_black_24dp));
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}