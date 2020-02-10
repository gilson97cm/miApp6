package com.codemort.minimarket.adapters;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codemort.minimarket.R;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.model.ProviderVo;
import com.codemort.minimarket.ui.activities.MakeOrder;
import com.codemort.minimarket.ui.activities.MyOrders;
import com.codemort.minimarket.ui.activities.Providers;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MakeOrderAdapter extends RecyclerView.Adapter<MakeOrderAdapter.ProductMOViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<ProductVo> listProducts;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

    //START OBJECTS OF DIALOG
    TextView txtDialogIdProductMO;
    TextView txtDialogProductMO;
    Spinner spinnerProviderMO;
    EditText txtDialogCantMO;
    Button btnDialogCancelMO;
    Button btnDialogSendOrderMO;
    //END OBJECTS OF DIALOG
    //enviar email
    String your_email;
    String your_pass;

    //String[] data = {"Cifrado", "Descifrado"};
    List<String> listProviderString;
    List<ProviderVo> listProvderObject;
    RequestQueue requestQueue;

    // JsonObjectRequest jsonObjectRequest;
    Util util;

    String  emailProvider = "";


    public MakeOrderAdapter(Context context, List<ProductVo> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    @Override
    public ProductMOViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_make_order, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ProductMOViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductMOViewHolder holder, int position) {
        holder.cardIdProdMO.setText(listProducts.get(position).getCodProd().toString());
        holder.cardNameProdMO.setText(listProducts.get(position).getNombreProd().toString());
        holder.cardDetailProdMO.setText(listProducts.get(position).getDetalleProd().toString());
        holder.cardPriceProdMO.setText(listProducts.get(position).getPrecioProd().toString());
        holder.cardStockProdMO.setText(listProducts.get(position).getStockProd().toString());
        holder.cardDateProdMO.setText(listProducts.get(position).getFechaProd());


        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ProductMOViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
        // Context context;
        TextView cardIdProdMO;
        TextView cardNameProdMO;
        TextView cardDetailProdMO;
        TextView cardPriceProdMO;
        TextView cardStockProdMO;
        TextView cardDateProdMO;
        Button btnCardDestroyProdMO;
        Button btnCardMakeOrder;


        public ProductMOViewHolder(View itemView) {
            super(itemView);
            cardIdProdMO = (TextView) itemView.findViewById(R.id.cardIdProdMO);
            cardNameProdMO = (TextView) itemView.findViewById(R.id.cardNameProdMO);
            cardDetailProdMO = (TextView) itemView.findViewById(R.id.cardDetailProdMO);
            cardPriceProdMO = (TextView) itemView.findViewById(R.id.cardPriceProdMO);
            cardStockProdMO = (TextView) itemView.findViewById(R.id.cardStockProdMO);
            cardDateProdMO = (TextView) itemView.findViewById(R.id.cardDateProdMO);

            btnCardMakeOrder = (Button) itemView.findViewById(R.id.btnCardMakeOrder);
            btnCardDestroyProdMO = (Button) itemView.findViewById(R.id.btnCardDestroyProdMO);
           // btnCardUpdateProd = (Button) itemView.findViewById(R.id.btnCardUpdateProd);


            listProviderString = new ArrayList<String>();
            listProvderObject = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(context);
            util = new Util();

        }

        void setOnClickListeners() {
            btnCardMakeOrder.setOnClickListener(this);
            btnCardDestroyProdMO.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardMakeOrder:
                    dialogProvide();
                   /// Toast.makeText(context, "holaaaa", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context, "Realizar orden", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnCardDestroyProdMO:
                    //  Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Eliminar Producto?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = cardIdProdMO.getText().toString();
                            // Toast.makeText(context, "id: "+id, Toast.LENGTH_SHORT).show();
                            webServiceDelete(id);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
            }
        }

        //eliminar producto
        private void webServiceDelete(String id) {
            progress = new ProgressDialog(context);
            progress.setMessage("Eliminando...");
            progress.show();

            Util util = new Util();

            String URL = util.getHost() + "wsJSONDeleteProduct.php?id=" + id;
            stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("elimina")) {
                        //volver a llamar al fragmento
                        senData();
                        Toast.makeText(context, "Se ha eliminado con exito", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.trim().equalsIgnoreCase("noExiste")) {
                            Toast.makeText(context, "No se encuentra el proveedor ", Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ", "" + response);
                        } else {
                            Toast.makeText(context, "No se ha Eliminado ", Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ", "" + response);
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    progress.hide();
                }
            });
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }

        //dialogo para editar

        private void dialogProvide() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_insert_cant, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogIdProductMO = (TextView) view.findViewById(R.id.txtDialogIdProductMO);
            txtDialogProductMO = (TextView) view.findViewById(R.id.txtDialogProductMO);
            spinnerProviderMO = (Spinner) view.findViewById(R.id.spinnerProviderMO);
            txtDialogCantMO = (EditText) view.findViewById(R.id.txtDialogCantMO);
            btnDialogCancelMO = (Button) view.findViewById(R.id.btnDialogCancelMO);
            btnDialogSendOrderMO = (Button) view.findViewById(R.id.btnDialogSendOrderMO);
            loadProviders(cardNameProdMO.getText().toString());
            txtDialogIdProductMO.setText(cardIdProdMO.getText().toString());
            txtDialogProductMO.setText(cardNameProdMO.getText().toString());
            btnDialogSendOrderMO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                    listProviderString.clear();
                    listProvderObject.clear();
                    dialog.dismiss();
                }
            });
            btnDialogCancelMO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listProviderString.clear();
                    listProvderObject.clear();
                    //Toast.makeText(context,"Enviando...",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        private void validate() {
            String cant = txtDialogCantMO.getText().toString();
            if (cant.isEmpty() || emailProvider.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("¡Error!")
                ;            builder.setMessage("No se realizo el pedido. Registre proveedores para este producto.");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
              //  Toast.makeText(context, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            } else {
                webServiceRegisterOrder();
            }
        }
        //START LOGIC SPINNER
        private void loadProviders(String product) {
            progress = new ProgressDialog(context);
            progress.setMessage("Cargando...");
            progress.show();
            String URL = util.getHost() + "/wsJSONListProvidersWithThisProduct.php?product="+product;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
            //requestQueue.add(jsonObjectRequest);
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(jsonObjectRequest);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("¡Alerta!")
;            builder.setMessage("No hay proveedores para este producto.");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(context, "No hay proveedores para este producto.", Toast.LENGTH_LONG).show();
            System.out.println();
            Log.d("ERROR: ", error.toString());
            progress.hide();
        }


        @Override
        public void onResponse(JSONObject response) {
            ProviderVo providerVo = null;
            JSONArray json = response.optJSONArray("providers");

            try {
                // assert json != null;
                for (int i = 0; i < json.length(); i++) {
                    providerVo = new ProviderVo();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    providerVo.setCompany(jsonObject.optString("empresaprov"));
                    providerVo.setEmail(jsonObject.optString("correo_prove"));

                    listProvderObject.add(providerVo);
                }
                progress.hide();
                //lisMarketString.add("Seleccione..");
                for (int i = 0; i < listProvderObject.size(); i++) {
                    listProviderString.add("" + listProvderObject.get(i).getCompany());
                }

                //SPINNER
                ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listProviderString);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                spinnerProviderMO.setAdapter(adapter);

                //cargar spinner con el nombre de producto
                /*String compareValue = cardProductNameProv.getText().toString();
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    spinnerProviderMO.setSelection(spinnerPosition);
                }*/
                //evento al spinner de la ventana de dialogo
                spinnerProviderMO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        emailProvider = listProvderObject.get(position).getEmail().toString();
                         Toast.makeText(context, "Correo:: " + emailProvider, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "No se ha podido establecer conexión con el servidor" +
                        " " + response, Toast.LENGTH_LONG).show();
                progress.hide();
            }
        }

        ///END LOGIC SPINNER

          private void webServiceRegisterOrder() {

            progress = new ProgressDialog(context);
            progress.setMessage("Enviando...");
            progress.show();

            //  String ip=getString(R.string.ip);
            Util util = new Util();

            String URL = util.getHost() + "wsJSONRegisterOrders.php";

            //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("registra")) {
                            senData();
                         sendMail();
                        // photoPlant.setImageResource(R.drawable.not_photo);
                        Toast.makeText(context, "Se ha Enviado con exito", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "No se ha registrado ", Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ", "" + response);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    progress.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                     String id_product = txtDialogIdProductMO.getText().toString();
                      String cant_product = txtDialogCantMO.getText().toString();
                    //  String prov_id = txtDialogIdProv.getText().toString();
                    Map<String, String> parametros = new HashMap<>();

                     parametros.put("id_product", id_product);
                     parametros.put("cant_product", cant_product);
                    // parametros.put("prov_id", prov_id);

                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }


       private void sendMail() {
            your_email = "elizabethminimarket@gmail.com";
            your_pass = "doris_saquinga";
            Session session = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.googlemail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");

            try {
                session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(your_email, your_pass);
                    }
                });

                if (session != null) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(your_email));
                    message.setSubject("Pedido Minimarket");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailProvider));
                    message.setContent("<h4><strong>MINIMARKET ELIZABETH</strong><h4> <br>" +
                            "<hr>"+
                            "<strong>Detalle de pedido:</strong><br>" +
                            "<hr>"+
                            "PRODUCTO: <strong>"+txtDialogProductMO.getText().toString()+"</strong><br>"+
                            "CANTIDAD: <strong>"+txtDialogCantMO.getText().toString()+"</strong>", "text/html; charset=utf-8");
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void senData(){
            SharedPreferences preferences = context.getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
            //   Boolean session = preferences.getBoolean("session",false);
            String name = preferences.getString("name",null);
            String last_name = preferences.getString("last_name",null);
            String phone = preferences.getString("phone",null);
            String email = preferences.getString("email",null);

            Intent intent = new Intent(context, MakeOrder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("name", name);
            intent.putExtra("last_name", last_name);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            context.startActivity(intent);
        }


    }
}
