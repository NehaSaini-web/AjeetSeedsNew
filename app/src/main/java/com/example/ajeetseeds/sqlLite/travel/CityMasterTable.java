package com.example.ajeetseeds.sqlLite.travel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CityMasterTable {
    // Table Name
    public static final String TABLE_NAME = "city_master";
    // Table columns
    public static final String code = "code";
    public static final String name = "name";
    public static final String country_region_code = "country_region_code";
    public static final String class_of_city = "class_of_city";
    public static final String active = "active";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY," +
            name + " TEXT NOT NULL," +
            country_region_code + " TEXT NULL," +
            class_of_city + " TEXT," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CityMasterTable(Context context) {
        this.context = context;
    }

    public CityMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(CityMasterModel data) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.code, data.code);
            contentValue.put(this.name, data.name);
            contentValue.put(this.country_region_code, data.country_region_code);
            contentValue.put(this.class_of_city, data.class_of_city);
            contentValue.put(this.active, data.active);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }


    public void insertArray(List<CityMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (CityMasterModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, data.code);
                contentValue.put(this.name, data.name);
                contentValue.put(this.country_region_code, data.country_region_code);
                contentValue.put(this.class_of_city, data.class_of_city);
                contentValue.put(this.active, data.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<CityMasterModel> fetch() {
        List<CityMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{code, name, country_region_code, class_of_city, active};
        Cursor cursor = database.query(TABLE_NAME, columns, this.active + "=" + 0, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new CityMasterModel(
                        cursor.getString(cursor.getColumnIndex(code)),
                        cursor.getString(cursor.getColumnIndex(name)),
                        cursor.getString(cursor.getColumnIndex(country_region_code)),
                        cursor.getString(cursor.getColumnIndex(class_of_city)),
                        cursor.getInt(cursor.getColumnIndex(active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String fetchCityName(String code) {
        String[] columns = new String[]{name};
        Cursor cursor = database.query(TABLE_NAME, columns,  this.code + "='" + code + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                return cursor.getString(cursor.getColumnIndex(name));
            } while (cursor.moveToNext());
        }
        return "";
    }


    public void delete(String code) {
        database.delete(TABLE_NAME, this.code + "=" + code, null);
    }

    public class CityMasterModel {
        public boolean condition;
        public String code;
        public String name;
        public String country_region_code;
        public String class_of_city;
        public int active;

        public CityMasterModel(String code, String name, String country_region_code, String class_of_city, int active) {
            this.code = code;
            this.name = name;
            this.country_region_code = country_region_code;
            this.class_of_city = class_of_city;
            this.active = active;
        }
    }
}
