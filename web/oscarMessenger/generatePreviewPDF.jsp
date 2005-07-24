<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ page import="oscar.oscarMessenger.docxfer.send.*,oscar.oscarMessenger.docxfer.util.*, 
                oscar.oscarEncounter.data.*, oscar.oscarEncounter.pageUtil.EctSessionBean, oscar.oscarRx.pageUtil.RxSessionBean,
                oscar.oscarRx.data.RxPatientData"%>
<%@  page import=" java.util.*, org.w3c.dom.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.util.*"%>


<%
String demographic_no = (String) request.getParameter("demographic_no");
%>

<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>

<% 
  String [][] dbQueries=new String[][] { 
        {"search_ect","select eChartId, timeStamp, subject, encounter from eChart where demographicNo=? order by timeStamp desc" },   };
  daySheetBean.doConfigure(dbParams,dbQueries);
  

oscar.oscarSecurity.CookieSecurity cs   = new oscar.oscarSecurity.CookieSecurity();
EctSessionBean bean = new EctSessionBean();
bean.demographicNo = demographic_no;



request.getSession().setAttribute("EctSessionBean",bean);          

//if(cs.FindThisCookie(request.getCookies(),cs.providerCookie)){} //pass security???  

%>

<html>
    <head><title>Generate Preview Page</title></head>
    <script type="text/javascript">   
    function SetBottomURL(url) {
        f = parent.srcFrame;
        
        if ( url != "") {
            loc = url;
        }
        else {
            loc = document.forms[0].url.value;
        }
        f.location = loc;          
    }
    function GetBottomSRC() {
        f = parent.srcFrame;
        document.forms[0].srcText.value = f.document.body.innerHTML;       
    }
    
    function PreviewPDF(url){
        document.forms[0].isPreview.value = true;
        SetBottomURL( url);
        setTimeout("GetBottomSRC()", 1000);      
        setTimeout("document.forms[0].submit()", 1000);
        
    }

    function ProcessPDF(demographic){
    
        var uriArray = document.forms[0].uriArray;
        var pdfTitleArray = document.forms[0].pdfTitle;
        document.forms[0].isPreview.value = false;

        var vheight = 100;
        var vwidth = 10;  
        j= 0;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=0,left=0";            
        for( i = 0; i < uriArray.length ; i ++ ) {
 
            if ( uriArray[i].checked ) {
               j++;
               
               var page = 'processAttFrameset.jsp?demographic_no=' +demographic + '&pdfTitle=' + pdfTitleArray[i].value + '&uri=' + uriArray[i].value; 
               var popUp=window.open(page, "msgAttachDemo" + i, windowprops);     
               document.forms[0].attachmentNumber.value = j;
               
               setTimeout("j = j", 1000);          
            }
        }
        
        document.forms[0].submit();
        //setTimeout("top.opener.location.reload()", 1500* i);          
        //setTimeout(" top.window.close()", 1500 * i);          
       
    }
    
    

    </script>

    

<body>
        <%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
        <%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
            
        
        <html:form action="/oscarMessenger/Doc2PDF">

        Selected Demographic <%=demographic_no%>

        <table border="1" cellpadding="1" cellspacing="1">
                <tr>
        <td colspan="3">
            Demographic information
        </td>
        </tr>       
        <tr>
            <td>
                <% String currentURI = "../demographic/demographiccontrol.jsp?demographic_no=" + demographic_no +"&displaymode=edit&dboperation=search_detail";  %>
                <html:checkbox property="uriArray" value="<%=currentURI%>" />
                <input type=checkbox name="pdfTitle" value='Demographic information' style="display:none" />

            </td>
            <td>       
                Information
            </td>
            <td>       
                <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
            </td>
        </tr>        
        <tr>
        <td colspan="3">
            Encounters:
        </td>
        </tr>       
        <%
          ResultSet rsdemo = null ;

          String[] param =new String[1];
          param[0]=demographic_no; 
          String datetime = null;

          rsdemo = daySheetBean.queryResults(param, "search_ect");
          while (rsdemo.next()) {

        %>
        <tr>
            <td>
                <% currentURI = "../oscarEncounter/echarthistoryprint.jsp?echartid=" + rsdemo.getString("eChartId") + "&demographic_no=" + demographic_no;  %>
                <html:checkbox property="uriArray" value="<%=currentURI%>" />
                <input type=checkbox name="pdfTitle" value='Encounter: <%=rsdemo.getString("timeStamp")%>' style="display:none" />

            </td>
            <td>       
               <%=rsdemo.getString("timeStamp")%>
            </td>
            <td>       
                <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
            </td>
        </tr>
               
        <%
          }
          daySheetBean.closePstmtConn();
        %>  
 
        <tr>
            <td colspan="3">
                Prescriptions
            </td>
        </tr>       
        <tr>
            <td>
            <%
                // Setup bean
                RxSessionBean Rxbean;

                if( request.getSession().getAttribute("RxSessionBean")!=null) {
                    Rxbean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
                }
                else {
                    Rxbean = new RxSessionBean();
                }

                request.getSession().setAttribute("RxSessionBean", Rxbean);
                
                RxPatientData rx = null;
                RxPatientData.Patient patient = null;
                try {
                    rx = new RxPatientData();
                    patient = rx.getPatient(demographic_no);
                }
                catch (java.sql.SQLException ex) {
                    throw new ServletException(ex);
                }

                if(patient!=null) {
                    request.getSession().setAttribute("Patient", patient);
                }

                Rxbean.setProviderNo((String) request.getSession().getAttribute("user"));              
                Rxbean.setDemographicNo(Integer.parseInt(demographic_no));
                                    
            %>
            
                <% currentURI = "../oscarRx/PrintDrugProfile.jsp?demographic_no=" + demographic_no;  %>
            
                <html:checkbox property="uriArray" value="<%=currentURI%>" />
                <input type=checkbox name="pdfTitle" value='Current prescriptions' style="display:none" />

            </td>
            <td>       
                Current prescriptions
            </td>
            <td>       
                <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
            </td>
        </tr>



        
        <!--
        <tr>
        <td colspan="2">       
            <input type="text" name="url" id="url" size="30" value="http://localhost:8084/oscar_mcmaster/form/forwardshortcutname.jsp?formname=Vascular%20Tracker&demographic_no=39" />
        </td>
        </tr>

        <tr>
        <td colspan="2">
            <input type="button" name="setURL" value="setURL" onclick="SetBottomURL( document.forms[0].url.value);" />
        </td>
        </tr>
        -->   
        
        <tr>
            <td colspan="3">
                <textarea name="srcText" rows="5" cols="80" /></textarea>
                <input type="button" name="process" value="Attach PDFs" onclick="ProcessPDF('<%=demographic_no%>')" />
                <br />            
            </td>
        </tr>
            
        <tr>
            <td colspan="3">          
                <html:hidden property="isPreview" />
                <html:hidden property="attachmentNumber" />
                <html:hidden property="jsessionid" value="" />

            </td>
        </tr>

         </table>
        </html:form>

    </body>
</html>


