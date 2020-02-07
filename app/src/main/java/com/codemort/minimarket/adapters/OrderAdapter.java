package com.codemort.minimarket.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.OrderVo;
import com.codemort.minimarket.ui.activities.MyOrders;
import com.codemort.minimarket.ui.activities.Providers;
import android.widget.EditText;

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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ProviderViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<OrderVo> listOrders;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

    EditText txtDialogCant;
    TextView txtDialogTitle;
    TextView txtDialogIdProv;
    TextView txtDialogNameProv;
    TextView txtDialogEmailProv;
    TextView txtDialogProductProv;
    Button btnDialogCancel;
    Button btnDialogSendOrder;


    //enviar email
    String your_email;
    String your_pass;

    public OrderAdapter(Context context, List<OrderVo> listOrders) {
        this.listOrders = listOrders;
        this.context = context;
    }

    @Override
    public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ProviderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProviderViewHolder holder, int position) {
      /*  holder.cardIdProv.setText(listOrders.get(position).getId().toString());
        holder.cardCodeProv.setText(listOrders.get(position).getCode().toString());
        holder.cardNameProv.setText(listOrders.get(position).getName().toString());
        holder.cardLastNameProv.setText(listOrders.get(position).getLast_name().toString());
        holder.cardProductProv.setText(listOrders.get(position).getProduct().toString());
        holder.cardPhoneProv.setText(listOrders.get(position).getPhone().toString());
        holder.cardEmailProv.setText(listOrders.get(position).getEmail().toString());*/
      holder.cardOrderId.setText(listOrders.get(position).getId().toString());
     // holder.cardOrderIdProv.setText(listOrders.get(position).getId_prov().toString());
      holder.cardOrderProv.setText(listOrders.get(position).getName_prov()+" "+listOrders.get(position).getLast_name_prov());
     // holder.cardOrderLastNameProv.setText(listOrders.get(position).getLast_name_prov().toString());
      holder.cardOrderEmail.setText(listOrders.get(position).getEmail_prov().toString());
      holder.cardOrderPhone.setText(listOrders.get(position).getPhone_prov().toString());
      holder.cardOrderProd.setText(listOrders.get(position).getName_product().toString());
      holder.cardOrderCant.setText(listOrders.get(position).getCant_product().toString());
      holder.cardOrderDate.setText(listOrders.get(position).getDate_order().toString());

        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Context context;

        /* TextView cardIdProv;
         TextView cardCodeProv;
         TextView cardNameProv;
         TextView cardLastNameProv;
         TextView cardProductProv;
         TextView cardPhoneProv;
         TextView cardEmailProv;

         Button btnCardMakeOrder;
         Button btnCardDestroyProv;*/
        TextView cardOrderId;
      //  TextView cardOrderIdProv;
        TextView cardOrderProv;
        TextView cardOrderLastNameProv;
        TextView cardOrderEmail;
        TextView cardOrderPhone;
        TextView cardOrderProd;
        TextView cardOrderCant;
        TextView cardOrderDate;
        Button btnCardEditOrder;
        Button btnCardOrderDestroy;

        public ProviderViewHolder(View itemView) {
            super(itemView);
          /*  cardIdProv = (TextView) itemView.findViewById(R.id.cardIdProv);
            cardCodeProv = (TextView) itemView.findViewById(R.id.cardCodeProv);
            cardNameProv = (TextView) itemView.findViewById(R.id.cardNameProv);
            cardLastNameProv = (TextView) itemView.findViewById(R.id.cardLastNameProv);
            cardProductProv = (TextView) itemView.findViewById(R.id.cardProductProv);
            cardPhoneProv = (TextView) itemView.findViewById(R.id.cardPhoneProv);
            cardEmailProv = (TextView) itemView.findViewById(R.id.cardEmailProv);

            btnCardMakeOrder = (Button) itemView.findViewById(R.id.btnCardMakeOrder);
            btnCardDestroyProv = (Button) itemView.findViewById(R.id.btnCardDestroyProv);*/
            cardOrderId = (TextView) itemView.findViewById(R.id.cardOrderId);
          //  cardOrderIdProv = (TextView) itemView.findViewById(R.id.cardOrderIdProv);
            cardOrderProv = (TextView) itemView.findViewById(R.id.cardOrderProv); //nombre del proveedor
            cardOrderLastNameProv = (TextView) itemView.findViewById(R.id.cardOrderLastNameProv); //apellido del proveedor
            cardOrderEmail = (TextView) itemView.findViewById(R.id.cardOrderEmail);
            cardOrderPhone = (TextView) itemView.findViewById(R.id.cardOrderPhone);
            cardOrderProd = (TextView) itemView.findViewById(R.id.cardOrderProd);
            cardOrderCant = (TextView) itemView.findViewById(R.id.cardOrderCant);
            cardOrderDate = (TextView) itemView.findViewById(R.id.cardOrderDate);
            btnCardEditOrder = (Button) itemView.findViewById(R.id.btnCardEditOrder);
            btnCardOrderDestroy = (Button) itemView.findViewById(R.id.btnCardOrderDestroy);


        }

        void setOnClickListeners() {
          /*  btnCardMakeOrder.setOnClickListener(this);
            btnCardDestroyProv.setOnClickListener(this);*/
            btnCardEditOrder.setOnClickListener(this);
            btnCardOrderDestroy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardEditOrder:
                    dialogOrder();
                    // Toast.makeText(context, "Realizar orden", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnCardOrderDestroy:
                    //  Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Cancelar Pedido?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = cardOrderId.getText().toString();
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

        //eliminar proveedor
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
                       // sendMailDelete();
                        Intent intent = new Intent(context, MyOrders.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("name", cardOrderProv.getText().toString());
                        intent.putExtra("last_name", cardOrderLastNameProv.getText().toString());
                        intent.putExtra("phone", cardOrderPhone.getText().toString());
                        intent.putExtra("email", cardOrderEmail.getText().toString());
                        context.startActivity(intent);
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

        //dialogo para el ingreso de la cantidad
        private void dialogOrder() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_insert_cant, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogTitle = (TextView) view.findViewById(R.id.txtDialogTitle);
            txtDialogCant = (EditText) view.findViewById(R.id.txtDialogCant);
            txtDialogIdProv = (TextView) view.findViewById(R.id.txtDialogIdProv);
            txtDialogNameProv = (TextView) view.findViewById(R.id.txtDialogNameProv);
            txtDialogEmailProv = (TextView) view.findViewById(R.id.txtDialogEmailProv);
            txtDialogProductProv = (TextView) view.findViewById(R.id.txtDialogProductProv);
            btnDialogCancel = (Button) view.findViewById(R.id.btnDialogCancel);
            btnDialogSendOrder = (Button) view.findViewById(R.id.btnDialogSendOrder);

            txtDialogTitle.setText("Editar Pedido");
            txtDialogNameProv.setText(cardOrderProv.getText().toString());
            txtDialogEmailProv.setText(cardOrderEmail.getText().toString());
            txtDialogProductProv.setText(cardOrderProd.getText().toString());
            txtDialogIdProv.setText(cardOrderId.getText().toString());


            btnDialogSendOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                    //Toast.makeText(context, "Enviando...", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"Enviando...",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        //enviar y registrar pedido
        private void validate() {
            String name_product = txtDialogProductProv.getText().toString();
            String cant_product = txtDialogCant.getText().toString();
            String prov_id = txtDialogIdProv.getText().toString();
            if (name_product.isEmpty() || cant_product.isEmpty() || prov_id.isEmpty()) {
                Toast.makeText(context, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            } else {
                cargarWebService();
            }
        }

        private void cargarWebService() {

            progress = new ProgressDialog(context);
            progress.setMessage("Enviando...");
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
                        sendMailUpdate();
                        Intent intent = new Intent(context, MyOrders.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("name", cardOrderProv.getText().toString());
                        intent.putExtra("last_name", cardOrderLastNameProv.getText().toString());
                        intent.putExtra("phone", cardOrderPhone.getText().toString());
                        intent.putExtra("email", cardOrderEmail.getText().toString());
                        context.startActivity(intent);
                        // photoPlant.setImageResource(R.drawable.not_photo);
                        Toast.makeText(context, "Se ha enviado actualizacion con exito", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "No se ha actualizado ", Toast.LENGTH_SHORT).show();
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

                    String id = txtDialogIdProv.getText().toString();  //para no crear otro text view en este dialogo se agrega el id de la orden en txtDialogProductProv
                    String cant_product = txtDialogCant.getText().toString();
                    Map<String, String> parametros = new HashMap<>();

                    parametros.put("id", id);
                    parametros.put("cant_product", cant_product);


                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }

        private void sendMailUpdate() {
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
                    message.setSubject("CAMBIO - en el Pedido Minimarket");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(txtDialogEmailProv.getText().toString()));
                    message.setContent("<h4><strong>MINIMARKET ELIZABETH</strong><h4> <br>" +
                            "<hr>" +
                            "<strong>Solicitud de CAMBIO en el pedido:</strong><br>" +
                            "<strong>Detalle de pedido ACTUALIZADO:</strong><br>" +
                            "<hr>" +
                            "PRODUCTO: <strong>" + txtDialogProductProv.getText().toString() + "</strong><br>" +
                            "CANTIDAD: <strong>" + txtDialogCant.getText().toString() + "</strong>", "text/html; charset=utf-8");
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
     /*   private void sendMailDelete(String email) {
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
                    message.setSubject("CANCELAR - Pedido Minimarket");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setContent("<h4><strong>MINIMARKET ELIZABETH</strong><h4> <br>" +
                            "<hr>" +
                            "<strong>Solicitud de CANCELACIÓN en el pedido:</strong><br>" +
                            "<strong>Detalle de pedido cancelado:</strong><br>" +
                            "<hr>" +
                            "PRODUCTO: <strong>" + txtDialogProductProv.getText().toString() + "</strong><br>" +
                            "CANTIDAD: <strong>" + txtDialogCant.getText().toString() + "</strong>", "text/html; charset=utf-8");
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }*/

    }
}

