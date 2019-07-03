package com.surveyor.app.surveyorapp.reporting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.attendance.AttendanceReportAdapter;
import com.surveyor.app.surveyorapp.attendance.AttendanceReportFragment;
import com.surveyor.app.surveyorapp.bean.AttendanceReportBean;
import com.surveyor.app.surveyorapp.bean.ReportStatisticsBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 14-Oct-18.
 */

public class ReportingFragment extends Fragment {

    SharedObjects sharedObjects;

    @BindView(R.id.txtFromDate) TextView txtFromDate;
    @BindView(R.id.txtToDate) TextView txtToDate;
    @BindView(R.id.llFromDate) LinearLayout llFromDate;
    @BindView(R.id.llToDate) LinearLayout llToDate;
    @BindView(R.id.btnSubmit) ImageButton btnSubmit;
    @BindView(R.id.txtTotalReports) TextView txtTotalReports;
    @BindView(R.id.txtTotalOngoing) TextView txtTotalOngoing;
    @BindView(R.id.txtTotalSubmitted) TextView txtTotalSubmitted;
    @BindView(R.id.circularProgress) CircularProgressBar circularProgress;
    private ProgressDialog progressDialog;
    final int animationDuration = 3500;

    public ReportingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reporting, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());

        txtFromDate.setText(SharedObjects.getTodaysDate(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
        txtToDate.setText(SharedObjects.getTodaysDate(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

        if (SharedObjects.isNetworkConnected(getActivity())) {
            getAttendanceReport();
        } else {
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.btnSubmit)
    public void getReports(View view) {
        if (SharedObjects.isNetworkConnected(getActivity())) {
            getAttendanceReport();
        } else {
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }
    }

    @OnClick(R.id.llFromDate)
    public void openFromDate(View view) {
        showDateDialog("from");
    }

    @OnClick(R.id.llToDate)
    public void openToDate(View view) {
        showDateDialog("to");
    }

    public void getAttendanceReport() {
        showProgressDialog();

        String from = SharedObjects.convertDateFormat(txtFromDate.getText().toString()
                ,AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE,AppConstants.DateFormats.DF_REPORT_STATISTICS_API);

        String to = SharedObjects.convertDateFormat(txtToDate.getText().toString()
                ,AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE,AppConstants.DateFormats.DF_REPORT_STATISTICS_API);

        RequestBody reqFromDate = RequestBody.create(MediaType.parse("multipart/form-data"), from);
        RequestBody reqToDate = RequestBody.create(MediaType.parse("multipart/form-data"), to);

        Call<JsonElement> call = RestClientToken.post().getReportStatistics("Bearer " + AppConstants.TOKEN,
                reqFromDate,reqToDate);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            ReportStatisticsBean bean = new Gson().fromJson(response.body().toString(), ReportStatisticsBean.class);

                            if (bean.getData() != null) {
                                txtTotalOngoing.setText(bean.getData().getTotalongoing());
                                txtTotalReports.setText(bean.getData().getTotalreports());
                                txtTotalSubmitted.setText(bean.getData().getTotalsubmitted());

                                circularProgress.setProgressWithAnimation(Float.parseFloat("0"), animationDuration);

                            } else {
                                txtTotalOngoing.setText("0");
                                txtTotalReports.setText("0");
                                txtTotalSubmitted.setText("0");
                            }

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
                dismissProgressDialog();
                txtTotalOngoing.setText("0");
                txtTotalReports.setText("0");
                txtTotalSubmitted.setText("0");
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

    private void showDateDialog(final String type) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.datepicker_layout);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        String fromDate = txtFromDate.getText().toString().trim();
        String date[] = fromDate.split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]) - 1;
        int year = Integer.parseInt(date[2]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.YEAR, year);

        if (type.equalsIgnoreCase("to")) {
            datePicker.setMinDate(c.getTimeInMillis());
        }

        Button btnDismissDate = (Button) dialog.findViewById(R.id.btnDismissDate);
        btnDismissDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());

                String date = day + "-" + month + "-" + year;
                try {
                    Calendar calendar =  Calendar.getInstance();
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.YEAR,datePicker.getYear());

                    if (type.equals("from")){
                        txtFromDate.setText(SharedObjects.convertDateFormat(date,
                                AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
                    }else{
                        txtToDate.setText(SharedObjects.convertDateFormat(date,
                                AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }



}
