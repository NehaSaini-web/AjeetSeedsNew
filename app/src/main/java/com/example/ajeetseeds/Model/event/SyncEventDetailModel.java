package com.example.ajeetseeds.Model.event;

import java.util.List;

public class SyncEventDetailModel {
    public boolean condition;
    public String message;
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
    public List<ExpanceLineModel> expense_line;

    public String crop_name;
    public String variety_name;
    public String state_name;
    public String district_name;
    public String taluka_name;
    public class ExpanceLineModel{
        public String event_code;
        public String line_no;
        public String expense_type;
        public String quantity;
        public String rate_unit_cost;
        public String amount;
        public String created_on;
    }
}
