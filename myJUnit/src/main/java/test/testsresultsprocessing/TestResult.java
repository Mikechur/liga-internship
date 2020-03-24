package test.testsresultsprocessing;

public class TestResult {
    public static final String EQUALS_SIGN = "==";
    public static final String NOT_EQUALS_SIGN = "!==";
    public static final String SUCCESS = "Test success: ";
    public static final String FAILED = "Test failed: ";
    public static final String NULL = "null";

    public static String testReport = "";

    public static String testResult(Object actual, Object expected, boolean result) {
        String actualStr = actual == null ? NULL : actual.toString();
        String expectedStr = expected == null ? NULL : expected.toString();

        String finalWord = result ? SUCCESS : FAILED;
        String sign = result ? EQUALS_SIGN : NOT_EQUALS_SIGN;

        if (actual != null && expected != null) {
            if ((actual.equals(expected)) != result) {
                sign = sign.equals(EQUALS_SIGN) ? NOT_EQUALS_SIGN : EQUALS_SIGN;
            }
        } else if ((actual == expected) != result) {
            sign = sign.equals(EQUALS_SIGN) ? NOT_EQUALS_SIGN : EQUALS_SIGN;
        }

        return testReport = finalWord + actualStr + sign + expectedStr;
    }

    public static void setResultToZero() {
        testReport = "";
    }

    public static String testDefaultResult() {
        return SUCCESS;
    }


}
