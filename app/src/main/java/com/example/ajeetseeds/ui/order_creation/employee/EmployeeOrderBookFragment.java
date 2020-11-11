package com.example.ajeetseeds.ui.order_creation.employee;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.ajeetseeds.MainActivity;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.ui.order_creation.orderBook.model.OrderBookModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EmployeeOrderBookFragment extends Fragment {
    AutoCompleteTextView selected_distributer_dropdown;
    private EmployeeOrderBookViewModel mViewModel;
    Button submitPage;
    TextInputLayout tiL_dropdown_custo;
    public static EmployeeOrderBookFragment newInstance() {
        return new EmployeeOrderBookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_order_book_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EmployeeOrderBookViewModel.class);
        // TODO: Use the ViewModel
    }

    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selected_distributer_dropdown = view.findViewById(R.id.selected_distributer_dropdown);
        selected_crop_category_data = view.findViewById(R.id.selected_crop_category_data);
        tiL_dropdown_custo=view.findViewById(R.id.tiL_dropdown_custo);
        submitPage = view.findViewById(R.id.submitPage);
        sessionManagement = new SessionManagement(getActivity());
        EventsOnFragments();
        bindCategoryFilterList();
    }

    public List<CustomerMasterTable.CustomerMasterModel> customerList;
    CustomerAdapter customer_Adapter;

    void bindCustomerDropdown() {
        try {
            CustomerMasterTable customerMasterTable = new CustomerMasterTable(getActivity());
            customerMasterTable.open();
            customerList = customerMasterTable.fetch(select_by_user_crop_code);
            customerMasterTable.close();
            customer_Adapter = new CustomerAdapter(getActivity(), R.layout.drop_down_textview, customerList);
            selected_distributer_dropdown.setAdapter(customer_Adapter);
        } catch (Exception e) {

        }
    }

    List<CropMasterTable.CropMasterModel> crop_list = new ArrayList<>();
    ChipGroup selected_crop_category_data;
    String select_by_user_crop_code="";

    int getDpValue(int dpValue) {
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics()
        );
        return paddingDp;
    }

    private void bindCategoryFilterList() {
        try {
            if (crop_list.size() <= 0) {
                CropMasterTable cropMasterTable = new CropMasterTable(getActivity());
                cropMasterTable.open();
                crop_list = cropMasterTable.fetch();
                cropMasterTable.close();
            }
        } catch (Exception e) {
        }
        selected_crop_category_data.removeAllViews();
        for (CropMasterTable.CropMasterModel cropObject : crop_list) {
            final CropMasterTable.CropMasterModel final_cropObject = cropObject;
            final Chip chip = new Chip(getActivity());
            //  chip.setPadding(getDpValue(10), getDpValue(10), getDpValue(10), getDpValue(10));
            chip.setText(final_cropObject.description);
            chip.setElevation(getDpValue(2));
            chip.setChipStrokeWidth(getDpValue(1));
            if (select_by_user_crop_code.equalsIgnoreCase(final_cropObject.code)) {
                chip.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setChipStrokeColorResource(R.color.colorPrimary);
            } else {
                chip.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                chip.setChipBackgroundColorResource(R.color.white);
                chip.setChipStrokeColorResource(R.color.chipripple);
                chip.setRippleColorResource(R.color.chipripple);
            }
            chip.setCloseIconEnabled(false);
            chip.setOnClickListener(v -> {
                if (select_by_user_crop_code.equalsIgnoreCase(final_cropObject.code)) {
                    select_by_user_crop_code = "";
                } else {
                    select_by_user_crop_code = final_cropObject.code;
                }
                bindCategoryFilterList();
                if(select_by_user_crop_code.equalsIgnoreCase("")){
                    tiL_dropdown_custo.setVisibility(View.GONE);
                    submitPage.setVisibility(View.GONE);
                    selectdCustomer=null;
                }else{
                    tiL_dropdown_custo.setVisibility(View.VISIBLE);
                    submitPage.setVisibility(View.VISIBLE);
                    bindCustomerDropdown();
                }

            });
            chip.setChipIconTintResource(R.color.colorPrimary);
            selected_crop_category_data.addView(chip);
        }
    }

    private void loadFragments(int id, String fragmentName) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id);
        getActivity().setTitle(fragmentName);
    }

    CustomerMasterTable.CustomerMasterModel selectdCustomer = null;

    void EventsOnFragments() {
        selected_distributer_dropdown.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectdCustomer = customerList.get(i);
            selected_distributer_dropdown.setError(null);
        });
        submitPage.setOnClickListener(view -> {
            if (selectdCustomer == null) {
                selected_distributer_dropdown.setError("Please Select Distributer.");
                return;
            }
            if (MainActivity.orderBookGlobalModel.selectdCustomer == null || !MainActivity.orderBookGlobalModel.selectdCustomer.no.equalsIgnoreCase(selectdCustomer.no)) {
                MainActivity.orderBookGlobalModel.alertCount = 0;
                MainActivity.orderBookGlobalModel.selectdCustomer = selectdCustomer;
                MainActivity.orderBookGlobalModel.selectedCropItem = new ArrayList<>();
                Intent i = new Intent("cartItemCountBrodcast");
                i.putExtra("cartcount", MainActivity.orderBookGlobalModel.alertCount);
                getActivity().sendBroadcast(i);
            }
            select_by_user_crop_code="";
            selected_distributer_dropdown.setText("");
            selectdCustomer=null;
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(selected_distributer_dropdown.getWindowToken(), 0);
            loadFragments(R.id.nav_order_book, "Order Book");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Employee Order Book");
    }
}
