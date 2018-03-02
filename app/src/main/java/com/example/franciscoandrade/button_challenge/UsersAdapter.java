package com.example.franciscoandrade.button_challenge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    List<RootObjectUser> listUsers;

    public UsersAdapter(Context context) {
        this.context = context;
        listUsers = new ArrayList<>();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        String name = listUsers.get(position).getName();
        String email = listUsers.get(position).getEmail();
        final String id = String.valueOf(listUsers.get(position).getId());
        holder.nameTV.setText(name);
        holder.emailTV.setText(email);
        holder.idTV.setText("ID: " + id);
        holder.transferTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntentData(id);
            }
        });
    }


    public void getIntentData(String id) {
        Intent view = new Intent(context, TransferActivity.class);
        view.putExtra("list", (Serializable) listUsers);
        view.putExtra("id", id);
        context.startActivity(view);
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void addUsers(List<RootObjectUser> rootObject) {
        listUsers.clear();
        listUsers.addAll(rootObject);
        notifyDataSetChanged();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, emailTV, idTV, transferTV;

        public UsersViewHolder(View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            emailTV = (TextView) itemView.findViewById(R.id.emailTV);
            idTV = (TextView) itemView.findViewById(R.id.idTV);
            transferTV = (TextView) itemView.findViewById(R.id.transferTV);
        }
    }
}
