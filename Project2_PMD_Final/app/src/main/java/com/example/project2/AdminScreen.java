package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminScreen extends AppCompatActivity implements SensorEventListener {

    BottomNavigationView bottomNavigationView;

    Button btn_history;
    Button btn_reviews;
    Button btn_options;

    private boolean isAvailable;

    private SensorManager mgr;
    private Sensor temp;
    private TextView text;
    private StringBuilder msg = new StringBuilder(2048);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        temp = mgr.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        text = (TextView) findViewById(R.id.tv_temp);

        //text.setText(msg);
        if(mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isAvailable = true;
        }else{
            text.setText("TEmp sensor unavailable");
            isAvailable = false;
        }


        btn_history = (Button) findViewById(R.id.btn_hisotry);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminScreen.this,TransactionHistory.class);
                startActivity(intent);
            }
        });

        btn_reviews = (Button) findViewById(R.id.btn_reviews);
        btn_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminScreen.this,ReviewsHistory.class);
                startActivity(intent);
            }
        });

        btn_options = (Button) findViewById(R.id.btn_options);
        btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminScreen.this,Options.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.layout_support);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.basket_page:
                        startActivity(new Intent(getApplicationContext(),Basket.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.home_page:
                        startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.products_page:
                        startActivity(new Intent(getApplicationContext(),Products.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.support_page:
                        startActivity(new Intent(getApplicationContext(),SupportForm.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.admin_page:
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAvailable){
            mgr.registerListener(this,temp,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        text.setText(sensorEvent.values[0]+" °C");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}