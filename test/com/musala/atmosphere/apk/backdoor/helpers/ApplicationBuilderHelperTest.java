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
