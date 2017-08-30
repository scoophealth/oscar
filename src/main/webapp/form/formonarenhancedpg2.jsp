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

<%@ page import="oscar.form.graphic.*, oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ page import="org.oscarehr.common.web.PregnancyAction"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>

<%
    String ctx = request.getContextPath();
    String formClass = "ONAREnhanced";
    String formLink = "formonarenhancedpg2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg2");

    //get project_home
    String project_home = ctx.substring(1);   
    
    //load eform groups
    List<LabelValueBean> cytologyForms = PregnancyAction.getEformsByGroup("Cytology");
    List<LabelValueBean> ultrasoundForms = PregnancyAction.getEformsByGroup("Ultrasound");
    
    String customEformGroup = oscar.OscarProperties.getInstance().getProperty("prenatal_screening_eform_group");
    String prenatalScreenName = oscar.OscarProperties.getInstance().getProperty("prenatal_screening_name");
    String prenatalScreen = oscar.OscarProperties.getInstance().getProperty("prenatal_screening_abbrv");

    List<LabelValueBean> customForms = PregnancyAction.getEformsByGroup(customEformGroup);
 
    if(props.getProperty("rf_num", "0").equals("")) {props.setProperty("rf_num","0");}
    if(props.getProperty("sv_num", "0").equals("")) {props.setProperty("sv_num","0");}
    if(props.getProperty("us_num", "0").equals("")) {props.setProperty("us_num","0");}
    
    String labReqVer = oscar.OscarProperties.getInstance().getProperty("onare_labreqver","07");
    if(labReqVer.equals("")) {labReqVer="07";}

	boolean bView = false;
  	if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 

    FrmAREnhancedBloodWorkTest ar1BloodWorkTest = new FrmAREnhancedBloodWorkTest(demoNo, formId); 
    java.util.Properties ar1Props = ar1BloodWorkTest.getAr1Props(); 
    int ar1BloodWorkTestListSize = ar1BloodWorkTest.getAr1BloodWorkTestListSize(); 
    String ar1CompleteSignal = "AR1 labs Complete"; 

	String abo = "";
	String rh ="";
	if(UtilMisc.htmlEscape(props.getProperty("ar2_bloodGroup", "")).equals("") ){
		abo = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labABO", ""));
	}else{
		abo = UtilMisc.htmlEscape(props.getProperty("ar2_bloodGroup", ""));
	}

	if(UtilMisc.htmlEscape(props.getProperty("ar2_rh", "")).equals("")){
		rh = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labRh", ""));
	}else{
		rh = UtilMisc.htmlEscape(props.getProperty("ar2_rh", ""));
	}
	String hbsag = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labHBsAg",""));
	String rubella = UtilMisc.htmlEscape(ar1Props.getProperty("pg1_labRubella",""));
%>
<html:html locale="true">
<head>
<title>Antenatal Record 2</title>
<html:base />
<script type="text/javascript" src="<%= ctx %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="<%=bView?"arStyleView.css" : "arStyle.css"%>">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="<%=ctx%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script src="<%=ctx%>/js/fg.menu.js"></script>
<script type="text/javascript" src="<%=ctx%>/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ctx%>/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />


<link rel="stylesheet" href="<%=ctx%>/css/cupertino/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=ctx%>/css/fg.menu.css">

<script>
	$(document).ready(function(){	
		window.moveTo(0, 0);
		window.resizeTo(screen.availWidth, screen.availHeight);
	});
</script>

<style type="text/css">
body{
margin: 0;
padding: 0;
border: 0;
overflow: hidden;
height: 100%; 
max-height: 100%; 
}

#framecontent{
position: absolute;
top: 0;
bottom: 0; 
left: 0;
width: 190px; /*Width of frame div*/
height: 100%;
overflow: hidden; /*Disable scrollbars. Set to "scroll" to enable*/
background: navy;
color: white;
}

#maincontent{
position: fixed;
top: 0; 
left: 190px; /*Set left value to WidthOfFrameDiv*/
right: 0;
bottom: 0;
overflow: auto; 
background: #fff;
}

.innertube{
margin: 5px; /*Margins for inner DIV inside each DIV (to provide padding)*/
}

* html body{ /*IE6 hack*/
padding: 0 0 0 200px; /*Set value to (0 0 0 WidthOfFrameDiv)*/
}

* html #maincontent{ /*IE6 hack*/
height: 100%; 
width: 100%; 
}

#bottomContent td,th,input,select{font-size:12px}
</style>

<script>
	$(document).ready(function(){
	<%if(bView) { %>
		$('input[type="text"],input[type="checkbox"],select').each(function(){
			$(this).attr("disabled", "disabled");
		});		
		$("#rf_add_btn").hide();
		$("#sv_add_btn").hide();
		$("#sv_badd_btn").hide();
		$("#us_add_btn").hide();
		$("#lock_req_btn").hide();
	<% } %>

	    $(".limit-text").on('keypress keyup', function() {
			mlength = $(this).attr('maxlength');
			msg = $(this).attr('data-msg');
			limitCharacters($(this),mlength,msg);
		});

		$('.limit-text').each(function(i, obj) {
		    mlength = $(obj).attr('maxlength');
			msg = $(obj).attr('data-msg');
			length = mlength - parseInt($(obj).val().length);
			if(length<0) {
				length=0;
			}
			$("#"+msg).html(length + ' Characters left');
		});

	});

	function limitCharacters( el, mlength, lbl ){	
		length = mlength - parseInt($(el).val().length);
		if(length<0) {
			length=0;
			$(el).val($(el).val().substring(0,150));
		}
		$("#"+lbl).html(length + ' Characters left');
		$(el).val( $(el).val().replace( /\r?\n/gi, '' ) );
	}

	function loadCytologyForms() {
	<%
		if(cytologyForms != null) {
			if(cytologyForms.size()==0) {
				%>
					alert('No Cytology Forms configured');
				<%
			} else if(cytologyForms.size() == 1) {
				%>
				popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=cytologyForms.get(0).getValue()%>&demographic_no=<%=demoNo%>&appointment=0','cytology');			
				<%
			} else {
				%>$( "#cytology-eform-form" ).dialog( "open" );<%
			}
		}
	%>
	}

	function loadUltrasoundForms() {
		<%
			if(ultrasoundForms != null) {
				if(ultrasoundForms.size()==0) {
					%>
						alert('No Ultrasound Forms configured');
					<%
				} else if(ultrasoundForms.size() == 1) {
					%>
					popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=ultrasoundForms.get(0).getValue()%>&demographic_no=<%=demoNo%>&appointment=0','ultrasound');			
					<%
				} else {
					%>$( "#ultrasound-eform-form" ).dialog( "open" );<%
				}
			}
		%>
		}
		
		
function loadCustomForms() {
	<%
		if(customForms != null) {
			if(customForms.size()==0) {
				%>
					alert('No <%=customEformGroup%> Forms configured');
				<%
			} else if(customForms.size() == 1) {
				%>
				popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=customForms.get(0).getValue()%>&demographic_no=<%=demoNo%>&appointment=0','<%=customEformGroup%>form');			
				<%
			} else {
				%>$( "#custom-eform-form" ).dialog( "open" );<%
			}
		}
	%>
	}
	
	function validate() {
		for(var x=1;x<=70;x++) {
			if($('#sv_'+ x).length>0) {
				var patt1=new RegExp("^(\\d{4}/\\d{2}/\\d{2})?$");
				if(!patt1.test($("input[name='pg2_date"+x+"']").val())) {
					alert("Subsequent Visit - Date is invalid");
					return false;
				}
				
				patt1=new RegExp("^((\\d)+w(\\+\\d)?)?$");
				if(!patt1.test($("input[name='pg2_gest"+x+"']").val())) {
					alert("Subsequent Visit - Gestational Age is invalid");
					return false;
				}
				patt1=new RegExp("^(\\d{1,4}(\\.\\d)?)?$");
				if(!patt1.test($("input[name='pg2_wt"+x+"']").val())) {
					alert("Subsequent Visit - Weight is invalid");
					return false;
				}
				patt1=new RegExp("^(\\d{1,3}/\\d{1,3})?$");
				if(!patt1.test($("input[name='pg2_BP"+x+"']").val())) {
					alert("Subsequent Visit - BP is invalid");
					return false;
				}
				
				
			}
		}

		for(var x=1;x<=12;x++) {
			if($('#us_'+ x).length>0) {
				var patt1=new RegExp("^(\\d{4}/\\d{2}/\\d{2})?$");
				if(!patt1.test($("input[name='ar2_uDate"+x+"']").val())) {
					alert("U/S - Date is invalid");
					return false;
				}
				
				patt1=new RegExp("^((\\d)+w(\\+\\d)?)?$");
				if(!patt1.test($("input[name='ar2_uGA"+x+"']").val())) {
					alert("U/S - Gestational Age is invalid");
					return false;
				}
			}
		}
		
		return true;
	}

	$(document).ready(function() {
		$("select[name='ar2_strep']").val('<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_strep", "")) %>');
		$("select[name='ar2_bloodGroup']").val('<%= abo %>');
		$("select[name='ar2_rh']").val('<%= rh %>');
		$("select[name='ar2_labCustom1Label']").val('<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_labCustom1Label", "")) %>');
		$("select[name='ar2_labCustom2Label']").val('<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_labCustom2Label", "")) %>');
			
		
		if($("select[name='ar2_rh']").val() == 'NEG'/* && getGAWeek() >= 9*/) {			
			$("#rh_warn").show();
		}
		
		if($("select[name='ar2_rh']").val() == 'NEG' && getGAWeek() <= 28) {			
			$("#rhogam_warn").show();
		} else {
			$("#rhogam_warn").hide();
		}
		
		if('<%=StringEscapeUtils.escapeJavaScript(props.getProperty("pg1_labRubella", ""))%>' == 'Non-Immune' ) {			
			$("#rubella_warn").show();
		}
		
		if('<%=StringEscapeUtils.escapeJavaScript(props.getProperty("pg1_labHBsAg", ""))%>' == 'POS' ) {			
			$("#hbsag_warn").show();
		}
		
				
		if($("select[name='ar2_strep']").val() == 'NDONE' && getGAWeek()>=35) {
			$("#strep_prompt").show();
		}
		
		if(getGAWeek()>=36) {
			$("#fetal_pos_prompt").show();
		}
		
		if($("input[name='ar2_hb']").val().length > 0) {
			var hgb_result = parseFloat($("input[name='ar2_hb']").val());
			if(hgb_result < 110)
				$("#hgb_warn").show();
		}
		
		if($("input[name='ar2_lab1GCT']").val().length == 0 && getGAWeek() >=24 && getGAWeek() <= 28) {
			$("#gct_warn").show();
		}
		
		if($("input[name='ar2_lab1GCT']").val().length > 0) {
			var val = parseFloat($("input[name='ar2_lab1GCT']").val());
			if(val >= 7.8 && val <= 10.2) {
				$("#2hrgtt_prompt").show();
			}
			if(val > 10.2) {
				$("#gct_diabetes_warn").show();
			}
		}
	
	});
	
	function adjustDynamicListTotals() {		
		$('#rf_num').val(adjustDynamicListTotalsRF('rf_',20,true));
		$('#sv_num').val(adjustDynamicListTotalsSV('sv_',70,true));
		$('#us_num').val(adjustDynamicListTotalsUS('us_',12,true));
	}
	
	function adjustDynamicListTotalsRF(name,max,adjust) {		
		var total = 0;
		for(var x=1;x<=max;x++) {
			if($('#'+ name +x).length>0) {
				total++;
				if((x != total) && adjust) {
					$("#rf_"+x).attr('id','rf_'+total);				
					$("input[name='c_riskFactors"+x+"']").attr('name','c_riskFactors'+total);
					$("input[name='c_planManage"+x+"']").attr('name','c_planManage'+total);				
				}
			}			
		}	
		return total;
	}
	
	function adjustDynamicListTotalsSV(name,max,adjust) {		
		var total = 0;
		for(var x=1;x<=max;x++) {
			if($('#'+ name +x).length>0) {
				total++;
				if((x != total) && adjust) {			
					$("#sv_"+x).attr('id','sv_'+total);				
					$("input[name='pg2_date"+x+"']").attr('name','pg2_date'+total);
					$("input[name='pg2_gest"+x+"']").attr('name','pg2_gest'+total);
					$("input[name='pg2_wt"+x+"']").attr('name','pg2_wt'+total);
					$("input[name='pg2_BP"+x+"']").attr('name','pg2_BP'+total);
					$("input[name='pg2_urinePr"+x+"']").attr('name','pg2_urinePr'+total);
					//$("input[name='pg2_urineGl"+x+"']").attr('name','pg2_urineGl'+total);
					$("input[name='pg2_ht"+x+"']").attr('name','pg2_ht'+total);
					$("input[name='pg2_presn"+x+"']").attr('name','pg2_presn'+total);
					$("input[name='pg2_FHR"+x+"']").attr('name','pg2_FHR'+total);
					$("input[name='pg2_comments"+x+"']").attr('name','pg2_comments'+total);
				}
			}			
		}	
		return total;
	}
	
	function adjustDynamicListTotalsUS(name,max,adjust) {		
		var total = 0;
		for(var x=1;x<=max;x++) {
			if($('#'+ name +x).length>0) {
				total++;
				if((x != total) && adjust) {
					$("#us_"+x).attr('id','us_'+total);				
					$("input[name='ar2_uDate"+x+"']").attr('name','ar2_uDate'+total);
					$("input[name='ar2_uGA"+x+"']").attr('name','ar2_uGA'+total);
					$("input[name='ar2_uResults"+x+"']").attr('name','ar2_uResults'+total);						  
				}
			}			
		}	
		return total;
	}

