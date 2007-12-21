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
 <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 <%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
 <%@ page import="oscar.oscarEncounter.data.*, oscar.oscarProvider.data.*" %>
<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
        response.sendRedirect("error.jsp");
        return;
    }
  
    String demoNo = bean.demographicNo;
    EctPatientData.Patient pd = new EctPatientData().getPatient(demoNo);
    String famDocName, famDocSurname, famDocColour, inverseUserColour, userColour;
    String user = (String) session.getAttribute("user");
    System.out.println("USER " + user);
    ProviderColourUpdater colourUpdater = new ProviderColourUpdater(user);
    userColour = colourUpdater.getColour();
    //we calculate inverse of provider colour for text        
    int base = 16;
    if( userColour.length() == 0 )
        userColour = "#CCCCFF";   //default blue if no preference set

    int num = Integer.parseInt(userColour.substring(1), base);      //strip leading # sign and convert        
    int inv = ~num;                                                 //get inverse
    inverseUserColour = Integer.toHexString(inv).substring(2);    //strip 2 leading digits as html colour codes are 24bits

    if(bean.familyDoctorNo.equals("")) {
        famDocName = "";
        famDocSurname = "";      
        famDocColour = "";        
    }
    else {
        EctProviderData.Provider prov = new EctProviderData().getProvider(bean.familyDoctorNo); 
        famDocName = prov.getFirstName();
        famDocSurname = prov.getSurname();
        colourUpdater = new ProviderColourUpdater(bean.familyDoctorNo);
        famDocColour = colourUpdater.getColour();
        if( famDocColour.length() == 0 )
            famDocColour = "#CCCCFF";        
    }

    String patientName = pd.getFirstName()+" "+pd.getSurname();
    String patientAge = pd.getAge();
    String patientSex = pd.getSex();
    java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    %>
    <script type="text/javascript" language=javascript>
        var openWindows = new Object();
        function popupPage(vheight,vwidth,name,varpage) { //open a new popup window
          var page = "" + varpage;
          windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
                //var popup =window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
                openWindows[name] = window.open(page, name, windowprops);

                if (openWindows[name] != null) {        
                    if (openWindows[name].opener == null) {
                        openWindows[name].opener = self;
                        alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
                    }
                    openWindows[name].focus();
                }            
        } 
        function urlencode(str) {
            var ns = (navigator.appName=="Netscape") ? 1 : 0;
            if (ns) { return escape(str); }
            var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
            var msi = 0;
            var i,c,rs,ts ;
            while (msi < ms.length) {
                c = ms.charAt(msi);
                rs = ms.substring(++msi, msi +2);
                msi += 2;
                i = 0;
                while (true)	{
                    i = str.indexOf(c, i);
                    if (i == -1) break;
                    ts = str.substring(0, i);
                    str = ts + "%" + rs + str.substring(++i, str.length);
                }
            }
            return str;
    }
    </script>
<div style="font-size: 12px; width:100%; color:#<%=inverseUserColour%>; background-color:<%=userColour%>">
    <span style="border-bottom: medium solid <%=famDocColour%>"><bean:message key="oscarEncounter.Index.msgMRP"/>&nbsp;&nbsp;
    <%=famDocName%>&nbsp;<%=famDocSurname%>&nbsp;&nbsp;</span>
    <form style="display:inline;" action="" name="ksearch" >
    <span class="Header" style="color:#<%=inverseUserColour%>; background-color:<%=userColour%>">
        <%
            String winName = "Master" + bean.demographicNo;
            String url;
            if (vLocale.getCountry().equals("BR"))
                url = "../demographic/demographiccontrol.jsp?demographic_no=" + bean.demographicNo + "&amp;displaymode=edit&amp;dboperation=search_detail_ptbr";                            
            else
                url = "../demographic/demographiccontrol.jsp?demographic_no=" + bean.demographicNo + "&amp;displaymode=edit&amp;dboperation=search_detail";
        %>
        <a href="#" onClick="popupPage(700,1000,'<%=winName%>','<%=url%>'); return false;" title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><%=bean.patientLastName %>, <%=bean.patientFirstName%></a>&nbsp;<%=bean.patientSex%> <%=bean.patientAge%>
        <span style="margin-left:20px;"><i>Next Appt: <oscar:nextAppt demographicNo="<%=bean.demographicNo%>"/></i></span>

        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       
        <select  name="channel" >
             <option value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="><bean:message key="oscarEncounter.Index.oscarSearch"/></option>
             <option value="http://www.google.com/search?q="><bean:message key="global.google"/></option>
             <option value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&amp;CDM=Search&amp;DB=PubMed&amp;term="><bean:message key="global.pubmed"/></option>
             <option value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&amp;FUNCTION=search&amp;SERVER2=server2&amp;SERVER1=server1&amp;PARAMETER="><bean:message key="global.medlineplus"/></option>
             <option value="http://www.bnf.org/bnf/bnf/current/noframes/search.htm?n=50&amp;searchButton=Search&amp;q="><bean:message key="global.BNF"/></option>
        </select>
        <input type="text" name="keyword"  value=""  onkeypress="return grabEnter('searchButton',event)"/>
        <input type="button" id="searchButton" name="button"  value="Search" onClick="popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>',forms['ksearch'].channel.options[forms['ksearch'].channel.selectedIndex].value+urlencode(forms['ksearch'].keyword.value) ); return false;">       
    </span>
  </form>
</div>


