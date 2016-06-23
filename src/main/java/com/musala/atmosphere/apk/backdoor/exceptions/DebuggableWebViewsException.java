package com.musala.atmosphere.apk.backdoor.exceptions;

/**
 * Exception thrown if any problem while adding web view debugging as an application backdoor.
 * 
 * @author boris.strandjev
 */
public class DebuggableWebViewsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DebuggableWebViewsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
