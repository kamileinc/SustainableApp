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
import android.os.Handler;
import android.text.format.DateFormat;
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
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    static String userID;
    static String id1 = "1";
    static String id2 = "2";
    static String id3 = "3";
    String category;
    String breakfastTime = "";
    String lunchTime = "";
    String dinnerTime = "";
    String wakingUpTime = "";
    String sleepingTime = "";
    NotificationManager notificationManager;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    private static BooVariable broadcastsForNextDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        try {
            String fragment = intent.getStringExtra("fragment");
            Log.i("mano", "fragmentas..." + fragment);
            if (fragment.equals("TasksFragment")) {
                Log.i("mano", "fragmentas2..." + fragment);
                //openFragment(new TasksFragment());
                //openFragment(TasksFragment.newInstance("", ""));
                Fragment frag = new TasksFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("userID", userID);
                frag.setArguments(bundle);
                transaction.replace(R.id.container, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            if (fragment.equals("MyResultsFragment")) {
                Log.i("mano", "fragmentas3..." + fragment);
                //openFragment(new TasksFragment());
                //openFragment(TasksFragment.newInstance("", ""));
                Fragment frag = new MyResultsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("userID", userID);
                frag.setArguments(bundle);
                transaction.replace(R.id.container, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        catch (Exception e) {
            Log.i("mano", "NEfragmentas...");
            openFragment(WheelFragment.newInstance("", ""));
            openFragment(new WheelFragment());
        }
        UserController uc = new UserController();
        uc.getProfile(userID, "UserActivity");
        Log.i("mano", "USER ACTIVITY " );
        getUsersSustainableActions();
        createNotificationChannel(id1);
        createNotificationChannel(id2);
        createNotificationChannel(id3);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
       // openFragment(WheelFragment.newInstance("", ""));

        Log.i("mano", "activity: " + userID);

       // openFragment(new WheelFragment());
        //openFragment(new EditProfileFragment());
        saListReturned = new BooVariable();
        saListReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (saListReturned.isBoo()) {
                    Log.i("mano", "sa kiekis: " + saList.size() + "OBJ: " + saList.get(0));

                    //jei data atitinka dabartine, rodyt faktus
                    String beginDate = saList.get((saList.size()-1)).getDateBegin();
                    String endDate = saList.get((saList.size()-1)).getDateEnd();
                    SustainableActionController sac = new SustainableActionController();
                    if (sac.isTodayInDates(beginDate, endDate)) {
                        //siandiena ieina i tas dienas
                        category = saList.get((saList.size()-1)).getCategory();
                        Log.i("mano", "YES, DATA IEINA");
                        Log.i("mano", "PRIMINIMUS SUKURTI");
                        Log.i("mano", "KATEGORIJA: " + category);
                        Log.i("mano", "breakfastTime: " + breakfastTime);
                        Log.i("mano", "lunchTime: " + lunchTime);
                        Log.i("mano", "dinnerTime: " + dinnerTime);
                        Log.i("mano", "wakingUpTime: " + wakingUpTime);
                        Log.i("mano", "sleepingTime: " + sleepingTime);
                        // priminimus sukurt
                        Date date1 = new Date(System.currentTimeMillis());
                        Calendar c = Calendar.getInstance();
                        c.setTime(date1);

                        Date date = c.getTime();
                        Log.i("mano", "data: " + date);
                        createBroadcasts(date, beginDate, endDate);


                            /*
                            c.setTime(date);
                            c.add(Calendar.DAY_OF_MONTH,  1);
                            date = c.getTime();

                             */

                    }
                }
            }
        });
        broadcastsForNextDay = new BooVariable();
        broadcastsForNextDay.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (broadcastsForNextDay.isBoo()) {
                    Log.i("mano", "create broadcast for next day");
                    createBroadcastsForNextDay();

                }
            }
        });
        foundProfile = new IntVariable();
        foundProfile.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "cia");
                if (profileData != null) {
                    Log.i("mano", profileData.get(0).toString());
                    breakfastTime = uc.formatTime(profileData.get(0).getBreakfastTime());
                    lunchTime = uc.formatTime(profileData.get(0).getLunchTime());
                    dinnerTime = uc.formatTime(profileData.get(0).getDinnerTime());
                    wakingUpTime = uc.formatTime(profileData.get(0).getWakingUpTime());
                    sleepingTime = uc.formatTime(profileData.get(0).getSleepingTime());
                }
            }});
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
    public void createBroadcast(String h, String min, String action) {

        Calendar alarmFor = Calendar.getInstance();

        Calendar alarmFor2 = Calendar.getInstance();
        if (broadcastsForNextDay.isBoo()) {
            Date date1 = new Date(System.currentTimeMillis());
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            Date date = c.getTime();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH,  1);
            date = c.getTime();
            String day = (String) DateFormat.format("dd",   date);
            String month = (String) DateFormat.format("MM",   date);
            Log.i("mano", "mėnuo: " + Integer.parseInt(month) + ", diena: " + Integer.parseInt(day));
            alarmFor.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            alarmFor.set(Calendar.MONTH, Integer.parseInt(month));
        }
        alarmFor.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h));
        alarmFor.set(Calendar.MINUTE, Integer.parseInt(min));
        alarmFor.set(Calendar.SECOND, 0);

        Intent MyIntent = new Intent(this, UserActivity.transl_imtuvas2.class);
        MyIntent.setAction(action);
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
            boolean sendNextDay = false;
            if (i.getAction().equals("Food1")) {
                sendNotification(context, id1, "Siūlymas", "Siūlome rinktis veganišką (❁❁❁), vegetarišką (❁❁) arba mažiau taršos išskiriančios mėsos (❁) patiekalą.", sendNextDay);
            }
            else if (i.getAction().equals("Food2")) {
                sendNotification(context, id2, "Priminimas", "Neužmirškite įvesti, ką valgėte pusryčiams.", sendNextDay);
            }
            else if (i.getAction().equals("Food3")) {
                sendNotification(context, id1, "Siūlymas", "Siūlome rinktis veganišką (❁❁❁), vegetarišką (❁❁) arba mažiau taršos išskiriančios mėsos (❁) patiekalą.", sendNextDay);
            }
            else if (i.getAction().equals("Food4")) {
                sendNotification(context, id2, "Priminimas", "Neužmirškite įvesti, ką valgėte pietums.", sendNextDay);
            }
            else if (i.getAction().equals("Food5")) {
                sendNotification(context, id1, "Siūlymas", "Siūlome rinktis veganišką (❁❁❁), vegetarišką (❁❁) arba mažiau taršos išskiriančios mėsos (❁) patiekalą.", sendNextDay);
            }
            else if (i.getAction().equals("Food6")) {
                sendNextDay = true;
                sendNotification(context, id2, "Priminimas", "Neužmirškite įvesti, ką valgėte vakarienei.", sendNextDay);
                sendNextDay = false;
            }
            else if (i.getAction().equals("Energy1")) {
                sendNotification(context, id1, "Siūlymas", "Siūlome nesimaudyti (❁❁❁) arba rinktis dušą (❁❁) vietoj vonios (❁). Be to, jei maudotės duše, stenkitės užtrukti kuo trumpiau.",sendNextDay);
            }
            else if (i.getAction().equals("Energy2")) {
                sendNextDay = true;
                sendNotification(context, id2, "Priminimas", "Neužmirškite įvesti, maudymosi ir el. prietaisų išjungimo duomenų.",sendNextDay);
                sendNextDay = false;
            }
            else if (i.getAction().equals("Transport1")) {
                sendNotification(context, id1, "Siūlymas", "Siūlome vaikščioti pėsčiomis (❁❁❁), važiuoti dviračiu (❁❁❁), naudotis viešuoju transportu (❁❁). Jei važiuosite automobiliu, pavežkite kartu kuo daugiau žmonių.", sendNextDay);
            }
            else if (i.getAction().equals("Transport2")) {
                sendNextDay = true;
                sendNotification(context, id2, "Priminimas", "Neužmirškite įvesti, kaip šiandien keliavote.", sendNextDay);
                sendNextDay = false;
            }

        }
    }
    private void createNotificationChannel(String id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "textTitle";
            String description ="textContent";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static void sendNotification(Context context, String id, String title, String message, boolean sendNextDay) {
        Intent resultIntent = new Intent(context, UserActivity.class);
        //Intent resultIntent = new Intent(context, TasksFragment.class);
        resultIntent.putExtra("userID", userID);
        if (id.equals(id2)) {
            resultIntent.putExtra("fragment", "TasksFragment");
        }
        if (id.equals(id3)) {
            resultIntent.putExtra("fragment", "MyResultsFragment");
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.ic_baseline_pie_chart_24)
                .setContentTitle(title)
                //.setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);
        NotificationCompat.BigTextStyle bigStyle =
                new NotificationCompat.BigTextStyle();
        bigStyle.setBigContentTitle(title);
        bigStyle.bigText(message);
        builder.setStyle(bigStyle);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Integer.parseInt(id), builder.build());
        if (sendNextDay) {

            broadcastsForNextDay.setBoo(true);

        }
    }
    public void createBroadcastsForNextDay() {
        Log.i("mano", "create broadcast for next day2");
        Date date1 = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        Date date = c.getTime();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,  1);
        date = c.getTime();
        String beginDate = saList.get((saList.size()-1)).getDateBegin();
        String endDate = saList.get((saList.size()-1)).getDateEnd();
        createBroadcasts(date, beginDate, endDate);
        broadcastsForNextDay.setBoo(false);
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "UserActivity";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "radom sa ACTIVITY:" + sa.size());
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();

    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "neradom sa ACTIVITY:" + sa.size());
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();

    }
    public static void checkUserFound(List<User> list) {
        Log.i("mano", "radom USER ACTIVITY" + list.size() + "...."  +  list.get(0).toString());
        profileData = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundProfile.setID(list.get(0).getId());
            if (foundProfile.getListener() != null) {
                foundProfile.getListener().onChange();
                list = null;
            }
        }
        else {
            checkUserNotFound(list);
        }
    }

    public static void checkUserNotFound(List<User> list) {
        Log.i("mano", "neradom user ACTIVITY");
    }
    private void createBroadcasts(Date date, String beginDate, String endDate) {
        SustainableActionController sac = new SustainableActionController();
        UserController uc = new UserController();
        if (sac.isDateInDates(date, beginDate, endDate)) {
            Log.i("mano", "PRIMINIMAI FOR SURE");
            if (category.equals("Food")) {
                if (uc.isUpcomingTime(uc.addTime(breakfastTime, -30))) {
                    IntentFilter filtras1 = new IntentFilter();
                    filtras1.addAction(category + "1");
                    registerReceiver(new transl_imtuvas2(), filtras1);
                    String[] sArr = uc.addTime(breakfastTime, -30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "1");
                }
                if (uc.isUpcomingTime(uc.addTime(breakfastTime, 30))) {
                    IntentFilter filtras2 = new IntentFilter();
                    filtras2.addAction(category + "2");
                    registerReceiver(new transl_imtuvas2(), filtras2);
                    String[] sArr = uc.addTime(breakfastTime, 30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "2");
                }
                if (uc.isUpcomingTime(uc.addTime(lunchTime, -30))) {
                    IntentFilter filtras3 = new IntentFilter();
                    filtras3.addAction(category + "3");
                    registerReceiver(new transl_imtuvas2(), filtras3);
                    String[] sArr = uc.addTime(lunchTime, -30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "3");
                }
                if (uc.isUpcomingTime(uc.addTime(lunchTime, 30))) {
                    IntentFilter filtras4 = new IntentFilter();
                    filtras4.addAction(category + "4");
                    registerReceiver(new transl_imtuvas2(), filtras4);
                    String[] sArr = uc.addTime(lunchTime, 30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "4");
                }

                if (uc.isUpcomingTime(uc.addTime(dinnerTime, -30))) {
                    IntentFilter filtras5 = new IntentFilter();
                    filtras5.addAction(category + "5");
                    registerReceiver(new transl_imtuvas2(), filtras5);
                    String[] sArr = uc.addTime(dinnerTime, -30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "5");
                }
                if (uc.isUpcomingTime(uc.addTime(dinnerTime, 30))) {

                    IntentFilter filtras6 = new IntentFilter();
                    filtras6.addAction(category + "6");
                    registerReceiver(new transl_imtuvas2(), filtras6);
                    String[] sArr = uc.addTime(dinnerTime, 30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "6");
                }
            }

            else if (category.equals("Transport")) {
                if (uc.isUpcomingTime(uc.addTime(wakingUpTime, 30))) {
                    IntentFilter filtras1 = new IntentFilter();
                    filtras1.addAction(category + "1");
                    registerReceiver(new transl_imtuvas2(), filtras1);
                    String[] sArr = uc.addTime(wakingUpTime, 30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "1");
                }
                if (uc.isUpcomingTime(uc.addTime(sleepingTime, -30))) {
                    IntentFilter filtras2 = new IntentFilter();
                    filtras2.addAction(category + "2");
                    registerReceiver(new transl_imtuvas2(), filtras2);
                    String[] sArr = uc.addTime(sleepingTime, -30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1), category + "2");
                }
            }
            else if (category.equals("Energy")) {
                if (uc.isUpcomingTime(uc.addTime(wakingUpTime, 30))) {
                    IntentFilter filtras1 = new IntentFilter();
                    filtras1.addAction(category + "1");
                    registerReceiver(new transl_imtuvas2(), filtras1);
                    String[] sArr = uc.addTime(wakingUpTime, 30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1),  category + "1");
                }
                if (uc.isUpcomingTime(uc.addTime(sleepingTime, -30))) {
                    IntentFilter filtras2 = new IntentFilter();
                    filtras2.addAction(category + "2");
                    registerReceiver(new transl_imtuvas2(), filtras2);
                    String[] sArr = uc.addTime(sleepingTime, -30).split(":", 3);
                    int h1 = Integer.parseInt(sArr[0]);
                    int m1 = Integer.parseInt(sArr[1]);
                    createBroadcast(String.valueOf(h1), String.valueOf(m1),  category + "2");
                }
            }


        }
    }
}
