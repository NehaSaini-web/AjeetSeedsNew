package com.example.ajeetseeds.sqlLite.travel;

public class TravelLineExpenseModel {
    public String android_travelcode;
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

    public String from_loc_name;
    public String to_loc_name;

    public TravelLineExpenseModel(String android_travelcode, String travelcode, String line_no, String date, String from_loc, String to_loc,
                                  String departure, String arrival, String fare, String mode_of_travel, String loading_in_any, String distance_km,
                                  String fuel_vehicle_expance, String daily_express, String vehicle_repairing, String local_convance,
                                  String other_expenses, String total_amount_calulated, String created_on,
                                  String mod_city, String mod_lodging, String mod_da_half, String mode_da_full, String mod_ope_max,
                                  String user_grade) {
        this.android_travelcode = android_travelcode;
        this.travelcode = travelcode;
        this.line_no = line_no;
        this.date = date;
        this.from_loc = from_loc;
        this.to_loc = to_loc;
        this.departure = departure;
        this.arrival = arrival;
        this.fare = fare;
        this.mode_of_travel = mode_of_travel;
        this.loading_in_any = loading_in_any;
        this.distance_km = distance_km;
        this.fuel_vehicle_expance = fuel_vehicle_expance;
        this.daily_express = daily_express;
        this.vehicle_repairing = vehicle_repairing;
        this.local_convance = local_convance;
        this.other_expenses = other_expenses;
        this.total_amount_calulated = total_amount_calulated;
        this.created_on = created_on;

        this.mod_city = mod_city;
        this.mod_lodging = mod_lodging;
        this.mod_da_half = mod_da_half;
        this.mode_da_full = mode_da_full;
        this.mod_ope_max = mod_ope_max;
        this.user_grade = user_grade;

    }
}