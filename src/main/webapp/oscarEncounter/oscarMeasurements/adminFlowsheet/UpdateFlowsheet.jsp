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
    if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
    //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
 //TODO: MOVE THIS TO AN ACTION
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

String flowsheet   = request.getParameter("flowsheet");
String measurement = request.getParameter("measurement");
String demographic = request.getParameter("demographic");

long start = System.currentTimeMillis() ;
List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"),demographic);
MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(flowsheet,custList);
long end = System.currentTimeMillis() ;
long diff = end - start;

Map h2 = mFlowsheet.getMeasurementFlowSheetInfo(measurement);
List<Recommendation> dsR = mFlowsheet.getDSElements((String) h2.get("measurement_type"));
FlowSheetItem fsi =mFlowsheet.getFlowSheetItem(measurement);
//EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<title><%=flowsheet%> - <oscar:nameage demographicNo="<%=demographic%>"/></title><!--I18n-->
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
                    Flowsheet : <%=flowsheet%>   - <%=measurement%>

                    Demographic :
                    <% if (demographic!=null) { %>
                    <oscar:nameage demographicNo="<%=demographic%>"/>  <!--  a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>">Go to all patients</a  -->
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

</td>

<td valign="top" class="MainTableRightColumn">
<div style="margin-left:10px;">
      <form action="FlowSheetCustomAction.do">
            <input type="hidden" name="method" value="update"/>
            <input type="hidden" name="flowsheet" value="<%=flowsheet%>"/>
            <input type="hidden" name="measurement" value="<%=measurement%>"/>
            <input type="hidden" name="demographic" value="<%=demographic%>"/>
            <fieldset width="300px">
               <input type="hidden" name="updater" value="yes"/>
               <input type="hidden" name="prevention_type" value="<%=h2.get("prevention_type")%>"/>
               <input type="hidden" name="measurement_type" value="<%=h2.get("measurement_type")%>" />
                Display Name: <input type="text" name="display_name" value="<%= h2.get("display_name")%>" /><br>
                Guideline:    <input type="text" name="guideline"   value="<%=h2.get("guideline")%>"   /><br>
                Graphable: <select name="graphable"   >
                    <option  value="yes" <%=sel(""+h2.get("graphable"),"yes")%> >YES</option>
                    <option  value="no"  <%=sel(""+h2.get("graphable"),"no")%> >NO</option>
                </select><br>
                Value Name:<input type="text" name="value_name"   value="<%=h2.get("value_name")%>"    /><br>
                <div>
                    <h3>Rule</h3>
                   <br/>

                    <%
                    int count = 0;
                    if (dsR != null) {
                        for (Recommendation e : dsR) { count++;
                        %>
                            Strength:   <select name="strength<%=count%>">
                                            <option value="recommendation" <%=sel(e.getStrength(),"recommendation")%>    >Recommendation</option>
                                            <option value="warning"        <%=sel(e.getStrength(),"warning")%>>Warning</option>
                                        </select>
                            Text: <input type="text" name="text<%=count%>" length="100"  value="<%=e.getText()%>" />

                               <ul style="list-style-type: none;" >
                               <%
                               List<RecommendationCondition> conds = e.getRecommendationCondition() ;
                               int condCount = 0;
                               for(RecommendationCondition cond:conds){condCount++;%>

                               <li><select name="type<%=count%>c<%=condCount%>" >
                                        <option value="monthrange"        <%=sel("monthrange", cond.getType())%>     >Month Range</option>
                                        <option value="lastValueAsInt"    <%=sel("lastValueAsInt",cond.getType())%>  >Last Int Value </option>
                                   </select>

                                   Param: <input type="text" name="param<%=count%>c<%=condCount%>" value="<%=s(cond.getParam())%>" />
                                   Value: <input type="text" name="value<%=count%>c<%=condCount%>" value="<%=cond.getValue()%>" />
                               </li>

                               <%} condCount++;%>

                               <li><select name="type<%=count%>c<%=condCount%>" >
                                        <option value="monthrange"         >Month Range</option>
                                        <option value="lastValueAsInt"     >Last Int Value </option>
                                   </select>

                                   Param: <input type="text" name="param<%=count%>c<%=condCount%>"  />
                                   Value: <input type="text" name="value<%=count%>c<%=condCount%>"  />
                               </li>
                               </ul>

                            <br/>
                        <%
                        }
                    }
                    count++;
                    %>

                    NEW<br>
                    Strength:   <select name="strength<%=count%>">
                                    <option value="recommendation"     >Recommendation</option>
                                    <option value="warning">Warning</option>
                                </select>
                                Text: <input type="text" name="text<%=count%>" length="100"   />

                               <ul style="list-style-type: none;" >


                               <li><select name="type<%=count%>c1" >
                                        <option value="monthrange"         >Month Range</option>
                                        <option value="lastValueAsInt"     >Last Int Value </option>
                                   </select>

                                   Param: <input type="text" name="param<%=count%>c1"  />
                                   Value: <input type="text" name="value<%=count%>c1"  />
                               </li>
                               </ul>




                    <br/>
                </div>


                <div>
                    <%
                           Hashtable colourHash = mFlowsheet.getIndicatorHashtable();
                           List<TargetColour> list = fsi.getTargetColour();
                    int targetCount = 0;
                    if (list !=null){
                    for(TargetColour tc:list){ targetCount++;%>
                        <div style="border: 1px;">
                           <h3>Target <%=targetCount%></h3>

                            <ul style="list-style-type: none;" >
                               <%
                               List<TargetCondition> conds = tc.getTargetConditions() ;
                               int condCount = 0;
                               for(TargetCondition cond:conds){condCount++;%>

                               <li><select name="targettype<%=targetCount%>c<%=condCount%>" >
                                   <option value="getDataAsDouble"     <%=sel("getDataAsDouble", cond.getType())%>  >Number Value</option>
                                        <option value="isMale"              <%=sel("isMale",cond.getType())%>> Is Male </option>
                                        <option value="isFemale"            <%=sel("isFemale",cond.getType())%>> Is Female </option>
                                        <option value="getNumberFromSplit"  <%=sel("getNumberFromSplit",cond.getType())%>> Number Split </option>
                                        <option value="isDataEqualTo"       <%=sel("isDataEqualTo",cond.getType())%>>  String </option>
                                   </select>

                                   Param: <input type="text" name="targetparam<%=targetCount%>c<%=condCount%>" value="<%=s(cond.getParam())%>" />
                                   Value: <input type="text" name="targetvalue<%=targetCount%>c<%=condCount%>" value="<%=cond.getValue()%>" />
                               </li>

                               <%}condCount++;%>

                               <li><select name="targettype<%=targetCount%>c<%=condCount%>">
                                       <option value="-1">Not Set</option>
                                        <option value="getDataAsDouble"       >Number Value</option>
                                        <option value="isMale"              > Is Male </option>
                                        <option value="isFemale"            > Is Female </option>
                                        <option value="getNumberFromSplit"  > Number Split </option>
                                        <option value="isDataEqualTo"       >  String </option>
                                   </select>

                                   Param: <input type="text" name="targetparam<%=targetCount%>c<%=condCount%>" value="" />
                                   Value: <input type="text" name="targetvalue<%=targetCount%>c<%=condCount%>" value="" />
                               </li>


                           </ul>

                           <!-- div style="width:200px;" -->
                           <ul style="display: inline;  list-style-type: none; ">
                               <%Enumeration en = colourHash.keys();
                               while(en.hasMoreElements()){
                                 String colour = (String) en.nextElement();  %>

                               <li style="display:inline;background-color:<%=colourHash.get(colour)%>;">
                                   <input type="radio" name="col<%=targetCount%>" value="<%=colour%>" <%=s(colour,tc.getIndicationColor())%> ><%=colour%></input>
                               </li>
                               <%}%>
                           </ul>
                           <!--  /div  -->

                   <%}
                    }targetCount++;%>

                   <h3>New Target <%=targetCount%></h3>

                            <ul style="list-style-type: none;" >

                               <li><select name="targettype<%=targetCount%>c1">
                                       <option value="-1">Not Set</option>
                                        <option value="getDataAsDouble"       >Number Value</option>
                                        <option value="isMale"              > Is Male </option>
                                        <option value="isFemale"            > Is Female </option>
                                        <option value="getNumberFromSplit"  > Number Split </option>
                                        <option value="isDataEqualTo"       >  String </option>
                                   </select>

                                   Param: <input type="text" name="targetparam<%=targetCount%>c1" value="" />
                                   Value: <input type="text" name="targetvalue<%=targetCount%>c1" value="" />
                               </li>


                           </ul>

                           <!-- div style="width:200px;" -->
                           <ul style="display: inline;  list-style-type: none; ">
                               <%Enumeration en = colourHash.keys();
                               while(en.hasMoreElements()){
                                 String colour = (String) en.nextElement();  %>
                               <li style="display:inline;background-color:<%=colourHash.get(colour)%>;">
                                   <input type="radio" name="col<%=targetCount%>" value="<%=colour%>"  ><%=colour%></input>
                               </li>
                               <%}%>
                           </ul>

                    </div>
                </div>

               <input type="button" value="Cancel" onclick="javascript:history.go(-1)">  <input type="submit" value="Update">
            </fieldset>
        </form>

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


    String s(String s){
    if (s ==null || s.equalsIgnoreCase("null")){
        return "";
    }
    return s;
}
String s(String s1,String s2){
    if (s1 != null && s2 != null && s1.equals(s2)){
        return "checked";
    }
    return "";
}

String sel(String s1,String s2){
    if (s1 != null && s2 != null && s1.equals(s2)){
        return "selected";
    }
    return "";
}



%>
