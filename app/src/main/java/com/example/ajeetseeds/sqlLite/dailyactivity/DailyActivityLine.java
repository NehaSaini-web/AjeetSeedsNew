package com.example.ajeetseeds.sqlLite.dailyactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DailyActivityLine {
    // Table Name
    public static final String TABLE_NAME = "daily_activity_Lines";
    // Table columns
    public static final String id = "id";
    public static final String android_activity_no = "android_activity_no";
    public static final String activity_no = "activity_no";

    public static final String farmer_name = "farmer_name";
    public static final String district = "district";
    public static final String village = "village";

    public static final String ajeet_crop_and_varity = "ajeet_crop_and_verity";
    public static final String ajeet_crop_age = "ajeet_crop_age";
    public static final String ajeet_fruits_per = "ajeet_fruits_per";
    public static final String ajeet_pest = "ajeet_pest";
    public static final String ajeet_disease = "ajeet_disease";

    public static final String check_crop_and_variety = "check_crop_and_verity";
    public static final String check_crop_age = "check_crop_age";
    public static final String check_fruits_per = "check_fruits_per";
    public static final String check_pest = "check_pest";
    public static final String check_disease = "check_disease";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            android_activity_no + " TEXT NOT NULL," +
            activity_no + " TEXT NULL," +
            farmer_name + " TEXT NULL," +
            district + " TEXT NULL," +
            village + " TEXT NULL," +

            ajeet_crop_and_varity + " TEXT NULL," +
            ajeet_crop_age + " TEXT NULL," +
            ajeet_fruits_per + " TEXT NULL," +
            ajeet_pest + " TEXT NULL," +
            ajeet_disease + " TEXT NULL," +

            check_crop_and_variety + " TEXT NULL," +
            check_crop_age + " TEXT NULL," +
            check_fruits_per + " TEXT NULL," +
            check_pest + " TEXT NULL," +
            check_disease + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DailyActivityLine(Context context) {
        this.context = context;
    }

    public DailyActivityLine open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public long insert(DailyActivityLinesModel dailyActivityLinesModel) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_activity_no, dailyActivityLinesModel.android_activity_no);
            contentValue.put(this.activity_no, dailyActivityLinesModel.activity_no);
            contentValue.put(this.farmer_name, dailyActivityLinesModel.farmer_name);
            contentValue.put(this.district, dailyActivityLinesModel.district);
            contentValue.put(this.village, dailyActivityLinesModel.village);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    public boolean insertBulkData(List<DailyActivityLinesModel> dailyActivityLinesModel) {
        database.beginTransaction();
        try {
            for(DailyActivityLinesModel data:dailyActivityLinesModel) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.android_activity_no, data.android_activity_no);
                contentValue.put(this.activity_no, data.activity_no);
                contentValue.put(this.farmer_name, data.farmer_name);
                contentValue.put(this.district, data.district);
                contentValue.put(this.village, data.village);

                contentValue.put(this.ajeet_crop_and_varity, data.ajeet_crop_and_varity);
                contentValue.put(this.ajeet_crop_age, data.ajeet_crop_age);
                contentValue.put(this.ajeet_fruits_per, data.ajeet_fruits_per);
                contentValue.put(this.ajeet_pest, data.ajeet_pest);
                contentValue.put(this.ajeet_disease, data.ajeet_disease);

                contentValue.put(this.check_crop_and_variety, data.check_crop_and_variety);
                contentValue.put(this.check_crop_age, data.check_crop_age);
                contentValue.put(this.check_fruits_per, data.check_fruits_per);
                contentValue.put(this.check_pest, data.check_pest);
                contentValue.put(this.check_disease, data.check_disease);
                database.insert(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
            return true;
        }catch (Exception e){
            return false;
        }finally {
            database.endTransaction();
        }
    }

    public List<DailyActivityLinesModel> fetch(String and_acivityno) {
        List<DailyActivityLinesModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_activity_no, activity_no, farmer_name, district, village,
                ajeet_crop_and_varity, ajeet_crop_age, ajeet_fruits_per, ajeet_pest, ajeet_disease,
                check_crop_and_variety, check_crop_age, check_fruits_per, check_pest, check_disease};
        Cursor cursor = database.query(TABLE_NAME, columns, android_activity_no+"=?", new String[]{and_acivityno}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DailyActivityLinesModel(
                        cursor.getString(cursor.getColumnIndex(this.android_activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_name)),
                        cursor.getString(cursor.getColumnIndex(this.district)),
                        cursor.getString(cursor.getColumnIndex(this.village)),

                        cursor.getString(cursor.getColumnIndex(this.ajeet_crop_and_varity)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_crop_age)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_fruits_per)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_pest)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_disease)),

                        cursor.getString(cursor.getColumnIndex(this.check_crop_and_variety)),
                        cursor.getString(cursor.getColumnIndex(this.check_crop_age)),
                        cursor.getString(cursor.getColumnIndex(this.check_fruits_per)),
                        cursor.getString(cursor.getColumnIndex(this.check_pest)),
                        cursor.getString(cursor.getColumnIndex(this.check_disease))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public List<DailyActivityLinesModel> fetch_by_acivityno(String acivityno) {
        List<DailyActivityLinesModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_activity_no, activity_no, farmer_name, district, village,
                ajeet_crop_and_varity, ajeet_crop_age, ajeet_fruits_per, ajeet_pest, ajeet_disease,
                check_crop_and_variety, check_crop_age, check_fruits_per, check_pest, check_disease};
        Cursor cursor = database.query(TABLE_NAME, columns, activity_no+"=?", new String[]{acivityno}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new DailyActivityLinesModel(
                        cursor.getString(cursor.getColumnIndex(this.android_activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.activity_no)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_name)),
                        cursor.getString(cursor.getColumnIndex(this.district)),
                        cursor.getString(cursor.getColumnIndex(this.village)),

                        cursor.getString(cursor.getColumnIndex(this.ajeet_crop_and_varity)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_crop_age)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_fruits_per)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_pest)),
                        cursor.getString(cursor.getColumnIndex(this.ajeet_disease)),

                        cursor.getString(cursor.getColumnIndex(this.check_crop_and_variety)),
                        cursor.getString(cursor.getColumnIndex(this.check_crop_age)),
                        cursor.getString(cursor.getColumnIndex(this.check_fruits_per)),
                        cursor.getString(cursor.getColumnIndex(this.check_pest)),
                        cursor.getString(cursor.getColumnIndex(this.check_disease))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update(String android_activity_no, String activity_no) {   //todo update activity no when data is going to post to the server
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.activity_no, activity_no);
            int i = database.update(TABLE_NAME, contentValues, this.android_activity_no + " = " + android_activity_no, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }


    public void delete(String activity_no) {
        database.delete(TABLE_NAME, this.activity_no + "=" + activity_no, null);
    }

    public class DailyActivityLinesModel {
        public String android_activity_no;
        public String activity_no;

        public String farmer_name;
        public String district;
        public String village;

        public String ajeet_crop_and_varity;
        public String ajeet_crop_age;
        public String ajeet_fruits_per;
        public String ajeet_pest;
        public String ajeet_disease;

        public String check_crop_and_variety;
        public String check_crop_age;
        public String check_fruits_per;
        public String check_pest;
        public String check_disease;

        public DailyActivityLinesModel(String android_activity_no, String activity_no, String farmer_name, String district, String village,
                                       String ajeet_crop_and_varity, String ajeet_crop_age, String ajeet_fruits_per, String ajeet_pest, String ajeet_disease,
                                       String check_crop_and_variety, String check_crop_age, String check_fruits_per, String check_pest, String check_disease) {
            this.android_activity_no = android_activity_no;
            this.activity_no = activity_no;
            this.farmer_name = farmer_name;
            this.district = district;
            this.village = village;

            this.ajeet_crop_and_varity = ajeet_crop_and_varity;
            this.ajeet_crop_age = ajeet_crop_age;
            this.ajeet_fruits_per = ajeet_fruits_per;
            this.ajeet_pest = ajeet_pest;
            this.ajeet_disease = ajeet_disease;

            this.check_crop_and_variety = check_crop_and_variety;
            this.check_crop_age = check_crop_age;
            this.check_fruits_per = check_fruits_per;
            this.check_pest = check_pest;
            this.check_disease = check_disease;
        }
    }
}
