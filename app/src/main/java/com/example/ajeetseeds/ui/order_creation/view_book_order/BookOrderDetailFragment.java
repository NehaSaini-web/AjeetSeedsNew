package com.example.ajeetseeds.ui.order_creation.view_book_order;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.Network.NetworkUtil;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookHeader;
import com.example.ajeetseeds.sqlLite.orderBook.OrderBookLine;
import com.example.ajeetseeds.ui.order_creation.order_approval.OrderApprovalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookOrderDetailFragment extends Fragment {

    private BookOrderDetailViewModel mViewModel;

    public static BookOrderDetailFragment newInstance() {
        return new BookOrderDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_order_detail_fragment, container, false);
    }

    SessionManagement sessionManagement;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BookOrderDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    ListView orderApprovalList;
    OrderListAdapter approvalAdapter;
    List<OrderBookHeader.OrderBookHeaderModel> listOrderheader = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getActivity());
        orderApprovalList = view.findViewById(R.id.orderList);
        boolean networkUtil = NetworkUtil.getConnectivityStatusString(getContext());
        if (networkUtil) {
            new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.getPendingBookOrder + sessionManagement.getUserEmail() + "&flag=GetAllOrder", null, "UpdateOrderStatus"));
        } else {
            bindUiListData();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    void OrderDetailItem(List<OrderBookLine.CustomeModel> selectedOrderLine) {
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
                LinearLayout.LayoutParams imageViewPrams = new LinearLayout.LayoutParams(160, 160);
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
                try{
                    CropItemMasterTable cropItemMasterTable=new CropItemMasterTable(getActivity());
                    cropItemMasterTable.open();
                    selectedOrderLine.get(i).pack_size=cropItemMasterTable.getFG_pack_size(selectedOrderLine.get(i).item_no);
                    cropItemMasterTable.close();
                }catch (Exception e){}
                TextView orderItemDetail = new TextView(getActivity());
                orderItemDetail.setText(selectedOrderLine.get(i).item_name + " ( "+selectedOrderLine.get(i).item_no+" )\nPack Size : "+selectedOrderLine.get(i).pack_size+" , Packet : " + selectedOrderLine.get(i).qty);
                orderItemDetail.setTextSize(12);
                orderItemDetail.setTextColor(Color.BLACK);
                orderItemDetail.setPadding(20, 40, 10, 10);
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

    LoadingDialog loadingDialog = new LoadingDialog();

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
            bindResponse(result.getJsonResponse(), flagOfAction);
        }
    }

    void bindResponse(String result, String flagOfAction) {
        try {
            List<OrderApprovalModel> responseslist = new Gson().fromJson(result, new TypeToken<List<OrderApprovalModel>>() {
            }.getType());
            if (responseslist.size() > 0 && responseslist.get(0).condition == true) {
                for (OrderApprovalModel response : responseslist) {
                    //todo insert Heder
                    OrderBookHeader orderBookHeader = new OrderBookHeader(getActivity());
                    orderBookHeader.open();
                    orderBookHeader.update_OrderStatus(response.order_no, response.order_status);
                    orderBookHeader.close();
                }
            }
        } catch (Exception e) {
        } finally {
            bindUiListData();
            loadingDialog.dismissLoading();
        }
    }

    private void bindUiListData() {
        try {
            OrderBookHeader orderBookHeader = new OrderBookHeader(getActivity());
            orderBookHeader.open();
            listOrderheader = orderBookHeader.fetch();
            orderBookHeader.close();
        } catch (Exception e) {
        }
        approvalAdapter = new OrderListAdapter(getActivity(), listOrderheader);
        orderApprovalList.setAdapter(approvalAdapter);
        orderApprovalList.setOnItemClickListener((adapterView, view1, i, l) -> {
            try {
                OrderBookLine orderBookLine = new OrderBookLine(getActivity());
                orderBookLine.open();
                List<OrderBookLine.CustomeModel> customeModels = orderBookLine.fetch_byOrderNo(listOrderheader.get(i).order_no);
                orderBookLine.close();
                OrderDetailItem(customeModels);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        });
    }

}