function addRiskFactor() {
	if(adjustDynamicListTotalsRF("rf_",20,false) >= 20) {
		alert('Maximum number of rows is 20');
		return;
	}
	
	var total = jQuery("#rf_num").val();
	total++;
	jQuery("#rf_num").val(total);
	jQuery.ajax({url:'onarenhanced_rf.jsp?n='+total,async:false, success:function(data) {
		  jQuery("#rf_container tbody").append(data);
	}});
}

function deleteRiskFactor(id) {
	var followUpId = jQuery("input[name='rf_"+id+"']").val();
	jQuery("form[name='FrmForm']").append("<input type=\"hidden\" name=\"rf.delete\" value=\""+followUpId+"\"/>");
	jQuery("#rf_"+id).remove();

}

function setInput(id,type,val) {
	jQuery("input[name='"+type+id+"']").each(function() {
		jQuery(this).val(val);
	});
}

function setCheckbox(id,type,val) {
	jQuery("input[name='"+type+id+"']").each(function() {
		if(val=='true')
			jQuery(this).attr("checked",true);
		else
			jQuery(this).attr("checked",false);
	});
}

function addBulkSubsequentVisit(times) {
	for(var x=0;x<parseInt(times);x++) {
		addSubsequentVisit();
	}	
}

function addSubsequentVisit() {
	if(adjustDynamicListTotalsSV("sv_",70,false) >= 70) {
		alert('Maximum number of rows is 70');
		return;
	}
	
	var total = jQuery("#sv_num").val();
	total++;
	jQuery("#sv_num").val(total);
	jQuery.ajax({url:'onarenhanced_sv.jsp?n='+total,async:false, success:function(data) {
		  jQuery("#sv_tbody").append(data);
	}});
}

function deleteSubsequentVisit(id) {
	var followUpId = jQuery("input[name='sv_"+id+"']").val();
	jQuery("form[name='FrmForm']").append("<input type=\"hidden\" name=\"sv.delete\" value=\""+followUpId+"\"/>");
	jQuery("#sv_"+id).remove();

}

