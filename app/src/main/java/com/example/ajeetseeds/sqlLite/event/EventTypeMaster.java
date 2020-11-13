package com.example.ajeetseeds.sqlLite.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EventTypeMaster {
    // Table Name
    public static final String TABLE_NAME = "event_type_master";
    // Table columns
    public static final String id = "id";
    public static final String event_type = "event_type";
    public static final String qty = "qty";
    public static final String rate = "rate";
    public static final String parent_id = "parent_id";
    public static final String no_of_attendee = "no_of_attendee";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER PRIMARY KEY, " +
            event_type + " TEXT NOT NULL, " +
            qty + " TEXT NOT NULL," +
            rate + " TEXT NOT NULL," +
            parent_id + " INTEGER NOT NULL," +
            no_of_attendee + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public EventTypeMaster(Context context) {
        this.context = context;
    }

    public EventTypeMaster open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public void insertArray(List<EventTypeMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (EventTypeMasterModel districtMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.id, districtMaster.id);
                contentValue.put(this.event_type, districtMaster.event_type);
                contentValue.put(this.qty, districtMaster.qty);
                contentValue.put(this.rate, districtMaster.rate);
                contentValue.put(this.parent_id, districtMaster.parent_id);
                contentValue.put(this.no_of_attendee, districtMaster.no_of_attendee);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<EventTypeMasterModel> fetchParent() {
        List<EventTypeMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.id, this.event_type,this.qty, this.rate, this.parent_id, no_of_attendee};
        Cursor cursor = database.query(TABLE_NAME, columns, this.parent_id + "=0", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new EventTypeMasterModel(
                        cursor.getString(cursor.getColumnIndex(this.id)),
                        cursor.getString(cursor.getColumnIndex(this.event_type)),
                        cursor.getString(cursor.getColumnIndex(this.qty)),
                        cursor.getString(cursor.getColumnIndex(this.rate)),
                        cursor.getInt(cursor.getColumnIndex(this.parent_id)),
                        cursor.getString(cursor.getColumnIndex(this.no_of_attendee))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int getParentId(String event_type) {
        List<EventTypeMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.id};
        Cursor cursor = database.query(TABLE_NAME, columns, this.event_type + "='" + event_type + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return cursor.getInt(cursor.getColumnIndex(this.id));
            } while (cursor.moveToNext());
        }
        return -1;
    }

    public List<EventTypeMasterModel> fetchChield(int parent_id) {
        List<EventTypeMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.id, this.event_type,this.qty, this.rate, this.parent_id, no_of_attendee};
        Cursor cursor = database.query(TABLE_NAME, columns, this.parent_id + "=" + parent_id, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new EventTypeMasterModel(
                        cursor.getString(cursor.getColumnIndex(this.id)),
                        cursor.getString(cursor.getColumnIndex(this.event_type)),
                        cursor.getString(cursor.getColumnIndex(this.qty)),
                        cursor.getString(cursor.getColumnIndex(this.rate)),
                        cursor.getInt(cursor.getColumnIndex(this.parent_id)),
                        cursor.getString(cursor.getColumnIndex(this.no_of_attendee))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public String fetchNameById(String id) {
        List<EventTypeMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.event_type};
        Cursor cursor = database.query(TABLE_NAME, columns, this.id + "='" + id+"'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                        return cursor.getString(cursor.getColumnIndex(this.event_type));
            } while (cursor.moveToNext());
        }
        return "";
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, this.id + "=" + id, null);
    }

    public void deleteAllRecord() {
        database.delete(TABLE_NAME, null, null);
    }

    public class EventTypeMasterModel {
        public boolean condition;
        public String id;
        public String event_type;
        public String qty;
        public String rate;
        public int parent_id;
        public String no_of_attendee;

        public EventTypeMasterModel(String id, String event_type, String qty,String rate, int parent_id, String no_of_attendee) {
            this.id = id;
            this.event_type = event_type;
            this.qty=qty;
            this.rate = rate;
            this.parent_id = parent_id;
            this.no_of_attendee = no_of_attendee;
        }
    }
}
