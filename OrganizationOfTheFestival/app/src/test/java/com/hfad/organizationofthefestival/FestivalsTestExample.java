package com.hfad.organizationofthefestival;

import com.hfad.organizationofthefestival.festival.Festival;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FestivalsTestExample {

    Festival properFest = new Festival("goodFest", "test proper example description",
            "exampleLogo", "startTime", "endTime");

    Festival badFest = new Festival("", "bad festival", "","lul-time", "ending");

    @Test
    public static void FestivalTest(){
        assertEquals(true, true);
    }

}
