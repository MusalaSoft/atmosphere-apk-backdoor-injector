package com.musala.atmosphere.apk.backdoor.exceptions;

/**
 * Exception thrown if any problem while interacting with xml files is encountered.
 * 
 * @author boris.strandjev
 */
public class XmlAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public XmlAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
