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
<% long startTime = System.currentTimeMillis(); %>
<%@ page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.oscarehr.common.dao.FlowSheetCustomizationDao,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@ page import="org.oscarehr.common.dao.FlowSheetDrugDao,org.oscarehr.common.model.FlowSheetDrug"%>
<%@ page import="oscar.util.UtilDateUtilities" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet"
	rights="r" reverse="<%=true%>">
"You have no right to access this page!"
<% response.sendRedirect("../../noRights.html"); %>
</security:oscarSec>


<!--*************************************************************************************
NOTES:
This is all you need to print a single meas
TemplateFlowSheetPrint.jsp?demographic_no=1&template=diab2&printView=true&printHP=LDL&printStyleLDL=all&numEleLDL=&sDateLDL=&eDateLDL=

maybe use jquery/ajax to post this data instead of submitting a form to send ALL data???

*************************************************************************************-->


<%
    String demographic_no = request.getParameter("demographic_no");
    String providerNo = (String) session.getAttribute("user");
    boolean printView = false;
    String date = UtilDateUtilities.getToday("yyyy-MM-dd");

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
    Prevention p = PreventionData.getPrevention(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.valueOf(demographic_no));

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

	try{
	    mFlowsheet.getMessages(mi);
	}catch(Exception e){
	//do nothing
	}
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<title><oscar:nameage demographicNo="<%=demographic_no%>"/> - <%=flowSheet%> Custom Print</title><!--I18n-->

<meta name="viewport" content="width=device-width, user-scalable=false;">

<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="<%=request.getContextPath() %>/js/html5.js"></script>
<![endif]-->

<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="ico/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="ico/apple-touch-icon-72-precomposed.png">
                <link rel="apple-touch-icon-precomposed" href="ico/apple-touch-icon-57-precomposed.png">
                               <link rel="shortcut icon" href="ico/favicon.png">
                                   
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/DT_bootstrap.css">

<style type="text/css">
    div.ImmSet { background-color: #ffffff;clear:left;margin-top:10px;}
    div.ImmSet h2 {  }
    div.ImmSet h2 span { font-size:smaller; }
    div.ImmSet ul {  }
    div.ImmSet li {  }
    div.ImmSet li a { text-decoration:none; color:blue;}
    div.ImmSet li a:hover { text-decoration:none; color:red; }
    div.ImmSet li a:visited { text-decoration:none; color:blue;}
</style>

<style type="text/css">
body {font-size:100%}

input[type=checkbox].css-checkbox {
position: absolute; 
overflow: hidden; 
clip: rect(0 0 0 0); 
height:1px; 
width:1px; 
margin:-1px; 
padding:0;
border:0;
}

input[type=checkbox].css-checkbox + label.css-label {
padding-left:20px;
height:15px; 
display:inline-block;
line-height:15px;
background-repeat:no-repeat;
background-position: 0 0;
font-size:15px;
vertical-align:middle;
cursor:pointer;
}

input[type=checkbox].css-checkbox:checked + label.css-label {
background-position: 0 -15px;
}

.css-label{ background-image:url(<%=request.getContextPath()%>/images/dark-check-green.png); }

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
    font-size: 8pt;
    font-variant:small-caps;
    font-weight:bold;
    margin-top:0px;
    padding-top:0px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.leftBox ul{
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

.DoNotScreen{ display:none;}

div.headPrevention {
vertical-align:top;
display:inline-block;
width:20%;
}

div.headPrevention p {
    display:inline;
    margin:0;
    padding: 4px 4px;
    line-height: 1.2;  
  
}

div.headPrevention a {
    text-decoration:none;
}

.inner{
display:inline-block;
width:70%;
}

div.preventionProcedure{
display:inline-block;
    width:9em;
    margin-left:3px;
    margin-bottom:3px;
}


div.preventionProcedure p {
vertical-align: top;
    font-size: 0.8em;
    font-family: verdana,tahoma,sans-serif;
    background: #F0F0E7;
    margin:0;
    padding: 1px 2px;
}

div.preventionSection {
    position:relative;
    width: 100%;
    margin-top:5px;
    border-bottom:thin solid #c6c6c6;  
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

.module-block{
position:absolute;top:0;left:0px;background-color:#333;color:#fff;font-size:20px;padding:6px;padding-right:20px;
}

.module-block a{
text-decoration:none;
color:#fff;
}

.controls{
display:inline-block;
padding-right:20px;

}

#scrollToTop{
position:fixed;
display:none;
bottom:30px;
right:15px;
}

.input-error{   
    border-color: rgba(229, 103, 23, 0.8) !important; 
    box-shadow: 0 1px 1px rgba(229, 103, 23, 0.075) inset, 0 0 8px rgba(229, 103, 23, 0.6) !important; 
    outline: 0 none !important;
    
}

#wrapper-header{
padding:0px;
margin:0px;
}

#wrapper-content{
position:relative;
width:100%;

}

