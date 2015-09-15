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
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.*,oscar.oscarPrevention.*"%>
<%@ page
	import="oscar.oscarProvider.data.*,oscar.oscarWorkflow.*,oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<%
    String formClass = "RhImmuneGlobulin";
    String formLink = "formRhImmuneGlobulin.jsp";
    
    String demographicNo = request.getParameter("demographic_no");
    if (demographicNo == null){
        demographicNo = (String) request.getAttribute("demographic_no");
    }    
    int demoNo = Integer.parseInt(demographicNo);
    
    String workflowId = request.getParameter("workflowId");
    String formIdStr = "0";
    if(request.getParameter("formId") != null ){   ////TEMPORARY
        formIdStr = request.getParameter("formId");     
    }
    
    int formId = Integer.parseInt(formIdStr);
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo, formId);

    String project_home = request.getContextPath().substring(1);
    boolean bView = false;
    if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;
    
    List providers = ProviderData.getProviderList();
    String prevDate = UtilDateUtilities.getToday("yyyy-MM-dd");
    String providerName = "";
    String provider = (String) session.getAttribute("user");
    
  
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
 * GNU General Public License for more details. * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rh Immune Globulin Injection Reporting Form</title>
<html:base />

<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="../share/prototype.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
        var choiceFormat  = new Array(6,7,8,9,12,13);        
        var allNumericField = new Array(14,15);
        var allMatch = null;
        var action = "/<%=project_home%>/form/formname.do";        

    </script>

<script type="text/javascript">
      function hideExtraName(ele){ 
       //alert(ele);
        if (ele.options[ele.selectedIndex].value != -1){
           hideItem('providerName');
           //alert('hidding');
        }else{                    
           showItem('providerName');
           document.getElementById('providerName').focus();
           //alert('showing');
        }                       
      }
      function showHideItem(id){ 
         if(document.getElementById(id).style.display == 'none')
                document.getElementById(id).style.display = ''; 
         else
                document.getElementById(id).style.display = 'none'; 
      }

        function showItem(id){
                document.getElementById(id).style.display = ''; 
        }

        function hideItem(id){
            document.getElementById(id).style.display = 'none'; 
        }

        function showHideNextDate(id,nextDate,nexerWarn){
            if(document.getElementById(id).style.display == 'none'){
                showItem(id);
            }else{
                hideItem(id);
                document.getElementById(nextDate).value = "";
                document.getElementById(nexerWarn).checked = false ;

            }        
        }

        function disableifchecked(ele,nextDate){        
            if(ele.checked == true){       
               document.getElementById(nextDate).disabled = true;       
            }else{                      
               document.getElementById(nextDate).disabled = false;              
            }
        }
    </script>

<script type="text/javascript" src="formScripts.js">
    
    </script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,833)">




<script type="text/javascript">
                    //new Form.Element.Observer($("myfield"), 1, myCallBackFunction);
                    
                    function process(formInject){
                        //alert(Form.Element.Serializers.inputSelector(formInject.reason_check));
                        //alert(formInject.reason_check);
                        for (i = 0; formInject.reason_check.length; i++){
                           if(formInject.reason_check[i].checked){
                            //alert(formInject.reason_check[i].value);
                            if ( formInject.reason_check[i].value == 'Other'){
                               formInject.reason.value = document.getElementById('reasonOtherText').value ;
                            }else{
                               formInject.reason.value = formInject.reason_check[i].value ;
                            }
                            break;
                           }
                        }
                       //alert( formInject.reason.value );
                       return False;
                    }
                </script>


