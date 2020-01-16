package com.hfad.organizationofthefestival;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.worker.Specialization;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class FailingTest {

    @Test
    public void emptyTests(){
        // Can we make a no-name specialization?
        assertThrows(IllegalArgumentException.class, () -> {
            Specialization emptySpec = new Specialization("");
        });

        // Can we make empty Festival?
        assertThrows(IllegalArgumentException.class, () -> {
            Festival boatFest = new Festival("", "very cool"
                    , "SAMPLE-LOGO", "6 oclock", "never"
            );
        });
    }
}
