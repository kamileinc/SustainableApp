package com.example.sustainableapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.R;
import com.example.sustainableapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    //private static booleanVar findUser;
    private static IntVariable foundUser;
    private static BooVariable notFoundUser;
    EditText username_et, password_et;
    // Boolean findUser = false;

    public static void checkUserFound(List<User> list, ArrayList<String> errors) {
        Log.i("mano", "radom" + list.get(0).toString());
        if (!list.isEmpty()) {
            foundUser.setID(list.get(0).getId());
            foundUser.getListener().onChange();
        }
    }

    public static void checkUserNotFound(List<User> list, ArrayList<String> errors) {
        Log.i("mano", "neradom");
        notFoundUser.getListener().onChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        foundUser = new IntVariable();
        foundUser.setListener(new IntVariable.ChangeListener() {
            @Override
            public void onChange() {
                Toast.makeText(getApplicationContext(),"Vartotojo prisijungimas sėkmigas",Toast. LENGTH_LONG).show();
                Log.i("mano", "salala1");
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("userID", foundUser.getID());
                startActivity(intent);
                finish();


            }
        });
        notFoundUser = new BooVariable();
        notFoundUser.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                username_et.setError("Vartotojo duomenys neteisingi");
                password_et.setError("Vartotojo duomenys neteisingi");
                Toast.makeText(getApplicationContext(),"Vartotojo duomenys neteisingi",Toast. LENGTH_LONG).show();
            }
        });
    }
    public void openRegisterActivity(View view) {
        Log.i("mano", "has to open Register Activity");
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkUser(View view) {
        Log.i("mano", "Checking login data");
        String enteredUsername = username_et.getText().toString().trim();
        String enteredPassword = password_et.getText().toString().trim();
        UserController uc = new UserController();
        ArrayList<String> errors = uc.validateLogin(enteredUsername, enteredPassword);
        int howManyErrors = 0;
        if (!errors.get(0).equals("")) {
            username_et.setError(errors.get(0));
            howManyErrors = howManyErrors + 1;
        }
        if (!errors.get(1).equals("")) {
            password_et.setError(errors.get(1));
            howManyErrors = howManyErrors + 1;
        }
        if (howManyErrors == 0 ) {
            String hashedPass = uc.hashingPassword(enteredPassword);
            Log.i("mano", "username: " + enteredUsername + ", hashed pass: " + hashedPass);
            uc.checkUserinDB(enteredUsername, hashedPass, errors);
        }
        // Intent intent = new Intent(getApplicationContext(), SecondScreen.class);
    }

    


}
