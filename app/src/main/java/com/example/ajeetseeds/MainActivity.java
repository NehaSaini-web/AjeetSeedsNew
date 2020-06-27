package com.example.ajeetseeds;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ajeetseeds.Http_Hanler.GlobalPostingMethod;
import com.example.ajeetseeds.Http_Hanler.HttpHandlerModel;
import com.example.ajeetseeds.Model.AsyModel;
import com.example.ajeetseeds.Model.LogoutModel;
import com.example.ajeetseeds.Model.SignalRReceiveModel;
import com.example.ajeetseeds.Model.StaticDataForApp;
import com.example.ajeetseeds.SessionManageMent.SessionManagement;
import com.example.ajeetseeds.backup.AndroidExceptionHandel;
import com.example.ajeetseeds.backup.ApplicationExceptionHandler;
import com.example.ajeetseeds.globalconfirmation.LoadingDialog;
import com.example.ajeetseeds.sidemenu.CustomExpandableListAdapter;
import com.example.ajeetseeds.sidemenu.ExpandableListDataPump;
import com.example.ajeetseeds.orderBookCart.OrderBookCartBottomDialogFragment;
import com.example.ajeetseeds.sqlLite.Database.SyncDataTable;
import com.example.ajeetseeds.sqlLite.SyncAppWithServerAsyTask;
import com.example.ajeetseeds.ui.order_creation.OrderBookGlobalModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OrderBookCartBottomDialogFragment.ItemClickListener {
    //    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    LoadingDialog loadingDialog;
    SignalRReceiveModel signalRReceiverdata;

    TextView tv_userName, tv_user_Email;
    SessionManagement sessionManagement;
    ImageView logoutInsideSidePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //todo floting button disable
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //todo end
        //todo load defalt menu for example home and Dashboard
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
//                .setDrawerLayout(drawer)
//                .build();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //todo end

        //todo ectivity Exception
        Thread.setDefaultUncaughtExceptionHandler(new ApplicationExceptionHandler(this));
        //todo exception end

        bindInItData();
        loadingDialog = new LoadingDialog();
        //todo recevice all message from server
        registerReceiver(broadcastReceiver, new IntentFilter("checkInterNetBackground"));
        registerReceiver(cartItemCountBrodcastReceiver, new IntentFilter("cartItemCountBrodcast"));
        syncAppWithServerAsyTask = new SyncAppWithServerAsyTask(MainActivity.this);
    }

    SyncAppWithServerAsyTask syncAppWithServerAsyTask;
    // Add this inside your class
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle b = intent.getExtras();
                boolean message = b.getBoolean("message");
                if (message) {
                    if (syncAppWithServerAsyTask != null && syncAppWithServerAsyTask.getStatus() != AsyncTask.Status.RUNNING) {
                        syncAppWithServerAsyTask = new SyncAppWithServerAsyTask(MainActivity.this);
                        syncAppWithServerAsyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                } else {
                    if (syncAppWithServerAsyTask != null && syncAppWithServerAsyTask.getStatus() == AsyncTask.Status.RUNNING) {
                        syncAppWithServerAsyTask.cancel(true);
                    }
                }
            } catch (Exception e) {

            }
        }
    };
    BroadcastReceiver cartItemCountBrodcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle b = intent.getExtras();
                orderBookGlobalModel.alertCount = b.getInt("cartcount", 0);
                updateAlertIcon();
            } catch (Exception e) {

            }
        }
    };

    void bindInItData() {
        //todo bind expandible menu
        Expandiblelistbind();
        sessionManagement = new SessionManagement(getApplicationContext());
        View headerLayout = navigationView.getHeaderView(0);
        tv_userName = headerLayout.findViewById(R.id.tv_userName);
        logoutInsideSidePanel = headerLayout.findViewById(R.id.logoutInsideSidePanel);
        tv_user_Email = headerLayout.findViewById(R.id.tv_user_Email);
        tv_userName.setText(sessionManagement.getUserName());
        tv_user_Email.setText(sessionManagement.getUserEmail() + " (" + sessionManagement.getUser_type() + ")");
        logoutInsideSidePanel.setOnClickListener(v -> {
            try {
                JSONObject postedJason = new JSONObject();
                postedJason.put("Email", sessionManagement.getUserEmail());
                new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.Logout,
                        postedJason, "LogoutHit"));
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void onItemClick(String item) {
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        if (item.equalsIgnoreCase("Cart")) {
//            loadFragments(R.id.nav_pick, "Pick");
        }
    }

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    Map<String, List<String>> expandableListDetail;
    public int selectedchildPosition = 0;
    public String groupName = "";

    public void Expandiblelistbind() {
        expandableListView = findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(getApplicationContext());
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            for (int g = 0; g < expandableListAdapter.getGroupCount(); g++) {
                if (g != groupPosition) {
                    expandableListView.collapseGroup(g);
                }
            }
            if (expandableListTitle.get(groupPosition).equalsIgnoreCase(groupName)) {
                expandableListView.setItemChecked(selectedchildPosition, true);
            } else {
                expandableListView.setItemChecked(selectedchildPosition, false);
            }
        });
        expandableListView.setOnGroupCollapseListener(groupPosition -> {
            //  Toast.makeText(getApplicationContext(),expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
        });
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // Toast.makeText(getApplicationContext(),expandableListTitle.get(groupPosition) + " -> "+ expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
            int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
            parent.setItemChecked(index, true);
            groupName = expandableListTitle.get(groupPosition);
            selectedchildPosition = index;
            SelectedChildNavigationExpandible(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.action_Cart);
//        //todo check cart icon display or not
//        if (sessionManagement.getUser_type().contentEquals("Employee") || sessionManagement.getUser_type().contentEquals("customer"))
//            item.setVisible(true);
//        else
//            item.setVisible(false);

        return true;
    }

    public FrameLayout redCircle, Added_item_cart_frgment;
    public TextView countTextView;
    public static OrderBookGlobalModel orderBookGlobalModel = new OrderBookGlobalModel();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.action_cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        Added_item_cart_frgment = rootView.findViewById(R.id.Added_item_cart_frgment);
        redCircle = rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = rootView.findViewById(R.id.view_alert_count_textview);
        Added_item_cart_frgment.setOnClickListener(view -> {
            // todo click on cart
            if (MainActivity.orderBookGlobalModel.selectedCropItem.size() > 0) {
                OrderBookCartBottomDialogFragment addPhotoBottomDialogFragment =
                        new OrderBookCartBottomDialogFragment("Cart").newInstance();
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        OrderBookCartBottomDialogFragment.TAG);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_Logout) {
            //todo check all data sync to server
            SyncDataTable syncDataTable = new SyncDataTable(this.getApplicationContext());
            syncDataTable.open();
            boolean checkststus = syncDataTable.fetchWhoNotPostToServer();
            if (checkststus) {
                Snackbar.make(toolbar, "Device Data Not Sync Two the server.", Snackbar.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject postedJason = new JSONObject();
                    postedJason.put("Email", sessionManagement.getUserEmail());
                    new CommanHitToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyModel(StaticDataForApp.Logout,
                            postedJason, "LogoutHit"));
                } catch (Exception e) {
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updateAlertIcon() {
        // if alert count extends into two digits, just show the red circle
        if (orderBookGlobalModel.alertCount == 0) {
            countTextView.setText("");
            redCircle.setVisibility(GONE);
        } else {
            countTextView.setText(String.valueOf(orderBookGlobalModel.alertCount));
            redCircle.setVisibility(VISIBLE);
        }
    }

    private void loadFragments(int id, String fragmentName) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(id);
        closeSidePanel();
        this.setTitle(fragmentName);
    }

    @Override
    public void onBackPressed() {
        expandableListView.setItemChecked(selectedchildPosition, false);
        closeSidePanel();
        super.onBackPressed();
    }

    void closeSidePanel() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    void SelectedChildNavigationExpandible(String selectedChild) {
        switch (selectedChild) {
            case "Add Daily Activity": {
                loadFragments(R.id.nav_daily_activity, "Daily Activity");
                break;
            }
            case "Order Books": {
                if (sessionManagement.getUser_type().contentEquals("Employee"))
                    loadFragments(R.id.nav_employee_orderbook, "Employee Order Book");
                else if (sessionManagement.getUser_type().contentEquals("Customer"))
                    loadFragments(R.id.nav_order_book, "Order Book");
                break;
            }
            case "Order Approve": {
                loadFragments(R.id.nav_order_approval, "Order Approve");
                break;
            }
            case "Order List": {
                loadFragments(R.id.nav_order_list, "Order List");
                break;
            }
            case "Daily Activity List": {
                loadFragments(R.id.nav_view_daily_activity_list, "Daily Activity List");
                break;
            }
            case "Create Event": {
                loadFragments(R.id.nav_event_create, "Create Event");
                break;
            }
            case "Event List": {
                loadFragments(R.id.nav_event_view, "Event List");
                break;
            }
            case "Event Approve": {
                loadFragments(R.id.nav_event_approve, "Event Approve");
                break;
            }
            case "Upgrade app": {
                loadFragments(R.id.nav_update_app, "Upgrade App");
                break;
            }
            case "Manual Sync": {
                loadFragments(R.id.nav_manualSync, "Manual Sync");
                break;
            }
            case "Add TA/DA Bill": {
                loadFragments(R.id.nav_create_travel, "Add TA/DA Bill");
                break;
            }
            case "View TA/DA List": {
                loadFragments(R.id.nav_view_travel, "View TA/DA List");
                break;
            }
            case "TA/DA Approve": {
                loadFragments(R.id.nav_approve_travel, "TA/DA Approve");
                break;
            }
            case "Create Inspection": {
                loadFragments(R.id.nav_inspection_create, "Inspection");
                break;
            }
        }
    }

    void bindResponse(HttpHandlerModel result, String flagOfAction) {
        try {
            if (result.isConnectStatus()) {
                if (flagOfAction.equalsIgnoreCase("LogoutHit"))
                    bindLogOutResponse(result.getJsonResponse(), flagOfAction);
            } else {
                Snackbar.make(toolbar, result.getJsonResponse(), Snackbar.LENGTH_INDEFINITE).setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        } catch (Exception e) {
            new AndroidExceptionHandel(e.getMessage(), flagOfAction, e.getStackTrace()[0].getLineNumber(), "MainActivity", e.getStackTrace()[0].getMethodName());
        } finally {
            loadingDialog.dismissLoading();
        }
    }

    void bindLogOutResponse(String result, String flagOfAction) throws Exception {
        ArrayList<LogoutModel> templist = new Gson().fromJson(result, new TypeToken<ArrayList<LogoutModel>>() {
        }.getType());
        if (templist.size() > 0) {
            if (templist.get(0).condition) {
                sessionManagement.ClearSession();
                Intent mainIntent = new Intent(this, SplashScreen.class);
                startActivity(mainIntent);
                finish();
            } else {
                Snackbar.make(toolbar, templist.get(0).message, Snackbar.LENGTH_INDEFINITE).setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        } else {
            Snackbar.make(toolbar, "Api Response Not Proper.", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class CommanHitToServer extends AsyncTask<AsyModel, Void, HttpHandlerModel> {
        private GlobalPostingMethod hitObj = new GlobalPostingMethod();
        private String flagOfAction;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.showLoadingDialog(MainActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
         unregisterReceiver(broadcastReceiver);
        unregisterReceiver(cartItemCountBrodcastReceiver);
    }
}
