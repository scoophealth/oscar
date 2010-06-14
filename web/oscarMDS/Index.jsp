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

<%


    //oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();
    CommonLabResultData comLab = new CommonLabResultData();
    //String providerNo = request.getParameter("providerNo");
    String providerNo =  (String) session.getAttribute("user");
    String searchProviderNo = request.getParameter("searchProviderNo");
    String ackStatus = request.getParameter("status");
    String demographicNo = request.getParameter("demographicNo"); // used when searching for labs by patient instead of provider
    String scannedDocStatus = request.getParameter("scannedDocument");
    scannedDocStatus = "I";

    if ( ackStatus == null ) { ackStatus = "N"; } // default to new labs only
    if ( providerNo == null ) { providerNo = ""; }
    if ( searchProviderNo == null ) { searchProviderNo = providerNo; }
    //mDSData.populateMDSResultsData2(searchProviderNo, demographicNo, request.getParameter("fname"), request.getParameter("lname"), request.getParameter("hnum"), ackStatus);
    ArrayList labs = comLab.populateLabResultsData(searchProviderNo, demographicNo, request.getParameter("fname"), request.getParameter("lname"), request.getParameter("hnum"), ackStatus,scannedDocStatus);
    Collections.sort(labs);

    Hashtable patientDocs=new Hashtable();
    Hashtable patientIdNames=new Hashtable();
    Hashtable docStatus=new Hashtable();
    Hashtable docType=new Hashtable();
    Hashtable<String,List<String>> ab_NormalDoc=new Hashtable();
    for(int i=0;i<labs.size();i++){
        LabResultData data=(LabResultData)labs.get(i);
        List<String> segIDs=new ArrayList();
        String labPatientId=data.getLabPatientId();
        MiscUtils.getLogger().debug(labPatientId+"--"+data.patientName);
        if(data.isAbnormal()){
            List<String> abns=ab_NormalDoc.get("abnormal");
            if(abns==null){
                abns=new ArrayList();
                abns.add(data.getSegmentID());
            }else{
                abns.add(data.getSegmentID());
            }
            ab_NormalDoc.put("abnormal",abns);
        }else{
            List<String> ns=ab_NormalDoc.get("normal");
            if(ns==null){
                ns=new ArrayList();
                ns.add(data.getSegmentID());
            }else{
                ns.add(data.getSegmentID());
            }
            ab_NormalDoc.put("normal",ns);
        }
        if(patientDocs.containsKey(labPatientId)) {
            MiscUtils.getLogger().debug(labPatientId+"--"+patientDocs);
            segIDs=(List)patientDocs.get(labPatientId);
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId,segIDs);
        }else{
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId, segIDs);
            patientIdNames.put(labPatientId, data.patientName);
        }
        docStatus.put(data.getSegmentID(), data.getAcknowledgedStatus());
        docType.put(data.getSegmentID(), data.labType);
    }
    MiscUtils.getLogger().debug("docType="+docType);
    Integer totalDocs=0;
    Integer totalHL7=0;
    Hashtable<String,List<String>> typeDocLab=new Hashtable();
    Enumeration keys=docType.keys();
    while(keys.hasMoreElements()){
        String keyDocLabId=((String)keys.nextElement());
        String valType=(String)docType.get(keyDocLabId);
        if(valType.equalsIgnoreCase("DOC")){
            if(typeDocLab.containsKey("DOC")){
                List<String> docids=(List<String>)typeDocLab.get("DOC");
                docids.add(keyDocLabId);//add doc id to list
                typeDocLab.put("DOC", docids);
            }else{
                List<String> docids=new ArrayList();
                docids.add(keyDocLabId);
                typeDocLab.put("DOC", docids);
            }
            totalDocs++ ;
        }else if(valType.equalsIgnoreCase("HL7")){
            if(typeDocLab.containsKey("HL7")){
                List<String> hl7ids=(List<String>)typeDocLab.get("HL7");
                hl7ids.add(keyDocLabId);
                typeDocLab.put("HL7", hl7ids);
            }else{
                List<String> hl7ids=new ArrayList();
                hl7ids.add(keyDocLabId);
                typeDocLab.put("HL7", hl7ids);
            }
            totalHL7++  ;
        }
    }

    Hashtable patientNumDoc=new Hashtable();
    Enumeration patientIds=patientDocs.keys();
    String patientIdStr="";
    Integer totalNumDocs=0;
    while(patientIds.hasMoreElements()){
        String key=(String)patientIds.nextElement();
        patientIdStr+=key;
        patientIdStr+=",";
        List<String> val=(List<String>)patientDocs.get(key);
        Integer numDoc=val.size();
        patientNumDoc.put(key, numDoc);
        totalNumDocs+=numDoc;
    }

    //Hashtable<String,String> docPatient=new Hashtable();
    //patientIds=patientDocs.keys();
    //while
    MiscUtils.getLogger().debug("patientDocs="+patientDocs);
    MiscUtils.getLogger().debug("patientNumDoc="+patientNumDoc);
    MiscUtils.getLogger().debug("docStatus="+docStatus);
    MiscUtils.getLogger().debug("typeDocLab="+typeDocLab);
    MiscUtils.getLogger().debug("ab_NormalDoc="+ab_NormalDoc);
    List<String> normals=ab_NormalDoc.get("normal");
    List<String> abnormals=ab_NormalDoc.get("abnormal");

    MiscUtils.getLogger().debug("labs.size="+labs.size());
    HashMap labMap = new HashMap();
    LinkedHashMap accessionMap = new LinkedHashMap();
    LabResultData result;
    for( int i = 0; i < labs.size(); i++ ) {
        result = (LabResultData) labs.get(i);
        labMap.put(result.segmentID, result);
        ArrayList labNums = new ArrayList();
        if (result.accessionNumber == null || result.accessionNumber.equals("")){
            labNums.add(result.segmentID);
            accessionMap.put("noAccessionNum"+i+result.labType, labNums);
        }else if (!accessionMap.containsKey(result.accessionNumber+result.labType)){
            labNums.add(result.segmentID);
            accessionMap.put(result.accessionNumber+result.labType, labNums);

        // Different MDS Labs may have the same accession Number if they are seperated
        // by two years. So accession numbers are limited to matching only if their
        // labs are within one year of eachother
        }else{
            labNums = (ArrayList) accessionMap.get(result.accessionNumber+result.labType);
            boolean matchFlag = false;
            for (int j=0; j < labNums.size(); j++){
                LabResultData matchingResult = (LabResultData) labMap.get(labNums.get(j));

                Date dateA = result.getDateObj();
                Date dateB = matchingResult.getDateObj();
                int monthsBetween = 0;
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){
                    matchFlag = true;
                    break;
                }
            }
            if (!matchFlag){
                labNums.add(result.segmentID);
                accessionMap.put(result.accessionNumber+result.labType, labNums);
            }
        }
    }
    ArrayList labArrays = new ArrayList(accessionMap.values());
    labs.clear();
    for (int i=0; i < labArrays.size(); i++){
        ArrayList labNums = (ArrayList) labArrays.get(i);
        // must sort through in reverse to keep the labs in the correct order
        for (int j=labNums.size()-1; j >= 0; j--){
            labs.add(labMap.get(labNums.get(j)));
        }
    }
    Collections.sort(labs);

    int pageNum = 1;
    if ( request.getParameter("pageNum") != null ) {
        pageNum = Integer.parseInt(request.getParameter("pageNum"));
    }
