package com.example.ajeetseeds.sqlLite.masters.Geographical_Setup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RegionMasterTable {
    // Table Name
    public static final String TABLE_NAME = "region_master";
    // Table columns
    public static final String code = "code";
    public static final String name = "name";
    public static final String regional_manager = "regional_manager";
    public static final String regional_manager_email_id = "regional_manager_email_id";
    public static final String regional_manager_emp_code = "regional_manager_emp_code";
    public static final String regional_head_emp_code = "regional_head_emp_code";
    public static final String regional_head = "regional_head";
    public static final String regional_head_email_id = "regional_head_email_id";
    public static final String regional_manager_mobile = "regional_manager_mobile";
    public static final String active = "active";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY, " +
            name + " TEXT NOT NULL," +
            regional_manager + " TEXT NOT NULL," +
            regional_manager_email_id + " TEXT NOT NULL," +
            regional_manager_emp_code + " TEXT NOT NULL," +
            regional_head_emp_code + " TEXT NOT NULL," +
            regional_head + " TEXT NOT NULL," +
            regional_head_email_id + " TEXT NOT NULL," +
            regional_manager_mobile + " TEXT NOT NULL," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public RegionMasterTable(Context context) {
        this.context = context;
    }

    public RegionMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(RegionMaster regionMaster) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.code, regionMaster.code);
        contentValue.put(this.name, regionMaster.name);
        contentValue.put(this.regional_manager, regionMaster.regional_manager);
        contentValue.put(this.regional_manager_email_id, regionMaster.regional_manager_email_id);
        contentValue.put(this.regional_manager_emp_code, regionMaster.regional_manager_emp_code);
        contentValue.put(this.regional_head_emp_code, regionMaster.regional_head_emp_code);
        contentValue.put(this.regional_head, regionMaster.regional_head);
        contentValue.put(this.regional_head_email_id, regionMaster.regional_head_email_id);
        contentValue.put(this.regional_manager_mobile, regionMaster.regional_manager_mobile);
        contentValue.put(this.active, regionMaster.active);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    public void insertArray(List<RegionMaster> bulkdata) {
        database.beginTransaction();
        try {
            for (RegionMaster regionMaster : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, regionMaster.code);
                contentValue.put(this.name, regionMaster.name);
                contentValue.put(this.regional_manager, regionMaster.regional_manager);
                contentValue.put(this.regional_manager_email_id, regionMaster.regional_manager_email_id);
                contentValue.put(this.regional_manager_emp_code, regionMaster.regional_manager_emp_code);
                contentValue.put(this.regional_head_emp_code, regionMaster.regional_head_emp_code);
                contentValue.put(this.regional_head, regionMaster.regional_head);
                contentValue.put(this.regional_head_email_id, regionMaster.regional_head_email_id);
                contentValue.put(this.regional_manager_mobile, regionMaster.regional_manager_mobile);
                contentValue.put(this.active, regionMaster.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<RegionMaster> fetch() {
        List<RegionMaster> returnData = new ArrayList<>();
        String[] columns = new String[]{code, name, regional_manager, regional_manager_email_id, regional_manager_emp_code, regional_head_emp_code, regional_head, regional_head_email_id, regional_manager_mobile, active};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new RegionMaster(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.name)),
                        cursor.getString(cursor.getColumnIndex(this.regional_manager)),
                        cursor.getString(cursor.getColumnIndex(this.regional_manager_email_id)),
                        cursor.getString(cursor.getColumnIndex(this.regional_manager_emp_code)),
                        cursor.getString(cursor.getColumnIndex(this.regional_head_emp_code)),
                        cursor.getString(cursor.getColumnIndex(this.regional_head)),
                        cursor.getString(cursor.getColumnIndex(this.regional_head_email_id)),
                        cursor.getString(cursor.getColumnIndex(this.regional_manager_mobile)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update(RegionMaster regionMaster) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.name, regionMaster.name);
        contentValue.put(this.regional_manager, regionMaster.regional_manager);
        contentValue.put(this.regional_manager_email_id, regionMaster.regional_manager_email_id);
        contentValue.put(this.regional_manager_emp_code, regionMaster.regional_manager_emp_code);
        contentValue.put(this.regional_head_emp_code, regionMaster.regional_head_emp_code);
        contentValue.put(this.regional_head, regionMaster.regional_head);
        contentValue.put(this.regional_head_email_id, regionMaster.regional_head_email_id);
        contentValue.put(this.regional_manager_mobile, regionMaster.regional_manager_mobile);
        contentValue.put(this.active, regionMaster.active);
        int i = database.update(TABLE_NAME, contentValue, this.code + " = " + regionMaster.code, null);
        return i;
    }

    public void delete(String code) {
        database.delete(TABLE_NAME, this.code + "=" + code, null);
    }

    public class RegionMaster {
        public String code;
        public String name;
        public String regional_manager;
        public String regional_manager_email_id;
        public String regional_manager_emp_code;
        public String regional_head_emp_code;
        public String regional_head;
        public String regional_head_email_id;
        public String regional_manager_mobile;
        public int active;

        public RegionMaster(String code, String name, String regional_manager, String regional_manager_email_id, String regional_manager_emp_code,
                            String regional_head_emp_code, String regional_head, String regional_head_email_id, String regional_manager_mobile, int active) {
            this.code = code;
            this.name = name;
            this.regional_manager = regional_manager;
            this.regional_manager_email_id = regional_manager_email_id;
            this.regional_manager_emp_code = regional_manager_emp_code;
            this.regional_head_emp_code = regional_head_emp_code;
            this.regional_head = regional_head;
            this.regional_head_email_id = regional_head_email_id;
            this.regional_manager_mobile = regional_manager_mobile;
            this.active = active;
        }
    }
}
