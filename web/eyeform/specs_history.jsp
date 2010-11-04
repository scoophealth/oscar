<%@page import="org.oscarehr.eyeform.model.SpecsHistory"%>


<%@ include file="/taglibs.jsp"%>


<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		
</head>
<body>
Specs History
<br />

<html:form action="/eyeform/SpecsHistory.do">
 <table border="0" cellspacing="5" width="100%">
  	   <tbody><tr>
	   <td>Date:</td>

	   <td>
	   <html:text property="specs.dateStr" size="10" styleId="pdate"/> <img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal">	  
	   </td>
	   <script type="text/javascript">
				Calendar.setup({ inputField : "pdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal", singleClick : true, step : 1 });
	   </script>	
	   </tr>
	   
	   <tr>
	   <td>Doctor Name:</td>
	   <td>
	   <html:text property="specs.doctor"/>
	   
	   </td>

	   </tr>
	   
	   <tr>
	   <td>Specs Type:</td>
	   <td>
	   <html:select property="specs.type">
	   	<html:option value="distance">distance</html:option>
	   	<html:option value="bifocal">bifocal</html:option>
	   	<html:option value="invisible bifocal">invisible bifocal</html:option>
	    <html:option value="reading">reading</html:option>
	   </html:select>
	   </td>
	   </tr>

	   <tr>
	   <td colspan="2" rowspan="3">
	   <table border="1" width="80%">
	   <tbody><tr>
	   <td width="10%"></td>

	   <td width="18%">Sph</td>
	   <td width="18%">Cyl</td>
	   <td width="18%">Axis</td>
	   <td width="18%">Add</td>
	   <td width="18%">Prism</td>
	   </tr>

	   <tr>
	   <td width="10%">OD</td>
	   <td width="18%"><html:text property="specs.odSph" size="8"   /></td>
	   <td width="18%"><html:text property="specs.odCyl" size="8"   /></td>
	   <td width="18%"><html:text property="specs.odAxis" size="8"   /></td>
	   <td width="18%"><html:text property="specs.odAdd" size="8"   /></td>
	   <td width="18%"><html:text property="specs.odPrism" size="8"   /></td>
	   </tr>

	   <tr>
	   <td width="10%">OS</td>
	   <td width="18%"><html:text property="specs.osSph" size="8"   /></td>
	   <td width="18%"><html:text property="specs.osCyl" size="8"   /></td>
	   <td width="18%"><html:text property="specs.osAxis" size="8"   /></td>
	   <td width="18%"><html:text property="specs.osAdd" size="8"   /></td>
	   <td width="18%"><html:text property="specs.osPrism" size="8"   /></td>
	   </tr>
	   
	   </tbody></table>
	   </td>
	   </tr>
	   
	   
  </tbody></table>
  <table width="80%">
  <tbody><tr>
	   <td colspan="2" align="right">
	   
	   <html:submit value="save" onclick="this.form.method.value='save'; return validate(this);" />
	   
	   
	   </td>

	   </tr>
  </tbody></table>


		<input type="hidden" name="method" value="save"/>
		
		<html:hidden property="specs.demographicNo"/>
		<html:hidden property="specs.appointmentNo"/>
		<html:hidden property="specs.id"/>
		
	

</html:form>

</body>
</html>
