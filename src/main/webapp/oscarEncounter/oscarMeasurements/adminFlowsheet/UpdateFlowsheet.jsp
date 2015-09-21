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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_flowsheet" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_flowsheet");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<% long startTime = System.currentTimeMillis(); %>
<%@ page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@ page import="org.jdom.Element,oscar.oscarEncounter.oscarMeasurements.data.*,org.jdom.output.Format,org.jdom.output.XMLOutputter,oscar.oscarEncounter.oscarMeasurements.util.*,java.io.*" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.oscarehr.common.dao.*,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>


<%
    long startTimeToGetP = System.currentTimeMillis();
    if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
    //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
 //TODO: MOVE THIS TO AN ACTION
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

String flowsheet   = request.getParameter("flowsheet");
String measurement = request.getParameter("measurement");
String demographic = request.getParameter("demographic");

long start = System.currentTimeMillis() ;

List<FlowSheetCustomization> custList = null;

if(demographic == null || demographic.isEmpty()) {
	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"));
} else {
	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"),Integer.parseInt(demographic));
}

String module="";
String htQueryString = "";
if(request.getParameter("htracker")!=null){
	module="htracker";
	htQueryString="&"+module;	
}

if(request.getParameter("htracker")!=null && request.getParameter("htracker").equals("slim")){
	module="slim";
	htQueryString=htQueryString+"=slim";
}


MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(flowsheet,custList);
long end = System.currentTimeMillis() ;
long diff = end - start;

Map h2 = mFlowsheet.getMeasurementFlowSheetInfo(measurement);
List<Recommendation> dsR = mFlowsheet.getDSElements((String) h2.get("measurement_type"));
FlowSheetItem fsi =mFlowsheet.getFlowSheetItem(measurement);
//EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<title>Update Flowsheet <%=flowsheet%>  <oscar:nameage demographicNo="<%=demographic%>"/></title><!--I18n-->

<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">


<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="<%=request.getContextPath() %>/js/html5.js"></script>
<![endif]-->



<style type="text/css">
#scrollToTop{
Position:fixed;
display:none;
bottom:30px;
right:15px;
}

.mtype-details{
display:inline-block;
}
</style>

<style type="text/css" media="print">
.DoNotPrint {
	display: none;
}
</style> 
                              
</head>

<body id="updateFlowsheetBody">

<%
if(request.getParameter("htracker")==null || ( request.getParameter("htracker")!=null && !request.getParameter("htracker").equals("slim")) ){

if(request.getParameter("demographic")==null){ %>
<div class="well well-small"></div>
<%}else{ %>
<%@ include file="/share/templates/patient.jspf"%>
<div style="height:60px;"></div>
<%} 
}
%>

<div class="container" id="container-main">

<div class="span8">
<h3 style="display:inline">Update Measurement</h3> <em>for <strong><%=flowsheet%></strong> flowsheet </em>

