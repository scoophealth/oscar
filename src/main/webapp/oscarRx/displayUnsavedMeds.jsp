<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License.
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version. *
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
*
* <OSCAR TEAM>
*
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*,org.oscarehr.common.model.Drug,org.oscarehr.phr.model.PHRMedication,oscar.oscarRx.data.RxPrescriptionData"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <%
    oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
    HashMap<Long,PHRMedication> phrMeds=bean.getPairPrevViewedPHRMed();
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
             console.log('selectedDrugs='+selectedDrugs);
    }
    function importPHRDrugs(){
        if(selectedDrugs.length>0){
            var url='./phrExchange.do';
            var data='method=saveSelectedPHRMeds&selectedDrugs='+selectedDrugs;
            new Ajax.Request(url,{method: 'get',parameters:data, onSuccess:function(transport){
                    console.log("success");
                   /* var json=transport.responseText.evalJSON();
                    var s=json.success;
                    if(s!=null && s==true){
                        $('phr_drug_div').innerHTML='<a>done importing</a>';
                    }*/

            }})
        }
    }
</script>
<body >
        <table id="phr_drug_div">
            <tr>
                <td><a>1111select</a></td>
                <td><a>Drug Name</a></td>
                <td><a>Rx Date</a></td>
                <td><a>Instruction</a></td>
                <td><a>Provider Name</a></td>
            </tr>

        <%while(itr.hasNext()){
            Long k=(Long)itr.next();
            PHRMedication p=phrMeds.get(k);
            Drug d=p.getDrug();
            if(d!=null){
    %>
    <tr>
        <td><input type="checkbox" id="drug_<%=i%>" name="phrDrugs" value="<%=k%>" onclick="selectDrug(this);"></td>
        <td><a><%=d.getDrugName()%></a></td>
        <td><a><%=oscar.util.UtilDateUtilities.DateToString(d.getRxDate())%></a></td>
        <td><a><%=d.daysToExpire()%></a></td>
        <td><%if(d.isLongTerm()){%><a>L</a><%} else{%><a>&nbsp;</a><%}%></td>
        <td><a><%=RxPrescriptionData.getFullOutLine(d.getSpecial()).replaceAll(";", " ")%></a></td>
        <td><a><%=d.getOutsideProviderName()%></a></td>
    </tr>
        <%i++;}
        }%>
        <input type="button" value="import" onclick="importPHRDrugs();">
        </table>
    </body>
</html>
