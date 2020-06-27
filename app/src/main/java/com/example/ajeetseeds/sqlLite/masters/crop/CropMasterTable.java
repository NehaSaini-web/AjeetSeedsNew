package com.example.ajeetseeds.sqlLite.masters.crop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CropMasterTable {
    // Table Name
    public static final String TABLE_NAME = "crop_master";
    // Table columns
    public static final String code = "code";
    public static final String description = "description";
    public static final String image_url = "image_url";
    public static final String active = "active";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            code + " TEXT PRIMARY KEY," +
            description + " TEXT NOT NULL," +
            image_url + " TEXT NOT NULL," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CropMasterTable(Context context) {
        this.context = context;
    }

    public CropMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(CropMasterModel cropMasterModel) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.code, cropMasterModel.code);
            contentValue.put(this.description, cropMasterModel.description);
            contentValue.put(this.image_url, cropMasterModel.image_url);
            contentValue.put(this.active, cropMasterModel.active);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }


    public void insertArray(List<CropMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (CropMasterModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.code, data.code);
                contentValue.put(this.description, data.description);
                contentValue.put(this.image_url, data.image_url);
                contentValue.put(this.active, data.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<CropMasterModel> fetch() {
        List<CropMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{code, description, image_url,active};
        Cursor cursor = database.query(TABLE_NAME, columns, this.active+"="+0, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
           do{
                returnData.add(new CropMasterModel(
                        cursor.getString(cursor.getColumnIndex(this.code)),
                        cursor.getString(cursor.getColumnIndex(this.description)),
                        cursor.getString(cursor.getColumnIndex(this.image_url)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public String getCropName(String code) {
        List<CropMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{description};
        Cursor cursor = database.query(TABLE_NAME, columns, this.code+"='"+code+"'", null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                        return cursor.getString(cursor.getColumnIndex(this.description));
            } while (cursor.moveToNext());
        }
        return "";
    }

    public void delete(String code) {
        database.delete(TABLE_NAME, this.code + "=" + code, null);
    }

    public class CropMasterModel {
        public String code;
        public String description;
        public String image_url;
        public int active;

        public CropMasterModel(String code, String description, String image_url,int active) {
            this.code = code;
            this.description = description;
            this.image_url = image_url;
            this.active=active;
        }
    }
}
