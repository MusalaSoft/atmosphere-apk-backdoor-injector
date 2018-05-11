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

package com.musala.atmosphere.apk.backdoor.decorations.webviewdebug;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.helpers.FileSystemHelper;
import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

/**
 * Tests {@link DebuggableWebViewsDecoration}.
 * 
 * @author boris.strandjev
 */
public class DebuggableWebViewsDecorationTest {
    private static final String FIXTURE_LOCATION = "fixtures/web_view_debug/";

    private static final File TMP_FOLDER = new File("test_debuggable_web_views_tmp");

    /** Helper class that makes visible attributes required for the testing. */
    private static class DebuggableWebviewsDecorationAdapter extends DebuggableWebViewsDecoration {

        public DebuggableWebviewsDecorationAdapter() {
            super(null);
        }

        @Override
        public void prepareAuxiliaryApplication(String applicationPackage,
                                                String applicationName,
                                                File auxiliaryApplicationCreationLocation) {
            super.prepareAuxiliaryApplication(applicationPackage, applicationName, auxiliaryApplicationCreationLocation);
        }

        public File getAuxiliaryAppBaseRootDir() {
            return AUXILIARY_APP_BASE_ROOT_DIR;
        }

    }

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testApplyDecoration() throws IOException {
        File inputRootFolder = new File(FIXTURE_LOCATION, "apply_decoration/decoration_input");
        FileSystemHelper.copyDirectory(inputRootFolder, TMP_FOLDER);

        DebuggableWebViewsDecoration decoration = new DebuggableWebViewsDecoration(null);
        decoration.applyDecoration(TMP_FOLDER);

        File expectedFolderRoot = new File(FIXTURE_LOCATION, "apply_decoration/expected_output");
        ExtendedTestUtility.assertDirectoriesEqual(expectedFolderRoot, TMP_FOLDER);
    }

    @Test
    public void testPrepareAppWithCodeToInjectNoApplication() throws IOException {
        DebuggableWebviewsDecorationAdapter decoration = new DebuggableWebviewsDecorationAdapter();

        File injectedAppRootFolder = new File(TMP_FOLDER, "injected_root_dir");
        FileSystemHelper.copyDirectory(decoration.getAuxiliaryAppBaseRootDir(), injectedAppRootFolder);
        decoration.prepareAuxiliaryApplication("com.study.pro", null, injectedAppRootFolder);

        File expectedRootDir = new File(FIXTURE_LOCATION, "prepare_code/no_application");
        ExtendedTestUtility.assertDirectoriesEqual(expectedRootDir, injectedAppRootFolder);
    }

    @Test
    public void testPrepareAppWithCodeToInjectNoPackage() throws IOException {
        DebuggableWebviewsDecorationAdapter decoration = new DebuggableWebviewsDecorationAdapter();

        File injectedAppRootFolder = new File(TMP_FOLDER, "injected_root_dir");
        FileSystemHelper.copyDirectory(decoration.getAuxiliaryAppBaseRootDir(), injectedAppRootFolder);
        decoration.prepareAuxiliaryApplication("com.other.example", ".StudyApplication", injectedAppRootFolder);

        File expectedRootDir = new File(FIXTURE_LOCATION, "prepare_code/with_application");
        ExtendedTestUtility.assertDirectoriesEqual(expectedRootDir, injectedAppRootFolder);
    }

    @Test
    public void testPrepareAppWithCodeToInjectWithPackage() throws IOException {
        DebuggableWebviewsDecorationAdapter decoration = new DebuggableWebviewsDecorationAdapter();

        File injectedAppRootFolder = new File(TMP_FOLDER, "injected_root_dir");
        FileSystemHelper.copyDirectory(decoration.getAuxiliaryAppBaseRootDir(), injectedAppRootFolder);
        decoration.prepareAuxiliaryApplication("com.study.pro",
                                               "com.other.example.StudyApplication",
                                               injectedAppRootFolder);

        File expectedRootDir = new File(FIXTURE_LOCATION, "prepare_code/with_application");
        ExtendedTestUtility.assertDirectoriesEqual(expectedRootDir, injectedAppRootFolder);
    }
}
