package org.app.loadingAndCheckingDataSubPrograms;

import org.app.checkSubPrograms.CheckListsOfIds;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class LoadSubFileThread implements Runnable {
    private final String nameOfFileToLoad;
    private final List<Long> idsInMinFileList; 
    public LoadSubFileThread(String nameOfFileToLoad){
        this.nameOfFileToLoad = nameOfFileToLoad;
        this.idsInMinFileList = new ArrayList<>();
    }

    public LoadSubFileThread(String nameOfFileToLoad, List<Long> idsInMinFileList) {
        this.nameOfFileToLoad = nameOfFileToLoad;
        this.idsInMinFileList = idsInMinFileList;
    }

    /**
     * Thread loads a file without waiting for it to finish loading.
     *
     * @param nameOfFileToLoad The name of the file to load.
     * @param idsInMinFileList The list of IDs associated with the file.
     */
    public static void loadFileWithoutWaitingToFinish(String nameOfFileToLoad, List<Long> idsInMinFileList){
        LoadSubFileThread file = new LoadSubFileThread(nameOfFileToLoad, idsInMinFileList);
        Thread thread = new Thread(file);
        thread.start();
    }

    /**
     * Runs the thread to load IDs from file and compare it with Array of IDs from main file. Without checking data.
     */
    @Override
    public void run() {
        List<Long> idInFileList = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile(nameOfFileToLoad, "r")) {
            //skip first line
            file.readLine();
            String line;
            while ((line = file.readLine()) != null) {
                try{
                    idInFileList.add(Long.parseLong(line.split(",")[0]));
                } catch (RuntimeException e){
                    System.out.println(e);
                }
            }
            file.close();
            CheckListsOfIds.testAllSubTestsWithSubFileData(idsInMinFileList, idInFileList, nameOfFileToLoad);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
