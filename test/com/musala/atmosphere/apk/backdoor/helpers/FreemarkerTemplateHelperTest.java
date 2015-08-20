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
