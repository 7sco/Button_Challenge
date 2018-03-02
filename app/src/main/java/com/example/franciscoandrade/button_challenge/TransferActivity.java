package com.example.franciscoandrade.button_challenge;

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

import com.example.franciscoandrade.button_challenge.restApi.EndPointApi;
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

    TextView historyContainer;
    ArrayList<String> usersList;
    Spinner usersSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<RootObjectUser> myList;
    String id;
    Retrofit retrofit;
    List<String> listTransferNumbers;
    List<RootObjectTransfers> listTransfers;
    RootObjectTransfers rootObjectTransfers;
    Button btnTransfer, btnDelete;
    TextInputEditText amountTransfer;
    HashMap<String, String> listMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        showToolBar("", true);
        historyContainer = (TextView) findViewById(R.id.historyContainer);
        usersSpinner = (Spinner) findViewById(R.id.usersSpinner);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        amountTransfer = (TextInputEditText) findViewById(R.id.amountTransfer);
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        if (null != b) {
            myList = (ArrayList<RootObjectUser>) getIntent().getSerializableExtra("list");
            usersList = new ArrayList<>();
            listMap = new HashMap<>();
            for (RootObjectUser name : myList) {
                if (name.getName() != null) {
                    usersList.add(name.getName());
                    listMap.put(name.getName(), String.valueOf(name.getId()));
                }
            }
        }
        spinnerLoadData();
        retrofitUser();
        getTransfersData(id);
        btnTransfer.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void spinnerLoadData() {
        adapter = new ArrayAdapter(this, R.layout.spinner_item, usersList);
        adapter.notifyDataSetChanged();
        usersSpinner.setAdapter(adapter);
    }

    @SuppressLint("ResourceType")
    private void showToolBar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void retrofitUser() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://fake-button.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getTransfersData(String id) {
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

    public void showHistory(List<String> list, String data) {
        historyContainer.setText(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTransfer:
                transferAmmounts();
                break;
            case R.id.btnDelete:
                deleteUser();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }

    private void deleteUser() {
        EndPointApi service = retrofit.create(EndPointApi.class);
        Call<ResponseBody> response = service.deleteUser(id, "cjm123");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("DELETE", "onResponse: " + response);
                Toast.makeText(TransferActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("FAILDELETE", "onFailure: " + t.getMessage());
            }
        });
    }

    private void transferAmmounts() {
        if (!TextUtils.isEmpty(amountTransfer.getText().toString()) && usersSpinner.getSelectedItem().toString() != null) {
            String idString = listMap.get(usersSpinner.getSelectedItem().toString());
            Integer id1 = Integer.parseInt(idString);
            Toast.makeText(this, "id #: " + id1, Toast.LENGTH_SHORT).show();
            String amount = amountTransfer.getText().toString();
            sendAmounts(amount, id1);
        }
    }

    private void sendAmounts(final String amount, int id) {
        RootObjectSendAmounts rootObjectSendAmounts = new RootObjectSendAmounts("cmj123", amount, id);
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
