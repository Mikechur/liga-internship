package TestAnno;


public class TestProcessing {

    public static boolean isTestProcessing = false;
    public static int countTest = 0;
    public static int countFailedTests = 0;
    public static int countSuccessTests = 0;


    public static void assertEquals(Object object1, Object object2) {
        if (object1 == null && object2 == null) {
            countSuccessTests++;
            return;
        }
        if (object1 == null || object2 == null) {
            countFailedTests++;
            System.out.println("Actual = " + object1 + "\nExpected = " + object2);
        }

        if (object1.equals(object2)) {
            countSuccessTests++;
        } else {
            countFailedTests++;
            System.out.println("Actual = " + object1 + "\nExpected = " + object2); // testName = ?
        }
        isTestProcessing = true;
    }

    public static void assertTrue(Boolean b) {
        if (b) {
            countSuccessTests++;
        } else {
            System.out.println("Actual = " + b + "\nExpected = " + true); // testName = ?
            countFailedTests++;
        }
        isTestProcessing = true;
    }

    public static void assertNotNull(Object object) {
        if (object != null) {
            countSuccessTests++;
        } else {
            countFailedTests++;
            System.out.println("Actual = " + null + "\nExpected = not null"); // testName = ?
        }
        isTestProcessing = true;
    }
}
