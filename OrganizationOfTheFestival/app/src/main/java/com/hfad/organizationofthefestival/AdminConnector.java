package com.hfad.organizationofthefestival;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminConnector extends Connector {

    public interface AdminListener {
        void onAdminResponse(ArrayList<String> pendingLeaders);
    }

    public static void getPendingLeaders(final String userIdentifier, final String password, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String adminUrl = url + "admin";

        StringRequest postRequest = new StringRequest(Request.Method.POST, adminUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> pendingLeaders = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONObject(response).getJSONArray("pending_leaders");
                            int i = 0;

                            while(true) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pendingLeaders.add((String)jsonObject.get("username"));
                                i++;
                            }


                        } catch (JSONException e) {
                            ((AdminListener)context).onAdminResponse(pendingLeaders);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        ((ServerListener)context).onLogInResponse(getStatus("server_down"));
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


    public static void sendDecision(final String username, final String password, final String leaderUsername,
                                    final Decision decision, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String adminUrl = url + "admin/send_decision";

        StringRequest postRequest = new StringRequest(Request.Method.POST, adminUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ((AdminListener) context).onSendDecisionResponse(getStatus(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((AdminListener) context).onSendDecisionResponse(getStatus("server_down"));
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_identifier", username);
                params.put("password", password);
                params.put("leader_username", leaderUsername);
                params.put("decision", convertDecisionToString(decision));
                return params;
            }
        };
        queue.add(postRequest);
    }

}
