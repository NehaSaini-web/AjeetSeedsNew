package com.example.ajeetseeds.ui.order_creation.orderBook.model;

import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

public class OrderBookModel {
    public CustomerMasterTable.CustomerMasterModel selectdCustomer;
    public CropMasterTable.CropMasterModel selectedCrop;
}
