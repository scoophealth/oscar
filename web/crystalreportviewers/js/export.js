// export.js
// This file contains the funcitons needed to construct the HTML for the export / print dialog.
//
// Global variable
var print = false;		// default to export, so set print to false
var crystal_postback =
        "<INPUT type=\"hidden\" name=\"reportsource\" id=\"reportsource\"/>" +
        "<INPUT type=\"hidden\" name=\"viewstate\" id=\"viewstate\"/>";

function getPageTitle() {
	if (print) {
		return L_PrintPageTitle;
	}
	else {
		return L_ExportPageTitle;
	}
}

function getOptionsTitle() {
	if (print) {
		return L_PrintOptions;
	}
	else {
		return L_ExportOptions;
	}
}

function getFormatDropdownList() {
	if (print) {
		return "<INPUT type=\"hidden\" name=\"exportformat\" id=\"exportformat\" value=\"PDF\"/>";
	}
	else {
		var list =
		"<select id=\"exportformatlist\" class=\"crexportselect\" name=\"exportformat\" onchange=\"checkDisableRange();\">";
		if( rpt )
		{
			list += "<OPTION value=\"CrystalReports\">" + L_CrystalRptFormat + "</OPTION>";
		}
		else
		{
			list += "<OPTION value=\"\">" + L_Formats + "</OPTION>";
		}
		if( pdf )
		{
			list += "<OPTION value=\"PDF\">" + L_AcrobatFormat + "</OPTION>";
		}
		if( xls )
		{
			list += "<OPTION value=\"MSExcel\">" + L_ExcelFormat + "</OPTION>";
		}
		if( recXls )
		{
			list += "<OPTION value=\"RecordToMSExcel\">" + L_ExcelRecordFormat + "</OPTION>";
		}
		if( word )
		{
			list += "<OPTION value=\"MSWord\">" + L_WordFormat + "</OPTION>";
		}
		if( ertf )
		{
			list += "<OPTION value=\"EditableRTF\">" + L_EditableRTFFormat +"</OPTION>";
		}
		if( rtf )
		{
			list += "<OPTION value=\"RTF\">" + L_RTFFormat +"</OPTION>";
		}
		
		list += "</SELECT>"
		return list;
	}
}

function getSelectPageRangeSentence() {
	if (print) {
		return L_PrintPageRange;
	}
	else {
		return L_ExportPageRange;
	}
}