#mtype-list{
padding-top:2px;
}

.alert{
display:none;
position:absolute;
top:40px;
right:4px;
z-index:999;
}

</style> 


<!--PRINT CSS-->
<style type="text/css" media="print">
.DoNotPrint {
	display: none;
}

.inner{
display:inline-block;
width:100%;
}

div.headPrevention a:active { color:black; }
div.headPrevention a:hover { color:black; }
div.headPrevention a:link { color:black; }
div.headPrevention a:visited { color:black; }
</style>


</head>

<body class="BodyStyle" id="printFlowsheetBody">

<form action="TemplateFlowSheetPrint.jsp" id="flowsheetPrintForm" method="post" class="form-inline">
    <input type="hidden" name="demographic_no" value="<%=demographic_no%>"/>
    <input type="hidden" name="template" value="<%=temp%>"/>
    <input type="hidden" name="printView" value="true"/>
    
<div id="wrapper-header">

<div class="module-block DoNotPrint">
<%if (!printView){%>
	<%if (request.getParameter("htracker") != null) {%> 
	<a href="HealthTrackerPage.jspf?demographic_no=<%=demographic_no%>&template=<%=temp%>" title="go back to <%=temp%>"><< <%=flowSheet%></a> <br/>
	<%}else{%>
	<a href="TemplateFlowSheet.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>" title="go back to <%=temp%>"><< <%=flowSheet%></a> <br/>
	<%} %>
<a href="JavaScript:void(0);" class="back" title="go back to <%=flowSheet%>"></a>

<%}else{%>
<a href="JavaScript:void(0);" class="back" title="go back to custom print"> << <%=flowSheet%> - Print</a>
<%}%>

</div><!-- module-block -->

<div class="well" style="padding-bottom:0px;margin-bottom:0px;">
	<h3><oscar:nameage demographicNo="<%=demographic_no%>"/></h3>				
</div>
                               
<!-- VIEW CONTROL -->
<div class="well DoNotPrint" style="margin:0px;padding-top:2px;padding-bottom:2px;background-color:#c6c6c6;overflow: hidden">

<%if (!printView){%>
<div class="controls">
	<input type="checkbox" name="select-all-chk" id="select-all-chk" class="css-checkbox"  value="select-all"/>
	<label for="select-all-chk" name="select-all-lbl" class="css-label">Select All</label>
</div>
	
<div class="controls">
		<input type="checkbox" name="print-comments-chk" id="print-comments-chk" class="css-checkbox"  value="print"/>
		<label for="print-comments-chk" name="print-comments-lbl" class="css-label">Print Comments <a href="#comments-list"><span class="label label-info">view</span></a></label>
</div>

<div class="controls">
		<input  type="checkbox" name="print-recommendation-chk" id="print-recommendation-chk" class="css-checkbox"  value="print"/>
		<label for="print-recommendation-chk" name="print-recommendation-lbl" class="css-label">Print Recommendations <a href="#recommendations-list"><span class="label label-info">view</span></a></label>
</div>
<%}%>
	

	<div class="controls DoNotPrint" style="float:right">

		<%if (printView){%>
		<a href="JavaScript:void(0);" class="btn btn-small back loading" title="Cancel" data-loading-text="cancelling...">Cancel</a>			
		<button type="button" class="btn btn-small btn-success DoNotPrint" onclick="javascript:window.print();"><i class="icon-print icon-white"></i> Print</button>	
		<%}else{%>

view:
		<div class="btn-group">
		<a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>" id="all-btn" class="btn btn-small loading" data-loading-text="Loading...">All</a>
		<a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=lastOnly" id="lastOnly-btn" class="btn btn-small loading" data-loading-text="Loading...">Last Only</a>
		<a href="TemplateFlowSheetPrint.jsp?demographic_no=<%=demographic_no%>&template=<%=temp%>&show=outOfRange" id="outOfRange-btn" class="btn btn-small loading" data-loading-text="Loading...">Out of Range</a>
		</div>

			<button type="submit" class="btn btn-small DoNotPrint loading-print preview" data-loading-text="Loading..."><i class="icon-print"></i> Preview</button>

		<%}%>
	</div><!-- controls -->
</div><!-- VIEW CONTROL -->
</div><!-- wrapper-header-->

<div id="wrapper-content">
<!--MEASUREMENTS SELECTION LIST  overflow: hidden;-->
<div class="well" id="mtype-list">

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

