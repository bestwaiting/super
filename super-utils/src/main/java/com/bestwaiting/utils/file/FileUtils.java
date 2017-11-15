package com.bestwaiting.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by bestwaiting on 17/5/13.
 */
public class FileUtils {

    static OutputStreamWriter outputStreamWriter = null;

    public static void createFile(String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName), true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.flush();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void writeFile(String content) {
        try {
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void closeFile() {
        try {
            outputStreamWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
