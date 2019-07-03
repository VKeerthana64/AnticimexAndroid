package com.surveyor.app.surveyorapp.sync_service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncAllSurveyBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;
import com.surveyor.app.surveyorapp.bean.JobTypeBean;
import com.surveyor.app.surveyorapp.bean.OptionBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberDataBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.ImageStorage;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MySyncService extends IntentService {

    DBHandler dbHandler;
    SharedObjects sharedObjects;

    ArrayList<OptionBean.Findings> arrOptionFindings = new ArrayList<>();
    ArrayList<OptionBean.Habitate> arrOptionHabitat = new ArrayList<>();
    ArrayList<OptionBean.Remarks> arrOptionRemarks = new ArrayList<>();
    ArrayList<OptionBean.BinCenter> arrOptionBitcenter = new ArrayList<>();
    ArrayList<OptionBean.ActionTaken> arrOptionActionTaken = new ArrayList<>();
    ArrayList<OptionBean.ProbableCauseOfBurrows> arrOptionProbable = new ArrayList<>();

    public MySyncService() {
        super("MySyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Service ", "started");
        dbHandler = new DBHandler(MySyncService.this);
        sharedObjects = new SharedObjects(MySyncService.this);

        getAllOptions();
        getJobType();
        getAllTechnician();

        if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.LOGIN_BEAN))) {
            if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                //when logged in as technician

                if (dbHandler.getCount() == 0) {
                    getJobListForSync();
                } else {
                    if (dbHandler.getUnsyncJobCount(sharedObjects.getUserId()) > 0) {
//                    JobAllSyncDataBean beanMain = dbHandler.getSingleUnsyncJob(sharedObjects.getUserId());
                        ArrayList<JobAllSyncDataBean> arrMain = dbHandler.getAllUnsyncJobs(sharedObjects.getUserId());
                        for (int i = 0; i < arrMain.size(); i++) {
                            JobAllSyncDataBean beanMain = arrMain.get(i);
                            ArrayList<JobAllSyncAllSurveyBean> arrForms = beanMain.getAllSurvey();
                            for (int j = 0; j < arrForms.size(); j++) {
                                if (arrForms.get(j).getIsSync() == 1) {
                                    submitSurveyForm(beanMain, arrForms, j);
                                } else if (arrForms.get(j).getIsSync() == 2) {  //submit delete form
                                    deleteSurveyForm(beanMain, arrForms, j);
                                }
                            }
                        }
                    } else {
                        getJobListForSync();
                    }
                }
            } else {
                if (dbHandler.getCountTL() == 0) {
                    getJobListForSync();
                } else {
                    if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) > 0) { //checking unsync job form
//                    JobAllSyncDataBean beanMain = dbHandler.getSingleUnsyncJob(sharedObjects.getUserId());
                        ArrayList<JobAllSyncDataBean> arrMain = dbHandler.getAllUnsyncJobsTLForm(sharedObjects.getUserId());
                        Log.e("unsync job", arrMain.size() + "");
                        for (int i = 0; i < arrMain.size(); i++) {
                            JobAllSyncDataBean beanMain = arrMain.get(i);
                            ArrayList<JobAllSyncAllSurveyBean> arrForms = beanMain.getAllSurvey();
                            for (int j = 0; j < arrForms.size(); j++) {
                                if (arrForms.get(j).getIsSync() == 1) {
                                    submitSurveyForm(beanMain, arrForms, j);
                                } else if (arrForms.get(j).getIsSync() == 2) {  //submit delete form
                                    deleteSurveyForm(beanMain, arrForms, j);
                                }
                            }
                        }
                    }

                    if (dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) > 0) { //checking unsync job teammember
//                    JobAllSyncDataBean beanMain = dbHandler.getSingleUnsyncJob(sharedObjects.getUserId());
                        ArrayList<JobAllSyncDataBean> arrMain = dbHandler.getAllUnsyncJobsTLTeamMember(sharedObjects.getUserId());
                        Log.e("unsync team", arrMain.size() + "");
                        for (int i = 0; i < arrMain.size(); i++) {
                            JobAllSyncDataBean beanMain = arrMain.get(i);
                            ArrayList<TeamMemberDataBean> arrTeamMember = beanMain.getSurveyteammember();
                            for (int j = 0; j < arrTeamMember.size(); j++) {
                                if (arrTeamMember.get(j).getIsSync() == 1) {
                                    if (arrTeamMember.get(j).isAssigned()) {
                                        addTeamMembers(beanMain, arrTeamMember, j);
                                    } else {
                                        removeTeamMembers(beanMain, arrTeamMember, j);
                                    }
                                }
                            }
                        }
                    }/* else {
                        getJobListForSync();
                    }*/

                    if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) == 0 &&
                            dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) == 0) {
                        getJobListForSync();
                    }
                }
            }
        }

        //sending the broadcast to refresh the list
        sendBroadcast(new Intent(DashboardActivity.DATA_SAVED_BROADCAST));
    }

    public void deleteSurveyForm(final JobAllSyncDataBean beanMain, final ArrayList<JobAllSyncAllSurveyBean> arrForms, final int position) {
        final JobAllSyncAllSurveyBean beanForm = arrForms.get(position);
        Call<JsonElement> call = RestClientToken.post().deleteSurveyForm("Bearer " + AppConstants.TOKEN, beanForm.getId());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
//                            arrServiceReport.remove(bean);
//                            adapter.notifyDataSetChanged();
                            arrForms.remove(position);
                            beanMain.setAllSurvey(arrForms);

                            if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.LOGIN_BEAN))) {
                                if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                                    dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 1);

                                    JobAllSyncDataBean bean = dbHandler.getSingleJob(sharedObjects.getUserId(), beanMain.getJoborderid());

                                    if (checkAllJobDataSync(bean)) {
                                        dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 0);
                                    } else {
                                        dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 1);
                                    }

                                    if (dbHandler.getUnsyncJobCount(sharedObjects.getUserId()) == 0) {
                                        getJobListForSync();
                                    }
                                } else {
                                    dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);

                                    JobAllSyncDataBean bean = dbHandler.getSingleJobTL(sharedObjects.getUserId(), beanMain.getJoborderid());

                                    if (checkAllJobDataSync(bean)) {
                                        dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 0);
                                            /*if (checkAllJobTeamMemberSync(bean)) {

                                            } else {
                                                dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);

                                                *//*ArrayList<JobAllSyncDataBean> arrMain = dbHandler.getAllUnsyncJobs(sharedObjects.getUserId());
                                         *//*
                                            }*/
                                    } else {
                                        dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);
                                    }

                                    if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) == 0 &&
                                            dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) == 0) {
                                        getJobListForSync();
                                    }
                                }
                            }

                        }
                    } else if (response.code() == 401) {
                        try {
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
            }
        });
    }

    public void submitSurveyForm(final JobAllSyncDataBean beanMain, final ArrayList<JobAllSyncAllSurveyBean> arrForms, final int position) {

        final JobAllSyncAllSurveyBean beanForm = arrForms.get(position);
        String id = beanForm.getId();

        String landowner = "";

        String Action = beanForm.getActiontaken();
        String BitCenter = beanForm.getBincenter();
        String Findings = beanForm.getFindings();
        String Habitat = beanForm.getHabitate();
        String ProbableCause = beanForm.getProbablecauseofburrows();
        String Remarks = beanForm.getRemarks();

        String constituency = beanForm.getConstituency();
        String contractordateresolved = beanForm.getDate_of_finding();
        String date_of_finding = beanForm.getDate_of_finding();
        String feedbacksubstantiated = "";
        String geoaddress = beanForm.getGeoaddress();

        String latitude = beanForm.getLatitude();
        String longitude = beanForm.getLongitude();

        String locationremarks = beanForm.getLocationremarks();
        String neajobid = "";
        String noofburrows = beanForm.getNoofburrows();

        String noofactiveburrows = beanForm.getNoofactiveburrows();
        String noofnonactiveburrows = beanForm.getNoofnonactiveburrows();


        String noofdefects = beanForm.getNoofdefects();
        String postalcode = beanForm.getPostalcode();
        String time_of_finding = beanForm.getTime_of_finding();
        String towncouncil = beanForm.getTowncouncil();

        String noofbinchute = beanForm.getNoofbinchute();
        String noofcrc = beanForm.getNoofcrc();

        RequestBody isDraftForm = RequestBody.create(MediaType.parse("multipart/form-data"), beanForm.getIsdraft());

        RequestBody reqId = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody reqJoScheduleId = RequestBody.create(MediaType.parse("multipart/form-data"), beanMain.getJo_scheduleid());
        RequestBody reqLandowner = RequestBody.create(MediaType.parse("multipart/form-data"), landowner);
        RequestBody reqPostal = RequestBody.create(MediaType.parse("multipart/form-data"), postalcode);
        RequestBody reqConstituency = RequestBody.create(MediaType.parse("multipart/form-data"), constituency);
        RequestBody reqTownConcil = RequestBody.create(MediaType.parse("multipart/form-data"), towncouncil);
        RequestBody reqDateFinding = RequestBody.create(MediaType.parse("multipart/form-data"), date_of_finding);
        RequestBody reqTimeFinding = RequestBody.create(MediaType.parse("multipart/form-data"), time_of_finding);
        RequestBody reqBitCenter = RequestBody.create(MediaType.parse("multipart/form-data"), BitCenter);
        RequestBody reqFinding = RequestBody.create(MediaType.parse("multipart/form-data"), Findings);
        RequestBody reqNoDefects = RequestBody.create(MediaType.parse("multipart/form-data"), noofdefects);
        RequestBody reqNoBurrows = RequestBody.create(MediaType.parse("multipart/form-data"), noofburrows);
        RequestBody reqNoActiveBurrows = RequestBody.create(MediaType.parse("multipart/form-data"), noofactiveburrows);
        RequestBody reqNoNonActiveBurrows = RequestBody.create(MediaType.parse("multipart/form-data"), noofnonactiveburrows);

        RequestBody reqNoBinChute = RequestBody.create(MediaType.parse("multipart/form-data"), noofbinchute);
        RequestBody reqNoCRC = RequestBody.create(MediaType.parse("multipart/form-data"), noofcrc);

        RequestBody reqHabitate = RequestBody.create(MediaType.parse("multipart/form-data"), Habitat);
        RequestBody reqCause = RequestBody.create(MediaType.parse("multipart/form-data"), ProbableCause);
        RequestBody reqNeaJobId = RequestBody.create(MediaType.parse("multipart/form-data"), neajobid);
        RequestBody reqFeedbacksubstantiated = RequestBody.create(MediaType.parse("multipart/form-data"), feedbacksubstantiated);
        RequestBody reqRemarks = RequestBody.create(MediaType.parse("multipart/form-data"), Remarks);
        RequestBody reqActionTaken = RequestBody.create(MediaType.parse("multipart/form-data"), Action);
        RequestBody reqContractordateresolved = RequestBody.create(MediaType.parse("multipart/form-data"), contractordateresolved);
        RequestBody reqLocationremarks = RequestBody.create(MediaType.parse("multipart/form-data"), locationremarks);

        RequestBody reqLat = RequestBody.create(MediaType.parse("multipart/form-data"), latitude);
        RequestBody reqLong = RequestBody.create(MediaType.parse("multipart/form-data"), longitude);
        RequestBody reqGeoAddress = RequestBody.create(MediaType.parse("multipart/form-data"), geoaddress);
        ArrayList<MultipartBody.Part> imglist = new ArrayList<>();

        ArrayList<JobAllSyncImageslistBean> arrServerImages = new ArrayList<>();
        arrServerImages = beanForm.getImageslist();
        File file = null;
        for (int i = 0; i < arrServerImages.size(); i++) {
            if (arrServerImages.get(i).isFromLocal()) {
                file = ImageStorage.getImage(arrServerImages.get(i).getImagename());
                if (file != null) {
                    RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imgfile = MultipartBody.Part.createFormData("imagefile", file.getName(), reqFile1);
                    imglist.add(imgfile);
                }
            }
        }

        Call<JsonElement> call = RestClientToken.post().saveForm("Bearer " + AppConstants.TOKEN, reqId,
                reqJoScheduleId, reqLandowner, reqPostal, reqConstituency, reqTownConcil, reqDateFinding, reqTimeFinding,
                reqBitCenter, reqFinding, reqNoBurrows,reqNoActiveBurrows, reqNoNonActiveBurrows, reqNoDefects, reqHabitate, reqCause, reqNeaJobId, reqFeedbacksubstantiated,
                reqRemarks, reqLocationremarks, reqActionTaken, reqContractordateresolved, reqLat, reqLong, reqGeoAddress,
                isDraftForm, reqNoBinChute, reqNoCRC, imglist);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            if (jsonObject.getString(AppConstants.RESPONSE.msg).equalsIgnoreCase(AppConstants.RESPONSE.Success)) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                beanForm.setId(jsonObject1.getString(AppConstants.RESPONSE.uniqueid));
                                beanForm.setIsSync(0);
                                arrForms.set(position, beanForm);
                                beanMain.setAllSurvey(arrForms);
                                if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.LOGIN_BEAN))) {
                                    if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
                                        dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 1);

                                        JobAllSyncDataBean bean = dbHandler.getSingleJob(sharedObjects.getUserId(), beanMain.getJoborderid());

                                        if (checkAllJobDataSync(bean)) {
                                            dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 0);
                                        } else {
                                            dbHandler.updateJob(sharedObjects.getUserId(), beanMain, 1);
                                        }

                                        if (dbHandler.getUnsyncJobCount(sharedObjects.getUserId()) == 0) {
                                            getJobListForSync();
                                        }
                                    } else {
                                        dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);

                                        JobAllSyncDataBean bean = dbHandler.getSingleJobTL(sharedObjects.getUserId(), beanMain.getJoborderid());

                                        if (checkAllJobDataSync(bean)) {
                                            dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 0);
                                            /*if (checkAllJobTeamMemberSync(bean)) {

                                            } else {
                                                dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);

                                                *//*ArrayList<JobAllSyncDataBean> arrMain = dbHandler.getAllUnsyncJobs(sharedObjects.getUserId());
                                             *//*
                                            }*/
                                        } else {
                                            dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, 1);
                                        }

                                        if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) == 0 &&
                                                dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) == 0) {
                                            getJobListForSync();
                                        }
                                    }
                                }
                                //{"success":true,"msg":"Success","data":{"uniqueid":9}}
                            }
                            /*if (jsonObject.getString("msg").equalsIgnoreCase("Success")) {
                                  ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.JOB, new Bundle());
                            }*/
                        }
                        Toast.makeText(MySyncService.this, jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MySyncService.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkAllJobDataSync(JobAllSyncDataBean bean) {
        int count = 0;
        for (int i = 0; i < bean.getAllSurvey().size(); i++) {
            if (bean.getAllSurvey().get(i).getIsSync() == 0) {
                count++;
            }
        }

        if (count == bean.getAllSurvey().size()) {
            return true;
        }
        return false;
    }

    private boolean checkAllJobTeamMemberSync(JobAllSyncDataBean bean) {
        int count = 0;
        for (int i = 0; i < bean.getSurveyteammember().size(); i++) {
            if (bean.getSurveyteammember().get(i).getIsSync() == 0) {
                count++;
            }
        }

        if (count == bean.getSurveyteammember().size()) {
            return true;
        }
        return false;
    }

    public void getJobListForSync() {
        Call<JsonElement> call = RestClientToken.get().getJoblistAllSync("Bearer " + AppConstants.TOKEN);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
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
            }
        });
    }

    public void addTeamMembers(final JobAllSyncDataBean beanMain, final ArrayList<TeamMemberDataBean> arrTeamMember, final int position) {

        final TeamMemberDataBean beanTeammember = arrTeamMember.get(position);

        RequestBody reqJo_scheduleid = RequestBody.create(MediaType.parse("multipart/form-data"), beanMain.getJo_scheduleid());
        RequestBody reqUserId = RequestBody.create(MediaType.parse("multipart/form-data"), arrTeamMember.get(position).getId());

        Call<JsonElement> call = RestClientToken.post().addTeamMembers("Bearer " + AppConstants.TOKEN,
                reqJo_scheduleid, reqUserId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            beanTeammember.setAssigned(true);
                            beanTeammember.setIsSync(0);
                            arrTeamMember.set(position, beanTeammember);
                            beanMain.setSurveyteammember(arrTeamMember);
                            dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);

                            if (checkAllJobTeamMemberSync(beanMain)) {
                                dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);
                            } else {
                                dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 1);
                            }

                            if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) == 0 &&
                                    dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) == 0) {
                                getJobListForSync();
                            }
                        }

                        Toast.makeText(MySyncService.this, jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    public void removeTeamMembers(final JobAllSyncDataBean beanMain, final ArrayList<TeamMemberDataBean> arrTeamMember, final int position) {

        final TeamMemberDataBean beanTeammember = arrTeamMember.get(position);

        RequestBody reqJo_scheduleid = RequestBody.create(MediaType.parse("multipart/form-data"), beanMain.getJo_scheduleid());
        RequestBody reqUserId = RequestBody.create(MediaType.parse("multipart/form-data"), arrTeamMember.get(position).getId());

        Call<JsonElement> call = RestClientToken.post().removeTeamMembers("Bearer " + AppConstants.TOKEN,
                reqJo_scheduleid, reqUserId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            beanTeammember.setAssigned(false);
                            beanTeammember.setIsSync(0);
                            arrTeamMember.set(position, beanTeammember);
                            beanMain.setSurveyteammember(arrTeamMember);
                            dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);

                            if (checkAllJobTeamMemberSync(beanMain)) {
                                dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 0);
                            } else {
                                dbHandler.updateJobTLTeamMember(sharedObjects.getUserId(), beanMain, 1);
                            }

                            if (dbHandler.getUnsyncJobCountTL(sharedObjects.getUserId()) == 0 &&
                                    dbHandler.getUnsyncJobCountTLTeamMember(sharedObjects.getUserId()) == 0) {
                                getJobListForSync();
                            }
                        }
                        Toast.makeText(MySyncService.this, jsonObject.getString(AppConstants.RESPONSE.msg), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    public void getJobType() {
        Call<JsonElement> call = RestClientToken.post().getJobType("Bearer " + AppConstants.TOKEN);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            JobTypeBean bean = new Gson().fromJson(response.body().toString(), JobTypeBean.class);
                            if (bean.getData() != null) {
                                sharedObjects.setPreference(AppConstants.OFFLINE.JOB_TYPE, new Gson().toJson(bean.getData()));
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

    public void getAllTechnician() {
        Call<JsonElement> call = RestClientToken.post().getAllTechnician("Bearer " + AppConstants.TOKEN);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            JSONArray jaData = jsonObject.getJSONArray(AppConstants.RESPONSE.data);
                            if (jaData != null && jaData.length() > 0) {
                                ArrayList<TeamMemberDataBean> arrTeam = new ArrayList<>();
                                for (int i = 0; i < jaData.length(); i++) {
                                    JSONObject jsonObject1 = jaData.getJSONObject(i);
                                    TeamMemberDataBean teamMemberDataBean = new TeamMemberDataBean();
                                    teamMemberDataBean.setId(jsonObject1.getString("id"));
                                    teamMemberDataBean.setUsername(jsonObject1.getString("username"));
                                    teamMemberDataBean.setUserrole(jsonObject1.getString("userrole"));
                                    teamMemberDataBean.setFirstname(jsonObject1.getString("firstname"));
                                    teamMemberDataBean.setLastname(jsonObject1.getString("lastname"));
                                    teamMemberDataBean.setProfileimage(jsonObject1.getString("profileimage"));
                                    if (jsonObject1.getString("assigned").equalsIgnoreCase("true")) {
                                        teamMemberDataBean.setAssigned(true);
                                    } else {
                                        teamMemberDataBean.setAssigned(false);
                                    }
                                    arrTeam.add(teamMemberDataBean);
                                }
                                sharedObjects.setPreference(AppConstants.OFFLINE.ALL_TECHNICIAN, new Gson().toJson(arrTeam));

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

    private void getAllOptions() {
        Call<JsonElement> call = RestClientToken.post().getAllOptions("Bearer " + AppConstants.TOKEN);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
//                        Log.e("Options : ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                            OptionBean bean = new Gson().fromJson(response.body().toString(), OptionBean.class);

                            if (bean.getData() != null && bean.getData().getRemarks() != null) {

                                dbHandler.deleteRemarks();

                                arrOptionRemarks = new ArrayList<>();
                                arrOptionRemarks = bean.getData().getRemarks();

                                for (int i = 0; i < arrOptionRemarks.size(); i++) {
                                    if (dbHandler.getRemarksCount() == 0) {
                                        dbHandler.addRemarks(arrOptionRemarks.get(i));
                                    } else if (dbHandler.isRemarksExist(arrOptionRemarks.get(i).getId())) {
                                        dbHandler.updateRemarks(arrOptionRemarks.get(i));
                                    } else {
                                        dbHandler.addRemarks(arrOptionRemarks.get(i));
                                    }
                                }

                            } else {
                                arrOptionRemarks = new ArrayList<>();
                            }

                            if (bean.getData() != null && bean.getData().getHabitate() != null) {

                                dbHandler.deleteHabitate();

                                arrOptionHabitat = new ArrayList<>();
                                arrOptionHabitat = bean.getData().getHabitate();

                                for (int i = 0; i < arrOptionHabitat.size(); i++) {
                                    if (dbHandler.getHabitateCount() == 0) {
                                        dbHandler.addHabitate(arrOptionHabitat.get(i));
                                    } else if (dbHandler.isHabitateExist(arrOptionHabitat.get(i).getId())) {
                                        dbHandler.updateHabitate(arrOptionHabitat.get(i));
                                    } else {
                                        dbHandler.addHabitate(arrOptionHabitat.get(i));
                                    }
                                }
                            } else {
                                arrOptionHabitat = new ArrayList<>();
                            }

                            if (bean.getData() != null && bean.getData().getFindings() != null) {

                                dbHandler.deleteFindings();

                                arrOptionFindings = new ArrayList<>();
                                arrOptionFindings = bean.getData().getFindings();

                                for (int i = 0; i < arrOptionFindings.size(); i++) {
                                    if (dbHandler.getFindingsCount() == 0) {
                                        dbHandler.addFindings(arrOptionFindings.get(i));
                                    } else if (dbHandler.isFindingsExist(arrOptionFindings.get(i).getId())) {
                                        dbHandler.updateFindings(arrOptionFindings.get(i));
                                    } else {
                                        dbHandler.addFindings(arrOptionFindings.get(i));
                                    }
                                }
                            } else {
                                arrOptionFindings = new ArrayList<>();
                            }

                            if (bean.getData() != null && bean.getData().getActionTaken() != null) {

                                dbHandler.deleteAction();
                                arrOptionActionTaken = new ArrayList<>();
                                arrOptionActionTaken = bean.getData().getActionTaken();

                                for (int i = 0; i < arrOptionActionTaken.size(); i++) {
                                    if (dbHandler.getActionCount() == 0) {
                                        dbHandler.addAction(arrOptionActionTaken.get(i));
                                    } else if (dbHandler.isActionExist(arrOptionActionTaken.get(i).getId())) {
                                        dbHandler.updateAction(arrOptionActionTaken.get(i));
                                    } else {
                                        dbHandler.addAction(arrOptionActionTaken.get(i));
                                    }
                                }
                            } else {
                                arrOptionActionTaken = new ArrayList<>();
                            }

                            if (bean.getData() != null && bean.getData().getProbableCauseOfBurrows() != null) {

                                dbHandler.deleteCause();
                                arrOptionProbable = new ArrayList<>();
                                arrOptionProbable = bean.getData().getProbableCauseOfBurrows();

                                for (int i = 0; i < arrOptionProbable.size(); i++) {
                                    if (dbHandler.getCauseCount() == 0) {
                                        dbHandler.addCause(arrOptionProbable.get(i));
                                    } else if (dbHandler.isCauseExist(arrOptionProbable.get(i).getId())) {
                                        dbHandler.updateCause(arrOptionProbable.get(i));
                                    } else {
                                        dbHandler.addCause(arrOptionProbable.get(i));
                                    }
                                }
                            } else {
                                arrOptionProbable = new ArrayList<>();
                            }

                            if (bean.getData() != null && bean.getData().getBinCenter() != null) {

                                dbHandler.deleteBitcenter();

                                arrOptionBitcenter = new ArrayList<>();
                                arrOptionBitcenter = bean.getData().getBinCenter();

                                for (int i = 0; i < arrOptionBitcenter.size(); i++) {
                                    if (dbHandler.getBitcenterCount() == 0) {
                                        dbHandler.addBitcenter(arrOptionBitcenter.get(i));
                                    } else if (dbHandler.isBitcenterExist(arrOptionBitcenter.get(i).getId())) {
                                        dbHandler.updateBitcenter(arrOptionBitcenter.get(i));
                                    } else {
                                        dbHandler.addBitcenter(arrOptionBitcenter.get(i));
                                    }
                                }
                            } else {
                                arrOptionBitcenter = new ArrayList<>();
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
