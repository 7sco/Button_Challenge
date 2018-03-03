package com.example.franciscoandrade.button_challenge.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franciscoandrade.button_challenge.R;
import com.example.franciscoandrade.button_challenge.view.UsersViewHolder;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{

    private Context context;
    private List<RootObjectUser> listUsers;

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
        RootObjectUser rootObjectUser= listUsers.get(position);
        holder.bindData(rootObjectUser, context, listUsers);
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

}
