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

    BottomNavigationView bottomNavigationView;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "onCreate: started");
        initImageBitmaps();
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.dashboard:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(),About.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: prepering images");

        mImageUrls.add("https://www.sonofabun.ie/index_files/stacks-image-5fc16eb.jpg");
        mNames.add("Son of a Bun");

        mImageUrls.add("https://www.coqbull.com/wp-content/uploads/2020/11/COQBULL-Cork-800x533.jpg");
        mNames.add("CoqBull");

        mImageUrls.add("https://static.wixstatic.com/media/2c519f_9315dedafb7e4df4b41133881c8c2fde~mv2_d_3596_2023_s_2.jpg/v1/fill/w_640,h_480,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/2c519f_9315dedafb7e4df4b41133881c8c2fde~mv2_d_3596_2023_s_2.jpg");
        mNames.add("Scoozi");

        mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/1d/28/fb/ac/the-oliver-plunkett.jpg");
        mNames.add("The Oliver Plunkett");

        mImageUrls.add("https://just-eat-prod-eu-res.cloudinary.com/image/upload/c_fill,f_auto,q_auto,w_1200,h_630,d_ie:cuisines:japanese-1.jpg/v1/ie/restaurants/4241.jpg");
        mNames.add("Wabisabi Sushi and Noodles Bar");

        mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/01/1a/2e/0e/exterior.jpg");
        mNames.add("Cafe Mexicana");

        mImageUrls.add("https://ie.publocation.com/sites/ie.publocation.com/files/styles/large/public/pub-images/the-old-oak-11750.jpg?itok=esSdnIuR&slideshow=true&slideshowAuto=false&slideshowSpeed=4000&speed=350&transition=elastic");
        mNames.add("Old Oak");

        mImageUrls.add("https://corknow.ie/wp-content/uploads/2018/10/bar-2014.png");
        mNames.add("SoHo Bar & Restaurant");

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

