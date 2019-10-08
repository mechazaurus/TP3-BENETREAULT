package com.ybene.projects.tp3_benetreault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ybene.projects.tp3_benetreault.services.BackgroundServiceBinder;
import com.ybene.projects.tp3_benetreault.services.BackgroundTimerService;
import com.ybene.projects.tp3_benetreault.services.IBackgroundTimerService;
import com.ybene.projects.tp3_benetreault.services.IBackgroundTimerServiceListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button buttonStart, buttonConnexion, buttonDeconnexion, buttonStop;
    private EditText editText;
    private IBackgroundTimerService monService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get objects from the layout
        textView = findViewById(R.id.activity_main_text_view);
        buttonStart = findViewById(R.id.activity_main_button_start);
        buttonConnexion = findViewById(R.id.activity_main_button_connexion);
        buttonDeconnexion = findViewById(R.id.activity_main_button_deconnexion);
        buttonStop = findViewById(R.id.activity_main_button_stop);
        editText = findViewById(R.id.activity_main_edit_text);

        // Set up the timer service
        final Intent timerIntent = new Intent(MainActivity.this, BackgroundTimerService.class);

        // Create a listener
        final IBackgroundTimerServiceListener listener = new IBackgroundTimerServiceListener() {
            @Override
            public void dataChanged(Object o) {
                // Calendar object used to get the hour
                Calendar calendar = (Calendar)o;
                Integer hours = calendar.get(Calendar.HOUR_OF_DAY);
                Integer minutes = calendar.get(Calendar.MINUTE);
                Integer seconds = calendar.get(Calendar.SECOND);
                Log.d(this.getClass().getName(), hours.toString() + ":" + minutes.toString() + ":" + seconds.toString());
            }
        };

        // Create connection
        final ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("BackgroundService", "Connected!");
                monService = ((BackgroundServiceBinder)service).getService();
                monService.addListener(listener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        // On click listener for the start button
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start service
                startService(timerIntent);

                // Enable button, disabled by default to avoid exceptions
                buttonConnexion.setEnabled(true);
            }
        });

        // On click listener for the connexion button
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bind service
                bindService(timerIntent, connection, BIND_AUTO_CREATE);

                // Disable connexion button to avoid execptions
                buttonConnexion.setEnabled(false);
                buttonDeconnexion.setEnabled(true);
            }
        });

        // On click listener for the deconnexion button
        buttonDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Unbind service
                unbindService(connection);

                // Remove listener
                monService.removeListener(listener);

                // Disable deconnexion button to avoid execeptions
                buttonDeconnexion.setEnabled(false);
                buttonConnexion.setEnabled(true);
            }
        });

        // On click listener for the stop button
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // To be sure that we disconnect the service
                if(buttonDeconnexion.isEnabled()) {
                    unbindService(connection);
                    monService.removeListener(listener);
                }

                // Disable buttons to avoid exceptions
                buttonConnexion.setEnabled(false);
                buttonDeconnexion.setEnabled(false);

                // Stop service
                stopService(timerIntent);
            }
        });
    }
}
