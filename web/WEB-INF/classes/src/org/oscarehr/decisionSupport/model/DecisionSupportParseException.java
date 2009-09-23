/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

/**
 *
 * @author apavel
 */
public class DecisionSupportParseException extends DecisionSupportException {
    public DecisionSupportParseException(String guidelineTitle, String message) {
        super("Error parsing decision support guideline titled '" + guidelineTitle + "'.  " + message);
    }

    public DecisionSupportParseException(String guidelineTitle, String message, Throwable e) {
        super("Error parsing decision support guideline titled '" + guidelineTitle + "'.  " + message, e);
    }

    public DecisionSupportParseException(String message) {
        super("Error parsing decision support guideline. " + message);
    }
    public DecisionSupportParseException(String message, Throwable e) {
        super(message, e);
    }
}
