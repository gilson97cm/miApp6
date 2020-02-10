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
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.model.ProviderVo;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<ProductVo> listProducts;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

    //START OBJECTS OF DIALOG
    TextView txtDialogProdIdProduct;
    EditText txtDialogEditProdName;
    EditText txtDialogEditProdDetail;
    EditText txtDialogEditProdPrice;
    EditText txtDialogEditProdStock;
    Spinner spinnerProviderEdit;
    Button btnDialogProdCancelEdit;
    Button btnDialogProdUpdate;

    //guardar la posicion del spinner

    //END OBJECTS OF DIALOG

    //enviar email
    String your_email;
    String your_pass;

    //SPINNER
    //String[] data = {"Cifrado", "Descifrado"};
    List<String> listProviderString;
    List<ProviderVo> listProviderObject;
    RequestQueue requestQueue;

    // JsonObjectRequest jsonObjectRequest;
    Util util;


    public ProductAdapter(Context context, List<ProductVo> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ProductViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.cardIdProd.setText(listProducts.get(position).getCodProd().toString());
        holder.cardNameProd.setText(listProducts.get(position).getNombreProd().toString());
        holder.cardDetailProd.setText(listProducts.get(position).getDetalleProd().toString());
        holder.cardPriceProd.setText(listProducts.get(position).getPrecioProd().toString());
        holder.cardStockProd.setText(listProducts.get(position).getStockProd().toString());
        holder.cardDateProd.setText(listProducts.get(position).getFechaProd().toString());
        holder.cardCompanyProd.setText(listProducts.get(position).getCompany().toString());


        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public void updateList(ArrayList<ProductVo> dealList_) {
        this.listProducts = new ArrayList<>();
        this.listProducts.addAll(dealList_);
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
        // Context context;
        TextView cardIdProd;
        TextView cardNameProd;
        TextView cardDetailProd;
        TextView cardPriceProd;
        TextView cardStockProd;
        TextView cardDateProd;
       TextView cardCompanyProd;
        Button btnCardUpdateProd;
        Button btnCardDestroyProd;
        String idProv;



        public ProductViewHolder(View itemView) {
            super(itemView);
            cardIdProd = (TextView) itemView.findViewById(R.id.cardIdProd);
            cardNameProd = (TextView) itemView.findViewById(R.id.cardNameProd);
            cardDetailProd = (TextView) itemView.findViewById(R.id.cardDetailProd);
            cardPriceProd = (TextView) itemView.findViewById(R.id.cardPriceProd);
            cardStockProd = (TextView) itemView.findViewById(R.id.cardStockProd);
            cardDateProd = (TextView) itemView.findViewById(R.id.cardDateProd);
            cardCompanyProd = (TextView) itemView.findViewById(R.id.cardCompanyProd);


            btnCardUpdateProd = (Button) itemView.findViewById(R.id.btnCardUpdateProd);
            btnCardDestroyProd = (Button) itemView.findViewById(R.id.btnCardDestroyProd);

            listProviderString = new ArrayList<String>();
            listProviderObject = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(context);
            util = new Util();


        }

        void setOnClickListeners() {
            btnCardUpdateProd.setOnClickListener(this);
            btnCardDestroyProd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardUpdateProd:
                    dialogUpdate();
                    // Toast.makeText(context, "Realizar orden", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnCardDestroyProd:
                    //  Toast.makeText(context, "Eliminar", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Eliminar Producto?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = cardIdProd.getText().toString();
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

            String URL = util.getHost() + "wsJSONDeleteProduct.php?id=" + id;
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
            View view = inflater.inflate(R.layout.dialog_edit_product, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            txtDialogProdIdProduct = (TextView) view.findViewById(R.id.txtDialogProdIdProduct);
            txtDialogEditProdName = (EditText) view.findViewById(R.id.txtDialogEditProdName);
            txtDialogEditProdDetail = (EditText) view.findViewById(R.id.txtDialogEditProdDetail);
            txtDialogEditProdPrice = (EditText) view.findViewById(R.id.txtDialogEditProdPrice);
            txtDialogEditProdStock = (EditText) view.findViewById(R.id.txtDialogEditProdStock);
            spinnerProviderEdit = (Spinner) view.findViewById(R.id.spinnerProviderEdit);

            btnDialogProdCancelEdit = (Button) view.findViewById(R.id.btnDialogProdCancelEdit);
            btnDialogProdUpdate = (Button) view.findViewById(R.id.btnDialogProdUpdate);
            loadProviders();

            txtDialogProdIdProduct.setText(cardIdProd.getText().toString());
            txtDialogEditProdName.setText(cardNameProd.getText().toString());
            txtDialogEditProdDetail.setText(cardDetailProd.getText().toString());
            txtDialogEditProdPrice.setText(cardPriceProd.getText().toString());
            txtDialogEditProdStock.setText(cardStockProd.getText().toString());



            btnDialogProdUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                    listProviderString.clear();
                    listProviderObject.clear();
                    //Toast.makeText(context, "Enviando...", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btnDialogProdCancelEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listProviderString.clear();
                    listProviderObject.clear();
                    //Toast.makeText(context,"Enviando...",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        //enviar y registrar pedido
        private void validate() {
            String name = txtDialogEditProdName.getText().toString();
            String detail = txtDialogEditProdDetail.getText().toString();
            String price = txtDialogEditProdPrice.getText().toString();
            String stock = txtDialogEditProdStock.getText().toString();


            if (name.isEmpty() || detail.isEmpty() || price.isEmpty() || stock.isEmpty()) {
                Toast.makeText(context, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
            } else {
                webServiceUpdate();
            }
        }

        private void webServiceUpdate() {
            progress=new ProgressDialog(context);
            progress.setMessage("Cargando...");
            progress.show();


            String url=util.getHost()+"/wsJSONUpdateProduct.php";

            stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.hide();

                    if (response.trim().equalsIgnoreCase("actualiza")){

                        Toast.makeText(context,"Se ha actualizado con exito",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"No se ha actualizado ",Toast.LENGTH_SHORT).show();
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
                    String idProd = txtDialogProdIdProduct.getText().toString();
                    String name = txtDialogEditProdName.getText().toString();
                    String detail = txtDialogEditProdDetail.getText().toString();
                    String price = txtDialogEditProdPrice.getText().toString();
                    String stock = txtDialogEditProdStock.getText().toString();

                    Map<String, String> parametros = new HashMap<>();

                    //lo de las comillas recibe el post en el api
                    parametros.put("id_product",idProd);
                    parametros.put("name_product", name);
                    parametros.put("detail_product", detail);
                    parametros.put("price_product", price);
                    parametros.put("stock_product", stock);
                    parametros.put("provider_id", idProv);

                    return parametros;
                }
            };
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        }

        //START LOGIC SPINNER

       private void loadProviders() {
            progress = new ProgressDialog(context);
            progress.setMessage("Cargando...");
            progress.show();
            String URL = util.getHost() + "/wsJSONListProviders.php";
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
            ProviderVo providerVo = null;
            JSONArray json = response.optJSONArray("providers");

            try {
                // assert json != null;
                for (int i = 0; i < json.length(); i++) {
                    providerVo = new ProviderVo();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    providerVo.setId(jsonObject.optInt("id_prove"));
                    providerVo.setCompany(jsonObject.optString("empresaprov"));

                    listProviderObject.add(providerVo);
                }
                progress.hide();
                //lisMarketString.add("Seleccione..");
                for (int i = 0; i < listProviderObject.size(); i++) {
                    listProviderString.add("" + listProviderObject.get(i).getCompany());
                }

                //SPINNER
                ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, listProviderString);
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                spinnerProviderEdit.setAdapter(adapter);

                //cargar spinner con el nombre de producto
                String compareValue = cardCompanyProd.getText().toString();
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    spinnerProviderEdit.setSelection(spinnerPosition);
                }
                //evento al spinner de la ventana de dialogo
                spinnerProviderEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        idProv = listProviderObject.get(position).getId().toString();
                        Toast.makeText(context, "id: " + idProv, Toast.LENGTH_SHORT).show();
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
