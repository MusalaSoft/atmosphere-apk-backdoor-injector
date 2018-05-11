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

import brut.androlib.Androlib;
import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.androlib.ApkOptions;
import brut.common.BrutException;
import brut.directory.DirectoryException;

import com.musala.atmosphere.apk.backdoor.exceptions.ApkConfigException;

/**
 * A class providing methods for interaction with apk tool.
 * 
 * @author boris.strandjev
 */
public class ApkToolHelper {

    /**
     * Decompiles the apk in the specified folder.
     * <p>
     * If the given folder does not exist - the method creates it.
     * 
     * @param apkPath
     *        - the path of the apk to decompile
     * @param targetFolder
     *        - the folder in which the apk is to be decompiled
     *        <p>
     *        <b>!!!Warning!!! make sure nothing important exists in this folder, because its contents are going to be
     *        erased in the process of decompilation</b>.
     * 
     * @throws ApkConfigException
     *         in case of a problem
     */
    public void decompileApk(File apkPath, File targetFolder) {
        if (targetFolder.exists()) {
            FileSystemHelper.removeFile(targetFolder);
        }
        targetFolder.getAbsoluteFile().getParentFile().mkdirs();

        try {
            ApkDecoder decoder = new ApkDecoder();
            decoder.setOutDir(targetFolder);
            decoder.setApkFile(apkPath);
            decoder.decode();
        } catch (DirectoryException e) {
            throw new ApkConfigException("Problem writing to decompilation target dir.", e);
        } catch (IOException e) {
            throw new ApkConfigException("IO problem decompiling apk.", e);
        } catch (AndrolibException e) {
            throw new ApkConfigException("Problem decompiling apk.", e);
        }
    }

    /**
     * Compiles back to apk Smali code decompiled with apk tool.
     * 
     * @param rootDecompileFolder
     *        - the root folder of the decompiled code (the target folder of the preceding decompilation)
     * @param targetApkLocation
     *        - the location at which to place the resulting apk file (including <apk_name>.apk)
     * @throws ApkConfigException
     *         in case of a problem
     */
    public void compileBackSmaliCode(File rootDecompileFolder, File targetApkLocation) {
        try {
            new Androlib(new ApkOptions()).build(rootDecompileFolder, targetApkLocation);
        } catch (BrutException e) {
            throw new ApkConfigException("Problem comipling decompiled smali code.", e);
        }
    }

}
