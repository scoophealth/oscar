/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.form.study.hsfo2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.oscarehr.common.dao.Hsfo2PatientDao;
import org.oscarehr.common.dao.Hsfo2VisitDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Hsfo2Patient;
import org.oscarehr.common.model.Hsfo2Visit;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.SpringUtils;

import oscar.form.study.hsfo2.pageUtil.ConvertUtil;

/**
 * Class used by the HSFO Study
 * 
 */
public class HSFODAO
{
	//private static org.apache.log4j.Logger logger = MiscUtils.getLogger();
	
	private static Hsfo2VisitDao visitDao = (Hsfo2VisitDao) SpringUtils.getBean("hsfo2VisitDao");
	private static Hsfo2PatientDao patientDao = (Hsfo2PatientDao) SpringUtils.getBean("hsfo2PatientDao");
	private static MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");
	
  /** Creates a new instance of HSFODAO */
  public HSFODAO()
  {
  }

  
  public void savePatient( Hsfo2Patient patientData, String providerNo ) {
	if ( isFirstRecord( patientData.getPatient_Id() ) )
    {
      insertPatient( patientData );
    }
    else
    {
      updatePatient( patientData );
    }
    saveDataToMeasurements( null, patientData, Integer.valueOf( patientData.getPatient_Id() ), providerNo );
  }

  public void insertPatient( Hsfo2Patient patientData )
  {
	  patientDao.persist(patientData);
  }  
 
  public void updatePatient( Hsfo2Patient patientData ) {
	  patientDao.merge(patientData);
  }
    
  public void lockVisit( int ID ) 
  {
	  Hsfo2Visit visit = visitDao.getHsfoVisitById( ID );
	  visit.setLocked(true);
	  visitDao.merge(visit);
  }
    

  public int numberOfVisits( String demographic_no ) {
	  Date preDate = new Date();
	  int count=0;
	  List<Hsfo2Visit> visits = visitDao.getHsfoVisitByDemographicNo(Integer.parseInt(demographic_no)) ;
	  for(Hsfo2Visit visit: visits ) {
		  Date visiteDate = visit.getVisitDate_Id();
		  if(visiteDate.equals(preDate)) {
			  count++;
		  }
	  }
	  return count;
  }
    
  public int getMaxVisitId() {
	  return visitDao.getMaxVisitId();
  }
  public boolean hasMoreThanOneVisit( String demographic_no ) 
  {
    boolean hasMoreThanOneVisit = false;
    if ( numberOfVisits( demographic_no ) > 1 )
    {
      hasMoreThanOneVisit = true;
    }
    return hasMoreThanOneVisit;
  }

  public boolean hasLockedVisit( String demographic_no )
  {
	  List<Hsfo2Visit> visits = visitDao.getLockedVisitByDemographicNo(demographic_no);
	  if(visits.size()>0)
		  return true;
	  else 
		  return false;
  }
  
  
  public int insertVisit( Hsfo2Visit visitData, String provider_no )
  {
    int id = -1;

    
    boolean insertNewRecord = true;
    
    if( visitData.getId()!=null && visitData.getId() >0 ) {
    	insertNewRecord = false; //just edit the existing record.
    }
    //st.setDate( 4, new java.sql.Date( visitData.getVisitDate_Id().getTime() ) );
    if(insertNewRecord) {
    	visitDao.persist(visitData); //insert new  	
    	    	
    } else {
    	visitDao.merge(visitData);  //update
    }
    id = visitData.getId();
    saveDataToMeasurements( visitData, null, Integer.valueOf( visitData.getPatient_Id() ), provider_no );    
    
    return id;
  }

  public java.sql.Date getSQLDate( Date date )  {
	  if ( date != null )
	  {
		  return new java.sql.Date( date.getTime() );
	  }
	  return null;
  }

 

  public Hsfo2Patient retrievePatientRecord( String ID )  {
    return patientDao.getHsfoPatientByPatientId(ID);
    //parseVisitData?
  }

  /**
   * check if this record is the first record for this patient in the hsfo system. it for both create new form or
   * history
   *
   */
  public boolean isFirstRecord( String ID )  {
	  if(patientDao.getHsfoPatientByPatientId(ID) == null)
		  return true;
	  else
		  return false;
  }
   

  public List<Hsfo2Visit> retrieveVisitRecord( String patientId ) {
	  return visitDao.getVisitRecordByPatientId(patientId);
	  //parseVisitData?
  }


  public Hsfo2Visit retrieveLatestRecord( Date visitdate, String demographic_no )  { 
	  return visitDao.getPatientLatestVisitRecordByVisitDate(visitdate, demographic_no);
  }
   
