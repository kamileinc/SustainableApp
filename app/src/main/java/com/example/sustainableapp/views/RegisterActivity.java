package com.example.sustainableapp.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName_et,lastName_et, username_et, photo_et, address_et, workingDayTrips_etn, weekendTrips_etn, takingShowerPerWeek_etn, showerTime_etn, takingBathPerWeek_etn, password1_et, password2_et;
    RadioButton car_rb, bicycle_rb;
    Spinner dietS, dietChangeS, workingDayTransportS, weekendDayTransportS;
    String breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime;
    TimePicker breakfast_dp, lunch_dp, dinner_dp, wakingUp_dp, sleeping_dp;
    TextView lunchTime_tv, dinnerTime_tv, sleepingTime_tv;
    int hour, minutes;
    private static ArrayList<String>  errors;
    private static IntVariable foundUser;
    private static BooVariable notFoundUser;
    ArrayList userData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lunchTime_tv = findViewById(R.id.lunchTime_tv);
        dinnerTime_tv = findViewById(R.id.dinnerTime_tv);
        sleepingTime_tv = findViewById(R.id.sleepingTime_tv);
        firstName_et = findViewById(R.id.firstName_et);
        lastName_et = findViewById(R.id.lastName_et);
        username_et = findViewById(R.id.username_et);
        photo_et = findViewById(R.id.photo_et);
        address_et = findViewById(R.id.address_et);

        breakfast_dp = findViewById(R.id.breakfast_dp);
        breakfast_dp.setIs24HourView(true);
        lunch_dp = findViewById(R.id.lunch_dp);
        lunch_dp.setIs24HourView(true);
        dinner_dp = findViewById(R.id.dinner_dp);
        dinner_dp.setIs24HourView(true);
        wakingUp_dp = findViewById(R.id.wakingUpTime_dp);
        wakingUp_dp.setIs24HourView(true);
        sleeping_dp = findViewById(R.id.sleepingTime_dp);
        sleeping_dp.setIs24HourView(true);

        car_rb = findViewById(R.id.car_rb);
        bicycle_rb = findViewById(R.id.bicycle_rb);

        workingDayTrips_etn = findViewById(R.id.workingDayTrips_etn);
        weekendTrips_etn = findViewById(R.id.weekendTrips_etn);
        takingShowerPerWeek_etn = findViewById(R.id.takingShowerPerWeek_etn);
        showerTime_etn = findViewById(R.id.showerTime_etn);
        takingBathPerWeek_etn = findViewById(R.id.takingBathPerWeek_etn);

        password1_et = findViewById(R.id.password1_et);
        password2_et = findViewById(R.id.password2_et);

        dietS = findViewById(R.id.diet_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietS.setAdapter(adapter);
        dietChangeS = findViewById(R.id.dietChange_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietChangeS.setAdapter(adapter2);
        workingDayTransportS = findViewById(R.id.workingDayTransport_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.transport_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workingDayTransportS.setAdapter(adapter3);
        weekendDayTransportS = findViewById(R.id.weekendDayTransport_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.transport_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekendDayTransportS.setAdapter(adapter4);
        foundUser = new IntVariable();
        foundUser.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Toast.makeText(getApplicationContext(),"Vartotojo vardas jau yra db",Toast. LENGTH_LONG).show();
                Log.i("mano", "salala1");
                showErrors();
                username_et.setError("Toks vartotojo vardas jau egzistuoja");
                foundUser.setListener(null);
            }
        });
        notFoundUser = new BooVariable();
        notFoundUser.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                Log.i("mano", "salala2");
                Toast.makeText(getApplicationContext(),"Vartotojo yra autentiskas",Toast. LENGTH_LONG).show();
                UserController uc = new UserController();
                int howManyErr = 0;
                for (int i =0; i<errors.size(); i++){
                    if (!errors.get(i).equals("")) {
                        howManyErr = howManyErr + 1;
                    }
                }
                showErrors();
                if (howManyErr==0) {
                    if (uc.addUserToDB(userData)) {
                        Toast.makeText(getApplicationContext(), "Vartotojas sėkmingai užregistruotas", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi langai turi būti užpildyti ir slaptažodis turi sutapti", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void showErrors() {
        if (!errors.get(0).equals("")) {
            firstName_et.setError(errors.get(0));
        }
        if (!errors.get(1).equals("")) {
            lastName_et.setError(errors.get(1));
        }
        if (!errors.get(2).equals("")) {
            photo_et.setError(errors.get(2));
        }
        if (!errors.get(3).equals("")) {
            address_et.setError(errors.get(3));
        }

        if (!errors.get(4).equals("")) {
            lunchTime_tv.setTextColor(Color.RED);
            lunchTime_tv.setText("Pietų laikas: \n" + errors.get(4));
        }
        else {
            lunchTime_tv.setTextColor(Color.GRAY);
            lunchTime_tv.setText("Pietų laikas");
        }
        if (!errors.get(5).equals("")) {
            dinnerTime_tv.setTextColor(Color.RED);
            dinnerTime_tv.setText("Vakarienės laikas: \n" + errors.get(5));
        }
        else {
            dinnerTime_tv.setTextColor(Color.GRAY);
            dinnerTime_tv.setText("Vakarienės laikas");
        }
        if (!errors.get(6).equals("")) {
            sleepingTime_tv.setTextColor(Color.RED);
            sleepingTime_tv.setText("Miego laikas: \n" + errors.get(6));
        }
        else {
            sleepingTime_tv.setTextColor(Color.GRAY);
            sleepingTime_tv.setText("Miego laikas");
        }

        if (!errors.get(7).equals("")) {
            workingDayTrips_etn.setError(errors.get(7));
        }
        if (!errors.get(8).equals("")) {
            weekendTrips_etn.setError(errors.get(8));
        }
        if (!errors.get(9).equals("")) {
            takingShowerPerWeek_etn.setError(errors.get(9));
        }
        if (!errors.get(10).equals("")) {
            showerTime_etn.setError(errors.get(10));
        }
        if (!errors.get(11).equals("")) {
            takingBathPerWeek_etn.setError(errors.get(11));
        }
        if (!errors.get(12).equals("")) {
            password1_et.setError(errors.get(12));
        }
        if (!errors.get(13).equals("")) {
            username_et.setError(errors.get(13));
        }
    }
    public void registerUser(View view) {

        String firstName = firstName_et.getText().toString();
        String lastName = lastName_et.getText().toString();
        String username = username_et.getText().toString();
        String photo = photo_et.getText().toString();
        String address = address_et.getText().toString();

        hour = breakfast_dp.getHour();
        minutes = breakfast_dp.getMinute();
        breakfastTime = hour + ":" + minutes;

        hour = lunch_dp.getHour();
        minutes = lunch_dp.getMinute();
        lunchTime = hour + ":" + minutes;

        hour = dinner_dp.getHour();
        minutes = dinner_dp.getMinute();
        dinnerTime = hour + ":" + minutes;

        hour = wakingUp_dp.getHour();
        minutes = wakingUp_dp.getMinute();
        wakingUpTime = hour + ":" + minutes;

        hour = sleeping_dp.getHour();
        minutes = sleeping_dp.getMinute();
        sleepingTime = hour + ":" + minutes;
        String transport = "0";
        boolean car = car_rb.isChecked();
        boolean bicycle = bicycle_rb.isChecked();
        if (car == true) {
            transport = "1";
        }
        if (bicycle == true) {
            transport = "2";
        }
        if (car == true && bicycle == true) {
            transport = "4";
        }

        String workingDayTrips = workingDayTrips_etn.getText().toString();
        String weekendDayTrips = weekendTrips_etn.getText().toString();
        String takingShowerPerWeek = takingShowerPerWeek_etn.getText().toString();
        String showerTime = showerTime_etn.getText().toString();
        String takingBathPerWeek = takingBathPerWeek_etn.getText().toString();

        String password1 = password1_et.getText().toString();
        String password2 = password2_et.getText().toString();

        String diet = dietS.getSelectedItem().toString();
        String dietChange = dietChangeS.getSelectedItem().toString();
        String workingDayTransport = workingDayTransportS.getSelectedItem().toString();
        String weekendDayTransport = weekendDayTransportS.getSelectedItem().toString();
        User userToCheck = new User(firstName, lastName, username, photo, address,
                diet, dietChange, breakfastTime, lunchTime, dinnerTime,
                wakingUpTime, sleepingTime, transport, workingDayTrips, workingDayTransport,
                weekendDayTrips, weekendDayTransport, takingShowerPerWeek, showerTime, takingBathPerWeek, password1);
        userData = new ArrayList<>();
        userData.add(firstName);
        userData.add(lastName);
        userData.add(username);
        userData.add(photo);
        userData.add(address);
        userData.add(diet);
        userData.add(dietChange);
        userData.add(breakfastTime);
        userData.add(lunchTime);
        userData.add(dinnerTime);
        userData.add(wakingUpTime);
        userData.add(sleepingTime);
        userData.add(transport);
        userData.add(workingDayTrips);
        userData.add(workingDayTransport);
        userData.add(weekendDayTrips);
        userData.add(weekendDayTransport);
        userData.add(takingShowerPerWeek);
        userData.add(showerTime);
        userData.add(takingBathPerWeek);
        userData.add(password1);
        userData.add(password2);
        UserController uc = new UserController();
        uc.validateUserForAddingToDB(userToCheck, password2);
    }
    public static void checkUserFound(List<User> list, ArrayList<String> err) {
        errors = err;
        Log.i("mano", "radom " + list.size() + "...."  +  list.get(0).toString());
        if (!list.isEmpty()) {
            foundUser.setID(list.get(0).getId());
            if (foundUser.getListener() != null) {
                foundUser.getListener().onChange();
                list = null;
            }
        }
        else {
        checkUserNotFound(list, errors);
        }
    }

    public static void checkUserNotFound(List<User> list, ArrayList<String>  err) {
        errors = err;
        Log.i("mano", "neradom");
        notFoundUser.getListener().onChange();
    }
    public void openLoginActivity(View view) {
        Log.i("mano", "has to open Login Activity");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
