package com.surveyor.app.surveyorapp.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.attendance.AttendanceReportFragment;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

/**
 * Created by DELL on 14-Oct-18.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtNameTitle)
    TextView txtNameTitle;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtEmailTitle)
    TextView txtEmailTitle;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtContactNoTitle)
    TextView txtContactNoTitle;
    @BindView(R.id.txtContactNo)
    TextView txtContactNo;
    @BindView(R.id.txtRoleTypeTitle)
    TextView txtRoleTypeTitle;
    @BindView(R.id.txtRoleType)
    TextView txtRoleType;
    private View view;

    SharedObjects sharedObjects;
    ProgressDialog progressDialog;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());

        txtUserName.setText(sharedObjects.getUserInfo().getUserrole());
        txtRoleType.setText(sharedObjects.getUserInfo().getUserrole());
        txtName.setText(sharedObjects.getUserInfo().getDispayname());
        txtEmail.setText(sharedObjects.getUserInfo().getUseremail());
        txtContactNo.setText(sharedObjects.getUserInfo().getUsercontactno());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (sharedObjects.getProfilePic() != null){
            imgUser.setImageBitmap(sharedObjects.getProfilePic());
        }else{
            imgUser.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.avatar));
        }

        return view;
    }

    @OnClick({R.id.imgUser})
    public void attendanceReport(View view) {
        // ((DashboardActivity) getActivity()).loadFragment(new AttendanceReportFragment());
        switch (view.getId()) {
            default:
                break;
            case R.id.imgUser:
                ImagePicker.create(getActivity())
                        .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                        .folderMode(true) // set folder mode (false by default)
                        .single()
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select")
//                        .toolbarDoneButtonText("DONE") // done button text
                        .start(0); // image selection title
                break;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Image> images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            Log.e("onActivityResult", images.get(0).getPath() + " -- ");
            uploadImage(new File(images.get(0).getPath()), images.get(0).getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage(File file, final String path) {
        progressDialog.show();
        MultipartBody.Part imgfile = null;
        if (file != null) {
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            imgfile = MultipartBody.Part.createFormData("imagefile", file.getName(), reqFile1);
        }
        Call<JsonElement> call = RestClientToken.post().saveProfileImage("Bearer " + AppConstants.TOKEN, imgfile);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {

                            Picasso.with(getActivity()).load(new File(path)).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgUser);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new ImageDownloader().execute(path);
                                }
                            },500);


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
                Log.e("profilePicUpload - ", t.toString());
                progressDialog.dismiss();
            }
        });
    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {

        String path ;

        @Override
        protected Bitmap doInBackground(String... param) {
            path = param[0];
            // TODO Auto-generated method stub
            return getBitmapFromURL(param[0]);
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            if (result != null) {

                sharedObjects.saveProfilePic1(path , result);

                ((DashboardActivity) getActivity()).loadToolbarProfile();
            }
        }

        public Bitmap getBitmapFromURL(String src) {
                Bitmap myBitmap = BitmapFactory.decodeFile(src);
                return myBitmap;
        }
    }
}
