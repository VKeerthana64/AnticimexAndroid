package com.surveyor.app.surveyorapp.jobs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobBean;
import com.surveyor.app.surveyorapp.bean.JobTypeBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 14-Oct-18.
 */

public class JobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SharedObjects sharedObjects;
    @BindView(R.id.txtToDate)
    TextView txtToDate;
    @BindView(R.id.llToDate)
    LinearLayout llToDate;
    @BindView(R.id.btnSubmit)
    ImageButton btnSubmit;
    @BindView(R.id.rvJobs)
    RecyclerView rvJobs;
    @BindView(R.id.spnType)
    Spinner spnType;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.cbShowAll)
    CheckBox cbShowAll;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private View view;
    private ProgressDialog progressDialog;

    ArrayList<JobAllSyncDataBean> arrJobs = new ArrayList<>();
    ArrayList<JobAllSyncDataBean> arrJobsTemp = new ArrayList<>();
    ArrayList<JobTypeBean.Data> arrJobType = new ArrayList<>();

    int year, month, day;

    DBHandler dbHandler;

    String jobTypeId = "" ;

    public JobsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());
        dbHandler = new DBHandler(getActivity());

        swipeContainer.setOnRefreshListener(this);
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        txtToDate.setText(SharedObjects.getTodaysDate(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

        if (SharedObjects.isNetworkConnected(getActivity())) {

        } else {
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }

        if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.OFFLINE.JOB_TYPE))) {
            Type type = new TypeToken<ArrayList<JobTypeBean.Data>>() {
            }.getType();
            arrJobType = new ArrayList<>();
            arrJobType = new Gson().fromJson(sharedObjects.getPreference(AppConstants.OFFLINE.JOB_TYPE), type);
            setJobTypeAdapter();

            jobTypeId = arrJobType.get(0).getJobtypeid() ;

            getDataFromLocal();
            filterData(jobTypeId); // filter date
        }

        cbShowAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   getDataFromLocal();
                   setMyAdapter();

                } else {
                    getDataFromLocal();
                    filterData(jobTypeId);

                }
            }
        });

        return view;
    }



    private void getDataFromLocal() {
        arrJobs = new ArrayList<>();
        arrJobsTemp = new ArrayList<>();
        if (dbHandler.getCount() > 0){
            arrJobs = dbHandler.getAllJobs(sharedObjects.getUserId());
            arrJobsTemp = dbHandler.getAllJobs(sharedObjects.getUserId());
        }
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_search);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);

        final EditText edtServiceDate = (EditText) dialog.findViewById(R.id.edtServiceDate);
        edtServiceDate.setInputType(InputType.TYPE_NULL);

        final EditText edtClientName = (EditText) dialog.findViewById(R.id.edtClientName);
        final EditText edtLocationName = (EditText) dialog.findViewById(R.id.edtLocationName);
        final EditText edtServiceAddress = (EditText) dialog.findViewById(R.id.edtServiceAddress);
        Button btnSearch = (Button) dialog.findViewById(R.id.btnSearch);

        edtServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDateDialog(edtServiceDate);
            }
        });

        //  Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedObjects.isNetworkConnected(getActivity())) {
                    if (!TextUtils.isEmpty(edtClientName.getText().toString()) || !TextUtils.isEmpty(edtLocationName.getText().toString()) ||
                            !TextUtils.isEmpty(edtServiceDate.getText().toString()) || !TextUtils.isEmpty(edtServiceAddress.getText().toString())) {
                        dialog.dismiss();
                        searchJobList(edtClientName.getText().toString().trim(), edtLocationName.getText().toString().trim(),
                                edtServiceDate.getText().toString().trim(), edtServiceAddress.getText().toString().trim());
                    } else {
                        Toast.makeText(getActivity(), "Please enter search criteria", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
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

    @OnClick({R.id.llToDate, R.id.btnSubmit})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.llToDate:
                cbShowAll.setChecked(false);
                showDateDialog();
                break;
            case R.id.btnSubmit:
                showDialog(getActivity(), "");
                break;
        }
    }


    private void setJobTypeAdapter() {
        ArrayAdapter adapter = null;
        try {
            adapter = new ArrayAdapter(getActivity(), R.layout.spn_jobtype, arrJobType) {
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                    return view;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        spnType.setAdapter(adapter);
//        spnType.setSelection(0, false);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cbShowAll.setChecked(false);
                jobTypeId = arrJobType.get(position).getJobtypeid() ;
                filterData(jobTypeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    public void searchJobList(String customername, String locationname, String scheduledate, String serviceaddress) {

        showProgressDialog();

        String scheduledateFormat = "";
        if (!TextUtils.isEmpty(scheduledate)) {
            scheduledateFormat = SharedObjects.convertDateFormat(scheduledate
                    , AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE, AppConstants.DateFormats.DF_REPORT_STATISTICS_API);
        }

        RequestBody reqCustName;
        if (!TextUtils.isEmpty(customername)) {
            reqCustName = RequestBody.create(MediaType.parse("multipart/form-data"), customername);
        } else {
            reqCustName = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        }

        RequestBody reqLocName;
        if (!TextUtils.isEmpty(locationname)) {
            reqLocName = RequestBody.create(MediaType.parse("multipart/form-data"), locationname);
        } else {
            reqLocName = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        }

        RequestBody reqSerAddress;
        if (!TextUtils.isEmpty(serviceaddress)) {
            reqSerAddress = RequestBody.create(MediaType.parse("multipart/form-data"), serviceaddress);
        } else {
            reqSerAddress = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        }

        RequestBody reqSchDate = RequestBody.create(MediaType.parse("multipart/form-data"), scheduledateFormat);

        Call<JsonElement> call = RestClientToken.post().searchJobList("Bearer " + AppConstants.TOKEN,
                reqCustName, reqLocName, reqSchDate, reqSerAddress);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            JobBean bean = new Gson().fromJson(response.body().toString(), JobBean.class);
                            if (bean.getData() != null) {
                                arrJobsTemp = new ArrayList<>();
//                                arrJobsTemp = bean.getData();
                            } else {
                                arrJobsTemp = new ArrayList<>();
                            }
                            setMyAdapter();
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
                arrJobsTemp = new ArrayList<>();
                setMyAdapter();
            }
        });
    }





    private void setMyAdapter() {
        if (arrJobsTemp.size() > 0) {
            JobsAdapter adapter = new JobsAdapter(arrJobsTemp, getActivity());
            rvJobs.setAdapter(adapter);
            hideShowView(true);
        } else {
            hideShowView(false);
        }
    }

    public void hideShowView(boolean viewFlag) {
        rvJobs.setVisibility(viewFlag ? View.VISIBLE : View.GONE);
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

    private void showDateDialog() {
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
//        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
//        datePicker.getTouchables().get(0).performClick();

        datePicker.init(year, month, day, null);

        //openYearView(datePicker);

        Button btnDismissDate = (Button) dialog.findViewById(R.id.btnDismissDate);
        btnDismissDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();

                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());

                String date = day + "-" + month + "-" + year;
                try {
                    txtToDate.setText(SharedObjects.convertDateFormat(date,
                            AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));

                    filterData(jobTypeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void filterData(String jobTypeId) {
        arrJobsTemp = new ArrayList<>();
        if (arrJobs.size() > 0) {
            for (int i = 0; i < arrJobs.size(); i++) {
                /*if (type.equalsIgnoreCase("date")) {
                    if (checkDate(arrJobs.get(i).getScheduledate()) && arrJobs.get(i).getJoborderid().equalsIgnoreCase(jobTypeId)) {
                        arrJobsTemp.add(arrJobs.get(i));
                    }
                } else if (type.equalsIgnoreCase("jobtypeid")) {
                    if (arrJobs.get(i).getJobtypeid().equalsIgnoreCase(jobTypeId)) {
                        arrJobsTemp.add(arrJobs.get(i));
                    }
                }*/

                if (checkDate(arrJobs.get(i).getScheduledate()) && arrJobs.get(i).getJobtypeid().equalsIgnoreCase(jobTypeId)) {
                    arrJobsTemp.add(arrJobs.get(i));
                }
            }

        }
        setMyAdapter();
    }

    public boolean checkDate(String date) {
        String strToDate = txtToDate.getText().toString();

//        String strGivenDate = date;
     /*   String strGivenDate = SharedObjects.convertDateFormat(date,
                AppConstants.DateFormats.DF_JOB_API, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);*/

        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE);
        try {
            Date tDate = sdf.parse(strToDate);
            Date givenDate = sdf.parse(date);

            if (givenDate.equals(tDate)) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showSearchDateDialog(final EditText edt) {
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
//        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        Button btnDismissDate = (Button) dialog.findViewById(R.id.btnDismissDate);
        btnDismissDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());

                String date = day + "-" + month + "-" + year;
                try {
                    edt.setText(SharedObjects.convertDateFormat(date,
                            AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRefresh() {
        if (SharedObjects.isNetworkConnected(getActivity())) {
//            getJobListScheduled();
            getJobListForSync();
           // getDataFromLocal(); Commented by parth
            filterData(jobTypeId); // filter date
        } else {
            if (swipeContainer.isRefreshing()) {
                swipeContainer.setRefreshing(false);
            }
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }
    }

//New added from service
    public void getJobListForSync() {
        Call<JsonElement> call = RestClientToken.get().getJoblistAllSync("Bearer " + AppConstants.TOKEN);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (swipeContainer.isRefreshing()) {
                            swipeContainer.setRefreshing(false);
                        }
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            JobAllSyncBean bean = new Gson().fromJson(response.body().toString(), JobAllSyncBean.class);
                            if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.LOGIN_BEAN))) {
                                if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
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
                                } else {
                                    if (bean.getData() != null && bean.getData().size() > 0) {
                                        for (int i = 0; i < bean.getData().size(); i++) {
                                            if (dbHandler.getCountTL() == 0) {
                                                dbHandler.addJobTL(sharedObjects.getUserId(), bean.getData().get(i));
                                            } else if (dbHandler.isJobExistTL(sharedObjects.getUserId(), bean.getData().get(i).getJoborderid())) {
                                                dbHandler.updateJobTLMain(sharedObjects.getUserId(), bean.getData().get(i), 0, 0);
                                            } else {
                                                dbHandler.addJobTL(sharedObjects.getUserId(), bean.getData().get(i));
                                            }
                                        }
                                    }
                                }
                            }

                            getDataFromLocal();
                            filterData(jobTypeId);

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
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }


}
