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

<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page
	import="oscar.oscarEncounter.data.*,java.net.*,oscar.oscarPrevention.*,java.util.*,oscar.util.*,org.apache.commons.lang.*"%>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<html:html>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Cumulative Patient Profile</title>
<link rel="stylesheet" type="text/css" media="print" href="print.css" />
<link rel="stylesheet" type="text/css" href="encounterPrintStyles.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/wz_dragdrop.js"></script>

<%
    //The oscarEncounter session manager, if the session bean is not in the context it looks for a session cookie with the appropriate name and value, if the required cookie is not available
    //it dumps you out to an erros page.

  oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
  if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
    response.sendRedirect("error.jsp");
    return;
  }

  String provNo = (String) request.getSession().getAttribute("user");
    CPPData cppData = new CPPData();
    HashMap h = cppData.getCPPData(bean.demographicNo);

    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));

    org.oscarehr.common.model.Allergy[] allergies
    = RxPatientData.getPatient(Integer.parseInt(bean.demographicNo)).getAllergies();

    ArrayList inject = new ArrayList();

      PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
      ArrayList<HashMap<String,String>> prevList  = pdc.getPreventions();
      for (int k =0 ; k < prevList.size(); k++){
             HashMap<String,String> a = prevList.get(k);
             if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
                inject.add(a.get("name"));
             }
      }





    List medsList = (List) h.get("medsList");
    List allergyList = (List) h.get("allergyList");
    List divList = (List) h.get("divList");
%>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript" language="Javascript">
    var dirty = false;
    var hiddenPres = new Array();
    var hiddenAllergy = new Array();

    var isIE = document.all?true:false;
    if (!isIE) document.captureEvents(Event.MOUSEMOVE);

    var enc = new Array();
    enc[0] = "<%=StringEscapeUtils.escapeJavaScript(bean.socialHistory)%>"
    enc[1] = "<%=StringEscapeUtils.escapeJavaScript(bean.ongoingConcerns)%>"
    enc[2] = "<%=StringEscapeUtils.escapeJavaScript(bean.medicalHistory)%>"
    enc[3] = "<%=StringEscapeUtils.escapeJavaScript(bean.reminders)%>"





    function onPrint() {
        window.print();
        return true;
    }
    function onClose() {
        window.close();
        return true;
    }
    function setDirtyTrue(){
       dirty = true;
    }
    function setDirtyFalse(){
       dirty = false;
    }
    function addToHiddenPres(id){
        hiddenPres[hiddenPres.length] = id;
    }
    function addToHiddenAllergy(id){
        hiddenAllergy[hiddenAllergy.length] = id;
    }

    function fillMedAllerg(){
        listArray(hiddenPres,'medsList');
        listArray(hiddenAllergy,'allergyList');
        getCheckedStr('divArr');
        //alert(document.getElementById('medsList').value+"\n"+document.getElementById('allergyList').value+"\n"+document.getElementById('divArr').value);
        //document.FrmForm.submit();
        document.forms[0].submit();
    }

    function listArray(arr,id){
      var listStr = "";
      for(var i = 0 ;i < arr.length ; i++){
         //alert(arr[i]);
         listStr = listStr + arr[i];
         if (i != (arr.length - 1 ) ){
            listStr = listStr + ",";
         }
      }
      document.getElementById(id).value = listStr;
      //alert(document.getElementById('medsList').value);
    }

</script>

<script type="text/javascript">

function processEditable(editableText,editId){
   var textVal = document.getElementById(editableText);
   //alert(editId);
   var idName = document.getElementById(editId);
   //alert(idName.value);
   //alert(idName.value+"Val"+"    "+idName.value+"   "+textVal.value);
   document.getElementById(idName.value+"Val").value = textVal.value;
   var conDiv = document.getElementById(idName.value);

   while (conDiv.hasChildNodes()){
        conDiv.removeChild(conDiv.firstChild);
     }

   var newText = document.createTextNode(textVal.value);
	var newNode = document.createElement("pre");
   newNode.appendChild(newText);
			conDiv.appendChild(newNode);

   //alert(document.getElementById(idName.value).innerHtml);
}


