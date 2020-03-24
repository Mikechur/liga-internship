package test.alltests.firsttests;

import test.anno.After;
import test.anno.Before;
import test.anno.MyJUNIT;

import static test.tools.Assertion.*;

public class SecondTest {
    StringBuilder stringBuilder;

    @Before
    public void setup() {
        System.out.println("Im gonna create a builder if u dont mind");
        stringBuilder = new StringBuilder();
        stringBuilder.append("123");
    }

    @MyJUNIT
    public void checkTwoNumbersAreEquals() {
        assertEquals(5, 5);
    }

    @MyJUNIT
    public void checkIfObjectIsNotNull() {
        Object object = null;
        assertNotNull(object);
    }

    @MyJUNIT
    public void checkStringValueInBuilder() {
        System.out.println("Im gonna check value in string builder");
        assertEquals(stringBuilder.toString(), "123");
    }

    @After
    public void deleteToHellStringBuilder() {
        System.out.println("Im deleting useless content of builder");
        stringBuilder.delete(0, stringBuilder.toString().length());
        System.out.println("Builder content is empty. Length = " + stringBuilder.length());
    }
}
