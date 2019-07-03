package com.surveyor.app.surveyorapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.surveyor.app.surveyorapp.LoginActivity;
import com.surveyor.app.surveyorapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


public class AppConstants {

    public static final String IMAGE_DIRECTORY_NAME = "Surveyor";

    public static String generateIMEI(String imei) {
    StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < imei.length()-6; i++) {
            stringBuilder.append("*");
        }
        stringBuilder.append(imei.substring(imei.length()-6,imei.length()));
        return stringBuilder.toString();
    }

    public static String getVersionNo(Context context) {
        String version = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
             version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }



    public static class MenuPosition{
        public static int ATTENDANCE = 1;
        public static int JOB = 2;
        public static int REPORTING = 3;
        public static int PROFILE = 4;
        public static int LOGOUT = 5;
        public static int DASHBOARD = 6;
        public static int SERVICE_REPORTING = 7;
        public static int SURVEY_FORM = 8;
    }

    public static class RESPONSE{
        public static String Success = "success";
        public static String True = "true";
        public static String False = "false";
        public static String msg = "msg";
        public static String data = "data";
        public static String uniqueid = "uniqueid";
    }

    public static class ROLE{
        public static String SURVEYOR = "SURVEYOR";
        public static String TECHNICIAN = "Technician";
    }

    public static class OPTIONS{
        public static String habitate = "habitate";
        public static String bincenter = "bincenter";
        public static String remarks = "remarks";
        public static String actiontaken = "actiontaken";
        public static String findings = "findings";
        public static String ProbableCauseOfBurrows = "ProbableCauseOfBurrows";
    }

    public static class DateFormats{
        public static String DATE_TIME_FORMAT_IMAGE = "yyyyMMdd_HHmmss";
        public static String DATE_TIME_FORMAT_API = "yyyy-MM-dd HH:mm:ss";
        public static String DATE_TIME_FORMAT_DISPLAY = "dd-MM-yyyy hh:mm a";
        public static String DATE_FORMAT_ATTENDANCE = "dd/MM/yyyy";
        public static String DATE_FORMAT_ATTENDANCE_DISPLAY = "dd-MM-yyyy";
        public static String DATE_FORMAT_MMMM_YYYY = "MM-yyyy";
        public static String DATE_FORMAT_API = "yyyy-MM-dd";
        public static String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss a";
        public static String TIME_FORMAT_API = "HH:mm:ss";
        public static String TIME_FORMAT = "HH:mm";
        public static String TIME_FORMAT_DISPLAY = "hh:mm aa";
        public static String DAY_FORMAT = "E";
        public static String DAY_FORMAT_FULL = "EEEE";
        public static String DATE_FORMAT_DD_MMM = "dd MMM";
        public static String TIME_DASHOBOARD = "EEEE, dd MMM yyyy";

        public static String DF_JOB_API = "MMM dd yyyy HH:mm a"; //Nov 27 2018 00:00 AM
        public static String DF_REPORT_STATISTICS_API = "MMM dd yyyy";
    }

    public static class OFFLINE {
        public static String JOB_TYPE = "JOB_TYPE";
        public static String ALL_TECHNICIAN = "ALL_TECHNICIAN";
    }

    public static final String LOGIN_BEAN = "LoginBean";
    public static final String CHECK_CLOCKIN = "CheckClockIn";

    public static final String INTENT_BEAN = "Bean";
    public static final String INTENT_BUNDLE = "Bundle";
    public static final String INTENT_FORM_ID = "FormId";
    public static final String INTENT_FORM_POS = "FormPos";
    public static final String EMAIL = "Email";

    public static String USERID = "UserId";
    public static String PASSWORD = "Password";
    public static String USERNAME = "UserName";
    public static String PROFILE_PIC = "ProfilePic";

    public static String TOKEN = "Token";

    public static final String IS_FIRST_TIME = "IsFirstTime";
    public static final String STATUS = "STATUS";
    public static final String STATUS_LOGOUT = "STATUS_LOGOUT";
    public static final String STATUS_LOGIN = "STATUS_LOGIN";
    public static final String STATUS_INTERVALTIME = "STATUS_INTERVALTIME";

    private static AlertDialog alertDialog;

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Drawable getAppIcon(Context context, String packageName){
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(packageName);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon ;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        byte[] byteArray = new byte[0];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 65, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }
    
    public static Bitmap getBitmapFromPath(String selectedImagePath, Context context){

        File imgFile = new File(selectedImagePath);
        Bitmap bitmap = null;
        Bitmap bitmapCompress = null;
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Log.e("myBitmap : ", bitmap + "");

        }
        return bitmap;

    }

    public static String getRealPathFromURI(FragmentActivity activity, Uri contentUri) {

        String result;
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static void deleteImage(String path){
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + path);
            } else {
                System.out.println("file not Deleted :" + path);
            }
        }
    }

    public static Bitmap getCompressedBitmap(String imagePath) {
        float maxHeight = 1920.0f;
        float maxWidth = 1080.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        Bitmap updatedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return updatedBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

/*    Uri imageUri = data.getData();
    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);*/


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static AlertDialog showConfirmDialog(String message, Context context,
                                                String btnPositiveName, final DialogInterface.OnClickListener okClickListener,
                                                String btnNegavtiveName, final DialogInterface.OnClickListener cancelClickListener) {
        TextView msgView = new TextView(context);
        msgView.setText(message);
        msgView.setTextSize(18);
        msgView.setPadding(100, 10, 10, 10);
        msgView.setGravity(Gravity.LEFT);
        msgView.setTextColor(Color.BLACK);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (cancelClickListener != null) {
            builder.setView(msgView)
                    .setCancelable(false)
                    .setPositiveButton(btnPositiveName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (okClickListener != null)
                                okClickListener.onClick(dialog, id);
                            dialog.dismiss();
                        }
                    }).setNegativeButton(btnNegavtiveName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (cancelClickListener != null)
                        cancelClickListener.onClick(dialog, i);
                    dialog.dismiss();
                }
            });
        } else {
            builder.setView(msgView)
                    .setCancelable(false)
                    .setPositiveButton(btnPositiveName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (okClickListener != null)
                                okClickListener.onClick(dialog, id);
                            dialog.dismiss();
                        }
                    });
        }

        AlertDialog alert = builder.create();
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setTitle(context.getResources().getString(R.string.app_name));
        alert.show();

        Button nbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.BLACK);
        return alert;
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


}
