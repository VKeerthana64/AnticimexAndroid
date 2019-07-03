package com.surveyor.app.surveyorapp.sync_service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.surveyor.app.surveyorapp.utils.DBHandler;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

/**
 * Created by Lenovo on 18-03-2018.
 */

public class ConnectivityBroadcastReceiver extends Service {
    BroadcastReceiver br;

    DBHandler dbHandler ;
    SharedObjects sharedObjects ;
    public static boolean firstConnect = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkInternetConnection();
        return START_STICKY;
    }

    private void checkInternetConnection() {
        dbHandler = new DBHandler(getApplicationContext());
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
                            context.startService(new Intent(context, MySyncService.class));
                           // Toast.makeText(getApplicationContext(),"Sync Successfully",Toast.LENGTH_LONG).show();
                        }
                    }else{
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
