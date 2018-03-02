package com.example.franciscoandrade.button_challenge;


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

import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
import com.example.franciscoandrade.button_challenge.restApi.model.Constants;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends Fragment {
    private View v;
    private Retrofit retrofit;
    private LinearLayout fragmentContainer;
    private Button btnfrag;
    private MyCustomObjectListener em;
    private TextInputEditText emailRegister, nameRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_user, container, false);
        fragmentContainer = v.findViewById(R.id.fragmentContainer);
        btnfrag = v.findViewById(R.id.btnfrag);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        em = (MyCustomObjectListener) context;
    }

    @Override
    public void onDestroyView() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();
        em.reloadView();
        super.onDestroyView();
    }

    private void retrofitUser() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void registerUser(String name, String email) {
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
                    Log.d("EXIST", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<RootObject> call, Throwable t) {
                Log.d("FAILL==", "onFailure: " + t.getMessage());
                Toast.makeText(v.getContext(), "Cant Register user already exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
