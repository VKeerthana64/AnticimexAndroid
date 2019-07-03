package com.surveyor.app.surveyorapp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.attendance.AttendanceFragment;
import com.surveyor.app.surveyorapp.bean.DrawerBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobTypeBean;
import com.surveyor.app.surveyorapp.bean.OptionBean;
import com.surveyor.app.surveyorapp.bean.ReverseGeoBean;
import com.surveyor.app.surveyorapp.jobs.JobsFragment;
import com.surveyor.app.surveyorapp.jobs.ServiceReportFragment;
import com.surveyor.app.surveyorapp.jobs_team_leader.JobTeamLeaderFragment;
import com.surveyor.app.surveyorapp.profile.ProfileFragment;
import com.surveyor.app.surveyorapp.reporting.ReportingFragment;
import com.surveyor.app.surveyorapp.retrofit.RestClient;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.survey_form.SurveyFormFragment;
import com.surveyor.app.surveyorapp.sync_service.ConnectivityBroadcastReceiver;
import com.surveyor.app.surveyorapp.sync_service.MySyncService;
import com.surveyor.app.surveyorapp.sync_service.SyncService;
import com.surveyor.app.surveyorapp.sync_service.UpdateLocationService;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    SharedObjects sharedObjects;

    @BindView(R.id.imgSync)
    ImageView imgSync;

    @BindView(R.id.llDashboard)
    LinearLayout llDashboard;

    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txtTime)
    TextView txtTime;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    DrawerAdapter drawerAdapter;

    ArrayList<DrawerBean> arrDrawer = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 4000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private LocationRequest mLocationRequest;
    public static Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    public static String address = "";
    public static String pincode = "";
    boolean checkLocationSetting = true;
    DBHandler dbHandler;

    public static final String DATA_SAVED_BROADCAST = "com.surveyor.app.surveyorapp.datasaved";
    private BroadcastReceiver dataSavedBR;

    public static DashboardActivity instance;
    public SurveyFormFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        instance = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ButterKnife.bind(this);
        setUpDrawer();
        sharedObjects = new SharedObjects(DashboardActivity.this);
        dbHandler = new DBHandler(DashboardActivity.this);

        AppConstants.TOKEN = sharedObjects.getToken();

        txtUserName.setText(sharedObjects.getUserInfo().getUsername());

        loadToolbarProfile();

        txtTime.setText(SharedObjects.getTodaysDate(AppConstants.DateFormats.TIME_DASHOBOARD));


        // todo
        // if (sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN).equalsIgnoreCase("true")){


        /*if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN)) &&
                sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
            selectDrawerItem(AppConstants.MenuPosition.ATTENDANCE,new Bundle()*//*""*//*);
        } else {
            selectDrawerItem(AppConstants.MenuPosition.DASHBOARD,new Bundle()*//*""*//*);
        }*/

        selectDrawerItem(AppConstants.MenuPosition.DASHBOARD, new Bundle()/*""*/);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                showLocationSettingDialog();
            } else {
                requestPermission();
            }
        } else {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            showLocationSettingDialog();
        }

        if (SharedObjects.isNetworkConnected(DashboardActivity.this)) {
            if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                //when logged in as technician
//                getJobType();
//                getJobListForSync();
            } else {

            }
        }

        imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncService.getSyncAllData(DashboardActivity.this);
                Toast.makeText(getApplicationContext(),"Sync Successfully",Toast.LENGTH_LONG).show();
            }
        });

        startService(new Intent(DashboardActivity.this, ConnectivityBroadcastReceiver.class));

        //the broadcast receiver to update sync status
        dataSavedBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        //registering the broadcast receiver to update sync status
        try {
            registerReceiver(dataSavedBR, new IntentFilter(DATA_SAVED_BROADCAST));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("startService", isMyServiceRunning(ConnectivityBroadcastReceiver.class) + "");

        Log.d("intertime>>>>>","xc" +sharedObjects.getUserInfo().getIntervalTime());

        int minutes = sharedObjects.getUserInfo().getIntervalTime();
        final long milliseconds = minutes * 60000;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int minutes = sharedObjects.getUserInfo().getIntervalTime();
                final long milliseconds = minutes * 60000;

                sendLocation();

                //  startService(new Intent(DashboardActivity.this, UpdateLocationService.class));
                handler.postDelayed(this, milliseconds);
            }
        }, milliseconds);
    }

    public void sendLocation()
    {

        RequestBody reqLatitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLatitude()+"");
        RequestBody reqLongitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLongitude()+"");

        Log.d("latitude>>==",">>" + reqLatitude);
        Log.d("Longitude>>==",">>" + reqLongitude);
        RestClient restClient = new RestClient(getApplicationContext());
        Call<JsonElement> call = restClient.post().insertLocation(reqLatitude,reqLongitude);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("checkloc:", ">>>>"+ jsonObject);
                    Toast.makeText(getApplicationContext(),"Location Sent Successfully",Toast.LENGTH_LONG);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //  dismissProgressDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadToolbarProfile() {
        if (sharedObjects.getProfilePic() != null) {
            imgUser.setImageBitmap(sharedObjects.getProfilePic());
        } else {
            imgUser.setImageDrawable(ContextCompat.getDrawable(DashboardActivity.this, R.drawable.avatar));
        }
