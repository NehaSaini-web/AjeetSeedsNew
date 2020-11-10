package com.example.ajeetseeds.ui.travel.viewtravel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.event.SyncEventDetailModel;
import com.example.ajeetseeds.Model.travel.SyncTravelDetailModel;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.event.EventManagementExpenseLineTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.travel.TravelHeaderTable;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseModel;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewTravelDetailFragment extends Fragment {

    private ViewTravelDetailViewModel mViewModel;

    public static ViewTravelDetailFragment newInstance() {
        return new ViewTravelDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_travel_detail_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewTravelDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView travel_List;
    TravelListAdapter travel_Adapter;
    List<TravelHeaderTable.TravelHeaderModel> travelListData = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        travel_List = view.findViewById(R.id.travel_List);
        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
        if (networkUtil) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.getTravelDeatil + "getTravelSync&email=" + sessionManagement.getUserEmail(), null, "UpdateOrderStatus"));
        } else {
            bindUiListData();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    LoadingDialog loadingDialog = new LoadingDialog();

    private class CommanHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;

        @Override
        protected void onPreExecute() {
            loadingDialog.showLoadingDialog(getActivity());
            super.onPreExecute();
        }

        @Override
        protected HttpHandlerModel doInBackground(AsyModel... asyModels) {
            try {
                URL postingUrl = hitObj.createUrl(asyModels[0].getPostingUrl());
                flagOfAction = asyModels[0].getFlagOfAction();
                if (asyModels[0].getPostingJson() == null)
                    return hitObj.getHttpRequest(postingUrl);
                else
                    return hitObj.postHttpRequest(postingUrl, asyModels[0].getPostingJson());
            } catch (Exception e) {
                return hitObj.setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(HttpHandlerModel result) {
            super.onPostExecute(result);
            bindResponse(result.getJsonResponse(), flagOfAction);
        }
    }

    void bindResponse(String result, String flagOfAction) {
        try {
            List<SyncTravelDetailModel> reponse = new Gson().fromJson(result, new TypeToken<List<SyncTravelDetailModel>>() {
            }.getType());
            if (reponse.size() > 0 && reponse.get(0).condition) {
                TravelHeaderTable travelHeaderTable = new TravelHeaderTable(getActivity());
                travelHeaderTable.open();
                for (SyncTravelDetailModel responseobject : reponse) {
                    travelHeaderTable.update_travelStatus(responseobject.travelcode, responseobject.STATUS, responseobject.reason, responseobject.approve_budget, responseobject.approve_on);
                }
                travelHeaderTable.close();
            }
        } catch (Exception e) {
        } finally {
            bindUiListData();
            loadingDialog.dismissLoading();
        }
    }

    private void bindUiListData() {
        try {
            TravelHeaderTable travelHeaderTable = new TravelHeaderTable(getActivity());
            travelHeaderTable.open();
            travelListData = travelHeaderTable.fetch();
            travelHeaderTable.close();
        } catch (Exception e) {
        }
        travel_Adapter = new TravelListAdapter(getActivity(), travelListData);
        travel_List.setAdapter(travel_Adapter);
        travel_List.setOnItemClickListener((adapterView, view1, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("flag", true);
            bundle.putString("passdata", new Gson().toJson(travelListData.get(i)));
            loadFragments(R.id.nav_create_travel, "Add TA/DA Bill", bundle);
        });
    }

    private void loadFragments(int id, String fragmentName, Bundle bundle) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("View TA/DA List");
    }
}
