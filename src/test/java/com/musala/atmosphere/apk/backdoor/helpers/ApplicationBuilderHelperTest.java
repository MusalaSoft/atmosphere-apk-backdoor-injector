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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

/**
 * Tests see "{@link ApplicationBuilderHelper}.
 *
 * @author boris.strandjev
 */
public class ApplicationBuilderHelperTest {

    private static final String FIXTURE_LOCATION = "fixtures/builder_helper/";

    private static final File TMP_FOLDER = new File("test_application_builder_tmp");

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testBuildApk() throws IOException {
        File sourceFolder = new File(FIXTURE_LOCATION, "build_test/source_dir");
        FileSystemHelper.copyDirectory(sourceFolder, TMP_FOLDER);

        ApplicationBuilderHelper buildHelper = new ApplicationBuilderHelper();
        File targetApkPath = new File(TMP_FOLDER, "build.apk");
        buildHelper.compileApplication(TMP_FOLDER, "ApplicationOnlyProject", targetApkPath);
        File expectedSignedApk = new File(FIXTURE_LOCATION, "build_test/build.apk");
        ExtendedTestUtility.assertContainsFile("Expected correct apk to be built.", expectedSignedApk, targetApkPath);
    }

    @Test
    public void testSignApk() throws IOException {
        File apkToSignBase = new File(FIXTURE_LOCATION, "sign_tests/unsigned.apk");
        TMP_FOLDER.mkdir();
        File apkToSign = new File(TMP_FOLDER, "unsigned.apk");
        FileSystemHelper.copyFile(apkToSignBase, apkToSign);
        File keystorePath = new File(FIXTURE_LOCATION, "sign_tests/resign.keystore");
        String keystorePassword = "Passw0rd";

        ApplicationBuilderHelper buildHelper = new ApplicationBuilderHelper();
        buildHelper.signApk(apkToSign, keystorePath, keystorePassword);
        File expectedSignedApk = new File(FIXTURE_LOCATION, "sign_tests/signed.apk");
        ExtendedTestUtility.assertContainsFile("Expected correct signed apk.", expectedSignedApk, apkToSign);
    }
}
