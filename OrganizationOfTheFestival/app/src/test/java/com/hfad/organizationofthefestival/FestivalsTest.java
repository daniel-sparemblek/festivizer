package com.hfad.organizationofthefestival;

import com.hfad.organizationofthefestival.festival.Festival;

import org.junit.Test;

public class FestivalsTest {

    Festival properFest = new Festival("goodFest", "test proper example description",
            "exampleLogo", "startTime", "endTime");

    Festival badFest = new Festival("");

    @Test
    public static FestivalTest(){

    }

}
