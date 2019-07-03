package com.surveyor.app.surveyorapp.attendance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.CaptureImageActivity;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.ReverseGeoBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by DELL on 14-Oct-18.
 */

public class AttendanceFragment extends Fragment {

    @BindView(R.id.imgAttendanceReport)
    ImageView imgAttendanceReport;
    @BindView(R.id.btnClockOut)
    Button btnClockOut;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.txtUserName)
    TextView txtUserName;

    SharedObjects sharedObjects;
    private ProgressDialog progressDialog;

    private static final int REQUEST_ATTENDANCE = 100;

    public AttendanceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        ButterKnife.bind(this, view);

        sharedObjects = new SharedObjects(getActivity());
        txtUserName.setText(sharedObjects.getUserInfo().getUsername());

        Picasso.with(getActivity()).load(sharedObjects.getUserInfo().getProfileimage()).
                placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgUser);

        checkAttendance();

        return view;
    }

    public void checkAttendance() {

        showProgressDialog();

        Call<JsonElement> call = RestClientToken.get().checkAttendance("Bearer " + AppConstants.TOKEN);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            if (jsonObject.getString(AppConstants.RESPONSE.data).equalsIgnoreCase(AppConstants.RESPONSE.True)){
                                sharedObjects.setPreference(AppConstants.CHECK_CLOCKIN,"true");
                            }else{
                                sharedObjects.setPreference(AppConstants.CHECK_CLOCKIN,"false");
                                startActivityForResult(new Intent(getActivity(),CaptureImageActivity.class),REQUEST_ATTENDANCE);
                            }
                        }
                    } else if (response.code() == 401) {
                        try {
                            DashboardActivity activity = DashboardActivity.instance;
                            if (activity != null) {
                                activity.logout();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ATTENDANCE){
            if (resultCode == RESULT_OK) {

                File myFile = (File)data.getSerializableExtra("image");

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath(),bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true);

                Canvas c = new Canvas(bitmap);
                c.drawBitmap(bitmap, 0, 0, null);

                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(20);
                c.drawText("Some Text", 0, 25, paint);

                if (sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN).equalsIgnoreCase("true")){
                   // check out
                    takeAttendanceOut(myFile);
                }else{
                    //check in
                    takeAttendanceIn(myFile,bitmap);
                }

            }else if (resultCode == RESULT_CANCELED){
                ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.DASHBOARD,new Bundle()/*""*/);
            }
        }
    }

    private void showAttendanceSuccessDialog(Bitmap bitmapNew) {

        final Handler handler = new Handler();

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialog_attendance_success);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final ImageView imgBack = (ImageView) dialog.findViewById(R.id.imgBack);
        final CircleImageView imgUserCapture = dialog.findViewById(R.id.imgUserCapture);
        imgUserCapture.setImageBitmap(bitmapNew);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 5000);
    }

    @OnClick(R.id.imgAttendanceReport)
    public void attendanceReport(View view) {
        ((DashboardActivity) getActivity()).loadFragment(new AttendanceReportFragment());
    }

    @OnClick(R.id.btnClockOut)
    public void clockOut(View view) {
        if (SharedObjects.isNetworkConnected(getActivity())) {
            File file = null;
            if (DashboardActivity.mCurrentLocation != null) {
                startActivityForResult(new Intent(getActivity(),CaptureImageActivity.class),REQUEST_ATTENDANCE);
//                takeAttendance(file);
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.cannot_fetch_location), Toast.LENGTH_SHORT).show();
            }
        } else {
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);
    }

    public void takeAttendanceIn(File file, final Bitmap bitmapNew) {
        showProgressDialog();
        MultipartBody.Part imgfile = null;

        if (file != null) {
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            imgfile = MultipartBody.Part.createFormData("imagefile", file.getName(), reqFile1);
        }

        RequestBody reqIsTimeout = RequestBody.create(MediaType.parse("multipart/form-data"), "false");
        RequestBody reqLatitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLatitude()+"");
        RequestBody reqLongitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLongitude()+"");
        RequestBody reqAddress = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.address);

        Call<JsonElement> call = RestClientToken.post().takeAttendance("Bearer " + AppConstants.TOKEN,
                reqIsTimeout, reqLatitude,
                reqLongitude, reqAddress, imgfile);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            showAttendanceSuccessDialog(bitmapNew);
                            sharedObjects.setPreference(AppConstants.CHECK_CLOCKIN,AppConstants.RESPONSE.True);
                        }
                    }else if (response.code() == 401){
                        try {
                            ((DashboardActivity) getActivity()).removeAllPreferenceOnLogout(sharedObjects);
                            Intent intent = new Intent(getActivity(),LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("takeAttendance - ", t.toString());
                dismissProgressDialog();
            }
        });
    }

    public void takeAttendanceOut(File file) {
        showProgressDialog();
        MultipartBody.Part imgfile = null;

        if (file != null) {
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            imgfile = MultipartBody.Part.createFormData("imagefile", file.getName(), reqFile1);
        }

        RequestBody reqIsTimeout = RequestBody.create(MediaType.parse("multipart/form-data"), "true");
        RequestBody reqLatitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLatitude()+"");
        RequestBody reqLongitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLongitude()+"");
        RequestBody reqAddress = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.address);

        Call<JsonElement> call = RestClientToken.post().takeAttendance("Bearer " + AppConstants.TOKEN,
                reqIsTimeout, reqLatitude,
                reqLongitude, reqAddress, imgfile);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            sharedObjects.setPreference(AppConstants.CHECK_CLOCKIN, AppConstants.RESPONSE.False);
                            ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.DASHBOARD,new Bundle()/*""*/);
                        }
                    }else if (response.code() == 401){
                        try {
                            ((DashboardActivity) getActivity()).removeAllPreferenceOnLogout(sharedObjects);
                            Intent intent = new Intent(getActivity(),LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("takeAttendance - ", t.toString());
                dismissProgressDialog();
            }
        });
    }



    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (!getActivity().isFinishing()) {
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


}
