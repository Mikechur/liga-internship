package test.alltests;

import test.anno.MyJUNIT;

import static test.tools.Assertion.assertNotNull;

import static test.tools.Assertion.assertTrue;

public class ForthTest {

    @MyJUNIT
    public void checkTwoNumbersUnEquals() {
        assertTrue(2 != 3);
    }

    @MyJUNIT
    public void checkNullIsNotNull() {
        assertNotNull(null);
    }
}