/*        Picasso.with(DashboardActivity.this).load(sharedObjects.getUserInfo().getProfileimage()).
                placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgUser);*/
    }

    public void closeDrawer() {
        drawer.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.imgUser)
    public void slideDrawer() {
        openDrawer();
    }

    @OnClick(R.id.txtTitle)
    public void openDashboard() {
        selectDrawerItem(6, new Bundle());
    }

    public void goToDashboard(boolean isDraftSaved) {
        if (isDraftSaved) {
            selectDrawerItem(6, new Bundle());
        }
    }

    @OnClick(R.id.llDashboard)
    public void openDashboardTitleClick() {
        myFragment = (SurveyFormFragment) getSupportFragmentManager().findFragmentByTag("com.surveyor.app.surveyorapp.survey_form.SurveyFormFragment");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            AppConstants.showConfirmDialog("Do you wish to save your draft?", DashboardActivity.this, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.e("openDashboard:", "Yes");
                    myFragment.saveDraft();
                }
            }, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.e("openDashboardTitle:", "NO");
                }
            });
            Log.e("openDashboardTitle:", "Test");
        } else {
            selectDrawerItem(6, new Bundle());
        }
    }

    private void setUpDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrDrawer = new ArrayList<>();
        arrDrawer.add(new DrawerBean(R.drawable.ic_menu_attendance));
        arrDrawer.add(new DrawerBean(R.drawable.ic_menu_job));
        arrDrawer.add(new DrawerBean(R.drawable.ic_menu_reporting));
        arrDrawer.add(new DrawerBean(R.drawable.ic_menu_profile));
        arrDrawer.add(new DrawerBean(R.drawable.ic_menu_logout));

