package com.musala.atmosphere.apk.backdoor.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.musala.atmosphere.apk.backdoor.test.helpers.ExtendedTestUtility;

/**
 * Tests {@link XmlFileAccessor} class.
 * 
 * @author boris.strandjev
 */
public class XmlFileAccessorTest {
    private static final String FIXTURES_LOCATION = "fixtures/xml_accessor/";

    private static final String TMP_FOLDER = "test_xml_file_accessor_tmp";

    private static final String FIXTURE_FILE = "get_attribute_test.xml";

    @Before
    public void setup() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @After
    public void tearDown() {
        FileSystemHelper.removeFile(TMP_FOLDER);
    }

    @Test
    public void testGetAttributeValueNonNamespaceAttribute() {
        File testFixture = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(testFixture);

        assertEquals("Expected the correct value of existing attribute to be retrieved.",
                     "com.source.example",
                     xmlFileAccessor.getAttributeValue("manifest", "package"));
        assertNull("Expected null value for non-existing attribute",
                   xmlFileAccessor.getAttributeValue("activity", "package"));
    }

    @Test
    public void testGetAttributeValueNamespaceAttribute() {
        File testFixture = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(testFixture);

        assertEquals("Expected the correct value of existing attribute to be retrieved.",
                     "keyboardHidden|orientation|screenSize",
                     xmlFileAccessor.getAttributeValue("activity", "android:configChanges"));
        assertEquals("Expected the correct value of existing attribute to be retrieved.",
                     "@drawable/ic_launcher",
                     xmlFileAccessor.getAttributeValue("application", "android:icon"));
        assertNull("Expected null value for non-existing attribute.",
                   xmlFileAccessor.getAttributeValue("activity", "android:icon"));
    }

    @Test
    public void testModifyExistingAttribute() throws IOException {
        File fixtureFile = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        File fileToModify = new File(TMP_FOLDER, FIXTURE_FILE);
        FileSystemHelper.copyFile(fixtureFile, fileToModify);

        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(fileToModify);
        xmlFileAccessor.changeAttribute("application", "android:name", "com.musala.OurActivity");

        File expectedFile = new File(FIXTURES_LOCATION, "modified/modified1_get_attribute_test.xml");
        ExtendedTestUtility.assertTextFilesEqual("Expected the correct modified xml.", expectedFile, fileToModify);
    }

    @Test
    public void testModifyAddNonExistingAttribute() throws IOException {
        File fixtureFile = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        File fileToModify = new File(TMP_FOLDER, FIXTURE_FILE);
        FileSystemHelper.copyFile(fixtureFile, fileToModify);

        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(fileToModify);
        xmlFileAccessor.changeAttribute("activity", "android:label", "@string/app_name");

        File expectedFile = new File(FIXTURES_LOCATION, "modified/modified2_get_attribute_test.xml");
        ExtendedTestUtility.assertTextFilesEqual("Expected the correct modified xml.", expectedFile, fileToModify);
    }

    @Test
    public void testModifyAddFirstAttribute() throws IOException {
        File fixtureFile = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        File fileToModify = new File(TMP_FOLDER, FIXTURE_FILE);
        FileSystemHelper.copyFile(fixtureFile, fileToModify);

        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(fileToModify);
        xmlFileAccessor.changeAttribute("intent-filter", "attrib", "valueOfMe");

        File expectedFile = new File(FIXTURES_LOCATION, "modified/modified3_get_attribute_test.xml");
        ExtendedTestUtility.assertTextFilesEqual("Expected the correct modified xml.", expectedFile, fileToModify);
    }

    @Test
    public void testModifyRetainValueAttribute() throws IOException {
        File fixtureFile = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        File fileToModify = new File(TMP_FOLDER, FIXTURE_FILE);
        FileSystemHelper.copyFile(fixtureFile, fileToModify);

        XmlFileAccessor xmlFileAccessor = new XmlFileAccessor(fileToModify);
        xmlFileAccessor.changeAttribute("activity", "android:configChanges", "keyboardHidden|orientation|screenSize");

        File expectedFile = new File(FIXTURES_LOCATION, FIXTURE_FILE);
        ExtendedTestUtility.assertTextFilesEqual("Expected the xml not to be modified.", expectedFile, fileToModify);
    }
}
