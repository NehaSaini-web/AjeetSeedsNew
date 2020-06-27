package com.example.ajeetseeds.Model;

public class StaticDataForApp {
    // TODO: 11-Sep-19 global Domain name where all hit goes to that domain....
    public static String globalurl = "https://ziv-android.pristinefulfil.com";

    public static String androidExceptionLog_UIException = globalurl + "/api/BaseException/UIException";
    public static String notificationSignalR = "/Notification";
    public static String versioncodeapiurl = globalurl + "/api/Version/CheckForUpdate?type=android";
    public static String syncAllTableData = globalurl + "/api/Version/syncAllTableData";

    //todo login urls
    public static String login = globalurl + "/api/User/login";
    public static String Logout = globalurl + "/api/User/Logout";

    //todo sync master api
    public static String GeographicalSetupData = globalurl + "/api/GeographicalSetup/GeographicalSetupData?lastSync=";
    public static String getCropAndItemMasterSync = globalurl + "/api/CropMaster/getCropAndItemMasterSync?lastSync=";

    //todo daily activity Urls
    public static String dailyActivityInsertHeaderLines = globalurl + "/api/DailyActivity/dailyActivityInsertHeaderLines";
    public static String getDailyActivityDetail = globalurl + "/api/DailyActivity/getDailyActivityDetail?email=";

    //todo Order Booking
    public static String insertOrderBookHeaderLines = globalurl + "/api/OrderBook/insertOrderBookHeaderLines";
    public static String getPendingBookOrder = globalurl + "/api/OrderBook/getPendingBookOrder?email=";
    public static String approveRejectOrder = globalurl + "/api/OrderBook/approveRejectOrder";
    // todo event+
    public static String getEventDeatil = globalurl + "/api/EventManagement/getEventDeatil?flag=";
    public static String insertEventManagement = globalurl + "/api/EventManagement/insertEventManagement";
    public static String updateEventStatusDeatil = globalurl + "/api/EventManagement/updateEventStatusDeatil";
    public static String insertEventExpense = globalurl + "/api/EventManagement/insertEventExpense";

    //todo travel
    public static String getCityMaster = globalurl + "/api/Travel/getTravelDeatil?flag=";
    public static String getTravelDeatil = globalurl + "/api/Travel/getTravelDeatil?flag=";
    public static String insertTravelHeader = globalurl + "/api/Travel/insertTravelHeader";
    public static String approveRejectTravel = globalurl + "/api/Travel/approveRejectTravel";
    public static String insertTravelExpense = globalurl + "/api/Travel/insertTravelExpense";

    //todo Inspection Urls
    public static String scanProductionLotNo = globalurl + "/api/Inspection/scanProductionLotNo?production_lot_no=";
    public static String get_inspection_by_lot_arrival_plan_no = globalurl + "/api/Inspection/get_inspection_by_lot_arrival_plan_no?arrival_plan_no=";
    public static String insert_inspection_one_line = globalurl + "/api/Inspection/insert_inspection_one_line";
    public static String Complete_inspection_one = globalurl + "/api/Inspection/Complete_inspection_one?arrival_plan_no=";
    public static String delete_inspection_one = globalurl + "/api/Inspection/delete_inspection_one?line_no=";

    //todo inspecton two
    public static String insert_inspection_two_line = globalurl + "/api/Inspection/insert_inspection_two_line";
    public static String Complete_inspection_two = globalurl + "/api/Inspection/Complete_inspection_two?arrival_plan_no=";
    public static String delete_inspection_two = globalurl + "/api/Inspection/delete_inspection_two?line_no=";
    //todo inspecton three
    public static String insert_inspection_three_line = globalurl + "/api/Inspection/insert_inspection_three_line";
    public static String Complete_inspection_three = globalurl + "/api/Inspection/Complete_inspection_three?arrival_plan_no=";
    public static String delete_inspection_three = globalurl + "/api/Inspection/delete_inspection_three?line_no=";

    //todo inspecton three
    public static String insert_inspection_four_line = globalurl + "/api/Inspection/insert_inspection_four_line";
    public static String Complete_inspection_four = globalurl + "/api/Inspection/Complete_inspection_four?arrival_plan_no=";
    public static String delete_inspection_four = globalurl + "/api/Inspection/delete_inspection_four?line_no=";


    //todo inspecton QC
    public static String insert_inspection_QC_line = globalurl + "/api/Inspection/insert_inspection_QC_line";
    public static String Complete_inspection_QC = globalurl + "/api/Inspection/Complete_inspection_QC?arrival_plan_no=";
    public static String delete_inspection_QC = globalurl + "/api/Inspection/delete_inspection_QC?line_no=";
}