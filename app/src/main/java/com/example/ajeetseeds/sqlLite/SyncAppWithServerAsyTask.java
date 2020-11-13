package com.example.ajeetseeds.sqlLite;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.dailyActivity.DailyActivityResponse;
import com.example.ajeetseeds.Model.event.EventCreateResponseModel;
import com.example.ajeetseeds.Model.event.EventTypeResponseModel;
import com.example.ajeetseeds.Model.event.SyncEventDetailModel;
import com.example.ajeetseeds.Model.orderbook.OrderBookResponseModel;
import com.example.ajeetseeds.Model.syncModel.CropMasterSyncModel;
import com.example.ajeetseeds.Model.syncModel.Geographical_SetupSyncModel;
import com.example.ajeetseeds.Model.travel.SyncTravelDetailModel;
import com.example.ajeetseeds.Model.travel.TravelExpenseResponseModel;
import com.example.ajeetseeds.Model.travel.TravelHeaderResponse;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
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
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseModel;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseTable;
import com.example.ajeetseeds.ui.dailyActivity.viewDailyActivity.DailyActivityResponseModel;
import com.example.ajeetseeds.ui.order_creation.order_approval.OrderApprovalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncAppWithServerAsyTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    public static boolean hitIsProcessing;
    SessionManagement sessionManagement;
    GlobalPostingMethod globalPostingMethod = new GlobalPostingMethod();

    String initDate = "2019-02-20T00:00:42.387";
    public static int timercounter = 0;

    public SyncAppWithServerAsyTask(Activity activity) {
        this.activity = activity;
        sessionManagement = new SessionManagement(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    SyncDataTable syncDataTable;

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            if (!hitIsProcessing) {
                hitIsProcessing = true;
                SyncDataProcessing();
            }

        } catch (Exception e) {

        } finally {
            hitIsProcessing = false;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    void SyncDataProcessing() {
        try {
            syncDataTable = new SyncDataTable(activity.getApplicationContext());
            syncDataTable.open();
            List<SyncDataTable.SyncData> synctableList = syncDataTable.fetchAllRecord();
            if (synctableList != null && synctableList.size() > 0) {
                Log.e("Run", "Sync Table Data Found.");
                //todo check All  table redy for in or an out process
                SyncTableOneByOneFrom_SyncDataTable(synctableList);
                NotificationSyncDataStop();
                //todo Reset
                BatchRedyForResetSync();
            } else {
                NotificationSyncDataStart();
                Log.e("Run", "data not found so push the all TableName In here");
                syncDataTable.insert(AllTablesName.Geographical_SetupTable, "master", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.CropMasterTable, "master", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.EventTypeMasterTable, "master", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.DailyActivityHeader, "", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.OrderBooking_header, "", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.EventManagementTable, "", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.CityMasterTable, "master", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.TravelHeaderTable, "", initDate, sessionManagement.getUserEmail());
                syncDataTable.insert(AllTablesName.ModeOfTravelMasterTable, "master", initDate, sessionManagement.getUserEmail());
            }
        } catch (Exception e) {
            Log.e("Sync Error", e.getMessage());
        } finally {
            syncDataTable.close();
        }
    }

    void BatchRedyForResetSync() {
        try {
            Thread.sleep(5000);
            timercounter += 5000;
            if (timercounter >= 60000) {
                timercounter = 0;
                NotificationSyncDataStart();
                deleteActiveOneRecordForAllTables();
                syncDataTable.updateActivate(AllTablesName.Geographical_SetupTable, 1);  // todo  reset in process when every 1houre like seduler
                syncDataTable.updateActivate(AllTablesName.CropMasterTable, 1);  // todo  reset in process when every 1houre like seduler
                syncDataTable.updateActivate(AllTablesName.EventTypeMasterTable, 1);  // todo  reset in Event process when every 1houre like seduler
            }
        } catch (Exception e) {

        }
    }

    void SyncTableOneByOneFrom_SyncDataTable(List<SyncDataTable.SyncData> synctableList) {
        try {
            for (SyncDataTable.SyncData data : synctableList) {
                //todo check geografical master and related other tables
                if (data.All_table_name.equalsIgnoreCase(AllTablesName.Geographical_SetupTable) && data.inReady == 1) {
                    syncDataTable.updateDeactivate(AllTablesName.Geographical_SetupTable, 0,
                            DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    //todo sync master
                    checkMasterTableUpdate(data.inLastDateTime);
                }
                //todo crop master sync
                else if (data.All_table_name.equalsIgnoreCase(AllTablesName.CropMasterTable) && data.inReady == 1) {
                    syncDataTable.updateDeactivate(AllTablesName.CropMasterTable, 0,
                            DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    //todo sync master
                    checkCropMasterUpdate(data.inLastDateTime);
                }
                //todo sync Daily activity in sync table
                else if (data.All_table_name.equalsIgnoreCase(AllTablesName.DailyActivityHeader)) {
                    if (data.inReady == 1) {
                        syncDataTable.updateDeactivate(AllTablesName.DailyActivityHeader, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_DailyActivitData("InDataFromServer");
                    }
                    if (data.outReady == 1) {
                        syncDataTable.OutDeactivate(AllTablesName.DailyActivityHeader, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_DailyActivitData("OutDataFromServer");
                    }
                }
                //todo sync order Book in  table
                else if (data.All_table_name.equalsIgnoreCase(AllTablesName.OrderBooking_header)) {
                    if (data.inReady == 1) {
                        syncDataTable.updateDeactivate(AllTablesName.OrderBooking_header, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_OrderBookingData("InDataFromServer");
                    }
                    if (data.outReady == 1) {
                        syncDataTable.OutDeactivate(AllTablesName.OrderBooking_header, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_OrderBookingData("OutDataFromServer");
                    }
                }
                //todo sync event inset  table
                else if (data.All_table_name.equalsIgnoreCase(AllTablesName.EventTypeMasterTable) && data.inReady == 1) {
                    syncDataTable.OutDeactivate(AllTablesName.EventTypeMasterTable, 0,
                            DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    Sync_EventBookingData("InsertEventType");
                }
                //todo sync event inset  table
                else if (data.All_table_name.equalsIgnoreCase(AllTablesName.EventManagementTable)) {
                    if (data.inReady == 1) {
                        syncDataTable.updateDeactivate(AllTablesName.EventManagementTable, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_EventBookingData("InDataFromServer");
                    }
                    if (data.outReady == 1) {
                        syncDataTable.OutDeactivate(AllTablesName.EventManagementTable, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_EventBookingData("OutDataFromServer");
                    }
                } else if (data.All_table_name.equalsIgnoreCase(AllTablesName.CityMasterTable) && data.inReady == 1) {
                    syncDataTable.updateDeactivate(AllTablesName.CityMasterTable, 0,
                            DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    Sync_CityMasterData();
                } else if (data.All_table_name.equalsIgnoreCase(AllTablesName.TravelHeaderTable)) {
                    if (data.inReady == 1) {
                        syncDataTable.updateDeactivate(AllTablesName.TravelHeaderTable, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_TravelData("InDataFromServer");
                    }
                    if (data.outReady == 1) {
                        syncDataTable.OutDeactivate(AllTablesName.TravelHeaderTable, 0,
                                DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                        Sync_TravelData("OutDataFromServer");
                    }
                } else if (data.All_table_name.equalsIgnoreCase(AllTablesName.ModeOfTravelMasterTable) && data.inReady == 1) {
                    syncDataTable.updateDeactivate(AllTablesName.ModeOfTravelMasterTable, 0,
                            DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    Sync_ModeOfTravelMasterTable();
                }
            }
        } catch (Exception e) {

        }
    }

    void checkMasterTableUpdate(String lastSyncdatetime) {
        try {
            //todo get data from server...
            Geographical_SetupSyncModel geographical_setupSyncModeldata = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.GeographicalSetupData + lastSyncdatetime)).getJsonResponse(), Geographical_SetupSyncModel.class);
            if (geographical_setupSyncModeldata != null) {
                if (geographical_setupSyncModeldata.geographical_Setup.size() > 0) {
                    Geographical_SetupTable geographical_setupTable = new Geographical_SetupTable(activity.getApplicationContext());
                    geographical_setupTable.open();
                    geographical_setupTable.insertArray(geographical_setupSyncModeldata.geographical_Setup);
                    geographical_setupTable.close();
                }

                //todo insert all zone from server
                if (geographical_setupSyncModeldata.zone_master.size() > 0) {
                    ZoneMasterTable zoneMasterTable = new ZoneMasterTable(activity.getApplicationContext());
                    zoneMasterTable.open();
                    zoneMasterTable.insertArray(geographical_setupSyncModeldata.zone_master);
                    zoneMasterTable.close();
                }
                //todo insert all State Master Table from server
                if (geographical_setupSyncModeldata.state_master.size() > 0) {
                    StateMasterTable stateMasterTable = new StateMasterTable(activity.getApplicationContext());
                    stateMasterTable.open();
                    stateMasterTable.insertArray(geographical_setupSyncModeldata.state_master);
                    stateMasterTable.close();
                }
                //todo insert all  Region Master Table from server
                if (geographical_setupSyncModeldata.region_master.size() > 0) {
                    RegionMasterTable regionMasterTable = new RegionMasterTable(activity.getApplicationContext());
                    regionMasterTable.open();
                    regionMasterTable.insertArray(geographical_setupSyncModeldata.region_master);
                    regionMasterTable.close();
                }
                //todo insert all District Master Table from server
                if (geographical_setupSyncModeldata.district_master.size() > 0) {
                    DistrictMasterTable districtMasterTable = new DistrictMasterTable(activity.getApplicationContext());
                    districtMasterTable.open();
                    districtMasterTable.insertArray(geographical_setupSyncModeldata.district_master);
                    districtMasterTable.close();
                }
                //todo insert all District Master Table from server
                if (geographical_setupSyncModeldata.taluka_master.size() > 0) {
                    TalukaMasterTable talukaMasterTable = new TalukaMasterTable(activity.getApplicationContext());
                    talukaMasterTable.open();
                    talukaMasterTable.insertArray(geographical_setupSyncModeldata.taluka_master);
                    talukaMasterTable.close();
                }
            }
            Log.e("Geogrphical Master Sync", "Done");
        } catch (Exception e) {
        }
    }

    void checkCropMasterUpdate(String lastSyncdatetime) {
        try {
            CropMasterSyncModel cropMasterSyncResponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCropAndItemMasterSync + lastSyncdatetime)).getJsonResponse(), CropMasterSyncModel.class);
            if (cropMasterSyncResponse != null) {
                if (cropMasterSyncResponse.crop_master.size() > 0) {
                    CropMasterTable cropMasterTable = new CropMasterTable(activity);
                    cropMasterTable.open();
                    cropMasterTable.insertArray(cropMasterSyncResponse.crop_master);
                    cropMasterTable.close();
                }
                if (cropMasterSyncResponse.crop_item_master.size() > 0) {
                    CropItemMasterTable cropItemMasterTable = new CropItemMasterTable(activity);
                    cropItemMasterTable.open();
                    cropItemMasterTable.insertArray(cropMasterSyncResponse.crop_item_master);
                    cropItemMasterTable.close();
                }
                if (cropMasterSyncResponse.customer_master.size() > 0) {
                    CustomerMasterTable customerMasterTable = new CustomerMasterTable(activity);
                    customerMasterTable.open();
                    customerMasterTable.insertArray(cropMasterSyncResponse.customer_master);
                    customerMasterTable.close();
                }
            }
            Log.e("Crop Master Sync", "Done");
        } catch (Exception e) {
            Log.e("Crop Master Error", e.getMessage());
        }
    }

    void Sync_DailyActivitData(String flag) {
        if (flag.equalsIgnoreCase("InDataFromServer")) {
            try {
                List<DailyActivityResponseModel> responseslist = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getDailyActivityDetail + sessionManagement.getUserEmail())).getJsonResponse(), new TypeToken<List<DailyActivityResponseModel>>() {
                }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition) {
                    for (DailyActivityResponseModel response : responseslist) {
                        //todo insert header
                        DailyActivityHeader dailyActivityHeader = new DailyActivityHeader(activity);
                        dailyActivityHeader.open();
                        String android_activity_no = dailyActivityHeader.getTableSequenceNo();
                        dailyActivityHeader.insert(dailyActivityHeader.new DailyActivityHeaderModel(android_activity_no, response.activity_no, response.contact_no, response.contact_no1, response.order_collected, response.payment_collected, response.updated_on));
                        dailyActivityHeader.close();
                        //todo insert line
                        DailyActivityLine dailyActivityLine = new DailyActivityLine(activity);
                        dailyActivityLine.open();
                        List<DailyActivityLine.DailyActivityLinesModel> addlines = new ArrayList<>();
                        for (DailyActivityResponseModel.DailyLineModel rsponse_D_line : response.d_line) {
                            DailyActivityLine.DailyActivityLinesModel tempObject = dailyActivityLine.
                                    new DailyActivityLinesModel(android_activity_no, rsponse_D_line.activity_no, rsponse_D_line.farmer_name,
                                    rsponse_D_line.district, rsponse_D_line.village,
                                    rsponse_D_line.ajeet_crop_and_verity, rsponse_D_line.ajeet_crop_age,
                                    rsponse_D_line.ajeet_fruits_per, rsponse_D_line.ajeet_pest, rsponse_D_line.ajeet_disease,
                                    rsponse_D_line.check_crop_and_verity, rsponse_D_line.check_crop_age,
                                    rsponse_D_line.check_fruits_per, rsponse_D_line.check_pest, rsponse_D_line.check_disease);
                            addlines.add(tempObject);
                        }
                        boolean result = dailyActivityLine.insertBulkData(addlines);
                        dailyActivityLine.close();
                    }
                }
            } catch (Exception e) {
            }
        } else if (flag.equalsIgnoreCase("OutDataFromServer")) {
            DailyActivityHeader dailyActivityHeader = new DailyActivityHeader(activity);
            dailyActivityHeader.open();
            try {
                //todo insert Heder
                List<DailyActivityHeader.DailyActivityHeaderModel> AllHeaderdataWhoNotPosted = dailyActivityHeader.fetchUnsendData();
                for (int i = 0; i < AllHeaderdataWhoNotPosted.size(); i++) {
                    //todo get header's line and send it to the server
                    DailyActivityHeader.DailyActivityHeaderModel dailyActivityHeaderModel = AllHeaderdataWhoNotPosted.get(i);  //todo single header line who not posted to the server
                    DailyActivityLine dailyActivityLine = new DailyActivityLine(activity);
                    dailyActivityLine.open();
                    List<DailyActivityLine.DailyActivityLinesModel> lineOfPerticulerHeader = dailyActivityLine.fetch(dailyActivityHeaderModel.android_activity_no);

                    JSONObject postedJson = new JSONObject();
                    postedJson.put("contact", dailyActivityHeaderModel.contact_no);
                    postedJson.put("contact1", dailyActivityHeaderModel.contact_no1);
                    postedJson.put("order_collected", dailyActivityHeaderModel.order_collected);
                    postedJson.put("payment_collected", dailyActivityHeaderModel.payment_collected);
                    postedJson.put("email_id", sessionManagement.getUserEmail());
                    JSONArray jsonArray = new JSONArray();
                    for (int j = 0; j < lineOfPerticulerHeader.size(); j++) {
                        JSONObject temp = new JSONObject();
                        temp.put("farmer_name", lineOfPerticulerHeader.get(j).farmer_name);
                        temp.put("district", lineOfPerticulerHeader.get(j).district);
                        temp.put("village", lineOfPerticulerHeader.get(j).village);

                        temp.put("ajeet_crop_and_verity", lineOfPerticulerHeader.get(j).ajeet_crop_and_varity);
                        temp.put("ajeet_crop_age", lineOfPerticulerHeader.get(j).ajeet_crop_age);
                        temp.put("ajeet_fruits_per", lineOfPerticulerHeader.get(j).ajeet_fruits_per);
                        temp.put("ajeet_pest", lineOfPerticulerHeader.get(j).ajeet_pest);
                        temp.put("ajeet_disease", lineOfPerticulerHeader.get(j).ajeet_disease);

                        temp.put("check_crop_and_verity", lineOfPerticulerHeader.get(j).check_crop_and_variety);
                        temp.put("check_crop_age", lineOfPerticulerHeader.get(j).check_crop_age);
                        temp.put("check_fruits_per", lineOfPerticulerHeader.get(j).check_fruits_per);
                        temp.put("check_pest", lineOfPerticulerHeader.get(j).check_pest);
                        temp.put("check_disease", lineOfPerticulerHeader.get(j).check_disease);
                        jsonArray.put(temp);
                    }
                    postedJson.put("addlines", jsonArray);
                    List<DailyActivityResponse> DailyActivityResponse = new Gson().fromJson(globalPostingMethod.postHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.dailyActivityInsertHeaderLines), postedJson).getJsonResponse(), new TypeToken<List<DailyActivityResponse>>() {
                    }.getType());
                    dailyActivityHeader.update(dailyActivityHeaderModel.android_activity_no, DailyActivityResponse.get(0).daily_activity_no);
                    dailyActivityLine.update(dailyActivityHeaderModel.android_activity_no, DailyActivityResponse.get(0).daily_activity_no);
                    dailyActivityLine.close();
                    //todo redy json who post the server and send it
                }
                dailyActivityHeader.close();
                Log.e("Daily Sync Success", "Done");
            } catch (Exception e) {

            } finally {
                dailyActivityHeader.close();
            }
        }
    }

    void Sync_OrderBookingData(String flag) {
        if (flag.equalsIgnoreCase("InDataFromServer")) {
            try {
                List<OrderApprovalModel> responseslist = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getPendingBookOrder + sessionManagement.getUserEmail() + "&flag=GetAllOrder")).getJsonResponse(), new TypeToken<List<OrderApprovalModel>>() {
                }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition == true) {
                    for (OrderApprovalModel response : responseslist) {
                        //todo insert Heder
                        OrderBookHeader orderBookHeader = new OrderBookHeader(activity);
                        orderBookHeader.open();
                        String android_order_no = orderBookHeader.getTableSequenceNo();
                        orderBookHeader.insert(orderBookHeader.new OrderBookHeaderModel(android_order_no, response.order_no, response.approver_email, response.user_type, response.customer_no, response.order_status, response.updated_on, response.orderline.get(0).image_url));
                        orderBookHeader.close();
                        //todo insert Liine

                        OrderBookLine orderBookLine = new OrderBookLine(activity);
                        orderBookLine.open();
                        List<OrderBookLine.OrderBookLineModel> addlines = new ArrayList<>();
                        for (OrderApprovalModel.OrderLineModel headerLines : response.orderline) {
                            OrderBookLine.OrderBookLineModel tempObject = orderBookLine.new OrderBookLineModel(android_order_no, headerLines.order_no, headerLines.item_no,
                                    headerLines.qty);
                            addlines.add(tempObject);
                        }
                        orderBookLine.insertBulkData(addlines);
                        orderBookLine.close();
                    }
                }
            } catch (Exception e) {
            }

        } else if (flag.equalsIgnoreCase("OutDataFromServer")) {
            OrderBookHeader orderBookHeader = new OrderBookHeader(activity);
            orderBookHeader.open();
            try {
                //todo insert Heder
                List<OrderBookHeader.OrderBookHeaderModel> AllHeaderdataWhoNotPosted = orderBookHeader.fetchUnsendData();
                for (int i = 0; i < AllHeaderdataWhoNotPosted.size(); i++) {
                    //todo get header's line and send it to the server
                    OrderBookHeader.OrderBookHeaderModel orderBookHeaderModel = AllHeaderdataWhoNotPosted.get(i);  //todo single header line who not posted to the server
                    OrderBookLine orderBookLine = new OrderBookLine(activity);
                    orderBookLine.open();
                    List<OrderBookLine.OrderBookLineModel> lineOfPerticulerHeader = orderBookLine.fetch(orderBookHeaderModel.android_order_no);

                    JSONObject postedJson = new JSONObject();
                    JSONObject orderHeader = new JSONObject();
                    orderHeader.put("approver_email", orderBookHeaderModel.approver_email);
                    orderHeader.put("user_type", orderBookHeaderModel.user_type);
                    orderHeader.put("customer_no", orderBookHeaderModel.customer_no);
                    orderHeader.put("email_id", sessionManagement.getUserEmail());
                    postedJson.put("orderHeader", orderHeader);

                    JSONArray orderLinejson = new JSONArray();
                    for (int j = 0; j < lineOfPerticulerHeader.size(); j++) {
                        JSONObject temp = new JSONObject();
                        temp.put("item_no", lineOfPerticulerHeader.get(j).item_no);
                        temp.put("qty", lineOfPerticulerHeader.get(j).qty);
                        orderLinejson.put(temp);
                    }
                    postedJson.put("orderLines", orderLinejson);

                    List<OrderBookResponseModel> orderBookResponseModels = new Gson().fromJson(globalPostingMethod.postHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.insertOrderBookHeaderLines), postedJson).getJsonResponse(), new TypeToken<List<OrderBookResponseModel>>() {
                    }.getType());
                    orderBookHeader.update(orderBookHeaderModel.android_order_no, orderBookResponseModels.get(0).order_no, orderBookResponseModels.get(0).created_on);
                    orderBookLine.update(orderBookHeaderModel.android_order_no, orderBookResponseModels.get(0).order_no);
                    orderBookLine.close();
                    //todo redy json who post the server and send it
                }
                orderBookHeader.close();
                Log.e("Order Book Sync Success", "Done");
            } catch (Exception e) {

            } finally {
                orderBookHeader.close();
            }
        }
    }

    void deleteActiveOneRecordForAllTables() {
//        try {
//            DatabaseHelper dbHelper = new DatabaseHelper(activity);
//            SQLiteDatabase database = dbHelper.getWritableDatabase();
//            for (int i = 0; i < AllTablesName.activeRecordTable.length; i++) {
//                int result = database.delete(AllTablesName.activeRecordTable[i], "active=1", null);
//                Log.e("Delete " + AllTablesName.activeRecordTable[i], result + "");
//            }
//            database.close();
//            dbHelper.close();
//        } catch (Exception e) {
//
//        }
    }

    void Sync_EventBookingData(String flag) {
        try {
            if (flag.equalsIgnoreCase("InsertEventType")) {
                //todo bind event type
                List<EventTypeMaster.EventTypeMasterModel> reponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getEventDeatil + "GetEventType")).getJsonResponse(), new TypeToken<List<EventTypeMaster.EventTypeMasterModel>>() {
                }.getType());
                if (reponse.size() > 0 && reponse.get(0).condition) {
                    EventTypeMaster eventTypeMaster = new EventTypeMaster(activity);
                    eventTypeMaster.open();
                    eventTypeMaster.insertArray(reponse);
                    eventTypeMaster.close();
                }
                //todo
                //todo bind event type
                List<ImageMasterTable.ImageMasterModel> imageReponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getEventDeatil + "GetImage&email=" + sessionManagement.getUserEmail())).getJsonResponse(), new TypeToken<List<ImageMasterTable.ImageMasterModel>>() {
                }.getType());
                if (imageReponse.size() > 0 && imageReponse.get(0).condition) {
                    ImageMasterTable imageMasterTable = new ImageMasterTable(activity);
                    imageMasterTable.open();
                    imageMasterTable.insertArray(imageReponse);
                    imageMasterTable.close();
                }
                //todo
            } else if (flag.equalsIgnoreCase("InDataFromServer")) {
                List<SyncEventDetailModel> reponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getEventDeatil + "getEventSync&email=" + sessionManagement.getUserEmail())).getJsonResponse(), new TypeToken<List<SyncEventDetailModel>>() {
                }.getType());
                if (reponse.size() > 0 && reponse.get(0).condition) {
                    EventManagementTable eventManagementTable = new EventManagementTable(activity);
                    eventManagementTable.open();
                    for (SyncEventDetailModel responseobject : reponse) {
                        String android_event_code = eventManagementTable.getTableSequenceNo();
                        eventManagementTable.insert(eventManagementTable.new EventManagemantModel(android_event_code,
                                responseobject.event_code, responseobject.event_desc, responseobject.event_date, responseobject.event_type, responseobject.event_budget,
                                responseobject.crop, responseobject.variety, responseobject.state, responseobject.district, responseobject.village,
                                responseobject.taluka, responseobject.farmer_name, responseobject.farmer_mobile_no, responseobject.expected_farmers
                                , responseobject.expected_dealers, responseobject.expected_distributer, responseobject.event_cover_villages, responseobject.created_on,
                                responseobject.created_by, responseobject.approver_email, responseobject.status, responseobject.reject_reason, responseobject.approve_on,
                                responseobject.actual_farmers, responseobject.actual_distributers, responseobject.actual_dealers));
                        for (SyncEventDetailModel.ExpanceLineModel lineObject : responseobject.expense_line) {
                            if (lineObject.line_no != null && lineObject.event_code != null) {
                                EventManagementExpenseLineTable eventManagementExpenseLineTable = new EventManagementExpenseLineTable(activity);
                                eventManagementExpenseLineTable.open();
                                eventManagementExpenseLineTable.insert(eventManagementExpenseLineTable.new EventManagementExpenseLineModel(lineObject.event_code,
                                        lineObject.line_no, lineObject.expense_type, lineObject.quantity, lineObject.rate_unit_cost, lineObject.amount,
                                        lineObject.created_on, "1"));
                                eventManagementExpenseLineTable.close();
                            }
                        }
                    }
                    eventManagementTable.close();
                }
            } else if (flag.equalsIgnoreCase("OutDataFromServer")) {
                EventManagementTable eventManagementTable = new EventManagementTable(activity);
                eventManagementTable.open();
                List<EventManagementTable.EventManagemantModel> eventManagementList = eventManagementTable.fetchUnsendData();
                for (EventManagementTable.EventManagemantModel tempEvent : eventManagementList) {
                    JSONObject postedJson = new JSONObject();
                    postedJson.put("email", sessionManagement.getUserEmail());
                    postedJson.put("event_desc", tempEvent.event_desc);
                    postedJson.put("event_date", tempEvent.event_date);
                    postedJson.put("event_type", tempEvent.event_type);
                    postedJson.put("event_budget", tempEvent.event_budget);
                    postedJson.put("crop", tempEvent.crop);
                    postedJson.put("variety", tempEvent.variety);
                    postedJson.put("state", tempEvent.state);
                    postedJson.put("district", tempEvent.district);
                    postedJson.put("village", tempEvent.village);
                    postedJson.put("taluka", tempEvent.taluka);
                    postedJson.put("farmer_name", tempEvent.farmer_name);
                    postedJson.put("farmer_mobile_no", tempEvent.farmer_mobile_no);
                    postedJson.put("expected_farmers", tempEvent.expected_farmers);
                    postedJson.put("expected_dealers", tempEvent.expected_dealers);
                    postedJson.put("expected_distributer", tempEvent.expected_distributer);
                    postedJson.put("event_cover_villages", tempEvent.event_cover_villages);
                    postedJson.put("approver_email", sessionManagement.getApprover_id());
                    List<EventCreateResponseModel> responseslist = new Gson().fromJson(globalPostingMethod.postHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.insertEventManagement), postedJson).getJsonResponse(), new TypeToken<List<EventCreateResponseModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition)
                        eventManagementTable.update(tempEvent.android_event_code, responseslist.get(0).event_code, responseslist.get(0).created_on);
                }
                eventManagementTable.close();
            }
        } catch (Exception e) {
        }
    }

    void Sync_TravelData(String flag) {
        try {
            if (flag.equalsIgnoreCase("InDataFromServer")) {
                List<SyncTravelDetailModel> reponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(
                        globalPostingMethod.createUrl(StaticDataForApp.getTravelDeatil + "getTravelSync&email=" + sessionManagement.getUserEmail())).getJsonResponse(), new TypeToken<List<SyncTravelDetailModel>>() {
                }.getType());
                if (reponse.size() > 0 && reponse.get(0).condition) {
                    TravelHeaderTable travelHeaderTable = new TravelHeaderTable(activity);
                    travelHeaderTable.open();
                    for (SyncTravelDetailModel responseobject : reponse) {
                        String android_travel_code = travelHeaderTable.getTableSequenceNo();
                        travelHeaderTable.insert(travelHeaderTable.new TravelHeaderModel(android_travel_code,
                                responseobject.travelcode, responseobject.from_loc, responseobject.to_loc, responseobject.start_date,
                                responseobject.end_date, responseobject.travel_reson, responseobject.expense_budget,
                                responseobject.approve_budget, responseobject.created_on,responseobject.user_type, responseobject.created_by, responseobject.STATUS, responseobject.approver_id,
                                responseobject.approve_on, responseobject.reason,responseobject.advance_amount));
                        if (responseobject.travel_line_expense.size() > 0 && responseobject.travel_line_expense.get(0).travelcode != null) {
                            ArrayList<TravelLineExpenseModel> insertlinedataLiset = new ArrayList<>();
                            for (int i = 0; i < responseobject.travel_line_expense.size(); i++) {
                                TravelLineExpenseModel insertdata = new TravelLineExpenseModel(android_travel_code, responseobject.travel_line_expense.get(i).travelcode
                                        , responseobject.travel_line_expense.get(i).line_no, responseobject.travel_line_expense.get(i).date,
                                        responseobject.travel_line_expense.get(i).from_loc, responseobject.travel_line_expense.get(i).to_loc,
                                        responseobject.travel_line_expense.get(i).departure, responseobject.travel_line_expense.get(i).arrival, responseobject.travel_line_expense.get(i).fare
                                        , responseobject.travel_line_expense.get(i).mode_of_travel, responseobject.travel_line_expense.get(i).loading_in_any,
                                        responseobject.travel_line_expense.get(i).distance_km, responseobject.travel_line_expense.get(i).fuel_vehicle_expance,
                                        responseobject.travel_line_expense.get(i).daily_express, responseobject.travel_line_expense.get(i).vehicle_repairing,
                                        responseobject.travel_line_expense.get(i).local_convance, responseobject.travel_line_expense.get(i).other_expenses,
                                        responseobject.travel_line_expense.get(i).total_amount_calulated, responseobject.travel_line_expense.get(i).created_on,
                                        responseobject.travel_line_expense.get(i).mod_city,responseobject.travel_line_expense.get(i).mod_lodging,responseobject.travel_line_expense.get(i).mod_da_half,
                                        responseobject.travel_line_expense.get(i).mode_da_full,responseobject.travel_line_expense.get(i).mod_ope_max,responseobject.travel_line_expense.get(i).user_grade);
                                insertlinedataLiset.add(insertdata);
                            }
                            TravelLineExpenseTable travelLineExpenseTable = new TravelLineExpenseTable(activity);
                            travelLineExpenseTable.open();
                            travelLineExpenseTable.insertBulk(insertlinedataLiset, null);
                            travelLineExpenseTable.close();
                        }
                    }
                    travelHeaderTable.close();
                }
            } else if (flag.equalsIgnoreCase("OutDataFromServer")) {
                TravelHeaderTable travelHeaderTable = new TravelHeaderTable(activity);
                travelHeaderTable.open();
                List<TravelHeaderTable.TravelHeaderModel> tempTravelList = travelHeaderTable.fetchUnsendData();
                for (TravelHeaderTable.TravelHeaderModel temptravel : tempTravelList) {
                    JSONObject postedJson = new JSONObject();
                    postedJson.put("email", sessionManagement.getUserEmail());
                    postedJson.put("from_loc", temptravel.from_loc);
                    postedJson.put("to_loc", temptravel.to_loc);
                    postedJson.put("start_date", temptravel.start_date);
                    postedJson.put("end_date", temptravel.end_date);
                    postedJson.put("travel_reson", temptravel.travel_reson);
                    postedJson.put("expense_budget", temptravel.expense_budget);
                    postedJson.put("approver_id", sessionManagement.getApprover_id());
                    postedJson.put("user_type", sessionManagement.getUser_type());
                    List<TravelHeaderResponse> responseslist = new Gson().fromJson(globalPostingMethod.postHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.insertTravelHeader), postedJson).getJsonResponse(), new TypeToken<List<TravelHeaderResponse>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition)
                        travelHeaderTable.update(temptravel.android_travelcode, responseslist.get(0).travelcode, responseslist.get(0).created_on);
                }
                travelHeaderTable.close();

                //todo send Line
                TravelLineExpenseTable travelLineExpenseTable = new TravelLineExpenseTable(activity);
                travelLineExpenseTable.open();
                List<TravelLineExpenseModel> travelLineExpenseList = travelLineExpenseTable.fetchLocalUnsendLine();
                JSONArray postedJasonArray = new JSONArray();
                for (int i = 0; i < travelLineExpenseList.size(); i++) {
                    TravelLineExpenseModel data = travelLineExpenseList.get(i);
                    JSONObject temp_json = new JSONObject();
                    temp_json.put("travelcode", data.travelcode);
                    temp_json.put("line_no", data.line_no);
                    temp_json.put("date", data.date);
                    temp_json.put("from_loc", data.from_loc);
                    temp_json.put("to_loc", data.to_loc);
                    temp_json.put("departure", data.departure);
                    temp_json.put("arrival", data.arrival);
                    temp_json.put("fare", data.fare);
                    temp_json.put("mode_of_travel", data.mode_of_travel);
                    temp_json.put("loading_in_any", data.loading_in_any);
                    temp_json.put("distance_km", data.distance_km);
                    temp_json.put("fuel_vehicle_expance", data.fuel_vehicle_expance);
                    temp_json.put("daily_express", data.daily_express);
                    temp_json.put("vehicle_repairing", data.vehicle_repairing);
                    temp_json.put("local_convance", data.local_convance);
                    temp_json.put("other_expenses", data.other_expenses);
                    temp_json.put("total_amount_calulated", data.total_amount_calulated);

                    temp_json.put("mod_city", data.mod_city);
                    temp_json.put("mod_lodging", data.mod_lodging);
                    temp_json.put("mod_da_half", data.mod_da_half);
                    temp_json.put("mode_da_full", data.mode_da_full);
                    temp_json.put("mod_ope_max", data.mod_ope_max);
                    temp_json.put("user_grade", data.user_grade);
                    postedJasonArray.put(temp_json);
                }
                List<TravelExpenseResponseModel> responseslist = new Gson().fromJson(globalPostingMethod.postArrayHttpRequest
                                (globalPostingMethod.createUrl(StaticDataForApp.insertTravelExpense), postedJasonArray).getJsonResponse()
                        , new TypeToken<List<TravelExpenseResponseModel>>() {
                        }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition) {
                    travelLineExpenseTable.updateAllExpenseLines(responseslist.get(0).created_on, null);
                }
                travelLineExpenseTable.close();

            }
        } catch (Exception e) {
        }
    }

    void Sync_CityMasterData() {
        try {
            List<CityMasterTable.CityMasterModel> reponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCityMaster + "getCityMaster")).getJsonResponse(), new TypeToken<List<CityMasterTable.CityMasterModel>>() {
            }.getType());
            if (reponse.size() > 0 && reponse.get(0).condition) {
                CityMasterTable cityMasterTable = new CityMasterTable(activity);
                cityMasterTable.open();
                cityMasterTable.insertArray(reponse);
                cityMasterTable.close();
            }
        } catch (Exception e) {
        }
    }

    void Sync_ModeOfTravelMasterTable() {
        try {
            List<ModeOfTravelMasterTable.ModeOfTravelModel> reponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCityMaster + "getMode_of_travel")).getJsonResponse(), new TypeToken<List<ModeOfTravelMasterTable.ModeOfTravelModel>>() {
            }.getType());
            if (reponse.size() > 0 && reponse.get(0).condition) {
                ModeOfTravelMasterTable modeOfTravelMasterTable = new ModeOfTravelMasterTable(activity);
                modeOfTravelMasterTable.open();
                modeOfTravelMasterTable.insertArray(reponse);
                modeOfTravelMasterTable.close();
            }
        } catch (Exception e) {
        }
    }

    //todo sync data start
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    int notificationId = 1;
    static NotificationCompat.Builder mBuilder;
    static NotificationManager manager;

    void NotificationSyncDataStart() {
//        builder = new NotificationCompat.Builder(activity, "Offline Data Sync")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
//                        R.mipmap.ic_launcher))
//                .setContentTitle("Offline Data Sync")
//                .setContentText("Sync Action")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Sync Action"))
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//        manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true);
//        manager.notify(notificationId, builder.build());

        //todo fix notification show
       mBuilder =
                new NotificationCompat.Builder(activity.getApplicationContext(), "Offline Data Sync");
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.bigText("Sync Action");
        bigText.setBigContentTitle("Offline Data Sync");
        bigText.setSummaryText("Background Sync");

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Offline Data Sync");
        mBuilder.setContentText("Sync Action");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Offline Data Sync";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Offline Data Sync",
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true);
        manager.notify(notificationId, mBuilder.build());
    }

    void NotificationSyncDataStop() {
        try {
            mBuilder.setContentText("Sync complete")
                    .setProgress(0, 0, false);
            manager.notify(notificationId, mBuilder.build());
            Thread.sleep(1000);
            manager.cancel(notificationId);
        } catch (Exception e) {
        }
    }
}