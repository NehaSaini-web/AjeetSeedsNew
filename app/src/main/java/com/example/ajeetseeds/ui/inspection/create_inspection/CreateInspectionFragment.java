package com.example.ajeetseeds.ui.inspection.create_inspection;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.inspection.InspectionModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.inspection.LocationModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.ui.eventManagement.createEvent.EventTypeAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CreateInspectionFragment extends Fragment {

    private CreateInspectionViewModel mViewModel;
    Chip chip_production_lot_no_submit;
    TextInputEditText edit_production_lot_no;
    LinearLayout header_layout, footer_screen, line_list_ly;
    ListView listview_headers_line;
    LoadingDialog loadingDialog = new LoadingDialog();
    ImageView go_back_screen;
    TextView tv_Arrival_Plan_No, tv_Organizer_No, tv_Organizer_Name, tv_Organizer_Name_2, tv_Organizer_Address, tv_Organizer_Address_2,
            tv_City, tv_Contact, tv_Season_Code;
    String entered_lot_no = "";
    AutoCompleteTextView dropdown_location;

    public static CreateInspectionFragment newInstance() {
        return new CreateInspectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_inspection_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        initView(view);
        chip_production_lot_no_submit.setOnClickListener(view1 -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view1.getApplicationWindowToken(), 0);
            entered_lot_no = edit_production_lot_no.getText().toString();
            if (entered_lot_no.equalsIgnoreCase("")) {
                edit_production_lot_no.setError("Enter Production Lot no.");
                return;
            } else if (selected_Location == null) {
                dropdown_location.setError("Please Select Location.");
                return;
            } else if (!dropdown_location.getText().toString().contains(selected_Location.location_code)) {
                dropdown_location.setError("Please Select Location Again.");
                return;
            }
                try{
                    if (!loadingDialog.getLoadingState()) {
                    JSONObject postedJson=new JSONObject();
                    postedJson.put("production_lot_no",entered_lot_no);
                    postedJson.put("location_code",selected_Location.location_code);
                    postedJson.put("location_name",selected_Location.location_name);
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.scanProductionLotNo , postedJson, "ScanProductionLotNo"));
                    }
                }catch (Exception e){}
        });
        go_back_screen.setOnClickListener(view1 -> {
            header_layout.setVisibility(View.VISIBLE);
            footer_screen.setVisibility(View.GONE);
            line_list_ly.setVisibility(View.GONE);
        });

        listview_headers_line.setOnItemClickListener((adapterView, view1, i, l) -> {
            SelectInspectionTypePopup(i);
//            Bundle bundle = new Bundle();
//            bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
//            bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(i).production_lot_no);
//            loadFragments(R.id.nav_inspection_main_page, "Inspection Main", bundle);
        });
        if (inspection_header_line.size() > 0) {
            bind_Ui();

        }
    }

    public class InspectionDataModel {
        public String value;
        public boolean is_selected;

        public InspectionDataModel(String value, boolean is_selected) {
            this.value = value;
            this.is_selected = is_selected;
        }
    }

    List<InspectionDataModel> inspection_array_list = new ArrayList<>();
    String selected_inspection_type = "";

    void SelectInspectionTypePopup(int selected_position) {
        selected_inspection_type = "";
        if ((inspection_header_line.get(0).il.get(selected_position).crop_code.equalsIgnoreCase("FCROP") || inspection_header_line.get(0).il.get(selected_position).crop_code.equalsIgnoreCase("VEG")) && inspection_header_line.get(0).il.get(selected_position).item_croptype.equalsIgnoreCase("Improved")) {
            inspection_array_list.clear();
            inspection_array_list.add(new InspectionDataModel("Inspection One", false));
            inspection_array_list.add(new InspectionDataModel("Inspection Four", false));
            inspection_array_list.add(new InspectionDataModel("Inspection QC", false));
        } else {
            inspection_array_list.clear();
            inspection_array_list.add(new InspectionDataModel("Inspection One", false));
            inspection_array_list.add(new InspectionDataModel("Inspection Two", false));
            inspection_array_list.add(new InspectionDataModel("Inspection Three", false));
            inspection_array_list.add(new InspectionDataModel("Inspection Four", false));
            inspection_array_list.add(new InspectionDataModel("Inspection QC", false));
        }
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Select Inspection Type... ");
        builder.setIcon(R.drawable.approve_order_icon);
        ScrollView ScrollViewparent = new ScrollView(getActivity());
        LinearLayout.LayoutParams scrollparentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        ScrollViewparent.setLayoutParams(scrollparentVerticalParams);

        LinearLayout parentVertical = new LinearLayout(getActivity());
        LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        parentVertical.setLayoutParams(parentVerticalParams);
        parentVertical.setOrientation(LinearLayout.VERTICAL);

        BindPopupdata(builder, parentVertical, inspection_array_list, selected_position);

        // parentVertical.setPadding(40, 20, 40, 10);

        ScrollViewparent.addView(parentVertical);

        builder.setView(ScrollViewparent);
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            selected_inspection_type = "";
            for (int j = 0; j < inspection_array_list.size(); j++) {
                if (inspection_array_list.get(j).is_selected) {
                    selected_inspection_type = inspection_array_list.get(j).value;
                }
            }
            //todo redirect to another page
           try{
                if (!loadingDialog.getLoadingState()) {
                    JSONObject postedJson=new JSONObject();
                    postedJson.put("production_lot_no",inspection_header_line.get(0).il.get(selected_position).production_lot_no);
                    postedJson.put("location_code",inspection_header_line.get(0).il.get(selected_position).location_code);
                    postedJson.put("location_name",inspection_header_line.get(0).il.get(selected_position).location_name);
                    new CommanHitToServer(selected_position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.scanProductionLotNo , postedJson, "RedirectIt"));
                }
            }catch (Exception e){}
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });
        builder.setCancelable(false);
        builder.show();
    }

    void redirect_ToAnother_Page(int selected_position) {
        if (selected_inspection_type.equalsIgnoreCase("Inspection One")) {
            Bundle bundle = new Bundle();
            bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
            bundle.putString("inspection_line", new Gson().toJson(inspection_header_line.get(0).il.get(selected_position)));
            bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(selected_position).production_lot_no);
            loadFragments(R.id.nav_inspection_one, "Inspection One "+inspection_header_line.get(0).il.get(selected_position).location_name, bundle);
        } else if (selected_inspection_type.equalsIgnoreCase("Inspection Two")) {
            if (inspection_header_line.get(0).il.get(selected_position).inspection_1 > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
                bundle.putString("inspection_line", new Gson().toJson(inspection_header_line.get(0).il.get(selected_position)));
                bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(selected_position).production_lot_no);
                loadFragments(R.id.nav_inspection_two, "Inspection Two "+inspection_header_line.get(0).il.get(selected_position).location_name, bundle);
            } else {
                Snackbar.make(listview_headers_line, "Please Submit Inspection One.", Snackbar.LENGTH_LONG).show();
            }
        } else if (selected_inspection_type.equalsIgnoreCase("Inspection Three")) {
            if (inspection_header_line.get(0).il.get(selected_position).inspection_1 > 0 && inspection_header_line.get(0).il.get(selected_position).inspection_2 > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
                bundle.putString("inspection_line", new Gson().toJson(inspection_header_line.get(0).il.get(selected_position)));
                bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(selected_position).production_lot_no);
                loadFragments(R.id.nav_inspection_three, "Inspection Three "+inspection_header_line.get(0).il.get(selected_position).location_name, bundle);
            } else {
                Snackbar.make(listview_headers_line, "Please Submit Previous Inspection.", Snackbar.LENGTH_LONG).show();
            }
        } else if (selected_inspection_type.equalsIgnoreCase("Inspection Four")) {
            if (inspection_header_line.get(0).il.get(selected_position).inspection_1 > 0 && inspection_header_line.get(0).il.get(selected_position).inspection_2 > 0 && inspection_header_line.get(0).il.get(selected_position).inspection_3 > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
                bundle.putString("inspection_line", new Gson().toJson(inspection_header_line.get(0).il.get(selected_position)));
                bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(selected_position).production_lot_no);
                loadFragments(R.id.nav_inspection_four, "Inspection Four "+inspection_header_line.get(0).il.get(selected_position).location_name, bundle);
            } else {
                Snackbar.make(listview_headers_line, "Please Submit Previous Inspection.", Snackbar.LENGTH_LONG).show();
            }
        } else if (selected_inspection_type.equalsIgnoreCase("Inspection QC")) {
            if (inspection_header_line.get(0).il.get(selected_position).inspection_1 > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("inspection_header", new Gson().toJson(inspection_header_line.get(0)));
                bundle.putString("inspection_line", new Gson().toJson(inspection_header_line.get(0).il.get(selected_position)));
                bundle.putString("Selected_production_lot_no", inspection_header_line.get(0).il.get(selected_position).production_lot_no);
                loadFragments(R.id.nav_inspection_Qc, "Inspection QC "+inspection_header_line.get(0).il.get(selected_position).location_name, bundle);
            } else {
                Snackbar.make(listview_headers_line, "Please Submit Inspection One Then Click On QC.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    void BindPopupdata(MaterialAlertDialogBuilder builder, LinearLayout parentVertical, List<InspectionDataModel> size_choose, int selected_position) {
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        for (int i = 0; i < size_choose.size(); i++) {
            final InspectionDataModel sizedata = size_choose.get(i);
            TextView messageSession = new TextView(getActivity());
            if (sizedata.value.equalsIgnoreCase("Inspection One") && inspection_header_line.get(0).il.get(selected_position).inspection_1 == 1) {
                messageSession.setText(sizedata.value + " (Completed)");
            } else if (sizedata.value.equalsIgnoreCase("Inspection Two") && inspection_header_line.get(0).il.get(selected_position).inspection_2 == 1) {
                messageSession.setText(sizedata.value + " (Completed)");
            } else if (sizedata.value.equalsIgnoreCase("Inspection Three") && inspection_header_line.get(0).il.get(selected_position).inspection_3 == 1) {
                messageSession.setText(sizedata.value + " (Completed)");
            } else if (sizedata.value.equalsIgnoreCase("Inspection Four") && inspection_header_line.get(0).il.get(selected_position).inspection_4 == 1) {
                messageSession.setText(sizedata.value + " (Completed)");
            } else if (sizedata.value.equalsIgnoreCase("Inspection QC") && inspection_header_line.get(0).il.get(selected_position).inspection_qc == 1) {
                messageSession.setText(sizedata.value + " (Completed)");
            } else {
                TypedValue outValue = new TypedValue();
                getActivity().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                messageSession.setBackgroundResource(outValue.resourceId);
                messageSession.setText(sizedata.value);
            }
//            messageSession.setTypeface(messageSession.getTypeface(), Typeface.BOLD);
            messageSession.setOnClickListener(v -> {
                for (int j = 0; j < size_choose.size(); j++) {
                    if (size_choose.get(j).value.equalsIgnoreCase(sizedata.value)) {
                        size_choose.get(j).is_selected = !size_choose.get(j).is_selected;
                        if (size_choose.get(j).is_selected)
                            selected_inspection_type = size_choose.get(j).value;
                    } else {
                        size_choose.get(j).is_selected = false;
                    }
                }
                parentVertical.removeAllViews();
                BindPopupdata(builder, parentVertical, size_choose, selected_position);
            });
            View horizontaldevider = new View(getActivity());
            int deviderhight = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1,
                    getResources().getDisplayMetrics()
            );
            horizontaldevider.setBackgroundColor(Color.parseColor("#72AAAAAA"));
            LinearLayout.LayoutParams devide_params = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, deviderhight);
            horizontaldevider.setLayoutParams(devide_params);

            ChipGroup selected_cub_size_chipgroup = new ChipGroup(getActivity());
            LinearLayout.LayoutParams chipgoupparams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            selected_cub_size_chipgroup.setLayoutParams(chipgoupparams);
            selected_cub_size_chipgroup.removeAllViews();

            //todo arrow up down
            if (sizedata.is_selected) {
                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_baseline_check_24);
                DrawableCompat.setTint(img, Color.RED);
                messageSession.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                messageSession.setTextColor(Color.RED);
                messageSession.setTypeface(messageSession.getTypeface(), Typeface.BOLD);
                horizontaldevider.setVisibility(View.VISIBLE);
                selected_cub_size_chipgroup.setVisibility(View.VISIBLE);
            } else {
                messageSession.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                selected_cub_size_chipgroup.setVisibility(View.GONE);
            }

            messageSession.setCompoundDrawablePadding(paddingDp);
            messageSession.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            selected_cub_size_chipgroup.setPadding(paddingDp, 0, 0, 0);
            parentVertical.addView(messageSession);
            parentVertical.addView(selected_cub_size_chipgroup);
            parentVertical.addView(horizontaldevider);
        }
    }


    private void loadFragments(int id, String fragmentName, Bundle bundle) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        navController.navigateUp();
        navController.navigate(id, bundle);
        getActivity().setTitle(fragmentName);
    }

    void initView(View view) {
        chip_production_lot_no_submit = view.findViewById(R.id.chip_production_lot_no_submit);
        edit_production_lot_no = view.findViewById(R.id.edit_production_lot_no);
        header_layout = view.findViewById(R.id.header_layout);
        footer_screen = view.findViewById(R.id.footer_screen);
        listview_headers_line = view.findViewById(R.id.listview_headers_line);
        line_list_ly = view.findViewById(R.id.line_list_ly);
        go_back_screen = view.findViewById(R.id.go_back_screen);

        tv_Arrival_Plan_No = view.findViewById(R.id.tv_Arrival_Plan_No);
        tv_Organizer_No = view.findViewById(R.id.tv_Organizer_No);
        tv_Organizer_Name = view.findViewById(R.id.tv_Organizer_Name);
        tv_Organizer_Name_2 = view.findViewById(R.id.tv_Organizer_Name_2);
        tv_Organizer_Address = view.findViewById(R.id.tv_Organizer_Address);
        tv_Organizer_Address_2 = view.findViewById(R.id.tv_Organizer_Address_2);
        tv_City = view.findViewById(R.id.tv_City);
        tv_Contact = view.findViewById(R.id.tv_Contact);
        tv_Season_Code = view.findViewById(R.id.tv_Season_Code);
        dropdown_location = view.findViewById(R.id.dropdown_location);
        if (!loadingDialog.getLoadingState()&& entered_lot_no.equalsIgnoreCase("")) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.GetUserLocationList + sessionManagement.getUserEmail(), null, "GetLocationList"));
        }else if(location_list.size()>0){
            LocationAdapter fruitAdapter = new LocationAdapter(getActivity(), R.layout.drop_down_textview, location_list);
            dropdown_location.setAdapter(fruitAdapter);
        }
        dropdown_location.setOnItemClickListener((adapterView, view1, i, l) -> {
            selected_Location = location_list.get(i);
            dropdown_location.setError(null);
        });
    }

    LocationModel selected_Location = null;
    List<LocationModel> location_list = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateInspectionViewModel.class);
        // TODO: Use the ViewModel
    }

    private class CommanHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;
        private int selected_position;

        public CommanHitToServer() {
        }

        public CommanHitToServer(int selected_position) {
            this.selected_position = selected_position;
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
            bindResponse(result, flagOfAction, selected_position);
        }
    }

    List<InspectionModel> inspection_header_line = new ArrayList<>();

    void bindResponse(HttpHandlerModel result, String flagOfAction, int selected_position) {
        try {
            if (result.isConnectStatus() && !result.getJsonResponse().equalsIgnoreCase("")) {
                if (flagOfAction.equalsIgnoreCase("ScanProductionLotNo")) {
                    List<InspectionModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<InspectionModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        inspection_header_line = responseslist;
                        bind_Ui();
                    } else {
                        Snackbar.make(chip_production_lot_no_submit, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
                if (flagOfAction.equalsIgnoreCase("RedirectIt")) {
                    List<InspectionModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<InspectionModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        inspection_header_line = responseslist;
                        redirect_ToAnother_Page(selected_position);
                    } else {
                        Snackbar.make(chip_production_lot_no_submit, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
                if (flagOfAction.equalsIgnoreCase("GetLocationList")) {
                    List<LocationModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<LocationModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        location_list = responseslist;
                        LocationAdapter fruitAdapter = new LocationAdapter(getActivity(), R.layout.drop_down_textview, location_list);
                        dropdown_location.setAdapter(fruitAdapter);
                    } else {
                        Snackbar.make(chip_production_lot_no_submit, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
            } else {
                Snackbar.make(chip_production_lot_no_submit, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bind_Ui() {
        tv_Arrival_Plan_No.setText(inspection_header_line.get(0).arrival_plan_no);
        tv_Organizer_No.setText(inspection_header_line.get(0).organizer_no);
        tv_Organizer_Name.setText(inspection_header_line.get(0).organizer_name);
        tv_Organizer_Name_2.setText(inspection_header_line.get(0).organizer_name_2);
        tv_Organizer_Address.setText(inspection_header_line.get(0).organizer_address);
        tv_Organizer_Address_2.setText(inspection_header_line.get(0).organizer_address_2);
        tv_City.setText(inspection_header_line.get(0).city);
        tv_Contact.setText(inspection_header_line.get(0).contact);
        tv_Season_Code.setText(inspection_header_line.get(0).season_code);
        header_layout.setVisibility(View.GONE);
        footer_screen.setVisibility(View.VISIBLE);
        line_list_ly.setVisibility(View.VISIBLE);
        InspectionLineListAdapter adapter = new InspectionLineListAdapter(getActivity(), inspection_header_line.get(0).il, entered_lot_no);
        listview_headers_line.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!entered_lot_no.equalsIgnoreCase("") && !loadingDialog.getLoadingState()){
            try{
                    JSONObject postedJson=new JSONObject();
                    postedJson.put("production_lot_no",entered_lot_no);
                    postedJson.put("location_code",selected_Location.location_code);
                    postedJson.put("location_name",selected_Location.location_name);
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            new AsyModel(StaticDataForApp.scanProductionLotNo , postedJson, "ScanProductionLotNo"));
            }catch (Exception e){}
        }
        getActivity().setTitle("Inspection");
    }
}
