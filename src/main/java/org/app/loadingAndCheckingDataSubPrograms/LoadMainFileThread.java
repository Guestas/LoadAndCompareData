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

    /**
     * Loads a main file and waits for it to finish loading before proceeding sub files.
     *
     * @param nameOfFileToLoad The name of the main file to load.
     * @param subFilesNames An array of sub-file names associated with the main file.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
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
            // Read each line from the file
            while ((line = file.readLine()) != null) {
                // Split the line into data elements
                List<String> splited = Arrays.stream(line.split(",")).toList();
                // Add the ID to the list
                idInFileList.add(Long.valueOf(splited.get(0)));

                // Iterate over positionsOfFilesInCsv to add IDs to corresponding lists for later check and compare data with sub-files
                for (Map.Entry<String, Integer> entry : positionsOfFilesInCsv.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();

                    // If the value at the position indicates a match ("1") that mens data should be in sub file, add the ID to the corresponding list and check data
                    if (Objects.equals(splited.get(value), "1")){
                        addToListByKey(key, Long.valueOf(splited.get(0)));

                        // Determine the index of the sub-file and its name of file by this index for load
                        int subFileIndex = Integer.parseInt(key.replaceAll("[^0-9]", "")) - 1;
                        String subFileName = subFilesNames[subFileIndex];

                        // Find and compare data from the main file with the sub-file
                        Long result = LoadSubFileFindByIdAndCompare.findByIdAndCompare(headers, splited, subFileName);
                        if (result > 0L)
                            System.out.println("In file: "+ subFileName +" different values in comparison with main file, ID: " + splited.get(0));
                    }
                }
            }
            file.close();

            // Check for duplicates in the list of IDs from the main file with sub file ids which will be loaded
            List<Long> duplicates = CheckListsOfIds.checkListForDuplicates(idInFileList);

            // Print messages based on the presence of duplicates
            if (!duplicates.isEmpty()) {
                System.out.println("Duplicates in " + nameOfFileToLoad + " with IDs: " + duplicates);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a value to a list associated with a specific key in the map. Key correspond with sub files.
     *
     * @param key The key to associate the value with.
     * @param value The value to add to the list.
     */
    private void addToListByKey(String key, Long value) {
        // Retrieve the list associated with the key
        List<Long> list = personsIdsInSubFiles.get(key);

        // Add the value to the list
        list.add(value);

        // Put the updated list back into the map
        personsIdsInSubFiles.put(key, list);
    }
}
