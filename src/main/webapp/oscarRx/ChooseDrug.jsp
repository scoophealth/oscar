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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*, oscar.OscarProperties" %>
<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html:html locale="true">
<head>
<title><bean:message key="ChooseDrug.title.DrugSearchResults"/></title>
<html:base/>
<script type="text/javascript" src="<c:out value="../share/javascript/Oscar.js"/>"></script>
<script type="text/javascript" src="<c:out value="../share/javascript/prototype.js"/>"></script>

<%
RxSessionBean bean = (RxSessionBean)pageContext.findAttribute("bean");
RxDrugData.DrugSearch drugSearch = (RxDrugData.DrugSearch) request.getAttribute("drugSearch");//set from searchdrugaction
String demoNo = (String) request.getAttribute("demoNo");//set from searchdrugaction
ArrayList brand = drugSearch.getBrand();
ArrayList gen = drugSearch.getGen();
ArrayList afhcClass = drugSearch.getAfhcClass();
int i;

String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
if (drugref_route==null) drugref_route = "";
String[] d_route = ("Oral,"+drugref_route).split(",");
String[] selRoute = new String[d_route.length];

for (int j=0; j<selRoute.length; j++) {
    selRoute[j] = request.getParameter("route"+j)!=null? "checked" : "";
}
%>


<link rel="stylesheet" type="text/css" href="styles.css">

