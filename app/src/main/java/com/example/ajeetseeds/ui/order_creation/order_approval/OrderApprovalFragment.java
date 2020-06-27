package com.example.ajeetseeds.ui.order_creation.order_approval;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.orderbook.OrderBookResponseModel;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderApprovalFragment extends Fragment {

    private OrderApprovalViewModel mViewModel;

    public static OrderApprovalFragment newInstance() {
        return new OrderApprovalFragment();
    }

    ListView orderApprovalList;
    OrderApprovalListAdapter approvalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_approval_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderApprovalViewModel.class);
        // TODO: Use the ViewModel
    }

    LoadingDialog loadingDialog = new LoadingDialog();
    SessionManagement sessionManagement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement = new SessionManagement(getActivity());
        orderApprovalList = view.findViewById(R.id.orderApprovalList);
        new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.getPendingBookOrder +sessionManagement.getUserEmail() +"&flag=Approve", null, "getorderBookApprovalList"));
    }

    private class CommanHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;
        private int position;

        CommanHitToServer() {
            this.position = -1;
        }

        CommanHitToServer(int position) {
            this.position = position;
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
            bindResponse(result, flagOfAction, position);
        }
    }

    List<OrderApprovalModel> ApproverList = new ArrayList<>();

    void bindResponse(HttpHandlerModel result, String flagOfAction, int position) {
        try {
            if (result.isConnectStatus() && !result.getJsonResponse().contentEquals("")) {
                if (flagOfAction.equalsIgnoreCase("getorderBookApprovalList")) {
                    List<OrderApprovalModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<OrderApprovalModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        ApproverList = responseslist;
                        bindListViewAddapterEvent(ApproverList);
                    } else {
                        Snackbar.make(orderApprovalList, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                } else if (flagOfAction.equalsIgnoreCase("approveRejectHit")) {
                    List<OrderBookResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<OrderBookResponseModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        if (position != -1) {
                            ApproverList.remove(position);
                        }
                        bindListViewAddapterEvent(ApproverList);
                    } else {
                        Snackbar.make(orderApprovalList, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }
                }
            } else {
                Snackbar.make(orderApprovalList, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), "Order Approval List", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bindListViewAddapterEvent(List<OrderApprovalModel> list) {
        try {
            approvalAdapter = new OrderApprovalListAdapter(getActivity(), list, (selectedOrderLine, position, flag) -> {
                if (flag.equalsIgnoreCase("ClickOnItem"))
                    OrderDetailItem(selectedOrderLine);
                else
                    approveRejectOrder(selectedOrderLine, position, flag);
            });
            orderApprovalList.setAdapter(approvalAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void approveRejectOrder(List<OrderApprovalModel.OrderLineModel> selectedOrderLine, int position, String flag) {
        if (flag.equalsIgnoreCase("approve_order")) {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Confirm " + selectedOrderLine.get(0).order_no)
                    .setMessage("Do you really want to approve?")
                    .setIcon(R.drawable.approve_order_icon)
                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
                        try {
                            JSONObject postedjson = new JSONObject();
                            postedjson.put("email", sessionManagement.getUserEmail());
                            postedjson.put("order_no", selectedOrderLine.get(0).order_no);
                            postedjson.put("order_status", "APPROVED");
                            postedjson.put("reson", "APPROVED");
                            new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.approveRejectOrder, postedjson, "approveRejectHit"));
                        } catch (Exception e) {
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {

                    })
                    .show();
        } else if (flag.equalsIgnoreCase("reject_order")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("Confirm " + selectedOrderLine.get(0).order_no);
            builder.setIcon(R.drawable.approve_order_icon);
            LinearLayout parentVertical = new LinearLayout(getActivity());
            LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            parentVertical.setLayoutParams(parentVerticalParams);
            parentVertical.setOrientation(LinearLayout.VERTICAL);
            TextView messageSession = new TextView(getActivity());
            messageSession.setText("Do you really want to reject?");
            messageSession.setTextColor(Color.BLACK);
            TextView enterReson_tv = new TextView(getActivity());
            enterReson_tv.setText("Enter Reason");
            enterReson_tv.setTextColor(getResources().getColor(R.color.colorAccent));
            enterReson_tv.setPadding(0, 10, 0, 0);
            EditText reason = new EditText(getActivity());
            parentVertical.addView(messageSession);
            parentVertical.addView(enterReson_tv);
            parentVertical.addView(reason);
            parentVertical.setPadding(40, 20, 40, 10);
            builder.setView(parentVertical);
            builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                try {
                    JSONObject postedjson = new JSONObject();
                    postedjson.put("email", sessionManagement.getUserEmail());
                    postedjson.put("order_no", selectedOrderLine.get(0).order_no);
                    postedjson.put("order_status", "REJECTED");
                    postedjson.put("reson", reason.getText().toString());
                    new CommanHitToServer(position).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.approveRejectOrder, postedjson, "approveRejectHit"));
                } catch (Exception e) {
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

    }

    void OrderDetailItem(List<OrderApprovalModel.OrderLineModel> selectedOrderLine) {
        try {
            ScrollView scrollView = new ScrollView(getActivity());
            ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(scrollViewParams);
            scrollView.setPadding(0, 20, 0, 0);
            LinearLayout parentVertical = new LinearLayout(getActivity());
            LinearLayout.LayoutParams parentVerticalParams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            parentVertical.setLayoutParams(parentVerticalParams);
            parentVertical.setOrientation(LinearLayout.VERTICAL);
            parentVertical.setPadding(10, 10, 10, 10);
            for (int i = 0; i < selectedOrderLine.size(); i++) {
                LinearLayout rowVertical = new LinearLayout(getActivity());
                LinearLayout.LayoutParams rowVerticalPrams = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                rowVertical.setLayoutParams(rowVerticalPrams);
                rowVertical.setOrientation(LinearLayout.HORIZONTAL);
                rowVertical.setPadding(10, 10, 10, 10);

                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams imageViewPrams = new LinearLayout.LayoutParams(200, 200);
                imageView.setLayoutParams(imageViewPrams);
                if (selectedOrderLine.get(i).image_url.contains("no_image_placeholder")) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                Glide.with(getActivity())
                        .load(StaticDataForApp.globalurl + selectedOrderLine.get(i).image_url)
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.color.gray3)
                        .into(imageView);

                TextView orderItemDetail = new TextView(getActivity());
                orderItemDetail.setText(selectedOrderLine.get(i).item_name + " Qty : " + selectedOrderLine.get(i).qty);
                orderItemDetail.setTextSize(16);
                orderItemDetail.setTextColor(Color.BLACK);
                orderItemDetail.setPadding(20, 60, 10, 10);
                rowVertical.addView(imageView);
                rowVertical.addView(orderItemDetail);
                parentVertical.addView(rowVertical, i);
            }

            // Prepare grid view
            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            scrollView.addView(parentVertical);
            relativeLayout.addView(scrollView);
            // Set grid view to alertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(relativeLayout);
            builder.setTitle("Order Items " + selectedOrderLine.get(0).order_no);
            builder.show();
        } catch (Exception ee) {
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Order Approve");
        super.onResume();
    }
}
