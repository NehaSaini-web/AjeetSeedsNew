package com.example.ajeetseeds.sqlLite.orderBook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderBookHeader {
    // Table Name
    public static final String TABLE_NAME = "order_header";
    // Table columns
    public static final String android_order_no = "android_order_no";
    public static final String order_no = "order_no";
    public static final String approver_email = "approver_email";
    public static final String user_type = "user_type";
    public static final String customer_no = "customer_no";
    public static final String order_status = "order_status";
    public static final String image_url = "image_url";
    public static final String updated_on = "updated_on";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            android_order_no + " TEXT PRIMARY KEY," +
            order_no + " TEXT NULL," +
            approver_email + " TEXT NOT NULL," +
            user_type + " TEXT NULL," +
            customer_no + " TEXT NOT NULL," +
            order_status + " TEXT NULL," +
            updated_on + " TEXT NULL," +
            image_url + " TEXT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public OrderBookHeader(Context context) {
        this.context = context;
    }

    public OrderBookHeader open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(OrderBookHeaderModel orderBookHeaderModel) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_order_no, orderBookHeaderModel.android_order_no);
            contentValue.put(this.order_no, orderBookHeaderModel.order_no);
            contentValue.put(this.approver_email, orderBookHeaderModel.approver_email);
            contentValue.put(this.user_type, orderBookHeaderModel.user_type);
            contentValue.put(this.image_url, orderBookHeaderModel.image_url);
            contentValue.put(this.customer_no, orderBookHeaderModel.customer_no);
            contentValue.put(this.order_status, orderBookHeaderModel.order_status);
            contentValue.put(this.updated_on, orderBookHeaderModel.updated_on);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    public String getTableSequenceNo() {
        String[] columns = new String[]{android_order_no};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        return String.valueOf((cursor.getCount() + 1));
    }

    public List<OrderBookHeaderModel> fetch() {
        List<OrderBookHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_order_no, order_no, approver_email, user_type, customer_no, order_status,updated_on,image_url};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, updated_on +" DESC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new OrderBookHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_order_no)),
                        cursor.getString(cursor.getColumnIndex(this.order_no)),
                        cursor.getString(cursor.getColumnIndex(this.approver_email)),
                        cursor.getString(cursor.getColumnIndex(this.user_type)),
                        cursor.getString(cursor.getColumnIndex(this.customer_no)),
                        cursor.getString(cursor.getColumnIndex(this.order_status)),
                        cursor.getString(cursor.getColumnIndex(this.updated_on)),
                        cursor.getString(cursor.getColumnIndex(this.image_url))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public List<OrderBookHeaderModel> fetchUnsendData() {
        List<OrderBookHeaderModel> returnData = new ArrayList<>();
        String[] columns = new String[]{android_order_no, order_no, approver_email, user_type, customer_no, order_status,image_url,updated_on};
        Cursor cursor = database.query(TABLE_NAME, columns, order_no + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new OrderBookHeaderModel(
                        cursor.getString(cursor.getColumnIndex(this.android_order_no)),
                        cursor.getString(cursor.getColumnIndex(this.order_no)),
                        cursor.getString(cursor.getColumnIndex(this.approver_email)),
                        cursor.getString(cursor.getColumnIndex(this.user_type)),
                        cursor.getString(cursor.getColumnIndex(this.customer_no)),
                        cursor.getString(cursor.getColumnIndex(this.order_status)),
                        cursor.getString(cursor.getColumnIndex(this.updated_on)),
                        cursor.getString(cursor.getColumnIndex(this.image_url))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public int update(String android_order_no, String order_no,String updated_on) {   //todo update activity no when data is going to post to the server
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.order_no, order_no);
            contentValues.put(this.updated_on, updated_on);
            int i = database.update(TABLE_NAME, contentValues, this.android_order_no + " = " + android_order_no, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }
    public int update_OrderStatus(String order_no,String order_status) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.order_status, order_status);
            int i = database.update(TABLE_NAME, contentValues, this.order_no + " = '" + order_no+"'", null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public void delete(String activity_no) {
        database.delete(TABLE_NAME, this.order_no + "=" + activity_no, null);
    }

    public class OrderBookHeaderModel {
        public String android_order_no;
        public String order_no;
        public String approver_email;
        public String user_type;
        public String customer_no;
        public String order_status;
        public String updated_on;
        public String image_url;
        public OrderBookHeaderModel(String android_order_no, String order_no, String approver_email, String user_type, String customer_no, String order_status, String updated_on,String image_url) {
            this.android_order_no = android_order_no;
            this.order_no = order_no;
            this.approver_email = approver_email;
            this.user_type = user_type;
            this.customer_no = customer_no;
            this.order_status = order_status;
            this.image_url=image_url;
            this.updated_on = updated_on;
        }
    }
}