//        rlMain = (RelativeLayout) findViewById(R.id.rlMain);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                rlMain.setTranslationX(slideOffset * drawerView.getWidth());
//                drawer.bringChildToFront(drawerView);
//                drawer.requestLayout();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerAdapter = new DrawerAdapter(arrDrawer, DashboardActivity.this);
        rvMenu.setAdapter(drawerAdapter);

        drawerAdapter.setOnItemClickListener(new DrawerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, DrawerBean bean) {
                selectDrawerItem(position + 1, new Bundle()/*""*/);
            }
        });
    }

    public void selectDrawerItem(int position, Bundle data/*String data*/) {
        Log.e("Pos : ", position + "");
        Bundle bundle;
        Fragment fragment = null;
        Class fragmentClass;
        switch (position) {
            case 1: //attendance

                if (SharedObjects.isNetworkConnected(DashboardActivity.this)) {
                    fragmentClass = AttendanceFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        loadFragment(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    SharedObjects.showAlertDialog(DashboardActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
                }

                /*if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN)) &&
                        sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                    if (SharedObjects.isNetworkConnected(DashboardActivity.this)) {
                        fragmentClass = AttendanceFragment.class;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            loadFragment(fragment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        SharedObjects.showAlertDialog(DashboardActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
                    }
                } else {
                    fragmentClass = AttendanceFragment.class;
//                    fragmentClass = DashboardFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        loadFragment(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                break;
            case 2: // jobs
                if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                    //when logged in as technician
                    fragmentClass = JobsFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        loadFragment(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    fragmentClass = JobTeamLeaderFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        loadFragment(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3: //reporting
                fragmentClass = ReportingFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    loadFragment(fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 4: //profile
                fragmentClass = ProfileFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    loadFragment(fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 5: //logout
                logout();
                break;
            case 6: //dashboard
                fragmentClass = DashboardFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    loadFragment(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
               /* bundle = new Bundle();
                bundle.putString(AppConstants.INTENT_BUNDLE, data);*/
                fragmentClass = ServiceReportFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    //    fragment.setArguments(bundle);
                    fragment.setArguments(data);
                    loadFragment(fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                /*bundle = new Bundle();
                bundle.putString(AppConstants.INTENT_BUNDLE, data );*/

                fragmentClass = SurveyFormFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    //fragment.setArguments(bundle);
                    fragment.setArguments(data);
                    loadFragment(fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        Log.e("fragmentTag", fragmentTag);

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.flContent, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        drawer.closeDrawers();
    }

    public void logout() {
        try {
            ConnectivityBroadcastReceiver.firstConnect = true;
            removeAllPreferenceOnLogout(sharedObjects);
            stopService(new Intent(DashboardActivity.this, MySyncService.class));
            stopService(new Intent(DashboardActivity.this, ConnectivityBroadcastReceiver.class));

            Log.e("Service : ", isMyServiceRunning(MySyncService.class) + " " + isMyServiceRunning(ConnectivityBroadcastReceiver.class));

            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void removeAllPreferenceOnLogout(SharedObjects sharedObjects) {
        sharedObjects.setPreference(AppConstants.STATUS, AppConstants.STATUS_LOGOUT);
        sharedObjects.removeSinglePreference(AppConstants.LOGIN_BEAN);
        sharedObjects.removeSinglePreference(AppConstants.CHECK_CLOCKIN);
        sharedObjects.removeSinglePreference(AppConstants.USERNAME);
        sharedObjects.removeSinglePreference(AppConstants.PASSWORD);
        sharedObjects.removeSinglePreference(AppConstants.OFFLINE.JOB_TYPE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawers();
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if (doubleBackToExitPressedOnce) {
                ConnectivityBroadcastReceiver.firstConnect = true;
                stopLocationUpdates();
                finish();
                System.exit(0);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
//
        } else {
            Fragment surveyFormFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
            if (surveyFormFragment instanceof SurveyFormFragment) {
                boolean b = ((SurveyFormFragment) surveyFormFragment).backPressed();
                if (b) {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;

        /*final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateChecker, intentFilter);*/


    }

    private void requestPermission() {
        Dexter.withActivity(DashboardActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e("Permissions", "All permissions are granted!");
                            // code to execute
                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            showLocationSettingDialog();

                        }
/*                        if (!report.areAllPermissionsGranted()) { // for taking permission if user denied any of the permission
                            requestPermission();
                        }*/

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                        token.cancelPermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private boolean checkPermissions() {
        int FineLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (FineLocPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle(getString(R.string.permission_title));
        builder.setMessage(getString(R.string.permission_msg));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onBackPressed();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == DashboardFragment.REQUEST_CODE_TAKE_PICTURE){
            selectDrawerItem();
        }*/
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("Location", "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("Location", "User chose not to make required location settings changes.");
                        showLocationSettingDialog();
                        break;
                }
                break;
            case PERMISSION_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermission();
                        }
                        break;
                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void startLocationUpdates() {
        try {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLocationRequest != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

       /* if (networkStateChecker != null){
            unregisterReceiver(networkStateChecker);
        }*/
    }

    public void showLocationSettingDialog() {
        try {
            if (checkLocationSetting) {
                checkLocationSetting = false;
//            Log.e("")
//            mLocationRequest = new LocationRequest();
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setSmallestDisplacement(10);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location requests here.
                                checkLocationSetting = true;
                                startLocationUpdates();
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                                try {
                                    checkLocationSetting = true;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(DashboardActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                checkLocationSetting = true;
                                // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection", " failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mCurrentLocation = location;
        String result = " Latitude is " + mCurrentLocation.getLatitude() + " " + "Longitude is " + mCurrentLocation.getLongitude();
//        Log.e("Current : ", result);
        ReverseGeoBean bean = new ReverseGeoBean(mCurrentLocation, "address");
        (new ReverseGeocoding(this)).execute(bean);
    }


    private class ReverseGeocoding extends AsyncTask<ReverseGeoBean, Void, ReverseGeoBean> {
        Context mContext;
        String result;
        String strPincode;

        public ReverseGeocoding(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ReverseGeoBean doInBackground(ReverseGeoBean... params) {

            try {
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

                Location loc = params[0].getLocation();

                if (loc != null) {
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        }

                        String add = address.getAddressLine(0);
                        String city = address.getLocality();
                        String subcity = address.getSubLocality();
                        String state = address.getAdminArea();
                        String country = address.getCountryName();
                        strPincode = address.getPostalCode();
                        if (add.length() > 20) {
                            sb.append(address.getAddressLine(0));
                        } else {
                            sb.append(add).append(", ");
                            sb.append(subcity).append(", ");
                            sb.append(city).append(", ");
                            sb.append(state).append(" ");
//                            sb.append(postalCode).append(", ");
                            sb.append(country);
                        }
                        result = sb.toString();
                    } else {
                        result = mContext.getString(R.string.unable_to_get_latlong);
                    }
                } else {
                    result = mContext.getString(R.string.unable_to_get_latlong);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ReverseGeoBean(result, strPincode);
        }

        @Override
        protected void onPostExecute(ReverseGeoBean s) {
            super.onPostExecute(s);

            try {
                address = s.getAddress();
                pincode = s.getPincode();
                Log.e("Address", address + " ++ " + pincode);

//                if (!TextUtils.isEmpty(address)) {
//                    txtAddress.setText(address);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getJobListForSync() {
//        showProgressDialog();
        Call<JsonElement> call = RestClientToken.get().getJoblistAllSync("Bearer " + AppConstants.TOKEN);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            JobAllSyncBean bean = new Gson().fromJson(response.body().toString(), JobAllSyncBean.class);
                            if (bean.getData() != null && bean.getData().size() > 0) {

                                for (int i = 0; i < bean.getData().size(); i++) {
                                    if (dbHandler.getCount() == 0) {
                                        dbHandler.addJob(sharedObjects.getUserId(), bean.getData().get(i));
                                    } else if (dbHandler.isJobExist(sharedObjects.getUserId(), bean.getData().get(i).getJoborderid())) {
                                        dbHandler.updateJob(sharedObjects.getUserId(), bean.getData().get(i), 0);
                                    } else {
                                        dbHandler.addJob(sharedObjects.getUserId(), bean.getData().get(i));
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }
}