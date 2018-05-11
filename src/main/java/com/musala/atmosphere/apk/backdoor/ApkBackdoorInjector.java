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

package com.musala.atmosphere.apk.backdoor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.musala.atmosphere.apk.backdoor.exceptions.ApkConfigException;
import com.musala.atmosphere.apk.backdoor.helpers.ApkToolHelper;
import com.musala.atmosphere.apk.backdoor.helpers.ApplicationBuilderHelper;
import com.musala.atmosphere.apk.backdoor.helpers.FileSystemHelper;

/**
 * The base class providing the android application backdooring functionality.
 * 
 * @author boris.strandjev
 * 
 */
public class ApkBackdoorInjector {
    private static final File BACKDOOR_WORKING_DIR = new File("backdooring_tmp_dir");

    private static final File PROPERTIES_FILE_LOCATION = new File("backdoor.properties");

    /** The backdooring is implemented using the decorator design pattern. */
    private ApkBackdoorDecoration decoration;

    private static File keystoreLocation;

    private static String keystorePassword;

    {
        Properties prop = new Properties();
        try (FileInputStream propertiesStream = new FileInputStream(PROPERTIES_FILE_LOCATION)) {
            prop.load(propertiesStream);
            keystoreLocation = new File(prop.getProperty(BackdoorProperties.KEYSTORE_LOCATION.getPropertyKey()));
            keystorePassword = prop.getProperty(BackdoorProperties.KEYSTORE_PASSWORD.getPropertyKey());
        } catch (IOException e) {
            throw new ApkConfigException("Problem loading backdoor injector properties.", e);
        }
    }

    public ApkBackdoorInjector(ApkBackdoorDecoration decoration) {
        this.decoration = decoration;
    }

    /**
     * Adds the selected backdooring decorations to the given apk.
     * <p>
     * <b>!!!Warning!!! Uses {@link #BACKDOOR_WORKING_DIR} as working dir, so any initial contents will be erased. </b>
     * 
     * @param sourceApkPath
     *        - the location of the apk to backdoor
     * @param outputApkPath
     *        - the location at which to record the backdoored apk
     */
    public void backdoorApplication(File sourceApkPath, File outputApkPath) {
        // make sure we work on clean location
        FileSystemHelper.removeFile(BACKDOOR_WORKING_DIR);
        BACKDOOR_WORKING_DIR.mkdirs();

        ApkToolHelper apkToolHelper = new ApkToolHelper();
        File decompiledDirectory = new File(BACKDOOR_WORKING_DIR, "decompiled");
        apkToolHelper.decompileApk(sourceApkPath, decompiledDirectory);

        decoration.applyDecoration(decompiledDirectory);

        apkToolHelper.compileBackSmaliCode(decompiledDirectory, outputApkPath);

        ApplicationBuilderHelper applicationBuilderHelper = new ApplicationBuilderHelper();
        applicationBuilderHelper.signApk(outputApkPath, keystoreLocation, keystorePassword);

        // cleanup after ourselves
        FileSystemHelper.removeFile(BACKDOOR_WORKING_DIR);
    }
}