function addUltraSound() {
	if(adjustDynamicListTotalsUS("us_",12,false) >= 12) {
		alert('Maximum number of rows is 12');
		return;
	}
	
	var total = jQuery("#us_num").val();
	total++;
	jQuery("#us_num").val(total);
	jQuery.ajax({url:'onarenhanced_us.jsp?n='+total,async:false, success:function(data) {
		  jQuery("#us_container tbody").append(data);
	}});
	
	Calendar.setup({ inputField : "ar2_uDate"+total, ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate"+total+"_cal", singleClick : true, step : 1 });
}


function createCalendarSetupOnLoad(){
	var numItems = $('.ar2uDate').length;
	for(var x=1;x<=numItems;x++) {	
		Calendar.setup({ inputField : "ar2_uDate"+x, ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_uDate"+x+"_cal", singleClick : true, step : 1 });
	}
}


function deleteUltraSound(id) {
	var followUpId = jQuery("input[name='us_"+id+"']").val();
	jQuery("form[name='FrmForm']").append("<input type=\"hidden\" name=\"us.delete\" value=\""+followUpId+"\"/>");
	jQuery("#us_"+id).remove();

}

jQuery(document).ready(function() {

	<%
		String rf = props.getProperty("rf_num", "0");
		if(rf.length() == 0)
			rf = "0";		
		int rfNum = Integer.parseInt(rf);
		for(int x=0;x<rfNum;x++) {	
			int y=x+1;
		%>
		jQuery.ajax({url:'onarenhanced_rf.jsp?n='+<%=y%>,async:false, success:function(data) {
			  jQuery("#rf_container tbody").append(data);
			  setInput(<%=y%>,"c_riskFactors",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("c_riskFactors"+y, "")) %>');
			  setInput(<%=y%>,"c_planManage",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("c_planManage"+y, "")) %>');			 
		}});
		<%
		}
		if(rfNum == 0) {
			%>addRiskFactor();<%
		}
		if(bView) {
			%>
			$("a").each(function(){
				if($(this).html() == '[x]') {
					$(this).hide();
				}
			});
			<%
		}
	%>
	
	
	<%
	String sv = props.getProperty("sv_num", "0");
	if(sv.length() == 0)
		sv = "0";		
	int svNum = Integer.parseInt(sv);
	for(int x=0;x<svNum;x++) {
		int y=x+1;
	%>
		jQuery.ajax({url:'onarenhanced_sv.jsp?n='+<%=y%>,async:false, success:function(data) {			
		  jQuery("#sv_tbody").append(data);		  
		  setInput(<%=y%>,"pg2_date",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_date"+y, "")) %>');
		  setInput(<%=y%>,"pg2_gest",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_gest"+y, "")) %>');
		  setInput(<%=y%>,"pg2_wt",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_wt"+y, "")) %>');
		  setInput(<%=y%>,"pg2_BP",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_BP"+y, "")) %>');
		  setInput(<%=y%>,"pg2_urinePr",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_urinePr"+y, "")) %>');
		 //removed urineGl
		  setInput(<%=y%>,"pg2_ht",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_ht"+y, "")) %>');
		  setInput(<%=y%>,"pg2_presn",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_presn"+y, "")) %>');
		  setInput(<%=y%>,"pg2_FHR",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_FHR"+y, "")) %>');
		  setInput(<%=y%>,"pg2_comments",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("pg2_comments"+y, "")) %>');		  
	}});
	<%
	}
	if(svNum == 0) {
		%>addSubsequentVisit();<%
	}
	
	if(bView) {
		%>
		$("a").each(function(){
			if($(this).html() == '[x]') {
				$(this).hide();
			}
		});
		<%
	}
%>


<%
String us = props.getProperty("us_num", "0");
if(us.length() == 0)
	us = "0";		
int usNum = Integer.parseInt(us);
for(int x=1;x<usNum+1;x++) {			
%>
	jQuery.ajax({url:'onarenhanced_us.jsp?n='+<%=x%>,async:false, success:function(data) {
	  jQuery("#us_container tbody").append(data);
	  setInput(<%=x%>,"ar2_uDate",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_uDate"+x, "")) %>');
	  setInput(<%=x%>,"ar2_uGA",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_uGA"+x, "")) %>');
	  setInput(<%=x%>,"ar2_uResults",'<%= StringEscapeUtils.escapeJavaScript(props.getProperty("ar2_uResults"+x, "")) %>');	  
}});
<%
}
if(usNum == 0) {
	%>addUltraSound();<%
}
if(bView) {
	%>
	$("a").each(function(){
		if($(this).html() == '[x]') {
			$(this).hide();
		}
	});
	<%
}
%>

createCalendarSetupOnLoad();

});


$(document).ready(function(){
	<%if(!bView) { %>
	updatePageLock(false);
	<% } %>
});

var lockData;
var mylock=false;

function requestLock() {
	updatePageLock(true);
}

function releaseLock() {
	updatePageLock(false);
}

function updatePageLock(lock) {
	   haveLock=false;
		$.ajax({
		   type: "POST",
		   url: "<%=ctx%>/PageMonitoringService.do",
		   data: { method: "update", page: "formonarenhanced", pageId: "<%=demoNo%>", lock: lock },
		   dataType: 'json',
		   success: function(data,textStatus) {
			   lockData=data;
				var locked=false;
				var lockedProviderName='';
				var providerNames='';
				haveLock=false;
			   $.each(data, function(key, val) {				
			     if(val.locked) {
			    	 locked=true;
			    	 lockedProviderName=val.providerName;
			     }
			     if(val.locked==true && val.self==true) {
					   haveLock=true;
				   }
			     if(providerNames.length > 0)
			    	 providerNames += ",";
			     providerNames += val.providerName;
			     
			   });

			   var lockedMsg = locked?'<span style="color:red" title="'+lockedProviderName+'">&nbsp(locked)</span>':'';
			   $("#lock_notification").html(
				'<span title="'+providerNames+'">Viewers:'+data.length+lockedMsg+'</span>'	   
			   );
			   
			  
			   if(haveLock==true) { //i have the lock
					$("#lock_req_btn").hide();
				   	$("#lock_rel_btn").show();
			   } else if(locked && !haveLock) { //someone else has lock.
				   $("#lock_req_btn").hide();
			   		$("#lock_rel_btn").hide();
			   } else { //no lock
				   $("#lock_req_btn").show();
			   		$("#lock_rel_btn").hide();
			   }
		   }
		 }
		);
		setTimeout(function(){updatePageLock(haveLock)},30000);
		   
}

function gbsReq() {
	$( "#gbs-req-form" ).dialog( "open" );
	return false;
}

function onPrint2() {
	$( "#print-dialog" ).dialog( "open" );
	return false;
}


    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {
            if( document.forms[0].c_finalEDB.value == "" && !confirm("<bean:message key="oscarEncounter.formOnar.msgNoEDB"/>")) {
                ret = false;
            }
            else {
            	<%
            	StringBuilder urlExt = new StringBuilder();
            	int multiple=0;
            	if(Integer.parseInt(props.getProperty("rf_num", "0")) > 7) {
            		int num=multiple+1;
            		urlExt.append("&__title"+num+"=Antenatal+Record+Part+2&__cfgfile"+num+"=onar2enhancedPrintCfgPgRf&__template"+num+"=onar2enhancedrf&__numPages"+num+"=1&postProcessor"+num+"=ONAR2EnhancedPostProcessor");
            		multiple++;
            	}
            	if(Integer.parseInt(props.getProperty("sv_num", "0")) > 18) {
            		int num=multiple+1;
            		urlExt.append("&__title"+num+"=Antenatal+Record+Part+2&__cfgfile"+num+"=onar2enhancedPrintCfgPgSv&__template"+num+"=onar2enhancedsv&__numPages"+num+"=1&postProcessor"+num+"=ONAR2EnhancedPostProcessor");
            		multiple++;
            	}
            	if(Integer.parseInt(props.getProperty("sv_num", "0")) > 56) {
            		int num=multiple+1;
            		urlExt.append("&__title"+num+"=Antenatal+Record+Part+2&__cfgfile"+num+"=onar2enhancedPrintCfgPgSv2&__template"+num+"=onar2enhancedsv&__numPages"+num+"=1&postProcessor"+num+"=ONAR2EnhancedPostProcessor");
            		multiple++;
            	}
            	if(Integer.parseInt(props.getProperty("us_num", "0")) > 4) {
            		int num=multiple+1;
            		urlExt.append("&__title"+num+"=Antenatal+Record+Part+2&__cfgfile"+num+"=onar2enhancedPrintCfgPgUs&__template"+num+"=onar2enhancedus&__numPages"+num+"=1&postProcessor"+num+"=ONAR2EnhancedPostProcessor");
            		multiple++;
            	}
            	if(multiple>0) {
            		urlExt.append("&multiple="+(multiple+1));
            	}
            	%>
                document.forms[0].action = "../form/createpdf?__title=Antenatal+Record+Part+2&__cfgfile=onar2enhancedPrintCfgPg1&__cfgGraphicFile=onar2PrintGraphCfgPg1&__template=onar2<%=urlExt.toString()%>&postProcessor=ONAR2EnhancedPostProcessor";
                document.forms[0].target="_blank";       
            }
                
        }   
        return ret;
    }

    function getFormEntity(name) {
		if (name.value.length>0) {
			return true;
		} else {
			return false;
		}
		/*
		for (var j=0; j<document.forms[0].length; j++) { 
				if (document.forms[0].elements[j] != null && document.forms[0].elements[j].name ==  name ) {
					 return document.forms[0].elements[j] ;
				}
		}*/
    }
    function onWtSVG() {
        var ret = checkAllDates();
		var param="";
		var obj = null;
		if (document.forms[0].c_finalEDB != null && (document.forms[0].c_finalEDB.value).length==10) {
			param += "?c_finalEDB=" + document.forms[0].c_finalEDB.value;
		} else {
			ret = false;
		}
		if (document.forms[0].c_ppWt != null && (document.forms[0].c_ppWt.value).length>0) {
			param += "&c_ppWt=" + document.forms[0].c_ppWt.value;
		} else {
			ret = false;
		}

		obj = document.forms[0].pg2_date1;
		if (getFormEntity(obj))  param += "&pg2_date1=" + obj.value; 
		obj = document.forms[0].pg2_date2;
		if (getFormEntity(obj))  param += "&pg2_date2=" + obj.value; 
		obj = document.forms[0].pg2_date3;
		if (getFormEntity(obj))  param += "&pg2_date3=" + obj.value; 
		obj = document.forms[0].pg2_date4;
		if (getFormEntity(obj))  param += "&pg2_date4=" + obj.value; 
		obj = document.forms[0].pg2_date5;
		if (getFormEntity(obj))  param += "&pg2_date5=" + obj.value; 
		obj = document.forms[0].pg2_date6;
		if (getFormEntity(obj))  param += "&pg2_date6=" + obj.value; 
		obj = document.forms[0].pg2_date7;
		if (getFormEntity(obj))  param += "&pg2_date7=" + obj.value; 
		obj = document.forms[0].pg2_date8;
		if (getFormEntity(obj))  param += "&pg2_date8=" + obj.value; 
		obj = document.forms[0].pg2_date9;
		if (getFormEntity(obj))  param += "&pg2_date9=" + obj.value; 
		obj = document.forms[0].pg2_date10;
		if (getFormEntity(obj))  param += "&pg2_date10=" + obj.value; 
		obj = document.forms[0].pg2_date11;
		if (getFormEntity(obj))  param += "&pg2_date11=" + obj.value; 
		obj = document.forms[0].pg2_date12;
		if (getFormEntity(obj))  param += "&pg2_date12=" + obj.value; 
		obj = document.forms[0].pg2_date13;
		if (getFormEntity(obj))  param += "&pg2_date13=" + obj.value; 
		obj = document.forms[0].pg2_date14;
		if (getFormEntity(obj))  param += "&pg2_date14=" + obj.value; 
		obj = document.forms[0].pg2_date15;
		if (getFormEntity(obj))  param += "&pg2_date15=" + obj.value; 
		obj = document.forms[0].pg2_date16;
		if (getFormEntity(obj))  param += "&pg2_date16=" + obj.value; 
		obj = document.forms[0].pg2_date17;
		if (getFormEntity(obj))  param += "&pg2_date17=" + obj.value; 
		obj = document.forms[0].pg2_wt1;
		if (getFormEntity(obj))  param += "&pg2_wt1=" + obj.value; 
		obj = document.forms[0].pg2_wt2;
		if (getFormEntity(obj))  param += "&pg2_wt2=" + obj.value; 
		obj = document.forms[0].pg2_wt3;
		if (getFormEntity(obj))  param += "&pg2_wt3=" + obj.value; 
		obj = document.forms[0].pg2_wt4;
		if (getFormEntity(obj))  param += "&pg2_wt4=" + obj.value; 
		obj = document.forms[0].pg2_wt5;
		if (getFormEntity(obj))  param += "&pg2_wt5=" + obj.value; 
		obj = document.forms[0].pg2_wt6;
		if (getFormEntity(obj))  param += "&pg2_wt6=" + obj.value; 
		obj = document.forms[0].pg2_wt7;
		if (getFormEntity(obj))  param += "&pg2_wt7=" + obj.value; 
		obj = document.forms[0].pg2_wt8;
		if (getFormEntity(obj))  param += "&pg2_wt8=" + obj.value; 
		obj = document.forms[0].pg2_wt9;
		if (getFormEntity(obj))  param += "&pg2_wt9=" + obj.value; 
		obj = document.forms[0].pg2_wt10;
		if (getFormEntity(obj))  param += "&pg2_wt10=" + obj.value; 
		obj = document.forms[0].pg2_wt11;
		if (getFormEntity(obj))  param += "&pg2_wt11=" + obj.value; 
		obj = document.forms[0].pg2_wt12;
		if (getFormEntity(obj))  param += "&pg2_wt12=" + obj.value; 
		obj = document.forms[0].pg2_wt13;
		if (getFormEntity(obj))  param += "&pg2_wt13=" + obj.value; 
		obj = document.forms[0].pg2_wt14;
		if (getFormEntity(obj))  param += "&pg2_wt14=" + obj.value; 
		obj = document.forms[0].pg2_wt15;
		if (getFormEntity(obj))  param += "&pg2_wt15=" + obj.value; 
		obj = document.forms[0].pg2_wt16;
		if (getFormEntity(obj))  param += "&pg2_wt16=" + obj.value; 
		obj = document.forms[0].pg2_wt17;
		if (getFormEntity(obj))  param += "&pg2_wt17=" + obj.value; 
/*
		for (var i = 1; i < 18; i++) {
				getFormEntity(("pg2_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg2_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg2_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg2_wt" + i + "=" + obj.value;
			}
		}
		for (var i = 18; i < 35; i++) {
			var obj = getFormEntity(("pg3_date"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg3_date" + i + "=" + obj.value;
			}
			obj = getFormEntity(("pg3_wt"+i)) ;
			if (obj != null && (obj.value).length>0) {
				param += "&pg3_wt" + i + "=" + obj.value;
			}
		}
*/

        if(ret==true)  {
            popupFixedPage(650,850,'formonar2wt.jsp'+param);
        }

    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        var ret1 = validate();
        if(ret==true && ret1 == true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        adjustDynamicListTotals();
        return ret && ret1;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        var ret1 = validate();
        if(ret==true && ret1 == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        adjustDynamicListTotals();
        return ret && ret1;
    }

    function onPageChange(url) {
    	var result = false;
    	var newID = 0;
    	document.forms[0].submit.value="save";
        var ret1 = validate();
        var ret = checkAllDates();
        if(ret==true && ret1==true)
        {
            reset();
            ret = confirm("Are you sure you want to save this form?");
            if(ret) {
	            window.onunload=null;
	            adjustDynamicListTotals();
	            jQuery.ajax({
	            	url:'<%=ctx%>/Pregnancy.do?method=saveFormAjax',
	            	data: $("form").serialize(),
	            	async:false, 
	            	dataType:'json', 
	            	success:function(data) {
	        			if(data.value == 'error') {
	        				alert('Error saving form.');
	        				result = false;	        				
	        			} else {
	        				result= true;
	        				newID = parseInt(data.value);
	        			}
	        		}
	            });
            } else {
            	url = url.replace('#id','<%=formId%>');
            	location.href=url;
            }
        }
        
        if(result == true) {
        	url = url.replace('#id',newID);
        	location.href=url;
        }
          
       return;
    }
    function onExit() {
    	<% if(!bView) { %>
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
        	refreshOpener();
            window.close();
        }
        return(false);
        <% } else { %>
        	window.close();
        	return false;
        <% } %>
    }
    function popupRequisitionPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar1", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    // open a new popup window
    function popupPage(vheight,vwidth,varpage) { 
      var page = "" + varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
      var popup=window.open(page, "attachment", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self; 
        }
      }
    }

    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }

	function isNumber(ss){
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
			if (c == '.') {
				continue;
			} else if (((c < "0") || (c > "9"))) {
                alert('Invalid '+s+' in field ' + ss.name);
                ss.focus();
                return false;
			}
        }
        // All characters are numbers.
        return true;
    }
function popupFixedPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
  var popup=window.open(page, "planner", windowprop);
}
function wtEnglish2Metric(obj) {
	//if(isNumber(document.forms[0].c_ppWt) ) {
	//	weight = document.forms[0].c_ppWt.value;
	if(isNumber(obj) ) {
		weight = obj.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			//document.forms[0].c_ppWt.value = weightM;
			obj.value = weightM;
		}
	}
}
function htEnglish2Metric(obj) {
	height = obj.value;
	if(height.length > 1 && height.indexOf("'") > 0 ) {
		feet = height.substring(0, height.indexOf("'"));
		inch = height.substring(height.indexOf("'"));
		if(inch.length == 1) {
			inch = 0;
		} else {
			inch = inch.charAt(inch.length-1)=='"' ? inch.substring(0, inch.length-1) : inch;
			inch = inch.substring(1);
		}
		
		//if(isNumber(feet) && isNumber(inch) )
			height = Math.round((feet * 30.48 + inch * 2.54) * 10) / 10 ;
			if(confirm("Are you sure you want to change " + feet + " feet " + inch + " inch(es) to " + height +"cm?") ) {
				obj.value = height;
			}
		//}
	}
}

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=9900;

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }

    function stripCharsInBag(s, bag){
        var i;
        var returnString = "";
        // Search through string's characters one by one.
        // If character is not in bag, append to returnString.
        for (i = 0; i < s.length; i++){
            var c = s.charAt(i);
            if (bag.indexOf(c) == -1) returnString += c;
        }
        return returnString;
    }

    function daysInFebruary (year){
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }
    function DaysArray(n) {
        for (var i = 1; i <= n; i++) {
            this[i] = 31
            if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
            if (i==2) {this[i] = 29}
       }
       return this
    }

    function isDate(dtStr){
        var daysInMonth = DaysArray(12)
        var pos1=dtStr.indexOf(dtCh)
        var pos2=dtStr.indexOf(dtCh,pos1+1)
        var strMonth=dtStr.substring(0,pos1)
        var strDay=dtStr.substring(pos1+1,pos2)
        var strYear=dtStr.substring(pos2+1)
        strYr=strYear
        if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
        if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
        }
        month=parseInt(strMonth)
        day=parseInt(strDay)
        year=parseInt(strYr)
        if (pos1==-1 || pos2==-1){
            return "format"
        }
        if (month<1 || month>12){
            return "month"
        }
        if (day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
            return "day"
        }
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            return "year"
        }
        if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
            return "date"
        }
    return true
    }


    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }

    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[0];
            var m = dt[1];
            var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }
        catch (ex)
        {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].c_finalEDB)==false){
            b = false;
        }else
        if(valDate(document.forms[0].pg2_formDate)==false){
            b = false;
        }
        return b;
    }

	function calcWeek(source) {		
<%
String fedb = props.getProperty("c_finalEDB", "");

String sDate = "";
if (!fedb.equals("") && fedb.length()==10 ) {
	FrmGraphicAR arG = new FrmGraphicAR();
	java.util.Date edbDate = arG.getStartDate(fedb);
    sDate = UtilDateUtilities.DateToString(edbDate, "MMMMM dd, yyyy"); //"yy,MM,dd");
%>
	    var delta = 0;
        var str_date = getDateField(source.name);        
        if (str_date.length < 10) return;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var check_date=new Date(yyyy,mm,dd);
        var start=new Date("<%=sDate%>");

		if (check_date.getUTCHours() != start.getUTCHours()) {
			if (check_date.getUTCHours() > start.getUTCHours()) {
			    delta = -1 * 60 * 60 * 1000;
			} else {
			    delta = 1 * 60 * 60 * 1000;
			}
		} 

		var day = eval((check_date.getTime() - start.getTime() + delta) / (24*60*60*1000));
        var week = Math.floor(day/7);
		var weekday = day%7;
        source.value = week + "w+" + weekday;
<% } %>
}

	function getDateField(name) {
		var temp = ""; //pg2_gest1 - pg2_date1
		var n1 = name.substring(eval(name.indexOf("t")+1));

		if(name.indexOf("ar2_")>=0) {
			n1 = name.substring(eval(name.indexOf("A")+1));
			name = "ar2_uDate" + n1;
		} else {
			name = "pg2_date" + n1;
		}
        
        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].name == name) {
               return document.forms[0].elements[i].value;
    	    }
	    }
        return temp;
    }
