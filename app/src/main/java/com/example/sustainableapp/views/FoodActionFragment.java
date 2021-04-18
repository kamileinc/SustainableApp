package com.example.sustainableapp.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.sustainableapp.R;
import com.example.sustainableapp.controllers.FoodActionController;
import com.example.sustainableapp.controllers.SustainableActionController;
import com.example.sustainableapp.controllers.UserController;
import com.example.sustainableapp.models.BooVariable;
import com.example.sustainableapp.models.FoodAction;
import com.example.sustainableapp.models.IntVariable;
import com.example.sustainableapp.models.SustainableAction;
import com.example.sustainableapp.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FoodActionFragment extends Fragment {
    String userID;
    private static IntVariable foundProfile;
    static ArrayList<User> profileData;
    private static BooVariable actionEdited;
    RadioButton breakfastVegan_rb, breakfastVegetarian_rb, breakfastLowImpactMeat_rb, breakfastHighImpactMeat_rb,
            lunchVegan_rb, lunchVegetarian_rb, lunchLowImpactMeat_rb, lunchHighImpactMeat_rb,
            dinnerVegan_rb, dinnerVegetarian_rb, dinnerLowImpactMeat_rb, dinnerHighImpactMeat_rb;
    private static IntVariable foundFA;
    static ArrayList<FoodAction> FAData;
    static ArrayList<SustainableAction> saList = new ArrayList<>();
    private static BooVariable saListReturned;
    private static BooVariable badge1Edited;
    private static BooVariable badge0Edited;
    SustainableAction sa = new SustainableAction();
    public FoodActionFragment() {
    }
    public static FoodActionFragment newInstance() {
        FoodActionFragment fragment = new FoodActionFragment();
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
        View view = inflater.inflate(R.layout.fragment_food_action, container, false);
        getUsersSustainableActions();
        FoodActionController fac = new FoodActionController();
        fac.getFAForFAFragment(userID, "FoodActionFragment");
        UserController uc = new UserController();
        uc.getProfile(userID, "foodAction");
        badge1Edited = new BooVariable();
        badge1Edited.setListener(() -> UserActivity.sendNotification(view.getContext(), "3", "Ženklelis", "Valio! Surinkote maksimalų taškų skaičių mitybos srityje pirmą kartą, todėl gaunate ženklelį!", false));
        badge0Edited = new BooVariable();
        badge0Edited.setListener(() -> UserActivity.sendNotification(view.getContext(), "3", "Ženklelis", "Valio! Išsaugojote savo pirmąją užduotį, todėl gaunate ženklelį!", false));
        foundProfile = new IntVariable();
        foundProfile.setListener(() -> {
            if (profileData != null) {
                String breakfastTime = profileData.get(0).getBreakfastTime();
                String lunchTime = profileData.get(0).getLunchTime();
                String dinnerTime = profileData.get(0).getDinnerTime();
                UserController uc1 = new UserController();
                if (uc1.isLater(breakfastTime)) {
                    breakfastVegan_rb.setEnabled(true);
                    breakfastVegetarian_rb.setEnabled(true);
                    breakfastLowImpactMeat_rb.setEnabled(true);
                    breakfastHighImpactMeat_rb.setEnabled(true);
                }
                else {
                    breakfastVegan_rb.setEnabled(false);
                    breakfastVegetarian_rb.setEnabled(false);
                    breakfastLowImpactMeat_rb.setEnabled(false);
                    breakfastHighImpactMeat_rb.setEnabled(false);
                }
                if (uc1.isLater(lunchTime)) {
                    lunchVegan_rb.setEnabled(true);
                    lunchVegetarian_rb.setEnabled(true);
                    lunchLowImpactMeat_rb.setEnabled(true);
                    lunchHighImpactMeat_rb.setEnabled(true);
                }
                else {
                    lunchVegan_rb.setEnabled(false);
                    lunchVegetarian_rb.setEnabled(false);
                    lunchLowImpactMeat_rb.setEnabled(false);
                    lunchHighImpactMeat_rb.setEnabled(false);
                }
                if (uc1.isLater(dinnerTime)) {
                    dinnerVegan_rb.setEnabled(true);
                    dinnerVegetarian_rb.setEnabled(true);
                    dinnerLowImpactMeat_rb.setEnabled(true);
                    dinnerHighImpactMeat_rb.setEnabled(true);
                }
                else {
                    dinnerVegan_rb.setEnabled(false);
                    dinnerVegetarian_rb.setEnabled(false);
                    dinnerLowImpactMeat_rb.setEnabled(false);
                    dinnerHighImpactMeat_rb.setEnabled(false);
                }
            }
        });
        foundFA = new IntVariable();
        foundFA.setListener(() -> {
            if (FAData != null) {
                if (FAData.get(0).getBreakfastFood()==0) {
                    breakfastVegan_rb.setChecked(true);
                }
                else if (FAData.get(0).getBreakfastFood()==1) {
                    breakfastVegetarian_rb.setChecked(true);
                }
                else if (FAData.get(0).getBreakfastFood()==2) {
                    breakfastLowImpactMeat_rb.setChecked(true);
                }
                else if (FAData.get(0).getBreakfastFood()==3) {
                    breakfastHighImpactMeat_rb.setChecked(true);
                }
                if (FAData.get(0).getLunchFood()==0) {
                    lunchVegan_rb.setChecked(true);
                }
                else if (FAData.get(0).getLunchFood()==1) {
                    lunchVegetarian_rb.setChecked(true);
                }
                else if (FAData.get(0).getLunchFood()==2) {
                    lunchLowImpactMeat_rb.setChecked(true);
                }
                else if (FAData.get(0).getLunchFood()==3) {
                    lunchHighImpactMeat_rb.setChecked(true);
                }
                if (FAData.get(0).getDinnerFood()==0) {
                    dinnerVegan_rb.setChecked(true);
                }
                else if (FAData.get(0).getDinnerFood()==1) {
                    dinnerVegetarian_rb.setChecked(true);
                }
                else if (FAData.get(0).getDinnerFood()==2) {
                    dinnerLowImpactMeat_rb.setChecked(true);
                }
                else if (FAData.get(0).getDinnerFood()==3) {
                    dinnerHighImpactMeat_rb.setChecked(true);
                }
            }
        });
        Button saveFA_b = view.findViewById(R.id.saveFA_b);
        saveFA_b.setOnClickListener(v -> {
            FoodActionController fac1 = new FoodActionController();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String dateStr = formatter.format(date);
            int breakfast = -1;
            int lunch = -1;
            int dinner = -1;
            if (breakfastVegan_rb.isChecked()) {
                breakfast = 0;
            }
            if (breakfastVegetarian_rb.isChecked()) {
                breakfast = 1;
            }
            if (breakfastLowImpactMeat_rb.isChecked()) {
                breakfast = 2;
            }
            if (breakfastHighImpactMeat_rb.isChecked()) {
                breakfast = 3;
            }
            if (lunchVegan_rb.isChecked()) {
                lunch = 0;
            }
            if (lunchVegetarian_rb.isChecked()) {
                lunch = 1;
            }
            if (lunchLowImpactMeat_rb.isChecked()) {
                lunch = 2;
            }
            if (lunchHighImpactMeat_rb.isChecked()) {
                lunch = 3;
            }
            if (dinnerVegan_rb.isChecked()) {
                dinner = 0;
            }
            if (dinnerVegetarian_rb.isChecked()) {
                dinner = 1;
            }
            if (dinnerLowImpactMeat_rb.isChecked()) {
                dinner = 2;
            }
            if (dinnerHighImpactMeat_rb.isChecked()) {
                dinner = 3;
            }
            FoodAction fa = new FoodAction(sa.getId(), sa.getCategory(), sa.getUserID(), sa.getDateBegin(), sa.getDateEnd(), dateStr, breakfast, lunch, dinner);
            fac1.updateFoodActionInDB(fa);
            fac1.checkForBadge1(fa, profileData.get(0));
        });
        saListReturned = new BooVariable();
        saListReturned.setListener(() -> {
            if (saListReturned.isBoo()) {
                String beginDate = saList.get((saList.size()-1)).getDateBegin();
                String endDate = saList.get((saList.size()-1)).getDateEnd();
                SustainableActionController sac = new SustainableActionController();
                if (sac.isTodayInDates(beginDate, endDate)) {
                    sa = saList.get((saList.size()-1));
                }
            }
        });
        actionEdited = new BooVariable();
        actionEdited.setListener(() -> {
            Toast.makeText(view.getContext(),"Sėkmingai išsaugoti pakeitimai",Toast. LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).findViewById(R.id.ic_tasks).performClick();
        });
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        breakfastVegan_rb = Objects.requireNonNull(getView()).findViewById(R.id.breakfastVegan_rb);
        breakfastVegetarian_rb = getView().findViewById(R.id.breakfastVegetarian_rb);
        breakfastLowImpactMeat_rb = getView().findViewById(R.id.breakfastLowImpactMeat_rb);
        breakfastHighImpactMeat_rb = getView().findViewById(R.id.breakfastHighImpactMeat_rb);
        lunchVegan_rb = getView().findViewById(R.id.lunchVegan_rb);
        lunchVegetarian_rb = getView().findViewById(R.id.lunchVegetarian_rb);
        lunchLowImpactMeat_rb = getView().findViewById(R.id.lunchLowImpactMeat_rb);
        lunchHighImpactMeat_rb = getView().findViewById(R.id.lunchHighImpactMeat_rb);
        dinnerVegan_rb = getView().findViewById(R.id.dinnerVegan_rb);
        dinnerVegetarian_rb = getView().findViewById(R.id.dinnerVegetarian_rb);
        dinnerLowImpactMeat_rb = getView().findViewById(R.id.dinnerLowImpactMeat_rb);
        dinnerHighImpactMeat_rb = getView().findViewById(R.id.dinnerHighImpactMeat_rb);
    }
    public static void checkFAFound(List<FoodAction> list) {
        FAData = (ArrayList<FoodAction>) list;
        if (!list.isEmpty()) {
            foundFA.setID(list.get(0).getId());
            if (foundFA.getListener() != null) {
                foundFA.getListener().onChange();
            }
        }
    }
    public static void checkUserFound(List<User> list) {
        profileData = (ArrayList<User>) list;
        if (!list.isEmpty()) {
            foundProfile.setID(list.get(0).getId());
            if (foundProfile.getListener() != null) {
                foundProfile.getListener().onChange();
            }
        }
    }
    public void getUsersSustainableActions() {
        SustainableActionController sac = new SustainableActionController();
        String purpose = "FoodActionFragment";
        sac.getUsersSustainableActions(userID, purpose);
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
    public static void checkFAEdited() {
        actionEdited.setBoo(true);
        if (actionEdited.getListener() != null) {
            actionEdited.getListener().onChange();
        }
    }
    public static void checkBadge1Edited() {
        badge1Edited.setBoo(true);
        if (badge1Edited.getListener() != null) {
            badge1Edited.getListener().onChange();
        }
    }
    public static void checkBadge0Edited() {
        badge0Edited.setBoo(true);
        if (badge0Edited.getListener() != null) {
            badge0Edited.getListener().onChange();
        }
    }
}