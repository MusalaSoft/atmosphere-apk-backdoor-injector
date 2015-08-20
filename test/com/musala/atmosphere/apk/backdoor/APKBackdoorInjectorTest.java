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
        ExtendedTestUtility.assertBinaryFilesEqual("Expected the correct debuggable apk.",
                                                   expectedDebuggableApk,
                                                   targetApk);
    }
}
