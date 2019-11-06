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

public class LeaderConnector extends Connector {

    public interface LeaderListener {
        void onLeaderResponse(ArrayList<String> pendingOrganizers);
    }

    public static void getPendingOrganizers(final String username, final String password, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String leaderUrl = url + "leader/"+username;

        StringRequest postRequest = new StringRequest(Request.Method.POST, leaderUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> pendingOrganizers = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONObject(response).getJSONArray("pending_organizers");
                            int i = 0;

                            while(true) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pendingOrganizers.add((String)jsonObject.get("username"));
                                i++;
                            }

                        } catch (JSONException e) {
                            ((LeaderListener)context).onLeaderResponse(pendingOrganizers);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((LeaderListener)context).onLeaderResponse(null);
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
                params.put("user_identifier", username);
                params.put("password", password);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
