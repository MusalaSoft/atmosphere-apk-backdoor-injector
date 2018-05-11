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
import java.util.HashMap;
import java.util.Map;

import com.musala.atmosphere.apk.backdoor.ApkBackdoorDecoration;
import com.musala.atmosphere.apk.backdoor.exceptions.DebuggableWebViewsException;
import com.musala.atmosphere.apk.backdoor.helpers.ApkToolHelper;
import com.musala.atmosphere.apk.backdoor.helpers.ApplicationBuilderHelper;
import com.musala.atmosphere.apk.backdoor.helpers.FileSystemHelper;
import com.musala.atmosphere.apk.backdoor.helpers.FreemarkerTemplateHelper;
import com.musala.atmosphere.apk.backdoor.helpers.XmlFileAccessor;

/**
 * Decoration that makes all the web views in the application debuggable so that ATMOSPHERE can interact with them.
 * 
 * @author boris.strandjev
 */
public class DebuggableWebViewsDecoration extends ApkBackdoorDecoration {

    /**
     * The decoration uses an auxiliary application in order to introduce its backdoor code.
     * <p>
     * This is the root folder of the base files used to create this auxiliary application.
     */
    protected static final File AUXILIARY_APP_BASE_ROOT_DIR = new File("web_view_debug");

    /** Where the work of the decoration will be executed. */
    private static final File WORKING_DIR = new File("debuggable_web_views_tmp_dir");

    /** The location at which the auxiliary app will be compiled. */
    private static final File AUXILIARY_APP_APK_LOCATION = new File(WORKING_DIR, "aux_app.apk");

    // Constants for the auxiliary application
    private static final File INJECTED_APPLICATION_FTL_LOCATION = new File("freemarker_templates/injected_application.ftl");

    private static final File BASE_APPLICATION_FTL_LOCATION = new File("freemarker_templates/base_application.ftl");

    private static final String INJECTED_APPLICATION_PACKAGE = "com.musala.atmosphere.android.inject.webviews";

    private static final String INJECTED_APPLICATION_NAME = "InjectedWebViewApplication";

    private static final String AUXILIARY_APPLICATION_PROJECT_NAME = "ApplicationOnlyProject";

    // Freemarker constants
    private static final String FREEMARKER_PARAMETER_INJECTED_APPPLICATION = "injected_application_name";

    private static final String FREEMARKER_PARAMETER_INJECTED_PACAKGE = "injected_application_package";

    private static final String FREEMARKER_PARAMETER_APPLICATION_NAME = "application_name";

    private static final String FREEMARKER_PARAMETER_PACKAGE = "package";

    // Manifest constants
    /** The name of the manifest file from which to read the configuration of app subject to backdooring. */
    private static final String MANIFEST_FILE_NAME = "AndroidManifest.xml";

    private static final String MANIFEST_APPLICATION_NAME_ATTR = "android:name";

    private static final String MANIFEST_APPLICATION_TAG = "application";

    public DebuggableWebViewsDecoration(ApkBackdoorDecoration decoration) {
        super(decoration);
    }

    @Override
    public void applyDecoration(File rootDirectorySmaliProject) {
        super.applyDecoration(rootDirectorySmaliProject);
        // ensuring we are working on clean environment
        FileSystemHelper.removeFile(WORKING_DIR);
        WORKING_DIR.mkdirs();

        XmlFileAccessor manifestAccessor = new XmlFileAccessor(new File(rootDirectorySmaliProject, MANIFEST_FILE_NAME));
        String applicationName = manifestAccessor.getAttributeValue(MANIFEST_APPLICATION_TAG,
                                                                    MANIFEST_APPLICATION_NAME_ATTR);
        String packageName = manifestAccessor.getAttributeValue("manifest", FREEMARKER_PARAMETER_PACKAGE);

        File injectedAppRootFolder = new File(WORKING_DIR, "injected_app");
        prepareAuxiliaryApplication(packageName, applicationName, injectedAppRootFolder);

        ApplicationBuilderHelper applicationBuilderHelper = new ApplicationBuilderHelper();
        applicationBuilderHelper.compileApplication(injectedAppRootFolder,
                                                    AUXILIARY_APPLICATION_PROJECT_NAME,
                                                    AUXILIARY_APP_APK_LOCATION);

        File decompiledInjectedAppRootFolder = new File(WORKING_DIR, "decompiled_injected_app");

        ApkToolHelper apkToolHelper = new ApkToolHelper();
        apkToolHelper.decompileApk(AUXILIARY_APP_APK_LOCATION, decompiledInjectedAppRootFolder);

        addInjectedApplicationSmali(rootDirectorySmaliProject, decompiledInjectedAppRootFolder);
        String injectedApplicationName = INJECTED_APPLICATION_PACKAGE + "." + INJECTED_APPLICATION_NAME;
        manifestAccessor.changeAttribute(MANIFEST_APPLICATION_TAG,
                                         MANIFEST_APPLICATION_NAME_ATTR,
                                         injectedApplicationName);

        // cleaning up after ourselves
        FileSystemHelper.removeFile(WORKING_DIR);
    }

