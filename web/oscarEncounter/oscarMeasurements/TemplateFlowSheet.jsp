<% long startTime = System.currentTimeMillis(); %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page  import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.common.dao.FlowSheetCustomizerDAO,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@page import="org.oscarehr.common.dao.FlowSheetDrugDAO,org.oscarehr.common.model.FlowSheetDrug"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
    //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<oscar:oscarPropertiesCheck property="SPEC3" value="yes">
    <security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="r" reverse="<%=true%>">
        "You have no right to access this page!"
        <% response.sendRedirect("../../noRights.html"); %>
    </security:oscarSec>
</oscar:oscarPropertiesCheck>
<%
    String demographic_no = request.getParameter("demographic_no");
    String providerNo = (String) session.getAttribute("user");

    //PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();//new PreventionDisplayConfig();
    //ArrayList prevList = pdc.getPreventions();
    //ArrayList configSets = pdc.getConfigurationSets();

    long startTimeToGetP = System.currentTimeMillis();
    PreventionData pd = new PreventionData();
    Prevention p = pd.getPrevention(demographic_no);

    System.out.println("Getting preventions took  "+ (System.currentTimeMillis() - startTimeToGetP) );
    boolean dsProblems = false;

    String temp = request.getParameter("template");
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            
    FlowSheetCustomizerDAO flowSheetCustomizerDAO = (FlowSheetCustomizerDAO) ctx.getBean("flowSheetCustomizerDAO");
    FlowSheetDrugDAO flowSheetDrugDAO = (FlowSheetDrugDAO) ctx.getBean("flowSheetDrugDAO");
    
    List custList = flowSheetCustomizerDAO.getFlowSheetCustomizations( temp,(String) session.getAttribute("user"),demographic_no);
    
    ////Start
    MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

    
    MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(temp,custList);
    
    MeasurementInfo mi = new MeasurementInfo(demographic_no);
    List<String> measurementLs = mFlowsheet.getMeasurementList();
    ArrayList<String> measurements = new ArrayList(measurementLs);
    long startTimeToGetM = System.currentTimeMillis();
     
    System.out.println("Measurements size "+measurements.size());
    mi.getMeasurements(measurements);
    System.out.println("Getting measurements  took  "+ (System.currentTimeMillis() - startTimeToGetM) );

    mFlowsheet.getMessages(mi);

    ArrayList recList = mi.getList();
    System.out.println("RECLIST "+recList.size());
    
    for(Object rec: recList){
        System.out.println("REF : "+rec);
    }
    
    mFlowsheet.sortToCurrentOrder(recList);
    StringBuffer recListBuffer = new StringBuffer();
    for(int i = 0; i < recList.size(); i++){
        recListBuffer.append("&amp;measurement="+response.encodeURL( (String) recList.get(i)));
    }


    String flowSheet = mFlowsheet.getDisplayName();
    ArrayList<String> warnings = mi.getWarnings();
    ArrayList<String> recomendations = mi.getRecommendations();
    System.out.println(" warnings "+mi.getWarnings().size());
    System.out.println(" recommendations "+mi.getRecommendations().size());
    ArrayList comments = new ArrayList();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


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
* McMaster Unviersity test2
* Hamilton
* Ontario, Canada
*/
-->

<html:html locale="true">

<head>
<title><%=flowSheet%> - <oscar:nameage demographicNo="<%=demographic_no%>"/></title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../../share/javascript/prototype.js"></script>