<script language=javascript>
    function ShowDrugInfoBN(drug){
        window.open("drugInfo.do?BN=" + escape(drug), "_blank",
            "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
    }
    function ShowDrugInfo(drug){
        window.open("DrugInfoRedirect.jsp?drugId=" + escape(drug), "_blank",
            "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
    }
    function ShowDrugInfoGN(drug){
        window.open("drugInfo.do?GN=" + escape(drug), "_blank",
            "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
    }

    
</script>
<script type="text/javascript">

    function setDrugRx2(drugId,drugName){
        window.opener.setSearchedDrug(drugId,drugName);
        window.close();
    }

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
          popupDrugOfChoice(720,700,'http://resource.oscarmcmaster.org/oscarResource/DoC/')
       }else{
          var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
          popupDrugOfChoice(720,700,docURL);                               
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
    
    function buildRoute() {
        pickRoute = "";
<% for (int j=0; j<d_route.length; j++) { %>
	if (document.forms[0].route<%=j%>.checked) pickRoute += " "+document.forms[0].route<%=j%>.value;
<% } %>
	document.forms[0].searchRoute.value = pickRoute;
    }

    function processData() {
	if (isEmpty()) buildRoute();
        else return false;
    }
    
    function callTreatments(textId,id){
        var ele = $(textId);
        var url = "TreatmentMyD.jsp"
        var ran_number=Math.round(Math.random()*1000000);
        var params = "demographicNo=<%=bean.getDemographicNo()%>&cond="+ele.value+"&rand="+ran_number;  //hack to get around ie caching the page
        new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
        $('treatmentsMyD').toggle();
    }

    
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<% if (drugSearch != null && drugSearch.failed){  out.write(drugSearch.errorMessage); } %>


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
  <%@ include file="TopLinks.jsp" %><!-- Row One included here-->
  <tr>
      <%@ include file="SideLinksNoEditFavorites.jsp" %><!-- <td></td>Side Bar File --->
        <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
                <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
                  <tr>
            	    <td width="0%" valign="top">
            	    <div class="DivCCBreadCrumbs">
              	    <a href="SearchDrug.jsp">
                    <bean:message key="SearchDrug.title"/></a>
                    <b><bean:message key="ChooseDrug.title"/></b>
                    </div>
                    </td>
            	  </tr>
            <!----Start new rows here-->
                  <tr>
                    <td>
 		      <div class="DivContentTitle"><bean:message key="ChooseDrug.title"/></div>
                    </td>
		  </tr>
                  <tr>
                    <td>
                      <div class="DivContentSectionHead"><bean:message key="ChooseDrug.section1Title"/></div>
                    </td>
                  </tr>
                  <tr>
                    <td>
                    <html:form action="/oscarRx/searchDrug" focus="searchString" onsubmit="processData();">
                    <%if(request.getParameter("rx2") != null && request.getParameter("rx2").equals("true")){ %>
                        <input type="hidden" name="rx2" value="true"/>
                    <%}%>

                    <html:hidden property="demographicNo" />
                    <table>
                        <tr>
			    <td>
				<bean:message key="ChooseDrug.searchAgain"/><br>
				<html:text styleId="searchString" property="searchString" size="16" maxlength="16"/>
			    <!--<html:hidden property="otcExcluded" value="true"/>OTC Excluded-->
			    </td>
			    <td width=100>
			    <% if(!OscarProperties.getInstance().getProperty("rx.drugofchoice.hide","false").equals("true")) { %>
				<a href="#" onclick="callTreatments('searchString','treatmentsMyD');return false;"><bean:message key="ChooseDrug.msgDrugOfChoice"/></a>                            
			    <%} %>
			    </td>
			    <td>
				<oscar:oscarPropertiesCheck property="drugref_route_search" value="on">				
				    <bean:message key="SearchDrug.drugSearchRouteLabel"/><br>
				    <%  for (int j=0; j<d_route.length; j++) { %>
				    <input type=checkbox <%=selRoute[j]%> name=route<%=j%> value="<%=d_route[j].trim()%>"><%=d_route[j].trim()%> &nbsp;</input>
				    <%  } %>
				    <html:hidden property="searchRoute"/>
				</oscar:oscarPropertiesCheck>
			    </td>
                        </tr>
                        <tr>
			    <td colspan=3>
                                <html:submit property="submit" styleClass="ControlPushButton" ><bean:message key="ChooseDrug.msgSearch"/></html:submit>
				&nbsp;&nbsp;&nbsp;
				<input type=button class="ControlPushButton"  onclick="javascript:document.forms.RxSearchDrugForm.searchString.value='';document.forms.RxSearchDrugForm.searchString.focus();" value="<bean:message key="ChooseDrug.msgReset"/>" />
				<%if(request.getParameter("rx2") == null || !request.getParameter("rx2").equals("true")){ %>
				<input type=button class="ControlPushButton"  onclick="javascript:customWarning();" value="<bean:message key="ChooseDrug.msgCustomDrug"/>" />                            
                                <%}%>

			    </td>
                        </tr>
                     </table>
                    </html:form>
                    </td>
                  </tr>
                  <tr>
 		    <td>
 		      <div class="DivContentSectionHead"><bean:message key="ChooseDrug.section2Title"/></div>
		    </td>
		  </tr>
                  <tr>
                    <td>
                        <%
                        if(brand.size() > 0 || gen.size() > 0){
                            %>
                          <table width="100%" border=0>
                            <tr>
                              <td width="50%">
                                <div class="LeftMargin"><bean:message key="ChooseDrug.genericDrugBox"/></div>
                              </td>
                              <td width="50%">
                                <div class="LeftMargin"><bean:message key="ChooseDrug.brandDrugBox"/></div>
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <div class="ChooseDrugBox">
                                <table width="100%" border=0>
                                  <%
                                    boolean grey = false;
                                    String bgColor="WHITE";
                                    for(i=0; i<gen.size(); i++){                                     
                                       bgColor=getColor(grey);                                        
                                       RxDrugData.MinDrug t = (RxDrugData.MinDrug) gen.get(i);                                     
                                  %>
                                  <tr>
                                    <td bgcolor="<%=bgColor%>">
                                      <a href="searchDrug.do?genericSearch=<%= response.encodeURL( t.pKey ) %>&demographicNo=<%= response.encodeURL(demoNo) %>" title="<%=t.name%>">
                                      <%= getMaxVal(t.name)%>  
                                      </a>
                                      <span>&nbsp;&nbsp;(<a href="javascript:ShowDrugInfoGN('<%= response.encodeURL(t.name)%>');"><bean:message key="ChooseDrug.msgInfo"/></a>)</span>
                                    </td>
                                  </tr>
                                  <%
                                    grey =!grey;
                                   }%>
                                </table>
                                </div>
                              </td>
                              <td>
                                <div class="ChooseDrugBox">
                                <table width="100%" border=0>
                                  <%
                                  for(i=0; i<brand.size(); i++){
                                      bgColor=getColor(grey);                                        
                                      RxDrugData.MinDrug t = (RxDrugData.MinDrug) brand.get(i);
                                       String brandName =  t.name;
                                    %>
                                    <tr>                                    
                                      <td bgcolor="<%=bgColor%>">
                                          <%if(request.getParameter("rx2") != null && request.getParameter("rx2").equals("true")){%>
                                            <a href="javascript: void(0);" onclick="setDrugRx2('<%=t.pKey%>','<%=brandName %>')" >
                                          <%}else{%>
                                            <a href="chooseDrug.do?BN=<%=java.net.URLEncoder.encode(brandName )%>&drugId=<%= response.encodeURL(t.pKey) %>&demographicNo=<%= response.encodeURL(demoNo) %>" title="<%=brandName %>" >
                                          <%}%>
                                            <%=brandName%>
                                            </a>
                                        <span>&nbsp;&nbsp;(<a href="javascript:ShowDrugInfoBN('<%=response.encodeURL(t.pKey) %>');"><bean:message key="ChooseDrug.msgInfo"/></a>)</span>
                                      </td>
                                    </tr>
                                  <%                                     
                                      grey =! grey;
                                   }%>
                                </table>
                                </div>
                              </td>
                            </tr>
                          </table>
                            <%
                        } else {
                            %>
                            <div class="LeftMargin">
                                <bean:message key="ChooseDrug.msgSearchNoResults"/>
                            </div>
                            <%
                        }
                        %>
                        <br/>
                        <script language="javascript">
                            function customWarning(){
                                if (confirm("<bean:message key="ChooseDrug.msgCustomWarning"/>")==true) {
                                    window.location.href = 'chooseDrug.do?demographicNo=<%= response.encodeURL(demoNo) %>';
                                }
                            }
                        </script>
                        <div class="LeftMargin">
                           <%if(request.getParameter("rx2") == null || !request.getParameter("rx2").equals("true")){ %>
                            <a href="javascript:customWarning();">
                                <bean:message key="ChooseDrug.msgDrugNotFound"/>
                            </a>
                           <%}%>
                        </div>
                    </td>
                  </tr>
                  <tr>
                    <td>
                        <div class="ChooseDrugBox">
                                <table width="100%" border=0>
                                  <%
                                    boolean grey2 = false;
                                    String bgColor="WHITE";                                    
                                    for(i=0; i<afhcClass.size(); i++){
                                      bgColor=getColor(grey2);                                        
                                      RxDrugData.MinDrug t = (RxDrugData.MinDrug) afhcClass.get(i);
                                  %>
                                  <tr>
                                    <td bgcolor="<%=bgColor%>">
                                      <a href="searchDrug.do?genericSearch=<%= response.encodeURL(t.pKey) %>&demographicNo=<%= response.encodeURL(demoNo) %>">
                                      <%= t.name%> 
                                      </a>
                                      <span>&nbsp;&nbsp;(<a href="javascript:ShowDrugInfo('<%= response.encodeURL(t.pKey)  %>');"><bean:message key="ChooseDrug.msgInfo"/></a>)</span>
                                    </td>
                                  </tr>
                                  <%
                                    grey2 =!grey2;
                                   } %>
                                </table>
                                </div>
                    </td>
                  </tr>
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

<div id="treatmentsMyD" style="position: absolute; left: 1px; top: 1px; width: 800px; height: 600px; display:none; z-index: 1">
       <a href="javascript: function myFunction() {return false; }" onclick="$('treatmentsMyD').toggle();" style="text-decoration: none;">X</a>
</div>


</body>

</html:html>
<%!

String getColor(boolean t){
    String bgColor = "";
    if(t){
        bgColor="#F5F5F5";
    } else {
        bgColor="WHITE";
    }
    return bgColor;
}
String getMaxVal(String name){
    if ( name.length() > 47 ){
        name = name.substring(0,47)+"...";
    }
    return name;
}
%>