    /**
     * Creates the auxiliary application that will be used to create the smali code to inject.
     * 
     * @param applicationPackage
     *        - the package as specified in the target application manifest
     * @param applicationName
     *        - the application name as specified in the application manifest. Can be <code>null</code>. Can start with
     *        the package
     * @param auxiliaryApplicationCreationLocation
     *        - the location at which to create the auxiliary application
     * 
     */
    protected void prepareAuxiliaryApplication(String applicationPackage,
                                               String applicationName,
                                               File auxiliaryApplicationCreationLocation) {
        boolean applicationDefined = true;
        if (applicationName == null) {
            applicationPackage = "android.app";
            applicationName = "Application";
            applicationDefined = false;
        } else if (applicationName.startsWith(".")) {
            applicationName = applicationName.substring(1);
        }

        if (applicationName.contains(".")) {
            applicationPackage = applicationName.substring(0, applicationName.lastIndexOf("."));
            applicationName = applicationName.substring(applicationName.lastIndexOf(".") + 1);
        }

        try {
            FileSystemHelper.copyDirectory(AUXILIARY_APP_BASE_ROOT_DIR, auxiliaryApplicationCreationLocation);
            createInjectedApplicationJavaFile(applicationPackage, applicationName, auxiliaryApplicationCreationLocation);

            if (applicationDefined) {
                createBaseApplicationJavaFile(applicationPackage, applicationName, auxiliaryApplicationCreationLocation);
            }
        } catch (IOException e) {
            throw new DebuggableWebViewsException("Problem creating auxiliary application.", e);
        }
    }

    private void addInjectedApplicationSmali(File rootDirectorySmaliProject, File decompiledInjectedAppRootFolder) {
        String relativeSmaliInjectedApplication = "smali/" + convertPackageToDirectories(INJECTED_APPLICATION_PACKAGE)
                + "/" + INJECTED_APPLICATION_NAME + ".smali";
        File smaliInjectedApplication = new File(decompiledInjectedAppRootFolder, relativeSmaliInjectedApplication);
        try {
            FileSystemHelper.copyFile(smaliInjectedApplication, new File(rootDirectorySmaliProject,
                                                                         relativeSmaliInjectedApplication));
        } catch (IOException e) {
            throw new DebuggableWebViewsException("Problem when adding injected application.", e);
        }
    }

    private void createInjectedApplicationJavaFile(String applicationPackage,
                                                   String applicationName,
                                                   File auxiliaryApplicationCreationLocation) {
        FreemarkerTemplateHelper freemarkerTemplateHelper = new FreemarkerTemplateHelper();

        File injectedApplicationJavaFileLocation = constructAuxAppJavaFilelocation(auxiliaryApplicationCreationLocation,
                                                                                   INJECTED_APPLICATION_PACKAGE,
                                                                                   INJECTED_APPLICATION_NAME);

        Map<String, String> inflateParameters = new HashMap<String, String>();
        inflateParameters.put(FREEMARKER_PARAMETER_PACKAGE, applicationPackage);
        inflateParameters.put(FREEMARKER_PARAMETER_APPLICATION_NAME, applicationName);
        inflateParameters.put(FREEMARKER_PARAMETER_INJECTED_PACAKGE, INJECTED_APPLICATION_PACKAGE);
        inflateParameters.put(FREEMARKER_PARAMETER_INJECTED_APPPLICATION, INJECTED_APPLICATION_NAME);
        freemarkerTemplateHelper.inflateTemplate(INJECTED_APPLICATION_FTL_LOCATION,
                                                 injectedApplicationJavaFileLocation,
                                                 inflateParameters);
    }

    private void createBaseApplicationJavaFile(String applicationPackage,
                                               String applicationName,
                                               File auxiliaryApplicationCreationLocation) {
        FreemarkerTemplateHelper freemarkerTemplateHelper = new FreemarkerTemplateHelper();

        File baseApplicationJavaFileLocation = constructAuxAppJavaFilelocation(auxiliaryApplicationCreationLocation,
                                                                               applicationPackage,
                                                                               applicationName);

        Map<String, String> inflateParameters = new HashMap<String, String>();
        inflateParameters.put(FREEMARKER_PARAMETER_PACKAGE, applicationPackage);
        inflateParameters.put(FREEMARKER_PARAMETER_APPLICATION_NAME, applicationName);
        freemarkerTemplateHelper.inflateTemplate(BASE_APPLICATION_FTL_LOCATION,
                                                 baseApplicationJavaFileLocation,
                                                 inflateParameters);
    }

    private File constructAuxAppJavaFilelocation(File auxiliaryApplicationLocation,
                                                 String applicationPackage,
                                                 String applicationName) {
        return new File(auxiliaryApplicationLocation, AUXILIARY_APPLICATION_PROJECT_NAME + "/src/"
                + convertPackageToDirectories(applicationPackage) + "/" + applicationName + ".java");
    }

    private String convertPackageToDirectories(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }
}
