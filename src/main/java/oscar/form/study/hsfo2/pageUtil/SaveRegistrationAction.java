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
package oscar.form.study.hsfo2.pageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.Hsfo2PatientDao;
import org.oscarehr.common.dao.Hsfo2VisitDao;
import org.oscarehr.common.model.Hsfo2Patient;
import org.oscarehr.common.model.Hsfo2Visit;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.form.study.hsfo2.HSFODAO;

/**
 * 
 * Class used to save data from the HSFO Study form
 */
public class SaveRegistrationAction extends DispatchAction
{
	
	Logger logger = MiscUtils.getLogger();
	private static Hsfo2PatientDao hsfo2PatientDao = (Hsfo2PatientDao) SpringUtils.getBean("hsfo2PatientDao");
	
  protected static enum SaveType
  {
    submit( "Submit" ), saveAndEdit( "Save and Continue Editing" );

    private String label;

    private SaveType( String label )
    {
      this.label = label;
    }

    public String getLabel()
    {
      return label;
    }

    public static SaveType getSaveType( String label )
    {
      for ( SaveType saveType : SaveType.values() )
      {
        if ( saveType.getLabel().equalsIgnoreCase( label ) )
          return saveType;
      }
      return null;
    }
  }

  /** Creates a new instance of SaveRegistrationAction */
  public SaveRegistrationAction()
  {
  }

  public boolean parameterHasValue( HttpServletRequest request, String parameterName )
  {
    return ( request.getParameter( parameterName ) != null );
  }

  public Boolean parameterValueEqualsYes( HttpServletRequest request, String parameterName )
  {
    return parameterValueEquals( request, parameterName, "Yes" );
  }
  public Boolean parameterValueEquals( HttpServletRequest request, String parameterName, String value )
  {
    return parameterValueEquals( request, parameterName, value, true );
  }
  
  /**
   *
   * @param request
   * @param parameterName
   * @param value
   * @param caseInsensitive
   * @return null if parameter doesn't set value
   */
  public Boolean parameterValueEquals( HttpServletRequest request, String parameterName, String value, boolean caseInsensitive )
  {
    String parameterValue = request.getParameter( parameterName );
    
    //if both are null, treat as equal
    if( value == parameterValue )
      return true;
    
    if( parameterValue == null )
      return null;
    
    return caseInsensitive ? parameterValue.equalsIgnoreCase( value ) : parameterValue.equals( value );
  }
  
  public boolean isStringEqual( String str, String comp )
  {
    boolean ret = false;
    if ( str != null && str.equals( comp ) )
    {
      ret = true;
    }
    return ret;
  }

  public int getIntFromRequest( HttpServletRequest request, String comp )
  {
    int val = 0;
    String str = request.getParameter( comp );
    if ( str != null && !( str.equals( "" ) ) )
    {
      val = Integer.parseInt( str );
    }
    return val;
  }

  public double getDoubleFromRequest( HttpServletRequest request, String comp )
  {
    double val = 0.0;
    String str = request.getParameter( comp );
    if ( str != null && !( str.equals( "" ) ) )
    {
      val = Double.parseDouble( str );
    }
    return val;
  }

