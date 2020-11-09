package com.example.ajeetseeds.golobalClass;

import android.app.Activity;
import android.app.TimePickerDialog;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CustomTimePicker {
    Activity activity;
   public static TimePickerDialog timePickerDialog;

    public CustomTimePicker(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(TextInputEditText textInputEditText) {
        //todo pass time formate shoud be HH:mm AM
        if (timePickerDialog == null || !timePickerDialog.isShowing()) {
            String passTime=textInputEditText.getText().toString();
            int mHour=0,mMinute=0;
            if(passTime!=null && !passTime.equalsIgnoreCase("")){
                try {
                    mHour = Integer.parseInt(passTime.split(":")[0]);
                    mMinute = Integer.parseInt(passTime.split(":")[1].split(" ")[0]);
                }catch (Exception e){
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                }
            }else{
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
            }
            // Launch Time Picker Dialog
            timePickerDialog = new TimePickerDialog(activity,
                    (timePicker, hourOfDay, minute) -> {
                        textInputEditText.setText(hourOfDay + ":" + minute);
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