  // check if this is a new record
  public boolean isRecordExists( Date visitdate, String demographicNo )
  {
	  Hsfo2Visit visit = visitDao.getPatientLatestVisitRecordByVisitDate(visitdate, demographicNo);
	  if(visit!=null) 
		  return true;
	  else 
		  return false;
  }
    
  
  public Hsfo2Visit retrieveSelectedRecord( int ID ) 
  {
    return visitDao.getHsfoVisitById(ID);
    
  }
  
 
  public List<String> getAllPatientId()
  {
    List<String> reList = new ArrayList<String>();
    List<Hsfo2Patient> patients = patientDao.getAllHsfoPatients();
    for(Hsfo2Patient patient : patients) {
    	if(!reList.contains(patient.getPatient_Id()))
    		reList.add(patient.getPatient_Id());
    }
    return reList;
  }
   
  public List<Hsfo2Visit> nullSafeRetrVisitRecord( String patientID, String startDate, String endDate)
  {
	  List<Hsfo2Visit> visitList = new LinkedList<Hsfo2Visit>();
	  List<Hsfo2Visit> visits = visitDao.getVisitRecordInDateRangeByDemographicNo(patientID, startDate, endDate);
	  for(Hsfo2Visit visit : visits) {
		  
		visit.setA1C(visit.getA1C()*100);
		
        visitList.add(visit);
	  }
	  if(visits.isEmpty()) {
		  Hsfo2Visit visitData = new Hsfo2Visit();
		  visitData.setSBP( Integer.MIN_VALUE );
		  visitData.setSBP_goal( Integer.MIN_VALUE );
		  visitData.setSBP_goal( Integer.MIN_VALUE );
		  visitData.setDBP_goal( Integer.MIN_VALUE );
		  visitData.setHeight( Double.MIN_VALUE );
		  visitData.setWeight( Double.MIN_VALUE );
		  visitData.setWaist( Double.MIN_VALUE );		  
		  visitData.setTC_HDL( Double.MIN_VALUE );
	      visitData.setLDL( Double.MIN_VALUE );
	      visitData.setHDL( Double.MIN_VALUE );
	      visitData.setA1C( Double.MIN_VALUE );
	      visitData.setChange_importance( Integer.MIN_VALUE );
	      visitData.setChange_confidence( Integer.MIN_VALUE );
	      visitData.setExercise_minPerWk( Integer.MIN_VALUE );
	      visitData.setSmoking_cigsPerDay( Integer.MIN_VALUE );
	      visitData.setAlcohol_drinksPerWk( Integer.MIN_VALUE );       
          visitData.setOften_miss( Integer.MIN_VALUE );
       
          visitList.add( visitData );
      }
     
	  return visitList;
  }

  /**
   * 
   * @param patientId
   * @param visitRecordId
   */
  public boolean isFirstVisitRecordForThePatient( String patientId, int visitRecordId )
  {	  
	  Hsfo2Visit visit = visitDao.getFirstVisitRecordForThePatient(patientId);
	  if(visit!=null && visit.getId().intValue() == visitRecordId)
		  return true;
	  else
		  return false;
  }	  
	

  public Hsfo2Visit getPatientBaseLineVisitData( Hsfo2Patient patientData )
  {
	  return visitDao.getPatientBaseLineVisitData(patientData.getPatient_Id());
  }
 
  
  public List getLabWorkInDateRange( String patientId, String startDate, String endDate, String visitDate) 
  {
	  List<Hsfo2Visit> pList = new ArrayList<Hsfo2Visit>();
	  Hsfo2Visit visitData = new Hsfo2Visit();
	  //Retrieve results and store into registrationData object
      Date obrDate = new Date();
      int count = 0;
	  List<Measurement> measurements = measurementDao.findByDemographicIdObservedDate(Integer.valueOf(patientId), oscar.util.DateUtils.toDate(startDate), oscar.util.DateUtils.toDate(endDate));
	  for(Measurement m : measurements) {
		 if(m.getDateObserved().before(oscar.util.DateUtils.toDate(visitDate)))
				 continue;
		 if(count!=0 && !m.getDateObserved().equals(obrDate)) {
			 pList.add(visitData);
			 //reset and clear data:
			 obrDate = m.getDateObserved();
			 visitData = new Hsfo2Visit();
		 }
		 setLabWorkField( visitData, m.getType(), m.getDataField(), m.getDateObserved());
		 count ++;
	  }
	  
	  return pList;
  }

