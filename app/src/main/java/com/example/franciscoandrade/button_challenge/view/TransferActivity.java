package com.example.franciscoandrade.button_challenge.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.franciscoandrade.button_challenge.R;
import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
import com.example.franciscoandrade.button_challenge.restApi.model.Constants;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectSendAmounts;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectTransfers;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView historyContainer;
    private ArrayList<String> usersList;
    private Spinner usersSpinner;
    private String id;
    private Retrofit retrofit;
    private List<String> listTransferNumbers;
    private TextInputEditText amountTransfer;
    private HashMap<String, String> listMap;


    /**
     * On View created get data passed (id, and Serialized List)
     * Add data to spinner
     * Get Transfer data by doing network call
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        showToolBar("", true);
        historyContainer = findViewById(R.id.historyContainer);
        usersSpinner = findViewById(R.id.usersSpinner);
        Button btnTransfer = findViewById(R.id.btnTransfer);
        Button btnDelete = findViewById(R.id.btnDelete);
        amountTransfer = findViewById(R.id.amountTransfer);
        getIntentData();
        spinnerLoadData();
        retrofitUser();
        getTransfersData(id);
        btnTransfer.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    /**
     * Get Data from Intent Extras
     * Setup List to be use in code
     * Add data to new List and Map that can be accessed through out the code

     */
    private void getIntentData() {
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        if (null != b) {
            ArrayList<RootObjectUser> myList = (ArrayList<RootObjectUser>) getIntent().getSerializableExtra("list");
            usersList = new ArrayList<>();
            listMap = new HashMap<>();
            for (RootObjectUser name : myList) {
                if (name.getName() != null) {
                    usersList.add(name.getName());
                    listMap.put(name.getName(), String.valueOf(name.getId()));
                }
            }
        }
    }


    /**
     * Load data users name passed by MainActivity into Spinner
     */
    private void spinnerLoadData() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.spinner_item, usersList);
        adapter.notifyDataSetChanged();
        usersSpinner.setAdapter(adapter);
    }


    /**
     * Show Custom Toolbar in view
     * Pass Title to be shown in Toolbar
     */
    @SuppressLint("ResourceType")
    private void showToolBar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
    }


    /**
     * Method to add functionality to Toolbar Back button
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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


    /**
     * Network Call to obtain Transfer data by passing user ID
     * Show outcome in Transfer History TextView
     */
    private void getTransfersData(String id) {
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<List<RootObjectTransfers>> response = service.getTransfers(id);
        response.enqueue(new Callback<List<RootObjectTransfers>>() {
            @Override
            public void onResponse(Call<List<RootObjectTransfers>> call, Response<List<RootObjectTransfers>> response) {
                if (response.isSuccessful()) {
                    listTransferNumbers = new ArrayList<>();
                    String data = "";
                    for (RootObjectTransfers num : response.body()) {
                        data += num.getAmount() + "\n";
                        Log.d("amounts", "onResponse: " + num.getAmount());
                        listTransferNumbers.add(num.getAmount());
                    }
                    showHistory(listTransferNumbers, data);
                }
            }

            @Override
            public void onFailure(Call<List<RootObjectTransfers>> call, Throwable t) {

            }
        });
    }


    /**
     * Show Transfer Data obtained to TextView
     */
    private void showHistory(List<String> list, String data) {
        historyContainer.setText(data);
    }


    /**
     * Handle click events to Transfer Money or Delete User
     * On User deletion go back to Main Activity
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTransfer:
                transferAmounts();
                break;
            case R.id.btnDelete:
                deleteUser();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }


    /**
     * Network Call @DELETE to delete user form Database
     */
    private void deleteUser() {
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<ResponseBody> response = service.deleteUser(id, Constants.CANDIDATE_CODE);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d("DELETE", "onResponse: " + response);
                Toast.makeText(TransferActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("FAILDELETE", "onFailure: " + t.getMessage());
            }
        });
    }


    /**
     * Get Amount to be Transfer and User before sending @POST request with amount to be transferred
     */
    private void transferAmounts() {
        if (!TextUtils.isEmpty(amountTransfer.getText().toString()) && usersSpinner.getSelectedItem().toString() != null) {
            String idString = listMap.get(usersSpinner.getSelectedItem().toString());
            Integer id1 = Integer.parseInt(idString);
            Toast.makeText(this, "id #: " + id1, Toast.LENGTH_SHORT).show();
            String amount = amountTransfer.getText().toString();
            sendAmounts(amount, id1);
        }

        else {
            Toast.makeText(this, "Can't Have empty fields", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Method receives amount and ID of User to whom the amount is being send
     * Make Network Call @POST to transfer some amount
     * Notify User amount was send
     */
    private void sendAmounts(final String amount, int id) {
        RootObjectSendAmounts rootObjectSendAmounts = new RootObjectSendAmounts(Constants.CANDIDATE_CODE, amount, id);
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<RootObjectSendAmounts> response = service.sendAmount(rootObjectSendAmounts);
        response.enqueue(new Callback<RootObjectSendAmounts>() {
            @Override
            public void onResponse(Call<RootObjectSendAmounts> call, Response<RootObjectSendAmounts> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TransferActivity.this, "Money Send: " + amount, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RootObjectSendAmounts> call, Throwable t) {
            }
        });
    }
}
