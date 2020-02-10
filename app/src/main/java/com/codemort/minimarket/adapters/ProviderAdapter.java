package com.codemort.minimarket.adapters;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.codemort.minimarket.model.ProviderVo;
import com.codemort.minimarket.ui.activities.MyOrders;
import com.codemort.minimarket.ui.activities.Providers;
import com.codemort.minimarket.ui.fragments.provider.ListProviders;

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

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<ProviderVo> listProviders;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

    //START OBJECTS OF DIALOG
    TextView txtDialogIdProv;
    EditText txtDialogEditProvRuc;
    EditText txtDialogEditProvCompany;
    EditText txtDialogEditProvName;
    EditText txtDialogEditProvPhone;
    EditText txtDialogEditProvEmail;
    EditText txtDialogEditProvPass;
    Button btnDialogCancelEdit;
    Button btnDialogUpdate;

    //guardar la posicion del spinner

    //END OBJECTS OF DIALOG

    //enviar email
    String your_email;
    String your_pass;

    //SPINNER
    //String[] data = {"Cifrado", "Descifrado"};
    List<String> listProductString;
    List<ProductVo> listProductObject;
    RequestQueue requestQueue;

    // JsonObjectRequest jsonObjectRequest;
    Util util;


    public ProviderAdapter(Context context, List<ProviderVo> listProviders) {
        this.listProviders = listProviders;
        this.context = context;
    }

    @Override
    public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_provider, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ProviderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProviderViewHolder holder, int position) {
        holder.cardIdProv.setText(listProviders.get(position).getId().toString());
        holder.cardRucProv.setText(listProviders.get(position).getRuc().toString());
        holder.cardNameProv.setText(listProviders.get(position).getName().toString());
        holder.cardCompanyProv.setText(listProviders.get(position).getCompany().toString());
        holder.cardPhoneProv.setText(listProviders.get(position).getPhone().toString());
        holder.cardEmailProv.setText(listProviders.get(position).getEmail().toString());


        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listProviders.size();
    }

    public class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Context context;

        TextView cardIdProv;
        TextView cardRucProv;
        TextView cardCompanyProv;
        TextView cardNameProv;

        TextView cardPhoneProv;
        TextView cardEmailProv;

        Button btnCardUpdateProv;
        Button btnCardDestroyProv;



        public ProviderViewHolder(View itemView) {
            super(itemView);
            cardIdProv = (TextView) itemView.findViewById(R.id.cardIdProv);
            cardRucProv = (TextView) itemView.findViewById(R.id.cardRucProv);
            cardNameProv = (TextView) itemView.findViewById(R.id.cardNameProv);
            cardCompanyProv = (TextView) itemView.findViewById(R.id.cardCompanyProv);
          cardPhoneProv = (TextView) itemView.findViewById(R.id.cardPhoneProv);
            cardEmailProv = (TextView) itemView.findViewById(R.id.cardEmailProv);

            btnCardUpdateProv = (Button) itemView.findViewById(R.id.btnCardUpdateProv);
            btnCardDestroyProv = (Button) itemView.findViewById(R.id.btnCardDestroyProv);


            listProductString = new ArrayList<String>();
            listProductObject = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(context);
            util = new Util();


        }

        void setOnClickListeners() {
            btnCardUpdateProv.setOnClickListener(this);
            btnCardDestroyProv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardUpdateProv:
                    dialogUpdate();
                    // Toast.makeText(context, "Realizar orden", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnCardDestroyProv:
                    //  Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Eliminar Proveedor?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = cardIdProv.getText().toString();
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
            progress.setMessage("Eliminando...");
            progress.show();

            Util util = new Util();

            String URL = util.getHost() + "wsJSONDeleteProvider.php?id=" + id;
            stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("elimina")) {
                        //volver a llamar al fragmento
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

        private void dialogUpdate() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_edit_provider, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogIdProv = (TextView) view.findViewById(R.id.txtDialogIdProv);
            txtDialogEditProvRuc = (EditText) view.findViewById(R.id.txtDialogEditProvRuc);
            txtDialogEditProvCompany = (EditText) view.findViewById(R.id.txtDialogEditProvCompany);
            txtDialogEditProvName = (EditText) view.findViewById(R.id.txtDialogEditProvName);
           txtDialogEditProvPhone = (EditText) view.findViewById(R.id.txtDialogEditProvPhone);
            txtDialogEditProvEmail = (EditText) view.findViewById(R.id.txtDialogEditProvEmail);
            txtDialogEditProvPass = (EditText) view.findViewById(R.id.txtDialogEditProvPass);
            btnDialogCancelEdit = (Button) view.findViewById(R.id.btnDialogCancelEdit);
            btnDialogUpdate = (Button) view.findViewById(R.id.btnDialogUpdate);
           // loadProducts();

            txtDialogIdProv.setText(cardIdProv.getText().toString());
            txtDialogEditProvRuc.setText(cardRucProv.getText().toString());
            txtDialogEditProvCompany.setText(cardCompanyProv.getText().toString());
            txtDialogEditProvName.setText(cardNameProv.getText().toString());
            txtDialogEditProvPhone.setText(cardPhoneProv.getText().toString());
            txtDialogEditProvEmail.setText(cardEmailProv.getText().toString());



            btnDialogUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                    //Toast.makeText(context, "Enviando...", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btnDialogCancelEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"Enviando...",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        //enviar y registrar pedido
        private void validate() {
            String ruc = txtDialogEditProvRuc.getText().toString();
            String company = txtDialogEditProvCompany.getText().toString();
            String name = txtDialogEditProvName.getText().toString();
            String phone = txtDialogEditProvPhone.getText().toString();
            String email = txtDialogEditProvEmail.getText().toString();
            String pass = txtDialogEditProvPass.getText().toString();

            if (ruc.isEmpty() || company.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            } else {
             webServiceUpdate();
            }
        }

        private void webServiceUpdate() {
            progress=new ProgressDialog(context);
            progress.setMessage("Cargando...");
            progress.show();


            String url=util.getHost()+"/wsJSONUpdateProvider.php";

            stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("actualiza")){

                        Toast.makeText(context,"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                    progress.hide();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String idProv = txtDialogIdProv.getText().toString();
                    String ruc = txtDialogEditProvRuc.getText().toString();
                    String name = txtDialogEditProvName.getText().toString();
                    String company = txtDialogEditProvCompany.getText().toString();
                    //   String product = "2"; //txtProductProv.getText().toString();
                    String phone = txtDialogEditProvPhone.getText().toString();
                    String email = txtDialogEditProvEmail.getText().toString();
                    String pass = txtDialogEditProvPass.getText().toString();
                    Map<String, String> parametros = new HashMap<>();

                    //lo de las comillas recibe el post en el api
                    parametros.put("id_prov",idProv);
                    parametros.put("ruc_prov", ruc);
                    parametros.put("company_prov", company);
                    parametros.put("name_prov", name);
                    parametros.put("email_prov", email);
                    parametros.put("pass_prov", pass);
                    parametros.put("phone_prov", phone);

                    return parametros;
                }
            };
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }


      /*  private void cargarWebService() {

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
                        //txtDialogCant.setText("");
                        //  sendMail();
                        // photoPlant.setImageResource(R.drawable.not_photo);
                        Toast.makeText(context, "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();

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

                    // String name_product = txtDialogProductNameProv.getText().toString();
                    //  String cant_product = txtDialogCant.getText().toString();
                    //  String prov_id = txtDialogIdProv.getText().toString();
                    Map<String, String> parametros = new HashMap<>();

                    // parametros.put("name_product", name_product);
                    // parametros.put("cant_product", cant_product);
                    // parametros.put("prov_id", prov_id);

                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }*/


      /*  private void sendMail() {
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
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(txtDialogEmailProv.getText().toString()));
                    message.setContent("<h4><strong>MINIMARKET ELIZABETH</strong><h4> <br>" +
                            "<hr>"+
                            "<strong>Detalle de pedido:</strong><br>" +
                            "<hr>"+
                            "PRODUCTO: <strong>"+txtDialogProductNameProv.getText().toString()+"</strong><br>"+
                            "CANTIDAD: <strong>"+txtDialogCant.getText().toString()+"</strong>", "text/html; charset=utf-8");
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/


    }
}
