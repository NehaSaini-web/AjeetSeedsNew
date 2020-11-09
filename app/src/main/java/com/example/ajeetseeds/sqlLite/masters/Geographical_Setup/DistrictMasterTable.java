package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DistrictMasterTable {
    // Table Name
    public static final String TABLE_NAME = "district_master";
    // Table columns
    public static final String code = "code";
    public static final String name = "name";
    public static final String class_of_city="class_of_city";
    public static final String active = "active";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY, " +
            name + " TEXT NOT NULL, " +
            class_of_city + " TEXT NULL, " +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DistrictMasterTable(Context context) {
        this.context = context;
    }

    public DistrictMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public long insert(DistrictMaster districtMaster) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.code, districtMaster.code);
        contentValue.put(this.name, districtMaster.name);
        contentValue.put(this.class_of_city, districtMaster.class_of_city);
        contentValue.put(this.active, districtMaster.active);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    public void insertArray(List<DistrictMaster> bulkdata) {
        database.beginTransaction();
        try {
            for (DistrictMaster districtMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, districtMaster.code);
                contentValue.put(this.name, districtMaster.name);
                contentValue.put(this.class_of_city, districtMaster.class_of_city);
                contentValue.put(this.active, districtMaster.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<DistrictMaster> fetch() {
        List<DistrictMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{this.code, this.name,this.class_of_city, this.active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DistrictMaster(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.name)),
                        cursor.getInt(cursor.getColumnIndex(this.active)),
                        cursor.getString(cursor.getColumnIndex(class_of_city))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public String fetchDistrictName(String districtcode) {
        String[] columns = new String[]{ this.name};
        Cursor cursor = database.query(TABLE_NAME, columns, this.code+"=?", new String[]{districtcode}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
              return  cursor.getString(cursor.getColumnIndex(this.name));
            } while (cursor.moveToNext());
        }
        return "";
    }
    public List<DistrictMaster> fetch_byGeograficalStateCode(String stateCode) {
        List<DistrictMaster> returnData = new ArrayList<>();
        Cursor cursor =database.rawQuery("SELECT * FROM district_master dm WHERE dm.code IN (SELECT gs.district FROM Geographical_Setup gs WHERE gs.[state]='"+stateCode+"')",null);;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DistrictMaster(
                        cursor.getString(cursor.getColumnIndex(code)),
                        cursor.getString(cursor.getColumnIndex(name)),
                        cursor.getInt(cursor.getColumnIndex(active)),
                        cursor.getString(cursor.getColumnIndex(class_of_city))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public int update(String code, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.code, code);
        contentValues.put(this.name, name);
        int i = database.update(TABLE_NAME, contentValues, this.code + " = " + code, null);
        return i;
    }

    public void delete(String code) {
        database.delete(TABLE_NAME, this.code + "=" + code, null);
    }

    public class DistrictMaster {
        public String code;
        public String name;
        public int active;
        public String class_of_city;

        public DistrictMaster(String code, String name, int active,String class_of_city) {
            this.code = code;
            this.name = name;
            this.active = active;
            this.class_of_city=class_of_city;
        }
    }
}
