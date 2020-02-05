package com.codemort.minimarket.adapters;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.ProviderVo;
import com.codemort.minimarket.ui.activities.Providers;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder> {
    Transition transition;
    private static final long DURATION_TRANSITION = 1000;
    List<ProviderVo> listProviders;

    Context context;
    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA

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
        holder.cardCodeProv.setText(listProviders.get(position).getCode().toString());
        holder.cardNameProv.setText(listProviders.get(position).getName().toString());
        holder.cardLastNameProv.setText(listProviders.get(position).getLast_name().toString());
        holder.cardProductProv.setText(listProviders.get(position).getProduct().toString());
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
        TextView cardCodeProv;
        TextView cardNameProv;
        TextView cardLastNameProv;
        TextView cardProductProv;
        TextView cardPhoneProv;
        TextView cardEmailProv;

        Button btnCardMakeOrder;
        Button btnCardDestroyProv;

        public ProviderViewHolder(View itemView) {
            super(itemView);
            cardIdProv = (TextView) itemView.findViewById(R.id.cardIdProv);
            cardCodeProv = (TextView) itemView.findViewById(R.id.cardCodeProv);
            cardNameProv = (TextView) itemView.findViewById(R.id.cardNameProv);
            cardLastNameProv = (TextView) itemView.findViewById(R.id.cardLastNameProv);
            cardProductProv = (TextView) itemView.findViewById(R.id.cardProductProv);
            cardPhoneProv = (TextView) itemView.findViewById(R.id.cardPhoneProv);
            cardEmailProv = (TextView) itemView.findViewById(R.id.cardEmailProv);

            btnCardMakeOrder = (Button) itemView.findViewById(R.id.btnCardMakeOrder);
            btnCardDestroyProv = (Button) itemView.findViewById(R.id.btnCardDestroyProv);


        }

        void setOnClickListeners() {
            btnCardMakeOrder.setOnClickListener(this);
            btnCardDestroyProv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCardMakeOrder:
                    dialogOrder();
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

        //dialogo para el ingreso de la cantidad
        private void dialogOrder() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_insert_cant, null);
            builder.setView(view);
           /*  builder.setView(inflater.inflate(R.layout.dialog_insert_cant,null))
             .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context,"Conectando...",Toast.LENGTH_SHORT).show();
            }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show();
            }
            });*/
            final AlertDialog dialog = builder.create();
            dialog.show();
            TextInputEditText txtDialogCant = (TextInputEditText) view.findViewById(R.id.txtDialogCant);
            TextView txtDialogNameProv = (TextView) view.findViewById(R.id.txtDialogNameProv);
            TextView txtDialogEmailProv = (TextView) view.findViewById(R.id.txtDialogEmailProv);
            TextView txtDialogProductProv = (TextView) view.findViewById(R.id.txtDialogProductProv);
            Button btnDialogCancel = (Button) view.findViewById(R.id.btnDialogCancel);
            Button btnDialogSendOrder = (Button) view.findViewById(R.id.btnDialogSendOrder);

            txtDialogNameProv.setText(cardNameProv.getText().toString()+" "+cardLastNameProv.getText().toString() );
            txtDialogEmailProv.setText(cardEmailProv.getText().toString());
            txtDialogProductProv.setText(cardProductProv.getText().toString());


            btnDialogSendOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Enviando...", Toast.LENGTH_SHORT).show();
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

    }
}
