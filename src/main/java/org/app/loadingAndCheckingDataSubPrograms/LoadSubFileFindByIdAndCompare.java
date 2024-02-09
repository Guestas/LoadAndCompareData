package org.app.loadingAndCheckingDataSubPrograms;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

public class LoadSubFileFindByIdAndCompare {

    /**
     * Finds and compares data in a sub-file with data in the main file.
     *
     * @param mainHeader The header of the main file.
     * @param mainData The data of the main file.
     * @param nameOfSubFile The name of the sub-file.
     * @return A Long value representing the comparison result.
     * If long is -1 it means data not found. If 0 data was found and was same with main. If more than 0 it means data wasn't same.
     */
    public static Long findByIdAndCompare(List<String> mainHeader, List<String> mainData, String nameOfSubFile){
        try (RandomAccessFile file = new RandomAccessFile(nameOfSubFile, "r");) {
            List<String> subHeader = Arrays.stream(file.readLine().split(",")).toList();
            String line;
            while ((line = file.readLine()) != null) {
                List<String> subData = Arrays.stream(line.split(",")).toList();
                if (mainData.get(0).equals(subData.get(0))) {
                    return compareData(mainHeader, subHeader, mainData, subData)?0L:Long.parseLong(subData.get(0));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1L;
    }

    /**
     * Compares data between the main file and the sub-file based on their headers.
     *
     * @param mainHeader The header of the main file.
     * @param subHeader The header of the sub-file.
     * @param mainData The data of the main file.
     * @param subData The data of the sub-file.
     * @return True if all data elements match, false otherwise.
     */
    public static boolean compareData(List<String> mainHeader, List<String> subHeader, List<String> mainData, List<String> subData){
        return subHeader.stream()
                .allMatch(header -> subData.get(subHeader.indexOf(header))
                        .equals(mainData.get(mainHeader.indexOf(header)))
                );
    }
}
