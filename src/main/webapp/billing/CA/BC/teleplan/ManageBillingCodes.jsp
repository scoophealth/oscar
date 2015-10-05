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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
        
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.*,oscar.oscarDemographic.data.*,java.util.*,oscar.oscarBilling.ca.bc.Teleplan.*,java.math.BigDecimal,oscar.oscarBilling.ca.bc.data.*,org.oscarehr.common.model.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ page import="org.oscarehr.common.dao.BillingServiceDao,org.oscarehr.util.SpringUtils,org.oscarehr.common.model.*" %>
<%BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao"); %>

<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">

<head>
<title>
Manage Billing Codes
</title><!--I18n-->
<html:base/>
<link rel="stylesheet" type="text/css" href="../../../../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../../../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../../../../share/javascript/prototype.js"></script>

<style type="text/css">
  div.ImmSet { background-color: #ffffff;clear:left;margin-top:10px;}
  div.ImmSet h2 {  }
  div.ImmSet h2 span { font-size:smaller; }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}  
  
  /*h3{font-size: 100%;margin:0 0 10px;padding: 2px 0;color: #497B7B;text-align: center}*/
  
  div.onPrint { display: none; }
</style>

<link rel="stylesheet" type="text/css" href="../../../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="../../../../share/css/niftyPrint.css" media="print" />
<link rel="stylesheet" type="text/css" href="preventPrint.css" media="print" />
<script type="text/javascript" src="../../../../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");

Rounded("div.leftBox","top","transparent","#CCCCFF","small border #ccccff");
Rounded("div.leftBox","bottom","transparent","#EEEEFF","small border #ccccff");

}

function display(elements) {

    for( var idx = 0; idx < elements.length; ++idx )
        elements[idx].style.display = 'block';
}

function EnablePrint(button) {
    if( button.value == "Enable Print" ) {
        button.value = "Print";        
        var checkboxes = document.getElementsByName("printHP");
        display(checkboxes);
        var spaces = document.getElementsByName("printSp");
        display(spaces);
    }
    else { 
        if( onPrint() )
            document.printFrm.submit();
    }
}

function onPrint() {    
    var checked = document.getElementsByName("printHP");    
    var thereIsData = false;
    
    for( var idx = 0; idx < checked.length; ++idx ) {
        if( checked[idx].checked ) {
            thereIsData = true;
            break;
        }
    }
        
    if( !thereIsData ) {   
        alert("You should check at least one prevention by selecting a checkbox next to a prevention");
        return false;
    }
    
    return true;
}
</script>




<script  type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>




<style type="text/css">
body {font-size:100%}

div.leftBox{
   width:90%;
   margin-top: 2px;
   margin-left:3px;
   margin-right:3px;   
   float: left;
}

div.leftBox h3 {  
   background-color: #ccccff; 
   /*font-size: 1.25em;*/
   font-size: 8pt;
   font-variant:small-caps;
   font:bold;
   margin-top:0px;
   padding-top:0px;
   margin-bottom:0px;
   padding-bottom:0px;
}

div.leftBox ul{ 
       /*border-top: 1px solid #F11;*/
       /*border-bottom: 1px solid #F11;*/
       font-size: 1.0em;
       list-style:none;
       list-style-type:none; 
       list-style-position:outside;       
       padding-left:1px;
       margin-left:1px;    
       margin-top:0px;
       padding-top:1px;
       margin-bottom:0px;
       padding-bottom:0px;	
}

div.leftBox li {
padding-right: 15px;
white-space: nowrap;
}


div.headPrevention {  
    position:relative; 
    float:left;     
    width:8.4em;
    height:2.5em;
}

div.headPrevention p {    
    background: #EEF;
    font-family: verdana,tahoma,sans-serif;
    margin:0;
    
    padding: 4px 5px;
    line-height: 1.3;
    text-align: justify;
    height:2em;
    font-family: sans-serif;
    border-left: 0px;
}

div.headPrevention a {    
    text-decoration:none;
}

div.headPrevention a:active { color:blue; }
div.headPrevention a:hover { color:blue; }
div.headPrevention a:link { color:blue; }
div.headPrevention a:visited { color:blue; }


