package com.example.ajeetseeds.orderBookCart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.MainActivity;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Model.orderbook.OrderBookResponseModel;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.AllTablesName;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.ui.order_creation.CartProductDetailListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderBookCartBottomDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    public static final String TAG = "ActionBottomDialog";
    String flag;
    private ItemClickListener mListener;
    SessionManagement sessionManagement;
    Button goto_mainMenu;

    public OrderBookCartBottomDialogFragment(String flag) {
        this.flag = flag;
    }

    public OrderBookCartBottomDialogFragment newInstance() {
        return new OrderBookCartBottomDialogFragment(flag);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.order_book_cart_bottom_sheet, container, false);
    }

    CartProductDetailListAdapter adapter;
    TextView cutomerDescription;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView cart_book_Item = view.findViewById(R.id.cart_book_Item);
        cutomerDescription = view.findViewById(R.id.cutomerDescription);
        if (MainActivity.orderBookGlobalModel.selectdCustomer != null) {
            cutomerDescription.setText(MainActivity.orderBookGlobalModel.selectdCustomer.name + " (" + MainActivity.orderBookGlobalModel.selectdCustomer.contact + ")");
            cutomerDescription.setVisibility(View.VISIBLE);
        } else
            cutomerDescription.setVisibility(View.GONE);
        adapter = new CartProductDetailListAdapter(getActivity(), MainActivity.orderBookGlobalModel.selectedCropItem, (position, flag1) -> {
            if (flag1.equalsIgnoreCase("Add")) {
                MainActivity.orderBookGlobalModel.selectedCropItem.get(position).total_buy_qty++;
            } else if (flag1.equalsIgnoreCase("Remove")) {
                if (MainActivity.orderBookGlobalModel.selectedCropItem.get(position).total_buy_qty > 0)
                    MainActivity.orderBookGlobalModel.selectedCropItem.get(position).total_buy_qty--;
                if (MainActivity.orderBookGlobalModel.selectedCropItem.get(position).total_buy_qty == 0) {
                    MainActivity.orderBookGlobalModel.selectedCropItem.remove(position);
                }
            }
            adapter.notifyDataSetChanged();
            Intent brodcastIntent = new Intent("cartItemCountBrodcast");
            brodcastIntent.putExtra("cartcount", MainActivity.orderBookGlobalModel.selectedCropItem.size());
            brodcastIntent.putExtra("resetCropItemList", true);
            getActivity().sendBroadcast(brodcastIntent);
            if (MainActivity.orderBookGlobalModel.selectedCropItem.size() == 0) {
                dismiss();
            }
        });
        cart_book_Item.setAdapter(adapter);
        goto_mainMenu = view.findViewById(R.id.goto_mainMenu);
        if (flag.equalsIgnoreCase("Cart")) {
            goto_mainMenu.setText("Confirm");
        }
        goto_mainMenu.setOnClickListener(view1 -> {
            if (flag.equalsIgnoreCase("Cart")) {
                //todo Order Book
                try {
                    InsertDataFromServerOrLOcal();
                } catch (Exception e) {
                }
                //  mListener.onItemClick("PickReloade");
            } else {
                mListener.onItemClick("close");
                dismiss();
            }
        });
    }

    void InsertDataFromServerOrLOcal() throws Exception {
        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
        if (networkUtil) {
            JSONObject postedJson = new JSONObject();
            JSONObject orderHeader = new JSONObject();
            orderHeader.put("approver_email", sessionManagement.getApprover_id());
            orderHeader.put("user_type", sessionManagement.getUser_type());
            orderHeader.put("customer_no", MainActivity.orderBookGlobalModel.selectdCustomer != null ? MainActivity.orderBookGlobalModel.selectdCustomer.no : "");
            orderHeader.put("email_id", sessionManagement.getUserEmail());
            postedJson.put("orderHeader", orderHeader);

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < MainActivity.orderBookGlobalModel.selectedCropItem.size(); i++) {
                JSONObject temp = new JSONObject();
                temp.put("item_no", MainActivity.orderBookGlobalModel.selectedCropItem.get(i).item_no);
                temp.put("qty", MainActivity.orderBookGlobalModel.selectedCropItem.get(i).total_buy_qty);
                jsonArray.put(temp);
            }
            postedJson.put("orderLines", jsonArray);
            new DailyActivityHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.insertOrderBookHeaderLines, postedJson, "orderBookConfirm"));
        } else {
            bindLOcalDataBase("0",null);
            PopupDisplay("Order Book Wait For Online.");
            clear_Screen();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        TextView tvSelected = (TextView) view;
        mListener.onItemClick(tvSelected.getText().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(String item);
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
                if (flagOfAction.equalsIgnoreCase("orderBookConfirm")) {
                    List<OrderBookResponseModel> responseslist = new Gson().fromJson(result.getJsonResponse(), new TypeToken<List<OrderBookResponseModel>>() {
                    }.getType());
                    if (responseslist.size() > 0 && responseslist.get(0).condition) {
                        bindLOcalDataBase(responseslist.get(0).order_no,responseslist.get(0).created_on);
                        PopupDisplay(responseslist.get(0).order_no);
                        clear_Screen();
                    } else {
                        Snackbar.make(goto_mainMenu, responseslist.size() > 0 ? responseslist.get(0).message : "Record Not inserted.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                        }).show();
                    }

                }
            } else {
                Snackbar.make(goto_mainMenu, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
                }).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), "Order Book Bind Response", e.getStackTrace()[0].getLineNumber()
                    , e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    public void PopupDisplay(String order_nodata) {

        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View successmessaePopupView = inflater.inflate(R.layout.success_message_popup, null);
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
            TextView order_no = (TextView) successmessaePopupView.findViewById(R.id.order_no);
            order_no.setText(order_nodata);

            ImageView succesessImg = (ImageView) successmessaePopupView.findViewById(R.id.succesessImg);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.success_animation);
            succesessImg.startAnimation(animation);

            dialog.setContentView(successmessaePopupView);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialog.show();
            Button goBackFromItem = successmessaePopupView.findViewById(R.id.goBackFromItem);
            goBackFromItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            //You need to add the following line for this solution to work; thanks skayred
            successmessaePopupView.setFocusableInTouchMode(true);
            successmessaePopupView.requestFocus();
            successmessaePopupView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
        }
    }

    public void bindLOcalDataBase(String order_no,String created_on) {
        //todo insert Heder
        OrderBookHeader orderBookHeader = new OrderBookHeader(getActivity());
        orderBookHeader.open();
        String android_order_no = orderBookHeader.getTableSequenceNo();
        orderBookHeader.insert(orderBookHeader.new OrderBookHeaderModel(android_order_no, order_no, sessionManagement.getApprover_id(), sessionManagement.getUser_type(), MainActivity.orderBookGlobalModel.selectdCustomer != null ? MainActivity.orderBookGlobalModel.selectdCustomer.no : "", "PENDING", created_on,MainActivity.orderBookGlobalModel.selectedCropItem.get(0).image_url ));
        orderBookHeader.close();
        //todo insert Liine
        try {
            OrderBookLine orderBookLine = new OrderBookLine(getActivity());
            orderBookLine.open();
            List<OrderBookLine.OrderBookLineModel> addlines = new ArrayList<>();
            for (int i = 0; i < MainActivity.orderBookGlobalModel.selectedCropItem.size(); i++) {
                OrderBookLine.OrderBookLineModel tempObject = orderBookLine.new OrderBookLineModel(android_order_no, order_no, MainActivity.orderBookGlobalModel.selectedCropItem.get(i).item_no,
                        String.valueOf(MainActivity.orderBookGlobalModel.selectedCropItem.get(i).total_buy_qty));
                addlines.add(tempObject);
            }
            orderBookLine.insertBulkData(addlines);
            orderBookLine.close();

            //todo master entry Table out redy is going to be 1 so that data uploaded to the server
            if (order_no.contentEquals("0")) {
                SyncDataTable syncDataTable = new SyncDataTable(getActivity());
                syncDataTable.open();
                syncDataTable.OutActivate(AllTablesName.OrderBooking_header, 1);
                syncDataTable.close();
            }
        } catch (Exception e) {
            Snackbar.make(goto_mainMenu, "Not Inserted. " + e.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", view -> {
            }).show();
        }
    }

    void clear_Screen() {
        MainActivity.orderBookGlobalModel.alertCount = 0;
        MainActivity.orderBookGlobalModel.selectedCropItem = new ArrayList<>();
        Intent brodcastIntent = new Intent("cartItemCountBrodcast");
        brodcastIntent.putExtra("cartcount", MainActivity.orderBookGlobalModel.alertCount);
        brodcastIntent.putExtra("resetCropItemList", true);
        getActivity().sendBroadcast(brodcastIntent);
        dismiss();
    }

}
