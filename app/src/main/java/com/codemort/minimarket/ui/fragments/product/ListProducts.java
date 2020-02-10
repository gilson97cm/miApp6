package com.codemort.minimarket.ui.fragments.product;

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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codemort.minimarket.R;
import com.codemort.minimarket.adapters.ProductAdapter;
import com.codemort.minimarket.helpers.Util;
import com.codemort.minimarket.helpers.VolleySingleton;
import com.codemort.minimarket.model.ProductVo;
import com.codemort.minimarket.model.ProductVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListProducts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProducts extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    RecyclerView recyclerProducts;
    ArrayList<ProductVo> listProducts;

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

    public ListProducts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListProducts.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProducts newInstance(String param1, String param2) {
        ListProducts fragment = new ListProducts();
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
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        init(view);


        listProducts=new ArrayList<>();

        // recyclerProducts= (RecyclerView) view.findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerProducts.setHasFixedSize(true);

        // request= Volley.newRequestQueue(getContext());

        //  listRecord = new ArrayList<>();
        // recyclerRecord = (RecyclerView) view.findViewById(R.id.recyclerRecords);
        //recyclerRecord.setLayoutManager(new LinearLayoutManager(getContext()));

       cargarWebService();
        return view;
    }

    private void cargarWebService() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();

        Util util = new Util();

        String URL = util.getHost() + "wsJSONListProductsWithStock.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Sin Datos.", Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        ProductVo product = null;

        JSONArray json = response.optJSONArray("products");

        try {

            for (int i = 0; i < json.length(); i++) {
                product = new ProductVo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                product.setCodProd(jsonObject.optInt("codProd"));
                product.setFechaProd(jsonObject.optString("fechaProd"));
                product.setNombreProd(jsonObject.optString("nombreProd"));
                product.setDetalleProd(jsonObject.optString("detalleProd"));
                product.setPrecioProd(jsonObject.optString("precioProd"));
                product.setStockProd(jsonObject.optInt("stockProd"));
                product.setCompany(jsonObject.optString("empresaprov"));

                listProducts.add(product);
            }
            progress.hide();
            ProductAdapter adapter = new ProductAdapter(getContext(),listProducts);
            recyclerProducts.setAdapter(adapter);
            //adapter.updateList(listProducts);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
            progress.hide();
        }

    }

    private void init(View view) {
        recyclerProducts = (RecyclerView) view.findViewById(R.id.recyclerProducts);
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
