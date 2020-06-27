package com.example.ajeetseeds.ui.order_creation;

import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

import java.util.ArrayList;
import java.util.List;

public class OrderBookGlobalModel {
    public int alertCount=0;
    public CustomerMasterTable.CustomerMasterModel selectdCustomer = null;
    public List<CropItemMasterTable.CropItemMasterModel> selectedCropItem=new ArrayList<>();
}
