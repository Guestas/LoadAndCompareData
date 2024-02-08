package org.app;

import org.app.consoleThread.ConsoleApp;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}