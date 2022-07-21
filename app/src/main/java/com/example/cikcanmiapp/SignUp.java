package com.example.cikcanmiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText txtSignUpUsername, txtSignUpPassword, txtSignUpEmail;
    Button btnSignUp;
    String strUsername, strPassword, strEmail;
    Handler hndlr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtSignUpEmail = findViewById(R.id.txtSignUpEmail);
        txtSignUpPassword = findViewById(R.id.txtSignUpPassword);
        txtSignUpUsername = findViewById(R.id.txtSignUpUsername);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = txtSignUpEmail.getText().toString();
                strPassword = txtSignUpPassword.getText().toString();
                strUsername = txtSignUpUsername.getText().toString();
                if(txtControl())
                {
                    signUp();
                }
            }
        });
    }
    private boolean txtControl()
    {
        if(strEmail.isEmpty() || strEmail.trim().isEmpty() || strPassword.isEmpty() || strPassword.trim().isEmpty() || strUsername.isEmpty() || strUsername.trim().isEmpty() )
        {
            Toast.makeText(this, "Değerlerinizi kontrol edin.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }

    }
    private void signUp()
    {
        String url = ApiCon.url+"users/signUp";
        RequestQueue queue = Volley.newRequestQueue(SignUp.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                if (user._id == null) {
                    Toast.makeText(SignUp.this, "kayit basarisiz", Toast.LENGTH_SHORT).show();
                }
                else{
                    hndlr = new Handler();

                    Toast.makeText(SignUp.this, "kayit basarılı 3 saniye sonra girise yonlendirilceksiniz", Toast.LENGTH_SHORT).show();
                    hndlr.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SignUp.this,Login.class);
                            startActivity(intent);
                        }
                    },3000);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("username", strUsername);
                params.put("password", strPassword);
                params.put("mail", strEmail);

                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
}