function calToday(field) {	
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}


/*
 * This function takes the EDB, removes 40 weeks (280 days), then looks at the difference 
 * between today and that start date to get the number of days into the pregnancy
 */
function getGADay() {
	 var finalEDB = $("input[name='c_finalEDB']").val();
	 if(finalEDB.length != 10) {
		 return -1;
	 }	 
	 var year = finalEDB.substring(0,4);
	 var month = finalEDB.substring(5,7)
	 var day = finalEDB.substring(8,10)
	 
	 
	 var edbDate=new Date(year,parseInt(month.substring(0,1)=='0'?month.substring(1,2):month)-1,day);
	 edbDate.setHours(8);
	 edbDate.setMinutes(0);
	 edbDate.setSeconds(0);
	 edbDate.setMilliseconds(0);
	 
	 var startDate = new Date();
	 startDate.setTime(edbDate.getTime()-(280*1000*60*60*24)  );
	 startDate.setHours(8);		
	 	  
	 var today = new Date();
	 today.setHours(8);
	 today.setMinutes(0);
	 today.setSeconds(0);
	 today.setMilliseconds(0);
	 
	 if(today > startDate) {			
		 var  days = daydiff(startDate,today);
		 days = Math.round(days);
		 return days;
	 }
	 
	 return -1;
}

function daydiff(first, second) {
    return (second-first)/(1000*60*60*24)
}

function getGAWeek() {
	var day = getGADay();
	var week;
	if(day > 0) {
		week = parseInt(day / 7);	
	}
	return parseInt(week);
}

function getGA() {
	var day = getGADay();
	var week;
	var offset;
	if(day > 0) {
		week = parseInt(day / 7);
		offset = day % 7;		
	}
	return parseInt(week) + "w+" + offset;
}

$(document).ready(function() {
	$("input[name='ar2_lab2GTT1']").bind('keyup',function(){
		updateGtt();
	});
	$("input[name='ar2_lab2GTT2']").bind('keyup',function(){
		updateGtt();
	});
	$("input[name='ar2_lab2GTT3']").bind('keyup',function(){
		updateGtt();
	});
	
	var gttVal = $("input[name='ar2_lab2GTT']").val();
	if(gttVal.length > 0) {
		var parts = gttVal.split("/");
		$("input[name='ar2_lab2GTT1']").val(parts[0]);
		$("input[name='ar2_lab2GTT2']").val(parts[1]);
		$("input[name='ar2_lab2GTT3']").val(parts[2]);
	}
});

function updateGtt() {
	$("input[name='ar2_lab2GTT']").val($("input[name='ar2_lab2GTT1']").val() + "/" + $("input[name='ar2_lab2GTT2']").val() + "/" + $("input[name='ar2_lab2GTT3']").val());
}

$(document).ready(function(){
	if('<%=rh%>' == 'NEG') {
		$("input[name='ar2_rhNeg']").attr('checked',true);
		$("#rhNegSpan").css('background-color','red');
	}	
	
	$("input[name='ar2_rhNeg']").bind('change',function(){
		if($("input[name='ar2_rhNeg']").attr('checked') == 'checked') {
			$("#rhNegSpan").css('background-color','red');
		} else {
			$("#rhNegSpan").css('background-color','');
		}
	});
	
	
	if('<%=hbsag%>' == 'POS') {
		$("input[name='ar2_hepBIG']").attr('checked',true);
		$("#hepbSpan").css('background-color','red');
		$("input[name='ar2_hepBVac']").attr('checked',true);
		$("#hepbSpan2").css('background-color','red');
	}	
	
	$("input[name='ar2_hepBIG']").bind('change',function(){
		if($("input[name='ar2_hepBIG']").attr('checked') == 'checked') {
			$("#hepbSpan").css('background-color','red');
		} else {
			$("#hepbSpan").css('background-color','');
		}
	});
	
	$("input[name='ar2_hepBVac']").bind('change',function(){
		if($("input[name='ar2_hepBVac']").attr('checked') == 'checked') {
			$("#hepbSpan2").css('background-color','red');
		} else {
			$("#hepbSpan2").css('background-color','');
		}
	});
	
	if('<%=rubella%>' == 'Non-Immune' || '<%=rubella%>' == 'Indeterminate') {
		$("input[name='ar2_rubella']").attr('checked',true);
		$("#rubellaSpan").css('background-color','red');
	}	
	
	$("input[name='ar2_rubella']").bind('change',function(){
		if($("input[name='ar2_rubella']").attr('checked') == 'checked') {
			$("#rubellaSpan").css('background-color','red');
		} else {
			$("#rubellaSpan").css('background-color','');
		}
	});
	
});

