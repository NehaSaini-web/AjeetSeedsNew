package com.example.ajeetseeds.Model.syncModel;

import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

import java.util.List;

public class CropMasterSyncModel {
    public List<CropMasterTable.CropMasterModel> crop_master;
    public List<CropItemMasterTable.CropItemMasterModel> crop_item_master;
    public List<CustomerMasterTable.CustomerMasterModel> customer_master;
}
