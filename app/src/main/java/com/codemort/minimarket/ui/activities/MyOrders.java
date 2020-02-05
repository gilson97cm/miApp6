package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codemort.minimarket.R;

public class MyOrders extends AppCompatActivity {
    Toolbar toolbar;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        init();
        setSupportActionBar(toolbar);

        //agrega la flecha para regresar en toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            //loadGreenHouseDetail(idinvernadero);
        }

        final Intent intent = new Intent(MyOrders.this, Home.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarMyOrders);
    }
}
