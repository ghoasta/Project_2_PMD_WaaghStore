package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewsHistory extends AppCompatActivity {

    private ArrayList<ProductItemList_Review> productItemList_reviews;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String db = "w40k";
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/" + db;
    static final String USER = "root";
    static final String PASS = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_history);


        recyclerView = (RecyclerView) findViewById(R.id.RC_reviews);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        productItemList_reviews = new ArrayList<ProductItemList_Review>();

        ReviewsHistory.SyncData orderData = new ReviewsHistory.SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String,String> {

        String msg = "starting string";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progress = ProgressDialog.show(ReviewsHistory.this,"Sync","Syncing with DB",true);

        }

        @Override
        protected void onPostExecute(String msg) {
            //super.onPostExecute(msg);
            progress.dismiss();
            Toast.makeText(ReviewsHistory.this, msg + "",Toast.LENGTH_LONG).show();
            if (success == false){

            }else {
                try {
                    myAppAdapter = new MyAppAdapter(productItemList_reviews, ReviewsHistory.this);
                    recyclerView.setAdapter(myAppAdapter);
                }catch (Exception e){

                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName(JDBC_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                if (con == null){
                    success = false;
                }
                else{
                    String query = "SELECT rating, content FROM review";
                    System.out.println(query);
                    //String query = "SELECT client_id, broker_id, asset_id, timestamp, value, quantity FROM transactions";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs != null){
                        while(rs.next()){
                            try{
                                productItemList_reviews.add(new ProductItemList_Review(rs.getString("rating"),rs.getString("content")));
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        msg = "Data Loaded";
                        success = true;
                    }else{
                        msg = "No data found";
                        success = false;
                    }
                }
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<ReviewsHistory.MyAppAdapter.ViewHolder>{
        private List<ProductItemList_Review> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView review_rating;
            public TextView review_content;
            public View layout;

            public ViewHolder(View v){
                super(v);
                layout = v;
                review_rating = (TextView) v.findViewById(R.id.textView_rating_dislpay);
                review_content = (TextView) v.findViewById(R.id.textView_review_content);
            }
        }


        public MyAppAdapter(List<ProductItemList_Review> myDataset, Context context){
            values = myDataset;
            this.context = context;

        }

        @NonNull
        @Override
        public ReviewsHistory.MyAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.recycler_view_row_reviews,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewsHistory.MyAppAdapter.ViewHolder holder, int position) {
            int zzz;
            final ProductItemList_Review productItemList_review = values.get(position);
            holder.review_rating.setText(productItemList_review.getReview_rating());
            holder.review_content.setText(productItemList_review.getReview_content());

        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}

