package com.musala.atmosphere.apk.backdoor.helpers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Class containing auxiliary methods for operating with the file system.
 * 
 * @author boris.strandjev
 */
public class FileSystemHelper {
    /**
     * Reads the contents of the given file in a string.
     * 
     * @param filePath
     *        - the path to the file which contents are to be fetched
     * @return The string content of the file
     */
    public static String getFileContents(String filePath) throws IOException {
        return getFileContents(new File(filePath));
    }

    /**
     * Reads the contents of the given file in a string.
     * 
     * @param file
     *        - the file which contents are to be fetched
     * @return The string content of the file
     */
    public static String getFileContents(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }

    /**
     * Erases from the file system the given file. If it is a directory erases recursively.
     * 
     * @param filePath
     *        - the path to the file to erase
     */
    public static void removeFile(String filePath) {
        removeFile(new File(filePath));
    }

    /**
     * Erases from the file system the given file. If it is a directory erases recursively.
     * 
     * @param file
     *        - the file to erase
     */
    public static void removeFile(File file) {
        FileUtils.deleteQuietly(file);
    }

    /**
     * Copies a file to designated location.
     * 
     * @param sourcePath
     *        - the path of the file which to copy
     * @param targetPath
     *        - the location in which to copy the file to
     * @throws IOException
     *         If a read / write problem occurs
     */
    public static void copyFile(File sourcePath, File targetPath) throws IOException {
        FileUtils.copyFile(sourcePath, targetPath);
    }

    /**
     * Deep copies a directory to designated location.
     * 
     * @param sourceDirectory
     *        - the location of the directory which to copy
     * @param targetDirectory
     *        - the location in which to copy the directory to
     * @throws IOException
     *         If a read / write problem occurs
     */
    public static void copyDirectory(File sourceDirectory, File targetDirectory) throws IOException {
        FileUtils.copyDirectory(sourceDirectory, targetDirectory);
    }
}
