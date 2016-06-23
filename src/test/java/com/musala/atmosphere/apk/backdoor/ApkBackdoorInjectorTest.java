package com.musala.atmosphere.apk.backdoor;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.decorations.webviewdebug.DebuggableWebViewsDecoration;
import com.musala.atmosphere.apk.backdoor.helpers.FileSystemHelper;
import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

/**
 * Tests see {@link ApkBackdoorInjector}.
 *
 * @author boris.strandjev
 */
public class ApkBackdoorInjectorTest {
    private static final String FIXTURE_LOCATION = "fixtures/apk_backdoor/";

    private static final File TMP_FOLDER = new File("test_backdoor_injector_tmp");

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testBackdoorApplication() throws IOException {
        ApkBackdoorInjector apkBackdoorInjector = new ApkBackdoorInjector(new DebuggableWebViewsDecoration(null));
        File sourceApk = new File(FIXTURE_LOCATION, "SimpleWebViewApplication.apk");
        File sourceApkCopy = new File(TMP_FOLDER, "SimpleWebViewApplication.apk");
        FileSystemHelper.copyFile(sourceApk, sourceApkCopy);
        File targetApk = new File(TMP_FOLDER, "SimpleWebViewApplication-debuggable.apk");

        apkBackdoorInjector.backdoorApplication(sourceApkCopy, targetApk);
        File expectedDebuggableApk = new File(FIXTURE_LOCATION, "SimpleWebViewApplication-debuggable.apk");
        FileSystemHelper.copyFile(targetApk, expectedDebuggableApk);

        /*
         * Tests if the injected APK file is larger than the old file and
         * if it contains the old file at the beginning (checked byte by byte).
         * This is working with the current version of the android command line tool
         * but this behavior might change in the future and this test might need to be fixed.
         */
        ExtendedTestUtility.assertIsLargerFile("Expected the correct debuggable apk.",
                                               expectedDebuggableApk,
                                               sourceApkCopy);
        ExtendedTestUtility.assertContainsFile("Expected the correct debuggable apk.",
                                               expectedDebuggableApk,
                                               sourceApkCopy);
    }
}
