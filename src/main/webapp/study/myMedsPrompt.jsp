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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MyMeds Prompt</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<script type="text/javascript">

function submitAnswer(frm) {
	
	var func = '<%=request.getParameter("func")%>'
	
	if( func == "Print") {
		window.opener.updateSaveAllDrugsPrintContinue();
	}
	else {
		window.opener.updateSaveAllDrugsContinue();
	}
	
	frm.submit();	
}

function confirm() {
	var windowprops = "height=350,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";	
	window.open("myMedsConfirm.html","myMeds2",windowprops);
}
</script>
</head>
<body>
<form method="post" action="ManageStudy.do" onsubmit="submitAnswer(this);">
<input type="hidden" name="studyId" value="<%=request.getParameter("studyId")%>"/>
<input type="hidden" name="providerNo" value="<%=request.getParameter("providerNo")%>"/>
<input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>"/>
<input type="hidden" name="method" value="saveStudyData"/>
<input type="hidden" name="key" value="answer"/>
<p style="text-align:center; border border: 1pt 1pt 1pt 1pt; border-style:solid;">
<b>THIS PATIENT MEETS THE ELIGIBILITY CRITERIA TO PARTICIPATE IN MyMEDS.</b><br/>
Records indicate he/she (1) is &gt;= 65 years of age and (2) was just prescribed a new antihypertensive medication.<br/> 
Please confirm this patient meets the eligibility criteria: 
</p>
<input type="submit" name="content" value="Eligible - ask now" onclick="confirm()"/>&nbsp;
<input type="submit" name="content" value="Eligible - ask later"/>&nbsp;
<input type="submit" name="content" value="Eligible - Don't ask again"/>&nbsp;
<input type="submit" name="content" value="Not Eligible"/>
</form>
</body>
</html>