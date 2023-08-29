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

public class TransactionHistory extends AppCompatActivity {


    private ArrayList<ProductListItem_Transaction> productListItem_transactions;
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
        setContentView(R.layout.activity_transaction_history);

        recyclerView = (RecyclerView) findViewById(R.id.RC_transaction);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        productListItem_transactions = new ArrayList<ProductListItem_Transaction>();

        TransactionHistory.SyncData orderData = new TransactionHistory.SyncData();
        orderData.execute("");


    }

    private class SyncData extends AsyncTask<String, String,String> {

        String msg = "starting string";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progress = ProgressDialog.show(TransactionHistory.this,"Sync","Syncing with DB",true);

        }

        @Override
        protected void onPostExecute(String msg) {
            //super.onPostExecute(msg);
            progress.dismiss();
            Toast.makeText(TransactionHistory.this, msg + "",Toast.LENGTH_LONG).show();
            if (success == false){

            }else {
                try {
                    myAppAdapter = new TransactionHistory.MyAppAdapter(productListItem_transactions, TransactionHistory.this);
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
                    String query = "SELECT transaction_id, time, value FROM transaction_history";
                    System.out.println(query);
                    //String query = "SELECT client_id, broker_id, asset_id, timestamp, value, quantity FROM transactions";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs != null){
                        while(rs.next()){
                            try{
                                productListItem_transactions.add(new ProductListItem_Transaction(rs.getString("transaction_id"),rs.getString("time")
                                        ,rs.getString("value")));
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
    public class MyAppAdapter extends RecyclerView.Adapter<TransactionHistory.MyAppAdapter.ViewHolder>{
        private List<ProductListItem_Transaction> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView transaction_id;
            public TextView transaction_time;
            public TextView transaction_value;
            public View layout;

            public ViewHolder(View v){
                super(v);
                layout = v;
                transaction_id = (TextView) v.findViewById(R.id.tv_transaction_id);
                transaction_time = (TextView) v.findViewById(R.id.tv_timestamp);
                transaction_value = (TextView) v.findViewById(R.id.tv_value);
            }
        }


        public MyAppAdapter(List<ProductListItem_Transaction> myDataset, Context context){
            values = myDataset;
            this.context = context;

        }

        @NonNull
        @Override
        public TransactionHistory.MyAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.recycler_view_row_transaction,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionHistory.MyAppAdapter.ViewHolder holder, int position) {
            int zzz;
            final ProductListItem_Transaction productItemList_transaction = values.get(position);
            holder.transaction_id.setText(productItemList_transaction.getTransaction_id());
            holder.transaction_time.setText(productItemList_transaction.getTransaction_time());
            holder.transaction_value.setText(productItemList_transaction.getTransaction_value());

        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}