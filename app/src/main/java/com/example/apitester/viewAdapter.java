package com.example.apitester;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.viewHolder> {

    Context context;
    List<apiClass> api;

    public viewAdapter(Context ct, List<apiClass> apis){
        context = ct;
        api = apis;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_row, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAdapter.viewHolder holder, int position) {
        holder.myText1.setText(api.get(position).getName());
        holder.myText2.setText(api.get(position).getUrl());
        holder.myText3.setText(api.get(position).getStatus());
        if (api.get(position).getStatus() == "LIVE"){
            holder.myText3.setTextColor(Color.GREEN);
        }else if (api.get(position).getStatus() == "DOWN"){
            holder.myText3.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return api.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2, myText3;
        ConstraintLayout mainLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.apiNamesTxt);
            myText2 = itemView.findViewById(R.id.linksTxt);
            myText3 = itemView.findViewById(R.id.statusTxt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    public void setStatus(@NonNull viewAdapter.viewHolder holder, String status){
        holder.myText3.setText(status);
    }
}
