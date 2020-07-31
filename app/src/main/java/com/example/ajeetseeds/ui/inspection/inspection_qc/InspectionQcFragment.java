package com.example.ajeetseeds.ui.inspection.inspection_qc;

import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.Model.inspection.InspectionOneModel;
import com.example.ajeetseeds.Model.inspection.InspectionResponse;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.ui.inspection.CropConditionAdapter;
import com.example.ajeetseeds.ui.inspection.inspection_four.InspectionFourFragment;
import com.example.ajeetseeds.ui.inspection.inspection_four.InspectionFourLineListAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InspectionQcFragment extends Fragment {

    private InspectionQcViewModel mViewModel;
    InspectionModel inspectionModel = null;
    String selected_production_lot_no = "";
    Chip chip_add_inspection_line, chip_complete_hit;
    ListView listview_headers_line;
    LoadingDialog loadingDialog = new LoadingDialog();
    TextView tv_doc_date, tv_production_lot_no, tv_Region_Code, tv_Organizer_Name, tv_District_Code, tv_Organizer_Address, tv_Organizer_Address_2,
            tv_City, tv_zone_code, tv_State_Code, tv_Taluka_Code;

    public static InspectionQcFragment newInstance() {
        return new InspectionQcFragment();
    }

    InspectionModel.Inspection_Line inspection_line;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_production_lot_no = getArguments().getString("Selected_production_lot_no", "");
            inspectionModel = new Gson().fromJson(getArguments().getString("inspection_header", ""), InspectionModel.class);
            inspection_line = new Gson().fromJson(getArguments().getString("inspection_line", ""), InspectionModel.Inspection_Line.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inspection_qc_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        initView(view);
    }

    void initView(View view) {
        chip_add_inspection_line = view.findViewById(R.id.chip_add_inspection_line);
        chip_complete_hit = view.findViewById(R.id.chip_complete_hit);
        listview_headers_line = view.findViewById(R.id.listview_headers_line);
        tv_doc_date = view.findViewById(R.id.tv_doc_date);
        tv_production_lot_no = view.findViewById(R.id.tv_production_lot_no);
        tv_Region_Code = view.findViewById(R.id.tv_Region_Code);
        tv_Organizer_Name = view.findViewById(R.id.tv_Organizer_Name);
        tv_District_Code = view.findViewById(R.id.tv_District_Code);
        tv_Organizer_Address = view.findViewById(R.id.tv_Organizer_Address);
        tv_Organizer_Address_2 = view.findViewById(R.id.tv_Organizer_Address_2);
        tv_City = view.findViewById(R.id.tv_City);
        tv_zone_code = view.findViewById(R.id.tv_zone_code);
        tv_State_Code = view.findViewById(R.id.tv_State_Code);
        tv_Taluka_Code = view.findViewById(R.id.tv_Taluka_Code);

        tv_doc_date.setText(DateUtilsCustome.getDateMMMDDYYYY(inspectionModel.create_on));
        tv_production_lot_no.setText(selected_production_lot_no);
        tv_Region_Code.setText(inspectionModel.Region_Code);
        tv_Organizer_Name.setText(inspectionModel.organizer_name);
        tv_District_Code.setText(inspectionModel.District_Code);
        tv_Organizer_Address.setText(inspectionModel.organizer_address);
        tv_Organizer_Address_2.setText(inspectionModel.organizer_address_2);
        tv_City.setText(inspectionModel.city);
        tv_zone_code.setText(inspectionModel.Zone_Code);
        tv_State_Code.setText(inspectionModel.State_Code);
        tv_Taluka_Code.setText(inspectionModel.Taluka_Code);

        if (!loadingDialog.getLoadingState()) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                    new AsyModel(StaticDataForApp.get_inspection_by_lot_arrival_plan_no +
                            inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no
                            + "&flag=INSQC", null, "getInItData"));
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
                        if (inspection_header_line.get(0).iQC.size() > 0) {
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModel(StaticDataForApp.Complete_inspection_QC
                                            + inspectionModel.arrival_plan_no
                                            + "&production_lot_no=" + selected_production_lot_no
                                            + "&email_id=" + sessionManagement.getUserEmail()
                                            + "&inspection_type=Inspection QC", null, "CompleteHit"));
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
            Add_Inspection_Line("View", inspection_header_line.get(0).iQC.get(i));
        });
        listview_headers_line.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            if (inspection_header_line.get(0).inspection_qc <= 0) {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Confirm...")
                        .setMessage("Do you really want to delete this line?")
                        .setIcon(R.drawable.approve_order_icon)
                        .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                            if (!loadingDialog.getLoadingState()) {
                                new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                        new AsyModel(StaticDataForApp.delete_inspection_QC + inspection_header_line.get(0).iQC.get(i).line_no, null, "DeleteLine"));
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

    public void Add_Inspection_Line(String flag, InspectionOneModel.InspectionQCLineModel viewModel) {
        try {
            isolation_distance_status_list.clear();
            isolation_distance_status_list.add("Maintained");
            isolation_distance_status_list.add("Non-Maintained");

            crop_condition_list.clear();
            crop_condition_list.add("Good");
            crop_condition_list.add("Medium");
            crop_condition_list.add("Poor");

            crop_stage_list.clear();
            crop_stage_list.add("Germination");
            crop_stage_list.add("Vegetative");
            crop_stage_list.add("Flag Leaf");
            crop_stage_list.add("Square Formation");
            crop_stage_list.add("Flowering");
            crop_stage_list.add("Crossing");
            crop_stage_list.add("Boll Formation");
            crop_stage_list.add("Pod Formation");
            crop_stage_list.add("Grain Formation");
            crop_stage_list.add("Fruit Formation");
            crop_stage_list.add("Maturity");
            crop_stage_list.add("Tassel");
            crop_stage_list.add("Silk");

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View PopupView = inflater.inflate(R.layout.add_inspection_qc_line_view, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(PopupView);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button submitPage = PopupView.findViewById(R.id.submitPage);
            TextInputEditText et_organizer_or_co_ordinator_name = PopupView.findViewById(R.id.et_organizer_or_co_ordinator_name);
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
            AutoCompleteTextView et_crop_condition = PopupView.findViewById(R.id.et_crop_condition);
            CropConditionAdapter cropConditionAdapter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_condition_list);
            et_crop_condition.setAdapter(cropConditionAdapter);
            AutoCompleteTextView et_crop_stage = PopupView.findViewById(R.id.et_crop_stage);
            CropConditionAdapter crop_stage_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_stage_list);
            et_crop_stage.setAdapter(crop_stage_adapeter);
            TextInputEditText et_date_of_inspection = PopupView.findViewById(R.id.et_date_of_inspection);
            TextInputEditText et_suggestion_to_grower = PopupView.findViewById(R.id.et_suggestion_to_grower);

            TextInputEditText et_avg_crossing_per_day = PopupView.findViewById(R.id.et_avg_crossing_per_day);
            TextInputEditText et_avg_cross_boll_per_plant = PopupView.findViewById(R.id.et_avg_cross_boll_per_plant);
            TextInputEditText et_self_boll_per_plant = PopupView.findViewById(R.id.et_self_boll_per_plant);
            TextInputEditText et_kapas_picking_if_any = PopupView.findViewById(R.id.et_kapas_picking_if_any);
            TextInputEditText et_approx_kapas_balance_for_picking = PopupView.findViewById(R.id.et_approx_kapas_balance_for_picking);
            TextInputEditText et_estimated_field_in_kg = PopupView.findViewById(R.id.et_estimated_field_in_kg);
            TextInputEditText et_name_of_fertilizer = PopupView.findViewById(R.id.et_name_of_fertilizer);
            TextInputEditText et_fertilizer_date = PopupView.findViewById(R.id.et_fertilizer_date);
            TextInputEditText et_fertilizer_dose = PopupView.findViewById(R.id.et_fertilizer_dose);
            TextInputEditText et_sprying_fungi_or_insecticide_date = PopupView.findViewById(R.id.et_sprying_fungi_or_insecticide_date);
            TextInputEditText et_name_of_insecticide_or_fungicide = PopupView.findViewById(R.id.et_name_of_insecticide_or_fungicide);
            TextInputEditText et_sprying_fungi_or_Insecticide_dose = PopupView.findViewById(R.id.et_sprying_fungi_or_Insecticide_dose);
            TextInputEditText et_plants_rouged_male = PopupView.findViewById(R.id.et_plants_rouged_male);
            TextInputEditText et_plants_rouged_female = PopupView.findViewById(R.id.et_plants_rouged_female);

            TextInputEditText et_previousCrop = PopupView.findViewById(R.id.et_previousCrop);
            et_previousCrop.setText(inspection_line.PreviousCrop);
            et_previousCrop.setEnabled(false);
            TextInputEditText et_givenArea = PopupView.findViewById(R.id.et_givenArea);
            et_givenArea.setText(inspection_line.GivenArea);
            et_givenArea.setEnabled(false);
            TextInputEditText et_rejection_PLDArea = PopupView.findViewById(R.id.et_rejection_PLDArea);
            TextInputEditText et_actualArea = PopupView.findViewById(R.id.et_actualArea);
            et_actualArea.setText(inspection_line.GivenArea);
            et_actualArea.setEnabled(false);
            et_rejection_PLDArea.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    float GivenArea = inspection_line.GivenArea.equalsIgnoreCase("")?0:Float.parseFloat( inspection_line.GivenArea);
                    float reject_area = !et_rejection_PLDArea.getText().toString().equalsIgnoreCase("") ? Float.parseFloat(et_rejection_PLDArea.getText().toString()) : 0;
                    float actual_area = GivenArea - reject_area;
                    if (actual_area < 0) {
                        et_rejection_PLDArea.setText("0");
                        et_actualArea.setText(inspection_line.GivenArea);
                    } else {
                        et_actualArea.setText(String.valueOf(actual_area));
                    }

                }
            });
            TextInputEditText et_crossingStartDate = PopupView.findViewById(R.id.et_crossingStartDate);
            et_crossingStartDate.setText(DateUtilsCustome.getDateMMMDDYYYY(inspection_header_line.get(0).crossing_start_date));
            et_crossingStartDate.setEnabled(false);
            TextInputEditText et_plantPopulationVariety = PopupView.findViewById(R.id.et_plantPopulationVariety);
            et_plantPopulationVariety.setText(inspection_line.PlantPopulationVariety);
            et_plantPopulationVariety.setEnabled(false);
            TextInputEditText et_plantPopulationFemale = PopupView.findViewById(R.id.et_plantPopulationFemale);
            et_plantPopulationFemale.setText(inspection_line.PlantPopulationFemale);
            et_plantPopulationFemale.setEnabled(false);
            TextInputEditText et_spacingFemaleRow = PopupView.findViewById(R.id.et_spacingFemaleRow);
            TextInputEditText et_spacingFemalePlant = PopupView.findViewById(R.id.et_spacingFemalePlant);
            TextInputEditText et_isolationDistanceinMetre = PopupView.findViewById(R.id.et_isolationDistanceinMetre);
            AutoCompleteTextView et_isolation_distance_status = PopupView.findViewById(R.id.et_isolation_distance_status);
            CropConditionAdapter isolation_distance_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, isolation_distance_status_list);
            et_isolation_distance_status.setAdapter(isolation_distance_adapeter);

            et_isolation_distance_status.setOnItemClickListener((adapterView, view, positin, l) -> {
                if (isolation_distance_status_list.get(positin).equalsIgnoreCase("Maintained"))
                    et_isolationDistanceinMetre.setEnabled(false);
                else
                    et_isolationDistanceinMetre.setEnabled(true);
            });
            TextInputEditText et_spacingVarietyRow = PopupView.findViewById(R.id.et_spacingVarietyRow);
            TextInputEditText et_spacingVarietyPlant = PopupView.findViewById(R.id.et_spacingVarietyPlant);
            TextInputEditText et_crossingEndDate = PopupView.findViewById(R.id.et_crossingEndDate);
            et_crossingEndDate.setText(DateUtilsCustome.getDateMMMDDYYYY(inspection_header_line.get(0).crossing_end_date));
            et_crossingEndDate.setEnabled(false);
            if (et_item_crop_type.getText().toString().equalsIgnoreCase("Hybrid")) {
                et_plants_rouged_male.setEnabled(true);
                et_plants_rouged_female.setEnabled(true);

                et_plantPopulationFemale.setEnabled(true);
                et_plantPopulationVariety.setEnabled(false);

                et_spacingFemaleRow.setEnabled(true);
                et_spacingVarietyRow.setEnabled(false);

                et_spacingFemalePlant.setEnabled(true);
                et_spacingVarietyPlant.setEnabled(false);
            } else {
                et_plants_rouged_male.setEnabled(false);
                et_plants_rouged_female.setEnabled(false);

                et_plantPopulationFemale.setEnabled(false);
                et_plantPopulationVariety.setEnabled(true);

                et_spacingFemaleRow.setEnabled(false);
                et_spacingVarietyRow.setEnabled(true);

                et_spacingFemalePlant.setEnabled(false);
                et_spacingVarietyPlant.setEnabled(true);
            }

            if (flag.equalsIgnoreCase("View")) {
                et_organizer_or_co_ordinator_name.setText(viewModel.organizer_or_co_ordinator_name);
                et_organizer_or_co_ordinator_name.setEnabled(false);
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
                et_crop_condition.setText(viewModel.crop_condition);
                et_crop_condition.setEnabled(false);
                et_crop_stage.setText(viewModel.crop_stage);
                et_crop_stage.setEnabled(false);
                et_date_of_inspection.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.date_of_inspection));
                et_date_of_inspection.setEnabled(false);
                et_suggestion_to_grower.setText(viewModel.suggestion_to_grower);
                et_suggestion_to_grower.setEnabled(false);
                et_avg_crossing_per_day.setText(viewModel.avg_crossing_per_day);
                et_avg_crossing_per_day.setEnabled(false);
                et_avg_cross_boll_per_plant.setText(viewModel.avg_cross_boll_per_plant);
                et_avg_cross_boll_per_plant.setEnabled(false);
                et_self_boll_per_plant.setText(viewModel.self_boll_per_plant);
                et_self_boll_per_plant.setEnabled(false);
                et_kapas_picking_if_any.setText(viewModel.kapas_picking_if_any);
                et_kapas_picking_if_any.setEnabled(false);
                et_approx_kapas_balance_for_picking.setText(viewModel.approx_kapas_balance_for_picking);
                et_approx_kapas_balance_for_picking.setEnabled(false);
                et_estimated_field_in_kg.setText(viewModel.estimated_field_in_kg);
                et_estimated_field_in_kg.setEnabled(false);
                et_name_of_fertilizer.setText(viewModel.name_of_fertilizer);
                et_name_of_fertilizer.setEnabled(false);
                et_fertilizer_date.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.fertilizer_date));
                et_fertilizer_date.setEnabled(false);
                et_fertilizer_dose.setText(viewModel.fertilizer_dose);
                et_fertilizer_dose.setEnabled(false);
                et_sprying_fungi_or_insecticide_date.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.sprying_fungi_or_insecticide_date));
                et_sprying_fungi_or_insecticide_date.setEnabled(false);
                et_name_of_insecticide_or_fungicide.setText(viewModel.name_of_insecticide_or_fungicide);
                et_name_of_insecticide_or_fungicide.setEnabled(false);
                et_sprying_fungi_or_Insecticide_dose.setText(viewModel.sprying_fungi_or_Insecticide_dose);
                et_sprying_fungi_or_Insecticide_dose.setEnabled(false);
                et_plants_rouged_male.setText(viewModel.plants_rouged_male);
                et_plants_rouged_male.setEnabled(false);
                et_plants_rouged_female.setText(viewModel.plants_rouged_female);
                et_plants_rouged_female.setEnabled(false);

                et_previousCrop.setText(viewModel.previousCrop);
                et_previousCrop.setEnabled(false);
                et_givenArea.setText(viewModel.givenArea);
                et_givenArea.setEnabled(false);
                et_rejection_PLDArea.setText(viewModel.rejection_PLDArea);
                et_rejection_PLDArea.setEnabled(false);
                et_actualArea.setText(viewModel.actualArea);
                et_actualArea.setEnabled(false);
                et_crossingStartDate.setText(viewModel.crossingStartDate);
                et_crossingStartDate.setEnabled(false);
                et_plantPopulationVariety.setText(viewModel.plantPopulationVariety);
                et_plantPopulationVariety.setEnabled(false);
                et_plantPopulationFemale.setText(viewModel.plantPopulationFemale);
                et_plantPopulationFemale.setEnabled(false);
                et_spacingFemaleRow.setText(viewModel.spacingFemaleRow);
                et_spacingFemaleRow.setEnabled(false);
                et_spacingFemalePlant.setText(viewModel.spacingFemalePlant);
                et_spacingFemalePlant.setEnabled(false);
                et_isolation_distance_status.setText(viewModel.isolationDistanceStatus);
                et_isolation_distance_status.setEnabled(false);
                et_isolationDistanceinMetre.setText(viewModel.isolationDistanceinMetre);
                et_isolationDistanceinMetre.setEnabled(false);
                et_spacingVarietyRow.setText(viewModel.spacingVarietyRow);
                et_spacingVarietyRow.setEnabled(false);
                et_spacingVarietyPlant.setText(viewModel.spacingVarietyPlant);
                et_spacingVarietyPlant.setEnabled(false);
                et_crossingEndDate.setText(viewModel.crossingEndDate);
                et_crossingEndDate.setEnabled(false);

                submitPage.setEnabled(false);
            } else {
                et_date_of_inspection.setText(DateUtilsCustome.getCurrentDateBY());
                et_fertilizer_date.setText(DateUtilsCustome.getCurrentDateBY());
                et_sprying_fungi_or_insecticide_date.setText(DateUtilsCustome.getCurrentDateBY());
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
                et_fertilizer_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_fertilizer_date.setText(picker.getHeaderText());
                                et_fertilizer_date.setError(null);
                            });
                            picker.addOnDismissListener(dialogInterface -> {
                                datedialog = false;
                            });
                        }
                    }
                    return true;
                });
                et_sprying_fungi_or_insecticide_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_sprying_fungi_or_insecticide_date.setText(picker.getHeaderText());
                                et_sprying_fungi_or_insecticide_date.setError(null);
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
                InspectionOneModel.InspectionQCLineModel postmodel = new InspectionOneModel().new InspectionQCLineModel();
                postmodel.arrival_plan_no = inspectionModel.arrival_plan_no;
                postmodel.production_lot_no = selected_production_lot_no;
                postmodel.organizer_or_co_ordinator_name = et_organizer_or_co_ordinator_name.getText().toString();
                postmodel.grower_or_land_owner_name = et_grower_or_land_owner_name.getText().toString();
                postmodel.item_no = et_item_no.getText().toString();
                postmodel.item_name = et_item_name.getText().toString();
                postmodel.crop_code = et_crop_code.getText().toString();
                postmodel.crop_name = et_crop_name.getText().toString();
                postmodel.Item_class_of_seeds = et_Item_class_of_seeds.getText().toString();
                postmodel.item_crop_type = et_item_crop_type.getText().toString();
                postmodel.crop_condition = et_crop_condition.getText().toString();
                postmodel.crop_stage = et_crop_stage.getText().toString();
                postmodel.date_of_inspection = et_date_of_inspection.getText().toString();
                postmodel.suggestion_to_grower = et_suggestion_to_grower.getText().toString();
                postmodel.avg_crossing_per_day = et_avg_crossing_per_day.getText().toString().equalsIgnoreCase("") ? "0" : et_avg_crossing_per_day.getText().toString();
                postmodel.avg_cross_boll_per_plant = et_avg_cross_boll_per_plant.getText().toString().equalsIgnoreCase("") ? "0" : et_avg_cross_boll_per_plant.getText().toString();
                postmodel.self_boll_per_plant = et_self_boll_per_plant.getText().toString();
                postmodel.kapas_picking_if_any = et_kapas_picking_if_any.getText().toString();
                postmodel.approx_kapas_balance_for_picking = et_approx_kapas_balance_for_picking.getText().toString().equalsIgnoreCase("") ? "0" : et_approx_kapas_balance_for_picking.getText().toString();
                postmodel.estimated_field_in_kg = et_estimated_field_in_kg.getText().toString().equalsIgnoreCase("") ? "0" : et_estimated_field_in_kg.getText().toString();
                postmodel.name_of_fertilizer = et_name_of_fertilizer.getText().toString();
                postmodel.fertilizer_date = et_fertilizer_date.getText().toString();
                postmodel.fertilizer_dose = et_fertilizer_dose.getText().toString();
                postmodel.sprying_fungi_or_insecticide_date = et_sprying_fungi_or_insecticide_date.getText().toString();
                postmodel.name_of_insecticide_or_fungicide = et_name_of_insecticide_or_fungicide.getText().toString();
                postmodel.sprying_fungi_or_Insecticide_dose = et_sprying_fungi_or_Insecticide_dose.getText().toString();
                postmodel.plants_rouged_male = et_plants_rouged_male.getText().toString();
                postmodel.plants_rouged_female = et_plants_rouged_female.getText().toString();

                postmodel.previousCrop = et_previousCrop.getText().toString();
                postmodel.givenArea = et_givenArea.getText().toString();
                postmodel.rejection_PLDArea = et_rejection_PLDArea.getText().toString();
                postmodel.actualArea = et_actualArea.getText().toString();
                postmodel.crossingStartDate = et_crossingStartDate.getText().toString();
                postmodel.plantPopulationVariety = et_plantPopulationVariety.getText().toString();
                postmodel.plantPopulationFemale = et_plantPopulationFemale.getText().toString();
                postmodel.spacingFemaleRow = et_spacingFemaleRow.getText().toString();
                postmodel.spacingFemalePlant = et_spacingFemalePlant.getText().toString();
                postmodel.isolationDistanceStatus = et_isolation_distance_status.getText().toString();
                ;
                postmodel.isolationDistanceinMetre = et_isolationDistanceinMetre.getText().toString();
                postmodel.spacingVarietyRow = et_spacingVarietyRow.getText().toString();
                postmodel.spacingVarietyPlant = et_spacingVarietyPlant.getText().toString();
                postmodel.crossingEndDate = et_crossingEndDate.getText().toString();
                try {
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.insert_inspection_QC_line,
                                    new JSONObject(new Gson().toJson(postmodel)), "Insert_Line"));
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
                                            + "&flag=INSQC", null, "getInItData"));
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
                        inspection_header_line.get(0).inspection_qc = 1;
                        bindUi();
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
                                        + "&flag=INSQC", null, "getInItData"));
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
        if (inspection_header_line.get(0).inspection_qc > 0) {
            chip_complete_hit.setVisibility(View.GONE);
            chip_add_inspection_line.setVisibility(View.GONE);
        } else {
            chip_complete_hit.setVisibility(View.VISIBLE);
            chip_add_inspection_line.setVisibility(View.VISIBLE);
        }
        if (inspection_header_line.get(0).iQC.get(0).arrival_plan_no != null) {
            InspectionQcLineListAdapter adapter = new InspectionQcLineListAdapter(getActivity(), inspection_header_line.get(0).iQC);
            listview_headers_line.setAdapter(adapter);
        } else {
            if (!inspection_header_line.get(0).iQC.isEmpty())
                inspection_header_line.get(0).iQC.clear();
            InspectionQcLineListAdapter adapter = new InspectionQcLineListAdapter(getActivity(), inspection_header_line.get(0).iQC);
            listview_headers_line.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InspectionQcViewModel.class);
        // TODO: Use the ViewModel
    }

}