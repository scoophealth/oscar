package org.oscarehr.PMmodule.exception;

/**
 */
public class OperationNotImplementedException extends IntegratorException {
    public OperationNotImplementedException() {
    }

    public OperationNotImplementedException(String msg) {
        super(msg);
    }

    public OperationNotImplementedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OperationNotImplementedException(Throwable throwable) {
        super(throwable);
    }
}
