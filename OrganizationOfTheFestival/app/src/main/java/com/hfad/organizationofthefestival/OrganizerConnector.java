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

public class OrganizerConnector extends Connector {

    public static interface OrganizerListener {
        void onGetFestivalsResponse(ArrayList<String> festivals);
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

}