<style type="text/css">
    div.ImmSet { background-color: #ffffff;clear:left;margin-top:10px;}
    div.ImmSet h2 {  }
    div.ImmSet h2 span { font-size:smaller; }
    div.ImmSet ul {  }
    div.ImmSet li {  }
    div.ImmSet li a { text-decoration:none; color:blue;}
    div.ImmSet li a:hover { text-decoration:none; color:red; }
    div.ImmSet li a:visited { text-decoration:none; color:blue;}

    /*h3{font-size: 100%;margin:0 0 10px;padding: 2px 0;color: #497B7B;text-align: center}*/

</style>

<style type="text/css" media="print">
.DoNotPrint {
	display: none;
}
</style>

<link rel="stylesheet" type="text/css" href="../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="../../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../../share/javascript/nifty.js"></script>

<script type="text/javascript">
    window.onload=function(){
        if(!NiftyCheck())
            return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
        Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
        Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");

//Rounded("span.footnote","all","transparent","#F0F0E7","small border #999");

        Rounded("div.leftBox","top","transparent","#CCCCFF","small border #ccccff");
        Rounded("div.leftBox","bottom","transparent","#EEEEFF","small border #ccccff");

    }
</script>


<style type="text/css">
body {font-size:100%}


div.leftBox{
    width:90%;
    margin-top: 2px;
    margin-left:3px;
    margin-right:3px;
    float: left;
}

span.footnote {
    background-color: #ccccee;
    border: 1px solid #000;
    width: 4px;
}

div.leftBox h3 {
    background-color: #ccccff;
/*font-size: 1.25em;*/
    font-size: 8pt;
    font-variant:small-caps;
    font-weight:bold;
    margin-top:0px;
    padding-top:0px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.leftBox ul{
/*border-top: 1px solid #F11;*/
/*border-bottom: 1px solid #F11;*/
    font-size: 1.0em;
    list-style:none;
    list-style-type:none;
    list-style-position:outside;
    padding-left:1px;
    margin-left:1px;
    margin-top:0px;
    padding-top:1px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.leftBox li {
    padding-right: 15px;
    white-space: nowrap;
}


div.headPrevention {
    position:relative;
    float:left;
    width:12em;
    height:2.9em;
}

div.headPrevention p {
    background: #ddddff;
    font-family: verdana,tahoma,sans-serif;
    margin:0;

    padding: 4px 4px;
    line-height: 1.2;
/*text-align: justify;*/
    height:2em;
    font-family: sans-serif;
}

div.headPrevention a {
    text-decoration:none;
}

div.headPrevention a:active { color:blue; }
div.headPrevention a:hover { color:blue; }
div.headPrevention a:link { color:blue; }
div.headPrevention a:visited { color:blue; }


div.preventionProcedure{
    width:9em;
    float:left;
    margin-left:3px;
    margin-bottom:3px;
}

div.preventionProcedure p {
    font-size: 0.8em;
    font-family: verdana,tahoma,sans-serif;
    background: #F0F0E7;
    margin:0;
    padding: 1px 2px;
/*line-height: 1.3;*/
/*text-align: justify*/
}

div.preventionSection {
    width: 100%;
    position:relative;
    margin-top:5px;
    float:left;
    clear:left;
}

div.preventionSet {
    border: thin solid grey;
    clear:left;
}

div.recommendations{
    font-family: verdana,tahoma,sans-serif;
    font-size: 1.2em;
}

div.recommendations ul{
    padding-left:15px;
    margin-left:1px;
    margin-top:0px;
    padding-top:1px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.recommendations li{

}



</style>

</head>

<body class="BodyStyle" >
<!--  -->
<table  class="MainTable" id="scrollNumber1" >
<tr class="MainTableTopRow">
    <td class="MainTableTopRowLeftColumn"  >
        <%=flowSheet%>
    </td>
    <td class="MainTableTopRowRightColumn">
        <table class="TopStatusBar">
            <tr>
                <td >
                    <oscar:nameage demographicNo="<%=demographic_no%>"/>
                    <oscar:oscarPropertiesCheck property="SPEC3" value="yes"> 
                    <a href="adminFlowsheet/EditFlowsheet.jsp?flowsheet=<%=temp%>&demographic=<%=demographic_no%>" target="_new">Edit</a>
                    &nbsp;
                    <a href="TemplateFlowSheet.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=lastOnly">Last Only</a>
                    &nbsp;
                    <a href="TemplateFlowSheet.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=outOfRange">Only out of Range</a>
                    &nbsp;
                    <a href="TemplateFlowSheet.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>">All</a>
                    &nbsp;
                    <a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>">Custom Print</a>
                    </oscar:oscarPropertiesCheck>
                </td>
                <td  >&nbsp;

                </td>
                <td style="text-align:right">
                    <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td class="MainTableLeftColumn" valign="top">
    <% if (recList.size() > 0){ %>
    <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(465,635,'AddMeasurementData.jsp?demographic_no=<%=demographic_no%><%=recListBuffer.toString()%>&amp;template=<%=temp%>','addMeasurementData<%=Math.abs( "ADDTHEMALL".hashCode() ) %>')">
        ADD ALL
    </a>
    <%}%>
    <!-- only show disease registry and prescriptions for flowsheets which aren't medical in nature -->
    <% if (mFlowsheet.isMedical()) {%>
    <div class="leftBox">
        <h3>&nbsp;Current Patient Dx List  <a href="#" onclick="Element.toggle('dxFullListing'); return false;" style="font-size:x-small;" >show/hide</a></h3>
        <div class="wrapper" id="dxFullListing"  >
            <jsp:include page="../../oscarResearch/oscarDxResearch/currentCodeList.jsp">
                <jsp:param name="demographicNo" value="<%=demographic_no%>"/>
                <jsp:param name="maxlen" value="15"/>
            </jsp:include>
        </div>
    </div>

    <security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r"  >
        <div class="leftBox">
            <h3>&nbsp;Current Patient Rx List  <a href="#" onclick="Element.toggle('rxFullListing'); return false;" style="font-size:x-small;" >show/hide</a></h3>
            <div class="wrapper" id="rxFullListing"  >

                <%
                    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
                    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
                    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demographic_no));
                    if (arr.length > 0){%>
                <ul>
                    <%for (int i = 0; i < arr.length; i++){
                        String rxD = arr[i].getRxDate().toString();
                        //String rxP = arr[i].getRxDisplay();
                        String rxP = arr[i].getFullOutLine().replaceAll(";"," ");
                        rxP = rxP + "   " + arr[i].getEndDate();
                        String styleColor = "";
                        if(arr[i].isCurrent()){
                    %>
                    <li title="<%=rxD%> - <%=rxP%>"> 
                        <oscar:oscarPropertiesCheck property="SPEC3" value="yes">
                            <a href="FlowSheetDrugAction.do?method=save&flowsheet=<%=temp%>&demographic=<%=demographic_no%>&atcCode=<%=arr[i].getAtcCode()%>">
                            </oscar:oscarPropertiesCheck>    
                            - <%= org.apache.commons.lang.StringUtils.abbreviate(rxP, 12)%>
                            <oscar:oscarPropertiesCheck property="SPEC3" value="yes">
                            </a>
                        </oscar:oscarPropertiesCheck>
                    </li>
                    <%  }
                    }%>
                </ul>
                <%}%>



            </div>
        </div>
    </security:oscarSec>
    <% } %>
<div>
   <input type="button" class="DoNotPrint" value="Print" onclick="javascript:window.print()">
</div>
</td>

<td valign="top" class="MainTableRightColumn">    
<% if (warnings.size() > 0 || recomendations.size() > 0  || dsProblems) { %>
<div class="recommendations">
    <span style="font-size:larger;"><%=flowSheet%> Recommendations</span>
    <a href="#" onclick="Element.toggle('recomList'); return false;" style="font-size:x-small;" >show/hide</a>
    <ul id="recomList" style="display:none;">
        <% for (String warn : warnings){ %>
        <li style="color: red;"><%=warn%></li>
        <%}%>
        <% for (String warn : recomendations ){%>
        <li style="color: black;"><%=warn%></li>
        <%}%>
        <!--li style="color: red;">6 month TD overdue</li>
  <li>12 month MMR due in 2 months</li-->
        <% if (dsProblems){ %>
        <li style="color: red;">Decision Support Had Errors Running.</li>
        <% } %>
    </ul>
</div>
<% } %>
<%=mFlowsheet.getTopHTMLStream() %>
<%--
<img src="../../oscarEncounter/GraphMeasurements.do?demographic_no=<%=demographic_no%>&type=INR&type2=COUM"/>
<br/>
--%>
<div >
<%
    EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
    long startTimeToLoopAndPrint = System.currentTimeMillis();
    for (String measure:measurements){
        Hashtable h2 = mFlowsheet.getMeasurementFlowSheetInfo(measure);
        FlowSheetItem item =  mFlowsheet.getFlowSheetItem(measure);
        System.out.println(">>>>"+item.getItemName()+" "+item.isHide());
        
        String hidden= "";
        if (item.isHide()){
            hidden= "display:none;";
        }

        if (h2.get("measurement_type") != null ){
            Hashtable h = new Hashtable();
            EctMeasurementTypesBean mtypeBean = mType.getMeasurementType(measure);//mFlowsheet.getMeasurementInfo(measure);

            if(mtypeBean!=null) {
                h.put("name",mtypeBean.getTypeDisplayName());
                h.put("desc",mtypeBean.getTypeDesc());
                
                System.err.println("mType ="+mtypeBean.getTypeDisplayName()+" hashtable value "+h.get("name"));
                if (!mtypeBean.getTypeDisplayName().equals(""+h.get("name"))){
                    System.err.println("\n\n\n\n\n\n");
                    System.err.println("!!!!mType ="+mtypeBean.getTypeDisplayName()+" hashtable value "+h.get("name"));
                    System.out.println("DESC "+h.get("desc"));
                }
                
            }
            String prevName = (String) h.get("name");
            //Collection aalist = mi.getMeasurementData(measure);
            ArrayList<EctMeasurementsDataBean> alist = mi.getMeasurementData(measure);
            //ArrayList alist =  new ArrayList(aalist);//pd.getPreventionData((String)h.get("name"), demographic_no);
            String extraColour = "";
            if(mi.hasRecommendation(measure)){
                extraColour = "style=\"background-color: "+mFlowsheet.getRecommendationColour()+"\" ";
            }else if(mi.hasWarning(measure)){
                extraColour = "style=\"background-color: "+mFlowsheet.getWarningColour()+"\" ";
            }
            //measurement_type="EDGI"
            //display_name="Autonomic Neuropathy"
            //guideline="Erectile Dysfunction, hastrointestinal disturbance"
            //graphable="no"
            //value_name="Answer"
%>
<div class="preventionSection"  style="overflow: auto;<%=hidden%>" nowrap>
    <div class="headPrevention">

        <p <%=extraColour%> title="fade=[on] header=[<%=h2.get("display_name")%>] body=[<%=wrapWithSpanIfNotNull(mi.getWarning(measure),"red")%><%=wrapWithSpanIfNotNull(mi.getRecommendation(measure),"red")%><%=h2.get("guideline")%>]"   >
            <%if(h2.get("graphable") != null && ((String) h2.get("graphable")).equals("yes")){%>
            <%if (alist != null && alist.size() > 1) { %>
               <img src="img/chart.gif" alt="Plot"  onclick="window.open('../../oscarEncounter/GraphMeasurements.do?demographic_no=<%=demographic_no%>&type=<%=measure%>');"/>
            <%}else{%>
               <img src="img/chart.gif" alt="Plot"/>
            <%}%>
           <%}%>
            <% System.out.println(h2.get("display_name")+ " "+ h2.get("value_name")); %>
            <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(465,635,'AddMeasurementData.jsp?measurement=<%= response.encodeURL( measure) %>&amp;demographic_no=<%=demographic_no%>&amp;template=<%= URLEncoder.encode(temp,"UTF-8") %>','addMeasurementData<%=Math.abs( ((String) h.get("name")).hashCode() ) %>')">
                <span  style="font-weight:bold;"><%=h2.get("display_name")%></span>
            </a>

        </p>
    </div>
    <%  int k = 0;
        for (EctMeasurementsDataBean mdb:alist){
            k++;
            mFlowsheet.runRulesForMeasurement(mdb);
            Hashtable hdata = new Hashtable();//(Hashtable) alist.get(k);
            hdata.put("age",mdb.getDataField());
            hdata.put("prevention_date",mdb.getDateObserved());
            hdata.put("id",""+mdb.getId());
            String com = mdb.getComments();
            boolean comb = false;
            if (com != null && !com.trim().equals("")){
                comments.add(com);
                comb = true;
            }else{
                com ="";
            }
            String hider = "";
            
            int num =4;
            if (request.getParameter("show") !=null && request.getParameter("show").equals("lastOnly")){
                num =1;
            }
            
            if ( k > num){
                hider = "style=\"display:none;\"";
            }

            String indColour = "";
            if ( mdb.getIndicationColour() != null ){
                System.out.println("INDICAT: "+mdb.getIndicationColour());
                indColour = "style=\"background-color:"+mFlowsheet.getIndicatorColour(mdb.getIndicationColour())+"\"";
            }
            
            if (request.getParameter("show") !=null && request.getParameter("show").equals("outOfRange") && indColour.equals("")){
                hider = "style=\"display:none;\"";
            }
            
    %>
    <div class="preventionProcedure" <%=hider%>  onclick="javascript:popup(465,635,'AddMeasurementData.jsp?measurement=<%= response.encodeURL( measure) %>&amp;id=<%=hdata.get("id")%>&amp;demographic_no=<%=demographic_no%>&amp;template=<%= URLEncoder.encode(temp,"UTF-8") %>','addMeasurementData')" >
        <p <%=indColour%> title="fade=[on] header=[<%=hdata.get("age")%> -- Date:<%=hdata.get("prevention_date")%>] body=[<%=com%>&lt;br/&gt;Entered By:<%=mdb.getProviderFirstName()%> <%=mdb.getProviderLastName()%>]"><%=h2.get("value_name")%>: <%=hdata.get("age")%> <br/>
            <%=hdata.get("prevention_date")%>&nbsp;<%=mdb.getNumMonthSinceObserved()%>M
            <%if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%}%>
        </p>
    </div>
    <%}%>
</div>
<%}else{
    String prevType = (String) h2.get("prevention_type");
    long startPrevType = System.currentTimeMillis();
    ArrayList alist = pd.getPreventionData(prevType, demographic_no);

    System.out.println("Getting prev  "+prevType+" data took "+(System.currentTimeMillis() - startPrevType) );


%>


<div class="preventionSection" style="<%=hidden%>" >
    <div class="headPrevention">
        <p title="fade=[on] header=[<%=h2.get("display_name")%>] body=[<%=wrapWithSpanIfNotNull(mi.getWarning(measure),"red")%><%=wrapWithSpanIfNotNull(mi.getRecommendation(measure),"red")%><%=h2.get("guideline")%>]">
            <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(465,635,'../../oscarPrevention/AddPreventionData.jsp?prevention=<%= response.encodeURL( prevType) %>&amp;demographic_no=<%=demographic_no%>','addPreventionData<%=Math.abs( prevType.hashCode() ) %>')">
                <span title="<%=h2.get("guideline")%>" style="font-weight:bold;"><%=h2.get("display_name")%></span>
            </a>
            &nbsp;

            <br/>
        </p>
    </div>
    <%
        out.flush();
        for (int k = 0; k < alist.size(); k++){
            Hashtable hdata = (Hashtable) alist.get(k);
            String com = pd.getPreventionComment(""+hdata.get("id"));
            boolean comb = false;
            System.out.println(com);
            if (com != null ){
                comments.add(com);
                comb = true;
            }else{
                com ="";
            }
    %>
    <div class="preventionProcedure"  onclick="javascript:popup(465,635,'../../oscarPrevention/AddPreventionData.jsp?id=<%=hdata.get("id")%>&amp;demographic_no=<%=demographic_no%>','addPreventionData')" >
        <p <%=r(hdata.get("refused"))%> title="fade=[on] header=[<%=hdata.get("age")%> -- Date:<%=hdata.get("prevention_date")%>] body=[<%=com%>]" >Age: <%=hdata.get("age")%> <br/>
            <!--<%=refused(hdata.get("refused"))%>-->Date: <%=hdata.get("prevention_date")%>
            <%if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%}%>
        </p>
    </div>
    <%}%>
</div>

<%System.out.println("Prev took  "+prevType+" "+(System.currentTimeMillis() - startPrevType) );
}%>

<%}
    System.out.println("Looping display took  "+ (System.currentTimeMillis() - startTimeToLoopAndPrint) );
