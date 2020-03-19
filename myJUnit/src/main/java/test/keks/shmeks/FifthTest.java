package test.keks.shmeks;




import TestAnno.MyJUNIT;

import static TestAnno.TestProcessing.*;

public class FifthTest {

    @MyJUNIT
    public void checkFloatsNotEquals(){
        assertEquals(1.0, 2.4);
    }

    @MyJUNIT
    public void checkEmptyArrayIsNotNull(){
        int mas[] = new int[10];
        assertNotNull(mas);
    }
}
