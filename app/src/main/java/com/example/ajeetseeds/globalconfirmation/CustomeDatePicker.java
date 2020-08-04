package com.example.ajeetseeds.globalconfirmation;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ajeetseeds.R;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CustomeDatePicker {
    Activity activity;
    public static boolean datedialog;

    public CustomeDatePicker(Activity activity) {
        this.activity = activity;
    }

    public void displayDate(TextInputEditText edit_date) {
        this.datedialog = true;
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Date");
        builder.setIcon(R.drawable.ic_baseline_calendar_today_24);
        builder.setCancelable(false);
        builder.setCancelable(false);
        // set the custom layout
        final View customLayout = activity.getLayoutInflater().inflate(R.layout.date_picker_view, null);
        MaterialCalendarView calendarView = customLayout.findViewById(R.id.calendarView);
        TextView tv_selected_date = customLayout.findViewById(R.id.tv_selected_date);
        Button btn_cancel = customLayout.findViewById(R.id.btn_cancel);
        Button btn_confirm = customLayout.findViewById(R.id.btn_confirm);
        builder.setView(customLayout);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        // add a button
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            tv_selected_date.setText(date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay());
        });
        btn_cancel.setOnClickListener(view -> {
            datedialog = false;
            dialog.dismiss();
        });
        btn_confirm.setOnClickListener(view -> {
            edit_date.setText(tv_selected_date.getText());
            edit_date.setError(null);
            datedialog = false;
            dialog.dismiss();
        });
        dialog.show();
    }

}
