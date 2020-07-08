package com.example.ajeetseeds.ui.inspection.inspection_three;

import androidx.lifecycle.ViewModelProviders;

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
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.DateUtilsCustome;
import com.example.ajeetseeds.ui.inspection.inspection_main_page.InspectionMainPageFragment;
import com.example.ajeetseeds.ui.inspection.inspectiontwo.InspectionTwoFragment;
import com.example.ajeetseeds.ui.inspection.inspectiontwo.InspectionTwoLineListAdapter;
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

public class InspectionThreeFragment extends Fragment {

    private InspectionThreeViewModel mViewModel;
    InspectionModel inspectionModel = null;
    String selected_production_lot_no = "";
    Chip chip_add_inspection_line, chip_complete_hit;
    ListView listview_headers_line;
    LoadingDialog loadingDialog = new LoadingDialog();
    TextView tv_Arrival_Plan_No, tv_Organizer_No, tv_Organizer_Name, tv_Organizer_Name_2, tv_Organizer_Address, tv_Organizer_Address_2,
            tv_City, tv_Contact, tv_Season_Code,tv_production_lot_no,tv_grower_name;

    public static InspectionThreeFragment newInstance() {
        return new InspectionThreeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_production_lot_no = getArguments().getString("Selected_production_lot_no", "");
            inspectionModel = new Gson().fromJson(getArguments().getString("inspection_header", ""), InspectionModel.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inspection_three_fragment, container, false);
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
        tv_Organizer_No = view.findViewById(R.id.tv_Organizer_No);
        tv_Organizer_Name = view.findViewById(R.id.tv_Organizer_Name);
        tv_Organizer_Name_2 = view.findViewById(R.id.tv_Organizer_Name_2);
        tv_Organizer_Address = view.findViewById(R.id.tv_Organizer_Address);
        tv_Organizer_Address_2 = view.findViewById(R.id.tv_Organizer_Address_2);
        tv_City = view.findViewById(R.id.tv_City);
        tv_Contact = view.findViewById(R.id.tv_Contact);
        tv_Season_Code = view.findViewById(R.id.tv_Season_Code);
        tv_production_lot_no=view.findViewById(R.id.tv_production_lot_no);
        tv_grower_name=view.findViewById(R.id.tv_grower_name);
        tv_production_lot_no.setText(selected_production_lot_no);

        tv_Arrival_Plan_No.setText(inspectionModel.arrival_plan_no);
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
                            + "&flag=INS3", null, "getInItData"));
        }
        chip_add_inspection_line.setOnClickListener(view1 -> {
            if (inspection_header_line.get(0).inspection_1>0 && inspection_header_line.get(0).inspection_2 > 0) {
                Add_Inspection_Line("", null);
            }else{
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
                        if (inspection_header_line.get(0).ithree.size() > 0) {
                            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                    new AsyModel(StaticDataForApp.Complete_inspection_three + inspectionModel.arrival_plan_no + "&production_lot_no=" + selected_production_lot_no, null, "CompleteHit"));
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
            Add_Inspection_Line("View", inspection_header_line.get(0).ithree.get(i));
        });
        listview_headers_line.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            if (inspection_header_line.get(0).inspection_3 <= 0) {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Confirm...")
                        .setMessage("Do you really want to delete this line?")
                        .setIcon(R.drawable.approve_order_icon)
                        .setPositiveButton("Confirm", (dialogInterface, i1) -> {
                            if (!loadingDialog.getLoadingState()) {
                                new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                                        new AsyModel(StaticDataForApp.delete_inspection_three + inspection_header_line.get(0).ithree.get(i).line_no, null, "DeleteLine"));
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

    public void Add_Inspection_Line(String flag, InspectionOneModel.InspectionThreeLineModel viewModel) {
        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View PopupView = inflater.inflate(R.layout.add_inspection_three_line_view, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            dialog.setContentView(PopupView);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button submitPage = PopupView.findViewById(R.id.submitPage);

            TextInputEditText et_grower_or_land_owner_name=  PopupView.findViewById(R.id.et_grower_or_land_owner_name);
            et_grower_or_land_owner_name.setText(inspection_header_line.get(0).grower_name);
            et_grower_or_land_owner_name.setEnabled(false);
            TextInputEditText et_item_no =PopupView.findViewById(R.id.et_item_no);
            et_item_no.setText(inspection_header_line.get(0).item_no);
            et_item_no.setEnabled(false);
            TextInputEditText et_item_name= PopupView.findViewById(R.id.et_item_name);
            et_item_name.setText(inspection_header_line.get(0).item_name);
            et_item_name.setEnabled(false);
            TextInputEditText et_crop_code= PopupView.findViewById(R.id.et_crop_code);
            et_crop_code.setText(inspection_header_line.get(0).crop_code);
            et_crop_code.setEnabled(false);
            TextInputEditText et_crop_name= PopupView.findViewById(R.id.et_crop_name);
            et_crop_name.setText(inspection_header_line.get(0).item_crop);
            et_crop_name.setEnabled(false);
            TextInputEditText et_Item_class_of_seeds= PopupView.findViewById(R.id.et_Item_class_of_seeds);
            et_Item_class_of_seeds.setText(inspection_header_line.get(0).itemclassofseeds);
            et_Item_class_of_seeds.setEnabled(false);
            TextInputEditText et_item_crop_type= PopupView.findViewById(R.id.et_item_crop_type);
            et_item_crop_type.setText(inspection_header_line.get(0).item_croptype);
            et_item_crop_type.setEnabled(false);
            TextInputEditText et_date_of_inspection = PopupView.findViewById(R.id.et_date_of_inspection);
            TextInputEditText et_crop_condition = PopupView.findViewById(R.id.et_crop_condition);
            TextInputEditText et_crop_stage = PopupView.findViewById(R.id.et_crop_stage);
            TextInputEditText et_net_area_as_per_insp2 = PopupView.findViewById(R.id.et_net_area_as_per_insp2);
            TextInputEditText et_not_cross_area = PopupView.findViewById(R.id.et_not_cross_area);
            TextInputEditText et_net_cross_area = PopupView.findViewById(R.id.et_net_cross_area);
            TextInputEditText et_crossing_start_date = PopupView.findViewById(R.id.et_crossing_start_date);
            TextInputEditText et_avg_crossing_per_day = PopupView.findViewById(R.id.et_avg_crossing_per_day);
            TextInputEditText et_self_boll_per_plant = PopupView.findViewById(R.id.et_self_boll_per_plant);
            TextInputEditText et_crossing_end_date = PopupView.findViewById(R.id.et_crossing_end_date);
            TextInputEditText et_kapas_picking_if_any = PopupView.findViewById(R.id.et_kapas_picking_if_any);
            TextInputEditText et_name_of_fertilizer = PopupView.findViewById(R.id.et_name_of_fertilizer);
            TextInputEditText et_fertilizer_date = PopupView.findViewById(R.id.et_fertilizer_date);
            TextInputEditText et_fertilizer_dose = PopupView.findViewById(R.id.et_fertilizer_dose);
            TextInputEditText et_sprying_fungi_or_insecticide_date = PopupView.findViewById(R.id.et_sprying_fungi_or_insecticide_date);
            TextInputEditText et_name_of_insecticide_or_fungicide = PopupView.findViewById(R.id.et_name_of_insecticide_or_fungicide);
            TextInputEditText et_sprying_fungi_or_insecticide_dose = PopupView.findViewById(R.id.et_sprying_fungi_or_insecticide_dose);
            TextInputEditText et_other_specific_observations = PopupView.findViewById(R.id.et_other_specific_observations);
            TextInputEditText et_suggestion_to_grower = PopupView.findViewById(R.id.et_suggestion_to_grower);

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
                et_date_of_inspection.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.date_of_inspection));
                et_date_of_inspection.setEnabled(false);
                et_crop_condition.setText(viewModel.crop_condition);
                et_crop_condition.setEnabled(false);
                et_crop_stage.setText(viewModel.crop_stage);
                et_crop_stage.setEnabled(false);
                et_net_area_as_per_insp2.setText(viewModel.net_area_as_per_insp2);
                et_net_area_as_per_insp2.setEnabled(false);
                et_not_cross_area.setText(viewModel.not_cross_area);
                et_not_cross_area.setEnabled(false);
                et_net_cross_area.setText(viewModel.net_cross_area);
                et_net_cross_area.setEnabled(false);
                et_crossing_start_date.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.crossing_start_date));
                et_crossing_start_date.setEnabled(false);
                et_avg_crossing_per_day.setText(viewModel.avg_crossing_per_day);
                et_avg_crossing_per_day.setEnabled(false);
                et_self_boll_per_plant.setText(viewModel.self_boll_per_plant);
                et_self_boll_per_plant.setEnabled(false);
                et_crossing_end_date.setText(DateUtilsCustome.getDateMMMDDYYYY(viewModel.crossing_end_date));
                et_crossing_end_date.setEnabled(false);
                et_kapas_picking_if_any.setText(viewModel.kapas_picking_if_any);
                et_kapas_picking_if_any.setEnabled(false);
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
                et_sprying_fungi_or_insecticide_dose.setText(viewModel.sprying_fungi_or_insecticide_dose);
                et_sprying_fungi_or_insecticide_dose.setEnabled(false);
                et_other_specific_observations.setText(viewModel.other_specific_observations);
                et_other_specific_observations.setEnabled(false);
                et_suggestion_to_grower.setText(viewModel.suggestion_to_grower);
                et_suggestion_to_grower.setEnabled(false);
                submitPage.setEnabled(false);
            } else {
                et_date_of_inspection.setText(DateUtilsCustome.getCurrentDateBY());
                et_crossing_start_date.setText(DateUtilsCustome.getCurrentDateBY());
                et_crossing_end_date.setText(DateUtilsCustome.getCurrentDateBY());
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
                et_crossing_start_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_crossing_start_date.setText(picker.getHeaderText());
                                et_crossing_start_date.setError(null);
                            });
                            picker.addOnDismissListener(dialogInterface -> {
                                datedialog = false;
                            });
                        }
                    }
                    return true;
                });
                et_crossing_end_date.setOnTouchListener((view1, motionEvent) -> {
                    if (!datedialog) {
                        datedialog = true;
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker picker = builder.build();
                        if (!picker.isVisible()) {
                            picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                et_crossing_end_date.setText(picker.getHeaderText());
                                et_crossing_end_date.setError(null);
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
                InspectionOneModel.InspectionThreeLineModel postmodel = new InspectionOneModel().new InspectionThreeLineModel();
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
                postmodel.net_area_as_per_insp2 = et_net_area_as_per_insp2.getText().toString().equalsIgnoreCase("")?"0":et_net_area_as_per_insp2.getText().toString();
                postmodel.not_cross_area = et_not_cross_area.getText().toString().equalsIgnoreCase("")?"0":et_not_cross_area.getText().toString();
                postmodel.net_cross_area = et_net_cross_area.getText().toString().equalsIgnoreCase("")?"0":et_net_cross_area.getText().toString();
                postmodel.crossing_start_date = et_crossing_start_date.getText().toString();
                postmodel.avg_crossing_per_day = et_avg_crossing_per_day.getText().toString().equalsIgnoreCase("")?"0": et_avg_crossing_per_day.getText().toString();
                postmodel.self_boll_per_plant = et_self_boll_per_plant.getText().toString();
                postmodel.crossing_end_date = et_crossing_end_date.getText().toString();
                postmodel.kapas_picking_if_any = et_kapas_picking_if_any.getText().toString();
                postmodel.name_of_fertilizer = et_name_of_fertilizer.getText().toString();
                postmodel.fertilizer_date = et_fertilizer_date.getText().toString();
                postmodel.fertilizer_dose = et_fertilizer_dose.getText().toString();
                postmodel.sprying_fungi_or_insecticide_date = et_sprying_fungi_or_insecticide_date.getText().toString();
                postmodel.name_of_insecticide_or_fungicide = et_name_of_insecticide_or_fungicide.getText().toString();
                postmodel.sprying_fungi_or_insecticide_dose = et_sprying_fungi_or_insecticide_dose.getText().toString();
                postmodel.other_specific_observations = et_other_specific_observations.getText().toString();
                postmodel.suggestion_to_grower = et_suggestion_to_grower.getText().toString();

                try {
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.insert_inspection_three_line,
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
                                            + "&flag=INS3", null, "getInItData"));
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
                        inspection_header_line.get(0).inspection_3 = 1;
                        bindUi();
                        InspectionMainPageFragment.viewPager.setCurrentItem(3);
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
                                        + "&flag=INS3", null, "getInItData"));
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
        if (inspection_header_line.get(0).inspection_3 > 0) {
            chip_complete_hit.setVisibility(View.GONE);
            chip_add_inspection_line.setVisibility(View.GONE);
        } else {
            chip_complete_hit.setVisibility(View.VISIBLE);
            chip_add_inspection_line.setVisibility(View.VISIBLE);
        }
        if (inspection_header_line.get(0).ithree.get(0).arrival_plan_no != null) {
            InspectionThreeLineListAdapter adapter = new InspectionThreeLineListAdapter(getActivity(), inspection_header_line.get(0).ithree);
            listview_headers_line.setAdapter(adapter);
        } else {
            if (!inspection_header_line.get(0).ithree.isEmpty())
                inspection_header_line.get(0).ithree.clear();
            InspectionThreeLineListAdapter adapter = new InspectionThreeLineListAdapter(getActivity(), inspection_header_line.get(0).ithree);
            listview_headers_line.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InspectionThreeViewModel.class);
        // TODO: Use the ViewModel
    }

}