 /*
 * 
 * Copyright (c) 2001-2005. Compete 3
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <Ivy Chan>
 * 
 * This software was written for the 
 * Compete 3 Project
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.form.util;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import org.apache.xmlbeans.*;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.util.UtilDateUtilities;
import noNamespace.*;

/**
 *
 * @author  Jay Gallagher
 */
public class FrmToXMLUtil{
   
   /** Creates a new instance of ToCDR */
   public FrmToXMLUtil() {
   }
   
   /**
    * @param args the command line arguments
    */
    public static String convertToXml(Vector measurementTypes, Properties nameProps, Properties dataProps){
        // TODO code application logic here            
        String _dateFormat = "yyyy/MM/dd";
        String dateEntered = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat);
        
        SitePatientVisitRecordsDocument visitDocument = SitePatientVisitRecordsDocument.Factory.newInstance();
        SitePatientVisitRecordsDocument.SitePatientVisitRecords visitRecord = visitDocument.addNewSitePatientVisitRecords();

        //Vector patientVec = comm.getAllPatientsInStudy();

        //for ( int pVec = 0 ; pVec < patientVec.size();pVec++){
        //Properties patientProps = (Properties) patientVec.get(pVec);

        //String patientCod = (String) patientProps.get("Patient_cod");         
        //Vector vec = comm.getElementByDemographicAndDate(patientCod,start,end);
        SitePatientVisitRecordsDocument.SitePatientVisitRecords.SitePatientVisit visit  = visitRecord.addNewSitePatientVisit();
         
        try{
            
            visit.setPatientCod(dataProps.getProperty("demographic_no"));
            visit.setVisitCod(dataProps.getProperty("visitCod"));
            String who = dataProps.getProperty("provider_no");
            String how = "EMR";
            String when = dateEntered;
            
            EctMeasurementTypesBean mt;
            for (int i = 0; i < measurementTypes.size(); i++){        
                mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
                String itemName = mt.getType();
                String methodCall = (String) nameProps.get(itemName+"Value");
                System.out.println("method "+methodCall);
                if (methodCall != null){                                       
                    
                   Class cls = visit.getClass();
                   System.out.println("calling addNew"+methodCall);
                   Method addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                   Object obj = addNewMethod.invoke(visit,new Object[]{});
                   
                   String value = dataProps.getProperty(itemName+"Value");
                   System.out.println("who "+who+" how "+how+ " when "+when+ " value "+value);            
                   setWhoWhatWhereWhen(obj,how,who,when,value);

                   //String date = dataProps.getProperty(itemName+"Date");
                   //setWhoWhatWhereWhen(obj,how,who,when,date);
                }                  
            }
        }
        catch(NoSuchMethodException e){
            e.printStackTrace();
        }
        catch(IllegalAccessException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

         //System.out.println(" number of records "+vec.size());      //
      XmlOptions xmlOptions = new XmlOptions();
      xmlOptions.setSavePrettyPrint();      
      xmlOptions.setSavePrettyPrintIndent(3);
      String xmlStr = visitDocument.xmlText(xmlOptions);      
      System.out.println(xmlStr);
      return xmlStr;
   }
   
   
   public static void setWhoWhatWhereWhen(Object obj,String how,String who,String when,String value) throws Exception{
      Class cls = obj.getClass();
      Method setSignedHowMethod = cls.getMethod("setSignedHow",new Class[] {String.class});
      setSignedHowMethod.invoke(obj, new Object[] {how});
      
      Method setSignedWhoMethod = cls.getMethod("setSignedWho",new Class[] {String.class});
      setSignedWhoMethod.invoke(obj, new Object[] {who});
      
      Method setSignedWhenMethod = cls.getMethod("setSignedWhen",new Class[] {String.class});
      setSignedWhenMethod.invoke(obj, new Object[] {when});
      
      setValueType(obj,cls,value);
      
    }
   
   public static int setValueType(Object obj,Class cls,String value) throws Exception{
      int i = 0;
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {String.class});
         setValueMethod.invoke(obj, new Object[] {value} );
         i = 1;
      }catch (NoSuchMethodException noSuchMethod1){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {boolean.class});
         i = 2;      
         Boolean bool = new Boolean(value);
         setValueMethod.invoke(obj, new Object[] {bool});               
      }catch (NoSuchMethodException noSuchMethod2){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {Calendar.class});
         i = 3;
         Calendar c = Calendar.getInstance();
         //TODO need way to change String Date into a Calendar instance                 
      }catch (NoSuchMethodException noSuchMethod3){}
      return i;
   }
          
}
