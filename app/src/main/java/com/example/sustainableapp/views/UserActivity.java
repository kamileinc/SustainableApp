package com.example.sustainableapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sustainableapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(WheelFragment.newInstance("", ""));
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        Log.i("mano", "activity: " + userID);
        openFragment(new WheelFragment());
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
}
