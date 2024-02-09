package org.app.loadingAndCheckingDataSubPrograms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadSubFileFindByIdAndCompareTest {

    @Test
    public void testCompareData_MatchingData() {
        List<String> mainHeader = Arrays.asList("ID", "firstname", "lastname", "title", "birthday", "function", "FILE1", "FILE2", "FILE3");
        List<String> subHeader = Arrays.asList("ID", "lastname", "firstname", "birthday");
        List<String> mainData = Arrays.asList("1", "Pavel", "Konečný", "Ing.", "13.4.1981", "správce sítě", "1", "1", "1");
        List<String> subData = Arrays.asList("1", "Konečný", "Pavel", "13.4.1981");

        Assertions.assertTrue(LoadSubFileFindByIdAndCompare.compareData(mainHeader, subHeader, mainData, subData));
    }

    @Test
    public void testCompareData_DifferentData() {
        List<String> mainHeader = Arrays.asList("ID", "firstname", "lastname", "title", "birthday", "function", "FILE1", "FILE2", "FILE3");
        List<String> subHeader = Arrays.asList("ID", "lastname", "firstname", "birthday");
        List<String> mainData = Arrays.asList("1", "Pavel", "Konečný", "Ing.", "13.4.1981", "správce sítě", "1", "1", "1");
        List<String> subData = Arrays.asList("1", "Začáteční", "Jan", "13.4.1981");

        Assertions.assertFalse(LoadSubFileFindByIdAndCompare.compareData(mainHeader, subHeader, mainData, subData));
    }

}