$(document).ready(function(){

	$( "#cytology-eform-form" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Dismiss": function() {			
				$( this ).dialog( "close" );	
			}
		},
		close: function() {
			
		}
	});
	
	$( "#ultrasound-eform-form" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Dismiss": function() {			
				$( this ).dialog( "close" );	
			}
		},
		close: function() {
			
		}
	});
	
	$( "#custom-eform-form" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Dismiss": function() {			
				$( this ).dialog( "close" );	
			}
		},
		close: function() {
			
		}
	});	
	
	$( "#edb-update-form" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Dismiss": function() {			
				$( this ).dialog( "close" );	
			}
		},
		close: function() {
			
		}
	});
	
	$( "#gbs-req-form" ).dialog({
		autoOpen: false,
		height: 275,
		width: 450,
		modal: true,
		buttons: {
			"Generate Requisition": function() {			
				$( this ).dialog( "close" );	
				var penicillin = $("#penicillin").attr('checked');	
				var demographic = '<%=props.getProperty("demographic_no", "0")%>';
				var user = '<%=session.getAttribute("user")%>';
				url = '<%=ctx%>/form/formlabreq<%=labReqVer %>.jsp?demographic_no='+demographic+'&formId=0&provNo='+user + '&fromSession=true';
				jQuery.ajax({url:'<%=ctx%>/Pregnancy.do?method=createGBSLabReq&demographicNo='+demographic + '&penicillin='+penicillin,async:false, success:function(data) {
					popupRequisitionPage(url);
				}});								
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			
		}
	});
	
	$( "#print-dialog" ).dialog({
		autoOpen: false,
		height: 275,
		width: 450,
		modal: true,
		buttons: {
			"Print": function() {			
				$( this ).dialog( "close" );	
				var printAr1 = $("#print_ar1").attr('checked');
				var printAr2 = $("#print_ar2").attr('checked');	
				var printLocation = $("#print_location").val();
				var printMethod = $("#print_method").val();
				var demographic = '<%=props.getProperty("demographic_no", "0")%>';
				var user = '<%=session.getAttribute("user")%>';
				
				var rfNum = $("#rf_num").val();
				var svNum = $("#sv_num").val();
				var usNum = $("#us_num").val();					
				
				
				var obxNum = '<%=props.getProperty("obxhx_num", "0")%>';								
				var hasExtraComments = <%=(props.getProperty("pg1_comments2AR1", "").length() > 0)%>;
								
				if ((typeof printAr1 == "undefined") && (typeof printAr2 == "undefined")) {
					return;
				}
				
				   
				if(printLocation.length>0) {
					jQuery.ajax({type:"POST",url:'<%=ctx%>/Pregnancy.do?method=recordPrint',data: {printLocation:printLocation,printMethod:printMethod,resourceName:'ONAREnhanced',resourceId:$('#episodeId').val()},async:true, success:function(data) {
						//do nothing at this time
					}});					
				}
								
								
				var ret = checkAllDates();
		        if(ret==true)
		        {		        	
		        	document.forms[0].submit.value="print"; 
		        	document.forms[0].target="_blank";        
		        	var url = "../form/createpdf?";
		        	var multiple=0;
		        	if (!(typeof printAr1 == "undefined")) {
			        	url += "__title=Antenatal+Record+Part+1&__cfgfile=onar1enhancedPrintCfgPg1&__template=onar1&__numPages=1&postProcessor=ONAR1EnhancedPostProcessor";		        	
			        	
			        	if((obxNum.length>0 && parseInt(obxNum)>6) || hasExtraComments) {
			        		multiple++;
			        		url = url+"&__title1=Antenatal+Record+Part+1&__cfgfile1=onar1enhancedPrintCfgPg2&__template1=onar1enhancedpg2&__numPages1=1&postProcessor1=ONAR1EnhancedPostProcessor";		        		
			        	}
		        	}
		        	if (!(typeof printAr2 == "undefined")) {
		        		if( document.forms[0].c_finalEDB.value == "" && !confirm("<bean:message key="oscarEncounter.formOnar.msgNoEDB"/>")) {
		                   return;
		                }		        		
		        		if (!(typeof printAr1 == "undefined")) {
		        			multiple++;
		        			url+="__title"+multiple+"=Antenatal+Record+Part+2&__cfgfile"+multiple+"=onar2enhancedPrintCfgPg1&__cfgGraphicFile"+multiple+"=onar2PrintGraphCfgPg1&__template"+multiple+"=onar2&postProcessor"+multiple+"=ONAR2EnhancedPostProcessor";
		        		} else {
		        			url+="__title=Antenatal+Record+Part+2&__cfgfile=onar2enhancedPrintCfgPg1&__cfgGraphicFile=onar2PrintGraphCfgPg1&__template=onar2&postProcessor=ONAR2EnhancedPostProcessor";
		        		}
		        		
			        	if(rfNum.length>0 && parseInt(rfNum)>7) {
			        		multiple++;		        		
			        		url = url+"&__title"+multiple+"=Antenatal+Record+Part+2&__cfgfile"+multiple+"=onar2enhancedPrintCfgPgRf&__template"+multiple+"=onar2enhancedrf&__numPages"+multiple+"=1&postProcessor"+multiple+"=ONAR2EnhancedPostProcessor";    	
			        	}
			        	if(svNum.length>0 && parseInt(svNum)>18) {
			        		multiple++;	
			        		url = url+"&__title"+multiple+"=Antenatal+Record+Part+2&__cfgfile"+multiple+"=onar2enhancedPrintCfgPgSv&__template"+multiple+"=onar2enhancedsv&__numPages"+multiple+"=1&postProcessor"+multiple+"=ONAR2EnhancedPostProcessor";
			        	}
			        	if(svNum.length>0 && parseInt(svNum)>56) {
			        		multiple++;	
			        		url = url+"&__title"+multiple+"=Antenatal+Record+Part+2&__cfgfile"+multiple+"=onar2enhancedPrintCfgPgSv2&__template"+multiple+"=onar2enhancedsv&__numPages"+multiple+"=1&postProcessor"+multiple+"=ONAR2EnhancedPostProcessor";
			        	}
			        	if(usNum.length>0 && parseInt(usNum)>4) {
			        		multiple++;
			        		url=url+"&__title"+multiple+"=Antenatal+Record+Part+2&__cfgfile"+multiple+"=onar2enhancedPrintCfgPgUs&__template"+multiple+"=onar2enhancedus&__numPages"+multiple+"=1&postProcessor"+multiple+"=ONAR2EnhancedPostProcessor";
			        	}
		        	}
		        	if(multiple>0) {
		        		url=url+"&multiple="+(multiple+1);
		        	}
		        	//go to it
		        	document.forms[0].action=url;
		        	$("#printBtn").click();
		        }
				
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			
		}
	});
	
	
	$( "#print-log-dialog" ).dialog({
		autoOpen: false,
		height: 350,
		width: 650,
		modal: true,
		buttons: {
			Dismiss: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			
		}
	});
});

    $(function(){

<% if(!bView) { %>     			
		$('#gbs_menu').bind('click',function(){gbsReq();});
		$("#gd_menu").bind('click',function(){popPage('http://www.diabetes.ca/diabetes-and-you/what/gestational/','resource')});
		$("#gct_menu").bind('click',function(){gctReq();});
		$("#gtt_menu").bind('click',function(){gttReq();});
		$("#edb_menu").bind('click',function(){
			var usNum = checkSOGCGuidelineForEDB();
			if(usNum > 0) {
				var usDate = $("#ar2_uDate"+usNum).val();
				var usGA = $("#ar2_uGA"+usNum).val();
				$("#edb_update_table tbody").append("<tr><td><input type=\"button\" onClick=\"updateEDB();\" value=\"Update\"/></td><td>Due to Ultrasound #"+usNum+" completed on "+usDate+", SOGC recommends a new EDB of "+newEDB.toLocaleFormat('%Y/%m/%d')+"</td></tr>");
				$( "#edb-update-form" ).dialog( "open" );
			}			
		});
		
		$("#print_log_menu").bind('click',function(){
			jQuery.ajax({type:"POST",url:'<%=ctx%>/Pregnancy.do?method=getPrintData',data: {resourceName:'ONAREnhanced',resourceId:$('#episodeId').val()},dataType:'json',async:true, success:function(data) {
				$("#print_log_table tbody").html("");
				$.each(data, function(key, val) {
					$("#print_log_table tbody").append('<tr><td>'+val.formattedDateString+'</td><td>'+val.providerName+'</td><td>'+val.externalLocation+'</td><td>'+val.externalMethod+'</td></tr>');					
				});
				$( "#print-log-dialog" ).dialog("open");
			}});
		});
		
		$('#lab_menu').menu({ 
			content: $('#lab_menu_div').html(), 
			showSpeed: 400 
		});
		
		$('#forms_menu').menu({ 
			content: $('#forms_menu_div').html(), 
			showSpeed: 400 
		});

<% } %>

		$('#graph_menu').bind('click',function(){
			fancyBoxFundal();
		});
		
		function fancyBoxFundal(){
			$("#fundal_link").attr('href',getFundalImageUrl('1'));
			$("#fundal_link").fancybox({type:'image'});
			$("#fundal_link").click();	
		}
		
		function getFundalImageUrl(c){
			var url="<%=ctx%>/Pregnancy.do?method=getFundalImage";
			//gest,ht
			var params="";
			var n = getGestHtVal();
			for(var x=1;x<=n;x++) {

				var gest = $("input[name='pg2_gest"+x+"']").val();
				var ht = $("input[name='pg2_ht"+x+"']").val();
				
				//if gest or ht are empty and the params not added the index breaks so change to 0 to not break the index which is the number that follows ga or ht
				if(gest==""){
					gest = "0";
				}
				
				if(ht==""){
					ht="0";
				}
					if(gest != undefined && gest.length>0 && ht != undefined && ht.length>0) {					
						params = params + "&ga"+x+"="+escape(gest)+"&fh"+x+"="+escape(ht);
					}
					
					
			}
			
			url += params;

			if(c=="1"){
				return url;
			}else{
				$("#fundalImage").attr('src',url);
			}
			
		}
		
	    $(document).on("change","input[name^='pg2_gest']", function(){   
	         getFundalImageUrl();
	    });
	    
	    $(document).on("change","input[name^='pg2_ht']", function(){
	         getFundalImageUrl();
	    });

				
		function getGestHtVal(){
			var n = $(":input[name^='pg2_gest']").length;
			return n;
		}
		
		$("#fundalImageLink").click(function(){
			getFundalImageUrl();
			fancyBoxFundal();
		});
		
		getFundalImageUrl();
		
		if(isFundalHeightOffTarget()) {
			$("#fundal_graph_text").css('color','red');
		}
    
    });

	function wk24VisitTool() {
		$( "#24wk-visit-form" ).dialog( "open" );	
	}
	function wk35VisitTool() {
		$( "#35wk-visit-form" ).dialog( "open" );
	}
	function ddVisitTool() {
		$( "#dd-visit-form" ).dialog( "open" );
	}
	
	$(function(){
		
		$("#24wk_visit_menu").bind('click',function(){wk24VisitTool();});
		$("#35wk_visit_menu").bind('click',function(){wk35VisitTool();});
		$("#dd_visit_menu").bind('click',function(){ddVisitTool();});
		
		var ga = getGA();
		if(ga != 'NaNw+undefined')
			$('#gest_age').html(ga);
		
		$( "#gct-req-form" ).dialog({
			autoOpen: false,
			height: 275,
			width: 450,
			modal: true,
			buttons: {
				"Generate Requisition": function() {			
					$( this ).dialog( "close" );			
					var gct_hb = $("#gct_hb").attr('checked');
					var gct_urine = $("#gct_urine").attr('checked');
					var gct_ab = $("#gct_ab").attr('checked');
					var gct_glu = $("#gct_glu").attr('checked');
					var user = '<%=session.getAttribute("user")%>';
					url = '<%=ctx%>/form/formlabreq<%=labReqVer %>.jsp?demographic_no=<%=demoNo%>&formId=0&provNo='+user + '&fromSession=true';
					var pregUrl = '<%=ctx%>/Pregnancy.do?method=createGCTLabReq&demographicNo=<%=demoNo%>&hb='+gct_hb+'&urine='+gct_urine+'&antibody='+gct_ab+'&glucose='+gct_glu;
					jQuery.ajax({url:pregUrl,async:false, success:function(data) {
						popupRequisitionPage(url);
					}});
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$( "#gtt-req-form" ).dialog({
			autoOpen: false,
			height: 275,
			width: 450,
			modal: true,
			buttons: {
				"Generate Requisition": function() {			
					$( this ).dialog( "close" );			
					var gtt_glu = $("#gtt_glu").attr('checked');
					var user = '<%=session.getAttribute("user")%>';
					url = '<%=ctx%>/form/formlabreq<%=labReqVer %>.jsp?demographic_no=<%=demoNo%>&formId=0&provNo='+user + '&fromSession=true';
					var pregUrl = '<%=ctx%>/Pregnancy.do?method=createGTTLabReq&demographicNo=<%=demoNo%>&glucose='+gtt_glu;
					jQuery.ajax({url:pregUrl,async:false, success:function(data) {
						popupRequisitionPage(url);
					}});
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$( "#ips-form" ).dialog({
			autoOpen: false,
			height: 275,
			width: 450,
			modal: true,
			buttons: {
				"Dismiss": function() {			
					$( this ).dialog( "close" );	
				}
			},
			close: function() {
				
			}
		});
		
		$( "#24wk-visit-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 450,
			modal: true,
			buttons: {
				"Dismiss": function() {			
					$( this ).dialog( "close" );	
				}
			},
			close: function() {
				
			}
		});
		
		$( "#35wk-visit-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 450,
			modal: true,
			buttons: {
				"Dismiss": function() {			
					$( this ).dialog( "close" );	
				}
			},
			close: function() {
				
			}
		});
		
		$( "#dd-visit-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 450,
			modal: true,
			buttons: {
				"Dismiss": function() {			
					$( this ).dialog( "close" );	
				}
			},
			close: function() {
				
			}
		});
		
	});
	
	function gctReq() {
		$( "#gct-req-form" ).dialog( "open" );
		return false;
	}
	
	function gttReq() {
		$( "#gtt-req-form" ).dialog( "open" );
		return false;
	}
	
	function ipsForms() {
		$( "#ips-form" ).dialog( "open" );
	}

	function isFundalHeightOffTarget() {
		var off=false;
		
		for(var x=1;x<=70;x++) {
			var gest = $("input[name='pg2_gest"+x+"']").val();
			var ht = $("input[name='pg2_ht"+x+"']").val();
			if(gest != undefined && gest.length>0 && ht != undefined && ht.length>0) {		
				var wks = gest.substring(0,gest.indexOf('w'));
				var offset=0;
				if(gest.indexOf('+')!=-1) {
					offset = gest.substring(gest.indexOf('+')+1);
				}
				var gatmp = parseInt(wks) + (parseInt(offset)/7);
				var fh = parseFloat(ht);
				
				if(gatmp >= 20 && gatmp < 21) {
					if(fh < 15.5 || fh > 23)
						off=true;
				} else if(gatmp >= 21 && gatmp < 22) {
					if(fh < 16 || fh > 24.5)
						off=true;
				} else if(gatmp >= 22 && gatmp < 23) {
					if(fh < 16.5 || fh > 26)
						off=true;
				} else if(gatmp >= 23 && gatmp < 24) {
					if(fh < 17.5 || fh > 26.5)
						off=true;
				} else if(gatmp >= 24 && gatmp < 25) {
					if(fh < 19 || fh > 27.5)
						off=true;
				} else if(gatmp >= 25 && gatmp < 26) {
					if(fh < 20 || fh > 29)
						off=true;
				} else if(gatmp >= 26 && gatmp < 27) {
					if(fh < 21 || fh > 29.5)
						off=true;
				} else if(gatmp >= 27 && gatmp < 28) {
					if(fh < 21 || fh > 31)
						off=true;
				} else if(gatmp >= 28 && gatmp < 29) {
					if(fh < 24 || fh > 32)
						off=true;
				} else if(gatmp >= 29 && gatmp < 30) {
					if(fh < 25.5 || fh > 33)
						off=true;
				} else if(gatmp >= 30 && gatmp < 31) {
					if(fh < 26 || fh > 34)
						off=true;
				} else if(gatmp >= 31 && gatmp < 32) {
					if(fh < 28 || fh > 35)
						off=true;
				} else if(gatmp >= 32 && gatmp < 33) {
					if(fh < 28 || fh > 36)
						off=true;
				} else if(gatmp >= 33 && gatmp < 34) {
					if(fh < 28.5 || fh > 36)
						off=true;
				} else if(gatmp >= 34 && gatmp < 35) {
					if(fh < 29 || fh > 37)
						off=true;
				} else if(gatmp >= 35 && gatmp < 36) {
					if(fh < 29.5 || fh > 37.5)
						off=true;
				} else if(gatmp >= 36 && gatmp < 37) {
					if(fh < 30.5 || fh > 38)
						off=true;
				} else if(gatmp >= 37 && gatmp < 38) {
					if(fh < 31 || fh > 39)
						off=true;
				} else if(gatmp >= 38 && gatmp < 39) {
					if(fh < 32 || fh > 39.5)
						off=true;
				} else if(gatmp >= 39 && gatmp < 40) {
					if(fh < 32 || fh > 40)
						off=true;
				} else if(gatmp >= 40 && gatmp < 41) {
					if(fh < 32.5 || fh > 40.5)
						off=true;
				} else if(gatmp >= 42 ) {
					if(fh < 33 || fh > 41)
						off=true;
				}				
				
			}
		}		
		return off;
	}
	
	function parseDate(input) {
		  var parts = input.match(/(\d+)/g);
		  if(parts!=null){
		  // new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
		  return new Date(parts[0], parts[1]-1, parts[2]); // months are 0-based
		  }
		}

	
	var newEDB=null;
	var us1=false;
	var us2=false;
	
	function updateEDB() {
		$("#c_finalEDB").val(newEDB.toLocaleFormat('%Y/%m/%d'));
		if (us1 && !$("input[name='pg1_edbByT1']")){
			$("form[name='FrmForm']").append("<input type=\"checkbox\" name=\"pg1_edbByT1\" checked=\"checked\"/>");
		}
		if (us2 && !$("input[name='pg1_edbByT2']")){	
			$("form[name='FrmForm']").append("<input type=\"checkbox\" name=\"pg1_edbByT2\" checked=\"checked\"/>");
		}
		
		$("#edb-update-form").dialog('close');
		
		$("#saveBtn").click();
		
		return false;
	}
	
	function checkSOGCGuidelineForEDB() {		
		//get LMP
		var lmp = '<%=props.getProperty("pg1_menLMP")%>';
		var edb = '<%=props.getProperty("c_finalEDB")%>';
		
		var dLMP = parseDate(lmp);
		dLMP.setHours(8);
		dLMP.setMinutes(0);
		dLMP.setSeconds(0);
		
	
		//edb based on dates
		var dateEDB = new Date();
		dateEDB.setTime(dLMP.getTime()+(280*1000*60*60*24)  );
		dateEDB.setHours(8);		
		dateEDB.setMinutes(0);
		dateEDB.setSeconds(0);
		
		
		//current EDB in form
		var curEDB = parseDate(edb);
		curEDB.setHours(8);		
		curEDB.setMinutes(0);
		curEDB.setSeconds(0);
					
		var us1diff=0;
		var us2diff=0;
		var usNum=0;
		
		//is there a 1st trimester U/S
		var numUS = '<%=props.getProperty("us_num", "0")%>';
		for(var x=1;x<=numUS;x++) {			
			//get the date and the GA
			var usDateStr = $("#ar2_uDate"+x).val(); // yyyy/m//dd
			var usGA = $("#ar2_uGA"+x).val();
			
			
			if(usDateStr == undefined || usDateStr.length == 0 || usGA == undefined || usGA.length == 0)
				continue;
			
			//determine if this U/S falls into the 1st trimester U/S date range of 11w+0 to 13w+6
			var usDate = parseDate(usDateStr);
			usDate.setHours(8);
			usDate.setMinutes(0);
			usDate.setSeconds(0);
			
			//where does this U/S fall under in terms of GA given LMP
			var diff = (usDate.getTime() - dLMP.getTime())/1000/60/60/24;
			
			//first trimester?
			if(diff >= 77 && diff <= 97) {
				var week = parseInt(usGA.substring(0,usGA.indexOf('w')));
				var offset = 0;
				if(usGA.indexOf('+')!=-1) {
					offset = parseInt(usGA.substring(usGA.indexOf('+')+1));
				}				
				var day = (week*7)+offset;
				
				if(Math.abs(diff-day) > 5) {
					us1=true;
					us1diff = day-diff;
					usNum=x;					
				}
			}
			
			//2nd trimester?
			if(diff >= 98 && diff <= 195) {
				var week = parseInt(usGA.substring(0,usGA.indexOf('w')));
				var offset = 0;
				if(usGA.indexOf('+')!=-1) {
					offset = parseInt(usGA.substring(usGA.indexOf('+')+1));
				}				
				var day = (week*7)+offset;
				
				if(Math.abs(diff-day) > 10) {
					us2=true;
					us2diff = day-diff;
					usNum=x;
				}
			}
						
		}
		
		if(us2 && us1) {
			us2=false;
		}
		
		if(us1) {		
			dLMP.setTime(dLMP.getTime()+(us1diff*24*60*60*1000));
			var suggestedEDB = new Date();
			suggestedEDB.setTime(dLMP.getTime()+(280*1000*60*60*24));	
			newEDB=suggestedEDB;
			//alert((Math.abs(curEDB.getTime()-suggestedEDB.getTime())/24/60/60/1000));
			//is this diff then what's in the form now
			if((Math.abs(curEDB.getTime()-suggestedEDB.getTime())/24/60/60/1000) >= 0.9) {			
				$("#edb_warn").show();
				return usNum;
			}
		}
		if(us2) {			
			dLMP.setTime(dLMP.getTime()+(us2diff*24*60*60*1000));
			var suggestedEDB = new Date();
			suggestedEDB.setTime(dLMP.getTime()+(280*1000*60*60*24));
			newEDB=suggestedEDB;
			//is this diff then what's in the form now
			if((Math.abs(curEDB.getTime()-suggestedEDB.getTime())/24/60/60/1000) >= 0.9) {			
				$("#edb_warn").show();
				return usNum;
			}
		}
		return 0;
	}
	
	$(document).ready(function(){checkSOGCGuidelineForEDB();});
</script>
<style>
.ui-widget-overlay
        {
            background: #000;
            opacity: .7;
            -moz-opacity: 0.7;
            filter: alpha(opacity=70);
        }

#content_bar{background-color:#c4e9f6;}
.Head{width:100%;background-color:#ccc !important;}
</style>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<div id="framecontent">
<div class="innertube">
	<div style="text-align:center;font-weight:bold;">Antenatal Pathway</div>
	<br/>
	<div style="text-align:left;">Gest. Age: <span id="gest_age"></span></div>
	<br/>
	<div id="lock_notification">
		<span title="">Viewers: N/A</span>
	</div>
	<div id="lock_req">
		<input id="lock_req_btn" type="button" value="Request Lock" onclick="requestLock();"/>
		<input style="display:none" id="lock_rel_btn" type="button" value="Release Lock" onclick="releaseLock();"/>
	</div>
	
		
	<br/><br/>

	<div style="background-color:magenta;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Visit Checklist</b></td>				
			</tr>			
			<tr id="24wk_visit">
				<td>24 week Visit<span style="float:right"><img id="24wk_visit_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			<tr id="35wk_visit">
				<td>35 week Visit<span style="float:right"><img id="35wk_visit_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			<!-- 
			<tr id="dd_visit">
				<td>Due Date<span style="float:right"><img id="dd_visit_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			-->
		</table>	
	</div>
	
	<div style="background-color:yellow;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Info</b></td>
			</tr>
			<tr id="graph">
				<td>
					<span id="fundal_graph_text">Fundus Height Graph</span><span style="float:right"><img id="graph_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span>
					<div style="display:none"><a href="#" id="fundal_link">dummy link</a></div>	
				</td>			
			</tr>	
			<tr>
				<td>
					Printing Log
					<span style="float:right"><img id="print_log_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span>
				</td>
			</tr>		
		</table>	
	</div>
	
	<div style="background-color:orange;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Warnings</b></td>
			</tr>
			<tr id="edb_warn" style="display:none">
				<td>Update EDB<span style="float:right"><img id="edb_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			<tr id="rh_warn" style="display:none">
				<td>RH Negative</td>
			</tr>
			<tr id="rhogam_warn" style="display:none">
				<td title="Consider Rhogam for pt @ 28 wks. and sooner if bleeding">Consider Rhogam</td>				
			</tr>
			<tr id="rubella_warn" style="display:none">
				<td>Rubella Non-Immune</td>
			</tr>
			
			<tr id="hbsag_warn" style="display:none">
				<td>HepB Surface Antigen</td>
			</tr>
			
			<tr id="hgb_warn" style="display:none">
				<td>HGB Low</td>
			</tr>
			
			<tr id="gct_warn" style="display:none">
				<td>Perform 1hr GCT<span style="float:right"><img id="gct_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>				
			</tr>	
			
			<tr id="gct_diabetes_warn" style="display:none">
				<td>Gestational Diabetes<span style="float:right"><img id="gd_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>			

			<tr id="2hrgtt_prompt" style="display:none">
				<td>GTT Req<span style="float:right"><img id="gtt_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			
		</table>	
	</div>		
	<div style="background-color:#00FF00;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Prompts</b></td>
			</tr>
			
			<tr id="lab_prompt">
				<td>Labs<span style="float:right"><img id="lab_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>	
			
			<tr id="forms_prompt" >
				<td>Forms<span style="float:right"><img id="forms_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>						
			</tr>
			
			<tr id="strep_prompt" style="display:none">
				<td>GBS<span style="float:right"><img id="gbs_menu" src="../images/right-circle-arrow-Icon.png" border="0"></span></td>
			</tr>
			

			<tr id="fetal_pos_prompt" style="display:none">
				<td>Assess Fetal Position</td>
			</tr>	
		</table>	
	</div>	

</div>
</div>


<div id="maincontent">
<div id="content_bar" class="innertube">

<html:form action="/form/formname">

	<input type="hidden" name="commonField" value="ar2_" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "pg2")%> />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" id="episodeId" name="episodeId"
		value="<%= props.getProperty("episodeId", "") %>" />		
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" id="formId" name="formId" value="<%=formId%>" />
	<input type="hidden" name="sent_to_born" value="0" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />
	<%
	String historyet = "";
	if (request.getParameter("historyet") != null) {
		out.println("<input type=\"hidden\" name=\"historyet\" value=\"" + request.getParameter("historyet") + "\">" );
		historyet = "&historyet=" + request.getParameter("historyet");
	}
%>

	<table class="Head hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save" id="saveBtn"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save & Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint2();" />
<span style="display:none"><input id="printBtn" type="submit" value="PrintIt"/></span>
			<%
  if (!bView) {
%>
&nbsp;&nbsp;&nbsp;
			<b>View:</b> <a
				href="javascript:void(0);" onclick="popupPage(960,700,'formonarenhancedpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo+historyet%>&view=1');">
			AR1</a> &nbsp;&nbsp;&nbsp;
			<b>Edit:</b> <a
				href="javascript:void(0);" onclick="return onPageChange('formonarenhancedpg1.jsp?demographic_no=<%=demoNo%>&formId=#id&provNo=<%=provNo%>');">AR1</a>
					
			</td>
			<%
  }
%>
		</tr>
	</table>

	<table class="title" border="0" cellspacing="0" cellpadding="0"
		width="100%">
		<tr>
			<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>ANTENATAL
			RECORD 2 </th>
		</tr>
	</table>
	
	<table width="50%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="50%">Patient's Last Name<br>
			<input type="text" name="c_lastName" style="width: 100%" size="30"
				maxlength="60" value="<%= UtilMisc.htmlEscape(props.getProperty("c_lastName", "")) %>" />
			</td>
			<td valign="top">Patient's First Name<br>
			<input type="text" name="c_firstName" style="width: 100%" size="30"
				maxlength="60" value="<%= UtilMisc.htmlEscape(props.getProperty("c_firstName", "")) %>" />
			</td>
		</tr>
		
		<tr>
			<td valign="top" width="50%">Birth attendants<br>
			<input type="text" name="c_ba" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_ba", "")) %>">
			</td>
			<td>Newborn care<br>
			<input type="text" name="c_nc" size="15" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_nc", "")) %>">
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="25%" colspan="5">Family physician<br>
			<input type="text" name="c_famPhys" size="30" maxlength="80"
				style="width: 100%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_famPhys", "")) %>" /></td>
			<td valign="top" rowspan="4" width="25%"><b>Final EDB</b>
			(yyyy/mm/dd)<br>
			<input type="text" id="c_finalEDB" name="c_finalEDB" style="width: 100%" size="10"
				maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("c_finalEDB", "")) %>">
			</td>
			<td valign="top" rowspan="4" width="25%">Allergies or
			Sensitivities<br/>
			<textarea name="c_allergies" style="width: 100%" cols="30" rows="3" maxlength="150" class="limit-text" data-msg="allergies_size"><%= UtilMisc.htmlEscape(props.getProperty("c_allergies", "")) %></textarea>
			<span id="allergies_size">150 Characters left</span>
			</td>
			<td valign="top" rowspan="4">Medications / Herbals<br/>
			<textarea name="c_meds" style="width: 100%" cols="30" rows="3" maxlength="146" class="limit-text" data-msg="meds_size"><%= UtilMisc.htmlEscape(props.getProperty("c_meds", "")) %></textarea>
			<span id="meds_size">146 Characters left</span>
			</td>
		</tr>
		<tr>
			<td bgcolor="#CCCCCC" width="5%">G<br/>
			<input type="text" name="c_gravida" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_gravida", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">T<br/>
			<input type="text" name="c_term" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_term", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">P<br/>
			<input type="text" name="c_prem" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_prem", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">A<br/>
			<input type="text" name="c_abort" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_abort", "")) %>"></td>
			<td bgcolor="#CCCCCC" width="5%">L<br/>
			<input type="text" name="c_living" size="2" style="width: 100%"
				maxlength="3"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_living", "")) %>"></td>
		</tr>
	</table>

	<input type="hidden" id="rf_num" name="rf_num" value="<%= props.getProperty("rf_num", "0") %>"/>
			

	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rf_container">
		<thead>
		<tr>
			<th bgcolor="#CCCCCC" width="5%"></th>
			<th bgcolor="#CCCCCC" width="30%">Identified Risk Factors</th>
			<th bgcolor="#CCCCCC">Plan of Management</th>
		</tr>	
		</thead>
		<tbody>
		</tbody>	
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3"><input id="rf_add_btn" type="button" value="Add New" onclick="addRiskFactor();"/></td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
		
		<tr>
			<%
				if(ar1BloodWorkTestListSize == 9){ 
			%>
			<td colspan="2"
				style="background-color: green; color: #FFFFFF; font-weight: bold;">
				<%=ar1CompleteSignal%>
			<% 
 	 			} else {
 	 		%>
 	 			<td colspan="2"
				style="background-color: red; color: #FFFFFF; font-weight: bold;">
				AR1 labs Incomplete
 	 		<%
 	 			}
 	 		%>
			</td>
		</tr>

	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th bgcolor="#CCCCCC" colspan="3">Recommended Immunoprophylaxis
			</th>
		</tr>
		<tr>
			<td width="30%" nowrap="nowrap">
				<span id="rhNegSpan">
					<b>Rh neg.</b> 
					<input type="checkbox" name="ar2_rhNeg" <%= UtilMisc.htmlEscape(props.getProperty("ar2_rhNeg", "")) %> />
				</span>
				&nbsp;&nbsp;&nbsp;
			
				<span>
					<b>Rh IG Given:</b> 
					<input type="text" name="ar2_rhIG" id="ar2_rhIG" size="7" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_rhIG", "")) %>">
					<img src="../images/cal.gif" id="ar2_rhIG_cal">
				</span>
			</td>
			
			<td width="30%" nowrap>
				<span id="rubellaSpan">
					<b>Rubella booster postpartum</b> 
					<input type="checkbox" name="ar2_rubella" <%= props.getProperty("ar2_rubella", "") %> />
				</span>
			</td>
			<td>
				<span id="hepbSpan">
					<b>Newborn needs: Hep B IG</b> 
					<input type="checkbox" name="ar2_hepBIG" <%= props.getProperty("ar2_hepBIG", "") %> />
				</span>
				&nbsp;&nbsp;&nbsp;
				<span id="hepbSpan2">
					<b>Hep B vaccine</b> 
					<input type="checkbox" name="ar2_hepBVac" <%= props.getProperty("ar2_hepBVac", "") %> />
				</span>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td valign="top" bgcolor="#CCCCCC" align="center"><b>Subsequent
			Visits</b></td>
		</tr>
	</table>
	
	<input type="hidden" id="sv_num" name="sv_num" value="<%= props.getProperty("sv_num", "0") %>"/>
	
	<table width="100%" border="1" cellspacing="0" cellpadding="0" id="sv_container">
	<thead>
		<tr align="center">
			<td width="3%"></td>
			<td width="7%">Date<br>
			(yyyy/mm/dd)</td>
			<td width="7%">GA<br>
			(weeks)</td>
			<td width="7%"><!--  a href=# onclick="javascript:onWtSVG(); return false;"-->Weight<br>
			(Kg)<!--</a>--></td>
			<td width="7%">B.P.</td>
			<td width="6%" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<thead>
				<tr>
					<td colspan="2" align="center">Urine</td>
				</tr>
				<tr align="center">
					<td colspan="2">Pr</td>
					<!-- 
					<td>Gl</td>
					-->
				</tr>
				</thead>
			</table>
			</td>
			<td width="7%">SFH</td>
			<td width="7%">Pres.<br>
			Posn.</td>
			<td width="7%">FHR/<br/>
			Fm</td>
			<td nowrap>Comments</td>
			<!--  td nowrap width="4%">Cig./<br>day</td>-->
		</tr>
		</thead>
		<tbody id="sv_tbody"></tbody>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="10"><input id="sv_add_btn" type="button" value="Add New" onclick="addSubsequentVisit();"/>&nbsp;<input id="sv_badd_btn" type="button" value="Add 5" onclick="addBulkSubsequentVisit(5);"/></td>
		</tr>
	</table>
	
	<input type="hidden" id="us_num" name="us_num" value="<%= props.getProperty("us_num", "0") %>"/>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="bottomContent">
		<tr>
			<td>
			
			<br>
			<a href="javascript:void(0)" id="fundalImageLink"><img id="fundalImage" src="" width="98%" style="max-width:340px;min-width:170px"></a>
			
			</td>
			<td valign="top">
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th colspan="3" align="center" bgcolor="#CCCCCC">Ultrasound</th>
					<th colspan="2" align="center" bgcolor="#CCCCCC">Additional
					Lab Investigations</th>
				</tr>
				
				<tr>
					<td colspan="3" align="center">
					<div style="height:10em;overflow-y:auto;width:100%">
						<table width="100%" id="us_container">
						<thead>
							<tr>
								<td width="5px"></td>
								<td align="center" width="110px">Date</td>
								<td align="center" width="60px">GA</td>
								<td align="center">Result</td>
							</tr>
						</thead>
						<tbody></tbody>							
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4"><input id="us_add_btn" type="button" value="Add New" onclick="addUltraSound();"/></td>
							</tr>
						</table>
						</div>
					</td>
					<td colspan="2" align="center" valign="top" >
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td align="center" width="15%">Test</td>
								<td align="center">Result</td>
							</tr>
							<tr>
								<td>Hb</td>
								<td><input type="text" name="ar2_hb" size="10" maxlength="10"
								value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_hb", "")) %>"></td>
							</tr>
				<tr>

					<td>ABO/Rh</td>
					
					<td>
						<select name="ar2_bloodGroup">
							<option value="ND">Not Done</option>
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="AB">AB</option>
							<option value="O">O</option>
							<option value="UN">Unknown</option>
						</select>		
						/
						<select name="ar2_rh">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="WPOS">Weak Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>					
					
					</td>

				</tr>
				<tr>

					<td>Repeat ABS</td>
					<td><input type="text" name="ar2_labABS" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labABS", "")) %>"></td>
				</tr>
				<tr>

					<td>1 hr. GCT</td>
					<td><input type="text" name="ar2_lab1GCT" size="10"
						maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_lab1GCT", "")) %>"></td>
				</tr>							
						</table>
					</td>
				</tr>
			

				<tr>
					<th colspan="3">Discussion Topics</th>
					<td colspan="2" nowrap="nowrap">2 hr. GTT &nbsp;
					
						<input type="hidden" name="ar2_lab2GTT" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_lab2GTT", "")) %>"/> 
						<input style="width:50px" type="text" name="ar2_lab2GTT1" size="4" maxlength="4">/
						<input style="width:50px" type="text" name="ar2_lab2GTT2" size="4" maxlength="4">/
						<input style="width:50px" type="text" name="ar2_lab2GTT3" size="4" maxlength="4">
					
					</td>					
				</tr>
				<tr>
					<td colspan="3" rowspan="5">

					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td width="33%"><input type="checkbox" name="ar2_exercise"
								<%= props.getProperty("ar2_exercise", "") %>>Exercise<br>
							<input type="checkbox" name="ar2_workPlan"
								<%= props.getProperty("ar2_workPlan", "") %>>Work plan<br>
							<input type="checkbox" name="ar2_intercourse"
								<%= props.getProperty("ar2_intercourse", "") %>>Intercourse<br>
							<input type="checkbox" name="ar2_travel"
								<%= props.getProperty("ar2_travel", "") %>>Travel<br>							
							<input type="checkbox" name="ar2_prenatal"
								<%= props.getProperty("ar2_prenatal", "") %>>Prenatal
							classes<br>
							<input type="checkbox" name="ar2_birth"
								<%= props.getProperty("ar2_birth", "") %>>Birth plan<br>
							<input type="checkbox" name="ar2_onCall"
								<%= props.getProperty("ar2_onCall", "") %>>On call
							providers</td>
							<td width="33%"><input type="checkbox" name="ar2_preterm"
								<%= props.getProperty("ar2_preterm", "") %>>Preterm
							labour<br>
							<input type="checkbox" name="ar2_prom"
								<%= props.getProperty("ar2_prom", "") %>>PROM<br>
							<input type="checkbox" name="ar2_aph"
								<%= props.getProperty("ar2_aph", "") %>>APH<br>
							<input type="checkbox" name="ar2_fetal"
								<%= props.getProperty("ar2_fetal", "") %>>Fetal movement<br>
							<input type="checkbox" name="ar2_admission"
								<%= props.getProperty("ar2_admission", "") %>>Admission
							timing<br>
							<input type="checkbox" name="ar2_pain"
								<%= props.getProperty("ar2_pain", "") %>>Pain management<br>
							<input type="checkbox" name="ar2_labour"
								<%= props.getProperty("ar2_labour", "") %>>Labour
							support<br>
							</td>
							<td width="33%"><input type="checkbox" name="ar2_breast"
								<%= props.getProperty("ar2_breast", "") %>>Breast
							feeding<br>
							<input type="checkbox" name="ar2_circumcision"
								<%= props.getProperty("ar2_circumcision", "") %>>Circumcision<br>
							<input type="checkbox" name="ar2_dischargePlan"
								<%= props.getProperty("ar2_dischargePlan", "") %>>Discharge
							planning<br>
							<input type="checkbox" name="ar2_car"
								<%= props.getProperty("ar2_car", "") %>>Car seat safety<br>
							<input type="checkbox" name="ar2_depression"
								<%= props.getProperty("ar2_depression", "") %>>Depression<br>
							<input type="checkbox" name="ar2_contraception"
								<%= props.getProperty("ar2_contraception", "") %>>Contraception<br>
							<input type="checkbox" name="ar2_postpartumCare"
								<%= props.getProperty("ar2_postpartumCare", "") %>>Postpartum
							care</td>
						</tr>
					</table>

					</td>
					<td>GBS</td>
					<td>
						<select name="ar2_strep">
							<option value="NDONE">Not Done</option>
							<option value="POSSWAB">Positive swab result</option>
							<option value="POSURINE">Urine Positive for GBS</option>
							<option value="NEGSWAB">Negative swab result</option>
							<option value="DONEUNK">Done-result unknown</option>
							<option value="UNK">Unknown if screened</option>						
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<select name="ar2_labCustom1Label">
							<option value=""></option>
							<option value="chlamydia_toc">chlamydia TOC</option>
							<option value="fu_urine">f/u urine</option>
						</select>
					</td>
					<td><input type="text"  size="10" name="ar2_labCustom1Result" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom1Result", "")) %>"/></td>
				</tr>
				<tr>
					<td>
						<select name="ar2_labCustom2Label">
							<option value=""></option>
							<option value="chlamydia_toc">chlamydia TOC</option>
							<option value="fu_urine">f/u urine</option>
						</select>
					</td>
					<td><input type="text"  size="10" name="ar2_labCustom2Result" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom2Result", "")) %>"/></td>
				</tr>
				<tr>
					<td><input type="text" size="10" name="ar2_labCustom3Label" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom3Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="ar2_labCustom3Result" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom3Result", "")) %>"/></td>
				</tr>
				<tr>
					<td><input type="text" size="10" name="ar2_labCustom4Label" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom4Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="ar2_labCustom4Result" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_labCustom4Result", "")) %>"/></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td width="30%">Signature<br>
			<input type="text" name="pg2_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_signature", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg2_formDate" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg2_formDate", "") %>"></td>
			<td width="30%">Signature<br>
			<input type="text" name="pg2_signature2" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg2_signature2", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg2_formDate2" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg2_formDate2", "") %>"></td>
		</tr>

	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save & Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint2();" />
			<%
  if (!bView) {
%>
	&nbsp;&nbsp;&nbsp;
			<font><b>View:</b> <a
				href="javascript:void(0);" onclick="popupPage(960,700,'formonarenhancedpg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">
			AR1</a> </font>
			&nbsp;&nbsp;&nbsp;
			<b>Edit:</b> <a
				href="javascript:void(0);" onclick="return onPageChange('formonarenhancedpg1.jsp?demographic_no=<%=demoNo%>&formId=#id&provNo=<%=provNo%>');">AR1</a>
			</td>
			<%
  }
%>
		</tr>
	</table>

</html:form>
</div>
</div>


<div id="gbs-req-form" title="Create GBS Lab Requisition">
	<p class="validateTips"></p>
	<form>
	<fieldset>
		<input type="checkbox" name="penicillin" id="penicillin" class="text ui-widget-content ui-corner-all" />
		<label for="ferritin">Patient Penicillin Allergic</label>				
	</fieldset>
	</form>
</div>

<div id="gct-req-form" title="Create Lab Requisition">
	<p class="validateTips"></p>

	<form>
	<fieldset>
		<input type="checkbox" name="gct_hb" id="gct_hb" checked="checked" class="text ui-widget-content ui-corner-all" />
		<label for="gct_hb">Hb</label>		
		<br/>
		<input type="checkbox" name="gct_urine" id="gct_urine" checked="checked" value="" class="text ui-widget-content ui-corner-all" />
		<label for="gct_urine">Urine C&S</label>
		<br/>
		<input type="checkbox" name="gct_ab" id="gct_ab" checked="checked" value="" class="text ui-widget-content ui-corner-all" />
		<label for="gct_ab">Repeat antibody screen</label>
		<br/>
		<input type="checkbox" name="gct_glu" id="gct_glu" checked="checked" value="" class="text ui-widget-content ui-corner-all" />
		<label for="gct_glu">1 hour 50 gm glucose screen</label>								
	</fieldset>
	</form>
</div>

<div id="gtt-req-form" title="Create Lab Requisition">
	<p class="validateTips"></p>

	<form>
	<fieldset>
		<input type="checkbox" name="gtt_glu" id="gtt_glu" checked="checked" class="text ui-widget-content ui-corner-all" />
		<label for="gtt_glu">2 hour 75m glucose screen</label>							
	</fieldset>
	</form>
</div>


<div id="24wk-visit-form" title="24 week Visit">
	<form>
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>
							Order 1 Hour GCT
							<a href="javascript:void(0);" onclick="return false;" title="Click on 'Labs' menu item under Prompts, and choose 1 Hour GCT"><img border="0" src="../images/icon_help_sml.gif"/></a>		
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>

<div id="35wk-visit-form" title="35 week Visit">
	<form>
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>
							Order GBS Lab
							<a href="javascript:void(0);" onclick="return false;" title="Click on 'Labs' menu item under Prompts, and choose GBS"><img border="0" src="../images/icon_help_sml.gif"/></a>		
						</td>
					</tr>
					<tr>
						<td>
							Consider ultrasound for position
							<a href="javascript:void(0);" onclick="return false;" title="Click on 'Forms' menu item under Prompts, and choose Ultrasound"><img border="0" src="../images/icon_help_sml.gif"/></a>		
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>

<div id="dd-visit-form" title="Due Date Visit">
	<form>
		<fieldset>
			<table>
				<tbody>
					
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>

<div id="lab_menu_div" class="hidden">
<ul>
	<li><a href="javascript:void(0)" onclick="popPage('formlabreq<%=labReqVer %>.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq')">Routine Prenatal</a></li>
	<li><a href="javascript:void(0)" onclick="gbsReq();return false;">GBS</a></li>
	<li><a href="javascript:void(0)" onclick="gctReq();return false;">1 Hour GCT</a></li>
	<li><a href="javascript:void(0)" onclick="gttReq();return false;">2 Hour GTT</a></li>
</ul>
</div>

<div id="forms_menu_div" class="hidden">
<ul>
	<li><a href="javascript:void(0)" onclick="loadUltrasoundForms();">Ultrasound</a></li>
	<li><a href="javascript:void(0)" onclick="loadCustomForms();"><%=customEformGroup%></a></li></ul>
</div>

<div id="ips-form" title="IPS Support Tool">
	<form>
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td><button id="credit_valley_genetic_btn">Lab Requisition</button></td>
						<td>Credit Valley Hospital</td>
					</tr>
					<tr>
						<td><button id="north_york_genetic_btn">Lab Requisition</button></td>
						<td>North York Hospital</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>



<div id="cytology-eform-form" title="Cytology Forms">
	<form>
		<fieldset>
			<table>
				<tbody>
				<%
					if(cytologyForms != null) {
						for(LabelValueBean bean:cytologyForms) {
							%>
							<tr>
								<td><button onClick="popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=bean.getValue()%>&demographic_no=<%=demoNo%>&appointment=0','cytology');return false;">Open</button></td>
								<td><%=bean.getLabel() %></td>
							</tr>
							<%
						}
					}
				%>										
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>



<div id="ultrasound-eform-form" title="Ultrasound Forms">
	<form>
		<fieldset>
			<table>
				<tbody>
				<%
					if(ultrasoundForms != null) {
						for(LabelValueBean bean:ultrasoundForms) {
							%>
							<tr>
								<td><button onClick="popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=bean.getValue()%>&demographic_no=<%=demoNo%>&appointment=0','ultrasound');return false;">Open</button></td>
								<td><%=bean.getLabel() %></td>
							</tr>
							<%
						}
					}
				%>										
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>


<div id="custom-eform-form" title="<%=customEformGroup%> Forms">
	<form>
		<fieldset>
			<table>
				<tbody>
				<%
					if(customForms != null) {
						for(LabelValueBean bean:customForms) {
							%>
							<tr>
								<td><button onClick="popPage('<%=ctx%>/eform/efmformadd_data.jsp?fid=<%=bean.getValue()%>&demographic_no=<%=demoNo%>&appointment=0','<%=customEformGroup%>form');return false;">Open</button></td>
								<td><%=bean.getLabel() %></td>
							</tr>
							<%
						}
					}
				%>										
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>

<div id="edb-update-form" title="EDB Update">
	<p>The EDB should be updated according to SOGC guideline (<a target="_sogc" href="http://sogc.org/guidelines/documents/gui214CPG0809.pdf">link</a>)</p>
	<form>
		<fieldset>
			<table id="edb_update_table">
				<tbody>
				
				</tbody>
			</table>
		</fieldset>
	</form>	
</div>


<div id="print-dialog" title="Print Antenatal Record">
	<p class="validateTips"></p>
	<form>
	<fieldset>
		<input type="checkbox" name="print_ar1" id="print_ar1" checked="checked" class="text ui-widget-content ui-corner-all" />
		<label for="print_ar1">AR1</label>
		<br/>
		<input type="checkbox" name="print_ar2" id="print_ar2" checked="checked" class="text ui-widget-content ui-corner-all" />
		<label for="print_ar2">AR2</label>
		<br/>
		<table>
			<tr>
				<td>External Location:</td>
				<td>
					<select name="print_location" id="print_location" class="text ui-widget-content ui-corner-all">
						<option value="none">None</option>
						<option value="hospital">Hospital</option>
						<option value="patient">Patient</option>
						<option value="other">Other</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Method of Transfer:</td>
				<td>
					<select name="print_method" id="print_method" class="text ui-widget-content ui-corner-all">
						<option value="none">None</option>
						<option value="fax">Fax</option>
						<option value="mail">Mail</option>
						<option value="email">Email</option>
					</select>
				</td>
			</tr>
		</table>
							
	</fieldset>
	</form>
</div>

<div id="print-log-dialog" title="Print Log" style="background-color:white">	
	<table id="print_log_table" style="width:100%">
		<thead style="text-align:left">
			<tr>
				<th>Date</th>
				<th>Provider</th>
				<th>External Location</th>
				<th>Method of Transfer</th>
			</tr>
		</thead>
		<tbody>		
		</tbody>
	</table>
</div>

<script type="text/javascript">
Calendar.setup({ inputField : "ar2_rhIG", ifFormat : "%Y/%m/%d", showsTime :false, button : "ar2_rhIG_cal", singleClick : true, step : 1 });
</script>
</body>
</html:html>
