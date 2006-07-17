<%@ page contentType="text/html" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<!DOCTYPE html PUBLIC "-//Tigris//DTD XHTML 1.0 Transitional//EN"
"tigris_transitional.dtd">

<html>
<head>
<title>Rating statistic</title>
<style type="text/css">
/* <![CDATA[ */
@import "<html:rewrite page="/css/tigris.css" />";
/*  ]]> */
</style>

</head>
<body marginwidth="0" marginheight="0">

<div  class="composite">


<table border="0" cellspacing="0" cellpadding="18" width="100%">
   <tr valign="top">
   <td>
   <div class=body>
	<div id="bodycol">

	<div id="apphead">
	<h3>Thank you for rating CAISI page!</h3>
	<h3>If you have any comments or suggestions please visit our CAISI 
	  <a href="http://caisi.ca/mailman/listinfo">forum</a>.</h3> 
	<br>
	<h3>Page rating statistic</h3>
	</div>
	</div>

	<div id="bodycol">

	<div id="projecthome" class="app">


	<div class="axial">
	
	<nested:form action="/ratingStatics.do">
	<table border="1" cellspacing="2" cellpadding="3" width="100%">
		<tr>
		<th>Page name</th>
		<th>Average score</th>
		<th>Visitors</th>
		</tr>

		<nested:iterate id="ratingPage" property="rate_table">
		
   		<tr class="b"> 
   			<td> 
   				<nested:write name="ratingPage" property="pageName"/>
      		</td>
      		
   			<td> 
   				<nested:write name="ratingPage" property="avrgScore"/>
   			</td> 

   			<td> 
   				<nested:write name="ratingPage" property="vstNumber"/>
   
   			</td> 

      	</tr> 

	</nested:iterate>
		
	</table>

</nested:form>
</div>
</tr>
</table>
</div>
</body>
</html>



