<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="oscar.oscarReport.data.DemographicSets, oscar.oscarDemographic.data.DemographicData" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="/casemgmt/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style type="text/css">
            a:hover {
                font-weight: bold;
            }
            
            div.demographicSection{
               //width:49%;
               width:100%;
               margin-top: 2px;
               margin-left:3px;
               border-top: 1px solid #ccccff;
               border-bottom: 1px solid #ccccff;
               border-left: 1px solid #ccccff;
               border-right: 1px solid #ccccff;
               float: left;
            }

            div.demographicSection h3 {
               background-color: #ccccff;
               font-size: 8pt;
               font-variant:small-caps;
               font:bold;
               margin-top:0px;
               padding-top:0px;
               margin-bottom:0px;
               padding-bottom:0px;
            }

            div.demographicSection ul{

                   list-style:none;
                   list-style-type:circle;
                   list-style-position:inside;
                   padding-left:1px;
                   margin-left:1px;
                   margin-top:0px;
                   padding-top:1px;
                   margin-bottom:0px;
                   padding-bottom:0px;
                   background-color: #EEEEFF;
            }


            div.demographicSection li {
            padding-right: 15px;
            white-space: nowrap;
            }
        </style>
    </head>
    <body>
        <div class="demographicSection">
            <%        
            String demoNo = request.getParameter("demographic_no");
            DemographicSets demoSets = new DemographicSets();
            ArrayList arrCurDemoSets = demoSets.getDemographicSets(demoNo);                
            ArrayList arrDemo = new ArrayList();
            arrDemo.add(demoNo);        
            pageContext.setAttribute("curSets",arrCurDemoSets);     
            DemographicData demoData = new DemographicData();
            String setName = request.getParameter("setName");
            if( setName != null && setName.trim().length() > 0 ) {
            if( !arrCurDemoSets.contains(setName) ) {
            demoSets.addDemographicSet(setName,arrDemo);
            arrCurDemoSets.add(setName);
            %>
            <p style="font-size:small; font-variant:small-caps">Saved <%=demoData.getDemographic(demoNo).getFirstName() + " " + demoData.getDemographic(demoNo).getLastName()%> to <%=setName%></p>
            <%
            }
            }    
            ArrayList arrDemoSets = demoSets.getDemographicSets();
            pageContext.setAttribute("arrDemoSets", arrDemoSets);
            %>  
            <h3>Current Patient Set(s)</h3>
            <ul>
                <logic:iterate id="set" name="curSets">
                    <li><c:out value="${set}"/></li>
                </logic:iterate>
            </ul>
            
            <h3>Add to Patient Set:</h3>
            <ul>
                <logic:iterate id="set" name="arrDemoSets">
                    <li><a href="demographicCohort.jsp?demographic_no=<%=demoNo%>&setName=<c:out value="${set}"/>"><c:out value="${set}"/></a></li>
                </logic:iterate>
            </ul>
            <br>
            <form method="get" action="demographicCohort.jsp">
                <input type="hidden" name="demographic_no" value="<%=demoNo%>">
                <h3>New Patient Set:</h3>
                <input type="text" name="setName">&nbsp;<input type="submit" value="Save">
            </form>
        </div>
    </body>
</html>
