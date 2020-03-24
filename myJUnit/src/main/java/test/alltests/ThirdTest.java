package test.alltests;

import test.anno.MyJUNIT;

import java.util.ArrayList;

import static test.tools.Assertion.*;

public class ThirdTest {
    @MyJUNIT
    public void checkTwoNumbersEqualsAgain() {
        assertEquals(2, 2);
    }

    @MyJUNIT
    public void checkArrayIsNotNull() {
        assertNotNull(new ArrayList());
    }

}
