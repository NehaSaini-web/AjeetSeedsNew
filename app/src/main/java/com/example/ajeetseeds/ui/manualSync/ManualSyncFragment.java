package com.example.ajeetseeds.ui.manualSync;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.dailyActivity.DailyActivityResponse;
import com.example.ajeetseeds.Model.syncModel.CropMasterSyncModel;
import com.example.ajeetseeds.Model.syncModel.Geographical_SetupSyncModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.CommanFunction;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityHeader;
import com.example.ajeetseeds.sqlLite.dailyactivity.DailyActivityLine;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.DistrictMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.Geographical_SetupTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.RegionMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.StateMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.TalukaMasterTable;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.ZoneMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.sqlLite.travel.CityMasterTable;
import com.example.ajeetseeds.sqlLite.travel.ModeOfTravelMasterTable;
import com.example.ajeetseeds.ui.dailyActivity.viewDailyActivity.DailyActivityResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManualSyncFragment extends Fragment {
    TextView geographicalCount, zoneCount, stateCount, regionCount, districtCount, talukaCount, cropCount, cropItemCount, customerCount
    ,cityCount,mode_of_travelCount;
    private ManualSyncViewModel mViewModel;
    //todo geographical setup
    LinearLayout ly_geographicsetupSection;
    ProgressBar geographical_setup_progress, zone_master_progress, state_master_progress, region_master_progress,
            district_master_progress, taluka_master_progress;
    ImageView geographical_setup_syncButton, geographicalSetupExpandOrcollaps;

    //todo Crop Master data
    LinearLayout ly_crop_setupSection;
    ProgressBar crop_setup_progress, crop_master_progress, crops_item_master_progress, customer_master_progress;
    ImageView crop_setup_syncButton, cropSetupExpandOrcollaps;

    //Travel data
    LinearLayout ly_travel_setupSection;
    ProgressBar travel_setup_progress, city_master_progress, mode_of_travel_progress;
    ImageView travel_setup_syncButton, travelSetupExpandOrcollaps;
    String initDate = "2019-02-20T00:00:42.387";
    SessionManagement sessionManagement;

    public static ManualSyncFragment newInstance() {
        return new ManualSyncFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manual_sync_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ManualSyncViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        //todo geographical setup
        ly_geographicsetupSection = view.findViewById(R.id.ly_geographicsetupSection);
        geographical_setup_progress = view.findViewById(R.id.geographical_setup_progress);
        geographicalSetupExpandOrcollaps = view.findViewById(R.id.geographicalSetupExpandOrcollaps);
        geographical_setup_syncButton = view.findViewById(R.id.geographical_setup_syncButton);
        zone_master_progress = view.findViewById(R.id.zone_master_progress);
        state_master_progress = view.findViewById(R.id.state_master_progress);
        region_master_progress = view.findViewById(R.id.region_master_progress);
        district_master_progress = view.findViewById(R.id.district_master_progress);
        taluka_master_progress = view.findViewById(R.id.taluka_master_progress);

        //todo crop master setup
        ly_crop_setupSection = view.findViewById(R.id.ly_crop_setupSection);
        crop_setup_progress = view.findViewById(R.id.crop_setup_progress);
        crop_master_progress = view.findViewById(R.id.crop_master_progress);
        crops_item_master_progress = view.findViewById(R.id.crops_item_master_progress);
        customer_master_progress = view.findViewById(R.id.customer_master_progress);
        crop_setup_syncButton = view.findViewById(R.id.crop_setup_syncButton);
        cropSetupExpandOrcollaps = view.findViewById(R.id.cropSetupExpandOrcollaps);

        //todo travel section
        ly_travel_setupSection = view.findViewById(R.id.ly_travel_setupSection);
        travel_setup_progress = view.findViewById(R.id.travel_setup_progress);
        city_master_progress = view.findViewById(R.id.city_master_progress);
        mode_of_travel_progress = view.findViewById(R.id.mode_of_travel_progress);
        travel_setup_syncButton = view.findViewById(R.id.travel_setup_syncButton);
        travelSetupExpandOrcollaps = view.findViewById(R.id.travelSetupExpandOrcollaps);

        //todo set count of all Table data
        geographicalCount = view.findViewById(R.id.geographicalCount);
        zoneCount = view.findViewById(R.id.zoneCount);
        stateCount = view.findViewById(R.id.stateCount);
        regionCount = view.findViewById(R.id.regionCount);
        districtCount = view.findViewById(R.id.districtCount);
        talukaCount = view.findViewById(R.id.talukaCount);
        cropCount = view.findViewById(R.id.cropCount);
        cropItemCount = view.findViewById(R.id.cropItemCount);
        customerCount = view.findViewById(R.id.customerCount);
        cityCount=view.findViewById(R.id.cityCount);
        mode_of_travelCount=view.findViewById(R.id.mode_of_travelCount);
        event_geographicalSetup();
        event_cropSetup();
        event_travelSetup();
        loadingDialog = new LoadingDialog();
        new CommanApiHit("GetServerTableData").executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    LoadingDialog loadingDialog;
    CommanFunction.ManualSyncModel GlobalmanualSyncServerData;

    void countOfAllTableData() {
        try {
            CommanFunction commanFunction = new CommanFunction(getActivity());
            commanFunction.open();
            CommanFunction.ManualSyncModel manualSyncdata = commanFunction.getManualSyncData();
            commanFunction.close();
            if (manualSyncdata != null) {
                geographicalCount.setText(" (" + manualSyncdata.geographical_setup + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.geographical_setup : "/0") + ")");
                zoneCount.setText(" (" + manualSyncdata.zone_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.zone_master : "/0") + ")");
                stateCount.setText(" (" + manualSyncdata.state_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.state_master : "/0") + ")");
                regionCount.setText(" (" + manualSyncdata.region_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.region_master : "/0") + ")");
                districtCount.setText(" (" + manualSyncdata.district_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.district_master : "/0") + ")");
                talukaCount.setText(" (" + manualSyncdata.taluka_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.taluka_master : "/0") + ")");
                cropCount.setText(" (" + manualSyncdata.crop_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.crop_master : "/0") + ")");
                cropItemCount.setText(" (" + manualSyncdata.crops_item_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.crops_item_master : "/0") + ")");
                customerCount.setText(" (" + manualSyncdata.customer_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.customer_master : "/0") + ")");
                cityCount.setText(" (" + manualSyncdata.city_master + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.city_master : "/0") + ")");
                mode_of_travelCount.setText(" (" + manualSyncdata.mode_of_travel + (GlobalmanualSyncServerData != null ? "/" + GlobalmanualSyncServerData.mode_of_travel : "/0") + ")");
            }
        } catch (Exception e) {
        }
    }

    void event_geographicalSetup() {
        geographicalSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        geographicalSetupExpandOrcollaps.setOnClickListener(view -> {
            if (ly_geographicsetupSection.getVisibility() == View.GONE) {
                ly_geographicsetupSection.setVisibility(View.VISIBLE);
                geographicalSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                ly_geographicsetupSection.setVisibility(View.GONE);
                geographicalSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        geographical_setup_syncButton.setOnClickListener(view -> {
            if (ly_geographicsetupSection.getVisibility() == View.GONE) {
                ly_geographicsetupSection.setVisibility(View.VISIBLE);
                geographicalSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }
            geographical_setup_progress.setVisibility(View.VISIBLE);
            zone_master_progress.setVisibility(View.VISIBLE);
            state_master_progress.setVisibility(View.VISIBLE);
            region_master_progress.setVisibility(View.VISIBLE);
            district_master_progress.setVisibility(View.VISIBLE);
            taluka_master_progress.setVisibility(View.VISIBLE);
            new CommanApiHit("updateGeographicalsetup").executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        });
    }

    void event_cropSetup() {
        cropSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        cropSetupExpandOrcollaps.setOnClickListener(view -> {
            if (ly_crop_setupSection.getVisibility() == View.GONE) {
                ly_crop_setupSection.setVisibility(View.VISIBLE);
                cropSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                ly_crop_setupSection.setVisibility(View.GONE);
                cropSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        crop_setup_syncButton.setOnClickListener(view -> {
            if (ly_crop_setupSection.getVisibility() == View.GONE) {
                ly_crop_setupSection.setVisibility(View.VISIBLE);
                cropSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }
            crop_setup_progress.setVisibility(View.VISIBLE);
            crop_master_progress.setVisibility(View.VISIBLE);
            crops_item_master_progress.setVisibility(View.VISIBLE);
            customer_master_progress.setVisibility(View.VISIBLE);
            new CommanApiHit("CropMasterUpdate").executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        });
    }

    void event_travelSetup() {
        travelSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        travelSetupExpandOrcollaps.setOnClickListener(view -> {
            if (ly_travel_setupSection.getVisibility() == View.GONE) {
                ly_travel_setupSection.setVisibility(View.VISIBLE);
                travelSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                ly_travel_setupSection.setVisibility(View.GONE);
                travelSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        travel_setup_syncButton.setOnClickListener(view -> {
            if (ly_travel_setupSection.getVisibility() == View.GONE) {
                ly_travel_setupSection.setVisibility(View.VISIBLE);
                travelSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }
            travel_setup_progress.setVisibility(View.VISIBLE);
            city_master_progress.setVisibility(View.VISIBLE);
            mode_of_travel_progress.setVisibility(View.VISIBLE);
            new CommanApiHit("TravelMasterUpdate").executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        });
    }

    GlobalPostingMethod globalPostingMethod = new GlobalPostingMethod();

    public class CommanApiHit extends AsyncTask<Void, String, Void> {
        String flagOfAction = "";

        CommanApiHit(String flagOfAction) {
            this.flagOfAction = flagOfAction;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (flagOfAction.equalsIgnoreCase("GetServerTableData"))
                loadingDialog.showLoadingDialog(getActivity());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (flagOfAction.equalsIgnoreCase("updateGeographicalsetup"))
                updateGeographicalsetup();
            if (flagOfAction.equalsIgnoreCase("CropMasterUpdate"))
                CropMasterUpdate();
            if (flagOfAction.equalsIgnoreCase("TravelMasterUpdate"))
                TravelMasterUpdate();
            if (flagOfAction.equalsIgnoreCase("GetServerTableData")) {
                try {
                    List<CommanFunction.ManualSyncModel> manualSyncServerData = new Gson().fromJson(
                            globalPostingMethod.getHttpRequest(
                                    globalPostingMethod.createUrl(StaticDataForApp.syncAllTableData)).getJsonResponse(),
                            new TypeToken<List<CommanFunction.ManualSyncModel>>() {
                            }.getType());
                    if (manualSyncServerData.size() > 0 && manualSyncServerData.get(0).condition) {
                        GlobalmanualSyncServerData = manualSyncServerData.get(0);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0].equalsIgnoreCase(AllTablesName.Geographical_SetupTable))
                geographical_setup_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.ZoneMasterTable))
                zone_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.StateMasterTable))
                state_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.RegionMasterTable))
                region_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.DistrictMasterTable))
                district_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.TalukaMasterTable))
                taluka_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.CropMasterTable))
                crop_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.CropItemMasterTable))
                crops_item_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.CustomerMasterTable))
                customer_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.CityMasterTable))
                city_master_progress.setVisibility(View.GONE);
            else if (values[0].equalsIgnoreCase(AllTablesName.ModeOfTravelMasterTable))
                mode_of_travel_progress.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flagOfAction.equalsIgnoreCase("GetServerTableData")) {
                loadingDialog.dismissLoading();
                countOfAllTableData();
            } else {
                bindFinalResponse(flagOfAction);
            }
        }

        void updateGeographicalsetup() {
            try {
                //todo get data from server...
                Geographical_SetupSyncModel geographical_setupSyncModeldata = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.GeographicalSetupData + initDate)).getJsonResponse(), Geographical_SetupSyncModel.class);
                if (geographical_setupSyncModeldata != null) {
                    if (geographical_setupSyncModeldata.geographical_Setup.size() > 0) {
                        Geographical_SetupTable geographical_setupTable = new Geographical_SetupTable(getActivity().getApplicationContext());
                        geographical_setupTable.open();
                        geographical_setupTable.insertArray(geographical_setupSyncModeldata.geographical_Setup);
                        geographical_setupTable.close();
                    }
                    publishProgress(AllTablesName.Geographical_SetupTable);
                    //todo insert all zone from server
                    if (geographical_setupSyncModeldata.zone_master.size() > 0) {
                        ZoneMasterTable zoneMasterTable = new ZoneMasterTable(getActivity().getApplicationContext());
                        zoneMasterTable.open();
                        zoneMasterTable.insertArray(geographical_setupSyncModeldata.zone_master);
                        zoneMasterTable.close();
                    }
                    publishProgress(AllTablesName.ZoneMasterTable);
                    //todo insert all State Master Table from server
                    if (geographical_setupSyncModeldata.state_master.size() > 0) {
                        StateMasterTable stateMasterTable = new StateMasterTable(getActivity().getApplicationContext());
                        stateMasterTable.open();
                        stateMasterTable.insertArray(geographical_setupSyncModeldata.state_master);
                        stateMasterTable.close();
                    }
                    publishProgress(AllTablesName.StateMasterTable);
                    //todo insert all  Region Master Table from server
                    if (geographical_setupSyncModeldata.region_master.size() > 0) {
                        RegionMasterTable regionMasterTable = new RegionMasterTable(getActivity().getApplicationContext());
                        regionMasterTable.open();
                        regionMasterTable.insertArray(geographical_setupSyncModeldata.region_master);
                        regionMasterTable.close();
                    }
                    publishProgress(AllTablesName.RegionMasterTable);
                    //todo insert all District Master Table from server
                    if (geographical_setupSyncModeldata.district_master.size() > 0) {
                        DistrictMasterTable districtMasterTable = new DistrictMasterTable(getActivity().getApplicationContext());
                        districtMasterTable.open();
                        districtMasterTable.insertArray(geographical_setupSyncModeldata.district_master);
                        districtMasterTable.close();
                    }
                    publishProgress(AllTablesName.DistrictMasterTable);
                    //todo insert all District Master Table from server
                    if (geographical_setupSyncModeldata.taluka_master.size() > 0) {
                        TalukaMasterTable talukaMasterTable = new TalukaMasterTable(getActivity().getApplicationContext());
                        talukaMasterTable.open();
                        talukaMasterTable.insertArray(geographical_setupSyncModeldata.taluka_master);
                        talukaMasterTable.close();
                    }
                    publishProgress(AllTablesName.TalukaMasterTable);
                }
            } catch (Exception e) {
                publishProgress(AllTablesName.Geographical_SetupTable);
                publishProgress(AllTablesName.ZoneMasterTable);
                publishProgress(AllTablesName.StateMasterTable);
                publishProgress(AllTablesName.RegionMasterTable);
                publishProgress(AllTablesName.DistrictMasterTable);
                publishProgress(AllTablesName.TalukaMasterTable);
            }
        }

        void CropMasterUpdate() {
            try {
                CropMasterSyncModel cropMasterSyncResponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCropAndItemMasterSync + initDate)).getJsonResponse(), CropMasterSyncModel.class);
                if (cropMasterSyncResponse != null) {
                    if (cropMasterSyncResponse.crop_master.size() > 0) {
                        CropMasterTable cropMasterTable = new CropMasterTable(getActivity());
                        cropMasterTable.open();
                        cropMasterTable.insertArray(cropMasterSyncResponse.crop_master);
                        cropMasterTable.close();
                    }
                    publishProgress(AllTablesName.CropMasterTable);
                    if (cropMasterSyncResponse.crop_item_master.size() > 0) {
                        CropItemMasterTable cropItemMasterTable = new CropItemMasterTable(getActivity());
                        cropItemMasterTable.open();
                        cropItemMasterTable.insertArray(cropMasterSyncResponse.crop_item_master);
                        cropItemMasterTable.close();
                    }
                    publishProgress(AllTablesName.CropItemMasterTable);
                    if (cropMasterSyncResponse.customer_master.size() > 0) {
                        CustomerMasterTable customerMasterTable = new CustomerMasterTable(getActivity());
                        customerMasterTable.open();
                        customerMasterTable.insertArray(cropMasterSyncResponse.customer_master);
                        customerMasterTable.close();
                    }
                    publishProgress(AllTablesName.CustomerMasterTable);
                }
            } catch (Exception e) {
                publishProgress(AllTablesName.CropMasterTable);
                publishProgress(AllTablesName.CropItemMasterTable);
                publishProgress(AllTablesName.CustomerMasterTable);
            }
        }

        void TravelMasterUpdate() {
            try {
                List<CityMasterTable.CityMasterModel> cityreponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCityMaster + "getCityMaster")).getJsonResponse(), new TypeToken<List<CityMasterTable.CityMasterModel>>() {
                }.getType());
                if (cityreponse.size() > 0 && cityreponse.get(0).condition) {
                    CityMasterTable cityMasterTable = new CityMasterTable(getActivity());
                    cityMasterTable.open();
                    cityMasterTable.insertArray(cityreponse);
                    cityMasterTable.close();
                }
                publishProgress(AllTablesName.CityMasterTable);

                List<ModeOfTravelMasterTable.ModeOfTravelModel> modeOfTravelreponse = new Gson().fromJson(globalPostingMethod.getHttpRequest(globalPostingMethod.createUrl(StaticDataForApp.getCityMaster + "getMode_of_travel")).getJsonResponse(), new TypeToken<List<ModeOfTravelMasterTable.ModeOfTravelModel>>() {
                }.getType());
                if (modeOfTravelreponse.size() > 0 && modeOfTravelreponse.get(0).condition) {
                    ModeOfTravelMasterTable modeOfTravelMasterTable = new ModeOfTravelMasterTable(getActivity());
                    modeOfTravelMasterTable.open();
                    modeOfTravelMasterTable.insertArray(modeOfTravelreponse);
                    modeOfTravelMasterTable.close();
                }
                publishProgress(AllTablesName.ModeOfTravelMasterTable);
            } catch (Exception e) {
                publishProgress(AllTablesName.CityMasterTable);
                publishProgress(AllTablesName.ModeOfTravelMasterTable);
            }
        }

        void bindFinalResponse(String flagOfAction) {
            if (flagOfAction.equalsIgnoreCase("updateGeographicalsetup")) {
//                if (ly_geographicsetupSection.getVisibility() == View.VISIBLE) {
//                    ly_geographicsetupSection.setVisibility(View.GONE);
//                    geographicalSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
//                }
            } else if (flagOfAction.equalsIgnoreCase("CropMasterUpdate")) {
//                if (ly_crop_setupSection.getVisibility() == View.VISIBLE) {
//                    ly_crop_setupSection.setVisibility(View.GONE);
//                    cropSetupExpandOrcollaps.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
//                }
                crop_setup_progress.setVisibility(View.GONE);
            } else if (flagOfAction.equalsIgnoreCase("TravelMasterUpdate")) {
                travel_setup_progress.setVisibility(View.GONE);
            }
            countOfAllTableData();
        }
    }
}
