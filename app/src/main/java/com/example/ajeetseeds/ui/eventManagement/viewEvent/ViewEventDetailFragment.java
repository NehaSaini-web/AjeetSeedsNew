package com.example.ajeetseeds.ui.eventManagement.viewEvent;

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
import com.example.ajeetseeds.Model.event.SyncEventDetailModel;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.event.EventManagementExpenseLineTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.ui.order_creation.order_approval.OrderApprovalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewEventDetailFragment extends Fragment {

    private ViewEventDetailViewModel mViewModel;

    public static ViewEventDetailFragment newInstance() {
        return new ViewEventDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_event_detail_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewEventDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView event_List;
    EventListAdapter event_Adapter;
    List<EventManagementTable.EventManagemantModel> eventListData = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        event_List = view.findViewById(R.id.event_List);
        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
        if (networkUtil) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.getEventDeatil + "getEventSync&email=" + sessionManagement.getUserEmail(), null, "UpdateOrderStatus"));
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
            List<SyncEventDetailModel> responseslist = new Gson().fromJson(result, new TypeToken<List<SyncEventDetailModel>>() {
            }.getType());
            if (responseslist.size() > 0 && responseslist.get(0).condition) {
                EventManagementTable eventManagementTable = new EventManagementTable(getActivity());
                eventManagementTable.open();
                for (SyncEventDetailModel responseobject : responseslist) {
                    eventManagementTable.update_EventStatus(responseobject.event_code, responseobject.status, responseobject.reject_reason, responseobject.approve_on);
                    for (SyncEventDetailModel.ExpanceLineModel lineObject : responseobject.expense_line) {
                        if (lineObject.line_no != null && lineObject.event_code != null) {
                            EventManagementExpenseLineTable eventManagementExpenseLineTable = new EventManagementExpenseLineTable(getActivity());
                            eventManagementExpenseLineTable.open();
                            eventManagementExpenseLineTable.insert(eventManagementExpenseLineTable.new EventManagementExpenseLineModel(lineObject.event_code,
                                    lineObject.line_no, lineObject.expense_type, lineObject.quantity, lineObject.rate_unit_cost, lineObject.amount,
                                    lineObject.created_on, "1"));
                            eventManagementExpenseLineTable.close();
                        }
                    }
                }
                eventManagementTable.close();
            }
        } catch (Exception e) {
        } finally {
            bindUiListData();
            loadingDialog.dismissLoading();
        }
    }

    private void bindUiListData() {
        try {
            EventManagementTable eventManagementTable = new EventManagementTable(getActivity());
            eventManagementTable.open();
            eventListData = eventManagementTable.fetch();
            eventManagementTable.close();
        } catch (Exception e) {
        }
        event_Adapter = new EventListAdapter(getActivity(), eventListData);
        event_List.setAdapter(event_Adapter);
        event_List.setOnItemClickListener((adapterView, view1, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("flag", true);
            bundle.putString("passdata", new Gson().toJson(eventListData.get(i)));
            loadFragments(R.id.nav_event_create, "Create Event", bundle);
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
        getActivity().setTitle("Event List");
    }
}
