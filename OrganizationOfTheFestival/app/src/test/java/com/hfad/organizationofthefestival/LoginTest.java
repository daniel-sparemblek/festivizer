package com.hfad.organizationofthefestival;

import com.hfad.organizationofthefestival.login.Login;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginTest {

    Login testLogin = new Login("marko22", "");

    @Test
    public void correctLogin() {
        assertEquals("marko22", testLogin.getUsername());
    }

    @Test
    public void isValidTest(){
        assertFalse(testLogin.isValid());
    }
}