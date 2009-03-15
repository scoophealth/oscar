/*
 * Numerator.java
 *
 * Created on June 17, 2006, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarReport.ClinicalReports;

import java.util.Hashtable;

/**
 *
 * @author jay
 */
public interface Numerator {
    public boolean evaluate(String demographicNo);
    public String getId();
    public String getNumeratorName();
    public Hashtable getOutputValues();
    public String[] getOutputFields();
    
    public boolean hasReplaceableValues();
    public String[] getReplaceableKeys();
    public void setReplaceableValues(Hashtable vals);
    public Hashtable getReplaceableValues();
       
}
