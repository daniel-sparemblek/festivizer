package com.hfad.organizationofthefestival.leader;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hfad.organizationofthefestival.Connector;
import com.hfad.organizationofthefestival.Decision;
import com.hfad.organizationofthefestival.ServerStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeaderController {

    protected static final String url = "https://barbil.pythonanywhere.com/";

    public interface LeaderListener {
        void onGetPendingOrganizersResponse(ArrayList<String> pendingOrganizers);
        void onSendDecisionResponse(ServerStatus serverStatus);
    }

    public static void getPendingOrganizers(final String username, final String password, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String leaderUrl = url + "leader/"+username+"/get_organizers";

        StringRequest postRequest = new StringRequest(Request.Method.POST, leaderUrl,
                new Response.Listener<String>()
                {
                    public void onResponse(String response) {
                        ArrayList<String> pendingOrganizers = new ArrayList<>();

                        String[] organizerList = response.split(";");

                        pendingOrganizers.addAll(Arrays.asList(organizerList));

                        ((LeaderListener)context).onGetPendingOrganizersResponse(pendingOrganizers);
                    }
                },
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error) {
                        ((LeaderListener)context).onGetPendingOrganizersResponse(null);
                    }
                }
        ) {
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

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

    // Fix when JSONs get here
    /*public static void sendDecision(final String leaderUsername, final String password, final String organizerUsername,
                                    final String festivalID, final Decision decision, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String leaderUrl = url + "leader/"+leaderUsername+"/send_decision";

        StringRequest postRequest = new StringRequest(Request.Method.POST, leaderUrl,
                new Response.Listener<String>()
                {
                    public void onResponse(String response) {
                        ((LeaderListener)context).onSendDecisionResponse(getStatus(response));
                    }
                },
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error) {
                        ((LeaderListener)context).onSendDecisionResponse(getStatus("server_down"));
                    }
                }
        ) {
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("user_identifier", leaderUsername);
                params.put("password", password);
                params.put("organizer_username", organizerUsername);
                params.put("festival_id", festivalID);
                params.put("decision", convertDecisionToString(decision));
                return params;
            }
        };
        queue.add(postRequest);
    }*/

    protected static String convertDecisionToString(Decision decision) {
        if(decision == Decision.ACCEPT) {
            return "accept";
        }
        if(decision == Decision.DECLINE) {
            return "decline";
        }
        return null;
    }
}
