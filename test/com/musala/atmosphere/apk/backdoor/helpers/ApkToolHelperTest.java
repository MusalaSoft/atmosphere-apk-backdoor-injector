package com.musala.atmosphere.apk.backdoor.helpers;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

import brut.androlib.AndrolibException;

/**
 * Tests {@link ApkToolHelper}.
 *
 * @author boris.strandjev
 */
public class ApkToolHelperTest {
    private static final String FIXTURE_LOCATION = "fixtures/apk_tool_helper/";

    private static final File FIXTURE_APK = new File(FIXTURE_LOCATION, "ApplicationOnlyProject.apk");

    private static final File FIXTURE_DECOMPILED_DIR = new File(FIXTURE_LOCATION, "decompiled");

    private static final File TMP_FOLDER = new File("test_apk_tool_tmp");

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testDecompileApk() throws IOException, AndrolibException {
        ApkToolHelper apkToolHelper = new ApkToolHelper();
        apkToolHelper.decompileApk(FIXTURE_APK, TMP_FOLDER);

        ExtendedTestUtility.assertDirectoriesEqual(FIXTURE_DECOMPILED_DIR, TMP_FOLDER);
    }

    @Test
    public void testDecompileApkExistingDirectory() throws IOException, AndrolibException {
        TMP_FOLDER.mkdir();
        ApkToolHelper apkToolHelper = new ApkToolHelper();
        apkToolHelper.decompileApk(FIXTURE_APK, TMP_FOLDER);

        ExtendedTestUtility.assertDirectoriesEqual(FIXTURE_DECOMPILED_DIR, TMP_FOLDER);
    }

    @Test
    public void testCompileBackSmaliCode() throws IOException {
        File decompiledFolderPath = new File(TMP_FOLDER, "decompiled");
        decompiledFolderPath.mkdir();

        FileSystemHelper.copyDirectory(FIXTURE_DECOMPILED_DIR, decompiledFolderPath);
        ApkToolHelper apkToolHelper = new ApkToolHelper();
        File compiledBackApkPath = new File(TMP_FOLDER, "compiledBack.apk");
        apkToolHelper.compileBackSmaliCode(decompiledFolderPath, compiledBackApkPath);

        File expectedCompiledBackApk = new File(FIXTURE_LOCATION, "recompiled/compiledBack.apk");

        /*
         * Tests if the injected APK file is larger than the old file and
         * if it contains the old file at the beginning (checked byte by byte).
         * This is working with the current version of the android command line tool
         * but this behavior might change in the future and this test might need to be fixed.
         */
        ExtendedTestUtility.assertIsLargerFile("Expected correct apk.",
                                               expectedCompiledBackApk,
                                               compiledBackApkPath);
        ExtendedTestUtility.assertContainsFile("Expected correct apk.",
                                               expectedCompiledBackApk,
                                               compiledBackApkPath);
    }

}
