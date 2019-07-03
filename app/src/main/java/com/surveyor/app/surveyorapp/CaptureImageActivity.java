package com.surveyor.app.surveyorapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CaptureImageActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView imgPreview, imgCapture, imgClose, imgDone, imgPreviewPic;
    TextView txtDateTime, txtLatLngPic, txtDateTimePic ;
    TextView txtLatLng;
    Bitmap mainbitmap;

    private CameraView camera;
    File fileSend ;

    RelativeLayout rlCaptureContent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();

        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            initCamera();
            if (camera != null) {
                camera.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            /*if (camera != null) {
                camera.start();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_capture_image);
    }

    private void initView() {
        rlCaptureContent = findViewById(R.id.rlCaptureContent);

        imgClose = findViewById(R.id.imgClose);
        imgDone = findViewById(R.id.imgDone);
        imgCapture = findViewById(R.id.imgCapture);
        camera = findViewById(R.id.camera);
        imgPreview = findViewById(R.id.imgPreview);
        imgPreviewPic = findViewById(R.id.imgPreviewPic);

        txtDateTimePic = findViewById(R.id.txtDateTimePic);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtLatLngPic = findViewById(R.id.txtLatLngPic);
        txtLatLng = findViewById(R.id.txtLatLng);
//        imgPreview.setOnClickListener(this);

        imgCapture.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgDone.setOnClickListener(this);

        imgDone.setVisibility(View.GONE);
        imgClose.setVisibility(View.GONE);
    }

    private void initCamera() {
        if (getCameraCount() == 2) {
            camera.setFacing(Facing.FRONT);
        } else {
            camera.setFacing(Facing.BACK);
        }

        camera.setRotation(0);
        camera.addCameraListener(new CameraListener() {
            public void onCameraOpened(CameraOptions options) {
            }

            public void onPictureTaken(byte[] jpeg) {
                onPicture(jpeg);
            }
        });
    }

    private void onPicture(final byte[] jpeg) {

        CameraUtils.decodeBitmap(jpeg, 500, 500, new CameraUtils.BitmapCallback() {
            @SuppressLint("WrongThread")
            @Override
            public void onBitmapReady(Bitmap bitmap) {

                imgClose.setVisibility(View.VISIBLE);
                imgDone.setVisibility(View.VISIBLE);
                imgPreview.setVisibility(View.VISIBLE);
                imgCapture.setVisibility(View.INVISIBLE);

                txtDateTime.setText("Date Time : "+Calendar.getInstance().getTime());
                txtLatLng.setText("Location : Lat : " + DashboardActivity.mCurrentLocation.getLatitude()
                        + " | Long : " + DashboardActivity.mCurrentLocation.getLongitude());

                txtDateTimePic.setText("Date Time : "+Calendar.getInstance().getTime());
                txtLatLngPic.setText("Location : Lat : " + DashboardActivity.mCurrentLocation.getLatitude()
                        + " | Long : " + DashboardActivity.mCurrentLocation.getLongitude());

                if (getCameraCount() == 2) {
                    mainbitmap = flip(bitmap);
                } else {
                    mainbitmap = bitmap;
                }

//                imgPreview.setImageBitmap(drawTextToBitmap(mainbitmap,data));
                imgPreview.setImageBitmap(mainbitmap);
                imgPreviewPic.setImageBitmap(mainbitmap);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // with latlong and date
                        Bitmap bitmapWithlatlong = Bitmap.createBitmap(rlCaptureContent.getWidth(), rlCaptureContent.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmapWithlatlong);
                        //

                        initCamera();

                        fileSend = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        try {
                            FileOutputStream fo = new FileOutputStream(fileSend);
                            rlCaptureContent.draw(canvas);

                            bitmapWithlatlong.compress(Bitmap.CompressFormat.JPEG, 90, fo);
                            fo.flush();
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },500);


            }
        });
    }

   /* public void save(View v, String StoredPath) {
        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        try {
            // Output the file
            FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
            v.draw(canvas);

            // Convert the output file to Image such as .png
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }

    }
*/
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConstants.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
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
    protected void onPause() {
        super.onPause();
        try {
            if (camera != null) {
                camera.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgClose:
                imgClose.setVisibility(View.GONE);
                imgDone.setVisibility(View.GONE);
                imgPreview.setVisibility(View.GONE);

                imgCapture.setVisibility(View.VISIBLE);

                imgPreview.setVisibility(View.GONE);

                txtDateTime.setText("");
                txtLatLng.setText("");

                initCamera();
                break;

            case R.id.imgCapture:
                try {
                    if (SharedObjects.isNetworkConnected(CaptureImageActivity.this)) {
                        camera.capturePicture();
                    } else {
                        SharedObjects.showAlertDialog(CaptureImageActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imgDone:
                if (SharedObjects.isNetworkConnected(CaptureImageActivity.this) && fileSend != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("image", fileSend);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    SharedObjects.showAlertDialog(CaptureImageActivity.this, getString(R.string.err_internet_title), getString(R.string.err_internet));
                }
                break;

        }
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e("Permissions", "All permissions are granted!");

                            initCamera();
                            if (camera != null) {
                                camera.start();
                            }

                        }
/*                        if (!report.areAllPermissionsGranted()) { // for taking permission if user denied any of the permission
                            requestPermission();
                        }*/

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (camera != null) {
            camera.stop();
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        try {
            if (camera != null) {
                camera.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CaptureImageActivity.this);
        builder.setTitle(getString(R.string.permission_title));
        builder.setMessage(getString(R.string.permission_msg));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public int getCameraCount() {
        return Camera.getNumberOfCameras();
    }


    //the front camera displays the mirror image, we should flip it to its original
    Bitmap flip(Bitmap d) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap src = d;
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }


    public void showAlertDialog(String Title, String Msg) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CaptureImageActivity.this);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder
                .setMessage(Msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