<!--MEASUREMENTS ROW-->
<div class="preventionSection"  style="<%=hidden%>">

    <%if (!printView){%>
    <div style="position: relative; float: left; padding-right: 10px;" class="DoNotPrint">
	
	<input type="checkbox" name="printHP" id="printHP<%=measure%>" class="css-checkbox"  value="<%=measure%>"  <%=setToPrint ? "checked" : ""%>/>
	<label for="printHP<%=measure%>" name="printHP<%=measure%>" class="css-label"></label><!--needed for chkbox effect-->

    </div>
    <%}%>



<!--MEASUREMENT TITLE-->
    <div class="headPrevention">

<p>
<span  style=""><%=item.getDisplayName()%></span>
<br/>

</p>

<div id="refine-results-<%=measure%>" style="display:none;position:relative;">

<select name="printStyle<%=measure%>" id="refineSelect-<%=measure%>" style="width:110px" rel="<%=measure%>">
	<option value="all" selected>all <%=measure%></option>
	<option value="num"># Elements</option>
	<option value="range">date range</option>
</select>


<input type="text" name="numEle<%=measure%>" class="num-<%=measure%>" style="display:none;width:20px" placeholder=""/>      


<div class="range-<%=measure%>" style="display:none">
	<div class="input-append date" id="dp-startDate" data-date="<%=date%>" data-date-format="yyyy-mm-dd" title="Start Date">
	<input style="width:90px" name="sDate<%=measure%>" id="sDate-<%=measure%>" size="16" type="text" value="" placeholder="start" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$">
	<span class="add-on"><i class="icon-calendar"></i></span>
	</div>

	<div class="input-append date" id="dp-endDate" data-date="<%=date%>" data-date-format="yyyy-mm-dd" title="End Date">
	<input style="width:90px" name="eDate<%=measure%>" id="eDate-<%=measure%>" size="16" type="text" value="" placeholder="end" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$">
	<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div><!--range container-->

 

</div><!--refine-results-->


</div><!--headPrevention-->



<div class="inner">
    <%  int k = 0;
        for (EctMeasurementsDataBean mdb:alist){
            k++;
            mFlowsheet.runRulesForMeasurement(LoggedInInfo.getLoggedInInfoFromSession(request), mdb);
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

	<p <%=indColour%> title="Entered By: <%=mdb.getProviderFirstName()%> <%=mdb.getProviderLastName()%>">
	<%=h2.get("value_name")%>: <%=hdata.get("age")%> <br/>
            <%=hdata.get("prevention_date")%>&nbsp;<%=mdb.getNumMonthSinceObserved()%>M
            <%if (comb) {%>
            <span class="footnote"><%=comments.size()%></span>
            <%}%>

        </p>
    </div>
    <%}%>
</div>
</div>

<!--PREVENTIONS -->
<%}else{


    String prevType = (String) h2.get("prevention_type");
    long startPrevType = System.currentTimeMillis();
    ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(LoggedInInfo.getLoggedInInfoFromSession(request), prevType, Integer.valueOf(demographic_no));
%>

<div class="preventionSection" style="<%=hidden%>">
    <%if (!printView){%>
    <div style="position: relative; float: left; padding-right: 10px;" class="DoNotPrint">
       	<input type="checkbox" name="printHP" id="printHP<%=measure%>" class="css-checkbox"  value="<%=measure%>"  <%=setToPrint ? "checked" : ""%>/>
	<label for="printHP<%=measure%>" name="printHP<%=measure%>" class="css-label"></label><!--needed for chkbox effect-->
    </div>
    <%}%>

    <div class="headPrevention">
        <p title="<%=h2.get("display_name")%>">
           <span title="<%=h2.get("guideline")%>"><%=h2.get("display_name")%></span>
        </p>
    </div><!--headPrevention-->
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
    </div><!--preventionProcedure-->
    <%}%>

</div> <!--preventionSection-->

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

	<input  type="checkbox" name="printHP" id="printHP<%=fsd.getAtcCode()%>" class="css-checkbox"  value="<%=fsd.getAtcCode()%>"/>
	<label for="printHP<%=measure%>" name="printHP<%=measure%>" class="css-label"></label><!--needed for chkbox effect-->


    </div>
    <%}%>

    <div class="headPrevention">
        <p title="">
		<span title=""><%=arr[0].getGenericName()%></span>
            <br/>
        </p>
    </div> <!--headPrevention-->
   
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
    </div> <!--preventionProcedure-->
    <%k++;}%>

</div> <!--preventionSection drugs-->

<%}%>


</div><!--MEASUREMENTS SELECTION LIST END-->
</form>


<%
String noPrintStyle = "style='display:none;'";
String noPrint1 = "";
String noPrint2 = "";

 if(printView){

		if(request.getParameter("print-comments-chk")!=null && request.getParameter("print-comments-chk").equals("print")){

		noPrint1="";

		}else{
		noPrint1=noPrintStyle;
		}


		if(request.getParameter("print-recommendation-chk")!=null && request.getParameter("print-recommendation-chk").equals("print")){
		 noPrint2="";
		}else{
		 noPrint2=noPrintStyle;
		}

}//if printView
%>



