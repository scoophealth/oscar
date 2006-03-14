/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of Prevention
 *
 * MeasurementDSHelper.java
 *
 * Created on February 22, 2006, 6:39 PM
 *
 */

package oscar.oscarEncounter.oscarMeasurements.util;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;

/**
 *
 * @author jay
 */
public class MeasurementDSHelper {
    
    
    EctMeasurementsDataBean mdb = null;
    java.util.Date dob = null;
    String sex = null;
    boolean problem = false;
    /** Creates a new instance of MeasurementDSHelper */
    public MeasurementDSHelper() {
    }
    
    public MeasurementDSHelper(EctMeasurementsDataBean mdb) {
        this.mdb= mdb;
        DemographicData dd = new DemographicData();
        dob = dd.getDemographicDOB(mdb.getDemo());
        sex = dd.getDemographicSex(mdb.getDemo());
        
    }
    
    public boolean hasProblem(){
        return problem;
    }
    
    public boolean isMale(){
       boolean ismale = false;
       if (sex != null && sex.trim().equalsIgnoreCase("M")){
          ismale = true;
       }
       return ismale;    
    }
    
    public boolean isFemale(){
       boolean isfemale = false;
       if (sex != null && sex.trim().equalsIgnoreCase("F")){
          isfemale = true;
       }
       return isfemale;    
    }
    
    public boolean isDataEqualTo(String str){
        boolean equal = false;
        try{
            equal = mdb.getDataField().equalsIgnoreCase(str);
        }catch(Exception e){
            e.printStackTrace();
            problem = true;
        }
        return equal;
    }
    
    public double getDataAsDouble() {
        double ret = -1;
        try{
           ret = Double.parseDouble(mdb.getDataField());
        }catch(Exception e){
            e.printStackTrace();
            problem =true;
        }  
        //System.out.println("DOUBLE val : "+ret);
        return ret;
    }       
    
    public double getNumberFromSplit(String delimiter,int number){
        double ret = -1;
        try{
           String data = mdb.getDataField();
           //System.out.println("Trying to parse "+data);
           ret = Double.parseDouble(  data.split(delimiter)[number]  );
        }catch(Exception e){
            e.printStackTrace();
            problem = true;
        }
        return ret;
    }
        
    public void setIndicationColor(String c){
        //System.out.println("SETTING COLOUR TO "+c);
        mdb.setIndicationColour(c);
    }
    
    
    
}
