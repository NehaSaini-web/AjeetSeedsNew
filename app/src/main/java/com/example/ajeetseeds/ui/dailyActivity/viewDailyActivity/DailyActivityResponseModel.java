package com.example.ajeetseeds.ui.dailyActivity.viewDailyActivity;

import java.util.List;

public class DailyActivityResponseModel {
    public boolean condition;
    public String activity_no;
    public String contact_no;
    public String contact_no1;
    public String order_collected;
    public String payment_collected;
    public String updated_on;
    public String created_by;
    public List<DailyLineModel> d_line;

    public class DailyLineModel {
        public String id;
        public String activity_no;
        public String farmer_name;
        public String district;
        public String village;
        public String ajeet_crop_and_verity;
        public String ajeet_crop_age;
        public String ajeet_fruits_per;
        public String ajeet_pest;
        public String ajeet_disease;
        public String check_crop_and_verity;
        public String check_crop_age;
        public String check_fruits_per;
        public String check_pest;
        public String check_disease;
    }
}
