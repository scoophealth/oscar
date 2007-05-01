<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="oscar.oscarRx.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData"%>


<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../noRights.html");%>
</security:oscarSec>

<% response.setHeader("Cache-Control","no-cache");%>
<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>
<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
%>

<% RxPharmacyData pharmacyData = new RxPharmacyData();
  RxPharmacyData.Pharmacy pharmacy ;                                  
  pharmacy = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));                                  
  String prefPharmacy = "";
  if (pharmacy != null){   
     prefPharmacy = pharmacy.name;                                  
  }
  
%>

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


<html:html locale="true">
<head>
<title><bean:message key="SearchDrug.title"/></title>
<link rel="stylesheet" type="text/css" href="styles.css">

<html:base />

<style type="text/css">
table.hiddenLayer {	
	height: 200px;        	
	margin-left: 20px;
	border: 1px solid #dcdcdc;
	background-color: #f5f5f5

}

</style>
<script type="text/javascript">

    function popupDrugOfChoice(vheight,vwidth,varpage) { //open a new popup window
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
      var popup=window.open(varpage, "oscarDoc", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }
    
    
    function goDOC(){
       if (document.RxSearchDrugForm.searchString.value.length == 0){
          popupDrugOfChoice(720,700,'http://doc.oscartools.org/')
       }else{
          //var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
          var docURL = "http://doc.oscartools.org/search?SearchableText="+document.RxSearchDrugForm.searchString.value;          
          popupDrugOfChoice(720,700,docURL);                               
       }
    }     

    function popupWindow(vheight,vwidth,varpage,varPageName) { //open a new popup window
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
      var popup=window.open(varpage,varPageName, windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }
    
    function customWarning(){
        if (confirm('This feature will allow you to manually enter a drug.'
               + '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
               + '\n  *  Known Dosage Forms / Routes'
               + '\n  *  Drug Allergy Information'
               + '\n  *  Drug-Drug Interaction Information'
               + '\n  *  Drug Information'
               + '\n\nAre you sure you wish to use this feature?')==true) {
            window.location.href = 'chooseDrug.do?demographicNo=<%= response.encodeURL(Integer.toString(bean.getDemographicNo())) %>';
        }
    }

    function isEmpty(){  
        if (document.RxSearchDrugForm.searchString.value.length == 0){
            alert("Search Field is Empty");
            document.RxSearchDrugForm.searchString.focus();
            return false;
        }
        return true;
    }
    
    function showpic(picture){
     if (document.getElementById){ // Netscape 6 and IE 5+      
        var targetElement = document.getElementById(picture);                
        var bal = document.getElementById("Calcs");

        var offsetTrail = document.getElementById("Calcs");
        var offsetLeft = 0;
        var offsetTop = 0;
        while (offsetTrail) {
           offsetLeft += offsetTrail.offsetLeft;
           offsetTop += offsetTrail.offsetTop;
           offsetTrail = offsetTrail.offsetParent;
        }
        if (navigator.userAgent.indexOf("Mac") != -1 && 
           typeof document.body.leftMargin != "undefined") {
           offsetLeft += document.body.leftMargin;
           offsetTop += document.body.topMargin;
        }
            
        targetElement.style.left = offsetLeft +bal.offsetWidth;        
        targetElement.style.top = offsetTop;	
        targetElement.style.visibility = 'visible';        
     }
  }

  function hidepic(picture){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        targetElement.style.visibility = 'hidden';
     }
  }
</script>
</head>

<%
    boolean showall = false;

    if(request.getParameter("show")!=null)
        if(request.getParameter("show").equals("all"))
            showall = true;
%>

<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient"/>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
    <%@ include file="TopLinks.jsp" %><!-- Row One included here-->
    <tr>
    <%@ include file="SideLinksEditFavorites.jsp" %><!-- <td></td>Side Bar File --->
    <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top"><!--Column Two Row Two-->
        <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
            <tr>
                <td width="0%" valign="top">
                    <div class="DivCCBreadCrumbs">
              	        <b><bean:message key="SearchDrug.title"/></b>
                    </div>
                </td>
            </tr>
<!----Start new rows here-->
            <tr>
                <td>
                    <div class="DivContentTitle"><bean:message key="SearchDrug.title"/></div>                    
               </td>
            </tr>
            <tr>
 		        <td>
                    <div class="DivContentSectionHead"><bean:message key="SearchDrug.section1Title"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td>
                                <b><bean:message key="SearchDrug.nameText"/></b>
                                <jsp:getProperty name="patient" property="firstName"/> <jsp:getProperty name="patient" property="surname"/>
                            </td>
                            <td></td>
                            <td>
                                <b><bean:message key="SearchDrug.ageText"/></b>
                                <jsp:getProperty name="patient" property="age"/>
                            </td>
                            <td></td>
                            <td>                               
                               <b>Prefered Pharmacy :</b>
                               <a href="javascript: function myFunction() {return false; }"  onClick="showpic('Layer1');"  id="Calcs" ><%=prefPharmacy%></a>                               
                            </td>
                        </tr>
                        <oscarProp:oscarPropertiesCheck property="MY_OSCAR" value="yes">
                            <indivo:indivoRegistered demographic="<%=String.valueOf(bean.getDemographicNo())%>" provider="<%=bean.getProviderNo()%>">                               
                                <tr>
                                    <td colspan="3">
                                        <a href="javascript:popupWindow(720,700,'AddDrugProfileToPHR.jsp?demoId=<%=Integer.toString(bean.getDemographicNo())%>','PresPHR')">Send To Personal Health Record</a>                                
                                    </td>                            
                                </tr>                                                      
                            </indivo:indivoRegistered>
                        </oscarProp:oscarPropertiesCheck>
                    </table>
                </td>
            </tr>

            <tr>
                <td>
                    <div class="DivContentSectionHead"><bean:message key="SearchDrug.section2Title"/> 
                                                   
                   (<a href="javascript:popupWindow(720,700,'PrintDrugProfile.jsp','PrintDrugProfile')">Print</a>)</div>
                </td>
            </tr>
            <tr>
                <td>
                    <table>
                    <tr>
                       <td width="100%">
                            <div class="Step1Text" style="width:100%">
                            <table width="100%" cellpadding="3">
                                <tr>
                                    <th align=left><b>Rx Date</b></th>
                                    <th align=left><b>Prescription</b></th>
                                    <th align=center width="100px"><b>Represcribe</b></th>
                                    <th align=center width="100px"><b>Delete</b></th>
                                </tr>

                                <%
                                oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;

                                if(showall)
                                    prescribedDrugs = patient.getPrescribedDrugs();
                                else
                                    prescribedDrugs = patient.getPrescribedDrugsUnique();

                                for(int i=0; i<prescribedDrugs.length; i++) {
                                    oscar.oscarRx.data.RxPrescriptionData.Prescription drug = prescribedDrugs[i];
                                    String styleColor = "";
                                    if(drug.isCurrent() == true && drug.isArchived() ){
					styleColor="style=\"color:red;text-decoration: line-through;\"";  
                                    }else if (drug.isCurrent() && !drug.isArchived())  {
                                        styleColor="style=\"color:red;\"";
				    }else if (!drug.isCurrent() && drug.isArchived()){
                                        styleColor="style=\"text-decoration: line-through;\"";
				    }
                                    %>
                                    <tr>
                                        <td>
                                            <a <%= styleColor%>
                                            href="StaticScript.jsp?gcn=<%= drug.getGCN_SEQNO()
                                            %>&cn=<%= response.encodeURL(drug.getCustomName()) %>">
                                                <%= drug.getRxDate() %>
                                            </a>
                                        </td>
                                        <td>
                                            <a <%= styleColor%>
                                            href="StaticScript.jsp?gcn=<%= drug.getGCN_SEQNO()
                                            %>&cn=<%= response.encodeURL(drug.getCustomName()) %>">
                                                <%= drug.getRxDisplay() %>
                                            </a>
                                        </td>
                                        <td width="100px" align="center">
                                            <input type=checkbox name="chkRePrescribe" align="center" drugId=<%= drug.getDrugId() %> />
                                        </td>
                                        <td width="100px" align="center">
                                            <input type=checkbox name="chkDelete" align="center" drugId=<%= drug.getDrugId() %> />
                                        </td>
                                    </tr>
                                    <%
                                }
                                %>
                            </table>

                            </div>
                            <div style="margin-top:10px; margin-left:20px; width:100%">
                            <table width="100%" cellspacing=0 cellpadding=0>
                                <tr>
                                    <td align=left>
                                        <% if(showall) { %>
                                            <a href="SearchDrug.jsp">Show Current</a>
                                        <% } else { %>
                                            <a href="SearchDrug.jsp?show=all">Show All</a>
                                        <% } %>
                                    </td>
                                    <td align=right>
                                        <span style="width:350px;align:right">
                                            <input type=button name="cmdAllergies" value="View / Edit Allergies"
                                                class="ControlPushButton" onclick="javascript:window.location.href='ShowAllergies.jsp';"
                                                style="width:100px" />
                                            <input type=button name="cmdRePrescribe" value="Represcribe"
                                                class="ControlPushButton" onclick="javascript:RePrescribe();"
                                                style="width:100px" />
                                            <input type=button name="cmdDelete" value="Delete"
                                                class="ControlPushButton" onclick="javascript:Delete();"
                                                style="width:100px" />
                                        </span>
                                    </td>
                                </tr>
                            </table>
                            </div>

                            <script language=javascript>
                                function RePrescribe(){
                                
                                    if(document.getElementsByName('chkRePrescribe')!=null){
                                        var checks = document.getElementsByName('chkRePrescribe');
                                        var s='';
                                        var i;

                                        for(i=0; i<checks.length; i++){
                                            if(checks[i].checked==true){
                                                s += checks[i].getAttribute("drugId") + ',';
                                            }
                                        }

                                        if(s.length>1){
                                            s = s.substring(0, s.length - 1);

                                            document.forms[0].drugList.value = s;
                                            document.forms[0].submit();
                                        }
                                    }
                                }

                                function Delete(){
                                    if(document.getElementsByName('chkDelete')!=null){
                                        var checks = document.getElementsByName('chkDelete');
                                        var s='';
                                        var i;

                                        for(i=0; i<checks.length; i++){
                                            if(checks[i].checked==true){
                                                s += checks[i].getAttribute("drugId") + ',';
                                            }
                                        }

                                        if(s.length>1){
                                            if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
                                                s = s.substring(0, s.length - 1);

                                                document.forms[1].drugList.value = s;
                                                document.forms[1].submit();
                                            }
                                        }
                                    }
                                }
                            </script>

                            <html:form  action="/oscarRx/rePrescribe">
                            <html:hidden property="drugList" />
                            </html:form>
                            <br>
                            <html:form   action="/oscarRx/deleteRx">
                            <html:hidden property="drugList" />
                            </html:form>
                        </td>
                    </tr>
                    </table>
                </td>
            </tr>
                <tr>
 		            <td>
                      <div class="DivContentSectionHead"><bean:message key="SearchDrug.section3Title"/></div>
                    </td>
                </tr>

            <tr>
 		   <td>
                      <html:form action="/oscarRx/searchDrug" focus="searchString" onsubmit="return isEmpty()">
                      <html:hidden property="demographicNo" value="<%= new Integer(patient.getDemographicNo()).toString()%>" />
                      <table>
                        <tr valign="center">
                          <td>
                            <bean:message key="SearchDrug.drugSearchTextBox"/>
                          </td>
                          <td>
                            <html:text property="searchString" size="16" maxlength="16"/>&nbsp;<a href="javascript:goDOC();">Drug of Choice</a>                            
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <html:submit property="submit" value="Search" styleClass="ControlPushButton"/>
                          </td>
                          <td>
                            <input type=button class="ControlPushButton"  onclick="javascript:document.forms.RxSearchDrugForm.searchString.value='';document.forms.RxSearchDrugForm.searchString.focus();" value="Reset" />
                            <input type=button class="ControlPushButton"  onclick="javascript:customWarning();" value="Custom Drug" /> 
                          </td>
                        </tr>
                      </table>
                      </html:form>
                    </td>
                  </tr>

