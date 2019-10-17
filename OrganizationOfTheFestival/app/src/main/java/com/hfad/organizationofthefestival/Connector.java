package com.hfad.organizationofthefestival;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents connector between Android application and
 * Flask Python server. This class provides all necessary methods
 * in order to communicate with the server.
 */
public class Connector {

    /**
     * This variable represents ipv4 address of your PC.
     * Change this variable depending on the ipv4 address of your PC.
     */
    private static final String ipv4 = "ENTER_IPV4";

    /**
     * This variable represents url address of server.
     */
    private static final String url = "http://" + ipv4 + ":5000/";

    /**
     * This is interface which every Activity that wants to call
     * methods provided in the class Connector must implement.
     */
    public interface ServerListener {
        /**
         * This method is used to make any action based on the <code>ServerStatus</code>
         * returned by the Connector's <code>logIn</code> method.
         * @param status status of server returned by Connector's <code>logIn</code> method.
         */
        void onLogInResponse(ServerStatus status);

        /**
         * This method is used to make any action based on the <code>ServerStatus</code>
         * returned by the Connector's <code>register</code> method.
         * @param status status of server returned by Connector's <code>register</code> method.
         */
        void onRegisterResponse(ServerStatus status);
    }

    /**
     * This method will communicate with the server and attempt to log in the user based on the
     * parameters provided. After calling this method, this method will call the method
     * <code>onLogInResponse</code> with <code>ServerStatus</code> parameter that is
     * based on the server's response, There are four possible scenarios:
     * <li>
     * <ul>SUCCESS - The user has managed to successfully log in. Username and password match.</ul>
     * <ul>NO_USERNAME - Log in has failed. Username does not exist in the database</ul>
     * <ul>WRONG_PASSWORD - Log in has failed. Username does exist in the database but password does not match.</ul>
     * <ul>SERVER_DOWN - Log in has failed. Server is down.</ul>
     * </li>
     * @param userName username
     * @param password password
     * @param context context
     */
    public static void logIn(final String userName, final String password, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String logInUrl = url + "login";

        StringRequest postRequest = new StringRequest(Request.Method.POST, logInUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ((ServerListener)context).onLogInResponse(getStatus(response));
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
                params.put("username", userName);
                params.put("password", password);
                return params;
            }
        };
        queue.add(postRequest);
    }

    public static void register(final String userName, final String password, final String firstName, final String lastName,
                                final byte[] picture, final String phone, final String email, final Context context) {

    }

    /**
     * This method is used to match server's String response with the correct <code>ServerStatus</code>.
     * @param string server's String response
     * @return <code>ServerStatus</code> status based on Server's String response.
     */
    private static ServerStatus getStatus(String string) {
        if(string.equals("no_username")) return ServerStatus.NO_USERNAME;
        if(string.equals("wrong_password")) return ServerStatus.WRONG_PASSWORD;
        if(string.equals("server_down")) return ServerStatus.SERVER_DOWN;
        if(string.equals("success")) return ServerStatus.SUCCESS;
        if(string.equals("username_exists")) return ServerStatus.USERNAME_EXISTS;
        return ServerStatus.UNKNOWN;
    }

    /**
     * This method will convert signed byte array to unsigned int array.
     * @param signedBytes signed bytes
     * @return unsigned bytes
     */
    private static int[] convertSignedByteToUnsignedByte(byte[] signedBytes) {
        int[] unsignedBytes = new int[signedBytes.length];

        for(int i = 0; i < signedBytes.length; i++) {
            unsignedBytes[i] = signedBytes[i] & 0xff;
        }
        return unsignedBytes;
    }
}