%>



<%


    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
    
    List<FlowSheetDrug> atcCodes = flowSheetDrugDAO.getFlowSheetDrugs(temp,demographic_no);
    for(FlowSheetDrug fsd : atcCodes){
            arr = prescriptData.getPrescriptionScriptsByPatientATC(Integer.parseInt(demographic_no),fsd.getAtcCode());   
     
%>

<div class="preventionSection"  >
    <div class="headPrevention">
        <p title="fade=[on] header=[ddddd] body=[ddddddddsdfsdf]">
            <!-- a href="javascript: function myFunction() {return false; }"  -->
                <span title="dd" style="font-weight:bold;"><%=arr[0].getGenericName()%></span>
            <!-- /a -->
            &nbsp;
            <br/>
        </p>
    </div>
    <%
        out.flush();
        for (oscar.oscarRx.data.RxPrescriptionData.Prescription pres : arr){
    %>
    <div class="preventionProcedure"  onclick="javascript:popup(465,635,'','addPreventionData')" >
        <p <%=""/*r(hdata.get("refused"))*/%> title="fade=[on] header=[<%=""/*hdata.get("age")*/%> -- Date:<%=""/*hdata.get("prevention_date")*/%>] body=[<%=""/*com*/%>]" ><%=pres.getBrandName()%> <br/>
            Date: <%=pres.getRxDate()%>
            <%-- if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%} --%>
        </p>
    </div>
    <%}%>
