package com.hfad.organizationofthefestival;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OrganizerConnector extends Connector {

    public interface OrganizerListener {
        void onGetFestivalsResponse(ArrayList<String> festivals);
        void onApplyForFestivalResponse(ServerStatus status);
    }

    public static void getFestivals(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String festival = url + "festival";

        StringRequest getRequest = new StringRequest(Request.Method.GET, festival,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> festivals = new ArrayList<>();

                        String[] festivalList = response.split(";");

                        festivals.addAll(Arrays.asList(festivalList));

                        ((OrganizerConnector.OrganizerListener)context).onGetFestivalsResponse(festivals);
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

    public static void applyForFestival(final String festivalName, final String organizerUsername, final Context context) {
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
                        ((OrganizerConnector.OrganizerListener)context).onApplyForFestivalResponse(getStatus("server_down"));
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
                System.out.println(festivalName  + " " + organizerUsername);
                Map<String, String>  params = new HashMap<>();
                params.put("festivalName", festivalName);
                params.put("username", organizerUsername);
                return params;
            }
        };
        queue.add(postRequest);
    }

}