function checkTheBox(e){
 var targ;
	if (!e) var e = window.event;
	if (e.target) targ = e.target;
	else if (e.srcElement) targ = e.srcElement;
	if (targ.nodeType == 3) // defeat Safari bug
		targ = targ.parentNode;

		//alert(targ.name+" "+targ.checked);

		if ( targ.checked){
		   showItem(targ.name);
		}else{
        hideItem(targ.name);
		}


}

function getTarget(e){
var targ;
	if (!e) var e = window.event;
	if (e.target) targ = e.target;
	else if (e.srcElement) targ = e.srcElement;
	if (targ.nodeType == 3) // defeat Safari bug
		targ = targ.parentNode;

   return targ;
}

function showElementHere(e,id){
   //alert(dirty);
   if (dirty){
      alert("You Must press Finish or Cancel");
      return false;
   }
    var targ;
	if (!e) var e = window.event;
	if (e.target) targ = e.target;
	else if (e.srcElement) targ = e.srcElement;
	if (targ.nodeType == 3) // defeat Safari bug
		targ = targ.parentNode;

    var ele = document.getElementById(id);
    var _x = 300;
    var _y = 300;
    if (!isIE) {
       _x = e.pageX;
       _y = e.pageY;
    }

    //alert(_y);
    ele.style.top = _y;
    ele.style.left = _x;

    //alert(targ.nodeName);

    if ( targ.nodeName == "DIV"){
       targ = targ.firstChild ;
    }

    var str = targ.innerHTML

    if (-1 != str.lastIndexOf("&nbsp;")){
       var atTheLast = str.length - str.lastIndexOf("&nbsp;") ;
       if (atTheLast == 6){
       str = str.substr(0,str.lastIndexOf("&nbsp;"));
       //alert(s);
       }

    }
    if (targ.id == ""){
        //alert("targ now == parent");
        targ = targ.parentNode;
    }else{
        //alert("It doesn't");
    }
    //alert(id+"  >"+targ.id+"<");
    document.getElementById(id+"Text").value = str;
    document.getElementById('editableVal').value = targ.id;

    showItem(id);
}

function showEdit(e,id,dataId){
   var _x = 100;
   var _y = 200;
   //alert(dirty);
   if (dirty){
      alert("You must press Finish or Cancel");
      return false;
   }
   //alert(dataId);
   var targ = document.getElementById(dataId);
   var ele = document.getElementById(id);
   ele.style.top = _y;
   ele.style.left = _x;

   //alert(targ.id);

   var str = "";
   if ( targ.nodeName == "DIV"){
       kids = targ.childNodes;
       for (var i = 0; i < kids.length; i++) {
     //    alert(i+" "+kids[i].nodeName);
         if (kids[i].nodeName == "PRE"){
            str = kids[i].innerHTML;
         }
       }

    }


   //alert(targ.firstChild);
   //var str = targ.firstChild.innerHTML;

   if (-1 != str.lastIndexOf("&nbsp;")){
      var atTheLast = str.length - str.lastIndexOf("&nbsp;") ;
      if (atTheLast == 6){
         str = str.substr(0,str.lastIndexOf("&nbsp;"));
         //alert(s);
      }
    }

    document.getElementById(id+"Text").value = str;
    document.getElementById('editableVal').value = dataId;

    showItem(id);
}




function setChecked(id){
   //alert(id);
   var ele = document.getElementById(id);

   if(ele.style.display == 'none'){
       document.getElementById(id+"Check").checked = false;
   }else{
       document.getElementById(id+"Check").checked = true;
   }

}

function getCheck(id){
    return document.getElementById(id+"Check").checked;
}


function getCheckedStr(id){
    var checkStr = "";
    var divArr = new Array('socialFam','ongoingCon','medHist','reminder','riskfactor','presBox','otherMed','allergyBox','otherAller','imms');
    for (i=0; i<divArr.length; i++){
        //alert(divArr[i]+"  "+getCheck(divArr[i]) );
        if(!getCheck(divArr[i])){

            if(checkStr.length == 0){
                checkStr = checkStr + divArr[i];
            }else{
                checkStr = checkStr +","+divArr[i];
            }
        }
        //alert(checkStr);
    }
    document.getElementById(id).value =  checkStr;
}


