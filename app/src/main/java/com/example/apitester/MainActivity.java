package com.example.apitester;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String[] urls;

    List<apiClass> apis = new ArrayList<>();

    RecyclerView recyclerView;

    int counter;

    private Button button;

    boolean checking;

    public int downCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recylerView);

        urls = getResources().getStringArray(R.array.urls);
        setApis();

        viewAdapter viewadapter = new viewAdapter(this, apis);

        counter = 0;
        checking = false;

        recyclerView.setAdapter(viewadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if(checking == false){
                setTestingStatus(viewadapter);
                checking = true;
                downCounter = 0;
                setApiStatus(button, viewadapter);
                checking = false;
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Checking in progress. Please wait.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void setApis(){
        for (int i = 0; i < this.urls.length; i++){
            this.apis.add(new apiClass(String.format("System %d", i+1), urls[i], "NONE"));
        }
    }

    public void setTestingStatus(viewAdapter viewadapter){
        for (int i = 0; i < this.urls.length; i++){
            this.apis.get(i).setStatus("TESTING");
            viewadapter.notifyItemChanged(i);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setApiStatus(Button button, viewAdapter viewadapter){
        button.setText("Testing...");
        for (int i = 0; i < this.urls.length; i++){
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
                        addDownCounter();
                        logCheck(button);
                        return;
                    }
                    apis.get(finalI).setStatus("LIVE");
                    viewadapter.notifyItemChanged(finalI);
                    logCheck(button);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    apis.get(finalI).setStatus("DOWN");
                    viewadapter.notifyItemChanged(finalI);
                    addDownCounter();
                    logCheck(button);
                }
            });
        }
    }

    public void addDownCounter(){
        synchronized (this){
            this.downCounter++;
        }
    }

    public void logCheck(Button button){
        synchronized (this){
            if (this.counter < 9){
                this.counter++;
            }else{
                this.counter = 0;
                String apiStatus;
                if (this.downCounter == 0){
                    apiStatus = "LIVE";
                }else{
                    apiStatus = "DOWN";
                }
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Done :)",
                        Toast.LENGTH_SHORT);
                toast.show();
                button.setText("Test APIs");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> status = new HashMap<>();
                status.put("status", apiStatus);
                status.put("time", strDate);
                status.put("downStatus", this.downCounter);

                db.collection("log")
                        .add(status)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        }
    }

}