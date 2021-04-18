package com.example.sustainableapp.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.FactController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.Fact;
import java.util.ArrayList;
import java.util.Objects;

public class FactFragment extends Fragment {
    String userID;
    String category;
    TextView title_tv, fact_tv;
    ImageView imageview;
    static ArrayList<Fact> factsList = new ArrayList<>();
    private static BooVariable factsListReturned;
    public FactFragment() {
    }
    public static FactFragment newInstance() {
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fact, container, false);
        Button toTask_b = view.findViewById(R.id.toTask_b);
        toTask_b.setOnClickListener(v -> openTasksFragment());
        factsListReturned = new BooVariable();
        factsListReturned.setListener(() -> {
            if (factsListReturned.isBoo()) {
                fact_tv.setText(factsList.get(0).getText());
            }
        });
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title_tv = Objects.requireNonNull(getView()).findViewById(R.id.title_tv);
        fact_tv = getView().findViewById(R.id.fact_tv);
        FactController fc = new FactController();
        fc.getFacts(category);
        imageview = getView().findViewById(R.id.imageView);

        switch (category) {
            case "Transport":
                String transport = getString(R.string.categoryOfTheWeek) + " " + getString(R.string.transport).toUpperCase();
                title_tv.setText(transport);
                imageview.setImageResource(R.drawable.wheel_transport);
                break;
            case "Food":
                String food = getString(R.string.categoryOfTheWeek) + " " + getString(R.string.food).toUpperCase();
                title_tv.setText(food);
                imageview.setImageResource(R.drawable.wheel_food);
                break;
            case "Energy":
                String energy = getString(R.string.categoryOfTheWeek) + " " + getString(R.string.energy).toUpperCase();
                title_tv.setText(energy);
                imageview.setImageResource(R.drawable.wheel_energy);
                break;
        }
    }
    public void openTasksFragment() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.ic_tasks).performClick();
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        TasksFragment fragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fragment.setArguments(bundle);
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public static void checkFactsFound(ArrayList<Fact> facts) {
        factsList = facts;
        factsListReturned.setBoo(true);
        factsListReturned.getListener().onChange();
    }
}