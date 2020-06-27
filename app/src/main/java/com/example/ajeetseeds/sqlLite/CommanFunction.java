package com.example.ajeetseeds.sqlLite;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

public class CommanFunction {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CommanFunction(Context context) {
        this.context = context;
    }

    public CommanFunction open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public ManualSyncModel getManualSyncData() {
        Cursor cursor = database.rawQuery("SELECT (SELECT count(1) FROM Geographical_Setup gs) AS geographical_setup," +
                "(SELECT count(1) FROM zone_master zm) AS zone_master," +
                "(SELECT count(1) FROM state_master sm) AS state_master," +
                "(SELECT count(1) FROM region_master rm) AS region_master," +
                "(SELECT count(1) FROM district_master dm) AS district_master," +
                "(SELECT count(1) FROM taluka_master tm) AS taluka_master," +
                "(SELECT count(1) FROM crop_master cm) AS crop_master," +
                "(SELECT count(1) FROM crops_item_master cim) AS crops_item_master," +
                "(SELECT count(1) FROM customer_master cm) AS customer_master," +
                "(SELECT count(1) FROM city_master cm) AS city_master," +
                "(SELECT count(1) FROM mode_of_travel mot) AS mode_of_travel", null);
        ;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return new ManualSyncModel(
                        cursor.getString(cursor.getColumnIndex("geographical_setup")),
                        cursor.getString(cursor.getColumnIndex("zone_master")),
                        cursor.getString(cursor.getColumnIndex("state_master")),
                        cursor.getString(cursor.getColumnIndex("region_master")),
                        cursor.getString(cursor.getColumnIndex("district_master")),
                        cursor.getString(cursor.getColumnIndex("taluka_master")),
                        cursor.getString(cursor.getColumnIndex("crop_master")),
                        cursor.getString(cursor.getColumnIndex("crops_item_master")),
                        cursor.getString(cursor.getColumnIndex("customer_master")),
                        cursor.getString(cursor.getColumnIndex("city_master")),
                        cursor.getString(cursor.getColumnIndex("mode_of_travel"))
                );
            } while (cursor.moveToNext());
        }
        return null;
    }

    public class ManualSyncModel {
        public boolean condition;
        public String geographical_setup;
        public String zone_master;
        public String state_master;

        public String region_master;
        public String district_master;
        public String taluka_master;
        public String crop_master;
        public String crops_item_master;
        public String customer_master;

        public String city_master;
        public String mode_of_travel;

        public ManualSyncModel(String geographical_setup, String zone_master, String state_master,
                               String region_master, String district_master, String taluka_master,
                               String crop_master, String crops_item_master, String customer_master,String city_master,String mode_of_travel) {
            this.geographical_setup = geographical_setup;
            this.zone_master = zone_master;
            this.state_master = state_master;

            this.region_master = region_master;
            this.district_master = district_master;
            this.taluka_master = taluka_master;
            this.crop_master = crop_master;
            this.crops_item_master = crops_item_master;
            this.customer_master = customer_master;

            this.city_master=city_master;
            this.mode_of_travel=mode_of_travel;
        }
    }
}
