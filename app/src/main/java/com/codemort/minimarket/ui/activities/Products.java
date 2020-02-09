package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.codemort.minimarket.ui.fragments.product.AddProduct;
import com.codemort.minimarket.ui.fragments.product.ListProducts;
import com.codemort.minimarket.R;

public class Products extends AppCompatActivity implements AddProduct.OnFragmentInteractionListener, ListProducts.OnFragmentInteractionListener, View.OnClickListener {
    Toolbar toolbar;
    //viene del home
    String nameHome;
    String last_nameHome;
    String phoneHome;
    String emailHome;

    ImageButton btnListProducts;
    ImageButton btnAddProduct;
    View viewLP;
    View viewAP;

    AddProduct fragmentAddProduct;
    ListProducts fragmentListProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        init();
        setSupportActionBar(toolbar);

        //agrega la flecha para regresar en toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameHome = extras.getString("name");
            last_nameHome = extras.getString("last_name");
            phoneHome = extras.getString("phone");
            emailHome = extras.getString("email");
        }

        final Intent intent = new Intent(Products.this, Home.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", nameHome);
                intent.putExtra("last_name", last_nameHome);
                intent.putExtra("phone", phoneHome);
                intent.putExtra("email", emailHome);
                startActivity(intent);
            }
        });

        loadFragment(fragmentListProducts);
        viewAP.setBackgroundColor(Color.WHITE);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentProduct, fragment).commit();
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarProducts);

        fragmentAddProduct = new AddProduct();
        fragmentListProducts = new ListProducts();

        btnListProducts = (ImageButton) findViewById(R.id.btnListProducts);
        btnAddProduct = (ImageButton) findViewById(R.id.btnAddProduct);

        viewLP = (View) findViewById(R.id.viewLProd);
        viewAP = (View) findViewById(R.id.viewAProd);

        btnListProducts.setOnClickListener(this);
        btnAddProduct.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnListProducts:
                loadFragment(fragmentListProducts);
                viewAP.setBackgroundColor(Color.WHITE);
                viewLP.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case R.id.btnAddProduct:
                loadFragment(fragmentAddProduct);
                viewLP.setBackgroundColor(Color.WHITE);
                viewAP.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
    }
}