<!--COMMENTS-->

<div class="well" id="comments-list"  <%=noPrint1%>  >
    <h4>Comments</h4>
    <ol>
        <% for (int i = 0; i < comments.size(); i++){
            String str = (String) comments.get(i);
        %>
        <li><%=str%></li>
        <% }%>
    </ol>
</div> <!--COMMENTS END-->


<!--RECOMMENDATIONS-->
<% if (warnings.size() > 0 || recomendations.size() > 0  || dsProblems) { %>
<div class="well" id="recommendations-list" <%=noPrint2%> >
   
 <h4><%=flowSheet%> Recommendations</h4>
   
    <ul id="recomList">
        <% for (String warn : warnings){ %>
        <li><%=warn%></li>
        <%}%>
        <% for (String warn : recomendations ){%>
        <li><%=warn%></li>
        <%}%>
        <!--li style="color: red;">6 month TD overdue</li>
  <li>12 month MMR due in 2 months</li-->
        <% if (dsProblems){ %>
        <li>Decision Support Had Errors Running.</li>
        <% } %>
    </ul><!--RECOMMENDATIONS END-->
        
</div>
<% } %>

</div> <!-- wrapper-content -->

<div id="scrollToTop" class="DoNotPrint"><a href="#printFlowsheetBody" class="DoNotPrint"><i class="icon-arrow-up"></i>Top</a></div>

    <div class="alert">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong>Oops!</strong> You need to make a selection before you can generate a print preview.
    </div>

<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script> 
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script> 

<script src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>

<script type="text/javascript">

$(".preview").click(function() {

	if ($('#flowsheetPrintForm :checkbox:checked').length > 0){
			$(".alert").fadeOut('slow');
			return true;
	  }
	  else{
		  $(".alert").fadeIn('slow');
			return false;
	  }

});

$(function (){ 
	$('[id^=dp-]').datepicker();
});

$(document).ready(function () {

	$(document).scroll(function () {
	    var y = $(this).scrollTop();
	    if (y > 60) {
	        $('#scrollToTop').fadeIn();
	    } else {
	        $('#scrollToTop').fadeOut();
	    }
	});

$('.loading').click(function(){
	$(this).button('loading');
});

$('a.back').click(function(){
	parent.history.back();
	return false;
});


$("[id$=-btn]").removeClass("btn-primary active");
<%if (request.getParameter("show") !=null ){%>
	$('#<%=request.getParameter("show")%>-btn').addClass("btn-primary active");
<%}else{%>
	$('#all-btn').addClass("btn-primary active");
<%}%>

$('#select-all-chk').click(function(){

	if($('#select-all-chk').is(':checked')){
	$("[id^=printHP]").attr ( "checked" ,"checked" );
	$("[id^=refine-results-]").show();
	}
	else
	{
	$("[id^=printHP]").removeAttr('checked');
	$("[id^=refine-results-]").hide();
	}

});


$("[id^=refineSelect]").change(function(){
	var v = $(this).val();
	var m = $(this).attr("rel");
	
	if(v=="num"){
		$("."+v+"-"+m).toggle();
		
		$(".range-"+m).hide();

	}else if(v=="range"){
		$("."+v+"-"+m).toggle();
		$(".num-"+m).hide();
	}else{
		$(".range-"+m).hide();
		$(".num-"+m).hide();
	}
});



$("[id^=printHP]").click(function(){
	
var v = $(this).val();

if($(this).is(':checked')){
$("#refine-results-"+v).show();
}else{
$("#refine-results-"+v).hide();
}

});


$("#flowsheetPrintForm").submit(function(){

	var error=false;

	$("[id^=printHP]").each(function(){
		

		if($(this).is(':checked')){

			var v = $(this).val();

			var x = $("#refineSelect-"+v).val();	

					
			if(x=="num" && $(".num-"+v).val()==""){
				
				$(".num-"+v).addClass("input-error");
				error=true;

			}else{

				$(".num-"+v).removeClass("input-error");
				
			} 



			if(x=="range" && $("#sDate-"+v).val()=="" ){
				$("#sDate-"+v).addClass("input-error");
				error=true;

		 	}else{
				$("#sDate-"+v).removeClass("input-error");

			} 


			if(x=="range" && $("#eDate-"+v).val()==""){
				$("#eDate-"+v).addClass("input-error");
				error=true;

			}else{
				$("#eDate-"+v).removeClass("input-error");
			}

		}
	});
     

	if(error==true){
	return false;
	}else{
	$(".loading-print").button('loading');
	return true;
	}


});

});
</script>


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
