package com.hfad.organizationofthefestival;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents connector between Android application and
 * Flask Python server. This class provides all necessary methods
 * in order to communicate with the server.
 */
public class Connector {

    /**
     * This variable represents url address of server.
     */
    protected static final String url = "http://barbil.pythonanywhere.com/";

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
     * @param userIdentifier username or email
     * @param password password
     * @param context context
     */
    public static void logIn(final String userIdentifier, final String password, final Context context) {

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
                params.put("user_identifier", userIdentifier);
                params.put("password", password);
                return params;
            }
        };
        queue.add(postRequest);
    }

    /**
     * This method will communicate with the server and attempt to register (add to
     * the database) the user based on the parameters provided. After calling this method, this
     * method will call the method <code>onRegisterResponse</code> with <code>ServerStatus</code>
     * parameter that is based on the server's response. There are three possible scenarios
     * <li>
     *     <ul>SUCCESS - The user has managed to successfully register. User was added to the database.</ul>
     *     <ul>USERNAME_EXISTS - The user has failed to register. Username already exists in the database, username has to be unique. </ul>
     *     <ul>SERVER_DOWN - The user has failed to register. Server is down.</ul>
     * </li>
     *
     * @param userName username
     * @param password password
     * @param firstName firstname
     * @param lastName lastname
     * @param picture picture
     * @param phone phone
     * @param email email
     * @param context context
     */
    public static void register(final String userName, final String password, final String firstName, final String lastName,
                                byte[] picture, final String phone, final String email, Role role, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String registerUrl = url + "register";
        final int[] unsignedPictureBytes = convertSignedByteToUnsignedByte(picture);
        final String roleAsString = convertRoleToString(role);

        if(userName.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                picture.length == 0 || phone.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ((ServerListener)context).onRegisterResponse(getStatus(response));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        ((ServerListener)context).onRegisterResponse(getStatus("server_down"));
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
                params.put("firstname", firstName);
                params.put("lastname", lastName);
                params.put("picture", Arrays.toString(unsignedPictureBytes));
                params.put("phone", phone);
                params.put("email", email);
                params.put("role", roleAsString);
                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * This method is used to match server's String response with the correct <code>ServerStatus</code>.
     * @param string server's String response
     * @return <code>ServerStatus</code> status based on Server's String response.
     */
    protected static ServerStatus getStatus(String string) {
        if(string.equals("no_username")) return ServerStatus.NO_USERNAME;
        if(string.equals("wrong_password")) return ServerStatus.WRONG_PASSWORD;
        if(string.equals("server_down")) return ServerStatus.SERVER_DOWN;
        if(string.equals("success")) return ServerStatus.SUCCESS;
        if(string.equals("username_exists")) return ServerStatus.USERNAME_EXISTS;
        if(string.equals("email_exists")) return ServerStatus.EMAIL_EXISTS;
        if(string.equals("phone_exists")) return ServerStatus.PHONE_EXISTS;
        if(string.equals("admin")) return ServerStatus.ADMIN;
        if(string.equals("organizer")) return ServerStatus.ORGANIZER;
        if(string.equals("leader")) return ServerStatus.LEADER;
        if(string.equals("permission_denied")) return ServerStatus.PERMISSION_DENIED;
        if(string.equals("accepted")) return ServerStatus.ACCEPTED;
        if(string.equals("denied")) return ServerStatus.DENIED;
        if(string.equals("pending")) return ServerStatus.PENDING;
        return ServerStatus.UNKNOWN;
    }

    private static String convertRoleToString(Role role) {
        if(role == Role.LEADER) {
            return "leader";
        }
        if(role == Role.ORGANIZER) {
            return "organizer";
        }
        return "worker";
    }

    /**
     * This method will convert signed byte array to unsigned int array.
     * @param signedBytes signed bytes
     * @return unsigned bytes
     */
    protected static int[] convertSignedByteToUnsignedByte(byte[] signedBytes) {
        int[] unsignedBytes = new int[signedBytes.length];

        for(int i = 0; i < signedBytes.length; i++) {
            unsignedBytes[i] = signedBytes[i] & 0xff;
        }
        return unsignedBytes;
    }

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
