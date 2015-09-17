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
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="oscar.form.*, oscar.form.data.*, oscar.util.*, java.util.Date"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Length & Weight Graph</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
</head>

<% 
	String formClass = "Rourke";
	String formLink = "formrourke1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = ((FrmRourkeRecord) rec).getGraph(demoNo, formId);

    String red = "graphics/redMark.gif";
    String blue = "graphics/blueMark.gif";
	String chart = ((FrmRourkeRecord) rec).isFemale(demoNo) == true ? "graphics/girlLength36m.jpg" : "graphics/boyLength36m.jpg" ;
%>


<%! double age(String dob, String today)
    {
        double age = -1;

        try
        {
            Date tToday = (oscar.util.UtilDateUtilities.StringToDate(today, "yyyy/MM/dd"));
            Date tDob = (oscar.util.UtilDateUtilities.StringToDate(dob, "yyyy/MM/dd"));

            age = (tToday.getTime() - tDob.getTime())/(1000*3600*24);
            age = age/30.4375; // the approximate number of days in a month
        }
        catch(Exception ex)
        {
        }
        return age;
    }
%>

<script language="JavaScript">
<!--
    var ageZeroLine = 0.1665;
    var lHundredLine = 0.163;
    var wTwoLine = 0.8815;
    var ageShift = 16.42;
    var lengthShift = 9.24;
    var weightShift = 46.2;
    var dateCol = 0.355;
    var lineOne = 0.773;
    var colShift = 70.0;
    var lineShift = 16.6;

    var ageBirth = "<%=age(props.getProperty("birthDate"),props.getProperty("birthDate"))%>";
    var age1w = "<%=age(props.getProperty("birthDate"),props.getProperty("date1w"))%>";
    var age2w = "<%=age(props.getProperty("birthDate"),props.getProperty("date2w"))%>";
    var age1m = "<%=age(props.getProperty("birthDate"),props.getProperty("date1m"))%>";
    var age2m = "<%=age(props.getProperty("birthDate"),props.getProperty("date2m"))%>";
    var age4m = "<%=age(props.getProperty("birthDate"),props.getProperty("date4m"))%>";
    var age6m = "<%=age(props.getProperty("birthDate"),props.getProperty("date6m"))%>";
    var age9m = "<%=age(props.getProperty("birthDate"),props.getProperty("date9m"))%>";
    var age12m = "<%=age(props.getProperty("birthDate"),props.getProperty("date12m"))%>";
    var age18m = "<%=age(props.getProperty("birthDate"),props.getProperty("date18m"))%>";
    var age2y = "<%=age(props.getProperty("birthDate"),props.getProperty("date2y"))%>";

    function addControl(controlString)
    {
        var anchor = document.anchors[0];
        anchor.innerHTML = anchor.innerHTML + controlString;
    }

    function fPercent(x, y, name) {
        var s = '<div ID="top" style="position:absolute; z-index:2; visibility:visible; left:'+x+'%; top:'+y+'%;">'
              + name
              + '</div>';
        addControl(s);
    }

    /* month requires the childs age in months,
        length requires the childs length in cm
    */
    function fLength(month,length,name) {
        var w = document.chart.width;
        var h = document.chart.height;
        var xShift = ageZeroLine*w;
        var yShift = lHundredLine*h;
        x = eval(xShift+(month*ageShift));
        y = eval(yShift+((100-length)*lengthShift));

        var s = '<div ID="length" style="position:absolute; z-index:2; visibility:visible; left:'+x+'px; top:'+y+'px;">'
              + name
              + '</div>';
        addControl(s);
    }

    /* month requires the childs age in months,
        length requires the childs weight in kg
    */
    function fWeight(month,weight,name) {
        var w = document.chart.width;
        var h = document.chart.height;
        var xShift = ageZeroLine*w;
        var yShift = wTwoLine*h;
        x = eval(xShift+(month*ageShift));
        y = eval(yShift+((2-weight)*weightShift));

        var s = '<div ID="weight" style="position:absolute; z-index:2; visibility:visible; left:'+x+'px; top:'+y+'px;">'
              + name
              + '</div>';
        addControl(s);
    }

    function fData(col, line, name) {
        var size = name.length;
        if(size == 0)
        {
            return;
        }
        else
        {
            size = size - 3;
            if(name.substring(0,size)!="")
            {
                var w = document.chart.width;
                var h = document.chart.height;
                var xShift = dateCol*w;
                var yShift = lineOne*h;
                x = eval(xShift+((col-1)*colShift));
                y = eval(yShift+((line-1)*lineShift));

                var s = '<div ID="data" class="data" style="position:absolute; z-index:2; visibility:visible; left:'+x+'px; top:'+y+'px;">'
                      + name
                      + '</div>';
                addControl(s);
            }
        }
    }

    function size() {
        alert("size: "+document.chart.width);
    }
    function graphLength(a,h) {
        if((h!="") && (a!="") && isInteger(h) && isInteger(a))
        {
            if((h>35) && (h<110) && (a>=0) && (a<38))
            {
                fLength(a,h,"<img src='<%=red%>'>");
            }
        }
    }
    function graphWeight(a,w) {
        if((w!="") && (a>=0) && isInteger(w) && isInteger(a))
        {
            if((w>0) && (w<=18) && (a>=0) && (a<38))
            {
                fWeight(a,w,"<img src='<%=blue%>'>");
            }
        }
    }

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if(c == ".") return true;
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }

    function graphChart() {
        fPercent(65,7.9,"<%=props.getProperty("pName", "")%>");

        graphLength(ageBirth,"<%=props.getProperty("length", "")%>");
        graphLength(age1w,"<%=props.getProperty("ht1w", "")%>");
        graphLength(age2w,"<%=props.getProperty("ht2w", "")%>" );
        graphLength(age1m,"<%=props.getProperty("ht1m", "")%>" );
        graphLength(age2m,"<%=props.getProperty("ht2m", "")%>" );
        graphLength(age4m, "<%=props.getProperty("ht4m", "")%>");
        graphLength(age6m,"<%=props.getProperty("ht6m", "")%>" );
        graphLength(age9m,"<%=props.getProperty("ht9m", "")%>" );
        graphLength(age12m,"<%=props.getProperty("ht12m", "")%>" );
        graphLength(age18m,"<%=props.getProperty("ht18m", "")%>" );
        graphLength(age2y,"<%=props.getProperty("ht2y", "")%>" );

        graphWeight(ageBirth,"<%=props.getProperty("birthWeight", "")%>");
        graphWeight(age1w, "<%=props.getProperty("wt1w", "")%>");
        graphWeight(age2w,"<%=props.getProperty("wt2w", "")%>" );
        graphWeight(age1m,"<%=props.getProperty("wt1m", "")%>" );
        graphWeight(age2m,"<%=props.getProperty("wt2m", "")%>" );
        graphWeight(age4m, "<%=props.getProperty("wt4m", "")%>");
        graphWeight(age6m,"<%=props.getProperty("wt6m", "")%>" );
        graphWeight(age9m,"<%=props.getProperty("wt9m", "")%>" );
        graphWeight(age12m,"<%=props.getProperty("wt12m", "")%>" );
        graphWeight(age18m,"<%=props.getProperty("wt18m", "")%>" );
        graphWeight(age2y,"<%=props.getProperty("wt2y", "")%>" );

//        fData(1, 1, "date1");
        var i = 1;
        fData(1,i,"<%=props.getProperty("birthDate", "")%>");
        fData(3,i,"<%=props.getProperty("birthWeight", "")%> kg");
        fData(4,i,"<%=props.getProperty("length", "")%> cm");
        fData(5,i,"<%=props.getProperty("headCirc", "")%> cm");

        var d = "<%=props.getProperty("date1w","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date1w", "")%>");
            fData(2,i,age1w.substring(0,4)+' mo');
            fData(3,i,"<%=props.getProperty("wt1w", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht1w", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc1w", "")%> cm");
        }
        d = "<%=props.getProperty("date2w","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date2w", "")%>");
            fData(2,i,age2w.substring(0,4)+' mo');
            fData(3,i,"<%=props.getProperty("wt2w", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht2w", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc2w", "")%> cm");
        }
        d = "<%=props.getProperty("date1m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date1m", "")%>");
            fData(2,i,age1m.substring(0,3)+' mo');
            fData(3,i,"<%=props.getProperty("wt1m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht1m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc1m", "")%> cm");
        }
        d = "<%=props.getProperty("date2m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date2m", "")%>");
            fData(2,i,age2m.substring(0,3)+' mo');
            fData(3,i,"<%=props.getProperty("wt2m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht2m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc2m", "")%> cm");
        }
        d = "<%=props.getProperty("date4m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date4m", "")%>");
            fData(2,i,age4m.substring(0,3)+' mo');
            fData(3,i,"<%=props.getProperty("wt4m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht4m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc4m", "")%> cm");
        }
        d = "<%=props.getProperty("date6m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date6m", "")%>");
            fData(2,i,age6m.substring(0,3)+' mo');
            fData(3,i,"<%=props.getProperty("wt6m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht6m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc6m", "")%> cm");
        }
        d = "<%=props.getProperty("date9m","")%>";
        if(d != "")
        {
            i++;
            var a = "<%=age(props.getProperty("birthDate"),props.getProperty("date9m"))%>";
            fData(1,i,"<%=props.getProperty("date9m", "")%>");
            fData(2,i,age9m.substring(0,3)+' mo');
            fData(3,i,"<%=props.getProperty("wt9m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht9m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc9m", "")%> cm");
        }
        d = "<%=props.getProperty("date12m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date12m", "")%>");
            fData(2,i,age12m.substring(0,4)+' mo');
            fData(3,i,"<%=props.getProperty("wt12m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht12m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc12m", "")%> cm");
        }
        d = "<%=props.getProperty("date18m","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date18m", "")%>");
            fData(2,i,age18m.substring(0,4)+' mo');
            fData(3,i,"<%=props.getProperty("wt18m", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht18m", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc18m", "")%> cm");
        }
        d = "<%=props.getProperty("date2y","")%>";
        if(d != "")
        {
            i++;
            fData(1,i,"<%=props.getProperty("date2y", "")%>");
            fData(2,i,age2y.substring(0,4)+' mo');
            fData(3,i,"<%=props.getProperty("wt2y", "")%> kg");
            fData(4,i,"<%=props.getProperty("ht2y", "")%> cm");
            fData(5,i,"<%=props.getProperty("hc2y", "")%> cm");
        }
    }

//-->
</SCRIPT>

<body class="graph">
<img name="chart" src="<%=chart%>" onLoad="javascript:graphChart();"
	border="0"
	style="position: absolute; width: 943px; z-index: 0; top: 0; left: 0;" />
<a name="ctls" id="ctls"></a>
<span class="hidePrint" class="Header"> <input type="button"
	value="print" onclick="javascript:window.print();"
	style="position: relative; width: 100px;" /> <input type="button"
	value="exit" onclick="javascript: window.close();"
	style="position: relative; width: 100px;" /> </span>
</body>
</html:html>
