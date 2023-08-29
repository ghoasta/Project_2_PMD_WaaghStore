package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SupportForm extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String db = "w40k";
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/" + db;

    // Database credentials
    static final String USER = "root";
    static final String PASS = "";

    //Buttons etc
    Button send_review;
    EditText review_content;
    String content,rating,totalStars;
    float rating_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_form);

        //assigning
        this.send_review = (Button) findViewById(R.id.button_send_review);
        this.review_content = (EditText) findViewById(R.id.review_textfield);
        final RatingBar simpleRatingBar =(RatingBar) findViewById(R.id.ratingBar);
        //click on send
        send_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = review_content.getText().toString();
                totalStars = "" + simpleRatingBar.getNumStars();
                rating = "" + simpleRatingBar.getRating();
                rating_final = Float.parseFloat(rating);
                Toast.makeText(SupportForm.this,"Review: "+rating_final+"/"+totalStars,Toast.LENGTH_SHORT).show();
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");
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
                        return true;
                    case R.id.admin_page:
                        startActivity(new Intent(getApplicationContext(),AdminScreen.class));
                        overridePendingTransition(0,0);
                        return false;
                }
                return false;
            }
        });
    }

    private class ConnectMySql extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                String result = "Database Connection Successful\n";
                System.out.println(result);
                Statement st = con.createStatement();

                @SuppressLint("DefaultLocale")
                String addString = String.format("INSERT INTO review(`rating`, `content`) " +
                                "VALUES('%s', '%s');",
                        rating_final,
                        content);

                System.out.println(addString);
                st.executeUpdate(addString);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }
    }
}