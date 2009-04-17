<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="java.util.*,java.net.*, oscar.util.*"
	errorPage="errorpage.jsp"%>

<caisi:isModuleLoad moduleName="caisi">
<%
    String isOscar = request.getParameter("infirmaryView_isOscar");
    if (session.getAttribute("infirmaryView_isOscar")==null) isOscar="false";
    if (isOscar!=null) session.setAttribute("infirmaryView_isOscar", isOscar);
    session.setAttribute(SessionConstants.CURRENT_PROGRAM_ID,request.getParameter(SessionConstants.CURRENT_PROGRAM_ID));
    session.setAttribute("infirmaryView_OscarURL",request.getRequestURL());

%><c:import url="/infirm.do?action=getSig" />
</caisi:isModuleLoad>

<%
    if(session.getAttribute("userrole") == null ) {
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
            response.sendRedirect("../logout.jsp");
            return;
        }
    %>
</security:oscarSec>

<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("../logout.jsp");
        return;
    }

    if(request.getParameter("year")==null && request.getParameter("month")==null && request.getParameter("day")==null && request.getParameter("displaymode")==null && request.getParameter("dboperation")==null) {
        GregorianCalendar now=new GregorianCalendar();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH)+1 ; //be care for the month +-1
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        response.sendRedirect("./providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday");
        return;
    }

    //associate each operation with an output JSP file - displaymode
    String[][] opToFile = new String[][] {
            {"day" , "appointmentprovideradminday.jsp"},
            {"month" , "appointmentprovideradminmonth.jsp"},
            {"addstatus" , "provideraddstatus.jsp"},
            {"updatepreference" , "providerupdatepreference.jsp"},
            {"displaymygroup" , "providerdisplaymygroup.jsp"},
            {"encounter" , "providerencounter.jsp"},
            {"prescribe" , "providerprescribe.jsp"},
            {"encountersingle" , "providerencountersingle.jsp"},
            {"vary" , request.getParameter("displaymodevariable")==null?"":URLDecoder.decode(request.getParameter("displaymodevariable")) },
            {"saveform" , "providersaveform.jsp"},
            {"saveencounter" , "providersaveencounter.jsp"},
            {"savebill" , "providersavebill.jsp"},
            {"saveprescribe" , "providersaveprescribe.jsp"},
            {"savedemographicaccessory" , "providersavedemographicaccessory.jsp"},
            {"encounterhistory" , "providerencounterhistory.jsp"},
            {"savedeletetemplate" , "providertemplate.jsp"},
            {"savetemplate" , "providersavetemplate.jsp"},
            {"savedeleteform" , "providerform.jsp"},
            {"save_encounterform" , "providersaveencounterform.jsp"},
            {"ar1" , "formar1_99_12.jsp"},
            {"ar2" , "formar2_99_08.jsp"},
            {"newgroup" , "providernewgroup.jsp"},
            {"savemygroup" , "providersavemygroup.jsp"},
//    {"billingobstetric" , "billingobstetric.jsp"},

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