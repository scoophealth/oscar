package org.oscarehr.PMmodule.exception;

/**
 */
public class ClientAlreadyRestrictedException extends Exception {
    public ClientAlreadyRestrictedException() {
    }

    public ClientAlreadyRestrictedException(String s) {
        super(s);
    }

    public ClientAlreadyRestrictedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ClientAlreadyRestrictedException(Throwable throwable) {
        super(throwable);
    }
}
