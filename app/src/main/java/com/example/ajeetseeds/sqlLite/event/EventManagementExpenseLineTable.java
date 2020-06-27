package com.example.ajeetseeds.sqlLite.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EventManagementExpenseLineTable {
    // Table Name
    public static final String TABLE_NAME = "event_management_expense_line";
    // Table columns
    public static final String event_code = "event_code";
    public static final String line_no = "line_no";
    public static final String expense_type = "expense_type";
    public static final String quantity = "quantity";
    public static final String rate_unit_cost = "rate_unit_cost";
    public static final String amount = "amount";
    public static final String created_on = "created_on";
    public static final String sendToServer = "sendToServer";

    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            event_code + " TEXT NOT NULL, " +
            line_no + " TEXT NOT NULL, " +
            expense_type + " TEXT NOT NULL," +
            quantity + " INTEGER," +
            rate_unit_cost + " TEXT NULL," +
            amount + " TEXT NULL," +
            created_on + " TEXT NULL," +
            sendToServer + " INTEGER NOT NULL," +
            " PRIMARY KEY("+event_code+","+line_no+")" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public EventManagementExpenseLineTable(Context context) {
        this.context = context;
    }

    public EventManagementExpenseLineTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }
    public void insert(EventManagementExpenseLineModel data) {
        database.beginTransaction();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(this.event_code, data.event_code);
            contentValue.put(this.line_no, data.line_no);
            contentValue.put(this.expense_type, data.expense_type);
            contentValue.put(this.quantity, data.quantity);
            contentValue.put(this.rate_unit_cost, data.rate_unit_cost);
            contentValue.put(this.amount, data.amount);
            contentValue.put(this.created_on, data.created_on);
            contentValue.put(this.sendToServer, data.sendToServer);
            database.replace(TABLE_NAME, null, contentValue);
           database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<EventManagementExpenseLineModel> fetch(String event_code) {
        List<EventManagementExpenseLineModel> returnData = new ArrayList<>();
        String[] columns = new String[]{this.event_code, this.line_no, this.expense_type, this.quantity, this.rate_unit_cost, this.amount, this.created_on, this.sendToServer};
        Cursor cursor = database.query(TABLE_NAME, columns, this.event_code + "='" + event_code + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new EventManagementExpenseLineModel(
                        cursor.getString(cursor.getColumnIndex(this.event_code)),
                        cursor.getString(cursor.getColumnIndex(this.line_no)),
                        cursor.getString(cursor.getColumnIndex(this.expense_type)),
                        cursor.getString(cursor.getColumnIndex(this.quantity)),
                        cursor.getString(cursor.getColumnIndex(this.rate_unit_cost)),
                        cursor.getString(cursor.getColumnIndex(this.amount)),
                        cursor.getString(cursor.getColumnIndex(this.created_on)),
                        cursor.getString(cursor.getColumnIndex(this.sendToServer))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }


    public void deleteEventAllLine(String event_code) {
        database.delete(TABLE_NAME, this.event_code + "='" + event_code + "'", null);
    }

    public class EventManagementExpenseLineModel {
        public String event_code;
        public String line_no;
        public String expense_type;
        public String quantity;
        public String rate_unit_cost;
        public String amount;
        public String created_on;
        public String sendToServer;
        public String expense_type_name;

        public EventManagementExpenseLineModel(String event_code, String line_no, String expense_type, String quantity, String rate_unit_cost, String amount, String created_on, String sendToServer) {
            this.event_code = event_code;
            this.line_no = line_no;
            this.expense_type = expense_type;
            this.quantity = quantity;
            this.rate_unit_cost = rate_unit_cost;
            this.amount = amount;
            this.created_on = created_on;
            this.sendToServer = sendToServer;
        }
    }
}
