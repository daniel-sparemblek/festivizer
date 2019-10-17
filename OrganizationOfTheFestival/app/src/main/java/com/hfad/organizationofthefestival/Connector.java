package com.hfad.organizationofthefestival;

import android.content.Context;

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

    public static void logIn(final String userName, final String password, final Context context) {

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
}
