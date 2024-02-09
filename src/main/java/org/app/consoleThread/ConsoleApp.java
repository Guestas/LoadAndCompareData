package org.app.consoleThread;

import org.app.loadingAndCheckingDataSubPrograms.LoadMainFileThread;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ConsoleApp {
    private volatile boolean running = true;
    private Long periodTime = null;
    private String mainFile = null;
    private String[] subFilesNames = null;

    /**
     * Constructor for the ConsoleApp class.
     * It initializes the ConsoleApp object by loading configuration from properties file.
     * If an IOException occurs during file loading, it prints the stack trace and returns.
     */
    public ConsoleApp() {
        //loading from properties
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.periodTime = Long.parseLong(properties.getProperty("periodTime"));
        this.mainFile = properties.getProperty("mainFile");
        this.subFilesNames = properties.getProperty("subFiles").split(",");
    }

    /**
     * Starts the ConsoleApp by scheduling periodic program execution and listening for stop command.
     * The program runs periodically according to the specified periodTime.
     * It listens for console input for the "stop" command to finish task and terminate the program.
     */
    public void start() {
        // periodical program run
        Timer timer = getTimer(mainFile, subFilesNames, periodTime);

        // Listen for console input for stop command
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                running = false;
                System.out.println("Stopping...");
                timer.cancel();
            }
        }
    }

    /**
     * Creates a timer that periodically loads the main file and its associated sub-files and make checks.
     *
     * @param mainFile The name of the main file to load.
     * @param subFilesNames An array of sub-file names associated with the main file.
     * @param periodTime The period between each execution of the timer task, in seconds.
     * @return A Timer object for scheduling the task.
     */
    private static Timer getTimer(String mainFile, String[] subFilesNames, Long periodTime) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    LoadMainFileThread.loadFileAndWait(mainFile, subFilesNames);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, periodTime *1000);
        return timer;
    }

}
