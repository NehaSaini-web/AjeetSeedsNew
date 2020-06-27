package com.example.ajeetseeds.sqlLite.travel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ModeOfTravelMasterTable {
    // Table Name
    public static final String TABLE_NAME = "mode_of_travel";
    // Table columns
    public static final String id = "id";
    public static final String grade = "grade";
    public static final String cities = "cities";
    public static final String travel_mode = "travel_mode";
    public static final String lodging = "lodging";
    public static final String da_half = "da_half";
    public static final String da_full = "da_full";
    public static final String ope_max = "ope_max";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER NOT NULL ," +
            grade + " TEXT NOT NULL," +
            cities + " TEXT NOT NULL," +
            travel_mode + " TEXT NOT NULL," +
            lodging + " INTEGER NOT NULL," +
            da_half + " INTEGER NOT NULL," +
            da_full + " INTEGER NOT NULL," +
            ope_max + " INTEGER NOT NULL," +
            " PRIMARY KEY(" + id + "," + grade + "," + cities + ")" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public ModeOfTravelMasterTable(Context context) {
        this.context = context;
    }

    public ModeOfTravelMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(ModeOfTravelModel data) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.id, data.id);
            contentValue.put(this.grade, data.grade);
            contentValue.put(this.cities, data.cities);
            contentValue.put(this.travel_mode, data.travel_mode);
            contentValue.put(this.lodging, data.lodging);
            contentValue.put(this.da_half, data.da_half);
            contentValue.put(this.da_full, data.da_full);
            contentValue.put(this.ope_max, data.ope_max);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }


    public void insertArray(List<ModeOfTravelModel> bulkdata) {
        database.beginTransaction();
        try {
            for (ModeOfTravelModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.id, data.id);
                contentValue.put(this.grade, data.grade);
                contentValue.put(this.cities, data.cities);
                contentValue.put(this.travel_mode, data.travel_mode);
                contentValue.put(this.lodging, data.lodging);
                contentValue.put(this.da_half, data.da_half);
                contentValue.put(this.da_full, data.da_full);
                contentValue.put(this.ope_max, data.ope_max);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<ModeOfTravelModel> fetch() {
        List<ModeOfTravelModel> returnData = new ArrayList<>();
        String[] columns = new String[]{id, grade, cities, travel_mode, lodging, da_half, da_full, ope_max};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new ModeOfTravelModel(
                        cursor.getInt(cursor.getColumnIndex(id)),
                        cursor.getString(cursor.getColumnIndex(grade)),
                        cursor.getString(cursor.getColumnIndex(cities)),
                        cursor.getString(cursor.getColumnIndex(travel_mode)),
                        cursor.getFloat(cursor.getColumnIndex(lodging)),
                        cursor.getFloat(cursor.getColumnIndex(da_half)),
                        cursor.getFloat(cursor.getColumnIndex(da_full)),
                        cursor.getFloat(cursor.getColumnIndex(ope_max))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public ModeOfTravelModel fetchModeOfTravel(String cityCode, String grade) {
        Cursor cursor = database.rawQuery("select * from mode_of_travel WHERE grade='" + grade + "' AND " +
                "cities=(select cm.class_of_city from city_master cm WHERE code='" + cityCode + "') LIMIT 1", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return new ModeOfTravelModel(
                        cursor.getInt(cursor.getColumnIndex(this.id)),
                        cursor.getString(cursor.getColumnIndex(this.grade)),
                        cursor.getString(cursor.getColumnIndex(this.cities)),
                        cursor.getString(cursor.getColumnIndex(this.travel_mode)),
                        cursor.getFloat(cursor.getColumnIndex(this.lodging)),
                        cursor.getFloat(cursor.getColumnIndex(this.da_half)),
                        cursor.getFloat(cursor.getColumnIndex(this.da_full)),
                        cursor.getFloat(cursor.getColumnIndex(this.ope_max))
                );
            } while (cursor.moveToNext());
        }
        return null;
    }

    public List<String> getModeOfTravelDropDown(String userGrade) {
        List<String> returnData = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT DISTINCT " +
                "mot.travel_mode " +
                " FROM mode_of_travel mot WHERE mot.grade='" + userGrade + "'" +
                " OR mot.id>(SELECT mot2.id FROM mode_of_travel mot2 WHERE mot2.grade='" + userGrade + "' LIMIT 1)", null);
        ;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(cursor.getString(cursor.getColumnIndex("travel_mode")));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public class ModeOfTravelModel {
        public boolean condition;
        public int id;
        public String grade;
        public String cities;
        public String travel_mode;
        public float lodging;
        public float da_half;
        public float da_full;
        public float ope_max;


        public ModeOfTravelModel(int id, String grade, String cities, String travel_mode, float lodging, float da_half, float da_full,
                                 float ope_max) {
            this.id = id;
            this.grade = grade;
            this.cities = cities;
            this.travel_mode = travel_mode;
            this.lodging = lodging;
            this.da_half = da_half;
            this.da_full = da_full;
            this.ope_max = ope_max;
        }
    }
}
