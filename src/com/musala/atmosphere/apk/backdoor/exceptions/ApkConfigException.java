package com.musala.atmosphere.apk.backdoor.exceptions;

/**
 * Exception thrown if any problem while apk configuring is encountered.
 * 
 * @author boris.strandjev
 */
public class ApkConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApkConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApkConfigException(String message) {
        super(message);
    }

}
