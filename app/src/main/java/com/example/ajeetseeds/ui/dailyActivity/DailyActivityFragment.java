package com.example.ajeetseeds.ui.dailyActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.dailyActivity.DailyActivityModel;
import com.example.ajeetseeds.Model.dailyActivity.DailyActivityResponse;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityHeader;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityLine;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyActivityFragment extends Fragment {
    AppCompatEditText contact_no, order_collected, payment_collected;
    ImageView add_Contact_bt;
    ChipGroup selected_contact_chipgroup;
    Button next_buttonForAddLines, clear_Design, submitPage;

    ListView listview_add_line;
    SessionManagement sessionManagement;
    private DailyActivityViewModel dailyActivityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dailyActivityViewModel =
                ViewModelProviders.of(this).get(DailyActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_daily_activity, container, false);
        return root;
    }

    DailyActivityModel dailyActivityModel = new DailyActivityModel();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getContext());
        bindUi_InIt();
        listview_add_line = view.findViewById(R.id.listview_add_line);

            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(
                            listview_add_line,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    if (!hideAllActions) {
                                        for (int position : reverseSortedPositions) {
                                            dailyActivityModel.addlines.remove(position);
                                            adapter.notifyDataSetChanged();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("dataPass", new Gson().toJson(dailyActivityModel));
                                            setArguments(bundle);
                                        }
                                    }

                                }
                            });
            listview_add_line.setOnTouchListener(touchListener);
        contact_no = view.findViewById(R.id.contact_no);
        order_collected = view.findViewById(R.id.order_collected);
        payment_collected = view.findViewById(R.id.payment_collected);
        selected_contact_chipgroup = view.findViewById(R.id.selected_contact_chipgroup);
        next_buttonForAddLines = view.findViewById(R.id.next_buttonForAddLines);
        clear_Design = view.findViewById(R.id.clear_Design);
        submitPage = view.findViewById(R.id.submitPage);
        add_Contact_bt = view.findViewById(R.id.add_Contact_bt);
        add_Contact_bt.setOnClickListener(view1 -> {
            if (contact_no.length() == 10) {
                contactList.add(contact_no.getText().toString());
                bindContactListNo();
                contact_no.setText("");
            } else {
                contact_no.setError("Contact Lenght must be 10 degit.");
            }
        });

        next_buttonForAddLines.setOnClickListener(view1 -> {
            if (contactList.size() <= 0) {
                contact_no.setError("Please Add minimum one contact.");
                return;
            } else if (order_collected.getText().toString().contentEquals("")) {
                order_collected.setError("Please Enter Order Collected.");
                return;
            } else if (payment_collected.getText().toString().contentEquals("")) {
                payment_collected.setError("Please Enter Payment Collected");
                return;
            }
            dailyActivityModel.contact = contactList.get(0);
            dailyActivityModel.contact1 = contactList.size() == 2 ? contactList.get(1) : "0";
            dailyActivityModel.order_collected = order_collected.getText().toString();
            dailyActivityModel.payment_collected = payment_collected.getText().toString();
            loadFragments(R.id.nav_daily_activityLine, "Add Lines", new Gson().toJson(dailyActivityModel));
        });
        clear_Design.setOnClickListener(view1 -> {
            clear_Screen();
        });

        submitPage.setOnClickListener(view1 -> {
            try {
                if (dailyActivityModel.addlines == null || dailyActivityModel.addlines.size() <= 0) {
                    Snackbar.make(submitPage, "Please Insert Minimun Single Line", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view2 -> {
                    }).show();
                    return;
                }
                if (!loadingDialog.getLoadingState()) {
                    boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
                    if (networkUtil) {
                        JSONObject postedJson = new JSONObject();
                        postedJson.put("contact", dailyActivityModel.contact);
                        postedJson.put("contact1", dailyActivityModel.contact1);
                        postedJson.put("order_collected", dailyActivityModel.order_collected);
                        postedJson.put("payment_collected", dailyActivityModel.payment_collected);
                        postedJson.put("email_id", sessionManagement.getUserEmail());
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < dailyActivityModel.addlines.size(); i++) {
                            JSONObject temp = new JSONObject();
                            temp.put("farmer_name", dailyActivityModel.addlines.get(i).farmer_name);
                            temp.put("district", dailyActivityModel.addlines.get(i).district);
                            temp.put("village", dailyActivityModel.addlines.get(i).village);

                            temp.put("ajeet_crop_and_verity", dailyActivityModel.addlines.get(i).ajeet_crop_and_verity);
                            temp.put("ajeet_crop_age", dailyActivityModel.addlines.get(i).ajeet_crop_age);
                            temp.put("ajeet_fruits_per", dailyActivityModel.addlines.get(i).ajeet_fruits_per);
                            temp.put("ajeet_pest", dailyActivityModel.addlines.get(i).ajeet_pest);
                            temp.put("ajeet_disease", dailyActivityModel.addlines.get(i).ajeet_disease);

                            temp.put("check_crop_and_verity", dailyActivityModel.addlines.get(i).check_crop_and_variety);
                            temp.put("check_crop_age", dailyActivityModel.addlines.get(i).check_crop_age);
                            temp.put("check_fruits_per", dailyActivityModel.addlines.get(i).check_fruits_per);
                            temp.put("check_pest", dailyActivityModel.addlines.get(i).check_pest);
                            temp.put("check_disease", dailyActivityModel.addlines.get(i).check_disease);
                            jsonArray.put(temp);
                        }
                        postedJson.put("addlines", jsonArray);
                        new DailyActivityHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.dailyActivityInsertHeaderLines, postedJson, "dailyActivitySubmit"));
                    } else {
                        bindLOcalDataBase("0", null);
                        clear_Screen();
                    }
                }
            } catch (Exception e) {
            }
        });
    }

    void clear_Screen() {
        try {
            setArguments(null);
            if (!contactList.isEmpty())
                contactList.clear();
            contact_no.setEnabled(true);
            order_collected.setEnabled(true);
            payment_collected.setEnabled(true);
            dailyActivityModel.contact = "";
            dailyActivityModel.contact1 = "";
            dailyActivityModel.payment_collected = "";
            dailyActivityModel.order_collected = "";
            dailyActivityModel.addlines.clear();
            bindUi_InIt();
            bindContactListNo();
            DailyActivityAdapter adapter = new DailyActivityAdapter(getActivity(), dailyActivityModel.addlines);
            listview_add_line.setAdapter(adapter);
            contact_no.setText("");
            order_collected.setText("");
            payment_collected.setText("");
            clear_Design.setVisibility(View.GONE);
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

    public void bindLOcalDataBase(String activity_no, String updated_on) {
        //todo insert Heder
        DailyActivityHeader dailyActivityHeader = new DailyActivityHeader(getActivity());
        dailyActivityHeader.open();
        String android_activity_no = dailyActivityHeader.getTableSequenceNo();
        dailyActivityHeader.insert(dailyActivityHeader.new DailyActivityHeaderModel(android_activity_no, activity_no, contactList.get(0), contactList.size() == 2 ? contactList.get(1) : "0", order_collected.getText().toString(), payment_collected.getText().toString(), updated_on));
        dailyActivityHeader.close();
        //todo insert Liine
        try {
            DailyActivityLine dailyActivityLine = new DailyActivityLine(getActivity());
            dailyActivityLine.open();
            List<DailyActivityLine.DailyActivityLinesModel> addlines = new ArrayList<>();
            for (int i = 0; i < dailyActivityModel.addlines.size(); i++) {
                DailyActivityLine.DailyActivityLinesModel tempObject = dailyActivityLine.new DailyActivityLinesModel(android_activity_no, activity_no, dailyActivityModel.addlines.get(i).farmer_name, dailyActivityModel.addlines.get(i).district, dailyActivityModel.addlines.get(i).village,
                        dailyActivityModel.addlines.get(i).ajeet_crop_and_verity, dailyActivityModel.addlines.get(i).ajeet_crop_age, dailyActivityModel.addlines.get(i).ajeet_fruits_per, dailyActivityModel.addlines.get(i).ajeet_pest, dailyActivityModel.addlines.get(i).ajeet_disease,
                        dailyActivityModel.addlines.get(i).check_crop_and_variety, dailyActivityModel.addlines.get(i).check_crop_age, dailyActivityModel.addlines.get(i).check_fruits_per, dailyActivityModel.addlines.get(i).check_pest, dailyActivityModel.addlines.get(i).check_disease);
                addlines.add(tempObject);
            }
            boolean result = dailyActivityLine.insertBulkData(addlines);
            dailyActivityLine.close();

            //todo master entry Table out redy is going to be 1 so that data uploaded to the server
            if (activity_no.contentEquals("0")) {
                SyncDataTable syncDataTable = new SyncDataTable(getActivity());
                syncDataTable.open();
                syncDataTable.OutActivate(AllTablesName.DailyActivityHeader, 1);
                syncDataTable.close();
            }
        } catch (Exception e) {
            Snackbar.make(submitPage, "Not Inserted. " + e.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
            }).show();
        }
    }


    public List<String> contactList = new ArrayList<>();

    private void bindContactListNo() {
        selected_contact_chipgroup.removeAllViews();
        for (int index = 0; index < contactList.size(); index++) {
            final String tagName = contactList.get(index);
            final Chip chip = new Chip(getActivity());
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            chip.setChipIconResource(R.drawable.ic_call_black_24dp);
            chip.setCloseIconResource(R.drawable.ic_close_black_24dp);
            if (isdataPass) {
                chip.setCloseIconEnabled(false);
            } else {
                chip.setCloseIconEnabled(true);
            }

            //Added click listener on close icon to remove tag from ChipGroup
            chip.setOnCloseIconClickListener(view -> {
                    contactList.remove(tagName);
                    selected_contact_chipgroup.removeView(chip);
            });
            selected_contact_chipgroup.addView(chip);
        }
    }

    boolean isdataPass = false;
    boolean hideAllActions = false;

    void bindUi_InIt() {
        try {
            dailyActivityModel = new Gson().fromJson(getArguments().getString("dataPass", ""), DailyActivityModel.class);
            if (!contactList.isEmpty())
                contactList.clear();
            contactList.add(dailyActivityModel.contact);
            if (!dailyActivityModel.contact1.contentEquals("0") && !dailyActivityModel.contact1.contentEquals("")) {
                contactList.add(dailyActivityModel.contact1);
            }
            order_collected.setText(dailyActivityModel.order_collected);
            payment_collected.setText(String.valueOf(dailyActivityModel.payment_collected));

            contact_no.setEnabled(false);
            order_collected.setEnabled(false);
            payment_collected.setEnabled(false);
            isdataPass = true;
            //bind add list Data...
            adapter = new DailyActivityAdapter(getActivity(), dailyActivityModel.addlines);
            listview_add_line.setAdapter(adapter);
            clear_Design.setVisibility(View.VISIBLE);
            if (getArguments().getBoolean("hideAllActions", false)) {
                hideAllActions = true;
                add_Contact_bt.setVisibility(View.GONE);
                next_buttonForAddLines.setVisibility(View.GONE);
                clear_Design.setVisibility(View.GONE);
                submitPage.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }

    DailyActivityAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        bindUi_InIt();
        bindContactListNo();
        getActivity().setTitle("Daily Activity");
    }


    LoadingDialog loadingDialog = new LoadingDialog();

    private class DailyActivityHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
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
                if (flagOfAction.equalsIgnoreCase("dailyActivitySubmit")) {
                    List<DailyActivityResponse> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<DailyActivityResponse>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        bindLOcalDataBase(responseslist.get(0).daily_activity_no, responseslist.get(0).updated_on);
                        clear_Screen();
                    } else {
                        Snackbar.make(submitPage, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }

                }
            } else {
                bindLOcalDataBase("0", null);
                Snackbar.make(submitPage, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), "Daily Activity", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

}