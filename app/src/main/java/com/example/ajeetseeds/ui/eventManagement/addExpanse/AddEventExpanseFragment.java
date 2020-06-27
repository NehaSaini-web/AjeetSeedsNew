package com.example.ajeetseeds.ui.eventManagement.addExpanse;

import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Http_Hanler.MultipartUtility;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.golobalClass.FilePath;
import com.example.ajeetseeds.sqlLite.event.EventManagementExpenseLineTable;
import com.example.ajeetseeds.sqlLite.event.EventManagementTable;
import com.example.ajeetseeds.sqlLite.event.EventTypeMaster;
import com.example.ajeetseeds.sqlLite.image.ImageMasterTable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddEventExpanseFragment extends Fragment {
    int PICK_IMAGE_MULTIPLE = 1;
    private AddEventExpanseViewModel mViewModel;
    AutoCompleteTextView dropdown_event_type;
    TextInputEditText et_quantity, et_unitCost;
    Button addExpenseLine, submitPage, selectImages;
    ListView event_List;
    TextView totalAmount;
    LinearLayout expenses_add_layout, eventImageSection;

    public static AddEventExpanseFragment newInstance() {
        return new AddEventExpanseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_event_expanse_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddEventExpanseViewModel.class);
        // TODO: Use the ViewModel
    }

    EventManagementTable.EventManagemantModel submiteEventData = null;
    List<EventTypeMaster.EventTypeMasterModel> expenseTypeList = new ArrayList<>();
    EventTypeMaster.EventTypeMasterModel selectedExpenseType = null;
    List<EventManagementExpenseLineTable.EventManagementExpenseLineModel> userInsertedExpenseList = new ArrayList<>();
    int line_no = 1;
    EventExpenseListAdapter event_expense_Adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dropdown_event_type = view.findViewById(R.id.dropdown_event_type);
        et_quantity = view.findViewById(R.id.et_quantity);
        et_unitCost = view.findViewById(R.id.et_unitCost);
        addExpenseLine = view.findViewById(R.id.addExpenseLine);
        submitPage = view.findViewById(R.id.submitPage);
        event_List = view.findViewById(R.id.event_List);
        totalAmount = view.findViewById(R.id.totalAmount);
        selectImages = view.findViewById(R.id.selectImages);
        expenses_add_layout = view.findViewById(R.id.expenses_add_layout);
        eventImageSection = view.findViewById(R.id.eventImageSection);
        try {
            submiteEventData = new Gson().fromJson(getArguments().getString("dataPass", ""), EventManagementTable.EventManagemantModel.class);
        } catch (Exception e) {
        }
        if (submiteEventData.status.equalsIgnoreCase("CREATE APPROVED")) {
            expenses_add_layout.setVisibility(View.VISIBLE);
            eventImageSection.setVisibility(View.GONE);
            EventTypeMaster eventTypeMaster = new EventTypeMaster(getActivity());
            eventTypeMaster.open();
            expenseTypeList = eventTypeMaster.fetchChield(eventTypeMaster.getParentId(submiteEventData.event_type));
            eventTypeMaster.close();
            EventTypeChieldAdapter fruitAdapter = new EventTypeChieldAdapter(getActivity(), R.layout.drop_down_textview, expenseTypeList);
            dropdown_event_type.setAdapter(fruitAdapter);
            dropdown_event_type.setOnItemClickListener((adapterView, view1, i, l) -> {
                selectedExpenseType = expenseTypeList.get(i);
            });
            addExpenseLine.setOnClickListener(view1 -> {
                if (dropdown_event_type.getText().toString().equalsIgnoreCase("")) {
                    dropdown_event_type.setError("Please Select Expense Type.");
                    return;
                } else if (et_quantity.getText().toString().equalsIgnoreCase("")) {
                    et_quantity.setError("Please Enter Quantity.");
                    return;
                } else if (et_unitCost.getText().toString().equalsIgnoreCase("")) {
                    et_unitCost.setError("Please Enter Rate.");
                    return;
                }
                dropdown_event_type.setError(null);
                userInsertedExpenseList.add(new EventManagementExpenseLineTable(getActivity()).new EventManagementExpenseLineModel(submiteEventData.event_code,
                        String.valueOf(line_no),
                        selectedExpenseType.id, et_quantity.getText().toString(), et_unitCost.getText().toString(), totalAmount.getText().toString(), null, "0"));
                userInsertedExpenseList.get(userInsertedExpenseList.size() - 1).expense_type_name = selectedExpenseType.event_type;
                line_no++;
                event_expense_Adapter = new EventExpenseListAdapter(getActivity(), userInsertedExpenseList);
                event_List.setAdapter(event_expense_Adapter);
                dropdown_event_type.setText("");
                et_quantity.setText("");
                et_unitCost.setText("");
                totalAmount.setText("0");
            });
            submitPage.setOnClickListener(view1 -> {
                try {
                    if (imagesEncodedList == null || imagesEncodedList.isEmpty()) {
                        Snackbar.make(submitPage, "Please Select Images.", Snackbar.LENGTH_LONG).show();
                        return;
                    } else if (userInsertedExpenseList == null || userInsertedExpenseList.isEmpty() || userInsertedExpenseList.size() == 0) {
                        Snackbar.make(submitPage, "Please Add Minimun Single Expense Line.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!loadingDialog.getLoadingState()) {
                        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
                        if (networkUtil) {
                            JSONArray array = new JSONArray();
                            for (EventManagementExpenseLineTable.EventManagementExpenseLineModel data : userInsertedExpenseList) {
                                JSONObject obj = new JSONObject();
                                obj.put("event_code", data.event_code);
                                obj.put("line_no", data.line_no);
                                obj.put("expense_type", data.expense_type);
                                obj.put("quantity", data.quantity);
                                obj.put("rate_unit_cost", data.rate_unit_cost);
                                obj.put("amount", data.amount);
                                array.put(obj);
                            }
                            new CommanHitToServer(array.toString()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        } else {
                            //when it is offline
                        }
                    }
                } catch (Exception e) {

                }
            });
            et_quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!et_unitCost.getText().toString().equalsIgnoreCase("")) {
                        if (et_quantity.getText().toString().equalsIgnoreCase("")) {
                            totalAmount.setText("0");
                        } else {
                            totalAmount.setText(String.valueOf((Integer.parseInt(et_quantity.getText().toString()) * Integer.parseInt(et_unitCost.getText().toString()))));
                        }
                    } else {
                        totalAmount.setText("0");
                    }
                }
            });
            et_unitCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!et_quantity.getText().toString().equalsIgnoreCase("")) {
                        if (et_unitCost.getText().toString().equalsIgnoreCase("")) {
                            totalAmount.setText("0");
                        } else {
                            totalAmount.setText(String.valueOf((Integer.parseInt(et_quantity.getText().toString()) * Integer.parseInt(et_unitCost.getText().toString()))));
                        }
                    } else {
                        totalAmount.setText("0");
                    }
                }
            });
            selectImages.setOnClickListener(view1 -> {
                if (selectImages.getText().toString().equalsIgnoreCase("Images")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                } else {
                    selectImages.setText("Images");
                    if (imagesEncodedList != null && !imagesEncodedList.isEmpty()) {
                        imagesEncodedList.clear();
                    }
                }
            });
        } else {
            selectImages.setEnabled(false);
            submitPage.setEnabled(false);
            EventManagementExpenseLineTable eventManagementExpenseLineTable = new EventManagementExpenseLineTable(getActivity());
            eventManagementExpenseLineTable.open();
            userInsertedExpenseList = eventManagementExpenseLineTable.fetch(submiteEventData.event_code);
            eventManagementExpenseLineTable.close();
            event_expense_Adapter = new EventExpenseListAdapter(getActivity(), userInsertedExpenseList);
            event_List.setAdapter(event_expense_Adapter);

            // todo bindImage section
            eventImageSection.setVisibility(View.VISIBLE);
            ImageMasterTable imageMasterTable = new ImageMasterTable(getActivity());
            imageMasterTable.open();
            imagesEncodedList = imageMasterTable.fetch_bycode(submiteEventData.event_code);
            imageMasterTable.close();

            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            EventImageHorizontalAdapter mAdapter = new EventImageHorizontalAdapter(getActivity(), imagesEncodedList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    LoadingDialog loadingDialog = new LoadingDialog();

    private class CommanHitToServer extends AsyncTask<Void, Void, HttpHandlerModel> {
        String postedArray;

        CommanHitToServer(String postedArray) {
            this.postedArray = postedArray;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog.showLoadingDialog(getActivity());
            super.onPreExecute();
        }

        @Override
        protected HttpHandlerModel doInBackground(Void... asyModels) {
            try {
                MultipartUtility multipartUtility = new MultipartUtility(StaticDataForApp.insertEventExpense);
                multipartUtility.addFormField("expancedata", postedArray);
                for (String url : imagesEncodedList) {
                    multipartUtility.addFilePart("files", new File(url));
                }
                return multipartUtility.finish();
            } catch (Exception e) {
                return MultipartUtility.setReturnMessage(false, "Problem retrieving the user JSON results." + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(HttpHandlerModel result) {
            super.onPostExecute(result);
            bindResponse(result);
        }
    }

    void bindResponse(HttpHandlerModel result) {
        try {
            if (result.isConnectStatus()) {
                List<HitResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<HitResponseModel>>() {
                }.getType());
                if (responseslist.size() > 0 && responseslist.get(0).condition) {
                    EventManagementTable eventManagementTable = new EventManagementTable(getActivity());
                    eventManagementTable.open();
                    eventManagementTable.update_EventStatus(submiteEventData.event_code, responseslist.get(0).event_status);
                    eventManagementTable.close();
                    ImageMasterTable imageMasterTable = new ImageMasterTable(getActivity());
                    imageMasterTable.open();
                    imageMasterTable.delete(submiteEventData.event_code);
                    for (HitResponseModel responseobject : responseslist) {
                        imageMasterTable.insert(imageMasterTable.new ImageMasterModel(responseobject.id, responseobject.code, responseobject.image_url));
                    }
                    imageMasterTable.close();
                    EventManagementExpenseLineTable eventManagementExpenseLineTable = new EventManagementExpenseLineTable(getActivity());
                    eventManagementExpenseLineTable.open();
                    for (EventManagementExpenseLineTable.EventManagementExpenseLineModel data : userInsertedExpenseList) {
                        data.created_on = responseslist.get(0).created_on;
                        data.sendToServer = "1";
                        eventManagementExpenseLineTable.insert(data);
                    }
                    eventManagementExpenseLineTable.close();
                    loadFragments(R.id.nav_event_view, "Event List");
                } else {
                    Snackbar.make(submitPage, responseslist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                    }).show();
                }
            } else {
                Snackbar.make(submitPage, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {

        } finally {
            loadingDialog.dismissLoading();
        }
    }

    private void loadFragments(int id, String fragmentName) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(id);
    }

    List<String> imagesEncodedList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    String selectedFilePath = FilePath.getPath(getActivity(), mImageUri);
                    imagesEncodedList.add(selectedFilePath);
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            String selectedFilePath = FilePath.getPath(getActivity(), uri);
                            imagesEncodedList.add(selectedFilePath);
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        } finally {
            selectImages.setText(imagesEncodedList.size() + " Files");
        }
    }

    public class HitResponseModel {
        public boolean condition;
        public String message;
        public String created_on;
        public int id;
        public String code;
        public String image_url;
        public String event_status;
    }
}

