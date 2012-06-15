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

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<%
    String formClass = "ONAREnhanced";
    String formLink = "formonarenhancedpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "../ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg1");

    //get project_home
    String project_home = request.getContextPath().substring(1);    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Antenatal Record 1</title>
<link rel="stylesheet" type="text/css" href="<%=bView?"arStyleView.css" : "arStyle.css"%>">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
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
width: 200px; /*Width of frame div*/
height: 100%;
overflow: hidden; /*Disable scrollbars. Set to "scroll" to enable*/
background: navy;
color: white;
}

#maincontent{
position: fixed;
top: 0; 
left: 200px; /*Set left value to WidthOfFrameDiv*/
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

</style>

<script>
	$(document).ready(function() {
		$("select[name='c_province']").val('<%= UtilMisc.htmlEscape(props.getProperty("c_province", "")) %>');
		$("select[name='pg1_language']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_language", "")) %>');		
		$("select[name='pg1_partnerEduLevel']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerEduLevel", "")) %>');
		$("select[name='pg1_partnerOccupation']").bind('change',function(){
			if($("select[name='pg1_partnerOccupation']").val() == 'OTHER') {
				$("input[name='c_partnerOccupationOther']").removeAttr('readonly');				
			} else {
				$("input[name='c_partnerOccupationOther']").attr('readonly','readonly');
				$("input[name='c_partnerOccupationOther']").val("");
			}
		});
		$("select[name='pg1_partnerOccupation']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerOccupation", "")) %>');
		if($("select[name='pg1_partnerOccupation']").val() == 'OTHER') {
			$("input[name='c_partnerOccupationOther']").removeAttr('readonly');				
		} else {
			$("input[name='c_partnerOccupationOther']").attr('readonly','readonly');
			$("input[name='c_partnerOccupationOther']").val("");
		}
		
		$("select[name='pg1_eduLevel']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_eduLevel", "")) %>');
		$("select[name='pg1_occupation']").bind('change',function(){
			if($("select[name='pg1_occupation']").val() == 'OTHER') {
				$("input[name='pg1_occupationOther']").removeAttr('readonly');				
			} else {
				$("input[name='pg1_occupationOther']").attr('readonly','readonly');
				$("input[name='pg1_occupationOther']").val("");
			}
		});
		$("select[name='pg1_occupation']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_occupation", "")) %>');
		if($("select[name='pg1_occupation']").val() == 'OTHER') {
			$("input[name='pg1_occupationOther']").removeAttr('readonly');				
		} else {
			$("input[name='pg1_occupationOther']").attr('readonly','readonly');
			$("input[name='pg1_occupationOther']").val("");
		}
				
		$("select[name='pg1_ethnicBgMother']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_ethnicBgMother", "")) %>');
		$("select[name='pg1_ethnicBgFather']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_ethnicBgFather", "")) %>');
		$("select[name='c_hinType']").val('<%= UtilMisc.htmlEscape(props.getProperty("c_hinType", "")) %>');
		
		$("select[name='pg1_box3']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_box3", "")) %>');		
		$("select[name='pg1_labHIV']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHIV", "")) %>');
		$("select[name='pg1_labABO']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labABO", "")) %>');
		$("select[name='pg1_labRh']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRh", "")) %>');
		$("select[name='pg1_labGC']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labGC", "")) %>');
		$("select[name='pg1_labChlamydia']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labChlamydia", "")) %>');
		$("select[name='pg1_labHBsAg']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHBsAg", "")) %>');
		$("select[name='pg1_labVDRL']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labVDRL", "")) %>');
		$("select[name='pg1_labSickle']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labSickle", "")) %>');
		$("select[name='pg1_labRubella']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRubella", "")) %>');
	
		if($("select[name='pg1_labRh']").val() == 'NEG'/* && getGAWeek() >= 9*/) {			
			$("#rh_warn").show();
		}		
		
		if($("select[name='pg1_labRubella']").val() == 'Non-Immune' ) {			
			$("#rubella_warn").show();
		}
		
		if($("select[name='pg1_labHBsAg']").val() == 'POS' ) {			
			$("#hbsag_warn").show();
		}
		
		if($("input[name='pg1_geneticA']").val().length>0 || $("input[name='pg1_geneticB']").val().length>0
				|| $("input[name='pg1_geneticC']").val().length>0) {
			$("#genetic_prompt").show();
		}
		
		if($("input[name='c_bmi']").val().length > 0) {
			var bmi = parseFloat($("input[name='c_bmi']").val());			
			if(bmi > 30) {
				$("#bmi30_warn").show();
			}
			if(bmi > 40) {
				$("#bmi40_warn").show();
			}
			if(bmi <= 18.5) {
				$("#bmi_low_warn").show();
			}
		}
		
		if($("select[name='pg1_ethnicBgMother']").val() == 'ANC005' || $("select[name='pg1_ethnicBgFather']").val() == 'ANC005') {
			$("#sickle_cell_warn").show();
		} 
		
		if($("select[name='pg1_ethnicBgMother']").val() == 'ANC005' || $("select[name='pg1_ethnicBgFather']").val() == 'ANC005') {
			$("#thalassemia_warn").show();
		}
		if($("select[name='pg1_ethnicBgMother']").val() == 'ANC002' || $("select[name='pg1_ethnicBgFather']").val() == 'ANC002') {
			$("#thalassemia_warn").show();
		}
		
		if($("input[name='pg1_wt']").val().length == 0) {
			$("#weight_warn").show();
		}
		
		if($("input[name='pg1_ht']").val().length == 0) {
			$("#height_warn").show();
		}
		
		if($("input[name='c_bmi']").val().length == 0) {
			$("#bmi_warn").show();
		}
		
		
		if($("input[name='pg1_labHb']").val().length > 0) {
			var hgb_result = parseFloat($("input[name='pg1_labHb']").val());
			$("#bmi_warn").show();
		}
	});
</script>

