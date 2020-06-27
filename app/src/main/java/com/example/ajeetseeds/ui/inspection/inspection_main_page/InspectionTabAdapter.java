package com.example.ajeetseeds.ui.inspection.inspection_main_page;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.ui.inspection.inspection_four.InspectionFourFragment;
import com.example.ajeetseeds.ui.inspection.inspection_one.InspectionOneFragment;
import com.example.ajeetseeds.ui.inspection.inspection_qc.InspectionQcFragment;
import com.example.ajeetseeds.ui.inspection.inspection_three.InspectionThreeFragment;
import com.example.ajeetseeds.ui.inspection.inspectiontwo.InspectionTwoFragment;
import com.google.gson.Gson;

public class InspectionTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    InspectionModel inspectionModel;
    String selected_production_lot_no;

    public InspectionTabAdapter(Context context, FragmentManager fm, int totalTabs, InspectionModel inspectionModel, String selected_production_lot_no) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.inspectionModel = inspectionModel;
        this.selected_production_lot_no = selected_production_lot_no;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("inspection_header", new Gson().toJson(inspectionModel));
        bundle.putString("Selected_production_lot_no", selected_production_lot_no);

        switch (position) {
            case 0:
                InspectionOneFragment inspectionOneFragment0 = new InspectionOneFragment();
                inspectionOneFragment0.setArguments(bundle);
                return inspectionOneFragment0;
            case 1:
                InspectionTwoFragment inspectionTwoFragment = new InspectionTwoFragment();
                inspectionTwoFragment.setArguments(bundle);
                return inspectionTwoFragment;
            case 2:
                InspectionThreeFragment inspectionThreeFragment = new InspectionThreeFragment();
                inspectionThreeFragment.setArguments(bundle);
                return inspectionThreeFragment;
            case 3:
                InspectionFourFragment inspectionFourFragment = new InspectionFourFragment();
                inspectionFourFragment.setArguments(bundle);
                return inspectionFourFragment;
            case 4:
                InspectionQcFragment inspectionQcFragment = new InspectionQcFragment();
                inspectionQcFragment.setArguments(bundle);
                return inspectionQcFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}