  public Date getDate( SimpleDateFormat formater, String year, String month, String day )
  {
    String dateStr = year + "-" + month + "-" + day;
    java.util.Date parsedDate = null;
    if ( year != null && !year.equals( "" ) )
    {
      try
      {
        parsedDate = formater.parse( dateStr );
      }
      catch ( ParseException pe )
      {
        logger.info( "Error parsing date" );
      }
    }
    return parsedDate;
  }

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response )
  {
    return registrationOrFlowSheet( mapping, form, request, response );
  }

  /**
   * registration and flow sheet are almost same, so, use same method to handle them here.
   */
  public ActionForward registrationOrFlowSheet( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                HttpServletResponse response )
  {
    
    // do different validation for different save type

    
    boolean isRegistrationForm = ConvertUtil.toBoolean( request.getParameter( "isFromRegistrationForm" ) );
   
    String Patient_Id = request.getParameter( "Patient_Id" ).toString().trim();
    
    // ///NEW DATA
    String SiteCode = request.getParameter( "SiteCode" );
    SiteCode = "20";
    
    SimpleDateFormat formater = new SimpleDateFormat( "yyyy-MM-dd" );
    HSFODAO dao = new HSFODAO();
    

    boolean isLastBaseLineRecord = false;
    String providerNo = (String) request.getSession().getAttribute( "user" );
    
    //only registration form allow to update patient data
    if( isRegistrationForm )
    {
    //save only or submit, it's for registration form and stay in that form
      boolean isSaveRegistrationFormOnly = "Save".equalsIgnoreCase( request.getParameter( "Save" ) );
      isLastBaseLineRecord = !isSaveRegistrationFormOnly;
      
      Hsfo2Patient patientData = new Hsfo2Patient();
      patientData = hsfo2PatientDao.getHsfoPatientByPatientId(Patient_Id);
      if(patientData == null)
    	  patientData = new Hsfo2Patient();
      
      patientData.setPatient_Id( Patient_Id );
      
      // Not needed now String Patient_Id = request.getParameter("Patient_Id").toString().trim();
      String FName = request.getParameter( "FName" ).toString().trim();
      String LName = request.getParameter( "LName" ).toString().trim();
  
      String BirthDate_year = request.getParameter( "BirthDate_year" ).toString().trim();
      String BirthDate_month = request.getParameter( "BirthDate_month" ).toString().trim();
      String BirthDate_day = request.getParameter( "BirthDate_day" ).toString().trim();
      Date BirthDate = null;
  
      
      String datestring = BirthDate_year + "-" + BirthDate_month + "-" + BirthDate_day;
  
      try
      {
        java.util.Date parsedDate = formater.parse( datestring );
        java.sql.Date result = new java.sql.Date( parsedDate.getTime() );
        BirthDate = result;
      }
      catch ( ParseException pe )
      {
        logger.info( "Error parsing date" );
      }
      
      String Sex = request.getParameter( "Sex" );
      String PostalCode = request.getParameter( "PostalCode" );
      //double Height = 0.0; // Double.parseDouble(request.getParameter("Height"));
      //String Height_unit = request.getParameter( "Height_unit" );
  
      // Replaced copy and paste coded
      boolean Ethnic_White = parameterHasValue( request, "Ethnic_White" );
      boolean Ethnic_Black = parameterHasValue( request, "Ethnic_Black" );
      boolean Ethnic_EIndian = parameterHasValue( request, "Ethnic_EIndian" );
      boolean Ethnic_Pakistani = parameterHasValue( request, "Ethnic_Pakistani" );
      boolean Ethnic_SriLankan = parameterHasValue( request, "Ethnic_SriLankan" );
      boolean Ethnic_Bangladeshi = parameterHasValue( request, "Ethnic_Bangladeshi" );
      boolean Ethnic_Chinese = parameterHasValue( request, "Ethnic_Chinese" );
      boolean Ethnic_Japanese = parameterHasValue( request, "Ethnic_Japanese" );
      boolean Ethnic_Korean = parameterHasValue( request, "Ethnic_Korean" );
      boolean Ethnic_Hispanic = parameterHasValue( request, "Ethnic_Hispanic" );
      boolean Ethnic_FirstNation = parameterHasValue( request, "Ethnic_FirstNation" );
      boolean Ethnic_Other = parameterHasValue( request, "Ethnic_Other" );
      boolean Ethnic_Refused = parameterHasValue( request, "Ethnic_Refused" );
      boolean Ethnic_Unknown = parameterHasValue( request, "Ethnic_Unknown" );
      
      String PharmacyName = ""; // request.getParameter("PharmacyName");
      String PharmacyLocation = ""; // request.getParameter("PharmacyLocation");
      String sel_TimeAgoDx = request.getParameter( "sel_TimeAgoDx" );
     // String EmrHCPId1 = request.getParameter( "EmrHCPId1" );
      //String EmrHCPId2 = request.getParameter( "EmrHCPId2" );
      //String EmrHCPId = EmrHCPId1 + "-" + EmrHCPId2;
      String EmrHCPId = request.getParameter( "EmrHCPId2" ); //should be the patient's doctor id
      
      Date consentDate = new Date(); // getDate(formater,consentDate_year,consentDate_month,consentDate_day);
      
      // store data in object
      patientData.setPatientData( SiteCode, Patient_Id, FName, LName, BirthDate, Sex, PostalCode, 
                                  Ethnic_White, Ethnic_Black, Ethnic_EIndian, Ethnic_Pakistani, Ethnic_SriLankan,
                                  Ethnic_Bangladeshi, Ethnic_Chinese, Ethnic_Japanese, Ethnic_Korean, Ethnic_Hispanic,
                                  Ethnic_FirstNation, Ethnic_Other, Ethnic_Refused, Ethnic_Unknown, PharmacyName,
                                  PharmacyLocation, sel_TimeAgoDx, EmrHCPId, consentDate );
      
      //added data for hsfo2:
      String dateOfHmpStatusYear = request.getParameter( "dateOfHmpStatus_year" );
      String dateOfHmpStatusMonth = request.getParameter( "dateOfHmpStatus_month" );
      String dateOfHmpStatusDay = request.getParameter( "dateOfHmpStatus_day" );
      Date dateOfHmpStatus = getDate( formater, dateOfHmpStatusYear, dateOfHmpStatusMonth, dateOfHmpStatusDay );
      
      patientData.setDateOfHmpStatus( dateOfHmpStatus );
      patientData.setStatusInHmp( request.getParameter( "statusInHmp" ) );      
      
      
      // distinguish save or submit;
      patientData.setSubmitted( !isSaveRegistrationFormOnly );
      
      try
      {
        dao.savePatient( patientData, providerNo );
      }
      catch ( Exception e )
      {
        logger.info(e.getMessage());
      }
    }


    String VisitDate_Id_year = request.getParameter( "VisitDate_Id_year" );
    String VisitDate_Id_month = request.getParameter( "VisitDate_Id_month" );
    String VisitDate_Id_day = request.getParameter( "VisitDate_Id_day" );
    Date VisitDate_Id = null;

    String visitdatestring = VisitDate_Id_year + "-" + VisitDate_Id_month + "-" + VisitDate_Id_day;
    try
    {
      java.util.Date parsedDate = formater.parse( visitdatestring );
      java.sql.Date result = new java.sql.Date( parsedDate.getTime() );
      VisitDate_Id = result;
    }
    catch ( ParseException pe )
    {
      logger.info( "Error parsing date" );
    }
    
    String Drugcoverage = request.getParameter( "Drugcoverage" );
    int SBP = getIntFromRequest( request, "SBP" );
    int SBP_goal = getIntFromRequest( request, "SBP_goal" );
    int DBP = getIntFromRequest( request, "DBP" );
    int DBP_goal = getIntFromRequest( request, "DBP_goal" );
    String Bptru_used = request.getParameter( "Bptru_used" );

    double Height = getDoubleFromRequest( request, "Height" );
    String Height_unit = request.getParameter( "Height_unit" );
    double Weight = getDoubleFromRequest( request, "Weight" );
    String Weight_unit = request.getParameter( "Weight_unit" );
    double Waist = getDoubleFromRequest( request, "Waist" );
    String Waist_unit = request.getParameter( "Waist_unit" );
    double TC_HDL = getDoubleFromRequest( request, "TC_HDL" );
    double LDL = getDoubleFromRequest( request, "LDL" );
    double HDL = getDoubleFromRequest( request, "HDL" );
    double Triglycerides = getDoubleFromRequest ( request, "Triglycerides");
    double A1C = getDoubleFromRequest( request, "A1C" );
    String Nextvisit = request.getParameter( "Nextvisit" );

    boolean Bpactionplan = parameterHasValue( request, "Bpactionplan" );
    boolean PressureOff = parameterHasValue( request, "PressureOff" );
    boolean PatientProvider = parameterHasValue( request, "PatientProvider" );
    boolean ABPM = parameterHasValue( request, "ABPM" );
    boolean Home = parameterHasValue( request, "Home" );
    boolean CommunityRes = parameterHasValue( request, "CommunityRes" );
    boolean ProRefer = parameterHasValue( request, "ProRefer" );
    String HtnDxType = request.getParameter( "HtnDxType" );
    boolean Dyslipid = parameterHasValue( request, "Dyslipid" );
    boolean Diabetes = parameterHasValue( request, "Diabetes" );
    boolean KidneyDis = parameterHasValue( request, "KidneyDis" );
    boolean Obesity = parameterHasValue( request, "Obesity" );
    boolean CHD = parameterHasValue( request, "CHD" );
    boolean Stroke_TIA = parameterHasValue( request, "Stroke_TIA" );
    Boolean Risk_weight = parameterValueEqualsYes( request, "Risk_weight" );
    Boolean Risk_activity = parameterValueEqualsYes( request, "Risk_activity" );
    Boolean Risk_diet = parameterValueEqualsYes( request, "Risk_diet" );
    Boolean Risk_smoking = parameterValueEqualsYes( request, "Risk_smoking" );
    Boolean Risk_alcohol = parameterValueEqualsYes( request, "Risk_alcohol" );
    Boolean Risk_stress = parameterValueEqualsYes( request, "Risk_stress" );
    String PtView = request.getParameter( "PtView" );

    int Change_importance = getIntFromRequest( request, "Change_importance" );
    int Change_confidence = getIntFromRequest( request, "Change_confidence" );
    int exercise_minPerWk = getIntFromRequest( request, "exercise_minPerWk" );
    int smoking_cigsPerDay = getIntFromRequest( request, "smoking_cigsPerDay" );

    int alcohol_drinksPerWk = getIntFromRequest( request, "alcohol_drinksPerWk" );

    String sel_DashDiet = request.getParameter( "sel_DashDiet" );
    String sel_HighSaltFood = request.getParameter( "sel_HighSaltFood" );
    String sel_Stressed = request.getParameter( "sel_Stressed" );
    String LifeGoal = request.getParameter( "LifeGoal" );

    boolean FamHx_Htn = parameterHasValue( request, "FamHx_Htn" );
    boolean FamHx_Dyslipid = parameterHasValue( request, "FamHx_Dyslipid" );
    boolean FamHx_Diabetes = parameterHasValue( request, "FamHx_Diabetes" );
    boolean FamHx_KidneyDis = parameterHasValue( request, "FamHx_KidneyDis" );
    boolean FamHx_Obesity = parameterHasValue( request, "FamHx_Obesity" );
    boolean FamHx_CHD = parameterHasValue( request, "FamHx_CHD" );
    boolean FamHx_Stroke_TIA = parameterHasValue( request, "FamHx_Stroke_TIA" );
    boolean Diuret_rx = parameterHasValue( request, "Diuret_rx" );
    boolean Diuret_SideEffects = parameterHasValue( request, "Diuret_SideEffects" );
    String Diuret_RxDecToday = request.getParameter( "Diuret_RxDecToday" );

    boolean Ace_rx = parameterHasValue( request, "Ace_rx" );
    boolean Ace_SideEffects = parameterHasValue( request, "Ace_SideEffects" );
    String Ace_RxDecToday = request.getParameter( "Ace_RxDecToday" );

    boolean Arecept_rx = parameterHasValue( request, "Arecept_rx" );
    boolean Arecept_SideEffects = parameterHasValue( request, "Arecept_SideEffects" );
    String Arecept_RxDecToday = request.getParameter( "Arecept_RxDecToday" );

    boolean Beta_rx = parameterHasValue( request, "Beta_rx" );
    boolean Beta_SideEffects = parameterHasValue( request, "Beta_SideEffects" );
    String Beta_RxDecToday = request.getParameter( "Beta_RxDecToday" );

    boolean Calc_rx = parameterHasValue( request, "Calc_rx" );
    boolean Calc_SideEffects = parameterHasValue( request, "Calc_SideEffects" );
    String Calc_RxDecToday = request.getParameter( "Calc_RxDecToday" );

    boolean Anti_rx = parameterHasValue( request, "Anti_rx" );
    boolean Anti_SideEffects = parameterHasValue( request, "Anti_SideEffects" );
    String Anti_RxDecToday = request.getParameter( "Anti_RxDecToday" );
    boolean Statin_rx = parameterHasValue( request, "Statin_rx" );
    boolean Statin_SideEffects = parameterHasValue( request, "Statin_SideEffects" );
    String Statin_RxDecToday = request.getParameter( "Statin_RxDecToday" );

    boolean Lipid_rx = parameterHasValue( request, "Lipid_rx" );
    boolean Lipid_SideEffects = parameterHasValue( request, "Lipid_SideEffects" );
    String Lipid_RxDecToday = request.getParameter( "Lipid_RxDecToday" );

    boolean Hypo_rx = parameterHasValue( request, "Hypo_rx" );
    boolean Hypo_SideEffects = parameterHasValue( request, "Hypo_SideEffects" );
    String Hypo_RxDecToday = request.getParameter( "Hypo_RxDecToday" );
    boolean Insul_rx = parameterHasValue( request, "Insul_rx" );
    boolean Insul_SideEffects = parameterHasValue( request, "Insul_SideEffects" );
    String Insul_RxDecToday = request.getParameter( "Insul_RxDecToday" );

    boolean ASA_rx = parameterHasValue( request, "ASA_rx" );
    boolean ASA_SideEffects = parameterHasValue( request, "ASA_SideEffects" );
    
    int Often_miss = getIntFromRequest( request, "Often_miss" );

    String Herbal = request.getParameter( "Herbal" );

    String TC_HDL_LabresultsDate_year = request.getParameter( "TC_HDL_LabresultsDate_year" );
    String TC_HDL_LabresultsDate_month = request.getParameter( "TC_HDL_LabresultsDate_month" );
    String TC_HDL_LabresultsDate_day = request.getParameter( "TC_HDL_LabresultsDate_day" );
    Date TC_HDL_LabresultsDate = getDate( formater, TC_HDL_LabresultsDate_year, TC_HDL_LabresultsDate_month,
                                          TC_HDL_LabresultsDate_day );

    String LDL_LabresultsDate_year = request.getParameter( "LDL_LabresultsDate_year" );
    String LDL_LabresultsDate_month = request.getParameter( "LDL_LabresultsDate_month" );
    String LDL_LabresultsDate_day = request.getParameter( "LDL_LabresultsDate_day" );
    Date LDL_LabresultsDate = getDate( formater, LDL_LabresultsDate_year, LDL_LabresultsDate_month,
                                       LDL_LabresultsDate_day );

    String HDL_LabresultsDate_year = request.getParameter( "HDL_LabresultsDate_year" );
    String HDL_LabresultsDate_month = request.getParameter( "HDL_LabresultsDate_month" );
    String HDL_LabresultsDate_day = request.getParameter( "HDL_LabresultsDate_day" );
    Date HDL_LabresultsDate = getDate( formater, HDL_LabresultsDate_year, HDL_LabresultsDate_month,
                                       HDL_LabresultsDate_day );

    String A1C_LabresultsDate_year = request.getParameter( "A1C_LabresultsDate_year" );
    String A1C_LabresultsDate_month = request.getParameter( "A1C_LabresultsDate_month" );
    String A1C_LabresultsDate_day = request.getParameter( "A1C_LabresultsDate_day" );
    Date A1C_LabresultsDate = getDate( formater, A1C_LabresultsDate_year, A1C_LabresultsDate_month,
                                       A1C_LabresultsDate_day );


    //eGFR and ACR is the same date
    String eGFRDateYear = request.getParameter( "egfrYear" );
    String egfrDateMonth = request.getParameter( "egfrMonth" );
    String egfrDateDay = request.getParameter( "egfrDay" );
    Date egfrDate = getDate( formater, eGFRDateYear, egfrDateMonth, egfrDateDay );
    
    // determine if data should be locked
   //String savestring = request.getParameter( "Save" );
    
    //String submitstring = request.getParameter( "Submit" );
    
    boolean locked = false;
    boolean monitor = parameterHasValue( request, "monitor");
    
    Hsfo2Visit visitData = new Hsfo2Visit();
    Hsfo2VisitDao visitDao = (Hsfo2VisitDao) SpringUtils.getBean("hsfo2VisitDao");
    String visit_id = request.getParameter("visit_form_id");
    if(visit_id!=null && Integer.parseInt(visit_id)!=0)
    	visitData = visitDao.getHsfoVisitById(Integer.parseInt(visit_id));
    if(visitData == null) 
    	visitData = new Hsfo2Visit();
    
    // store data in object
    visitData.setVisitData( 
                            Patient_Id,
                            VisitDate_Id,
                            null,
                            null,
                            Drugcoverage, // enum('yes', 'no');
                            SBP,
                            SBP_goal,
                            DBP,
                            DBP_goal,
                            Bptru_used, // enum('yes', 'no');
                            Height,
                            Height_unit,
                            Weight, // double(3, 1);
                            Weight_unit, // enum('kg', 'lb');
                            Waist,// double(3, 1);
                            Waist_unit, // enum('cm', 'inch');
                            TC_HDL, // double(2, 1);
                            LDL, // double(2, 1);
                            HDL, // double(1, 1);
                            A1C, // double(1, 2);,
                            Nextvisit, Bpactionplan, PressureOff,
                            PatientProvider,
                            ABPM,
                            Home,
                            CommunityRes,
                            ProRefer,
                            HtnDxType, // enum('PrimaryHtn', 'ElevatedBpReadings');
                            Dyslipid, Diabetes, KidneyDis, Obesity, CHD,
                            Stroke_TIA,
                            Risk_weight,
                            Risk_activity,
                            Risk_diet,
                            Risk_smoking,
                            Risk_alcohol,
                            Risk_stress,
                            PtView, // enum('Uninterested', 'Thinking', 'Deciding', 'TakingAction', 'Maintaining',
                                    // 'Relapsing');
                            Change_importance,
                            Change_confidence,
                            exercise_minPerWk,
                            smoking_cigsPerDay,
                            alcohol_drinksPerWk,
                            sel_DashDiet, // enum('Always', 'Often', 'Sometimes', 'Never');
                            sel_HighSaltFood, // enum('Always', 'Often', 'Sometimes', 'Never');
                            sel_Stressed, // enum('Always', 'Often', 'Sometimes', 'Never');
                            LifeGoal,
                            FamHx_Htn, // enum('PrimaryHtn', 'ElevatedBpReadings');
                            FamHx_Dyslipid, FamHx_Diabetes, FamHx_KidneyDis, FamHx_Obesity, FamHx_CHD,
                            FamHx_Stroke_TIA, Diuret_rx,
                            Diuret_SideEffects,
                            Diuret_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start',
                                               // 'InClassSwitch');
                            Ace_rx,
                            Ace_SideEffects,
                            Ace_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Arecept_rx, Arecept_SideEffects, Arecept_RxDecToday, Beta_rx,
                            Beta_SideEffects,
                            Beta_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Calc_rx, Calc_SideEffects,
                            Calc_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Anti_rx, Anti_SideEffects,
                            Anti_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Statin_rx, Statin_SideEffects,
                            Statin_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start',
                                               // 'InClassSwitch');
                            Lipid_rx, Lipid_SideEffects,
                            Lipid_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Hypo_rx, Hypo_SideEffects,
                            Hypo_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            Insul_rx, Insul_SideEffects,
                            Insul_RxDecToday, // enum('Same', 'Increase', 'Decrease', 'Stop', 'Start', 'InClassSwitch');
                            ASA_rx, ASA_SideEffects, Often_miss, Herbal, TC_HDL_LabresultsDate, LDL_LabresultsDate, HDL_LabresultsDate,
                            A1C_LabresultsDate, locked, Triglycerides, monitor );

    // ======= add new data information which are added for hsfo2
    boolean depression = parameterHasValue( request, "Depression" );
    visitData.setDepression( depression );
    boolean famHx_depression = parameterHasValue( request, "FamHx_Depression" );
    visitData.setFamHx_Depression( famHx_depression );
    
    String assessActivity = request.getParameter( "assessActivity" );
    visitData.setAssessActivity( ConvertUtil.toInteger( assessActivity ) );
    String assessSmoking = request.getParameter( "assessSmoking" );
    visitData.setAssessSmoking( ConvertUtil.toInteger( assessSmoking ) );
    String assessAlcohol = request.getParameter( "assessAlcohol" );
    visitData.setAssessAlcohol( ConvertUtil.toInteger( assessAlcohol ) );

    visitData.setFBSP1( ConvertUtil.toInt( request.getParameter( "FBSP1" ) ) );
    visitData.setFBSP2( ConvertUtil.toInt( request.getParameter( "FBSP2" ) ) );
    
    visitData.setEgfrDate( egfrDate );
    visitData.setMonitor( ConvertUtil.toBoolean( request.getParameter( "monitor" ) ) );

    visitData.setEgfr( ConvertUtil.toInt( request.getParameter( "egfr" ) ) );
    visitData.setAcrP1( ConvertUtil.toInt( request.getParameter( "acrP1" ) ) );
    visitData.setAcrP2( ConvertUtil.toInt( request.getParameter( "acrP2" ) ) );
    
    visitData.setNextVisitInMonths( ConvertUtil.toInt( request.getParameter( "nextVisitInMonths" ) ) );
    visitData.setNextVisitInWeeks( ConvertUtil.toInt( request.getParameter( "nextVisitInWeeks" ) ) );
    
    
    // some data format already changed
    visitData.setA1CP1( ConvertUtil.toInt( request.getParameter( "A1CP1" ) ) );
    visitData.setA1CP2( ConvertUtil.toInt( request.getParameter( "A1CP2" ) ) );
 
    visitData.setHeightP1( ConvertUtil.toInt( request.getParameter( "HeightP1" ) ) );
    visitData.setHeightP2( ConvertUtil.toInt( request.getParameter( "HeightP2" ) ) );

    visitData.setWeightP1( ConvertUtil.toInt( request.getParameter( "WeightP1" ) ) );
    visitData.setWeightP2( ConvertUtil.toInt( request.getParameter( "WeightP2" ) ) );

    visitData.setWaistP1( ConvertUtil.toInt( request.getParameter( "WaistP1" ) ) );
    visitData.setWaistP2( ConvertUtil.toInt( request.getParameter( "WaistP2" ) ) );

    visitData.setLDLP1( ConvertUtil.toInt( request.getParameter( "LDLP1" ) ) );
    visitData.setLDLP2( ConvertUtil.toInt( request.getParameter( "LDLP2" ) ) );

    visitData.setTC_HDLP1( ConvertUtil.toInt( request.getParameter( "TC_HDLP1" ) ) );
    visitData.setTC_HDLP2( ConvertUtil.toInt( request.getParameter( "TC_HDLP2" ) ) );

    visitData.setHDLP1( ConvertUtil.toInt( request.getParameter( "HDLP1" ) ) );
    visitData.setHDLP2( ConvertUtil.toInt( request.getParameter( "HDLP2" ) ) );
    
    visitData.setTriglyceridesP1( ConvertUtil.toInt( request.getParameter( "TriglyceridesP1" ) ) );
    visitData.setTriglyceridesP2( ConvertUtil.toInt( request.getParameter( "TriglyceridesP2" ) ) );
    
    visitData.setSel_DashDiet( request.getParameter( "assessDash" ) );
    visitData.setSel_HighSaltFood( request.getParameter( "assessSalt" ) );
    visitData.setSel_Stressed( request.getParameter( "assessStressed" ) );
    
    visitData.setASA_RxDecToday( request.getParameter( "ASA_RxDecToday" ) );
    /*?????
    if(visitData.isLastBaseLineRecord()) {
    	visitData.setId(null);
    	
    }*/
    visitData.setLastBaseLineRecord( isLastBaseLineRecord );
    visitData.setProvider_no(providerNo);
    visitData.setDemographic_no(Integer.parseInt(Patient_Id));
    
    // ======= end add new data information which are added in version2

    try
    {
      int insertId = dao.insertVisit( visitData, (String) request.getSession().getAttribute( "user" ) );
      request.setAttribute( "formId", new Integer( insertId ) );
    }
    catch ( Exception e )
    {
      logger.info(e.getMessage());
    }
    
    request.setAttribute( "demographic_no", visitData.getPatient_Id() );
    request.setAttribute( "visitDate", visitData.getVisitDate_Id() );
    // request.setAttribute("fromURL", "SubmitFollowup");
   
    if ( request.getParameter( "Save" ) != null && request.getParameter( "Save" ).equals( "Assessment Complete" ) )
    {
      return mapping.findForward( "exit" );
    }

    return mapping.findForward( "success" );
  }

}
