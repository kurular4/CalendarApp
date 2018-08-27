package com.omer.user.calendarapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.omer.user.calendarapp.RestApi.ManagerAll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    boolean check;

    public NotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        check = true;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (check)
                    send_notification();
            }
        },0,5000);
        return START_REDELIVER_INTENT;
    }

    private void send_notification() {
        Call<String> x = ManagerAll.getInstance().send_notification();
        x.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
               if(!response.body().toString().equals("{error:1}"))
                   check = false;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("jsonres", t.getMessage().toString());
                check = false;
            }
        });
    }


}
