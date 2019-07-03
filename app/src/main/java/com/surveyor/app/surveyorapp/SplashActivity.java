package com.surveyor.app.surveyorapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.surveyor.app.surveyorapp.bean.LoginBean;
import com.surveyor.app.surveyorapp.retrofit.RestClient;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String loginStatus;
    SharedObjects sharedObjects;
    String imei;
    private String manufacturer,model,versionRelease;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            imei = SharedObjects.getImeiNumber(SplashActivity.this);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedObjects = new SharedObjects(SplashActivity.this);
                loginStatus = sharedObjects.getPreference(AppConstants.STATUS);
                newlogin();
            }
        }, SPLASH_TIME_OUT);

    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e("Permissions", "All permissions are granted!");
                            // code to execute
                            imei = SharedObjects.getImeiNumber(SplashActivity.this);
                            //  txtIMEI.setText(getString(R.string.imei)+" "+imei);
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


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
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



    private void newlogin() {
//        showProgressDialog();

        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), imei);
//        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), "353419101599285");
        Log.e("reqIMEI==","oooo" + imei);
        Log.e("reqIMEI==","oooo" + reqIMEI);

        // "789898798"
        RestClient restClient = new RestClient(SplashActivity.this);
        Call<JsonElement> call = restClient.post().loginimeiCheck(imei);
//        Call<JsonElement> call = restClient.post().loginimeiCheck("789898798");
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try
                {
                    // dismissProgressDialog();
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("login:",">>"+ response.body().toString());

                    if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                        Intent intentLogin ;
                        /*if (TextUtils.isEmpty(loginStatus)) {
                            intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                        } else if (loginStatus.equals(AppConstants.STATUS_LOGOUT)) {
                            intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                        } else {
                            intentLogin = new Intent(SplashActivity.this, DashboardActivity.class);
                        }*/
                        intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentLogin);
                        finish();
                    }
                    else
                    {
                        insertImei();

                        Intent intent = new Intent(SplashActivity.this, PendingDeviceActivity.class);
                        intent.putExtra("imei",String.valueOf(imei));
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //  dismissProgressDialog();

                Log.e("loginerror:","==" + t);
                Log.e("loginerrorcall:","==" + call);
                Toast.makeText(SplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertImei() {
//        showProgressDialog();

        try {
            manufacturer = Build.MANUFACTURER;
            model = Build.MODEL;
            versionRelease = Build.VERSION.RELEASE;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), imei);
        Log.e("model==","oooo" + manufacturer+model+versionRelease);
        Log.e("reqIMEI==","oooo" + reqIMEI);

        RestClient restClient = new RestClient(SplashActivity.this);
        Call<JsonElement> call = restClient.post().insertUnRegisteredIMEI(reqIMEI,manufacturer,model,versionRelease,"","","");
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("logininsert:", ">>>>"+ response.body().toString());
                    Log.e("insertreg:",">>>>"+ jsonObject);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //  dismissProgressDialog();

                Log.e("loginerror:","==" + t);
                Log.e("loginerrorcall:","==" + call);
                Toast.makeText(SplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void login() {
////        showProgressDialog();
//
//        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), imei);
////        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), "353419101599285");
//        Log.e("reqIMEI==","oooo" + imei);
//        Log.e("reqIMEI==","oooo" + reqIMEI);
//
//        RestClient restClient = new RestClient(SplashActivity.this);
//        Call<JsonElement> call = restClient.post().login(reqIMEI);
//        call.enqueue(new Callback<JsonElement>()
//        {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                try
//                {
//                   // dismissProgressDialog();
//                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                    Log.e("login : ", response.body().toString());
//                    if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
//                        Intent intentLogin ;
//                        /*if (TextUtils.isEmpty(loginStatus)) {
//                            intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                        } else if (loginStatus.equals(AppConstants.STATUS_LOGOUT)) {
//                            intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                        } else {
//                            intentLogin = new Intent(SplashActivity.this, DashboardActivity.class);
//                        }*/
//                        intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intentLogin);
//                        finish();
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(SplashActivity.this, PendingDeviceActivity.class);
//                        intent.putExtra("imei",String.valueOf(imei));
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//              //  dismissProgressDialog();
//
//                Log.e("loginerror:","==" + t);
//                Log.e("loginerrorcall:","==" + call);
//                Toast.makeText(SplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            }
        }
    }
}