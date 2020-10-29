package com.example.ajeetseeds.ui.order_creation.orderBook.crop_item_List;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ajeetseeds.MainActivity;
import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.masters.crop.CropItemMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CropMasterTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;
import com.example.ajeetseeds.ui.order_creation.orderBook.model.OrderBookModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CropItemFragment extends Fragment {

    private CropItemViewModel mViewModel;
    AppCompatEditText search_crop_item;
    ImageView search_button_crop;
    ListView cropItem_List;
    CropItemListViewAdapter cropItemListViewAdapter;
    public static String globale_crop_item_category = "";
    ChipGroup selected_category_data;

    int getDpValue(int dpValue){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics()
        );
        return paddingDp;
    }
    private void bindCategoryFilterList() {
        selected_category_data.removeAllViews();
        for(String categoriName:categories_list){
            final String chip_name=categoriName;
            final Chip chip = new Chip(getActivity());
          //  chip.setPadding(getDpValue(10), getDpValue(10), getDpValue(10), getDpValue(10));
            chip.setText(chip_name);
            chip.setElevation(getDpValue(2));
            chip.setChipStrokeWidth(getDpValue(1));
            if(globale_crop_item_category.equalsIgnoreCase(chip_name)){
                chip.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setChipStrokeColorResource(R.color.colorPrimary);
            }else{
                chip.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                chip.setChipBackgroundColorResource(R.color.white);
                chip.setChipStrokeColorResource(R.color.chipripple);
                chip.setRippleColorResource(R.color.chipripple);
            }
            chip.setCloseIconEnabled(false);
            chip.setOnClickListener(v -> {
                if(globale_crop_item_category.equalsIgnoreCase(chip_name)){
                    globale_crop_item_category="";
                }else{
                    globale_crop_item_category=chip_name;
                }
                bindCategoryFilterList();
                cropItemListViewAdapter.getFilter().filter(search_crop_item.getText().toString());
            });
            chip.setChipIconTintResource(R.color.colorPrimary);
            selected_category_data.addView(chip);
        }
    }

    public static CropItemFragment newInstance() {
        return new CropItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crop_item_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CropItemViewModel.class);
        // TODO: Use the ViewModel
    }

    CropMasterTable.CropMasterModel select_crop = null;
    List<String> categories_list = new ArrayList<>();

    void bindData(View view) {
        try {
            if (getArguments() != null) {
                select_crop = new Gson().fromJson(getArguments().getString("dataPass", ""), CropMasterTable.CropMasterModel.class);
                if (crop_list == null || crop_list.size() == 0) {
                    CropItemMasterTable cropItemMasterTable = new CropItemMasterTable(getActivity());
                    cropItemMasterTable.open();
                    crop_list = cropItemMasterTable.fetch(select_crop.code);
                    cropItemMasterTable.close();
                }
                //todo get Category
                selected_category_data = view.findViewById(R.id.selected_category_data);
                if (categories_list != null && !categories_list.isEmpty())
                    categories_list.clear();
                for (int i = 0; i < crop_list.size(); i++) {
                    boolean verify = false;
                    for (String categori : categories_list) {
                        if (categori.equalsIgnoreCase(crop_list.get(i).crop)) {
                            verify = true;
                            break;
                        }
                    }
                    if (!verify) {
                        categories_list.add(crop_list.get(i).crop);
                    }
                }
                bindCategoryFilterList();
            }
        } catch (Exception e) {
        }
    }

    List<CropItemMasterTable.CropItemMasterModel> crop_list;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData(view);
        try {
            //todo update cart qty from list
            for (int i = 0; i < MainActivity.orderBookGlobalModel.selectedCropItem.size(); i++) {
                for (int j = 0; j < this.crop_list.size(); j++) {
                    if (MainActivity.orderBookGlobalModel.selectedCropItem.get(i).item_no.equalsIgnoreCase(this.crop_list.get(j).item_no)) {
                        this.crop_list.get(j).total_buy_qty = MainActivity.orderBookGlobalModel.selectedCropItem.get(i).total_buy_qty;
                        break;
                    }
                }
            }
            cropItem_List = view.findViewById(R.id.cropItem_List);
            cropItemListViewAdapter = new CropItemListViewAdapter(crop_list, getActivity(), (item_no, flag) -> {
                int position = -1;
                for (int i = 0; i < this.crop_list.size(); i++) {
                    if (item_no.equalsIgnoreCase(this.crop_list.get(i).item_no)) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    if (flag.contentEquals("Add")) {
                        crop_list.get(position).total_buy_qty++;
                    } else {
                        if (crop_list.get(position).total_buy_qty > 0)
                            crop_list.get(position).total_buy_qty--;
                    }
                    cropItemListViewAdapter.notifyDataSetChanged();
                    boolean verefy = false;
                    for (int i = 0; i < MainActivity.orderBookGlobalModel.selectedCropItem.size(); i++) {
                        if (crop_list.get(position).item_no.equalsIgnoreCase(MainActivity.orderBookGlobalModel.selectedCropItem.get(i).item_no)) {
                            verefy = true;
                            MainActivity.orderBookGlobalModel.selectedCropItem.set(i, crop_list.get(position));
                        }
                    }
                    if (!verefy) {
                        MainActivity.orderBookGlobalModel.selectedCropItem.add(crop_list.get(position));
                    }
                    Intent brodcastIntent = new Intent("cartItemCountBrodcast");
                    brodcastIntent.putExtra("cartcount", MainActivity.orderBookGlobalModel.selectedCropItem.size());
                    getActivity().sendBroadcast(brodcastIntent);
                    cropItemListViewAdapter.notifyDataSetChanged();
                }
            });
            cropItem_List.setAdapter(cropItemListViewAdapter);
            search_crop_item = view.findViewById(R.id.search_crop_item);
            search_button_crop = view.findViewById(R.id.search_button_crop);
            search_button_crop.setOnClickListener(view1 -> {
                cropItemListViewAdapter.getFilter().filter(search_crop_item.getText().toString());
            });
            search_crop_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        cropItemListViewAdapter.getFilter().filter(search_crop_item.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
        }
        getActivity().registerReceiver(cartItemCountBrodcastReceiver, new IntentFilter("cartItemCountBrodcast"));
    }


    BroadcastReceiver cartItemCountBrodcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle b = intent.getExtras();
                boolean resetUi = b.getBoolean("resetCropItemList");
                if (resetUi) {
                    for (int i = 0; i < crop_list.size(); i++) {
                        boolean verify = false;
                        for (int j = 0; j < MainActivity.orderBookGlobalModel.selectedCropItem.size(); j++) {
                            if (crop_list.get(i).item_no.equalsIgnoreCase(MainActivity.orderBookGlobalModel.selectedCropItem.get(j).item_no)) {
                                crop_list.get(i).total_buy_qty = MainActivity.orderBookGlobalModel.selectedCropItem.get(j).total_buy_qty;
                                verify = true;
                                break;
                            }
                        }
                        if (!verify) {
                            crop_list.get(i).total_buy_qty = 0;
                        }
                        cropItemListViewAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        bindData(getView());
        getActivity().setTitle("Crop Item");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(cartItemCountBrodcastReceiver);
    }
}
