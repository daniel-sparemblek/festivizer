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

    public static void logIn(final String userName, final String password, final Context context) {

    }

    public static void register(final String userName, final String password, final String firstName, final String lastName,
                                 final byte[] picture, final String phone, final String email, final Context context) {

    }

}
