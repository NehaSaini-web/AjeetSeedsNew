package com.example.ajeetseeds.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ajeetseeds.R;
import com.example.ajeetseeds.sqlLite.Database.DatabaseHelper;
import com.example.ajeetseeds.sqlLite.masters.Geographical_Setup.Geographical_SetupTable;
import com.example.ajeetseeds.sqlLite.masters.crop.CustomerMasterTable;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    DatabaseHelper mydb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private Geographical_SetupTable geographical_setup;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//       try {
//           geographical_setup = new Geographical_SetupTable(getActivity().getApplicationContext());
//           geographical_setup.open();
//           ArrayList<Geographical_SetupTable.Geographical_Setup>post=new ArrayList<>();
//           for(int i=0;i<1000;i++){
//               post.add(geographical_setup.new Geographical_Setup(1,"a"+i, "a"+i, "a"+i, "a"+i, "a"+i, "a"+i));
//           }
//           geographical_setup.insertArray(post);
//           List<Geographical_SetupTable.Geographical_Setup> data = geographical_setup.fetch();
//           for(Geographical_SetupTable.Geographical_Setup dataa:data){
//               Log.e("data",dataa.zone);
//           }
//           geographical_setup.close();
//       }catch (Exception e){
//           Log.e("Error",e.getMessage());
//       }

    }

    @Override
    public void onResume() {
        getActivity().setTitle("Home");
        super.onResume();
    }
}