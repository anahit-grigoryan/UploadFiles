package src;

import src.service.CloudMigration;

import java.io.*;

public class Application {

    public static void main(String[] args) {

        CloudMigration migrationService = new CloudMigration();
        try {
            migrationService.run();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
