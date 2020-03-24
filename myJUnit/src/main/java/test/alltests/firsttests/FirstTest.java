package test.alltests.firsttests;

import test.anno.MyJUNIT;

import java.util.ArrayList;
import java.util.List;

import static test.tools.Assertion.*;

public class FirstTest {

    @MyJUNIT
    public void testListEquals() {
        List arrayList1 = new ArrayList();
        arrayList1.add(2);
        arrayList1.add(5);
        arrayList1.add(7);

        List arrayList2 = new ArrayList();
        arrayList2.add(2);
        arrayList2.add(5);
        arrayList2.add(7);

        assertEquals(arrayList1, arrayList2);

    }

    @MyJUNIT
    public void testListNoEquals() {
        List arrayList1 = new ArrayList();
        arrayList1.add(2);
        arrayList1.add(5);
        arrayList1.add(7);

        List arrayList2 = new ArrayList();
        arrayList2.add(2);
        arrayList2.add(5);

        assertEquals(arrayList1, arrayList2);

    }

    @MyJUNIT
    public void testNumbersEquals() {
        int k = 5 + 3;
        int d = 8;

        assertEquals(k, d);
    }

    @MyJUNIT
    public void testNumbersNoEquals() {
        int k = 5 + 3;
        int d = 8;

        assertEquals(k, 20);
    }

    @MyJUNIT
    public void testOneNumberGreaterThanAnother() {
        assertTrue(2 > 7);
    }

    @MyJUNIT
    public void testOneNumberLessThanAnother() {
        int k = 7 + 3;
        int d = 8;

        assertTrue(d < k);
    }

    @MyJUNIT
    public void testNumberIsNotNull() {
        assertNotNull(3);
    }


}
