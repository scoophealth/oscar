<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <%      
  if(session.getValue("user") == null)
      response.sendRedirect("../logout.jsp");
  String user_no; 
  user_no = (String) session.getAttribute("user");
  String asstProvider_no = "";
  String color ="";
  String premiumFlag="";
String service_form="", service_name="";
%>       
<html>
<head>
<title>oscarBilling :: Clip board  ::</title>
<link rel="stylesheet" href="billing.css" >

<style type="text/css">
	<!--
	BODY                  {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD                    {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000                                                    }
	TD.black              {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	TD.lilac              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.boldlilac          {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:hover      {font-weight: normal;                                                                            color: #000000; background-color: #CDCFFF  ;}
	TD.white              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.heading            {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	H2                    {font-weight: bold  ; font-size: 12pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H3                    {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H4                    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H6                    {font-weight: bold  ; font-size: 7pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	A:link                {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; background-color: #FFFFFF;}
	A:visited             {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; background-color: #FFFFFF;}
	A:hover               {                                                                            color: red; background-color: #CDCFFF  ;}
	TD.cost               {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	TD.black A:link       {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699;}
	TD.black A:visited    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699;}
	TD.black A:hover      {                                                                            color: #FDCB03; background-color: #666699;}
	TD.title              {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white 	      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #CDCFFF  ;}
	#navbar               {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	SPAN.navbar A:link    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	SPAN.navbar A:visited {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #EFEFEF; background-color: #666699   ;}
	SPAN.navbar A:hover   {                                                                            color: #FDCB03; background-color: #666699   ;}
	SPAN.bold             {font-weight: bold  ;                                                                                            background-color: #666699   ;}
	.sbttn {background: #EEEEFF;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}
        .mbttn {background: #D7DBF2;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}
	-->
</style>  
<script language="JavaScript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}

function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}


//-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-2">
</head>

<body leftmargin="0" topmargin="5" rightmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="20%"><form><input class=mbttn type=button name=print value=PRINT onClick=window.print()><input class=mbttn type=button name=back value=BACK onClick=history.go(-1)></form> </td>
    <td width="80%" align="left" bgcolor="#000000"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3">Billing</font></font></b></font> 
        <font color="#CCCCCC">Ciipboard </font></p>
    </td>
  </tr>
</table> 


<pre>
<%=request.getParameter("textfield")==null?"":request.getParameter("textfield")%>
</pre>

  <pre>
<% 
String tmp1 = "";
String tmp =request.getParameter("textfield1")==null?"":request.getParameter("textfield1");
tmp1 = tmp;
while (tmp1.length() > 70){
%>
<%=tmp1.substring(0,70)%>
<%
tmp1 = tmp1.substring(70);

}


%>
<%=tmp1%>
</pre>


 </body>
</html>
