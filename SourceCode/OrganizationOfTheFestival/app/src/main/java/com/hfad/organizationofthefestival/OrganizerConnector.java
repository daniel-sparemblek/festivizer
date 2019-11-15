package com.hfad.organizationofthefestival;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class OrganizerConnector extends Connector {

    public interface OrganizerListener {
        void onGetFestivalsResponse(HashMap<String, Integer> festivals);
        void onApplyForFestivalResponse(ServerStatus status);
    }

    public static void getFestivals(final String username, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String festival = url + "festival/" + username;

        StringRequest getRequest = new StringRequest(Request.Method.GET, festival,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, Integer> festivalStatusPair = new HashMap<>();
                        String[] keyValue = response.split(";");

                        for (String s : keyValue) {
                            if(s.isEmpty()) break;
                            String[] seperated = s.split(",");
                            int value = Integer.parseInt(seperated[1]);
                            festivalStatusPair.put(seperated[0], value);
                        }

                        ((OrganizerConnector.OrganizerListener)context).onGetFestivalsResponse(festivalStatusPair);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((OrganizerConnector.OrganizerListener)context).onGetFestivalsResponse(null);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        queue.add(getRequest);
    }

    public static void applyForFestival(final String festivalName, final String organizerUsername, final String status, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String festivalApply = url + "festival/apply";

        StringRequest postRequest = new StringRequest(Request.Method.POST, festivalApply,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ((OrganizerConnector.OrganizerListener)context).onApplyForFestivalResponse(getStatus(response));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((OrganizerConnector.OrganizerListener)context).onApplyForFestivalResponse(null);
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
                params.put("festivalName", festivalName);
                params.put("username", organizerUsername);
                params.put("status", status);
                return params;
            }
        };
        queue.add(postRequest);
    }

}
