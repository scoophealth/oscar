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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*,oscar.util.StringUtils,oscar.util.UtilDateUtilities" %>
<%@ page import="org.apache.commons.collections.MultiHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>

<%

Integer pageNum=(Integer)request.getAttribute("pageNum");
Hashtable docType=(Hashtable)request.getAttribute("docType");
Hashtable patientDocs=(Hashtable)request.getAttribute("patientDocs");
String providerNo=(String)request.getAttribute("providerNo");
String searchProviderNo=(String)request.getAttribute("searchProviderNo");
Hashtable patientIdNames=(Hashtable)request.getAttribute("patientIdNames");
Hashtable docStatus=(Hashtable)request.getAttribute("docStatus");
String patientIdStr =(String)request.getAttribute("patientIdStr");
Hashtable typeDocLab =(Hashtable)request.getAttribute("typeDocLab");
String demographicNo=(String)request.getAttribute("demographicNo");
String ackStatus = (String)request.getAttribute("ackStatus");
List labdocs=(List)request.getAttribute("labdocs");
Hashtable patientNumDoc=(Hashtable)request.getAttribute("patientNumDoc");
Integer totalDocs=(Integer) request.getAttribute("totalDocs");
Integer totalHL7=(Integer)request.getAttribute("totalHL7");
List<String> normals=(List<String>)request.getAttribute("normals");
List<String> abnormals=(List<String>)request.getAttribute("abnormals");
Integer totalNumDocs=(Integer)request.getAttribute("totalNumDocs");

%>




<html>

<head>
    <!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<!-- calendar style sheet -->
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/scriptaculous.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

        <script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/effects.js"></script>
        <script type="text/javascript" src="../share/javascript/controls.js"></script>

        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="../js/demographicProviderAutocomplete.js"></script>
        
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />


<title>
<bean:message key="oscarMDS.index.title"/> Page <%=pageNum%>
</title>
<html:base/>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<style type="text/css">
    span.categoryCB{
        color:white;
}
<!--
.RollRes     { font-weight: 700; font-size: 8pt; color: white; font-family:
               Verdana, Arial, Helvetica }
.RollRes a:link { color: white }
.RollRes a:hover { color: white }
.RollRes a:visited { color: white }
.RollRes a:active { color: white }
.AbnormalRollRes { font-weight: 700; font-size: 8pt; color: red; font-family:
               Verdana, Arial, Helvetica }
.AbnormalRollRes a:link { color: red }
.AbnormalRollRes a:hover { color: red }
.AbnormalRollRes a:visited { color: red }
.AbnormalRollRes a:active { color: red }
.CorrectedRollRes { font-weight: 700; font-size: 8pt; color: yellow; font-family:
               Verdana, Arial, Helvetica }
.CorrectedRollRes a:link { color: yellow }
.CorrectedRollRes a:hover { color: yellow }
.CorrectedRollRes a:visited { color: yellow }
.CorrectedRollRes a:active { color: yellow }
.AbnormalRes { font-weight: bold; font-size: 8pt; color: red; font-family:
               Verdana, Arial, Helvetica; height: 25px }
.AbnormalRes a:link { color: red }
.AbnormalRes a:hover { color: red }
.AbnormalRes a:visited { color: red }
.AbnormalRes a:active { color: red }
.NormalRes   { font-weight: bold; font-size: 8pt; color: black; font-family:
               Verdana, Arial, Helvetica; height: 25px }
.NormalRes a:link { color: black }
.NormalRes a:hover { color: black }
.NormalRes a:visited { color: black }
.NormalRes a:active { color: black }
.HiLoRes     { font-weight: bold; font-size: 8pt; color: blue; font-family:
               Verdana, Arial, Helvetica; height: 25px }
.HiLoRes a:link { color: blue }
.HiLoRes a:hover { color: blue }
.HiLoRes a:visited { color: blue }
.HiLoRes a:active { color: blue }
.CorrectedRes { font-weight: bold; font-size: 8pt; color: #4d8977; font-family:
               Verdana, Arial, Helvetica }
