package org.app.fileTests;

import org.app.checkSubPrograms.CheckListsOfIds;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class TestsTest {


    @Test
    void checkListForDuplicates() {
        List<Long> list = Arrays.asList(1L, 1L, 3L, 4L, 5L, 4L);
        List<Long> expected = Arrays.asList(1L, 4L);

        List<Long> out = CheckListsOfIds.checkListForDuplicates(list, "main file");
        Assertions.assertNotNull(out);
        Assertions.assertEquals(expected, out);
    }


    @Test
    void checkForMissingIdsInSubFile(){
        List<Long> main = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L, 6L, 7L);

        List<Long> expected = Arrays.asList(1L, 2L);

        List<Long> out = CheckListsOfIds.compareIdsMissingInSub(main, sub, "sub file");

        Assertions.assertNotNull(out);
        Assertions.assertEquals(expected, out);
    }

    @Test
    void checkForRedundantIdsInSubFile(){
        List<Long> main = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L, 6L, 7L);

        List<Long> expected = Arrays.asList(6L, 7L);

        List<Long> out = CheckListsOfIds.compareIdsRedundantInSub(main, sub, "sub file");

        Assertions.assertNotNull(out);
        Assertions.assertEquals(expected, out);
    }

    @Test
    void checkForCorrectIds(){
        List<Long> main = Arrays.asList(3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L);

        boolean out = CheckListsOfIds.compareIfValuesAreSame(main, sub, "sub file");

        Assertions.assertTrue(out);
    }

    @Test
    void checkAllTests0(){
        List<Long> main = Arrays.asList(3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(0, out);
    }

    @Test
    void checkAllTests1(){
        List<Long> main = Arrays.asList(3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L, 5L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(1, out);
    }

    @Test
    void checkAllTests2(){
        List<Long> main = Arrays.asList(3L, 4L, 5L);
        List<Long> sub = Arrays.asList(3L, 4L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(3, out);
    }

    @Test
    void checkAllTests3(){
        List<Long> main = Arrays.asList(3L, 4L);
        List<Long> sub = Arrays.asList(3L, 4L, 5L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(5, out);
    }

    @Test
    void checkAllTests4(){
        List<Long> main = Arrays.asList(3L, 4L);
        List<Long> sub = Arrays.asList(3L, 5L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(8, out);
    }

    @Test
    void checkAllTests5(){
        List<Long> main = Arrays.asList(3L, 4L);
        List<Long> sub = Arrays.asList(3L, 5L, 3L);

        int out = CheckListsOfIds.testAllSubTestsWithSubFileData(main, sub, "sub file");

        Assertions.assertEquals(9, out);
    }
}