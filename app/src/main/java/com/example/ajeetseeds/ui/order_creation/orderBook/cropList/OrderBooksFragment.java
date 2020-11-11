package com.example.ajeetseeds.ui.order_creation.orderBook.cropList;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ajeetseeds.MainActivity;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.ui.order_creation.orderBook.model.OrderBookModel;
import com.google.gson.Gson;

import java.util.List;

public class OrderBooksFragment extends Fragment {

    private OrderBooksViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static OrderBooksFragment newInstance() {
        return new OrderBooksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_books_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderBooksViewModel.class);
        // TODO: Use the ViewModel
    }

    List<CropMasterTable.CropMasterModel> crop_list;
    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        try {
            CropMasterTable cropMasterTable = new CropMasterTable(getActivity());
            cropMasterTable.open();
            if (sessionManagement.getUser_type().contentEquals("Employee"))
                crop_list = cropMasterTable.fetch(MainActivity.orderBookGlobalModel.selectdCustomer.customer_crop_code);
            else if (sessionManagement.getUser_type().contentEquals("Customer"))
                crop_list = cropMasterTable.fetch();

            cropMasterTable.close();
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new OrderCropListAdapter(crop_list, getActivity(), item -> {
                loadFragments(R.id.nav_crop_item, "Crop Item", new Gson().toJson(item));
            });
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {

        }
    }

    private void loadFragments(int id, String fragmentName, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("dataPass", data);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(MainActivity.orderBookGlobalModel.selectdCustomer != null ? MainActivity.orderBookGlobalModel.selectdCustomer.name + " Order Book" : "Order Book");
    }
}
