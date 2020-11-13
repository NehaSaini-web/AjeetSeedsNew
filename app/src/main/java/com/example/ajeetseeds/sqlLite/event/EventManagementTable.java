package com.example.ajeetseeds.sqlLite.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EventManagementTable {
    // Table Name
    public static final String TABLE_NAME = "event_management";
    // Table columns
    public static final String android_event_code = "android_event_code";
    public static final String event_code = "event_code";
    public static final String event_desc = "event_desc";
    public static final String event_date = "event_date";
    public static final String event_type = "event_type";
    public static final String event_budget = "event_budget";

    public static final String crop = "crop";
    public static final String variety = "variety";
    public static final String state = "state";
    public static final String district = "district";
    public static final String village = "village";
    public static final String taluka = "taluka";
    public static final String farmer_name = "farmer_name";
    public static final String farmer_mobile_no = "farmer_mobile_no";
    public static final String expected_farmers = "expected_farmers";
    public static final String expected_dealers = "expected_dealers";
    public static final String expected_distributer = "expected_distributer";
    public static final String event_cover_villages = "event_cover_villages";
    public static final String created_on = "created_on";
    public static final String created_by = "created_by";
    public static final String approver_email = "approver_email";
    public static final String status = "status";
    public static final String reject_reason = "reject_reason";
    public static final String approve_on = "approve_on";

    public static final String actual_farmers = "actual_farmers";
    public static final String actual_distributers = "actual_distributers";
    public static final String actual_dealers = "actual_dealers";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            android_event_code + " TEXT PRIMARY KEY," +
            event_code + " TEXT NOT NULL," +
            event_desc + " TEXT NOT NULL," +
            event_date + " TEXT NOT NULL," +
            event_type + " TEXT NOT NULL," +
            event_budget + " TEXT NOT NULL," +
            crop + " TEXT NOT NULL," +
            variety + " TEXT NOT NULL," +
            state + " TEXT NOT NULL," +
            district + " TEXT NOT NULL," +
            village + " TEXT NOT NULL," +
            taluka + " TEXT NOT NULL," +
            farmer_name + " TEXT NOT NULL," +
            farmer_mobile_no + " TEXT NOT NULL," +
            expected_farmers + " TEXT NOT NULL," +
            expected_dealers + " TEXT NOT NULL," +
            expected_distributer + " TEXT NOT NULL," +
            event_cover_villages + " TEXT NOT NULL," +
            created_on + " TEXT NULL," +
            created_by + " TEXT NOT NULL," +
            approver_email + " TEXT NOT NULL," +
            status + " TEXT NOT NULL," +
            reject_reason + " TEXT NULL," +
            approve_on + " TEXT NULL," +

            actual_farmers + " TEXT NULL," +
            actual_distributers + " TEXT NULL," +
            actual_dealers + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public EventManagementTable(Context context) {
        this.context = context;
    }

    public EventManagementTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(EventManagemantModel passdata) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_event_code, passdata.android_event_code);
            contentValue.put(this.event_code, passdata.event_code);
            contentValue.put(this.event_desc, passdata.event_desc);
            contentValue.put(this.event_date, passdata.event_date);
            contentValue.put(this.event_type, passdata.event_type);
            contentValue.put(this.event_budget, passdata.event_budget);
            contentValue.put(this.crop, passdata.crop);
            contentValue.put(this.variety, passdata.variety);
            contentValue.put(this.state, passdata.state);
            contentValue.put(this.district, passdata.district);
            contentValue.put(this.village, passdata.village);
            contentValue.put(this.taluka, passdata.taluka);
            contentValue.put(this.farmer_name, passdata.farmer_name);
            contentValue.put(this.farmer_mobile_no, passdata.farmer_mobile_no);
            contentValue.put(this.expected_farmers, passdata.expected_farmers);
            contentValue.put(this.expected_dealers, passdata.expected_dealers);
            contentValue.put(this.expected_distributer, passdata.expected_distributer);
            contentValue.put(this.event_cover_villages, passdata.event_cover_villages);
            contentValue.put(this.created_on, passdata.created_on);
            contentValue.put(this.created_by, passdata.created_by);
            contentValue.put(this.approver_email, passdata.approver_email);
            contentValue.put(this.status, passdata.status);
            contentValue.put(this.reject_reason, passdata.reject_reason);
            contentValue.put(this.approve_on, passdata.approve_on);

            contentValue.put(this.actual_farmers, passdata.actual_farmers);
            contentValue.put(this.actual_distributers, passdata.actual_distributers);
            contentValue.put(this.actual_dealers, passdata.actual_dealers);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    public String getTableSequenceNo() {
        String[] columns = new String[]{android_event_code};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        return String.valueOf((cursor.getCount() + 1));
    }

    public List<EventManagemantModel> fetch() {
        List<EventManagemantModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_event_code, event_code, event_desc, event_date, event_type, event_budget, crop, variety, state, district,
                village, taluka, farmer_name, farmer_mobile_no, expected_farmers, expected_dealers, expected_distributer, event_cover_villages, created_on,
                created_by, approver_email, status, reject_reason, approve_on,this.actual_farmers,this.actual_distributers,this.actual_dealers};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new EventManagemantModel(
                        cursor.getString(cursor.getColumnIndex(this.android_event_code)),
                        cursor.getString(cursor.getColumnIndex(this.event_code)),
                        cursor.getString(cursor.getColumnIndex(this.event_desc)),
                        cursor.getString(cursor.getColumnIndex(this.event_date)),
                        cursor.getString(cursor.getColumnIndex(this.event_type)),
                        cursor.getString(cursor.getColumnIndex(this.event_budget)),
                        cursor.getString(cursor.getColumnIndex(this.crop)),
                        cursor.getString(cursor.getColumnIndex(this.variety)),
                        cursor.getString(cursor.getColumnIndex(this.state)),
                        cursor.getString(cursor.getColumnIndex(this.district)),
                        cursor.getString(cursor.getColumnIndex(this.village)),
                        cursor.getString(cursor.getColumnIndex(this.taluka)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_name)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_mobile_no)),
                        cursor.getString(cursor.getColumnIndex(this.expected_farmers)),
                        cursor.getString(cursor.getColumnIndex(this.expected_dealers)),
                        cursor.getString(cursor.getColumnIndex(this.expected_distributer)),
                        cursor.getString(cursor.getColumnIndex(this.event_cover_villages)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),
                        cursor.getString(cursor.getColumnIndex(this.created_by)),
                        cursor.getString(cursor.getColumnIndex(this.approver_email)),
                        cursor.getString(cursor.getColumnIndex(this.status)),
                        cursor.getString(cursor.getColumnIndex(this.reject_reason)),
                        cursor.getString(cursor.getColumnIndex(this.approve_on)),

                        cursor.getString(cursor.getColumnIndex(this.actual_farmers)),
                        cursor.getString(cursor.getColumnIndex(this.actual_distributers)),
                        cursor.getString(cursor.getColumnIndex(this.actual_dealers))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public List<EventManagemantModel> fetchUnsendData() {
        List<EventManagemantModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_event_code, event_code, event_desc, event_date, event_type, event_budget, crop, variety, state, district,
                village, taluka, farmer_name, farmer_mobile_no, expected_farmers, expected_dealers, expected_distributer, event_cover_villages,
                created_on, created_by, approver_email, status, reject_reason, approve_on,this.actual_farmers,this.actual_distributers,this.actual_dealers};
        Cursor cursor = database.query(TABLE_NAME, columns, event_code + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new EventManagemantModel(
                        cursor.getString(cursor.getColumnIndex(this.android_event_code)),
                        cursor.getString(cursor.getColumnIndex(this.event_code)),
                        cursor.getString(cursor.getColumnIndex(this.event_desc)),
                        cursor.getString(cursor.getColumnIndex(this.event_date)),
                        cursor.getString(cursor.getColumnIndex(this.event_type)),
                        cursor.getString(cursor.getColumnIndex(this.event_budget)),
                        cursor.getString(cursor.getColumnIndex(this.crop)),
                        cursor.getString(cursor.getColumnIndex(this.variety)),
                        cursor.getString(cursor.getColumnIndex(this.state)),
                        cursor.getString(cursor.getColumnIndex(this.district)),
                        cursor.getString(cursor.getColumnIndex(this.village)),
                        cursor.getString(cursor.getColumnIndex(this.taluka)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_name)),
                        cursor.getString(cursor.getColumnIndex(this.farmer_mobile_no)),
                        cursor.getString(cursor.getColumnIndex(this.expected_farmers)),
                        cursor.getString(cursor.getColumnIndex(this.expected_dealers)),
                        cursor.getString(cursor.getColumnIndex(this.expected_distributer)),
                        cursor.getString(cursor.getColumnIndex(this.event_cover_villages)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),
                        cursor.getString(cursor.getColumnIndex(this.created_by)),
                        cursor.getString(cursor.getColumnIndex(this.approver_email)),
                        cursor.getString(cursor.getColumnIndex(this.status)),
                        cursor.getString(cursor.getColumnIndex(this.reject_reason)),
                        cursor.getString(cursor.getColumnIndex(this.approve_on)),

                        cursor.getString(cursor.getColumnIndex(this.actual_farmers)),
                        cursor.getString(cursor.getColumnIndex(this.actual_distributers)),
                        cursor.getString(cursor.getColumnIndex(this.actual_dealers))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update_EventStatus(String event_code, String status) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.status, status);
            int i = database.update(TABLE_NAME, contentValues, this.event_code + " = '" + event_code + "'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public int update_EventStatus(String event_code, String status, String reject_reason, String approve_on) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.status, status);
            contentValues.put(this.reject_reason, reject_reason);
            contentValues.put(this.approve_on, approve_on);
            int i = database.update(TABLE_NAME, contentValues, this.event_code + " = '" + event_code + "'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public int update(String android_event_code, String event_code, String created_on) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.event_code, event_code);
            contentValues.put(this.created_on, created_on);
            int i = database.update(TABLE_NAME, contentValues, this.android_event_code + " = " + android_event_code, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public int updateFarmaerDealerDistributers(String android_event_code, String event_code, String actual_farmers,String actual_distributers,String actual_dealers) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.actual_farmers, actual_farmers);
            contentValues.put(this.actual_dealers, actual_dealers);
            contentValues.put(this.actual_distributers, actual_distributers);
            int i = database.update(TABLE_NAME, contentValues, this.android_event_code + " = " + android_event_code +" and "+this.event_code+"='"+event_code+"'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public void delete(String event_code) {
        database.delete(TABLE_NAME, this.event_code + "=" + event_code, null);
    }

    public class EventManagemantModel {
        public String android_event_code;
        public String event_code;
        public String event_desc;
        public String event_date;
        public String event_type;
        public String event_budget;
        public String crop;
        public String variety;
        public String state;
        public String district;
        public String village;
        public String taluka;
        public String farmer_name;
        public String farmer_mobile_no;
        public String expected_farmers;
        public String expected_dealers;
        public String expected_distributer;
        public String event_cover_villages;
        public String created_on;
        public String created_by;
        public String approver_email;
        public String status;
        public String reject_reason;
        public String approve_on;

        public String crop_name;
        public String variety_name;
        public String state_name;
        public String district_name;
        public String taluka_name;

        public String actual_farmers;
        public String actual_distributers;
        public String actual_dealers;

        public EventManagemantModel(String android_event_code, String event_code, String event_desc, String event_date, String event_type,
                                    String event_budget, String crop, String variety, String state, String district, String village,
                                    String taluka, String farmer_name, String farmer_mobile_no, String expected_farmers, String expected_dealers,
                                    String expected_distributer, String event_cover_villages, String created_on, String created_by, String approver_email,
                                    String status, String reject_reason, String approve_on, String actual_farmers, String actual_distributers,
                                    String actual_dealers) {
            this.android_event_code = android_event_code;
            this.event_code = event_code;
            this.event_desc = event_desc;
            this.event_date = event_date;
            this.event_type = event_type;
            this.event_budget = event_budget;
            this.crop = crop;
            this.variety = variety;
            this.state = state;
            this.district = district;
            this.village = village;
            this.taluka = taluka;
            this.farmer_name = farmer_name;
            this.farmer_mobile_no = farmer_mobile_no;
            this.expected_farmers = expected_farmers;
            this.expected_dealers = expected_dealers;
            this.expected_distributer = expected_distributer;
            this.event_cover_villages = event_cover_villages;
            this.created_on = created_on;
            this.created_by = created_by;
            this.approver_email = approver_email;
            this.status = status;
            this.reject_reason = reject_reason;
            this.approve_on = approve_on;

            this.actual_farmers = actual_farmers;
            this.actual_distributers = actual_distributers;
            this.actual_dealers = actual_dealers;
        }
    }
}
