package com.ybene.projects.tp3_benetreault.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundTimerService extends Service implements IBackgroundTimerService {

    private Timer timer;
    private BackgroundServiceBinder binder;
    private ArrayList<IBackgroundTimerServiceListener> listeners = null;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        binder = new BackgroundServiceBinder(this);
        listeners = new ArrayList<>();
        Log.d(this.getClass().getName(),"Lancement de onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getName(),"Lancement de onStartCommand");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                Integer hours = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minutes = calendar.get(Calendar.MINUTE);
                Integer seconds = calendar.get(Calendar.SECOND);
                Log.d(this.getClass().getName(), hours.toString() + ":" + minutes.toString() + ":" + seconds.toString());

                fireDataChanged(calendar);
            }
        }, 0, 5000);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getName(),"Lancement de onDestroy");
        this.timer.cancel();
        this.listeners.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void addListener(IBackgroundTimerServiceListener listener) {
        if (listeners != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(IBackgroundTimerServiceListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }

        Log.i("BackgroundSerivce", "Disconnected !");
    }

    private void fireDataChanged(Object data) {
        if(listeners != null) {
            for (IBackgroundTimerServiceListener listener : listeners) {
                listener.dataChanged(data);
            }
        }
    }
}
