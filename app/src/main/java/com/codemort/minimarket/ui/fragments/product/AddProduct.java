package com.codemort.minimarket.ui.fragments.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProduct.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProduct extends Fragment implements View.OnClickListener {

    EditText txtNameProd;
    EditText txtDetailProd;
    EditText txtPriceProd;
    EditText txtStockProd;
    Button btnRegisterProd;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    Util util;
    StringRequest stringRequest;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProduct newInstance(String param1, String param2) {
        AddProduct fragment = new AddProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        init(view);

        requestQueue = Volley.newRequestQueue(getContext());
        util = new Util();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegisterProd:
                validate();
                break;
        }
    }

    private void validate() {
        String name = txtNameProd.getText().toString();
        String detail = txtDetailProd.getText().toString();
        String price = txtPriceProd.getText().toString();
        String stock = txtStockProd.getText().toString();
        if (name.isEmpty() || detail.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            Toast.makeText(getContext(), "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        } else {
            cargarWebService();
        }
    }

    private void cargarWebService() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //  String ip=getString(R.string.ip);
        Util util = new Util();

        String URL = util.getHost() + "wsJSONRegisterProducts.php";

        //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("registra")) {
                    txtNameProd.setText("");
                    txtDetailProd.setText("");
                    txtPriceProd.setText("");
                    txtStockProd.setText("");
                    // photoPlant.setImageResource(R.drawable.not_photo);
                    Toast.makeText(getContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();

                } else if (response.trim().equalsIgnoreCase("isRepeat")) {
                    Toast.makeText(getContext(), "El producto ya existe!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se ha registrado ", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ", "" + response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String name = txtNameProd.getText().toString();
                String detail = txtDetailProd.getText().toString();
                String price = txtPriceProd.getText().toString();
                String stock = txtStockProd.getText().toString();
                Map<String, String> parametros = new HashMap<>();

                //lo de las comillas recibe el post en el api
                parametros.put("name_product", name);
                parametros.put("detail_product", detail);
                parametros.put("price_product", price);
                parametros.put("stock_product", stock);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    private void init(View v){
        txtNameProd = (EditText) v.findViewById(R.id.txtNameProd);
        txtDetailProd = (EditText) v.findViewById(R.id.txtDetailProd);
        txtPriceProd = (EditText) v.findViewById(R.id.txtPriceProd);
        txtStockProd = (EditText) v.findViewById(R.id.txtStockProd);

        btnRegisterProd = (Button) v.findViewById(R.id.btnRegisterProd);
        btnRegisterProd.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
