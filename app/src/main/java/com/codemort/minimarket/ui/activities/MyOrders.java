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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.adapters.MakeOrderAdapter;
import com.codemort.minimarket.adapters.MyOrderAdapter;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.OrderVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity  implements Response.Listener<JSONObject>, Response.ErrorListener {
    Toolbar toolbar;
    //viene del home
    String nameHome;
    String last_nameHome;
    String phoneHome;
    String emailHome;

    RecyclerView recyclerMyOrders;
    ArrayList<OrderVo> listOrders;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;

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
            nameHome = extras.getString("name");
            last_nameHome = extras.getString("last_name");
            phoneHome = extras.getString("phone");
            emailHome = extras.getString("email");
            //loadGreenHouseDetail(idinvernadero);
        }

        final Intent intent = new Intent(MyOrders.this, Home.class);
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

        listOrders=new ArrayList<>();

        recyclerMyOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyOrders.setHasFixedSize(true);

        cargarWebService();
    }
    private void cargarWebService() {

        progress = new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();

        Util util = new Util();

        String URL = util.getHost() + "wsJSONListOrders.php";

       jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
      //  request.add(jsonObjectRequest);
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
        OrderVo order = null;

        JSONArray json = response.optJSONArray("orders");

        try {

            for (int i = 0; i < json.length(); i++) {
                order = new OrderVo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                order.setId_pedido(jsonObject.optInt("id_pedido"));
                order.setDate_order(jsonObject.optString("date_order"));
                order.setEmpresaprov(jsonObject.optString("empresaprov"));
                order.setNombre_prove(jsonObject.optString("nombre_prove"));
                order.setCorreo_prove(jsonObject.optString("correo_prove"));
                order.setTelefono_prove(jsonObject.optString("telefono_prove"));
                order.setCodProd(jsonObject.optInt("codProd"));
                order.setCantidad_prod(jsonObject.optString("cantidad_prod"));
                order.setNombreProd(jsonObject.optString("nombreProd"));

                listOrders.add(order);
            }
            progress.hide();
            MyOrderAdapter adapter = new MyOrderAdapter(MyOrders.this,listOrders);
            recyclerMyOrders.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
            progress.hide();
        }

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMyOrders);
        recyclerMyOrders = (RecyclerView) findViewById(R.id.recyclerMyOrders);

    }

}
