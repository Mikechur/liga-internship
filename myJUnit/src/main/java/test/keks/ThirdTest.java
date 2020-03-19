package test.keks;

import TestAnno.MyJUNIT;

import java.util.ArrayList;

import static TestAnno.TestProcessing.*;

public class ThirdTest {
    @MyJUNIT
    public void checkTwoNumbersEqualsAgain(){
        assertEquals(2,2);
    }

    @MyJUNIT
    public void checkArrayIsNotNull(){
        assertNotNull(new ArrayList());
    }


}
