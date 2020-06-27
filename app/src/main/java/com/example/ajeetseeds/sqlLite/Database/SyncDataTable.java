package com.example.ajeetseeds.sqlLite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.AllTablesName;

import java.util.ArrayList;
import java.util.List;

public class SyncDataTable {
    // Table Name
    public static final String TABLE_NAME = "SyncDataTable";
    // Table columns
    public static final String All_table_name = "All_table_name";
    public static final String table_type = "table_type";
    public static final String inReady = "inReady";
    public static final String inLastDateTime = "inLastDateTime";
    public static final String outReady = "outReady";
    public static final String outLastDateTime = "outLastDateTime";
    public static final String user_login = "user_login";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            All_table_name + " TEXT PRIMARY KEY, " +
            table_type + " TEXT DEFAULT NULL," +
            inReady + " INTEGER  DEFAULT 1," +
            inLastDateTime + " DATETIME  DEFAULT NULL," +
            outReady + " INTEGER  DEFAULT 1," +
            outLastDateTime + " DATETIME  DEFAULT NULL," +
            user_login + " TEXT  NOT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public SyncDataTable(Context context) {
        this.context = context;
    }

    public SyncDataTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public List<SyncData> fetchAllRecord() {
        List<SyncData> returnData = new ArrayList<>();
        String[] columns = new String[]{All_table_name, table_type, inReady, inLastDateTime, outReady, outLastDateTime,user_login};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new SyncData(
                        cursor.getString(cursor.getColumnIndex(this.All_table_name)),
                        cursor.getString(cursor.getColumnIndex(this.table_type)),
                        cursor.getInt(cursor.getColumnIndex(this.inReady)),
                        cursor.getString(cursor.getColumnIndex(this.inLastDateTime)),
                        cursor.getInt(cursor.getColumnIndex(this.outReady)),
                        cursor.getString(cursor.getColumnIndex(this.outLastDateTime)),
                        cursor.getString(cursor.getColumnIndex(this.user_login))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public SyncData fetchTableNameRecord(String tableName) {
        String[] columns = new String[]{All_table_name, table_type, inReady, inLastDateTime, outReady, outLastDateTime,user_login};
        Cursor cursor = database.query(TABLE_NAME, columns, All_table_name + "=?", new String[]{tableName}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return new SyncData(
                        cursor.getString(cursor.getColumnIndex(this.All_table_name)),
                        cursor.getString(cursor.getColumnIndex(this.table_type)),
                        cursor.getInt(cursor.getColumnIndex(this.inReady)),
                        cursor.getString(cursor.getColumnIndex(this.inLastDateTime)),
                        cursor.getInt(cursor.getColumnIndex(this.outReady)),
                        cursor.getString(cursor.getColumnIndex(this.outLastDateTime)),
                        cursor.getString(cursor.getColumnIndex(this.user_login))
                );
            } while (cursor.moveToNext());
        }
        return null;
    }

    //todo fech record who not post two the server
    public boolean fetchWhoNotPostToServer() {
        String[] columns = new String[]{All_table_name, table_type, inReady, inLastDateTime, outReady, outLastDateTime};
        Cursor cursor = database.query(TABLE_NAME, columns, outReady + "=? AND table_type!=?", new String[]{ "1","master"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public long insert(String All_table_name, String table_type, String inLastDateTime,String user_login) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.All_table_name, All_table_name);
        contentValue.put(this.table_type, table_type);
        contentValue.put(this.inLastDateTime, inLastDateTime);
        contentValue.put(this.user_login, user_login);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    // todo all tables in data from server done.
    public int updateDeactivate(String All_table_name, int inReady, String inLastDateTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.inReady, inReady);
        contentValues.put(this.inLastDateTime, inLastDateTime);
        int i = database.update(TABLE_NAME, contentValues, this.All_table_name + " = '" + All_table_name + "'", null);
        return i;
    }

    public int updateActivate(String All_table_name, int inReady) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.inReady, inReady);
        int i = database.update(TABLE_NAME, contentValues, this.All_table_name + " = '" + All_table_name + "'", null);
        return i;
    }
//todo data out ready
    public int OutDeactivate(String All_table_name, int outReady, String inLastDateTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.outReady, outReady);
        contentValues.put(this.inLastDateTime, inLastDateTime);
        int i = database.update(TABLE_NAME, contentValues, this.All_table_name + " = '" + All_table_name + "'", null);
        return i;
    }

    public int OutActivate(String All_table_name, int outReady) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.outReady, outReady);
        int i = database.update(TABLE_NAME, contentValues, this.All_table_name + " = '" + All_table_name + "'", null);
        return i;
    }

//    todo  out

    public class SyncData {
        public String All_table_name;
        public String table_type;
        public int inReady;
        public String inLastDateTime;
        public int outReady;
        public String outLastDateTime;
        public String user_login;

        public SyncData(String All_table_name, String table_type, int inReady, String inLastDateTime, int outReady, String outLastDateTime,String user_login) {
            this.All_table_name = All_table_name;
            this.table_type = table_type;
            this.inReady = inReady;
            this.inLastDateTime = inLastDateTime;
            this.outReady = outReady;
            this.outLastDateTime = outLastDateTime;
            this.user_login=user_login;
        }
    }
}
