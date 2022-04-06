package com.example.apitester;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String[] urls;

    List<apiClass> apis = new ArrayList<>();

    RecyclerView recyclerView;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recylerView);

        urls = getResources().getStringArray(R.array.urls);
        setApis();

        viewAdapter viewadapter = new viewAdapter(this, apis);

        recyclerView.setAdapter(viewadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> setApiStatus(button, viewadapter));
    }

    @SuppressLint("DefaultLocale")
    public void setApis(){
        for (int i = 0; i < this.urls.length; i++){
            apis.add(new apiClass(String.format("System %d", i+1), urls[i], "NONE"));
        }
    }

    @SuppressLint("SetTextI18n")
    public void setApiStatus(Button button, viewAdapter viewadapter){
        for (int i = 0; i < this.urls.length; i++){
            button.setText("Testing...");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apis.get(i).getUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            jsonPlaceHolderApi apiCall = retrofit.create(jsonPlaceHolderApi.class);

            Call<Void> call = apiCall.callApi(apis.get(i).getUrl());
//            HttpUrlConnection
            int finalI = i;
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        apis.get(finalI).setStatus("DOWN");
                        viewadapter.notifyItemChanged(finalI);
                        button.setText("Done");
                        return;
                    }
                    apis.get(finalI).setStatus("LIVE");
                    viewadapter.notifyItemChanged(finalI);
                    button.setText("Done");
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    apis.get(finalI).setStatus("DOWN");
                    viewadapter.notifyItemChanged(finalI);
                    button.setText("Done");
                }
            });
        }
    }

}