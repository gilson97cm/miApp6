package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.codemort.minimarket.model.UserVo;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {
    //   Toolbar toolbar;
    TextView btnGoToRegister;
    TextInputEditText txtEmail;
    TextInputEditText txtPass;
    Button btnLogin;

    ProgressDialog progress;
    StringRequest stringRequest;
    Integer id;
    String name,last_name,phone,email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        loadData();

    }

    private void init() {
        btnGoToRegister = (TextView) findViewById(R.id.btnGoToRegister);
        txtEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        txtPass = (TextInputEditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGoToRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnLogin:
                email=txtEmail.getText().toString();
                pass=txtPass.getText().toString();
                validate();
                break;
            case R.id.btnGoToRegister:
                intent = new Intent(Login.this, AdminRegister.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
            finish();
        }
    }

    private void validate(){
      //  String email=txtEmail.getText().toString();
        //String pass=txtPass.getText().toString();

        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        }else{
            login();
        }
    }

    private void login() {

        progress=new ProgressDialog(Login.this);
        progress.setMessage("Cargando...");
        progress.show();

        //  String ip=getString(R.string.ip);
        Util util = new Util();

        String URL = util.getHost()+"wsJSONLogin.php";

        //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

        stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progress.hide();
                UserVo userVo =new UserVo();

                if(!response.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        userVo.setId(jsonObject.optInt("id"));
                        userVo.setName(jsonObject.getString("name_user"));
                        userVo.setLast_name(jsonObject.getString("last_name_user"));
                        userVo.setPhone(jsonObject.getString("phone_user"));
                        userVo.setEmail(jsonObject.getString("email_user"));
                        userVo.setPassword(jsonObject.getString("password_user"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    id =userVo.getId();
                    name = userVo.getName();
                    last_name = userVo.getLast_name();
                    phone = userVo.getPhone();

                    saveData();
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Login.this, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
              //  String email=txtEmail.getText().toString();
               // String pass=txtPass.getText().toString();
                Map<String,String> parametros=new HashMap<>();
                parametros.put("email_user",email);
                parametros.put("password_user",pass);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(Login.this).addToRequestQueue(stringRequest);
    }

    private void saveData(){
        SharedPreferences preferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
       editor.putInt("id",id);
        editor.putString("name", name);
        editor.putString("last_name", last_name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("password", pass);
        editor.putBoolean("session", true);
        editor.commit();
    }

    private void loadData(){
        SharedPreferences preferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
        txtEmail.setText(preferences.getString("email","codemort@email.com"));
        txtPass.setText(preferences.getString("password","123456789"));

    }
}
