package com.example.ajeetseeds.ui.eventManagement.approveEvent;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.event.EventCreateResponseModel;
import com.example.ajeetseeds.Model.event.SyncEventDetailModel;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApproveEventDetailFragment extends Fragment {

    private ApproveEventDetailViewModel mViewModel;

    public static ApproveEventDetailFragment newInstance() {
        return new ApproveEventDetailFragment();
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
        mViewModel = ViewModelProviders.of(this).get(ApproveEventDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView event_List;
    EventApproveListAdapter event_Adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        event_List = view.findViewById(R.id.event_List);
        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.getEventDeatil + "getEventApproveExpense&email="+sessionManagement.getUserEmail(), null, "UpdateOrderStatus"));
        super.onViewCreated(view, savedInstanceState);
    }

    LoadingDialog loadingDialog = new LoadingDialog();

    private class CommanHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;
        private int position = -1;

        CommanHitToServer() {
            this.position = -1;
        }

        CommanHitToServer(int position) {
            this.position = position;
        }

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
            bindResponse(result.getJsonResponse(), flagOfAction, this.position);
        }
    }

    List<SyncEventDetailModel> list = new ArrayList<>();

    void bindResponse(String result, String flagOfAction, int position) {
        try {
            if (flagOfAction.equalsIgnoreCase("UpdateOrderStatus")) {
                List<SyncEventDetailModel> responseslist = new Gson().fromJson(result, new TypeToken<List<SyncEventDetailModel>>() {
                }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition) {
                    list = responseslist;
                    bindUiListData();
                }else{
                    Snackbar.make(event_List, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                    }).show();
                }
            } else if (flagOfAction.equalsIgnoreCase("approveRejectHit")) {
                List<EventCreateResponseModel> responseslist = new Gson().fromJson(result, new TypeToken<List<EventCreateResponseModel>>() {
                }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition) {
                    list.remove(position);
                    event_Adapter.notifyDataSetChanged();
                }else {
                    Snackbar.make(event_List, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                    }).show();
                }
            }
        } catch (Exception e) {
        } finally {
            bindUiListData();
            loadingDialog.dismissLoading();
        }
    }

    private void bindUiListData() {
        event_Adapter = new EventApproveListAdapter(getActivity(), list, (selectedeventData, position, flag) -> {
            approveRejectOrder(selectedeventData, position, flag);
        });
        event_List.setAdapter(event_Adapter);
    }

    void approveRejectOrder(SyncEventDetailModel selectedeventData, int position, String flag) {
        if (flag.equalsIgnoreCase("approve")) {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Confirm " + selectedeventData.event_code)
                    .setMessage("Do you really want to approve?")
                    .setIcon(R.drawable.approve_order_icon)
                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
                        try {
                            JSONObject postedjson = new JSONObject();
                            postedjson.put("email", sessionManagement.getUserEmail());
                            postedjson.put("event_code", selectedeventData.event_code);
                            postedjson.put("event_status", selectedeventData.status.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                            postedjson.put("reson", selectedeventData.status.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                            new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.updateEventStatusDeatil, postedjson, "approveRejectHit"));
                        } catch (Exception e) {
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {

                    })
                    .show();
        } else if (flag.equalsIgnoreCase("reject")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("Confirm " + selectedeventData.event_code);
            builder.setIcon(R.drawable.approve_order_icon);
            LinearLayout parentVertical = new LinearLayout(getActivity());
            LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            parentVertical.setLayoutParams(parentVerticalParams);
            parentVertical.setOrientation(LinearLayout.VERTICAL);
            TextView messageSession = new TextView(getActivity());
            messageSession.setText("Do you really want to reject?");
            messageSession.setTextColor(Color.BLACK);
            TextView enterReson_tv = new TextView(getActivity());
            enterReson_tv.setText("Enter Reason");
            enterReson_tv.setTextColor(getResources().getColor(R.color.colorAccent));
            enterReson_tv.setPadding(0, 10, 0, 0);
            EditText reason = new EditText(getActivity());
            parentVertical.addView(messageSession);
            parentVertical.addView(enterReson_tv);
            parentVertical.addView(reason);
            parentVertical.setPadding(40, 20, 40, 10);
            builder.setView(parentVertical);
            builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                try {
                    JSONObject postedjson = new JSONObject();
                    postedjson.put("email", sessionManagement.getUserEmail());
                    postedjson.put("event_code", selectedeventData.event_code);
                    postedjson.put("event_status", selectedeventData.status.equalsIgnoreCase("PENDING") ? "CREATE REJECTED" : "REJECTED");
                    postedjson.put("reson", reason.getText().toString());
                    new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.updateEventStatusDeatil, postedjson, "approveRejectHit"));
                } catch (Exception e) {
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

    }


}
