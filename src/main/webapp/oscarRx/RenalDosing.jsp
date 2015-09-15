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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page %><%@page import="oscar.oscarDemographic.data.*,org.oscarehr.common.model.Demographic"%>
<%@page import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler,java.util.*,oscar.oscarRx.util.*" %>
<%@page import="oscar.oscarLab.ca.on.*,oscar.util.*,oscar.oscarLab.*" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
String atc = request.getParameter("atcCode");
String demographicNo = request.getParameter("demographicNo");

DosingRecomendation rd = RenalDosingFactory.getDosingInformation(atc);
if (rd == null){  // No data so don't continue
    return;
}

DemographicData demoData = new DemographicData();
Demographic demographic = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

int age = demographic.getAgeInYears();
boolean female = DemographicData.isFemale(demographic);
double weight = -1;
Hashtable measurementHash = EctMeasurementsDataBeanHandler.getLast(demographicNo, "WT");

String wt= null;
Date  wtDate = null;
if (measurementHash != null && measurementHash.get("value") != null){
    weight = Double.parseDouble((String) measurementHash.get("value"));
    wtDate = (Date) measurementHash.get("dateObserved_date");
}

double measurementsCr = -1;
Date measurementsCrDate = null;

measurementHash = EctMeasurementsDataBeanHandler.getLast(demographicNo, "sCr");
if (measurementHash != null && measurementHash.get("value") != null){
    measurementsCr = Double.parseDouble((String) measurementHash.get("value"));
    measurementsCrDate = (Date) measurementHash.get("dateObserved_date");
}


double sCr = -1;
Date sCrDate = null;

List labs = CommonLabTestValues.findValuesForTest("CML",Integer.valueOf(demographicNo), "CREATININE");
if(labs != null && labs.size() >0 ){
//SortHashtable sorter = ;
    Collections.sort(labs,new SortHashtable());
    Hashtable hash =  (Hashtable) labs.get(0);
    String sCrStr = (String) hash.get("result");
    sCrDate = (Date) hash.get("collDateDate");
    try{
        sCr = Double.parseDouble(sCrStr);
    }catch(Exception e){
   	 MiscUtils.getLogger().debug("No idea if this is ok or not, some one else left the block blank", e);
    }
}



//Use value with the latest Date
/////////
if (sCrDate == null && measurementsCrDate != null){  // If there is no lab value, go with measurement value
    sCr = measurementsCr;
    sCrDate = measurementsCrDate;
}else if (sCrDate != null && measurementsCrDate != null && sCrDate.before(measurementsCrDate)){  //if both measurement and lab are present go with later
    sCr = measurementsCr;
    sCrDate = measurementsCrDate;
}


//////



boolean ageb,weightb,sCrb,equate;
ageb = weightb = sCrb = equate =false;


if  ( age > 0){    ageb =true ;}
if  ( weight > 0){ weightb = true;}
if  ( sCr > 0){    sCrb = true;}


int Clcr = 0;
if (ageb & weightb & sCrb){
    equate = true;
    Clcr = RxUtil.getClcr(age,weight,sCr,female);
}

//int Clcr = RxUtil.getClcr(age,weight,sCr,female);
ArrayList list = rd.getDose();


//Todo: create Struts Action for above
%>
<!--
  CIPRO : 	J01MA02 S01AX13 S03AA07

RENAL DOSING INFORMATION  ATC: <%=atc%>  Demographic: <%=demographicNo%>
  <br/>
  Clcr = {(140 - age ) X weight[kg] )} / (sCr [umol/L] X 0.8)           if female X 0.85
  <br/>
Clcr = {(140 - <%=age%> ) X <%=weight%>[kg] )} / (sCr [umol/L] X 0.8)   <% if(female){%> X 0.85 <%}%>
  -->
