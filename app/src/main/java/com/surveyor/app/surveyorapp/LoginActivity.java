package com.surveyor.app.surveyorapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    SharedObjects sharedObjects;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.txtForgotPassword)
    TextView txtForgotPassword;
    @BindView(R.id.txtIMEI)
    TextView txtIMEI;
    @BindView(R.id.txtVersionNo)
    TextView txtVersionNo;

    private ProgressDialog progressDialog;
    private static final int PERMISSION_REQUEST_CODE = 1;

    String imei;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initView();
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edtPassword.setOnEditorActionListener(this);

        /*edtUsername.setText("technician2");
        edtUsername.setText("newtechnician");*/
//        edtUsername.setText("teamleader2@yopmail.com");
//        edtPassword.setText("test");

        /*edtUsername.setText("christianteamleader");
        edtPassword.setText("12345");*/

        //todo
        //kenneth.sam@anticimex.com.sg
        //password@123

        txtVersionNo.setText(getResources().getString(R.string.versionNo, AppConstants.getVersionNo(LoginActivity.this)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            imei = SharedObjects.getImeiNumber(LoginActivity.this);
            txtIMEI.setText(getString(R.string.imei) + " " + AppConstants.generateIMEI(imei));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.txtForgotPassword)
    public void forgotPassword(View view) {
        if (SharedObjects.isNetworkConnected(LoginActivity.this)) {
            /*Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();*/
        } else {
            SharedObjects.showAlertDialog(LoginActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
        }
    }

    @OnClick(R.id.btnLogin)
    public void checkLogin(View view) {
        if (SharedObjects.isNetworkConnected(LoginActivity.this)) {
            if (!validateUsername(edtUsername)) {
            } else if (!validatePassword(edtPassword)) {
            } else {
                sharedObjects.setPreference(AppConstants.USERNAME, edtUsername.getText().toString());
                sharedObjects.setPreference(AppConstants.PASSWORD, edtPassword.getText().toString());
                login();
            }
        } else {
            SharedObjects.showAlertDialog(LoginActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
        }
    }

    private void login() {
        showProgressDialog();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        RequestBody reqUsername = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody reqPassword = RequestBody.create(MediaType.parse("multipart/form-data"), password);

        RestClient restClient = new RestClient(LoginActivity.this);
        Call<JsonElement> call = restClient.post().insertUserDetails(reqUsername,reqPassword);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try
                {
                    dismissProgressDialog();
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("login : ", response.body().toString());
                    if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                        sharedObjects.setPreference(AppConstants.LOGIN_BEAN, response.body().toString());
                        sharedObjects.setPreference(AppConstants.STATUS, AppConstants.STATUS_LOGIN);

                        LoginBean.Userinfo userinfo = sharedObjects.getUserInfo();
                        new ImageDownloader().execute(userinfo.getProfileimage());
//                        getBitmapFromURL(userinfo.getProfileimage());

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, PendingDeviceActivity.class);
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
                dismissProgressDialog();

                Log.e("loginerror:","==" + t);
                Log.e("loginerrorcall:","==" + call);
                Toast.makeText(LoginActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void login() {
//        showProgressDialog();
//
//        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), imei);
////        RequestBody reqIMEI = RequestBody.create(MediaType.parse("multipart/form-data"), "353419101599285");
//        Log.e("reqIMEI==","oooo" + imei);
//        Log.e("reqIMEI==","oooo" + reqIMEI);
//
//        RestClient restClient = new RestClient(LoginActivity.this);
//        Call<JsonElement> call = restClient.post().login(reqIMEI);
//        call.enqueue(new Callback<JsonElement>()
//        {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                try
//                {
//                    dismissProgressDialog();
//                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                    Log.e("login : ", response.body().toString());
//                    if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
//
//                        sharedObjects.setPreference(AppConstants.LOGIN_BEAN, response.body().toString());
//                        sharedObjects.setPreference(AppConstants.STATUS, AppConstants.STATUS_LOGIN);
//
//                        LoginBean.Userinfo userinfo = sharedObjects.getUserInfo();
//                        new ImageDownloader().execute(userinfo.getProfileimage());
////                        getBitmapFromURL(userinfo.getProfileimage());
//
//                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                        finish();
//                    }
//                    else
//                    {
//                        Toast.makeText(LoginActivity.this, jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(LoginActivity.this, PendingDeviceActivity.class);
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
//                dismissProgressDialog();
//
//                Log.e("loginerror:","==" + t);
//                Log.e("loginerrorcall:","==" + call);
//                Toast.makeText(LoginActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            return getBitmapFromURL(param[0]);
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("-Example", "onPostExecute Called" + result);
            if (result != null) {
                sharedObjects.saveProfilePic(result);
            }
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 160, 160, true);

                return resized;
            } catch (ConnectException ce) {
                ce.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if (myBitmap != null) {
                sharedObjects.saveProfilePic(myBitmap);
            }
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean validateUsername(EditText editText) {
        String email = editText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editText.setError(getResources().getString(R.string.errUsernameRequired));
            requestFocus(editText);
            return false;
        }
        return true;
    }

    private boolean validatePassword(EditText editText) {
        String pass = editText.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            editText.setError(getResources().getString(R.string.errPasswordRequired));
            requestFocus(editText);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void initView() {
        sharedObjects = new SharedObjects(LoginActivity.this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            SharedObjects.hideKeyboard(btnLogin, LoginActivity.this);
            checkLogin(btnLogin);
            return true;
        }
        return false;
    }

    public void showProgressDialog() {
        try {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            if (!LoginActivity.this.isFinishing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
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
                            imei = SharedObjects.getImeiNumber(LoginActivity.this);
                            //  txtIMEI.setText(getString(R.string.imei)+" "+imei);
                            txtIMEI.setText(getString(R.string.imei) + " " + AppConstants.generateIMEI(imei));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            }
        }
    }
}
