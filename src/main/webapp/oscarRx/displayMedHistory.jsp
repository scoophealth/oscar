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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.*" %>
<%@page import="oscar.oscarRx.util.*" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
    <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/dragiframe.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body onload="addHandle(document.getElementsByTagName('body').item(0), window);">
    <%
try{
    oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
    String randomId=request.getParameter("randomId");
        if(randomId!=null){
                RxPrescriptionData.Prescription rx=bean.getStashItem2(Integer.parseInt(randomId));
                String drugName=rx.getBrandName();
                if(drugName==null || drugName.equalsIgnoreCase("null") || drugName.trim().length()==0)
                    drugName=rx.getCustomName();
                List<HashMap<String,String>> listMedHistory=(List<HashMap<String,String>>)bean.getListMedHistory();
                %>

<a onmouseover="this.style.cursor='pointer';" onMouseDown="parent.mb.hide();" ><img src="<c:out value="${ctx}/images/close.png"/>"  border="0" TITLE="Close" style="position: absolute; top: 0.5em; right: 0.5em; "></a> 
<br /><br />
<table class="mhTable" >
        <tr>
            <th colspan="3" align="center" style="font-style:normal;font-weight:bold;margin:0;font-family:sans-serif;font-size:80%"><%=drugName%> Rx Examples</th>
        </tr>
        <tr>
            <td align="center" style="">Instruction</td>
            <td align="center" style="">Special Instruction</td>
        </tr>
                <%
                int i=0;
            for(HashMap<String,String> hm:listMedHistory){
                String ins=hm.get("instruction");
                String specIns=hm.get("special_instruction");
                Boolean instructionExist=false;
                Boolean specialInstructionExist=false;
                if(ins!=null && !ins.equalsIgnoreCase("null") && ins.trim().length()>0 ){
                    instructionExist=true;
                }
                if(specIns!=null && !specIns.equalsIgnoreCase("null") && specIns.trim().length()>0){
                    specialInstructionExist=true;
                }
            %>
            <%if(instructionExist && specialInstructionExist){%>
            <tr>
                <td align="left" style=""><a id="mhInst_<%=i%>" href="javascript:void(0);" onclick="parent.addInstruction(this.innerHTML,'<%=randomId%>');parent.addSpecialInstruction(document.getElementById('mhSpecInst_<%=i%>').innerHTML,'<%=randomId%>');parent.mb.hide();"><%=ins%></a></td>
                <td align="left" style=""><a id="mhSpecInst_<%=i%>" href="javascript:void(0);" onclick="parent.addSpecialInstruction(this.innerHTML,'<%=randomId%>');parent.mb.hide();"><%=specIns%></a></td>
            </tr>
        <%}else if(instructionExist && !specialInstructionExist){%>
            <tr>
                <td align="left" style=""><a id="mhInst_<%=i%>" href="javascript:void(0);" onclick="parent.addInstruction(this.innerHTML,'<%=randomId%>');parent.mb.hide();" ><%=ins%></a></td>
                <td>&nbsp;</td>
            </tr>
            <%}else if(!instructionExist && specialInstructionExist){%>
                <tr>
                    <td>&nbsp;</td>
                    <td align="left" style=""><a id="mhSpecInst_<%=i%>" href="javascript:void(0);" onclick="parent.addSpecialInstruction(this.innerHTML,'<%=randomId%>');parent.mb.hide();"><%=specIns%></a></td>
                </tr>
            <%}%>
                <%
            i++;
            }
    }

}catch(Exception e){ MiscUtils.getLogger().error("Error", e);}
%>
    </table>
</body>
</html>
