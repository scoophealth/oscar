<%-- 
    Document   : displayMedHistory
    Created on : Mar 31, 2010, 3:02:41 PM
    Author     : jackson
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.*" %>
<%@page import="oscar.oscarRx.util.*" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/dragiframe.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body onload="addHandle(document.getElementsByTagName('body').item(0), window);">
    <%
try{
    System.out.println("in displaymedhistory.jsp");
    oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
    String randomId=(String)request.getParameter("randomId");
        if(randomId!=null){
                RxPrescriptionData.Prescription rx=bean.getStashItem2(Integer.parseInt(randomId));
                String drugName=rx.getBrandName();
                if(drugName==null || drugName.equalsIgnoreCase("null") || drugName.trim().length()==0)
                    drugName=rx.getCustomName();
                List<HashMap<String,String>> listMedHistory=(List<HashMap<String,String>>)bean.getListMedHistory();
                System.out.println("drugName="+drugName+"--randomId="+randomId+"--listMedHistory="+listMedHistory);

                %>

<a style="position:fixed;top:0px;right:4px;color:red;font-size:12pt" onmouseover="this.style.cursor='pointer';" onclick="parent.mb.hide();">X</a>
</br>
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
                <td align="left" style=""><a id="mhInst_<%=i%>" href="javascript:void(0);" onclick="parent.addInstruction(this.innerHTML,'<%=randomId%>');parent.addSpecialInstruction(document.getElementById('mhSpecInst_<%=i%>').innerHTML,'<%=randomId%>');"><%=ins%></a></td>
                <td align="left" style=""><a id="mhSpecInst_<%=i%>" href="javascript:void(0);" onclick="parent.addSpecialInstruction(this.innerHTML,'<%=randomId%>');"><%=specIns%></a></td>
            </tr>
        <%}else if(instructionExist && !specialInstructionExist){%>
            <tr>
                <td align="left" style=""><a id="mhInst_<%=i%>" href="javascript:void(0);" onclick="parent.addInstruction(this.innerHTML,'<%=randomId%>');" ><%=ins%></a></td>
                <td>&nbsp;</td>
            </tr>
            <%}else if(!instructionExist && specialInstructionExist){%>
                <tr>
                    <td>&nbsp;</td>
                    <td align="left" style=""><a id="mhSpecInst_<%=i%>" href="javascript:void(0);" onclick="parent.addSpecialInstruction(this.innerHTML,'<%=randomId%>');"><%=specIns%></a></td>
                </tr>
            <%}%>
                <%
            i++;
            }
    }

}catch(Exception e){e.printStackTrace();}
%>
    </table>
</body>
</html>