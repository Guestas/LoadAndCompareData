package org.app.checkSubPrograms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckListsOfIds {

    /**
     * Tests all subtests with sub-file data by comparing IDs from the main file and sub-file.
     *
     * @param listOfIdsFromMain The list of IDs from the main file.
     * @param listOfIdsInSub The list of IDs from the sub-file.
     * @param fileToLoadName The name of the file being tested.
     * @return An integer code representing the test result.
     */
    public static int testAllSubTestsWithSubFileData(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub, String fileToLoadName){
        List<Long> duplicatedValuesInSub = checkListForDuplicates(listOfIdsInSub);
        List<Long> missingInSub = compareIdsMissingInSub(listOfIdsFromMain, listOfIdsInSub);
        List<Long> redundantInSub = compareIdsRedundantInSub(listOfIdsFromMain, listOfIdsInSub);
        boolean arraysAreSame = compareIfValuesAreSame(listOfIdsFromMain,listOfIdsInSub);

        int code = (!duplicatedValuesInSub.isEmpty()?1:0) +
                (!missingInSub.isEmpty()?3:0)+
                (!redundantInSub.isEmpty()?5:0);

        String message = "In file: "+ fileToLoadName +
                (!duplicatedValuesInSub.isEmpty()?" Duplicated IDs : " + duplicatedValuesInSub:"") +
                (!missingInSub.isEmpty()?" Missing IDs : " + missingInSub:"") +
                (!redundantInSub.isEmpty()?" Redundant IDs: " + redundantInSub:"");
        if (!duplicatedValuesInSub.isEmpty()||!missingInSub.isEmpty()||!redundantInSub.isEmpty())
            System.out.println(message);
        return code;
    }

    /**
     * Checks a list of IDs for duplicates.
     *
     * @param listOfIds The list of IDs to check for duplicates.
     * @return A list containing the duplicate IDs found.
     */
    public static List<Long> checkListForDuplicates(List<Long> listOfIds){
        Set<Long> duplicates = new HashSet<>();
        return listOfIds.stream()
                .filter(id->!duplicates.add(id)).toList();
    }

    /**
     * Compares IDs from the main file with IDs from the sub-file to find missing IDs in the sub-file.
     *
     * @param listOfIdsFromMain The list of IDs from the main file.
     * @param listOfIdsInSub The list of IDs from the sub-file.
     * @return A list containing IDs that are present in the main file but missing in the sub-file.
     */
    public static List<Long> compareIdsMissingInSub(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub){
        return listOfIdsFromMain.stream()
                .filter(value -> !listOfIdsInSub.contains(value))
                .toList();
    }

    /**
     * Compares IDs from the sub-file with IDs from the main file to find redundant IDs in the sub-file.
     *
     * @param listOfIdsFromMain The list of IDs from the main file.
     * @param listOfIdsInSub The list of IDs from the sub-file.
     * @return A list containing IDs that are present in the sub-file but redundant in the main file.
     */
    public static List<Long> compareIdsRedundantInSub(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub){
        return listOfIdsInSub.stream()
                .filter(value -> !listOfIdsFromMain.contains(value))
                .toList();
    }

    /**
     * Compares if the values in two lists of IDs are the same.
     *
     * @param listOfIdsFromMain The list of IDs from the main file.
     * @param listOfIdsInSub The list of IDs from the sub-file.
     * @return True if the values in both lists are the same, false otherwise.
     */
    public static boolean compareIfValuesAreSame(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub){
        return new HashSet<>(listOfIdsFromMain).equals(new HashSet<>(listOfIdsInSub));
    }

}
