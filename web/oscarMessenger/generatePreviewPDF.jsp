<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ page import="oscar.oscarMessenger.docxfer.send.*,oscar.oscarMessenger.docxfer.util.*, 
                oscar.oscarEncounter.data.*, oscar.oscarEncounter.pageUtil.EctSessionBean, oscar.oscarRx.pageUtil.RxSessionBean,
                oscar.oscarRx.data.RxPatientData, oscar.oscarMessenger.pageUtil.MsgSessionBean"%>
<%@  page import=" java.util.*, org.w3c.dom.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.util.*"%>


<%
String demographic_no = (String) request.getParameter("demographic_no");
int indexCount = 0;
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

oscar.oscarMessenger.pageUtil.MsgSessionBean MsgSessionBean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");

request.getSession().setAttribute("EctSessionBean",bean);          


%>

<html>
    <head><title>Generate Preview Page</title></head>
    <script type="text/javascript">   
    var timerID = null
    var timerRunning = false    
    
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
        document.forms[0].srcText.value = "";
        document.forms[0].isPreview.value = true;
        SetBottomURL( url);
        setTimeout("GetBottomSRC()", 1000);
        timerID = setInterval("CheckSrcText()", 1000);
        timerRunning = true;        
        
    }  
    
    function testing(){
        document.forms[0].isPreview.value = true;
        timerID = setInterval("CheckSrcText()", 1000);
        timerRunning = true;        
    }

    function AttachingPDF( number){
        
        var uriArray = document.forms[0].uriArray;
        var titleArray = document.forms[0].titleArray;
        var indexArray = document.forms[0].indexArray;
        var wantedIndex = 0;
        document.forms[0].srcText.value = "";
        document.forms[0].isPreview.value = false;
        document.forms[0].isAttaching.value = true;
        
    
        if ( number == -1 ) {
            document.forms[0].isNew.value = true;   
            wantedIndex = -1;
        }
        else {
            document.forms[0].isNew.value = false;    
        }
        
        j = 0;
        
        if ( number != -1 ) {
            for( i = 0; i < indexArray.length ; i ++ ) {
                if ( indexArray[i].checked ) {
                   if ( number == j) {
                        wantedIndex = i;
                   }        
                   j++;
                }

            }
        }
        else {
            for( i = 0; i < indexArray.length ; i ++ ) {
                if ( indexArray[i].checked ) {
                   j++;

                   if ( wantedIndex < 0 ) {
                        wantedIndex = i;
                   }
                }
            }
        }
        
        if ( j ==0 ) {
            document.forms[0].submit();
            return;
        }
        
        document.forms[0].attachmentCount.value = j;
        document.forms[0].attachmentTitle.value = titleArray[wantedIndex].value;        
        SetBottomURL( uriArray[wantedIndex].value );
        setTimeout("GetBottomSRC()", 1000);
        timerID = setInterval("CheckSrcText()", 1000);
        timerRunning = true;
    }    
    
    
    function CheckSrcText() {
        if ( document.forms[0].srcText.value != "") {
            if(timerRunning) { 
              clearInterval(timerID)
            }
            timerRunning = false        
        
            document.forms[0].submit();
        }

        return;
    }
    
    </script>

    

<body>
        <%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
        <%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
            
        
        <html:form action="/oscarMessenger/Doc2PDF">
        
        Attach document for: <%=demographic_no%>

        <table border="1" cellpadding="1" cellspacing="1" width="400">
                <tr>
        <td colspan="3">
            Demographic information
        </td>
        </tr>       
        <tr>
            <td>

                <% String currentURI = "../demographic/demographiccontrol.jsp?demographic_no=" + demographic_no +"&displaymode=pdflabel&dboperation=search_detail";  %>
                <html:checkbox property="uriArray" value="<%=currentURI%>" style="display:none" />
                <html:multibox property="indexArray" value="<%= Integer.toString(indexCount++) %>" />
                <input type=checkbox name="titleArray" value='Demographic information' style="display:none" />

            </td>
            <td>       
                Information
            </td>
            <td>       
                <% if ( request.getParameter("isAttaching") == null ) { %>
                    <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
                <% } %>
                &nbsp;
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
                <html:checkbox property="uriArray" value="<%=currentURI%>" style="display:none" />
                <html:multibox property="indexArray" value="<%= Integer.toString(indexCount++) %>" />
                <input type=checkbox name="titleArray" value='Encounter: <%=rsdemo.getString("timeStamp")%>' style="display:none" />

            </td>
            <td>       
               <%=rsdemo.getString("timeStamp")%>
            </td>
            <td> 
                <% if ( request.getParameter("isAttaching") == null ) { %>
                    <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
                <% } %>
                &nbsp;
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
            
                <html:checkbox property="uriArray" value="<%=currentURI%>" style="display:none" />
                <html:multibox property="indexArray" value="<%= Integer.toString(indexCount++) %>" />
                <input type=checkbox name="titleArray" value='Current prescriptions' style="display:none" />

            </td>
            <td>       
                Current prescriptions
            </td>
            <td>       
                <% if ( request.getParameter("isAttaching") == null ) { %>
                    <input type=button value=Preview onclick=PreviewPDF('<%=currentURI%>') />
                <% } %>
                &nbsp;
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
            <td colspan="3" align="center">
                
                
                <% if ( request.getParameter("isAttaching") != null ) { %>
                    <input type=text name=status value='' />
                <% } else { %>     
                    <input type="button" name="Attach" value="Attach Document" onclick="AttachingPDF(-1)" />
                 <% } %> 
                <br />            
            </td>
        </tr>
            
        <tr>
            <td colspan="3">          
                
                <input type="button" name="test" value="test" onclick="testing()" />            
                <textarea name="srcText" id="srcText" cols="80" rows="10"></textarea>
            
                <html:hidden property="attachmentCount" value='<%=request.getParameter("attachmentCount")%>'/>

                <html:hidden property="demographic_no" value='<%=demographic_no%>'/>

                <html:hidden property="isPreview" value='<%=request.getParameter("isPreview")%>'/>

                <html:hidden property="isAttaching" value='<%=request.getParameter("isAttaching")%>'/>                

                <html:hidden property="isNew" value='true' />                                

                <html:hidden property="attachmentTitle" value='' />                                
                
                
                
            </td>
        </tr>

         </table>
        </html:form>
        
        
        <script>
            if ( document.forms[0].isAttaching.value == "true") {
              
                j = 0;
                var indexArray = document.forms[0].indexArray;
                for( i = 0; i < indexArray.length ; i ++ ) {
                    if ( indexArray[i].checked ) {
                       j++;
                    }
                }

                document.forms[0].status.value = "Attaching <%=MsgSessionBean.getCurrentAttachmentCount() + 1%> out of " + j;
                AttachingPDF( <%=MsgSessionBean.getCurrentAttachmentCount()%>);      
                    
            }
            
            
        </script>

    </body>
</html>

