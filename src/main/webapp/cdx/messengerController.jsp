
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IProvider" %>
<%@ page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@ page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IClinic" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.casemgmt.model.Issue" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.util.StringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.integration.cdx.PatientOtherInfo" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%
    String providerNo = (String)session.getAttribute("user");
    java.util.ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
    boolean useNewCmgmt = false;
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    CaseManagementManager cmgmtMgr = null;
    if (users != null && users.size() > 0 && (users.get(0).equalsIgnoreCase("all") || Collections.binarySearch(users, providerNo) >= 0))
    {
        useNewCmgmt = true;
        cmgmtMgr = (CaseManagementManager)ctx.getBean("caseManagementManager");
    }

    UserPropertyDAO userPropertyDAO = (UserPropertyDAO)ctx.getBean("UserPropertyDAO");
    UserProperty fmtProperty = userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_REQ_PASTE_FMT);
    String pasteFmt = fmtProperty != null?fmtProperty.getValue():null;
%>


<%!protected String listNotes(CaseManagementManager cmgmtMgr, String code, String providerNo, String demoNo)
{
    // filter the notes by the checked issues
    List<Issue> issues = cmgmtMgr.getIssueInfoByCode(providerNo, code);

    String[] issueIds = new String[issues.size()];
    int idx = 0;
    for (Issue issue : issues)
    {
        issueIds[idx] = String.valueOf(issue.getId());
    }

    // need to apply issue filter
    List<CaseManagementNote> notes = cmgmtMgr.getNotes(demoNo, issueIds);
    StringBuffer noteStr = new StringBuffer();
    for (CaseManagementNote n : notes)
    {
        if (!n.isLocked() && !n.isArchived()) noteStr.append(n.getNote() + "\n");
    }

    return noteStr.toString();
}%>

<%
    //to get allergies, family history at cdxmessenger.
    if(request.getParameter("demoid")!=null && !request.getParameter("demoid").isEmpty() && request.getParameter("type")!=null && !request.getParameter("type").isEmpty()) {
        String value = "";
        String cleanString="";
        String demographyid = request.getParameter("demoid");
        String type = request.getParameter("type");
        PatientOtherInfo otherInfo=PatientOtherInfo.valueOf(type);
        switch(otherInfo){
            case FamilyHistory:
                if (OscarProperties.getInstance().getBooleanProperty("caisi", "on"))
                {
                    oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                    value = EctInfo.getFamilyHistory();
                }
                else
                {
                    if (useNewCmgmt)
                    {
                        value = listNotes(cmgmtMgr, "FamHistory", providerNo, demographyid);
                    }
                    else
                    {
                        oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                        value = EctInfo.getFamilyHistory();
                    }
                }
                if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
                {
                    value = StringUtils.lineBreaks(value);
                }
                value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                cleanString = StringEscapeUtils.unescapeJava(value);
   %>
            <p><%=cleanString%></p>
      <%
            break;

          case MedicalHistory:
               if (useNewCmgmt)
               {
                   value = listNotes(cmgmtMgr, "MedHistory", providerNo, demographyid);
               }
               else
               {
                   oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request), demographyid);
                   value = EctInfo.getMedicalHistory();
               }
               if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
               {
                   value = StringUtils.lineBreaks(value);
               }
               value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
              cleanString = StringEscapeUtils.unescapeJava(value);
      %>
<p><%=cleanString%></p>
<%
               break;
    case ongoingConcerns:
                if (useNewCmgmt)
                {
                    value = listNotes(cmgmtMgr, "Concerns", providerNo, demographyid);
                }
                else
                {
                    oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                    value = EctInfo.getOngoingConcerns();
                }
                if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
                {
                    value = StringUtils.lineBreaks(value);
                }
                value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);

        cleanString = StringEscapeUtils.unescapeJava(value);
%>
<p><%=cleanString%></p>
<%
                break;
    case SocialHistory:
                if (useNewCmgmt)
                {
                    value = listNotes(cmgmtMgr, "SocHistory", providerNo, demographyid);
                }
                else
                {
                    oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                    value = EctInfo.getSocialHistory();
                }
                if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
                {
                    value = StringUtils.lineBreaks(value);
                }
                value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);

        cleanString = StringEscapeUtils.unescapeJava(value);
%>
<p><%=cleanString%></p>
<%
                break;
    case OtherMeds:
                if (OscarProperties.getInstance().getBooleanProperty("caisi", "on"))
                {
                    value = "";
                }
                else
                {
                    if (useNewCmgmt)
                    {
                        value = listNotes(cmgmtMgr, "OMeds", providerNo, demographyid);
                    }
                    else
                    {
                        //family history was used as bucket for Other Meds in old encounter
                        oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                        value = EctInfo.getFamilyHistory();
                    }
                }
                if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
                {
                    value = StringUtils.lineBreaks(value);
                }
                value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
        cleanString = StringEscapeUtils.unescapeJava(value);
%>
<p><%=cleanString%></p>
<%
                break;
    case Reminders:
                if (useNewCmgmt)
                {
                    value = listNotes(cmgmtMgr, "Reminders", providerNo, demographyid);
                }
                else
                {
                    oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demographyid);
                    value = EctInfo.getReminders();
                }
                //if( !value.equals("") ) {
                if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
                {
                    value = StringUtils.lineBreaks(value);
                }


                value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
        cleanString = StringEscapeUtils.unescapeJava(value);
