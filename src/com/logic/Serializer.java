package com.logic;

import com.Logger;

import java.io.*;
import java.util.List;

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
//        this.issues = (List<String>) issues;
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
}
