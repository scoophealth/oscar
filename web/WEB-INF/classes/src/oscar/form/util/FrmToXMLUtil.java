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
import java.text.*;
import org.apache.xmlbeans.*;
import org.apache.xmlbeans.XmlCalendar;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.oscarRx.data.*;
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
        String _dateFormat = "yyyy-MM-dd hh:mm:ss";
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
                //System.out.println("method "+methodCall);
                org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
                
                if(mt.getType().equalsIgnoreCase("BP") && !gValidator.isBlankOrNull(dataProps.getProperty("SBPValue"))){
                    methodCall = (String) nameProps.get("SBPValue");
                    if (methodCall != null){
                       Class cls = visit.getClass();
                       //System.out.println("calling addNew"+methodCall);
                       Method addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                       Object obj = addNewMethod.invoke(visit,new Object[]{});

                       String value = dataProps.getProperty("SBPValue");
                       //System.out.println(itemName + " who "+who+" how "+how+ " when "+when+ " value "+value);            
                       setWhoWhatWhereWhen(obj,how,who,when,value);

                       //String date = dataProps.getProperty(itemName+"Date");
                       //setWhoWhatWhereWhen(obj,how,who,when,date);
                    }
                    methodCall = (String) nameProps.get("DBPValue");
                    if (methodCall != null){
                       Class cls = visit.getClass();
                       //System.out.println("calling addNew"+methodCall);
                       Method addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                       Object obj = addNewMethod.invoke(visit,new Object[]{});

                       String value = dataProps.getProperty("DBPValue");
                       //System.out.println(itemName + " who "+who+" how "+how+ " when "+when+ " value "+value);            
                       setWhoWhatWhereWhen(obj,how,who,when,value);

                       //String date = dataProps.getProperty(itemName+"Date");
                       //setWhoWhatWhereWhen(obj,how,who,when,date);
                    }
                    methodCall = (String) nameProps.get("BPDate");
                    //System.out.println("method "+methodCall);
                    if (methodCall != null){                                       

                       Class cls = visit.getClass();
                       //System.out.println("calling addNew"+methodCall);
                       Method addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                       Object obj = addNewMethod.invoke(visit,new Object[]{});

                       String value = dataProps.getProperty(itemName+"Date");
                       //System.out.println(itemName + "Date: who "+who+" how "+how+ " when "+when+ " value "+value);            
                       setWhoWhatWhereWhen(obj,how,who,when,value);

                       //String date = dataProps.getProperty(itemName+"Date");
                       //setWhoWhatWhereWhen(obj,how,who,when,date);
                    } 
                    
                }                

                else if (methodCall != null && !gValidator.isBlankOrNull(dataProps.getProperty(itemName+"Value"))){                                                                               

                   Class cls = visit.getClass();
                   //System.out.println("calling addNew"+methodCall);
                   Method addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                   Object obj = addNewMethod.invoke(visit,new Object[]{});

                   String value = dataProps.getProperty(itemName+"Value");
                   //System.out.println(itemName + " who "+who+" how "+how+ " when "+when+ " value "+value);            
                   setWhoWhatWhereWhen(obj,how,who,when,value);

                   //String date = dataProps.getProperty(itemName+"Date");
                   //setWhoWhatWhereWhen(obj,how,who,when,date);

                    methodCall = (String) nameProps.get(itemName+"Date");
                    //System.out.println("method "+methodCall);
                    if (methodCall != null){                                       

                       cls = visit.getClass();
                       //System.out.println("calling addNew"+methodCall);
                       addNewMethod  = cls.getMethod("addNew"+methodCall, new Class[] {});

                       obj = addNewMethod.invoke(visit,new Object[]{});

                       value = dataProps.getProperty(itemName+"Date");
                       //System.out.println(itemName + "Date: who "+who+" how "+how+ " when "+when+ " value "+value);            
                       setWhoWhatWhereWhen(obj,how,who,when,value);

                       //String date = dataProps.getProperty(itemName+"Date");
                       //setWhoWhatWhereWhen(obj,how,who,when,date);
                    } 
                }
               
            }
                        
            //get drug list             
            RxPatientData pData = new RxPatientData();            
            RxPatientData.Patient p = pData.getPatient(Integer.parseInt(dataProps.getProperty("demographic_no")==null?"0":dataProps.getProperty("demographic_no")));
            RxPrescriptionData.Prescription[] prescribedDrugs = p.getPrescribedDrugsUnique();            
            for(int i=0; i<prescribedDrugs.length; i++){                
                SitePatientVisitRecordsDocument.SitePatientVisitRecords.SitePatientVisit.SitePatientVisitDrug drug = visit.addNewSitePatientVisitDrug();                
                drug.setDrugCod("ATC_"+prescribedDrugs[i].getAtcCode().trim());
                SitePatientVisitRecordsDocument.SitePatientVisitRecords.SitePatientVisit.SitePatientVisitDrug.TxtDrugName drugName = drug.addNewTxtDrugName();                
                drugName.setSignedHow(how);
                drugName.setSignedWho(who);
                drugName.setSignedWhen(when);
                drugName.setValue(prescribedDrugs[i].getDrugName());
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
      System.out.println("*********************************************************************************");
      System.out.println("************************** XML GENERATED BY OSCAR *******************************");
      System.out.println("*********************************************************************************");
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
         Method setValueMethod = cls.getMethod("setValue",new Class[] {int.class});
         if(value.equalsIgnoreCase(""))
             value="0";
         Integer integer = new Integer(value);
         setValueMethod.invoke(obj, new Object[] {integer}); 
         i = 2;
      }catch (NoSuchMethodException noSuchMethod1){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {double.class});
         if(value.equalsIgnoreCase(""))
             value="0";
         Double dbl = new Double(value);
         setValueMethod.invoke(obj, new Object[] {dbl});           
         i = 3;
      }catch (NoSuchMethodException noSuchMethod1){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {boolean.class});
         i = 4;      
         Boolean bool = new Boolean(value);
         setValueMethod.invoke(obj, new Object[] {bool});               
      }catch (NoSuchMethodException noSuchMethod2){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {Calendar.class});
         i = 5;
         if(value!=null){
             //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
             //Date date = (Date)formatter.parse(value);
             Calendar c = new XmlCalendar(value);
             //c.setTime(date);
             setValueMethod.invoke(obj, new Object[] {c});       
         }
         
         //TODO need way to change String Date into a Calendar instance                 
      }catch (NoSuchMethodException noSuchMethod3){}
      
      try{
         Method setValueMethod = cls.getMethod("setValue",new Class[] {Date.class});
         i = 5;
         if(value!=null){
            Date date = new Date(value);
            setValueMethod.invoke(obj, new Object[] {date});       
         }
         
      }catch (NoSuchMethodException noSuchMethod3){}
      return i;
   }
   
   public static void getMembers(Object obj){
     Class cls = obj.getClass();
     //Method[] methods = cls.getDeclaredMethods();
     Method[] methods = cls.getMethods();
          for (int i=0; i < methods.length; i++){
                //if(methods[i].getName().startsWith("get")){
           Class[] params = methods[i].getParameterTypes();
                   System.out.print(methods[i].getName());
           System.out.print("(");
           for (int j=0; j < params.length; j++){
              System.out.print(" "+params[j].getName());
           }
           System.out.println(")");
        //}
     }     
  }    
          
}