<logic:notEqual name="bean" property="stashSize" value="0">
                  <tr>
                    <td>
                        <script language=javascript>
                            function submitPending(stashId, action){
                                var frm = document.forms.RxStashForm;
                                frm.stashId.value = stashId;
                                frm.action.value = action;
                                frm.submit();
                            }

                        </script>
                        <html:form action="/oscarRx/stash">                       
                             <html:hidden property="action" />
                             <html:hidden property="stashId" />                       
                        </html:form>

                        <div class="DivContentSectionHead"><bean:message key="WriteScript.section5Title"/></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <script language=javascript>
                            function ShowDrugInfo(GN){
                                window.open("drugInfo.do?GN=" + escape(GN), "_blank",
                                    "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
                            }
                        </script>
                        <table cellspacing=0 cellpadding=5>
                        <% int i=0; %>
                        <logic:iterate id="rx" name="bean" property="stash" length="stashSize">
                            <tr>
                                <td><a href="javascript:submitPending(<%= i%>, 'edit');">Edit</a></td>
                                <td><a href="javascript:submitPending(<%= i%>, 'delete');">Delete</a></td>
                                <td>
                                    <a href="javascript:submitPending(<%= i%>, 'edit');">
                                        <bean:write name="rx" property="rxDisplay" />
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:ShowDrugInfo('<%= ((oscar.oscarRx.data.RxPrescriptionData.Prescription)rx).getGenericName() %>');">Info</a>
                                </td>
                            </tr>
                            <% i++; %>
                        </logic:iterate>
                        </table>
                        <br>

                        <input type=button class="ControlPushButton" onclick="javascript:window.location.href='viewScript.do';" value="Save and Print" />
                    </td>
                </tr>
</logic:notEqual>

                        <!----End new rows here-->
                <tr height="100%">
                    <td>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    </tr>

    <tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
    </tr>

    <tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
    </tr>

</table>


                                                         
                                                                
                                                                   
      
<% if (pharmacy != null ){ %>
<div id="Layer1" style="position:absolute; left:1px; top:1px; width:350px; height:311px; visibility: hidden; z-index:1"   >
<!--  This should be changed to automagically fill if this changes often -->      
                       
<table  border="0" cellspacing="1" cellpadding="1" align=center class="hiddenLayer">
    <tr class="LightBG"> 
      <td class="wcblayerTitle">&nbsp;</td>
      <td class="wcblayerTitle">&nbsp;</td>   
      <td class="wcblayerTitle" align="right">
             <a href="javascript: function myFunction() {return false; }" onclick="hidepic('Layer1');" style="text-decoration: none;">X</a>           
      </td>
    </tr>
   
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Name
          </td>
          <td class="wcblayerItem">&nbsp;</td>
          <td><%=pharmacy.name%></td>
    </tr>                  
  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Address
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.address%></td>
    </tr>                  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">               
          City                 
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.city%></td>
    </tr>                  
	                        
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Province
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.province%></td>
    </tr>                  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Postal Code :
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.postalCode%></td>
    </tr>                  	                          
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Phone 1 :
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.phone1%></td>
    </tr>                  	                          
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Phone 2 :
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.phone2%></td>
    </tr>                  	                                                                                        
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Fax :
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.fax%></td>
    </tr>                  	
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
          Email :
          </td>
          <td class="wcblayerItem">&nbsp;</td>	       
          <td><%=pharmacy.email%></td>
    </tr>                  	                                                                                                                                                                                
    <tr class="LightBG"> 
          <td colspan="3" class="wcblayerTitle">
          Notes :
          </td>          
    </tr>                  	                                                                                                                                                                                
    <tr class="LightBG">           
          <td colspan="3"><%=pharmacy.notes%></td>
    </tr>                  	                                                                                                                                                                                
    
  </table>
                                
</div>
<%}%>
</body>
</html:html>








