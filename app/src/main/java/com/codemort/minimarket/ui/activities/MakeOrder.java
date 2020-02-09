package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.adapters.MakeOrderAdapter;
import com.codemort.minimarket.adapters.ProductAdapter;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.ui.fragments.product.AddProduct;
import com.codemort.minimarket.ui.fragments.product.ListProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MakeOrder extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    Toolbar toolbar;

    String nameHome;
    String last_nameHome;
    String phoneHome;
    String emailHome;

    RecyclerView recyclerMakeORders;
    ArrayList<ProductVo> listProducts;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

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



        final Intent intent = new Intent(MakeOrder.this, Home.class);
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

        listProducts = new ArrayList<>();

        recyclerMakeORders.setLayoutManager(new LinearLayoutManager(this));
        recyclerMakeORders.setHasFixedSize(true);

        cargarWebService();
    }


    private void cargarWebService() {

        progress = new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();

        Util util = new Util();

        String URL = util.getHost() + "wsJSONListProductsWithOutStock.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Sin Datos.", Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        ProductVo product = null;

        JSONArray json = response.optJSONArray("products");

        try {

            for (int i = 0; i < json.length(); i++) {
                product = new ProductVo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                product.setCodProd(jsonObject.optInt("codProd"));
                product.setFechaProd(jsonObject.optString("fechaProd"));
                product.setNombreProd(jsonObject.optString("nombreProd"));
                product.setDetalleProd(jsonObject.optString("detalleProd"));
                product.setPrecioProd(jsonObject.optString("precioProd"));
                product.setStockProd(jsonObject.optInt("stockProd"));

                listProducts.add(product);
            }
            progress.hide();
            MakeOrderAdapter adapter = new MakeOrderAdapter(MakeOrder.this, listProducts);
            recyclerMakeORders.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
            progress.hide();
        }

    }

    private void init() {
        recyclerMakeORders = (RecyclerView) findViewById(R.id.recyclerMakeOrders);
        toolbar = (Toolbar) findViewById(R.id.toolbarMakeOrder);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
