/*
 * CumulativeLabValuesComparator.java
 *
 * Created on August 3, 2007, 3:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.util;

import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import oscar.util.UtilDateUtilities;

/**
 *  A custom comparator used to compare the Hashtables within an array by the 
 *  date value.
 *
 *  Used by /oscar/lab/CumulativeLabValues3.jsp
 *
 *
 * @author wrighd
 */
public class CumulativeLabValuesComparator implements Comparator{
    
    public int compare(Object o1, Object o2) {
        Date dateA = UtilDateUtilities.getDateFromString((String) ((Hashtable) o1).get("date") , "yyyy-MM-dd HH:mm:ss");
        Date dateB = UtilDateUtilities.getDateFromString((String) ((Hashtable) o2).get("date") , "yyyy-MM-dd HH:mm:ss");
        int ret = 0;
        
        if (dateA.after( dateB )){
            ret = -1;
        }else if(dateA.before( dateB )){
            ret = 1;
        }
        
        return ret;
    }
    
}