function setCheckedAll(){
    setChecked('socialFam');
    setChecked('ongoingCon');
    setChecked('medHist');
    setChecked('reminder');
    setChecked('riskfactor');
    setChecked('presBox');
    setChecked('otherMed');
    setChecked('allergyBox');
    setChecked('otherAller');
    setChecked('imms');
}




</script>

</head>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors />
<div style="width: 7in">

<div class="Header"><input type="button"
	value="<bean:message key="global.btnPrint"/>"
	onclick="javascript:return onPrint();" style="margin-left: 2px;" /> <input
	type="button" value="<bean:message key="global.btnClose"/>"
	onclick="javascript:return onClose();" /> <input type="button"
	value="Save Changes" onclick="javascript:fillMedAllerg();" /> <input
	type="button" value="Options"
	onclick="javascript:showHideItem('optionss');" /></div>

<div style="width: 100%;">
<div style="width: 100%; clear: left;"><span
	style="float: left; font-weight: bold; margin-top: 4pt;"><%=bean.patientLastName %>,
<%=bean.patientFirstName%> <%=bean.patientSex%> <%=bean.patientAge%></span> <span
	style="float: right; font-weight: bold; margin-top: 4pt;"><bean:message
	key="oscarEncounter.encounterPrint.msgDr" />. <%=providerBean.getProperty(bean.familyDoctorNo)%></span>
</div>
<br />

<div style="width: 100%; clear: left;">

<div
	style="position:relative;float:left; margin-left:5pt;margin-top:5pt;<%=displayNone("socialFam",divList)%>"
	id="socialFam">
<div class="RowTop" style="white-space: nowrap;"><bean:message
	key="oscarEncounter.Index.socialFamHist" />: <!--a class="hideShow" onclick="javascript:showHideItem('socialFam');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','socialFamText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder" id="socialFamText"
	ondblclick="javascript:showElementHere(event,'editable');"
	style="min-width: 200px;"><pre name='shTextarea'><%=hashval(h.get("socialFam"))%><%=""/*bean.socialHistory*/%>&nbsp;</pre>
</div>
</div>

<div
	style="position:relative;float:left;margin-left:5pt;margin-top:5pt;<%=displayNone("ongoingCon",divList)%>"
	id="ongoingCon">
<div class="RowTop" style="white-space: nowrap;"><bean:message
	key="oscarEncounter.encounterPrint.msgOngCon" />:aka Problem List <!--a class="hideShow" onclick="javascript:showHideItem('ongoingCon');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','ongoingConText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="ongoingConText" style="min-width: 200px;"><pre
	name='ocTextarea'><%=hashval(h.get("ongoingCon"))%><%=""/*bean.ongoingConcerns*/%>&nbsp;</pre>
</div>
</div>

<div
	style="position:relative;float:left; margin-left:5pt;margin-top:5pt;<%=displayNone("medHist",divList)%>"
	id="medHist">
<div class="RowTop" style="white-space: nowrap;"><bean:message
	key="oscarEncounter.Index.medHist" />: <!--a class="hideShow" onclick="javascript:showHideItem('medHist');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','medHistText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="medHistText" style="min-width: 200px;"><pre name='mhTextarea'><%=hashval(h.get("medHist"))%><%=""/*bean.medicalHistory*/%>&nbsp;</pre>
</div>
</div>

<div
	style="position:relative;float:left;margin-left:5pt;margin-top:5pt;<%=displayNone("reminder",divList)%>"
	id="reminder">
<div class="RowTop" style="white-space: nowrap;"><bean:message
	key="oscarEncounter.encounterPrint.msgReminders" />: aka Med alerts and
special needs <!--a class="hideShow" onclick="javascript:showHideItem('reminder');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','reminderText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="reminderText" style="min-width: 200px;"><pre
	name='reTextarea'><%=hashval(h.get("reminder"))%><%=""/*bean.reminders*/%>&nbsp;</pre>
</div>
</div>

