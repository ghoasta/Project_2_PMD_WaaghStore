package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mysql.jdbc.PreparedStatement;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Basket<pivate> extends AppCompatActivity implements RecyclerViewInterface {

    BottomNavigationView bottomNavigationView;

    private ArrayList<ProductItemList_Basket> productItemList_baskets;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String db = "w40k";
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/" + db;
    static final String USER = "root";
    static final String PASS = "";

    int total_value;
    TextView textView_totalValue;
    int basket_value_sql;

    Button btn_remove;
    EditText text_remove;
    int id_to_remove;

    Button btn_checkout;

    //paypal things
    public static final String clientKey = "AZvilYwLsi-AMyU43LZj_nfe1QIZVp1jpNXrJTe8yU9Gv9pRoNg1tinBbxDfMU8toY4rzcIadjbMfP7x";
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientKey);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        textView_totalValue = (TextView) findViewById(R.id.textView_total_value);

        //recycler view
        recyclerView = (RecyclerView) findViewById(R.id.basket_rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        productItemList_baskets = new ArrayList<ProductItemList_Basket>();

        SyncData orderData = new SyncData();
        orderData.execute("");

        System.out.println("TOTAL VALUE v2: "+total_value);

        btn_remove = (Button) findViewById(R.id.button_remove_basket_item);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_remove = (EditText) findViewById(R.id.textView_number);
                id_to_remove = Integer.parseInt(text_remove.getText().toString());
                Toast.makeText(Basket.this,"Item removed",Toast.LENGTH_LONG).show();
                ConnectMySql_RemoveItem connectMySql = new ConnectMySql_RemoveItem();
                connectMySql.execute("");
            }
        });

        btn_checkout = (Button) findViewById(R.id.button_checkout);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Basket.this,"TO PAYPAL",Toast.LENGTH_LONG).show();
                ConnectMySql_Complete_Transaction connectMySql = new ConnectMySql_Complete_Transaction();
                connectMySql.execute("");
                getPayment();
            }
        });




        //bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.layout_basket);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.basket_page:
                        return true;
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
                        startActivity(new Intent(getApplicationContext(),AdminScreen.class));
                        overridePendingTransition(0,0);
                        return false;
                }
                return false;
            }
        });

    }

    private void getPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total_value)), "EUR", "Amount to pay",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE){

            if (resultCode == Activity.RESULT_OK){

                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try{
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        //paymentTV.setText("Payment " + state + "\n with payment id is " + payID);
                    } catch (JSONException e){
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(Basket.this,"TESTO",Toast.LENGTH_LONG).show();
    }

    private class SyncData extends AsyncTask<String, String,String> {

        String msg = "starting string";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progress = ProgressDialog.show(Basket.this,"Sync","Syncing with DB",true);

        }

        @Override
        protected void onPostExecute(String msg) {
            //super.onPostExecute(msg);
            progress.dismiss();
            Toast.makeText(Basket.this, msg + "",Toast.LENGTH_LONG).show();
            if (success == false){

            }else {
                try {
                    myAppAdapter = new MyAppAdapter(productItemList_baskets, Basket.this);
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
                    String query = "SELECT name, basket_id, quantity, value FROM basket";
                    System.out.println(query);
                    //String query = "SELECT client_id, broker_id, asset_id, timestamp, value, quantity FROM transactions";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs != null){
                        while(rs.next()){
                            try{
                                productItemList_baskets.add(new ProductItemList_Basket(rs.getString("name"),rs.getString("basket_id"),rs.getString("quantity"),rs.getString("value")));
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

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder>{
        private List<ProductItemList_Basket> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView basket_name;
            public TextView basket_id;
            public TextView  basket_quantity;
            public TextView  basket_value;
            public View layout;

            public ViewHolder(View v){
                super(v);
                layout = v;
                basket_name = (TextView) v.findViewById(R.id.textView_basket_name);
                basket_id = (TextView) v.findViewById(R.id.textView_basketId);
                basket_quantity = (TextView) v.findViewById(R.id.textView_bakset_quantity);
                basket_value = (TextView) v.findViewById(R.id.textView_basket_value);



            }
        }


        public MyAppAdapter(List<ProductItemList_Basket> myDataset, Context context){
            values = myDataset;
            this.context = context;

        }

        @NonNull
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.rececler_view_row_basket,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAppAdapter.ViewHolder holder, int position) {
            int zzz;
            final ProductItemList_Basket productItemList_basket = values.get(position);
            holder.basket_name.setText(productItemList_basket.getBasket_name());
            holder.basket_id.setText(productItemList_basket.getBasket_id());
            holder.basket_quantity.setText(productItemList_basket.getBasket_quantity());
            holder.basket_value.setText(productItemList_basket.getBasket_value());

            String a = productItemList_basket.basket_value;
            zzz= Integer.parseInt(a);
            total_value += zzz;
            System.out.println("TOTAL VALUE: "+total_value);
            basket_value_sql = total_value;
            textView_totalValue.setText(String.valueOf(total_value));
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }

    private class ConnectMySql_RemoveItem extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                String result = "Database Connection Successful\n";
                System.out.println(result);
                Statement st = con.createStatement();

                @SuppressLint("DefaultLocale")
                String addString = String.format("DELETE FROM basket WHERE `basket_id` =" +
                                "'%d';",
                        id_to_remove);

                System.out.println(addString);
                st.executeUpdate(addString);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }
    }

    private class ConnectMySql_Complete_Transaction extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                String result = "Database Connection Successful\n";
                System.out.println(result);
                Statement st = con.createStatement();

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                @SuppressLint("DefaultLocale")
                String addString = String.format("INSERT INTO transaction_history(`time`, `value`) " +
                                "VALUES('%s', '%s');",
                        timestamp,
                        basket_value_sql);

                System.out.println(addString);
                st.executeUpdate(addString);

                Statement st_2 = con.createStatement();

                @SuppressLint("DefaultLocale")
                String addString_2 = String.format("DELETE FROM basket");

                System.out.println(addString_2);
                st_2.executeUpdate(addString_2);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }
    }


}
