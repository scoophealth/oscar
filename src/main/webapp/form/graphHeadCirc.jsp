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
<title>Head Circumference Graph</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
</head>

<% /*There will be a whole bunch of logic here to determine which graph
    I should display, based on sex, and either hc or height
    */
	String formClass = "Rourke";
	String formLink = "formrourke1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = ((FrmRourkeRecord) rec).getGraph(demoNo, formId);

    String red = "graphics/redMark.gif";
    String blue = "graphics/blueMark.gif";
	String chart = ((FrmRourkeRecord) rec).isFemale(demoNo) == true ? "graphics/girlHeadCirc36m.jpg" : "graphics/boyHeadCirc36m.jpg" ;
%>

<%! double age(String dob, String today)
    {
        double age = -1;

        try
        {
            Date tToday = oscar.util.UtilDateUtilities.StringToDate(today, "yyyy/MM/dd");
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

<script language="JavaScript" type="text/javascript">
<!--



    var ageZeroLine = 0.1665;
    var ageShift = 16.42;
    var hcThirtyLine = 0.5475;
    var hcShift = 22.35;
    var lengthFiftyLine = 0.219;
    var lengthShift = 9.855;
    var wOneLine = 0.841;
    var weightShift = 26.85;

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
        length requires the childs weight in kg
    */
    function fWeight(length,weight,name) {
        var w = document.chart.width;
        var h = document.chart.height;
        var xShift = lengthFiftyLine*w;
        var yShift = wOneLine*h;
        x = eval(xShift+((length-50)*lengthShift));
        y = eval(yShift+((1-weight)*weightShift));

        var s = '<div ID="weight" STYLE="position:absolute; visibility:visible; left:'+x+'px; top:'+y+'px;"> '
              + name
              + '</div>';
        addControl(s);
    }

    /* month requires the child's age in months,
        hc requires the child's head circumference in cm
    */
    function fHeadCirc(month,hc,name) {
        var w = document.chart.width;
        var h = document.chart.height;
        var xShift = ageZeroLine*w;
        var yShift = hcThirtyLine*h;
        x = eval(xShift+(month*ageShift));
        y = eval(yShift+((30-hc)*hcShift));

        var s = '<div ID="hc" STYLE="position:absolute; visibility:visible; left:'+x+'px; top:'+y+'px;"> '
              + name
              + '</div>';
        addControl(s);
    }

    function size() {
        alert("size: "+document.chart.width);
    }
    function graphHC(age,hc) {
        if((hc!="")  && (age!="") && isInteger(hc) && isInteger(age))
        {
            if((hc>=28) && (hc<54) && (age>=0) && (age<38))
            {
                fHeadCirc(age,hc,"<img src='<%=red%>'>");
            }
        }
    }
    function graphWeight(length,w) {
        if((w!="") && (length!="") && isInteger(w) && isInteger(length))
        {
            if((w>0) && (w<23) && (length>44) && (length<105))
            {
                fWeight(length,w,"<img src='<%=blue%>'>");
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

        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("birthDate"))%>, "<%=props.getProperty("headCirc", "")%>");
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date1w"))%>, "<%=props.getProperty("hc1w", "")%>");
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date2w"))%>,"<%=props.getProperty("hc2w", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date1m"))%>,"<%=props.getProperty("hc1m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date2m"))%>,"<%=props.getProperty("hc2m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date4m"))%>, "<%=props.getProperty("hc4m", "")%>");
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date6m"))%>,"<%=props.getProperty("hc6m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date9m"))%>,"<%=props.getProperty("hc9m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date12m"))%>,"<%=props.getProperty("hc12m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date18m"))%>,"<%=props.getProperty("hc18m", "")%>" );
        graphHC(<%=age(props.getProperty("birthDate"),props.getProperty("date2y"))%>,"<%=props.getProperty("hc2y", "")%>" );

        graphWeight("<%=props.getProperty("length", "")%>","<%=props.getProperty("birthWeight", "")%>");
        graphWeight("<%=props.getProperty("ht1w", "")%>", "<%=props.getProperty("wt1w", "")%>");
        graphWeight("<%=props.getProperty("ht2w", "")%>","<%=props.getProperty("wt2w", "")%>" );
        graphWeight("<%=props.getProperty("ht1m", "")%>","<%=props.getProperty("wt1m", "")%>" );
        graphWeight("<%=props.getProperty("ht2m", "")%>","<%=props.getProperty("wt2m", "")%>" );
        graphWeight("<%=props.getProperty("ht4m", "")%>", "<%=props.getProperty("wt4m", "")%>");
        graphWeight("<%=props.getProperty("ht6m", "")%>","<%=props.getProperty("wt6m", "")%>" );
        graphWeight("<%=props.getProperty("ht9m", "")%>","<%=props.getProperty("wt9m", "")%>" );
        graphWeight("<%=props.getProperty("ht12m", "")%>","<%=props.getProperty("wt12m", "")%>" );
        graphWeight("<%=props.getProperty("ht18m", "")%>","<%=props.getProperty("wt18m", "")%>" );
        graphWeight("<%=props.getProperty("ht2y", "")%>","<%=props.getProperty("wt2y", "")%>" );
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
