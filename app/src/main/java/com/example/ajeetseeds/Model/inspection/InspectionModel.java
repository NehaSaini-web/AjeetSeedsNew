package com.example.ajeetseeds.Model.inspection;

import java.util.List;

public class InspectionModel {
    public boolean condition;
    public String message;
    public String arrival_plan_no;
    public String organizer_no;
    public String organizer_name;
    public String organizer_name_2;
    public String organizer_address;
    public String organizer_address_2;
    public String city;
    public String contact;
    public String season_code;
    public List<Inspection_Line> il;

    public class Inspection_Line {
        public String arrival_plan_no;
        public String production_lot_no;
        public String variety_no;
        public String  grower_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String item_crop;
        public String item_croptype;
        public String itemclassofseeds;

        public int inspection_1;
        public int inspection_2;
        public int inspection_3;
        public int inspection_4;
        public int inspection_qc;
    }
}
