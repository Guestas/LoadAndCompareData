package org.app.loadingAndCheckingDataSubPrograms;

import org.app.checkSubPrograms.CheckListsOfIds;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class LoadMainFileThread implements Runnable {
    private final String nameOfFileToLoad;
    private final String[] subFilesNames;
    private final List<Long> idInFileList = new ArrayList<>();
    private final Map<String, List<Long>> personsIdsInSubFiles = new HashMap<>();
    private List<String> headers = new ArrayList<>();
    
    public LoadMainFileThread(String nameOfFileToLoad, String[] subFilesNames){
        this.nameOfFileToLoad = nameOfFileToLoad;
        this.subFilesNames = subFilesNames;
    }

    public static void loadFileAndWait(String nameOfFileToLoad, String[] subFilesNames) throws InterruptedException {
        System.out.println("\n".repeat(2));
        LoadMainFileThread file = new LoadMainFileThread(nameOfFileToLoad, subFilesNames);
        Thread thread = new Thread(file);
        thread.start();
        thread.join();

        // main file list of ids loaded
        int i = 1;
        for (String subFileName: subFilesNames){
            LoadSubFileThread.loadFileWithoutWaitingToFinish(subFileName, file.getPersonsIdsInSubFiles().get("FILE"+i++));
        }

    }

    public Map<String, List<Long>> getPersonsIdsInSubFiles() {
        return personsIdsInSubFiles;
    }

    public List<Long> getIdInFileList() {
        return idInFileList;
    }

    @Override
    public void run() {
        try (RandomAccessFile file = new RandomAccessFile(nameOfFileToLoad, "r")) {
            headers = Arrays.stream(file.readLine().split(",")).toList();
            Map<String, Integer> positionsOfFilesInCsv = new HashMap<>();
            
            headers.stream()
                    .filter(string -> string.startsWith("FILE"))
                    .toList()
                    .forEach(string -> {
                        personsIdsInSubFiles.put(string, new ArrayList<>());
                        positionsOfFilesInCsv.put(string, headers.indexOf(string));
                    });

            String line;
            while ((line = file.readLine()) != null) {
                List<String> splited = Arrays.stream(line.split(",")).toList();
                idInFileList.add(Long.valueOf(splited.get(0)));

                // going through all columns by key like FILE1, FILE2, ... and add ids to arraylist
                // for comparing later in code
                for (Map.Entry<String, Integer> entry : positionsOfFilesInCsv.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (Objects.equals(splited.get(value), "1")){
                        addToListByKey(key, Long.valueOf(splited.get(0)));

                        int subFileIndex = Integer.parseInt(key.replaceAll("[^0-9]", "")) - 1;
                        String subFileName = subFilesNames[subFileIndex];

                        Long result = LoadSubFileFindByIdAndCompare.findByIdAndCompare(headers, splited, subFileName);
                        if (result > 0L)
                            System.out.println("Values not same: " + subFileName + " sub ID: " + splited.get(0));
                        else if (result == -1L)
                            System.out.println("Values missing in: " + subFileName + " sub ID: " + splited.get(0));
                    }
                }

            }

            List<Long> duplicates = CheckListsOfIds.checkListForDuplicates(idInFileList, nameOfFileToLoad);

            if (duplicates.isEmpty()) {
                System.out.println("No duplicates in " + nameOfFileToLoad);
            } else {
                System.out.println("Duplicates in " + nameOfFileToLoad + " with IDs: " + duplicates);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void addToListByKey(String key, Long value) {
        // Retrieve the list associated with the key
        List<Long> list = personsIdsInSubFiles.getOrDefault(key, new ArrayList<>());

        // Add the value to the list
        list.add(value);

        // Put the updated list back into the map
        personsIdsInSubFiles.put(key, list);
    }
}
