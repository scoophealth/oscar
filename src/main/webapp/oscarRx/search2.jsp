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

<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*,oscar.oscarRx.util.*" %>
<ul>
<%
String searchString = request.getParameter("searchString");

RxDrugData drugData = new RxDrugData();
RxDrugRef rxref = new RxDrugRef();

RxDrugData.DrugSearch drugSearch = null;

if (searchString != null){

    Vector<Hashtable> vec = rxref.list_drug_element2(searchString);
    for(Hashtable drug : vec){
        String cat = ""+drug.get("category");
        if (cat.equals("18")){
        %>
        <li id="g_<%=drug.get("id")%>"><%=drug.get("name")%></li>
    <%}

    }

    
    ArrayList<RxDrugData.MinDrug> genList = new ArrayList();//  drugSearch.getGen();
    if(drugSearch != null){
        for(RxDrugData.MinDrug drug: genList){
    %>
        <li id="g_<%=drug.pKey%>"><%=drug.name%></li>
    <%
        }
    }

    ArrayList genWBrand = new ArrayList();

    ArrayList<RxDrugData.MinDrug> brandList =  new ArrayList();//drugSearch.getBrand();
    if(drugSearch != null){
        for(RxDrugData.MinDrug drug: brandList){
            //String genName = drugData.getGenericName(drug.pKey);
            //if (!genWBrand.contains(genName)){
             //   genWBrand.add(genName);
                %>
                <li id="b_<%=drug.pKey%>" ><%=drug.name%>  </li>
                <%
         //}
        }
    }
}
%>
</ul>
