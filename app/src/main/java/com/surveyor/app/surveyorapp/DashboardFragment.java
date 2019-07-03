package com.surveyor.app.surveyorapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.otaliastudios.cameraview.CameraView;
import com.surveyor.app.surveyorapp.bean.AttendanceReportBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberBean;
import com.surveyor.app.surveyorapp.jobs_team_leader.TeamMemberAdapter;
import com.surveyor.app.surveyorapp.retrofit.RestClient;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by DELL on 14-Oct-18.
 */

public class DashboardFragment extends Fragment
{
    @BindView(R.id.llAttendance)
    LinearLayout llAttendance;
    @BindView(R.id.llJob)
    LinearLayout llJob;
    @BindView(R.id.llReporting)
    LinearLayout llReporting;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    SharedObjects sharedObjects;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            ((HomeActivity) getActivity()).selectMenuItem(getActivity().getString(R.string.menu_profile));
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sharedObjects = new SharedObjects(getActivity());

        return view;
    }

    @OnClick(R.id.llProfile)
    public void menuProfile(View view) {
        ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.PROFILE,new Bundle()/*""*/);
    }

    @OnClick(R.id.llReporting)
    public void menuReporting(View view) {
        ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.REPORTING,new Bundle()/*""*/);
    }

    @OnClick(R.id.llJob)
    public void menuJob(View view) {
        ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.JOB,new Bundle()/*""*/);
    }

    @OnClick(R.id.llAttendance)
    public void menuAttendance(View view) {

        ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.ATTENDANCE,new Bundle()/*""*/);

        /*if (!TextUtils.isEmpty(sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN)) &&
                sharedObjects.getPreference(AppConstants.CHECK_CLOCKIN).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
            ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.ATTENDANCE,new Bundle()*//*""*//*);
        } else {
//            takePicture();
            if (SharedObjects.isNetworkConnected(getActivity())) {
                startActivityForResult(new Intent(getActivity(),CaptureImageActivity.class),100);
            } else {
                SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_internet_title), getString(R.string.err_internet));
            }

        }*/
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



}
