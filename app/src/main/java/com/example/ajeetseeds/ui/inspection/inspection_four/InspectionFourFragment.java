package com.example.ajeetseeds.ui.inspection.inspection_four;

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
import com.example.ajeetseeds.globalconfirmation.CustomeDatePicker;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.golobalClass.StaticMethods;
import com.example.ajeetseeds.ui.inspection.CropConditionAdapter;
import com.example.ajeetseeds.ui.inspection.inspection_three.InspectionThreeFragment;
import com.example.ajeetseeds.ui.inspection.inspection_three.InspectionThreeLineListAdapter;
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

public class InspectionFourFragment extends Fragment {

    private InspectionFourViewModel mViewModel;
    InspectionModel inspectionModel = null;
    String selected_production_lot_no = "";
    Chip chip_add_inspection_line, chip_complete_hit;
    ListView listview_headers_line;
    LoadingDialog loadingDialog = new LoadingDialog();
    TextView tv_doc_date, tv_production_lot_no, tv_Region_Code, tv_Organizer_Name, tv_District_Code, tv_Organizer_Address, tv_Organizer_Address_2,
            tv_City, tv_zone_code, tv_State_Code, tv_Taluka_Code;
    InspectionModel.Inspection_Line inspectionModel_selected_line;

    public static InspectionFourFragment newInstance() {
        return new InspectionFourFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_production_lot_no = getArguments().getString("Selected_production_lot_no", "");
            inspectionModel = new Gson().fromJson(getArguments().getString("inspection_header", ""), InspectionModel.class);
            inspectionModel_selected_line = new Gson().fromJson(getArguments().getString("inspection_line", ""), InspectionModel.Inspection_Line.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inspection_four_fragment, container, false);
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
                            + "&flag=INS4", null, "getInItData"));
        }
        chip_add_inspection_line.setOnClickListener(view1 -> {
            if (inspection_header_line.get(0).inspection_1 > 0 && inspection_header_line.get(0).inspection_2 > 0 && inspection_header_line.get(0).inspection_3 > 0) {
                Add_Inspection_Line("", null);
            } else {
                Snackbar.make(chip_add_inspection_line, "Please Complete Previous Inspection ", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view12 -> {
                }).show();
            }
        });
        chip_complete_hit.setOnClickListener(view1 -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Confirm...")
                    .setMessage("Do you really want to Complete This?")
                    .setIcon(R.drawable.approve_order_icon)
                    .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                        if (inspection_header_line.get(0).ifour.size() > 0) {
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModel(StaticDataForApp.Complete_inspection_four
                                            + inspectionModel.arrival_plan_no
                                            + "&production_lot_no=" + selected_production_lot_no
                                            + "&email_id=" + sessionManagement.getUserEmail()
                                            + "&inspection_type=Inspection IV", null, "CompleteHit"));
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
            Add_Inspection_Line("View", inspection_header_line.get(0).ifour.get(i));
        });
        listview_headers_line.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            if (inspection_header_line.get(0).inspection_4 <= 0) {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Confirm...")
                        .setMessage("Do you really want to delete this line?")
                        .setIcon(R.drawable.approve_order_icon)
                        .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                            if (!loadingDialog.getLoadingState()) {
                                new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                        new AsyModel(StaticDataForApp.delete_inspection_four + inspection_header_line.get(0).ifour.get(i).line_no, null, "DeleteLine"));
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

    public void Add_Inspection_Line(String flag, InspectionOneModel.InspectionFourLineModel viewModel) {
        try {
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
            View PopupView = inflater.inflate(R.layout.add_inspection_four_line_view, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(PopupView);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button submitPage = PopupView.findViewById(R.id.submitPage);
            TextInputEditText et_grower_or_land_owner_name = PopupView.findViewById(R.id.et_grower_or_land_owner_name);
            et_grower_or_land_owner_name.setText(inspection_header_line.get(0).grower_name);
            et_grower_or_land_owner_name.setEnabled(false);
            TextInputEditText et_grower_village = PopupView.findViewById(R.id.et_grower_village);
            et_grower_village.setText(inspectionModel_selected_line.Grower_Village);
            et_grower_village.setEnabled(false);
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

            TextInputEditText et_planting_sowing_date_female = PopupView.findViewById(R.id.et_planting_sowing_date_female);
            et_planting_sowing_date_female.setText(inspectionModel_selected_line.SowingDateFemale);
            et_planting_sowing_date_female.setEnabled(false);

            TextInputEditText et_planting_sowing_date_other = PopupView.findViewById(R.id.et_planting_sowing_date_other);
            et_planting_sowing_date_other.setText(inspectionModel_selected_line.SowingDateOther);
            et_planting_sowing_date_other.setEnabled(false);

            AutoCompleteTextView et_crop_condition = PopupView.findViewById(R.id.et_crop_condition);
            CropConditionAdapter cropConditionAdapter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_condition_list);
            et_crop_condition.setAdapter(cropConditionAdapter);
            AutoCompleteTextView et_crop_stage = PopupView.findViewById(R.id.et_crop_stage);
            CropConditionAdapter crop_stage_adapeter = new CropConditionAdapter(getContext(), R.layout.drop_down_textview, crop_stage_list);
            et_crop_stage.setAdapter(crop_stage_adapeter);
            TextInputEditText et_net_area_as_per_insp_3 = PopupView.findViewById(R.id.et_net_area_as_per_insp_3);
            et_net_area_as_per_insp_3.setText(inspection_header_line.get(0).net_area_as_per_insp_3);
            et_net_area_as_per_insp_3.setEnabled(false);
            TextInputEditText et_crossing_start_date = PopupView.findViewById(R.id.et_crossing_start_date);
            TextInputEditText et_crossing_end_date = PopupView.findViewById(R.id.et_crossing_end_date);
            if (inspection_header_line.get(0).crossing_start_date != null && !inspection_header_line.get(0).crossing_start_date.equalsIgnoreCase("")) {
                et_crossing_start_date.setText(inspection_header_line.get(0).crossing_start_date);
                et_crossing_start_date.setEnabled(false);
            }
            if (inspection_header_line.get(0).crossing_end_date != null && !inspection_header_line.get(0).crossing_end_date.equalsIgnoreCase("")) {
                et_crossing_end_date.setText(inspection_header_line.get(0).crossing_end_date);
                //et_crossing_end_date.setEnabled(false);
            }

            TextInputEditText et_final_plant_population = PopupView.findViewById(R.id.et_final_plant_population);
            et_final_plant_population.setText(inspection_header_line.get(0).final_plant_population);
//            et_final_plant_population.setEnabled(false);

            TextInputEditText et_avg_cross_boll_per_plant = PopupView.findViewById(R.id.et_avg_cross_boll_per_plant);
            TextInputEditText et_kapas_picking_if_any = PopupView.findViewById(R.id.et_kapas_picking_if_any);
            TextInputEditText et_approx_kapas_balance_for_picking = PopupView.findViewById(R.id.et_approx_kapas_balance_for_picking);
            et_approx_kapas_balance_for_picking.setEnabled(false);
            et_avg_cross_boll_per_plant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        float avg_cross_boll_per_plant = et_avg_cross_boll_per_plant.getText().toString().equalsIgnoreCase("") ? 0 : (Float.parseFloat(et_avg_cross_boll_per_plant.getText().toString()));
                        float approx_kapas_balance_for_picking = avg_cross_boll_per_plant *
                                (et_final_plant_population.getText().toString().equalsIgnoreCase("") ? 0 : Float.parseFloat(et_final_plant_population.getText().toString())) *
                                ((Float) Float.parseFloat(inspectionModel_selected_line.item_weight) / 1000);
                        et_approx_kapas_balance_for_picking.setText(String.valueOf(StaticMethods.removeDecimal(approx_kapas_balance_for_picking)));
                    } catch (Exception e) {
                        et_approx_kapas_balance_for_picking.setText("0");
                    }
                }
            });
            et_final_plant_population.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        float avg_cross_boll_per_plant = et_avg_cross_boll_per_plant.getText().toString().equalsIgnoreCase("") ? 0 : (Float.parseFloat(et_avg_cross_boll_per_plant.getText().toString()));
                        float approx_kapas_balance_for_picking = avg_cross_boll_per_plant *
                                (et_final_plant_population.getText().toString().equalsIgnoreCase("") ? 0 : Float.parseFloat(et_final_plant_population.getText().toString())) *
                                ((Float) Float.parseFloat(inspectionModel_selected_line.item_weight) / 1000);
                        et_approx_kapas_balance_for_picking.setText(String.valueOf(StaticMethods.removeDecimal(approx_kapas_balance_for_picking)));
                    } catch (Exception e) {
                        et_approx_kapas_balance_for_picking.setText("0");
                    }
                }
            });
            TextInputEditText et_estimated_field_in_kg = PopupView.findViewById(R.id.et_estimated_field_in_kg);
            et_estimated_field_in_kg.setText("0");
            //et_estimated_field_in_kg.setEnabled(false);
            et_kapas_picking_if_any.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if ((inspection_header_line.get(0).crop_code.equalsIgnoreCase("CT"))) {
                            double kapas_picking_if_any = !et_kapas_picking_if_any.getText().toString().equalsIgnoreCase("") ? Float.parseFloat(et_kapas_picking_if_any.getText().toString()) : 0;
                            double approx_kapas_balance_for_picking = !et_approx_kapas_balance_for_picking.getText().toString().equalsIgnoreCase("") ? Float.parseFloat(et_approx_kapas_balance_for_picking.getText().toString()) : 0;
                            double estimated_field_in_kg = kapas_picking_if_any + approx_kapas_balance_for_picking;
                            et_estimated_field_in_kg.setText(String.valueOf(StaticMethods.removeDecimalKG(estimated_field_in_kg)));
                        }
                    } catch (Exception e) {
                        et_estimated_field_in_kg.setText("0");
                    }
                }
            });
            et_approx_kapas_balance_for_picking.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if ((inspection_header_line.get(0).crop_code.equalsIgnoreCase("CT"))) {
                            double kapas_picking_if_any = !et_kapas_picking_if_any.getText().toString().equalsIgnoreCase("") ? Float.parseFloat(et_kapas_picking_if_any.getText().toString()) : 0;
                            double approx_kapas_balance_for_picking = !et_approx_kapas_balance_for_picking.getText().toString().equalsIgnoreCase("") ? Float.parseFloat(et_approx_kapas_balance_for_picking.getText().toString()) : 0;
                            double estimated_field_in_kg = kapas_picking_if_any + approx_kapas_balance_for_picking;
                            et_estimated_field_in_kg.setText(String.valueOf(StaticMethods.removeDecimalKG(estimated_field_in_kg)));
                        }
                    } catch (Exception e) {
                        et_estimated_field_in_kg.setText("0");
                    }
                }
            });
            TextInputEditText et_other_specific_observations = PopupView.findViewById(R.id.et_other_specific_observations);
            TextInputEditText et_suggestion_to_grower = PopupView.findViewById(R.id.et_suggestion_to_grower);
            TextInputEditText et_HarvestingDateMale = PopupView.findViewById(R.id.et_HarvestingDateMale);
            TextInputEditText et_HarvestingDateFemale = PopupView.findViewById(R.id.et_HarvestingDateFemale);
            TextInputEditText et_HarvestingDateOther = PopupView.findViewById(R.id.et_HarvestingDateOther);
            if (et_item_crop_type.getText().toString().equalsIgnoreCase("Hybrid")) {
                et_HarvestingDateMale.setEnabled(true);
                et_HarvestingDateFemale.setEnabled(true);
                et_HarvestingDateOther.setEnabled(false);
            } else {
                et_HarvestingDateMale.setEnabled(false);
                et_HarvestingDateFemale.setEnabled(false);
                et_HarvestingDateOther.setEnabled(true);
            }

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
                et_date_of_inspection.setText(DateUtilsCustome.getDateYYYYMMDD(viewModel.date_of_inspection));
                et_date_of_inspection.setEnabled(false);
                et_crop_condition.setText(viewModel.crop_condition);
                et_crop_condition.setEnabled(false);
                et_crop_stage.setText(viewModel.crop_stage);
                et_crop_stage.setEnabled(false);
                et_net_area_as_per_insp_3.setText(viewModel.net_area_as_per_insp_3);
                et_net_area_as_per_insp_3.setEnabled(false);
                et_crossing_start_date.setText(viewModel.crossing_start_date);
                et_crossing_start_date.setEnabled(false);
                et_crossing_end_date.setText(viewModel.crossing_end_date);
                et_crossing_end_date.setEnabled(false);
                et_final_plant_population.setText(viewModel.final_plant_population);
                et_final_plant_population.setEnabled(false);
                et_avg_cross_boll_per_plant.setText(viewModel.avg_cross_boll_per_plant);
                et_avg_cross_boll_per_plant.setEnabled(false);
                et_kapas_picking_if_any.setText(viewModel.kapas_picking_if_any);
                et_kapas_picking_if_any.setEnabled(false);
                et_approx_kapas_balance_for_picking.setText(viewModel.approx_kapas_balance_for_picking);
                et_approx_kapas_balance_for_picking.setEnabled(false);
                et_estimated_field_in_kg.setText(viewModel.estimated_field_in_kg);
                et_estimated_field_in_kg.setEnabled(false);
                et_other_specific_observations.setText(viewModel.other_specific_observations);
                et_other_specific_observations.setEnabled(false);
                et_suggestion_to_grower.setText(viewModel.suggestion_to_grower);
                et_suggestion_to_grower.setEnabled(false);
                et_HarvestingDateMale.setText(viewModel.harvestingDateMale);
                et_HarvestingDateMale.setEnabled(false);
                et_HarvestingDateFemale.setText(viewModel.harvestingDateFemale);
                et_HarvestingDateFemale.setEnabled(false);
                et_HarvestingDateOther.setText(viewModel.harvestingDateOther);
                et_HarvestingDateOther.setEnabled(false);
                submitPage.setEnabled(false);
            } else {
                et_date_of_inspection.setText(DateUtilsCustome.getCurrentDateBY_());
                et_date_of_inspection.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_date_of_inspection);
                    }
                    return true;
                });

                et_crossing_start_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_crossing_start_date);
                    }
                    return true;
                });

                et_crossing_end_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_crossing_end_date);
                    }
                    return true;
                });
                et_HarvestingDateMale.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_HarvestingDateMale);
                    }
                    return true;
                });
                et_HarvestingDateFemale.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_HarvestingDateFemale);
                    }
                    return true;
                });
                et_HarvestingDateOther.setOnTouchListener((view1, motionEvent) -> {
                    if (!CustomeDatePicker.datedialog) {
                        new CustomeDatePicker(getActivity()).displayDate(et_HarvestingDateOther);
                    }
                    return true;
                });
            }


            submitPage.setOnClickListener(view -> {
                InspectionOneModel.InspectionFourLineModel postmodel = new InspectionOneModel().new InspectionFourLineModel();
                postmodel.arrival_plan_no = inspectionModel.arrival_plan_no;
                postmodel.production_lot_no = selected_production_lot_no;
                postmodel.grower_or_land_owner_name = et_grower_or_land_owner_name.getText().toString();
                postmodel.item_no = et_item_no.getText().toString();
                postmodel.item_name = et_item_name.getText().toString();
                postmodel.crop_code = et_crop_code.getText().toString();
                postmodel.crop_name = et_crop_name.getText().toString();
                postmodel.Item_class_of_seeds = et_Item_class_of_seeds.getText().toString();
                postmodel.item_crop_type = et_item_crop_type.getText().toString();
                postmodel.date_of_inspection = et_date_of_inspection.getText().toString();
                postmodel.crop_condition = et_crop_condition.getText().toString();
                postmodel.crop_stage = et_crop_stage.getText().toString();
                postmodel.net_area_as_per_insp_3 = et_net_area_as_per_insp_3.getText().toString().equalsIgnoreCase("") ? "0" : et_net_area_as_per_insp_3.getText().toString();
                postmodel.crossing_start_date = et_crossing_start_date.getText().toString();
                postmodel.crossing_end_date = et_crossing_end_date.getText().toString();
                postmodel.final_plant_population = et_final_plant_population.getText().toString();
                postmodel.avg_cross_boll_per_plant = et_avg_cross_boll_per_plant.getText().toString();
                postmodel.kapas_picking_if_any = et_kapas_picking_if_any.getText().toString();
                postmodel.approx_kapas_balance_for_picking = et_approx_kapas_balance_for_picking.getText().toString();
                postmodel.estimated_field_in_kg = et_estimated_field_in_kg.getText().toString();
                postmodel.other_specific_observations = et_other_specific_observations.getText().toString();
                postmodel.suggestion_to_grower = et_suggestion_to_grower.getText().toString();
                postmodel.harvestingDateMale = et_HarvestingDateMale.getText().toString();
                postmodel.harvestingDateFemale = et_HarvestingDateFemale.getText().toString();
                postmodel.harvestingDateOther = et_HarvestingDateOther.getText().toString();

                try {
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.insert_inspection_four_line,
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
                                            + "&flag=INS4", null, "getInItData"));
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
                        inspection_header_line.get(0).inspection_4 = 1;
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
                                        + "&flag=INS4", null, "getInItData"));
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

        if (inspection_header_line.get(0).inspection_4 > 0) {
            chip_complete_hit.setVisibility(View.GONE);
            chip_add_inspection_line.setVisibility(View.GONE);
        } else {
            chip_complete_hit.setVisibility(View.VISIBLE);
            chip_add_inspection_line.setVisibility(View.VISIBLE);
        }
        if (inspection_header_line.get(0).ifour.get(0).arrival_plan_no != null) {
            InspectionFourLineListAdapter adapter = new InspectionFourLineListAdapter(getActivity(), inspection_header_line.get(0).ifour);
            listview_headers_line.setAdapter(adapter);
        } else {
            if (!inspection_header_line.get(0).ifour.isEmpty())
                inspection_header_line.get(0).ifour.clear();
            InspectionFourLineListAdapter adapter = new InspectionFourLineListAdapter(getActivity(), inspection_header_line.get(0).ifour);
            listview_headers_line.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InspectionFourViewModel.class);
        // TODO: Use the ViewModel
    }

}