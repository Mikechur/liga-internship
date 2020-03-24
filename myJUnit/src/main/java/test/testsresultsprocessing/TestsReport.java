package test.testsresultsprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestsReport {
    public static List allTests = new ArrayList<String>();

    public static List addTestResult(String smallResult) {
        allTests.add(smallResult);
        return allTests;
    }

    public static List getTestsWithTypeResult(List<String> tests, String typeResult) {
        List<String> typeTests = tests.stream().filter(test -> test.startsWith(typeResult)).collect(Collectors.toList());
        return typeTests;
    }

    public static void printTests(List<String> tests) {
        System.out.println("Length of tests: " + tests.size());
        tests.forEach(System.out::println);
    }
}
