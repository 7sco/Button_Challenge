package com.example.franciscoandrade.button_challenge.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.franciscoandrade.button_challenge.controller.MyCustomObjectListener;
import com.example.franciscoandrade.button_challenge.R;
import com.example.franciscoandrade.button_challenge.controller.UsersAdapter;
import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
import com.example.franciscoandrade.button_challenge.restApi.model.Constants;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyCustomObjectListener {

    private Retrofit retrofit;
    private UsersAdapter usersAdapter;
    private List<RootObjectUser> listUsers;
    private LinearLayout fragmentContainer;

    /*
     * Create Adapter and layout for  RecyclerView
     * Create instance of retrofit
     * method to connect with the network
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerContainer = findViewById(R.id.recyclerContainer);
        Button mainBtn = findViewById(R.id.mainBtn);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        usersAdapter = new UsersAdapter(this);
        recyclerContainer.setAdapter(usersAdapter);
        recyclerContainer.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerContainer.setLayoutManager(linearLayoutManager);
        recyclerContainer.setNestedScrollingEnabled(false);
        fragmentContainer.setVisibility(View.GONE);
        mainBtn.setOnClickListener(this);
        retrofitUser();
        getListUsers();
    }


    /*
     * Method use to reload list on fragment close
     */
    @Override
    protected void onResume() {
        super.onResume();
        onRestart();
    }

    /*
     * Hide fragment container  onRestart view
     */
    @Override
    protected void onRestart() {
        fragmentContainer.setVisibility(View.GONE);
        usersAdapter.notifyDataSetChanged();
        super.onRestart();
    }


    /*
     * Create retrofit instance to use on network petitions
     */
    private void retrofitUser() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /*
     * Clear list so data does not get added instead it cleans and adds new data
     * A dd list response to list
     * Send list to adapter to render in Recyclerview
     */
    private void getListUsers() {
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<List<RootObjectUser>> response = service.getUserList();
        response.enqueue(new Callback<List<RootObjectUser>>() {
            @Override
            public void onResponse(Call<List<RootObjectUser>> call, Response<List<RootObjectUser>> response) {
                if (response.isSuccessful()) {
                    usersAdapter.notifyDataSetChanged();
                    listUsers = new ArrayList<>();
                    listUsers.clear();
                    listUsers.addAll(response.body());
                    usersAdapter.addUsers(listUsers);
                }
            }

            @Override
            public void onFailure(Call<List<RootObjectUser>> call, Throwable t) {

            }
        });
    }


    /*
     * Handle click event on Register Button
     * Inflate fragment
     * Make fragment container visible
     */
    @Override
    public void onClick(View v) {
        fragmentContainer.setVisibility(View.VISIBLE);
        android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.replace(R.id.fragmentContainer, new CreateUserFragment());
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }


    /*
     * Listener allows to re-load list after new user is created
     */
    @Override
    public void reloadView() {
        getListUsers();
        usersAdapter.notifyDataSetChanged();
        onRestart();
    }
}
