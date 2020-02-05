package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codemort.minimarket.R;
import com.codemort.minimarket.ui.fragments.provider.AddProvider;
import com.codemort.minimarket.ui.fragments.provider.ListProviders;

public class Providers extends AppCompatActivity implements AddProvider.OnFragmentInteractionListener, ListProviders.OnFragmentInteractionListener, View.OnClickListener {
    Toolbar toolbar;
    ImageButton btnListProviders;
    ImageButton btnAddProvider;
    View viewLP;
    View viewAP;

    String name;

    AddProvider fragmentAddProvider;
    ListProviders fragmentListProviders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        init();
        setSupportActionBar(toolbar);

        //agrega la flecha para regresar en toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            //loadGreenHouseDetail(idinvernadero);
        }

        final Intent intent = new Intent(Providers.this, Home.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

       loadFragment(fragmentListProviders);
       viewAP.setBackgroundColor(Color.WHITE);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentProvider, fragment).commit();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProvider);

        fragmentAddProvider = new AddProvider();
        fragmentListProviders = new ListProviders();

        btnListProviders = (ImageButton) findViewById(R.id.btnListProviders);
        btnAddProvider = (ImageButton) findViewById(R.id.btnAddProvider);

        viewLP = (View) findViewById(R.id.viewLP);
        viewAP = (View) findViewById(R.id.viewAP);

        btnListProviders.setOnClickListener(this);
        btnAddProvider.setOnClickListener(this);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnListProviders:
                loadFragment(fragmentListProviders);
                viewAP.setBackgroundColor(Color.WHITE);
                viewLP.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case R.id.btnAddProvider:
               loadFragment(fragmentAddProvider);
                viewLP.setBackgroundColor(Color.WHITE);
                viewAP.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
    }
}
