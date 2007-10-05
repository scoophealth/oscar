package org.oscarehr.PMmodule.exception;

/**
 */
public class IntegratorNotInitializedException extends IntegratorException {
    public IntegratorNotInitializedException() {
    }

    public IntegratorNotInitializedException(String msg) {
        super(msg);
    }

    public IntegratorNotInitializedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IntegratorNotInitializedException(Throwable throwable) {
        super(throwable);
    }
}
