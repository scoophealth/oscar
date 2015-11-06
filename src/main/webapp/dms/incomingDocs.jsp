<%--

    Copyright (c) 2012- Centre de Medecine Integree

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

    This software was written for
    Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
    as part of the OSCAR McMaster EMR System

--%>
    
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

  
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="oscar.util.UtilDateUtilities"%>
<%@page import="java.io.File"%>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
    
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.common.dao.ProviderLabRoutingDao,org.oscarehr.common.dao.DemographicDao, org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.dao.CtlDocClassDao,org.oscarehr.common.dao.QueueDao" %>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.ProviderLabRoutingModel" %>
    
<%@page import="oscar.dms.IncomingDocUtil" %>
    
<jsp:useBean id="LastPatientsBean" class="java.util.ArrayList" scope="session" />
    
    
<style type="text/css">
    .autocomplete_style {
        background: #fff;
        text-align: left;
        z-index:2;
    }
        
    .autocomplete_style ul {
        border: 1px solid #aaa;
        margin: 0px;
        padding: 2px;
        list-style: none;
    }
        
    .autocomplete_style ul li.selected {
        background-color: #ffa;
        text-decoration: underline;
    }
    
    .multiPage {
        background-color: RED;
        color: WHITE;
        font-weight:bold;
        padding: 0px 5px;
        font-size: medium;
    }
    .singlePage {
        
    }
    .topalign {
        vertical-align:text-top;
    } 
</style>
    
