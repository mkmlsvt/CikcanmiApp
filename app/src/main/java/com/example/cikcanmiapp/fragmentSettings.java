package com.example.cikcanmiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class fragmentSettings extends Fragment {
    EditText txtUpdateEmail,txtUpdatePassword,txtUpdateUsername;
    Button btnUpdateUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);
        txtUpdateEmail = (EditText) rootView.findViewById(R.id.txtUpdateEmail);
        txtUpdatePassword = (EditText) rootView.findViewById(R.id.txtUpdatePassword);
        txtUpdateUsername = (EditText) rootView.findViewById(R.id.txtUpdateUsername);
        btnUpdateUser = (Button) rootView.findViewById(R.id.btnUpdate);
        setProptxt();
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtControl())
                {
                    if(txtCountcontrol())
                    {
                        updateUser();

                    }
                }
            }
        });
        return rootView;
    }
    private void setProptxt()
    {
        txtUpdateEmail.setText(User.userCurrent.mail);
        txtUpdatePassword.setText(User.userCurrent.password);
        txtUpdateUsername.setText(User.userCurrent.username);
    }
    private void updateUser(){
        String url = ApiCon.url + "users/updateUser";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User userUp = gson.fromJson(response, User.class);
                if (userUp._id == null) {
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    User.userCurrent = userUp;
                    Toast.makeText(getActivity(), "Güncelleme işlemi başarılı", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "on error response" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("_id", User.userCurrent._id);
                params.put("username", txtUpdateUsername.getText().toString());
                params.put("password", txtUpdatePassword.getText().toString());
                params.put("mail", txtUpdateEmail.getText().toString());
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private boolean txtControl()
    {
        String strUsername, strPassword, strEmail;
        strEmail = txtUpdateEmail.getText().toString();
        strEmail = strEmail.trim();
        strPassword = txtUpdatePassword.getText().toString();
        strPassword = strPassword.trim();
        strUsername = txtUpdateUsername.getText().toString();
        strUsername = strUsername.trim();
        if(!strEmail.equals("") )
        {
            return true;
        }
        else
        {
            Toast.makeText(getActivity(), "degerler boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean txtCountcontrol()
    {
        String strUsername, strPassword;
        strPassword = txtUpdatePassword.getText().toString();
        strUsername = txtUpdateUsername.getText().toString();
        if(strUsername.trim().length()<5)
        {
            Toast.makeText(getActivity(), "username en az 5 harfli olmalıdır", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(strPassword.trim().length()<4){
            Toast.makeText(getActivity(), "password en az 4 harfli olmalıdır", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
}