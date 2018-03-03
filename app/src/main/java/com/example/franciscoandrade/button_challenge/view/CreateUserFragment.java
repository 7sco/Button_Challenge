package com.example.franciscoandrade.button_challenge.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.franciscoandrade.button_challenge.controller.MyCustomObjectListener;
import com.example.franciscoandrade.button_challenge.R;
import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
import com.example.franciscoandrade.button_challenge.restApi.model.Constants;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateUserFragment extends Fragment {
    private View v;
    private Retrofit retrofit;
    private MyCustomObjectListener em;
    private TextInputEditText emailRegister, nameRegister;

    /**
     * Inflate fragment
     * Set button click listener
     * Creates retrofit instance on create
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_user, container, false);
        Button btnfrag = v.findViewById(R.id.btnfrag);
        emailRegister = v.findViewById(R.id.emailRegister);
        nameRegister = v.findViewById(R.id.nameRegister);
        btnfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        retrofitUser();
        return v;
    }


    /**
     * Method checks if EdiText are not empty
     * If ediText text can be take it sends data to registerUser method to take care of registration
     */
    private void createUser() {
        if (TextUtils.isEmpty(nameRegister.getText().toString()) || TextUtils.isEmpty(emailRegister.getText().toString())) {
            if (TextUtils.isEmpty(nameRegister.getText().toString())) {
                nameRegister.setError("Please Enter a valid Name");
                nameRegister.setText("");
            }
            if (TextUtils.isEmpty(emailRegister.getText().toString())) {
                emailRegister.setError("Please Enter a valid Email");
                emailRegister.setText("");
            }
        } else {
            registerUser(nameRegister.getText().toString(), emailRegister.getText().toString());
            nameRegister.setText("");
            emailRegister.setText("");
        }
    }


    /**
     *Add listener so it can work with the Main activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        em = (MyCustomObjectListener) context;
    }


    /**
     * When Fragment gets destroy it calls listener to reload data in the main view
     * Removes Fragment from Stack
     */
    @Override
    public void onDestroyView() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();
        em.reloadView();
        super.onDestroyView();
    }


    /**
     * Retrofit Instance
     */
    private void retrofitUser() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     *Register User network call using @POST
     * Method runs if EdiText are not empty
     * Method accepts name, email to be registered with
     * OnFail  runs when user with same name or password are already in the system
     */
    private void registerUser(String name, String email) {
        RootObject rootObject = new RootObject(name, email, Constants.CANDIDATE_CODE);
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<RootObject> response = service.createUser(rootObject);
        response.enqueue(new Callback<RootObject>() {
            @Override
            public void onResponse(Call<RootObject> call, Response<RootObject> response) {
                if (response.isSuccessful()) {
                    hideSoftKeyboard(getActivity());
                    onDestroyView();
                    Toast.makeText(v.getContext(), "Registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "User Already exist in DB", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {
                Log.d("FAILL==", "onFailure: " + t.getMessage());
                Toast.makeText(v.getContext(), "Cant Register user already exist", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Method Hides Soft keyboard if User has been registered into Button Database
     */
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