%>


<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils.CategoryType"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils.CategoryType"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils.CategoryType"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
    <!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<!-- calendar stylesheet -->
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


<script type="text/javascript" language=javascript>

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

function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}

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

<%-- call parent window Ajax updater and then close the lab report window --%>
function wrapUp() {
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_lab");
	setTimeout("window.close();",100);
    } else {
	window.close();
    }
}

  Element.addMethods("select",(function(){
                                function getSelected(element){
                                    if(!(element=$(element))) return;
                                    var index=element.selectedIndex;
                                    return index>=0 ? element.options[index].value:undefined;
                                }
                                return {
                                    getSelected: getSelected
                                };
                            })());
                            function showAllLabs(){
                                var eles1=document.getElementsByClassName('NormalRes');
                                var eles2=document.getElementsByClassName('AbnormalRes');
                                var i=0;
                                for(i=0;i<eles1.length;i++){
                                    var ele=eles1[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else
                                        ;
                                }
                                for(i=0;i<eles2.length;i++){
                                    var ele=eles2[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else
                                        ;
                                }
                            }
                            function showOnlyScannedDocs(){
                                var eles=document.getElementsByName('scannedDoc');
                                var i=0;
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else
                                        ;
                                }
                                eles=document.getElementsByName('notScannedDoc');
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ;
                                    else
                                        ele.setStyle({display:'none'});
                                }

                            }
                            function hideScannedDocs(){
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

                                eles=document.getElementsByName('notScannedDoc');
                                for(i=0;i<eles.length;i++){
                                    var ele=eles[i];
                                    var display=ele.getStyle('display');
                                    if(display=='none')
                                        ele.setStyle({display:'table-row'});
                                    else
                                        ;
                                }
                            }
                            function toggleScannedDocs(ele){
                                var selected=$('selectScannedDocs').getSelected();
                                if(selected=='I'){
                                    //show both docs and other
                                    showAllLabs();
                                }else if(selected=='O'){
                                    //show only docs
                                    showOnlyScannedDocs();
                                }else if(selected=='N'){
                                    //hide scanned docs
                                    hideScannedDocs();
                                }
                            }
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
                                oscarLog(docTypes+"-+-"+p);
                                if(docTypes.match(p)!=null && (docTypes.match(p)).length>0){
                                    var text=(docTypes.match(p))[0];
                                    oscarLog('matched='+text);
                                    return text.split("=")[1];
                                }else
                                    return '';
                            }
                            function showDocLab(childId,docNo,providerNo,searchProviderNo,status,demoName,showhide){//showhide is 0 = document currently hidden, 1=currently shown
                                //create child element in docViews
                                docNo=docNo.replace(' ','');//trim
                                var type=checkType(docNo);
                                oscarLog('type'+type);
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

                                //oscarLog('showhide  '+$(showhide).value);
                                //if($(showhide).value==0){//show this doc
                                  //  var ele=window.frames[0].document.getElementById('labdoc_'+docNo);
                                    //if(ele==null){
                                        oscarLog('url='+url);
                                        var data="segmentID="+docNo+"&providerNo="+providerNo+"&searchProviderNo="+searchProviderNo+"&status="+status+"&demoName="+demoName;
                                        oscarLog('url='+url+'+-+ \n data='+data);
                                        new Ajax.Updater(div,url,{method:'get',parameters:data,insertion:Insertion.Bottom,evalScripts:true,onSuccess:function(transport){}});

                            }

                            function createNewElement(parent,child){
                                oscarLog('11 create new leme');
                                var newdiv=document.createElement('div');
                                oscarLog('22 after create new leme');
                                newdiv.setAttribute('id',child);
                                oscarLog('33 after create new leme');
                                //var parentdiv=document.getElementById(parent);
                               // var parentdiv=window.frames[0].document.getElementById(parent);
                               var parentdiv=$(parent);
                                //alert(parentdiv);
                                //newdiv.innerHTML='test html';
                                oscarLog('parentdiv='+parentdiv+"; child ="+newdiv);
                                parentdiv.appendChild(newdiv);
                                oscarLog('55 after create new leme');
                            }
                            function getLabDocFromPatientId(patientId){//return array of doc ids and lab ids from patient id.
                                var pattern=new RegExp(patientId+"=\\[.*?\\]","g");
                                var text='<%=patientDocs%>';
                                oscarLog(text);
                                var result=(text.match(pattern))[0];
                                oscarLog(result);
                                result=result.replace(patientId+"=[","");
                                result=result.replace("]","");
                                result=result.replace(/\s/g,"");
                                oscarLog(result);
                                var resultAr=result.split(",");
                                return resultAr;
                            }

                            function showThisPatientDocs(patientId,keepPrevious){
                                oscarLog("patientId in show this patientdocs="+patientId);
                                var labDocsArr=getLabDocFromPatientId(patientId);
                                var patientName=getPatientNameFromPatientId(patientId);
                                if(patientName!=null&&patientName.length>0){
                                        oscarLog(patientName);
                                        var childId='patient'+patientId;
                                      //if(toggleElement(childId));
                                      //else{
                                      if(keepPrevious);
                                      else clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<labDocsArr.length;i++){
                                            var docId=labDocsArr[i].replace(' ', '');

                                            var ackStatus=getAckStatusFromDocLabId(docId);
                                            oscarLog('childId='+childId+',docId='+docId+',ackStatus='+ackStatus);
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
                            function toggleElement(ele){// return true if element is present,false if not
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
                            }
                            function showAllDocLabs(){
                                var patientids='<%=patientIdStr%>';
                                var idsArr=patientids.split(',');
                                clearDocView();
                                for(var i=0;i<idsArr.length;i++){
                                    var id=idsArr[i];
                                    oscarLog("ids in showalldoclabs="+id);
                                    if(id.length>0){
                                        showThisPatientDocs(id,true);
                                        
                                    }
                                }
                               //toggleMarker('allshow');
                            }
                            //not used
                            function toggleMarker(marker){
                                oscarLog('before toggleMarker='+$(marker).value);
                                if($(marker).value==0)
                                    $(marker).value=1;
                                else
                                    $(marker).value=0;
                                oscarLog('after toggleMarker='+$(marker).value);
                            }
                            function showCategory(cat){
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
                                 oscarLog("sA="+sA);
                                 var childId="category"+cat;
                                 //if(toggleElement(childId));
                                // else{
                                clearDocView();
                                     createNewElement('docViews',childId);
                                     for(var i=0;i<sA.length;i++){
                                         var docLabId=sA[i];
                                         docLabId=docLabId.replace(/\s/g, "");
                                         oscarLog("docLabId="+docLabId);
                                         var patientId=getPatientIdFromDocLabId(docLabId);
                                         oscarLog("patientId="+patientId);
                                         var patientName=getPatientNameFromPatientId(patientId);
                                         var ackStatus=getAckStatusFromDocLabId(docLabId);
                                         oscarLog("patientName="+patientName);
                                         oscarLog("ackStatus="+ackStatus);

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
                                oscarLog(childId);
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
                                            oscarLog('check type input='+labdoc);
                                            var type=checkType(labdoc);
                                            var ackStatus=getAckStatusFromDocLabId(labdoc);
                                            var patientName=getPatientNameFromPatientId(patientId);
                                            oscarLog("type="+type+"--subType="+subType);
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
                            function showLabDocFromPatient(patientId){
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
                            }
                            var currentBold='';
                            function un_bold(ele){
                                oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                                if(currentBold==ele.id){
                                    ;
                                }else{
                                  if($(currentBold)!=null)
                                      $(currentBold).style.fontWeight='';
                                    ele.style.fontWeight='bold';
                                    currentBold=ele.id;
                                }
                                oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                            }

                            function popupConsultation(segmentId) {
                            	  var page = '<%=request.getContextPath()%>/oscarEncounter/ViewRequest.do?segmentId='+segmentId;
                            	  windowprops = "height=960,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
                            	  var popup=window.open(page, "<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsReq"/>", windowprops);
                            	  if (popup != null) {
                            	    if (popup.opener == null) {
                            	      popup.opener = self;
                            	    }
                            	  }
                            	}
                            
</script>
</head>

<body oldclass="BodyStyle" vlink="#0000FF" >
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
                                        Page : <%=pageNum%>
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
                                <% if (demographicNo == null && labs.size() > 0) { %>
                                    <!-- <input type="button" class="smallButton" value="Reassign" onClick="popupStart(300, 400, 'SelectProvider.jsp', 'providerselect')"> -->
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                    <% if (ackStatus.equals("N")) {%>
                                        <input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                    <% }
                                }%>
                                <select id="selectScannedDocs" name="scannedDocument" onchange="toggleScannedDocs(this);">
                                                    <option value="I">Include Scanned Documents</option>
                                                    <option value="O">Show Only Scanned Documents</option>
                                                    <option value="N">Hide Scanned Documents</option>
                                                  </select>
                            </td>
                            <td align="center" valign="center" width="40%" class="Nav">
                                &nbsp;&nbsp;&nbsp;
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
                                        Page : <%=pageNum%>
                                     </span>
                                <% } %>
                            </td>
                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')" style="color: #FFFFFF;"><bean:message key="global.help"/></a>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                                | <a href="javascript:popupStart(800,1000,'../lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                | <a href="javascript:popupStart(800,1000,'../dms/addDocuments.jsp')" style="color: #FFFFFF; ">Doc Upload</a>
                                | <a href="javascript:popupStart(800,1000,'../dms/addProviderQueue.jsp')" style="color: #FFFFFF; ">Add Queue</a>
                                | <a href="javascript:void(0);" onclick="changeView()" style="color: #FFFFFF;">Change view</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table id="summaryView" width="100%">
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
                            int startIndex = 0;
                            if ( request.getParameter("startIndex") != null ) {
                                startIndex = Integer.parseInt(request.getParameter("startIndex"));
                            }
                            int endIndex = startIndex+20;
                            if ( labs.size() < endIndex ) {
                                endIndex = labs.size();
                                //endIndex = labNoArray.size();
                            }

                            for (int i = startIndex; i < endIndex; i++) {


                                result =  (LabResultData) labs.get(i);
                                //LabResultData result = (LabResultData) labMap.get(labNoArray.get(i));

                                String segmentID        = (String) result.segmentID;
                                String status           = (String) result.acknowledgedStatus;

                                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                                if (!result.isMatchedToPatient()){
                                   bgcolor = "#FFCC00";
                                }

                                String discipline=result.getDiscipline();
                                if(discipline==null || discipline.equalsIgnoreCase("null"))
                                    discipline="";
                                MiscUtils.getLogger().debug("result.isAbnormal()="+result.isAbnormal());
                                %>

                                <tr bgcolor="<%=bgcolor%>" <%if(result.isDocument()){%> name="scannedDoc" <%} else{%> name="notScannedDoc" <%}%>style="display:hidden" class="<%= (result.isAbnormal() ? "AbnormalRes" : "NormalRes" ) %>">
                                <td nowrap>
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
                                    	OscarToOscarUtils.CategoryType categoryType=null;
                                    	try
                                    	{
                                    		categoryType=OscarToOscarUtils.CategoryType.valueOf(result.getDiscipline());
                                    	}
                                    	catch (Exception e)
                                    	{
                                    		// this okay, it means it's not a special category
                                    	}
                                   		
                                    	if (categoryType==OscarToOscarUtils.CategoryType.REFERRAL)
                                    	{
	                                    	%>
                                      			<a href="javascript:popupConsultation('<%=segmentID%>')"><%=(String) result.getPatientName()%></a>
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
                                    <a href="javascript:reportWindow('../dms/DocumentDisplay3.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&demoName=<%=(String)result.getPatientName()%> ')"><%=(String) result.getPatientName()%></a>
                                    <% }else {%>
                                    <a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
                                    <!--a href="javascript:reportWindow('../lab/CA/BC/report.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')">2</a-->
                                    <% }%>
                                </td>
                                <td nowrap>
                                    <center><%= (String) result.getSex() %></center>
                                </td>
                                <td nowrap>
                                    <%= (result.isAbnormal() ? "Abnormal" : "" ) %>
                                </td>
                                <td nowrap>
                                    <%= (String) result.getDateTime()%>
                                </td>
                                <td nowrap>
                                    <%= (String) result.getPriority()%>
                                </td>
                                <td nowrap>
                                    <%= (String) result.getRequestingClient()%>
                                </td>
                                <td nowrap>
                                    <%= StringUtils.maxLenString( discipline, 13, 10, "...") %>
                                </td>
                                <td nowrap>
                                    <%= ( (String) ( result.isFinal() ? "Final" : "Partial") )%>
                                </td>
                                <td nowrap>
                                    <% int multiLabCount = result.getMultipleAckCount(); %>
                                    <%= result.getAckCount() %>&#160<% if ( multiLabCount >= 0 ) { %>(<%= result.getMultipleAckCount() %>)<%}%>
                                </td>
                            </tr>
                         <% }

                            if ( endIndex == 0 ) { %>
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
                                                <% if (demographicNo == null && labs.size() > 0) { %>
                                                    <!-- <input type="button" class="smallButton" value="Reassign" onClick="popupStart(300, 400, 'SelectProvider.jsp', 'providerselect')"> -->
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                                    <% if (ackStatus.equals("N")) {%>
                                                        <br><input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                                    <% }
                                                } %>
                                            </td>
                                            <td align="center" valign="middle" width="40%">
                                                <div class="Nav">
                                                <% if ( pageNum > 1 || labs.size() > endIndex ) {
                                                 //if (pageNum > 1 || labNoArray.size() > endIndex ) {
                                                    if ( pageNum > 1 ) { %>
                                                        <a href="Index.jsp?providerNo=<%=providerNo%><%= (demographicNo == null ? "" : "&demographicNo="+demographicNo ) %>&searchProviderNo=<%=searchProviderNo%>&status=<%=ackStatus%><%= (request.getParameter("lname") == null ? "" : "&lname="+request.getParameter("lname")) %><%= (request.getParameter("fname") == null ? "" : "&fname="+request.getParameter("fname")) %><%= (request.getParameter("hnum") == null ? "" : "&hnum="+request.getParameter("hnum")) %>&pageNum=<%=pageNum-1%>&startIndex=<%=startIndex-20%>">< <bean:message key="oscarMDS.index.msgPrevious"/></a>
                                                 <% } else { %>
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                 <% } %>
                                                    <%int count = 1;
                                                      for( int i =0; i < labs.size(); i = i +20){
                                                      //for ( int i=0; i < labNoArray.size(); i = i+20){%>
                                                      <a style="text-decoration:none;" href="Index.jsp?providerNo=<%=providerNo%><%= (demographicNo == null ? "" : "&demographicNo="+demographicNo ) %>&searchProviderNo=<%=searchProviderNo%>&status=<%=ackStatus%><%= (request.getParameter("lname") == null ? "" : "&lname="+request.getParameter("lname")) %><%= (request.getParameter("fname") == null ? "" : "&fname="+request.getParameter("fname")) %><%= (request.getParameter("hnum") == null ? "" : "&hnum="+request.getParameter("hnum")) %>&pageNum=<%=count%>&startIndex=<%=i%>">[<%=count%>]</a>
                                                      <%count++;
                                                      }%>
                                                 <% if ( labs.size() > endIndex ) {
                                                    //if ( labNoArray.size() > endIndex ) {
                    %>
                                                        <a href="Index.jsp?providerNo=<%=providerNo%><%= (demographicNo == null ? "" : "&demographicNo="+demographicNo ) %>&searchProviderNo=<%=searchProviderNo%>&status=<%=ackStatus%><%= (request.getParameter("lname") == null ? "" : "&lname="+request.getParameter("lname")) %><%= (request.getParameter("fname") == null ? "" : "&fname="+request.getParameter("fname")) %><%= (request.getParameter("hnum") == null ? "" : "&hnum="+request.getParameter("hnum")) %>&pageNum=<%=pageNum+1%>&startIndex=<%=startIndex+20%>"><bean:message key="oscarMDS.index.msgNext"/> ></a>

                                                 <% } else { %>
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                 <% }
                                                   } %>
                                                 </div>
                                            </td>
                                            <td align="right" width="30%">
                                               &nbsp;
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
                                                 <table id="readerViewTable" style="display:none;table-layout: fixed" width="1080" border="1">
                                                     <col width="120">
                                                     <col width="950">
          <tr>
              <td valign="top" style="overflow:hidden" >
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
                        else if(t.equals("CML"))
                                CMLs++;
                        else if(t.equals("MDSs"))
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
             <td style="width:100%;height:600px">
                 <div id="docViews" style="width:100%;height:600px;overflow:auto;"></div>  
             </td>
          </tr>
     </table>
</body>
</html>
