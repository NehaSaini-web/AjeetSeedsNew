package com.example.ajeetseeds.sqlLite.travel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TravelHeaderTable {
    // Table Name
    public static final String TABLE_NAME = "travel_header";
    // Table columns
    public static final String android_travelcode = "android_travelcode";
    public static final String travelcode = "travelcode";
    public static final String from_loc = "from_loc";
    public static final String to_loc = "to_loc";
    public static final String start_date = "start_date";
    public static final String end_date = "end_date";
    public static final String travel_reson = "travel_reson";
    public static final String expense_budget = "expense_budget";
    public static final String approve_budget = "approve_budget";
    public static final String created_on = "created_on";
    public static final String created_by = "created_by";
    public static final String status = "status";
    public static final String approver_id = "approver_id";
    public static final String approve_on = "approve_on";
    public static final String reason = "reason";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            android_travelcode+ " TEXT PRIMARY KEY, "+
            travelcode+ " TEXT NOT NULL, "+
            from_loc+ " TEXT NOT NULL, "+
            to_loc+ " TEXT NOT NULL, "+
            start_date+ " TEXT NOT NULL, "+
            end_date+ " TEXT NOT NULL, "+
            travel_reson+ " TEXT NOT NULL, "+
            expense_budget+ " TEXT NOT NULL, "+
            approve_budget+ " TEXT NULL, "+
            created_on+ " TEXT NOT NULL, "+
            created_by+ " TEXT NOT NULL, "+
            status+ " TEXT NOT NULL, "+
            approver_id+ " TEXT NOT NULL, "+
            approve_on+ " TEXT NULL, "+
            reason+ " TEXT NULL "+
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public TravelHeaderTable(Context context) {
        this.context = context;
    }

    public TravelHeaderTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }
    public String getTableSequenceNo() {
        String[] columns = new String[]{android_travelcode};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        return String.valueOf((cursor.getCount() + 1));
    }
    public void insert(TravelHeaderModel data) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_travelcode,data.android_travelcode);
            contentValue.put(this.travelcode,data.travelcode);
            contentValue.put(this.from_loc,data.from_loc);
            contentValue.put(this.to_loc,data.to_loc);
            contentValue.put(this.start_date,data.start_date);
            contentValue.put(this.end_date,data.end_date);
            contentValue.put(this.travel_reson,data.travel_reson);
            contentValue.put(this.expense_budget,data.expense_budget);
            contentValue.put(this.created_on,data.created_on);
            contentValue.put(this.created_by,data.created_by);
            contentValue.put(this.status,data.status);
            contentValue.put(this.approver_id,data.approver_id);
            database.replace(TABLE_NAME, null, contentValue);
           database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
    public int updateStatus(String travelcode,String status,String approve_on) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.status, status);
            contentValues.put(this.approve_on, approve_on);
            int i = database.update(TABLE_NAME, contentValues, this.travelcode + " = '" + travelcode+"'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }
  public int update(String android_event_code, String travelcode,String created_on) {
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
    public int update_travelStatus(String travelcode,String status,String reason,String approve_budget,String approve_on) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.status, status);
            contentValues.put(this.reason, reason);
            contentValues.put(this.approve_budget, approve_budget);
            contentValues.put(this.approve_on, approve_on);
            int i = database.update(TABLE_NAME, contentValues, this.travelcode + " = '" + travelcode+"'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }
    public List<TravelHeaderModel> fetch() {
        List<TravelHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_travelcode,this.travelcode,this.from_loc,this.to_loc,this.start_date,this.end_date,this.travel_reson,this.expense_budget,this.approve_budget,this.created_on,this.created_by,this.status,this.approver_id,this.approve_on,this.reason};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TravelHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.from_loc)),
                        cursor.getString(cursor.getColumnIndex(this.to_loc)),
                        cursor.getString(cursor.getColumnIndex(this.start_date)),
                        cursor.getString(cursor.getColumnIndex(this.end_date)),
                        cursor.getString(cursor.getColumnIndex(this.travel_reson)),
                        cursor.getString(cursor.getColumnIndex(this.expense_budget)),
                        cursor.getString(cursor.getColumnIndex(this.approve_budget)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),
                        cursor.getString(cursor.getColumnIndex(this.created_by)),
                        cursor.getString(cursor.getColumnIndex(this.status)),
                        cursor.getString(cursor.getColumnIndex(this.approver_id)),
                        cursor.getString(cursor.getColumnIndex(this.approve_on)),
                        cursor.getString(cursor.getColumnIndex(this.reason))
                        ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public List<TravelHeaderModel> fetchUnsendData() {
        List<TravelHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_travelcode,this.travelcode,this.from_loc,this.to_loc,this.start_date,this.end_date,this.travel_reson,this.expense_budget,this.approve_budget,this.created_on,this.created_by,this.status,this.approver_id,this.approve_on,this.reason};
        Cursor cursor = database.query(TABLE_NAME, columns, travelcode + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TravelHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.travelcode)),
                        cursor.getString(cursor.getColumnIndex(this.from_loc)),
                        cursor.getString(cursor.getColumnIndex(this.to_loc)),
                        cursor.getString(cursor.getColumnIndex(this.start_date)),
                        cursor.getString(cursor.getColumnIndex(this.end_date)),
                        cursor.getString(cursor.getColumnIndex(this.travel_reson)),
                        cursor.getString(cursor.getColumnIndex(this.expense_budget)),
                        cursor.getString(cursor.getColumnIndex(this.approve_budget)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),
                        cursor.getString(cursor.getColumnIndex(this.created_by)),
                        cursor.getString(cursor.getColumnIndex(this.status)),
                        cursor.getString(cursor.getColumnIndex(this.approver_id)),
                        cursor.getString(cursor.getColumnIndex(this.approve_on)),
                        cursor.getString(cursor.getColumnIndex(this.reason))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public void delete(String travelcode) {
        database.delete(TABLE_NAME, this.travelcode + "='" + travelcode + "'", null);
    }

    public class TravelHeaderModel {
        public String android_travelcode;
        public String travelcode;
        public String from_loc;
        public String to_loc;
        public String start_date;
        public String end_date;
        public String travel_reson;
        public String expense_budget;
        public String approve_budget;
        public String created_on;
        public String created_by;
        public String status;
        public String approver_id;
        public String approve_on;
        public String reason;

        public String from_loc_name;
        public String to_loc_name;
        public TravelHeaderModel(String android_travelcode,String travelcode, String from_loc, String to_loc, String start_date, String end_date, String travel_reson, String expense_budget, String approve_budget, String created_on, String created_by, String status, String approver_id, String approve_on, String reason) {
          this.android_travelcode=android_travelcode;
            this.travelcode=travelcode;
            this.from_loc=from_loc;
            this.to_loc=to_loc;
            this.start_date=start_date;
            this.end_date=end_date;
            this.travel_reson=travel_reson;
            this.expense_budget=expense_budget;
            this.approve_budget=approve_budget;
            this.created_on=created_on;
            this.created_by=created_by;
            this.status=status;
            this.approver_id=approver_id;
            this.approve_on=approve_on;
            this.reason=reason;

        }
    }
}
