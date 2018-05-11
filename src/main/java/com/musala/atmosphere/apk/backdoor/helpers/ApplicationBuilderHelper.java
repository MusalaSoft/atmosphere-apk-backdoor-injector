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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.musala.atmosphere.apk.backdoor.exceptions.ApkConfigException;

/**
 * Provides methods facilitating the building of Android applications.
 * 
 * @author boris.strandjev
 */
public class ApplicationBuilderHelper {
    private static final String PROPERTIES_FILE_LOCATION = "local.properties";

    private static final String JARSIGNER_COMMAND = "%s -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore %s %s atmosphere -storepass %s";

    private static final String ANDROID_UPDATE_PROJECT_COMMAND = "%s update project --name %s --path %s";

    // FIXME: This command will work only on windows OS
    private static final String BUILD_PROJECT_COMMAND = "%s  -buildfile \"%s" + File.separator
            + "build.xml\" release -Dout.packaged.file=\"%s\" -Daapt \"%s" + File.separator + "aapt\" -Ddx \"%s"
            + File.separator + "dx.bat\"";

    private static String jarsignerLocation;

    private static String antLocation;

    private static String androidLocation;

    private static String buildToolsLocation;

    {
        Properties prop = new Properties();
        try (FileInputStream propertiesStream = new FileInputStream(PROPERTIES_FILE_LOCATION)) {
            prop.load(propertiesStream);
            androidLocation = prop.getProperty("android.executable.path");
            jarsignerLocation = prop.getProperty("jarsigner.path");
            antLocation = prop.getProperty("ant.executable.path");
            buildToolsLocation = prop.getProperty("android.build.tools.path");
        } catch (IOException e) {
            throw new ApkConfigException("Problem loading application builder helper properties", e);
        }
    }

    /**
     * Compiles an android project into an unsigned apk.
     * 
     * @param projectBaseDir
     *        - a directory containing the folder of the project to build. All needed resources for android application
     *        should exist on the specified location: AndroidManifest.xml, src folder etc in this folder of the project
     * @param projectName
     *        - the name of the project to build
     * @param apkPath
     *        - the location at which to create the apk
     */
    public void compileApplication(File projectBaseDir, String projectName, File apkPath) {
        File projectPath = new File(projectBaseDir, projectName);
        if (!projectPath.exists()) {
            throw new ApkConfigException("The project to build can not be found at location: " + projectPath);
        }
        String updateProjectCommand = String.format(ANDROID_UPDATE_PROJECT_COMMAND,
                                                    androidLocation,
                                                    projectName,
                                                    projectPath.getAbsolutePath());
        executeSystemCommand(updateProjectCommand, "Android update project");

        String buildProjectCommand = String.format(BUILD_PROJECT_COMMAND,
                                                   antLocation,
                                                   projectPath.getAbsolutePath(),
                                                   apkPath.getAbsolutePath(),
                                                   buildToolsLocation,
                                                   buildToolsLocation);
        executeSystemCommand(buildProjectCommand, "Build project");
    }

    /**
     * Signs the given unsigned APK with the given keystore. The signed apk will replace the unsigned one.
     * 
     * @param unsignedApkPath
     *        - the apk to sign
     * @param keystorePath
     *        - the path to the keystore to use for the signing
     * @param password
     *        - the password of the keystore
     * @throws ApkConfigException
     *         in case of a problem
     */
    public void signApk(File unsignedApkPath, File keystorePath, String password) {
        String jarsignerCommandString = String.format(JARSIGNER_COMMAND,
                                                      jarsignerLocation,
                                                      keystorePath.getAbsolutePath(),
                                                      unsignedApkPath.getAbsolutePath(),
                                                      password);
        executeSystemCommand(jarsignerCommandString, "Apk signing process");
    }

    private void executeSystemCommand(String command, String processLabel) {
        System.out.println("<ApplicationBuilderHelper> Executing command: " + command);
        String line;
        try {
            Process commandProcess = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(commandProcess.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    System.out.println("<ApplicationBuilderHelper> System command output: " + line);
                }
            }
            commandProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new ApkConfigException(String.format("Exception in process: '%s'.", processLabel), e);
        }
    }
}
