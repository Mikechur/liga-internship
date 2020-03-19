package test.keks;

import TestAnno.MyJUNIT;

import java.util.ArrayList;

import static TestAnno.TestProcessing.assertEquals;
import static TestAnno.TestProcessing.assertNotNull;

import static TestAnno.TestProcessing.assertTrue;

public class ForthTest {

    @MyJUNIT
    public void checkTwoNumbersUnEquals(){
        assertTrue(2 != 3);
    }

    @MyJUNIT
    public void checkNullIsNotNull(){
        assertNotNull(null);
    }
}
