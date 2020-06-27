package com.example.ajeetseeds.sqlLite.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityHeader;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityLine;
import com.example.ajeetseeds.sqlLite.event.EventManagementExpenseLineTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.event.EventTypeMaster;
import com.example.ajeetseeds.sqlLite.image.ImageMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.Geographical_SetupTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.RegionMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.ZoneMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.ModeOfTravelMasterTable;
import com.example.ajeetseeds.sqlLite.travel.TravelHeaderTable;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Information
   public static final String DB_NAME = "AJEET_SEEDS.DB";
    // database version
    static final int DB_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SyncDataTable.CREATE_TABLE);  //todo hold all table name who sunc or not

        db.execSQL(Geographical_SetupTable.CREATE_TABLE);  //todo Geographical_Setup
        db.execSQL(ZoneMasterTable.CREATE_TABLE);  //todo zone_master
        db.execSQL(StateMasterTable.CREATE_TABLE);  //todo state_master
        db.execSQL(TalukaMasterTable.CREATE_TABLE);  //todo taluka_master
        db.execSQL(DistrictMasterTable.CREATE_TABLE);  //todo district_master
        db.execSQL(RegionMasterTable.CREATE_TABLE);  //todo region_master

        //todo crop master
        db.execSQL(CropMasterTable.CREATE_TABLE);  //todo Crop Master Table
        db.execSQL(CropItemMasterTable.CREATE_TABLE);  //todo Crop item Master Table
        db.execSQL(CustomerMasterTable.CREATE_TABLE);  //todo Customer Master Table

        db.execSQL(DailyActivityHeader.CREATE_TABLE);  //todo daily activity header
        db.execSQL(DailyActivityLine.CREATE_TABLE);  //todo daily activity Line

        //todo order booking
        db.execSQL(OrderBookHeader.CREATE_TABLE);  //todo order booking header
        db.execSQL(OrderBookLine.CREATE_TABLE);  //todo order booking Line

        //todo event
        db.execSQL(EventTypeMaster.CREATE_TABLE);  //todo event type
        db.execSQL(EventManagementTable.CREATE_TABLE);  //todo Event Create
        db.execSQL(EventManagementExpenseLineTable.CREATE_TABLE);  //todo Event Expense Create
        db.execSQL(ImageMasterTable.CREATE_TABLE); //todo image master

        //todo travel
        db.execSQL(CityMasterTable.CREATE_TABLE);//todo city master
        db.execSQL(ModeOfTravelMasterTable.CREATE_TABLE);//todo Mode of travel Travel
        db.execSQL(TravelHeaderTable.CREATE_TABLE);//todo  Travel Header
        db.execSQL(TravelLineExpenseTable.CREATE_TABLE);//todo  Travel Line Expense
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(Geographical_SetupTable.DROP_TABLE);
        // db.execSQL(ZoneMasterTable.DROP_TABLE);
        // db.execSQL(StateMasterTable.DROP_TABLE);
        onCreate(db);
    }
}