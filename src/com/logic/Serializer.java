package com.logic;

import com.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;

public class Serializer {
    public static boolean serialize(Object object, String filename) {
        boolean state = true;
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(object);

            out.close();
            file.close();
        } catch (Exception e) {
            state = false;
        }

        return state;
    }

    public static Object unserialize(String filename) {
        Object object = null;
        try {
            FileInputStream file = new FileInputStream(filename);

            ObjectInputStream in = new ObjectInputStream(file);
            object = in.readObject();

            in.close();
            file.close();
            Logger.success(String.format("Serializated file '%s' found.", filename));
        } catch (FileNotFoundException e) {
            Logger.warn(String.format("Serializated file '%s' not found. Returning NULL.", filename));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }

        return object;
    }

    static Object unserialize(String filename, int dayAgeLimit) {
        BasicFileAttributes attr = null;
        Path filePath = Paths.get(filename);
        try {
            attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            long creationTime = attr.creationTime().toMillis();
            Calendar cal = Calendar.getInstance();
            int seconds = (int) ((cal.getTimeInMillis()-creationTime)/1000);
            int minutes = seconds / 60; seconds %= 60;
            int hours = minutes / 60; minutes %= 60;
            int days = hours / 24; hours %= 24;

            if (days >= dayAgeLimit) {
                Logger.info(String.format("Days: %d | Hours: %d | Minutes: %d | Seconds: %d", days, hours, minutes, seconds));
                Logger.warn(String.format("File '%s' is too old. Deleting. ", filename), false);
                Logger.info(String.format("Older than %d days.", dayAgeLimit));
                Files.delete(filePath);
            }
        } catch (NoSuchFileException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return unserialize(filename);
    }

    public static void main(String[] args) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get("resources/issues.ser"), BasicFileAttributes.class);
        long creationTime = attr.creationTime().toMillis();
        Calendar cal = null;

        while (true) {
            cal = Calendar.getInstance();
            int seconds = (int) ((cal.getTimeInMillis()-creationTime)/1000);
            int minutes = seconds / 60; seconds %= 60;
            int hours = minutes / 60; minutes %= 60;
            int days = hours / 24; hours %= 24;
            System.out.println(String.format("%d days | %d hours | %d minutes | %d seconds", days, hours, minutes, seconds));
        }
//        System.exit(1);
    }
}