<div
	style="position:relative;float:left;margin-left:5pt;margin-top:5pt;<%=displayNone("riskfactor",divList)%>"
	" id="riskfactor">
<div class="RowTop" style="white-space: nowrap;">Risk Factors: <!--a class="hideShow" onclick="javascript:showHideItem('riskfactor');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','riskfactorText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="riskfactorText" style="min-width: 200px;"><pre
	name='reTextarea'><%=hashval(h.get("riskfactor"))%><%=""/*bean.reminders*/%>&nbsp;</pre>
</div>
</div>

</div>

<div
	style="margin-left:5pt;width:100%;clear:left;<%=displayNone("presBox",divList)%>"
	id="presBox">
<div class="RowTop">Medications <a class="hideShow"
	onclick="javascript:showHideItem('presBox');setCheckedAll();"
	href="javascript: function myFunction() {return false; }">hide</a></div>
<div class="presBox">
<div
	style=" float:left; margin-left:3pt;<%=displayNone("otherMed",divList)%> "
	id="otherMed">
<div class="RowTop"><bean:message
	key="oscarEncounter.Index.otherMed" />: <!--a class="hideShow" onclick="javascript:showHideItem('otherMed');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','otherMedText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="otherMedText" style="min-width: 200px;"><pre
	name='fhTextarea'><%=hashval(h.get("otherMed"))%><%=""/*bean.familyHistory*/%>&nbsp;</pre>
</div>
</div>
<br />
<ul>
	<%for (int i = 0; i < arr.length; i++){
                        String rxD = arr[i].getRxDate().toString();
                        String rxP = arr[i].getFullOutLine().replaceAll(";"," ");
                        rxP = rxP + "   " + arr[i].getEndDate();
                        String styleColor = "";
                        String id = ""+arr[i].getDrugId();
                        if(arr[i].isCurrent() == true){  styleColor="color:red;";  }
                    %>
	<li
		style="border-bottom: 1pt solid #888888; font-size:8pt;margin-top:1px; <%=styleColor%><%=displayNone(id,medsList)%>"
		id="pres<%=id%>"><a class="hideShow"
		onclick="javascript:showHideItem('pres<%=id%>');addToHiddenPres('<%=id%>');"
		href="javascript: function myFunction() {return false; }">hide</a> <%=rxD%>&nbsp;
	<%=rxP%></li>
	<%}%>
</ul>
</div>
</div>
<div
	style="margin-left:5pt;clear:left;<%=displayNone("allergyBox",divList)%>"
	id="allergyBox">
<div class="RowTop">Allergies <a class="hideShow"
	onclick="javascript:showHideItem('allergyBox');setCheckedAll();"
	href="javascript: function myFunction() {return false; }">hide</a></div>
<div class="presBox">
<div
	style=" float:left; margin-left:3pt;<%=displayNone("otherAller",divList)%>"
	id="otherAller">
<div class="RowTop">Other Allergies: <!--a class="hideShow" onclick="javascript:showHideItem('otherAller');setCheckedAll();" href="javascript: function myFunction() {return false; }" >hide</a-->
<a class="hideShow"
	onclick="javascript:showEdit(event,'editable','otherAllerText');"
	href="javascript: function myFunction() {return false; }">Edit</a></div>
<div class="TableWithBorder"
	ondblclick="javascript:showElementHere(event,'editable');"
	id="otherAllerText" style="min-width: 200px;"><pre
	name='fhTextarea'><%=hashval(h.get("otherAller"))%><%=""/*bean.familyHistory*/%>&nbsp;</pre>
</div>
</div>
<br />
<ul>
	<%for (int j=0; j<allergies.length; j++){%>
	<li id="allergy<%=allergies[j].getAllergyId()%>"
		style="<%=displayNone(""+allergies[j].getAllergyId(),allergyList)%>">
	<a class="hideShow"
		onclick="javascript:showHideItem('allergy<%=allergies[j].getAllergyId()%>');addToHiddenAllergy('<%=allergies[j].getAllergyId()%>');"
		href="javascript: function myFunction() {return false; }">hide</a> <b><%= allergies[j].getDescription() %></b>
	<!--%= allergies[j].getAllergy().getTypeDesc() %--> &nbsp;Severity: <%= org.oscarehr.common.model.Allergy.getSeverityOfReactionDesc(allergies[j].getSeverityOfReaction()) %>
	Onset: <%= org.oscarehr.common.model.Allergy.getOnSetOfReactionDesc(allergies[j].getOnsetOfReaction()) %>
	Reaction: <%= allergies[j].getReaction() %></li>
	<%}%>
