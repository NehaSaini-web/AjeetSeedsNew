package com.example.ajeetseeds.sqlLite;

import java.util.List;

public class AllTablesName {
    //todo master tables pass table type master
    public static String Geographical_SetupTable = "Geographical_Setup";
    public static String ZoneMasterTable = "zone_master";
    public static String StateMasterTable = "state_master";
    public static String RegionMasterTable = "region_master";
    public static String DistrictMasterTable = "district_master";
    public static String TalukaMasterTable = "taluka_master";

    //todo daily activity table
    public static final String DailyActivityHeader = "daily_activity_header";
    public static final String DailyActivityLine = "daily_activity_Lines";
    //todo Order Booking Model
    public static final String OrderBooking_header = "order_header";
    public static final String OrderBookLine = "order_line";

    //crop master
    public static final String CropMasterTable = "crop_master";
    public static final String CropItemMasterTable = "crops_item_master";
    public static final String CustomerMasterTable = "customer_master";

    //todo event
    public static final String EventTypeMasterTable = "event_type_master";
    public static final String EventManagementTable = "event_management";
    public static final String EventManagementExpenseLineTable = "event_management_expense_line";
    public static final String imageMasterModule = "image_master";

    //todo city master
    public static final String CityMasterTable = "city_master";
    public static final String ModeOfTravelMasterTable = "mode_of_travel";
    public static final String TravelHeaderTable="travel_header";
    public static final String TravelLineExpenseTable="travel_line_expanse";

    //todo delete all Record where active=1
    public static String[] activeRecordTable = {
            Geographical_SetupTable,
            ZoneMasterTable,
            StateMasterTable,
            RegionMasterTable,
            DistrictMasterTable,
            TalukaMasterTable,
            CropMasterTable,
            CropItemMasterTable,
            CustomerMasterTable
    };

}
