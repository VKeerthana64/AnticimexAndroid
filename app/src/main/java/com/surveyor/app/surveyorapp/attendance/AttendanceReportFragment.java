package com.surveyor.app.surveyorapp.attendance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.AttendanceReportBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 14-Oct-18.
 */

public class AttendanceReportFragment extends Fragment {

    @BindView(R.id.rvAttendanceReport)
    RecyclerView rvAttendanceReport;
    @BindView(R.id.txtToDate)
    TextView txtToDate;
    @BindView(R.id.txtFromDate)
    TextView txtFromDate;
    @BindView(R.id.llFromDate)
    LinearLayout llFromDate;
    @BindView(R.id.llToDate)
    LinearLayout llToDate;
    @BindView(R.id.txtError)
    TextView txtError;

    private ProgressDialog progressDialog;

    ArrayList<AttendanceReportBean.Data> arrAttendanceReport = new ArrayList<>();
    ArrayList<AttendanceReportBean.Data> arrAttendanceReportTemp = new ArrayList<>();

    SharedObjects sharedObjects;

    public AttendanceReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_attendance_report, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());

        txtToDate.setText(SharedObjects.getTodaysDate(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

        String toDate = txtToDate.getText().toString();
        String splitDate[] = toDate.split("/");
        Calendar calendar = Calendar.getInstance();

        int month = Integer.parseInt(splitDate[1]) - 1;
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        calendar.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));

        DateFormat df = new SimpleDateFormat(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);
        String strFromDate = df.format(getPast7DaysDate(calendar));
        txtFromDate.setText(strFromDate);

        if (SharedObjects.isNetworkConnected(getActivity())) {
            getAttendanceReport();
        } else {
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }

        return view;
    }

    @OnClick(R.id.llFromDate)
    public void openFromDate(View view) {
        showDateDialog("from");
    }

    @OnClick(R.id.llToDate)
    public void openToDate(View view) {
        showDateDialog("to");
    }

    @OnClick(R.id.txtShow)
    public void filterData(View view) {
        arrAttendanceReportTemp = new ArrayList<>();
        if (arrAttendanceReport.size() > 0) {
            for (int i = 0; i < arrAttendanceReport.size(); i++) {
                if (checkDate(arrAttendanceReport.get(i).getDate())) {
                    arrAttendanceReportTemp.add(arrAttendanceReport.get(i));
                }
            }
            Log.e("Sizr : ", arrAttendanceReportTemp.size() + "");
            setAttendanceReportAdapter();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getAttendanceReport() {
        showProgressDialog();
        Call<JsonElement> call = RestClientToken.post().getAttendanceReport("Bearer " + AppConstants.TOKEN);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            AttendanceReportBean bean = new Gson().fromJson(response.body().toString(), AttendanceReportBean.class);
                            if (bean.getData() != null) {
                                arrAttendanceReport = new ArrayList<>();
                                arrAttendanceReportTemp = new ArrayList<>();
                                arrAttendanceReport = bean.getData();
                                arrAttendanceReportTemp = bean.getData();
                            } else {
                                arrAttendanceReport = new ArrayList<>();
                                arrAttendanceReportTemp = new ArrayList<>();
                            }
                            setAttendanceReportAdapter();
                        }
                    } else if (response.code() == 401) {
                        try {
                            ((DashboardActivity) getActivity()).removeAllPreferenceOnLogout(sharedObjects);

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
                arrAttendanceReport = new ArrayList<>();
                arrAttendanceReportTemp = new ArrayList<>();
                setAttendanceReportAdapter();
            }
        });
    }

    private void setAttendanceReportAdapter() {
        if (arrAttendanceReportTemp.size() > 0) {
            AttendanceReportAdapter adapter = new AttendanceReportAdapter(arrAttendanceReportTemp, getActivity());
            rvAttendanceReport.setAdapter(adapter);
            hideShowView(true);
        } else {
            hideShowView(false);
        }
    }

    public void hideShowView(boolean viewFlag) {
        rvAttendanceReport.setVisibility(viewFlag ? View.VISIBLE : View.GONE);
        txtError.setVisibility(viewFlag ? View.GONE : View.VISIBLE);
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

        Button btnDismissDate = (Button) dialog.findViewById(R.id.btnDismissDate);
        btnDismissDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());

                String date = day + "-" + month + "-" + year;
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    if (type.equals("from")) {
                        txtFromDate.setText(SharedObjects.convertDateFormat(date,
                                AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

                        DateFormat df = new SimpleDateFormat(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);
                        String strToDate = df.format(getFuture7DaysDate(calendar));
                        txtToDate.setText(strToDate);
                    } else {
                        txtToDate.setText(SharedObjects.convertDateFormat(date,
                                AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

                        DateFormat df = new SimpleDateFormat(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);
                        String strFromDate = df.format(getPast7DaysDate(calendar));
                        txtFromDate.setText(strFromDate);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public Date getPast7DaysDate(Calendar calendar) {
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

    public Date getFuture7DaysDate(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        calendar.add(Calendar.DATE, +7);
        return calendar.getTime();
    }

    public boolean checkDate(String date) {
        String fromDate = txtFromDate.getText().toString();
        String toDate = txtToDate.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);
        try {
            Date fDate = sdf.parse(fromDate);
            Date tDate = sdf.parse(toDate);
            Date givenDate = sdf.parse(date);

            if (givenDate.after(fDate) && givenDate.before(tDate) || givenDate.equals(tDate)) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

}