  public void getLabWork( Hsfo2Visit visitData, Hsfo2Patient patientData, int patientId )
  {
	  List<Measurement> measurements = measurementDao.findByDemographicId(Integer.valueOf(patientId));
	  
     for(Measurement m : measurements) {
        setLabWorkField( visitData, m.getType(), m.getDataField(), m.getDateObserved());         
      }
  }
  
  
  protected void setLabWorkField( Hsfo2Visit visitData, String type, String dataField, Date dateObserved )
  {
    /**
    +------+-----------+
    | type | dataField |
    +------+-----------+
    | HR   | 64        |
    | BP   | 112/74    |
    | CK   | 355       |
    | CR   | 95        |
    | UA   | 367       |
    | CRP  | 5.7       |
    | Hb   | 125       |
    | FBS  | 5.1       |
    | ALP  | 100       |
    | TSH  | 1.86      |
    | ALT  | 17        |
    | AST  | 33        |
    | B12  | 589       |
    | EGFR | 51        |
    | TG   | 0.75      |
    | TCHL | 3.65      |
    | HDL  | 1.45      |
    | VB12 | 258       |
    | TCHD | 2.5       |
    | LDL  | 1.86      |
    | EGFR | 97        |
    | A1C  | 0.061     |
    | ALT  | 37        |
    **/
    
   
    //HT for height
    if( "HT".equalsIgnoreCase( type ) )
    {
      visitData.setHeight( ConvertUtil.toDouble( dataField ) );
      visitData.setHeight_unit( "cm" );
    }
    
    //SBP, DBP
    /*Don't prepopulate BP in both baseline form and follow up form.
    if( "BP".equalsIgnoreCase( type ) )
    {
      String bps[] = dataField.split( "/" );   //130/79
      if( bps != null && bps.length == 2 )
      {
        visitData.setSBP( ConvertUtil.toInt( bps[0] ) );
        visitData.setDBP( ConvertUtil.toInt( bps[1] ) );
      }
    }
    */
    
 //LDL
    if( "LDL".equalsIgnoreCase( type ) )
    {
      visitData.setLDL( ConvertUtil.toDouble( dataField ) );
      visitData.setLDL_LabresultsDate( dateObserved );      
    }
    
    // TC/HDL
    if( "TCHD".equalsIgnoreCase( type ) )
    {
      visitData.setTC_HDL( ConvertUtil.toDouble( dataField ) );
      visitData.setTC_HDL_LabresultsDate( dateObserved );      
    }
    
    //HDL
    if( "HDL".equalsIgnoreCase( type ) )
    {
      visitData.setHDL( ConvertUtil.toDouble( dataField ) );
      visitData.setHDL_LabresultsDate( dateObserved );
      
    }
    
    //Triglycerides: TRIG -> TG?
    if( "TG".equalsIgnoreCase( type ) )
    {
      visitData.setTriglycerides( ConvertUtil.toDouble( dataField ) );
      visitData.setTC_HDL_LabresultsDate( dateObserved );
      
    }
    
    //A1C
    if( "A1C".equalsIgnoreCase( type ) )
    {
      visitData.setA1C( ConvertUtil.toDouble( dataField )*100 );
      visitData.setA1C_LabresultsDate( dateObserved );
    }
    
    //FBS
    if( "FBS".equalsIgnoreCase( type ) )
    {
      visitData.setFBS( ConvertUtil.toDouble( dataField ) );
      visitData.setA1C_LabresultsDate( dateObserved );
    }

    //eGFR
    if( "EGFR".equalsIgnoreCase( type ) )
    {
      visitData.setEgfr( ConvertUtil.toInt( dataField ) );
      visitData.setEgfrDate( dateObserved );
    }
    
    //ACR - Alb creat ratio (albumin creatinine ratio)
    if( "ACR".equalsIgnoreCase( type ) )    
    {
      visitData.setAcr( ConvertUtil.toInt( dataField ) );
      visitData.setEgfrDate( dateObserved );
    }
  }
  
  public void saveDataToMeasurements( Hsfo2Visit visitData, Hsfo2Patient patientData, int patientId, String providerNo )
  {    
    
    if( visitData != null )
    {
    	//save height;
        double height = visitData.getHeight();
        if( (int)height != 0 )
        {
          if( !"cm".equalsIgnoreCase( visitData.getHeight_unit() ) )
          {
            height *= 2.54;
          }
          saveDataToMeasurements( "HT", String.valueOf( height ), patientId, providerNo );
        }
    	
      //WT: weight
      double weight = visitData.getWeight();
      if( (int)weight != 0 )
      {
        if( !"kg".equalsIgnoreCase( visitData.getWeight_unit() ) )
        {
          weight *= 0.45359237;
        }
        saveDataToMeasurements( "WT", String.valueOf( weight ), patientId, providerNo );
      }
      
      //WAIS  for waist
      double waist = visitData.getWaist();
      if( (int)waist != 0 )
      {
        if( !"cm".equalsIgnoreCase( visitData.getWaist_unit()) )
        {
          waist *= 2.54;
        }
        saveDataToMeasurements( "WAIS", String.valueOf( waist ), patientId, providerNo );
      }
      
    }
    
  }
  
  public void saveDataToMeasurements( String type, String value, int patientId, String providerNo )
  {
	  Measurement m = new Measurement();
	  m.setDemographicId(patientId);
	  m.setType(type);
	  m.setDataField(value);
	  m.setProviderNo(providerNo);
	  m.setDateObserved(new java.sql.Date( Calendar.getInstance().getTimeInMillis() ) );
	  
	  //These fields cannot be null.
	  m.setMeasuringInstruction("");
	  m.setComments("From HSFO");
	  m.setAppointmentNo(0); //Needed?
	  measurementDao.merge(m);
  }
}


