// Javascripts that handle the code lookups
//
codeField = null;
descField = null;
descFieldx = null;
gradeField = null;
stepField = null;
rateField = null;
listFlag = "";
getEditValue = true;
getEditValueTime = true;
getEditValueDate = true;
codeValue = "";
doPostBack = false;
delaySearch= false;
emptyDate = new Date("01/01/1901");

function PrintForm(reportId, recordId){
    var url = appPath + "/Print/PrintView.aspx?id=" + reportId + ":" + recordId + ":p";
	top.childWin = window.open(url,"_blank","resizable=yes,scrollbars=yes,status=yes,width=650,height=400,top=50, left=50");
	top.childWin.focus();
}

function showReport(url){
//var url=str + "/PMmodule/Reports/quatroReportViewer.do";
	
top.childWin = window.open(url,"_blank","toolbar=yes,menubar= yes,resizable=yes,scrollbars=yes,status=yes,width=650,height=400,top=50, left=50");
	top.childWin.focus();
}

function showPrint(reportId, recordId){
    var url = appPath + "/Report/ReportPrint.aspx?repid=" + reportId + "&id=" + recordId;
	top.childWin = window.open(url,"_blank","resizable=yes,scrollbars=yes,status=yes,width=650,height=400,top=50, left=50");
	top.childWin.focus();
}


