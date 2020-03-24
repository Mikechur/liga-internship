package test.tools;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import test.anno.After;
import test.anno.Before;
import test.anno.MyJUNIT;
import test.testsresultsprocessing.TestsReport;
import test.testsresultsprocessing.TestResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public class Framework {

    public static void customJUnitRunTests(String packageWithTestClasses) throws Exception {
        Reflections reflections = new Reflections(packageWithTestClasses, new SubTypesScanner(false));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        for (Class<?> someClass : classes) {
            Object newInstance = someClass.getDeclaredConstructor().newInstance();
            launchMethods(newInstance, someClass, Before.class);
            launchMethods(newInstance, someClass, MyJUNIT.class);
            launchMethods(newInstance, someClass, After.class);
        }

        List<String> allTests = TestsReport.allTests;
        List<String> failedTests = TestsReport.getTestsWithTypeResult(allTests, TestResult.FAILED);
        List<String> successTests = TestsReport.getTestsWithTypeResult(allTests, TestResult.SUCCESS);
        TestsReport.printTests(failedTests);
        TestsReport.printTests(successTests);
    }

    public static void launchMethods(Object ob, Class someClass, Class<? extends Annotation> anno) throws Exception {
        Class cl = Class.forName(someClass.getName());
        Method[] methods = cl.getMethods();
        for (Method md : methods) {
            if (!checkMethodSignature(md, anno)) {
                continue;
            }
            md.invoke(ob, null);

            if (!md.isAnnotationPresent(MyJUNIT.class)) {
                continue;
            }

            // Добавление результата запуска теста без ассерта
            if (TestResult.testReport.equals("")) {
                String defaultTestResult = TestResult.testDefaultResult() + " " + md.getName();
                TestsReport.addTestResult(defaultTestResult);
                continue;
            }

            // Добавление результата запуска теста с ассертом
            String testResult = TestResult.testReport + " " + cl.getName() + "." + md.getName();
            TestsReport.addTestResult(testResult);
            TestResult.setResultToZero();
        }
    }

    public static boolean checkMethodSignature(Method method, Class<? extends Annotation> anno) {
        return method.getReturnType().equals(void.class) && method.getModifiers() == Modifier.PUBLIC
                && method.isAnnotationPresent(anno);
    }

}

