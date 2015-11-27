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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Hsfo2Patient;
import org.oscarehr.common.model.Hsfo2Visit;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.form.study.hsfo2.HSFODAO;
import oscar.form.study.hsfo2.PatientList;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.UtilDateUtilities;


/**
 * 
 * Class used to fill data for the HSFO form Study
 */
public class ManageHSFOAction extends Action
{ 
	Logger logger = MiscUtils.getLogger();

  public static enum FORM
  {
    registration, flowsheet, graphs
  }

  /** Creates a new instance of ManageHSFOAction */
  public ManageHSFOAction()
  {
  }
  
  protected Integer getFormIdFromRequest( HttpServletRequest request )
  {
    Integer formId = null;
  
    if ( request.getParameter( "formId" ) != null )
    {
      formId = Integer.parseInt( request.getParameter( "formId" ) );
    }
    if ( formId == null && request.getAttribute( "formId" ) != null )
    {
      formId = (Integer) request.getAttribute( "formId" );
    }
    return formId;
  }

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response ) throws Exception
  {
    logger.info( "ContextPath: " + request.getContextPath() );
    logger.info( "pathInfo: " + request.getPathInfo() );
   // Map< String, String[] > params = request.getParameterMap();


    Hsfo2Visit latestHsfo2Visit = new Hsfo2Visit();
    PatientList historyList = new PatientList();
    // RecordList record = new RecordList();
    //List recordList = new LinkedList();

    String patientId = (String) request.getAttribute( "demographic_no" );
    if ( patientId == null )
    {
      patientId = request.getParameter( "demographic_no" );
    }
    String isfirstrecord = "";
    boolean isFirstRecord = false;
    String user = (String) request.getSession().getAttribute( "user" );
   
    HSFODAO hsfoDAO = new HSFODAO();
    isFirstRecord = hsfoDAO.isFirstRecord( patientId );

    DemographicData demoData = new DemographicData();
    //DemographicData.Demographic de = demoData.getDemographic( patientId );
    Demographic de = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), patientId);
    
    boolean isDisplayGraphs = "displayGraphs".equalsIgnoreCase( request.getParameter( "operation" ) );
    
    
    
    boolean isFromRegistrationForm = false;
    if ( "true".equalsIgnoreCase( request.getParameter( "isFromRegistrationForm" ) ) )
    {
      //true means the request is from registration form, should goto followup form
      isFromRegistrationForm = true;
    }
    
    FORM forwardToForm = null;
    int patientHistorySize = 0;
    boolean isFirstVisitRecordForThePatient = false;
