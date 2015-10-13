<!DOCTYPE html>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page errorPage="../../../appointment/errorpage.jsp"	import="java.util.*,java.sql.*,oscar.*,java.text.*, java.lang.*,java.net.*"%>

<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.util.SpringUtils, org.oscarehr.common.dao.CSSStylesDAO, org.oscarehr.common.model.CssStyle, java.util.List"%>
<%@ page import="org.oscarehr.common.model.BillingService" %>
<%@ page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@ page import="org.oscarehr.billing.CA.ON.model.BillingPercLimit" %>
<%@ page import="org.oscarehr.billing.CA.ON.dao.BillingPercLimitDao" %>
<%
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
	BillingPercLimitDao billingPercLimitDao = SpringUtils.getBean(BillingPercLimitDao.class);

  int serviceCodeLen = 5;
  String msg = "Type in a service code and search first to see if it is available.";
  String alert = "info";
  String action = "search"; // add/edit
  String action2 = ""; //add a new record even if code already exists
  //BillingServiceCode serviceCodeObj = new BillingServiceCode();
  Properties	prop  = new Properties();
  LinkedHashMap codes = new LinkedHashMap();
  
  List<CssStyle> styles = new ArrayList<CssStyle>();
  if (request.getParameter("submitFrm") != null && (request.getParameter("submitFrm").equals("Save") || request.getParameter("submitFrm").equalsIgnoreCase("Add Service Code"))) {
    // check the input data
    // if input the perc code,
    String valuePara = request.getParameter("value");
    if(request.getParameter("percentage").length()>0 && valuePara.trim().equals("") ) {
    	valuePara = ".00";
    }

    if(request.getParameter("action").startsWith("edit")) {
      	// update the service code

		String serviceCode = request.getParameter("service_code");
        String billingservice_no = request.getParameter("billingservice_no");
		if(serviceCode.equals(request.getParameter("action").substring("edit".length()))) {
			BillingService bs = null;
			if(billingservice_no != null) {
				bs = billingServiceDao.find(Integer.parseInt(billingservice_no));
			}else {
				List<BillingService> bsList = billingServiceDao.findByServiceCode(serviceCode);
				if(bsList.size()>=0) {
					bs = bsList.get(0);
				}else {
					msg = serviceCode + " is not updated. Action failed! Try edit it again." ;
				    action = "search";
					alert="error";
				}
			}
			if(bs != null) {
				bs.setDescription(request.getParameter("description"));
				bs.setValue(valuePara);
				bs.setPercentage(request.getParameter("percentage"));
				bs.setBillingserviceDate(MyDateFormat.getSysDate(request.getParameter("billingservice_date")));
				bs.setSliFlag("true".equals(request.getParameter("sliFlag"))?true:false);
				bs.setTerminationDate(MyDateFormat.getSysDate(request.getParameter("termination_date")));

				 String servicecodeStyle = request.getParameter("servicecode_style");
                 String styleId;
                 String[] tmp;
                 if( !servicecodeStyle.startsWith("-1")) {
                 	tmp = servicecodeStyle.split(",");
                 	bs.setDisplayStyle(Integer.parseInt(tmp[0]));
                 }			
            	 else {
            		bs.setDisplayStyle(null);
            	 }


				if(request.getParameter("percentage").length()>1 && request.getParameter("min").length()>1 && request.getParameter("max").length()>1) {
					List<BillingPercLimit> percLimits=billingPercLimitDao.findByServiceCode(serviceCode);
					if(percLimits.size() == 0) {
						BillingPercLimit percLimit = new BillingPercLimit();
						percLimit.setService_code(serviceCode);
						percLimit.setMin(request.getParameter("min"));
						percLimit.setMax(request.getParameter("max"));
						percLimit.setEffective_date(MyDateFormat.getSysDate(request.getParameter("billingservice_date")));
						billingPercLimitDao.persist(percLimit);
					}else {
						BillingPercLimit percLimit= billingPercLimitDao.findByServiceCodeAndEffectiveDate(serviceCode,MyDateFormat.getSysDate(request.getParameter("billingservice_date")));
						if(percLimit != null) {
							percLimit.setMin(request.getParameter("min"));
							percLimit.setMax(request.getParameter("max"));
							billingPercLimitDao.merge(percLimit);
						}
					}

				}

				billingServiceDao.merge(bs);
				msg = serviceCode + " is updated.<br>" + "Type in a service code and search first to see if it is available.";
				alert="success";
	  			action = "search";
			    prop.setProperty("service_code", serviceCode);
			}else {
				msg = serviceCode + " is not updated. Action failed! Try edit it again." ;
				alert="error";
			    action = "edit" + serviceCode;
			    prop.setProperty("service_code", serviceCode);
			    prop.setProperty("description", request.getParameter("description"));
			    prop.setProperty("value", request.getParameter("value"));
			    prop.setProperty("percentage", request.getParameter("percentage"));
			    prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
			    prop.setProperty("sliFlag", request.getParameter("sliFlag"));
			}

		} else {
      		msg = "You can not save the service code - " + serviceCode + ". Please search the service code first.";
		alert="error";
  			action = "search";
		    prop.setProperty("service_code", serviceCode);
		}

    } else if (request.getParameter("action").startsWith("add")) {
		String serviceCode = request.getParameter("service_code");
		if(serviceCode.equals(request.getParameter("action").substring("add".length()))) {
			BillingService bs = new BillingService();
			bs.setServiceCompositecode("");
			bs.setServiceCode(serviceCode);
			bs.setDescription(request.getParameter("description"));
			bs.setValue(valuePara);
			bs.setPercentage(request.getParameter("percentage"));
			bs.setBillingserviceDate(MyDateFormat.getSysDate(request.getParameter("billingservice_date")));
			bs.setSpecialty("");
			bs.setRegion("ON");
			bs.setAnaesthesia("00");
			bs.setTerminationDate(MyDateFormat.getSysDate(request.getParameter("termination_date")));
			bs.setSliFlag("true".equals(request.getParameter("sliFlag"))?true:false);

			String servicecodeStyle = request.getParameter("servicecode_style");
            String styleId;
            String[] tmp;
            if( !servicecodeStyle.startsWith("-1")) {
            	tmp = servicecodeStyle.split(",");
            	bs.setDisplayStyle(Integer.parseInt(tmp[0]));
            }
            bs.setGstFlag(false);

			if(request.getParameter("percentage").length()>1 && request.getParameter("min").length()>1 && request.getParameter("max").length()>1) {
				BillingPercLimit bpl = new BillingPercLimit();
				bpl.setService_code(serviceCode);
				bpl.setMin(request.getParameter("min"));
				bpl.setMax(request.getParameter("max"));
				bpl.setEffective_date(MyDateFormat.getSysDate(request.getParameter("billingservice_date")));
				billingPercLimitDao.persist(bpl);
			}

			// Check that service date is unique for service code
			List scadList = billingServiceDao.findByServiceCodeAndDate(bs.getServiceCode(), bs.getBillingserviceDate());
			if(!scadList.isEmpty()) {
	      		msg = "The selected Service Code has an entry for this Issue Date. <br> Select new issue date, or use 'Save' to update the existing entry.";
			alert="error";
                prop.setProperty("service_code", serviceCode);
                prop.setProperty("description", oscar.util.StringUtils.noNull(bs.getDescription()));
                prop.setProperty("value", oscar.util.StringUtils.noNull(bs.getValue()));
                prop.setProperty("percentage", oscar.util.StringUtils.noNull(bs.getPercentage()));
                prop.setProperty("billingservice_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getBillingserviceDate())));
                prop.setProperty("sliFlag", oscar.util.StringUtils.noNull(bs.getSliFlag().toString()));
                prop.setProperty("termination_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getTerminationDate())));
                action = "edit" + serviceCode;
                action2 = "add" + serviceCode;
			}
			else {
				billingServiceDao.persist(bs);

  				msg = serviceCode + " is added.<br>" + "Type in a service code and search first to see if it is available.";
				alert="success";
  				action = "search";
		    	prop.setProperty("service_code", serviceCode);
			}

		} else {
      		msg = "You can not save the service code - " + serviceCode + ". Please search the service code first.";
		alert="error";
  			action = "search";
		    prop.setProperty("service_code", serviceCode);
		}
    } else {
      msg = "You can not save the service code. Please search the service code first.";
	alert="error";
    }
  } else if (request.getParameter("submitFrm") != null && request.getParameter("submitFrm").equals("Search")) {
    // check the input data
    if(request.getParameter("service_code") == null || request.getParameter("service_code").length() != serviceCodeLen) {
      msg = "Please type in a right service code.";
	alert="warning";
    } else {
        String serviceCode = request.getParameter("service_code");

        List<BillingService> bsList = billingServiceDao.findByServiceCode(serviceCode);
        String tmp;
        int count = 0;
		for(BillingService bs:bsList) {
			count++;

                    codes.put(MyDateFormat.getMyStandardDate(bs.getBillingserviceDate()), bs.getId().toString());
                    if( count == 1 ) {
                        prop.setProperty("service_code", serviceCode);
                        prop.setProperty("description", oscar.util.StringUtils.noNull(bs.getDescription()));
                        prop.setProperty("value", oscar.util.StringUtils.noNull(bs.getValue()));
                        prop.setProperty("percentage", oscar.util.StringUtils.noNull(bs.getPercentage()));
                        prop.setProperty("billingservice_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getBillingserviceDate())));
                        prop.setProperty("sliFlag", oscar.util.StringUtils.noNull(bs.getSliFlag().toString()));
                        prop.setProperty("termination_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getTerminationDate())));
                        if(bs.getDisplayStyle()!=null)
                        	prop.setProperty("displaystyle", oscar.util.StringUtils.noNull(bs.getDisplayStyle().toString()));
                        msg = "You can edit the service code by clicking 'Save' or add a new entry for this code by clicking 'Add Service Code'";
                        action = "edit" + serviceCode;
                        action2 = "add" + serviceCode;

					    BillingPercLimit bpl = billingPercLimitDao.findByServiceCodeAndEffectiveDate(serviceCode, MyDateFormat.getSysDate(prop.getProperty("billingservice_date")));
					    if(bpl!=null) {
					    	prop.setProperty("min", bpl.getMin());
                            prop.setProperty("max", bpl.getMax());
					    }
                    }
                }

				CSSStylesDAO cssStylesDao = (CSSStylesDAO) SpringUtils.getBean("CSSStylesDAO");
				styles = cssStylesDao.findAll();

				if( count == 0 ) {
				    prop.setProperty("service_code", serviceCode);
				    msg = "It is a NEW service code. You can add it.";
					alert="success";
				    action = "add" + serviceCode;
				}
			}
  }
  else if( request.getParameter("action") != null && request.getParameter("action").startsWith("single") ) {
      String serviceCode = request.getParameter("service_code");

      int serviceNo = Integer.parseInt(request.getParameter("billingservice_no"));

      BillingService bs = billingServiceDao.find(serviceNo);

      String tmp;
    int count = 0;

    if (bs != null) {
        ++count;
        codes.put(MyDateFormat.getMyStandardDate(bs.getBillingserviceDate()), bs.getId().toString());

        if( serviceNo == bs.getId().intValue() ) {
            prop.setProperty("service_code", serviceCode);
            prop.setProperty("description", oscar.util.StringUtils.noNull(bs.getDescription()));
            prop.setProperty("value", oscar.util.StringUtils.noNull(bs.getValue()));
            prop.setProperty("percentage", oscar.util.StringUtils.noNull(bs.getPercentage()));
            prop.setProperty("billingservice_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getBillingserviceDate())));
            prop.setProperty("sliFlag", oscar.util.StringUtils.noNull(bs.getSliFlag().toString()));
            prop.setProperty("termination_date", oscar.util.StringUtils.noNull(MyDateFormat.getMyStandardDate(bs.getTerminationDate())));
            if(bs.getDisplayStyle()!=null)
            	prop.setProperty("displaystyle", oscar.util.StringUtils.noNull(bs.getDisplayStyle().toString()));
            msg = "You can edit the service code by clicking 'Save' or add a new entry for this code by clicking 'Add Service Code'";
            action = "edit" + serviceCode;
            action2 = "add" + serviceCode;

		    BillingPercLimit bpl = billingPercLimitDao.findByServiceCodeAndEffectiveDate(serviceCode, MyDateFormat.getSysDate(prop.getProperty("billingservice_date")));
		    if(bpl!=null) {
		    	prop.setProperty("min", bpl.getMin());
                prop.setProperty("max", bpl.getMax());
		    }
        }
    }

    CSSStylesDAO cssStylesDao = (CSSStylesDAO) SpringUtils.getBean("CSSStylesDAO");
	styles = cssStylesDao.findAll();
  }

%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<title><bean:message key="admin.admin.manageBillingServiceCode"/></title>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<script type="text/javascript">

      <!--
      	function displayStyleText(value) {
    		var tmp = value.split(",");
    		document.getElementById('displayStyle').value = tmp[1];
      	}

		function setfocus() {
		  var optionElements = document.getElementById("servicecode_style");
		  displayStyleText(optionElements.options[optionElements.selectedIndex].value);
		  this.focus();
		  document.forms[0].service_code.focus();
		  document.forms[0].service_code.select();
		}
	    function onSearch() {
	        //document.forms[0].submit.value="Search";
	        var ret = checkServiceCode();
	        return ret;
	    }
	    function onSave() {
	        //document.forms[0].submit.value="Save";
	        var ret = checkServiceCode();
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        return ret;
	    }
		function checkServiceCode() {
	        var b = true;
	        if(document.forms[0].service_code.value.length!=5 || !isServiceCode(document.forms[0].service_code.value)){
	            b = false;
	            alert ("You must type in a service code with 5 (upper case) letters/digits. The service code ends with \'A\' or \'B\'...");
	        }
	        return b;
	    }
    function isServiceCode(s){
        // temp for 0.
    	if(s.length==0) return true;
    	if(s.length!=5) return false;
        if((s.charAt(0) < "A") || (s.charAt(0) > "Z")) return false;
        if((s.charAt(4) < "A") || (s.charAt(4) > "Z")) return false;

        var i;
        for (i = 1; i < s.length-1; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        return true;
    }
		function checkAllFields() {
	        var b = true;
	        if(document.forms[0].value.value.length>0 && document.forms[0].percentage.value.length>0){
	            b = false;
	            alert ("You can NOT type in a fee and a percentage at the same time. (leave one blank)");
	        } else if(document.forms[0].value.value.length>0) {
		        if(!isNumber(document.forms[0].value.value)){
		            b = false;
		            alert ("You must type in a number in the field fee");
		        }
	        } else if(document.forms[0].percentage.value.length>0) {
		        if(!isNumber(document.forms[0].percentage.value)){
		            b = false;
		            alert ("You must type in a number in the field percentage");
		        } else if(document.forms[0].percentage.value > 1){
		            b = false;
		            alert ("The percentage should be less than 1.");
		        }
		        if(document.forms[0].min.value.length>0 && document.forms[0].max.value.length>0) {
					if(!isNumber(document.forms[0].min.value) || !isNumber(document.forms[0].max.value)){
		            	b = false;
		            	alert ("You must type in a number in the min/max fields.");
		        	}
		        }
	        } else if(document.forms[0].value.value.length==0 && document.forms[0].percentage.value.length==0) {
	            b = false;
	            alert ("You must type in a number in the field fee");
	        }

			if(document.forms[0].billingservice_date.value.length<10) {
	            b = false;
	            alert ("You need to select a date from the calendar.");
	        }
			return b;
	    }
	    function isNumber(s){
	        var i;
	        for (i = 0; i < s.length; i++){
	            // Check that current character is number.
	            var c = s.charAt(i);
	            if (c == ".") continue;
	            if (((c < "0") || (c > "9"))) return false;
	        }
	        // All characters are numbers.
	        return true;
	    }
	    function upCaseCtrl(ctrl) {
			ctrl.value = ctrl.value.toUpperCase();
		}


                function fetchBillService(billno) {
                    document.getElementById('action').value="single" + billno;
                    var frm = document.getElementById("baseurl");
                    frm.submit();
                }
//-->

      </script>
      
<style type="text/css">
	input[name=value],input[name=percentage],input[name=min],input[name=max] {width: 70px;}
	input[name=termination_date], input[name=billingservice_date]{width:90px;}
	input[name=description] {width: 350px;}
	input[name=submitFrm] {margin-bottom: 10px;}
</style>
      
</head>
<body onLoad="setfocus()">
<h3><bean:message key="admin.admin.manageBillingServiceCode"/></h3>



<div class="container-fluid well">

<div class="alert alert-<%=alert%>">
  <%=msg%>
</div>

<form method="post" id="baseurl" name="baseurl" action="addEditServiceCode.jsp">

<div class="span10">
Service Code <small>5 Characters, e.g. A001A</small><br>
<div class="input-append">
<input type="text" name="service_code" value="<%=prop.getProperty("service_code", "")%>" class="span2" maxlength='5' onblur="upCaseCtrl(this)" /> 
<button class="btn btn-primary" type="submit" name="submitFrm" value="Search" onclick="javascript:return onSearch();">Search</button>
</div>
		<br/>	
	<%
	    if( codes.size() > 1 ) {
	        Set dates = codes.keySet();
	        Iterator<String> i = dates.iterator();
	        String date;
	%>
	Edit Entry<br>
	<select name="billingservice_no" onchange="fetchBillService(this.options[this.selectedIndex].value);">
    <%
        while( i.hasNext() ) {
            date = i.next();
    %>
		<option value="<%=codes.get(date)%>" <%=prop.getProperty("billingservice_date", "").equalsIgnoreCase(date) ? "selected" : ""%>><%=date%>
	<%}%>
	</select>
	<	
	<%}%>
</div>


<div class="span10">
Description <small>50 Characters</small><br>
<textarea name="description" class="span6"><%=prop.getProperty("description", "")%></textarea>
</div>

<div class="span10">	
Style<br>
<select id="servicecode_style" name="servicecode_style" class="span2" onchange="displayStyleText(this.options[this.selectedIndex].value);" title="CSS Style Viewer">
	<option value="-1,None">None</option>
	<%
		for( CssStyle cssStyle: styles ) {
	%>
			<option value="<%=cssStyle.getId()+","+cssStyle.getStyle()%>" <%=prop.getProperty("displaystyle", "").equals(cssStyle.getId().toString())?"selected":""%>><%=cssStyle.getName()%></option>
	<%
		}

	%>
</select>
<br>
<textarea id="displayStyle" readonly="readonly" class="span6"></textarea>
</div>

<div class="span2">
Fee <small> e.g. 18.20</small><br>
<input type="text" name="value" value="<%=prop.getProperty("value", "")%>" size='8' maxlength='8'pattern="\d+(\.\d{2})?" ><br/>
</div>

<div class="span6">
Percentage <small> e.g. 0.20</small><br>
<input type="text" name="percentage" value="<%=prop.getProperty("percentage", "")%>" size='8' maxlength='8'>  
min.<input type="text" name="min" value="<%=prop.getProperty("min", "")%>" size='7' maxlength='8'> 
max.<input type="text" name="max" value="<%=prop.getProperty("max", "")%>" size='7' maxlength='8'>
</div>

<div class="span2">		
	<label>Issued Date</label>
	<div class="input-append">
		<input type="text" name="billingservice_date" id="billingservice_date" value="<%=prop.getProperty("billingservice_date", "")%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div>

<div class="span2">		
	<label>Termination Date</label>
	<div class="input-append">
		<input type="text" name="termination_date" id="termination_date" value="<%=prop.getProperty("termination_date", "9999-12-31")%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div>
	
	

<div class="span10">	
	<% String sliFlagValue = prop.getProperty("sliFlag", "0");
	   sliFlagValue = sliFlagValue.equals("1") || sliFlagValue.equals("true") ? "checked" : "";
	%>
	<input type="checkbox" name="sliFlag" id="sliFlag" value="true" <%=sliFlagValue%>> Requires SLI Code
</div>



<div class="span10">
<br>
<input type="hidden" id="action" name="action" value=''> <input class="btn" type="submit" name="submitFrm"
	value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
	onclick="document.getElementById('action').value='<%=action%>';return onSave();">
	
	<%
	if( !action2.equals("") ) {
	%>
	    <input class="btn" type="submit" name="submitFrm" value="<bean:message key="admin.resourcebaseurl.btnAdd"/>"
	    onclick="document.getElementById('action').value='<%=action2%>';return onSave();">
	<%}%>
</div>

</form>
</div>
<script type="text/javascript">
var startDate = $("#billingservice_date").datepicker({
	format : "yyyy-mm-dd"
});

var endDate = $("#termination_date").datepicker({
	format : "yyyy-mm-dd"
});
</script>

</body>
</html:html>
