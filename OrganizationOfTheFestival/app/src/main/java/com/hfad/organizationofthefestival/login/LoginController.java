package com.hfad.organizationofthefestival.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hfad.organizationofthefestival.AdminActivity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {

    private final String LOGIN_ENDPOINT = "https://barbil.pythonanywhere.com/login";
    private final String ADMINISTRATOR = "administrator";
    private final String LEADER = "leader";
    private final String ORGANIZER = "organizer";
    private final String WORKER = "worker";


    private LoginActivity loginActivity;

    public LoginController(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void login(final String userIdentifier, final String password, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, LOGIN_ENDPOINT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("user_identifier", userIdentifier);
                params.put("password", password);
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void handleResponse(String response) {
        try {
            JSONObject user  = new JSONObject(response);
            String role = user.getString("role");

            if(role.equals(ADMINISTRATOR)) {
                Intent intent = new Intent(loginActivity, AdminActivity.class);
                loginActivity.startActivity(intent);

            }
            else if(role.equals(LEADER)) {

            }
            else if(role.equals(ORGANIZER)) {

            }
            else if(role.equals(WORKER)) {

            }
            else {

            }
        }
        catch(JSONException error) {
            Log.d("Error", error.toString());
        }
    }

}
