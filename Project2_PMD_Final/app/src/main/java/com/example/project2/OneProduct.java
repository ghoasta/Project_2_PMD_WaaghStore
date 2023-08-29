package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OneProduct extends AppCompatActivity {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String db = "w40k";
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/" + db;

    // Database credentials
    static final String USER = "root";
    static final String PASS = "";

    public int product_id;
    public int quantity = 1;
    public int value ;
    String product_name_basket = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);

        String name = getIntent().getStringExtra("NAME");
        String type = getIntent().getStringExtra("TYPE");
        String price = getIntent().getStringExtra("PRICE");
        String desc = getIntent().getStringExtra("DESC");
        int image = getIntent().getIntExtra("IMAGE",0);
        product_id = getIntent().getIntExtra("POSITION",0);
        value = Integer.parseInt(price);

        TextView product_name = findViewById(R.id.textView_name);
        TextView product_type = findViewById(R.id.textView_type);
        TextView product_price = findViewById(R.id.textView_price);
        TextView product_desc = findViewById(R.id.textView_desc);
        ImageView product_image = findViewById(R.id.imageView_one_product);

        product_name.setText(name);
        product_type.setText(type);
        product_price.setText(price);
        product_desc.setText(desc);
        product_image.setImageResource(image);

        EditText edit_q = findViewById(R.id.quantity_input);

        Button btn_buy;
        btn_buy = findViewById(R.id.button_buy);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OneProduct.this,"Thanks for buying!",Toast.LENGTH_SHORT).show();
                product_name_basket = name;
                String quant = edit_q.getEditableText().toString();
                quantity = Integer.parseInt(quant);
                value = quantity * value;
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");
            }
        });

    }

    public class ConnectMySql extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                String result = "Database Connection Successful\n";
                System.out.println(result);
                Statement st = con.createStatement();

                @SuppressLint("DefaultLocale")
                String addString = String.format("INSERT INTO basket(`basket_id`, `quantity`,`value`,`name`) " +
                                "VALUES('%d', '%d', '%d' ,'%s' );",
                        product_id, quantity,value,product_name_basket );

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