package com.surveyor.app.surveyorapp.jobs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncAllSurveyBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobBean;
import com.surveyor.app.surveyorapp.bean.ServiceReportBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.SharedObjects;
import com.surveyor.app.surveyorapp.utils.SimpleTooltip;
import com.surveyor.app.surveyorapp.utils.SimpleTooltipUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceReportFragment extends Fragment {

    @BindView(R.id.rvServiceReport)
    RecyclerView rvServiceReport;
    private View view;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.txtHelp)
    TextView txtHelp;
    @BindView(R.id.imgSync)
    ImageView imgSync;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    private ProgressDialog progressDialog;

    ArrayList<JobAllSyncAllSurveyBean> arrServiceReport = new ArrayList<>();
    SharedObjects sharedObjects;
    String jo_scheduleid;
    //    JobBean.Data bean;
    JobAllSyncDataBean beanMain;
    ServiceReportAdapter adapter;
    DBHandler dbHandler;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_report, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());
        dbHandler = new DBHandler(getActivity());

        Bundle bundle = this.getArguments();
        //  jo_scheduleid = bundle.getString(AppConstants.INTENT_BUNDLE);
        if (bundle != null) {
            beanMain = bundle.getParcelable(AppConstants.INTENT_BUNDLE);
            if (beanMain != null) {
                jo_scheduleid = beanMain.getJo_scheduleid();
                if (beanMain.getAllSurvey() != null && beanMain.getAllSurvey().size() > 0) {
                    arrServiceReport = new ArrayList<>();
                    for (int i = 0; i < beanMain.getAllSurvey().size(); i++) {
                        if (beanMain.getAllSurvey().get(i).getIsSync() != 2){
                            arrServiceReport.add(beanMain.getAllSurvey().get(i));
                        }
                    }
                }
                setMyAdapter();
            }
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
//        setHasOptionsMenu(true);
    }

    @OnClick(R.id.txtHelp)
    public void showToolTip(View view) {
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(getActivity())
                .anchorView(view)
                //.text(R.string.btn_modal_custom)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(false)
                .modal(true)
                .animated(false)
                /* .animationDuration(2000)
                 .animationPadding(SimpleTooltipUtils.pxFromDp(50))*/
                .contentView(R.layout.tooltip_custom)
                .focusable(true)
                .build();
        tooltip.show();
    }

    @OnClick(R.id.btnAdd)
    public void addSurvey(View view) {
//        bean.setFormId("");
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.INTENT_BUNDLE, beanMain);
        bundle.putString(AppConstants.INTENT_FORM_ID, "0");
        bundle.putInt(AppConstants.INTENT_FORM_POS, -1);
        //   ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.SURVEY_FORM,jo_scheduleid);
        ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.SURVEY_FORM, bundle);
    }

    @OnClick(R.id.imgSync)
    public void startSyncing(View view) {
    }


    private void setMyAdapter() {
        if (arrServiceReport.size() > 0) {

            adapter = new ServiceReportAdapter(arrServiceReport, getActivity(), beanMain);
            rvServiceReport.setAdapter(adapter);
            rvServiceReport.setNestedScrollingEnabled(false);
            adapter.setOnItemClickListener(new ServiceReportAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position, JobAllSyncAllSurveyBean bean1) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.INTENT_BUNDLE, beanMain);
                    bundle.putString(AppConstants.INTENT_FORM_ID, bean1.getId());
                    bundle.putInt(AppConstants.INTENT_FORM_POS, position);
                    ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.SURVEY_FORM, bundle);
                }
            });

            adapter.setOnItemDeleteListener(new ServiceReportAdapter.OnItemDeleteListener() {
                @Override
                public void onItemDeleteListener(int position, JobAllSyncAllSurveyBean bean) {
                    if (SharedObjects.isNetworkConnected(getActivity())) {
                        deleteSurveyForm(bean);
                    } else {
                        updateFormInLocal(1,bean);
                    }

                }
            });

            hideShowView(true);
        } else {
            hideShowView(false);
        }
    }

    public void deleteSurveyForm(final JobAllSyncAllSurveyBean bean) {
        showProgressDialog();
        Call<JsonElement> call = RestClientToken.post().deleteSurveyForm("Bearer " + AppConstants.TOKEN, bean.getId());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    dismissProgressDialog();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            updateFormInLocal(0,bean);
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
            }
        });
    }

    private void updateFormInLocal(int isSync, JobAllSyncAllSurveyBean bean) {
        for (int i = 0; i < arrServiceReport.size(); i++) {
            if (arrServiceReport.get(i).getId().equalsIgnoreCase(bean.getId())){
//                bean.setIsSync(formSync);
                if (isSync == 0){
                    arrServiceReport.remove(i);
                }else{
                    bean.setIsSync(2);
                    arrServiceReport.set(i,bean);
                }
                beanMain.setAllSurvey(arrServiceReport);

                int status ;
                if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                    status = dbHandler.updateJob(sharedObjects.getUserId(), beanMain,isSync);
                }else{
                    status = dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain,isSync);
                }

                if (status != -1) {
                    arrServiceReport.remove(i);
//            Toast.makeText(getActivity(), getActivity().getString(R.string.form_saved_success), Toast.LENGTH_SHORT).show();
                }
//                arrServiceReport.remove(i);
                break;
            }
        }

        if (adapter != null)
        adapter.notifyDataSetChanged();

        if (arrServiceReport.size()>0){
            hideShowView(true);
        }else{
            hideShowView(false);
        }

        Log.e("Arr size : ",arrServiceReport.size() + " " + beanMain.getAllSurvey().size());

    }

    public void hideShowView(boolean viewFlag) {
        rvServiceReport.setVisibility(viewFlag ? View.VISIBLE : View.GONE);
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


}
