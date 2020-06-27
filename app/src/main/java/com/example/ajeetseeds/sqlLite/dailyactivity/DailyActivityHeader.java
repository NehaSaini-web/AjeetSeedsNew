package com.example.ajeetseeds.sqlLite.dailyactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DailyActivityHeader {
    // Table Name
    public static final String TABLE_NAME = "daily_activity_header";
    // Table columns
    public static final String android_activity_no = "android_activity_no";
    public static final String activity_no = "activity_no";
    public static final String contact_no = "contact_no";
    public static final String contact_no1 = "contact_no1";
    public static final String order_collected = "order_collected";
    public static final String payment_collected = "payment_collected";
    public static final String updated_on = "updated_on";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            android_activity_no + " TEXT PRIMARY KEY," +
            activity_no + " TEXT NULL," +
            contact_no + " TEXT NOT NULL," +
            contact_no1 + " TEXT NULL," +
            order_collected + " TEXT NOT NULL," +
            payment_collected + " TEXT NOT NULL," +
            updated_on + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DailyActivityHeader(Context context) {
        this.context = context;
    }

    public DailyActivityHeader open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(DailyActivityHeaderModel dailyActivityHeaderModel) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_activity_no, dailyActivityHeaderModel.android_activity_no);
            contentValue.put(this.activity_no, dailyActivityHeaderModel.activity_no);
            contentValue.put(this.contact_no, dailyActivityHeaderModel.contact_no);
            contentValue.put(this.contact_no1, dailyActivityHeaderModel.contact_no1);
            contentValue.put(this.order_collected, dailyActivityHeaderModel.order_collected);
            contentValue.put(this.payment_collected, dailyActivityHeaderModel.payment_collected);
            contentValue.put(this.updated_on, dailyActivityHeaderModel.updated_on);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }
    public String getTableSequenceNo() {
            String[] columns = new String[]{android_activity_no};
            Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
            return String.valueOf((cursor.getCount() + 1));
    }
    public List<DailyActivityHeaderModel> fetch() {
        List<DailyActivityHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_activity_no, activity_no, contact_no, contact_no1, order_collected, payment_collected,updated_on};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DailyActivityHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.contact_no)),
                        cursor.getString(cursor.getColumnIndex(this.contact_no1)),
                        cursor.getString(cursor.getColumnIndex(this.order_collected)),
                        cursor.getString(cursor.getColumnIndex(this.payment_collected)),
                        cursor.getString(cursor.getColumnIndex(this.updated_on))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public List<DailyActivityHeaderModel> fetchUnsendData() {
        List<DailyActivityHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_activity_no, activity_no, contact_no, contact_no1, order_collected, payment_collected,updated_on};
        Cursor cursor = database.query(TABLE_NAME, columns, activity_no + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DailyActivityHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.contact_no)),
                        cursor.getString(cursor.getColumnIndex(this.contact_no1)),
                        cursor.getString(cursor.getColumnIndex(this.order_collected)),
                        cursor.getString(cursor.getColumnIndex(this.payment_collected)),
                        cursor.getString(cursor.getColumnIndex(this.updated_on))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public int update(String android_activity_no, String activity_no) {   //todo update activity no when data is going to post to the server
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.activity_no, activity_no);
            int i = database.update(TABLE_NAME, contentValues, this.android_activity_no + " = " + android_activity_no, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }


    public void delete(String activity_no) {
        database.delete(TABLE_NAME, this.activity_no + "=" + activity_no, null);
    }

    public class DailyActivityHeaderModel {
        public String android_activity_no;
        public String activity_no;
        public String contact_no;
        public String contact_no1;
        public String order_collected;
        public String payment_collected;
        public String updated_on;

        public DailyActivityHeaderModel(String android_activity_no, String activity_no, String contact_no, String contact_no1, String order_collected, String payment_collected,String updated_on) {
            this.android_activity_no = android_activity_no;
            this.activity_no = activity_no;
            this.contact_no = contact_no;
            this.contact_no1 = contact_no1;
            this.order_collected = order_collected;
            this.payment_collected = payment_collected;
            this.updated_on=updated_on;
        }
    }
}
