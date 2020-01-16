package com.hfad.organizationofthefestival;

import com.hfad.organizationofthefestival.festival.Event.CreateEventActivity;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.login.Login;
import com.hfad.organizationofthefestival.worker.JobApplyActivity;
import com.hfad.organizationofthefestival.worker.JobProfileActivity;
import com.hfad.organizationofthefestival.worker.Specialization;

import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SuccessfulTests {

    Login testLogin = new Login("marko22", "");
    Login goodTestLogin = new Login("validMarko", "goodPass");

    @Test
    public void correctLogin() {

        assertEquals("marko22", testLogin.getUsername());
    }

    @Test
    public void isValidTest() {
        assertFalse(testLogin.isValid());
        assertTrue(goodTestLogin.isValid());
    }

    @Test
    public void specializationsToStringsTest(){
        List<Specialization> specializationsList = new ArrayList<>();

        Specialization toAdd1 = new Specialization("Writing JUnit tests");
        Specialization toAdd2 = new Specialization("Debugging");
        Specialization toAdd3 = new Specialization("Drinking alcoholic beverages");
    
        specializationsList.add(toAdd1);
        specializationsList.add(toAdd2);
        specializationsList.add(toAdd3);

        JobProfileActivity testJPA = new JobProfileActivity();
        List<String> actualStringList = testJPA.specializationsToStrings(specializationsList);

        List<String> expectedStringList = new ArrayList<>();
        expectedStringList.add("Writing JUnit tests");
        expectedStringList.add("Debugging");
        expectedStringList.add("Drinking alcoholic beverages");

        assertEquals(expectedStringList, actualStringList);

        // Let's test a mismatch
        specializationsList.clear();
        toAdd1 = new Specialization("Vacuuming");
        toAdd2 = new Specialization("Floor cleaning");
        toAdd3 = new Specialization("Desk cleaning");
        Specialization toAdd4 = new Specialization("Stage lights management");
        Specialization toAdd5 = new Specialization("Music");

        specializationsList.add(toAdd1);
        specializationsList.add(toAdd2);
        specializationsList.add(toAdd3);
        specializationsList.add(toAdd4);
        specializationsList.add(toAdd5);

        actualStringList = testJPA.specializationsToStrings(specializationsList);
        assertNotEquals(expectedStringList, actualStringList);
    }

    @Test
    public void parseDateTimeTest(){
        JobApplyActivity JAATest = new JobApplyActivity();

        String testDateTime = "2019.07.07 03:14:22";
        int year = 2019;
        int month = 7;
        int day = 7;
        int hour = 3;
        int minute = 14;
        int second = 22;

        ZonedDateTime actualZonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
        ZonedDateTime expectedZonedDateTime = JAATest.parseDateTime(testDateTime);
        assertEquals(expectedZonedDateTime, actualZonedDateTime);

        // Let's try something that wouldn't work
        assertThrows(NumberFormatException.class, () -> {JAATest.parseDateTime("WRONG");});

        // Another good example
        testDateTime = "1000.01.01 03:14:22";
        year = 1000;
        month = 1;
        day = 1;
        hour = 3;
        minute = 14;
        second = 22;

        actualZonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
        expectedZonedDateTime = JAATest.parseDateTime(testDateTime);
        assertEquals(expectedZonedDateTime, actualZonedDateTime);

        // 30.2 shouldn't exist
        assertThrows(DateTimeException.class, () -> {JAATest.parseDateTime("2019.30.02 03:14:22");});

        // Mismatch dates
        testDateTime = "1000.01.02 03:14:22";
        year = 1000;
        month = 1;
        day = 1;
        hour = 3;
        minute = 14;
        second = 22;

        actualZonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
        expectedZonedDateTime = JAATest.parseDateTime(testDateTime);
        assertNotEquals(expectedZonedDateTime, actualZonedDateTime);
    }

    @Test
    public void convertTimeTest(){
        int year = 2019;
        int month = 7;
        int day = 7;
        int hour = 3;
        int minute = 14;
        int second = 22;
        ZonedDateTime expectedZDT = ZonedDateTime.of(
                year, month, day, hour, minute, second, 0, ZoneId.systemDefault()
        );
        // First a proper one

        CreateEventActivity CEATest = new CreateEventActivity();

        String expectedZDTString = "2019-07-07T03:14:00.000+0000";
        String actualZDTString = CEATest.convertTime("03:14", "07.07.2019.");
        assertEquals(expectedZDTString, actualZDTString);

        // Another example - a bit older but still checks out
        expectedZDTString = "0768-01-01T00:00:00.000+0000";
        actualZDTString = CEATest.convertTime("00:00", "01.01.0768.");
        assertEquals(expectedZDTString, actualZDTString);

        // Now let's do a bad example - MARGINAL CASE
        // 3 digit year?
        assertThrows(DateTimeParseException.class, () -> {CEATest.convertTime("00:00", "01.01.768");});

        // What about normal years, but single-digit minutes?
        assertThrows(DateTimeParseException.class, () -> {CEATest.convertTime("1:00", "01.01.2020");});

        // What about single-digit months or days?
        assertThrows(DateTimeParseException.class, () -> {CEATest.convertTime("00:00", "1.01.2021");});
    }

    @Test
    public void getFestivalTest() {
        Leader testLeader = new Leader(0, "testLeader",
                "123", "Testo", "Testen"
                , "SAMPLE-IMAGE", "09819819"
                , "test@test.com", 1, 0);

        // Let's first test an empty list
        List<Festival> festivalsList = new ArrayList<>();
        testLeader.setFestivals(festivalsList);

        assertEquals(null, testLeader.getFestival("CoolFest"));
        // Now let's add the CoolFest
        Festival coolFest = new Festival("coolFest", "very cool"
                , "SAMPLE-LOGO", "6 oclock", "never"
                );
        festivalsList.add(coolFest);

        assertEquals(null, testLeader.getFestival("wut"));
        assertEquals(coolFest, testLeader.getFestival("coolFest"));

        // Now let's test getFestivalNames method
        // Will need more festivals
        Festival superFest = new Festival("superFest", "very cool"
                , "SAMPLE-LOGO", "6 oclock", "never"
        );
        Festival marinFest = new Festival("marinFest", "very cool"
                , "SAMPLE-LOGO", "6 oclock", "never"
        );
        Festival boatFest = new Festival("boatFest", "very cool"
                , "SAMPLE-LOGO", "6 oclock", "never"
        );

        festivalsList.add(superFest);
        festivalsList.add(marinFest);
        festivalsList.add(boatFest);
        List<String> expectedFestNames = new ArrayList<>();
        expectedFestNames.add("coolFest");
        expectedFestNames.add("superFest");
        expectedFestNames.add("marinFest");
        expectedFestNames.add("boatFest");
        assertEquals(expectedFestNames, testLeader.getFestivalNames());
    }
}