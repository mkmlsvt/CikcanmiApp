package com.example.cikcanmiapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class fragmentGroup extends Fragment {
    Groups groups;
    TextView txtGroupName,txtGroupCode, txtDurumAyarla;
    Button btnDurumAyarla;
    String groupId;
    LinearLayout layout2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groups = new Groups();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_group,container,false);
        groupId = getArguments().getString("id");
        txtGroupCode = (TextView) rootView.findViewById(R.id.txtGroupCode);
        txtDurumAyarla = (TextView) rootView.findViewById(R.id.txtDurumAyarla);
        txtGroupName = (TextView) rootView.findViewById(R.id.txtGroupName);
        btnDurumAyarla = (Button) rootView.findViewById(R.id.btnDurumAyarla);
        layout2 = (LinearLayout) rootView.findViewById(R.id.container2);
        findGroupById(groupId);
        int i = 3 ;
        /*while (i > 0){
            addUserCard("alex","musaitim cıkarım falan filan fismekan");
            i--;
        }*/
        btnDurumAyarla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDurumAyarla.getText().toString().equals("") || txtDurumAyarla.getText().toString().equals(" "))
                {
                    Toast.makeText(getActivity(), "boş alanları doldurun", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    durumAyarla(txtDurumAyarla.getText().toString());
                }
            }
        });
        return rootView;
    }
    private void addUserCard(String name,String durum) {
        final View view = getLayoutInflater().inflate(R.layout.card_durum, null);
        TextView lblUserNameCard = view.findViewById(R.id.lblUsernameCard);
        TextView lblDurum = view.findViewById(R.id.lblDurum);
        //Button delete = view.findViewById(R.id.delete);
        lblDurum.setText(durum);
        lblUserNameCard.setText(name);
        layout2.addView(view);
    }
    private void findGroupById(String groupId) {
        String url = ApiCon.url + "groups/findGroupById";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                groups = gson.fromJson(response, Groups.class);
                if (groups._id == null) {
                    Toast.makeText(getActivity(), "post islemi basarisiz", Toast.LENGTH_SHORT).show();
                } else {
                    Groups.currentGroup = groups;
                    setGroupProp();
                    setUserProp();
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
                params.put("_id", groupId);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void setGroupProp(){
        txtGroupName.setText(Groups.currentGroup.name.toString());
        txtGroupCode.setText(Groups.currentGroup.code.toString());
    }
    private void setUserProp()
    {
        for(int i = 0 ; i<Groups.currentGroup.members.size(); i++)
        {
            //addUserCard("sss","musaitim cıkarım falan filan fismekan");
            getUserByIdAndAddList(Groups.currentGroup.members.get(i));
        }
    }
    private void getUserByIdAndAddList(String userId) {
        String url = ApiCon.url + "users/findById";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User userUp = gson.fromJson(response, User.class);
                if (userUp._id == null) {
                    Toast.makeText(getActivity(), "post islemi basarisiz", Toast.LENGTH_SHORT).show();
                } else {
                    User.userInGroup.add(userUp);
                    addUserCard(userUp.username,userUp.durum);
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
                params.put("_id", userId);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void durumAyarla(String durum){
        String url = ApiCon.url + "users/updateDurum";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User userUp = gson.fromJson(response, User.class);
                if (userUp._id == null) {
                    Toast.makeText(getActivity(), "post islemi basarisiz", Toast.LENGTH_SHORT).show();
                } else {
                    User.userCurrent = userUp;
                    Toast.makeText(getActivity(), "durum başarıyla güncellendi, 2 saniye sonra ana sayfaya yönlendirilceksiniz..", Toast.LENGTH_SHORT).show();
                    refreshFragment();

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
                params.put("durum", durum);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void refreshFragment(){
        Handler hndlr = new Handler();
        fragmentHome fragmentHome = new fragmentHome();
        Toast.makeText(getActivity(), "2 saniye sonra ana sayfaya yönlendirlceksiniz..", Toast.LENGTH_SHORT).show();
        hndlr.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FragmentContainer,fragmentHome,"group");
                transaction.commit();
            }
        },3000);

    }
}
