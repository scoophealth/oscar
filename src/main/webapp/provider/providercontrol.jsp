<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="java.util.*,java.net.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.OscarProperties" %>

<caisi:isModuleLoad moduleName="caisi">
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    String isOscar = request.getParameter("infirmaryView_isOscar");
    if (session.getAttribute("infirmaryView_isOscar")==null) isOscar="false";
    if (isOscar!=null) session.setAttribute("infirmaryView_isOscar", isOscar);
    if(request.getParameter(SessionConstants.CURRENT_PROGRAM_ID) != null) {
    	session.setAttribute(SessionConstants.CURRENT_PROGRAM_ID,request.getParameter(SessionConstants.CURRENT_PROGRAM_ID));
    	org.caisi.core.web.InfirmAction.updateCurrentProgram(request.getParameter(SessionConstants.CURRENT_PROGRAM_ID), loggedInInfo.getLoggedInProviderNo());
    }
    session.setAttribute("infirmaryView_OscarURL",request.getRequestURL());

%><c:import url="/infirm.do?action=getSig" />
</caisi:isModuleLoad>

<%
    if(session.getAttribute("userrole") == null ) {
   	 	MiscUtils.getLogger().error("userrole is null? logging user out");
        response.sendRedirect("../logout.jsp");
        return;
    }
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    if(roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
        response.sendRedirect("er_clerk.jsp");
        return;
    }

    if(roleName$.indexOf("Vaccine Provider") != -1) {
        response.sendRedirect("vaccine_provider.jsp");
        return;
    }
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="r" reverse="<%=true%>">
	<%
        if (true)
        {
      	  	MiscUtils.getLogger().error("wrong role? logging user out");
            response.sendRedirect("../logout.jsp");
            return;
        }
    %>
</security:oscarSec>

<%
    if(session.getAttribute("user") == null) {
 	  	MiscUtils.getLogger().error("missing session user? logging user out");
        response.sendRedirect("../logout.jsp");
        return;
    }

	//preserving context for new ui this is temp until schedule is created in new ui
	String router = "";
	String record = "";
	String module = "";
	//retained date as well  
	
	if(request.getParameter("record")!=null){
		record="record="+request.getParameter("record");
	}
	
	if(request.getParameter("module")!=null){
		module="module=" + request.getParameter("module");
	}
	
	if(!record.equals("") && !module.equals("")){
		router = "&" + record + "&" + module;
	}else if(record.equals("") && !module.equals("")){
		router = "&" + module;
	}
	//preserving context ---end

    if(request.getParameter("year")==null && request.getParameter("month")==null && request.getParameter("day")==null && request.getParameter("displaymode")==null && request.getParameter("dboperation")==null) {
        GregorianCalendar now=new GregorianCalendar();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH)+1 ; //be care for the month +-1
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        OscarProperties props = OscarProperties.getInstance();
        String caisiView = null;
        caisiView = request.getParameter("GoToCaisiViewFromOscarView");
        boolean viewAll_bool = true;  // false, restore original schedule view on appointment screen
        
        if (props.getProperty("default_schedule_viewall", "").startsWith("false") )
            viewAll_bool = false;
            
        if(caisiView!=null && "true".equals(caisiView)) {
        	if (viewAll_bool){
	        	response.sendRedirect("./providercontrol.jsp?GoToCaisiViewFromOscarView=true&year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1");
	        	return;
        	}
        	else{
	        	response.sendRedirect("./providercontrol.jsp?GoToCaisiViewFromOscarView=true&year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday&viewall=0");
	        	return;
	        }
        }
        if (viewAll_bool){
        	       	
	        response.sendRedirect("./providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1" + router);
	        return;
	    }
	    else{
	    	response.sendRedirect("./providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday&viewall=0" + router);
	        return;
	    }
	        
    }

    //associate each operation with an output JSP file - displaymode
    String[][] opToFile = new String[][] {
            {"day" , "appointmentprovideradminday.jsp"},
            {"month" , "appointmentprovideradminmonth.jsp"},
            {"addstatus" , "provideraddstatus.jsp"},
            {"updatepreference" , "providerupdatepreference.jsp"},
            {"displaymygroup" , "providerdisplaymygroup.jsp"},
            {"encounter" , "providerencounter.jsp"},
            {"encountersingle" , "providerencountersingle.jsp"},
            {"vary" , request.getParameter("displaymodevariable")==null?"":URLDecoder.decode(request.getParameter("displaymodevariable")) },
            {"saveencounter" , "providersaveencounter.jsp"},
            {"savebill" , "providersavebill.jsp"},
            {"savedemographicaccessory" , "providersavedemographicaccessory.jsp"},
            {"encounterhistory" , "providerencounterhistory.jsp"},
            {"savedeletetemplate" , "providertemplate.jsp"},
            {"ar1" , "formar1_99_12.jsp"},
            {"ar2" , "formar2_99_08.jsp"},
            {"newgroup" , "providernewgroup.jsp"},
            {"savemygroup" , "providersavemygroup.jsp"}

    };

    // create an operation-to-file dictionary
    UtilDict opToFileDict = new UtilDict();
    opToFileDict.setDef(opToFile);

    // create a request parameter name-to-value dictionary
    UtilDict requestParamDict = new UtilDict();
    requestParamDict.setDef(request);

    // get operation name from request
    String operation = requestParamDict.getDef("displaymode","");

    // redirect to a file associated with operation
    pageContext.forward(opToFileDict.getDef(operation,""));
%>
