package com.codemort.minimarket.ui.fragments.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.adapters.ProviderAdapter;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.ProviderVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListProviders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListProviders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProviders extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    RecyclerView recyclerProviders;
    ArrayList<ProviderVo> listProviders;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListProviders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListProviders.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProviders newInstance(String param1, String param2) {
        ListProviders fragment = new ListProviders();
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

        listProviders=new ArrayList<>();

        recyclerProviders= (RecyclerView) view.findViewById(R.id.recyclerProviders);
        recyclerProviders.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerProviders.setHasFixedSize(true);

        // request= Volley.newRequestQueue(getContext());

        cargarWebService();
        return view;
    }

    private void cargarWebService() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();

        Util util = new Util();

        String URL = util.getHost() + "wsJSONListProviders.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        ProviderVo provider = null;

        JSONArray json = response.optJSONArray("providers");

        try {

            for (int i = 0; i < json.length(); i++) {
                provider = new ProviderVo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                provider.setId(jsonObject.optInt("id"));
                provider.setCode(jsonObject.optString("code_prov"));
                provider.setName(jsonObject.optString("name_prov"));
                provider.setLast_name(jsonObject.optString("last_name_prov"));
                provider.setProduct(jsonObject.optString("product_prov"));
                provider.setPhone(jsonObject.optString("phone_prov"));
                provider.setEmail(jsonObject.optString("email_prov"));

                listProviders.add(provider);
            }
            progress.hide();
            ProviderAdapter adapter = new ProviderAdapter(listProviders);
            recyclerProviders.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
            progress.hide();
        }

    }

    private void init(View view) {
        recyclerProviders = (RecyclerView) view.findViewById(R.id.recyclerProviders);

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