<html:form action="/oscarPrevention/AddPrevention"
	onsubmit="return process(this);" styleId="injectForm">
	<input type="hidden" name="prevention" value="RH" />
	<input type="hidden" name="demographic_no" value="<%=demographicNo%>" />
	<input type="hidden" name="workflowId" value="<%=workflowId%>" />
	<input type="hidden" name="formId" value="<%=formIdStr%>" />
	<input type="hidden" name="reason" value="" />


	<fieldset><legend>Rh Injection</legend>
	<div style="float: left;"><input name="given" type="radio"
		value="given" checked>Completed</input><br />
	<input name="given" type="radio" value="refused">Refused</input><br />

	</div>
	<div style="float: left; margin-left: 30px;"><label
		for="prevDate" class="fields">Date:</label> <input type="text"
		name="prevDate" id="prevDate" value="<%=prevDate%>" size="9">
	<a id="date"><img title="Calendar" src="../images/cal.gif"
		alt="Calendar" border="0" /></a> <br>
	<label for="provider" class="fields">Provider:</label> <input
		type="text" name="providerName" id="providerName"
		value="<%=providerName%>" /> <select
		onchange="javascript:hideExtraName(this);" id="providerDrop"
		name="provider">
		<%for (int i=0; i < providers.size(); i++) {
                                           Map h = (Map) providers.get(i);%>
		<option value="<%= h.get("providerNo")%>"
			<%= ( h.get("providerNo").equals(provider) ? " selected" : "" ) %>><%= h.get("lastName") %>
		<%= h.get("firstName") %></option>
		<%}%>
		<option value="-1" <%= ( "-1".equals(provider) ? " selected" : "" ) %>>Other</option>
	</select></div>
	</fieldset>
	<fieldset><legend>Result</legend> <label for="location">Hospital/Clinic:</label>
	<input type="text" name="location" /> <br />
	<label for="route">Route:</label> <input type="text" name="route" /><br />
	<label for="lot">Lot:</label> <input type="text" name="lot" /><br />
	<label for="lot">Product:</label> <input type="text" name="product" /><br />
	<label for="manufacture">Manufacture:</label> <input type="text"
		name="manufacture" /><br />
	<label>Dosage:</label> <input type="text" name="dosage" size="9" /><small>mcg</small>
	</fieldset>


	<%--
                        <div class="boxed2">
                            <label for="prevDate" class="fields" >Date:</label>    <input type="text" name="prevDate" id="prevDate" value="<%=prevDate%>" size="9" > <a id="date" style="float:left;"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a>                       
                            <label >Hospital/Clinic:</label><input type="text" name="location" size="9"/> 
                            <br/>
                            
                            <label for="provider" class="fields">Provider:</label> <input type="text" name="providerName" id="providerName" value="<%=providerName%>"/> 
                                  <select onchange="javascript:hideExtraName(this);" id="providerDrop" name="provider">                          
                                      <%for (int i=0; i < providers.size(); i++) {
                                           Hashtable ph = (Hashtable) providers.get(i);%>
                                        <option value="<%= ph.get("providerNo")%>" <%= ( ph.get("providerNo").equals(provider) ? " selected" : "" ) %>><%= ph.get("lastName") %> <%= ph.get("firstName") %></option>
                                      <%}%>                    
                                      <option value="-1" <%= ( "-1".equals(provider) ? " selected" : "" ) %> >Other</option>
                                  </select>  
                              
                            <br/>
                            <label>Lot No:</label><input type="text" name="lot" size="9"/>
                            <label>Dosage:</label> <input type="text" name="dosage" size="9"/><small>mcg</small>   
                        </div>
                    --%>

	<fieldset><legend>REASON FOR INJECTION (please
	check):</legend>
	<ul>
		<li><input type="radio" name="reason_check"
			value="Antepartum (28 weeks)" checked>Antepartum (28 weeks)</input></li>
		<li><input type="radio" name="reason_check" value="Amniocentesis">Amniocentesis
		</input></li>
		<li><input type="radio" name="reason_check"
			value="Ectopic Pregnancy">Ectopic Pregnancy </input></li>
		<li><input type="radio" name="reason_check"
			value="Antenatal Bleeding (threatened abortion)">Antenatal
		Bleeding (threatened abortion) </input></li>
		<li><input type="radio" name="reason_check"
			value="Spontaneous Abortion">Spontaneous Abortion </input></li>
		<li><input type="radio" name="reason_check"
			value="Therapeutic Abortion">Therapeutic Abortion </input></li>
		<li><input type="radio" name="reason_check"
			value="Platelet Transfusion">Platelet Transfusion </input></li>
		<li><input type="radio" name="reason_check" value="Postpartum">Postpartum</input></li>
		<li><input type="radio" name="reason_check" value="Other">Other</input>
		<input type="text" name="reasonOtherText" id="reasonOtherText" /></li>
	</ul>
	</fieldset>
	<%-- input type="button" onclick="process(document.getElementById('injectForm'))"/ --%>
                        &nbsp;<input type="submit"
		value="Save Injection" />

</html:form>
</div>

<script type="text/javascript">
            Calendar.setup( { inputField : "prevDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
            hideExtraName(document.getElementById('providerDrop'));                                    
                            
        </script>






</body>
</html:html>


<%!
   
    
   
   
%>
