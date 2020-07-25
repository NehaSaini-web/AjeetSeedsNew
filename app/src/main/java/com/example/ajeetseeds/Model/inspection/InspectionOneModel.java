package com.example.ajeetseeds.Model.inspection;

import java.util.List;

public class InspectionOneModel {
    public boolean condition;
    public String message;
    public String arrival_plan_no;
    public String production_lot_no;
    public int inspection_1;
    public String one_complete_on;
    public int inspection_2;
    public String two_completed_on;
    public int inspection_3;
    public String three_completed_on;
    public int inspection_4;
    public String forth_completed_on;
    public int inspection_qc;
    public String qc_completed_on;

    public String grower_name;
    public String item_no;
    public String item_name;
    public String crop_code;
    public String item_crop;
    public String item_croptype;
    public String itemclassofseeds;

    public String net_area_as_per_ins2;
    public String   crossing_start_date_ins2;
    public String durationofCrop;

    public String net_area_as_per_insp_3;
    public String   crossing_start_date;
    public String crossing_end_date;

    public List<InspectionLineModel> io;

    public class InspectionLineModel {
        public String arrival_plan_no;
        public String production_lot_no;
        public String line_no;
        public String grower_or_land_owner_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String crop_name;
        public String Item_class_of_seeds;
        public String item_crop_type;
        public String date_of_inspection;
        public String isolation_distance_status;
        public String isolation_distance_in_metre;
        public String previous_crop;
        public String germination_status;
        public String germination_per;
        public String area;
        public String rejection_area;
        public String net_area;
        public String spacing_variety;
        public String spacing_male;
        public String spacing_female;
        public String plant_population_variety;
        public String plant_population_male;
        public String plant_population_female;
        public String crop_condition;
        public String crop_stage;
        public String suggestion_to_grower;
        //todo new add field
        public String  planting_sowing_date_female;
        public String planting_sowing_date_other;
        public String spacing_female_row;
        public String spacing_female_plant;
        public String spacing_variety_row;
        public String spacing_variety_plant;
        public String spacing_male_row;
        public String spacing_male_plant;
    }

    //todo for inspection 2
    public List<InspectionTwoLines> it;

    public class InspectionTwoLines {
        public String arrival_plan_no;
        public String production_lot_no;
        public String line_no;
        public String grower_or_land_owner_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String crop_name;
        public String Item_class_of_seeds;
        public String item_crop_type;
        public String date_of_inspection;
        public String crop_condition;
        public String crop_stage;
        public String net_area_as_per_insp1;
        public String pld_area;
        public String rejected_area;
        public String net_area;
        public String crossing_start_date;
        public String avg_crossing_per_day;
        public String avg_cross_boll_per_plant;
        public String self_boll_per_plant;
        public String off_type_plant;
        public String suggestion_to_grower;

        public String  planting_sowing_date_female;
        public String planting_sowing_date_other;
        public String spacing_female_row;
        public String spacing_female_plant;
        public String spacing_variety_row;
        public String spacing_variety_plant;
        public String spacing_male_row;
        public String spacing_male_plant;
    }

    //todo inspection three
    public List<InspectionThreeLineModel> ithree;

    public class InspectionThreeLineModel {
        public String arrival_plan_no;
        public String production_lot_no;
        public String line_no;
        public String grower_or_land_owner_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String crop_name;
        public String Item_class_of_seeds;
        public String item_crop_type;
        public String date_of_inspection;
        public String crop_condition;
        public String crop_stage;
        public String net_area_as_per_insp2;
        public String not_cross_area;
        public String net_cross_area;
        public String crossing_start_date;
        public String avg_crossing_per_day;
        public String self_boll_per_plant;
        public String crossing_end_date;
        public String kapas_picking_if_any;
        public String name_of_fertilizer;
        public String fertilizer_date;
        public String fertilizer_dose;
        public String sprying_fungi_or_insecticide_date;
        public String name_of_insecticide_or_fungicide;
        public String sprying_fungi_or_insecticide_dose;
        public String other_specific_observations;
        public String suggestion_to_grower;
        public String durationofCrop;
    }

    public List<InspectionFourLineModel> ifour;

    public class InspectionFourLineModel {
        public String arrival_plan_no;
        public String production_lot_no;
        public String line_no;
        public String grower_or_land_owner_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String crop_name;
        public String Item_class_of_seeds;
        public String item_crop_type;
        public String date_of_inspection;
        public String crop_condition;
        public String crop_stage;
        public String net_area_as_per_insp_3;
        public String crossing_start_date;
        public String crossing_end_date;
        public String final_plant_population;
        public String avg_cross_boll_per_plant;
        public String kapas_picking_if_any;
        public String approx_kapas_balance_for_picking;
        public String estimated_field_in_kg;
        public String other_specific_observations;
        public String suggestion_to_grower;

        public String harvestingDateMale;
        public String harvestingDateFemale;
        public String harvestingDateOther;
    }

    public List<InspectionQCLineModel> iQC;

    public class InspectionQCLineModel {
        public String arrival_plan_no;
        public String production_lot_no;
        public String line_no;
        public String organizer_or_co_ordinator_name;
        public String grower_or_land_owner_name;
        public String item_no;
        public String item_name;
        public String crop_code;
        public String crop_name;
        public String Item_class_of_seeds;
        public String item_crop_type;
        public String crop_condition;
        public String crop_stage;
        public String date_of_inspection;
        public String suggestion_to_grower;
        public String avg_crossing_per_day;
        public String avg_cross_boll_per_plant;
        public String self_boll_per_plant;
        public String kapas_picking_if_any;
        public String approx_kapas_balance_for_picking;
        public String estimated_field_in_kg;
        public String name_of_fertilizer;
        public String fertilizer_date;
        public String fertilizer_dose;
        public String sprying_fungi_or_insecticide_date;
        public String name_of_insecticide_or_fungicide;
        public String sprying_fungi_or_Insecticide_dose;
        public String plants_rouged_male;
        public String plants_rouged_female;
    }
}
