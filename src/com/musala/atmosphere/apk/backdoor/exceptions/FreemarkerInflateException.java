package com.musala.atmosphere.apk.backdoor.exceptions;

/**
 * Exception thrown if any problem while inflating freemarker tempaltes is encountered.
 * 
 * @author boris.strandjev
 */
public class FreemarkerInflateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FreemarkerInflateException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
