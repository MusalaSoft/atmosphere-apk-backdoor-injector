package com.musala.atmosphere.apk.backdoor;

import java.io.File;

/**
 * A base class for all backdooring decorations of the backdoor injector.
 * 
 * @author boris.strandjev
 */
public class ApkBackdoorDecoration {
    protected ApkBackdoorDecoration decoration;

    public ApkBackdoorDecoration(ApkBackdoorDecoration decoration) {
        this.decoration = decoration;
    }

    /**
     * Base implementation of the decoration function.
     * 
     * @param rootDirectorySmaliProject
     *        - the location in which the apk subject to backdooring is decompiled to Smali code
     */
    public void applyDecoration(File rootDirectorySmaliProject) {
        if (decoration != null) {
            decoration.applyDecoration(rootDirectorySmaliProject);
        }
    }
}