.CorrectedRes a:link { color: #6da997 }
.CorrectedRes a:hover { color: #6da997 }
.CorrectedRes a:visited { color: #6da997 }
.CorrectedRes a:active { color: #6da997 }
.Field       { font-weight: bold; font-size: 8.5pt; color: black; font-family:
               Verdana, Arial, Helvetica }
div.Field a:link { color: black }
div.Field a:hover { color: black }
div.Field a:visited { color: black }
div.Field a:active { color: black }
.Field2      { font-weight: bold; font-size: 8pt; color: #ffffff; font-family:
               Verdana, Arial, Helvetica }
div.Field2   { font-weight: bold; font-size: 8pt; color: #ffffff; font-family:
               Verdana, Arial, Helvetica }
div.FieldData { font-weight: normal; font-size: 8pt; color: black; font-family:
               Verdana, Arial, Helvetica }
div.Field3   { font-weight: normal; font-size: 8pt; color: black; font-style: italic;
               font-family: Verdana, Arial, Helvetica }
div.Title    { font-weight: 800; font-size: 10pt; color: white; font-family:
               Verdana, Arial, Helvetica; padding-top: 4pt; padding-bottom:
               2pt }
div.Title a:link { color: white }
div.Title a:hover { color: white }
div.Title a:visited { color: white }
div.Title a:active { color: white }
div.Title2   { font-weight: bolder; font-size: 9pt; color: black; text-indent: 5pt;
               font-family: Verdana, Arial, Helvetica; padding-bottom: 2pt }
div.Title2 a:link { color: black }
div.Title2 a:hover { color: black }
div.Title2 a:visited { color: black }
div.Title2 a:active { color: black }
.Cell        { background-color: #9999CC; border-left: thin solid #CCCCFF;
               border-right: thin solid #6666CC;
               border-top: thin solid #CCCCFF;
               border-bottom: thin solid #6666CC;
               height: 40px;
               font-weight: bold;
               font-size: 8pt;
               color: #ffffff;
               font-family: Verdana, Arial, Helvetica }
.Cell2       { background-color: #376c95; border-left-style: none; border-left-width: medium;
               border-right-style: none; border-right-width: medium;
               border-top: thin none #bfcbe3; border-bottom-style: none;
               border-bottom-width: medium }
.Cell3       { background-color: #add9c7; border-left: thin solid #dbfdeb;
               border-right: thin solid #5d9987;
               border-top: thin solid #dbfdeb;
               border-bottom: thin solid #5d9987 }
.CellHdr     { background-color: #cbe5d7; border-right-style: none; border-right-width:
               medium; border-bottom-style: none; border-bottom-width: medium }
.Nav         { font-weight: bold; font-size: 8pt; color: white; font-family:
               Verdana, Arial, Helvetica }
div.Nav a:link { color: white }
div.Nav a:hover { color: #eeeeee }
div.Nav a:visited { color: white }
div.Nav a:active { color: #eeeeee }
.PageLink a:link { font-size: 8pt; color: white }
.PageLink a:hover { color: red }
.PageLink a:visited { font-size: 9pt; color: yellow }
.PageLink a:active { font-size: 12pt; color: yellow }
.PageLink    { font-family: Verdana }
.text1       { font-size: 8pt; color: black; font-family: Verdana, Arial, Helvetica }
div.txt1     { font-size: 8pt; color: black; font-family: Verdana, Arial }
div.txt2     { font-weight: bolder; font-size: 6pt; color: black; font-family: Verdana, Arial }
div.Title3   { font-weight: bolder; font-size: 12pt; color: black; font-family:
               Verdana, Arial }
.red         { color: red }
.text2       { font-size: 7pt; color: black; font-family: Verdana, Arial }
.white       { color: white }
.title1      { font-size: 9pt; color: black; font-family: Verdana, Arial }
div.Title4   { font-weight: 600; font-size: 8pt; color: white; font-family:
               Verdana, Arial, Helvetica }
.smallButton { font-size: 8pt; }
-->
</style>
<script type="text/javascript" >
    function checkSelected() {
    aBoxIsChecked = false;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            aBoxIsChecked = true;
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                aBoxIsChecked = true;
            }
        }
    }
    if (aBoxIsChecked) {
        popupStart(300, 400, 'SelectProvider.jsp', 'providerselect');
    } else {
        alert('<bean:message key="oscarMDS.index.msgSelectOneLab"/>');
    }
}
    function popupConsultation(segmentId) {
                            	  var page = '<%=request.getContextPath()%>/oscarEncounter/ViewRequest.do?segmentId='+segmentId;
                            	  var windowprops = "height=960,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
                            	  var popup=window.open(page, "<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsReq"/>", windowprops);
                            	  if (popup != null) {
                            	    if (popup.opener == null) {
                            	      popup.opener = self;
                            	    }
                            	  }
                           }



function popupStart(vheight,vwidth,varpage) {
    popupStart(vheight,vwidth,varpage,"helpwindow");
}

function popupStart(vheight,vwidth,varpage,windowname) {
        //console.log("in popupstart 4 args");
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    if(!windowname)
        windowname="helpwindow";
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}

function reportWindow(page,height,width) {
    //console.log(page);
    if(height && width){
        windowprops="height="+660+", width="+width+", location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0" ;
    }else{
        windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    }
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}


function submitFile(){
   aBoxIsChecked = false;
   submitLabs = true;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            if (document.reassignForm.ackStatus.value == "false"){
                aBoxIsChecked = confirm("The lab for "+document.reassignForm.patientName.value+" has not been attached to a demographic, would you like to file it anyways?");
            }else{
                aBoxIsChecked = true;
            }
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                if (document.reassignForm.ackStatus[i].value == "false"){
                    aBoxIsChecked = confirm("The lab for "+document.reassignForm.patientName[i].value+" has not been attached to a demographic, would you like to file it anyways?");
                    if(!aBoxIsChecked)
                        break;
                }else{
                    aBoxIsChecked = true;
                }
            }
        }
    }
    if (aBoxIsChecked) {
       document.reassignForm.action = '../oscarMDS/FileLabs.do';
       document.reassignForm.submit();
    }
}

function checkAll(formId){
   var f = document.getElementById(formId);
   var val = f.checkA.checked;
   for (i =0; i < f.flaggedLabs.length; i++){
      f.flaggedLabs[i].checked = val;
   }
}

function wrapUp() {
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_lab");
	setTimeout("window.close();",100);
    } else {
	window.close();
    }
}

 /* Element.addMethods("select",(function(){
                                function getSelected(element){
                                    if(!(element=$(element))) return;
                                    var index=element.selectedIndex;
                                    return index>=0 ? element.options[index].value:undefined;
                                }
                                return {
                                    getSelected: getSelected
                                };
                            })());*/
                         /*   function showAllSummaryDocLabs(){
                                var eles1=document.getElementsByClassName('NormalRes');
                                var eles2=document.getElementsByClassName('AbnormalRes');
                                var i=0;
                                for(i=0;i<eles1.length;i++){

                                    var ele=eles1[i];
                                    oscarLog(ele.id);
                                        ele.setStyle({display:'table-row'});
                                }
                                for(i=0;i<eles2.length;i++){
                                    var ele=eles2[i];
                                        ele.setStyle({display:'table-row'});
                                  }
                            }*/
                            /*function showDocs(){
                                var eles=document.getElementsByName('scannedDoc');
                                var i=0;
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    oscarLog(ele.id);
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else   ;
                                }
                                eles=document.getElementsByName('HL7lab');
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ;
                                    else
                                        ele.setStyle({display:'none'});
                                }
                            }*/
                          /*  function hideScannedDocs(){//not used
                                var eles=document.getElementsByName('scannedDoc');
                                var i=0;
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ;
                                    else
                                        ele.setStyle({display:'none'});
                                }

                                eles=document.getElementsByName('HL7lab');
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else
                                        ;
                                }
                            }*/
                          /*  function showHL7s(){
                                var eles=document.getElementsByName('HL7lab');
                                var i=0;
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    ele.setStyle({display:'table-row'});
                                }
                                eles=document.getElementsByName('scannedDoc');
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    ele.setStyle({display:'none'});
                                }
                            }*/
                           /* function showNormals(){
                                var eles1=document.getElementsByClassName('NormalRes');
                                var i=0;
                                for(i=0;i<eles1.length;i++){
                                    var ele=eles1[i];
                                    ele.setStyle({display:'table-row'});
                                }
                                var eles2=document.getElementsByClassName('AbnormalRes');
                                i=0;
                                for(i=0;i<eles2.length;i++){
                                    var ele=eles2[i];
                                    ele.setStyle({display:'none'});
                                }
                            }*/
                           /* function showAbnormals(){
                                var eles2=document.getElementsByClassName('AbnormalRes');
                                var i=0;
                                for(i=0;i<eles2.length;i++){
                                    var ele=eles2[i];
                                    ele.setStyle({display:'table-row'});
                                }
                                var eles1=document.getElementsByClassName('NormalRes');
                                i=0;
                                for(i=0;i<eles1.length;i++){
                                    var ele=eles1[i];
                                    ele.setStyle({display:'none'});
                                }
                            }*/
                           /* function toggleDocLabs(ele){
                                var selected=ele.getSelected();

                                if(selected=='A'){
                                    $('Atop').selected=true;
                                    $('Abottom').selected=true;
                                    //show all
                                    showAllSummaryDocLabs();
                                }else if(selected=='D'){
                                    $('Dtop').selected=true;
                                    $('Dbottom').selected=true;
                                    //show only docs
                                    showDocs();
                                }else if(selected=='H'){
                                    $('Htop').selected=true;
                                    $('Hbottom').selected=true;
                                    showHL7s();
                                }else if(selected=='N'){
                                    $('Ntop').selected=true;
                                    $('Nbottom').selected=true;
                                    showNormals();
                                }else if(selected=='AB'){
                                    $('ABtop').selected=true;
                                    $('ABbottom').selected=true;
                                    showAbnormals();
                                }
                            }*/
                            function changeView(){
                                if($('summaryView').getStyle('display')=='none'){
                                     $('summaryView').show();
                                     $('readerViewTable').hide();
                                }
                                else{
                                     $('summaryView').hide();
                                     $('readerViewTable').show();
                                 }
                            }

                            function checkType(docNo){
                                docNo=docNo.replace(' ','');
                                var docTypes='<%=docType%>';
                                var s='[{\\s]'+docNo+'='+'\\w+';
                                var p= new RegExp(s,'g');
                                //oscarLog(docTypes+"-+-"+p);
                                if(docTypes.match(p)!=null && (docTypes.match(p)).length>0){
                                    var text=(docTypes.match(p))[0];
                                    //oscarLog('matched='+text);
                                    return text.split("=")[1];
                                }else
                                    return '';
                            }
                            function showDocLab(childId,docNo,providerNo,searchProviderNo,status,demoName,showhide){//showhide is 0 = document currently hidden, 1=currently shown
                                //create child element in docViews
                                docNo=docNo.replace(' ','');//trim
                                var type=checkType(docNo);
                                //oscarLog('type'+type);
                                //var div=childId;

                                //var div=window.frames[0].document.getElementById(childId);
                                var div=$(childId);
                                //alert(div);
                                var url='';
                                if(type=='DOC')
                                    url="../dms/showDocument.jsp";
                                else if(type=='MDS')
                                    url="";
                                else if(type=='HL7')
                                    url="../lab/CA/ALL/labDisplayAjax.jsp";
                                else if(type=='CML')
                                    url="";
                                else
                                    url="";

                                        //oscarLog('url='+url);
                                        var data="segmentID="+docNo+"&providerNo="+providerNo+"&searchProviderNo="+searchProviderNo+"&status="+status+"&demoName="+demoName;
                                        //oscarLog('url='+url+'+-+ \n data='+data);
                                        new Ajax.Updater(div,url,{method:'get',parameters:data,insertion:Insertion.Bottom,evalScripts:true,onSuccess:function(transport){}});

                            }

                            function createNewElement(parent,child){
                                //oscarLog('11 create new leme');
                                var newdiv=document.createElement('div');
                                //oscarLog('22 after create new leme');
                                newdiv.setAttribute('id',child);
                                //oscarLog('33 after create new leme');
                                //var parentdiv=document.getElementById(parent);
                               // var parentdiv=window.frames[0].document.getElementById(parent);
                               var parentdiv=$(parent);
                                //alert(parentdiv);
                                //newdiv.innerHTML='test html';
                                //oscarLog('parentdiv='+parentdiv+"; child ="+newdiv);
                                parentdiv.appendChild(newdiv);
                                //oscarLog('55 after create new leme');
                            }
                            function getLabDocFromPatientId(patientId){//return array of doc ids and lab ids from patient id.
                                var pattern=new RegExp(patientId+"=\\[.*?\\]","g");
                                var text='<%=patientDocs%>';
                                //oscarLog(text);
                                var result=(text.match(pattern))[0];
                                //oscarLog(result);
                                result=result.replace(patientId+"=[","");
                                result=result.replace("]","");
                                result=result.replace(/\s/g,"");
                                //oscarLog(result);
                                var resultAr=result.split(",");
                                return resultAr;
                            }

                            function showThisPatientDocs(patientId,keepPrevious){
                                //oscarLog("patientId in show this patientdocs="+patientId);
                                var labDocsArr=getLabDocFromPatientId(patientId);
                                var patientName=getPatientNameFromPatientId(patientId);
                                if(patientName!=null&&patientName.length>0){
                                        //oscarLog(patientName);
                                        var childId='patient'+patientId;
                                      //if(toggleElement(childId));
                                      //else{
                                      if(keepPrevious);
                                      else clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<labDocsArr.length;i++){
                                            var docId=labDocsArr[i].replace(' ', '');

                                            var ackStatus=getAckStatusFromDocLabId(docId);
                                            //oscarLog('childId='+childId+',docId='+docId+',ackStatus='+ackStatus);
                                            showDocLab(childId,docId,'<%=providerNo%>','<%=searchProviderNo%>',ackStatus,patientName,'labdoc'+patientId+'show');
                                        }
                                        //toggleMarker('labdoc'+patientId+'show');
                                     //}
                                }
                            }
                            function getPatientIdFromDocLabId(docLabId){
                                var pna=new RegExp("-1=\\[.*?"+docLabId+".*?\\]");
                                var p=new RegExp("[{\\s]\\d+=\\[.*?"+docLabId+".*?\\]",'g');
                                var text='<%=patientDocs%>';
                                var rna=text.match(pna);
                                if(rna!=null && rna.length>0){
                                    return '-1';
                                }else{
                                        var r=text.match(p);
                                        if(r!=null && r.length>0){
                                            var s=r[0];
                                            s=(s.split("="))[0];
                                            return s;
                                        }
                                        return null;
                                }
                            }
                            function getPatientNameFromPatientId(patientId){
                                var text2='<%=patientIdNames%>';
                                var p2=new RegExp(patientId+"=\\w+,\\s*\\w+");
                                var patientName=null;
                                if(text2.match(p2)!=null&&(text2.match(p2)).length>0){
                                        var r2=(text2.match(p2))[0];
                                        patientName=r2.split("=")[1];
                                }
                                return patientName;
                            }
                            function getAckStatusFromDocLabId(docLabId){
                                var p3=new RegExp(docLabId+"=\\w");
                                var text3='<%=docStatus%>';
                                var m=(text3.match(p3))[0];
                                var ackStatus=m.split("=")[1];
                                return ackStatus;
                            }
                            //not used
                           /* function toggleElement(ele){// return true if element is present,false if not
                                //var div=window.frames[0].document.getElementById(ele);
                                var div=$(ele);
                                oscarLog("div="+div);
                                if(div!=null){
                                    if(div.style.display!='none')
                                        div.style.display='none';
                                    else
                                        div.style.display='';
                                    return true;
                                }else
                                    return false;
                            }*/
                            function showAllDocLabs(){
                                var patientids='<%=patientIdStr%>';
                                var idsArr=patientids.split(',');
                                clearDocView();
                                for(var i=0;i<idsArr.length;i++){
                                    var id=idsArr[i];
                                    //oscarLog("ids in showalldoclabs="+id);
                                    if(id.length>0){
                                        showThisPatientDocs(id,true);

                                    }
                                }
                               //toggleMarker('allshow');
                            }
                            //not used
                        /*    function toggleMarker(marker){
                                oscarLog('before toggleMarker='+$(marker).value);
                                if($(marker).value==0)
                                    $(marker).value=1;
                                else
                                    $(marker).value=0;
                                oscarLog('after toggleMarker='+$(marker).value);
                            }*/
                            function showCategory(cat){
                                //oscarLog('cat ='+cat);
                                var pattern=new RegExp(cat+"=\\[.*?\\]","g");
                                var text='<%=typeDocLab%>';
                                var resultA=text.match(pattern);
                                var result;
                                if(resultA==null || resultA.length==0);
                                else
                                    result=resultA[0];
                                var r=result.split("=")[1];
                                 r=r.replace("[","");
                                 r=r.replace("]","");
                                 var sA=r.split(",");//array of doc ids belong to this category
                                 //oscarLog("sA="+sA);
                                 var childId="category"+cat;
                                 //if(toggleElement(childId));
                                // else{
                                clearDocView();
                                     createNewElement('docViews',childId);
                                     for(var i=0;i<sA.length;i++){
                                         var docLabId=sA[i];
                                         docLabId=docLabId.replace(/\s/g, "");
                                         //oscarLog("docLabId="+docLabId);
                                         var patientId=getPatientIdFromDocLabId(docLabId);
                                         //oscarLog("patientId="+patientId);
                                         var patientName=getPatientNameFromPatientId(patientId);
                                         var ackStatus=getAckStatusFromDocLabId(docLabId);
                                         //oscarLog("patientName="+patientName);
                                         //oscarLog("ackStatus="+ackStatus);

                                         if(patientName!=null) showDocLab(childId,docLabId,'<%=providerNo%>','<%=searchProviderNo%>',ackStatus,patientName,cat+'show');
                                     }
                                    //toggleMarker(cat+'show');
                            }
                            function showAb_Normal(ab_normal,str){
                                    str=str.replace('[','');
                                    str=str.replace(']','');
                                    var ids=str.split(',');
                                    var childId;
                                if(ab_normal=='normal'){
                                    childId='normals';
                                }else if (ab_normal=='abnormal'){
                                    childId='abnormals';
                                }
                                //oscarLog(childId);
                                if(childId!=null && childId.length>0){
                                    //if(toggleElement(childId));
                                      //else{
                                      clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<ids.length;i++){
                                            var docLabId=ids[i].replace(/\s/g,'');
                                            var ackStatus=getAckStatusFromDocLabId(docLabId);
                                            var patientId=getPatientIdFromDocLabId(docLabId);
                                            var patientName=getPatientNameFromPatientId(patientId);
                                            showDocLab(childId,docLabId,'<%=providerNo%>','<%=searchProviderNo%>',ackStatus,patientName,ab_normal+'show');
                                        }
                                    //}
                                }
                              //toggleMarker(ab_normal+'show');

                            }
                            function clearDocView(){
                            var docview=$('docViews');
                               //var docview=window.frames[0].document.getElementById('docViews');
                               docview.innerHTML='';
                            }
                            function showSubType(patientId,subType){
                                    var labdocsArr=getLabDocFromPatientId(patientId);
                                    var childId='subType'+subType+patientId;
                                    if(labdocsArr.length>0){
                                        //if(toggleElement(childId));
                                     // else{
                                     clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<labdocsArr.length;i++){
                                            var labdoc=labdocsArr[i];
                                            labdoc=labdoc.replace(' ','');
                                            //oscarLog('check type input='+labdoc);
                                            var type=checkType(labdoc);
                                            var ackStatus=getAckStatusFromDocLabId(labdoc);
                                            var patientName=getPatientNameFromPatientId(patientId);
                                            //oscarLog("type="+type+"--subType="+subType);
                                            if(type==subType)
                                                showDocLab(childId,labdoc,'<%=providerNo%>','<%=searchProviderNo%>',ackStatus,patientName,'subtype'+subType+patientId+'show');
                                            else;
                                        }
                                        //toggleMarker('subtype'+subType+patientId+'show');
                                    //}
                                  }
                            }


                            function showhideSubCat(plus_minus,patientId){
                                if(plus_minus=='plus'){
                                    $('plus'+patientId).hide();
                                    $('minus'+patientId).show();
                                    $('labdoc'+patientId+'showSublist').show();
                                }else{
                                    $('minus'+patientId).hide();
                                    $('plus'+patientId).show();
                                    $('labdoc'+patientId+'showSublist').hide();
                                }
                            }

                            //not used
                       /*     function showLabDocFromPatient(patientId){
                                $('labdoc'+patientId+'show').value=0;
                                showThisPatientDocs(patientId);
                                $('labdocshow'+patientId).hide();
                                $('labdochide'+patientId).show();

                            }
                            //not used
                            function hideLabDocFromPatient(patientId){
                                $('labdoc'+patientId+'show').value=1;
                                showThisPatientDocs(patientId);
                                $('labdocshow'+patientId).show();
                                $('labdochide'+patientId).hide();
                            }*/
                            var currentBold='';
                            function un_bold(ele){
                                //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                                if(currentBold==ele.id){
                                    ;
                                }else{
                                  if($(currentBold)!=null)
                                      $(currentBold).style.fontWeight='';
                                    ele.style.fontWeight='bold';
                                    currentBold=ele.id;
                                }
                                //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                            }


                           var number_of_row_per_page=20;
                           function showPageNumber(page){
                                    var totalNoRow=$('totalNumberRow').value;
                                    var newStartIndex=number_of_row_per_page*(parseInt(page)-1);
                                    var newEndIndex=parseInt(newStartIndex)+19;
                                    var isLastPage=false;
                                    if(newEndIndex>totalNoRow){
                                        newEndIndex=totalNoRow;
                                        isLastPage=true;
                                    }
                                    //oscarLog("new start="+newStartIndex+";new end="+newEndIndex);
                                   for(var i=0;i<totalNoRow;i++){
                                       if($('row'+i) && parseInt(newStartIndex)<=i && i<=parseInt(newEndIndex)) {
                                           //oscarLog("show row-"+i);
                                           $('row'+i).show();
                                       }else if($('row'+i)){
                                           //oscarLog("hide row-"+i);
                                           $('row'+i).hide();
                                       }
                                   }
                               //update current page
                               $('currentPageNum').innerHTML=page;
                               if(page==1)
                               {
                                   $('msgPrevious').hide();
                               }else if(page>1){
                                   $('msgPrevious').show();
                               }
                               if(isLastPage)
                                   $('msgNext').hide();
                               else
                                   $('msgNext').show();
                           }
                           function showTypePageNumber(page,type){
                               var eles;
                               var numberPerPage=20;
                               if(type=='D'){
                                   eles=document.getElementsByName('scannedDoc');
                                   var length=eles.length;
                                   var startindex=(parseInt(page)-1)*numberPerPage;
                                   var endindex=startindex+numberPerPage-1;
                                   if(endindex>length-1){
                                       endindex=length-1;
                                   }
                                   //only display current page
                                   for(var i=startindex;i<endindex+1;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'table-row'});
                                   }
                                   //hide previous page
                                   for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all labs
                                   eles=document.getElementsByName('HL7lab');
                                   for(i=0;i<eles.length;i++){
                                        var ele=eles[i];
                                        ele.setStyle({display:'none'});
                                   }
                               }else if (type=='H'){
                                   eles=document.getElementsByName('HL7lab');
                                   var length=eles.length;
                                   var startindex=(parseInt(page)-1)*numberPerPage;
                                   var endindex=startindex+numberPerPage-1;
                                   if(endindex>length-1){
                                       endindex=length-1;
                                   }
                                   //only display current page
                                   for(var i=startindex;i<endindex+1;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'table-row'});
                                   }
                                   //hide previous page
                                   for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all labs
                                   eles=document.getElementsByName('scannedDoc');
                                   for(i=0;i<eles.length;i++){
                                        var ele=eles[i];
                                        ele.setStyle({display:'none'});
                                   }
                               }else if (type=='N'){
                                    var eles1=document.getElementsByClassName('NormalRes');
                                    var length=eles.length;
                                    var startindex=(parseInt(page)-1)*numberPerPage;
                                    var endindex=startindex+numberPerPage-1;
                                    if(endindex>length-1){
                                           endindex=length-1;
                                    }

                                    for(var i=startindex;i<endindex+1;i++){
                                        var ele=eles1[i];
                                        ele.setStyle({display:'table-row'});
                                    }
                                    //hide previous page
                                    for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all abnormals
                                    var eles2=document.getElementsByClassName('AbnormalRes');
                                    i=0;
                                    for(i=0;i<eles2.length;i++){
                                        var ele=eles2[i];
                                        ele.setStyle({display:'none'});
                                    }
                               }else if (type=='AB'){
                                    var eles1=document.getElementsByClassName('AbnormalRes');
                                    var length=eles.length;
                                    var startindex=(parseInt(page)-1)*numberPerPage;
                                    var endindex=startindex+numberPerPage-1;
                                    if(endindex>length-1){
                                           endindex=length-1;
                                    }
                                    for(var i=startindex;i<endindex+1;i++){
                                        var ele=eles1[i];
                                        ele.setStyle({display:'table-row'});
                                    }
                                    //hide previous page
                                    for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all normals
                                    var eles2=document.getElementsByClassName('NormalRes');
                                    for(var i=0;i<eles2.length;i++){
                                        var ele=eles2[i];
                                        ele.setStyle({display:'none'});
                                    }
                               }
                           }

                          /* function displayPage(page){//when category is all
                               var selected=$("selectDocLabBottom").getSelected();
                               if(selected=='A'){
                                   if(page=='Next'){
                                        var currentpage=parseInt($('currentPageNum').innerHTML);
                                        showPageNumber(currentpage+1);
                                   }
                                   else if( page=='Previous'){
                                        var currentpage=parseInt($('currentPageNum').innerHTML);
                                        showPageNumber(currentpage-1);
                                   }
                                   else if(page>0){
                                       showPageNumber(page);
                                   }
                               }else if(selected=='D'||selected=='H'||selected=='N'||selected=='AB'){
                                   if(page=='Next'){
                                       var cp=$('currentPageNum').innerHTML;
                                       showTypePageNumber(parseInt(cp)+1,selected);
                                   }
                                   else if( page=='Previous'){
                                       var cp=$('currentPageNum').innerHTML;
                                       showTypePageNumber(parseInt(cp)-1,selected);
                                   }
                                   else if(page>0){
                                       showTypePageNumber(page,selected);
                                   }

                               }
                           }*/
                           var current_category=new Array();
                           var current_rows=new Array();
                           var current_numberofpages=1;
                           var total_rows=new Array();

                           function setTotalRows(){
                               var ds=document.getElementsByName('scannedDoc');
                               var ls=document.getElementsByName('HL7lab');
                               for(var i=0;i<ds.length;i++){
                                   var ele=ds[i];
                                   total_rows.push(ele.id);
                               }
                               for(var i=0;i<ls.length;i++){
                                   var ele=ls[i];
                                   total_rows.push(ele.id);
                               }
                               total_rows=sortRowId(uniqueArray(total_rows));
                               current_category=new Array();
                                                        current_category[0]=document.getElementsByName('scannedDoc');
                                                        current_category[1]=document.getElementsByName('HL7lab');
                                                        current_category[2]=document.getElementsByClassName('NormalRes');
                                                        current_category[3]=document.getElementsByClassName('AbnormalRes');
                           }
                           function checkBox(){
                                                    //oscarLog("in checkBox");
                                                    var checkedArray=new Array();
                                                    if($('documentCB').checked==1){
                                                        checkedArray.push('document');
                                                    }
                                                    if($('hl7CB').checked==1){
                                                        checkedArray.push('hl7');
                                                    }
                                                    if($('normalCB').checked==1){
                                                        checkedArray.push('normal');
                                                    }
                                                    if($('abnormalCB').checked==1){
                                                        checkedArray.push('abnormal');
                                                    }
                                         if(checkedArray.length==0||checkedArray.length==4){
                                                        var endindex= number_of_row_per_page-1;
                                                        if(endindex>=total_rows.length)
                                                            endindex=total_rows.length-1;

                                                        //show all
                                                        for(var i=0;i<endindex+1;i++){
                                                            var id=total_rows[i];
                                                            if($(id)){
                                                                $(id).show();
                                                            }
                                                        }
                                                        for(var i=endindex+1;i<total_rows.length;i++){
                                                            var id=total_rows[i];
                                                            if($(id)){
                                                                $(id).hide();
                                                            }
                                                        }
                                                        current_numberofpages=Math.ceil(total_rows.length/number_of_row_per_page);
                                                        initializeNavigation();
                                                        current_category=new Array();
                                                        current_category[0]=document.getElementsByName('scannedDoc');
                                                        current_category[1]=document.getElementsByName('HL7lab');
                                                        current_category[2]=document.getElementsByClassName('NormalRes');
                                                        current_category[3]=document.getElementsByClassName('AbnormalRes');
                                           }
                                            else{
                                                        //oscarLog('checkedArray='+checkedArray);
                                                        var eles=new Array();
                                                    for(var i=0;i<checkedArray.length;i++){
                                                        var type=checkedArray[i];

                                                        if(type=='document'){
                                                            var docs=document.getElementsByName('scannedDoc');
                                                            eles.push(docs);
                                                        }
                                                        else if(type=='hl7'){
                                                            var labs=document.getElementsByName('HL7lab');
                                                            eles.push(labs);
                                                        }
                                                        else if(type=='normal'){
                                                            var normals=document.getElementsByClassName('NormalRes');
                                                            eles.push(normals);

                                                        }
                                                        else if(type=='abnormal'){
                                                            var abnormals=document.getElementsByClassName('AbnormalRes');
                                                            eles.push(abnormals);
                                                        }
                                                    }
                                                    current_category=eles;
                                                    displayCategoryPage(1);
                                                    initializeNavigation();
                                                }
                                            }

                                            function displayCategoryPage(page){
                                                //oscarLog('in displaycategorypage, page='+page);
                                                //write all row ids to an array
                                                var displayrowids=new Array();
                                                    for(var p=0;p<current_category.length;p++){
                                                        var elements=new Array();
                                                        elements=current_category[p];
                                                        //oscarLog("elements.lenght="+elements.length);
                                                        for(var j=0;j<elements.length;j++){
                                                            var e=elements[j];
                                                            var rowid=e.id;
                                                            displayrowids.push(rowid);
                                                        }
                                                    }
                                                    //oscarLog('displayrowids='+displayrowids);
                                                    //oscarLog('size='+displayrowids.length);
                                                    //make array unique
                                                    displayrowids=uniqueArray(displayrowids);
                                                    //oscarLog('unique displayrowids='+displayrowids);
                                                    //oscarLog('unique size='+displayrowids.length);
                                                    displayrowids=sortRowId(displayrowids);
                                                    //oscarLog('sort and unique displaywords='+displayrowids);

                                                    var numOfRows=displayrowids.length;
                                                    //oscarLog(numOfRows);
                                                    current_numberofpages=Math.ceil(numOfRows/number_of_row_per_page);
                                                    //oscarLog(current_numberofpages);
                                                    var startIndex=(parseInt(page)-1)*number_of_row_per_page;
                                                    var endIndex=startIndex+(number_of_row_per_page-1);
                                                    if(endIndex>displayrowids.length-1){
                                                        endIndex=displayrowids.length-1;
                                                    }
                                                    //set current displaying rows
                                                    current_rows=new Array();
                                                    for(var i=startIndex;i<endIndex+1;i++){
                                                        if($(displayrowids[i])){
                                                            current_rows.push(displayrowids[i]);
                                                        }
                                                    }
                                                    //oscarLog('total_rows='+total_rows);
                                                    //oscarLog('current_rows='+current_rows);
                                                    //loop through every thing,if it's in displayrowids, show it , if it's not hide it.
                                                    for(var i=0;i<total_rows.length;i++){
                                                        var rowid=total_rows[i];
                                                        if(a_contain_b(current_rows,rowid)){
                                                            $(rowid).show();
                                                        }else
                                                            $(rowid).hide();
                                                    }
                                            }

                                            function initializeNavigation(){
                                                   $('currentPageNum').innerHTML=1;
                                                    //update the page number shown and update previous and next words
                                                    if(current_numberofpages>1){
                                                        $('msgNext').show();
                                                        $('msgPrevious').hide();
                                                    }else if(current_numberofpages<1){
                                                        $('msgNext').hide();
                                                        $('msgPrevious').hide();
                                                    }else if(current_numberofpages==1){
                                                        $('msgNext').hide();
                                                        $('msgPrevious').hide();
                                                    }
                                                    //oscarLog("current_numberofpages "+current_numberofpages);
                                                    $('current_individual_pages').innerHTML="";
                                                   if(current_numberofpages>1){
                                                       for(var i=1;i<=current_numberofpages;i++){
                                                        $('current_individual_pages').innerHTML+='<a style="text-decoration:none;" href="javascript:void(0);" onclick="navigatePage('+i+')> [ '+i+' ] </a>';
                                                    }
                                                   }
                                            }
                                            function sortRowId(a){
                                                    var numArray=new Array();
                                                    //sort array
                                                    for(var i=0;i<a.length;i++){
                                                        var id=a[i];
                                                        var n=id.replace('row','');
                                                        numArray.push(parseInt(n));
                                                    }
                                                    numArray.sort(function(a,b){return a-b;});
                                                    a=new Array();
                                                    for(var i=0;i<numArray.length;i++){
                                                        a.push('row'+numArray[i]);
                                                    }
                                                    return a;
                                            }
                                            function a_contain_b(a,b){//a is an array, b maybe an element in a.
                                                for(var i=0;i<a.length;i++){
                                                    if(a[i]==b){
                                                        return true;
                                                    }
                                                }
                                                return false;
                                            }

                                            function uniqueArray(a){
                                                var r=new Array();
                                                o:for(var i=0,n=a.length;i<n;i++){
                                                    for(var x=0,y=r.length;x<y;x++){
                                                        if(r[x]==a[i]) continue o;
                                                    }
                                                    r[r.length]=a[i];
                                                }
                                                return r;
                                            }

                                            function navigatePage(p){
                                                var pagenum=parseInt($('currentPageNum').innerHTML);
                                                if(p=='Previous'){
                                                    displayCategoryPage(pagenum-1);
                                                    $('currentPageNum').innerHTML=pagenum-1
                                                }
                                                else if(p=='Next'){
                                                    displayCategoryPage(pagenum+1);
                                                    $('currentPageNum').innerHTML=pagenum+1
                                                }
                                                else if(parseInt(p)>0){
                                                    displayCategoryPage(parseInt(p));
                                                    $('currentPageNum').innerHTML=p;
                                                }
                                                changeNavigationBar();
                                            }
                                            function changeNavigationBar(){
                                                var pagenum=parseInt($('currentPageNum').innerHTML);
                                                if(current_numberofpages==1){
                                                    $('msgNext').hide();
                                                    $('msgPrevious').hide();
                                                }
                                                else if(current_numberofpages>1 && current_numberofpages==pagenum){
                                                    $('msgNext').hide();
                                                    $('msgPrevious').show();
                                                }
                                                else if(current_numberofpages>1 && pagenum==1){
                                                    $('msgNext').show();
                                                    $('msgPrevious').hide();
                                                }else if(pagenum<current_numberofpages && pagenum>1){
                                                    $('msgNext').show();
                                                    $('msgPrevious').show();
                                                }
                                            }
                                            function syncCB(ele){
                                                var id=ele.id;
                                                if(id=='documentCB'){
                                                    if(ele.checked==1)
                                                        $('documentCB2').checked=1;
                                                    else
                                                        $('documentCB2').checked=0;
                                                }
                                                else if(id=='documentCB2'){
                                                    if(ele.checked==1)
                                                        $('documentCB').checked=1;
                                                    else
                                                        $('documentCB').checked=0;
                                                }
                                                else if(id=='hl7CB'){
                                                    if(ele.checked==1)
                                                        $('hl7CB2').checked=1;
                                                    else
                                                        $('hl7CB2').checked=0;
                                                }
                                                else if(id=='hl7CB2'){
                                                    if(ele.checked==1)
                                                        $('hl7CB').checked=1;
                                                    else
                                                        $('hl7CB').checked=0;
                                                }
                                                else if(id=='normalCB'){
                                                    if(ele.checked==1)
                                                        $('normalCB2').checked=1;
                                                    else
                                                        $('normalCB2').checked=0;
                                                }
                                                else if(id=='normalCB2'){
                                                    if(ele.checked==1)
                                                        $('normalCB').checked=1;
                                                    else
                                                        $('normalCB').checked=0;
                                                }
                                                else if(id=='abnormalCB'){
                                                    if(ele.checked==1)
                                                        $('abnormalCB2').checked=1;
                                                    else
                                                        $('abnormalCB2').checked=0;
                                                }
                                                else if(id=='abnormalCB2'){
                                                    if(ele.checked==1)
                                                        $('abnormalCB').checked=1;
                                                    else
                                                        $('abnormalCB').checked=0;
                                                }
                                            }


