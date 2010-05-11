<%-- 
    Document   : LimitedUseCode
    Created on : Apr 15, 2010, 1:02:38 PM
    Author     : jackson
--%>

<%@page contentType="text/html"%><%@page pageEncoding="ISO-8859-1"%> 
<%@page import="oscar.oscarDemographic.data.*,oscar.oscarDemographic.data.DemographicData.Demographic"%>
<%@page import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler,java.util.*,oscar.oscarRx.util.*" %>
<%@page import="oscar.oscarLab.ca.on.*,oscar.util.*,oscar.oscarLab.*" %>

           <%
           String din=request.getParameter("din");
           String randomId=request.getParameter("randomId");
            ArrayList<LimitedUseCode> luList = LimitedUseLookup.getLUInfoForDin(din);

            if (luList != null){
            System.out.println("inside luList size is "+luList.size());
     %>
            <div style="float:left; margin-left:2px; margin-right: 2px;">
            <table style="border-width: 1px; border-spacing: 2px; border-style: outset; border-color: black;">
                        <tr>
                                <th colspan="2" align="left">Limited Use Codes</th>
                        </tr>

                        <%for (LimitedUseCode limitedUseCode : luList){%>
                        <tr>
                            <td valign="top">
                                <a onclick="javascript:addLuCode('instructions_<%=randomId%>','<%=limitedUseCode.getUseId()%>')" href="javascript: return void();"><%=limitedUseCode.getUseId()%></a>&nbsp;
                            </td>
                            <td><%=limitedUseCode.getTxt()%></td>
                        </tr>
                        <%}%>
            </table>
            </div>
            <%}%>
