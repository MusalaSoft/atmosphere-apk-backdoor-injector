// This file is part of the ATMOSPHERE mobile testing framework.
// Copyright (C) 2016 MusalaSoft
//
// ATMOSPHERE is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// ATMOSPHERE is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with ATMOSPHERE.  If not, see <http://www.gnu.org/licenses/>.

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
