import TestAnno.After;
import TestAnno.Before;
import TestAnno.MyJUNIT;
import TestAnno.TestProcessing;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        CustomJUnitRunTests("test");

    }

    public static void CustomJUnitRunTests(String packageWithTestClasses) throws Exception {
        Reflections reflections = new Reflections("test", new SubTypesScanner(false));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        for (Class<?> someClass : classes) {
            if (someClass.getSimpleName().endsWith("Test")) {
                Object newInstance = someClass.getDeclaredConstructor().newInstance();
                launchMethods(newInstance, someClass, Before.class);
                launchMethods(newInstance, someClass, MyJUNIT.class);
                launchMethods(newInstance, someClass, After.class);
            }
        }
        TestProcessing.countTest = TestProcessing.countFailedTests + TestProcessing.countSuccessTests;
        System.out.println("\n---------------------\nAll tests amount = " + TestProcessing.countTest);
        System.out.println("Passed = " + TestProcessing.countSuccessTests);
        System.out.println("Failed = " + TestProcessing.countFailedTests);
    }

    public static void launchMethods(Object ob, Class someClass, Class<? extends Annotation> anno) throws Exception {
        Class cl = Class.forName(someClass.getName());
        Method[] methods = cl.getMethods();
        for (Method md : methods) {
            if (checkMethodSignature(md, anno)) {
                int currentTestProcessing = TestProcessing.countSuccessTests;
                    md.invoke(ob, null);
                    // Условие для случая когда в тесте отсуствуют ассерты
                    if (!TestProcessing.isTestProcessing && md.isAnnotationPresent(MyJUNIT.class)) {
                        TestProcessing.countSuccessTests++;
                    }
                    // Если изменилось количество пройденных тестов
                    if (md.isAnnotationPresent(MyJUNIT.class)) {
                        if (currentTestProcessing == TestProcessing.countSuccessTests - 1) {
                            System.out.println("Test success " + cl.getName() + "." + md.getName()+"\n");
                        } else {
                            System.out.println("Test failed " + cl.getName() + "." + md.getName()+"\n");
                        }
                    }
                    TestProcessing.isTestProcessing = false;
            }
        }
    }

    public static boolean checkMethodSignature(Method method, Class<? extends Annotation> anno) {
        return method.getReturnType().equals(void.class) && method.getModifiers() == Modifier.PUBLIC
                && method.isAnnotationPresent(anno);
    }
}

