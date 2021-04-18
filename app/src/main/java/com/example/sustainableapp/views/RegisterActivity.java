package com.example.sustainableapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName_et,lastName_et, username_et, photo_et, password1_et, password2_et;
    String firstName, lastName, username, breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime, password1, password2;
    TimePicker breakfast_dp, lunch_dp, dinner_dp, wakingUp_dp, sleeping_dp;
    TextView lunchTime_tv, dinnerTime_tv, sleepingTime_tv;
    ImageView photo_iv;
    private static Bitmap bitmapToUpload;
    int hour, minutes;
    private static ArrayList<String>  errors;
    private static IntVariable foundUser;
    private static BooVariable notFoundUser;
    private static BooVariable goToLogin;
    User userToCheck;
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
        foundUser.setListener(() -> {
            showErrors();
            username_et.setError("Toks vartotojo vardas jau egzistuoja");
            foundUser.setListener(null);
        });
        notFoundUser = new BooVariable();
        notFoundUser.setListener(() -> {
            UserController uc = new UserController();
            int howManyErr = 0;
            for (int i =0; i<errors.size(); i++){
                if (!errors.get(i).equals("")) {
                    howManyErr = howManyErr + 1;
                }
            }
            showErrors();
            if (howManyErr==0) {
                if (uc.addUserToDB(userToCheck)) {
                    Toast.makeText(getApplicationContext(), "Vartotojas sėkmingai užregistruotas", Toast.LENGTH_SHORT).show();
                    uc.findUserByUsername(username_et.getText().toString(), "getUserID" );
                }
            }
        });
        goToLogin = new BooVariable();
        goToLogin.setListener(() -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
        ImageButton image_b = findViewById(R.id.image_b);
        image_b.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Pasirinkite nuotrauką"), 1);

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
            String lunchTime = getString(R.string.lunchTime) + getString(R.string.semicolonAndEnter) + errors.get(3);
            lunchTime_tv.setText(lunchTime);
        }
        else {
            lunchTime_tv.setTextColor(Color.GRAY);
            lunchTime_tv.setText(getString(R.string.lunchTime));
        }
        if (!errors.get(4).equals("")) {
            dinnerTime_tv.setTextColor(Color.RED);
            String dinnerTime = getString(R.string.dinnerTime) + getString(R.string.semicolonAndEnter) + errors.get(4);
            dinnerTime_tv.setText(dinnerTime);
        }
        else {
            dinnerTime_tv.setTextColor(Color.GRAY);
            dinnerTime_tv.setText(getString(R.string.dinnerTime));
        }
        if (!errors.get(5).equals("")) {
            sleepingTime_tv.setTextColor(Color.RED);
            String sleepingTime = getString(R.string.sleepingTime) + getString(R.string.semicolonAndEnter) + errors.get(5);
            sleepingTime_tv.setText(sleepingTime);
        }
        else {
            sleepingTime_tv.setTextColor(Color.GRAY);
            sleepingTime_tv.setText(getString(R.string.sleepingTime));
        }
        if (!errors.get(6).equals("")) {
            password1_et.setError(errors.get(6));
        }
        if (!errors.get(7).equals("")) {
            username_et.setError(errors.get(7));
        }
    }
    public void registerUser(View view) {

        firstName = firstName_et.getText().toString();
        lastName = lastName_et.getText().toString();
        username = username_et.getText().toString();
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
        password1 = password1_et.getText().toString();
        password2 = password2_et.getText().toString();
        userToCheck = new User(firstName, lastName, username, "noPhoto.png",
                breakfastTime, lunchTime, dinnerTime, wakingUpTime, sleepingTime, password1);
        UserController uc = new UserController();
        uc.validateUserForAddingToDB(userToCheck, password2);
    }
    public static void checkUserFound(List<User> list, ArrayList<String> err) {
        errors = err;
        if (!list.isEmpty()) {
            foundUser.setID(list.get(0).getId());
            if (foundUser.getListener() != null) {
                foundUser.getListener().onChange();
            }
        }
        else {
        checkUserNotFound(errors);
        }
    }
    public static void checkUserIDFound(List<User> list) {
        if (!list.isEmpty()) {
            if (bitmapToUpload != null) {
                uploadPhotoToFirebase(bitmapToUpload, list.get(0).getId());
            }
        }
        goToLogin.getListener().onChange();
    }

    public static void checkUserNotFound(ArrayList<String>  err) {
        errors = err;
        notFoundUser.getListener().onChange();
    }
    public void openLoginActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                InputStream inputStream = null;
                if (data != null) {
                    inputStream = getContentResolver().openInputStream(data.getData());
                }
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
