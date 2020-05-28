
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>

<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IProvider" %>
<%@ page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@ page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IClinic" %>

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
    if(request.getParameter("key")!=null && !request.getParameter("key").isEmpty() )
    {

    DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    List<Demographic> demoList=null;

        String key = request.getParameter("key");
        demoList=demographicDao.searchDemographicByFullName(key);
        for(int i=0;i<demoList.size();i++){



%>
<li class="list-group-item"><%=demoList.get(i).getFullName()%></li>
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
                   String[] lastfirst = searchString.split(" ");
                   OscarProperties props = OscarProperties.getInstance();
                   boolean showCdx = "bc".equalsIgnoreCase(props.getProperty("billregion"));
                   List<IProvider> providers = null;
                   if (showCdx) {
                       CDXSpecialist cdxSpecialist = new CDXSpecialist();
                       providers = cdxSpecialist.findCdxSpecialistByName(lastfirst[1].trim());
                       for (IProvider provider : providers) {
                           //   String cdxSpecId = provider.getID();
                           // MiscUtils.getLogger().debug("cdxSpecId: " + cdxSpecId);
                           //String fName = provider.getFirstName();
                           //String lName = provider.getLastName();

                           if (provider.getFirstName() != null && !provider.getFirstName().isEmpty()) {
                                if(provider.getLastName().equalsIgnoreCase(lastfirst[0].trim())) {
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
        for(IClinic clinic : provider.getClinics())
        {

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
    <title>cdx Messenger v0.1</title>
</head>
<body>

</body>
</html>
