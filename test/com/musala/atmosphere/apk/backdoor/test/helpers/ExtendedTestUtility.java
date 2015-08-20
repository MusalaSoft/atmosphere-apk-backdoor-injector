package com.musala.atmosphere.apk.backdoor.test.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.musala.atmosphere.apk.backdoor.helpers.FileSystemHelper;

/**
 * A helper class for the tests. Contains auxiliary assert functions.
 * 
 * @author boris.strandjev
 */
public class ExtendedTestUtility {
    // The name of the subversion specific directory
    private static final String SUBVERSION_DIRECTORY_NAME = ".svn";

    /**
     * Checks whether the two given files have the same content.
     * 
     * @param message
     *        - the message to display if the check fails
     * @param expectedFile
     *        - the file with the expected content
     * @param actualFile
     *        - the actual file
     */
    public static void assertTextFilesEqual(String message, File expectedFile, File actualFile) throws IOException {
        String expectedFileContents = removeAllWhitespaces(FileSystemHelper.getFileContents(expectedFile));
        String actualFileContents = removeAllWhitespaces(FileSystemHelper.getFileContents(actualFile));
        assertEquals(message, expectedFileContents, actualFileContents);
    }

    /**
     * Checks whether the two given binary files have the same content.
     * 
     * @param message
     *        - the message to display if the check fails
     * @param expectedFile
     *        - the file with the expected content
     * @param actualFile
     *        - the actual file
     */
    public static void assertBinaryFilesEqual(String message, File expectedFile, File actualFile) throws IOException {
        assertTrue("Expected the file to exist.", actualFile.exists());
        assertEquals(message + ": The file expected to be of correct size.", expectedFile.length(), actualFile.length());

        try (BufferedInputStream expectedStream = new BufferedInputStream(new FileInputStream(expectedFile));
                BufferedInputStream actualStream = new BufferedInputStream(new FileInputStream(actualFile))) {
            int nextExpectedByte, nextActualByte;
            int byteIndex = 0;
            while ((nextExpectedByte = expectedStream.read()) > 0) {
                nextActualByte = actualStream.read();
                assertEquals(message + ": The byte at position " + byteIndex + " is expected to be correct.",
                             nextExpectedByte,
                             nextActualByte);
                byteIndex++;
            }
        }
    }

    /**
     * Compares two strings ignoring the whitespaces.
     * 
     * @param message
     *        - the message to display if the check fails
     * @param expectedString
     *        - the expected string
     * @param actualString
     *        - the actual string
     */
    public static void assertStringsIgnoringWhitespace(String message, String expectedString, String actualString) {
        String expected = removeAllWhitespaces(expectedString);
        String actual = removeAllWhitespaces(actualString);
        assertEquals(message, expected, actual);
    }

    /**
     * Compares two directory structures recursively including the file contents.
     * 
     * @param expectedDirectoryPath
     *        - the path to the root of the expected directory structure
     * @param actualDirectoryPath
     *        - the path to root of the directory structure to compare to
     */
    public static void assertDirectoriesEqual(String expectedDirectoryPath, String actualDirectoryPath)
        throws IOException {
        assertDirectoriesEqual(new File(expectedDirectoryPath), new File(actualDirectoryPath));
    }

    /**
     * Compares two directory structures recursively including the file contents.
     * 
     * @param expectedDirectory
     *        - the root of the expected directory structure
     * @param actualDirectory
     *        - the root of the directory structure to compare to
     */
    public static void assertDirectoriesEqual(File expectedDirectory, File actualDirectory) throws IOException {
        compareDirectories(expectedDirectory, actualDirectory, "");
    }

    /** Removes all whitespace symbols from the given string. */
    private static String removeAllWhitespaces(String string) {
        // drop the byte order mark char, it exists because of file saving
        if (!string.isEmpty() && string.charAt(0) == '\uFEFF') {
            string = string.substring(1);
        }
        return string.replaceAll("[\\s, \\n, \\r, \\t]+", "");
    }

    /**
     * Compares two directory structures recursively including the file contents.
     * 
     * @param expectedDirectory
     *        - the root of the expected directory structure
     * @param actualDirectory
     *        - the root of the directory structure to compare to
     * @param relativePath
     *        - the relative path in the directory structure which is being checked now (used for logging purposes)
     */
    private static void compareDirectories(File expectedDirectory, File actualDirectory, String relativePath)
        throws IOException {
        File[] expectedFiles = expectedDirectory.listFiles();
        File[] actualFiles = actualDirectory.listFiles();
        int genFileIndex = 0;
        assertEquals("Expected the folders to contain the same number of files at location " + expectedDirectory,
                     expectedFiles.length,
                     actualFiles.length);
        for (int expectedFileIdx = 0, actualFileIdx = 0; expectedFileIdx < expectedFiles.length; expectedFileIdx++, actualFileIdx++) {
            genFileIndex++;
            if (expectedFiles[expectedFileIdx].getName().equals(SUBVERSION_DIRECTORY_NAME)) {
                actualFileIdx--; // Ignoring the svn files
                genFileIndex--;
                continue;
            }
            assertEquals("The file/directory names do not coincide.",
                         relativePath + expectedFiles[expectedFileIdx].getName(),
                         relativePath + actualFiles[actualFileIdx].getName());
            if (expectedFiles[expectedFileIdx].isDirectory()) {
                assertTrue("Expected directory, got file.", actualFiles[actualFileIdx].isDirectory());
                compareDirectories(expectedFiles[expectedFileIdx], actualFiles[actualFileIdx], relativePath
                        + File.separator + expectedFiles[expectedFileIdx].getName());
            } else {
                assertTrue("Expected file, got directory.", actualFiles[actualFileIdx].isFile());
                String assertFilesMessage = "The contents of file " + relativePath + File.separator
                        + expectedFiles[expectedFileIdx].getName() + " is not as expected.";
                ExtendedTestUtility.assertTextFilesEqual(assertFilesMessage,
                                                         expectedFiles[expectedFileIdx],
                                                         actualFiles[actualFileIdx]);
            }
        }

        assertEquals("There are spurious files in the generated structure.",
                     genFileIndex,
                     actualDirectory.listFiles().length);
    }
}
