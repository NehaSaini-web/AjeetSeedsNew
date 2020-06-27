package com.example.ajeetseeds.sqlLite.masters.crop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomerMasterTable {
    // Table Name
    public static final String TABLE_NAME = "customer_master";
    // Table columns
    public static final String no = "no";
    public static final String name = "name";
    public static final String address = "address";
    public static final String city = "city";
    public static final String contact = "contact";
    public static final String customer_posting_group = "customer_posting_group";
    public static final String payment_terms_code = "payment_terms_code";
    public static final String country = "country";
    public static final String post_code = "post_code";
    public static final String email = "email";
    public static final String pan_no = "pan_no";
    public static final String state_code = "state_code";
    public static final String gst_registration_no = "gst_registration_no";
    public static final String gst_registration_type = "gst_registration_type";
    public static final String gst_customer_type = "gst_customer_type";
    public static final String arn_no = "arn_no";
    public static final String district_code = "district_code";
    public static final String region_code = "region_code";
    public static final String taluka = "taluka";
    public static final String active = "active";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            no + " TEXT PRIMARY KEY," +
            name + " TEXT ," +
            address + " TEXT ," +
            city + " TEXT ," +
            contact + " TEXT ," +
            customer_posting_group + " TEXT ," +
            payment_terms_code + " TEXT ," +
            country + " TEXT ," +
            post_code + " TEXT ," +
            email + " TEXT ," +
            pan_no + " TEXT ," +
            state_code + " TEXT ," +
            gst_registration_no + " TEXT ," +
            gst_registration_type + " TEXT ," +
            gst_customer_type + " TEXT ," +
            arn_no + " TEXT ," +
            district_code + " TEXT ," +
            region_code + " TEXT ," +
            taluka + " TEXT ," +
            active + " INTEGER" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CustomerMasterTable(Context context) {
        this.context = context;
    }

    public CustomerMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public void insertArray(List<CustomerMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (CustomerMasterModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.no, data.no);
                contentValue.put(this.name, data.name);
                contentValue.put(this.address, data.address);
                contentValue.put(this.city, data.city);
                contentValue.put(this.contact, data.contact);
                contentValue.put(this.customer_posting_group, data.customer_posting_group);
                contentValue.put(this.payment_terms_code, data.payment_terms_code);
                contentValue.put(this.country, data.country);
                contentValue.put(this.post_code, data.post_code);
                contentValue.put(this.email, data.email);
                contentValue.put(this.pan_no, data.pan_no);
                contentValue.put(this.state_code, data.state_code);
                contentValue.put(this.gst_registration_no, data.gst_registration_no);
                contentValue.put(this.gst_registration_type, data.gst_registration_type);
                contentValue.put(this.gst_customer_type, data.gst_customer_type);
                contentValue.put(this.arn_no, data.arn_no);
                contentValue.put(this.district_code, data.district_code);
                contentValue.put(this.region_code, data.region_code);
                contentValue.put(this.taluka, data.taluka);
                contentValue.put(this.active, data.active);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<CustomerMasterModel> fetch() {
        List<CustomerMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{no, name, address, city, contact, customer_posting_group, payment_terms_code, country, post_code, email, pan_no, state_code,
                gst_registration_no, gst_registration_type, gst_customer_type, arn_no, district_code, region_code, taluka, active};
        Cursor cursor = database.query(TABLE_NAME, columns, this.active+"=0", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new CustomerMasterModel(
                        cursor.getString(cursor.getColumnIndex(this.no)),
                        cursor.getString(cursor.getColumnIndex(this.name)),
                        cursor.getString(cursor.getColumnIndex(this.address)),
                        cursor.getString(cursor.getColumnIndex(this.city)),
                        cursor.getString(cursor.getColumnIndex(this.contact)),
                        cursor.getString(cursor.getColumnIndex(this.customer_posting_group)),
                        cursor.getString(cursor.getColumnIndex(this.payment_terms_code)),
                        cursor.getString(cursor.getColumnIndex(this.country)),
                        cursor.getString(cursor.getColumnIndex(this.post_code)),
                        cursor.getString(cursor.getColumnIndex(this.email)),
                        cursor.getString(cursor.getColumnIndex(this.pan_no)),
                        cursor.getString(cursor.getColumnIndex(this.state_code)),
                        cursor.getString(cursor.getColumnIndex(this.gst_registration_no)),
                        cursor.getString(cursor.getColumnIndex(this.gst_registration_type)),
                        cursor.getString(cursor.getColumnIndex(this.gst_customer_type)),
                        cursor.getString(cursor.getColumnIndex(this.arn_no)),
                        cursor.getString(cursor.getColumnIndex(this.district_code)),
                        cursor.getString(cursor.getColumnIndex(this.region_code)),
                        cursor.getString(cursor.getColumnIndex(this.taluka)),
                        cursor.getInt(cursor.getColumnIndex(this.active))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String fetchCustomerName(String customer_no) {
        String[] columns = new String[]{no, name};
        Cursor cursor = database.query(TABLE_NAME, columns, this.no+"='"+customer_no+"'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
               return  cursor.getString(cursor.getColumnIndex(this.name));
            } while (cursor.moveToNext());
        }
        return "";
    }

    public void delete(String item_no) {
        database.delete(TABLE_NAME, this.no + "=" + item_no, null);
    }

    public class CustomerMasterModel {
        public String no;
        public String name;
        public String address;
        public String city;
        public String contact;
        public String customer_posting_group;
        public String payment_terms_code;
        public String country;
        public String post_code;
        public String email;
        public String pan_no;
        public String state_code;
        public String gst_registration_no;
        public String gst_registration_type;
        public String gst_customer_type;
        public String arn_no;
        public String district_code;
        public String region_code;
        public String taluka;
        public int active;

        public CustomerMasterModel(String no, String name, String address, String city, String contact, String customer_posting_group, String payment_terms_code,
                                   String country, String post_code, String email, String pan_no, String state_code, String gst_registration_no,
                                   String gst_registration_type, String gst_customer_type, String arn_no, String district_code, String region_code,
                                   String taluka, int active) {
            this.no = no;
            this.name = name;
            this.address = address;
            this.city = city;
            this.contact = contact;
            this.customer_posting_group = customer_posting_group;
            this.payment_terms_code = payment_terms_code;
            this.country = country;
            this.post_code = post_code;
            this.email = email;
            this.pan_no = pan_no;
            this.state_code = state_code;
            this.gst_registration_no = gst_registration_no;
            this.gst_registration_type = gst_registration_type;
            this.gst_customer_type = gst_customer_type;
            this.arn_no = arn_no;
            this.district_code = district_code;
            this.region_code = region_code;
            this.taluka = taluka;
            this.active = active;
        }
    }
}
