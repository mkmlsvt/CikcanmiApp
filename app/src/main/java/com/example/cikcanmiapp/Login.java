package com.example.cikcanmiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnGoToSignUp , btnLogin;
    String str, txtUsername, txtPassword;
    EditText txtUsernameLogin, txtPassowrdLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtPassowrdLogin = findViewById(R.id.txtPasswordLogin);
        txtUsernameLogin = findViewById(R.id.txtUsernameLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.goToSignUp);
        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUsername = txtUsernameLogin.getText().toString();
                txtPassword = txtPassowrdLogin.getText().toString();
                if(txtControl()){
                    login(txtUsername,txtPassword);
                }
            }
        });
    }

    private void login(String username, String password) {
        // url to post our data
        String url = ApiCon.url+"users/logIn";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Login.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty


                // on below line we are displaying a success toast message

                Gson gson = new Gson();
                User user = gson.fromJson(response,User.class);
                if(user._id != null)
                {
                    User.userInGroup.clear();
                    Groups.currentGroup = null;
                    Groups.joinGroupId = null;
                    User.userCurrent = user;
                    Intent intent = new Intent(Login.this,splash_kahve.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this, "giriş başarısız değerlerinizi kontrol edin", Toast.LENGTH_SHORT).show();
                }
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String name = respObj.getString("name");
                    String job = respObj.getString("job");

                    // on below line we are setting this string s to our text view.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Login.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("username", username);
                params.put("password", password);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    private boolean txtControl(){
        if(txtUsername.isEmpty() || txtUsername.trim().isEmpty() || txtPassword.isEmpty() || txtPassword.trim().isEmpty())
        {
            Toast.makeText(this, "Boş alanları doldurun", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
}