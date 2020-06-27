package com.example.ajeetseeds.ui.travel.addExpanse;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.AsyModelArray;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.event.EventCreateResponseModel;
import com.example.ajeetseeds.Model.travel.TravelExpenseResponseModel;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.ModeOfTravelMasterTable;
import com.example.ajeetseeds.sqlLite.travel.TravelHeaderTable;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseModel;
import com.example.ajeetseeds.sqlLite.travel.TravelLineExpenseTable;
import com.example.ajeetseeds.ui.travel.createTravel.CityAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTravelExpanseFragment extends Fragment {
    Button add_expenseButton, submitPage;
    ListView expense_List;
    private AddTravelExpanseViewModel mViewModel;
    List<TravelLineExpenseModel> travelLineExpenseList = new ArrayList<>();
    TravelExpenseListAdapter travelExpenseListAdapter;

    public static AddTravelExpanseFragment newInstance() {
        return new AddTravelExpanseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_travel_expanse_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddTravelExpanseViewModel.class);
        // TODO: Use the ViewModel
    }

    boolean passBackFragmentData = false;
    TravelHeaderTable.TravelHeaderModel submitTravelData = null;
    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        add_expenseButton = view.findViewById(R.id.add_expenseButton);
        expense_List = view.findViewById(R.id.expense_List);
        submitPage = view.findViewById(R.id.submitPage);
        add_expenseButton.setOnClickListener(view1 -> {
            AddExpense();
        });
        try {
            passBackFragmentData = getArguments().getBoolean("flag", false);
            submitTravelData = new Gson().fromJson(getArguments().getString("dataPass", ""),
                    TravelHeaderTable.TravelHeaderModel.class);

        } catch (Exception e) {

        } finally {
            try {
                TravelLineExpenseTable travelLineExpenseTable = new TravelLineExpenseTable(getActivity());
                travelLineExpenseTable.open();
                travelLineExpenseList = travelLineExpenseTable.fetch(submitTravelData.travelcode);
                travelLineExpenseTable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            travelExpenseListAdapter = new TravelExpenseListAdapter(getActivity(), travelLineExpenseList);
            expense_List.setAdapter(travelExpenseListAdapter);
            submitPage.setOnClickListener(view1 -> {
                try {
                    if (!loadingDialog.getLoadingState()) {
                        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
                        if (networkUtil) {
                            JSONArray postedJason = new JSONArray();
                            for (int i = 0; i < travelLineExpenseList.size(); i++) {
                                TravelLineExpenseModel data = travelLineExpenseList.get(i);
                                JSONObject temp_json = new JSONObject();
                                temp_json.put("travelcode", data.travelcode);
                                temp_json.put("line_no", data.line_no);
                                temp_json.put("date", data.date);
                                temp_json.put("from_loc", data.from_loc);
                                temp_json.put("to_loc", data.to_loc);
                                temp_json.put("departure", data.departure);
                                temp_json.put("arrival", data.arrival);
                                temp_json.put("fare", data.fare);
                                temp_json.put("mode_of_travel", data.mode_of_travel);
                                temp_json.put("loading_in_any", data.loading_in_any);
                                temp_json.put("distance_km", data.distance_km);
                                temp_json.put("fuel_vehicle_expance", data.fuel_vehicle_expance);
                                temp_json.put("daily_express", data.daily_express);
                                temp_json.put("vehicle_repairing", data.vehicle_repairing);
                                temp_json.put("local_convance", data.local_convance);
                                temp_json.put("other_expenses", data.other_expenses);
                                temp_json.put("total_amount_calulated", data.total_amount_calulated);
                                postedJason.put(temp_json);
                            }
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModelArray(StaticDataForApp.insertTravelExpense, postedJason, "insertTravelExpense"));
                        } else {
                            TravelExpenseResponseModel model = new TravelExpenseResponseModel();
                            model.condition = true;
                            model.created_on = null;
                            model.event_status = "INSERT EXPENSE";
                            model.message = "Insert Localy";
                            SyncDataTable syncDataTable = new SyncDataTable(getActivity());
                            syncDataTable.open();
                            syncDataTable.OutActivate(AllTablesName.TravelHeaderTable, 1);
                            syncDataTable.close();
                            bindLOcalDataBase(model);
                        }
                    }
                } catch (Exception e) {
                }
            });
            if (submitTravelData.status.equalsIgnoreCase("INSERT EXPENSE") || submitTravelData.status.equalsIgnoreCase("APPROVED")) {
                add_expenseButton.setEnabled(false);
                submitPage.setEnabled(false);
            } else {
                add_expenseButton.setEnabled(true);
                submitPage.setEnabled(true);
            }
        }
    }

    LoadingDialog loadingDialog = new LoadingDialog();
    boolean datedialog = false;
    List<CityMasterTable.CityMasterModel> cityList = new ArrayList<>();
    CityMasterTable.CityMasterModel selectedFromCity = null;
    CityMasterTable.CityMasterModel selectedToCity = null;
    ModeOfTravelMasterTable.ModeOfTravelModel selectedModeOfTravelCost = null;

    public void AddExpense() {
        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View PopupView = inflater.inflate(R.layout.add_travel_expense_view, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(PopupView);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            TextInputEditText et_date = PopupView.findViewById(R.id.et_date);
            AutoCompleteTextView from_city = PopupView.findViewById(R.id.from_city);
            AutoCompleteTextView to_city = PopupView.findViewById(R.id.to_city);
            TextInputEditText et_departure = PopupView.findViewById(R.id.et_departure);
            TextInputEditText et_arrival = PopupView.findViewById(R.id.et_arrival);
            TextInputEditText et_fare = PopupView.findViewById(R.id.et_fare);
            AutoCompleteTextView mode_of_travel_drop = PopupView.findViewById(R.id.mode_of_travel_drop);
            TextInputEditText et_loading_in_any = PopupView.findViewById(R.id.et_loading_in_any);
            TextInputEditText et_distance_km = PopupView.findViewById(R.id.et_distance_km);
            TextInputEditText et_fuel_vehicle_expance = PopupView.findViewById(R.id.et_fuel_vehicle_expance);
            TextInputEditText et_daily_express = PopupView.findViewById(R.id.et_daily_express);
            TextInputEditText et_vehicle_repairing = PopupView.findViewById(R.id.et_vehicle_repairing);
            TextInputEditText et_local_convance = PopupView.findViewById(R.id.et_local_convance);
            TextInputEditText et_other_expenses = PopupView.findViewById(R.id.et_other_expenses);
            TextView tv_totalApplyExpenceAmmount = PopupView.findViewById(R.id.tv_totalApplyExpenceAmmount);
            TextView tv_travel_modelrate=PopupView.findViewById(R.id.tv_travel_modelrate);
            Button submitPage = PopupView.findViewById(R.id.submitPage);
            et_date.setOnTouchListener((view1, motionEvent) -> {
                if (!datedialog) {
                    datedialog = true;
                    MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                    MaterialDatePicker picker = builder.build();
                    if (!picker.isVisible()) {
                        picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                        picker.addOnPositiveButtonClickListener(selection -> {
                            et_date.setText(picker.getHeaderText());
                            et_date.setError(null);
                        });
                        picker.addOnDismissListener(dialogInterface -> {
                            datedialog = false;
                        });
                    }
                }
                return true;
            });
            if (cityList.size() <= 0) {
                try {
                    CityMasterTable cityMasterTable = new CityMasterTable(getActivity());
                    cityMasterTable.open();
                    cityList = cityMasterTable.fetch();
                    cityMasterTable.close();
                } catch (Exception e) {
                }
            }
            CityAdapter from_cityAdapter = new CityAdapter(getActivity(), R.layout.drop_down_textview, cityList);
            from_city.setAdapter(from_cityAdapter);
            CityAdapter to_cityAdapter = new CityAdapter(getActivity(), R.layout.drop_down_textview, cityList);
            to_city.setAdapter(to_cityAdapter);

            from_city.setOnItemClickListener((adapterView, view, i, l) -> {
                selectedFromCity = cityList.get(i);
                from_city.setError(null);
            });
            to_city.setOnItemClickListener((adapterView, view, i, l) -> {
                selectedToCity = cityList.get(i);
                ModeOfTravelMasterTable modeOfTravelMasterTable = new ModeOfTravelMasterTable(getActivity());
                modeOfTravelMasterTable.open();
                selectedModeOfTravelCost = modeOfTravelMasterTable.fetchModeOfTravel(selectedToCity.code, sessionManagement.getModelOfTravel());
                modeOfTravelMasterTable.close();
                tv_travel_modelrate.setText("Lodging "+selectedModeOfTravelCost.lodging+"And Half DA "+ selectedModeOfTravelCost.da_half+" AND FULL DA "+selectedModeOfTravelCost.da_full);
                to_city.setError(null);
            });
            et_departure.setOnClickListener(view -> {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        (timePicker, hourOfDay, minute) -> {
                            et_departure.setText(hourOfDay + ":" + minute);
                            et_departure.setError(null);
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            });
            et_arrival.setOnClickListener(view -> {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        (timePicker, hourOfDay, minute) -> {
                            et_arrival.setText(hourOfDay + ":" + minute);
                            et_arrival.setError(null);
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            });
            try {
                ModeOfTravelMasterTable modeOfTravelMasterTable = new ModeOfTravelMasterTable(getActivity());
                modeOfTravelMasterTable.open();
                List<String> modeOftravel = modeOfTravelMasterTable.getModeOfTravelDropDown(sessionManagement.getModelOfTravel());
                modeOfTravelMasterTable.close();
                ModeOftravelAdapter modeOftravelAdapter = new ModeOftravelAdapter(getActivity(), R.layout.drop_down_textview, modeOftravel);
                mode_of_travel_drop.setAdapter(modeOftravelAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_totalApplyExpenceAmmount.setText("0");

            et_fare.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = Math.abs((date2.getTime() - date1.getTime()) / (1000 * 60*60));
                        if (difference >= 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference >= 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {

                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_loading_in_any.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_fuel_vehicle_expance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_daily_express.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_vehicle_repairing.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_local_convance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            et_other_expenses.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float fare = Float.parseFloat(et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString());
                    float loadging_in_any = Float.parseFloat(et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString());
                    float fuel_vehicle_expance = Float.parseFloat(et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString());
                    float daily_express = Float.parseFloat(et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString());
                    float vehicle_repairing = Float.parseFloat(et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString());
                    float local_convance = Float.parseFloat(et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString());
                    float other_expenses = Float.parseFloat(et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString());
                    float appy_full_And_Half_DA = 0;
                    try {
                        String time1 = et_departure.getText().toString();
                        String time2 = et_arrival.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        float difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                        if (difference > 12) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_full;
                        } else if (difference > 8) {
                            appy_full_And_Half_DA = selectedModeOfTravelCost.da_half;
                        }
                    } catch (Exception e) {
                    }
                    tv_totalApplyExpenceAmmount.setText(String.valueOf(fare + loadging_in_any + fuel_vehicle_expance +
                            daily_express + vehicle_repairing + local_convance + other_expenses +
                            selectedModeOfTravelCost.lodging + appy_full_And_Half_DA));
                }
            });
            submitPage.setOnClickListener(view -> {
                if(et_date.getText().toString().equalsIgnoreCase("")){
                    et_date.setError("Please Enter Date");
                    return;
                }else  if(from_city.getText().toString().equalsIgnoreCase("")){
                    from_city.setError("Please Select From City");
                    return;
                }else  if(to_city.getText().toString().equalsIgnoreCase("")){
                    to_city.setError("Please Select To City");
                    return;
                }
                else  if(et_departure.getText().toString().equalsIgnoreCase("")){
                    et_departure.setError("Please Enter Departure Time");
                    return;
                }  else  if(et_arrival.getText().toString().equalsIgnoreCase("")){
                    et_arrival.setError("Please Enter Arrival Time");
                    return;
                } else  if(et_fare.getText().toString().equalsIgnoreCase("")){
                    et_fare.setError("Please Enter Fare");
                    return;
                }else  if(mode_of_travel_drop.getText().toString().equalsIgnoreCase("")){
                    mode_of_travel_drop.setError("Please Select Mode Of Travel");
                    return;
                }
                //todo bind submit
                TravelLineExpenseModel travelLineExpensedata = new TravelLineExpenseModel(
                        submitTravelData.android_travelcode, submitTravelData.travelcode, String.valueOf(travelLineExpenseList.size() + 1),
                        et_date.getText().toString(), selectedFromCity.code, selectedToCity.code, et_departure.getText().toString(),
                        et_arrival.getText().toString(), et_fare.getText().toString().equalsIgnoreCase("") ? "0" : et_fare.getText().toString(),
                        mode_of_travel_drop.getText().toString(), et_loading_in_any.getText().toString().equalsIgnoreCase("") ? "0" : et_loading_in_any.getText().toString()
                        , et_distance_km.getText().toString().equalsIgnoreCase("") ? "0" : et_distance_km.getText().toString(),
                        et_fuel_vehicle_expance.getText().toString().equalsIgnoreCase("") ? "0" : et_fuel_vehicle_expance.getText().toString(),
                        et_daily_express.getText().toString().equalsIgnoreCase("") ? "0" : et_daily_express.getText().toString(),
                        et_vehicle_repairing.getText().toString().equalsIgnoreCase("") ? "0" : et_vehicle_repairing.getText().toString(),
                        et_local_convance.getText().toString().equalsIgnoreCase("") ? "0" : et_local_convance.getText().toString(),
                        et_other_expenses.getText().toString().equalsIgnoreCase("") ? "0" : et_other_expenses.getText().toString(),
                        tv_totalApplyExpenceAmmount.getText().toString(), null);
                travelLineExpenseList.add(travelLineExpensedata);
                travelExpenseListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });
            //You need to add the following line for this solution to work; thanks skayred
            PopupView.setFocusableInTouchMode(true);
            PopupView.requestFocus();
            PopupView.setOnKeyListener((view, keyCode, keyEvent) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    datedialog = false;
                    dialog.dismiss();
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
        }
    }

    private class CommanHitToServer extends AsyncTask<AsyModelArray, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;

        @Override
        protected void onPreExecute() {
            loadingDialog.showLoadingDialog(getActivity());
            super.onPreExecute();
        }

        @Override
        protected HttpHandlerModel doInBackground(AsyModelArray... asyModels) {
            try {
                URL postingUrl = hitObj.createUrl(asyModels[0].getPostingUrl());
                flagOfAction = asyModels[0].getFlagOfAction();
                return hitObj.postArrayHttpRequest(postingUrl, asyModels[0].getPostingJson());
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
                if (flagOfAction.equalsIgnoreCase("insertTravelExpense")) {
                    List<TravelExpenseResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(),
                            new TypeToken<List<TravelExpenseResponseModel>>() {
                            }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        bindLOcalDataBase(responseslist.get(0));
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
            new AndroidExceptionHandel(e.getMessage(), "Travel Expance Cteate", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bindLOcalDataBase(TravelExpenseResponseModel model) {
        try {
            TravelHeaderTable travelHeaderTable = new TravelHeaderTable(getActivity());
            travelHeaderTable.open();
            travelHeaderTable.updateStatus(submitTravelData.travelcode, model.event_status, model.created_on);
            travelHeaderTable.close();
            TravelLineExpenseTable travelLineExpenseTable = new TravelLineExpenseTable(getActivity());
            travelLineExpenseTable.open();
            travelLineExpenseTable.insertBulk(travelLineExpenseList, model.created_on, model.created_on == null ? "LOCAL" : null);
            travelLineExpenseTable.close();
            loadFragments(R.id.nav_view_travel, "View TA/DA List");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFragments(int id, String fragmentName) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        //navController.navigateUp();
        navController.navigate(id);
        getActivity().setTitle(fragmentName);
    }

}

