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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.viewHolder> {

    String data1[], data2[];
    Context context;
    private RequestQueue queue;

    public viewAdapter(Context ct, String apiNames[], String links[]){
        context = ct;
        data1 = apiNames;
        data2 = links;
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
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.text3 = holder.myText3;
        text3.setText("NONE");
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        TextView myText3;
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
