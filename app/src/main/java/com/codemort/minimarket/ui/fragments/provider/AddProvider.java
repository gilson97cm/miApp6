package com.codemort.minimarket.ui.fragments.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.ui.activities.AdminRegister;
import com.codemort.minimarket.ui.activities.Login;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProvider.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProvider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProvider extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject>, AdapterView.OnItemSelectedListener {
    ProgressDialog progress;
    StringRequest stringRequest;
    //instancias
    EditText txtRucProv;
    EditText txtNameProv;
    EditText txtCompanyProv;
  //  EditText txtLastNameProv;
   Spinner spinnerProduct; // EditText txtProductProv; //cambiar con spinner
    EditText txtPhoneProv;
    EditText txtEmailProv;
    EditText txtPassProv;

    Button btnRegisterProv;
    ListProviders fragmentListProviders;

    //SPINNER
    //String[] data = {"Cifrado", "Descifrado"};
    List<String> listProductString;
    List<ProductVo> listProductObject;
    String codProd;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    Util util;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddProvider() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProvider.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProvider newInstance(String param1, String param2) {
        AddProvider fragment = new AddProvider();
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
        View view = inflater.inflate(R.layout.fragment_add_provider, container, false);
        init(view);
        listProductString = new ArrayList<String>();
        listProductObject = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        util = new Util();
        loadProducts();

        return view;
    }

    //START LOGIC SPINNER

    private void loadProducts() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        String URL = util.getHost() + "/wsJSONListProducts.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,  null, this, this);
        //requestQueue.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar. " + error.toString(), Toast.LENGTH_SHORT).show();
        System.out.println();
        Log.d("ERROR", error.toString());
        progressDialog.hide();
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
            progressDialog.hide();
            //lisMarketString.add("Seleccione..");
            for (int i = 0; i < listProductObject.size(); i++) {
                listProductString.add("" + listProductObject.get(i).getNombreProd());
            }

            //SPINNER
            ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, listProductString);
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            spinnerProduct.setAdapter(adapter);
            spinnerProduct.setOnItemSelectedListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        codProd = listProductObject.get(position).getCodProd().toString();
        Toast.makeText(getContext(), "ID: "+codProd, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ///END LOGIC SPINNER

    private void init(View view) {
        txtRucProv = (EditText) view.findViewById(R.id.txtRucProv);
        txtCompanyProv = (EditText) view.findViewById(R.id.txtCompanyProv);
        txtNameProv = (EditText) view.findViewById(R.id.txtNameProv);
        spinnerProduct = (Spinner) view.findViewById(R.id.spinnerProduct); //txtProductProv = (EditText) view.findViewById(R.id.txtProductProv); //cambiar por spinner
        txtPhoneProv = (EditText) view.findViewById(R.id.txtPhoneProv);
        txtEmailProv = (EditText) view.findViewById(R.id.txtEmailProv);
        txtPassProv = (EditText) view.findViewById(R.id.txtPassProv);

        btnRegisterProv = (Button) view.findViewById(R.id.btnRegisterProv);

        fragmentListProviders = new ListProviders();

        btnRegisterProv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegisterProv:
                validate();
                break;
        }
    }

    private void validate() {
        String ruc = txtRucProv.getText().toString();
        String name = txtNameProv.getText().toString();
        String company = txtCompanyProv.getText().toString();
        //String product = "2";// txtProductProv.getText().toString();
        String phone = txtPhoneProv.getText().toString();
        String email = txtEmailProv.getText().toString();
        if (name.isEmpty() || company.isEmpty() || phone.isEmpty() || email.isEmpty() || ruc.isEmpty() ) {
            Toast.makeText(getContext(), "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        } else {
            cargarWebService();
        }
    }

    private void cargarWebService() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();

        //  String ip=getString(R.string.ip);
        Util util = new Util();

        String URL = util.getHost() + "wsJSONRegisterProvider.php";

        //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("registra")) {
                    txtRucProv.setText("");
                    txtNameProv.setText("");
                    txtCompanyProv.setText("");
                    spinnerProduct.setSelection(0);
                    txtPhoneProv.setText("");
                    txtEmailProv.setText("");
                    txtPassProv.setText("");
                    // photoPlant.setImageResource(R.drawable.not_photo);
                    Toast.makeText(getContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();

                } else if (response.trim().equalsIgnoreCase("isRepeat")) {
                    Toast.makeText(getContext(), "El correo ya existe!", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equalsIgnoreCase("isRepeatCode")) {
                    Toast.makeText(getContext(), "El código ya existe!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se ha registrado ", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ", "" + response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String ruc = txtRucProv.getText().toString();
                String name = txtNameProv.getText().toString();
                String company = txtCompanyProv.getText().toString();
             //   String product = "2"; //txtProductProv.getText().toString();
                String phone = txtPhoneProv.getText().toString();
                String email = txtEmailProv.getText().toString();
                String pass = txtPassProv.getText().toString();
                Map<String, String> parametros = new HashMap<>();

                //lo de las comillas recibe el post en el api
                parametros.put("ruc_prov", ruc);
                parametros.put("company_prov", company);
                parametros.put("name_prov", name);
                parametros.put("email_prov", email);
                parametros.put("pass_prov", pass);
                parametros.put("phone_prov", phone);

                //enviar id de producto cargado en spinner
                parametros.put("product_id", codProd);


                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
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
