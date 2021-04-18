package com.example.sustainableapp.views;

import android.content.Intent;
import android.os.Bundle;
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
    private static IntVariable foundUser;
    private static BooVariable notFoundUser;
    EditText username_et, password_et;
    public static void checkUserFound(List<User> list) {
        if (!list.isEmpty()) {
            foundUser.setID(list.get(0).getId());
            foundUser.getListener().onChange();
        }
    }

    public static void checkUserNotFound() {
        notFoundUser.getListener().onChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        foundUser = new IntVariable();
        foundUser.setListener(() -> {
            Toast.makeText(getApplicationContext(),"Vartotojo prisijungimas sėkmingas",Toast. LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            intent.putExtra("userID", foundUser.getID());
            startActivity(intent);
            finish();
        });
        notFoundUser = new BooVariable();
        notFoundUser.setListener(() -> {
            username_et.setError("Vartotojo duomenys neteisingi");
            password_et.setError("Vartotojo duomenys neteisingi");
        });
    }
    public void openRegisterActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkUser(View view) {
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
            uc.checkUserinDB(enteredUsername, hashedPass, errors);
        }
    }

    


}
