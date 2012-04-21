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

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.util.OntarioMD,java.util.Hashtable,org.apache.commons.collections.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Ontario MD Disease</title>
        <script type="text/javascript">

            function popupDrugOfChoice(vheight,vwidth,varpage) { //open a new popup window
                var page = varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
                var popup=window.open(varpage, "oscarDoc", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                    popup.focus();
                }
            }

            function goDOC(){
                if (document.RxSearchDrugForm.searchString.value.length == 0){
                    popupDrugOfChoice(720,700,'http://doc.oscartools.org/')
                }else{
                    //var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
                    var docURL = "http://doc.oscartools.org/search?SearchableText="+document.RxSearchDrugForm.searchString.value;          
                    popupDrugOfChoice(720,700,docURL);                               
                }
            }    
                
                
            function goOMD(params){
                var docURL = "../common/OntarioMDRedirect.jsp?keyword="+params;          
                popupDrugOfChoice(743,817,docURL);                               
            }           
        </script>       
    </head>
    <body>
        <ul>
            <%OrderedMap map = new OntarioMD().getDiseaseList();
            OrderedMapIterator iter = map.orderedMapIterator();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                String val = (String) iter.getValue();
            %>   
             <li>
                 <a href="javascript:void(0)" onclick="goOMD('<%=key%>')"><%=val%></a>
             </li>
            
            <%}%>
        </ul>
    </body>
</html>
