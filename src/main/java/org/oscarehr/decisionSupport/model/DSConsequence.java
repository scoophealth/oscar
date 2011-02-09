/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import java.util.List;

/**
 *
 * @author apavel
 */
public class DSConsequence {
    public enum ConsequenceType {warning, java}
    
    public enum ConsequenceStrength {warning, recommendation}
    //this is poorly done, at this point I kinda lack direction
    //feel free to redo in the future
    //need to have an interface for DSConsequence and implement with Java, Warning etc..
    private ConsequenceType consequenceType;
    private ConsequenceStrength consequenceStrength;
    private String text;
    private List<Object> objConsequence;

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the consequenceType
     */
    public ConsequenceType getConsequenceType() {
        return consequenceType;
    }

    /**
     * @param consequenceType the consequenceType to set
     */
    public void setConsequenceType(ConsequenceType consequenceType) {
        this.consequenceType = consequenceType;
    }

    /**
     * @return the consequenceStrength
     */
    public ConsequenceStrength getConsequenceStrength() {
        return consequenceStrength;
    }

    /**
     * @param consequenceStrength the consequenceStrength to set
     */
    public void setConsequenceStrength(ConsequenceStrength consequenceStrength) {
        this.consequenceStrength = consequenceStrength;
    }

    /**
     * @return the objConsequence
     */
    public List<Object> getObjConsequence() {
        return objConsequence;
}

    /**
     * @param objConsequence the objConsequence to set
     */
    public void setObjConsequence(List<Object> objConsequence) {
        this.objConsequence = objConsequence;
    }
}
