package com.example.sustainableapp.views;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
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
import com.example.sustainableapp.controllers.EnergyActionController;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.TransportActionController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.SustainableAction;

public class WheelFragment extends Fragment {
    String userID;
    String category;
    TextView wheel_tv, explain_tv;
    ImageView wheel_iv;
    Button wheel_b;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    public WheelFragment() {
    }
    public static WheelFragment newInstance() {
        WheelFragment fragment = new WheelFragment();
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wheel, container, false);
        getUsersSustainableActions();
        Button wheel_b = view.findViewById(R.id.wheel_b);
        wheel_b.setOnClickListener(v -> selectCategory(saList));
        saListReturned = new BooVariable();
        saListReturned.setListener(() -> {
            if (saListReturned.isBoo()) {
                String beginDate = saList.get((saList.size()-1)).getDateBegin();
                String endDate = saList.get((saList.size()-1)).getDateEnd();
                SustainableActionController sac = new SustainableActionController();
                if (sac.isTodayInDates(beginDate, endDate)) {
                    category = saList.get((saList.size()-1)).getCategory();
                    openFactFragment();
                }
                else {
                    setVisibilityWheelItems();
                }
            }
            else {
                setVisibilityWheelItems();
            }
        });
        return view;
    }
    public void setVisibilityWheelItems() {
        wheel_iv.setVisibility(View.VISIBLE);
        wheel_tv.setVisibility(View.VISIBLE);
        wheel_b.setVisibility(View.VISIBLE);
        explain_tv.setVisibility(View.VISIBLE);
    }
    public void setInvisibilityWheelItems() {
        wheel_iv.setVisibility(View.INVISIBLE);
        wheel_tv.setVisibility(View.INVISIBLE);
        wheel_b.setVisibility(View.INVISIBLE);
        explain_tv.setVisibility(View.INVISIBLE);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        wheel_iv = Objects.requireNonNull(getView()).findViewById(R.id.wheel_iv);
        wheel_tv = getView().findViewById(R.id.wheel_tv);
        wheel_b = getView().findViewById(R.id.wheel_b);
        explain_tv = getView().findViewById(R.id.explain_tv);
        setInvisibilityWheelItems();
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "WheelFragment";
        sac.getUsersSustainableActions(userID, purpose);
    }
    public void selectCategory(ArrayList<SustainableAction> categoriesFromDB) {
        ArrayList<String> categories = new ArrayList<>();

        categories.add("Transport");
        categories.add("Food");
        categories.add("Energy");

        int pickedCategory;
        String newCategory = "";
        Random rand = new Random();
        if (categoriesFromDB.size() == 0) {
            pickedCategory = rand.nextInt(3);
            if (pickedCategory == 0) {
                newCategory = categories.get(0);
            }
            else if (pickedCategory == 1) {
                newCategory = categories.get(1);
            }
            else if (pickedCategory == 2) {
                newCategory = categories.get(2);
            }
        }
        else if (categoriesFromDB.size() == 1) {

            if (categoriesFromDB.get(0).getCategory().equals(categories.get(0))) {
                categories.remove(0);
            }
            else if (categoriesFromDB.get(0).getCategory().equals(categories.get(1))) {
                categories.remove(1);
            }
            else if (categoriesFromDB.get(0).getCategory().equals(categories.get(2))) {
                categories.remove(2);
            }

            pickedCategory = rand.nextInt(2);
            if (pickedCategory == 0) {
                newCategory = categories.get(0);
            }
            else if (pickedCategory == 1) {
                newCategory = categories.get(1);
            }
        }
        else if (categoriesFromDB.size() == 2) {
            for (int j = 0; j < categoriesFromDB.size(); j++) {

                if (categoriesFromDB.get(j).getCategory().equals(categories.get(0))) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).equals(categories.get(0))) {
                            categories.remove(i);
                        }
                    }
                } else if (categoriesFromDB.get(j).getCategory().equals(categories.get(1))) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).equals(categories.get(1))) {
                            categories.remove(i);
                        }
                    }
                } else if (categoriesFromDB.get(j).getCategory().equals(categories.get(2))) {
                    for (int i = 0; i < categories.size(); i++) {
                        if (categories.get(i).equals(categories.get(2))) {
                            categories.remove(i);
                        }
                    }
                }
            }
            newCategory = categories.get(0);
        }
        else {
            newCategory = categoriesFromDB.get(categoriesFromDB.size()-3).getCategory();
        }
        SustainableActionController sac = new SustainableActionController();
        SustainableAction sa = sac.addNewCategory(userID, newCategory);
        if (newCategory.equals("Energy")) {
            EnergyActionController eac = new EnergyActionController();
            eac.addEnergyActionsToDB(sa);
        }
        if (newCategory.equals("Transport")) {
            TransportActionController tac = new TransportActionController();
            tac.addTransportActionsToDB(sa);
        }
        if (newCategory.equals("Food")) {
            FoodActionController fac = new FoodActionController();
            fac.addFoodActionsToDB(sa);
        }
        getUsersSustainableActions();
    }
    public static void checkUsersSAFound(ArrayList<SustainableAction> sa) {
        saList = sa;
        saListReturned.setBoo(true);
        saListReturned.getListener().onChange();

    }
    public static void checkUsersSANotFound(ArrayList<SustainableAction> sa) {
        saList = sa;
        saListReturned.setBoo(false);
        saListReturned.getListener().onChange();
    }
    public void openFactFragment() {

        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        FactFragment fragment = new FactFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("category", category);
        fragment.setArguments(bundle);
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}