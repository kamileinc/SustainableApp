package com.example.sustainableapp.views;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sustainableapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        createNotificationChannel();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(WheelFragment.newInstance("", ""));
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        Log.i("mano", "activity: " + userID);
        IntentFilter filtras = new IntentFilter();
        filtras.addAction("broadcastas");
        this.registerReceiver(new transl_imtuvas2(), filtras);
        createBroadcast();
        Log.i("mano", "broadcast created?activityje");
        openFragment(new WheelFragment());
        //openFragment(new EditProfileFragment());
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.ic_wheel:
                            openFragment(WheelFragment.newInstance("", ""));
                            return true;
                        case R.id.ic_tasks:
                            openFragment(TasksFragment.newInstance("", ""));
                            return true;
                        case R.id.ic_all_results:
                            openFragment(AllResultsFragment.newInstance("", ""));
                            return true;
                        case R.id.ic_my_results:
                            openFragment(MyResultsFragment.newInstance("", ""));
                            return true;
                        case R.id.ic_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i ("mano", "pasirinktas menu komponentas");
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout(this.getApplicationContext());
                break;
            default:
                break;
        }
        return true;
    }
    public void logout(Context context) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void createBroadcast() {
        Calendar alarmFor = Calendar.getInstance();

        Calendar alarmFor2 = Calendar.getInstance();
        alarmFor.set(Calendar.HOUR_OF_DAY, 18);
        alarmFor.set(Calendar.MINUTE, 51);
        alarmFor.set(Calendar.SECOND, 0);

        Intent MyIntent = new Intent(this, UserActivity.transl_imtuvas2.class);
        PendingIntent MyPendIntent = PendingIntent.getBroadcast(this, 0, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager MyAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        MyAlarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmFor.getTimeInMillis(), MyPendIntent);

        Log.i("mano", "broadcast created???activityje: " + alarmFor.getTime());
        Log.i("mano", "broadcast created???activityje: " + alarmFor2.getTime());
    }
    public static class transl_imtuvas2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {
            Log.i("mano", "broadcast onReceive() called");
            sendNotification(context);
        }
    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "textTitle";
            String description ="textContent";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static void sendNotification(Context context) {
        Intent resultIntent = new Intent(context, UserActivity.class);
        resultIntent.putExtra("tekstas", "tekstas?");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.ic_baseline_category_24)
                .setContentTitle("Pranešimas")
                .setContentText("Užpildyk šios dienos užduotis")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
