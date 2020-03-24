package test.tools;


import test.testsresultsprocessing.TestResult;

public class Assertion {
    public static Object expected;
    public static Object actual;

    public static boolean assertEquals(Object object1, Object object2) {
        expected = object1;
        actual = object2;
        if (expected == null && actual == null) {
            TestResult.testResult(actual, expected, true);
            return true;
        }
        if (expected == null || actual == null) {
            TestResult.testResult(actual, expected, false);
            return false;
        }
        TestResult.testResult(actual, expected, expected.equals(actual));
        return expected.equals(actual);
    }

    public static boolean assertTrue(Boolean b) {
        expected = true;
        actual = b;

        TestResult.testResult(actual, expected, actual == expected);
        return actual == expected;
    }

    public static boolean assertNotNull(Object object) {
        expected = null;
        actual = object;

        TestResult.testResult(actual, expected, actual != expected);
        return actual != expected;
    }
}
