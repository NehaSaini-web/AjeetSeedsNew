package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ZoneMasterTable {
    // Table Name
    public static final String TABLE_NAME = "zone_master";
    // Table columns
    public static final String code = "code";
    public static final String description = "description";
    public static final String active = "active";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY, " +
            description + " TEXT, " +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public ZoneMasterTable(Context context) {
        this.context = context;
    }

    public ZoneMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public long insert(ZoneMaster zoneMaster) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.code, zoneMaster.code);
            contentValue.put(this.description, zoneMaster.description);
            contentValue.put(this.active, zoneMaster.active);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    public void insertArray(List<ZoneMaster> bulkdata) {
        database.beginTransaction();
        try {
            for (ZoneMaster zoneMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, zoneMaster.code);
                contentValue.put(this.description, zoneMaster.description);
                contentValue.put(this.active, zoneMaster.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ZoneMaster> fetch() {
        List<ZoneMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{code, description, active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new ZoneMaster(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.description)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update(String code, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.code, code);
        contentValues.put(this.description, description);
        int i = database.update(TABLE_NAME, contentValues, this.code + " = " + code, null);
        return i;
    }

    public void delete(long id) {
        database.delete(TABLE_NAME, this.code + "=" + id, null);
    }

    public class ZoneMaster {
        public String code;
        public String description;
        public int active;

        public ZoneMaster(String code, String descriptionm, int active) {
            this.code = code;
            this.description = description;
            this.active = active;
        }
    }
}
