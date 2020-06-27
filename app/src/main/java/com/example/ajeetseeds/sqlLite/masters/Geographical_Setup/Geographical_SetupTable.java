package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Geographical_SetupTable {
    // Table Name
    public static final String TABLE_NAME = "Geographical_Setup";
    // Table columns
    public static final String id = "id";
    public static final String zone = "zone";
    public static final String state = "state";
    public static final String region = "region";
    public static final String district = "district";
    public static final String taluka = "taluka";
    public static final String managers = "managers";
    public static final String active = "active";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER PRIMARY KEY," +
            zone + " TEXT NOT NULL," +
            state + " TEXT NOT NULL," +
            region + " TEXT NOT NULL," +
            district + " TEXT NOT NULL," +
            taluka + " TEXT NOT NULL," +
            active + " INTEGER," +
            managers + " TEXT" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public Geographical_SetupTable(Context context) {
        this.context = context;
    }

    public Geographical_SetupTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public void insertArray(List<Geographical_Setup> bulkdata) {
        database.beginTransaction();
        try {
            for (Geographical_Setup data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.id, data.id);
                contentValue.put(this.zone, data.zone);
                contentValue.put(this.state, data.state);
                contentValue.put(this.region, data.region);
                contentValue.put(this.district, data.district);
                contentValue.put(this.taluka, data.taluka);
                contentValue.put(this.managers, data.managers);
                contentValue.put(this.active, data.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<Geographical_Setup> fetch() {
        List<Geographical_Setup> returnData = new ArrayList<>();
        String[] columns = new String[]{id, zone, state, region, district, taluka, managers,active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
           do{
                returnData.add(new Geographical_Setup(
                        cursor.getLong(cursor.getColumnIndex(this.id)),
                        cursor.getString(cursor.getColumnIndex(this.zone)),
                        cursor.getString(cursor.getColumnIndex(this.state)),
                        cursor.getString(cursor.getColumnIndex(this.region)),
                        cursor.getString(cursor.getColumnIndex(this.district)),
                        cursor.getString(cursor.getColumnIndex(this.taluka)),
                        cursor.getString(cursor.getColumnIndex(this.managers)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update(long id, String zone, String state, String region, String district, String taluka, String managers) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.zone, zone);
            contentValues.put(this.state, state);
            contentValues.put(this.region, region);
            contentValues.put(this.district, district);
            contentValues.put(this.taluka, taluka);
            contentValues.put(this.managers, managers);
            int i = database.update(TABLE_NAME, contentValues, this.id + " = " + id, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }


    public void delete(long id) {
        database.delete(TABLE_NAME, this.id + "=" + id, null);
    }

    public class Geographical_Setup {
        public long id;
        public String zone;
        public String state;
        public String region;
        public String district;
        public String taluka;
        public String managers;
        public int active;

        public Geographical_Setup(long id, String zone, String state, String region, String district, String taluka, String managers,int active) {
            this.id = id;
            this.zone = zone;
            this.state = state;
            this.region = region;
            this.district = district;
            this.taluka = taluka;
            this.managers = managers;
            this.active=active;
        }
    }
}