</script>
</head>

<body oldclass="BodyStyle" vlink="#0000FF" onload="setTotalRows();" >
    <form name="reassignForm" method="post" action="ReportReassign.do" id="lab_form">
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" width="100%">
            <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="10" align="left">
                 <table width="100%">
                    <tr>
                        <td align="center" colspan="2" class="Nav">
                                <% if (demographicNo == null) { %>
                                    <span class="white">
                                     <% if (ackStatus.equals("N")) {%>
                                           <bean:message key="oscarMDS.index.msgNewLabReportsFor"/>
                                     <%} else if (ackStatus.equals("A")) {%>
                                           <bean:message key="oscarMDS.index.msgAcknowledgedLabReportsFor"/>
                                     <%} else {%>
                                           <bean:message key="oscarMDS.index.msgAllLabReportsFor"/>
                                     <%}%>&nbsp;
                                     
                                     <% if (searchProviderNo.equals("")) {%>
                                            <bean:message key="oscarMDS.index.msgAllPhysicians"/>
                                     <%} else if (searchProviderNo.equals("0")) {%>
                                            <bean:message key="oscarMDS.index.msgUnclaimed"/>
                                     <%} else {%>
                                            <%=ProviderData.getProviderName(searchProviderNo)%>
                                     <%}%>
                                        &nbsp;&nbsp;&nbsp;
                                        Page : <a id="currentPageNum"><%=pageNum%></a>
                                     </span>
                                <% } %>
                        </td>
                        </tr>
                        <tr>
                            <td align="left" valign="center" > <%-- width="30%" --%>
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>
                                <input type="hidden" name="status" value="<%= ackStatus %>">
                                <input type="hidden" name="selectedProviders">
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                <input type="button" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                <% if (demographicNo == null && request.getParameter("fname") != null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <% if (demographicNo == null && labdocs.size() > 0) { %>                                    
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                    <% if (ackStatus.equals("N")) {%>
                                        <input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                    <% }
                                }%>
                                
                                <input type="checkbox" name="documentCB" id="documentCB" onclick="syncCB(this);checkBox();" ><span class="categoryCB"><bean:message key="global.Document"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" id="hl7CB" name="hl7CB"><span class="categoryCB"><bean:message key="global.hl7"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB"  name="normalCB"><span class="categoryCB"><bean:message key="global.normal"/></span>
                                <input type="checkbox" id="abnormalCB" onclick="syncCB(this);checkBox();" name="abnormalCB"><span class="categoryCB"><bean:message key="global.abnormal"/></span>
                                <input type="hidden" id="currentNumberOfPages" value="0"/>
                            </td>
                            
                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')" style="color: #FFFFFF;"><bean:message key="global.help"/></a>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                                | <a href="javascript:popupStart(800,1000,'../lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                | <a href="javascript:popupStart(600,500,'../dms/addDocuments2.jsp')" style="color: #FFFFFF; ">Doc Upload</a>
                                | <a href="javascript:void(0);" onclick="changeView()" style="color: #FFFFFF;">Change view</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="margin:0px;padding:0px;">
                    <table id="summaryView" width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
                        <tr>
                            <th align="left" valign="bottom" class="cell" nowrap>
                                <input type="checkbox" onclick="checkAll('lab_form');" name="checkA"/>
                                <bean:message key="oscarMDS.index.msgHealthNumber"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgPatientName"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgSex"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgResultStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDateTest"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgOrderPriority"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgRequestingClient"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDiscipline"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgReportStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                Ack #
                            </th>
                        </tr>

                                                <%
                            int number_of_rows_per_page=20;
                            int totalNoPages=labdocs.size()/number_of_rows_per_page;
                            if(labdocs.size()%number_of_rows_per_page!=0)
                                totalNoPages+=1;
                            System.out.println("totalNoPages="+totalNoPages);
                            int total_row_index=labdocs.size()-1;
                            System.out.println("total_row_index="+total_row_index);
                            for (int i = 0; i < labdocs.size(); i++) {

                                LabResultData   result =  (LabResultData) labdocs.get(i);
                                //LabResultData result = (LabResultData) labMap.get(labNoArray.get(i));

                                String segmentID        =  result.segmentID;
                                String status           =  result.acknowledgedStatus;

                                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                                if (!result.isMatchedToPatient()){
                                   bgcolor = "#FFCC00";
                                }

                                String discipline=result.getDiscipline();
                                if(discipline==null || discipline.equalsIgnoreCase("null"))
                                    discipline="";
                                MiscUtils.getLogger().debug("result.isAbnormal()="+result.isAbnormal());
                                %>
                        
                                <tr id="row<%=i%>" <%if((number_of_rows_per_page-1)<i){%>style="display:none;"<%}%> bgcolor="<%=bgcolor%>" <%if(result.isDocument()){%> name="scannedDoc" <%} else{%> name="HL7lab" <%}%> class="<%= (result.isAbnormal() ? "AbnormalRes" : "NormalRes" ) %>">
                                <td nowrap>
                                    <input type="hidden" id="totalNumberRow" value="<%=total_row_index+1%>">
                                    <input type="checkbox" name="flaggedLabs" value="<%=segmentID%>">
                                    <input type="hidden" name="labType<%=segmentID+result.labType%>" value="<%=result.labType%>"/>
                                    <input type="hidden" name="ackStatus" value="<%= result.isMatchedToPatient() %>" />
                                    <input type="hidden" name="patientName" value="<%= result.patientName %>"/>
                                    <%=result.getHealthNumber() %>
                                </td>
                                <td nowrap>
                                    <% if ( result.isMDS() ){ %>
                                    <a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%= result.getPatientName()%></a>
                                    <% }else if (result.isCML()){ %>
                                    <a href="javascript:reportWindow('../lab/CA/ON/CMLDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
                                    <% }else if (result.isHL7TEXT())
                                   	{ 
                                    	String categoryType=result.getDiscipline();
                                   		
                                    	if ("REF_I12".equals(categoryType))
                                    	{
	                                    	%>
                                      			<a href="javascript:popupConsultation('<%=segmentID%>')"><%=result.getPatientName()%></a>
                                    		<%                                    		
                                    	}
                                    	else if (categoryType!=null && categoryType.startsWith("ORU_R01:"))
                                    	{
	                                    	%>
                                      			<a href="<%=request.getContextPath()%>/lab/CA/ALL/viewOruR01.jsp?segmentId=<%=segmentID%>"><%=result.getPatientName()%></a>
                                    		<%                                    		
                                    	}
                                    	else
                                    	{
	                                    	%>
	                                    		<a href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
	                                    	<%
                                    	}
                                    }
                                    else if (result.isDocument()){ %>
                                    <a href="javascript:reportWindow('../dms/DocumentDisplay3.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&demoName=<%=(String)result.getPatientName()%> ',660,1020)"><%=(String) result.getPatientName()%></a>
                                    <% }else {%>
                                    <a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
                                    <!--a href="javascript:reportWindow('../lab/CA/BC/report.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')">2</a-->
                                    <% }%>
                                </td>
                                <td nowrap>
                                    <center><%=result.getSex() %></center>
                                </td>
                                <td nowrap>
                                    <%= (result.isAbnormal() ? "Abnormal" : "" ) %>
                                </td>
                                <td nowrap>
                                    <%=result.getDateTime()%>
                                </td>
                                <td nowrap>
                                    <%=result.getPriority()%>
                                </td>
                                <td nowrap>
                                    <%=result.getRequestingClient()%>
                                </td>
                                <td nowrap>
                                    <%=result.getDisciplineDisplayString()%>
                                </td>
                                <td nowrap>
                                    <%= (result.isFinal() ? "Final" : "Partial")%>
                                </td>
                                <td nowrap>
                                    <% int multiLabCount = result.getMultipleAckCount(); %>
                                    <%= result.getAckCount() %>&#160<% if ( multiLabCount >= 0 ) { %>(<%= result.getMultipleAckCount() %>)<%}%>
                                </td>
                            </tr>                         
                         <% } 

                            if ( total_row_index < 0 ) { %>
                            <tr>
                                <td colspan="9" align="center">
                                    <i><bean:message key="oscarMDS.index.msgNoReports"/></i>
                                </td>
                            </tr>
                         <% } %>
                            <tr class="MainTableBottomRow">
                                <td class="MainTableBottomRowRightColumn" bgcolor="#003399" colspan="10" align="left">
                                    <table width="100%">
                                        <tr>
                                            <td align="left" valign="middle" width="30%">
                                                <% if (demographicNo == null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                                <input type="button" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                                <% if (request.getParameter("fname") != null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <% if (demographicNo == null && labdocs.size() > 0) { %>
                                                    <!-- <input type="button" class="smallButton" value="Reassign" onClick="popupStart(300, 400, 'SelectProvider.jsp', 'providerselect')"> -->
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                                    <% if (ackStatus.equals("N")) {%>
                                                        <input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                                    <% }
                                                } %>                                                
                                                    <input type="checkbox" name="documentCB2" id="documentCB2" onclick="syncCB(this);checkBox();"><span class="categoryCB"><bean:message key="global.Document"/></span>
                                                    <input type="checkbox" onclick="syncCB(this);checkBox();" id="hl7CB2" name="hl7CB2"><span class="categoryCB"><bean:message key="global.hl7"/></span>
                                                    <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB2"  name="normalCB2"><span class="categoryCB"><bean:message key="global.normal"/></span>
                                                    <input type="checkbox" id="abnormalCB2" onclick="syncCB(this);checkBox();" name="abnormalCB2"><span class="categoryCB"><bean:message key="global.abnormal"/></span>
                                            </td>
                                        <script type="text/javascript">
                                                
                                        </script>
                                        </tr>
                                        <tr>
                                            <td align="center" valign="middle" width="40%">
                                                <div class="Nav">
                                                <% if ( totalNoPages>1 ) {
                                                    %>
                                                    <a id="msgPrevious" <%if(pageNum<=1){%> style="display:none;" <%}%> href="javascript:void(0);" onclick="navigatePage('Previous');">< <bean:message key="oscarMDS.index.msgPrevious"/></a>
                                                    <span id="current_individual_pages"><%
                                                      for( int i =0; i <totalNoPages; i ++){
                                                      %>
                                                       <a  style="text-decoration:none;" href="javascript:void(0);" onclick="navigatePage('<%=i+1%>')">[<%=i+1%>]</a>
                                                      <%
                                                                                                           }%></span>
                                                                                                      
                    <a id="msgNext" <%if( pageNum==totalNoPages ) {%> style="display:none;" <%}%> href="javascript:void(0);" onclick="navigatePage('Next');"><bean:message key="oscarMDS.index.msgNext"/> ></a>

                                                <%  } %>
                                                 </div>
                                            </td>
                                         
                                        </tr>
                                    </table>
                                </td>                            </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
        <table id="readerViewTable" style="display:none;table-layout: fixed;border-color: blue;border-width: thin;border-spacing: 0px;background-color: #E0E1FF" width="1080" border="1">
                                                     <col width="120">
                                                     <col width="950">
          <tr>
              <td valign="top" style="overflow:hidden;border-color: blue;border-width: thin;background-color: #E0E1FF" >
                    <%Enumeration en=patientIdNames.keys();
                        if(en.hasMoreElements()){%>
                        <div><a id="totalAll" href="javascript:void(0);" onclick="showAllDocLabs();un_bold(this);">All <span id="totalNumDocs">(<%=totalNumDocs%>)</span></a></div><br>

    <%}
                    if(totalDocs>0){
%>
<div><a id="totalDocs" href="javascript:void(0);" onclick="showCategory('DOC');un_bold(this);" title="Documents">Documents<span id="totalDocs">(<%=totalDocs%>)</span></a></div>
                     <%} if(totalHL7>0){%>
                     <div><a id="totalHL7s" href="javascript:void(0);" onclick="showCategory('HL7');un_bold(this);" title="HL7">HL7<span id="totalHL7">(<%=totalHL7%>)</span></a></div>

<%}
if(totalDocs>0||totalHL7>0){%><br><%}
                    if(normals!=null && normals.size()>0){
        Integer normalNum=normals.size();
    %>
    <div><a id="totalNormals" href="javascript:void(0);" onclick="showAb_Normal('normal','<%=normals%>');un_bold(this);" title="Normal" >Normal<span id="normalNum">(<%=normalNum%>)</span></a></div>

<%} if(abnormals!=null && abnormals.size()>0){
        Integer abnormalNum=abnormals.size() ;
    %>
    <div><a id="totalAbnormals" href="javascript:void(0);" onclick="showAb_Normal('abnormal','<%=abnormals%>');un_bold(this);" title="Abnormal">Abnormal<span id="abnormalNum">(<%=abnormalNum%>)</span></a></div>

<%}%>
<dl>
    <%
         Enumeration em=patientIdNames.keys();
         while(em.hasMoreElements()){
                        String patientId=(String)em.nextElement();
                        String patientName=(String)patientIdNames.get(patientId);
                        int numDocs=(Integer)patientNumDoc.get(patientId);
                        //get patient's doc
                        List<String> docs=(List<String>)patientDocs.get(patientId);

                        //check each doc if for it's type
                    Integer scanDocs=0;
                    Integer HL7s=0;
                    Integer CMLs=0;
                    Integer MDSs=0;
                        for(String s:docs){
                            s=s.trim();
                            String t=(String)docType.get(s);
                        if(t.equals("DOC"))
                                scanDocs++;
                        else if(t.equals("HL7"))
                                HL7s++;
                        else if(t.equals("CML"))//not used
                                CMLs++;
                        else if(t.equals("MDSs"))//not used
                                MDSs++;
                        }


   %>

   <dt><img id="plus<%=patientId%>" alt="plus" src="../images/plus.png" onclick="showhideSubCat('plus','<%=patientId%>');"/>
       <img id="minus<%=patientId%>" alt="minus" style="display:none;" src="../images/minus.png" onclick="showhideSubCat('minus','<%=patientId%>');"/>
       <a id="patient<%=patientId%>all" href="javascript:void(0);"  onclick="showThisPatientDocs('<%=patientId%>');un_bold(this);" title="<%=patientName%>"><%=patientName%> (<span id="patientNumDocs<%=patientId%>"><%=numDocs%></span>)</a>
                         <dl id="labdoc<%=patientId%>showSublist" style="display:none" >
                        <%if(scanDocs>0){%>
                        <dt><a id="patient<%=patientId%>docs" href="javascript:void(0);" onclick="showSubType('<%=patientId%>','DOC');un_bold(this);" title="Documents">Documents<span>(<%=scanDocs%>)</span></a>
                        </dt>
                     <%} if(HL7s>0){%>
                     <dt><a id="patient<%=patientId%>hl7s" href="javascript:void(0);" onclick="showSubType('<%=patientId%>','HL7');un_bold(this);" title="HL7">HL7<span>(<%=HL7s%>)</span></a>
                        </dt>
                     <%} if(CMLs>0){%>
                     <dt><a id="patient<%=patientId%>cmls" href="javascript:void(0);" onclick="showSubType('<%=patientId%>','CML');un_bold(this);" title="CML">CML<span>(<%=CMLs%>)</span></a>
                     </dt>
                    <%}
                      if(MDSs>0){%>
                      <dt><a id="patient<%=patientId%>mdss" href="javascript:void(0);" onclick="showSubType('<%=patientId%>','MDS');un_bold(this);" title="MDS">MDS<span>(<%=MDSs%>)</span></a>
                        </dt>
                     <%}%>
                    </dl>
                        <%}%>

                    </dt></dl>
             </td>
             <td style="width:100%;height:600px;background-color: #E0E1FF">
                 <div id="docViews" style="width:100%;height:600px;overflow:auto;"></div>  
             </td>
          </tr>
     </table>
</body>
</html>
