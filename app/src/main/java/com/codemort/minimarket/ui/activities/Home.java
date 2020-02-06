package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codemort.minimarket.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener {

    TextView lblNameUser;
    TextView lblName;
    TextView lblLastName;
    TextView lblPhone;
    TextView lblEmail;

    Toolbar toolbar;
    //String name;
    CardView cardGoToProducts;
    CardView cardGoToProvider;
    CardView cardGoToMakeOrder;
    CardView cardGoToMyOrders;

    String nameLogin;
    String last_nameLogin;
    String phoneLogin;
    String emailLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setSupportActionBar(toolbar);

        //agrega la flecha para regresar en toolbar
        toolbar.setNavigationIcon(R.drawable.ic_home_black);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameLogin = extras.getString("name");
            last_nameLogin = extras.getString("last_name");
            phoneLogin = extras.getString("phone");
            emailLogin = extras.getString("email");

            lblNameUser.setText(nameLogin+" "+last_nameLogin);
            lblName.setText(nameLogin);
            lblLastName.setText(last_nameLogin);
            lblPhone.setText(phoneLogin);
            lblEmail.setText(emailLogin);
            //loadGreenHouseDetail(idinvernadero);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true; //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.btnLogout:
                msgLogout("", "¿Cerrar Sesión?");

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void msgLogout(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nameLogin = lblName.getText().toString();
                SharedPreferences preferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent i = new Intent(Home.this, Login.class);
                i.putExtra("name", nameLogin);
                i.putExtra("last_name", last_nameLogin);
                i.putExtra("phone", phoneLogin);
                i.putExtra("email", emailLogin);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        Intent i = null;
        nameLogin = lblName.getText().toString();
        switch (v.getId()) {
            case R.id.cardGoToProducts:
                i = new Intent(Home.this, Products.class);
                i.putExtra("name", nameLogin);
                i.putExtra("last_name", last_nameLogin);
                i.putExtra("phone", phoneLogin);
                i.putExtra("email", emailLogin);

                Toast.makeText(this, "Productos.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardGoToProvider:
                i = new Intent(Home.this, Providers.class);
                i.putExtra("name", nameLogin);
                i.putExtra("last_name", last_nameLogin);
                i.putExtra("phone", phoneLogin);
                i.putExtra("email", emailLogin);

                Toast.makeText(this, "Proveedores.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardGoToMakeOrder:
                i = new Intent(Home.this, MakeOrder.class);
                i.putExtra("name", nameLogin);
                i.putExtra("last_name", last_nameLogin);
                i.putExtra("phone", phoneLogin);
                i.putExtra("email", emailLogin);

                Toast.makeText(this, "Realizar Pedido.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardGoToMyOrders:
                i = new Intent(Home.this, MyOrders.class);
                i.putExtra("name", nameLogin);
                i.putExtra("last_name", last_nameLogin);
                i.putExtra("phone", phoneLogin);
                i.putExtra("email", emailLogin);

                Toast.makeText(this, "Pedidos realizados.", Toast.LENGTH_SHORT).show();
                break;
        }

        if(i != null){
            startActivity(i);
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        lblNameUser = (TextView) findViewById(R.id.lblNameUser);
        lblName = (TextView) findViewById(R.id.lblName);
       lblLastName = (TextView) findViewById(R.id.lblLastName);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        lblEmail = (TextView) findViewById(R.id.lblEmail);

        cardGoToProducts = (CardView) findViewById(R.id.cardGoToProducts);
        cardGoToProvider = (CardView) findViewById(R.id.cardGoToProvider);
        cardGoToMakeOrder = (CardView) findViewById(R.id.cardGoToMakeOrder);
        cardGoToMyOrders = (CardView) findViewById(R.id.cardGoToMyOrders);

        setOnClick();
    }

    private void setOnClick() {
        cardGoToProducts.setOnClickListener(this);
        cardGoToProvider.setOnClickListener(this);
        cardGoToMakeOrder.setOnClickListener(this);
        cardGoToMyOrders.setOnClickListener(this);

    }
}
