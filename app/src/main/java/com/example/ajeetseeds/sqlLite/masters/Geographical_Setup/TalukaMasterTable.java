package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TalukaMasterTable {
    // Table Name
    public static final String TABLE_NAME = "taluka_master";
    // Table columns
    public static final String code = "code";
    public static final String description = "description";
    public static final String active = "active";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY, " +
            description + " TEXT NOT NULL ," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public TalukaMasterTable(Context context) {
        this.context = context;
    }

    public TalukaMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(TalukaMaster talukaMaster) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.code, talukaMaster.code);
        contentValue.put(this.description, talukaMaster.description);
        contentValue.put(this.active, talukaMaster.active);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    public void insertArray(List<TalukaMaster> bulkdata) {
        database.beginTransaction();
        try {
            for (TalukaMaster talukaMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, talukaMaster.code);
                contentValue.put(this.description, talukaMaster.description);
                contentValue.put(this.active, talukaMaster.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<TalukaMaster> fetch() {
        List<TalukaMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{code, description, active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TalukaMaster(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.description)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String getTalukaName(String code) {
        String[] columns = new String[]{description};
        Cursor cursor = database.query(TABLE_NAME, columns, this.code + "='" + code + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return cursor.getString(cursor.getColumnIndex(this.description));
            } while (cursor.moveToNext());
        }
        return "";
    }

    public List<TalukaMaster> fetchBydistrictNo(String district_no) {
        List<TalukaMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{code, description, active};
        Cursor cursor = database.rawQuery("SELECT * FROM taluka_master tm WHERE tm.code IN (SELECT gs.taluka FROM Geographical_Setup gs WHERE gs.district='" + district_no + "')", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new TalukaMaster(
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

    public class TalukaMaster {
        public String code;
        public String description;
        public int active;

        public TalukaMaster(String code, String description, int active) {
            this.code = code;
            this.description = description;
            this.active = active;
        }
    }
}
