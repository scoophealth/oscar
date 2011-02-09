package org.oscarehr.PMmodule.exception;

import org.oscarehr.PMmodule.model.ProgramClientRestriction;

/**
 */
public class ServiceRestrictionException extends Exception {

    private ProgramClientRestriction restriction;

    public ServiceRestrictionException(ProgramClientRestriction restriction) {
        this.restriction = restriction;
    }

    public ServiceRestrictionException(String s, ProgramClientRestriction restriction) {
        super(s);
        this.restriction = restriction;
    }

    public ServiceRestrictionException(String s, Throwable throwable, ProgramClientRestriction restriction) {
        super(s, throwable);
        this.restriction = restriction;
    }

    public ServiceRestrictionException(Throwable throwable, ProgramClientRestriction restriction) {
        super(throwable);
        this.restriction = restriction;
    }

    public ProgramClientRestriction getRestriction() {
        return restriction;
    }
}