<style type="text/css">
    table.sofT{
    float:left;
    text-align: center;
    font-family: Verdana;
    font-weight: normal;
    font-size: 11px;
    color: #404040;
    width: 400px;
    background-color: #fafafa;
    border: 1px #cfcfcf solid;
    border-collapse: collapse;
    border-spacing: 0px; }

    tr.selected{
    /*background-color: #BEC8D1;*/
    background-color: yellow;

    font-weight: bold;
    font-size: 11px;
    color: #404040; }
    tr.heading{
    border-bottom: 1px #cfcfcf solid;
    }

    table.equation {
    font-family: Verdana;
    font-weight: normal;
    font-size: 11px;
    }
</style>
<div style="float:left; margin-left:10px; margin-right: 10px;">
    <h3>Dosing Information <small><a href="javascript: function myFunction() {return false; }" onclick="getRenalDosingInformation();">Refresh</a></small></h3>
    <table class="sofT">
        <tr class="heading">
            <th>Clcr</th>
            <th>Recommendation</th>
        </tr>
        <%for (int i = 0; i < list.size(); i++){
        Hashtable h = (Hashtable) list.get(i);
        String sel = "";
        if ( rd.valueInRangeOfDose(Clcr , h) & ageb & weightb & sCrb){
        sel = "class=\"selected\" ";
        }

        %>
        <tr <%=sel%> >

            <td><%=h.get("clcrrange")%></td>
            <td><%=h.get("recommendation")%></td>
        </tr>
        <%}%>
    </table>


    <div style="width:410px; float:left;">

        <!--
        <div style="float:left; border: 1px yellow solid;" >Clcr <%=Clcr%> =</div> <div style="float:left;" > (140 - <%=age%>[age] ) X <%=weight%>[kg] ) <hr/> (<%=sCr%> sCr [umol/L] X 0.8)   <% if(female){%> X 0.85 <%}%> </div>
      -->
        <table class="equation">
            <tr>
                <th rowspan=2 valign="middle">Clcr <%=setNA(equate,Clcr)%> =</th>
                <td align="center">
                    (140 - <%=setNA(ageb,age)%>[age] ) X <%=setNA(weightb,weight)%>
                    <a href="javascript: function myFunction() {return false; }"  onclick="popup(500,1000,'/oscar/oscarEncounter/oscarMeasurements/SetupMeasurements.do?groupName=Renal Dosing&amp;demographic_no=<%=demographicNo%>','dddsfds'); return false;">
                    [kg <%=UtilDateUtilities.DateToString( wtDate , "yyyy-MMM-dd")%>]
                    </a> X 1.23

                </td>
                 <% if(female){%>
                   <td nowrap rowspan="2"> X 0.85 </td>
                 <%}%>
            </tr>
            <tr>
                <td align="center" style="border-top: 2px black solid;"><%=setNA(sCrb,sCr)%> sCr
                    <a href="javascript: function myFunction() {return false; }"  onclick="popup(500,1000,'/oscar/oscarEncounter/oscarMeasurements/SetupMeasurements.do?groupName=Renal Dosing&amp;demographic_no=<%=demographicNo%>','dddsfds'); return false;">
                        [umol/L <%=UtilDateUtilities.DateToString( sCrDate , "yyyy-MMM-dd")%>]
                    </a>
                </td>
            </tr>
        </table>
    </div>

    <div style="clear:left"><%=rd.getMoreinfo()%></div>
    <%if (request.getParameter("divId") != null){ %>
    <div style="float:right"><a href="javascript:void(0);"  onclick="$('<%=request.getParameter("divId")%>').toggle();">hide</a></div>
    <%}%>
</div>
<%--
  -Right now only works for CML
  -what to do if patient doesn't have a any creatine values?
  --%>
  <%!

  String setNA(boolean valb, int value){
       if (valb){
           return ""+value;
       }
       return "<span style=\"color:orange\">N/A</span>";
  }

  String setNA(boolean valb, double value){
       if (valb){
           return ""+value;
       }
       return "<span style=\"color:orange\">N/A</span>";
  }

  %>
