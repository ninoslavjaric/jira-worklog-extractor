package com.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileSystem {
    public static void save(String path, String content) {
        try {
            Files.write(Paths.get(path), Arrays.asList(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String load(String path) {
        List<String> lines;
        String output = "";

        try {
            lines = Files.readAllLines(Paths.get(path));

            for (String line :
                    lines) {
                output = output.concat("\n" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            output = null;
        }

        return ((output == null) ? null : output.trim());
    }

    public static boolean delete(String path) {
        boolean status = true;
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }
}
