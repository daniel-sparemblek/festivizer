package com.hfad.organizationofthefestival;

public enum ServerStatus {

    /**
     * Server will return this status when user has tried to log in with wrong password.
     */
    WRONG_PASSWORD,
    /**
     * Server will return this status when user has tried to log in with the user name
     * that does not exist in database.
     */
    NO_USERNAME,
    /**
     * Server will return this status when user's action has been successfully completed.
     */
    SUCCESS,
    /**
     * Server will return this status when Administrator has successfully logged in.
     */
    ADMIN,
    /**
     * Server will return this status when Organizer has successfully logged in.
     */
    ORGANIZER,
    /**
     * Server will return this status when Leader has successfully logged in.
     */
    LEADER,
    /**
     * Server will return this status when user tries to connect with server but
     * server is not running.
     */
    SERVER_DOWN,
    /**
     * Server will return this status when user tries to call methods without required permission.
     */
    PERMISSION_DENIED,
    /**
     * Server will return this status when user tries to register with the username
     * that already exists.
     */
    USERNAME_EXISTS,
    /**
     * Server will return this status when user tries to register with an email
     * that already exists.
     */
    EMAIL_EXISTS,
    /**
     * Server will return this status when user tries to register with a phone that
     * already exists.
     */
    PHONE_EXISTS,
    /**
     * Server will return this status if none of the other statuses above is returned.
     */
    UNKNOWN
}
