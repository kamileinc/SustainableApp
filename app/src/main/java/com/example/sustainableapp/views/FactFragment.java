package com.example.sustainableapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.FactController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.Database;
import com.example.sustainableapp.models.Fact;
import com.example.sustainableapp.models.SustainableAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class FactFragment extends Fragment {
    String userID;
    String category;
    TextView title_tv, fact_tv;
    static ArrayList<Fact> factsList = new ArrayList<>();
    private static BooVariable factsListReturned;
    public FactFragment() {
    }

    public static FactFragment newInstance(String param1, String param2) {
        FactFragment fragment = new FactFragment();
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
            category = bundle.getString("category", "-");
            Log.i("mano", "user id: " + userID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fact, container, false);
        Button toTask_b = (Button) view.findViewById(R.id.toTask_b);
        toTask_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openTasksFragment(view);
            }
        });
        factsListReturned = new BooVariable();
        factsListReturned.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (factsListReturned.isBoo()) {
                    int pickedFactInt = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    Date d = new Date();
                    String dayOfTheWeek = sdf.format(d);
                    Log.i("mano", "siandien yra: " + dayOfTheWeek + factsList.size());
                    if (dayOfTheWeek.equals("Monday")) {
                        pickedFactInt = 0;
                    }
                    else if (dayOfTheWeek.equals("Tuesday")) {
                        pickedFactInt = 1;
                    }
                    else if (dayOfTheWeek.equals("Wednesday")) {
                        pickedFactInt = 2;
                    }
                    else if (dayOfTheWeek.equals("Thursday")) {
                        pickedFactInt = 3;
                    }
                    else if (dayOfTheWeek.equals("Friday")) {
                        pickedFactInt = 4;
                    }
                    else if (dayOfTheWeek.equals("Saturday")) {
                        pickedFactInt = 5;
                    }
                    else if (dayOfTheWeek.equals("Sunday")) {
                        pickedFactInt = 6;
                    }
                    fact_tv.setText(factsList.get(pickedFactInt).getText());
                }
                else {

                }
            }
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title_tv = getView().findViewById(R.id.title_tv);
        fact_tv = getView().findViewById(R.id.fact_tv);
        FactController fc = new FactController();
        fc.getFacts(category);
        title_tv.setText("Šios savaitės kategorija - ");
        if (category.equals("Transport")) {
            title_tv.setText("Šios savaitės kategorija - TRANSPORTAS");
        }
        else  if (category.equals("Food")) {
            title_tv.setText("Šios savaitės kategorija - MAISTAS");
        }
        else  if (category.equals("Energy")) {
            title_tv.setText("Šios savaitės kategorija - BŪSTAS");
        }
    }
    public void openTasksFragment(View view) {
        getActivity().findViewById(R.id.ic_tasks).performClick();
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TasksFragment fragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static void checkFactsFound(ArrayList<Fact> facts) {
        Log.i("mano", "radom faktus:" + facts.size());
        factsList = facts;
        factsListReturned.setBoo(true);
        factsListReturned.getListener().onChange();

    }
}