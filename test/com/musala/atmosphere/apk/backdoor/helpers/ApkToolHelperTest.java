package com.musala.atmosphere.apk.backdoor.helpers;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import brut.androlib.AndrolibException;

import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

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
        ExtendedTestUtility.assertBinaryFilesEqual("Expected correct apk.",
                                                   expectedCompiledBackApk,
                                                   compiledBackApkPath);
    }

}
