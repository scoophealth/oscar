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
<% long startTime = System.currentTimeMillis(); %><%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.common.dao.FlowSheetCustomizationDao,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@page import="org.oscarehr.common.dao.FlowSheetDrugDao,org.oscarehr.common.model.FlowSheetDrug"%>
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

<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet"
	rights="r" reverse="<%=true%>">
"You have no right to access this page!"
<% response.sendRedirect("../../noRights.html"); %>
</security:oscarSec>

<%
    String demographic_no = request.getParameter("demographic_no");
    String providerNo = (String) session.getAttribute("user");
    boolean printView = false;

    //preset for patient handout
    ArrayList<String> phandout = new ArrayList<String>();
    if( request.getParameter("patientHandout") != null ) {
        phandout.add("FGLC");
        phandout.add("A1C");
        phandout.add("TCHD");
        phandout.add("LDL");
        phandout.add("HDL");
        phandout.add("TG");
        phandout.add("BP");
        phandout.add("ACR");
        phandout.add("SCR");
        phandout.add("EYEE");
        phandout.add("FTE");
        phandout.add("FTLS");
        phandout.add("WT");
        phandout.add("WC");
        phandout.add("SMCD");
    }

    /////ITEMS for printing
    if (request.getParameter("printView") != null){
        printView = true;
    }
    String[] elesToPrint = request.getParameterValues("printHP");

    //For List of measurements Selected Put how they are to be printed
    Hashtable forPrint = new Hashtable();
    if (elesToPrint != null){
    for(String toPrint:elesToPrint){
        String printStyle = request.getParameter("printStyle"+ toPrint);
        if (printStyle != null && printStyle.equals("all")){
            forPrint.put(toPrint,"all");
        }else if (printStyle != null && printStyle.equals("num")){
            String numEle = request.getParameter("numEle"+toPrint);
            Integer in = new Integer(Integer.parseInt(numEle));
            forPrint.put(toPrint,in);
        }else if (printStyle != null && printStyle.equals("range")){
            Date sdate = oscar.util.UtilDateUtilities.StringToDate(request.getParameter("sDate"+ toPrint));
            Date edate = oscar.util.UtilDateUtilities.StringToDate(request.getParameter("eDate"+ toPrint));
            Hashtable dates = new Hashtable();
            dates.put("sdate",sdate);
            dates.put("edate",edate);
            forPrint.put(toPrint,dates);
        }
    }
    }
    /////

    long startTimeToGetP = System.currentTimeMillis();
    Prevention p = PreventionData.getPrevention(Integer.valueOf(demographic_no));

    boolean dsProblems = false;

    String temp = request.getParameter("template");
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

    FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
    FlowSheetDrugDao flowSheetDrugDAO = ctx.getBean(FlowSheetDrugDao.class);

    List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations( temp,(String) session.getAttribute("user"),Integer.parseInt(demographic_no));

    ////Start
    MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();


    MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(temp,custList);

    MeasurementInfo mi = new MeasurementInfo(demographic_no);
    List<String> measurementLs = mFlowsheet.getMeasurementList();
    ArrayList<String> measurements = new ArrayList(measurementLs);
    long startTimeToGetM = System.currentTimeMillis();

    mi.getMeasurements(measurements);

    mFlowsheet.getMessages(mi);

    ArrayList recList = mi.getList();

    mFlowsheet.sortToCurrentOrder(recList);
    StringBuffer recListBuffer = new StringBuffer();
    for(int i = 0; i < recList.size(); i++){
        recListBuffer.append("&amp;measurement="+response.encodeURL( (String) recList.get(i)));
    }


    String flowSheet = mFlowsheet.getDisplayName();
    ArrayList<String> warnings = mi.getWarnings();
    ArrayList<String> recomendations = mi.getRecommendations();
    ArrayList comments = new ArrayList();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
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

div.headPrevention {
    position:relative;
    margin-left:1px;
    width:22em;
    /*height:2.9em;*/
}

div.headPrevention a:active { color:black; }
div.headPrevention a:hover { color:black; }
div.headPrevention a:link { color:black; }
div.headPrevention a:visited { color:black; }
</style>
<style type="text/css" media="screen">
    .DoNotScreen{ display:none;}

    div.headPrevention {
    position:relative;
    float:left;
    width:12em;
    /*height:2.9em;*/
}


div.headPrevention a:active { color:blue; }
div.headPrevention a:hover { color:blue; }
div.headPrevention a:link { color:blue; }
div.headPrevention a:visited { color:blue; }
</style>

