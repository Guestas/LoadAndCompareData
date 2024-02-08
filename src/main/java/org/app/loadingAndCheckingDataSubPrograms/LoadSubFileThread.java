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

    public static void loadFileWithoutWaitingToFinish(String nameOfFileToLoad, List<Long> idsInMinFileList){
        LoadSubFileThread file = new LoadSubFileThread(nameOfFileToLoad, idsInMinFileList);
        Thread thread = new Thread(file);
        thread.start();
    }

    @Override
    public void run() {
        List<Long> idInFileList = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(nameOfFileToLoad, "r");) {

            String line;
            while ((line = file.readLine()) != null) {
                try{
                    idInFileList.add(Long.valueOf(line.split(",")[0]));
                } catch (NumberFormatException e){
                    //System.out.println(e);
                }
            }
            CheckListsOfIds.testAllSubTestsWithSubFileData(idsInMinFileList, idInFileList, nameOfFileToLoad);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
