package com.example.ajeetseeds.ui.order_creation.employee;

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
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.ui.order_creation.orderBook.model.OrderBookModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EmployeeOrderBookFragment extends Fragment {
    AutoCompleteTextView selected_distributer_dropdown;
    private EmployeeOrderBookViewModel mViewModel;
    Button submitPage;

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
        submitPage = view.findViewById(R.id.submitPage);
        sessionManagement = new SessionManagement(getActivity());
        bindCustomerDropdown();
        EventsOnFragments();

    }

    public List<CustomerMasterTable.CustomerMasterModel> customerList;

    void bindCustomerDropdown() {
        try {
            CustomerMasterTable customerMasterTable = new CustomerMasterTable(getActivity());
            customerMasterTable.open();
            customerList = customerMasterTable.fetch();
            customerMasterTable.close();
            CustomerAdapter fruitAdapter = new CustomerAdapter(getActivity(), R.layout.drop_down_textview, customerList);
            selected_distributer_dropdown.setAdapter(fruitAdapter);
        } catch (Exception e) {

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
            loadFragments(R.id.nav_order_book, "Order Book");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Employee Order Book");
    }
}