</div>

<%}%>














</div>
<br style="clear:left;"/>
<div style="margin-top: 20px;">
    <h3>Comments</h3>
    <ol>
        <% for (int i = 0; i < comments.size(); i++){
            String str = (String) comments.get(i);
        %>
        <li><%=str%></li>
        <% }%>
    </ol>
</div>
</td>
</tr>
<tr>
    <td class="MainTableBottomRowLeftColumn">
        &nbsp;
    </td>
    <td class="MainTableBottomRowRightColumn" valign="top">
        &nbsp;
    </td>
</tr>
</table>
<script type="text/javascript" src="../../share/javascript/boxover.js"></script>
</body>
</html:html>
<% System.out.println("Template took  "+ (System.currentTimeMillis() - startTime) +" to display"); %>
<%!
    String refused(Object re){
        String ret = "Given";
        if (re instanceof java.lang.String){

            if (re != null && re.equals("1")){
                ret = "Refused";
            }
        }
        return ret;
    }
    String r(Object re){
        String ret = "";
        if (re instanceof java.lang.String){
            if (re != null && re.equals("1")){
                ret = "style=\"background: #FFDDDD;\"";
            }else if(re !=null && re.equals("2")){
                ret = "style=\"background: #FFCC24;\"";
            }
        }
        return ret;
    }
    String wrapWithSpanIfNotNull(String s,String colour){
        String ret = "";
        String q = "\"";
        if (s != null){
            ret = "<span style='color:"+colour+"'>"+s+"</span> </br>";
        }
        return ret;
    }
    
    
    public void printOutStringLists(List<String> measurements){
       for (String measurement : measurements){
          System.out.println(":*:measurement= "+measurement);
       }
    }
    
    
    
%>
