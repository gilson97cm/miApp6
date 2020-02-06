package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class AdminRegister extends AppCompatActivity implements View.OnClickListener {
    TextView btnGoToLogin;

    EditText txtNameAdmin;
    EditText txtLastNameAdmin;
    EditText txtPhoneAdmin;
    EditText txtEmailAdmin;
    EditText txtPassAdmin;
    EditText txtConfirmPass;

    Button btnRegisterUser;
    ProgressDialog progress;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        init();

    }
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnRegisterUser:
                validate();
                break;
            case R.id.btnGoToLogin:
                intent = new Intent(AdminRegister.this, Login.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        btnGoToLogin = (TextView) findViewById(R.id.btnGoToLogin);

        btnRegisterUser = (Button) findViewById(R.id.btnRegisterUser);
        txtNameAdmin = (EditText) findViewById(R.id.txtNameAdmin);
        txtLastNameAdmin = (EditText) findViewById(R.id.txtLastNameAdmin);
        txtPhoneAdmin = (EditText) findViewById(R.id.txtPhoneAdmin);
        txtEmailAdmin = (EditText) findViewById(R.id.txtEmailAdmin);
        txtPassAdmin = (EditText) findViewById(R.id.txtPassAdmin);
        txtConfirmPass = (EditText) findViewById(R.id.txtConfirmPass);

        btnRegisterUser.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);
    }

    private void validate(){
        String name=txtNameAdmin.getText().toString();
        String last_name=txtLastNameAdmin.getText().toString();
        String phone=txtPhoneAdmin.getText().toString();
        String email=txtEmailAdmin.getText().toString();
        String pass=txtPassAdmin.getText().toString();
        String confirmPass=txtConfirmPass.getText().toString();
        if(name.isEmpty() || last_name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() ){
            Toast.makeText(this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        }else{
            if(pass.equals(confirmPass)){
              cargarWebService();
              //  Toast.makeText(this, "Listo para guardar", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
            }
        }
    }

   private void cargarWebService() {

        progress=new ProgressDialog(AdminRegister.this);
        progress.setMessage("Cargando...");
        progress.show();

        //  String ip=getString(R.string.ip);
        Util util = new Util();

        String URL = util.getHost()+"wsJSONRegisterUser.php";

        //  String url=ip+"/ejemploBDRemota/wsJSONRegistroMovil.php?";

        stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("registra")){
                    txtNameAdmin.setText("");
                    txtLastNameAdmin.setText("");
                    txtPhoneAdmin.setText("");
                    txtEmailAdmin.setText("");
                    txtPassAdmin.setText("");
                    txtConfirmPass.setText("");
                   // photoPlant.setImageResource(R.drawable.not_photo);
                    Toast.makeText(AdminRegister.this,"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminRegister.this, Login.class);
                  //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else if(response.trim().equalsIgnoreCase("isRepeat")){
                    Toast.makeText(AdminRegister.this, "El correo ya existe!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(AdminRegister.this,"No se ha registrado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminRegister.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String name=txtNameAdmin.getText().toString();
                String last_name=txtLastNameAdmin.getText().toString();
                String phone=txtPhoneAdmin.getText().toString();
                String email=txtEmailAdmin.getText().toString();
                String pass=txtPassAdmin.getText().toString();
                Map<String,String> parametros=new HashMap<>();

  /*              if((!name.isEmpty()) && (!last_name.isEmpty())
                        && (!phone.isEmpty()) && (!email.isEmpty())
                        && (!pass.isEmpty())){
*/
                    parametros.put("name_user",name);
                    parametros.put("last_name_user",last_name);
                    parametros.put("phone_user",phone);
                    parametros.put("email_user",email);
                    parametros.put("password_user",pass);

               /* }else {
                    Toast.makeText(AdminRegister.this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
                }*/
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(AdminRegister.this).addToRequestQueue(stringRequest);
    }
}
