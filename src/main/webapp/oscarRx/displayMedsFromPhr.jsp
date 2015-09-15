
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>


<%-- 
    Document   : displayMedsFromPhr
    Created on : Jan 14, 2011, 10:48:13 AM
    Author     : jackson
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*,org.oscarehr.common.model.Drug,org.oscarehr.phr.model.PHRMedication,oscar.oscarRx.data.RxPrescriptionData"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <%
    Boolean unimportedMed=(Boolean)request.getAttribute("unimportedMed");
    oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
    HashMap<Long,PHRMedication> phrMeds=new HashMap<Long,PHRMedication>();
    if(unimportedMed!=null && unimportedMed){
        phrMeds=bean.getPairPrevViewedPHRMed();
    }else{
        phrMeds=bean.getPairPHRMed();
    }
    int i=0;
    Set ks=phrMeds.keySet();
    Iterator itr=ks.iterator();
%>

        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
<script type="text/javascript" language="javascript">
    var selectedDrugs='';
    function selectDrug(e){
             if(this){
                 if(selectedDrugs.match(e.value)==null){
                     if(e.checked) selectedDrugs+=','+e.value;
                 }else{
                     if(!e.checked) {
                         selectedDrugs=selectedDrugs.replace(','+e.value,'');                         
                     }
                 }
             }
             //console.log('selectedDrugs='+selectedDrugs);
             $('successImport').innerHTML="";
    }
    function disableCB(){
                        var result=selectedDrugs.split(",");
                        for(var j=0;j<result.length;j++){
                            if(result[j].length>1){
                                if(document.getElementById("drug_"+result[j]))
                                    document.getElementById("drug_"+result[j]).disabled=true;
    }
                        }
                        resetSelectedDrugs();
    }
    function resetSelectedDrugs(){
        selectedDrugs='';
    }
    /*
    function importPHRDrugs(){
        if(selectedDrugs.length>0){
            var url='./phrExchange.do';
            var data='method=saveSelectedPHRMeds&selectedDrugs='+selectedDrugs+'&unimportedMed=<%=unimportedMed%>';
            new Ajax.Request(url,{method: 'get',parameters:data, onSuccess:function(transport){
                    //disable the checkbox
                    var json=transport.responseText.evalJSON();
                    var s=json.success;
                    if(s!=null && s==true){
                        disableCB();
                        //write a success message beside the import button
                        $('successImport').innerHTML="success";
                    }
            }})
        }
    }
    */
</script>
        <body ><%if(ks.isEmpty()){%> <a>No drugs to view</a> <%}else{%>
        <table id="phr_drug_div">
            <tr><td><input type="button" value="import" ></td><td id="successImport" style="color: blue" ></td></tr>
            <tr>
                <td align="center" ><a>Select</a></td>
                <td align="center" ><a>Drug Name</a></td>
                <td align="center" ><a>Rx Date</a></td>
                <td align="center" ><a>Days To Expire</a></td>
                <td align="center" ><a>Longterm</a></td>
                <td align="center" ><a>Instruction</a></td>
                <td align="center" ><a>Provider Name</a></td>
            </tr>

        <%while(itr.hasNext()){
            Long k=(Long)itr.next();
            PHRMedication p=phrMeds.get(k);
            Drug d=p.getDrug();
            if(d!=null){
    %>
    <tr>
        <td align="center" ><input type="checkbox" id="drug_<%=k%>" name="phrDrugs" value="<%=k%>" onclick="selectDrug(this);"></td>
        <td align="center" ><a><%=d.getDrugName()%></a></td>
        <td align="center" ><a><%=oscar.util.UtilDateUtilities.DateToString(d.getRxDate())%></a></td>
        <td align="center" ><a><%=d.daysToExpire()%></a></td>
        <td align="center" ><%if(d.isLongTerm()){%><a>L</a><%} else{%><a>&nbsp;</a><%}%></td>
        <td align="left" ><a><%=RxPrescriptionData.getFullOutLine(d.getSpecial()).replaceAll(";", " ")%></a></td>
        <td align="left" ><a><%=d.getOutsideProviderName()%></a></td>
    </tr>
        <%i++;}
        }%>
        </table><%}%>
    </body>
</html>
