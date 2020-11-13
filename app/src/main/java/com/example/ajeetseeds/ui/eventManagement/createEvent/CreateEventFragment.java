package com.example.ajeetseeds.ui.eventManagement.createEvent;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.CustomDatePicker;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.Model.event.EventCreateResponseModel;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.event.EventTypeMaster;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateEventFragment extends Fragment {

    private CreateEventViewModel mViewModel;
    AutoCompleteTextView dropdown_event_type, dropdown_crop, dropdown_variety, dropdown_state, dropdown_district, dropdown_taluka;
    ChipGroup selected_no_odvillageCovered_chipgroup;
    ImageView add_village_name_button;
    AppCompatEditText et_event_desc, et_village, et_farmer_name, et_farmer_contact_no, et_expected_farmers, et_expected_dealers,
            et_expected_distributers, et_village_covered_name;
    Button submitPage, add_eventExpense;
    TextInputEditText et_event_date, et_Actual_farmers, et_Actual_dealers, et_Actual_distributers;
    LinearLayout create_approval_section;

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_event_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateEventViewModel.class);
        // TODO: Use the ViewModel
    }

    LoadingDialog loadingDialog = new LoadingDialog();
    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        dropdown_event_type = view.findViewById(R.id.dropdown_event_type);
        dropdown_crop = view.findViewById(R.id.dropdown_crop);
        dropdown_variety = view.findViewById(R.id.dropdown_variety);
        dropdown_state = view.findViewById(R.id.dropdown_state);
        dropdown_district = view.findViewById(R.id.dropdown_district);
        dropdown_taluka = view.findViewById(R.id.dropdown_taluka);
        et_village_covered_name = view.findViewById(R.id.et_village_covered_name);
        selected_no_odvillageCovered_chipgroup = view.findViewById(R.id.selected_no_odvillageCovered_chipgroup);
        add_village_name_button = view.findViewById(R.id.add_village_name_button);

        et_event_desc = view.findViewById(R.id.et_event_desc);
        et_event_date = view.findViewById(R.id.et_event_date);
        et_event_date.setText(DateUtilsCustome.getCurrentDateBYMM_DD_YYYY());

        et_village = view.findViewById(R.id.et_village);
        et_farmer_name = view.findViewById(R.id.et_farmer_name);
        et_farmer_contact_no = view.findViewById(R.id.et_farmer_contact_no);
        et_expected_farmers = view.findViewById(R.id.et_expected_farmers);
        et_expected_dealers = view.findViewById(R.id.et_expected_dealers);
        et_expected_distributers = view.findViewById(R.id.et_expected_distributers);

        submitPage = view.findViewById(R.id.submitPage);
        add_eventExpense = view.findViewById(R.id.add_eventExpense);
        create_approval_section = view.findViewById(R.id.create_approval_section);
        et_Actual_farmers = view.findViewById(R.id.et_Actual_farmers);
        et_Actual_dealers = view.findViewById(R.id.et_Actual_dealers);
        et_Actual_distributers = view.findViewById(R.id.et_Actual_distributers);
        checkBackFragmentData();
    }

    private boolean passBackFragmentData = false;
    private boolean approve_view_display = false;
    EventManagementTable.EventManagemantModel submiteEventData = null;

    void checkBackFragmentData() {
        try {
            passBackFragmentData = getArguments().getBoolean("flag", false);
            submiteEventData = new Gson().fromJson(getArguments().getString("passdata", ""), EventManagementTable.EventManagemantModel.class);
            approve_view_display = getArguments().getBoolean("approveView", false);
        } catch (Exception e) {
        } finally {
            if (passBackFragmentData) {
                et_event_desc.setEnabled(false);
                et_event_date.setEnabled(false);
                dropdown_event_type.setEnabled(false);
                dropdown_crop.setEnabled(false);
                dropdown_variety.setEnabled(false);
                dropdown_state.setEnabled(false);
                dropdown_district.setEnabled(false);
                dropdown_taluka.setEnabled(false);
                et_village.setEnabled(false);
                et_farmer_name.setEnabled(false);
                et_farmer_contact_no.setEnabled(false);
                et_expected_farmers.setEnabled(false);
                et_expected_dealers.setEnabled(false);
                et_expected_distributers.setEnabled(false);
                et_village_covered_name.setEnabled(false);
                add_village_name_button.setVisibility(View.GONE);

                et_event_desc.setText(submiteEventData.event_desc);
                et_event_date.setText(DateUtilsCustome.getDateOnlyMM_DD_YYYY(submiteEventData.event_date));
                dropdown_event_type.setText(submiteEventData.event_type);
                dropdown_crop.setText(submiteEventData.crop_name);
                dropdown_variety.setText(submiteEventData.variety_name);
                dropdown_state.setText(submiteEventData.state_name);
                dropdown_district.setText(submiteEventData.district_name);
                dropdown_taluka.setText(submiteEventData.taluka_name);
                et_village.setText(submiteEventData.village);
                et_farmer_name.setText(submiteEventData.farmer_name);
                et_farmer_contact_no.setText(submiteEventData.farmer_mobile_no);
                et_expected_farmers.setText(submiteEventData.expected_farmers);
                et_expected_dealers.setText(submiteEventData.expected_dealers);
                et_expected_distributers.setText(submiteEventData.expected_distributer);
                String[] arrayVillage = submiteEventData.event_cover_villages.split(",");
                if (VillagesCoveredNameList != null && !VillagesCoveredNameList.isEmpty())
                    VillagesCoveredNameList.clear();
                for (int i = 0; i < arrayVillage.length; i++) {
                    VillagesCoveredNameList.add(arrayVillage[i]);
                }
                bindVillageCoverdNameListNo();
                add_eventExpense.setVisibility(View.VISIBLE);
                submitPage.setVisibility(View.GONE);
                add_eventExpense.setOnClickListener(view -> {
                    if (submiteEventData.status.equalsIgnoreCase("CREATE REJECTED")) {
                        Snackbar.make(add_eventExpense, "You don't have Expense line So Go Back.", Snackbar.LENGTH_LONG).show();
                    } else {
                        if (submiteEventData.status.equalsIgnoreCase("CREATE APPROVED")) {
                            if (et_Actual_farmers.getText().toString().equalsIgnoreCase("")) {
                                Snackbar.make(add_eventExpense, "Please Enter Actual Farmers.", Snackbar.LENGTH_LONG).show();
                                return;
                            } else if (et_Actual_distributers.getText().toString().equalsIgnoreCase("")) {
                                Snackbar.make(add_eventExpense, "Please Enter Actual Distributers.", Snackbar.LENGTH_LONG).show();
                                return;
                            } else if (et_Actual_dealers.getText().toString().equalsIgnoreCase("")) {
                                Snackbar.make(add_eventExpense, "Please Enter Actual Dealers.", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                EventManagementTable eventManagementTable = new EventManagementTable(getActivity());
                                eventManagementTable.open();
                                eventManagementTable.updateFarmaerDealerDistributers(submiteEventData.android_event_code, submiteEventData.event_code,
                                        et_Actual_farmers.getText().toString(), et_Actual_distributers.getText().toString(), et_Actual_dealers.getText().toString());
                                eventManagementTable.close();
                                submiteEventData.actual_farmers = et_Actual_farmers.getText().toString();
                                submiteEventData.actual_distributers = et_Actual_distributers.getText().toString();
                                submiteEventData.actual_dealers = et_Actual_dealers.getText().toString();

                            } catch (Exception e) {
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("dataPass", new Gson().toJson(submiteEventData));
                        loadFragments(R.id.nav_event_expanse_Add, "Add Expance " + submiteEventData.event_code, bundle);
                    }
                });
                if (submiteEventData.status.equalsIgnoreCase("PENDING")) {
                    add_eventExpense.setVisibility(View.GONE);
                    create_approval_section.setVisibility(View.GONE);
                } else if (submiteEventData.status.equalsIgnoreCase("CREATE REJECTED")) {
                    create_approval_section.setVisibility(View.GONE);
                } else if (submiteEventData.status.equalsIgnoreCase("CREATE APPROVED")) {
                    create_approval_section.setVisibility(View.VISIBLE);
                    et_Actual_dealers.setText(submiteEventData.actual_dealers);
                    et_Actual_distributers.setText(submiteEventData.actual_distributers);
                    et_Actual_farmers.setText(submiteEventData.actual_farmers);
                } else {
                    create_approval_section.setVisibility(View.VISIBLE);
                    et_Actual_dealers.setEnabled(false);
                    et_Actual_distributers.setEnabled(false);
                    et_Actual_farmers.setEnabled(false);
                    et_Actual_dealers.setText(submiteEventData.actual_dealers);
                    et_Actual_distributers.setText(submiteEventData.actual_distributers);
                    et_Actual_farmers.setText(submiteEventData.actual_farmers);
                    //todo disable all input field of this section;
                }
                if (approve_view_display) {
                    getActivity().setTitle("View Event " + submiteEventData.event_code);
                    if (submiteEventData.status.equalsIgnoreCase("PENDING")) {
                        add_eventExpense.setVisibility(View.GONE);
                    }
                    else if (submiteEventData.status.equalsIgnoreCase("CREATE APPROVED")) {
                        add_eventExpense.setVisibility(View.GONE);
                    }else{
                        add_eventExpense.setVisibility(View.VISIBLE);
                    }
                    submitPage.setVisibility(View.GONE);
                } else
                    getActivity().setTitle("Update Event " + submiteEventData.event_code);

            } else {
                et_event_date.setOnTouchListener((view, motionEvent) -> {
                    new CustomDatePicker(getActivity()).showDatePickerDialog(et_event_date);
                    return true;
                });
                add_village_name_button.setVisibility(View.VISIBLE);
                bindEventType();
                bindCrop();
                bindState();
                bindControllEvent();
            }
        }
    }

    private void loadFragments(int id, String fragmentName, Bundle bundle) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

    EventTypeMaster.EventTypeMasterModel selectedEventType;
    CropMasterTable.CropMasterModel selectedCrop;
    CropItemMasterTable.CropItemMasterModel selectedCropItem;
    StateMasterTable.StateMaster selectedState;
    DistrictMasterTable.DistrictMaster selecteddistrict;
    TalukaMasterTable.TalukaMaster selectedTaluka;

    void bindControllEvent() {
        dropdown_event_type.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectedEventType = eventTypeList.get(i);
            dropdown_event_type.setError(null);
        });
        dropdown_crop.setOnItemClickListener((adapterView, view1, i, l) -> {
            cropItemList.clear();
            selectedCropItem = null;
            bindCropItem(cropList.get(i).code);
            selectedCrop = cropList.get(i);
            dropdown_crop.setError(null);
        });
        dropdown_variety.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectedCropItem = cropItemList.get(i);
            dropdown_variety.setError(null);
        });
        dropdown_state.setOnItemClickListener((adapterView, view1, i, l) -> {
            districtList.clear();
            bindDistrict(statelist.get(i).code);
            selectedState = statelist.get(i);
            dropdown_state.setError(null);
        });

        dropdown_district.setOnItemClickListener((adapterView, view1, i, l) -> {
            talukaList.clear();
            bindTaluka(districtList.get(i).code);
            selecteddistrict = districtList.get(i);
            dropdown_district.setError(null);
        });
        dropdown_taluka.setOnItemClickListener((adapterView, view1, i, l) -> {
            selectedTaluka = talukaList.get(i);
            dropdown_taluka.setError(null);
        });
        add_village_name_button.setOnClickListener(view1 -> {
            VillagesCoveredNameList.add(et_village_covered_name.getText().toString());
            bindVillageCoverdNameListNo();
            et_village_covered_name.setText("");
            et_village_covered_name.setError(null);
        });
        submitPage.setOnClickListener(view -> {
            try {
                if (et_event_desc.getText().toString().equalsIgnoreCase("")) {
                    et_event_desc.setError("Please Enter Event Discription.");
                    return;
                } else if (et_event_date.getText().toString().equalsIgnoreCase("")) {
                    et_event_date.setError("Please Enter Event Date.");
                    return;
                } else if (selectedEventType == null || !selectedEventType.event_type.equalsIgnoreCase(dropdown_event_type.getText().toString())) {
                    dropdown_event_type.setError("Please Select Event Type.");
                    return;
                } else if (selectedCrop == null || !selectedCrop.description.equalsIgnoreCase(dropdown_crop.getText().toString())) {
                    dropdown_crop.setError("Please Select Crop.");
                    return;
                } else if (selectedCropItem == null || !dropdown_variety.getText().toString().contains(selectedCropItem.item_desc)) {
                    dropdown_variety.setError("Please Select Variety.");
                    return;
                } else if (selectedState == null || !selectedState.name.equalsIgnoreCase(dropdown_state.getText().toString())) {
                    dropdown_state.setError("Please Select State.");
                    return;
                } else if (selecteddistrict == null || !selecteddistrict.name.equalsIgnoreCase(dropdown_district.getText().toString())) {
                    dropdown_district.setError("Please Select District.");
                    return;
                } else if (selectedTaluka == null || !selectedTaluka.description.equalsIgnoreCase(dropdown_taluka.getText().toString())) {
                    dropdown_taluka.setError("Please Select Taluka.");
                    return;
                } else if (et_village.getText().toString().equalsIgnoreCase("")) {
                    et_village.setError("Please Enter Village Name.");
                    return;
                } else if (et_farmer_name.getText().toString().equalsIgnoreCase("")) {
                    et_farmer_name.setError("Please Enter Farmer Name.");
                    return;
                } else if (et_farmer_contact_no.getText().toString().equalsIgnoreCase("")) {
                    et_farmer_contact_no.setError("Please Enter Contact no.");
                    return;
                } else if (et_expected_farmers.getText().toString().equalsIgnoreCase("")) {
                    et_expected_farmers.setError("Please Enter Expected Farmers.");
                    return;
                } else if (et_expected_dealers.getText().toString().equalsIgnoreCase("")) {
                    et_expected_dealers.setError("Please Enter Expected Dealers.");
                    return;
                } else if (et_expected_distributers.getText().toString().equalsIgnoreCase("")) {
                    et_expected_distributers.setError("Please Enter Expected Distributers.");
                    return;
                } else if (VillagesCoveredNameList == null || VillagesCoveredNameList.size() == 0) {
                    et_village_covered_name.setError("Please Enter Village Covered Name.");
                    return;
                }
                if (!loadingDialog.getLoadingState()) {
                    event_cover_villages = "";
                    for (int i = 0; i < VillagesCoveredNameList.size(); i++) {
                        event_cover_villages += VillagesCoveredNameList.get(i) + (i == VillagesCoveredNameList.size() - 1 ? "" : ",");
                    }
                    boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
                    if (networkUtil) {
                        JSONObject postedJson = new JSONObject();
                        postedJson.put("email", sessionManagement.getUserEmail());
                        postedJson.put("event_desc", et_event_desc.getText().toString());
                        postedJson.put("event_date", et_event_date.getText().toString());
                        postedJson.put("event_type", selectedEventType.event_type);
                        postedJson.put("event_budget", selectedEventType.rate);
                        postedJson.put("crop", selectedCrop.code);
                        postedJson.put("variety", selectedCropItem.item_no);
                        postedJson.put("state", selectedState.code);
                        postedJson.put("district", selecteddistrict.code);
                        postedJson.put("village", et_village.getText().toString());
                        postedJson.put("taluka", selectedTaluka.code);
                        postedJson.put("farmer_name", et_farmer_name.getText().toString());
                        postedJson.put("farmer_mobile_no", et_farmer_contact_no.getText().toString());
                        postedJson.put("expected_farmers", et_expected_farmers.getText().toString());
                        postedJson.put("expected_dealers", et_expected_dealers.getText().toString());
                        postedJson.put("expected_distributer", et_expected_distributers.getText().toString());
                        postedJson.put("event_cover_villages", event_cover_villages);
                        postedJson.put("approver_email", sessionManagement.getApprover_id());

                        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                new AsyModel(StaticDataForApp.insertEventManagement, postedJson, "EventCreateSubmit"));
                    } else {
                        bindLOcalDataBase("0", null);
                    }
                }
            } catch (Exception e) {

            }
        });
    }

    String event_cover_villages = "";

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
                if (flagOfAction.equalsIgnoreCase("EventCreateSubmit")) {
                    List<EventCreateResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<EventCreateResponseModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        bindLOcalDataBase(responseslist.get(0).event_code, responseslist.get(0).created_on);
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
            new AndroidExceptionHandel(e.getMessage(), "Event Cteate", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bindLOcalDataBase(String event_code, String created_on) {
        try {
            EventManagementTable eventManagementTable = new EventManagementTable(getActivity());
            eventManagementTable.open();
            String android_event_code = eventManagementTable.getTableSequenceNo();
            EventManagementTable.EventManagemantModel insertObject = eventManagementTable.new EventManagemantModel(android_event_code,
                    event_code, et_event_desc.getText().toString(), et_event_date.getText().toString(), selectedEventType.event_type, selectedEventType.rate,
                    selectedCrop.code, selectedCropItem.item_no, selectedState.code, selecteddistrict.code, et_village.getText().toString(),
                    selectedTaluka.code, et_farmer_name.getText().toString(), et_farmer_contact_no.getText().toString(), et_expected_farmers.getText().toString()
                    , et_expected_dealers.getText().toString(), et_expected_distributers.getText().toString(), event_cover_villages, created_on,
                    sessionManagement.getUserEmail(), sessionManagement.getApprover_id(), "PENDING", null, null,
                    "", "", "");
            eventManagementTable.insert(insertObject);
            eventManagementTable.close();
            //todo master entry Table out redy is going to be 1 so that data uploaded to the server
            if (event_code.contentEquals("0")) {
                SyncDataTable syncDataTable = new SyncDataTable(getActivity());
                syncDataTable.open();
                syncDataTable.OutActivate(AllTablesName.EventManagementTable, 1);
                syncDataTable.close();
            }
            SuccessMessage(event_code.equalsIgnoreCase("0") ? "Event Create Wait For Online." : event_code);
            loadFragments(R.id.nav_event_create, "Create Event");
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

    List<EventTypeMaster.EventTypeMasterModel> eventTypeList = new ArrayList<>();

    void bindEventType() {
        EventTypeMaster eventTypeMaster = new EventTypeMaster(getActivity());
        eventTypeMaster.open();
        eventTypeList = eventTypeMaster.fetchParent();
        eventTypeMaster.close();
        EventTypeAdapter fruitAdapter = new EventTypeAdapter(getActivity(), R.layout.drop_down_textview, eventTypeList);
        dropdown_event_type.setAdapter(fruitAdapter);
    }

    List<CropMasterTable.CropMasterModel> cropList = new ArrayList<>();

    void bindCrop() {
        CropMasterTable cropMasterTable = new CropMasterTable(getActivity());
        cropMasterTable.open();
        cropList = cropMasterTable.fetch();
        cropMasterTable.close();
        CropAdapter cropAdapter = new CropAdapter(getActivity(), R.layout.drop_down_textview, cropList);
        dropdown_crop.setAdapter(cropAdapter);
    }

    List<CropItemMasterTable.CropItemMasterModel> cropItemList = new ArrayList<>();

    void bindCropItem(String cropcode) {
        CropItemMasterTable cropItemMasterTable = new CropItemMasterTable(getActivity());
        cropItemMasterTable.open();
        cropItemList = cropItemMasterTable.fetch(cropcode);
        cropItemMasterTable.close();
        CropItemAdapter cropAdapter = new CropItemAdapter(getActivity(), R.layout.drop_down_textview, cropItemList);
        dropdown_variety.setAdapter(cropAdapter);
    }

    List<StateMasterTable.StateMaster> statelist = new ArrayList<>();

    void bindState() {
        StateMasterTable stateMasterTable = new StateMasterTable(getActivity());
        stateMasterTable.open();
        statelist = stateMasterTable.fetch();
        stateMasterTable.close();
        StateAdapter stateAdapter = new StateAdapter(getActivity(), R.layout.drop_down_textview, statelist);
        dropdown_state.setAdapter(stateAdapter);
    }

    List<DistrictMasterTable.DistrictMaster> districtList = new ArrayList<>();

    void bindDistrict(String statecode) {
        DistrictMasterTable districtMasterTable = new DistrictMasterTable(getActivity());
        districtMasterTable.open();
        districtList = districtMasterTable.fetch_byGeograficalStateCode(statecode);
        DistrictAdapter districtAdapter = new DistrictAdapter(getActivity(), R.layout.drop_down_textview, districtList);
        dropdown_district.setAdapter(districtAdapter);
    }

    List<TalukaMasterTable.TalukaMaster> talukaList = new ArrayList<>();

    void bindTaluka(String districtCode) {
        TalukaMasterTable talukaMasterTable = new TalukaMasterTable(getActivity());
        talukaMasterTable.open();
        talukaList = talukaMasterTable.fetchBydistrictNo(districtCode);
        TalukaAdapter districtAdapter = new TalukaAdapter(getActivity(), R.layout.drop_down_textview, talukaList);
        dropdown_taluka.setAdapter(districtAdapter);
    }


    //    todo no of villages covered
    public List<String> VillagesCoveredNameList = new ArrayList<>();

    private void bindVillageCoverdNameListNo() {
        selected_no_odvillageCovered_chipgroup.removeAllViews();
        for (int index = 0; index < VillagesCoveredNameList.size(); index++) {
            final String tagName = VillagesCoveredNameList.get(index);
            final Chip chip = new Chip(getActivity());
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            chip.setChipIconResource(R.drawable.ic_farmer_icon);
            chip.setCloseIconResource(R.drawable.ic_close_black_24dp);
            if (passBackFragmentData)
                chip.setCloseIconEnabled(false);
            else
                chip.setCloseIconEnabled(true);

            //Added click listener on close icon to remove tag from ChipGroup
            chip.setOnCloseIconClickListener(view -> {
                VillagesCoveredNameList.remove(tagName);
                selected_no_odvillageCovered_chipgroup.removeView(chip);
            });
            selected_no_odvillageCovered_chipgroup.addView(chip);
        }
    }

}
