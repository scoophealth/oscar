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
<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@page import="org.jdom.Element,oscar.oscarEncounter.oscarMeasurements.data.*,org.jdom.output.Format,org.jdom.output.XMLOutputter,oscar.oscarEncounter.oscarMeasurements.util.*,java.io.*" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.common.dao.*,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>


<%
    long startTimeToGetP = System.currentTimeMillis();
    //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");


    String temp = "diab2";//"physFunction";  //
    if (request.getParameter("flowsheet") != null) {
        temp = request.getParameter("flowsheet");
    }
    String flowsheet = temp;
    String demographic = request.getParameter("demographic");
    MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
    Hashtable<String, String> flowsheetNames = templateConfig.getFlowsheetDisplayNames();

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
    List<FlowSheetCustomization> custList = null;
    if(demographic == null || demographic.isEmpty()) {
    	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"));
    } else {
    	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"),Integer.parseInt(demographic));
    }
    Enumeration en = flowsheetNames.keys();

    EctMeasurementTypesBeanHandler hd = new EctMeasurementTypesBeanHandler();
    Vector<EctMeasurementTypesBean> vec = hd.getMeasurementTypeVector();
    String demographicStr = "";
    if (demographic != null){
        demographicStr = "&demographic="+demographic;
    }

    XMLOutputter outp = new XMLOutputter();
    outp.setFormat(Format.getPrettyFormat());

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<title><%=flowsheet%> </title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../../../share/javascript/prototype.js"></script>

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
        editFlowsheet
    </td>
    <td class="MainTableTopRowRightColumn">
        <table class="TopStatusBar">
            <tr>
                <td >
                    Flowsheet : <%=flowsheet%>

                    Demographic :
                    <% if (demographic!=null) { %>
                    <oscar:nameage demographicNo="<%=demographic%>"/>  <a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>">Go to all patients</a>
                    <%}else{%>
                        All Patients
                    <%}%>

                </td>
                <td  >&nbsp;

                </td>
                <td style="text-align:right">
                    <oscar:help keywords="flowsheet" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td class="MainTableLeftColumn" valign="top">

   <ul style="width:200px; float:left;list-style-type:none;padding-left:5px;">
            <%
            MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(temp,custList);
            Element va = templateConfig.getExportFlowsheet(mFlowsheet);

            List<String> measurements = mFlowsheet.getMeasurementList();

            int count = 0;
            if (measurements != null) {
                for (String mstring : measurements) {

                    count++;%>
            <li>
                <%=count%> : <span title="<%=mstring%>"><%=mFlowsheet.getFlowSheetItem(mstring).getDisplayName()%></span>
                <a href="UpdateFlowsheet.jsp?flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%>">edit</a>
                <a href="FlowSheetCustomAction.do?method=delete&flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%>">delete</a>

            </li>
            <%
                }

            }
            %>
        </ul>


</td>

<td valign="top" class="MainTableRightColumn">
<div style="margin-left:10px;">
        <form action="FlowSheetCustomAction.do">
            <input type="hidden" name="flowsheet" value="<%=temp%>"/>
            <input type="hidden" name="method" value="save"/>
            <%if (demographic !=null){%>
                    <input type="hidden" name="demographic" value="<%=demographic%>"/>
            <%}%>
            <fieldset width="300px">

                <select name="measurement">
                    <% for (EctMeasurementTypesBean measurementTypes : vec){ %>
                    <option value="<%=measurementTypes.getType()%>" ><%=measurementTypes.getTypeDisplayName()%> (<%=measurementTypes.getType()%>) </option>
                    <% } %>
                </select><br>
                Display Name: <input type="text" name="display_name" /><br>
                Guideline:    <input type="text" name="guideline"    /><br>
                Graphable: <select name="graphable"   >
                    <option  value="yes" >YES</option>
                    <option  value="no">NO</option>
                </select><br>
                Value Name:<input type="text" name="value_name"       /><br>
                <div>
                    <h3>Add Rule</h3>
                    <br>
                    Month Range: <input type="text" name="monthrange1"/>
                    Strength:   <select name="strength1">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select>
                    Text: <input type="text" name="text1" length="100"/>

                    <br/>

                    Month Range: <input type="text" name="monthrange2"/>
                    Strength:   <select name="strength2">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select>
                    Text: <input type="text" name="text2" length="100"/>

                    <br/>

                    Month Range: <input type="text" name="monthrange3"/>
                    Strength:   <select name="strength3">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select>
                    Text: <input type="text" name="text3"  length="100"/>

                    <br/>


                </div>


                Count: <input type="text" name="count" /> <br>
                <input type="submit" value="Save" />
            </fieldset>
        </form>
    </div>


     <ul>
    <%for (FlowSheetCustomization cust :custList){%>
       <li><a href="FlowSheetCustomAction.do?method=archiveMod&id=<%=cust.getId()%>&flowsheet=<%=flowsheet%><%=demographicStr%>"><%=cust.getAction()%> -  <%=cust.getMeasurement()%> - <%=cust.getProviderNo()%> - <%=cust.getDemographicNo()%></a></li>
    <%}%>
    </ul>




</td>
</tr>
<tr>
    <td class="MainTableBottomRowLeftColumn">
        &nbsp;
    </td>
    <td class="MainTableBottomRowRightColumn" valign="top">
        <textarea style="display:none;" cols="200" rows="200">
            <%=outp.outputString(va)%>
        </textarea>
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
