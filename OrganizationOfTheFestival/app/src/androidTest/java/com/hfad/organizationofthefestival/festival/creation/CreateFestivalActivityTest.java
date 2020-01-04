package com.hfad.organizationofthefestival.festival.creation;

import org.junit.Test;

public class CreateFestivalActivityTest {

    @Test
    public void convertTimeTest(){
        CreateFestivalActivity activity = new CreateFestivalActivity();
        String dateTimeString = "4.1.2020. 17:00";
        System.out.println("TEST " + activity.convertTime(dateTimeString));
    }

}