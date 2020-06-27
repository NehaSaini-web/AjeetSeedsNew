package com.example.ajeetseeds.sqlLite.orderBook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderBookLine {
    // Table Name
    public static final String TABLE_NAME = "order_line";
    // Table columns
    public static final String id = "id";
    public static final String android_order_no = "android_order_no";
    public static final String order_no = "order_no";
    public static final String item_no = "item_no";
    public static final String qty = "qty";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            android_order_no + " TEXT NOT NULL," +
            order_no + " TEXT NULL," +
            item_no + " TEXT NOT NULL," +
            qty + " TEXT NOT NULL" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public OrderBookLine(Context context) {
        this.context = context;
    }

    public OrderBookLine open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();

    }

    public long insert(OrderBookLineModel orderBookLineModels) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.android_order_no, orderBookLineModels.android_order_no);
            contentValue.put(this.order_no, orderBookLineModels.order_no);
            contentValue.put(this.item_no, orderBookLineModels.item_no);
            contentValue.put(this.qty, orderBookLineModels.qty);
            long result = database.replace(TABLE_NAME, null, contentValue);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }
    public boolean insertBulkData(List<OrderBookLineModel> orderBookLineModels) {
        database.beginTransaction();
        try {
            for(OrderBookLineModel data:orderBookLineModels) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.android_order_no, data.android_order_no);
                contentValue.put(this.order_no, data.order_no);
                contentValue.put(this.item_no, data.item_no);
                contentValue.put(this.qty, data.qty);
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
    public List<OrderBookLineModel> fetch(String android_order_no) {
        List<OrderBookLineModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.android_order_no, order_no, item_no, qty};
        Cursor cursor = database.query(TABLE_NAME, columns, this.android_order_no+"=?", new String[]{android_order_no}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new OrderBookLineModel(
                        cursor.getString(cursor.getColumnIndex(this.android_order_no)),
                        cursor.getString(cursor.getColumnIndex(this.order_no)),
                        cursor.getString(cursor.getColumnIndex(this.item_no)),
                        cursor.getString(cursor.getColumnIndex(this.qty))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public List<CustomeModel> fetch_byOrderNo(String order_no) {
        List<CustomeModel> returnData = new ArrayList<>();
        Cursor cursor =database.rawQuery("SELECT ol.order_no,ol.item_no,ol.qty," +
                "(SELECT cim.image_url FROM crops_item_master cim WHERE cim.item_no=ol.item_no ) as image_url, " +
                "(SELECT cim.name FROM crops_item_master cim WHERE cim.item_no=ol.item_no ) as item_name " +
                "FROM order_line ol" +
                " WHERE ol.order_no='"+order_no+"';" ,null);;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new CustomeModel(
                        cursor.getString(cursor.getColumnIndex("order_no")),
                        cursor.getString(cursor.getColumnIndex("item_no")),
                        cursor.getString(cursor.getColumnIndex("qty")),
                        cursor.getString(cursor.getColumnIndex("image_url")),
                        cursor.getString(cursor.getColumnIndex("item_name"))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }
    public int update(String android_order_no, String order_no) {   //todo update activity no when data is going to post to the server
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.order_no, order_no);
            int i = database.update(TABLE_NAME, contentValues, this.android_order_no + " = " + android_order_no, null);
            database.setTransactionSuccessful();
            return i;
        } finally {
            database.endTransaction();
        }
    }

    public void delete(String order_no) {
        database.delete(TABLE_NAME, this.order_no + "=" + order_no, null);
    }

    public class OrderBookLineModel {
        public String android_order_no;
        public String order_no;
        public String item_no;
        public String qty;

        public OrderBookLineModel(String android_order_no, String order_no, String item_no, String qty) {
            this.android_order_no = android_order_no;
            this.order_no = order_no;
            this.item_no = item_no;
            this.qty = qty;
        }
    }
    public class CustomeModel{
        public String order_no;
        public String item_no;
        public String qty;
        public String image_url;
        public String item_name;
        public CustomeModel(String order_no,String item_no,String qty,String image_url,String item_name){
            this.order_no=order_no;
            this.item_no=item_no;
            this.qty=qty;
            this.image_url=image_url;
            this.item_name=item_name;
        }
    }
}
