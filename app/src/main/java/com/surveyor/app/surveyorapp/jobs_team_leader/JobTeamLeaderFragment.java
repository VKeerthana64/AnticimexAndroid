package com.surveyor.app.surveyorapp.jobs_team_leader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.surveyor.app.surveyorapp.bean.JobTeamLeaderBean;
import com.surveyor.app.surveyorapp.bean.JobTypeBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberDataBean;
import com.surveyor.app.surveyorapp.jobs.JobsAdapter;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

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

public class JobTeamLeaderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rvJobs)
    RecyclerView rvJobs;
    @BindView(R.id.txtError)
    TextView txtError;

    @BindView(R.id.txtToDate)
    TextView txtToDate;
    @BindView(R.id.llToDate)
    LinearLayout llToDate;
    @BindView(R.id.btnSubmit)
    ImageButton btnSubmit;
    @BindView(R.id.spnType)
    Spinner spnType;
    @BindView(R.id.cbShowAll)
    CheckBox cbShowAll;

    SharedObjects sharedObjects;

    //    ArrayList<JobTeamLeaderBean.Data> arrJobs = new ArrayList<>();
    ArrayList<JobAllSyncDataBean> arrJobs = new ArrayList<>();
    ArrayList<JobAllSyncDataBean> arrJobsTemp = new ArrayList<>();
    ArrayList<JobTypeBean.Data> arrJobType = new ArrayList<>();

    ArrayList<TeamMemberDataBean> arrTeamMember = new ArrayList<>();
    ArrayList<TeamMemberDataBean> arrTeamMemberTemp = new ArrayList<>();
    private ProgressDialog progressDialog;
    TeamMemberAdapter teamMemberAdapter;
    JobTeamLeaderAdapter jobTeamLeaderAdapter;

    DBHandler dbHandler;

    int year, month, day;
    String jobTypeId = "";

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    public JobTeamLeaderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_job_team_leader, container, false);
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
            JobTypeBean jobTypeBean = new JobTypeBean();
            JobTypeBean.Data data = jobTypeBean.new Data();
            data.setJobtypename("Select job type");
            data.setJobtypeid("0");
            arrJobType.add(data);

            ArrayList<JobTypeBean.Data> arrJobTypeDummy = new Gson().fromJson(sharedObjects.getPreference(AppConstants.OFFLINE.JOB_TYPE), type);

            arrJobType.addAll(arrJobTypeDummy);
            setJobTypeAdapter();

            jobTypeId = "";

            getDataFromLocal();
            filterData(jobTypeId); // filter date
        }


        if (TextUtils.isEmpty(jobTypeId)) {

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

    @Override
    public void onRefresh() {

        if (SharedObjects.isNetworkConnected(getActivity())) {
            getJobListForSync();
        } else {
            if (swipeContainer.isRefreshing()) {
                swipeContainer.setRefreshing(false);
            }
            SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
        }

    }


    private void getDataFromLocal() {
        arrJobs = new ArrayList<>();
        arrJobsTemp = new ArrayList<>();
        if (dbHandler.getCountTL() > 0) {
            arrJobs = dbHandler.getAllJobsTL(sharedObjects.getUserId());
            arrJobsTemp = dbHandler.getAllJobsTL(sharedObjects.getUserId());
        }
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
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
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cbShowAll.setChecked(false);
                if (position == 0) {
                    jobTypeId = "";
                } else {
                    jobTypeId = arrJobType.get(position).getJobtypeid();
                }
                filterData(jobTypeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                if (!TextUtils.isEmpty(jobTypeId)) {
                    if (checkDate(arrJobs.get(i).getScheduledate()) && arrJobs.get(i).getJobtypeid().equalsIgnoreCase(jobTypeId)) {
                        arrJobsTemp.add(arrJobs.get(i));
                    }
                } else {
                    if (checkDate(arrJobs.get(i).getScheduledate())) {
                        arrJobsTemp.add(arrJobs.get(i));
                    }
                }

            }
        }
        setMyAdapter();
    }

    public boolean checkDate(String date) {
        String strToDate = txtToDate.getText().toString();

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

    private void setMyAdapter() {
        if (arrJobsTemp.size() > 0) {
            jobTeamLeaderAdapter = new JobTeamLeaderAdapter(arrJobsTemp, getActivity());
            rvJobs.setAdapter(jobTeamLeaderAdapter);

            jobTeamLeaderAdapter.setOnItemClickListener(new JobTeamLeaderAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position, JobAllSyncDataBean bean) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.INTENT_BUNDLE, bean);
                    ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.SERVICE_REPORTING, bundle);
                }
            });

            jobTeamLeaderAdapter.setOnStatusClickListener(new JobTeamLeaderAdapter.OnStatusClickListener() {
                @Override
                public void onStatusClickListener(int position, JobAllSyncDataBean bean) {
//                    getTeamMembers(bean.getJo_scheduleid(), position);

                    arrTeamMember = new ArrayList<>();
                    arrTeamMemberTemp = new ArrayList<>();
                    arrTeamMember = bean.getSurveyteammember(); //show from local DB based on job only
                    arrTeamMemberTemp.addAll(arrTeamMember);
                    showTeamMemberDialog(position, bean);
                }
            });
            hideShowView(true);
        } else {
            hideShowView(false);
        }
    }

    private void showTeamMemberDialog(final int jobPosition, final JobAllSyncDataBean beanMain) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_team_member);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final EditText edtSearch = dialog.findViewById(R.id.edtSearch);
        final CheckBox cbAll = dialog.findViewById(R.id.cbAll);
        final RecyclerView rvTeam = dialog.findViewById(R.id.rvTeam);

        teamMemberAdapter = new TeamMemberAdapter(arrTeamMemberTemp, getActivity());
        rvTeam.setAdapter(teamMemberAdapter);

        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.OFFLINE.ALL_TECHNICIAN))) {
                        Type type = new TypeToken<ArrayList<TeamMemberDataBean>>() {
                        }.getType();
//                        arrTeamMember = new ArrayList<>();
                        ArrayList<TeamMemberDataBean> arrAllTeamMember = new Gson().fromJson(sharedObjects.getPreference(AppConstants.OFFLINE.ALL_TECHNICIAN), type);

                        for (int i = 0; i < arrAllTeamMember.size(); i++) {
                            if (checkTeamMembers(beanMain, arrAllTeamMember.get(i).getId())) {
                                if (checkTeamMembersAssign(beanMain, arrAllTeamMember.get(i).getId())) {
                                        arrAllTeamMember.get(i).setAssigned(true);
                                }else{
                                    arrAllTeamMember.get(i).setAssigned(false);
                                }
                            }else{
                                arrAllTeamMember.get(i).setAssigned(false);
                            }
                        }
                        arrTeamMemberTemp.clear();
                        arrTeamMemberTemp.addAll(arrAllTeamMember);

                        Log.e("Teams", arrTeamMemberTemp.size() + "");
                    } // show all technician
                } else {
                    arrTeamMemberTemp.clear();
                    arrTeamMemberTemp.addAll(beanMain.getSurveyteammember());

                    Log.e("Teams else", arrTeamMemberTemp.size() + "");
//                    arrTeamMember = beanMain.getSurveyteammember(); //show from local DB based on job only
                }

                if (teamMemberAdapter != null) {
                    teamMemberAdapter.notifyDataSetChanged();
                }
            }
        });

        teamMemberAdapter.setOnItemClickListener(new TeamMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, TeamMemberDataBean bean) {
                if (bean.isAssigned()) {
                    if (SharedObjects.isNetworkConnected(getActivity())) {
                        removeTeamMembers(beanMain, bean, position, jobPosition);
                    } else {
                        bean.setAssigned(false);
                        bean.setIsSync(1);
                        arrTeamMemberTemp.set(position, bean);

                        arrTeamMember = new ArrayList<>();
                        for (int i = 0; i < arrTeamMemberTemp.size(); i++) {
                            if (arrTeamMemberTemp.get(i).isAssigned()){
                                arrTeamMember.add(arrTeamMemberTemp.get(i));
                            }
                        }

                        beanMain.setSurveyteammember(arrTeamMember);
                        arrJobs.set(jobPosition, beanMain);
                        dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 1);
                        teamMemberAdapter.notifyDataSetChanged();
                        jobTeamLeaderAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (SharedObjects.isNetworkConnected(getActivity())) {
                        addTeamMembers(beanMain, bean, position, jobPosition);
                    } else {
                        bean.setAssigned(true);
                        bean.setIsSync(1);
                        arrTeamMemberTemp.set(position, bean);

                        arrTeamMember = new ArrayList<>();
                        for (int i = 0; i < arrTeamMemberTemp.size(); i++) {
                            if (arrTeamMemberTemp.get(i).isAssigned()){
                                arrTeamMember.add(arrTeamMemberTemp.get(i));
                            }
                        }

                        beanMain.setSurveyteammember(arrTeamMember);
                        arrJobs.set(jobPosition, beanMain);
                        dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 1);
                        teamMemberAdapter.notifyDataSetChanged();
                        jobTeamLeaderAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (teamMemberAdapter != null) {
                    teamMemberAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
    }

    private boolean checkTeamMembers(JobAllSyncDataBean beanMain, String id) {
        for (int i = 0; i < beanMain.getSurveyteammember().size(); i++) {
            if (beanMain.getSurveyteammember().get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTeamMembersAssign(JobAllSyncDataBean beanMain, String id) {
        for (int i = 0; i < beanMain.getSurveyteammember().size(); i++) {
            if (beanMain.getSurveyteammember().get(i).isAssigned()) {
                return true;
            }
        }
        return false;
    }

    public void addTeamMembers(final JobAllSyncDataBean beanMain, final TeamMemberDataBean bean, final int position, final int jobPosition) {
        showProgressDialog();

        RequestBody reqJo_scheduleid = RequestBody.create(MediaType.parse("multipart/form-data"), beanMain.getJo_scheduleid());
        RequestBody reqUserId = RequestBody.create(MediaType.parse("multipart/form-data"), bean.getId());

        Call<JsonElement> call = RestClientToken.post().addTeamMembers("Bearer " + AppConstants.TOKEN,
                reqJo_scheduleid, reqUserId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            bean.setAssigned(true);
                            bean.setIsSync(0);
                            arrTeamMemberTemp.set(position, bean);

                            arrTeamMember = new ArrayList<>();
                            for (int i = 0; i < arrTeamMemberTemp.size(); i++) {
                                if (arrTeamMemberTemp.get(i).isAssigned()){
                                    arrTeamMember.add(arrTeamMemberTemp.get(i));
                                }
                            }

                            beanMain.setSurveyteammember(arrTeamMember);
                            arrJobs.set(jobPosition, beanMain);
                            dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    teamMemberAdapter.notifyDataSetChanged();
                                    jobTeamLeaderAdapter.notifyDataSetChanged();
                                }
                            }, 1000);


                        }

                        Toast.makeText(getActivity(), jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), getActivity().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeTeamMembers(final JobAllSyncDataBean beanMain, final TeamMemberDataBean bean, final int position, final int jobPosition) {
        showProgressDialog();

        RequestBody reqJo_scheduleid = RequestBody.create(MediaType.parse("multipart/form-data"), beanMain.getJo_scheduleid());
        RequestBody reqUserId = RequestBody.create(MediaType.parse("multipart/form-data"), bean.getId());

        Call<JsonElement> call = RestClientToken.post().removeTeamMembers("Bearer " + AppConstants.TOKEN,
                reqJo_scheduleid, reqUserId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            bean.setAssigned(false);
                            bean.setIsSync(0);
                            arrTeamMemberTemp.set(position, bean);

                            arrTeamMember = new ArrayList<>();
                            for (int i = 0; i < arrTeamMemberTemp.size(); i++) {
                                if (arrTeamMemberTemp.get(i).isAssigned()){
                                    arrTeamMember.add(arrTeamMemberTemp.get(i));
                                }
                            }

                            beanMain.setSurveyteammember(arrTeamMember);
                            arrJobs.set(jobPosition, beanMain);
                            dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    teamMemberAdapter.notifyDataSetChanged();
                                    jobTeamLeaderAdapter.notifyDataSetChanged();
                                }
                            }, 1000);


                        } else {

                        }
                        Toast.makeText(getActivity(), jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), getActivity().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
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
