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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.ui.activities.AdminRegister;
import com.codemort.minimarket.ui.activities.Login;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProvider.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProvider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProvider extends Fragment implements View.OnClickListener {
    ProgressDialog progress;
    StringRequest stringRequest;
    //instancias
    EditText txtCodeProv;
    EditText txtNameProv;
    EditText txtLastNameProv;
    EditText txtProductProv;
    EditText txtPhoneProv;
    EditText txtEmailProv;
    Button btnRegisterProv;
    ListProviders fragmentListProviders;

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
        //loadRecyclerExpenses(view);
        return view;
    }

    private void init(View view) {
        txtCodeProv = (EditText) view.findViewById(R.id.txtCodeProv);
        txtNameProv = (EditText) view.findViewById(R.id.txtNameProv);
        txtLastNameProv = (EditText) view.findViewById(R.id.txtLastNameProv);
        txtProductProv = (EditText) view.findViewById(R.id.txtProductProv);
        txtPhoneProv = (EditText) view.findViewById(R.id.txtPhoneProv);
        txtEmailProv = (EditText) view.findViewById(R.id.txtEmailProv);
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
        String code = txtCodeProv.getText().toString();
        String name = txtNameProv.getText().toString();
        String last_name = txtLastNameProv.getText().toString();
        String product = txtProductProv.getText().toString();
        String phone = txtPhoneProv.getText().toString();
        String email = txtEmailProv.getText().toString();
        if (name.isEmpty() || last_name.isEmpty() || phone.isEmpty() || email.isEmpty() || code.isEmpty() || product.isEmpty()) {
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
                    txtCodeProv.setText("");
                    txtNameProv.setText("");
                    txtLastNameProv.setText("");
                    txtProductProv.setText("");
                    txtPhoneProv.setText("");
                    txtEmailProv.setText("");
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

                String code = txtCodeProv.getText().toString();
                String name = txtNameProv.getText().toString();
                String last_name = txtLastNameProv.getText().toString();
                String product = txtProductProv.getText().toString();
                String phone = txtPhoneProv.getText().toString();
                String email = txtEmailProv.getText().toString();
                Map<String, String> parametros = new HashMap<>();

                parametros.put("code_prov", code);
                parametros.put("name_prov", name);
                parametros.put("last_name_prov", last_name);
                parametros.put("product_prov", product);
                parametros.put("phone_prov", phone);
                parametros.put("email_prov", email);

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
