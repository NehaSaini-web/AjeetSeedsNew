package com.example.ajeetseeds.ui.travel.approveTravel;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.ajeetseeds.Model.travel.SyncTravelDetailModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApproveTravelDetailFragment extends Fragment {

    private ApproveTravelDetailViewModel mViewModel;

    public static ApproveTravelDetailFragment newInstance() {
        return new ApproveTravelDetailFragment();
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
        mViewModel = ViewModelProviders.of(this).get(ApproveTravelDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView travel_List;
    TravelListAdapter event_Adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        travel_List = view.findViewById(R.id.travel_List);
        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(
                StaticDataForApp.getTravelDeatil + "getTravelApproveExpense&email="+sessionManagement.getUserEmail(), null, "UpdateOrderStatus"));
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
            bindResponse(result, flagOfAction, this.position);
        }
    }

    List<SyncTravelDetailModel> list = new ArrayList<>();

    void bindResponse(HttpHandlerModel result, String flagOfAction, int position) {
        try {
            if (result.isConnectStatus() && !result.getJsonResponse().equalsIgnoreCase("")) {
                if (flagOfAction.equalsIgnoreCase("UpdateOrderStatus")) {
                    List<SyncTravelDetailModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<SyncTravelDetailModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        list = responseslist;
                        bindUiListData();
                    } else {
                        Snackbar.make(travel_List, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                } else if (flagOfAction.equalsIgnoreCase("approveRejectHit")) {
                    List<EventCreateResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<EventCreateResponseModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        list.remove(position);
                        event_Adapter.notifyDataSetChanged();
                    } else {
                        Snackbar.make(travel_List, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
            } else {
                Snackbar.make(travel_List, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
        } finally {
            bindUiListData();
            loadingDialog.dismissLoading();
        }
    }

    private void bindUiListData() {
        event_Adapter = new TravelListAdapter(getActivity(), list, (selectedeventData, position, flag) -> {
            approveRejectTravel(selectedeventData, position, flag);
        });
        travel_List.setAdapter(event_Adapter);
    }

    void approveRejectTravel(SyncTravelDetailModel selectedeventData, int position, String flag) {
        if (flag.equalsIgnoreCase("approve")) {
            if (selectedeventData.STATUS.equalsIgnoreCase("PENDING")) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("Approve Confirm " + selectedeventData.travelcode);
                builder.setIcon(R.drawable.approve_order_icon);
                LinearLayout parentVertical = new LinearLayout(getActivity());
                LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                parentVertical.setLayoutParams(parentVerticalParams);
                parentVertical.setOrientation(LinearLayout.VERTICAL);
                TextView enterReson_tv = new TextView(getActivity());
                enterReson_tv.setText("Enter Approve Budget");
                enterReson_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                enterReson_tv.setPadding(10, 30, 0, 0);
                EditText approvebudget = new EditText(getActivity());
                approvebudget.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers
                parentVertical.addView(enterReson_tv);
                parentVertical.addView(approvebudget);

                TextView enterAdvance_tv = new TextView(getActivity());
                enterAdvance_tv.setText("Enter Advance Amount");
                enterAdvance_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                enterAdvance_tv.setPadding(10, 10, 0, 0);
                EditText Advancebudget = new EditText(getActivity());
                Advancebudget.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers

                parentVertical.addView(enterAdvance_tv);
                parentVertical.addView(Advancebudget);

                parentVertical.setPadding(40, 20, 40, 10);
                builder.setView(parentVertical);

                builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                    try {
                        float budget_needed=Float.parseFloat(selectedeventData.expense_budget);
                        float approvebudget_by_approver=approvebudget.getText().toString().equalsIgnoreCase("")?0:Float.parseFloat(approvebudget.getText().toString());
                        float Advancebudget_by_approver=Advancebudget.getText().toString().equalsIgnoreCase("")?0:Float.parseFloat(Advancebudget.getText().toString());

                        if(approvebudget_by_approver>budget_needed)
                        {
                            Snackbar.make(travel_List,"Please Enter Approve Budget < "+selectedeventData.expense_budget,Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if(Advancebudget_by_approver>approvebudget_by_approver)
                        {
                            Snackbar.make(travel_List,"Please Enter Advance Budget < "+approvebudget.getText().toString(),Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if(!loadingDialog.getLoadingState()) {
                            JSONObject postedjson = new JSONObject();
                            postedjson.put("email", sessionManagement.getUserEmail());
                            postedjson.put("travel_code", selectedeventData.travelcode);
                            postedjson.put("approve_budget", approvebudget.getText().toString());
                            postedjson.put("advance_budget", Advancebudget.getText().toString());
                            postedjson.put("travel_status", selectedeventData.STATUS.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                            postedjson.put("reson", selectedeventData.STATUS.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                            new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.approveRejectTravel, postedjson, "approveRejectHit"));
                        }
                    } catch (Exception e) {
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            } else {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Confirm " + selectedeventData.travelcode)
                        .setMessage("Do you really want to approve?")
                        .setIcon(R.drawable.approve_order_icon)
                        .setPositiveButton("Confirm", (dialogInterface, i) -> {
                            try {
                                JSONObject postedjson = new JSONObject();
                                postedjson.put("email", sessionManagement.getUserEmail());
                                postedjson.put("travel_code", selectedeventData.travelcode);
                                postedjson.put("approve_budget", "0");
                                postedjson.put("advance_budget","0");
                                postedjson.put("travel_status", selectedeventData.STATUS.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                                postedjson.put("reson", selectedeventData.STATUS.equalsIgnoreCase("PENDING") ? "CREATE APPROVED" : "APPROVED");
                                new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.approveRejectTravel, postedjson, "approveRejectHit"));
                            } catch (Exception e) {
                            }
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {

                        })
                        .show();
            }
        } else if (flag.equalsIgnoreCase("reject")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("Confirm " + selectedeventData.travelcode);
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
                    postedjson.put("travel_code", selectedeventData.travelcode);
                    postedjson.put("approve_budget", selectedeventData.approve_budget);
                    postedjson.put("travel_status", selectedeventData.STATUS.equalsIgnoreCase("PENDING") ? "CREATE REJECTED" : "REJECTED");
                    postedjson.put("reson", reason.getText().toString());
                    new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.approveRejectTravel, postedjson, "approveRejectHit"));
                } catch (Exception e) {
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("TA/DA Approve");
    }
}
