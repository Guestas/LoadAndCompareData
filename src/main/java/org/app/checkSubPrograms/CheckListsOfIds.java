package org.app.checkSubPrograms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckListsOfIds {

    public static int testAllSubTestsWithSubFileData(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub, String fileToLoadName){
        List<Long> duplicatedValuesInSub = checkListForDuplicates(listOfIdsInSub, fileToLoadName);
        List<Long> missingInSub = compareIdsMissingInSub(listOfIdsFromMain, listOfIdsInSub, fileToLoadName);
        List<Long> redundantInSub = compareIdsRedundantInSub(listOfIdsFromMain, listOfIdsInSub, fileToLoadName);
        boolean arraysAreSame = compareIfValuesAreSame(listOfIdsFromMain,listOfIdsInSub,fileToLoadName);

        int code = (!duplicatedValuesInSub.isEmpty()?1:0) +
                (!missingInSub.isEmpty()?3:0)+
                (!redundantInSub.isEmpty()?5:0);

        String message = "In file: "+ fileToLoadName +
                (!duplicatedValuesInSub.isEmpty()?" Duplicates with IDs : " + duplicatedValuesInSub:" ") +
                (!missingInSub.isEmpty()?" Missing with IDs : " + missingInSub:" ") +
                (!redundantInSub.isEmpty()?" Redundant with IDs: " + redundantInSub:" ") +
                (!arraysAreSame?" ":" IDs corresponding correctly and no duplicates.");

        System.out.println(message);
        return code;
    }

    public static List<Long> checkListForDuplicates(List<Long> listOfIds, String fileToLoadName){
        Set<Long> duplicates = new HashSet<>();
        return listOfIds.stream()
                .filter(id->!duplicates.add(id)).toList();
    }

    public static List<Long> compareIdsMissingInSub(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub, String fileToLoadName){
        return listOfIdsFromMain.stream()
                .filter(value -> !listOfIdsInSub.contains(value))
                .toList();
    }

    public static List<Long> compareIdsRedundantInSub(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub, String fileToLoadName){
        return listOfIdsInSub.stream()
                .filter(value -> !listOfIdsFromMain.contains(value))
                .toList();
    }

    public static boolean compareIfValuesAreSame(List<Long> listOfIdsFromMain, List<Long> listOfIdsInSub, String fileToLoadName){
        return new HashSet<>(listOfIdsFromMain).equals(new HashSet<>(listOfIdsInSub));
    }

}
