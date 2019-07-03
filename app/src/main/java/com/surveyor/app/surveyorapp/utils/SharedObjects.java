package com.surveyor.app.surveyorapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.media.ExifInterface;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.LoginBean;

import io.fabric.sdk.android.Fabric;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sunil on 23-Oct-16.
 */
public class SharedObjects extends MultiDexApplication {

    public static Context context;
    public static int PRIVATE_MODE = 0;
    public static String PREF_NAME = "SurveyorApp";

    private static SharedObjects instance;

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    public SharedObjects() {
    }

    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }

    public SharedObjects(Context context) {
        this.context = context;
        sharedPreference = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreference.edit();

        initializeStetho();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/robotolight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public void initializeStetho() {
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(context);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    public static boolean isValidEmail(String emailid) {
        return Patterns.EMAIL_ADDRESS.matcher(emailid).matches();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideKeyboard(View view, Context c) {
        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getImeiNumber(Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version ;
    }

    public static String getTodaysDate(String dateFormat){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getTodaysDateSPore(String dateFormat){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        df.setTimeZone(TimeZone.getTimeZone("Singapore"));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String convertDateFormat(String dateString, String originalDateFormat, String outputDateFormat) {
        String finalDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(originalDateFormat);
        try {
            java.util.Date date = simpleDateFormat.parse(dateString);
            simpleDateFormat = new SimpleDateFormat(outputDateFormat);
            finalDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    public static void showAlertDialog(Context context, String Title, String Msg) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder
                .setMessage(Msg)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public String getToken(){
        LoginBean bean = new Gson().fromJson(getPreference(AppConstants.LOGIN_BEAN),LoginBean.class);
        return bean.getData().getToken();
    }

    public LoginBean.Userinfo getUserInfo(){
        LoginBean bean = new Gson().fromJson(getPreference(AppConstants.LOGIN_BEAN),LoginBean.class);
        return bean.getData().getUserinfo();
    }

    public String getUserId(){
        LoginBean bean = new Gson().fromJson(getPreference(AppConstants.LOGIN_BEAN),LoginBean.class);
        return bean.getData().getUserinfo().getUserid();
    }

    public Bitmap getProfilePic(){
        Bitmap bitmap ;
        if(!TextUtils.isEmpty(getPreference(AppConstants.PROFILE_PIC))){
            byte[] b = Base64.decode(getPreference(AppConstants.PROFILE_PIC), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }else{
            bitmap = null ;
        }
        return bitmap;
    }

    public void saveProfilePic(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] arr = baos.toByteArray();

        setPreference(AppConstants.PROFILE_PIC, Base64.encodeToString(arr, Base64.DEFAULT));
    }

    public void saveProfilePic1(String path, Bitmap bitmap){

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90, baos);
        byte[] arr = baos.toByteArray();

        setPreference(AppConstants.PROFILE_PIC, Base64.encodeToString(arr, Base64.DEFAULT));
    }

    public static byte[] rotateImageIfRequired(Uri uri, byte[] fileBytes) {
        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);
        ByteArrayOutputStream outputStream = null;

        try {
            bitmap = rotateImageIfRequired(bitmap, uri);
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            data = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Intentionally blank
            }
        }

        return data;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Log.e("orientation: %s", "" + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void setPreference(String key, String value) {
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreference(String key) {
        try {
            return sharedPreference.getString(key, "");
        } catch (Exception exception) {
            return "";
        }
    }

    public void setIntervalPreference(String key, String value) {
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getIntervalPreference(String key) {
        try {
            return sharedPreference.getString(key, "");
        } catch (Exception exception) {
            return "";
        }
    }

    public void removeSinglePreference(String pref) {
        if (sharedPreference.contains(pref)) {
            editor = sharedPreference.edit();
            editor.remove(pref);
            editor.commit();
        }
    }

    public void clear() {
        editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
    }
}
