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


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.dao.ProviderDAO;
import org.hsfo.v2.HsfHmpDataDocument;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.Hsfo2RecommitScheduleDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Hsfo2RecommitSchedule;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.struts.DispatchActionSupport;

import oscar.OscarProperties;
import oscar.form.study.hsfo2.pageUtil.XMLTransferUtil.SoapElementKey;

public class RecommitHSFOAction extends DispatchActionSupport
{

  static Logger logger = MiscUtils.getLogger();
  private static Hsfo2RecommitScheduleDao rd = (Hsfo2RecommitScheduleDao) SpringUtils.getBean("hsfo2RecommitScheduleDao");
  
  public ActionForward showSchedule( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response )
  {
    LazyValidatorForm sform = (LazyValidatorForm) form;
    
    boolean sflag = false;// whether have current schedule
    boolean cflag = true;// whether copy the demographic info to
    // hsfo_patient    
    
    String sdate = "";
    String shour = "03";
    String smin = "30";
    Hsfo2RecommitSchedule rsd = rd.getLastSchedule( true );
    boolean lastlog_flag = false;
    String lastlog_time = "";
    String lastlog = "";
    if ( rsd != null )
    {
      lastlog_flag = true;
      SimpleDateFormat sf1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      lastlog_time = sf1.format( rsd.getSchedule_time() );
      lastlog = rsd.getMemo();
    }
    sform.set( "lastlog_flag", lastlog_flag ? "true" : "false" );
    sform.set( "lastlog_time", lastlog_time );
    sform.set( "lastlog", lastlog != null ? lastlog : "" );

    Integer sid = new Integer( 0 );
    if ( rd.isLastActivExpire() )
      rd.deActiveLast();
    Hsfo2RecommitSchedule rs = rd.getLastSchedule( false );
    if ( rs != null && !"D".equalsIgnoreCase( rs.getStatus() ) )
    {
      sflag = true;
      Date dd = rs.getSchedule_time();
      SimpleDateFormat sf1 = new SimpleDateFormat( "yyyy-MM-dd" );
      SimpleDateFormat sf2 = new SimpleDateFormat( "HH" );
      SimpleDateFormat sf3 = new SimpleDateFormat( "mm" );
      sdate = sf1.format( dd );
      shour = sf2.format( dd );
      smin = sf3.format( dd );
      cflag = rs.isCheck_flag();
      sid = rs.getId();
    }
    sform.set( "schedule_flag", sflag ? "true" : "false" );// already have
    // a current
    // schedule
    sform.set( "check_flag", cflag ? "true" : "false" );
    sform.set( "isCheck", cflag ? "true" : "false" );
    sform.set( "schedule_date", sdate );
    sform.set( "schedule_shour", shour );
    sform.set( "schedule_min", smin );
    sform.set( "schedule_id", sid.toString() );

    return mapping.findForward( "schedulePage" );
  }

  public ActionForward saveSchedule( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response )  throws Exception
  {
    //Hsfo2RecommitScheduleDao rd = new Hsfo2RecommitScheduleDao();
    Hsfo2RecommitSchedule rs = new Hsfo2RecommitSchedule();
    LazyValidatorForm sform = (LazyValidatorForm) form;
    String user = (String) request.getSession().getAttribute( "user" );
    String sid = (String) sform.get( "schedule_id" );
    String sdate = (String) sform.get( "schedule_date" );
    String shour = (String) sform.get( "schedule_shour" );
    String smin = (String) sform.get( "schedule_min" );
    
    SimpleDateFormat sf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
    try{
	    Date stime = sf.parse( sdate + " " + shour + ":" + smin );
	    
	    if ( stime.before( new Date() ) )
	    {
	      request.setAttribute( "schedule_err", "Invalid time,please schedule another time!" );
	      return showSchedule( mapping, form, request, response );
	    }
    
	    rs.setStatus( "A" );
	    rs.setUser_no( user );
	    String copyflag = (String) sform.get( "isCheck" );
	    if ( copyflag != null && "false".equalsIgnoreCase( copyflag ) )
	      rs.setCheck_flag( false );
	    else
	      rs.setCheck_flag( true );
	
	    rs.setSchedule_time( stime );
	
	    Integer id = new Integer( sid );
	    if ( id.intValue() != 0 )
	    {
	      rs.setId( id );
	      rd.updateLastSchedule( rs );
	    }
	    else
	      rd.insertchedule( rs );
	 } catch (Exception e) {
	    	logger.info(e.getMessage());	    	
	 
    }
    request.setAttribute( "schedule_message", "successfully update!" );

    oscar.form.study.hsfo2.pageUtil.HsfoQuartzServlet.schedule();

    return mapping.findForward( "schedulePage" );
  }

  public static class ResubmitJob implements Job
  {

