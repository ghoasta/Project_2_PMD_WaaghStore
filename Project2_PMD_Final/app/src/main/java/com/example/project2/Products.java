package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Products extends AppCompatActivity implements RecyclerViewInterface{

    BottomNavigationView bottomNavigationView;
    ArrayList<ProductsModel> productsModels = new ArrayList<>();
    int[] productsImagesList = {R.drawable.product_1_dc,R.drawable.product_2_nw,R.drawable.product_3_sq,R.drawable.product_4_pt,
            R.drawable.product_5_ai,R.drawable.product_6_g,R.drawable.product_7_br,R.drawable.product_8_fc,
            R.drawable.product_9_ft,R.drawable.product_10_upb,R.drawable.product_11_gk,R.drawable.product_12_mtr,
            R.drawable.product_13_skulls,R.drawable.product_14_gls,R.drawable.product_15_rm,R.drawable.product_16_m};

    String dropdown_selector = "Tool";
    Products_RV_Adapter adapter = new Products_RV_Adapter(this,productsModels, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setProductsModels();


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.layout_products);

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
                        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void setProductsModels(){
        String[] productNames = getResources().getStringArray(R.array.product_name);
        String[] productTypes = getResources().getStringArray(R.array.product_type);
        String[] productPrices = getResources().getStringArray(R.array.product_price);
        String[] productDesc = getResources().getStringArray(R.array.product_desc);

        for (int i=0 ; i< productNames.length; i++){
            productsModels.add(new ProductsModel(productNames[i],productTypes[i],productPrices[i],productsImagesList[i],productDesc[i]));
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(Products.this,OneProduct.class);
        intent.putExtra("NAME",productsModels.get(position).getProductName());
        intent.putExtra("TYPE",productsModels.get(position).getProductType());
        intent.putExtra("PRICE",productsModels.get(position).getProductPrice());
        intent.putExtra("IMAGE",productsModels.get(position).getImage());
        intent.putExtra("DESC",productsModels.get(position).getProductDesc());
        intent.putExtra("POSITION",position+1);

        startActivity(intent);

    }
}