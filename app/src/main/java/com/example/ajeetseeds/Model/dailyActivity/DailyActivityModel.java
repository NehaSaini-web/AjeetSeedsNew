package com.example.ajeetseeds.Model.dailyActivity;

import java.util.List;

public class DailyActivityModel {
    public String android_activity_no;
    public String contact;
    public String contact1;
    public String order_collected;
    public String payment_collected;
    public List<DailyActivityLines> addlines;

    public class DailyActivityLines{

        public String farmer_name;
        public String district;
        public String village;

        public String ajeet_crop_and_verity;
        public String ajeet_crop_age;
        public String ajeet_fruits_per;
        public String ajeet_pest;
        public String ajeet_disease;

        public String check_crop_and_variety;
        public String check_crop_age;
        public String check_fruits_per;
        public String check_pest;
        public String check_disease;
    }
}