<link rel="stylesheet" type="text/css" href="../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="../../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../../share/javascript/nifty.js"></script>

<script type="text/javascript">
    window.onload=function(){
        if(!NiftyCheck())
            return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
        <%if(!printView){%>
        Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
        Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");
        <%}%>
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




div.headPrevention p {
    background: #ddddff;
    font-family: verdana,tahoma,sans-serif;
    margin:0;

    padding: 4px 4px;
    line-height: 1.2;
/*text-align: justify;*/
    /*height:2em;*/
    font-family: sans-serif;
}

div.headPrevention a {
    text-decoration:none;
}




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
<form action="TemplateFlowSheetPrint.jsp" method="post">
<table  class="MainTable" id="scrollNumber1" >
<tr class="MainTableTopRow">
    <td class="MainTableTopRowLeftColumn"  >
        <%=flowSheet%> <span class="DoNotScreen"> --- <oscar:nameage demographicNo="<%=demographic_no%>"/></span>
    </td>
    <td class="MainTableTopRowRightColumn DoNotPrint">
        <table class="TopStatusBar">
            <tr>
                <td >
                    <oscar:nameage demographicNo="<%=demographic_no%>"/>
                    &nbsp;
                    <a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=lastOnly">Last Only</a>
                    &nbsp;
                    <a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=outOfRange">Only out of Range</a>
                    &nbsp;
                    <a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>">All</a>
                </td>
                <td  >&nbsp;

                </td>
                <td style="text-align:right">
                    <oscar:help keywords="measurement" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td class="MainTableLeftColumn DoNotPrint" valign="top">
   &nbsp;


</td>

<td valign="top" class="MainTableRightColumn">
<% if (warnings.size() > 0 || recomendations.size() > 0  || dsProblems) { %>
<div class="recommendations DoNotPrint" >
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
        <% if( !printView ) {%>
    <br>
    <input type="submit" value="Print Preview" class="DoNotPrint"/>&nbsp;<input type="button" onclick="history.go(-1);" value="Back">
        <%}%>
</div>
<% } %>
<%=mFlowsheet.getTopHTMLStream() %>

<div >
<%
    EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
    long startTimeToLoopAndPrint = System.currentTimeMillis();
    boolean setToPrint;
    for (String measure:measurements){
        Map h2 = mFlowsheet.getMeasurementFlowSheetInfo(measure);
        FlowSheetItem item =  mFlowsheet.getFlowSheetItem(measure);
        setToPrint = phandout.contains(measure);
        String hidden= "";

        if (item.isHide() || (forPrint.get(measure) == null && printView)  ){
            hidden= "display:none;";
        }

        if (h2.get("measurement_type") != null ){
            Hashtable h = new Hashtable();
            EctMeasurementTypesBean mtypeBean = mType.getMeasurementType(measure);//mFlowsheet.getMeasurementInfo(measure);

            if(mtypeBean!=null) {
                h.put("name",mtypeBean.getTypeDisplayName());
                h.put("desc",mtypeBean.getTypeDesc());
            }
            String prevName = (String) h.get("name");
            ArrayList<EctMeasurementsDataBean> alist = mi.getMeasurementData(measure);
            String extraColour = "";
            if(mi.hasRecommendation(measure)){
                extraColour = "style=\"background-color: "+mFlowsheet.getRecommendationColour()+"\" ";
            }else if(mi.hasWarning(measure)){
                extraColour = "style=\"background-color: "+mFlowsheet.getWarningColour()+"\" ";
            }


%>
    <input type="hidden" name="demographic_no" value="<%=demographic_no%>"/>
    <input type="hidden" name="template" value="<%=temp%>"/>
    <input type="hidden" name="printView" value="true"/>
<div class="preventionSection"  style="<%=hidden%>" nowrap>
    <%if (!printView){%>
    <div style="position: relative; float: left; padding-right: 10px;" class="DoNotPrint">
       <input  type="checkbox" name="printHP"   value="<%=measure%>"  <%=setToPrint ? "checked" : ""%>/>
       <input type="radio" name="printStyle<%=measure%>" value="all" checked >ALL</input><br>
       <input type="radio" name="printStyle<%=measure%>" value="num" ># Elements</input>
       <input type="text" name="numEle<%=measure%>" size="3"/>    <br>
       <input type="radio" name="printStyle<%=measure%>"  value="range">Range</input>
       <input type="text" size="10" name="sDate<%=measure%>"/> <input type="text" name="eDate<%=measure%>" size="10"/>
    </div>
    <%}%>
    <div class="headPrevention">

        <p <%=extraColour%> title="fade=[on] header=[<%=item.getDisplayName()%>] body=[<%=wrapWithSpanIfNotNull(mi.getWarning(measure),"red")%><%=wrapWithSpanIfNotNull(mi.getRecommendation(measure),"red")%><%=item.getGuideline()%>]"   >
            <%if(item.isGraphable()){%>
               <%if (alist != null && alist.size() > 1) { %>
                  <img class="DoNotPrint" src="img/chart.gif" alt="Plot"  onclick="window.open('../../oscarEncounter/GraphMeasurements.do?demographic_no=<%=demographic_no%>&type=<%=measure%>');"/>
               <%}else{%>
                  <img class="DoNotPrint" src="img/chart.gif" alt="Plot"/>
               <%}%>
            <%}%>
            <security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="w">
            <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(465,635,'AddMeasurementData.jsp?measurement=<%= response.encodeURL( measure) %>&amp;demographic_no=<%=demographic_no%>&amp;template=<%= URLEncoder.encode(temp,"UTF-8") %>','addMeasurementData<%=Math.abs( item.getItemName().hashCode() ) %>')">
            </security:oscarSec>

                <span  style="font-weight:bold;"><%=item.getDisplayName()%></span>

            <security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="w">
            </a>
			</security:oscarSec>
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

            int num =1000;

            if (request.getParameter("num") != null){
                num = Integer.parseInt(request.getParameter("num"));
            }


            if (request.getParameter("show") !=null && request.getParameter("show").equals("lastOnly")){
                num =1;
            }

            if( forPrint.get(measure) != null ){
                Object oj = forPrint.get(measure);
                if ( oj instanceof Integer){
                    num = (Integer) oj;
                }else if (oj instanceof Hashtable){
                    Hashtable dates = (Hashtable)oj;
                    Date sdate = (Date) dates.get("sdate");
                    Date edate = (Date)dates.get("edate");
                    Date itDate = mdb.getDateObservedAsDate();
                    if (itDate.before(sdate) || itDate.after(edate)){
                        hider = "style=\"display:none\"";
                    }
                }
            }

            if ( k > num){
                hider = "style=\"display:none;\"";
            }




            String indColour = "";
            if ( mdb.getIndicationColour() != null ){
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
    ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(prevType, Integer.valueOf(demographic_no));
%>


<div class="preventionSection" style="<%=hidden%>" >
    <%if (!printView){%>
    <div style="position: relative; float: left; padding-right: 10px;" class="DoNotPrint">
       <input  type="checkbox" name="printHP"   value="<%=measure%>" />
       <input type="radio" name="printStyle<%=measure%>" value="all" checked >ALL</input><br>
       <input type="radio" name="printStyle<%=measure%>" value="num" ># Elements</input>
       <input type="text" name="numEle<%=measure%>" size="3"/>    <br>
       <input type="radio" name="printStyle<%=measure%>"  value="range">Range</input>
       <input type="text" size="10" name="sDate<%=measure%>"/> <input type="text" name="eDate<%=measure%>" size="10"/>
    </div>
    <%}%>

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
      	 	Map<String,Object> hdata =  alist.get(k);
            String com = PreventionData.getPreventionComment(""+hdata.get("id"));
            boolean comb = false;
            if (com != null ){
                comments.add(com);
                comb = true;
            }else{
                com ="";
            }



            ///////PREV
            String hider = "";

            int num =1000;

            if (request.getParameter("num") != null){
                num = Integer.parseInt(request.getParameter("num"));
            }


            if (request.getParameter("show") !=null && request.getParameter("show").equals("lastOnly")){
                num =1;
            }

            if( forPrint.get(measure) != null ){
                Object oj = forPrint.get(measure);
                if ( oj instanceof Integer){
                    num = (Integer) oj;
                }else if (oj instanceof Hashtable){
                    Hashtable dates = (Hashtable)oj;
                    Date sdate = (Date) dates.get("sdate");
                    Date edate = (Date)dates.get("edate");
                    Date itDate = oscar.util.UtilDateUtilities.StringToDate((String) hdata.get("prevention_date"));
                    if (itDate.before(sdate) || itDate.after(edate)){
                        hider = "style=\"display:none\"";
                    }
                }
            }

            if ( k > num){
                hider = "style=\"display:none;\"";
            }



            //////PREV END




    %>
    <div class="preventionProcedure" <%=hider%> onclick="javascript:popup(465,635,'../../oscarPrevention/AddPreventionData.jsp?id=<%=hdata.get("id")%>&amp;demographic_no=<%=demographic_no%>','addPreventionData')" >
        <p <%=r(hdata.get("refused"))%> title="fade=[on] header=[<%=hdata.get("age")%> -- Date:<%=hdata.get("prevention_date")%>] body=[<%=com%>]" >Age: <%=hdata.get("age")%> <br/>
            <!--<%=refused(hdata.get("refused"))%>-->Date: <%=hdata.get("prevention_date")%>
            <%if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%}%>
        </p>
    </div>
    <%}%>
</div>

<%
}
}


    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};

    List<FlowSheetDrug> atcCodes = flowSheetDrugDAO.getFlowSheetDrugs(temp,Integer.parseInt(demographic_no));
    for(FlowSheetDrug fsd : atcCodes){
            arr = prescriptData.getPrescriptionScriptsByPatientATC(Integer.parseInt(demographic_no),fsd.getAtcCode());
       String measure = fsd.getAtcCode();
       String hidden = "";
        if (forPrint.get(measure) == null && printView  ){
            hidden= "display:none;";
        }


%>

<div class="preventionSection" style="<%=hidden%>" >
    <%if (!printView){%>
    <div style="position: relative; float: left; padding-right: 10px;" class="DoNotPrint">
       <input  type="checkbox" name="printHP"   value="<%=fsd.getAtcCode()%>" />
       <input type="radio" name="printStyle<%=fsd.getAtcCode()%>" value="all" checked >ALL</input><br>
       <input type="radio" name="printStyle<%=fsd.getAtcCode()%>" value="num" ># Elements</input>
       <input type="text" name="numEle<%=fsd.getAtcCode()%>" size="3"/>    <br>
       <input type="radio" name="printStyle<%=fsd.getAtcCode()%>"  value="range">Range</input>
       <input type="text" size="10" name="sDate<%=fsd.getAtcCode()%>"/> <input type="text" name="eDate<%=fsd.getAtcCode()%>" size="10"/>
    </div>
    <%}%>
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
        int k = 0;
        for (oscar.oscarRx.data.RxPrescriptionData.Prescription pres : arr){

            String hider = "";

            int num =1000;

            if (request.getParameter("num") != null){
                num = Integer.parseInt(request.getParameter("num"));
            }


            if (request.getParameter("show") !=null && request.getParameter("show").equals("lastOnly")){
                num =1;
            }

            if( forPrint.get(measure) != null ){
                Object oj = forPrint.get(measure);
                if ( oj instanceof Integer){
                    num = (Integer) oj;
                }else if (oj instanceof Hashtable){
                    Hashtable dates = (Hashtable)oj;
                    Date sdate = (Date) dates.get("sdate");
                    Date edate = (Date)dates.get("edate");
                    Date itDate = pres.getRxDate();
                    if (itDate.before(sdate) || itDate.after(edate)){
                        hider = "style=\"display:none\"";
                    }
                }
            }

            if ( k > num){
                hider = "style=\"display:none;\"";
            }



    %>
    <div class="preventionProcedure" <%=hider%> onclick="javascript:popup(465,635,'','addPreventionData')" >
        <p <%=""/*r(hdata.get("refused"))*/%> title="fade=[on] header=[<%=""/*hdata.get("age")*/%> -- Date:<%=""/*hdata.get("prevention_date")*/%>] body=[<%=""/*com*/%>]" ><%=pres.getBrandName()%> <br/>
            Date: <%=pres.getRxDate()%>
            <%-- if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%} --%>
        </p>
    </div>
    <%k++;}%>
</div>

<%}%>







<%if (printView){%>
   <input type="button" class="DoNotPrint" value="Print" onclick="javascript:window.print()">
   <input type="button" class="DoNotPrint" value="Back" onclick="javascript:history.go(-1);">
<%}else{%>
<input type="submit" value="Print Preview" class="DoNotPrint"/>
<%}%>
</form>




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
    <td class="MainTableBottomRowLeftColumn DoNotPrint">
        &nbsp;
    </td>
    <td class="MainTableBottomRowRightColumn DoNotPrint" valign="top">
        &nbsp;
    </td>
</tr>
</table>
<script type="text/javascript" src="../../share/javascript/boxover.js"></script>
</body>
</html:html>
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

%>
