<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page language="java" import="java.util.*, java.io.*, oscar.*" errorPage="errorpage.jsp" %>

<jsp:useBean id="loginBean" scope="page" class="oscar.Login">
  <jsp:setProperty name="loginBean" property="*" />
</jsp:useBean>
<jsp:useBean id="monitor" scope="session" class="java.util.HashMap" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<jsp:useBean id="loginInfo" scope="application" class="java.util.Hashtable" />
//monitor is used to trace the session available even if no logout

<%
  String displaypage="flogin.htm";
    oscarVariables.setProperty("file_separator", System.getProperty("file.separator"));
    oscarVariables.setProperty("working_dir", System.getProperty("user.dir"));
    char sep = oscarVariables.getProperty("file_separator").toCharArray()[0];
    String strTemp = oscarVariables.getProperty("working_dir").substring(0,oscarVariables.getProperty("working_dir").indexOf(sep));
    try {
      oscarVariables.load(new FileInputStream(strTemp + sep + "root" + sep +"oscar_sfhc.properties")); //change to speciallll name
    } catch(Exception e) {System.out.println("*** No Property File ***"); }

  //judge the local network
  boolean bWAN = true ;
  if(request.getRemoteAddr().startsWith(oscarVariables.getProperty("login_local_ip")) ) bWAN = false ;
  loginBean.setIp(request.getRemoteAddr());
  
  //delete the old entry in the loginfo bean
  GregorianCalendar now = new GregorianCalendar();
  LoginInfoBean info = null;
  String sTemp = null;
  if(!loginInfo.isEmpty() && bWAN) {
    for (Enumeration e = loginInfo.keys() ; e.hasMoreElements() ;) {
      sTemp = (String) e.nextElement();
      info = (LoginInfoBean) loginInfo.get(sTemp );
      if(info.getTimeOutStatus(now) ) loginInfo.remove(sTemp);
    }
    //check if it is blocked
    if( ((LoginInfoBean) loginInfo.get( request.getRemoteAddr())).getStatus() ==0 )  {
      response.sendRedirect("block.htm");
      return;
    }
  }

  //authenticate is used to check password
  loginBean.setVariables(oscarVariables);
  String[] strAuth = loginBean.authenticate();
  if(strAuth!=null) { //login successfully
    if(monitor.containsKey(strAuth[0])) {
      //there is an old seesion for the user, refresh - no logout by a user
      HttpSession oldSession = (HttpSession)monitor.get(strAuth[0]);
      try { // will get error if finding action of maxInactiveInterval
        if(oldSession!=null)  oldSession.invalidate(); 
      }catch (Exception e) {;}
    }
  
    session.setAttribute("user", strAuth[0]);
    session.setAttribute("userfirstname", strAuth[1]);
    session.setAttribute("userlastname", strAuth[2]);
    session.setAttribute("userprofession", strAuth[3]);
    
    monitor.put(strAuth[0], session);
    System.out.println("Assigned new session for: " + strAuth[0]+ " : "+ strAuth[3] );

    //GregorianCalendar now=new GregorianCalendar();
    int nowYear =now.get(Calendar.YEAR);
    int nowMonth =now.get(Calendar.MONTH)+1 ; //be care for the month +-1
    int nowDay=now.get(Calendar.DAY_OF_MONTH);

    if(strAuth[3].equalsIgnoreCase("receptionist")|| strAuth[3].equalsIgnoreCase("doctor")) { 
      //get preferences from preference table
      String [] strPreferAuth = loginBean.getPreferences();
      session.setAttribute("starthour", strPreferAuth[0]);
      session.setAttribute("endhour", strPreferAuth[1]);
      session.setAttribute("everymin", strPreferAuth[2]);
      session.setAttribute("groupno", strPreferAuth[3]);
	if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
      session.setAttribute("newticklerwarningwindow", strPreferAuth[4]);
	}
    }

    if(strAuth[3].equalsIgnoreCase("receptionist")) { // go to receptionist directory
    	displaypage = "receptionist/receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";
    } else if (strAuth[3].equalsIgnoreCase("doctor")) { // go to provider directory


// pda is from here
      //displaypage="main.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";   //session.setAttribute("refreshurl", displaypage);
	  int curYear ;
	  int curMonth ;
	  int curDay ;   
          String strYear=null, strMonth=null, strDay=null;
	    curYear = now.get(Calendar.YEAR);
	    curMonth = (now.get(Calendar.MONTH)+1);
	    curDay = now.get(Calendar.DAY_OF_MONTH);
    String curMonthString ; 
      if (curMonth<10){
        curMonthString="0"+new Integer(curMonth).toString();
      }else{
        curMonthString= new Integer(curMonth).toString();
      }
      String curDayString ; 
      if (curDay<10){
        curDayString="0"+new Integer(curDay).toString();
      }else{
        curDayString= new Integer(curDay).toString();
      }

      String todayString = curYear+"-"+curMonthString+"-"+curDayString;
      displaypage="AppointmentToday.jsp?todayString="+todayString;

      //displaypage="provider/providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";   //session.setAttribute("refreshurl", displaypage);
    } else if (strAuth[3].equalsIgnoreCase("admin")) { // go to admin directory
      displaypage="admin/admin.jsp"; //for the administrator
    } else { // go to normal directory
      displaypage="logout.jsp";
    }
  } else {  //login failed
    if(bWAN) {
      if(loginInfo.get( request.getRemoteAddr()) == null ) {
        info = new LoginInfoBean(now, Integer.parseInt(oscarVariables.getProperty("login_max_failed_times")), Integer.parseInt(oscarVariables.getProperty("login_max_duration")));
      } else {
        info = (LoginInfoBean) loginInfo.get(request.getRemoteAddr() );
        info.updateLoginInfoBean(now, 1);
      }
      loginInfo.put( request.getRemoteAddr(), info);
    System.out.println(request.getRemoteAddr()+"  status: "+ ((LoginInfoBean) loginInfo.get( request.getRemoteAddr())).getStatus() + " times: "+ info.getTimes()+ " time: " );
    }
  }
  response.sendRedirect(displaypage);
%>
