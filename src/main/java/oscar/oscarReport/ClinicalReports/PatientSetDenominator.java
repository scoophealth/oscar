/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package oscar.oscarReport.ClinicalReports;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import oscar.oscarReport.data.DemographicSets;

/**
 *
 * @author rjonasz
 */
public class PatientSetDenominator implements Denominator {
    private String[] replaceKeys;
    private String id = null;
    private String name = null;
    private Hashtable replaceableValues = null;

    /** Creates a new instance of PatientSetDenominator */
    public PatientSetDenominator() {
        replaceKeys = new String[1];
        replaceKeys[0] = "patientSet";
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public List getDenominatorList() {
       List<String> list = new ArrayList<String>();
       DemographicSets demoSets = new DemographicSets();
       List<String> strNames = null;
       if( this.hasReplaceableValues() ) {
           strNames = new ArrayList<String>(replaceableValues.values());
           list = demoSets.getDemographicSet(strNames);
       }
       else {
           list = demoSets.getDemographicSets();
       }

       return list;
    }

   public void setReplaceableValues(Hashtable vals) {
        replaceableValues = vals;
    }

    public Hashtable getReplaceableValues() {
        return replaceableValues;
    }

    public boolean hasReplaceableValues(){
        return true;
    }

    public String[] getReplaceableKeys() {
        return replaceKeys;
    }


     public String getDenominatorName() {
        return this.name;
    }
    public void setDenominatorName(String name){
        this.name = name;
    }
}
