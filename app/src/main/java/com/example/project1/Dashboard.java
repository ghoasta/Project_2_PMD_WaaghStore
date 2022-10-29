package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = "Dashboard";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "onCreate: started");

        initImageBitmaps();

    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: prepering images");


        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");

        initRecycleView();
    }

    private void initRecycleView() {
        Log.d(TAG, "initRecycleView: init recycle");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames,mImageUrls,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}

