<html>
<head>
<title>Macros</title>
</head>
<body>

<form name="inputForm" method="post" action="Macro.do">
	<input type="hidden" name="method" value="editMacro">
	<input type="hidden" name="editTarget" value="">
	<h1>Eyeform Macros</h1>

	
	<table style="border:0px">
	<tr>
	<td>
	<input type="button" value="Add" onclick="this.form.method.value='addMacro';this.form.submit();">
	<input type="button" value="Delete" onclick="if(confirm('Are you sure to delete the selected macros?')){this.form.method.value='deleteMacro';this.form.submit();}">
	<input type="button" value="Close" onclick="window.opener.location.reload();window.close();return false;">
	</td>
	</tr>
	</table>

	
</form>

</body>
</html>