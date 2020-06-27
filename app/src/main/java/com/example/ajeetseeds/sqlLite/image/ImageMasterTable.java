package com.example.ajeetseeds.sqlLite.image;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ImageMasterTable {
    // Table Name
    public static final String TABLE_NAME = "image_master";
    // Table columns
    public static final String id = "id";
    public static final String code = "code";
    public static final String image_url = "image_url";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER NOT NULL PRIMARY KEY, " +
            code + " TEXT NOT NULL, " +
            image_url + " TEXT NOT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public ImageMasterTable(Context context) {
        this.context = context;
    }

    public ImageMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public long insert(ImageMasterModel data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(this.code, data.code);
        contentValue.put(this.id, data.id);
        contentValue.put(this.image_url, data.image_url);
        return database.replace(TABLE_NAME, null, contentValue);
    }

    public void insertArray(List<ImageMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (ImageMasterModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, data.code);
                contentValue.put(this.id, data.id);
                contentValue.put(this.image_url, data.image_url);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<String> fetch_bycode(String code) {
        List<String> returnData = new ArrayList<>();
        String[] columns = new String[]{this.image_url};
        Cursor cursor = database.query(TABLE_NAME, columns, this.code + "='" + code + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(cursor.getString(cursor.getColumnIndex(this.image_url)));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public void delete(String code) {
        database.delete(TABLE_NAME, this.code + "='" + code+"'", null);
    }

    public class ImageMasterModel {
        public boolean condition;
        public int id;
        public String code;
        public String image_url;

        public ImageMasterModel(int id, String code, String image_url) {
            this.id = id;
            this.code = code;
            this.image_url = image_url;
        }
    }
}