</ul>
</div>
</div>

<div
	style="margin-left:5pt;width:100%;clear:left;<%=displayNone("imms",divList)%>"
	id="imms">
<div class="RowTop">Immunizations <a class="hideShow"
	onclick="javascript:showHideItem('imms');setCheckedAll();"
	href="javascript: function myFunction() {return false; }">hide</a></div>
<ul>
	<%for (int i = 0; i < inject.size(); i++ ){
                 ArrayList<Map<String,Object>> list = PreventionData.getPreventionData((String) inject.get(i),bean.demographicNo);
                 if ( list.size() > 0 ){%>
	<li id="imm<%=i%>"><a class="hideShow"
		onclick="javascript:showHideItem('imm<%=i%>')"
		href="javascript: function myFunction() {return false; }">hide</a> <b><%=(String) inject.get(i)%></b>

	<%for (int k =0 ; k < list.size(); k++){
							Map<String,Object> a = list.get(k);
                            if (a != null && inject.contains(a.get("type")) ){%>
	(<%=completeRefused(a.get("refused"))%>: <%=(String) a.get("prevention_date")%>)

	<%}
                       }
                    %>
	</li>
	<%}%>
	<%}%>
</ul>
</div>

</div>
<!--printing div-->

<div style="clear: left;">&nbsp;</div>
<div class="Header" style="width: 100%;"><input type="button"
	value="<bean:message key="global.btnPrint"/>"
	onclick="javascript:return onPrint();" style="margin-left: 2px;" /> <input
	type="button" value="<bean:message key="global.btnClose"/>"
	onclick="javascript:return onClose();" /></div>
</div>


<script type="text/javascript">
function getDataFromEnc(){
   var val = document.getElementById('encDataSelection').value;
   document.getElementById('encText').value = enc[val];
}

function putDataInTextArea(tarea) {
    var txtarea = document.getElementById(tarea);
    var ele = document.getElementById('editableText');
    var text = "";

    if(document.all) {
       text = document.selection.createRange().text;
    }else{
       var selLength = txtarea.textLength;
       var selStart = txtarea.selectionStart;
       var selEnd = txtarea.selectionEnd;
       //alert(selLength);
       //alert(selStart);
       //alert(selEnd);
       if ( selStart != selLength ){
          if (selEnd==1 || selEnd==2) selEnd=selLength;
          text = (txtarea.value).substring(selStart, selEnd);
       }
    }
    var eleVal = ele.value;
    ele.value = eleVal + text;
}

</script>

<div id="editable"
	style="position: absolute; left: 3.5in; top: 4in; border: 2px outset #ccccff; background: #ccccff; display: none;">
<div><select id="encDataSelection">
	<option value="0"><bean:message
		key="oscarEncounter.Index.socialFamHist" /></option>
	<option value="1"><bean:message
		key="oscarEncounter.encounterPrint.msgOngCon" /></option>
	<option value="2"><bean:message
		key="oscarEncounter.Index.medHist" /></option>
	<option value="3"><bean:message
		key="oscarEncounter.encounterPrint.msgReminders" /></option>
</select> <input type="button" value="Get Data"
	onclick="javascript: getDataFromEnc();" /></div>

<textarea cols="40" rows="20" id="editableText" wrap="hard"
	onchange="javascript: dirty = true;"></textarea> <input type="button"
	value="<<" onclick=" javascript:putDataInTextArea('encText')"/> <textarea
	cols="40" rows="20" id="encText" wrap="hard"></textarea> <br />
<input type="button" value="Finish"
	onclick="javascript:processEditable('editableText','editableVal');hideItem('editable');setDirtyFalse();" />
