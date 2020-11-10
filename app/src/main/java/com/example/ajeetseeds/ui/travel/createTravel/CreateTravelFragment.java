package com.example.ajeetseeds.ui.travel.createTravel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.event.EventCreateResponseModel;
import com.example.ajeetseeds.Model.travel.TravelHeaderResponse;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.CustomDatePicker;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.TravelHeaderTable;
import com.example.ajeetseeds.ui.dailyActivity.addLine.DistrictAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTravelFragment extends Fragment {

    private CreateTravelViewModel mViewModel;
    TextInputEditText et_start_date, et_end_date;
    Button submitPage, add_eventExpense;
    AutoCompleteTextView from_city, to_city;
    TextInputEditText et_reason, et_expense_budget;

    public static CreateTravelFragment newInstance() {
        return new CreateTravelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_travel_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateTravelViewModel.class);
        // TODO: Use the ViewModel
    }

    LoadingDialog loadingDialog = new LoadingDialog();
    SessionManagement sessionManagement;
    boolean datedialog = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        from_city = view.findViewById(R.id.from_city);
        to_city = view.findViewById(R.id.to_city);
        et_start_date = view.findViewById(R.id.et_start_date);
        et_end_date = view.findViewById(R.id.et_end_date);
        et_reason = view.findViewById(R.id.et_reason);
        et_expense_budget = view.findViewById(R.id.et_expense_budget);
        submitPage = view.findViewById(R.id.submitPage);
        add_eventExpense = view.findViewById(R.id.add_eventExpense);
        et_start_date.setOnTouchListener((view1, motionEvent) -> {
            new CustomDatePicker(getActivity()).showDatePickerDialog(et_start_date);
            return true;
        });

        et_end_date.setOnTouchListener((view1, motionEvent) -> {
            new CustomDatePicker(getActivity()).showDatePickerDialog(et_end_date);
            return true;
        });
        checkBackFragmentData();
        bindEventDropDown();
        submitPage.setOnClickListener(view1 -> {
            try {
                if (!loadingDialog.getLoadingState()) {
                    boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
                    if (networkUtil) {
                        JSONObject postedJson = new JSONObject();
                        postedJson.put("email", sessionManagement.getUserEmail());
                        postedJson.put("from_loc", selectedFromDistrict.code);
                        postedJson.put("to_loc", selectedToDistrict.code);
                        postedJson.put("start_date", et_start_date.getText().toString());
                        postedJson.put("end_date", et_end_date.getText().toString());
                        postedJson.put("travel_reson", et_reason.getText().toString());
                        postedJson.put("expense_budget", et_expense_budget.getText().toString());
                        postedJson.put("approver_id", sessionManagement.getApprover_id());
                        postedJson.put("user_type", sessionManagement.getUser_type());

                        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                new AsyModel(StaticDataForApp.insertTravelHeader, postedJson, "insertTravelHeader"));
                    } else {
                        bindLOcalDataBase("0", DateUtilsCustome.formatDateTimeFromDate(DateUtilsCustome.DATE_FORMAT_18, Calendar.getInstance().getTime()));
                    }
                }
            } catch (Exception e) {

            }
        });
    }

    private boolean passBackFragmentData = false;
    TravelHeaderTable.TravelHeaderModel submitTravelData = null;

    void checkBackFragmentData() {
        try {
            passBackFragmentData = getArguments().getBoolean("flag", false);
            submitTravelData = new Gson().fromJson(getArguments().getString("passdata", ""),
                    TravelHeaderTable.TravelHeaderModel.class);

        } catch (Exception e) {

        } finally {
            if (passBackFragmentData) {
                from_city.setEnabled(false);
                to_city.setEnabled(false);
                et_start_date.setEnabled(false);
                et_end_date.setEnabled(false);
                et_reason.setEnabled(false);
                et_expense_budget.setEnabled(false);

                from_city.setText(submitTravelData.from_loc_name);
                to_city.setText(submitTravelData.to_loc_name);
                et_start_date.setText(submitTravelData.start_date);
                et_end_date.setText(submitTravelData.end_date);
                et_reason.setText(submitTravelData.travel_reson);
                et_expense_budget.setText(submitTravelData.expense_budget);
                from_city.setText(submitTravelData.from_loc_name);
                add_eventExpense.setVisibility(View.VISIBLE);
                submitPage.setVisibility(View.GONE);
                add_eventExpense.setOnClickListener(view -> {
                    if (submitTravelData.status.equalsIgnoreCase("CREATE REJECTED")) {
                        Snackbar.make(add_eventExpense, "You don't have Expense line So Go Back.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("flag", true);
                        bundle.putString("dataPass", new Gson().toJson(submitTravelData));
                        loadFragments(R.id.nav_travel_expence_list_add, "Add Expance " + submitTravelData.travelcode, bundle);
                    }
                });
                getActivity().setTitle("Update TA/DA Bill " + submitTravelData.travelcode);
                if (submitTravelData.status.equalsIgnoreCase("PENDING")) {
                    add_eventExpense.setVisibility(View.GONE);
                }
            } else {

            }
        }
    }

    private void loadFragments(int id, String fragmentName, Bundle bundle) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

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
                return hitObj.postHttpRequest(postingUrl, asyModels[0].getPostingJson());
            } catch (Exception e) {
                return hitObj.setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(HttpHandlerModel result) {
            super.onPostExecute(result);
            bindResponse(result, flagOfAction);
        }
    }

    void bindResponse(HttpHandlerModel result, String flagOfAction) {
        try {
            if (result.isConnectStatus() && !result.getJsonResponse().contentEquals("")) {
                if (flagOfAction.equalsIgnoreCase("insertTravelHeader")) {
                    List<TravelHeaderResponse> responseslist = new Gson().fromJson(result.getJsonResponse(),
                            new TypeToken<List<TravelHeaderResponse>>() {
                            }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        bindLOcalDataBase(responseslist.get(0).travelcode, responseslist.get(0).created_on);
                    } else {
                        Snackbar.make(submitPage, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
            } else {
                Snackbar.make(submitPage, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), "Travel Cteate", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bindLOcalDataBase(String travelcode, String created_on) {
        try {
            TravelHeaderTable travelHeaderTable = new TravelHeaderTable(getActivity());
            travelHeaderTable.open();
            String android_travel_code = travelHeaderTable.getTableSequenceNo();
            TravelHeaderTable.TravelHeaderModel insertObject = travelHeaderTable.new TravelHeaderModel(android_travel_code,
                    travelcode, selectedFromDistrict.code, selectedToDistrict.code, et_start_date.getText().toString(),
                    et_end_date.getText().toString(), et_reason.getText().toString(), et_expense_budget.getText().toString(),
                    "0", created_on, sessionManagement.getUser_type(),sessionManagement.getUserEmail(), "PENDING", sessionManagement.getApprover_id(),
                    null, null,"0");
            travelHeaderTable.insert(insertObject);
            travelHeaderTable.close();
            //todo master entry Table out redy is going to be 1 so that data uploaded to the server
            if (travelcode.contentEquals("0")) {
                SyncDataTable syncDataTable = new SyncDataTable(getActivity());
                syncDataTable.open();
                syncDataTable.OutActivate(AllTablesName.TravelHeaderTable, 1);
                syncDataTable.close();
            }
            SuccessMessage(travelcode.equalsIgnoreCase("0") ? "TA/DA Bill Create Wait For Online." : travelcode);
            loadFragments(R.id.nav_create_travel, "Add TA/DA Bill");
        } catch (Exception e) {
        }
    }

    public void SuccessMessage(String event_code) {

        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View successmessaePopupView = inflater.inflate(R.layout.success_message_popup, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            TextView order_no = (TextView) successmessaePopupView.findViewById(R.id.order_no);
            order_no.setText(event_code);

            ImageView succesessImg = (ImageView) successmessaePopupView.findViewById(R.id.succesessImg);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.success_animation);
            succesessImg.startAnimation(animation);

            dialog.setContentView(successmessaePopupView);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button goBackFromItem = successmessaePopupView.findViewById(R.id.goBackFromItem);
            goBackFromItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            //You need to add the following line for this solution to work; thanks skayred
            successmessaePopupView.setFocusableInTouchMode(true);
            successmessaePopupView.requestFocus();
            successmessaePopupView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
        }
    }

    private void loadFragments(int id, String fragmentName) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(id);
        getActivity().setTitle(fragmentName);
    }

    List<DistrictMasterTable.DistrictMaster> fromDistrictList = new ArrayList<>();
    List<DistrictMasterTable.DistrictMaster> toDistrictList = new ArrayList<>();
    DistrictMasterTable.DistrictMaster selectedFromDistrict = null;
    DistrictMasterTable.DistrictMaster selectedToDistrict = null;

    void bindEventDropDown() {
        DistrictMasterTable districtMasterTable = new DistrictMasterTable(getActivity());
        districtMasterTable.open();
        fromDistrictList = districtMasterTable.fetch();
        toDistrictList = districtMasterTable.fetch();
        districtMasterTable.close();
        DistrictAdapter from_cityAdapter = new DistrictAdapter(getActivity(), R.layout.drop_down_textview, fromDistrictList);
        from_city.setAdapter(from_cityAdapter);
        DistrictAdapter to_cityAdapter = new DistrictAdapter(getActivity(), R.layout.drop_down_textview, toDistrictList);
        to_city.setAdapter(to_cityAdapter);
        from_city.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedFromDistrict = fromDistrictList.get(i);
        });
        to_city.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedToDistrict = toDistrictList.get(i);
        });

    }
}
