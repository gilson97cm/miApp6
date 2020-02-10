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
import com.codemort.minimarket.model.OrderVo;
import com.codemort.minimarket.model.OrderVo;
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

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<OrderVo> listOrders;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

    //START OBJECTS OF DIALOG
    TextView txtDialogOrderIdPed;
    TextView txtDialogOrderProd;
    TextView txtDialogOrderCant;
    TextView txtDialogOrderEmail;
    EditText txtDialogCantInput;
    Button btnDialogOrderCancel;
    Button btnDialogOrderSendUp;
    //END OBJECTS OF DIALOG
    //enviar email
    String your_email;
    String your_pass;

 /*   //String[] data = {"Cifrado", "Descifrado"};
    List<String> listProviderString;
    List<ProviderVo> listProvderObject;*/
    RequestQueue requestQueue;

    // JsonObjectRequest jsonObjectRequest;
    Util util;


    public MyOrderAdapter(Context context, List<OrderVo> listOrders) {
        this.listOrders = listOrders;
        this.context = context;
    }

    @Override
    public MyOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new MyOrderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(MyOrderViewHolder holder, int position) {
        holder.cardOrderIdPed.setText(listOrders.get(position).getId_pedido().toString());
        holder.cardOrderCompany.setText(listOrders.get(position).getEmpresaprov().toString());
        holder.cardOrderProv.setText(listOrders.get(position).getNombre_prove().toString());
        holder.cardOrderEmail.setText(listOrders.get(position).getCorreo_prove().toString());
        holder.cardOrderPhone.setText(listOrders.get(position).getTelefono_prove().toString());
        holder.cardOrderProd.setText(listOrders.get(position).getNombreProd());

        holder.cardOrderCant.setText(listOrders.get(position).getCantidad_prod());
        holder.cardOrderDate.setText(listOrders.get(position).getDate_order());


        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Context context;
        TextView cardOrderIdPed;
        TextView cardOrderCompany;
        TextView cardOrderProv;
        TextView cardOrderEmail;
        TextView cardOrderPhone;
        TextView cardOrderProd;
        TextView cardOrderCant;
        TextView cardOrderDate;

        Button btnCardEditOrder;
        Button btnCardOrderDestroy;


        public MyOrderViewHolder(View itemView) {
            super(itemView);
            cardOrderIdPed = (TextView) itemView.findViewById(R.id.cardOrderIdPed);
            cardOrderCompany = (TextView) itemView.findViewById(R.id.cardOrderCompany);
            cardOrderProv = (TextView) itemView.findViewById(R.id.cardOrderProv);
            cardOrderEmail = (TextView) itemView.findViewById(R.id.cardOrderEmail);
            cardOrderPhone = (TextView) itemView.findViewById(R.id.cardOrderPhone);
            cardOrderProd = (TextView) itemView.findViewById(R.id.cardOrderProd);
            cardOrderCant = (TextView) itemView.findViewById(R.id.cardOrderCant);
            cardOrderDate = (TextView) itemView.findViewById(R.id.cardOrderDate);

            btnCardEditOrder = (Button) itemView.findViewById(R.id.btnCardEditOrder);
            btnCardOrderDestroy = (Button) itemView.findViewById(R.id.btnCardOrderDestroy);
            // btnCardUpdateProd = (Button) itemView.findViewById(R.id.btnCardUpdateProd);


           // listProviderString = new ArrayList<String>();
            //listProvderObject = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(context);
            util = new Util();

        }

        void setOnClickListeners() {
            btnCardEditOrder.setOnClickListener(this);
            btnCardOrderDestroy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardEditOrder:
                    dialogOrder();
                    /// Toast.makeText(context, "holaaaa", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context, "Realizar orden", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnCardOrderDestroy:
                    //  Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Cancelar Pedido?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = cardOrderIdPed.getText().toString();
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
            progress.setMessage("Cancelando...");
            progress.show();

            Util util = new Util();

            String URL = util.getHost() + "wsJSONDeleteOrder.php?id=" + id;
            stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("elimina")) {
                        //volver a llamar al fragmento
                        sendData();
                        Toast.makeText(context, "Se ha cancelado con exito", Toast.LENGTH_SHORT).show();
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

        private void dialogOrder() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_edit_order, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogOrderIdPed = (TextView) view.findViewById(R.id.txtDialogOrderIdPed);
            txtDialogOrderProd = (TextView) view.findViewById(R.id.txtDialogOrderProd);
            txtDialogOrderCant = (TextView) view.findViewById(R.id.txtDialogOrderCant);
            txtDialogOrderEmail = (TextView) view.findViewById(R.id.txtDialogOrderEmail);
            txtDialogCantInput = (EditText) view.findViewById(R.id.txtDialogCantInput);

            btnDialogOrderCancel = (Button) view.findViewById(R.id.btnDialogOrderCancel);
            btnDialogOrderSendUp = (Button) view.findViewById(R.id.btnDialogOrderSendUp);
           // loadProviders(cardNameProdMO.getText().toString());

            txtDialogOrderIdPed.setText(cardOrderIdPed.getText().toString());
            txtDialogOrderProd.setText(cardOrderProd.getText().toString());
            txtDialogOrderCant.setText(cardOrderCant.getText().toString());
            txtDialogOrderEmail.setText(cardOrderEmail.getText().toString());

            btnDialogOrderSendUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                    dialog.dismiss();
                }
            });
            btnDialogOrderCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"Enviando...",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        private void validate() {
            String cant = txtDialogCantInput.getText().toString();

            if (cant.isEmpty()) {
                Toast.makeText(context, "Ingrese la cantidad.", Toast.LENGTH_SHORT).show();
            } else {
                webServiceUpdateOrder();
            }
        }


        //enviar y registrar pedido
        private void webServiceUpdateOrder() {

            progress = new ProgressDialog(context);
            progress.setMessage("Actualizando...");
            progress.show();

            //  String ip=getString(R.string.ip);
            Util util = new Util();

            String URL = util.getHost() + "wsJSONUpdateOrders.php";

            //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("actualiza")) {
                        //txtDialogCant.setText("");
                        sendData();
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

                    String id_order = txtDialogOrderIdPed.getText().toString();
                    String cant_product = txtDialogCantInput.getText().toString();
                    //  String prov_id = txtDialogIdProv.getText().toString();
                    Map<String, String> parametros = new HashMap<>();

                    parametros.put("id_order", id_order);
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
                    message.setSubject("CAMBIO - Pedido Minimarket");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(txtDialogOrderEmail.getText().toString()));
                    message.setContent("<h3><strong>CAMBIO EN EL PEDIDO</strong></h3><br><h4><strong>MINIMARKET ELIZABETH</strong><h4> <br>" +
                            "<hr>"+
                            "<strong>Solicitud de cambio.</strong><br>" +
                            "<strong>Detalle de pedido:</strong><br>" +
                            "<hr>"+
                            "PRODUCTO: <strong>"+txtDialogOrderProd.getText().toString()+"</strong><br>"+
                            "CANTIDAD: <strong>"+txtDialogCantInput.getText().toString()+"</strong>", "text/html; charset=utf-8");
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void sendData(){
            SharedPreferences preferences = context.getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
            //   Boolean session = preferences.getBoolean("session",false);
            String name = preferences.getString("name",null);
            String last_name = preferences.getString("last_name",null);
            String phone = preferences.getString("phone",null);
            String email = preferences.getString("email",null);

            Intent intent = new Intent(context, MyOrders.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("name", name);
            intent.putExtra("last_name", last_name);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            context.startActivity(intent);
        }


    }
}