    public void execute( JobExecutionContext ctx ) throws JobExecutionException
    {    	
    	String providerNo = OscarProperties.getInstance().getProperty("hsfo_job_run_as_provider");
		if(providerNo == null) {
			return;
		}
		
		ProviderDAO providerDao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = providerDao.getProvider(providerNo);
		
		if(provider == null) {
			return;
		}
		
		SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
		List<Security> securityList = securityDao.findByProviderNo(providerNo);
		
		if(securityList.isEmpty()) {
			return;
		}
		
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(securityList.get(0));
		
		

      try
      {
        XMLTransferUtil tfutil = new XMLTransferUtil();
        //Hsfo2RecommitScheduleDao rDao = new Hsfo2RecommitScheduleDao();
        
        Hsfo2RecommitSchedule rs = rd.getLastSchedule( false );
        // if no schedule, do nothing
        if ( rs == null )
          return;

        String retS = null;
        //if ( !"D".equalsIgnoreCase( rs.getStatus() ) )
        //{
          rs.setStatus( "D" );
          rs.setSchedule_time(new Date());
          
          if ( rs.isCheck_flag() )
            retS = rd.SynchronizeDemoInfo(x);
          else
            retS = rd.checkProvider(x);

          if ( retS != null )
          {
            rs.setMemo( "Upload failure. Missing internal doctor for patient " + retS + "." );
            rd.updateLastSchedule( rs );
            return;
          }
          
          // send to hsfo web

          try
          {
            StringBuilder memoMsg = new StringBuilder();
            boolean updateSuccess = uploadXmlToServer( tfutil, rs.getUser_no(), 0, memoMsg );
            
            rs.setMemo( memoMsg.toString() );
            rd.updateLastSchedule( rs );
          }
          catch ( Exception e )
          {
            logger.info( e.getMessage() );
            throw e;
          }
          
       // }
      }
      catch ( Exception e )
      {
         logger.info(e.getMessage());
      }
      finally
      {
    	  DbConnectionFilter.releaseAllThreadDbResources();
      }
    }

  }
  
  public static Calendar toCalendar( String sDate )
  {
    return toCalendar( sDate, "yyyy-MM-dd" );
  }
  public static Calendar toCalendar( String sDate, String datePattern )
  {
    if ( sDate == null || sDate.length() == 0 ) 
    {
      return null;
    }

    SimpleDateFormat oFormat = new SimpleDateFormat(datePattern);

    try
    {
      Date oDate = oFormat.parse( sDate );
      Calendar calendar = Calendar.getInstance();
      calendar.setTime( oDate );
      return calendar;
    }
    catch( Exception e )
    {
      return null;
    }
  }
  
  public static boolean uploadXmlToServer( XMLTransferUtil tfutil, String providerNo, Integer demographicNo, StringBuilder memoMsg ) throws Exception
  {
    //GetDataDateRange
    Map< SoapElementKey, Object > output = tfutil.soapHttpCallGetDataDateRange( tfutil.getSiteID().intValue(), tfutil.getUserId(), 
                                                                                tfutil.getLoginPasswd() );
    if( (Integer)output.get( SoapElementKey.responseStatusCode ) != 200 || !output.get( SoapElementKey.GetDataDateRangeResult ).equals( "0" ) )
    {
      memoMsg.append( " GetDataDateRange status code: " + output.get( SoapElementKey.responseStatusCode ) 
                      + "; GetDataDateRange result code: " + output.get( SoapElementKey.GetDataDateRangeResult ) );
      return false;
    }
    
    // DataVault
    String sBeginDate = (String)output.get( SoapElementKey.DataBeginDate );
    String sEndDate = (String)output.get( SoapElementKey.DataEndDate );
    Calendar beginDate = toCalendar( sBeginDate );
    Calendar endDate = toCalendar( sEndDate );
    
    HsfHmpDataDocument doc = tfutil.generateDataVaultXML( providerNo, demographicNo, beginDate, endDate );

    if ( doc == null )
    {
      memoMsg.append( " Patient(s) data not found in the database." );
      return false;
    }
    
    List message = tfutil.validateDoc( doc );
    if ( message.size() != 0 )
    {
      memoMsg.append( message.get( 0 ).toString() );
      return false;
    }
    
    String rstr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + doc.xmlText();
    
    output = tfutil.soapHttpCallDataVault( tfutil.getSiteID().intValue(), tfutil.getUserId(), tfutil.getLoginPasswd(), rstr );
    
    if( (Integer)output.get( SoapElementKey.responseStatusCode ) != 200 || !"0".equals( output.get( SoapElementKey.GetDataDateRangeResult ) ) )
    {
      memoMsg.append( "DataVault status code: " + output.get( SoapElementKey.responseStatusCode ) 
    		  + " DataVaultStatusStrResult: " + output.get( SoapElementKey.DataVaultStatusStrResult) 
    		  + " StatusMessage:" + output.get( SoapElementKey.StatusMessage) );  
      
    } else {
    	memoMsg.append( "DataVault status code: " + output.get( SoapElementKey.responseStatusCode ) 
      		  + " DataVaultStatusStrResult: " + output.get( SoapElementKey.DataVaultStatusStrResult) 
      		  + " StatusMessage: " + output.get( SoapElementKey.StatusMessage) 
      		  + " DataBeginDate: " + output.get( SoapElementKey.DataBeginDate)
      		  + " DataEndDate: " + output.get( SoapElementKey.DataEndDate) );
    }
    return true;
  }

  public ActionForward test( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                             HttpServletResponse response ) 
  {

    return null;
  }
}
