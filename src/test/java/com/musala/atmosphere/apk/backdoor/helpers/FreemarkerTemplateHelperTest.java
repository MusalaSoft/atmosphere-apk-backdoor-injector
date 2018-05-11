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
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

/**
 * Tests see {@link FreemarkerTemplateHelper}.
 * 
 * @author boris.strandjev
 */
public class FreemarkerTemplateHelperTest {
    private static final String FIXTURE_LOCATION = "fixtures/freemarker/";

    private static final String TEST_INPUT_FOLDER = "freemarker_templates/";

    private static final File TMP_FOLDER = new File("test_freemarker_tmp");

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testInflateTemplateNoFolderNeeded() throws IOException {
        FreemarkerTemplateHelper freemarkerHelper = new FreemarkerTemplateHelper();
        TMP_FOLDER.mkdirs();
        File outputLocation = new File(TMP_FOLDER, "output.java");
        File templateLocation = new File(TEST_INPUT_FOLDER, "injected_application.ftl");
        Map<String, String> inflateParameters = prepareInflateParameters();
        freemarkerHelper.inflateTemplate(templateLocation, outputLocation, inflateParameters);

        File expectedDirectoryStructure = new File(FIXTURE_LOCATION, "first_test");
        ExtendedTestUtility.assertDirectoriesEqual(expectedDirectoryStructure, TMP_FOLDER);
    }

    @Test
    public void testInflateTemplateCreatingFolders() throws IOException {
        FreemarkerTemplateHelper freemarkerHelper = new FreemarkerTemplateHelper();
        // partially creating the parent folders of the file to create
        new File(TMP_FOLDER, "com/musala").mkdirs();
        File outputLocation = new File(TMP_FOLDER, "com/musala/atmosphere/android/inject/webviews/output.java");
        File templateLocation = new File(TEST_INPUT_FOLDER, "injected_application.ftl");
        Map<String, String> inflateParameters = prepareInflateParameters();
        freemarkerHelper.inflateTemplate(templateLocation, outputLocation, inflateParameters);

        File expectedDirectoryStructure = new File(FIXTURE_LOCATION, "second_test");
        ExtendedTestUtility.assertDirectoriesEqual(expectedDirectoryStructure, TMP_FOLDER);
    }

    @Test
    public void testSecondTempalteInflation() throws IOException {
        FreemarkerTemplateHelper freemarkerHelper = new FreemarkerTemplateHelper();
        TMP_FOLDER.mkdirs();
        File outputLocation = new File(TMP_FOLDER, "output.java");
        File templateLocation = new File(TEST_INPUT_FOLDER, "base_application.ftl");
        Map<String, String> inflateParameters = prepareInflateParameters();
        freemarkerHelper.inflateTemplate(templateLocation, outputLocation, inflateParameters);

        File expectedDirectoryStructure = new File(FIXTURE_LOCATION, "third_test");
        ExtendedTestUtility.assertDirectoriesEqual(expectedDirectoryStructure, TMP_FOLDER);
    }

    private Map<String, String> prepareInflateParameters() {
        Map<String, String> inflateParameters = new HashMap<String, String>();
        inflateParameters.put("package", "com.real.application");
        inflateParameters.put("application_name", "RealApplication");
        inflateParameters.put("injected_application_package", "com.musala.atmosphere.android.inject.webviews");
        inflateParameters.put("injected_application_name", "InjectedWebViewApplication");
        return inflateParameters;
    }
}
