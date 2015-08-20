package com.musala.atmosphere.apk.backdoor;

/**
 * 
 * Enumerates all the properties that can be configured for {@link ApkBackdoorInjector}
 * 
 * @author boris.strandjev
 */
public enum BackdoorProperties {
    KEYSTORE_LOCATION("backdoor.keystore.location"),
    KEYSTORE_PASSWORD("backdoor.keystore.password");

    private String propertyKey;

    private BackdoorProperties(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}
