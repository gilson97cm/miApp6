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
    Spinner spinnerProductEdit;
    EditText txtDialogEditProvPhone;
    EditText txtDialogEditProvEmail;
    EditText txtDialogEditProvPass;
    Button btnDialogCancelEdit;
    Button btnDialogUpdate;
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
        holder.cardProductNameProv.setText(listProviders.get(position).getProduct().toString());
        holder.cardPhoneProv.setText(listProviders.get(position).getPhone().toString());
        holder.cardEmailProv.setText(listProviders.get(position).getEmail().toString());

        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listProviders.size();
    }

    public class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
        // Context context;

        TextView cardIdProv;
        TextView cardRucProv;
        TextView cardCompanyProv;
        TextView cardNameProv;
        TextView cardProductNameProv;
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
            cardProductNameProv = (TextView) itemView.findViewById(R.id.cardProductNameProv);
            cardPhoneProv = (TextView) itemView.findViewById(R.id.cardPhoneProv);
            cardEmailProv = (TextView) itemView.findViewById(R.id.cardEmailProv);

            btnCardUpdateProv = (Button) itemView.findViewById(R.id.btnCardUpdateProv);
            btnCardDestroyProv = (Button) itemView.findViewById(R.id.btnCardDestroyProv);


            listProductString = new ArrayList<String>();
            listProductObject = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(context);
            util = new Util();
            //  loadProducts();


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
                        Intent intent = new Intent(context, Providers.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("name", cardNameProv.getText().toString());
                        //intent.putExtra("last_name", cardLastNameProv.getText().toString());
                        intent.putExtra("phone", cardPhoneProv.getText().toString());
                        intent.putExtra("email", cardEmailProv.getText().toString());
                        context.startActivity(intent);
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
            spinnerProductEdit = (Spinner) view.findViewById(R.id.spinnerProductEdit);
            txtDialogEditProvPhone = (EditText) view.findViewById(R.id.txtDialogEditProvPhone);
            txtDialogEditProvEmail = (EditText) view.findViewById(R.id.txtDialogEditProvEmail);
            txtDialogEditProvPass = (EditText) view.findViewById(R.id.txtDialogEditProvPass);
            btnDialogCancelEdit = (Button) view.findViewById(R.id.btnDialogCancelEdit);
            btnDialogUpdate = (Button) view.findViewById(R.id.btnDialogUpdate);
            loadProducts();

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

        //dialogo para el ingreso de la cantidad
       /* private void dialogOrder() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_insert_cant, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogCant = (EditText) view.findViewById(R.id.txtDialogCant);
            txtDialogIdProv = (TextView) view.findViewById(R.id.txtDialogIdProv);
            txtDialogNameProv = (TextView) view.findViewById(R.id.txtDialogNameProv);
            txtDialogEmailProv = (TextView) view.findViewById(R.id.txtDialogEmailProv);
            txtDialogProductNameProv = (TextView) view.findViewById(R.id.txtDialogProductProv);
            btnDialogCancel = (Button) view.findViewById(R.id.btnDialogCancel);
            btnDialogSendOrder = (Button) view.findViewById(R.id.btnDialogSendOrder);

            txtDialogNameProv.setText(cardNameProv.getText().toString());
            txtDialogEmailProv.setText(cardEmailProv.getText().toString());
            txtDialogProductNameProv.setText(cardProductNameProv.getText().toString());
            txtDialogIdProv.setText(cardIdProv.getText().toString());


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
        }*/

        //enviar y registrar pedido
      /*  private void validate() {
            String name_product = txtDialogProductNameProv.getText().toString();
            String cant_product = txtDialogCant.getText().toString();
            String prov_id = txtDialogIdProv.getText().toString();
            if (name_product.isEmpty() || prov_id.isEmpty()) {
                Toast.makeText(context, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            } else {
                cargarWebService();
            }
        }*/

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

        //START LOGIC SPINNER

        private void loadProducts() {
            progress = new ProgressDialog(context);
            progress.setMessage("Cargando...");
            progress.show();
            String URL = util.getHost() + "/wsJSONListProducts.php";
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
            //requestQueue.add(jsonObjectRequest);
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(jsonObjectRequest);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, "Sin Datos.", Toast.LENGTH_LONG).show();
            System.out.println();
            Log.d("ERROR: ", error.toString());
            progress.hide();
        }


        @Override
        public void onResponse(JSONObject response) {
            ProductVo productVo = null;
            JSONArray json = response.optJSONArray("products");

            try {
                // assert json != null;
                for (int i = 0; i < json.length(); i++) {
                    productVo = new ProductVo();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    productVo.setCodProd(jsonObject.optInt("codProd"));
                    productVo.setNombreProd(jsonObject.optString("nombreProd"));

                    listProductObject.add(productVo);
                }
                progress.hide();
                //lisMarketString.add("Seleccione..");
                for (int i = 0; i < listProductObject.size(); i++) {
                    listProductString.add("" + listProductObject.get(i).getNombreProd());
                }

                //SPINNER
                ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listProductString);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                spinnerProductEdit.setAdapter(adapter);
                spinnerProductEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