<input type="button" value="Cancel"
	onclick="javascript:hideItem('editable');setDirtyFalse();" /> <input
	type="hidden" id="editableVal" /></div>

<html:form action="oscarEncounter/SaveCPPAction">
	<input type="hidden" name="demoNo" value="<%=bean.demographicNo%>" />
	<input type="hidden" name="socialFamVal" id="socialFamTextVal"
		value="<%=hashval(h.get("socialFam"))%>" />
	<input type="hidden" name="ongoingConVal" id="ongoingConTextVal"
		value="<%=hashval(h.get("ongoingCon"))%>" />
	<input type="hidden" name="medHistVal" id="medHistTextVal"
		value="<%=hashval(h.get("medHist"))%>" />
	<input type="hidden" name="reminderVal" id="reminderTextVal"
		value="<%=hashval(h.get("reminder"))%>" />
	<input type="hidden" name="riskfactorVal" id="riskfactorTextVal"
		value="<%=hashval(h.get("riskfactor"))%>" />
	<input type="hidden" name="presBoxVal" id="presBoxTextVal" />
	<input type="hidden" name="otherMedVal" id="otherMedTextVal"
		value="<%=hashval(h.get("otherMed"))%>" />
	<input type="hidden" name="allergyBoxVal" id="allergyBoxTextVal" />
	<input type="hidden" name="otherAllerVal" id="otherAllerTextVal"
		value="<%=hashval(h.get("otherAller"))%>" />
	<input type="hidden" name="immsVal" id="immsTextVal" />

	<input type="hidden" name="medsList" id="medsList" />
	<input type="hidden" name="allegryList" id="allergyList" />
	<input type="hidden" name="divArr" id="divArr" />

</html:form>

<div id="optionss"
	style="position: absolute; left: 7.5in; top: 2px; border: 2pxoutset #ccccff; background: #ccccff; display: none;">
<html:form action="oscarEncounter/SaveCPPUserPrefAction">
	<ul>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="socialFam"
			id="socialFamCheck"><bean:message
			key="oscarEncounter.Index.socialFamHist" /></li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="ongoingCon"
			id="ongoingConCheck"><bean:message
			key="oscarEncounter.encounterPrint.msgOngCon" />:aka Problem List</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="medHist"
			id="medHistCheck"><bean:message
			key="oscarEncounter.Index.medHist" /></li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="reminder"
			id="reminderCheck"><bean:message
			key="oscarEncounter.encounterPrint.msgReminders" />: aka Med alerts
		and special needs</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="riskfactor"
			id="riskfactorCheck">Risk Factors</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="presBox"
			id="presBoxCheck">Medications</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="otherMed"
			id="otherMedCheck"><bean:message
			key="oscarEncounter.Index.otherMed" /></li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="allergyBox"
			id="allergyBoxCheck">Allergies</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="otherAller"
			id="otherAllerCheck">Other Allergies</li>
		<li><input type="checkbox"
			onchange="javascript:checkTheBox(event);" name="imms" id="immsCheck">Immunizations</li>
	</ul>
	<input type="submit" value="Save This as my Default View" />
</html:form></div>


<script type="text/javascript">
setCheckedAll();

<% if (medsList != null){
      for (int i =0; i < medsList.size();i++){
      String pres =  (String) medsList.get(i); %>
  addToHiddenPres('<%=pres%>');
<%    }

   }   if (allergyList != null){
      for (int i =0; i < allergyList.size();i++){
        String allerg =  (String) allergyList.get(i); %>
  addToHiddenAllergy('<%=allerg%>');
<%    }
   }%>



</script>


</body>

</html:html>


<%!

    String displayNone(String s,List l){
       String ret = "";
       if (s != null && l != null  && l.contains(s)){
           ret="display:none;";
       }
       return ret;
    }

    String hashval(Object s){
        String st = "";
        if ( s instanceof String){
             st = (String) s;
        }
        return st;
    }

    String completeRefused(Object s){
        String ret = "Competed";
        String st = "";
        if ( s instanceof String){
             st = (String) s;
        }
        if ( ret != null && st.equalsIgnoreCase("1")){
            ret = "Refused";
        }
        return ret;
    }
%>
