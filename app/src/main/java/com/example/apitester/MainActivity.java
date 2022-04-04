package com.example.apitester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    String apiNames[], links[];

    RecyclerView recyclerView;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recylerView);

        apiNames = getResources().getStringArray(R.array.apiNames);
        links = getResources().getStringArray(R.array.links);

        viewAdapter viewAdapter = new viewAdapter(this, apiNames, links);

        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                View x = recyclerView.getLayoutManager().findViewByPosition(0);
            }
        });
    }

    public void apiTest(){

    }

}