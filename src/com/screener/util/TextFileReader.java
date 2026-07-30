package com.screener.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * File I/O Utility class for reading plain text (.txt) resume files.
 * 
 * Uses standard JDK BufferedReader for thread-safe, buffered character-stream reading
 * with explicit resource cleanup and exception handling.
 */
public class TextFileReader {

    /**
     * Reads a single plain text (.txt) file from disk and returns its contents as a String.
     * 
     * @param file Target File object to read
     * @return Complete text content of the file
     * @throws IOException If an I/O error occurs or the file is invalid
     */
    public String readTextFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new IOException("Invalid file reference provided.");
        }

        StringBuilder content = new StringBuilder();

        // Use try-with-resources to guarantee automatic closure of BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }

        return content.toString();
    }

    /**
     * Reads a single plain text (.txt) file given its absolute file path.
     * 
     * @param filePath Absolute path to the file
     * @return Complete text content of the file
     * @throws IOException If an I/O error occurs or file path is invalid
     */
    public String readTextFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IOException("File path cannot be null or empty.");
        }
        return readTextFile(new File(filePath));
    }

    /**
     * Reads all plain text (.txt) files within a specified directory.
     * 
     * @param directoryPath Path to directory containing .txt resume files
     * @return List of String arrays where index 0 is the filename and index 1 is the file content
     */
    public List<String[]> readAllTextFilesFromDirectory(String directoryPath) {
        List<String[]> fileContentsList = new ArrayList<>();

        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            System.err.println("Directory path is null or empty.");
            return fileContentsList;
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Specified path is not a valid directory: " + directoryPath);
            return fileContentsList;
        }

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.err.println("No .txt files found in directory: " + directoryPath);
            return fileContentsList;
        }

        for (File file : files) {
            try {
                String content = readTextFile(file);
                fileContentsList.add(new String[]{file.getName(), content});
            } catch (IOException e) {
                System.err.println("Error reading file: " + file.getName() + " - " + e.getMessage());
            }
        }

        return fileContentsList;
    }
}
