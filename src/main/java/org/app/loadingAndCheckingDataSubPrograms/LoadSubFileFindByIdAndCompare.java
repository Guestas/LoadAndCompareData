package org.app.loadingAndCheckingDataSubPrograms;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

public class LoadSubFileFindByIdAndCompare {

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

    public static boolean compareData(List<String> mainHeader, List<String> subHeader, List<String> mainData, List<String> subData){
        return subHeader.stream()
                .allMatch(header -> subData.get(subHeader.indexOf(header))
                        .equals(mainData.get(mainHeader.indexOf(header)))
                );
    }
}
