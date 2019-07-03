package com.surveyor.app.surveyorapp.survey_form;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.FormImages;
import com.surveyor.app.surveyorapp.bean.GetSurveyFormBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncAllSurveyBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;
import com.surveyor.app.surveyorapp.bean.JobBean;
import com.surveyor.app.surveyorapp.bean.JobTypeBean;
import com.surveyor.app.surveyorapp.bean.OptionBean;
import com.surveyor.app.surveyorapp.bean.ReverseGeoBean;
import com.surveyor.app.surveyorapp.bean.SaveFormBean;
import com.surveyor.app.surveyorapp.retrofit.RestClientToken;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.ImageStorage;
import com.surveyor.app.surveyorapp.utils.SharedObjects;
/*import com.vinaygaba.rubberstamp.RubberStamp;
import com.vinaygaba.rubberstamp.RubberStampConfig;
import com.vinaygaba.rubberstamp.RubberStampPosition;*/
import com.surveyor.app.surveyorapp.overlaytext.RubberStamp;
import com.surveyor.app.surveyorapp.overlaytext.RubberStampConfig;
import com.surveyor.app.surveyorapp.overlaytext.RubberStampPosition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Part;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by DELL on 14-Oct-18.
 */

public class SurveyFormFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "SurveyFormFragment";
    SharedObjects sharedObjects;
    @BindView(R.id.txtRefNo)
    TextView txtRefNo;
    @BindView(R.id.txtCustomerName)
    TextView txtCustomerName;
    @BindView(R.id.txtTechnicianName)
    TextView txtTechnicianName;
    @BindView(R.id.txtTechnicianId)
    TextView txtTechnicianId;
    @BindView(R.id.txtConstituency)
    TextView txtConstituency;
    @BindView(R.id.txtDeadLine)
    TextView txtDeadLine;
    @BindView(R.id.txtDivision)
    TextView txtDivision;
    @BindView(R.id.spnType)
    Spinner spnType;
    @BindView(R.id.spnJobType)
    Spinner spnJobType;
    @BindView(R.id.edtLocation)
    EditText edtLocation;
    @BindView(R.id.edtLocationRemarks)
    EditText edtLocationRemarks;
    @BindView(R.id.edtPostal)
    EditText edtPostal;
    @BindView(R.id.edtDateFinding)
    EditText edtDateFinding;
    @BindView(R.id.edtTimeFinding)
    EditText edtTimeFinding;

    @BindView(R.id.rvFindings)
    RecyclerView rvFindings;

    @BindView(R.id.edtNonActiveBurrows)
    EditText edtNonActiveBurrows;
   @BindView(R.id.edtActiveBurrows)
    EditText edtActiveBurrows;
   @BindView(R.id.edtBurrows)
    EditText edtBurrows;
    @BindView(R.id.edtDefects)
    EditText edtDefects;
    @BindView(R.id.edtClassification)
    EditText edtClassification;
    @BindView(R.id.edtConstituency)
    EditText edtConstituency;
    @BindView(R.id.edtTownCouncil)
    EditText edtTownCouncil;

    @BindView(R.id.ibPrevious)
    ImageButton ibPrevious;
    @BindView(R.id.ibNext)
    ImageButton ibNext;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnDraft)
    Button btnDraft;

    @BindView(R.id.rvImages)
    RecyclerView rvImages;
    @BindView(R.id.rvImagesTop)
    RecyclerView rvImagesTop;

    @BindView(R.id.imgUploadImage)
    ImageView imgUploadImage;
    @BindView(R.id.imgUploadImageTop)
    ImageView imgUploadImageTop;

    @BindView(R.id.txtZone)
    TextView txtZone;
    @BindView(R.id.txtJobType)
    TextView txtJobType;
    @BindView(R.id.txtFindings)
    TextView txtFindings;
    @BindView(R.id.tag_group)
    TagView tag_group;


    @BindView(R.id.txtBitCenter)
    TextView txtBitCenter;
    @BindView(R.id.txtHabitat)
    TextView txtHabitat;
    @BindView(R.id.txtProbableCause)
    TextView txtProbableCause;
    @BindView(R.id.txtRemarks)
    TextView txtRemarks;
    @BindView(R.id.txtActionTaken)
    TextView txtActionTaken;
    @BindView(R.id.llButtons)
    LinearLayout llButtons;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.edtBinChute)
    EditText edtBinChute;
    @BindView(R.id.edtCRC)
    EditText edtCRC;
    @BindView(R.id.txtClassification)
    TextView txtClassification;
    @BindView(R.id.llTechnicianName)
    LinearLayout llTechnicianName;
    @BindView(R.id.llTechnicianId)
    LinearLayout llTechnicianId;

    @BindView(R.id.txtFindingsValue)
    EditText txtFindingsValue;
    @BindView(R.id.txtBitcenterValue)
    EditText txtBitcenterValue;
    @BindView(R.id.txtHabitatValue)
    EditText txtHabitatValue;
    @BindView(R.id.txtProbableCauseValue)
    EditText txtProbableCauseValue;
    @BindView(R.id.txtRemarksValue)
    EditText txtRemarksValue;
    @BindView(R.id.txtActionTakenValue)
    EditText txtActionTakenValue;

    ArrayList<JobTypeBean.Data> arrJobType = new ArrayList<>();

    private ProgressDialog progressDialog;
    String jo_scheduleid;

    EditText edtServiceDate;

    ArrayList<String> arrFindings = new ArrayList<>();
    ArrayList<String> arrHabitat = new ArrayList<>();
    ArrayList<String> arrRemarks = new ArrayList<>();
    ArrayList<String> arrBitcenter = new ArrayList<>();
    ArrayList<String> arrActionTaken = new ArrayList<>();
    ArrayList<String> arrProbable = new ArrayList<>();

    ArrayList<OptionBean.Findings> arrOptionFindings = new ArrayList<>();
    ArrayList<OptionBean.Habitate> arrOptionHabitat = new ArrayList<>();
    ArrayList<OptionBean.Remarks> arrOptionRemarks = new ArrayList<>();
    ArrayList<OptionBean.BinCenter> arrOptionBitcenter = new ArrayList<>();
    ArrayList<OptionBean.ActionTaken> arrOptionActionTaken = new ArrayList<>();
    ArrayList<OptionBean.ProbableCauseOfBurrows> arrOptionProbable = new ArrayList<>();

    String Action = "", BitCenter = "", Findings = "", Habitat = "", ProbableCause = "", Remarks = "";

    OptionsSelectedFindingsAdapter adapter;

    OptionsRemarksAdapter optionsRemarksAdapter;
    OptionsHabitatAdapter optionsHabitatAdapter;
    OptionsProbableCauseAdapter optionsProbableCauseAdapter;
    OptionsActionAdapter optionsActionAdapter;
    OptionsBitcenterAdapter optionsBitcenterAdapter;
    OptionsFindingsAdapter optionsFindingsAdapter;

    //    JobBean.Data bean;
    JobAllSyncDataBean beanMain;
    JobAllSyncAllSurveyBean beanForm;
    ArrayList<JobAllSyncAllSurveyBean> arrSurveyForm = new ArrayList<>();
    String formId = "0", formRemarks = "", formFindings = "", formBitcenter = "", formHabitat = "", formProbableCause = "", formActionTaken = "";

    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    private int SELECT_FILE = 1;
    Uri imageUri;
    File fileImageSend;

    ArrayList<FormImages> arrImages = new ArrayList<>();
    FormImagesAdapter formImagesAdapter;

    DBHandler dbHandler;

    int formPos = -1;
    ArrayList<JobAllSyncImageslistBean> arrServerImages = new ArrayList<>();


    String enteredFindings = "";


    public boolean dashboardClick = false;

    public SurveyFormFragment() {
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
        View view = inflater.inflate(R.layout.fragment_survey_form, container, false);
        ButterKnife.bind(this, view);
        sharedObjects = new SharedObjects(getActivity());
        dbHandler = new DBHandler(getActivity());


        rvFindings.setNestedScrollingEnabled(false);


        tag_group.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(TagView tagView, Tag tag, int position) {
                for (int i = 0; i < arrOptionFindings.size(); i++) {
                    if (arrOptionFindings.get(i).getOptionname().equals(tag.text)) {
                        arrOptionFindings.get(i).setSelected(false);
                    }
                }
            }
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            beanMain = bundle.getParcelable(AppConstants.INTENT_BUNDLE);

            formId = bundle.getString(AppConstants.INTENT_FORM_ID);
            Log.e("formId", formId);
            formPos = bundle.getInt(AppConstants.INTENT_FORM_POS);

            arrSurveyForm = beanMain.getAllSurvey();

            if (formPos != -1) { // check form is new or old
                edtTownCouncil.setClickable(true);
                edtTownCouncil.setFocusable(true);
                edtTownCouncil.setFocusableInTouchMode(true);
                edtConstituency.setClickable(true);
                edtConstituency.setFocusable(true);
                edtConstituency.setFocusableInTouchMode(true);

                llTechnicianId.setVisibility(View.GONE);
                llTechnicianName.setVisibility(View.GONE);

                if (arrSurveyForm.size() > 0)
                    beanForm = arrSurveyForm.get(formPos);
            } else {
                //called when form is new
                llTechnicianId.setVisibility(View.VISIBLE);
                llTechnicianName.setVisibility(View.VISIBLE);

                txtTechnicianName.setText(sharedObjects.getUserInfo().getUsername());
                txtTechnicianId.setText(sharedObjects.getUserId());

                edtTownCouncil.setClickable(false);
                edtTownCouncil.setFocusable(false);
                edtTownCouncil.setFocusableInTouchMode(false);
                edtConstituency.setClickable(false);
                edtConstituency.setFocusable(false);
                edtConstituency.setFocusableInTouchMode(false);

                txtClassification.setVisibility(View.GONE);
                edtConstituency.setVisibility(View.GONE);

                edtDateFinding.setText(SharedObjects.getTodaysDateSPore(AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
                edtTimeFinding.setText(SharedObjects.getTodaysDateSPore(AppConstants.DateFormats.TIME_FORMAT));

                edtLocation.setText(DashboardActivity.address);
                edtPostal.setText(DashboardActivity.pincode);
            }

            setData();


            edtActiveBurrows.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (edtNonActiveBurrows.getText().length() > 0 && edtActiveBurrows.getText().length() > 0) {
                            int activeBorrow = Integer.parseInt(edtActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            int nonActiveBorrow = Integer.parseInt(edtNonActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(activeBorrow + nonActiveBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else if (edtNonActiveBurrows.getText().length() > 0) {
                            int nonActiveBorrow = Integer.parseInt(edtNonActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(nonActiveBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else if (edtActiveBurrows.getText().length() > 0) {
                            int activeBorrow = Integer.parseInt(edtActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(activeBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else {
                            edtBurrows.setEnabled(true);
                            edtBurrows.setClickable(true);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            });

            edtNonActiveBurrows.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (edtNonActiveBurrows.getText().length() > 0 && edtActiveBurrows.getText().length() > 0) {
                            int activeBorrow = Integer.parseInt(edtActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            int nonActiveBorrow = Integer.parseInt(edtNonActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(activeBorrow + nonActiveBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else if (edtNonActiveBurrows.getText().length() > 0) {
                            int nonActiveBorrow = Integer.parseInt(edtNonActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(nonActiveBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else if (edtActiveBurrows.getText().length() > 0) {
                            int activeBorrow = Integer.parseInt(edtActiveBurrows.getText().toString().replace("[-+.^:,]",""));
                            edtBurrows.setText(String.valueOf(activeBorrow));
                            edtBurrows.setEnabled(false);
                            edtBurrows.setClickable(false);
                        } else {
                            edtBurrows.setEnabled(true);
                            edtBurrows.setClickable(true);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });

            txtRemarksValue.setOnTouchListener(this);
            txtFindingsValue.setOnTouchListener(this);
            txtBitcenterValue.setOnTouchListener(this);
            txtHabitatValue.setOnTouchListener(this);
            txtProbableCauseValue.setOnTouchListener(this);
            txtActionTakenValue.setOnTouchListener(this);


        }


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
        }

        //You need to add the following line for this solution to work; thanks skayred
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.e("KEYCODE_BACK", "Yes");
                    showBackDialog();
                    return true;
                }
                Log.e("KEYCODE_BACK", "No");
                return false;
            }
        });
        return view;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.txtFindingsValue:
                if (txtFindingsValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
            case R.id.txtBitcenterValue:
                if (txtBitcenterValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
            case R.id.txtHabitatValue:
                if (txtHabitatValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
            case R.id.txtProbableCauseValue:
                if (txtProbableCauseValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
            case R.id.txtRemarksValue:
                if (txtRemarksValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
            case R.id.txtActionTakenValue:
                if (txtActionTakenValue.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                break;
        }
        return false;
    }

    private void setData() {
        if (!TextUtils.isEmpty(beanMain.getRefno())) {
            txtRefNo.setText(beanMain.getRefno());
        }
        if (!TextUtils.isEmpty(beanMain.getCustomername())) {
            txtCustomerName.setText(beanMain.getCustomername());
        }
        if (!TextUtils.isEmpty(beanMain.getClassification())) {
            edtClassification.setText(beanMain.getClassification());
        }
        if (!TextUtils.isEmpty(beanMain.getConstituency())) {
            edtConstituency.setText(beanMain.getConstituency());
            txtConstituency.setText(beanMain.getConstituency());
        }
        if (!TextUtils.isEmpty(beanMain.getTowncouncil())) {
            edtTownCouncil.setText(beanMain.getTowncouncil());
        }

        if (!TextUtils.isEmpty(beanMain.getDeadlinedate())) {

            txtDeadLine.setText(beanMain.getDeadlinedate());
        }

        txtDivision.setText(beanMain.getDivision());
        txtZone.setText(beanMain.getZone());
        txtJobType.setText(beanMain.getJobtypename());

        if (beanForm != null) {

            if (beanForm.getIscomplete().equalsIgnoreCase("true")) {
                llButtons.setVisibility(View.GONE);
            } else {
                if (beanForm.getIsdraft().equalsIgnoreCase("true")) {
                    llButtons.setVisibility(View.VISIBLE);
                } else {
                    llButtons.setVisibility(View.GONE);
                }
            }

            if (!TextUtils.isEmpty(beanForm.getGeoaddress())) {
                edtLocation.setText(beanForm.getGeoaddress());
            }

            if (!TextUtils.isEmpty(beanForm.getLocationremarks())) {
                edtLocationRemarks.setText(beanForm.getLocationremarks());
            }

            edtPostal.setText(beanForm.getPostalcode());
            edtBurrows.setText(beanForm.getNoofburrows());
            edtDefects.setText(beanForm.getNoofdefects());
            edtBinChute.setText(beanForm.getNoofbinchute());
            edtCRC.setText(beanForm.getNoofcrc());

            if (!TextUtils.isEmpty(beanForm.getDate_of_finding())) {
                edtDateFinding.setText(beanForm.getDate_of_finding());
            }

            if (!TextUtils.isEmpty(beanForm.getTime_of_finding())) {
                if (beanForm.getTime_of_finding().length() > 5) {
                    String time = SharedObjects.convertDateFormat(beanForm.getTime_of_finding()
                            , AppConstants.DateFormats.TIME_FORMAT_API, AppConstants.DateFormats.TIME_FORMAT);
                    edtTimeFinding.setText(time);
                } else {
                    edtTimeFinding.setText(beanForm.getTime_of_finding());
                }
            }

            if (!TextUtils.isEmpty(beanForm.getFindings())) {
                formFindings = beanForm.getFindings();
                Log.e(TAG, "formFindings : " + formFindings);
                String display = formFindings.replace(",", "\n");
                txtFindingsValue.setText(display);
                //Commented by Parth
                setFindingsAdapter(formFindings);
            }

            if (!TextUtils.isEmpty(beanForm.getBincenter())) {
                formBitcenter = beanForm.getBincenter();
                String display = formFindings.replace(",", "\n");
                txtBitcenterValue.setText(display);
            }

            if (!TextUtils.isEmpty(beanForm.getHabitate())) {
                formHabitat = beanForm.getHabitate();
                txtHabitatValue.setText(formHabitat);
            }

            if (!TextUtils.isEmpty(beanForm.getProbablecauseofburrows())) {
                formProbableCause = beanForm.getProbablecauseofburrows();
                txtProbableCauseValue.setText(formProbableCause);
            }

            if (!TextUtils.isEmpty(beanForm.getRemarks())) {
                formRemarks = beanForm.getRemarks();
                txtRemarksValue.setText(formRemarks);
//                setRemarksAdapter();
            }

            if (!TextUtils.isEmpty(beanForm.getActiontaken())) {
                formActionTaken = beanForm.getActiontaken();
                txtActionTakenValue.setText(formActionTaken);
            }

            ArrayList<JobAllSyncImageslistBean> arrayImages = beanForm.getImageslist();
            if (arrayImages.size() > 0) {
                for (int i = 0; i < arrayImages.size(); i++) {
                    if (!ImageStorage.checkifImageExists(arrayImages.get(i).getImagename())) {
                        if (SharedObjects.isNetworkConnected(getActivity())) {
                            JobAllSyncImageslistBean bean = arrayImages.get(i);
                            new DownloadImage().execute(bean);
                        }
                    } else {
                        arrServerImages.add(new JobAllSyncImageslistBean(arrayImages.get(i).getFullpath(), arrayImages.get(i).getImagename(), false));
                    }
                }
            }
            arrServerImages = beanForm.getImageslist();
        }


        formImagesAdapter = new FormImagesAdapter(arrServerImages, getActivity());
        rvImages.setAdapter(formImagesAdapter);
        rvImagesTop.setAdapter(formImagesAdapter);
    }



    private void setFindingsAdapter(String findings) {

        ArrayList<String> list = new ArrayList<>();
        if (!findings.equals("")) {
            String strSplit[] = new String[1];
            if (findings.contains(",")) {
                strSplit = findings.split(",");
            } else {
                strSplit[0] = findings;
            }

            for (int j = 0; j < strSplit.length; j++) {
                if (strSplit[j] != null) {
                    if (!strSplit[j].equals("")) {
                        list.add(strSplit[j]);
                    }
                }
            }
            adapter = new OptionsSelectedFindingsAdapter(list, getActivity());
        }


        rvFindings.setAdapter(adapter);
        ViewTreeObserver vto = rvFindings.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rvFindings.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    rvFindings.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = rvFindings.getMeasuredWidth();
                int height = rvFindings.getMeasuredHeight();
                Log.e("BitCenterHeight - ", "" + height);
                if (height >= 525) {
                    rvFindings.setNestedScrollingEnabled(true);
                    ViewGroup.LayoutParams params = rvFindings.getLayoutParams();
                    params.height = 525;
                    rvFindings.setLayoutParams(params);
                } else {
                    rvFindings.setNestedScrollingEnabled(false);
                }
            }
        });
    }



    private void showDialogOptions(final String title, String selectedValue) {

        final Dialog dialog = new Dialog(getActivity(), R.style.customDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.setContentView(R.layout.dialog_options);
        lp.dimAmount = 0.8f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        final TextView edtValue = (EditText) dialog.findViewById(R.id.edtValue);
        txtTitle.setText(title);
        edtValue.setText(selectedValue);

        RecyclerView rvOptions = (RecyclerView) dialog.findViewById(R.id.rvOptions);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.btnDialogCancel);
        Button btnDialogSubmit = (Button) dialog.findViewById(R.id.btnDialogSubmit);

        if (title.equalsIgnoreCase("Findings"))
        {
            if (dbHandler.getFindingsCount() > 0) {
                arrOptionFindings = new ArrayList<>();
                arrOptionFindings = dbHandler.getAllFindings();
                if (arrOptionFindings.size() > 0) {
                    StringBuilder sbFindingsText = new StringBuilder();
                    formFindings = selectedValue;
                    if (!TextUtils.isEmpty(formFindings)) {
                        String strSplit[] = new String[1];
                        if (formFindings.contains("\n")) {
                            strSplit = formFindings.split("\n");
                        } else {
                            strSplit[0] = formFindings;
                        }
                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInOptionList(strSplit[j], arrOptionFindings)) {
                                sbFindingsText.append(strSplit[j]);
                                sbFindingsText.append("\n");
                            }

                        }
                    }
                    enteredFindings = formFindings;
                }
                optionsFindingsAdapter = new OptionsFindingsAdapter(arrOptionFindings, getActivity());
                optionsFindingsAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsFindingsAdapter);
            }
        } else if (title.equalsIgnoreCase("BinCenter")) {

            if (dbHandler.getBitcenterCount() > 0) {
                arrOptionBitcenter = new ArrayList<>();
                arrOptionBitcenter = dbHandler.getAllBitcenter();
                if (arrOptionBitcenter.size() > 0) {
                    StringBuilder sbBitcenterText = new StringBuilder();
                    formBitcenter = selectedValue;
                    if (!TextUtils.isEmpty(formBitcenter)) {
                        String strSplit[] = new String[1];
                        if (formBitcenter.contains("\n")) {
                            strSplit = formBitcenter.split("\n");
                        } else {
                            strSplit[0] = formBitcenter;
                        }

                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInBitCenterOptionList(strSplit[j], arrOptionBitcenter)) {
                                sbBitcenterText.append(strSplit[j]);
                                sbBitcenterText.append("\n");
                            }
                        }


                    }
                    enteredFindings = formBitcenter;
                }
                optionsBitcenterAdapter = new OptionsBitcenterAdapter(arrOptionBitcenter, getActivity());
                optionsBitcenterAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsBitcenterAdapter);
            }
        } else if (title.equalsIgnoreCase("Habitate")) {
            if (dbHandler.getHabitateCount() > 0) {
                arrOptionHabitat = new ArrayList<>();
                arrOptionHabitat = dbHandler.getAllHabitate();
                if (arrOptionHabitat.size() > 0) {
                    StringBuilder sbHabitateText = new StringBuilder();
                    formHabitat = selectedValue;
                    if (!TextUtils.isEmpty(formHabitat)) {
                        String strSplit[] = new String[1];
                        if (formHabitat.contains("\n")) {
                            strSplit = formHabitat.split("\n");
                        } else {
                            strSplit[0] = formHabitat;
                        }

                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInHabitatOptionList(strSplit[j], arrOptionHabitat)) {
                                sbHabitateText.append(strSplit[j]);
                                sbHabitateText.append("\n");
                            }
                        }


                    }
                    enteredFindings = formHabitat;
                }
                optionsHabitatAdapter = new OptionsHabitatAdapter(arrOptionHabitat, getActivity());
                optionsHabitatAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsHabitatAdapter);
            }
        } else if (title.equalsIgnoreCase("ProbableCauseOfBurrows")) {

            if (dbHandler.getCauseCount() > 0) {
                arrOptionProbable = new ArrayList<>();
                arrOptionProbable = dbHandler.getAllCause();
                if (arrOptionProbable.size() > 0) {
                    StringBuilder sbProbableText = new StringBuilder();
                    formProbableCause = selectedValue;
                    if (!TextUtils.isEmpty(formProbableCause)) {
                        String strSplit[] = new String[1];
                        if (formProbableCause.contains("\n")) {
                            strSplit = formProbableCause.split("\n");
                        } else {
                            strSplit[0] = formProbableCause;
                        }

                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInProbableOptionList(strSplit[j], arrOptionProbable)) {
                                sbProbableText.append(strSplit[j]);
                                sbProbableText.append("\n");
                            }
                        }

                    }
                    enteredFindings = formProbableCause;
                }

                optionsProbableCauseAdapter = new OptionsProbableCauseAdapter(arrOptionProbable, getActivity());
                optionsProbableCauseAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsProbableCauseAdapter);
            }

        } else if (title.equalsIgnoreCase("Remarks")) {
            if (dbHandler.getRemarksCount() > 0) {
                arrOptionRemarks = new ArrayList<>();
                arrOptionRemarks = dbHandler.getAllRemarks();

                if (arrOptionRemarks.size() > 0) {
                    StringBuilder sbRemarksText = new StringBuilder();
                    formRemarks = selectedValue;
                    if (!TextUtils.isEmpty(formRemarks)) {
                        String strSplit[] = new String[1];
                        if (formRemarks.contains("\n")) {
                            strSplit = formRemarks.split("\n");
                        } else {
                            strSplit[0] = formRemarks;
                        }

                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInRemarksOptionList(strSplit[j], arrOptionRemarks)) {
                                sbRemarksText.append(strSplit[j]);
                                sbRemarksText.append("\n");
                            }
                        }

                    }
                    enteredFindings = formRemarks;
                }
                optionsRemarksAdapter = new OptionsRemarksAdapter(arrOptionRemarks, getActivity());
                optionsRemarksAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsRemarksAdapter);
            }
        } else if (title.equalsIgnoreCase("ActionTaken")) {
            if (dbHandler.getActionCount() > 0) {
                arrOptionActionTaken = new ArrayList<>();
                arrOptionActionTaken = dbHandler.getAllAction();

                if (arrOptionActionTaken.size() > 0) {
                    StringBuilder sbActionTakenText = new StringBuilder();
                    formActionTaken = selectedValue;
                    if (!TextUtils.isEmpty(formActionTaken)) {
                        String strSplit[] = new String[1];
                        if (formActionTaken.contains("\n")) {
                            strSplit = formActionTaken.split("\n");
                        } else {
                            strSplit[0] = formActionTaken;
                        }

                        for (int j = 0; j < strSplit.length; j++) {
                            if (!checkValueExistInActionTakenOptionList(strSplit[j], arrOptionActionTaken)) {
                                sbActionTakenText.append(strSplit[j]);
                                sbActionTakenText.append("\n");
                            }
                        }

                    }
                    enteredFindings = formActionTaken;
                }
                optionsActionAdapter = new OptionsActionAdapter(arrOptionActionTaken, getActivity());
                optionsActionAdapter.setOnValueSelectedListener(new OnValueSelectedListener() {
                    @Override
                    public void onValueSelectedListener(String value) {
                        edtValue.setText(value);
                    }
                });
                rvOptions.setAdapter(optionsActionAdapter);
            }
        }

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equalsIgnoreCase("Findings")) {
                    if (optionsFindingsAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        Findings = "";
                        Findings = optionsFindingsAdapter.selectedItem();
                        setFindingsAdapter(Findings);
                        if (!enteredFindings.equals("")) {
                            txtFindingsValue.setText(enteredFindings + "\n" + Findings);
                        } else {
                            txtFindingsValue.setText(Findings);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select findings", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.equalsIgnoreCase("BinCenter")) {
                    if (optionsBitcenterAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        BitCenter = "";
                        BitCenter = optionsBitcenterAdapter.selectedItem();
                        if (!enteredFindings.equals("")) {
                            txtBitcenterValue.setText(enteredFindings + "\n" + BitCenter);
                        } else {
                            txtBitcenterValue.setText(BitCenter);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select bitcenter", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.equalsIgnoreCase("Habitate")) {
                    if (optionsHabitatAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        Habitat = "";
                        Habitat = optionsHabitatAdapter.selectedItem();
                        if (!enteredFindings.equals("")) {
                            txtHabitatValue.setText(enteredFindings + "\n" + Habitat);
                        } else {
                            txtHabitatValue.setText(Habitat);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select habitat", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.equalsIgnoreCase("ProbableCauseOfBurrows")) {
                    if (optionsProbableCauseAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        ProbableCause = "";
                        ProbableCause = optionsProbableCauseAdapter.selectedItem();
                        if (!enteredFindings.equals("")) {
                            txtProbableCauseValue.setText(enteredFindings + "\n" + ProbableCause);
                        } else {
                            txtProbableCauseValue.setText(ProbableCause);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select probable cause", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.equalsIgnoreCase("Remarks")) {
                    if (optionsRemarksAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        Remarks = "";
                        Remarks = optionsRemarksAdapter.selectedItem();

                        if (!enteredFindings.equals("")) {
                            txtRemarksValue.setText(enteredFindings + "\n" + Remarks);
                        } else {
                            txtRemarksValue.setText(Remarks);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select remarks", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.equalsIgnoreCase("ActionTaken")) {
                    if (optionsActionAdapter.selectedCount() > 0) {
                        dialog.dismiss();
                        Action = "";
                        Action = optionsActionAdapter.selectedItem();
                        if (!enteredFindings.equals("")) {
                            txtActionTakenValue.setText(enteredFindings + "\n" + Action);
                        } else {
                            txtActionTakenValue.setText(Action);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select action taken", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }

    private boolean checkValueExistInOptionList(String value, ArrayList<OptionBean.Findings> arrOptionFindings) {
        for (int i = 0; i < arrOptionFindings.size(); i++) {
            OptionBean.Findings bean = arrOptionFindings.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValueExistInBitCenterOptionList(String value, ArrayList<OptionBean.BinCenter> arrOptionBinCenter) {
        for (int i = 0; i < arrOptionBinCenter.size(); i++) {
            OptionBean.BinCenter bean = arrOptionBinCenter.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValueExistInHabitatOptionList(String value, ArrayList<OptionBean.Habitate> arrOptionHabitate) {
        for (int i = 0; i < arrOptionHabitate.size(); i++) {
            OptionBean.Habitate bean = arrOptionHabitate.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValueExistInProbableOptionList(String value, ArrayList<OptionBean.ProbableCauseOfBurrows> arrOptionProbable) {
        for (int i = 0; i < arrOptionProbable.size(); i++) {
            OptionBean.ProbableCauseOfBurrows bean = arrOptionProbable.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValueExistInRemarksOptionList(String value, ArrayList<OptionBean.Remarks> arrOptionRemarks) {
        for (int i = 0; i < arrOptionRemarks.size(); i++) {
            OptionBean.Remarks bean = arrOptionRemarks.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValueExistInActionTakenOptionList(String value, ArrayList<OptionBean.ActionTaken> arrOptionActionTaken) {
        for (int i = 0; i < arrOptionActionTaken.size(); i++) {
            OptionBean.ActionTaken bean = arrOptionActionTaken.get(i);
            if (!TextUtils.isEmpty(bean.getOptionname()) && bean.getOptionname().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void setJobTypeAdapter() {
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spn_jobtype, arrJobType) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spnJobType.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick({R.id.edtDateFinding, R.id.txtBitCenter, R.id.txtHabitat, R.id.txtProbableCause, R.id.txtRemarks, R.id.txtActionTaken,
            R.id.edtTimeFinding, R.id.ibPrevious, R.id.btnSubmit,
            R.id.btnDraft, R.id.imgUploadImage, R.id.imgUploadImageTop, R.id.txtFindings})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.edtDateFinding:
                showDateDialog("");
                break;
            case R.id.edtTimeFinding:
                showTimeDialog();
                break;
            case R.id.btnSubmit:
                SharedObjects.hideKeyboard(btnSubmit, getActivity());
                submitSurveyForm(false);

                break;
            case R.id.btnDraft:
                SharedObjects.hideKeyboard(btnDraft, getActivity());
                submitSurveyForm(true);

                break;
            case R.id.ibPrevious:

                break;
            case R.id.txtFindings:
                showDialogOptions("Findings", txtFindingsValue.getText().toString());
                break;
            case R.id.txtBitCenter:
                showDialogOptions("BinCenter", txtBitcenterValue.getText().toString());
                break;
            case R.id.txtHabitat:
                showDialogOptions("Habitate", txtHabitatValue.getText().toString());
                break;
            case R.id.txtProbableCause:
                showDialogOptions("ProbableCauseOfBurrows", txtProbableCauseValue.getText().toString());
                break;
            case R.id.txtRemarks:
                showDialogOptions("Remarks", txtRemarksValue.getText().toString());
                break;
            case R.id.txtActionTaken:
                showDialogOptions("ActionTaken", txtActionTakenValue.getText().toString());
                break;
            case R.id.imgUploadImage:

                if (arrServerImages.size() < 18) {
                    selectImage();
                } else {
                    SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_image_validation_title), getString(R.string.err_image_validation_msg));
                }

                break;
            case R.id.imgUploadImageTop:

                if (arrServerImages.size() < 18) {
                    selectImage();
                } else {
                    SharedObjects.showAlertDialog(getActivity(), getString(R.string.err_image_validation_title), getString(R.string.err_image_validation_msg));
                }

                break;
        }
    }

    boolean shouldGoBack = false;  // pass true to go back

    public boolean backPressed() {
//        return showBackDialog();
        return true;
    }

    public void saveDraft() {
        dashboardClick = true;
        SharedObjects.hideKeyboard(btnDraft, getActivity());
        submitSurveyForm(true);
    }

    public boolean showBackDialog() {
        final Dialog dialogBack = new Dialog(getActivity());
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.dialog_service_report);
        final Window window = dialogBack.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);

        Button btnYes = (Button) dialogBack.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialogBack.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldGoBack = true;
                //dialogBack.cancel();
                dialogBack.dismiss();
                ((DashboardActivity) getActivity()).onBackPressed();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldGoBack = false;
                dialogBack.dismiss();
            }
        });
        if (!dialogBack.isShowing()) {
            dialogBack.show();
        }
        return shouldGoBack;
    }


    public void submitSurveyForm(boolean isDraft) {

        if (TextUtils.isEmpty(edtBinChute.getText().toString().trim())) {
            edtBinChute.setError("Enter no of bin chute");
            requestFocus(edtBinChute);
            return;
        }
        if (TextUtils.isEmpty(edtCRC.getText().toString().trim())) {
            edtCRC.setError("Enter no of CRC");
            requestFocus(edtCRC);
            return;
        }
        if (TextUtils.isEmpty(edtLocation.getText().toString().trim())) {
            edtLocation.setError("Enter location");
            requestFocus(edtLocation);
            return;
        }
        if (TextUtils.isEmpty(edtLocationRemarks.getText().toString().trim())) {
            edtLocationRemarks.setError("Enter location remarks");
            requestFocus(edtLocationRemarks);
            return;
        }
        if (TextUtils.isEmpty(edtPostal.getText().toString().trim())) {
            edtPostal.setError("Enter postal code");
            requestFocus(edtPostal);
            return;
        }

        if (TextUtils.isEmpty(edtDateFinding.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select date finding", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtTimeFinding.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select time finding", Toast.LENGTH_SHORT).show();
            return;
        }

        //        if (optionsFindingsAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtFindingsValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select findings", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edtBurrows.getText().toString().trim())) {
            edtBurrows.setError("Enter no of burrows");
            requestFocus(edtBurrows);
            return;
        }
        if (TextUtils.isEmpty(edtDefects.getText().toString().trim())) {
            edtDefects.setError("Enter no of defects");
            requestFocus(edtDefects);
            return;
        }

//        if (optionsBitcenterAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtBitcenterValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select bitcenter", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (optionsHabitatAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtHabitatValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select habitat", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (optionsProbableCauseAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtProbableCauseValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select probable cause", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (optionsRemarksAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtRemarksValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select remarks", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (optionsActionAdapter.selectedCount() == 0) {
        if (TextUtils.isEmpty(txtActionTakenValue.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Select action taken", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = "0";
        if (!TextUtils.isEmpty(formId)) {
            id = formId;
        }
        String landowner = "";



        Action = txtActionTakenValue.getText().toString();
        BitCenter = txtBitcenterValue.getText().toString();
        Findings = txtFindingsValue.getText().toString();
        Habitat = txtHabitatValue.getText().toString();
        ProbableCause = txtProbableCauseValue.getText().toString();
        Remarks = txtRemarksValue.getText().toString();

        String constituency = "1";
        String contractordateresolved = edtDateFinding.getText().toString().trim();
        String date_of_finding = edtDateFinding.getText().toString().trim();
        String feedbacksubstantiated = "";
        String geoaddress = edtLocation.getText().toString().trim();

        String latitude = "";
        if (DashboardActivity.mCurrentLocation != null) {
            latitude = DashboardActivity.mCurrentLocation.getLatitude() + "";
        }

        String longitude = "";
        if (DashboardActivity.mCurrentLocation != null) {
            longitude = DashboardActivity.mCurrentLocation.getLongitude() + "";
        }

        String locationremarks = edtLocationRemarks.getText().toString().trim();
        String neajobid = "";
        String noofburrows = edtBurrows.getText().toString().trim();
        String noofactiveburrows = edtActiveBurrows.getText().toString().trim();
        String noofnonactiveburrows = edtNonActiveBurrows.getText().toString().trim();
        String noofdefects = edtDefects.getText().toString().trim();

        String noofbinchute = edtBinChute.getText().toString().trim();
        String noofcrc = edtCRC.getText().toString().trim();

        String postalcode = edtPostal.getText().toString().trim();
        String time_of_finding = edtTimeFinding.getText().toString().trim();
        String towncouncil = "1";

        if (beanForm == null) {
            beanForm = new JobAllSyncAllSurveyBean();
        }
        if (SharedObjects.isNetworkConnected(getActivity())) {
            beanForm.setIsSync(0); //for local DB
        } else {
            beanForm.setIsSync(1); //for local DB
        }

        beanForm.setId(id);
        beanForm.setLandowner(landowner);
        beanForm.setActiontaken(Action);
        beanForm.setBincenter(BitCenter);
        beanForm.setConstituency(constituency);
        beanForm.setContractordateresolved(contractordateresolved);
        beanForm.setDate_of_finding(date_of_finding);
        beanForm.setFeedbacksubstantiated(feedbacksubstantiated);
        beanForm.setFindings(Findings);
        beanForm.setGeoaddress(geoaddress);
        beanForm.setHabitate(Habitat);
        beanForm.setImageslist(arrServerImages);
        beanForm.setIsdraft(isDraft + "");
        beanForm.setJo_scheduleid(beanMain.getJo_scheduleid());
        beanForm.setLatitude(latitude);
        beanForm.setLocationremarks(locationremarks);
        beanForm.setLongitude(longitude);
        beanForm.setNeajobid(neajobid);
        beanForm.setNoofburrows(noofburrows);
        beanForm.setNoofactiveburrows(noofactiveburrows);
        beanForm.setNoofnonactiveburrows(noofnonactiveburrows);
        beanForm.setNoofburrows(noofburrows);
        beanForm.setNoofdefects(noofdefects);
        beanForm.setNoofbinchute(noofbinchute);
        beanForm.setNoofcrc(noofcrc);
        beanForm.setPostalcode(postalcode);
        beanForm.setProbablecauseofburrows(ProbableCause);
        beanForm.setRemarks(Remarks);
        beanForm.setTime_of_finding(time_of_finding);
        beanForm.setTowncouncil(towncouncil);

        if (SharedObjects.isNetworkConnected(getActivity())) {

            RequestBody isDraftForm;
            if (!isDraft) {
                isDraftForm = RequestBody.create(MediaType.parse("multipart/form-data"), "false");
            } else {
                isDraftForm = RequestBody.create(MediaType.parse("multipart/form-data"), "true");
            }
            showProgressDialog();

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

            Log.d("savesurvey>>","Authorization" + "Bearer " +AppConstants.TOKEN);
            Log.d("savesurvey>>","id" + id);
            Log.d("savesurvey>>","jo_scheduleid" + beanMain.getJo_scheduleid());
            Log.d("savesurvey>>","landowner" + landowner);
            Log.d("savesurvey>>","postalcode" + postalcode);
            Log.d("savesurvey>>","constituency" + constituency);
            Log.d("savesurvey>>","town_council" + towncouncil);
            Log.d("savesurvey>>","date_of_finding" + date_of_finding);
            Log.d("savesurvey>>","time_of_finding" + time_of_finding);
            Log.d("savesurvey>>","bincenter" + BitCenter);
            Log.d("savesurvey>>","findings" + Findings);
            Log.d("savesurvey>>","noofburrows" + noofburrows);
            Log.d("savesurvey>>","activeburrows" + noofactiveburrows);
            Log.d("savesurvey>>","nonactiveburrows" + noofnonactiveburrows);
            Log.d("savesurvey>>","noofdefects" + noofdefects);
            Log.d("savesurvey>>","habitate" + Habitat);
            Log.d("savesurvey>>","probablecauseofburrows" + ProbableCause);
            Log.d("savesurvey>>","neajobid" + neajobid);
            Log.d("savesurvey>>","feedbacksubstantiated" + feedbacksubstantiated);
            Log.d("savesurvey>>","remarks" + Remarks);
            Log.d("savesurvey>>","locationremarks" + locationremarks);
            Log.d("savesurvey>>","actiontaken" + Action);
            Log.d("savesurvey>>","contractordateresolved" + contractordateresolved);
            Log.d("savesurvey>>","latitude" + latitude);
            Log.d("savesurvey>>","longitude" + longitude);
            Log.d("savesurvey>>","geoaddress" + geoaddress);
            Log.d("savesurvey>>","isdraft" + isDraftForm);
            Log.d("savesurvey>>","noofbinchute" + noofbinchute);
            Log.d("savesurvey>>","noofcrc" + noofcrc);
            Log.d("savesurvey>>","file" + file);

            Call<JsonElement> call = RestClientToken.post().saveForm("Bearer " + AppConstants.TOKEN, reqId,
                    reqJoScheduleId, reqLandowner, reqPostal, reqConstituency, reqTownConcil, reqDateFinding, reqTimeFinding,
                    reqBitCenter, reqFinding, reqNoBurrows,reqNoActiveBurrows, reqNoNonActiveBurrows,reqNoDefects, reqHabitate, reqCause, reqNeaJobId, reqFeedbacksubstantiated,
                    reqRemarks, reqLocationremarks, reqActionTaken, reqContractordateresolved, reqLat, reqLong, reqGeoAddress,
                    isDraftForm, reqNoBinChute, reqNoCRC, imglist);
            Log.d("savesurvey>>call","call" + call);

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        dismissProgressDialog();
                        if (response.isSuccessful()) {
                            JSONObject jsonObject = new JSONObject(response.body().toString());

                            Log.d("ckeckresponse>>>",">>" + jsonObject);
                            if (jsonObject.getString(AppConstants.RESPONSE.Success).equalsIgnoreCase(AppConstants.RESPONSE.True)) {
                                if (jsonObject.getString(AppConstants.RESPONSE.msg).equalsIgnoreCase(AppConstants.RESPONSE.Success)) {
                                    SaveFormBean bean = new Gson().fromJson(response.body().toString(), SaveFormBean.class);
                                    if (formId.equals("0")) {
                                        beanForm.setIscomplete("false");
                                    }
                                    formId = bean.getData().getUniqueid();

                                    beanForm.setId(formId);
                                    updateFormInLocal(0);
                                }

                            }
                            Log.d("ckeckresponse>>>1",">>" + jsonObject.getString(AppConstants.RESPONSE.msg));
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
                    dashboardClick = false;
                    Toast.makeText(getActivity(), getActivity().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateFormInLocal(1);


        }
    }

    private void updateFormInLocal(int isSync) {
        if (!beanForm.getId().equalsIgnoreCase("0")) {
            if (arrSurveyForm == null || arrSurveyForm.size() == 0) {
                arrSurveyForm = new ArrayList<>();
                arrSurveyForm.add(beanForm);
            } else {
                if (formPos == -1) {
                    beanForm.setIscomplete("false");
                    arrSurveyForm.add(beanForm);
                }
            }
        } else {
            if (formPos == -1) {
                beanForm.setIscomplete("false");
                arrSurveyForm.add(beanForm);
            }
        }

        beanMain.setAllSurvey(arrSurveyForm);
        int status;
        if (sharedObjects.getUserInfo().getUserrole().equalsIgnoreCase(AppConstants.ROLE.TECHNICIAN)) {
            status = dbHandler.updateJob(sharedObjects.getUserId(), beanMain, isSync);
        } else {
            status = dbHandler.updateJobTLForm(sharedObjects.getUserId(), beanMain, isSync);
        }
        if (status != -1) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.form_saved_success), Toast.LENGTH_SHORT).show();
            if (dashboardClick) {
                ((DashboardActivity) getActivity()).goToDashboard(true);
            } else {
                ((DashboardActivity) getActivity()).selectDrawerItem(AppConstants.MenuPosition.SERVICE_REPORTING, new Bundle());
            }
        }
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
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());

        Button btnDismissDate = (Button) dialog.findViewById(R.id.btnDismissDate);
        btnDismissDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());

                String date = day + "-" + month + "-" + year;
                try {
                    if (type.equalsIgnoreCase("filter")) {
                        edtServiceDate.setText(SharedObjects.convertDateFormat(date,
                                AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE_DISPLAY, AppConstants.DateFormats.DATE_FORMAT_ATTENDANCE));
                    } else {
                        edtDateFinding.setText(SharedObjects.convertDateFormat(date,
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

    private void showTimeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.timepicker_layout);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);//for 24 hrs

        Button btnDismissTime = (Button) dialog.findViewById(R.id.btnDismissTime);
        btnDismissTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = String.valueOf(timePicker.getCurrentHour());
                String minute = String.valueOf(timePicker.getCurrentMinute());
                String time = hour + ":" + minute;

                edtTimeFinding.setText(SharedObjects.convertDateFormat(time, AppConstants.DateFormats.TIME_FORMAT,
                        AppConstants.DateFormats.TIME_FORMAT));
                dialog.dismiss();
            }
        });
        dialog.show();
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void selectImage() {

        final CharSequence[] items = {"Capture Image", "Pick from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Image")) {
//                    userChoosenTask = "Capture Image";
                    takePicture();
                } else if (items[item].equals("Pick from Gallery")) {
//                    userChoosenTask = "Pick from Gallery";
                    //galleryIntent();

                    ImagePicker.create(getActivity())
                            .returnMode(ReturnMode.NONE) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                            .folderMode(true) // set folder mode (false by default)
                            .limit(5)
//                            .single()
                            .showCamera(false)
                            .toolbarFolderTitle("Folder") // folder selection title
                            .toolbarImageTitle("Tap to select")
//                            .toolbarDoneButtonText("DONE") // done button text
                            .start(0); // image selection title

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        getActivity().startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConstants.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat(AppConstants.DateFormats.DATE_TIME_FORMAT_IMAGE,
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Get a list of picked images
                List<Image> images = ImagePicker.getImages(data);

                Log.e("Images ", images.size() + "");
                // or get a single image only
                //           Image image = ImagePicker.getFirstImageOrNull(data)

                compressSelectedImages(images);
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                onCaptureImage();
            }
        } else if (requestCode == SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void compressSelectedImages(List<Image> images) {

        for (int i = 0; i < images.size(); i++) {
            JobAllSyncImageslistBean jobAllSyncImageslistBean = new JobAllSyncImageslistBean(images.get(i).getPath(),
                    images.get(i).getName(), true);
            new CompressImageAsync().execute(jobAllSyncImageslistBean);
        }
    }


    public class CompressImageAsync extends AsyncTask<JobAllSyncImageslistBean, Void, JobAllSyncImageslistBean> {

        Bitmap bitmap;
        Bitmap bitmapNew;
        Bitmap newBit = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobAllSyncImageslistBean doInBackground(JobAllSyncImageslistBean... params) {

            String imageURL = params[0].getFullpath();
            String imageName = params[0].getImagename();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(imageURL, options);

            final int maxSize = 800;
            int outWidth;
            int outHeight;
            int inWidth = bitmap.getWidth();
            int inHeight = bitmap.getHeight();
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            bitmapNew = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);



            //Working but client dont want stemp on image
            /*newBit = null;
            String stamp = "Date Time : " + Calendar.getInstance().getTime() + "-" + "Location : Lat : " + DashboardActivity.mCurrentLocation.getLatitude()
                    + " | Long : " + DashboardActivity.mCurrentLocation.getLongitude();

            RubberStampConfig config = new RubberStampConfig.RubberStampConfigBuilder()
                    .base(bitmapNew)
                    .rubberStamp(stamp)
                    .rubberStampPosition(RubberStampPosition.BOTTOM_LEFT)
                    .alpha(255)
                    .margin(0, -50)
                    .rotation(0)
                    .textColor(Color.WHITE)
                    .textBackgroundColor(getActivity().getResources().getColor(R.color.transparentBlack))
                    *//*.textShadow(0.1f, 5, 5, Color.BLUE)*//*
                    .textSize(16)
                    .textFont("fonts/robotolight.ttf")
                    .build();
            RubberStamp rubberStamp = new RubberStamp(getActivity());
            newBit = rubberStamp.addStamp(config);*/

            //return new JobAllSyncImageslistBean(imageName, bitmapNew);
            //sleep 5 seconds
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*//return new JobAllSyncImageslistBean(imageName, newBit);*/
            return new JobAllSyncImageslistBean(imageName, bitmapNew);
        }

        @Override
        protected void onPostExecute(final JobAllSyncImageslistBean result) {

            ImageStorage.saveToSdCard(result.getBitmap(), result.getImagename());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    arrServerImages.add(new JobAllSyncImageslistBean(result.getFullpath(), result.getImagename(), true));
                    if (formImagesAdapter != null) {
                        formImagesAdapter.notifyDataSetChanged();
                    }
                }
            }, 1000);
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Image> images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            uploadImage(new File(images.get(0).getPath()), images.get(0).getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private void onCaptureImage() {

        new AsyncTask<Void, Void, Void>() {
            Bitmap bitmap;
            Bitmap bitmapNew;
            ProgressDialog progressDialog;
            Bitmap newBit = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMax(100);
                progressDialog.setMessage(getString(R.string.processing_image));
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);

                    final int maxSize = 800;
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap.getWidth();
                    int inHeight = bitmap.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }

                    bitmapNew = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);

                    //Working but client dont want stemp on image
                    /*newBit = null;
                    String stamp = "Date Time : " + Calendar.getInstance().getTime() + "-" + "Location : Lat : " + DashboardActivity.mCurrentLocation.getLatitude()
                            + " | Long : " + DashboardActivity.mCurrentLocation.getLongitude();

                    RubberStampConfig config = new RubberStampConfig.RubberStampConfigBuilder()
                            .base(bitmap)
                            .rubberStamp(stamp)
                            .rubberStampPosition(RubberStampPosition.BOTTOM_LEFT)
                            .alpha(255)
                            .margin(0, -50)
                            .rotation(0)
                            .textColor(Color.WHITE)
                            .textBackgroundColor(getActivity().getResources().getColor(R.color.transparentBlack))
                            *//*.textShadow(0.1f, 5, 5, Color.BLUE)*//*
                            .textSize(16)
                            .textFont("fonts/robotolight.ttf")
                            .build();
                    RubberStamp rubberStamp = new RubberStamp(getActivity());
                    newBit = rubberStamp.addStamp(config);*/

                    //return new JobAllSyncImageslistBean(imageName, bitmapNew);
                    //sleep 5 seconds
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    fileImageSend = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                    try {
                        FileOutputStream fo = new FileOutputStream(fileImageSend);
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
                        /*newBit.compress(Bitmap.CompressFormat.JPEG, 90, fo);*/
                        fo.flush();
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                //uploadImage(fileImageSend);
//                img.put(filePos, fileImageSend);
                arrServerImages.add(new JobAllSyncImageslistBean(fileImageSend.getAbsolutePath(), fileImageSend.getName(), true));
                formImagesAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    private void onSelectFromGalleryResult(final Intent data) {

        new AsyncTask<Void, Void, Void>() {
            Bitmap bitmap;
            byte[] imgByte;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMax(100);
                progressDialog.setMessage(getString(R.string.processing_image));
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                //Bitmap bitmap = null;
                if (data != null) {
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                        final int maxSize = 800;
                        int outWidth;
                        int outHeight;
                        int inWidth = bitmap.getWidth();
                        int inHeight = bitmap.getHeight();
                        if (inWidth > inHeight) {
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }

                        bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()), outWidth, outHeight, true);
                        fileImageSend = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        try {
                            FileOutputStream fo = new FileOutputStream(fileImageSend);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
                            fo.flush();
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
//                img.put(filePos, fileImageSend);
//                arrImages.add(new FormImages("", fileImageSend, false));
                arrServerImages.add(new JobAllSyncImageslistBean(fileImageSend.getAbsolutePath(), fileImageSend.getName(), true));
                formImagesAdapter.notifyDataSetChanged();
                // uploadImage(fileImageSend);
            }

        }.execute();

    }

    // DownloadImage AsyncTask
    public class DownloadImage extends AsyncTask<JobAllSyncImageslistBean, Void, JobAllSyncImageslistBean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobAllSyncImageslistBean doInBackground(JobAllSyncImageslistBean... params) {

            String imageURL = params[0].getFullpath();
            String imageName = params[0].getImagename();

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JobAllSyncImageslistBean(imageName, bitmap);
        }

        @Override
        protected void onPostExecute(final JobAllSyncImageslistBean result) {
            ImageStorage.saveToSdCard(result.getBitmap(), result.getImagename());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    arrServerImages.add(new JobAllSyncImageslistBean(result.getFullpath(), result.getImagename(), true));

//                    arrImages.add(new FormImages("", ImageStorage.getImage(result.getImagename()), false));
                    if (formImagesAdapter != null)
                        formImagesAdapter.notifyDataSetChanged();
                }
            }, 1000);
        }
    }

}
