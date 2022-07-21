package com.example.cikcanmiapp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class fragmentHome extends Fragment {
    Groups group = new Groups();
    Button add, btnJoin;
    AlertDialog dialog, dialogjoin;
    String groupCode;
    LinearLayout layout;
    String groupName;
    User user = new User();
    Handler hndlr = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        //add = findViewById(R.id.add);
        add = (Button) rootView.findViewById(R.id.add);
        btnJoin = (Button) rootView.findViewById(R.id.btnJoin);
        //layout = findViewById(R.id.container);
        layout = (LinearLayout) rootView.findViewById(R.id.container);
        buildDialogJoin();
        buildDialog();
        getCurrentUserById();
        for(int i = 0; i<=User.userCurrent.groups.size()-1; i++)
        {
            findGroupById(User.userCurrent.groups.get(i));
        }
        //findGroupById();
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogjoin.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        return rootView;
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //addCard(name.getText().toString());
                        groupName = name.getText().toString();
                        createGroup(groupName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();

    }
    private void buildDialogJoin()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_join, null);

        final EditText txtCode = view.findViewById(R.id.txtCode);

        builder.setView(view);
        builder.setTitle("Enter a code")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //addCard(name.getText().toString());
                        groupCode = txtCode.getText().toString();
                        findGroupByCode();
                        if(isAlreadyInGroup())
                        {
                            //joinGroup();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "zaten bu grupta varsınız", Toast.LENGTH_SHORT).show();
                        }

                        //createGroup(groupName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialogjoin = builder.create();
    }

    private void addCard(String name,String id) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        //Button delete = view.findViewById(R.id.delete);
        Button btnincele = view.findViewById(R.id.btnincele);
        nameView.setText(name);
        btnincele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentGroup fragmentGroup = new fragmentGroup();
                Bundle args = new Bundle();
                args.putString("id",id);
                fragmentGroup.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FragmentContainer,fragmentGroup,"group");
                transaction.commit();


            }
        });
       /* delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });*/

        layout.addView(view);
    }

    private void findGroupById(String groupId) {
        String url = ApiCon.url + "groups/findGroupById";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Groups group = gson.fromJson(response, Groups.class);
                if (group._id == null) {
                    Toast.makeText(getActivity(), "post islemi basarisiz", Toast.LENGTH_SHORT).show();
                } else {
                    addCard(group.name.toString(),group._id);
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

    private void getCurrentUserById() {
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
                    User.userCurrent = userUp;
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
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void createGroup(String groupName) {
        String url = ApiCon.url + "groups/create";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                group = gson.fromJson(response, Groups.class);
                if (group._id == null)
                    Toast.makeText(getActivity(), "grup olusturma basarısız", Toast.LENGTH_SHORT).show();
                else {
                    getCurrentUserById();
                    findGroupById(group._id);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "on error response" + error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("members", User.userCurrent._id);
                params.put("name", groupName);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void findGroupByCode() {
        String url = ApiCon.url + "groups/findByCode";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                group = gson.fromJson(response, Groups.class);

                if(group._id==null)
                {
                    Toast.makeText(getActivity(), "yanlıs biseyler oldu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Groups.joinGroupId = group._id;
                    Toast.makeText(getActivity(), group._id, Toast.LENGTH_SHORT).show();
                    joinGroup();
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
                params.put("code", groupCode);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public boolean isAlreadyInGroup()
    {
        if(User.userCurrent.groups.size()==0)
        {
            return true;
        }
        /*for(int i = 0 ; i <= User.userCurrent.groups.size(); i++)
        {
            if(group._id.equals(User.userCurrent.groups.get(i)))
            {
                return false;
            }
        }*/
        return true;
    }
    private void joinGroup() {
        String url = ApiCon.url + "users/joinGroup";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                user = gson.fromJson(response, User.class);
                if(user._id==null)
                {
                    Toast.makeText(getActivity(), "yanlıs biseyler oldu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User.userCurrent = user;
                    Toast.makeText(getActivity(), "gruba katıldınnnnnnnnnnnnnnnn", Toast.LENGTH_SHORT).show();
                    findGroupById(Groups.joinGroupId);
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
                params.put("userid", User.userCurrent._id);
                params.put("groupid",Groups.joinGroupId);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(stringRequest);
    }

}