<script>
	function validate() {		
		var patt1=new RegExp("([a-zA-Z][0-9][a-zA-Z][0-9][a-zA-Z][0-9])?");
		if(!patt1.test($("input[name='c_postal']").val())) {
			alert('Postal Code must be in the following format A#A#A#');
			return false;
		}		
		patt1=new RegExp("^([0-9]{3}\\-[0-9]{3}\\-[0-9]{4})?$");	
		if(!patt1.test( $("input[name='pg1_homePhone']").val() ) ) {
			alert('Home phone must be in the following format 555-555-5555');
			return false;
		}
			
		patt1=new RegExp("^([0-9]{3}\\-[0-9]{3}\\-[0-9]{4})?$");
		if(!patt1.test($("input[name='pg1_workPhone']").val())) {
			alert('Work phone must be in the following format 555-555-5555');
			return false;
		}
		/*
		var finalEDB = $("input[name='c_finalEDB']").val();
		if(finalEDB.trim().length==0) {
			alert('Please set a final EDB');
			return false;
		}
		*/
		
		patt1=new RegExp("^\\d{1,2}$");
		if(!patt1.test($("input[name='pg1_age']").val())) {
			alert('Age must be a number');
			return false;
		}
		
		patt1=new RegExp("(^\\d{1,2}$)?");
		if($("input[name='pg1_partnerAge']").val().length > 0 && !patt1.test($("input[name='pg1_partnerAge']").val())) {
			alert("Partner's age must be a number");
			return false;
		}
	
		for(var x=1;x<=10;x++) {
			if($('#obxhx_'+ x).length>0) {
				//found a row
				if($("input[name='pg1_year"+x+"']").val().length > 0) {
					patt1=new RegExp("^\\d{4}$");
					if(!patt1.test($("input[name='pg1_year"+x+"']").val())) {
						alert("Obstetrical Hx year must be in 4 digit format (ex. 1999)");
						return false;
					}
				}
				if($("input[name='pg1_sex"+x+"']").val().length > 0) {
					patt1=new RegExp("^[mMfF]$");
					if(!patt1.test($("input[name='pg1_sex"+x+"']").val())) {
						alert("Obstetrical Hx sex must be M or F");
						return false;
					}
				}
				patt1=new RegExp("^(\\d*)?$");
				if(!patt1.test($("input[name='pg1_oh_gest"+x+"']").val())) {
					alert("Obstetrical Gestation Age must be a number");
					return false;
				}
				
				patt1=new RegExp("^(\\d*(\\.\\d*)?)?$");
				if(!patt1.test($("input[name='pg1_length"+x+"']").val())) {
					alert("Obstetrical Length of Labour must be a number");
					return false;
				}
								
			}
		}
				
		patt1=new RegExp("^\\d*$");
		if(!patt1.test($("input[name='c_gravida']").val())) {
			alert("Gravida must be a number");
			return false;
		}
		
		patt1=new RegExp("^\\d*$");
		if(!patt1.test($("input[name='c_term']").val())) {
			alert("Term must be a number");
			return false;
		}
		
		patt1=new RegExp("^\\d*$");
		if(!patt1.test($("input[name='c_prem']").val())) {
			alert("Premature must be a number");
			return false;
		}
		
		patt1=new RegExp("^\\d*$");
		if(!patt1.test($("input[name='c_abort']").val())) {
			alert("Abortuses must be a number");
			return false;
		}
		
		patt1=new RegExp("^\\d*$");
		if(!patt1.test($("input[name='c_living']").val())) {
			alert("Living must be a number");
			return false;
		}
		
		if(!validateYesNo('pg1_cp1')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_cp2')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_cp3')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_cp4')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_cp8')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_naDiet')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_naMilk')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo('pg1_naFolic')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
				
		if(!validateYesNo2('9')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('10')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('12')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('13')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('14')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('17')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		
		if(!validateYesNo2('22')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('20')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('21')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('24')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('15')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('25')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('27')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('31')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('32')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo2('34')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo3('35')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo3('40')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo3('38')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		
		if(!validateYesNo3('42')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('43')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('44')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('45')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('46')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('47')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo4('48')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		
		if(!validateYesNo5('pg1_bloodTran')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo5('pg1_infectDisOther')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo5('pg1_reliCult')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
		if(!validateYesNo5('pg1_fhRisk')) {alert("Cannot choose yes and no together for an item in Medical History");return false;}
				
		if(!validateYesNo5('pg1_thyroid')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_chest')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_breasts')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_cardio')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_abdomen')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_vari')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_extGen')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_cervix')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_uterus')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_adnexa')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		if(!validateYesNo5('pg1_pExOther')) {alert("Cannot choose normal and abnormal together for an item in Physical Exam");return false;}
		
		patt1=new RegExp("^(\\d{2,3}(\\.\\d)?)?$");
		if(!patt1.test($("input[name='pg1_ht']").val())) {
			alert("Please choose a valid height");
			return false;
		}
		
		patt1=new RegExp("^(\\d{1,3}((\\.)\\d{1,2})?)?$");
		if(!patt1.test($("input[name='pg1_wt']").val())) {
			alert("Please choose a valid weight");
			return false;
		}
		
		patt1=new RegExp("^(\\d{1,2}((\\.)\\d{1,2})?)?$");
		if(!patt1.test($("input[name='c_bmi']").val())) {
			alert("Please choose a valid BMI");
			return false;
		}
		
		patt1=new RegExp("^(\\d{1,3}/\\d{1,3})?$");
		if(!patt1.test($("input[name='pg1_BP']").val())) {
			alert("Please choose a valid BP");
			return false;
		}
		
		patt1=new RegExp("^(\\d{1,3}(\\.\\d)?)?$");
		if(!patt1.test($("input[name='pg1_labMCV']").val())) {
			alert("Please choose a valid MCV result");
			return false;
		}	
		
		return true;
	}
	
	function validateNormalAbnormal(name) {
		var yesC = $("input[name='"+name+"']").attr('checked');
		var noC = $("input[name='"+name+"A']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}
	
	function validateYesNo5(name) {
		var yesC = $("input[name='"+name+"Y']").attr('checked');
		var noC = $("input[name='"+name+"N']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}

	function validateYesNo(name) {
		var yesC = $("input[name='"+name+"']").attr('checked');
		var noC = $("input[name='"+name+"N']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}
	
	function validateYesNo2(number) {
		var yesC = $("input[name='pg1_yes"+number+"']").attr('checked');
		var noC = $("input[name='pg1_no"+number+"']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}

	function validateYesNo3(number) {
		var yesC = $("input[name='pg1_idt"+number+"']").attr('checked');
		var noC = $("input[name='pg1_idt"+number+"N']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}
	
	function validateYesNo4(number) {
		var yesC = $("input[name='pg1_pdt"+number+"']").attr('checked');
		var noC = $("input[name='pg1_pdt"+number+"N']").attr('checked');
		if(yesC == 'checked' && noC == 'checked') {
			return false;
		}
		return true;
	}
	
	
	function adjustDynamicListTotals() {
		$('#obxhx_num').val(adjustDynamicListTotalsOH('obxhx_',10,true));
	}
	
	function adjustDynamicListTotalsOH(name,max,adjust) {		
		var total = 0;
		for(var x=1;x<=max;x++) {
			if($('#'+ name +x).length>0) {
				total++;
				if((x != total) && adjust) {
					$("#obxhx_"+x).attr('id','obxhx_'+total);
					
					$("input[name='pg1_year"+x+"']").attr('name','pg1_year'+total);
					$("input[name='pg1_sex"+x+"']").attr('name','pg1_sex'+total);
					$("input[name='pg1_oh_gest"+x+"']").attr('name','pg1_oh_gest'+total);
					$("input[name='pg1_weight"+x+"']").attr('name','pg1_weight'+total);
					$("input[name='pg1_length"+x+"']").attr('name','pg1_length'+total);
					$("input[name='pg1_place"+x+"']").attr('name','pg1_place'+total);
					$("input[name='pg1_svb"+x+"']").attr('name','pg1_svb'+total);
					$("input[name='pg1_cs"+x+"']").attr('name','pg1_cs'+total);
					$("input[name='pg1_ass"+x+"']").attr('name','pg1_ass'+total);
					$("input[name='pg1_oh_comments"+x+"']").attr('name','pg1_oh_comments'+total);
				}
			}			
		}	
		return total;
	}
	
</script>

<script>
function addObxHx() {
	if(adjustDynamicListTotalsOH("obxhx_",10,false) >= 10) {
		alert('Maximum number of rows is 10');
		return;
	}
	
	var total = jQuery("#obxhx_num").val();
	total++;
	
	jQuery("#obxhx_num").val(total);
	
	jQuery.ajax({url:'onarenhanced_obxhx.jsp?n='+total,async:false, success:function(data) {
		  jQuery("#obxhx_container tbody").append(data);		  
	}});	
}

function deleteObxHx(id) {
	var followUpId = jQuery("input[name='obxhx_"+id+".id']").val();
	jQuery("form[name='FrmForm']").append("<input type=\"hidden\" name=\"obxhx.delete\" value=\""+followUpId+"\"/>");
	jQuery("#obxhx_"+id).remove();	
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

jQuery(document).ready(function() {
	<%
		String obxHx = props.getProperty("obxhx_num", "0");
		if(obxHx.length() == 0)
			obxHx = "0";		
		int obxHxNum = Integer.parseInt(obxHx);
		for(int x=1;x<obxHxNum+1;x++) {
			
		%>
			jQuery.ajax({url:'onarenhanced_obxhx.jsp?n='+<%=x%>,async:false, success:function(data) {
			  jQuery("#obxhx_container tbody").append(data);
			  setInput(<%=x%>,"pg1_year",'<%= props.getProperty("pg1_year"+x, "") %>');
			  setInput(<%=x%>,"pg1_sex",'<%= props.getProperty("pg1_sex"+x, "") %>');
			  setInput(<%=x%>,"pg1_oh_gest",'<%= props.getProperty("pg1_oh_gest"+x, "") %>');
			  setInput(<%=x%>,"pg1_weight",'<%= props.getProperty("pg1_weight"+x, "") %>');
			  setInput(<%=x%>,"pg1_length",'<%= props.getProperty("pg1_length"+x, "") %>');
			  setInput(<%=x%>,"pg1_place",'<%= props.getProperty("pg1_place"+x, "") %>');
			  setCheckbox(<%=x%>,"pg1_svb",'<%= (props.getProperty("pg1_svb"+x, "").length()>0)?"true":"false" %>');
			  setCheckbox(<%=x%>,"pg1_cs",'<%= (props.getProperty("pg1_cs"+x, "").length()>0)?"true":"false" %>');
			  setCheckbox(<%=x%>,"pg1_ass",'<%= (props.getProperty("pg1_ass"+x, "").length()>0)?"true":"false" %>');
			  setInput(<%=x%>,"pg1_oh_comments",'<%= props.getProperty("pg1_oh_comments"+x, "") %>');
		}});
		<%
		}
	%>
});

$(document).ready(function(){
	updatePageLock(false);
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
		   url: "<%=request.getContextPath()%>/PageMonitoringService.do",
		   data: { method: "update", page: "formonarenhanced<%=demoNo%>", lock: lock },
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

</script>
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
    }
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=Antenatal+Record+Part+1&__cfgfile=onar1PrintCfgPg1&__template=onar1";
            document.forms[0].target="_blank";            
        }
        return ret;
    }
    
    function refreshOpener() {
		if (window.opener && window.opener.name=="inboxDocDetails") {
			window.opener.location.reload(true);
		}	
    }
    window.onunload=refreshOpener;
    function onSave() {
        document.forms[0].submit.value="save";
        var ret1 = validate();
        var ret = checkAllDates();
        if(ret==true && ret1==true)
        {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        if (ret && ret1)
            window.onunload=null;
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
	            	url:'<%=request.getContextPath()%>/Pregnancy.do?method=saveFormAjax',
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
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
        	refreshOpener();
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret1 = validate();        
        var ret = checkAllDates();
        if(ret == true && ret1==true)
        {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        if (ret&&ret1)
        	refreshOpener();
        adjustDynamicListTotals();
        return ret && ret1;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popupPageFull(varpage) {
        windowprops = "location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
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
    function popupFixedPage(vheight,vwidth,varpage) { 
       var page = "" + varpage;
       windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
       var popup=window.open(page, "planner", windowprop);
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
function calcBMIMetric(obj) {
	if(isNumber(document.forms[0].pg1_wt) && isNumber(document.forms[0].pg1_ht)) {
		weight = document.forms[0].pg1_wt.value;
		height = document.forms[0].pg1_ht.value / 100;
		if(weight!="" && weight!="0" && height!="" && height!="0") {
			obj.value = Math.round(weight * 10 / height / height) / 10;
		}
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
    //            alert('dateString'+dateString);
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
        if(valDate(document.forms[0].pg1_formDate)==false){
            b = false;
        }

        return b;
    }
function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}
function calByLMP(obj) {
	if (document.forms[0].pg1_menLMP.value!="" && valDate(document.forms[0].pg1_menLMP)==true) {
		var str_date = document.forms[0].pg1_menLMP.value;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
		var calDate=new Date();
		calDate.setFullYear(yyyy);
		calDate.setMonth(mm);
		calDate.setDate(dd);
		calDate.setHours("8");
		var odate = new Date(calDate.getTime() + (280 * 86400000));
		varMonth1 = odate.getMonth()+1;
		varMonth1 = varMonth1>9? varMonth1 : ("0"+varMonth1);
		varDate1 = odate.getDate()>9? odate.getDate(): ("0"+odate.getDate());
		obj.value = odate.getFullYear() + '/' + varMonth1 + '/' + varDate1;
	}
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
	 
	 var edbDate=new Date(year,parseInt(month)-1,day);
	 edbDate.setHours(0);
	 edbDate.setMinutes(0);
	 edbDate.setSeconds(0);
	 edbDate.setMilliseconds(0);
	 
	 var startDate = new Date();
	 startDate.setTime(edbDate.getTime()-(280*1000*60*60*24)  );
	 startDate.setHours(0);		
	 
	 var today = new Date();
	 today.setHours(0);
	 today.setMinutes(0);
	 today.setSeconds(0);
	 today.setMilliseconds(0);
	 
	 if(today > startDate) {		
		 var  days = daydiff(startDate,today);
		 return days;
	 }
	 
	 return -1;
}

function daydiff(first, second) {
    return (second-first)/(1000*60*60*24)
}

function getGAWeek() {
	var day = getGADay();
	if(day > 0) {
		week = parseInt(day / 7);	
	}
	return parseInt(week);
}

function getGA() {
	var day = getGADay();
	if(day > 0) {
		week = parseInt(day / 7);
		offset = day % 7;		
	}
	return parseInt(week) + "w+" + offset;
}

function updateAllergies() {
	jQuery.ajax({url:'<%=request.getContextPath()%>/Pregnancy.do?method=getAllergies&demographicNo=<%=demoNo%>',async:true, dataType:'json', success:function(data) {
		$("textarea[name='c_allergies']").val($("textarea[name='c_allergies']").val() + "\n" + data.value);
	}});
}

function updateMeds() {
	jQuery.ajax({url:'<%=request.getContextPath()%>/Pregnancy.do?method=getMeds&demographicNo=<%=demoNo%>',async:true, dataType:'json', success:function(data) {
		$("textarea[name='c_meds']").val($("textarea[name='c_meds']").val() + "\n" + data.value);
	}});
}
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<div id="framecontent">
<div class="innertube">
	Reminders
	<br/>
	<br/>
	<div id="lock_notification">
		<span title="">Viewers:</span>
	</div>
	<div id="lock_req">
		<input id="lock_req_btn" type="button" value="Request Lock" onclick="requestLock();"/>
		<input style="display:none" id="lock_rel_btn" type="button" value="Release Lock" onclick="releaseLock();"/>
	</div>
	<br/><br/>
	<div style="background-color:yellow;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Warnings</b></td>
			</tr>
			<tr id="rh_warn" style="display:none">
				<td>RH Negative</td>
			</tr>
			<tr id="rubella_warn" style="display:none">
				<td>Rubella Non-Immune</td>
			</tr>
			
			<tr id="hbsag_warn" style="display:none">
				<td>HepB Surface Antigen</td>
			</tr>
			
			<tr id="weight_warn" style="display:none">
				<td>No Weight Entered</td>
			</tr>
			
			<tr id="height_warn" style="display:none">
				<td>No Height Entered</td>
			</tr>
			
			<tr id="bmi_warn" style="display:none">
				<td>No BMI Entered</td>
			</tr>
			
			<tr id="bmi30_warn" style="display:none">
				<td>BMI is High</td>
			</tr>
			<tr id="bmi40_warn" style="display:none">
				<td>BMI is High</td>
			</tr>
			<tr id="bmi_low_warn" style="display:none">
				<td>BMI is Low</td>
			</tr>
			<tr id="sickle_cell_warn" style="display:none">
				<td>Risk: Sicle Cell</td>
			</tr>
			<tr id="thalassemia_warn" style="display:none">
				<td>Risk: Thalassemia</td>
			</tr>
			
		</table>	
	</div>
	
		<br/><br/>
	<div style="background-color:green;border:2px solid black;width:100%;color:black">
		<table style="width:100%" border="0">
			<tr>
				<td><b>Prompts</b></td>
			</tr>
			
			<tr id="strep_prompt" style="display:none">
				<td><a href="javascript:void(0)" onClick="gbsReq();return false;">Print Req. for GBS </a></td>
			</tr>
			
			<tr id="genetic_prompt" style="display:none">
				<td><a href="javascript:void(0)" onClick="geneticReferral();return false;">Genetics Referral</a></td>
			</tr>		
					
		</table>	
	</div>
</div>
</div>


<div id="maincontent">
<div id="content_bar" class="innertube" style="background-color: #c4e9f6">
<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "pg1")%> />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>" /-->
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" />
            
</td>
			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq');">LAB</a>
			</td>

			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formonarenhancedpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			</a>&nbsp;</td>
			<td align="right"><b>Edit:</b> <a
				href="javascript:void(0)" onclick="onPageChange('formonarenhancedpg2.jsp?demographic_no=<%=demoNo%>&formId=#id&provNo=<%=provNo%>');">AR2</a>
			
			<%if(((FrmONAREnhancedRecord)rec).isSendToPing(""+demoNo)) {	%> <a
				href="study/ar2ping.jsp?demographic_no=<%=demoNo%>">Send to PING</a>
			<% }	%>
			</td>
			
			<td align="right"><a
				href="formonarenhancedxml.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">XML</a>			
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
			RECORD 1</th>
			<%
	if (request.getParameter("historyet") != null) {
		out.println("<input type=\"hidden\" name=\"historyet\" value=\"" + request.getParameter("historyet") + "\">" );
	}
%>
		</tr>
	</table>
	<table width="50%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="50%">Patient's Last Name<br>
			<input type="text" name="c_lastName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_lastName", "") %>" />
			</td>
			<td valign="top" colspan='2'>Patient's First Name<br>
			<input type="text" name="c_firstName" style="width: 100%" size="30"
				maxlength="60" value="<%= props.getProperty("c_firstName", "") %>" />
			</td>
		</tr>
		<tr>
			<td colspan='2'>Address - number, street name<br>
			<input type="text" name="c_address" style="width: 100%" size="60"
				maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_address", "")) %>" />
			</td>
			<td width="25%">Apt/Suite/Unit<br>
			<input type="text" name="c_apt" style="width: 100%" size="20"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_apt", "")) %>" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" colspan='2'>City/Town<br>
			<input type="text" name="c_city" style="width: 100%" size="60"
				maxlength="80"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_city", "")) %>" />
			</td>
			<td valign="top" width="8%">Province<br>

				<select name="c_province" style="width: 100%">
					<option value="AB" >AB-Alberta</option>
					<option value="BC" >BC-British Columbia</option>
					<option value="MB" >MB-Manitoba</option>
					<option value="NB" >NB-New Brunswick</option>
					<option value="NL" >NL-Newfoundland Labrador</option>
					<option value="NT" >NT-Northwest Territory</option>
					<option value="NS" >NS-Nova Scotia</option>
					<option value="NU" >NU-Nunavut</option>
					<option value="ON" >ON-Ontario</option>
					<option value="PE" >PE-Prince Edward Island</option>
					<option value="QC" >QC-Quebec</option>
					<option value="SK" >SK-Saskatchewan</option>
					<option value="YT" >YT-Yukon</option>
					<option value="US" >US resident</option>
					<option value="US-AK" >US-AK-Alaska</option>
					<option value="US-AL" >US-AL-Alabama</option>
					<option value="US-AR" >US-AR-Arkansas</option>
					<option value="US-AZ" >US-AZ-Arizona</option>
					<option value="US-CA" >US-CA-California</option>
					<option value="US-CO" >US-CO-Colorado</option>
					<option value="US-CT" >US-CT-Connecticut</option>
					<option value="US-CZ" >US-CZ-Canal Zone</option>
					<option value="US-DC" >US-DC-District Of Columbia</option>
					<option value="US-DE" >US-DE-Delaware</option>
					<option value="US-FL" >US-FL-Florida</option>
					<option value="US-GA" >US-GA-Georgia</option>
					<option value="US-GU" >US-GU-Guam</option>
					<option value="US-HI" >US-HI-Hawaii</option>
					<option value="US-IA" >US-IA-Iowa</option>
					<option value="US-ID" >US-ID-Idaho</option>
					<option value="US-IL" >US-IL-Illinois</option>
					<option value="US-IN" >US-IN-Indiana</option>
					<option value="US-KS" >US-KS-Kansas</option>
					<option value="US-KY" >US-KY-Kentucky</option>
					<option value="US-LA" >US-LA-Louisiana</option>
					<option value="US-MA" >US-MA-Massachusetts</option>
					<option value="US-MD" >US-MD-Maryland</option>
					<option value="US-ME" >US-ME-Maine</option>
					<option value="US-MI" >US-MI-Michigan</option>
					<option value="US-MN" >US-MN-Minnesota</option>
					<option value="US-MO" >US-MO-Missouri</option>
					<option value="US-MS" >US-MS-Mississippi</option>
					<option value="US-MT" >US-MT-Montana</option>
					<option value="US-NC" >US-NC-North Carolina</option>
					<option value="US-ND" >US-ND-North Dakota</option>
					<option value="US-NE" >US-NE-Nebraska</option>
					<option value="US-NH" >US-NH-New Hampshire</option>
					<option value="US-NJ" >US-NJ-New Jersey</option>
					<option value="US-NM" >US-NM-New Mexico</option>
					<option value="US-NU" >US-NU-Nunavut</option>
					<option value="US-NV" >US-NV-Nevada</option>
					<option value="US-NY" >US-NY-New York</option>
					<option value="US-OH" >US-OH-Ohio</option>
					<option value="US-OK" >US-OK-Oklahoma</option>
					<option value="US-OR" >US-OR-Oregon</option>
					<option value="US-PA" >US-PA-Pennsylvania</option>
					<option value="US-PR" >US-PR-Puerto Rico</option>
					<option value="US-RI" >US-RI-Rhode Island</option>
					<option value="US-SC" >US-SC-South Carolina</option>
					<option value="US-SD" >US-SD-South Dakota</option>
					<option value="US-TN" >US-TN-Tennessee</option>
					<option value="US-TX" >US-TX-Texas</option>
					<option value="US-UT" >US-UT-Utah</option>
					<option value="US-VA" >US-VA-Virginia</option>
					<option value="US-VI" >US-VI-Virgin Islands</option>
					<option value="US-VT" >US-VT-Vermont</option>
					<option value="US-WA" >US-WA-Washington</option>
					<option value="US-WI" >US-WI-Wisconsin</option>
					<option value="US-WV" >US-WV-West Virginia</option>
					<option value="US-WY" >US-WY-Wyoming</option>
				</select>				
			</td>
			<td width="12%">Postal Code<br>
			<input type="text" name="c_postal" style="width: 100%" size="7"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_postal", "")) %>" />
			</td>
			<td colspan="2" width="25%">Partner's Last Name<br>
			<input type="text" name="c_partnerLastName" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("c_partnerLastName", "") %>" /></td>
			<td colspan="2" width="25%">Partner's First Name<br>
			<input type="text" name="c_partnerFirstName" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("c_partnerFirstName", "") %>" /></td>
		</tr>
		<tr>
			<td width="15%" valign="top">Telephone - Home<br>
			<input type="text" name="pg1_homePhone" size="15" style="width: 100%"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_homePhone", "")) %>" />
			</td>
			<td width="15%" valign="top">Telephone - Work<br>
			<input type="text" name="pg1_workPhone" size="15" style="width: 100%"
				maxlength="20" value="<%= props.getProperty("pg1_workPhone", "") %>" />
			</td>
			<td colspan='2' valign="top">Language<br>
					<select name="pg1_language"  style="width: 100%">
						<option value="ENG">English</option>
						<option value="FRA">French</option>
						<option value="AAR">Afar</option>
						<option value="AFR">Afrikaans</option>
						<option value="AKA">Akan</option>
						<option value="SQI">Albanian</option>
						<option value="ASE">American Sign Language (ASL)</option>
						<option value="AMH">Amharic</option>
						<option value="ARA">Arabic</option>
						<option value="ARG">Aragonese</option>
						<option value="HYE">Armenian</option>
						<option value="ASM">Assamese</option>
						<option value="AVA">Avaric</option>
						<option value="AYM">Aymara</option>
						<option value="AZE">Azerbaijani</option>
						<option value="BAM">Bambara</option>
						<option value="BAK">Bashkir</option>
						<option value="EUS">Basque</option>
						<option value="BEL">Belarusian</option>
						<option value="BEN">Bengali</option>
						<option value="BIS">Bislama</option>
						<option value="BOS">Bosnian</option>
						<option value="BRE">Breton</option>
						<option value="BUL">Bulgarian</option>
						<option value="MYA">Burmese</option>
						<option value="CAT">Catalan</option>
						<option value="KHM">Central Khmer</option>
						<option value="CHA">Chamorro</option>
						<option value="CHE">Chechen</option>
						<option value="YUE">Chinese Cantonese</option>
						<option value="CMN">Chinese Mandarin</option>
						<option value="CHV">Chuvash</option>
						<option value="COR">Cornish</option>
						<option value="COS">Corsican</option>
						<option value="CRE">Cree</option>
						<option value="HRV">Croatian</option>
						<option value="CES">Czech</option>
						<option value="DAN">Danish</option>
						<option value="DIV">Dhivehi</option>
						<option value="NLD">Dutch</option>
						<option value="DZO">Dzongkha</option>						
						<option value="EST">Estonian</option>
						<option value="EWE">Ewe</option>
						<option value="FAO">Faroese</option>
						<option value="FIJ">Fijian</option>
						<option value="FIL">Filipino</option>
						<option value="FIN">Finnish</option>						
						<option value="FUL">Fulah</option>
						<option value="GLG">Galician</option>
						<option value="LUG">Ganda</option>
						<option value="KAT">Georgian</option>
						<option value="DEU">German</option>
						<option value="GRN">Guarani</option>
						<option value="GUJ">Gujarati</option>
						<option value="HAT">Haitian</option>
						<option value="HAU">Hausa</option>
						<option value="HEB">Hebrew</option>
						<option value="HER">Herero</option>
						<option value="HIN">Hindi</option>
						<option value="HMO">Hiri Motu</option>
						<option value="HUN">Hungarian</option>
						<option value="ISL">Icelandic</option>
						<option value="IBO">Igbo</option>
						<option value="IND">Indonesian</option>
						<option value="IKU">Inuktitut</option>
						<option value="IPK">Inupiaq</option>
						<option value="GLE">Irish</option>
						<option value="ITA">Italian</option>
						<option value="JPN">Japanese</option>
						<option value="JAV">Javanese</option>
						<option value="KAL">Kalaallisut</option>
						<option value="KAN">Kannada</option>
						<option value="KAU">Kanuri</option>
						<option value="KAS">Kashmiri</option>
						<option value="KAZ">Kazakh</option>
						<option value="KIK">Kikuyu</option>
						<option value="KIN">Kinyarwanda</option>
						<option value="KIR">Kirghiz</option>
						<option value="KOM">Komi</option>
						<option value="KON">Kongo</option>
						<option value="KOR">Korean</option>
						<option value="KUA">Kuanyama</option>
						<option value="KUR">Kurdish</option>
						<option value="LAO">Lao</option>
						<option value="LAV">Latvian</option>
						<option value="LIM">Limburgan</option>
						<option value="LIN">Lingala</option>
						<option value="LIT">Lithuanian</option>
						<option value="LUB">Luba-Katanga</option>
						<option value="LTZ">Luxembourgish</option>
						<option value="MKD">Macedonian</option>
						<option value="MLG">Malagasy</option>
						<option value="MSA">Malay</option>
						<option value="MAL">Malayalam</option>
						<option value="MLT">Maltese</option>
						<option value="GLV">Manx</option>
						<option value="MRI">Maori</option>
						<option value="MAR">Marathi</option>
						<option value="MAH">Marshallese</option>
						<option value="ELL">Greek</option>
						<option value="MON">Mongolian</option>
						<option value="NAU">Nauru</option>
						<option value="NAV">Navajo</option>
						<option value="NDO">Ndonga</option>
						<option value="NEP">Nepali</option>
						<option value="NDE">North Ndebele</option>
						<option value="SME">Northern Sami</option>
						<option value="NOR">Norwegian</option>
						<option value="NOB">Norwegian Bokmål</option>
						<option value="NNO">Norwegian Nynorsk</option>
						<option value="NYA">Nyanja</option>
						<option value="OCI">Occitan (post 1500)</option>
						<option value="OJI">Ojibwa</option>
						<option value="OJC">Oji-cree</option>
						<option value="ORI">Oriya</option>
						<option value="ORM">Oromo</option>
						<option value="OSS">Ossetian</option>
						<option value="PAN">Panjabi</option>
						<option value="FAS">Persian</option>
						<option value="POL">Polish</option>
						<option value="POR">Portuguese</option>
						<option value="PUS">Pushto</option>
						<option value="QUE">Quechua</option>
						<option value="RON">Romanian</option>
						<option value="ROH">Romansh</option>
						<option value="RUN">Rundi</option>
						<option value="RUS">Russian</option>
						<option value="SMO">Samoan</option>
						<option value="SAG">Sango</option>
						<option value="SRD">Sardinian</option>
						<option value="GLA">Scottish Gaelic</option>
						<option value="SRP">Serbian</option>
						<option value="SNA">Shona</option>
						<option value="III">Sichuan Yi</option>
						<option value="SND">Sindhi</option>
						<option value="SIN">Sinhala</option>
						<option value="SGN">Other Sign Language</option>
						<option value="SLK">Slovak</option>
						<option value="SLV">Slovenian</option>
						<option value="SOM">Somali</option>
						<option value="NBL">South Ndebele</option>
						<option value="SOT">Southern Sotho</option>
						<option value="SPA">Spanish</option>
						<option value="SUN">Sundanese</option>
						<option value="SWA">Swahili (macrolanguage)</option>
						<option value="SSW">Swati</option>
						<option value="SWE">Swedish</option>
						<option value="TGL">Tagalog</option>
						<option value="TAH">Tahitian</option>
						<option value="TGK">Tajik</option>
						<option value="TAM">Tamil</option>
						<option value="TAT">Tatar</option>
						<option value="TEL">Telugu</option>
						<option value="THA">Thai</option>
						<option value="BOD">Tibetan</option>
						<option value="TIR">Tigrinya</option>
						<option value="TON">Tonga (Tonga Islands)</option>
						<option value="TSO">Tsonga</option>
						<option value="TSN">Tswana</option>
						<option value="TUR">Turkish</option>
						<option value="TUK">Turkmen</option>
						<option value="TWI">Twi</option>
						<option value="UIG">Uighur</option>
						<option value="UKR">Ukrainian</option>
						<option value="URD">Urdu</option>
						<option value="UZB">Uzbek</option>
						<option value="VEN">Venda</option>
						<option value="VIE">Vietnamese</option>
						<option value="WLN">Walloon</option>
						<option value="CYM">Welsh</option>
						<option value="FRY">Western Frisian</option>
						<option value="WOL">Wolof</option>
						<option value="XHO">Xhosa</option>
						<option value="YID">Yiddish</option>
						<option value="YOR">Yoruba</option>
						<option value="ZHA">Zhuang</option>
						<option value="ZUL">Zulu</option>
						<option value="OTH">Other</option>
						<option value="UN">Unknown</option>					
					</select>						
			</td>
			<td width="20%" valign="top">Partner's Occupation<br>			
				<select name="pg1_partnerOccupation" style="width: 100%">
					<option value="OCC1290">Unknown</option>
					<option value="OTHER">Other</option>			
					<option value="OCC0005">Agriculture \ Natural Resources</option>
					<option value="OCC0010">Agriculture \ Natural Resources | Agriculture \ Farm Worker</option>
					<option value="OCC0015">Agriculture \ Natural Resources | Environmental Field worker</option>
					<option value="OCC0020">Agriculture \ Natural Resources | Environmentalist</option>
					<option value="OCC0025">Agriculture \ Natural Resources | Farmer</option>
					<option value="OCC0030">Agriculture \ Natural Resources | Fisherman</option>
					<option value="OCC0035">Agriculture \ Natural Resources | Forestry Worker</option>
					<option value="OCC0040">Agriculture \ Natural Resources | Hunter</option>
					<option value="OCC0045">Agriculture \ Natural Resources | Landscaper \ Gardener</option>
					<option value="OCC0050">Agriculture \ Natural Resources | Miner</option>
					<option value="OCC0055">Agriculture \ Natural Resources | Nuclear Plant Worker</option>
					<option value="OCC0060">Agriculture \ Natural Resources | Oil & Gas Worker</option>
					<option value="OCC0065">Agriculture \ Natural Resources | Other</option>
					<option value="OCC0070">Art \ Culture \ Sport</option>
					<option value="OCC0075">Art \ Culture \ Sport | Actor</option>
					<option value="OCC0080">Art \ Culture \ Sport | Archivist</option>
					<option value="OCC0085">Art \ Culture \ Sport | Artist</option>
					<option value="OCC0090">Art \ Culture \ Sport | Athlete</option>
					<option value="OCC0095">Art \ Culture \ Sport | Coach</option>
					<option value="OCC0100">Art \ Culture \ Sport | Conductor</option>
					<option value="OCC0105">Art \ Culture \ Sport | Conservator</option>
					<option value="OCC0110">Art \ Culture \ Sport | Curator</option>
					<option value="OCC0115">Art \ Culture \ Sport | Dancer</option>
					<option value="OCC0120">Art \ Culture \ Sport | Director</option>
					<option value="OCC0125">Art \ Culture \ Sport | Editor</option>
					<option value="OCC0130">Art \ Culture \ Sport | Events co-ordinator</option>
					<option value="OCC0135">Art \ Culture \ Sport | Exotic Dancer</option>
					<option value="OCC0140">Art \ Culture \ Sport | Fashion Designer</option>
					<option value="OCC0145">Art \ Culture \ Sport | Film producer</option>
					<option value="OCC0150">Art \ Culture \ Sport | Fitness instructor</option>
					<option value="OCC0155">Art \ Culture \ Sport | Golf course superintendant</option>
					<option value="OCC0160">Art \ Culture \ Sport | Graphic Designer</option>
					<option value="OCC0165">Art \ Culture \ Sport | Interior Designer</option>
					<option value="OCC0170">Art \ Culture \ Sport | Jewellery designer</option>
					<option value="OCC0175">Art \ Culture \ Sport | Journalist</option>
					<option value="OCC0180">Art \ Culture \ Sport | Librarian</option>
					<option value="OCC0185">Art \ Culture \ Sport | Musician</option>
					<option value="OCC0190">Art \ Culture \ Sport | Other</option>
					<option value="OCC0195">Art \ Culture \ Sport | Personal trainer</option>
					<option value="OCC0200">Art \ Culture \ Sport | Photographer</option>
					<option value="OCC0205">Art \ Culture \ Sport | Producer</option>
					<option value="OCC0210">Art \ Culture \ Sport | Singer</option>
					<option value="OCC0215">Art \ Culture \ Sport | Writer \ Author</option>
					<option value="OCC0220">Govt. \ Business \ Admin.\ Finance</option>
					<option value="OCC0225">Govt. \ Business \ Admin.\ Finance | Accountant</option>
					<option value="OCC0230">Govt. \ Business \ Admin.\ Finance | Actuary</option>
					<option value="OCC0235">Govt. \ Business \ Admin.\ Finance | Administrative Officer</option>
					<option value="OCC0240">Govt. \ Business \ Admin.\ Finance | Auditor</option>
					<option value="OCC0245">Govt. \ Business \ Admin.\ Finance | Bank Teller</option>
					<option value="OCC0250">Govt. \ Business \ Admin.\ Finance | Banker</option>
					<option value="OCC0255">Govt. \ Business \ Admin.\ Finance | Bookkeeper</option>
					<option value="OCC0260">Govt. \ Business \ Admin.\ Finance | Business analyst</option>
					<option value="OCC0265">Govt. \ Business \ Admin.\ Finance | Civil servant</option>
					<option value="OCC0270">Govt. \ Business \ Admin.\ Finance | Clerk</option>
					<option value="OCC0275">Govt. \ Business \ Admin.\ Finance | Company executive</option>
					<option value="OCC0280">Govt. \ Business \ Admin.\ Finance | Consultant</option>
					<option value="OCC0285">Govt. \ Business \ Admin.\ Finance | Director</option>
					<option value="OCC0290">Govt. \ Business \ Admin.\ Finance | Executive assistant</option>
					<option value="OCC0295">Govt. \ Business \ Admin.\ Finance | Financial Advisor \ Officer</option>
					<option value="OCC0300">Govt. \ Business \ Admin.\ Finance | Government Official \ Legislator</option>
					<option value="OCC0305">Govt. \ Business \ Admin.\ Finance | Human Resources Worker</option>
					<option value="OCC0310">Govt. \ Business \ Admin.\ Finance | Import \ Export</option>
					<option value="OCC0315">Govt. \ Business \ Admin.\ Finance | Insurance</option>
					<option value="OCC0320">Govt. \ Business \ Admin.\ Finance | Investment Advisor</option>
					<option value="OCC0325">Govt. \ Business \ Admin.\ Finance | Manager</option>
					<option value="OCC0330">Govt. \ Business \ Admin.\ Finance | Mortgage broker</option>
					<option value="OCC0335">Govt. \ Business \ Admin.\ Finance | Other</option>
					<option value="OCC0340">Govt. \ Business \ Admin.\ Finance | Secretary \ Clerical</option>
					<option value="OCC0345">Govt. \ Business \ Admin.\ Finance | Securities Agent</option>
					<option value="OCC0350">Govt. \ Business \ Admin.\ Finance | Stock Broker</option>
					<option value="OCC0355">Healthcare</option>
					<option value="OCC0360">Healthcare | Aesthetician</option>
					<option value="OCC0365">Healthcare | Behavioural therapist</option>
					<option value="OCC0370">Healthcare | Chiropractor</option>
					<option value="OCC0375">Healthcare | Dental Assistant</option>
					<option value="OCC0380">Healthcare | Dental Hygienist</option>
					<option value="OCC0385">Healthcare | Dentist</option>
					<option value="OCC0390">Healthcare | Denturist</option>
					<option value="OCC0395">Healthcare | Dietary Aide</option>
					<option value="OCC0400">Healthcare | Dietician</option>
					<option value="OCC0405">Healthcare | Genetics Counsellor</option>
					<option value="OCC0410">Healthcare | Lab Technician</option>
					<option value="OCC0415">Healthcare | LPN  (licenced practical nurse)</option>
					<option value="OCC0420">Healthcare | Massage therapist</option>
					<option value="OCC0425">Healthcare | Medical receptionist</option>
					<option value="OCC0430">Healthcare | Medical Technologist \ Technician</option>
					<option value="OCC0435">Healthcare | Midwife</option>
					<option value="OCC0440">Healthcare | MRI technologist</option>
					<option value="OCC0445">Healthcare | Nurse  (RN or BScN)</option>
					<option value="OCC0450">Healthcare | Nutritionist</option>
					<option value="OCC0455">Healthcare | Occupational Therapist  (OT)</option>
					<option value="OCC0460">Healthcare | Optician \ Optometrist</option>
					<option value="OCC0465">Healthcare | Other</option>
					<option value="OCC0470">Healthcare | Paramedic</option>
					<option value="OCC0475">Healthcare | Patient relations</option>
					<option value="OCC0480">Healthcare | Personal care worker</option>
					<option value="OCC0485">Healthcare | Pharmaceutical Rep.</option>
					<option value="OCC0490">Healthcare | Pharmacist</option>
					<option value="OCC0495">Healthcare | Pharmacy technician</option>
					<option value="OCC0500">Healthcare | Physician</option>
					<option value="OCC0505">Healthcare | Physiotherapist</option>
					<option value="OCC0510">Healthcare | Prosthetist</option>
					<option value="OCC0515">Healthcare | Psychologist</option>
					<option value="OCC0520">Healthcare | Psychotherapist</option>
					<option value="OCC0525">Healthcare | Public health inspector</option>
					<option value="OCC0530">Healthcare | Radiation therapist</option>
					<option value="OCC0535">Healthcare | Recreation worker</option>
					<option value="OCC0540">Healthcare | Respiratory therapist</option>
					<option value="OCC0545">Healthcare | Speech Therapist</option>
					<option value="OCC0550">Healthcare | Veterinarian</option>
					<option value="OCC0555">Home \ other</option>
					<option value="OCC0560">Home \ other | Homemaker</option>
					<option value="OCC0565">Home \ other | Other</option>
					<option value="OCC0570">Home \ other | Recently immigrated - not working yet</option>
					<option value="OCC0575">Home \ other | Self employed</option>
					<option value="OCC0580">Home \ other | Unemployed</option>
					<option value="OCC0585">Home \ other | Unknown</option>
					<option value="OCC0590">Police \ Military \ Fire</option>
					<option value="OCC0595">Police \ Military \ Fire | Airport screening officer</option>
					<option value="OCC0600">Police \ Military \ Fire | Firefighter</option>
					<option value="OCC0605">Police \ Military \ Fire | Law enforcement officer - other</option>
					<option value="OCC0610">Police \ Military \ Fire | Military \ Soldier</option>
					<option value="OCC0615">Police \ Military \ Fire | Other</option>
					<option value="OCC0620">Police \ Military \ Fire | Police</option>
					<option value="OCC0625">Police \ Military \ Fire | Private detective</option>
					<option value="OCC0630">Police \ Military \ Fire | Sailor</option>
					<option value="OCC0635">Police \ Military \ Fire | Security guard</option>
					<option value="OCC0640">Processing \ Manufacturing</option>
					<option value="OCC0645">Processing \ Manufacturing | Chemical Plant Worker</option>
					<option value="OCC0650">Processing \ Manufacturing | Delivery \ supplies</option>
					<option value="OCC0655">Processing \ Manufacturing | Distribution supervisor</option>
					<option value="OCC0660">Processing \ Manufacturing | Factory \ Assembly Line Worker</option>
					<option value="OCC0665">Processing \ Manufacturing | Fish Plant Worker</option>
					<option value="OCC0670">Processing \ Manufacturing | Food processing</option>
					<option value="OCC0675">Processing \ Manufacturing | Foundry Worker</option>
					<option value="OCC0680">Processing \ Manufacturing | Inventory auditor - warehouse</option>
					<option value="OCC0685">Processing \ Manufacturing | Labourer (Processing)</option>
					<option value="OCC0690">Processing \ Manufacturing | Mineral \ Metal Processing Operator</option>
					<option value="OCC0695">Processing \ Manufacturing | Other</option>
					<option value="OCC0700">Processing \ Manufacturing | Printer</option>
					<option value="OCC0705">Processing \ Manufacturing | Product manager</option>
					<option value="OCC0710">Processing \ Manufacturing | Quality control</option>
					<option value="OCC0715">Processing \ Manufacturing | Rubber Plant Worker</option>
					<option value="OCC0720">Processing \ Manufacturing | Sheet metal worker</option>
					<option value="OCC0725">Processing \ Manufacturing | Shipping \ Receiving</option>
					<option value="OCC0730">Processing \ Manufacturing | Supervisor (Processing)</option>
					<option value="OCC0735">Processing \ Manufacturing | Textile \ Dye Worker</option>
					<option value="OCC0740">Processing \ Manufacturing | Warehouse manager</option>
					<option value="OCC0745">Sales \ Service</option>
					<option value="OCC0750">Sales \ Service | Animal Control Officer</option>
					<option value="OCC0755">Sales \ Service | Automobile detailing</option>
					<option value="OCC0760">Sales \ Service | Babysitter \ Nanny</option>
					<option value="OCC0765">Sales \ Service | Baker</option>
					<option value="OCC0770">Sales \ Service | Bartender</option>
					<option value="OCC0775">Sales \ Service | Butcher</option>
					<option value="OCC0780">Sales \ Service | Car rental agency</option>
					<option value="OCC0785">Sales \ Service | Cashier</option>
					<option value="OCC0790">Sales \ Service | Chef \ Cook</option>
					<option value="OCC0795">Sales \ Service | Childcare Worker</option>
					<option value="OCC0800">Sales \ Service | Cleaner</option>
					<option value="OCC0805">Sales \ Service | Concierge</option>
					<option value="OCC0810">Sales \ Service | Customer service agent</option>
					<option value="OCC0815">Sales \ Service | Dry Cleaning \ Laundry Worker</option>
					<option value="OCC0820">Sales \ Service | Esthetician</option>
					<option value="OCC0825">Sales \ Service | Exterminator</option>
					<option value="OCC0830">Sales \ Service | Funeral director</option>
					<option value="OCC0835">Sales \ Service | Hairdresser \ Barber</option>
					<option value="OCC0840">Sales \ Service | Housekeeper</option>
					<option value="OCC0845">Sales \ Service | Insurance Agent</option>
					<option value="OCC0850">Sales \ Service | Letter Carrier \ Postal Worker</option>
					<option value="OCC0855">Sales \ Service | Manager</option>
					<option value="OCC0860">Sales \ Service | Marketing</option>
					<option value="OCC0865">Sales \ Service | Other</option>
					<option value="OCC0870">Sales \ Service | Realtor</option>
					<option value="OCC0875">Sales \ Service | Restauranteur</option>
					<option value="OCC0880">Sales \ Service | Sales Rep.\ Salesperson</option>
					<option value="OCC0885">Sales \ Service | Stocker</option>
					<option value="OCC0890">Sales \ Service | Supervisor (sales\service)</option>
					<option value="OCC0895">Sales \ Service | Telemarketer \ call centre agent</option>
					<option value="OCC0900">Sales \ Service | Travel Agent</option>
					<option value="OCC0905">Sales \ Service | Waitress \ Waiter \ Restaurant worker</option>
					<option value="OCC0910">Sales \ Service | Wholesaler</option>
					<option value="OCC0915">Science \ IT</option>
					<option value="OCC0920">Science \ IT | Architect</option>
					<option value="OCC0925">Science \ IT | Computer Programmer</option>
					<option value="OCC0930">Science \ IT | Data entry</option>
					<option value="OCC0935">Science \ IT | Draftsman \ Designer</option>
					<option value="OCC0940">Science \ IT | Engineer</option>
					<option value="OCC0945">Science \ IT | IT (Information Technology)</option>
					<option value="OCC0950">Science \ IT | Other</option>
					<option value="OCC0955">Science \ IT | Project manager</option>
					<option value="OCC0960">Science \ IT | Research assistant</option>
					<option value="OCC0965">Science \ IT | Research scientist</option>
					<option value="OCC0970">Science \ IT | Scientist</option>
					<option value="OCC0975">Science \ IT | Statistician</option>
					<option value="OCC0980">Science \ IT | Systems Analyst</option>
					<option value="OCC0985">Science \ IT | Technician \ Technologist</option>
					<option value="OCC0990">Science \ IT | Web designer</option>
					<option value="OCC0995">Soc. Science \ Education \ Religion</option>
					<option value="OCC1000">Soc. Science \ Education \ Religion | Child & Youth worker</option>
					<option value="OCC1005">Soc. Science \ Education \ Religion | Early Childhood Educator \ Daycare worker</option>
					<option value="OCC1010">Soc. Science \ Education \ Religion | Judge</option>
					<option value="OCC1015">Soc. Science \ Education \ Religion | Lawyer</option>
					<option value="OCC1020">Soc. Science \ Education \ Religion | Military</option>
					<option value="OCC1025">Soc. Science \ Education \ Religion | Minister \ Pastor</option>
					<option value="OCC1030">Soc. Science \ Education \ Religion | Notary Public</option>
					<option value="OCC1035">Soc. Science \ Education \ Religion | Other</option>
					<option value="OCC1040">Soc. Science \ Education \ Religion | Post graduate studies</option>
					<option value="OCC1045">Soc. Science \ Education \ Religion | Professor</option>
					<option value="OCC1050">Soc. Science \ Education \ Religion | Psychologist</option>
					<option value="OCC1055">Soc. Science \ Education \ Religion | Recreation therapist</option>
					<option value="OCC1060">Soc. Science \ Education \ Religion | Religious Leader</option>
					<option value="OCC1065">Soc. Science \ Education \ Religion | Social Worker</option>
					<option value="OCC1070">Soc. Science \ Education \ Religion | Student  (college)</option>
					<option value="OCC1075">Soc. Science \ Education \ Religion | Student  (high school)</option>
					<option value="OCC1080">Soc. Science \ Education \ Religion | Teacher</option>
					<option value="OCC1085">Soc. Science \ Education \ Religion | Teacher's aide \ Tutor</option>
					<option value="OCC1090">Trades\Transport</option>
					<option value="OCC1095">Trades\Transport | Aircraft technician</option>
					<option value="OCC1100">Trades\Transport | Autobody shop</option>
					<option value="OCC1105">Trades\Transport | Boiler maker</option>
					<option value="OCC1110">Trades\Transport | Bus Driver</option>
					<option value="OCC1115">Trades\Transport | Carpenter</option>
					<option value="OCC1120">Trades\Transport | Construction worker</option>
					<option value="OCC1125">Trades\Transport | Contractor</option>
					<option value="OCC1130">Trades\Transport | Electrician</option>
					<option value="OCC1135">Trades\Transport | Flight Attendant</option>
					<option value="OCC1140">Trades\Transport | Flooring contractor</option>
					<option value="OCC1145">Trades\Transport | Forklift operator</option>
					<option value="OCC1150">Trades\Transport | Garbage Collector</option>
					<option value="OCC1155">Trades\Transport | Gas Fitter</option>
					<option value="OCC1160">Trades\Transport | Handyman</option>
					<option value="OCC1165">Trades\Transport | Heavy equipment operator</option>
					<option value="OCC1170">Trades\Transport | Janitor</option>
					<option value="OCC1175">Trades\Transport | Labourer (Builder)</option>
					<option value="OCC1180">Trades\Transport | Mechanic</option>
					<option value="OCC1185">Trades\Transport | Miner</option>
					<option value="OCC1190">Trades\Transport | Oil rig worker</option>
					<option value="OCC1195">Trades\Transport | Other</option>
					<option value="OCC1200">Trades\Transport | Packer</option>
					<option value="OCC1205">Trades\Transport | Painter</option>
					<option value="OCC1210">Trades\Transport | Pilot</option>
					<option value="OCC1215">Trades\Transport | Plumber</option>
					<option value="OCC1220">Trades\Transport | Power Station Operator</option>
					<option value="OCC1225">Trades\Transport | Project manager</option>
					<option value="OCC1230">Trades\Transport | Railway Conductor \ Worker</option>
					<option value="OCC1235">Trades\Transport | Roofer</option>
					<option value="OCC1240">Trades\Transport | Security \ Commissionaire</option>
					<option value="OCC1245">Trades\Transport | Supervisor</option>
					<option value="OCC1250">Trades\Transport | Tailor \ Dressmaker</option>
					<option value="OCC1255">Trades\Transport | Taxi Driver</option>
					<option value="OCC1260">Trades\Transport | Telecommunication \ Cable  Worker</option>
					<option value="OCC1265">Trades\Transport | Telephone technician</option>
					<option value="OCC1270">Trades\Transport | Tool & Die Maker</option>
					<option value="OCC1275">Trades\Transport | Transportation Controller</option>
					<option value="OCC1280">Trades\Transport | Truck Driver</option>
					<option value="OCC1285">Trades\Transport | Welder</option>						
				</select>
				<br/>
				<input type="text" name="c_partnerOccupationOther" size="10" style="width: 100%"
				maxlength="20" readonly="readonly"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_partnerOccupationOther", "")) %>" />
			</td>
			<td colspan="2" valign="top">Partner's Education level <br>
					<select name="pg1_partnerEduLevel" style="width: 100%" >
						<option value="UNK">Select</option>
						<option value="ED001">College/University Completed</option>
						<option value="ED002">College/University not Completed</option>
						<option value="ED003">Grade School Completed</option>
						<option value="ED004">High School Completed</option>
						<option value="ED005">High School not Completed</option>						
					</select>													
			</td>
			<td width="8%" valign="top">Age<br>
			<input type="text" name="pg1_partnerAge" style="width: 100%" size="5"
				maxlength="5" value="<%= props.getProperty("pg1_partnerAge", "") %>" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" width="12%">Date of birth<br>
			<input type="text" name="pg1_dateOfBirth" style="width: 100%"
				size="10" maxlength="10"
				value="<%= props.getProperty("pg1_dateOfBirth", "") %>"
				readonly="true" /></td>
			<td width="8%" valign="top">Age<br>
			<input type="text" name="pg1_age" style="width: 100%" size="10"
				maxlength="10" value="<%= props.getProperty("pg1_age", "") %>" /></td>
			<td width="15%" valign="top">Occupation<br>
				<select name="pg1_occupation" style="width: 100%">
					<option value="OCC1290">Unknown</option>
					<option value="OTHER">Other</option>			
					<option value="OCC0005">Agriculture \ Natural Resources</option>
					<option value="OCC0010">Agriculture \ Natural Resources | Agriculture \ Farm Worker</option>
					<option value="OCC0015">Agriculture \ Natural Resources | Environmental Field worker</option>
					<option value="OCC0020">Agriculture \ Natural Resources | Environmentalist</option>
					<option value="OCC0025">Agriculture \ Natural Resources | Farmer</option>
					<option value="OCC0030">Agriculture \ Natural Resources | Fisherman</option>
					<option value="OCC0035">Agriculture \ Natural Resources | Forestry Worker</option>
					<option value="OCC0040">Agriculture \ Natural Resources | Hunter</option>
					<option value="OCC0045">Agriculture \ Natural Resources | Landscaper \ Gardener</option>
					<option value="OCC0050">Agriculture \ Natural Resources | Miner</option>
					<option value="OCC0055">Agriculture \ Natural Resources | Nuclear Plant Worker</option>
					<option value="OCC0060">Agriculture \ Natural Resources | Oil & Gas Worker</option>
					<option value="OCC0065">Agriculture \ Natural Resources | Other</option>
					<option value="OCC0070">Art \ Culture \ Sport</option>
					<option value="OCC0075">Art \ Culture \ Sport | Actor</option>
					<option value="OCC0080">Art \ Culture \ Sport | Archivist</option>
					<option value="OCC0085">Art \ Culture \ Sport | Artist</option>
					<option value="OCC0090">Art \ Culture \ Sport | Athlete</option>
					<option value="OCC0095">Art \ Culture \ Sport | Coach</option>
					<option value="OCC0100">Art \ Culture \ Sport | Conductor</option>
					<option value="OCC0105">Art \ Culture \ Sport | Conservator</option>
					<option value="OCC0110">Art \ Culture \ Sport | Curator</option>
					<option value="OCC0115">Art \ Culture \ Sport | Dancer</option>
					<option value="OCC0120">Art \ Culture \ Sport | Director</option>
					<option value="OCC0125">Art \ Culture \ Sport | Editor</option>
					<option value="OCC0130">Art \ Culture \ Sport | Events co-ordinator</option>
					<option value="OCC0135">Art \ Culture \ Sport | Exotic Dancer</option>
					<option value="OCC0140">Art \ Culture \ Sport | Fashion Designer</option>
					<option value="OCC0145">Art \ Culture \ Sport | Film producer</option>
					<option value="OCC0150">Art \ Culture \ Sport | Fitness instructor</option>
					<option value="OCC0155">Art \ Culture \ Sport | Golf course superintendant</option>
					<option value="OCC0160">Art \ Culture \ Sport | Graphic Designer</option>
					<option value="OCC0165">Art \ Culture \ Sport | Interior Designer</option>
					<option value="OCC0170">Art \ Culture \ Sport | Jewellery designer</option>
					<option value="OCC0175">Art \ Culture \ Sport | Journalist</option>
					<option value="OCC0180">Art \ Culture \ Sport | Librarian</option>
					<option value="OCC0185">Art \ Culture \ Sport | Musician</option>
					<option value="OCC0190">Art \ Culture \ Sport | Other</option>
					<option value="OCC0195">Art \ Culture \ Sport | Personal trainer</option>
					<option value="OCC0200">Art \ Culture \ Sport | Photographer</option>
					<option value="OCC0205">Art \ Culture \ Sport | Producer</option>
					<option value="OCC0210">Art \ Culture \ Sport | Singer</option>
					<option value="OCC0215">Art \ Culture \ Sport | Writer \ Author</option>
					<option value="OCC0220">Govt. \ Business \ Admin.\ Finance</option>
					<option value="OCC0225">Govt. \ Business \ Admin.\ Finance | Accountant</option>
					<option value="OCC0230">Govt. \ Business \ Admin.\ Finance | Actuary</option>
					<option value="OCC0235">Govt. \ Business \ Admin.\ Finance | Administrative Officer</option>
					<option value="OCC0240">Govt. \ Business \ Admin.\ Finance | Auditor</option>
					<option value="OCC0245">Govt. \ Business \ Admin.\ Finance | Bank Teller</option>
					<option value="OCC0250">Govt. \ Business \ Admin.\ Finance | Banker</option>
					<option value="OCC0255">Govt. \ Business \ Admin.\ Finance | Bookkeeper</option>
					<option value="OCC0260">Govt. \ Business \ Admin.\ Finance | Business analyst</option>
					<option value="OCC0265">Govt. \ Business \ Admin.\ Finance | Civil servant</option>
					<option value="OCC0270">Govt. \ Business \ Admin.\ Finance | Clerk</option>
					<option value="OCC0275">Govt. \ Business \ Admin.\ Finance | Company executive</option>
					<option value="OCC0280">Govt. \ Business \ Admin.\ Finance | Consultant</option>
					<option value="OCC0285">Govt. \ Business \ Admin.\ Finance | Director</option>
					<option value="OCC0290">Govt. \ Business \ Admin.\ Finance | Executive assistant</option>
					<option value="OCC0295">Govt. \ Business \ Admin.\ Finance | Financial Advisor \ Officer</option>
					<option value="OCC0300">Govt. \ Business \ Admin.\ Finance | Government Official \ Legislator</option>
					<option value="OCC0305">Govt. \ Business \ Admin.\ Finance | Human Resources Worker</option>
					<option value="OCC0310">Govt. \ Business \ Admin.\ Finance | Import \ Export</option>
					<option value="OCC0315">Govt. \ Business \ Admin.\ Finance | Insurance</option>
					<option value="OCC0320">Govt. \ Business \ Admin.\ Finance | Investment Advisor</option>
					<option value="OCC0325">Govt. \ Business \ Admin.\ Finance | Manager</option>
					<option value="OCC0330">Govt. \ Business \ Admin.\ Finance | Mortgage broker</option>
					<option value="OCC0335">Govt. \ Business \ Admin.\ Finance | Other</option>
					<option value="OCC0340">Govt. \ Business \ Admin.\ Finance | Secretary \ Clerical</option>
					<option value="OCC0345">Govt. \ Business \ Admin.\ Finance | Securities Agent</option>
					<option value="OCC0350">Govt. \ Business \ Admin.\ Finance | Stock Broker</option>
					<option value="OCC0355">Healthcare</option>
					<option value="OCC0360">Healthcare | Aesthetician</option>
					<option value="OCC0365">Healthcare | Behavioural therapist</option>
					<option value="OCC0370">Healthcare | Chiropractor</option>
					<option value="OCC0375">Healthcare | Dental Assistant</option>
					<option value="OCC0380">Healthcare | Dental Hygienist</option>
					<option value="OCC0385">Healthcare | Dentist</option>
					<option value="OCC0390">Healthcare | Denturist</option>
					<option value="OCC0395">Healthcare | Dietary Aide</option>
					<option value="OCC0400">Healthcare | Dietician</option>
					<option value="OCC0405">Healthcare | Genetics Counsellor</option>
					<option value="OCC0410">Healthcare | Lab Technician</option>
					<option value="OCC0415">Healthcare | LPN  (licenced practical nurse)</option>
					<option value="OCC0420">Healthcare | Massage therapist</option>
					<option value="OCC0425">Healthcare | Medical receptionist</option>
					<option value="OCC0430">Healthcare | Medical Technologist \ Technician</option>
					<option value="OCC0435">Healthcare | Midwife</option>
					<option value="OCC0440">Healthcare | MRI technologist</option>
					<option value="OCC0445">Healthcare | Nurse  (RN or BScN)</option>
					<option value="OCC0450">Healthcare | Nutritionist</option>
					<option value="OCC0455">Healthcare | Occupational Therapist  (OT)</option>
					<option value="OCC0460">Healthcare | Optician \ Optometrist</option>
					<option value="OCC0465">Healthcare | Other</option>
					<option value="OCC0470">Healthcare | Paramedic</option>
					<option value="OCC0475">Healthcare | Patient relations</option>
					<option value="OCC0480">Healthcare | Personal care worker</option>
					<option value="OCC0485">Healthcare | Pharmaceutical Rep.</option>
					<option value="OCC0490">Healthcare | Pharmacist</option>
					<option value="OCC0495">Healthcare | Pharmacy technician</option>
					<option value="OCC0500">Healthcare | Physician</option>
					<option value="OCC0505">Healthcare | Physiotherapist</option>
					<option value="OCC0510">Healthcare | Prosthetist</option>
					<option value="OCC0515">Healthcare | Psychologist</option>
					<option value="OCC0520">Healthcare | Psychotherapist</option>
					<option value="OCC0525">Healthcare | Public health inspector</option>
					<option value="OCC0530">Healthcare | Radiation therapist</option>
					<option value="OCC0535">Healthcare | Recreation worker</option>
					<option value="OCC0540">Healthcare | Respiratory therapist</option>
					<option value="OCC0545">Healthcare | Speech Therapist</option>
					<option value="OCC0550">Healthcare | Veterinarian</option>
					<option value="OCC0555">Home \ other</option>
					<option value="OCC0560">Home \ other | Homemaker</option>
					<option value="OCC0565">Home \ other | Other</option>
					<option value="OCC0570">Home \ other | Recently immigrated - not working yet</option>
					<option value="OCC0575">Home \ other | Self employed</option>
					<option value="OCC0580">Home \ other | Unemployed</option>
					<option value="OCC0585">Home \ other | Unknown</option>
					<option value="OCC0590">Police \ Military \ Fire</option>
					<option value="OCC0595">Police \ Military \ Fire | Airport screening officer</option>
					<option value="OCC0600">Police \ Military \ Fire | Firefighter</option>
					<option value="OCC0605">Police \ Military \ Fire | Law enforcement officer - other</option>
					<option value="OCC0610">Police \ Military \ Fire | Military \ Soldier</option>
					<option value="OCC0615">Police \ Military \ Fire | Other</option>
					<option value="OCC0620">Police \ Military \ Fire | Police</option>
					<option value="OCC0625">Police \ Military \ Fire | Private detective</option>
					<option value="OCC0630">Police \ Military \ Fire | Sailor</option>
					<option value="OCC0635">Police \ Military \ Fire | Security guard</option>
					<option value="OCC0640">Processing \ Manufacturing</option>
					<option value="OCC0645">Processing \ Manufacturing | Chemical Plant Worker</option>
					<option value="OCC0650">Processing \ Manufacturing | Delivery \ supplies</option>
					<option value="OCC0655">Processing \ Manufacturing | Distribution supervisor</option>
					<option value="OCC0660">Processing \ Manufacturing | Factory \ Assembly Line Worker</option>
					<option value="OCC0665">Processing \ Manufacturing | Fish Plant Worker</option>
					<option value="OCC0670">Processing \ Manufacturing | Food processing</option>
					<option value="OCC0675">Processing \ Manufacturing | Foundry Worker</option>
					<option value="OCC0680">Processing \ Manufacturing | Inventory auditor - warehouse</option>
					<option value="OCC0685">Processing \ Manufacturing | Labourer (Processing)</option>
					<option value="OCC0690">Processing \ Manufacturing | Mineral \ Metal Processing Operator</option>
					<option value="OCC0695">Processing \ Manufacturing | Other</option>
					<option value="OCC0700">Processing \ Manufacturing | Printer</option>
					<option value="OCC0705">Processing \ Manufacturing | Product manager</option>
					<option value="OCC0710">Processing \ Manufacturing | Quality control</option>
					<option value="OCC0715">Processing \ Manufacturing | Rubber Plant Worker</option>
					<option value="OCC0720">Processing \ Manufacturing | Sheet metal worker</option>
					<option value="OCC0725">Processing \ Manufacturing | Shipping \ Receiving</option>
					<option value="OCC0730">Processing \ Manufacturing | Supervisor (Processing)</option>
					<option value="OCC0735">Processing \ Manufacturing | Textile \ Dye Worker</option>
					<option value="OCC0740">Processing \ Manufacturing | Warehouse manager</option>
					<option value="OCC0745">Sales \ Service</option>
					<option value="OCC0750">Sales \ Service | Animal Control Officer</option>
					<option value="OCC0755">Sales \ Service | Automobile detailing</option>
					<option value="OCC0760">Sales \ Service | Babysitter \ Nanny</option>
					<option value="OCC0765">Sales \ Service | Baker</option>
					<option value="OCC0770">Sales \ Service | Bartender</option>
					<option value="OCC0775">Sales \ Service | Butcher</option>
					<option value="OCC0780">Sales \ Service | Car rental agency</option>
					<option value="OCC0785">Sales \ Service | Cashier</option>
					<option value="OCC0790">Sales \ Service | Chef \ Cook</option>
					<option value="OCC0795">Sales \ Service | Childcare Worker</option>
					<option value="OCC0800">Sales \ Service | Cleaner</option>
					<option value="OCC0805">Sales \ Service | Concierge</option>
					<option value="OCC0810">Sales \ Service | Customer service agent</option>
					<option value="OCC0815">Sales \ Service | Dry Cleaning \ Laundry Worker</option>
					<option value="OCC0820">Sales \ Service | Esthetician</option>
					<option value="OCC0825">Sales \ Service | Exterminator</option>
					<option value="OCC0830">Sales \ Service | Funeral director</option>
					<option value="OCC0835">Sales \ Service | Hairdresser \ Barber</option>
					<option value="OCC0840">Sales \ Service | Housekeeper</option>
					<option value="OCC0845">Sales \ Service | Insurance Agent</option>
					<option value="OCC0850">Sales \ Service | Letter Carrier \ Postal Worker</option>
					<option value="OCC0855">Sales \ Service | Manager</option>
					<option value="OCC0860">Sales \ Service | Marketing</option>
					<option value="OCC0865">Sales \ Service | Other</option>
					<option value="OCC0870">Sales \ Service | Realtor</option>
					<option value="OCC0875">Sales \ Service | Restauranteur</option>
					<option value="OCC0880">Sales \ Service | Sales Rep.\ Salesperson</option>
					<option value="OCC0885">Sales \ Service | Stocker</option>
					<option value="OCC0890">Sales \ Service | Supervisor (sales\service)</option>
					<option value="OCC0895">Sales \ Service | Telemarketer \ call centre agent</option>
					<option value="OCC0900">Sales \ Service | Travel Agent</option>
					<option value="OCC0905">Sales \ Service | Waitress \ Waiter \ Restaurant worker</option>
					<option value="OCC0910">Sales \ Service | Wholesaler</option>
					<option value="OCC0915">Science \ IT</option>
					<option value="OCC0920">Science \ IT | Architect</option>
					<option value="OCC0925">Science \ IT | Computer Programmer</option>
					<option value="OCC0930">Science \ IT | Data entry</option>
					<option value="OCC0935">Science \ IT | Draftsman \ Designer</option>
					<option value="OCC0940">Science \ IT | Engineer</option>
					<option value="OCC0945">Science \ IT | IT (Information Technology)</option>
					<option value="OCC0950">Science \ IT | Other</option>
					<option value="OCC0955">Science \ IT | Project manager</option>
					<option value="OCC0960">Science \ IT | Research assistant</option>
					<option value="OCC0965">Science \ IT | Research scientist</option>
					<option value="OCC0970">Science \ IT | Scientist</option>
					<option value="OCC0975">Science \ IT | Statistician</option>
					<option value="OCC0980">Science \ IT | Systems Analyst</option>
					<option value="OCC0985">Science \ IT | Technician \ Technologist</option>
					<option value="OCC0990">Science \ IT | Web designer</option>
					<option value="OCC0995">Soc. Science \ Education \ Religion</option>
					<option value="OCC1000">Soc. Science \ Education \ Religion | Child & Youth worker</option>
					<option value="OCC1005">Soc. Science \ Education \ Religion | Early Childhood Educator \ Daycare worker</option>
					<option value="OCC1010">Soc. Science \ Education \ Religion | Judge</option>
					<option value="OCC1015">Soc. Science \ Education \ Religion | Lawyer</option>
					<option value="OCC1020">Soc. Science \ Education \ Religion | Military</option>
					<option value="OCC1025">Soc. Science \ Education \ Religion | Minister \ Pastor</option>
					<option value="OCC1030">Soc. Science \ Education \ Religion | Notary Public</option>
					<option value="OCC1035">Soc. Science \ Education \ Religion | Other</option>
					<option value="OCC1040">Soc. Science \ Education \ Religion | Post graduate studies</option>
					<option value="OCC1045">Soc. Science \ Education \ Religion | Professor</option>
					<option value="OCC1050">Soc. Science \ Education \ Religion | Psychologist</option>
					<option value="OCC1055">Soc. Science \ Education \ Religion | Recreation therapist</option>
					<option value="OCC1060">Soc. Science \ Education \ Religion | Religious Leader</option>
					<option value="OCC1065">Soc. Science \ Education \ Religion | Social Worker</option>
					<option value="OCC1070">Soc. Science \ Education \ Religion | Student  (college)</option>
					<option value="OCC1075">Soc. Science \ Education \ Religion | Student  (high school)</option>
					<option value="OCC1080">Soc. Science \ Education \ Religion | Teacher</option>
					<option value="OCC1085">Soc. Science \ Education \ Religion | Teacher's aide \ Tutor</option>
					<option value="OCC1090">Trades\Transport</option>
					<option value="OCC1095">Trades\Transport | Aircraft technician</option>
					<option value="OCC1100">Trades\Transport | Autobody shop</option>
					<option value="OCC1105">Trades\Transport | Boiler maker</option>
					<option value="OCC1110">Trades\Transport | Bus Driver</option>
					<option value="OCC1115">Trades\Transport | Carpenter</option>
					<option value="OCC1120">Trades\Transport | Construction worker</option>
					<option value="OCC1125">Trades\Transport | Contractor</option>
					<option value="OCC1130">Trades\Transport | Electrician</option>
					<option value="OCC1135">Trades\Transport | Flight Attendant</option>
					<option value="OCC1140">Trades\Transport | Flooring contractor</option>
					<option value="OCC1145">Trades\Transport | Forklift operator</option>
					<option value="OCC1150">Trades\Transport | Garbage Collector</option>
					<option value="OCC1155">Trades\Transport | Gas Fitter</option>
					<option value="OCC1160">Trades\Transport | Handyman</option>
					<option value="OCC1165">Trades\Transport | Heavy equipment operator</option>
					<option value="OCC1170">Trades\Transport | Janitor</option>
					<option value="OCC1175">Trades\Transport | Labourer (Builder)</option>
					<option value="OCC1180">Trades\Transport | Mechanic</option>
					<option value="OCC1185">Trades\Transport | Miner</option>
					<option value="OCC1190">Trades\Transport | Oil rig worker</option>
					<option value="OCC1195">Trades\Transport | Other</option>
					<option value="OCC1200">Trades\Transport | Packer</option>
					<option value="OCC1205">Trades\Transport | Painter</option>
					<option value="OCC1210">Trades\Transport | Pilot</option>
					<option value="OCC1215">Trades\Transport | Plumber</option>
					<option value="OCC1220">Trades\Transport | Power Station Operator</option>
					<option value="OCC1225">Trades\Transport | Project manager</option>
					<option value="OCC1230">Trades\Transport | Railway Conductor \ Worker</option>
					<option value="OCC1235">Trades\Transport | Roofer</option>
					<option value="OCC1240">Trades\Transport | Security \ Commissionaire</option>
					<option value="OCC1245">Trades\Transport | Supervisor</option>
					<option value="OCC1250">Trades\Transport | Tailor \ Dressmaker</option>
					<option value="OCC1255">Trades\Transport | Taxi Driver</option>
					<option value="OCC1260">Trades\Transport | Telecommunication \ Cable  Worker</option>
					<option value="OCC1265">Trades\Transport | Telephone technician</option>
					<option value="OCC1270">Trades\Transport | Tool & Die Maker</option>
					<option value="OCC1275">Trades\Transport | Transportation Controller</option>
					<option value="OCC1280">Trades\Transport | Truck Driver</option>
					<option value="OCC1285">Trades\Transport | Welder</option>						
				</select>
				<br/>
				<input type="text" name="pg1_occupationOther" size="10" style="width: 100%"
				maxlength="20" readonly="readonly"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_occupationOther", "")) %>" />			
			</td>
			<td width="15%" valign="top">Educational level <br>
					<select name="pg1_eduLevel" style="width: 100%" >
						<option value="UNK">Select</option>
						<option value="ED001">College/University Completed</option>
						<option value="ED002">College/University not Completed</option>
						<option value="ED003">Grade School Completed</option>
						<option value="ED004">High School Completed</option>
						<option value="ED005">High School not Completed</option>						
					</select>					
			</td>
			<td colspan="3" width="50%" valign="top">Ethnic or Racial backgrounds<br>
				Mother:
				<select name="pg1_ethnicBgMother" >
					<option value="UN">Select</option>					
					<option value="ANC001">Aboriginal</option>
					<option value="ANC002">Asian</option>
					<option value="ANC005">Black</option>
					<option value="ANC007">Caucasian</option>
					<option value="OTHER">Other</option>									
				</select>
				<br/>
				Father
				<select name="pg1_ethnicBgFather"  >
					<option value="UN">Select</option>					
					<option value="ANC001">Aboriginal</option>
					<option value="ANC002">Asian</option>
					<option value="ANC005">Black</option>
					<option value="ANC007">Caucasian</option>
					<option value="OTHER">Other</option>									
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2" width="20%" valign="top">
				<select name="c_hinType">
					<option value="OHIP">OHIP</option>
					<option value="RAMQ">RAMQ</option>
					<option value="OTHER">OTHER</option>
				</select>
			<input type="text" name="c_hin" size="10" style="width: 100%"
				maxlength="20"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_hin", "")) %>" />
			</td>
			<td width="15%" valign="top">Patient File No. <br>
			<input type="text" name="c_fileNo" size="10" style="width: 100%"
				maxlength="10"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_fileNo", "")) %>" />
			</td>
			<td width="15%" valign="top" nowrap>Marital status <br>
				<select name="pg1_maritalStatus">
					<option value="UN">Unknown</option>
					<option value="M">Married</option>
					<option value="CL">Common Law</option>					
					<option value="S">Single</option>
					
				</select>
			</td>
			<td width="17%"><font size="-1">Birth attendants<br>
			<input type="checkbox" name="pg1_baObs"
				<%= props.getProperty("pg1_baObs", "") %> />OBS <input
				type="checkbox" name="pg1_baFP"
				<%= props.getProperty("pg1_baFP", "") %> />FP <input
				type="checkbox" name="pg1_baMidwife"
				<%= props.getProperty("pg1_baMidwife", "") %> />Midwife<br>
			</font> <input type="text" name="c_ba" size="10" style="width: 100%"
				maxlength="25"
				value="<%= UtilMisc.htmlEscape(props.getProperty("c_ba", "")) %>" />
			</td>
			<td width="17%"><font size="-1">Newborn care<br>
			<input type="checkbox" name="pg1_ncPed"
				<%= props.getProperty("pg1_ncPed", "") %> />Ped. <input
				type="checkbox" name="pg1_ncFP"
				<%= props.getProperty("pg1_ncFP", "") %> />FP <input type="checkbox"
				name="pg1_ncMidwife" <%= props.getProperty("pg1_ncMidwife", "") %> />Midwife<br>
			</font> <input type="text" name="c_nc" size="10" style="width: 100%"
				maxlength="25" value="<%= props.getProperty("c_nc", "") %>" /></td>
			<td>Family physician<br>
			<input type="text" name="c_famPhys" size="30" maxlength="80"
				style="width: 100%"
				value="<%= props.getProperty("c_famPhys", "") %>" /></td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%">Allergies or Sensitivities &nbsp;<a href="javascript:void(0)" onclick="updateAllergies();">Update from Chart</a><br>
			<div align="center"><textarea name="c_allergies"
				style="width: 100%" cols="30" rows="2"><%= props.getProperty("c_allergies", "") %></textarea></div>
			</td>
			<td width="50%">Medications/Herbals&nbsp;<a href="javascript:void(0)" onclick="updateMeds();">Update from Chart</a><br>
			<div align="center"><textarea name="c_meds" style="width: 100%"
				cols="30" rows="2"><%= props.getProperty("c_meds", "") %></textarea></div>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr bgcolor="#99FF99">
			<td valign="top" bgcolor="#CCCCCC">
			<div align="center"><b>Pregnancy Summary</b></div>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" nowrap>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" nowrap>LMP <input type="text"
						name="pg1_menLMP" id="pg1_menLMP" size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menLMP", "")) %>" />
					<img src="../images/cal.gif" id="pg1_menLMP_cal"></td>
					<td>Certain</td>
					<td>Yes <input type="checkbox" name="pg1_psCertY"
						<%= props.getProperty("pg1_psCertY", "") %> /> No <input
						type="checkbox" name="pg1_psCertN"
						<%= props.getProperty("pg1_psCertN", "") %> /></td>
				</tr>
				<tr>
					<td>Cycle q__ <input type="text" name="pg1_menCycle" size="7"
						maxlength="7"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_menCycle", "")) %>" />
					</td>
					<td>Regular</td>
					<td>Yes <input type="checkbox" name="pg1_menReg"
						<%= props.getProperty("pg1_menReg", "") %> /> No <input
						type="checkbox" name="pg1_menRegN"
						<%= props.getProperty("pg1_menRegN", "") %> /></td>
				</tr>
				<tr>
					<td>Contraceptive type <input type="text" name="pg1_contracep"
						size="15" maxlength="25"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_contracep", "")) %>" />
					</td>
					<td>Last used</td>
					<td nowrap="nowrap"><input type="text" name="pg1_lastUsed" id="pg1_lastUsed"
						size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_lastUsed", "")) %>" />
					<img src="../images/cal.gif" id="pg1_lastUsed_cal"></td>
				</tr>
			</table>

			</td>
			<td valign="top" nowrap>EDB (by dates)</br>
			<input type="text" name="pg1_menEDB" id="pg1_menEDB" class="spe"
				onDblClick="calByLMP(this);" size="10" maxlength="10"
				value="<%= props.getProperty("pg1_menEDB", "") %>" /> <img
				src="../images/cal.gif" id="pg1_menEDB_cal"></td>


			<td valign="top" width="35%" rowspan="2">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" align="center"><font size="+1"><b>Final
					EDB</font></b></font><br>
					<input type="text" name="c_finalEDB" id="c_finalEDB" size="10"
						maxlength="10" value="<%= props.getProperty("c_finalEDB", "") %>" />
					<img src="../images/cal.gif" id="c_finalEDB_cal"></td>
					<td width="40%"><u>Dating Method</u></br>
					<input type="checkbox" name="pg1_edbByDate"
						<%= props.getProperty("pg1_edbByDate", "") %> />Dates<br>
					<input type="checkbox" name="pg1_edbByT1"
						<%= props.getProperty("pg1_edbByT1", "") %> />T1US<br>
					<input type="checkbox" name="pg1_edbByT2"
						<%= props.getProperty("pg1_edbByT2", "") %> />T2US<br>
					<input type="checkbox" name="pg1_edbByART"
						<%= props.getProperty("pg1_edbByART", "") %> />ART (e.g. IVF)</td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td colspan="2">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td >Gravida <input type="text" name="c_gravida" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_gravida", "")) %>" />
					</td>
					<td >Term <input type="text" name="c_term" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_term", "")) %>" />
					</td>
					<td >Premature <input type="text" name="c_prem" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_prem", "")) %>" />
					</td>
					<td valign="top" >Abortuses <input type="text"
						name="c_abort" size="1" maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_abort", "")) %>" />
					</td>
					<td>Living <input type="text" name="c_living" size="1"
						maxlength="3"
						value="<%= UtilMisc.htmlEscape(props.getProperty("c_living", "")) %>" />
					</td>
					
				</tr>
			</table>

			</td>
		</tr>
	</table>
	
	<table width="100%" border="0">
		<tr bgcolor="#99FF99">
			<td align="center" colspan="2" bgcolor="#CCCCCC"><b>Obstetrical History</b></td>
		</tr>
	</table>
	
	<table width="100%" border="0">
		<tr>
			<td valign="top">
			<table width="100%" border="1" cellspacing="0" cellpadding="0" id="obxhx_container">
				<thead>
				<tr align="center">
					<td width="20">No.</td>
					<td width="40">Year</td>
					<td width="30">Sex<br>
					M/F</td>
					<td width="60">Gest. age<br>
					(weeks)</td>
					<td width="60">Birth<br>
					weight</td>
					<td width="60">Length<br>
					of labour</td>
					<td width="80">Place<br>
					of birth</td>
					<td width="90">Type of delivery<br>
					<font size="-1">SVB CS Ass'd</font></td>
					<td nowrap>Comments regarding pregnancy and birth</td>
				</tr>	
				</thead>
				<tbody>
				</tbody>
			</table>
			
			<input type="hidden" id="obxhx_num" name="obxhx_num" value="<%= props.getProperty("obxhx_num", "0") %>"/>
			<table width="100%" border="1" cellspacing="0" cellpadding="0">			
				<tr>
					<td colspan="9"><input type="button" value="Add New" onclick="addObxHx();"/></td>
				</tr>				
			</table>
		</td>
	</tr>
	</table>
			


	<table class="shrinkMe" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr rowspan="2">
			<td width="65%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3" align="center" bgcolor="#CCCCCC" nowrap><b>
					Medical History and Physical Exam (provide details in comments)</b></td>
				</tr>
				<tr>
					<td valign="top" width="33%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Current Pregnancy</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td width="6%">1.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c1p1");return false;'>Bleeding</a></td>
							<td><input type="checkbox" name="pg1_cp1"
								<%= props.getProperty("pg1_cp1", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp1N"
								<%= props.getProperty("pg1_cp1N", "") %> /></td>
						</tr>
						<tr>
							<td>2.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c2p1");return false;'>Nausea,
							vomiting</a></td>
							<td><input type="checkbox" name="pg1_cp2"
								<%= props.getProperty("pg1_cp2", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp2N"
								<%= props.getProperty("pg1_cp2N", "") %> /></td>
						</tr>
						<tr>
							<td>3.</td>
							<td nowrap><a href=#
								onClick='popupPage("<%=resource%>c3p1");return false;'>Smoking</a>
							<font size=1> 
								<select name="pg1_box3">
									<option value="">Select</option>
									<option value="LESS10">&lt;10</option>
									<option value="UP20">10-20</option>
									<option value="OVER20">&gt;20</option>
								</select>								
							</font>
							</td>
							<td><input type="checkbox" name="pg1_cp3"
								<%= props.getProperty("pg1_cp3", "") %>></td>
							<td><input type="checkbox" name="pg1_cp3N"
								<%= props.getProperty("pg1_cp3N", "") %>></td>
						</tr>
						<tr>
							<td>4.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c4p1");return false;'>Alcohol,
							street drugs</a></td>
							<td><input type="checkbox" name="pg1_cp4"
								<%= props.getProperty("pg1_cp4", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp4N"
								<%= props.getProperty("pg1_cp4N", "") %> /></td>
						</tr>
						
						<tr>
							<td valign="top">5.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c8p1");return false;'>Occup/Environ.
							risks</a></td>
							<td><input type="checkbox" name="pg1_cp8"
								<%= props.getProperty("pg1_cp8", "") %> /></td>
							<td><input type="checkbox" name="pg1_cp8N"
								<%= props.getProperty("pg1_cp8N", "") %> /></td>
						</tr>
						<tr>
							<td valign="top">6.</td>
							<td>Dietary restrictions</td>
							<td><input type="checkbox" name="pg1_naDiet"
								<%= props.getProperty("pg1_naDiet", "") %>></td>
							<td><input type="checkbox" name="pg1_naDietN"
								<%= props.getProperty("pg1_naDietN", "") %>></td>
						</tr>
						<tr>
							<td valign="top">7.</td>
							<td>Calcium adequate</td>
							<td><input type="checkbox" name="pg1_naMilk"
								<%= props.getProperty("pg1_naMilk", "") %>></td>
							<td><input type="checkbox" name="pg1_naMilkN"
								<%= props.getProperty("pg1_naMilkN", "") %>></td>
						</tr>
						<tr>
							<td valign="top">8.</td>
							<td>Preconceptual folate</td>
							<td><input type="checkbox" name="pg1_naFolic"
								<%= props.getProperty("pg1_naFolic", "") %>></td>
							<td><input type="checkbox" name="pg1_naFolicN"
								<%= props.getProperty("pg1_naFolicN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" nowrap><b>Medical History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>9.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c9p1");return false;'>Hypertension</a></td>
							<td><input type="checkbox" name="pg1_yes9"
								<%= props.getProperty("pg1_yes9", "") %>></td>
							<td><input type="checkbox" name="pg1_no9"
								<%= props.getProperty("pg1_no9", "") %>></td>
						</tr>
						<tr>
							<td>10.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c10p1");return false;'>Endocrine</a></td>
							<td><input type="checkbox" name="pg1_yes10"
								<%= props.getProperty("pg1_yes10", "") %>></td>
							<td><input type="checkbox" name="pg1_no10"
								<%= props.getProperty("pg1_no10", "") %>></td>
						</tr>
						
						<tr>
							<td>11.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c12p1");return false;'>Urinary
							tract</a></td>
							<td><input type="checkbox" name="pg1_yes12"
								<%= props.getProperty("pg1_yes12", "") %>></td>
							<td><input type="checkbox" name="pg1_no12"
								<%= props.getProperty("pg1_no12", "") %>></td>
						</tr>
						<tr>
							<td>12.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c13p1");return false;'>Cardiac/Pulmonary</a></td>
							<td><input type="checkbox" name="pg1_yes13"
								<%= props.getProperty("pg1_yes13", "") %>></td>
							<td><input type="checkbox" name="pg1_no13"
								<%= props.getProperty("pg1_no13", "") %>></td>
						</tr>
						<tr>
							<td>13.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c14p1");return false;'>Liver,
							hepatitis, GI</a></td>
							<td><input type="checkbox" name="pg1_yes14"
								<%= props.getProperty("pg1_yes14", "") %>></td>
							<td><input type="checkbox" name="pg1_no14"
								<%= props.getProperty("pg1_no14", "") %>></td>
						</tr>
						<tr>
							<td>14.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c17p1");return false;'>Gynaecology/Breast</a></td>
							<td><input type="checkbox" name="pg1_yes17"
								<%= props.getProperty("pg1_yes17", "") %>></td>
							<td><input type="checkbox" name="pg1_no17"
								<%= props.getProperty("pg1_no17", "") %>></td>
						</tr>
						<tr>
							<td>15.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c22p1");return false;'>Hem./Immunology</a></td>
							<td><input type="checkbox" name="pg1_yes22"
								<%= props.getProperty("pg1_yes22", "") %>></td>
							<td><input type="checkbox" name="pg1_no22"
								<%= props.getProperty("pg1_no22", "") %>></td>
						</tr>
						<tr>
							<td>16.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c20p1");return false;'>Surgeries</a></td>
							<td><input type="checkbox" name="pg1_yes20"
								<%= props.getProperty("pg1_yes20", "") %>></td>
							<td><input type="checkbox" name="pg1_no20"
								<%= props.getProperty("pg1_no20", "") %>></td>
						</tr>
						<tr>
							<td>17.</td>
							<td>Blood transfusion</td>
							<td><input type="checkbox" name="pg1_bloodTranY"
								<%= props.getProperty("pg1_bloodTranY", "") %>></td>
							<td><input type="checkbox" name="pg1_bloodTranN"
								<%= props.getProperty("pg1_bloodTranN", "") %>></td>
						</tr>
						<tr>
							<td>18.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c21p1");return false;'>Anesthetics
							compl.</a></td>
							<td><input type="checkbox" name="pg1_yes21"
								<%= props.getProperty("pg1_yes21", "") %>></td>
							<td><input type="checkbox" name="pg1_no21"
								<%= props.getProperty("pg1_no21", "") %>></td>
						</tr>
						<tr>
							<td>19.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c24p1");return false;'>Psychiatric</a></td>
							<td><input type="checkbox" name="pg1_yes24"
								<%= props.getProperty("pg1_yes24", "") %>></td>
							<td><input type="checkbox" name="pg1_no24"
								<%= props.getProperty("pg1_no24", "") %>></td>
						</tr>
						<tr>
							<td>20.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c15p1");return false;'>Epilepsy/Neurological</a></td>
							<td><input type="checkbox" name="pg1_yes15"
								<%= props.getProperty("pg1_yes15", "") %>></td>
							<td><input type="checkbox" name="pg1_no15"
								<%= props.getProperty("pg1_no15", "") %>></td>
						</tr>
						<tr>
							<td>21.</td>
							<td>Other <input type="text" name="pg1_box25" size="8"
								maxlength="25"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box25", "")) %>">
							</td>
							<td><input type="checkbox" name="pg1_yes25"
								<%= props.getProperty("pg1_yes25", "") %>></td>
							<td><input type="checkbox" name="pg1_no25"
								<%= props.getProperty("pg1_no25", "") %>></td>
						</tr>
					</table>


					</td>
					<td valign="top" width="35%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Genetic History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>22.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c27p1");return false;'>At
							risk population</a></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_yes27" <%= props.getProperty("pg1_yes27", "") %>></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_no27" <%= props.getProperty("pg1_no27", "") %>></td>
						</tr>
						<tr>
							<td colspan="4"><span class="small">(e.g.:Ashkenazi,
							consanguinity, CF, sickle cell,Tay Sachs,thalassemia)</span></td>
						</tr>
						<tr>
							<td colspan="4" nowrap><u>Family history of:</u></td>
						</tr>
						<tr>
							<td>23.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c31p1");return false;'>Developmental
							delay</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes31"
								<%= props.getProperty("pg1_yes31", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no31"
								<%= props.getProperty("pg1_no31", "") %>></td>
						</tr>
						<tr>
							<td>24.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c32p1");return false;'>Congenital
							anomalies</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes32"
								<%= props.getProperty("pg1_yes32", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no32"
								<%= props.getProperty("pg1_no32", "") %>></td>
						</tr>
						
						<tr>
							<td>25.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c34p1");return false;'>Chromosomal
							disorders</a></td>
							<td align="center"><input type="checkbox" name="pg1_yes34"
								<%= props.getProperty("pg1_yes34", "") %>></td>
							<td align="center"><input type="checkbox" name="pg1_no34"
								<%= props.getProperty("pg1_no34", "") %>></td>
						</tr>
						<tr>
							<td>26.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c35p1");return false;'>Genetic
							disorders</a></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_yes35" <%= props.getProperty("pg1_yes35", "") %>></td>
							<td align="center" valign="top"><input type="checkbox"
								name="pg1_no35" <%= props.getProperty("pg1_no35", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Infectious Disease</b></td>
						</tr>
						<tr>
							<td>27.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c40p1");return false;'>Varicella
							susceptible</a></td>
							<td><input type="checkbox" name="pg1_idt40"
								<%= props.getProperty("pg1_idt40", "") %>></td>
							<td><input type="checkbox" name="pg1_idt40N"
								<%= props.getProperty("pg1_idt40N", "") %>></td>
						</tr>
						<tr>
							<td>28.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c38p1");return false;'>STDs/HSV/BV</a></td>
							<td><input type="checkbox" name="pg1_idt38"
								<%= props.getProperty("pg1_idt38", "") %>></td>
							<td><input type="checkbox" name="pg1_idt38N"
								<%= props.getProperty("pg1_idt38N", "") %>></td>
						</tr>
						<tr>
							<td>29.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c42p1");return false;'>Tuberculosis
							risk</a></td>
							<!--  input type="text" name="pg1_box42" size="10" maxlength="20" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_box42", "")) %>"></td>-->
							<td><input type="checkbox" name="pg1_idt42"
								<%= props.getProperty("pg1_idt42", "") %>></td>
							<td><input type="checkbox" name="pg1_idt42N"
								<%= props.getProperty("pg1_idt42N", "") %>></td>
						</tr>
						<tr>
							<td>30.</td>
							<td>Other <input type="text" name="pg1_infectDisOther"
								size="10" maxlength="20"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_infectDisOther", "")) %>"></td>
							<td><input type="checkbox" name="pg1_infectDisOtherY"
								<%= props.getProperty("pg1_infectDisOtherY", "") %>></td>
							<td><input type="checkbox" name="pg1_infectDisOtherN"
								<%= props.getProperty("pg1_infectDisOtherN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Psychosocial</b></td>
						</tr>
						<tr>
							<td>31.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c43p1");return false;'>Poor
							social support</a></td>
							<td><input type="checkbox" name="pg1_pdt43"
								<%= props.getProperty("pg1_pdt43", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt43N"
								<%= props.getProperty("pg1_pdt43N", "") %>></td>
						</tr>
						<tr>
							<td>32.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c44p1");return false;'>Relationship
							problems</a></td>
							<td><input type="checkbox" name="pg1_pdt44"
								<%= props.getProperty("pg1_pdt44", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt44N"
								<%= props.getProperty("pg1_pdt44N", "") %>></td>
						</tr>
						<tr>
							<td>33.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c45p1");return false;'>Emotional/Depression</a></td>
							<td><input type="checkbox" name="pg1_pdt45"
								<%= props.getProperty("pg1_pdt45", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt45N"
								<%= props.getProperty("pg1_pdt45N", "") %>></td>
						</tr>
						<tr>
							<td>34.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c46p1");return false;'>Substance
							abuse</a></td>
							<td><input type="checkbox" name="pg1_pdt46"
								<%= props.getProperty("pg1_pdt46", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt46N"
								<%= props.getProperty("pg1_pdt46N", "") %>></td>
						</tr>
						<tr>
							<td>35.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c47p1");return false;'>Family
							violence</a></td>
							<td><input type="checkbox" name="pg1_pdt47"
								<%= props.getProperty("pg1_pdt47", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt47N"
								<%= props.getProperty("pg1_pdt47N", "") %>></td>
						</tr>
						<tr>
							<td>36.</td>
							<td><a href=#
								onClick='popupPage("<%=resource%>c48p1");return false;'>Parenting
							concerns</a></td>
							<td><input type="checkbox" name="pg1_pdt48"
								<%= props.getProperty("pg1_pdt48", "") %>></td>
							<td><input type="checkbox" name="pg1_pdt48N"
								<%= props.getProperty("pg1_pdt48N", "") %>></td>
						</tr>
						<tr>
							<td>37.</td>
							<td>Relig./Cultural issues</td>
							<td><input type="checkbox" name="pg1_reliCultY"
								<%= props.getProperty("pg1_reliCultY", "") %>></td>
							<td><input type="checkbox" name="pg1_reliCultN"
								<%= props.getProperty("pg1_reliCultN", "") %>></td>
						</tr>
					</table>

					</td>
					<td valign="top">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>Family History</b></td>
							<td align="center" width="12%">
							<div align="right">Yes</div>
							</td>
							<td align="center" nowrap width="12%">
							<div align="right">No</div>
							</td>
						</tr>
						<tr>
							<td>38.</td>
							<td>At risk population</td>
							<td><input type="checkbox" name="pg1_fhRiskY"
								<%= props.getProperty("pg1_fhRiskY", "") %>></td>
							<td><input type="checkbox" name="pg1_fhRiskN"
								<%= props.getProperty("pg1_fhRiskN", "") %>></td>
						</tr>
						<tr>
							<td colspan="4">(e.g.: DM, DVT/PE, PIH/HT,</br>
							postpartum depression, thyroid)</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" nowrap><b>Physical Examination</b></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">Ht.<input type="text" name="pg1_ht"
								onDblClick="htEnglish2Metric(this);" size="5" maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_ht", "")) %>" />
								<br/>
							Wt.<input type="text" name="pg1_wt"
								onDblClick="wtEnglish2Metric(this);" size="5" maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_wt", "")) %>" />
							</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">BMI<input type="text" name="c_bmi"
								class="spe" onDblClick="calcBMIMetric(this);" size="6"
								maxlength="6"
								value="<%= UtilMisc.htmlEscape(props.getProperty("c_bmi", "")) %>">
							<br/>BP<input type="text" name="pg1_BP" size="6" maxlength="10"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_BP", "")) %>">
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td align="center" width="12%">N</td>
							<td align="center" nowrap width="12%">Abn</td>
						</tr>
						<tr>
							<td>39.</td>
							<td>Thyroid</td>
							<td><input type="checkbox" name="pg1_thyroid"
								<%= props.getProperty("pg1_thyroid", "") %>></td>
							<td><input type="checkbox" name="pg1_thyroidA"
								<%= props.getProperty("pg1_thyroidA", "") %>></td>
						</tr>
						<tr>
							<td>40.</td>
							<td>Chest</td>
							<td><input type="checkbox" name="pg1_chest"
								<%= props.getProperty("pg1_chest", "") %>></td>
							<td><input type="checkbox" name="pg1_chestA"
								<%= props.getProperty("pg1_chestA", "") %>></td>
						</tr>
						<tr>
							<td>41.</td>
							<td>Breasts</td>
							<td><input type="checkbox" name="pg1_breasts"
								<%= props.getProperty("pg1_breasts", "") %>></td>
							<td><input type="checkbox" name="pg1_breastsA"
								<%= props.getProperty("pg1_breastsA", "") %>></td>
						</tr>
						<tr>
							<td>42.</td>
							<td>Cardiovascular</td>
							<td><input type="checkbox" name="pg1_cardio"
								<%= props.getProperty("pg1_cardio", "") %>></td>
							<td><input type="checkbox" name="pg1_cardioA"
								<%= props.getProperty("pg1_cardioA", "") %>></td>
						</tr>
						<tr>
							<td>43.</td>
							<td>Abdomen</td>
							<td><input type="checkbox" name="pg1_abdomen"
								<%= props.getProperty("pg1_abdomen", "") %>></td>
							<td><input type="checkbox" name="pg1_abdomenA"
								<%= props.getProperty("pg1_abdomenA", "") %>></td>
						</tr>
						<tr>
							<td>44.</td>
							<td>Varicosities / Extrm.</td>
							<td><input type="checkbox" name="pg1_vari"
								<%= props.getProperty("pg1_vari", "") %>></td>
							<td><input type="checkbox" name="pg1_variA"
								<%= props.getProperty("pg1_variA", "") %>></td>
						</tr>

						<tr>
							<td>45.</td>
							<td>External genitalia</td>
							<td><input type="checkbox" name="pg1_extGen"
								<%= props.getProperty("pg1_extGen", "") %>></td>
							<td><input type="checkbox" name="pg1_extGenA"
								<%= props.getProperty("pg1_extGenA", "") %>></td>
						</tr>
						<tr>
							<td>46.</td>
							<td>Cervix, vagina</td>
							<td><input type="checkbox" name="pg1_cervix"
								<%= props.getProperty("pg1_cervix", "") %>></td>
							<td><input type="checkbox" name="pg1_cervixA"
								<%= props.getProperty("pg1_cervixA", "") %>></td>
						</tr>
						<tr>
							<td>47.</td>
							<td>Uterus</td>
							<td><input type="checkbox" name="pg1_uterus"
								<%= props.getProperty("pg1_uterus", "") %>></td>
							<td><input type="checkbox" name="pg1_uterusA"
								<%= props.getProperty("pg1_uterusA", "") %>></td>
						</tr>
						<tr>
							<td>48.</td>
							<td nowrap="nowrap">Size: <input type="text" name="pg1_uterusBox" size="3"
								maxlength="3"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_uterusBox", "")) %>">
							<span class="small"> weeks</span></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>49.</td>
							<td>Adnexa</td>
							<td><input type="checkbox" name="pg1_adnexa"
								<%= props.getProperty("pg1_adnexa", "") %>></td>
							<td><input type="checkbox" name="pg1_adnexaA"
								<%= props.getProperty("pg1_adnexaA", "") %>></td>
						</tr>
						<tr>
							<td>50.</td>
							<td>Other <input type="text" name="pg1_pExOtherDesc" size="6"
								maxlength="20"
								value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_pExOtherDesc", "")) %>"></td>
							<td><input type="checkbox" name="pg1_pExOther"
								<%= props.getProperty("pg1_pExOther", "") %>></td>
							<td><input type="checkbox" name="pg1_pExOtherA"
								<%= props.getProperty("pg1_pExOtherA", "") %>></td>
						</tr>
					</table>

					</td>
				</tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="4" align="center" bgcolor="#CCCCCC"><b><font
						face="Verdana, Arial, Helvetica, sans-serif"> Initial
					Laboratory Investigations</font></b></td>
				</tr>
				<tr>
					<td width="30%"><b>Test</b></td>
					<td width="20%"><b>Result</b></td>
					<td width="30%"><b>Test</b></td>
					<td><b>Result</b></td>
				</tr>
				<tr>
					<td>Hb</td>
					<td><input type="text" name="pg1_labHb" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHb", "")) %>">
					</td>
					<td>HIV</td>
					<td>
						<select name="pg1_labHIV">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>					
					</td>
				</tr>
				<tr>
					<td>MCV</td>
					<td><input type="text" name="pg1_labMCV" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labMCV", "")) %>">
					</td>
					<td colspan="2"><input type="checkbox"
						name="pg1_labHIVCounsel"
						<%= props.getProperty("pg1_labHIVCounsel", "") %>>
					Counseled and test declined</td>
				</tr>
				<tr>
					<td>ABO</td>
					<td>
						<select name="pg1_labABO">
							<option value="NDONE">Not Done</option>
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="AB">AB</option>
							<option value="O">O</option>
							<option value="UNK">Unknown</option>
						</select>						
					</td>
					<td valign="top" rowspan="2" nowrap="nowrap">Last Pap  <br/>
					<input type="text" name="pg1_labLastPapDate"
						id="pg1_labLastPapDate" size="10" maxlength="10"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labLastPapDate", "")) %>">
						<img
						src="../images/cal.gif" id="pg1_labLastPapDate_cal">
					</td>
					<td valign="top" rowspan="2"><input type="text"
						name="pg1_labLastPap" size="10" maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labLastPap", "")) %>">
					</td>
				</tr>
				<tr>
					<td>Rh</td>
					<td>
						<select name="pg1_labRh">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>	
					
					</td>
				</tr>
				<tr>
					<td>Antibody Screen</td>
					<td><input type="text" name="pg1_labAntiScr" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labAntiScr", "")) %>">
					</td>
					<td>GC</td>
					<td>
						<select name="pg1_labGC">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>						
					</td>
				</tr>
				<tr>
					<td>Rubella immune</td>
					<td>
						<select name="pg1_labRubella">
							<option value="NDONE">Not Done</option>
							<option value="Non-Immune">Non-Immune</option>
							<option value="Immune">Immune </option>
							<option value="Indeterminate">Indeterminate</option>
						</select>
										
					</td>
					<td>Chlamydia</td>
					<td>
						<select name="pg1_labChlamydia">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>						
					</td>					
				</tr>
				<tr>
					<td>HBsAg</td>
					<td>
						<select name="pg1_labHBsAg">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
					</td>
					<td>Urine C&S</td>
					<td><input type="text" name="pg1_labUrine" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labUrine", "")) %>">
					</td>
				</tr>
				<tr>
					<td>VDRL</td>
					<td>
						<select name="pg1_labVDRL">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>					
					</td>
					<td><input type="text" size="10" name="pg1_labCustom1Label" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom1Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="pg1_labCustom1Result" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom1Result", "")) %>"/></td>				
				</tr>
				<tr>
					<td>Sickle Cell</td>
					<td>
						<select name="pg1_labSickle">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>
					
					</td>
					<td><input type="text" size="10" name="pg1_labCustom2Label" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom2Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="pg1_labCustom2Result" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom2Result", "")) %>"/></td>					
				</tr>				
			</table>
			<br/>
			
			
			<br>
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th><u>Prenatal Genetic Investigations</u></th>
					<th>Result</th>
				</tr>
				<tr>
					<td>a) All ages-<a href=#
						onClick='popupPage("<%=resource%>c37p1");return false;'>MSS</a>,
					IPS, FTS</td>
					<td><input type="text" name="pg1_geneticA" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticA", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td><a href=#
						onClick='popupPage("<%=resource%>c26p1");return false;'>b) Age
					&gt;= 35 at EDB-CVS/amnio</a></td>
					<td><input type="text" name="pg1_geneticB" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticB", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td>c) If a or b declined, or twins, then MSAFP</td>
					<td><input type="text" name="pg1_geneticC" size="10"
						maxlength="20"
						value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_geneticC", "")) %>"></td>
					</td>
				</tr>
				<tr>
					<td><input type="text" size="10" name="pg1_labCustom3Label" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom3Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="pg1_labCustom3Result" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom3Result", "")) %>"/></td>					
				</tr>
				<tr>
					<td><input type="text" size="10" name="pg1_labCustom4Label" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom4Label", "")) %>"/></td>
					<td><input type="text"  size="10" name="pg1_labCustom4Result" value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_labCustom4Result", "")) %>"/></td>					
				</tr>
				<tr>
					<td>d) Counseled and test declined, or too late</td>
					<td align="center"><input type="checkbox" name="pg1_geneticD"
						<%= props.getProperty("pg1_geneticD", "") %>></td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr bgcolor="#CCCCCC">
			<th colspan="4"><b>Comments</b></th>
		</tr>
		<tr>
			<td colspan="4"><textarea name="pg1_commentsAR1"
				style="width: 100%" cols="80" rows="5"><%= props.getProperty("pg1_commentsAR1", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td width="30%">Signature<br>
			<input type="text" name="pg1_signature" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_signature", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg1_formDate" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg1_formDate", "") %>"></td>
			<td width="30%">Signature<br>
			<input type="text" name="pg1_signature2" size="30" maxlength="50"
				style="width: 80%"
				value="<%= UtilMisc.htmlEscape(props.getProperty("pg1_signature2", "")) %>">
			</td>
			<td width="20%">Date (yyyy/mm/dd)<br>
			<input type="text" name="pg1_formDate2" class="spe"
				onDblClick="calToday(this)" size="10" maxlength="10"
				style="width: 80%"
				value="<%= props.getProperty("pg1_formDate2", "") %>"></td>
		</tr>

	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>
			<td><a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AnteNatal','LabReq');">LAB</a>
			</td>
			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formonarenhancedpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			</a> </td>
			<td align="right"><b>Edit:</b> 
			<a
				href="javascript:void(0)" onclick="onPageChange('formonarenhancedpg2.jsp?demographic_no=<%=demoNo%>&formId=#id&provNo=<%=provNo%>');">AR2</a> </td>
			<%
  }
%>
		</tr>
	</table>

</html:form>
</div>
</div>
</body>

<script type="text/javascript">
Calendar.setup({ inputField : "pg1_menLMP", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_menLMP_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_lastUsed", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_lastUsed_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_menEDB", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_menEDB_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "c_finalEDB", ifFormat : "%Y/%m/%d", showsTime :false, button : "c_finalEDB_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_labLastPapDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "pg1_labLastPapDate_cal", singleClick : true, step : 1 });

</script>
</html:html>

<%!
	String getSelected(String a, String b) {
		if(a.trim().equalsIgnoreCase(b.trim())) {
			return " selected=\"selected\" ";
		}
		return "";
	}
%>
