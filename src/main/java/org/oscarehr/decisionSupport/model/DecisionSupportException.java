/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

/**
 *
 * @author apavel
 */
public class DecisionSupportException extends Exception {
    public DecisionSupportException(String message, Throwable e) {
        super(message, e);
    }
    public DecisionSupportException(String message) {
        super(message);
    }
}