//    boolean isFromRegistrationForm = false;  
    
    Integer formId = getFormIdFromRequest( request );
    Hsfo2Visit formHsfo2Visit = null;
    if( formId != null )
      formHsfo2Visit = hsfoDAO.retrieveSelectedRecord( formId );    
    boolean isHistoryForm = !isFromRegistrationForm && ( formId != null && formHsfo2Visit != null);
    
    if( formId != null )
      isFirstVisitRecordForThePatient = hsfoDAO.isFirstVisitRecordForThePatient( patientId, formId );
    boolean isRegistForm = !isDisplayGraphs && !isFromRegistrationForm && ( isFirstRecord || isFirstVisitRecordForThePatient );
    
    //prepare data
    Hsfo2Patient hsfo2Patient = hsfoDAO.retrievePatientRecord( patientId );
    if(hsfo2Patient == null) 
    	hsfo2Patient = new Hsfo2Patient();
    List patientHistory = hsfoDAO.retrieveVisitRecord( patientId );

    //save only or submit, it's for registration form and stay in that form
    boolean isSaveOnly = "Save".equalsIgnoreCase( request.getParameter( "Save" ) );
    if( !isSaveOnly && !isFirstRecord )
    {
      isSaveOnly = !hsfo2Patient.isSubmitted();
    }
    
    if( isSaveOnly )
    {
      //stay in regist form and treat as history
      isRegistForm = true;
      isHistoryForm = true;
      if( patientHistory.size() > 0 )
        formHsfo2Visit = (Hsfo2Visit)patientHistory.get( patientHistory.size() - 1 );
    }
    
    if( isHistoryForm )
    {
      latestHsfo2Visit = formHsfo2Visit;
    }
    else  // create new form
    {
      patientHistorySize = patientHistory.size();

      if( patientHistorySize >= 1 )
      {
        latestHsfo2Visit = (Hsfo2Visit) patientHistory.get( patientHistorySize - 1 );
        latestHsfo2Visit.setVisitDateIdToday();        
        latestHsfo2Visit.setId(hsfoDAO.getMaxVisitId()+1);
        cleanNonePrefilledData( latestHsfo2Visit );  
        getLabWork( latestHsfo2Visit, hsfo2Patient, ConvertUtil.toInt( patientId ) );      
        
        //If it's followup form, BP should not be prepopulated. Clean again.
        latestHsfo2Visit.setSBP(0);
        latestHsfo2Visit.setDBP(0); 
      }
      else
      {
        latestHsfo2Visit = new Hsfo2Visit();
        latestHsfo2Visit.setVisitDateIdToday();
        getLabWork( latestHsfo2Visit, hsfo2Patient, ConvertUtil.toInt( patientId ) );
      }
      
            
     
    }
    
    
    if ( isRegistForm )
    {
      // registration, get data from DemographicData;
      isfirstrecord = "true";
      
      hsfo2Patient.setPatient_Id( patientId );
      
      if( !isHistoryForm )
      {
        hsfo2Patient.setFName( de.getFirstName() );
        hsfo2Patient.setLName( de.getLastName() );       
        hsfo2Patient.setBirthDate( oscar.util.DateUtils.toDate(de.getFormattedDob()) );
        hsfo2Patient.setSex( de.getSex() );
        hsfo2Patient.setPostalCode( de.getPostal() );
        
        hsfo2Patient.setRegistrationId( HsfoUtil.getRegistrationId() );
        latestHsfo2Visit.setVisitDateIdToday();
      }
      
      request.setAttribute( "EmrHCPId1", user );
      request.setAttribute( "EmrHCPId2", de.getProviderNo() ); // TODO: may need to convert to provider name
      
      forwardToForm = FORM.registration;
    }
    else
    {
      //populate graphy data for followup form. the latestHsfo2Visit already keep the information of last visit.
      isfirstrecord = "false";

      if( !isDisplayGraphs )
        forwardToForm = FORM.flowsheet;
      else
      {
        // If patientHistory is greater than 1 than fill the graphing arrays
        TimeSeries sbpSeries = new TimeSeries( "Systolic Blood Pressure", Day.class );
        TimeSeries dbpSeries = new TimeSeries("Diastolic Blood Pressure", Day.class);
        TimeSeries bmiSeries = new TimeSeries("BMI", Day.class);
        TimeSeries waistSeries = new TimeSeries("Waist", Day.class);
        TimeSeries ldlSeries = new TimeSeries("LDL", Day.class);
        TimeSeries tcHdlSeries = new TimeSeries("TC/HDL", Day.class);
        TimeSeries importanceSeries = new TimeSeries("Importance", Day.class);
        TimeSeries confidenceSeries = new TimeSeries("Confidence", Day.class);
        
        Map<GraphDesc,TimeSeries> graphDescSeriesMap = new HashMap<GraphDesc,TimeSeries>();
        graphDescSeriesMap.put( new GraphDesc( "Systolic Blood Pressure", "Dates", "SBP(mmHg)"), sbpSeries );
        graphDescSeriesMap.put( new GraphDesc( "Diastolic Blood Pressure", "Dates", "DBP(mmHg)"), dbpSeries );
        graphDescSeriesMap.put( new GraphDesc( "BMI", "Dates", "BMI(kg/m2)"), bmiSeries );
        graphDescSeriesMap.put( new GraphDesc( "Waist", "Dates", "Waist(cm)"), waistSeries );
        graphDescSeriesMap.put( new GraphDesc( "LDL", "Dates", "LDL(mmol/L)"), ldlSeries );
        {
          GraphDesc tcHdlDesc = new GraphDesc( "TC/HDL", "Dates", "TC/HDL(ratio)");
          tcHdlDesc.setFileName( "TC_HDL" );
          graphDescSeriesMap.put( tcHdlDesc, tcHdlSeries );
        }
        graphDescSeriesMap.put( new GraphDesc( "Importance", "Dates", "Importance(1-10)"), importanceSeries );
        graphDescSeriesMap.put( new GraphDesc( "Confidence", "Dates", "Confidence(1-10)"), confidenceSeries );
        
        if ( patientHistorySize >= 1 )
        {
  
          ListIterator patientHistoryIt = patientHistory.listIterator();
  //        int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;
          while ( patientHistoryIt.hasNext() )
          {
            Hsfo2Visit Hsfo2Visit = (Hsfo2Visit) patientHistoryIt.next();
  //          visitDateArray.add( setDateFull( Hsfo2Visit.getVisitDate_Id() ) );
  //          visitIdArray.add( "" + Hsfo2Visit.getID() );
  
            // ////////
            final Date visitDate = Hsfo2Visit.getVisitDate_Id();
            if ( visitDate != null )
            {
              final Day visitDay = new Day( visitDate );
              if ( Hsfo2Visit.getSBP() != 0 )
              {
                sbpSeries.addOrUpdate( visitDay, Hsfo2Visit.getSBP() );
              }
  
              if ( Hsfo2Visit.getDBP() != 0 )
              {
                dbpSeries.addOrUpdate( visitDay, Hsfo2Visit.getDBP() );
              }
  
              if ( (int)Hsfo2Visit.getWeight() != 0 )
              {
                Double bmi = getBmi( Hsfo2Visit, hsfo2Patient );
                if( bmi > 0 )
                  bmiSeries.addOrUpdate( visitDay, bmi ); 
              }
              // modified by victor for waist_unit null bug,2007
              // if (Hsfo2Visit.getWaist() != 0{
              if ( (int)Hsfo2Visit.getWaist() != 0 && Hsfo2Visit.getWaist_unit() != null )
              {
                double waistv = Hsfo2Visit.getWaist();
                String waistunit = Hsfo2Visit.getWaist_unit();
                double waist = 0.0;
                if ( waistunit.equals( "cm" ) )
                {
                  waist = waistv;
                }
                else
                {
                  // 1 inch = 2.54 cm
                  waist = waistv * 2.54;
                }
                waistSeries.addOrUpdate( visitDay, waist );
              }
  
              if ( Hsfo2Visit.getChange_importance() != 0 )
              {
                importanceSeries.addOrUpdate( visitDay, Hsfo2Visit.getChange_importance() );
              }
  
              if ( Hsfo2Visit.getChange_confidence() != 0 )
              {
                confidenceSeries.addOrUpdate( visitDay, Hsfo2Visit.getChange_confidence() );
              }
            }
  
            final Date labResultDate = Hsfo2Visit.getTC_HDL_LabresultsDate();
            if ( labResultDate != null )
            {
              final Day labResultDay = new Day( labResultDate );
              if ( (int)Hsfo2Visit.getTC_HDL() != 0 )
              {
                tcHdlSeries.addOrUpdate( labResultDay, Hsfo2Visit.getTC_HDL() );
              }
              
              if ( (int)Hsfo2Visit.getLDL() != 0 )
              {
                ldlSeries.addOrUpdate( labResultDay, Hsfo2Visit.getLDL() );
              }
            }
  
          }
         
        }
        
        //generate the graph and export as picture.
        generateGraphs( request, response, graphDescSeriesMap );
        forwardToForm = FORM.graphs;;
      }
      
    }

    historyList.setPatientHistory( patientHistory );

    
    // set request attributes to forward to jsp
    request.setAttribute( "siteId", OscarProperties.getInstance().getProperty( "hsfo2.loginSiteCode", "xxx" ) );
    
    request.setAttribute( "Hsfo2Patient", hsfo2Patient );
    request.setAttribute( "historyList", historyList );
    request.setAttribute( "Hsfo2Visit", latestHsfo2Visit );   //getDay() is date of week
    request.setAttribute( "isFirstRecord", isfirstrecord );

    return mapping.findForward( forwardToForm.name() );
  }

  protected void cleanNonePrefilledData( Hsfo2Visit Hsfo2Visit )
  {
    if( Hsfo2Visit == null )
      return;
    
    //assessment of CV risk factor
    Hsfo2Visit.setAssessActivity( null );
    Hsfo2Visit.setAssessAlcohol( null );
    Hsfo2Visit.setAssessSmoking( null );
    
    //Always Often
    Hsfo2Visit.setSel_HighSaltFood ( null );    
    Hsfo2Visit.setSel_DashDiet ( null );
    Hsfo2Visit.setSel_Stressed ( null );
    
    //Patient View
    Hsfo2Visit.setPtView ( null );    
    
    //Current assessment of selected lifestyle goal- Importance and Confidence
    Hsfo2Visit.setChange_importance( 0 );
    Hsfo2Visit.setChange_confidence( 0 );
    
    //Physical Exam Measures Except height
    Hsfo2Visit.setDBP( 0 );
    Hsfo2Visit.setSBP( 0 );
    Hsfo2Visit.setWeight( 0.0 );
    Hsfo2Visit.setWeight_unit( "" );
    Hsfo2Visit.setWaist( 0.0 );
    Hsfo2Visit.setWaist_unit( "" );
   
    
    //How often does patient miss taking his/her meds
    Hsfo2Visit.setOften_miss( 0 );
    
    //Next Visit in
    Hsfo2Visit.setNextVisitInMonths( 0 );
    Hsfo2Visit.setNextVisitInWeeks( 0 );
    
    //labwork: only prefilled if the data get from oscar emr
    Hsfo2Visit.setTC_HDL_LabresultsDate( null );
    Hsfo2Visit.setLDL( 0 );
    Hsfo2Visit.setTC_HDL( 0 );
    Hsfo2Visit.setHDL( 0 );
    Hsfo2Visit.setTriglycerides( 0 );
    
    Hsfo2Visit.setA1C_LabresultsDate( null );
    Hsfo2Visit.setA1C(0.0);
    Hsfo2Visit.setFBS( 0.0 );
    
    Hsfo2Visit.setEgfrDate( null );
    Hsfo2Visit.setEgfr( 0 );
    Hsfo2Visit.setAcr( 0.0 );
        
    //Data fields that is NOT PREFILLED in the follow up form (do not flow from baseline to follow up/ one follow up to next follow up)
    //    Side effects
    //    Rx decision
    Hsfo2Visit.setDiuret_SideEffects( false );
    Hsfo2Visit.setDiuret_RxDecToday( "" );
    Hsfo2Visit.setAce_SideEffects( false );
    Hsfo2Visit.setAce_RxDecToday( "" );
    Hsfo2Visit.setArecept_SideEffects( false );
    Hsfo2Visit.setArecept_RxDecToday( "" );
    Hsfo2Visit.setBeta_SideEffects( false );
    Hsfo2Visit.setBeta_RxDecToday( "" );
    Hsfo2Visit.setCalc_SideEffects( false );
    Hsfo2Visit.setCalc_RxDecToday( "" );
    Hsfo2Visit.setAnti_SideEffects( false );
    Hsfo2Visit.setAnti_RxDecToday( "" );
    Hsfo2Visit.setStatin_SideEffects( false );
    Hsfo2Visit.setStatin_RxDecToday( "" );
    Hsfo2Visit.setLipid_SideEffects( false );
    Hsfo2Visit.setLipid_RxDecToday( "" );
    Hsfo2Visit.setHypo_SideEffects( false );
    Hsfo2Visit.setHypo_RxDecToday( "" );
    Hsfo2Visit.setInsul_SideEffects( false );
    Hsfo2Visit.setInsul_RxDecToday( "" );
    Hsfo2Visit.setASA_SideEffects( false );
    Hsfo2Visit.setASA_RxDecToday( "" );
    Hsfo2Visit.setMonitor(null);
  }
  
  // return 0 if invalid
  protected double getBmi( Hsfo2Visit Hsfo2Visit, Hsfo2Patient Hsfo2Patient )
  {
    double weight = Hsfo2Visit.getWeight();
    String weightunit = Hsfo2Visit.getWeight_unit();
    double heightr = 0;
    heightr = Hsfo2Visit.getHeight();
    String heightunit = Hsfo2Visit.getHeight_unit();
    
    double height = 0.0;

    if ( heightunit != null )
    {
      // convert height to meter
      if ( heightunit.equals( "cm" ) )
      {
        height = heightr / 100;
      }
      else
      {
        // 1 inch = 0.0254 meter
        height = heightr * 0.0254;
      }
    }
    if ( weightunit != null )
    {
      double bmi;
      if ( weightunit.equals( "kg" ) )
      {
        bmi = weight / ( height * height );
      }
      else
      {
        // 1 kilogram = 2.20462262 pound
        bmi = ( weight / 2.20462262 ) / ( height * height );
      }
      
//      BigDecimal b = new BigDecimal(bmi).setScale(2,BigDecimal.ROUND_HALF_UP);
//      return b.doubleValue();
      return bmi;
    }
    return 0.0;   //invalid
  }
  
 /**
  * get the most recent lab work of this patient
  * @param hsfo2Visit
  * @param hsfo2Patient
  * @param patientId
  */
  protected void getLabWork( Hsfo2Visit hsfo2Visit, Hsfo2Patient hsfo2Patient, int patientId )
  {
    HSFODAO hsfoDao = new HSFODAO();
    hsfoDao.getLabWork( hsfo2Visit, hsfo2Patient, patientId );
  }
  
  public void generateGraphs( HttpServletRequest request,
                              HttpServletResponse response,
                              Map<GraphDesc,TimeSeries> graphDescSeriesMap )
  {
    if( graphDescSeriesMap == null || graphDescSeriesMap.size() == 0 )
      return;
    
    OscarProperties props = OscarProperties.getInstance();
    String graphDirPath = props.getProperty( "hsfo2.generategraph.dir", "/hsfo2Graphs" );
    //graphDirPath = this.getServlet().getServletContext().getContextPath() + "/" + graphDirPath;
    //graphDirPath = request.getContextPath() + "/" + graphDirPath; 
    
    String graphDirRealPath = getServlet().getServletContext().getRealPath( graphDirPath );
    //make sure the directory exists
    File graphDir = new File( graphDirRealPath );
    if( !graphDir.exists() || !graphDir.isDirectory() )
    {
      graphDir.mkdirs();
    }
    
    for( Map.Entry<GraphDesc,TimeSeries> entry : graphDescSeriesMap.entrySet() )
    {
      //one dataset only contain one series here
      GraphDesc graphDesc = entry.getKey();
      TimeSeriesCollection dataset = new TimeSeriesCollection();
      dataset.addSeries( entry.getValue() );
      
      JFreeChart chart = ChartFactory.createTimeSeriesChart( graphDesc.getGraphTitle(), graphDesc.getXAxisLabel(), 
                                                             graphDesc.getYAxisLabel(), dataset, true, true, true);
      //might need to adjust the XYPlot, see Line 459 of MeasurementGraphAction2
      try
      {
        String fileName = graphDesc.getFileName() + "." + Calendar.getInstance().getTimeInMillis() + ".jpg";
        String realFilePath = graphDirRealPath + "/" + fileName;        
        ChartUtilities.saveChartAsJPEG( new File( realFilePath ), chart, 900,  200 );
        logger.info( "graph file: " + realFilePath + " generated and saved. " );
        
        request.setAttribute( "graphFile."+graphDesc.getFileName(), request.getContextPath() + "/" + graphDirPath + "/" + fileName );
      }
      catch ( IOException e )
      {
        logger.error( "Problem in creating chart: " + e.toString() );
      }
      
    } 
  }
  
 
  protected String setDate( Date visitDate )
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( visitDate );
    return setDate( cal.get( Calendar.MONTH ), cal.get( Calendar.YEAR ) + 1900 );
  }

  // method to convert the date
  protected String setDate( int mnth, int year )
  {
    
    String date = "";
    String month = "";
    String yr = "";

    yr = Integer.toString( year );
    yr = yr.substring( 2 );

    switch ( mnth )
    {
      case 0:
        month = "Jan";
        break;
      case 1:
        month = "Feb";
        break;
      case 2:
        month = "Mar";
        break;
      case 3:
        month = "Apr";
        break;
      case 4:
        month = "May";
        break;
      case 5:
        month = "Jun";
        break;
      case 6:
        month = "Jul";
        break;
      case 7:
        month = "Aug";
        break;
      case 8:
        month = "Sep";
        break;
      case 9:
        month = "Oct";
        break;
      case 10:
        month = "Nov";
        break;
      case 11:
        month = "Dec";
        break;
      default:
        month = " ";
        break;
    }

    date = month + "-" + yr;

    return date;
  }

  protected String setDateFull( Date visitDate )
  {
    return UtilDateUtilities.DateToString( visitDate, "yyyy-MMM-dd" );
  }

  // method to convert the date
  protected String setDate( int mnth, int day, int year )
  {
    String date = "";
    String month = "";

    switch ( mnth )
    {
      case 0:
        month = "Jan";
        break;
      case 1:
        month = "Feb";
        break;
      case 2:
        month = "Mar";
        break;
      case 3:
        month = "Apr";
        break;
      case 4:
        month = "May";
        break;
      case 5:
        month = "Jun";
        break;
      case 6:
        month = "Jul";
        break;
      case 7:
        month = "Aug";
        break;
      case 8:
        month = "Sep";
        break;
      case 9:
        month = "Oct";
        break;
      case 10:
        month = "Nov";
        break;
      case 11:
        month = "Dec";
        break;
      default:
        month = " ";
        break;
    }

    date = month + " " + day + ", " + year;

    return date;
  }

}