<form action="FlowSheetCustomAction.do">

		    <%if(request.getParameter("htracker")!=null){ %>
		    <input type="hidden" name="htracker" value="<%=module%>">
		    <%}%> 
			
            <input type="hidden" name="method" value="update"/>
            <input type="hidden" name="flowsheet" value="<%=flowsheet%>"/>
            <input type="hidden" name="measurement" value="<%=measurement%>"/>
            
            <%if(request.getParameter("demographic")!=null){ %>
            <input type="hidden" name="demographic" value="<%=demographic%>"/>
            <%} %>
            <fieldset width="300px">
               <input type="hidden" name="updater" value="yes"/>
               <input type="hidden" name="prevention_type" value="<%=h2.get("prevention_type")%>"/>
               <input type="hidden" name="measurement_type" value="<%=h2.get("measurement_type")%>" />
                
                <div class="well">
                <h4>Measurement Details</h4>
                
                <div class="mtype-details">
                Display Name: <br />
                <input type="text" name="display_name" value="<%= h2.get("display_name")%>" />
                </div>
                
                <div class="mtype-details">
                Guideline:  <br />  
                <input type="text" name="guideline"   value="<%=h2.get("guideline")%>"   />
                </div>
                
                <div class="mtype-details">
                Graphable: <br />
                <select name="graphable" style="width:80px">
                    <option  value="yes" <%=sel(""+h2.get("graphable"),"yes")%> >YES</option>
                    <option  value="no"  <%=sel(""+h2.get("graphable"),"no")%> >NO</option>
                </select> 
                </div>
                
                <div class="mtype-details">
                Value Name:<br />
                <input type="text" name="value_name"   value="<%=h2.get("value_name")%>"    />
                </div>
                </div>
                
                <div class="well">                
                    <h4>Rule</h4>
                    
                    <table class="table table-striped">
                    <%
                    int count = 0;
                    if (dsR != null) {
                        for (Recommendation e : dsR) { count++;
                        %>
                        <tr><td>
                            <div class="mtype-details">
                            Strength:  <br /> <select name="strength<%=count%>">
                                            <option value="recommendation" <%=sel(e.getStrength(),"recommendation")%>    >Recommendation</option>
                                            <option value="warning"        <%=sel(e.getStrength(),"warning")%>>Warning</option>
                                        </select>
                            </div>
                            
                            <div class="mtype-details">
                            Text: <br /><input type="text" name="text<%=count%>" length="100"  value="<%=e.getText()%>" />
							</div>

                               <%
                               List<RecommendationCondition> conds = e.getRecommendationCondition() ;
                               int condCount = 0;
                               for(RecommendationCondition cond:conds){condCount++;%>
							<br />
							<div class="mtype-details">
							<br />
                               <select name="type<%=count%>c<%=condCount%>" >
                                        <option value="monthrange"        <%=sel("monthrange", cond.getType())%>     >Month Range</option>
                                        <option value="lastValueAsInt"    <%=sel("lastValueAsInt",cond.getType())%>  >Last Int Value </option>
                                   </select>
							</div>
							
							<div class="mtype-details">
                                   Param: <br /><input type="text" name="param<%=count%>c<%=condCount%>" value="<%=s(cond.getParam())%>" />
                            </div>
                                                              
                            <div class="mtype-details">       
                                   Value: <br /><input type="text" name="value<%=count%>c<%=condCount%>" value="<%=cond.getValue()%>" />
                             </div>  

                               <%} condCount++;%>
                               <br />
                               
							<div class="mtype-details">
							<br />
                              <select name="type<%=count%>c<%=condCount%>" >
                                        <option value="monthrange"         >Month Range</option>
                                        <option value="lastValueAsInt"     >Last Int Value </option>
                              </select>
							</div>

								<div class="mtype-details">								
                                   Param: <br/> <input type="text" name="param<%=count%>c<%=condCount%>"  />
                                   </div>
                                   
                                    <div class="mtype-details">
                                    Value: <br /><input type="text" name="value<%=count%>c<%=condCount%>"  />
                               		</div>

                            <br/>
                            </td></tr>
                        <%
                        }
                    }
                    count++;
                    %>
					<tr><td>
                    <div class="mtype-details">
                    Strength:<br />   <select name="strength<%=count%>">
                                    <option value="recommendation"     >Recommendation</option>
                                    <option value="warning">Warning</option>
                                </select>
                    </div>
                    
                    <div class="mtype-details">
                                Text: <br /><input type="text" name="text<%=count%>" length="100"   />
					</div>
                    
                    <br />          

					<div class="mtype-details">
					<br />
                               <select name="type<%=count%>c1" >
                                        <option value="monthrange"         >Month Range</option>
                                        <option value="lastValueAsInt"     >Last Int Value </option>
                                   </select>
					</div>
					
					<div class="mtype-details">
                                   Param: <br /><input type="text" name="param<%=count%>c1"  />
                    </div>
                    
                    <div class="mtype-details">
                                   Value: <br /><input type="text" name="value<%=count%>c1"  />
                    </div>           
    				</td></tr>
    				</table>
                </div>


                <div class="well">
                <table class="table table-striped">
                    <%
                           Hashtable colourHash = mFlowsheet.getIndicatorHashtable();
                           List<TargetColour> list = fsi.getTargetColour();
                    int targetCount = 0;
                    if (list !=null){
                    for(TargetColour tc:list){ targetCount++;%>
                        
                            
                            <tr><td>
                           <h4>Target <%=targetCount%></h4>

                           
                               <%
                               List<TargetCondition> conds = tc.getTargetConditions() ;
                               int condCount = 0;
                               for(TargetCondition cond:conds){condCount++;%>
                               
							<div class="mtype-details">
							<br />
                               <select name="targettype<%=targetCount%>c<%=condCount%>" >
                                   <option value="getDataAsDouble"     <%=sel("getDataAsDouble", cond.getType())%>  >Number Value</option>
                                        <option value="isMale"              <%=sel("isMale",cond.getType())%>> Is Male </option>
                                        <option value="isFemale"            <%=sel("isFemale",cond.getType())%>> Is Female </option>
                                        <option value="getNumberFromSplit"  <%=sel("getNumberFromSplit",cond.getType())%>> Number Split </option>
                                        <option value="isDataEqualTo"       <%=sel("isDataEqualTo",cond.getType())%>>  String </option>
                                   </select>
							</div>
							
							<div class="mtype-details">
                                   Param:<br /> <input type="text" name="targetparam<%=targetCount%>c<%=condCount%>" value="<%=s(cond.getParam())%>" />
                            </div>
                            
                            <div class="mtype-details">       
                                   Value: <br /><input type="text" name="targetvalue<%=targetCount%>c<%=condCount%>" value="<%=cond.getValue()%>" />
                            </div>
                              <br />

                               <%}condCount++;%>

							<div class="mtype-details">
							<br />
                               <select name="targettype<%=targetCount%>c<%=condCount%>">
                                       <option value="-1">Not Set</option>
                                        <option value="getDataAsDouble"       >Number Value</option>
                                        <option value="isMale"              > Is Male </option>
                                        <option value="isFemale"            > Is Female </option>
                                        <option value="getNumberFromSplit"  > Number Split </option>
                                        <option value="isDataEqualTo"       >  String </option>
                                   </select>
							</div>
							
							<div class="mtype-details">
                                   Param: <br /><input type="text" name="targetparam<%=targetCount%>c<%=condCount%>" value="" />
                            </div>
                            
                            <div class="mtype-details">
                                   Value: <br /><input type="text" name="targetvalue<%=targetCount%>c<%=condCount%>" value="" />
                            </div>
                              
							<br />

                           

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
                           </td></tr>

                   <%}
                    }targetCount++;%>

				<tr><td>
                   <h4>Target <%=targetCount%></h4>

                            
					<div class="mtype-details">
					<br />
                               <select name="targettype<%=targetCount%>c1">
                                       <option value="-1">Not Set</option>
                                        <option value="getDataAsDouble"       >Number Value</option>
                                        <option value="isMale"              > Is Male </option>
                                        <option value="isFemale"            > Is Female </option>
                                        <option value="getNumberFromSplit"  > Number Split </option>
                                        <option value="isDataEqualTo"       >  String </option>
                                   </select>
					</div>
					
					<div class="mtype-details">
                                   Param: <br /><input type="text" name="targetparam<%=targetCount%>c1" value="" />
                    </div>
                    
                    <div class="mtype-details">
                                   Value: <br /><input type="text" name="targetvalue<%=targetCount%>c1" value="" />
                    </div>               
                     <br />
  
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
						</td></tr>
						</table>
						
                    </div>
               
				<div style="width:100%;text-align:right">
				<%if(request.getParameter("demographic")==null){ %>
				<a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>" class="btn">Cancel</a>
				<%}else{ %>
                <a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>&demographic=<%=demographic%><%=htQueryString%>" class="btn">Cancel</a> 
                <%} %>
                <input type="submit" class="btn btn-primary" value="Update" />
                </div>
                
            </fieldset>
           </form>
           </div>
    </div>

<div id="scrollToTop"><a href="#updateFlowsheetBody"><i class="icon-arrow-up"></i>Top</a></div>


<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script> 
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>

<script src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>

<script>
$(document).ready(function () {
	var h = $(document).height();
	parent.parent.document.getElementById('trackerSlim').style.height = h+"px";
	
	$(document).scroll(function () {
	    var y = $(this).scrollTop();
	    if (y > 60) {
	        $('#scrollToTop').fadeIn();
	    } else {
	        $('#scrollToTop').fadeOut();
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
