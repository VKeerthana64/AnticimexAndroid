package com.surveyor.app.surveyorapp.sync_service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.SplashActivity;
import com.surveyor.app.surveyorapp.retrofit.RestClient;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateLocationService extends Service {
    BroadcastReceiver br;
    SharedObjects sharedObjects ;
    public static boolean firstConnect = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkInternetConnection();
        return START_STICKY;
    }

    private void checkInternetConnection() {
        sharedObjects = new SharedObjects(getApplicationContext());

        if (br == null) {
            Log.e("BR","null");
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.e("BR","onReceive " + "" + checkInternetConnection() + " - " + firstConnect);
                    if (checkInternetConnection()){
                        if(firstConnect) {
                            Log.e("Device","Connected");
                            firstConnect = false;

                            Log.d("latitude>>",">>" + DashboardActivity.mCurrentLocation.getLatitude());
                            Log.d("Longitude>>",">>" + DashboardActivity.mCurrentLocation.getLongitude());
                            RequestBody reqLatitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLatitude()+"");
                            RequestBody reqLongitude = RequestBody.create(MediaType.parse("multipart/form-data"), DashboardActivity.mCurrentLocation.getLongitude()+"");

                            Log.d("latitude>>==",">>" + reqLatitude);
                            Log.d("Longitude>>==",">>" + reqLongitude);
                            RestClient restClient = new RestClient(getApplicationContext());
                                Call<JsonElement> call = restClient.post().insertLocation(reqLatitude,reqLongitude);
                                call.enqueue(new Callback<JsonElement>()
                                {
                                    @Override
                                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                        try
                                        {
                                            JSONObject jsonObject = new JSONObject(response.body().toString());
                                            Log.e("checkloc:", ">>>>"+ jsonObject);
                                            Toast.makeText(getApplicationContext(),"Location Sent Successfully",Toast.LENGTH_LONG);
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonElement> call, Throwable t) {
                                        //  dismissProgressDialog();
                                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                    }
                    else
                    {
                        firstConnect= true;
                        Log.e("Device","Not Connected");
                    }
                }
                public boolean checkInternetConnection() {
                    final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
                    return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(br, intentFilter);
        }else{
            Log.e("BR","not null");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (br != null) {
            unregisterReceiver(br);
            Log.e("Service", " onDestroy: is unregistered.");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