div.preventionProcedure{    
    width:10em;
    float:left;     
    margin-left:3px; 
    margin-bottom:3px;
}

div.preventionProcedure p {
    font-size: 0.8em;
    font-family: verdana,tahoma,sans-serif;
    background: #F0F0E7;    
    margin:0;
    padding: 1px 2px;
    /*line-height: 1.3;*/
    /*text-align: justify*/
}

div.preventionSection {    
    width: 100%;
    postion:relative;   
    margin-top:5px;
    float:left;
    clear:left;
}

div.preventionSet {
    border: thin solid grey;
    clear:left;
    }
    
div.recommendations{
    font-family: verdana,tahoma,sans-serif;
    font-size: 1.2em;
}    

div.recommendations ul{
    padding-left:15px;
    margin-left:1px;    
    margin-top:0px;
    padding-top:1px;
    margin-bottom:0px;
    padding-bottom:0px;	
}    

div.recommendations li{
    
}    
   
tr.newCode{
   background-color: #66FF66;
}
tr.nochange{
   display: none;
}
tr.down{
   background-color: red;
}
tr.up{
   background-color: #33FF33;
}
       

</style>

</head>

<body class="BodyStyle" >
<!--  -->
    
    <table  class="MainTable" id="scrollNumber1" >
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn"  >
      teleplan
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >     
                            
                            manage billing codes
                        </td>
                        <td  >&nbsp;
							
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
            
                       
              &nbsp;
               
            </td>
            
            <td valign="top" class="MainTableRightColumn">

               <html:form action="/billing/CA/BC/ManageTeleplan">
               <input type="hidden" name="method" value="commitUpdateBillingCodes"/> 
               <input type="submit" value="Update Codes"/>
                <table border="1"> 
                    <tr>
                        <th>Update</th>
                        <th>Code</th>
                        <th>OLD Fee</th>
                        <th>NEW Fee</th>
                        <th>Desc</th>
                        <th>Status</th>
                    </tr>
                    <%
                    List<HashMap> list = (List) request.getAttribute("codes");
                    for (HashMap h : list){
                        BillingService thisCode = billingServiceDao.searchBillingCode((String)  h.get("code"),"BC"); 
                        String oldFee = "---";
                        String classStyle = "newCode";
                        String selected = "checked";
                        if (thisCode != null){
                            oldFee = thisCode.getValue();
                            try{
                                double newDoub = Double.parseDouble(thisCode.getValue());                                             
                                BigDecimal oldPriceDec = new BigDecimal(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal newPriceDec = (BigDecimal) h.get("fee");

                                if(oldPriceDec.compareTo(newPriceDec) == 0){
                                    classStyle = "nochange";
                                    selected = "";
                                }else if(oldPriceDec.compareTo(newPriceDec) > 0){
                                    classStyle = "down";
                                }else if(oldPriceDec.compareTo(newPriceDec) < 0){
                                    classStyle = "up";
                                }
                            }catch(Exception e){
                                MiscUtils.getLogger().error("ERROR: oldfee "+oldFee);
                                classStyle = "error";
                            }
                            
                        }
                        String lin = Misc.forwardSpace(""+h.get("code"), 5)+Misc.forwardSpace(""+h.get("fee"),8)+Misc.forwardSpace(""+h.get("desc"),50);
                    %>
                   
                    <tr class="<%=classStyle%>">
                        <td><input type="checkbox" name="codes" value="<%=lin%>" <%=selected%>/></td>
                        <td><%=h.get("code")%></td>
                        <td><%=oldFee%></td>
                        <td><%=h.get("fee")%></td>
                        <td><%=h.get("desc")%></td>
                        <td><%=classStyle%></td>     
                    </tr>
                  <%}%>
                </table> 
                </html:form>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
                <input type="button" class="noPrint" name="printButton" onclick="EnablePrint(this)" value="Enable Print"/>
                &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
                &nbsp;
            </td>
        </tr>
    </table>
    
    
    </form>
</body>
</html:html>
