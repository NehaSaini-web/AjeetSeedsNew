package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StateMasterTable {
    // Table Name
    public static final String TABLE_NAME = "state_master";
    // Table columns
    public static final String code = "code";
    public static final String name = "name";
    public static final String active = "active";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY, " +
            name + " TEXT NOT NULL ," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public StateMasterTable(Context context) {
        this.context = context;
    }

    public StateMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(StateMaster stateMaster) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.code, stateMaster.code);
        contentValue.put(this.name, stateMaster.name);
        contentValue.put(this.active, stateMaster.active);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    public void insertArray(List<StateMaster> bulkdata) {
        database.beginTransaction();
        try {
            for (StateMaster stateMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, stateMaster.code);
                contentValue.put(this.name, stateMaster.name);
                contentValue.put(this.active, stateMaster.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<StateMaster> fetch() {
        List<StateMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{this.code, this.name, this.active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new StateMaster(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.name)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String getStateName(String code) {
        String[] columns = new String[]{this.name};
        Cursor cursor = database.query(TABLE_NAME, columns, this.code + "='" + code + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return cursor.getString(cursor.getColumnIndex(this.name));
            } while (cursor.moveToNext());
        }
        return "";
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

    public class StateMaster {
        public String code;
        public String name;
        public int active;

        public StateMaster(String code, String name, int active) {
            this.code = code;
            this.name = name;
            this.active = active;
        }
    }
}
