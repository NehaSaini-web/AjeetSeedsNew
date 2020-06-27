package com.example.ajeetseeds.ui.dailyActivity.addLine;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.ajeetseeds.Model.dailyActivity.DailyActivityModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddLinesFragment extends Fragment {
    DailyActivityModel dailyActivityModel;
    AutoCompleteTextView filled_district_dropdown;
    TextInputLayout text_input;
    Button submitPage;
    TextInputEditText edit_farmer_name, edit_village,
            edit_ajeet_crop_and_verity, edit_ajeet_crop_age, edit_ajeet_fruits_per, edit_ajeet_pest, edit_ajeet_disease,
            edit_check_crop_and_verity, edit_check_crop_age, edit_check_fruits_per, edit_check_pest, edit_check_disease;
    private AddLinesViewModel mViewModel;

    public static AddLinesFragment newInstance() {
        return new AddLinesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_lines_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddLinesViewModel.class);
        // TODO: Use the ViewModel
    }

    void initView(View view) {
        dailyActivityModel = new Gson().fromJson(getArguments().getString("dataPass", ""), DailyActivityModel.class);
        filled_district_dropdown = view.findViewById(R.id.filled_district_dropdown);
        submitPage = view.findViewById(R.id.submitPage);
        text_input = view.findViewById(R.id.text_input);

        edit_farmer_name = view.findViewById(R.id.edit_farmer_name);
        edit_village = view.findViewById(R.id.edit_village);

        edit_ajeet_crop_and_verity = view.findViewById(R.id.edit_ajeet_crop_and_verity);
        edit_ajeet_crop_age = view.findViewById(R.id.edit_ajeet_crop_age);
        edit_ajeet_fruits_per = view.findViewById(R.id.edit_ajeet_fruits_per);
        edit_ajeet_pest = view.findViewById(R.id.edit_ajeet_pest);
        edit_ajeet_disease = view.findViewById(R.id.edit_ajeet_disease);

        edit_check_crop_and_verity = view.findViewById(R.id.edit_check_crop_and_verity);
        edit_check_crop_age = view.findViewById(R.id.edit_check_crop_age);
        edit_check_fruits_per = view.findViewById(R.id.edit_check_fruits_per);
        edit_check_pest = view.findViewById(R.id.edit_check_pest);
        edit_check_disease = view.findViewById(R.id.edit_check_disease);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        eventOnUI();
        bindDistrict();
    }

    List<String> district_list = new ArrayList<>();

    void bindDistrict() {
        if (!district_list.isEmpty()) {
            district_list.clear();
        }
        DistrictMasterTable districtMasterTable = new DistrictMasterTable(getContext());
        districtMasterTable.open();
        List<DistrictMasterTable.DistrictMaster> returnData = districtMasterTable.fetch();
        districtMasterTable.close();
        districtadapter = new DistrictAdapter(getContext(), R.layout.drop_down_textview, returnData);
        filled_district_dropdown.setAdapter(districtadapter);
    }

    DistrictAdapter districtadapter;

    void eventOnUI() {
        //todo if dropdown use this is autocomplete
        filled_district_dropdown.setOnClickListener(view -> {
            if (filled_district_dropdown.hasFocus()) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        //todo end

        submitPage.setOnClickListener(view -> {
            if (dailyActivityModel.addlines == null) {
                dailyActivityModel.addlines = new ArrayList<>();
            }
            if (edit_farmer_name.getText().toString().contentEquals("")) {
                edit_farmer_name.setError("Please Enter Farmer Name.");
                return;
            } else if (filled_district_dropdown.getText().toString().contentEquals("")) {
                filled_district_dropdown.setError("Please Select District.");
                return;
            } else if (edit_village.getText().toString().contentEquals("")) {
                edit_village.setError("Please Enter Village");
                return;
            }
            DailyActivityModel.DailyActivityLines postthedata = dailyActivityModel.new DailyActivityLines();
            postthedata.farmer_name = edit_farmer_name.getText().toString();
            postthedata.district = SelectedDistrict;
            postthedata.village = edit_village.getText().toString();

            postthedata.ajeet_crop_and_verity = edit_ajeet_crop_and_verity.getText().toString();
            postthedata.ajeet_crop_age = edit_ajeet_crop_age.getText().toString();
            postthedata.ajeet_fruits_per = edit_ajeet_fruits_per.getText().toString();
            postthedata.ajeet_pest = edit_ajeet_pest.getText().toString();
            postthedata.ajeet_disease = edit_ajeet_disease.getText().toString();

            postthedata.check_crop_and_variety = edit_check_crop_and_verity.getText().toString();
            postthedata.check_crop_age = edit_check_crop_age.getText().toString();
            postthedata.check_fruits_per = edit_check_fruits_per.getText().toString();
            postthedata.check_pest = edit_check_pest.getText().toString();
            postthedata.check_disease = edit_check_disease.getText().toString();
            dailyActivityModel.addlines.add(postthedata);
            loadFragments(R.id.nav_daily_activity, "Daily Activity", new Gson().toJson(dailyActivityModel));
        });
        filled_district_dropdown.setOnItemClickListener((adapterView, view1, i, l) -> {
            SelectedDistrict = districtadapter.getItem(i).code;
        });
    }

    String SelectedDistrict = "";

    private void loadFragments(int id, String fragmentName, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("dataPass", data);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack(id, true);
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

    @Override
    public void onResume() {
        super.onResume();
        dailyActivityModel = new Gson().fromJson(getArguments().getString("dataPass", ""), DailyActivityModel.class);
        getActivity().setTitle("Add Lines");
    }
}
