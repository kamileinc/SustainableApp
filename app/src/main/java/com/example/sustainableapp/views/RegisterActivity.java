package com.example.sustainableapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName_et,lastName_et, username_et, photo_et, password1_et, password2_et;
    String breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime;
    TimePicker breakfast_dp, lunch_dp, dinner_dp, wakingUp_dp, sleeping_dp;
    TextView lunchTime_tv, dinnerTime_tv, sleepingTime_tv;
    ImageView photo_iv;
    private static Bitmap bitmapToUpload;
    int hour, minutes;
    private static ArrayList<String>  errors;
    private static IntVariable foundUser;
    private static BooVariable notFoundUser;
    private static BooVariable goToLogin;
    ArrayList userData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        photo_iv = findViewById(R.id.photo_iv);
        lunchTime_tv = findViewById(R.id.lunchTime_tv);
        dinnerTime_tv = findViewById(R.id.dinnerTime_tv);
        sleepingTime_tv = findViewById(R.id.sleepingTime_tv);
        firstName_et = findViewById(R.id.firstName_et);
        lastName_et = findViewById(R.id.lastName_et);
        username_et = findViewById(R.id.username_et);
        //photo_et = findViewById(R.id.photo_et);

        breakfast_dp = findViewById(R.id.breakfast_dp);
        breakfast_dp.setIs24HourView(true);
        breakfast_dp.setHour(8);
        breakfast_dp.setMinute(0);
        lunch_dp = findViewById(R.id.lunch_dp);
        lunch_dp.setIs24HourView(true);
        lunch_dp.setHour(13);
        lunch_dp.setMinute(0);
        dinner_dp = findViewById(R.id.dinner_dp);
        dinner_dp.setIs24HourView(true);
        dinner_dp.setHour(18);
        dinner_dp.setMinute(0);
        wakingUp_dp = findViewById(R.id.wakingUpTime_dp);
        wakingUp_dp.setIs24HourView(true);
        wakingUp_dp.setHour(7);
        wakingUp_dp.setMinute(0);
        sleeping_dp = findViewById(R.id.sleepingTime_dp);
        sleeping_dp.setIs24HourView(true);
        sleeping_dp.setHour(22);
        sleeping_dp.setMinute(0);


        password1_et = findViewById(R.id.password1_et);
        password2_et = findViewById(R.id.password2_et);

        foundUser = new IntVariable();
        foundUser.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
               // Toast.makeText(getApplicationContext(),"Vartotojo vardas jau yra db",Toast. LENGTH_LONG).show();
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
               // Toast.makeText(getApplicationContext(),"Vartotojo yra autentiskas",Toast. LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Vartotojas sėkmingai užregistruotas", Toast.LENGTH_SHORT).show();

                        //gaut user id
                        uc.findUserByUsername(username_et.getText().toString(), "getUserID" );
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        //finish();
                    } else {
                       // Toast.makeText(getApplicationContext(), "Visi langai turi būti užpildyti ir slaptažodis turi sutapti", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        goToLogin = new BooVariable();
        goToLogin.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton image_b = (ImageButton) findViewById(R.id.image_b);
        image_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pasirinkite nuotrauką"), 1);

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
            lunchTime_tv.setTextColor(Color.RED);
            lunchTime_tv.setText("Pietų laikas: \n" + errors.get(3));
        }
        else {
            lunchTime_tv.setTextColor(Color.GRAY);
            lunchTime_tv.setText("Pietų laikas");
        }
        if (!errors.get(4).equals("")) {
            dinnerTime_tv.setTextColor(Color.RED);
            dinnerTime_tv.setText("Vakarienės laikas: \n" + errors.get(4));
        }
        else {
            dinnerTime_tv.setTextColor(Color.GRAY);
            dinnerTime_tv.setText("Vakarienės laikas");
        }
        if (!errors.get(5).equals("")) {
            sleepingTime_tv.setTextColor(Color.RED);
            sleepingTime_tv.setText("Miego laikas: \n" + errors.get(5));
        }
        else {
            sleepingTime_tv.setTextColor(Color.GRAY);
            sleepingTime_tv.setText("Miego laikas");
        }
        if (!errors.get(6).equals("")) {
            password1_et.setError(errors.get(6));
        }
        if (!errors.get(7).equals("")) {
            username_et.setError(errors.get(7));
        }
    }
    public void registerUser(View view) {

        String firstName = firstName_et.getText().toString();
        String lastName = lastName_et.getText().toString();
        String username = username_et.getText().toString();
        //String photo = photo_et.getText().toString();

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

        String password1 = password1_et.getText().toString();
        String password2 = password2_et.getText().toString();

        User userToCheck = new User(firstName, lastName, username, "noPhoto.png",
                breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime, password1);
        userData = new ArrayList<>();
        userData.add(firstName);
        userData.add(lastName);
        userData.add(username);
        userData.add("noPhoto.png");
        userData.add(breakfastTime);
        userData.add(lunchTime);
        userData.add(dinnerTime);
        userData.add(wakingUpTime);
        userData.add(sleepingTime);
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
    public static void checkUserIDFound(List<User> list, ArrayList<String> err) {
        Log.i("mano", "radom " + list.size() + "...."  +  list.get(0).toString());
        if (!list.isEmpty()) {
            //nuotraukos ikelimas
            if (bitmapToUpload != null) {
                uploadPhotoToFirebase(bitmapToUpload, list.get(0).getId());
            }
        }
        //perejimas i login
        goToLogin.getListener().onChange();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                photo_iv.setImageBitmap(bitmap);
                bitmapToUpload = bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static void uploadPhotoToFirebase(Bitmap bmp, String userID) {
        UserController uc = new UserController();
        uc.uploadPhoto(bmp, userID);

    }
}
