/*
 * Denominator.java
 *
 * Created on June 17, 2006, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarReport.ClinicalReports;

import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author jay
 */
public interface Denominator {
    public List getDenominatorList();
    public String getDenominatorName();
    public String getId();
    public boolean hasReplaceableValues();
    public String[] getReplaceableKeys();
    public void setReplaceableValues(Hashtable vals);
    public Hashtable getReplaceableValues();
        
      
}
