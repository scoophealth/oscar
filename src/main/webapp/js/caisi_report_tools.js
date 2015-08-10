
   
function getGeneralFormsReport()
{		
	popupPage2('<html:rewrite action="/PMmodule/ClientManager.do"/>?method=getGeneralFormsReport',"generalFormsReport");
}

function getIntakeReport(type) {
    var oneWeekAgo = new Date();
    oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

    var startDate = prompt("Please enter a start date in this format (e.g. 2000-01-01)", dojo.date.format(oneWeekAgo, {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
    if (startDate == null) {
        return;
    }
    if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
        alert("'" + startDate + "' is not a valid start date");
        return;
    }

    var endDate = prompt("Please enter the end date in this format (e.g. 2000-12-01)", dojo.date.format(new Date(), {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
    if (endDate == null) {
        return;
    }
    if (!dojo.validate.isValidDate(endDate, 'YYYY-MM-DD')) {
        alert("'" + endDate + "' is not a valid end date");
        return;
    }

    var includePast = confirm("Do you want to include past intake forms in your report? ([OK] for yes / [Cancel] for no)");

    alert("Generating report from " + startDate + " to " + endDate + ". Please note: it is normal for the generation process to take up to a few minutes to complete, be patient.");

    var url = "../PMmodule/GenericIntake/Report.do?method=report&type="+type + "&startDate=" + startDate + "&endDate=" + endDate + "&includePast=" + includePast;
    popupPage2(url, "IntakeReport" + type);
}



function createStreetHealthReport()
{
    var startDate = "";

    while (startDate.length != 10 || startDate.substring(4, 5) != "-" || startDate.substring(7, 8) != "-")
    {
        startDate = prompt("Please enter start date (e.g. 2006-01-01)", "<%=dateStr%>");
        if (startDate == null) {
            return false;
        }
        if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
            alert("'" + startDate + "' is not a valid start date");
            return false;
        }

    }

    alert('Generating report for date ' + startDate);

    //popupPage2('<html:rewrite action="/PMmodule/StreetHealthIntakeReportAction.do"/>?startDate=' + startDate, "StreetHealthReport");
    popupPage2("../PMmodule/StreetHealthIntakeReportAction.do?startDate=" + startDate, "StreetHealthReport");
}


function popupPage2(varpage, windowname) 
{
    var page = "" + varpage;
    var windowprops = "height=700,width=1000,top=10,left=0,location=yes,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup = window.open(page, windowname, windowprops);
    if (popup != null) {
        if (popup.opener == null) {
            popup.opener = self;
        }
        popup.focus();
    }
}


