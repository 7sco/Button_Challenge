package com.example.franciscoandrade.button_challenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
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
    private RecyclerView recyclerContainer;
    private UsersAdapter usersAdapter;
    private List<RootObjectUser> listUsers;
    private RootObjectUser rootObjectUser;
    private Button mainBtn;
    private LinearLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerContainer = (RecyclerView) findViewById(R.id.recyclerContainer);
        mainBtn = (Button) findViewById(R.id.mainBtn);
        fragmentContainer = (LinearLayout) findViewById(R.id.fragmentContainer);
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

    @Override
    protected void onResume() {
        super.onResume();
        ///fragmentContainer.setVisibility(View.GONE);
        Log.d("RESUME", "onResume: ");
        onRestart();
    }

    @Override
    protected void onRestart() {
        fragmentContainer.setVisibility(View.GONE);
        Log.d("RESTART", "onRestart: ");
        usersAdapter.notifyDataSetChanged();
        super.onRestart();
    }

    private void retrofitUser() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://fake-button.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getListUsers() {
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
                    //Log.d("RESPONSE", "onResponse: "+response);
                    // Log.d("RESPONSE", "onResponse: "+response.body());
                    // Log.d("RESPONSE", "onResponse: "+response.body().get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<RootObjectUser>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        fragmentContainer.setVisibility(View.VISIBLE);
        android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.replace(R.id.fragmentContainer, new CreateUserFragment());
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    @Override
    public void reloadView() {
        getListUsers();
        usersAdapter.notifyDataSetChanged();
        onRestart();
    }
}