<%
    String user_no = (String) session.getAttribute("user");
        
    String imageType = IncomingDocUtil.getAndSetViewDocumentAs(user_no, request.getParameter("imageType"));
    String queueIdStr = IncomingDocUtil.getAndSetIncomingDocQueue(user_no, request.getParameter("defaultQueue"));
    String entryMode = IncomingDocUtil.getAndSetEntryMode(user_no, request.getParameter("entryMode"));

    UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
    UserProperty uProp = userPropertyDAO.getProp(user_no, UserProperty.DOCUMENT_DESCRIPTION_TEMPLATE);                        
    String useDocumentDescriptionTemplateType=UserProperty.CLINIC;
    if( uProp != null && uProp.getValue().equals(UserProperty.USER)) {
        useDocumentDescriptionTemplateType=UserProperty.USER;
    }           
        
    // add to most recent patient list
    int listsize = LastPatientsBean.size();
    String ptid = request.getParameter("lastdemographic_no") == null ? "" : request.getParameter("lastdemographic_no");
    if (ptid.length() > 0) {
        if (LastPatientsBean.contains(ptid) == true) {
            LastPatientsBean.remove(LastPatientsBean.indexOf(ptid));
            LastPatientsBean.add(ptid);
        } else {
            LastPatientsBean.add(ptid);
            if (listsize > 2) {
                LastPatientsBean.remove(0);
            }
        }
    }
        
    java.util.Locale vLocale = (java.util.Locale) session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    ProviderLabRoutingDao providerLabRoutingDao = (ProviderLabRoutingDao) ctx.getBean("providerLabRoutingDao");
    ProviderDao providerDao = (ProviderDao) ctx.getBean("providerDao");
    DemographicDao demographicDao = (DemographicDao) ctx.getBean("demographicDao");
    QueueDao queueDao = (QueueDao) ctx.getBean("queueDao");

    CtlDocClassDao docClassDao = (CtlDocClassDao) ctx.getBean("ctlDocClassDao");
    List<String> reportClasses = docClassDao.findUniqueReportClasses();
    ArrayList<String> subClasses = new ArrayList<String>();
    ArrayList<String> consultA = new ArrayList<String>();
    ArrayList<String> consultB = new ArrayList<String>();
    for (String reportClass : reportClasses) {
        List<String> subClassList = docClassDao.findSubClassesByReportClass(reportClass);
        if (reportClass.equals("Consultant ReportA")) {
            consultA.addAll(subClassList);
        } else if (reportClass.equals("Consultant ReportB")) {
            consultB.addAll(subClassList);
        } else {
            subClasses.addAll(subClassList);
        }
            
        if (!consultA.isEmpty() && !consultB.isEmpty()) {
            for (String partA : consultA) {
                for (String partB : consultB) {
                    subClasses.add(partA + " " + partB);
                }
            }
        }
    }
        
        
    List<Integer> routingIdList=providerLabRoutingDao.findLastRoutingIdGroupedByProviderAndCreatedByDocCreator(user_no);
    Collections.sort(routingIdList,Collections.reverseOrder());
    
    List<Hashtable> queues = queueDao.getQueues();
    int queueId = 1;
    if (queueIdStr != null) {
        queueIdStr = queueIdStr.trim();
        queueId = Integer.parseInt(queueIdStr);
    }
        
    String errorMessage = "";
    String pdfNo = "";
    String pdfDir = request.getParameter("pdfDir") == null ? "Fax" : request.getParameter("pdfDir");
    String pdfDirectory = IncomingDocUtil.getIncomingDocumentFilePath(queueIdStr, pdfDir);
    String pdfAction = request.getParameter("pdfAction") == null ? "" : request.getParameter("pdfAction");
    String pdfPageNumber = request.getParameter("pdfPageNumber") == null ? "1" : request.getParameter("pdfPageNumber");
    String pdfName = request.getParameter("pdfName") == null ? "" : request.getParameter("pdfName");
    String pdfExtractPageNumber = request.getParameter("pdfExtractPageNumber") == null ? "" : request.getParameter("pdfExtractPageNumber");
        
    try {
        IncomingDocUtil.doPagesAction(pdfAction, queueIdStr, pdfDir, pdfName, pdfPageNumber, pdfExtractPageNumber, vLocale);
    } catch (Exception e) {
        errorMessage= e.getMessage();
    }
  
        
    IncomingDocUtil myIncomingDocUtil = new IncomingDocUtil();
    ArrayList pdfList = myIncomingDocUtil.getDocList(pdfDirectory);
    ArrayList pdfListModifiedDate = myIncomingDocUtil.getPdfListModifiedDate();
        
    pdfNo = request.getParameter("pdfNo") == null ? "1" : request.getParameter("pdfNo");
    if (Integer.parseInt(pdfNo) <= 0) {
            pdfNo = "1"; 
    }
    
    if (pdfList.size() < Integer.parseInt(pdfNo)) {
        pdfNo = (new Integer(pdfList.size())).toString();
    }
        
    int PdfIndex = Integer.parseInt(pdfNo) - 1;
    if (pdfList.size() >= 1 && PdfIndex <= (pdfList.size() - 1)) {
        pdfName = (String) pdfList.get(PdfIndex);
    } else {
        pdfName = "";
    }

    ArrayList docTypes = EDocUtil.getDoctypes("demographic");
    String todayDate = UtilDateUtilities.DateToString(new Date(),"yyyy-MM-dd");


    int tabIndex = 0;
    int numOfPage = 0;
    if (!pdfName.isEmpty()) {
    numOfPage = IncomingDocUtil.getNumOfPages(queueIdStr, pdfDir, pdfName);
    }
            
    if (Integer.parseInt(pdfPageNumber) > numOfPage) {
        pdfPageNumber = String.valueOf(numOfPage);
    }
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
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
            
       <script type="text/javascript" src="../share/javascript/nifty.js"></script>
             
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />
        <script type="text/javascript">
            var curPage=<%=pdfPageNumber%>;
            var totalPage=<%=numOfPage%>;
            
            function popupPage(vheight,vwidth,varpage) { 
                var page = "" + varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=0,top=0,left=0";
                var popup=window.open(page, "PopUp", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }    
                    popup.focus();
                }
            }
    
            function loadSelectedPdf(pdfDir) {
                seldocobj=document.getElementById('SelectPdfList');
                pdfNo=seldocobj.selectedIndex;
                if(pdfNo>0) {
                    loadPdf(pdfNo,pdfDir);
                }
                else {alert("<bean:message key="dms.incomingDocs.nothingSelected" />");}
                
            }
            
            function loadSelectedPage(queueId,pdfDir,pdfName) {
                seldocobj=document.getElementById('SelectPageList');
                PageNo=seldocobj.selectedIndex;
                
                if(PageNo>0) {
                    curPage=PageNo;
                    showPageImg(queueId,pdfDir,pdfName,curPage);
                }
                else {alert("<bean:message key="dms.incomingDocs.nothingSelected" />");}
            }
            
            function loadPdf(pdfNo,pdfDir) {
                if(parseInt(pdfNo, 10)<=0) { pdfNo="1";}
                document.PdfInfoForm.pdfName.value="";
                document.PdfInfoForm.pdfNo.value=pdfNo;
                document.PdfInfoForm.pdfDir.value=pdfDir;
                document.PdfInfoForm.submit();
            }
            
            function setImageType() {
                document.PdfInfoForm.imageType.value=document.getElementById('imageTypeList').options[document.getElementById('imageTypeList').selectedIndex].value;
                document.PdfInfoForm.pdfPageNumber.value=curPage;
                document.PdfInfoForm.submit();
            }
            
            function setEntryMode() {
                document.PdfInfoForm.entryMode.value=document.getElementById('entryModeList').options[document.getElementById('entryModeList').selectedIndex].value;
                document.PdfInfoForm.pdfPageNumber.value=curPage;
                document.PdfInfoForm.submit();
            }
            
            function setQueue(){
                document.PdfInfoForm.defaultQueue.value=document.getElementById('queueList').options[document.getElementById('queueList').selectedIndex].value;
                document.PdfInfoForm.submit();
            }
            
            function rotatePdf(pdfNo,pdfDir,pdfName,degrees) {
                if(totalPage==0)
                {
                    alert("<bean:message key="dms.incomingDocs.selectDocumentFirst" />");
                }
                else
                {
                    document.PdfInfoForm.pdfNo.value=pdfNo;
                    document.PdfInfoForm.pdfDir.value=pdfDir;
                    document.PdfInfoForm.pdfName.value=pdfName;
                    document.PdfInfoForm.pdfAction.value="Rotate"+degrees;
                    document.PdfInfoForm.pdfPageNumber.value=curPage;
                    document.PdfInfoForm.submit();
                }       
            }
            
            function rotateAllPagePdf(pdfNo,pdfDir,pdfName,degrees) {
                if(totalPage==0)
                {
                    alert("<bean:message key="dms.incomingDocs.selectDocumentFirst" />");
                }
                else
                {
                    document.PdfInfoForm.pdfNo.value=pdfNo;
                    document.PdfInfoForm.pdfDir.value=pdfDir;
                    document.PdfInfoForm.pdfName.value=pdfName;
                    document.PdfInfoForm.pdfAction.value="RotateAll"+degrees;
                    document.PdfInfoForm.pdfPageNumber.value=curPage;
                    document.PdfInfoForm.submit();
                }
                
            }
            
            function deletePdf(pdfNo,pdfDir,pdfName) {
                if(pdfNo<=0)
                {
                    alert("<bean:message key="dms.incomingDocs.selectDocumentFirst" />");
                } else {
                    var answer = confirm("<bean:message key="dms.incomingDocs.areYouSureToDelete" />"+pdfName+ " ?");
                    if (answer){
                        document.PdfInfoForm.pdfNo.value=pdfNo;
                        document.PdfInfoForm.pdfDir.value=pdfDir;
                        document.PdfInfoForm.pdfName.value=pdfName;
                        document.PdfInfoForm.pdfAction.value="DeletePDF";                
                        document.PdfInfoForm.submit();
                    }
                }
            }
            
            function deletePagePdf(pdfNo,pdfDir,pdfName) {
                if(totalPage==0) {
                    alert("<bean:message key="dms.incomingDocs.nothingToDelete" />");
                } 
                else if(totalPage==1) {
                    alert("<bean:message key="dms.incomingDocs.onlyOneToDeleteUseDeletePDF" />");
                }
                else {
                    var answer = confirm("<bean:message key="dms.incomingDocs.areYouSureToDeletePage" />"+curPage);
                    if (answer){
                        document.PdfInfoForm.pdfNo.value=pdfNo;
                        document.PdfInfoForm.pdfDir.value=pdfDir;
                        document.PdfInfoForm.pdfName.value=pdfName;
                        document.PdfInfoForm.pdfAction.value="DeletePage";
                        document.PdfInfoForm.pdfPageNumber.value=curPage;
                        document.PdfInfoForm.submit();
                    }
                }
            }
            
            function extractPagePdf(pdfNo,pdfDir,pdfName) {
                var validPages=true;
                if(totalPage<=1) {
                    alert("<bean:message key="dms.incomingDocs.nothingToExtract" />");
                }
                else {
                    var range=prompt("<bean:message key="dms.incomingDocs.enterPagesToExtract" /> ","1-"+curPage);
                    var rangestr="";
                    if (range==null || range=="") {validPages=false;}
                    range=trim(range);
                    
                    var numbers = [];
                    var ranges = range.split(',');
                    
                    for (var i = 0; i < ranges.length; i++) {
                        if(ranges[i].length==0) {validPages=false;}
                        if (ranges[i]) {
                            var ranges1=ranges[i].split('-');
                            if(ranges1.length>2) { validPages=false;}
                            for (var j = 0; j < ranges1.length; j++) {
                                var re=/^[0-9]+$/;
                                if(!re.test(ranges1[j])) {validPages=false;}
                            }
                            if(validPages) {
                                var range3 = ranges[i].concat('-' + ranges[i]).split('-');
                                for (var k = parseInt(range3[0], 10); k <= parseInt(range3[1], 10); k++) {
                                    if(k><%=numOfPage%>) {validPages=false;}
                                    if(k==0) {validPages=false;}
                                    numbers[k] = k;
                                }
                            }
                        }
                    }
                    
                    if(validPages) {
                        var notwholedoc=false;
                        for (var m = 1; m < numbers.length; m++) {
                            if (numbers[m]==null) {
                                notwholedoc=true;
                            }
                        }
                        if(!notwholedoc) {
                            if((numbers.length-1)==<%=numOfPage%>) {validPages=false;}
                        }
                    }
                    
                    if(validPages) {
                        document.PdfInfoForm.pdfNo.value=pdfNo;
                        document.PdfInfoForm.pdfDir.value=pdfDir;
                        document.PdfInfoForm.pdfName.value=pdfName;
                        document.PdfInfoForm.pdfAction.value="ExtractPagePDF";
                        document.PdfInfoForm.pdfPageNumber.value=curPage;
                        document.PdfInfoForm.pdfExtractPageNumber.value=range;
                        document.PdfInfoForm.submit();
                    }
                    else
                    {
                        alert("<bean:message key="dms.incomingDocs.invalidPages" />");
                    }
                }
            }
            
            function printPdf(queueId,pdfDir,pdfName) {
                if(totalPage==0)
                {
                    alert("<bean:message key="dms.incomingDocs.selectDocumentFirst" />");
                } 
                else {
                    var url='<c:out value="${ctx}"/>/dms/ManageDocument.do?method=displayIncomingDocs'
                        +'&pdfDir='+encodeURIComponent(pdfDir)+'&queueId='+queueId+'&pdfName='+encodeURIComponent(pdfName);
                    popupPage(700,960,url);
                }
            }
            
            function loadRecentDemo(thisdemoid,thisDemoName)
            {
                var demogObj = document.getElementById('demofind');
                var autodemoObj = document.getElementById('autocompletedemo');
                var saveObj = document.getElementById('save');
                
                demogObj.value=thisdemoid;
                autodemoObj.value=thisDemoName;
                saveObj.disabled=false;
                
            }
            
            function getWidth() {
                var myWidth = 0;
                if( typeof( window.innerWidth ) == 'number' ) {
                    //Non-IE
                    myWidth = window.innerWidth;
                } else if( document.documentElement &&  document.documentElement.clientWidth  ) {
                    //IE 6+ in 'standards compliant mode'
                    myWidth = document.documentElement.clientWidth;
                } else if( document.body && document.body.clientHeight  ) {
                    //IE 4 compatible
                    myWidth = document.body.clientWidth;
                }
                return myWidth;
            }
            
            
            function getHeight() {
                var myHeight = 0;
                if( typeof( window.innerHeight ) == 'number' ) {
                    //Non-IE                    
                    myHeight = window.innerHeight;
                } else if( document.documentElement && document.documentElement.clientHeight  ) {
                    //IE 6+ in 'standards compliant mode'                    
                    myHeight = document.documentElement.clientHeight;
                } else if( document.body && (document.body.clientHeight ) ) {
                    //IE 4 compatible                    
                    myHeight = document.body.clientHeight;
                }
                return myHeight;
            }
            
            function showPageImg(queueId,pdfDir,pdfName,pn) {
                var url="";
                var height=700;
                if(getHeight()>750) {
                    height=getHeight()-50;
                }

                var width=800;
                if(getWidth()>1250)
                {
                    width=getWidth()-450;
                }
                
                if(pdfName.length==0) {
                    
                    url="";
                    document.getElementById('pgnum').innerHTML = '';
                    document.getElementById('docdisp').innerHTML = '<iframe	src=""  width="800" height="900" ></iframe>';
                } else  {
                    document.getElementById('pgnum').innerHTML = pn+ ' of <span class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>">'+totalPage+'</span>';
                    
                    if(document.PdfInfoForm.imageType.value=="Pdf") {
                        url='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewIncomingDocPageAsPdf'
                            +'&curPage='+pn+'&pdfDir='+encodeURIComponent(pdfDir)+'&queueId='+queueId+'&pdfName='+encodeURIComponent(pdfName)+"#view=fitV";
                    }
                    else
                    {
                        url='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewIncomingDocPageAsImage'
                            +'&curPage='+pn+'&pdfDir='+encodeURIComponent(pdfDir)+'&queueId='+queueId+'&pdfName='+encodeURIComponent(pdfName);
                    }
                    document.getElementById('docdisp').innerHTML = '<iframe	src="' +url +'"  width="'+(width)+'" height="'+(height)+'" ></iframe>';
                }    
            }
            
            function nextPage(queueId,pdfDir,pdfName){
                curPage++;
                if(curPage>=totalPage){
                    curPage=totalPage;
                }
                showPageImg(queueId,pdfDir,pdfName,curPage);
            }
            
            function prevPage(queueId,pdfDir,pdfName){
                curPage--;
                if(curPage<1){
                    curPage=1;
                }
                showPageImg(queueId,pdfDir,pdfName,curPage);
            }
            
            function firstPage(queueId,pdfDir,pdfName){
                if(totalPage>0) {curPage=1;} else {curPage=0;}
                showPageImg(queueId,pdfDir,pdfName,curPage);
            }
            
            function lastPage(queueId,pdfDir,pdfName){
                curPage=totalPage;
                showPageImg(queueId,pdfDir,pdfName,curPage);
            }
            
            function selectDocDesc(desc){
                var docDescObj = document.getElementById('documentDescription');
                docDescObj.value=desc;
            }
            
            function selectDocType(num) {
                var selObj = document.getElementById('docType');
                selObj.selectedIndex = num;
                addDocumentDescriptionTemplateButton();
            }    
                
            
            function trim(stringToTrim) {
                return stringToTrim.replace(/^\s+|\s+$/g,"");
            }
            
            function checkDocument(){
                var n="<%=pdfName%>";
                if(n.length==0) {
                    alert("<bean:message key="dms.incomingDocs.nothingToSave" />");
                    return false;
                }
                
                               
                if(totalPage==0) {
                    alert("<bean:message key="dms.incomingDocs.nothingToSave" />");
                    return false;
                }
                
                var selObj = document.getElementById('docType');
                var selIndex = selObj.selectedIndex;
                if(selIndex==0) {
                    alert("<bean:message key="dms.incomingDocs.missingDocumentType" />");
                    return false;
                }
                
                var docDescObj = document.getElementById('documentDescription');
                
                var a=trim(docDescObj.value);
                if(a.length==0)
                {
                    alert("<bean:message key="dms.incomingDocs.missingDocumentDescription" />");
                    return false;
                }


                if (!validDate("observationDate")) {
                        alert("<bean:message key="dms.incomingDocs.invalidDate" />");
                    return false;
                }
                
                if(document.PdfInfoForm.pdfDir.value!="File")  {
                    var flagproviderObj = document.getElementsByName('flagproviders');
                    if(flagproviderObj.length==0) {
                        if (!confirm("<bean:message key="dms.incomingDocs.saveWithoutFlagging" />"))
                        {
                            return false;
                        }
                    }
                }
                

                document.getElementById('lastdemographic_no').value=document.getElementById('demofind').value;
                return true;
            }
            
            function removeThisProv(th){
                var parent = th.parentNode;
                var parent1 = parent.parentNode;
                parent1.removeChild(parent);
            }
            
            function loadMaster()
            {
                var demogObj = document.getElementById('demofind');
                var demo=demogObj.value;
                if(demo=="-1") {alert("<bean:message key="dms.incomingDocs.selectDemographicFirst" />");} else {
                    popupPage(710,1024,'<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?demographic_no='+demo+'&displaymode=edit&dboperation=search_detail');
                }
            }
            
            function loadApptHistory()
            {
                var demogObj = document.getElementById('demofind');
                var demo=demogObj.value;
                
                if(demo=="-1") {alert("<bean:message key="dms.incomingDocs.selectDemographicFirst" />");} else {
                    popupPage(710,1024,'<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?demographic_no='+demo+'&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25');
                }
            }              
            
            function setDescriptionIfEmpty()
            {
                var docDescObj = document.getElementById('documentDescription');
                
                var a=trim(docDescObj.value);
                if(a.length==0)
                {
                    docDescObj.value=document.getElementById('docSubClass').value;
                }
                
            }
            
            window.onload=function(){
                new Autocompleter.Local('docSubClass', 'docSubClass_list', docSubClassList);
                if(!NiftyCheck())
                    return;
            }
            
            var docSubClassList = [
            <% for (int i = 0; i < subClasses.size(); i++) {%>
                    "<%=subClasses.get(i)%>"<%=(i < subClasses.size() - 1) ? "," : ""%>
            <% }%>
                ];
                
            if(typeof(window.external) != 'undefined'){
                //yes, this is evil browser sniffing, but only IE has this bug
                document.getElementsByName = function(name, tag){
                    if(!tag){
                        tag = '*';
                    }
                    
                    var elems = document.getElementsByTagName(tag);
                    var res = [];
                    var att="";
                    //alert("len:"+elems.length+" "+name);
                    for(var i=0;i<elems.length;i++){
                        att = elems[i].getAttribute('name');
                        //alert(att);
                        if(att == name) {
                            //      alert("att"+att);
                            res.push(elems[i]);
                            //    alert("elem"+elems[i]);
                        }
                    }
                    return res;
                }
                
            }
            
            renderCalendar=function(id,inputFieldId){
                Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
            }
            
            
            function addMRP()
            {
                var MRPName=document.getElementById('MRPName').value;
                var MRPNo=document.getElementById('MRPNo').value;
                var demo = document.getElementById('demofind').value;
                if(demo=="-1") {alert("<bean:message key="dms.incomingDocs.selectDemographicFirst" />");} else
                {
                    if(MRPNo!=null && MRPNo.length>0) {
                        addflagprovider(MRPName,"(<bean:message key="dms.incomingDocs.MRP" />)",MRPNo);
                    }
                    else {
                        alert("<bean:message key="dms.incomingDocs.noMRP" />");
                    }
                }
            }
            
            function addDocumentDescriptionTemplateButton() {
                if(document.PdfInfoForm.entryMode.value=="Fast") {   
                    var bdoc;
                    var docDescriptionList;
                    var docType=document.getElementById('docType').options[document.getElementById('docType').selectedIndex].value;

                    docDescriptionList = $('docDescriptionList');
                    while( docDescriptionList.hasChildNodes() ) { docDescriptionList.removeChild( docDescriptionList.lastChild ); }
                
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=getDocumentDescriptionFromDocType&doctype='+docType+"&providerNo=<%=user_no%>&useDocumentDescriptionTemplateType=<%=useDocumentDescriptionTemplateType%>";
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                        if(json!=null ){
                            for (var i = 0; i < json.documentDescriptionTemplate.length; i++) {
                                
                                bdoc = document.createElement('input');
                                bdoc.setAttribute("type", "button");
                                bdoc.setAttribute("value", json.documentDescriptionTemplate[i].descriptionShortcut);
                                bdoc.setAttribute("title", json.documentDescriptionTemplate[i].description);
                                bdoc.setAttribute("onclick", "selectDocDesc('"+json.documentDescriptionTemplate[i].description+"');");

                                docDescriptionList = $('docDescriptionList');
                                docDescriptionList.appendChild(bdoc);
                            }
                        }
                    }});
                }
            }
        </script>
    </head>
    <body> 
        <table>                
            <tr>                   
                <td align="left" valign="top" >
                    <form  method="post" name="PdfInfoForm" action="incomingDocs.jsp" >
                        <input type="hidden" name="pdfNo" value="<%=pdfNo%>">
                        <input type="hidden" name="pdfDir" value="<%=pdfDir%>">
                        <input type="hidden" name="pdfName" value="<%=pdfName%>">
                        <input type="hidden" name="pdfAction" value="">
                        <input type="hidden" name="pdfPageNumber" value="1">
                        <input type="hidden" name="pdfExtractPageNumber" value="">
                        <input type="hidden" name="imageType" value="<%=imageType%>">
                        <input type="hidden" name="defaultQueue" value="<%=queueIdStr%>">
                        <input type="hidden" name="entryMode" value="<%=entryMode%>">
                        <table  width="350">
                            <%if (errorMessage.length() > 0) {%><tr><td><b><font color="red"><%=errorMessage%></font></b></td></tr><%}%>
                            <tr><td><bean:message key="dms.incomingDocs.queue" />:
                                    <select  id="queueList" name="queueList" onchange="setQueue();">
                                        <%
                                            for (Hashtable ht : queues) {
                                                int id = (Integer) ht.get("id");
                                                String qName = (String) ht.get("queue");
                                        %>
                                        <option value="<%=id%>" <%=((id == queueId) ? " selected" : "")%>><%= qName%> </option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr><td><input type="button" value="<bean:message key="dms.incomingDocs.fax" />" onclick="loadPdf('1','Fax');"> 
                                    <input type="button" value="<bean:message key="dms.incomingDocs.mail" />" onclick="loadPdf('1','Mail');">
                                    <input type="button" value="<bean:message key="dms.incomingDocs.file" />" onclick="loadPdf('1','File');">
                                    <input type="button" value="<bean:message key="dms.incomingDocs.refile" />" onclick="loadPdf('1','Refile');">
                                        
                                </td>
                            </tr>
                            <tr><td>
                                    <fieldset>
                                        <legend>[<%=pdfDir%>]: <% if (Integer.parseInt(pdfNo) <= 0) {%><bean:message key="dms.incomingDocs.noFile" /><% } else {%> <%=pdfNo%> / <%=pdfList.size()%> <b><%=pdfList.get(Integer.parseInt(pdfNo) - 1)%></b> <%}%> </legend>
                                        <table>
                                            <tr><td>
                                                    <select tabIndex="<%=tabIndex++%>" name ="SelectPdfList" id="SelectPdfList" onchange="loadSelectedPdf('<%=pdfDir%>');">
                                                        <option value=""><bean:message key="dms.incomingDocs.selectPDF" /></option>
                                                        <%for (int p = 0; p < pdfList.size(); p++) {
                                                                String docName = (String) pdfList.get(p);
                                                                String docModifiedDate = (String) pdfListModifiedDate.get(p);
                                                        %>
                                                        <option value="<%= docName%>"  title="<%=docName%>" ><%=p + 1%>) <%=docModifiedDate%></option>
                                                        <%}%>
                                                    </select></td>
                                            </tr>
                                            <tr><td><input type="button" value="<bean:message key="dms.incomingDocs.first" />" onclick="loadPdf('1','<%=pdfDir%>');">
                                                    <input type="button" value="<bean:message key="dms.incomingDocs.previous" />" onclick="loadPdf('<%=Integer.parseInt(pdfNo) - 1%>','<%=pdfDir%>');" >
                                                    <input type="button" value="<bean:message key="dms.incomingDocs.next" />" onclick="loadPdf('<%=Integer.parseInt(pdfNo) + 1%>','<%=pdfDir%>');"> 
                                                    <input type="button" value="<bean:message key="dms.incomingDocs.last" />" onclick="loadPdf('<%=pdfList.size()%>','<%=pdfDir%>');">
                                                </td>
                                            </tr>
                                            <tr><td><input type="button"  value=" <bean:message key="global.btnPrint"/> " onClick="printPdf('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                    <input type="button" value="<bean:message key="dms.incomingDocs.deletePDF" />" onclick="deletePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>');"></td>
                                            </tr>
                                        </table>
                                    </fieldset>
                                    <fieldset>
                                        <legend><bean:message key="dms.incomingDocs.page" /></legend>
                                        <table>
                                            <tr><td>
                                                    <select tabIndex="<%=tabIndex++%>" name ="SelectPageList" id="SelectPageList" onchange="loadSelectedPage('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                        <option value=""><bean:message key="dms.incomingDocs.selectPage" /></option>
                                                        <%for (int p = 1; p <= numOfPage; p++) {
                                                        %>
                                                        <option value="<%=p%>" ><%=p%></option>
                                                        <%}%>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr><td><input type="button" id="firstP" value="<bean:message key="dms.incomingDocs.first" />" onclick="firstPage('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                    <input type="button"  value="<bean:message key="dms.incomingDocs.previous" />"  onclick="prevPage('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                    <input type="button"  value="<bean:message key="dms.incomingDocs.next" />" onclick="nextPage('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                    <input type="button"  value="<bean:message key="dms.incomingDocs.last" />" onclick="lastPage('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>');">
                                                        
                                                </td>
                                            </tr>
                                            <tr><td><bean:message key="dms.incomingDocs.rotateThisPage" />:<input type="button" value="180" onclick="rotatePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','180');">
                                                    <input type="button" value="+90" onclick="rotatePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','90');">
                                                    <input type="button" value="-90" onclick="rotatePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','M90');"></td></tr>
                                            <tr><td><bean:message key="dms.incomingDocs.rotateAllPages" />:<input type="button" value="180" onclick="rotateAllPagePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','180');">
                                                    <input type="button" value="+90" onclick="rotateAllPagePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','90');">
                                                    <input type="button" value="-90" onclick="rotateAllPagePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>','M90');"></td></tr>
                                            <tr><td><input type="button" value="<bean:message key="dms.incomingDocs.extractPage" />" onclick="extractPagePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>');">
                                                    <input type="button" value="<bean:message key="dms.incomingDocs.deletePage" />" onclick="deletePagePdf('<%=pdfNo%>','<%=pdfDir%>','<%=pdfName%>');">
                                                </td>
                                            </tr>
                                        </table>
                                    </fieldset>
                                </td>
                            </tr>
                        </table>
                    </form>
                    <fieldset>
                        <legend><bean:message key="dms.incomingDocs.dataEntryMode" />: 
                            <select tabIndex="<%=tabIndex++%>" name ="entryModeList" id="entryModeList" onchange="setEntryMode();">
                                <option value="Normal" <%=entryMode.equals("Normal") ? "selected" : ""%> ><bean:message key="dms.incomingDocs.normal" /></option>
                                <option value="Fast" <%=entryMode.equals("Fast") ? "selected" : ""%> ><bean:message key="dms.incomingDocs.fast" /></option>
                            </select>
                        </legend>
                        <form id="forms_" method="post" action="ManageDocument.do"  >
                            <input type="hidden" name="method" value="addIncomingDocument" />
                            <input type="hidden" name="pdfDir" value="<%=pdfDir%>">
                            <input type="hidden" name="pdfName" value="<%=pdfName%>">
                            <input type="hidden" name="queueId" value="<%=queueIdStr%>">
                            <input type="hidden" name="pdfNo" value="<%=pdfNo%>">
                            <input type="hidden" name="queue" value="1">
                            <input type="hidden" name="pdfAction" value="">
                            <input type="hidden" name="lastdemographic_no" id="lastdemographic_no" value="">
                            <input type="hidden" name="entryMode" value="<%=entryMode%>">
                            <table border="0" width="350">
                                <% if (entryMode.equals("Fast")) {%>
                                <tr><td colspan="2" width="350">
                                        <%for (int j = 0; j < docTypes.size(); j++) {
                                                String docType = (String) docTypes.get(j);%>
                                        <input type="button" value="<%=docType.length()<3?docType:docType.substring(0, 3)%>" title="<%=docType%>" onclick="selectDocType(<%=j%>+1);"> <%}%>
                                    </td>
                                </tr> <%}%>
                                <tr>
                                    <td><bean:message key="dms.incomingDocs.type" />:</td>
                                    <td>
                                        <select tabIndex="<%=tabIndex++%>" name ="docType" id="docType" onchange="addDocumentDescriptionTemplateButton()">
                                            <option value=""><bean:message key="dms.incomingDocs.selectType" /></option>
                                            <%for (int j = 0; j < docTypes.size(); j++) {
                                                    String docType = (String) docTypes.get(j);%>
                                            <option value="<%= docType%>" ><%= docType%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><bean:message key="dms.incomingDocs.class"/>:</td>
                                    <td><select name="docClass" id="docClass">
                                            <option value=""><bean:message key="dms.incomingDocs.selectClass"/></option>
                                            <% boolean consultShown = false;
                                                for (String reportClass : reportClasses) {
                                                    if (reportClass.startsWith("Consultant Report")) {
                                                        if (consultShown) {
                                                            continue;
                                                        }
                                                        reportClass = "Consultant Report";
                                                        consultShown = true;
                                                    }
                                            %>
                                            <option value="<%=reportClass%>" ><%=reportClass%></option>
                                            <% }%>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2"><bean:message key="dms.incomingDocs.docSubClass"/>:
                                        <input type="text" name="docSubClass" id="docSubClass"  style="width:100%;"   >
                                        <div class="autocomplete_style" id="docSubClass_list"></div>
                                    </td>
                                </tr>
                                <tr><td colspan="2"><div id="docDescriptionList"></div></td></tr>
                                <tr><td colspan="2"><bean:message key="dms.incomingDocs.description" />:</td></tr>
                                <tr>
                                    <td colspan="2"><input tabIndex="<%=tabIndex++%>"  type="text" style="width:100%;" id="documentDescription" name="documentDescription" value=""  onfocus="setDescriptionIfEmpty();" /></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="dms.incomingDocs.obsDate" />:
                                        <a id="obsdate" onmouseover="renderCalendar(this.id,'observationDate' );" href="javascript:void(0);" ><img title="Calendar" src="<%=request.getContextPath()%>/images/cal.gif" alt="Calendar" border="0" /></a></td><td>
                                        <input tabIndex="<%=tabIndex++%>"  id="observationDate" name="observationDate" type="text" maxlength="10" size="10" value="<%=todayDate%>">
                                    </td>
                                </tr>
                                <% if (entryMode.equals("Fast")) {%>
                                <tr><td><bean:message key="dms.incomingDocs.lastPatients" />:</td></tr>
                                <tr>
                                    <td colspan="2"> <%
                                        String valueid = "";
                                        if (!LastPatientsBean.isEmpty()) {
                                            for (int counter = (LastPatientsBean.size() - 1); counter >= 0; counter--) {
                                                valueid = LastPatientsBean.get(counter).toString();
                                                Demographic demo = demographicDao.getDemographic(valueid);
                                                if (demo != null) {
                                        %>   
                                        <input type="button" value="<%=demo.getLastName()%>, <%=demo.getFirstName()%> (<%=demo.getYearOfBirth()%>-<%=demo.getMonthOfBirth()%>-<%=demo.getDateOfBirth()%>)" id="demvalueid<%=valueid%>" onclick="loadRecentDemo('<%=valueid%>','<%=demo.getLastName()%>, <%=demo.getFirstName()%> (<%=demo.getYearOfBirth()%>-<%=demo.getMonthOfBirth()%>-<%=demo.getDateOfBirth()%>)')" />
                                        <%
                                            
                                                    }
                                                }
                                            }%>
                                    </td>
                                </tr> <%}%>
                                <tr>
                                    <td colspan="2"><bean:message key="dms.incomingDocs.demographic" />: <input type="button" value="M" onclick="loadMaster()"> <input type="button" value="H" onclick="loadApptHistory();"> 
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <input id="saved" type="hidden" name="saved" value="false"/>
                                        <input type="hidden" name="demog" value="-1" id="demofind" />
                                        <input tabIndex="<%=tabIndex++%>" type="text"  id="autocompletedemo" onchange="checkSave('')" name="demographicKeyword" />
                                        <div id="autocomplete_choices" class="autocomplete" ></div>
                                    </td>
                                </tr>
                                <tr><td colspan="2"><input type="button" id="createNewDemo" value="<bean:message key="dms.incomingDocs.createNewDemographic" />"  onclick="popupPage(700,960,'<%= request.getContextPath() %>/demographic/demographicaddarecordhtm.jsp','demographic')"/>
                                    </td>    
                                </tr>
                                <tr>
                                    <td valign="top" colspan="2"><bean:message key="dms.incomingDocs.flagProvider" />: </td></tr>
                                <tr>
                                    <td colspan="2">
                                        <input type="hidden" name="provi" id="provfind" />
                                        <input type="hidden" name="MRPNo" id="MRPNo" />
                                        <input type="hidden" name="MRPName" id="MRPName" />
                                        <input type="button" value="MRP" onclick="addMRP();">
                                        <% if (entryMode.equals("Fast")) {
                                                int i = 0;
                                                SortedMap<String, Provider> sm = new TreeMap<String, Provider>();
                                                String pname = "";
                                                for (i = 0; (i < routingIdList.size() && i < 10); i++) {
                                                    
                                                    Integer p1 = routingIdList.get(i);
                                                    List<Object[]> searchResult;
                                                    searchResult = providerLabRoutingDao.findProviderAndLabRoutingById(p1);

                                                    for (Object[] o : searchResult) {
                                                        Provider provider = (Provider) o[0];
                                                            
                                                        if (provider != null) {
                                                            pname = provider.getLastName() + " " + provider.getFirstName();
                                                            sm.put(pname, provider);
                                                        }
                                                    }
                                                }
                                                Set s = sm.entrySet();
                                                Iterator it = s.iterator();
                                                    
                                                while (it.hasNext()) {
                                                    Map.Entry m = (Map.Entry) it.next();
                                                        
                                                    Provider sortedprovider = (Provider) m.getValue();
                                                    if (sortedprovider != null) {
                                                        pname = sortedprovider.getLastName() + " " + sortedprovider.getFirstName();
                                                        StringBuilder sbInitials = new StringBuilder();
                                                        String[] nameParts = pname.split(" ");
                                                        for (String part : nameParts) {
                                                            sbInitials.append(part.charAt(0));
                                                        }
                                                        String initials = sbInitials.toString();
                                        %>
                                        <input type="button" value="<%=initials%>" title="<%=sortedprovider.getFirstName()%> <%=sortedprovider.getLastName()%> " onclick="addflagprovider('<%=sortedprovider.getFirstName()%>','<%=sortedprovider.getLastName()%>','<%=sortedprovider.getProviderNo()%>');">
                                        <%              }
                                                }
                                                    
                                            }%>

                                        <input tabIndex="<%=tabIndex++%>" type="text" id="autocompleteprov" name="ProvKeyword"/>
                                        <div id="autocomplete_choicesprov" class="autocomplete"></div>
                                        <div id="providerList"></div> 
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="left"><p><p><input type="submit" onclick="return checkDocument();" name="save" tabIndex="<%=tabIndex++%>" id="save" disabled value="Save & Next" /></td>
                                </tr>
                            </table>
                        </form>
                    </fieldset>
                </td>
                <td  class="topalign">
                    <div > 
                        <%if (Integer.parseInt(pdfNo) > 0) {%>Page : <b id="pgnum"><%=pdfPageNumber%> <bean:message key="dms.incomingDocs.of" /><span class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>"> <%=numOfPage%> <%} else {%> <b id="pgnum" style="display:none"></b> <%} %></span></b> 
                        <bean:message key="dms.incomingDocs.viewAs" />: 
                        <select tabIndex="<%=tabIndex++%>" name ="imageTypeList" id="imageTypeList" onchange="setImageType();">
                            <option value="Pdf" <%=imageType.equals("Pdf") ? "selected" : ""%> ><bean:message key="dms.incomingDocs.PDF" /></option>
                            <option value="Image" <%=imageType.equals("Image") ? "selected" : ""%> ><bean:message key="dms.incomingDocs.image" /></option>
                        </select>
                    </div>
                    <div id="docdisp"></div>
                </td>                                            
            </tr>
            <script type="text/javascript">
                YAHOO.example.BasicRemote = function() {
                    var url = "<%= request.getContextPath()%>/provider/SearchProvider.do";
                    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
                    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
                    
                    // Define the schema of the delimited results
                    oDS.responseSchema = {
                        resultsList : "results",
                        fields : ["providerNo","firstName","lastName"]
                    };
                    
                    // Enable caching
                    oDS.maxCacheEntries = 0;
                    
                    // Instantiate the AutoComplete
                    var oAC = new YAHOO.widget.AutoComplete("autocompleteprov", "autocomplete_choicesprov", oDS);
                    oAC.queryMatchSubset = true;
                    oAC.minQueryLength = 3;
                    oAC.maxResultsDisplayed = 25;
                    oAC.formatResult = resultFormatter3;
                    
                    oAC.queryMatchContains = true;
                    
                    oAC.itemSelectEvent.subscribe(function(type, args) {
                        
                        var myAC = args[0];
                        var str = myAC.getInputEl().id.replace("autocompleteprov","provfind");
                        var oData=args[2];
                        $(str).value = args[2][0];
                        myAC.getInputEl().value = args[2][2] + ","+args[2][1];
                        
                        addflagprovider(oData[2],oData[1],oData[0]);
                        
                        myAC.getInputEl().value = '';
                        
                    });
                    
                    
                    return {
                        oDS: oDS,
                        oAC: oAC
                    };
                }();
                
                function addflagprovider(pfirstname,plastname,provider_no) {
                    //enable Save button whenever a selection is made
                    var bdoc;
                    if (navigator.appName=="Microsoft Internet Explorer") {
                        
                        var bdoc = document.createElement('<a id="removeProv" onclick="removeThisProv(this)" >');
                        
                    } else
                    {
                        var bdoc = document.createElement('a');
                        bdoc.setAttribute("id", "removeProv");
                        bdoc.setAttribute("onclick", "removeThisProv(this);");
                    }
                    
                    bdoc.appendChild(document.createTextNode(" -remove- "));
                    
                    
                    var adoc = document.createElement('div');
                    adoc.appendChild(document.createTextNode(pfirstname + " " +plastname));
                    
                    var idoc;
                    if (navigator.appName=="Microsoft Internet Explorer") {
                        idoc = document.createElement('<input type="hidden" name=flagproviders   value="'+provider_no+'" >');
                    } else
                    {
                        idoc = document.createElement('input');
                        idoc.setAttribute("type", "hidden");
                        idoc.setAttribute("name","flagproviders");
                        idoc.setAttribute("value",provider_no);
                    }
                    
                    adoc.appendChild(idoc);
                    
                    adoc.appendChild(bdoc);
                    var providerList = $('providerList');
                    
                    providerList.appendChild(adoc);
                    
                }

                YAHOO.example.BasicRemote = function() {
                    if($("autocompletedemo") && $("autocomplete_choices")){
                        
                        var url = "../demographic/SearchDemographic.do";
                        var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
                        oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
                        
                        // Define the schema of the delimited results
                        oDS.responseSchema = {
                            resultsList : "results",
                            fields : ["formattedName","fomattedDob","demographicNo","status","providerNo","providerName"]
                        };
                        
                        // Enable caching
                        oDS.maxCacheEntries = 0;
                        
                        var oAC = new YAHOO.widget.AutoComplete("autocompletedemo","autocomplete_choices",oDS);
                        
                        oAC.queryMatchSubset = true;
                        oAC.minQueryLength = 3;
                        oAC.maxResultsDisplayed = 25;
                        oAC.formatResult = resultFormatter2;
                        oAC.queryMatchContains = true;
                        
                        oAC.itemSelectEvent.subscribe(function(type, args) {
                            
                            var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");
                            
                            
                            document.getElementById('MRPNo').value=args[2][4];
                            document.getElementById('MRPName').value=args[2][5];
                            
                            $(str).value = args[2][2];
                            
                            args[0].getInputEl().value = args[2][0] + " ("+args[2][1]+")";
                            
                            selectedDemos.push(args[0].getInputEl().value);
                            
                            //enable Save button whenever a selection is made
                            $('save').enable();
                            
                            if(document.PdfInfoForm.pdfDir.value!="File")  {
                                var MRPName=document.getElementById('MRPName').value;
                                var MRPNo=document.getElementById('MRPNo').value;
                                if(MRPNo!=null && MRPNo.length>0) {
                                addflagprovider(MRPName,"(<bean:message key="dms.incomingDocs.MRP" />)",MRPNo);
                                }
                            }
                        });
                        
                        
                        return {
                            oDS: oDS,
                            oAC: oAC
                        };
                    }
                }();
            </script>
        </table>
        <script type="text/javascript">
            showPageImg('<%=queueIdStr%>','<%=pdfDir%>','<%=pdfName%>','<%=pdfPageNumber%>');
        </script>
    </body>
</html>