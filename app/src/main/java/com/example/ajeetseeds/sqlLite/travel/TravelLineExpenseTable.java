package com.example.ajeetseeds.sqlLite.travel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TravelLineExpenseTable {
    // Table Name
    public static final String TABLE_NAME = "travel_line_expanse";
    // Table columns
    public static final String android_travelcode = "android_travelcode";
    public static final String travelcode = "travelcode";
    public static final String line_no = "line_no";
    public static final String date = "date";
    public static final String from_loc = "from_loc";
    public static final String to_loc = "to_loc";
    public static final String departure = "departure";
    public static final String arrival = "arrival";
    public static final String fare = "fare";
    public static final String mode_of_travel = "mode_of_travel";
    public static final String loading_in_any = "loading_in_any";
    public static final String distance_km = "distance_km";
    public static final String fuel_vehicle_expance = "fuel_vehicle_expance";
    public static final String daily_express = "daily_express";
    public static final String vehicle_repairing = "vehicle_repairing";
    public static final String local_convance = "local_convance";
    public static final String other_expenses = "other_expenses";
    public static final String total_amount_calulated = "total_amount_calulated";
    public static final String created_on = "created_on";
    public static final String lineSend = "lineSend";

    public static final String mod_city = "mod_city";
    public static final String mod_lodging = "mod_lodging";
    public static final String mod_da_half = "mod_da_half";
    public static final String mode_da_full = "mode_da_full";
    public static final String mod_ope_max = "mod_ope_max";
    public static final String user_grade = "user_grade";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            android_travelcode + " TEXT NOT NULL, " +
            travelcode + " TEXT  NOT NULL," +
            line_no + " TEXT  NOT NULL," +
            date + " TEXT  NOT NULL," +
            from_loc + " TEXT  NOT NULL," +
            to_loc + " TEXT  NOT NULL," +
            departure + " TEXT  NOT NULL," +
            arrival + " TEXT  NOT NULL," +
            fare + " TEXT  NOT NULL," +
            mode_of_travel + " TEXT  NOT NULL," +
            loading_in_any + " TEXT  NOT NULL," +
            distance_km + " TEXT  NOT NULL," +
            fuel_vehicle_expance + " TEXT  NOT NULL," +
            daily_express + " TEXT  NOT NULL," +
            vehicle_repairing + " TEXT  NOT NULL," +
            local_convance + " TEXT  NOT NULL," +
            other_expenses + " TEXT  NOT NULL," +
            total_amount_calulated + " TEXT  NOT NULL," +
            created_on + " TEXT NULL," +
            lineSend + " TEXT NULL," +

            mod_city + " TEXT  NULL," +
            mod_lodging + " TEXT  NULL," +
            mod_da_half + " TEXT  NULL," +
            mode_da_full + " TEXT  NULL," +
            mod_ope_max + " TEXT NULL," +
            user_grade + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public TravelLineExpenseTable(Context context) {
        this.context = context;
    }

    public TravelLineExpenseTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }
    public int getTravelCodeLineExist(String travelcode,String line_no) {
        String[] columns = new String[]{this.android_travelcode,this.travelcode,this.line_no};
        Cursor cursor = database.query(TABLE_NAME, columns, this.travelcode+"='"+travelcode+"' and "+this.line_no+"='"+line_no+"'", null, null, null, null);
        return cursor.getCount();
    }
    public void insert(TravelLineExpenseModel data) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_travelcode, data.android_travelcode);
            contentValue.put(this.travelcode, data.travelcode);
            contentValue.put(this.line_no, data.line_no);
            contentValue.put(this.date, data.date);
            contentValue.put(this.from_loc, data.from_loc);
            contentValue.put(this.to_loc, data.to_loc);
            contentValue.put(this.departure, data.departure);
            contentValue.put(this.arrival, data.arrival);
            contentValue.put(this.fare, data.fare);
            contentValue.put(this.mode_of_travel, data.mode_of_travel);
            contentValue.put(this.loading_in_any, data.loading_in_any);
            contentValue.put(this.distance_km, data.distance_km);
            contentValue.put(this.fuel_vehicle_expance, data.fuel_vehicle_expance);
            contentValue.put(this.daily_express, data.daily_express);
            contentValue.put(this.vehicle_repairing, data.vehicle_repairing);
            contentValue.put(this.local_convance, data.local_convance);
            contentValue.put(this.other_expenses, data.other_expenses);
            contentValue.put(this.total_amount_calulated, data.total_amount_calulated);
            contentValue.put(this.created_on, data.created_on);
            contentValue.put(this.lineSend, lineSend);
            contentValue.put(this.mod_city, data.mod_city);
            contentValue.put(this.mod_lodging, data.mod_lodging);
            contentValue.put(this.mod_da_half, data.mod_da_half);
            contentValue.put(this.mode_da_full, data.mode_da_full);
            contentValue.put(this.mod_ope_max, data.mod_ope_max);
            contentValue.put(this.user_grade, data.user_grade);
            database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void insertBulk(List<TravelLineExpenseModel> bulkitems, String created_on, String lineSend) {
        database.beginTransaction();
        try {
            for (TravelLineExpenseModel data : bulkitems) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.android_travelcode, data.android_travelcode);
                contentValue.put(this.travelcode, data.travelcode);
                contentValue.put(this.line_no, data.line_no);
                contentValue.put(this.date, data.date);
                contentValue.put(this.from_loc, data.from_loc);
                contentValue.put(this.to_loc, data.to_loc);
                contentValue.put(this.departure, data.departure);
                contentValue.put(this.arrival, data.arrival);
                contentValue.put(this.fare, data.fare);
                contentValue.put(this.mode_of_travel, data.mode_of_travel);
                contentValue.put(this.loading_in_any, data.loading_in_any);
                contentValue.put(this.distance_km, data.distance_km);
                contentValue.put(this.fuel_vehicle_expance, data.fuel_vehicle_expance);
                contentValue.put(this.daily_express, data.daily_express);
                contentValue.put(this.vehicle_repairing, data.vehicle_repairing);
                contentValue.put(this.local_convance, data.local_convance);
                contentValue.put(this.other_expenses, data.other_expenses);
                contentValue.put(this.total_amount_calulated, data.total_amount_calulated);
                contentValue.put(this.created_on, created_on);
                contentValue.put(this.lineSend, lineSend);

                contentValue.put(this.mod_city, data.mod_city);
                contentValue.put(this.mod_lodging, data.mod_lodging);
                contentValue.put(this.mod_da_half, data.mod_da_half);
                contentValue.put(this.mode_da_full, data.mode_da_full);
                contentValue.put(this.mod_ope_max, data.mod_ope_max);
                contentValue.put(this.user_grade, data.user_grade);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void insertBulk(List<TravelLineExpenseModel> bulkitems, String lineSend) {
        database.beginTransaction();
        try {
            for (TravelLineExpenseModel data : bulkitems) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.android_travelcode, data.android_travelcode);
                contentValue.put(this.travelcode, data.travelcode);
                contentValue.put(this.line_no, data.line_no);
                contentValue.put(this.date, data.date);
                contentValue.put(this.from_loc, data.from_loc);
                contentValue.put(this.to_loc, data.to_loc);
                contentValue.put(this.departure, data.departure);
                contentValue.put(this.arrival, data.arrival);
                contentValue.put(this.fare, data.fare);
                contentValue.put(this.mode_of_travel, data.mode_of_travel);
                contentValue.put(this.loading_in_any, data.loading_in_any);
                contentValue.put(this.distance_km, data.distance_km);
                contentValue.put(this.fuel_vehicle_expance, data.fuel_vehicle_expance);
                contentValue.put(this.daily_express, data.daily_express);
                contentValue.put(this.vehicle_repairing, data.vehicle_repairing);
                contentValue.put(this.local_convance, data.local_convance);
                contentValue.put(this.other_expenses, data.other_expenses);
                contentValue.put(this.total_amount_calulated, data.total_amount_calulated);
                contentValue.put(this.created_on, data.created_on);
                contentValue.put(this.lineSend, lineSend);

                contentValue.put(this.mod_city, data.mod_city);
                contentValue.put(this.mod_lodging, data.mod_lodging);
                contentValue.put(this.mod_da_half, data.mod_da_half);
                contentValue.put(this.mode_da_full, data.mode_da_full);
                contentValue.put(this.mod_ope_max, data.mod_ope_max);
                contentValue.put(this.user_grade, data.user_grade);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public int update(String android_event_code, String travelcode, String created_on) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.travelcode, travelcode);
            contentValues.put(this.created_on, created_on);
            int i = database.update(TABLE_NAME, contentValues, this.android_travelcode + " = " + android_event_code, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public int updateAllExpenseLines(String created_on, String lineSend) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.created_on, created_on);
            contentValues.put(this.lineSend, lineSend);
            int i = database.update(TABLE_NAME, contentValues, null, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public List<TravelLineExpenseModel> fetchLocalUnsendLine() {
        List<TravelLineExpenseModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_travelcode, this.travelcode, this.line_no, this.date, this.from_loc, this.to_loc, this.departure,
                this.arrival, this.fare, this.mode_of_travel, this.loading_in_any, this.distance_km, this.fuel_vehicle_expance, this.daily_express,
                this.vehicle_repairing, this.local_convance, this.other_expenses, this.total_amount_calulated, this.created_on, this.lineSend
                , this.mod_city, this.mod_lodging, this.mod_da_half, this.mode_da_full, this.mod_ope_max, this.user_grade};
        Cursor cursor = database.query(TABLE_NAME, columns, lineSend + "='LOCAL'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TravelLineExpenseModel(
                        cursor.getString(cursor.getColumnIndex(this.android_travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.line_no)),
                        cursor.getString(cursor.getColumnIndex(this.date)),
                        cursor.getString(cursor.getColumnIndex(this.from_loc)),
                        cursor.getString(cursor.getColumnIndex(this.to_loc)),
                        cursor.getString(cursor.getColumnIndex(this.departure)),
                        cursor.getString(cursor.getColumnIndex(this.arrival)),
                        cursor.getString(cursor.getColumnIndex(this.fare)),
                        cursor.getString(cursor.getColumnIndex(this.mode_of_travel)),
                        cursor.getString(cursor.getColumnIndex(this.loading_in_any)),
                        cursor.getString(cursor.getColumnIndex(this.distance_km)),
                        cursor.getString(cursor.getColumnIndex(this.fuel_vehicle_expance)),
                        cursor.getString(cursor.getColumnIndex(this.daily_express)),
                        cursor.getString(cursor.getColumnIndex(this.vehicle_repairing)),
                        cursor.getString(cursor.getColumnIndex(this.local_convance)),
                        cursor.getString(cursor.getColumnIndex(this.other_expenses)),
                        cursor.getString(cursor.getColumnIndex(this.total_amount_calulated)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),

                        cursor.getString(cursor.getColumnIndex(this.mod_city)),
                        cursor.getString(cursor.getColumnIndex(this.mod_lodging)),
                        cursor.getString(cursor.getColumnIndex(this.mod_da_half)),
                        cursor.getString(cursor.getColumnIndex(this.mode_da_full)),
                        cursor.getString(cursor.getColumnIndex(this.mod_ope_max)),
                        cursor.getString(cursor.getColumnIndex(this.user_grade))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public List<TravelLineExpenseModel> fetch(String travelcode, String android_travelcode) {
        List<TravelLineExpenseModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_travelcode, this.travelcode, this.line_no, this.date, this.from_loc, this.to_loc, this.departure,
                this.arrival, this.fare, this.mode_of_travel, this.loading_in_any, this.distance_km, this.fuel_vehicle_expance, this.daily_express,
                this.vehicle_repairing, this.local_convance, this.other_expenses, this.total_amount_calulated, this.created_on
                , this.mod_city, this.mod_lodging, this.mod_da_half, this.mode_da_full, this.mod_ope_max, this.user_grade};
        Cursor cursor = database.query(TABLE_NAME, columns, this.travelcode + "='" + travelcode + "' and " + this.android_travelcode + "='" + android_travelcode + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TravelLineExpenseModel(
                        cursor.getString(cursor.getColumnIndex(this.android_travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.line_no)),
                        cursor.getString(cursor.getColumnIndex(this.date)),
                        cursor.getString(cursor.getColumnIndex(this.from_loc)),
                        cursor.getString(cursor.getColumnIndex(this.to_loc)),
                        cursor.getString(cursor.getColumnIndex(this.departure)),
                        cursor.getString(cursor.getColumnIndex(this.arrival)),
                        cursor.getString(cursor.getColumnIndex(this.fare)),
                        cursor.getString(cursor.getColumnIndex(this.mode_of_travel)),
                        cursor.getString(cursor.getColumnIndex(this.loading_in_any)),
                        cursor.getString(cursor.getColumnIndex(this.distance_km)),
                        cursor.getString(cursor.getColumnIndex(this.fuel_vehicle_expance)),
                        cursor.getString(cursor.getColumnIndex(this.daily_express)),
                        cursor.getString(cursor.getColumnIndex(this.vehicle_repairing)),
                        cursor.getString(cursor.getColumnIndex(this.local_convance)),
                        cursor.getString(cursor.getColumnIndex(this.other_expenses)),
                        cursor.getString(cursor.getColumnIndex(this.total_amount_calulated)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),

                        cursor.getString(cursor.getColumnIndex(this.mod_city)),
                        cursor.getString(cursor.getColumnIndex(this.mod_lodging)),
                        cursor.getString(cursor.getColumnIndex(this.mod_da_half)),
                        cursor.getString(cursor.getColumnIndex(this.mode_da_full)),
                        cursor.getString(cursor.getColumnIndex(this.mod_ope_max)),
                        cursor.getString(cursor.getColumnIndex(this.user_grade))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public List<TravelLineExpenseModel> fetchUnsendData() {
        List<TravelLineExpenseModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_travelcode, this.travelcode, this.line_no, this.date, this.from_loc, this.to_loc,
                this.departure, this.arrival, this.fare, this.mode_of_travel, this.loading_in_any, this.distance_km, this.fuel_vehicle_expance,
                this.daily_express, this.vehicle_repairing, this.local_convance, this.other_expenses, this.total_amount_calulated, this.created_on
                , this.mod_city, this.mod_lodging, this.mod_da_half, this.mode_da_full, this.mod_ope_max, this.user_grade};
        Cursor cursor = database.query(TABLE_NAME, columns, travelcode + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TravelLineExpenseModel(
                        cursor.getString(cursor.getColumnIndex(this.android_travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.line_no)),
                        cursor.getString(cursor.getColumnIndex(this.date)),
                        cursor.getString(cursor.getColumnIndex(this.from_loc)),
                        cursor.getString(cursor.getColumnIndex(this.to_loc)),
                        cursor.getString(cursor.getColumnIndex(this.departure)),
                        cursor.getString(cursor.getColumnIndex(this.arrival)),
                        cursor.getString(cursor.getColumnIndex(this.fare)),
                        cursor.getString(cursor.getColumnIndex(this.mode_of_travel)),
                        cursor.getString(cursor.getColumnIndex(this.loading_in_any)),
                        cursor.getString(cursor.getColumnIndex(this.distance_km)),
                        cursor.getString(cursor.getColumnIndex(this.fuel_vehicle_expance)),
                        cursor.getString(cursor.getColumnIndex(this.daily_express)),
                        cursor.getString(cursor.getColumnIndex(this.vehicle_repairing)),
                        cursor.getString(cursor.getColumnIndex(this.local_convance)),
                        cursor.getString(cursor.getColumnIndex(this.other_expenses)),
                        cursor.getString(cursor.getColumnIndex(this.total_amount_calulated)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),

                        cursor.getString(cursor.getColumnIndex(this.mod_city)),
                        cursor.getString(cursor.getColumnIndex(this.mod_lodging)),
                        cursor.getString(cursor.getColumnIndex(this.mod_da_half)),
                        cursor.getString(cursor.getColumnIndex(this.mode_da_full)),
                        cursor.getString(cursor.getColumnIndex(this.mod_ope_max)),
                        cursor.getString(cursor.getColumnIndex(this.user_grade))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public void delete(String travelcode, String android_travelcode, String line_no) {
        database.delete(TABLE_NAME, this.travelcode + "='" + travelcode + "' and " + this.android_travelcode + "='" + android_travelcode + "' and " +
                this.line_no + "='" + line_no + "'", null);
    }

}
