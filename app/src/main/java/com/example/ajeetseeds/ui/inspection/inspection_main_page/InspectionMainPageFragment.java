package com.example.ajeetseeds.ui.inspection.inspection_main_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class InspectionMainPageFragment extends Fragment {

    private InspectionMainPageViewModel mViewModel;

    public static InspectionMainPageFragment newInstance() {
        return new InspectionMainPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inspection_main_page_fragment, container, false);
    }

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    InspectionModel inspectionModel = null;
    String selected_production_lot_no = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            selected_production_lot_no = getArguments().getString("Selected_production_lot_no", "");
            inspectionModel = new Gson().fromJson(getArguments().getString("inspection_header", ""), InspectionModel.class);
        }
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("INS 1"));
        tabLayout.getTabAt(0).setIcon(R.drawable.inspection_1);
        tabLayout.addTab(tabLayout.newTab().setText("INS 2"));
        tabLayout.getTabAt(1).setIcon(R.drawable.inspection_2);
        tabLayout.addTab(tabLayout.newTab().setText("INS 3"));
        tabLayout.getTabAt(2).setIcon(R.drawable.inspection_3);
        tabLayout.addTab(tabLayout.newTab().setText("INS 4"));
        tabLayout.getTabAt(3).setIcon(R.drawable.inspection_4);
        tabLayout.addTab(tabLayout.newTab().setText("INS QC"));
        tabLayout.getTabAt(4).setIcon(R.drawable.inspection_qc);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        InspectionTabAdapter adapter = new InspectionTabAdapter(getActivity(), getChildFragmentManager(), tabLayout.getTabCount(), inspectionModel, selected_production_lot_no);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(inspectionModel.inspection_1>0){
            viewPager.setCurrentItem(1);
        }
        if(inspectionModel.inspection_2>0){
            viewPager.setCurrentItem(2);
        }
        if(inspectionModel.inspection_3>0){
            viewPager.setCurrentItem(3);
        }
        if(inspectionModel.inspection_4>0){
            viewPager.setCurrentItem(0);
        }

    }

}
