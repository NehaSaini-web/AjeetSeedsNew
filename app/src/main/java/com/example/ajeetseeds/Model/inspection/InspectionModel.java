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
    public String create_on;
    public String Zone_Code;
    public String State_Code;
    public String District_Code;
    public String Region_Code;
    public String Taluka_Code;
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

        public String  NetArea;
        public String  NetAreaAsPerPrevINSP;
        public String CrossingStartDate;
        public String PreviousInspectionType;
        public String DurationofCrop;
        public String GivenArea;
        public String  PlantPopulationVariety;
        public String PlantPopulationFemale;
        public String PreviousCrop;
        public String Area;
        public String SowingDateFemale;
        public String SowingDateOther;
        public String item_weight;

        public String Grower_Village;
        public int inspection_1;
        public int inspection_2;
        public int inspection_3;
        public int inspection_4;
        public int inspection_qc;

        public String location_code;
        public String location_name;
    }
}