function getExportDialog() {
	var exportDialog =
		"<HTML>" +
		"<HEAD>" +
		"<LINK rel=\"stylesheet\" type=\"text/css\" href=\"../css/default.css\">" +

		"<script type=\"text/javascript\" src=\"../js/exportdialog.js\"></script>" +
		"<script type=\"text/javascript\">" +
		"	function checkValuesAndSubmit() {" +
		"		if (document.forms[\"dlgform\"].isRange != null &&" +
		"		    document.forms[\"dlgform\"].isRange[1].checked) {" +
		"			if (!isValidNumber(document.forms[\"dlgform\"].from.value) ||" +
		"			    !isValidNumber(document.forms[\"dlgform\"].to.value) ||" +
		"		    	    (parseInt(document.forms[\"dlgform\"].from.value, 10) > parseInt(document.forms[\"dlgform\"].to.value, 10))) {" +
		"				alert(\"" + L_InvalidPageRange + "\");" +
		"				return;" +
		"			}" +
		"		}" +
		"		if (document.forms[\"dlgform\"].exportformat != null &&" +
		"		    document.forms[\"dlgform\"].exportformat.value == \"\") {" +
		"			alert(\"" + L_ExportFormat + "\");" +
		"			return;" +
		"		}" +	
		"		document.dlgform.action = opener.document.getElementById(\"crystal_handler_page\").value;" +
		"		document.dlgform.submit();" +
		"		document.getElementById(\"submitexport\").disabled = true;" +
		"	}" +
		"</script>" +
		"<TITLE>" + getPageTitle() + "</TITLE>" +
		"</HEAD>" +
		"<BODY class=\"crexportpage\" bottommargin=\"5\" topmargin=\"5\" onload=\"checkDisableRange();\">" +
		"<FORM name=\"dlgform\" method=\"POST\">" +
		crystal_postback +		
		"<TABLE cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"100%\" valign=\"bottom\" halign=\"center\" border=\"0\">";

	if (!print) {
		exportDialog +=
		"  <TR><TD align=\"left\"><table width=\"100%\" border=\"0\">" + 
		"    <tr>" +
		"      <td width=\"35%\"><span class=\"crexportmessage\"><label for=\"exportformatlist\">" + L_Formats + "</label></span></td>" + 
		"      <td align=\"left\" width=\"65%\" class=\"crexportselect\">" + 
		getFormatDropdownList() +
		"      </td>" +
		"    </tr>" + 
		"  </table></TD></TR>" + 
		"  <TR><TD><hr class=\"crexportruler\"></TD></TR>";
	}

		exportDialog +=
		"  <TR><TD align=\"left\"><table width=\"100%\" border=\"0\">" +
		"    <tr>" +
		"      <td width=\"35%\"><span class=\"crexportmessage\">" + L_PageRange + "</span></td>" + 
		"      <td width=\"65%\" align=\"left\"><input type=\"radio\" id=\"radio1\" name=\"isRange\" value=\"all\" onclick=\"return toggleRangeFields(this);\"/><span class=\"crexportmessage\"><label for=\"radio1\">" + L_All + "</label></span></td>" + 
		"    </tr>" +
		"    <tr>" + 
		"      <td rowspan=\"2\"></td>" + 
		"      <td align=\"left\"><input type=\"radio\" id=\"radio2\" checked name=\"isRange\" value=\"selection\" onclick=\"return toggleRangeFields(this);\"/><span class=\"crexportmessage\"><label for=\"radio2\">" + L_Pages + "</label></span></td>" +
		"    </tr>" +
		"    <tr>" +
		"      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"crexportmessage\"><label for=\"from\">" + L_From + "</label></span>&nbsp;&nbsp;<input class=\"crexporttextbox\" type=\"text\" width=\"20\" size=\"6\" maxlength=\"6\" name=\"from\" value=\"1\">&nbsp;&nbsp;<span class=\"crexportmessage\"><label for=\"to\">" + L_To + "</label></span>&nbsp;&nbsp;<input class=\"crexporttextbox\" type=\"text\" width=\"20\" size=\"6\" maxlength=\"6\" name=\"to\" value=\"1\"></td>" +
		"    </tr>" +
		"  </table></TD></TR>" +
		"  <TR><TD><hr class=\"crexportruler\"></TD></TR>";
	
	if (print) {
		exportDialog +=
		"  <TR><TD>" +
		"    <input type=\"hidden\" name=\"exportformat\" value=\"PDF\">" +
		"    <table>" +
		"    <tr><td><span class=\"crexportmessage\">" + L_PrintStep0 + "</span></td></tr>" +
		"    <tr><td><span class=\"crexportmessage\">" + L_PrintStep1 + "</span></td></tr>" +
		"    <tr><td><span class=\"crexportmessage\">" + L_PrintStep2 + "</span></td></tr>" +
		"    </table>"+
		"  </TD></TR>";
	}

		exportDialog +=
		"  <TR valign=\"top\"><TD align=\"right\"><input class=\"crexportbutton\" id=\"submitexport\" type=\"button\" value=\"" + L_OK + "\" onclick=\"checkValuesAndSubmit();\"/>&nbsp;&nbsp;<input class=\"crexportbutton\" type=\"button\" value=\"" + L_Cancel + "\" onclick=\"window.close();\"/></TD></TR>" +
		"</TABLE>" +
		"</FORM>" +
		"</BODY>" +
		"</HTML>";

		return exportDialog;
}