%>
<p><%=cleanString%></p>
<%
                break;

}

       }

   %>





<%
    if(request.getParameter("demoName")!=null && !request.getParameter("demoName").isEmpty()){
        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        List<Demographic> demoList=null;


        String demoName = request.getParameter("demoName");
        String lastFirst[]=demoName.split(",");
        String demoid=null;
        demoList=demographicDao.searchDemographicByFullName(lastFirst[0]);
        for(int i=0;i<demoList.size();i++){
            if(demoList.get(i).getFirstName().equalsIgnoreCase(lastFirst[1].trim())){
                demoid=demoList.get(i).getId().toString();
                break;
            }
        }

%>
<li class="list-group-item" id="demoid"><%=demoid%></li>
<%
    }



    if(request.getParameter("patient")!=null && !request.getParameter("patient").isEmpty() )
    {

    DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    List<Demographic> demoList=null;

        String patient = request.getParameter("patient");
        demoList=demographicDao.searchDemographicByFullName(patient);
        for(int i=0;i<demoList.size();i++){



%>
<li class="list-group-item" ><%=demoList.get(i).getFullName()%></li>
<%
        }
    }

    else if(request.getParameter("recipient")!=null && !request.getParameter("recipient").isEmpty()) {

            OscarProperties props = OscarProperties.getInstance();
            boolean showCdx = "bc".equalsIgnoreCase(props.getProperty("billregion"));

            String searchString = request.getParameter("recipient");
            //MiscUtils.getLogger().debug("recipient: " + searchString);
            List<IProvider> providers = null;
            if (showCdx) {
                CDXSpecialist cdxSpecialist = new CDXSpecialist();
                providers = cdxSpecialist.findCdxSpecialistByName(searchString);
                if(providers!=null && !providers.isEmpty()){
                for (IProvider provider : providers) {
                 //   String cdxSpecId = provider.getID();
                   // MiscUtils.getLogger().debug("cdxSpecId: " + cdxSpecId);
                    //String fName = provider.getFirstName();
                    //String lName = provider.getLastName();

                    if(provider.getFirstName()!=null && !provider.getFirstName().isEmpty()){


                    %>
   <li class="list-group-item"><%=provider.getLastName()+" "+provider.getFirstName()%></li>
   <%
               }
                }
           }
                else{
   %>
<li class="list-group-item">No result found !</li>
     <%

               }


            }
        }

    else if(request.getParameter("names")!=null && !request.getParameter("names").isEmpty()) {

                   String searchString = request.getParameter("names");
                   String[] lastfirst = searchString.split(" ",2);
                   OscarProperties props = OscarProperties.getInstance();
                   boolean showCdx = "bc".equalsIgnoreCase(props.getProperty("billregion"));
                   List<IProvider> providers = null;
                   if (showCdx) {
                       CDXSpecialist cdxSpecialist = new CDXSpecialist();
                       providers = cdxSpecialist.findCdxSpecialistByName(lastfirst[0].trim());
                       for (IProvider provider : providers) {
                           //   String cdxSpecId = provider.getID();
                           // MiscUtils.getLogger().debug("cdxSpecId: " + cdxSpecId);
                           //String fName = provider.getFirstName();
                           //String lName = provider.getLastName();

                           if (provider.getFirstName() != null && !provider.getFirstName().isEmpty()) {
                                if(provider.getLastName().equalsIgnoreCase(lastfirst[0].trim()) && provider.getFirstName().equalsIgnoreCase(lastfirst[1].trim()) ) {
     %>



<table class="table table-bordered table-striped">
    <h5>Clinics information for <u><b><%=provider.getLastName()+" ,"+provider.getFirstName()%> </b></u></h5>
    <thead>
    <tr>
        <th>Check to Add</th>
        <th>Clinic Id</th>
        <th>Clinic Name</th>
        <th>Clinic Address</th>

    </tr>
    </thead>
    <tbody>

    <%
        if(provider.getClinics()!=null && !provider.getClinics().isEmpty() && provider.getClinics().size()>=1)
        {
            //Setting ClinicId and Clinic name to later use in doCdxSend.
            HashMap<String,String> ClinicInfo =new HashMap<String, String>();
        for(IClinic clinic : provider.getClinics())
        {
            ClinicInfo.put(clinic.getID(),clinic.getName());

    %>
    <tr>


        <td>

            <input type=checkbox name="seletectedclinics" value=<%=clinic.getID()%>>

        </td>
        <td>

            <%=clinic.getID()%>

        </td>


        <td>

            <%=clinic.getName()%>


        </td>
        <td>

            <%=clinic.getStreetAddress()+" "+clinic.getCity()+" "+clinic.getProvince()%>


        </td>

    </tr>
    <%
        }

    %>
    </tbody>
</table>

     <%

                                 session.setAttribute("clinicInfo",ClinicInfo);
                             }

        else{

     %>
  <h5> No clinic information is available ! </h5>

        <%

                             }
                                }

                           }
                       }
                   }
       }
        %>

<html>
<head>
    <title></title>
</head>
<body>

</body>
</html>
