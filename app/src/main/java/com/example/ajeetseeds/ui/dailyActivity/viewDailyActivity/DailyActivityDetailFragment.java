package com.example.ajeetseeds.ui.dailyActivity.viewDailyActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.dailyActivity.DailyActivityModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityHeader;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityLine;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.ui.order_creation.order_approval.OrderApprovalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DailyActivityDetailFragment extends Fragment {

    private DailyActivityDetailViewModel mViewModel;

    public static DailyActivityDetailFragment newInstance() {
        return new DailyActivityDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_activity_detail_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DailyActivityDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView dailyActivityList;
    DailyActivityListAdapter DailyActivityAdapter;
    List<DailyActivityHeader.DailyActivityHeaderModel> listDailyActivityheader = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        dailyActivityList = view.findViewById(R.id.dailyActivityList);
        try {
            DailyActivityHeader dailyActivityHeader = new DailyActivityHeader(getActivity());
            dailyActivityHeader.open();
            listDailyActivityheader = dailyActivityHeader.fetch();
            dailyActivityHeader.close();
        } catch (Exception e) {
        }
        DailyActivityAdapter = new DailyActivityListAdapter(getActivity(), listDailyActivityheader);
        dailyActivityList.setAdapter(DailyActivityAdapter);
        dailyActivityList.setOnItemClickListener((adapterView, view1, i, l) -> {
            try {
                DailyActivityLine dailyActivityLine = new DailyActivityLine(getActivity());
                dailyActivityLine.open();
                List<DailyActivityLine.DailyActivityLinesModel> customeModels = dailyActivityLine.fetch_by_acivityno(listDailyActivityheader.get(i).activity_no);
                dailyActivityLine.close();
                OrderDetailItem(customeModels, listDailyActivityheader.get(i));
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    void OrderDetailItem(List<DailyActivityLine.DailyActivityLinesModel> selectedOrderLine, DailyActivityHeader.DailyActivityHeaderModel headerdata) {
        DailyActivityModel passData = new DailyActivityModel();
        passData.contact = headerdata.contact_no;
        passData.contact1 = headerdata.contact_no;
        passData.payment_collected = headerdata.contact_no;
        passData.order_collected = headerdata.contact_no;
        passData.addlines = new ArrayList<>();
        for (int i = 0; i < selectedOrderLine.size(); i++) {
            DailyActivityModel.DailyActivityLines lines = passData.new DailyActivityLines();
            lines.farmer_name = selectedOrderLine.get(i).farmer_name;
            lines.district = selectedOrderLine.get(i).district;
            lines.village = selectedOrderLine.get(i).village;

            lines.ajeet_crop_and_verity = selectedOrderLine.get(i).ajeet_crop_and_varity;
            lines.ajeet_crop_age = selectedOrderLine.get(i).ajeet_crop_age;
            lines.ajeet_fruits_per = selectedOrderLine.get(i).ajeet_fruits_per;
            lines.ajeet_pest = selectedOrderLine.get(i).ajeet_pest;
            lines.ajeet_disease = selectedOrderLine.get(i).ajeet_disease;

            lines.check_crop_and_variety = selectedOrderLine.get(i).check_crop_and_variety;
            lines.check_crop_age = selectedOrderLine.get(i).check_crop_age;
            lines.check_fruits_per = selectedOrderLine.get(i).check_fruits_per;
            lines.check_pest = selectedOrderLine.get(i).check_pest;
            lines.check_disease = selectedOrderLine.get(i).check_disease;
            passData.addlines.add(lines);
        }
        loadFragments(R.id.nav_daily_activity, "Daily Activity", new Gson().toJson(passData));
    }
    private void loadFragments(int id, String fragmentName, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("dataPass", data);
        bundle.putBoolean("hideAllActions", true);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack(id, true);
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }
    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());

        return drawableResourceId;
    }
}
