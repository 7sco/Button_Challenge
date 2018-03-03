package com.example.franciscoandrade.button_challenge.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.franciscoandrade.button_challenge.R;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;
import java.io.Serializable;
import java.util.List;

/**
 * Created by franciscoandrade on 3/2/18.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder{
    private TextView nameTV, emailTV, idTV, transferTV;
    private Context context;
    private List<RootObjectUser> listUsers;


    public UsersViewHolder(View itemView) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.nameTV);
        emailTV = itemView.findViewById(R.id.emailTV);
        idTV = itemView.findViewById(R.id.idTV);
        transferTV = itemView.findViewById(R.id.transferTV);
    }


    public void bindData(RootObjectUser rootObjectUser, Context context, List<RootObjectUser> listUsers) {
        this.context= context;
        this.listUsers= listUsers;
        String name = rootObjectUser.getName();
        String email = rootObjectUser.getEmail();
        final String id = String.valueOf(rootObjectUser.getId());
        String idShow = "ID: " + id;
        nameTV.setText(name);
        emailTV.setText(email);
        idTV.setText(idShow);
        transferTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntentData(id);
            }
        });
    }


    /**
     * Get Data from user clicked and go to new Activity passing id and List of users
     */
    private void getIntentData(String id) {
        Intent view = new Intent(context, TransferActivity.class);
        view.putExtra("list", (Serializable) listUsers);
        view.putExtra("id", id);
        context.startActivity(view);
    }

}
