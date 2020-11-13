package com.example.ajeetseeds.golobalClass;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CustomDatePicker {
    Activity activity;
    public static DatePickerDialog datePickerDialog;

    public CustomDatePicker(Activity activity) {
        this.activity = activity;
    }

    public void showDatePickerDialog(TextInputEditText editTextDate) {
        //todo date formate shoud be MM-dd-yyyy
        if (datePickerDialog == null || !datePickerDialog.isShowing()) {
            String passdate = editTextDate.getText().toString();
            int day = 0, month = 0, year = 0;
            if (passdate != null && !passdate.equalsIgnoreCase("")) {
                String[] split = passdate.split("/");
                month = Integer.valueOf(split[0]) - 1;
                day = Integer.valueOf(split[1]);
                year = Integer.valueOf(split[2]);
            } else {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year_this, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    editTextDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year_this);
                }
            };

            datePickerDialog = new DatePickerDialog(activity,
                    dateSetListener, year, month, day);
            datePickerDialog.setCancelable(false);
            datePickerDialog.show();
        }

    }
}
