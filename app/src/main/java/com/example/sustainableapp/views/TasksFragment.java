package com.example.sustainableapp.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.SustainableAction;

import java.util.ArrayList;

public class TasksFragment extends Fragment {
    String userID;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    String category;
    TextView text_tv, title_tv;
    Button chooseCategory_b;
    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
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
            Log.i("mano", "user id: " + userID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        getUsersSustainableActions();
        Button chooseCategory_b = (Button) view.findViewById(R.id.chooseCategory_b);
        chooseCategory_b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openWheelFragment(v);
            }
        });
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
                        openActionFragment(view);
                    }
                    else {
                        Log.i("mano", "DATA neIEINA");
                        setVisibilityForItems();
                    }
                    //kitu atveju leist sukt.
                }
                else {
                    Log.i("mano", "DEJA: " + saList.size());
                    setVisibilityForItems();
                }
            }
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        text_tv = getView().findViewById(R.id.text_tv);
        title_tv = getView().findViewById(R.id.title_tv);
        chooseCategory_b = getView().findViewById(R.id.chooseCategory_b);
        setInvisibilityForItems();
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "TasksFragment";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "radom:" + sa.size());
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();

    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        Log.i("mano", "neradom:" + sa.size());
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();

    }
    public void openActionFragment(View view) {

        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("category", category);
        if (category.equals("Food")) {
            FoodActionFragment fragment = new FoodActionFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment);
        }
        else if (category.equals("Transport")) {
            TransportActionFragment fragment = new TransportActionFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment);
        }
        else if (category.equals("Energy")) {
            EnergyActionFragment fragment = new EnergyActionFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void openWheelFragment(View view) {
        getActivity().findViewById(R.id.ic_wheel).performClick();
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        WheelFragment fragment = new WheelFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void setVisibilityForItems() {
        text_tv.setVisibility(View.VISIBLE);
        title_tv.setVisibility(View.VISIBLE);
        chooseCategory_b.setVisibility(View.VISIBLE);
    }
    public void setInvisibilityForItems() {
        text_tv.setVisibility(View.INVISIBLE);
        title_tv.setVisibility(View.INVISIBLE);
        chooseCategory_b.setVisibility(View.INVISIBLE);
    }
}