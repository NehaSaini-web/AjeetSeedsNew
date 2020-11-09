package com.example.ajeetseeds.Model.travel;

import java.util.List;

public class SyncTravelDetailModel {
    public boolean condition;
    public String message;
    public String travelcode;
    public String from_loc;
    public String to_loc;
    public String start_date;
    public String end_date;
    public String travel_reson;
    public String expense_budget;
    public String approve_budget;
    public String created_on;
    public String user_type;
    public String created_by;
    public String STATUS;
    public String approver_id;
    public String approve_on;
    public String reason;
    public String from_loc_name;
    public String to_loc_name;
    public List<Travel_line_Expense> travel_line_expense;

    public class Travel_line_Expense {
        public String travelcode;
        public String line_no;
        public String date;
        public String from_loc;
        public String to_loc;
        public String departure;
        public String arrival;
        public String fare;
        public String mode_of_travel;
        public String loading_in_any;
        public String distance_km;
        public String fuel_vehicle_expance;
        public String daily_express;
        public String vehicle_repairing;
        public String local_convance;
        public String other_expenses;
        public String total_amount_calulated;
        public String created_on;

        public String mod_city;
        public String mod_lodging;
        public String mod_da_half;
        public String mode_da_full;
        public String mod_ope_max;
        public String user_grade;
    }

}
