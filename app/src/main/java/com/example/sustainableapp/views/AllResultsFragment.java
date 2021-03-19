package com.example.sustainableapp.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sustainableapp.R;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AllResultsFragment extends Fragment {
    String userID;


    public AllResultsFragment() {
        // Required empty public constructor
    }

    public static AllResultsFragment newInstance(String param1, String param2) {
        AllResultsFragment fragment = new AllResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID", "0");
            Log.i("mano", "user id: " + userID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_results, container, false);
        IntentFilter filtras = new IntentFilter();
        filtras.addAction("2_Programos_nesist_isreikst_trans");
        view.getContext().registerReceiver(new transl_imtuvas2(), filtras);
        createBroadcast(view);
        Log.i("mano", "broadcast created?");
        return view;
    }
    public void createBroadcast(View view) {
        Calendar alarmFor = Calendar.getInstance();
        alarmFor.set(Calendar.HOUR_OF_DAY, 16);
        alarmFor.set(Calendar.MINUTE, 22);
        alarmFor.set(Calendar.SECOND, 0);

        Intent MyIntent = new Intent(view.getContext(), transl_imtuvas2.class);
        PendingIntent MyPendIntent = PendingIntent.getBroadcast(view.getContext(), 0, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager MyAlarm = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
        MyAlarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmFor.getTimeInMillis(), MyPendIntent);
        view.getContext().sendBroadcast(MyIntent);
        Log.i("mano", "broadcast created???");
    }
    public static class transl_imtuvas2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {
            /*
            Log.i("mano", "1 programa: transl_imtuvas2");
            String intent = i.getAction();
            Bundle rezExtra = i.getExtras();
            siuntejas = rezExtra.getString("siuntejas");
            String papildoma_info = rezExtra.getString("papildoma_info");
            {
                final String pranesimas =
                        "Gautas naujas tarnsliavimo pranešimas" +
                                "\n Intentas: " + intent +
                                "\n Siuntėjas: " + siuntejas +
                                " \n Papildoma intento informacija: \n" + papildoma_info;
                textView.setText(pranesimas);
                //Toast.makeText(MainActivity.this, pranesimas, Toast.LENGTH_SHORT).show();
                sendNotification();
            }
            */
            Log.i("mano", "onReceive() called");
        }
    }
}