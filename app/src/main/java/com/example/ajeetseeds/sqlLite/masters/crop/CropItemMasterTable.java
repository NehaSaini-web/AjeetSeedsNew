package com.example.ajeetseeds.sqlLite.masters.crop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CropItemMasterTable {
    // Table Name
    public static final String TABLE_NAME = "crops_item_master";
    // Table columns
    public static final String item_no = "item_no";
    public static final String name = "name";
    public static final String item_desc = "item_desc";
    public static final String base_unit_of_measure = "base_unit_of_measure";
    public static final String inventory_posting_group = "inventory_posting_group";
    public static final String crop_code = "crop_code";
    public static final String class_of_seeds = "class_of_seeds";
    public static final String item_type = "item_type";
    public static final String fg_pack_size = "fg_pack_size";
    public static final String production_code = "production_code";
    public static final String marketing_code = "marketing_code";
    public static final String item_group = "item_group";
    public static final String crop_type = "crop_type";
    public static final String class_of_variety = "class_of_variety";
    public static final String image_url = "image_url";
    public static final String active = "active";
    public static final String crop_category ="crop_category";
    public static final String  crop="crop";
    // Creating table query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            item_no + " TEXT PRIMARY KEY," +
            name + " TEXT," +
            item_desc + " TEXT," +
            base_unit_of_measure + " TEXT," +
            inventory_posting_group + " TEXT," +
            crop_code + " TEXT," +
            class_of_seeds + " TEXT," +
            item_type + " TEXT," +
            fg_pack_size + " TEXT," +
            production_code + " TEXT," +
            marketing_code + " TEXT," +
            item_group + " TEXT," +
            crop_type + " TEXT," +
            class_of_variety + " TEXT," +
            image_url + " TEXT," +
            active + " INTEGER," +
            crop_category + " TEXT," +
            crop + " TEXT" +
            ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //todo all functionality  regarding this table

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CropItemMasterTable(Context context) {
        this.context = context;
    }

    public CropItemMasterTable open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public void insertArray(List<CropItemMasterModel> bulkdata) {
        database.beginTransaction();
        try {
            for (CropItemMasterModel data : bulkdata) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(this.item_no, data.item_no);
                contentValue.put(this.name, data.name);
                contentValue.put(this.item_desc, data.item_desc);
                contentValue.put(this.base_unit_of_measure, data.base_unit_of_measure);
                contentValue.put(this.inventory_posting_group, data.inventory_posting_group);
                contentValue.put(this.crop_code, data.crop_code);
                contentValue.put(this.class_of_seeds, data.class_of_seeds);
                contentValue.put(this.item_type, data.item_type);
                contentValue.put(this.fg_pack_size, data.fg_pack_size);
                contentValue.put(this.production_code, data.production_code);
                contentValue.put(this.marketing_code, data.marketing_code);
                contentValue.put(this.item_group, data.item_group);
                contentValue.put(this.crop_type, data.crop_type);
                contentValue.put(this.class_of_variety, data.class_of_variety);
                contentValue.put(this.image_url, data.image_url);
                contentValue.put(this.active, data.active);
                contentValue.put(this.crop_category, data.crop_category);
                contentValue.put(this.crop, data.crop);
                database.replace(TABLE_NAME, null, contentValue);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<CropItemMasterModel> fetch(String cropcode) {
        List<CropItemMasterModel> returnData = new ArrayList<>();
        String[] columns = new String[]{item_no, name, item_desc, base_unit_of_measure, inventory_posting_group, crop_code, class_of_seeds, item_type,
                fg_pack_size, production_code, marketing_code, item_group, crop_type, class_of_variety, image_url, active,crop_category,crop};
        Cursor cursor = database.query(TABLE_NAME, columns, crop_code + "='" + cropcode + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                returnData.add(new CropItemMasterModel(
                        cursor.getString(cursor.getColumnIndex(this.item_no)),
                        cursor.getString(cursor.getColumnIndex(this.name)),
                        cursor.getString(cursor.getColumnIndex(this.item_desc)),
                        cursor.getString(cursor.getColumnIndex(this.base_unit_of_measure)),
                        cursor.getString(cursor.getColumnIndex(this.inventory_posting_group)),
                        cursor.getString(cursor.getColumnIndex(this.crop_code)),
                        cursor.getString(cursor.getColumnIndex(this.class_of_seeds)),
                        cursor.getString(cursor.getColumnIndex(this.item_type)),
                        cursor.getString(cursor.getColumnIndex(this.fg_pack_size)),
                        cursor.getString(cursor.getColumnIndex(this.production_code)),
                        cursor.getString(cursor.getColumnIndex(this.marketing_code)),
                        cursor.getString(cursor.getColumnIndex(this.item_group)),
                        cursor.getString(cursor.getColumnIndex(this.crop_type)),
                        cursor.getString(cursor.getColumnIndex(this.class_of_variety)),
                        cursor.getString(cursor.getColumnIndex(this.image_url)),
                        cursor.getInt(cursor.getColumnIndex(this.active)),
                        cursor.getString(cursor.getColumnIndex(this.crop_category)),
                        cursor.getString(cursor.getColumnIndex(this.crop))
                ));
            } while (cursor.moveToNext());
        }
        return returnData;
    }

    public String getCropItemname(String item_no) {
        String[] columns = new String[]{name};
        Cursor cursor = database.query(TABLE_NAME, columns, this.item_no + "='" + item_no + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                return cursor.getString(cursor.getColumnIndex(this.name));
            } while (cursor.moveToNext());
        }
        return "";
    }

    public void delete(String item_no) {
        database.delete(TABLE_NAME, this.item_no + "=" + item_no, null);
    }

    public class CropItemMasterModel {
        public String item_no;
        public String name;
        public String item_desc;
        public String base_unit_of_measure;
        public String inventory_posting_group;
        public String crop_code;
        public String class_of_seeds;
        public String item_type;
        public String fg_pack_size;
        public String production_code;
        public String marketing_code;
        public String item_group;
        public String crop_type;
        public String class_of_variety;
        public String image_url;
        public String crop_category;
        public String crop;
        public int active;
        //todo use in add tocart
        public int total_buy_qty = 0;

        public CropItemMasterModel(String item_no, String name, String item_desc, String base_unit_of_measure, String inventory_posting_group, String crop_code,
                                   String class_of_seeds, String item_type, String fg_pack_size,
                                   String production_code, String marketing_code, String item_group,
                                   String crop_type, String class_of_variety, String image_url, int active,String crop_category,String crop) {
            this.item_no = item_no;
            this.name = name;
            this.item_desc = item_desc;
            this.base_unit_of_measure = base_unit_of_measure;
            this.inventory_posting_group = inventory_posting_group;
            this.crop_code = crop_code;
            this.class_of_seeds = class_of_seeds;
            this.item_type = item_type;

            this.fg_pack_size = fg_pack_size;
            this.production_code = production_code;
            this.marketing_code = marketing_code;
            this.item_group = item_group;
            this.crop_type = crop_type;
            this.class_of_variety = class_of_variety;
            this.image_url = image_url;
            this.active = active;
            this.crop_category=crop_category;
            this.crop=crop;
        }
    }
}
