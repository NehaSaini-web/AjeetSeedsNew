package com.example.ajeetseeds.ui.inspection.inspection_one;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.inspection.InspectionOneInsertModel;
import com.example.ajeetseeds.Model.inspection.InspectionResponse;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.Model.inspection.InspectionOneModel;
import com.example.ajeetseeds.ui.dailyActivity.addLine.DistrictAdapter;
import com.example.ajeetseeds.ui.inspection.CropConditionAdapter;
import com.example.ajeetseeds.ui.inspection.inspection_main_page.InspectionMainPageFragment;
import com.example.ajeetseeds.ui.travel.approveTravel.ApproveTravelDetailFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InspectionOneFragment extends Fragment {
    InspectionModel inspectionModel = null;
    String selected_production_lot_no = "";
    Chip chip_add_inspection_line, chip_complete_hit;
    ListView listview_headers_line;
    LoadingDialog loadingDialog = new LoadingDialog();
    TextView tv_Arrival_Plan_No, tv_production_lot_no, tv_Organizer_No, tv_Organizer_Name, tv_Organizer_Name_2, tv_Organizer_Address, tv_Organizer_Address_2,
            tv_City, tv_Contact, tv_Season_Code, tv_grower_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_production_lot_no = getArguments().getString("Selected_production_lot_no", "");
            inspectionModel = new Gson().fromJson(getArguments().getString("inspection_header", ""), InspectionModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inspection_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    void initView(View view) {
        chip_add_inspection_line = view.findViewById(R.id.chip_add_inspection_line);
        chip_complete_hit = view.findViewById(R.id.chip_complete_hit);
        listview_headers_line = view.findViewById(R.id.listview_headers_line);
        tv_Arrival_Plan_No = view.findViewById(R.id.tv_Arrival_Plan_No);
        tv_production_lot_no = view.findViewById(R.id.tv_production_lot_no);
        tv_grower_name = view.findViewById(R.id.tv_grower_name);
        tv_Organizer_No = view.findViewById(R.id.tv_Organizer_No);
        tv_Organizer_Name = view.findViewById(R.id.tv_Organizer_Name);
        tv_Organizer_Name_2 = view.findViewById(R.id.tv_Organizer_Name_2);
        tv_Organizer_Address = view.findViewById(R.id.tv_Organizer_Address);
        tv_Organizer_Address_2 = view.findViewById(R.id.tv_Organizer_Address_2);
        tv_City = view.findViewById(R.id.tv_City);
        tv_Contact = view.findViewById(R.id.tv_Contact);
        tv_Season_Code = view.findViewById(R.id.tv_Season_Code);

        tv_Arrival_Plan_No.setText(inspectionModel.arrival_plan_no);
        tv_production_lot_no.setText(selected_production_lot_no);
        tv_Organizer_No.setText(inspectionModel.organizer_no);
        tv_Organizer_Name.setText(inspectionModel.organizer_name);
        tv_Organizer_Name_2.setText(inspectionModel.organizer_name_2);
        tv_Organizer_Address.setText(inspectionModel.organizer_address);
        tv_Organizer_Address_2.setText(inspectionModel.organizer_address_2);
        tv_City.setText(inspectionModel.city);
        tv_Contact.setText(inspectionModel.contact);
        tv_Season_Code.setText(inspectionModel.season_code);
        if (!loadingDialog.getLoadingState()) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                    new AsyModel(StaticDataForApp.get_inspection_by_lot_arrival_plan_no +
                            inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no
                            + "&flag=INS1", null, "getInItData"));
        }
        chip_add_inspection_line.setOnClickListener(view1 -> {
            Add_Inspection_Line("", null);
        });
        chip_complete_hit.setOnClickListener(view1 -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Confirm...")
                    .setMessage("Do you really want to Complete This?")
                    .setIcon(R.drawable.approve_order_icon)
                    .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                        if (inspection_header_line.get(0).io.size() > 0) {
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModel(StaticDataForApp.Complete_inspection_one + inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no, null, "CompleteHit"));
                            dialogInterface.dismiss();
                        } else {
                            Snackbar.make(chip_add_inspection_line, "Please Add Minimum Single Line.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view12 -> {
                            }).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i1) -> {

                    })
                    .show();

        });
        listview_headers_line.setOnItemClickListener((adapterView, view1, i, l) -> {
            Add_Inspection_Line("View", inspection_header_line.get(0).io.get(i));
        });
        listview_headers_line.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            if (inspection_header_line.get(0).inspection_1 <= 0) {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Confirm...")
                        .setMessage("Do you really want to delete this line?")
                        .setIcon(R.drawable.approve_order_icon)
                        .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                            if (!loadingDialog.getLoadingState()) {
                                new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                        new AsyModel(StaticDataForApp.delete_inspection_one + inspection_header_line.get(0).io.get(i).line_no, null, "DeleteLine"));
                            }
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i1) -> {

                        })
                        .show();
            }

            return true;
        });
    }

    boolean datedialog = false;
    public List<String> crop_condition_list = new ArrayList<>();
    public List<String> crop_stage_list = new ArrayList<>();
    public List<String> isolation_distance_status_list = new ArrayList<>();
    public List<String> germination_status_list = new ArrayList<>();

    public void Add_Inspection_Line(String flag, InspectionOneModel.InspectionLineModel viewModel) {
        try {
            isolation_distance_status_list.clear();
            isolation_distance_status_list.add("Maintained");
            isolation_distance_status_list.add("Non-Maintained");

            germination_status_list.clear();
            germination_status_list.add("Satisfactory");
            germination_status_list.add("Non-Satisfactory");

            crop_condition_list.clear();
            crop_condition_list.add("Good");
            crop_condition_list.add("Medium");
            crop_condition_list.add("Poor");

            crop_stage_list.clear();
            crop_stage_list.add("Germination");
            crop_stage_list.add("Vegetative");
            crop_stage_list.add("Flag-Off");
            crop_stage_list.add("Square-Formation");
            crop_stage_list.add("Flowering");
            crop_stage_list.add("Boll Formation");
            crop_stage_list.add("Crossing");
            crop_stage_list.add("Maturity");
            crop_stage_list.add("Pod Formation");
            crop_stage_list.add("Grain Formation");
            crop_stage_list.add("Fruit Formation");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View PopupView = inflater.inflate(R.layout.add_inspection_one_line_view, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(PopupView);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button submitPage = PopupView.findViewById(R.id.submitPage);
            TextInputEditText et_grower_or_land_owner_name = PopupView.findViewById(R.id.et_grower_or_land_owner_name);
            et_grower_or_land_owner_name.setText(inspection_header_line.get(0).grower_name);
            et_grower_or_land_owner_name.setEnabled(false);
            TextInputEditText et_item_no = PopupView.findViewById(R.id.et_item_no);
            et_item_no.setText(inspection_header_line.get(0).item_no);
            et_item_no.setEnabled(false);
            TextInputEditText et_item_name = PopupView.findViewById(R.id.et_item_name);
            et_item_name.setText(inspection_header_line.get(0).item_name);
            et_item_name.setEnabled(false);
            TextInputEditText et_crop_code = PopupView.findViewById(R.id.et_crop_code);
            et_crop_code.setText(inspection_header_line.get(0).crop_code);
            et_crop_code.setEnabled(false);
            TextInputEditText et_crop_name = PopupView.findViewById(R.id.et_crop_name);
            et_crop_name.setText(inspection_header_line.get(0).item_crop);
            et_crop_name.setEnabled(false);
            TextInputEditText et_Item_class_of_seeds = PopupView.findViewById(R.id.et_Item_class_of_seeds);
            et_Item_class_of_seeds.setText(inspection_header_line.get(0).itemclassofseeds);
            et_Item_class_of_seeds.setEnabled(false);
            TextInputEditText et_item_crop_type = PopupView.findViewById(R.id.et_item_crop_type);
            et_item_crop_type.setText(inspection_header_line.get(0).item_croptype);
            et_item_crop_type.setEnabled(false);
            TextInputEditText et_date_of_inspection = PopupView.findViewById(R.id.et_date_of_inspection);
            et_date_of_inspection.setText(DateUtilsCustome.getCurrentDateBY());
            AutoCompleteTextView et_isolation_distance_status = PopupView.findViewById(R.id.et_isolation_distance_status);
            CropConditionAdapter isolation_distance_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, isolation_distance_status_list);
            et_isolation_distance_status.setAdapter(isolation_distance_adapeter);
            TextInputEditText et_isolation_distance_in_metre = PopupView.findViewById(R.id.et_isolation_distance_in_metre);
            TextInputEditText et_previous_crop = PopupView.findViewById(R.id.et_previous_crop);
            AutoCompleteTextView et_germination_status = PopupView.findViewById(R.id.et_germination_status);
            CropConditionAdapter germination_status_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, germination_status_list);
            et_germination_status.setAdapter(germination_status_adapeter);
            TextInputEditText et_germination_per = PopupView.findViewById(R.id.et_germination_per);
            TextInputEditText et_area = PopupView.findViewById(R.id.et_area);
            TextInputEditText et_rejection_area = PopupView.findViewById(R.id.et_rejection_area);
            TextInputEditText et_net_area = PopupView.findViewById(R.id.et_net_area);
            TextInputEditText et_spacing_variety = PopupView.findViewById(R.id.et_spacing_variety);
            TextInputEditText et_spacing_male = PopupView.findViewById(R.id.et_spacing_male);
            TextInputEditText et_spacing_female = PopupView.findViewById(R.id.et_spacing_female);
            TextInputEditText et_plant_population_variety = PopupView.findViewById(R.id.et_plant_population_variety);
            TextInputEditText et_plant_population_male = PopupView.findViewById(R.id.et_plant_population_male);
            TextInputEditText et_plant_population_female = PopupView.findViewById(R.id.et_plant_population_female);
            AutoCompleteTextView et_crop_condition = PopupView.findViewById(R.id.et_crop_condition);
            CropConditionAdapter cropConditionAdapter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_condition_list);
            et_crop_condition.setAdapter(cropConditionAdapter);
            AutoCompleteTextView et_crop_stage = PopupView.findViewById(R.id.et_crop_stage);
            CropConditionAdapter crop_stage_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_stage_list);
            et_crop_stage.setAdapter(crop_stage_adapeter);
            TextInputEditText et_suggestion_to_grower = PopupView.findViewById(R.id.et_suggestion_to_grower);
            TextInputEditText et_planting_sowing_date_female = PopupView.findViewById(R.id.et_planting_sowing_date_female);
            et_planting_sowing_date_female.setText(DateUtilsCustome.getCurrentDateBY());
            TextInputEditText et_planting_sowing_date_other = PopupView.findViewById(R.id.et_planting_sowing_date_other);
            et_planting_sowing_date_other.setText(DateUtilsCustome.getCurrentDateBY());
            TextInputEditText et_spacing_female_row = PopupView.findViewById(R.id.et_spacing_female_row);
            TextInputEditText et_spacing_female_plant = PopupView.findViewById(R.id.et_spacing_female_plant);
            TextInputEditText et_spacing_variety_row = PopupView.findViewById(R.id.et_spacing_variety_row);
            TextInputEditText et_spacing_variety_plant = PopupView.findViewById(R.id.et_spacing_variety_plant);
            TextInputEditText et_spacing_male_row = PopupView.findViewById(R.id.et_spacing_male_row);
            TextInputEditText et_spacing_male_plant = PopupView.findViewById(R.id.et_spacing_male_plant);
            if (flag.equalsIgnoreCase("View")) {
                et_grower_or_land_owner_name.setText(viewModel.grower_or_land_owner_name);
                et_grower_or_land_owner_name.setEnabled(false);
                et_item_no.setText(viewModel.item_no);
                et_item_no.setEnabled(false);
                et_item_name.setText(viewModel.item_name);
                et_item_name.setEnabled(false);
                et_crop_code.setText(viewModel.crop_code);
                et_crop_code.setEnabled(false);
                et_crop_name.setText(viewModel.crop_name);
                et_crop_name.setEnabled(false);
                et_Item_class_of_seeds.setText(viewModel.Item_class_of_seeds);
                et_Item_class_of_seeds.setEnabled(false);
                et_item_crop_type.setText(viewModel.item_crop_type);
                et_item_crop_type.setEnabled(false);
                et_date_of_inspection.setText(viewModel.date_of_inspection);
                et_date_of_inspection.setEnabled(false);
                et_isolation_distance_status.setText(viewModel.isolation_distance_status);
                et_isolation_distance_status.setEnabled(false);
                et_isolation_distance_in_metre.setText(viewModel.isolation_distance_in_metre);
                et_isolation_distance_in_metre.setEnabled(false);
                et_previous_crop.setText(viewModel.previous_crop);
                et_previous_crop.setEnabled(false);
                et_germination_status.setText(viewModel.germination_status);
                et_germination_status.setEnabled(false);
                et_germination_per.setText(viewModel.germination_per);
                et_germination_per.setEnabled(false);
                et_area.setText(viewModel.area);
                et_area.setEnabled(false);
                et_rejection_area.setText(viewModel.rejection_area);
                et_rejection_area.setEnabled(false);
                et_net_area.setText(viewModel.net_area);
                et_net_area.setEnabled(false);
                et_spacing_variety.setText(viewModel.spacing_variety);
                et_spacing_variety.setEnabled(false);
                et_spacing_male.setText(viewModel.spacing_male);
                et_spacing_male.setEnabled(false);
                et_spacing_female.setText(viewModel.spacing_female);
                et_spacing_female.setEnabled(false);
                et_plant_population_variety.setText(viewModel.plant_population_variety);
                et_plant_population_variety.setEnabled(false);
                et_plant_population_male.setText(viewModel.plant_population_male);
                et_plant_population_male.setEnabled(false);
                et_plant_population_female.setText(viewModel.plant_population_female);
                et_plant_population_female.setEnabled(false);
                et_crop_condition.setText(viewModel.crop_condition);
                et_crop_condition.setEnabled(false);
                et_crop_stage.setText(viewModel.crop_stage);
                et_crop_stage.setEnabled(false);
                et_suggestion_to_grower.setText(viewModel.suggestion_to_grower);
                et_suggestion_to_grower.setEnabled(false);
                submitPage.setEnabled(false);
                et_planting_sowing_date_female.setText(viewModel.planting_sowing_date_female);
                et_planting_sowing_date_female.setEnabled(false);
                et_planting_sowing_date_other.setText(viewModel.planting_sowing_date_other);
                et_planting_sowing_date_other.setEnabled(false);
                et_spacing_female_row.setText(viewModel.spacing_female_row);
                et_spacing_female_row.setEnabled(false);
                et_spacing_female_plant.setText(viewModel.spacing_female_plant);
                et_spacing_female_plant.setEnabled(false);
                et_spacing_variety_row.setText(viewModel.spacing_variety_row);
                et_spacing_variety_row.setEnabled(false);
                et_spacing_variety_plant.setText(viewModel.spacing_variety_plant);
                et_spacing_variety_plant.setEnabled(false);
                et_spacing_male_row.setText(viewModel.spacing_male_row);
                et_spacing_female_row.setEnabled(false);
                et_spacing_male_plant.setText(viewModel.spacing_male_plant);
                et_spacing_male_plant.setEnabled(false);
            } else {
                et_date_of_inspection.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_date_of_inspection.setText(picker.getHeaderText());
                                et_date_of_inspection.setError(null);
                            });
                            picker.addOnDismissListener(dialogInterface -> {
                                datedialog = false;
                            });
                        }
                    }
                    return true;
                });
                et_planting_sowing_date_female.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_planting_sowing_date_female.setText(picker.getHeaderText());
                                et_planting_sowing_date_female.setError(null);
                            });
                            picker.addOnDismissListener(dialogInterface -> {
                                datedialog = false;
                            });
                        }
                    }
                    return true;
                });
                et_planting_sowing_date_other.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_planting_sowing_date_other.setText(picker.getHeaderText());
                                et_planting_sowing_date_other.setError(null);
                            });
                            picker.addOnDismissListener(dialogInterface -> {
                                datedialog = false;
                            });
                        }
                    }
                    return true;
                });
            }


            submitPage.setOnClickListener(view -> {
                InspectionOneModel.InspectionLineModel oneInsertModel = new InspectionOneModel().new InspectionLineModel();
                oneInsertModel.arrival_plan_no = inspectionModel.arrival_plan_no;
                oneInsertModel.production_lot_no = selected_production_lot_no;
                oneInsertModel.grower_or_land_owner_name = et_grower_or_land_owner_name.getText().toString();
                oneInsertModel.item_no = et_item_no.getText().toString();
                oneInsertModel.item_name = et_item_name.getText().toString();
                oneInsertModel.crop_code = et_crop_code.getText().toString();
                oneInsertModel.crop_name = et_crop_name.getText().toString();
                oneInsertModel.Item_class_of_seeds = et_Item_class_of_seeds.getText().toString();
                oneInsertModel.item_crop_type = et_item_crop_type.getText().toString();
                oneInsertModel.date_of_inspection = et_date_of_inspection.getText().toString();
                oneInsertModel.isolation_distance_status = et_isolation_distance_status.getText().toString();
                oneInsertModel.isolation_distance_in_metre = et_isolation_distance_in_metre.getText().toString().equalsIgnoreCase("") ? "0" : et_isolation_distance_in_metre.getText().toString();
                oneInsertModel.previous_crop = et_previous_crop.getText().toString();
                oneInsertModel.germination_status = et_germination_status.getText().toString();
                oneInsertModel.germination_per = et_germination_per.getText().toString().equalsIgnoreCase("") ? "0" : et_germination_per.getText().toString();
                oneInsertModel.area = et_area.getText().toString().equalsIgnoreCase("") ? "0" : et_area.getText().toString();
                oneInsertModel.rejection_area = et_rejection_area.getText().toString().equalsIgnoreCase("") ? "0" : et_rejection_area.getText().toString();
                oneInsertModel.net_area = et_net_area.getText().toString().equalsIgnoreCase("") ? "0" : et_net_area.getText().toString();
                oneInsertModel.spacing_variety = et_spacing_variety.getText().toString().equalsIgnoreCase("") ? "0" : et_spacing_variety.getText().toString();
                oneInsertModel.spacing_male = et_spacing_male.getText().toString().equalsIgnoreCase("") ? "0" : et_spacing_male.getText().toString();
                oneInsertModel.spacing_female = et_spacing_female.getText().toString().equalsIgnoreCase("") ? "0" : et_spacing_female.getText().toString();
                oneInsertModel.plant_population_variety = et_plant_population_variety.getText().toString().equalsIgnoreCase("") ? "0" : et_plant_population_variety.getText().toString();
                oneInsertModel.plant_population_male = et_plant_population_male.getText().toString().equalsIgnoreCase("") ? "0" : et_plant_population_male.getText().toString();
                oneInsertModel.plant_population_female = et_plant_population_female.getText().toString().equalsIgnoreCase("") ? "0" : et_plant_population_female.getText().toString();
                oneInsertModel.crop_condition = et_crop_condition.getText().toString();
                oneInsertModel.crop_stage = et_crop_stage.getText().toString();
                oneInsertModel.suggestion_to_grower = et_suggestion_to_grower.getText().toString();

                oneInsertModel.planting_sowing_date_female = et_planting_sowing_date_female.getText().toString();
                oneInsertModel.planting_sowing_date_other = et_planting_sowing_date_other.getText().toString();
                oneInsertModel.spacing_female_row = et_spacing_female_row.getText().toString();
                oneInsertModel.spacing_female_plant = et_spacing_female_plant.getText().toString();
                oneInsertModel.spacing_variety_row = et_spacing_variety_row.getText().toString();
                oneInsertModel.spacing_variety_plant = et_spacing_variety_plant.getText().toString();
                oneInsertModel.spacing_male_row = et_spacing_male_row.getText().toString();
                oneInsertModel.spacing_male_plant = et_spacing_male_plant.getText().toString();
                try {
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.insert_inspection_one_line,
                                    new JSONObject(new Gson().toJson(oneInsertModel)), "Insert_Line"));
                } catch (Exception e) {
                }

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
            bindResponse(result, flagOfAction);
        }
    }

    List<InspectionOneModel> inspection_header_line = new ArrayList<>();

    void bindResponse(HttpHandlerModel result, String flagOfAction) {
        try {
            if (result.isConnectStatus() && !result.getJsonResponse().equalsIgnoreCase("")) {
                if (flagOfAction.equalsIgnoreCase("getInItData")) {
                    List<InspectionOneModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<InspectionOneModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        inspection_header_line = responseslist;
                        bindUi();
                    } else {
                        Snackbar.make(chip_add_inspection_line, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                } else if (flagOfAction.equalsIgnoreCase("Insert_Line")) {
                    List<InspectionResponse> responseslist = new Gson().fromJson(result.getJsonResponse(),
                            new TypeToken<List<InspectionResponse>>() {
                            }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        loadingDialog.dismissLoading();
                        if (!loadingDialog.getLoadingState()) {
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModel(StaticDataForApp.get_inspection_by_lot_arrival_plan_no +
                                            inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no
                                            + "&flag=INS1", null, "getInItData"));
                        }
                    } else {
                        Snackbar.make(chip_add_inspection_line, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                } else if (flagOfAction.equalsIgnoreCase("CompleteHit")) {
                    List<InspectionResponse> responseslist = new Gson().fromJson(result.getJsonResponse(),
                            new TypeToken<List<InspectionResponse>>() {
                            }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        inspection_header_line.get(0).inspection_1 = 1;
                        bindUi();
                        InspectionMainPageFragment.viewPager.setCurrentItem(1);
                    } else {
                        Snackbar.make(chip_add_inspection_line, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                } else if (flagOfAction.equalsIgnoreCase("DeleteLine")) {
                    loadingDialog.dismissLoading();
                    if (!loadingDialog.getLoadingState()) {
                        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                new AsyModel(StaticDataForApp.get_inspection_by_lot_arrival_plan_no +
                                        inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no
                                        + "&flag=INS1", null, "getInItData"));
                    }
                }

            } else {
                Snackbar.make(chip_add_inspection_line, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!flagOfAction.equalsIgnoreCase("Insert_Line") || !flagOfAction.equalsIgnoreCase("DeleteLine"))
                loadingDialog.dismissLoading();
        }
    }


    public void bindUi() {
        tv_grower_name.setText(inspection_header_line.get(0).grower_name);
        if (inspection_header_line.get(0).inspection_1 > 0) {
            chip_complete_hit.setVisibility(View.GONE);
            chip_add_inspection_line.setVisibility(View.GONE);
        } else {
            chip_complete_hit.setVisibility(View.VISIBLE);
            chip_add_inspection_line.setVisibility(View.VISIBLE);
        }
        if (inspection_header_line.get(0).io.get(0).arrival_plan_no != null) {
            InspectionOneLineListAdapter adapter = new InspectionOneLineListAdapter(getActivity(), inspection_header_line.get(0).io);
            listview_headers_line.setAdapter(adapter);
        } else {
            if (!inspection_header_line.get(0).io.isEmpty())
                inspection_header_line.get(0).io.clear();
            InspectionOneLineListAdapter adapter = new InspectionOneLineListAdapter(getActivity(), inspection_header_line.get(0).io);
            listview_headers_line.setAdapter(adapter);
        }
    }

}