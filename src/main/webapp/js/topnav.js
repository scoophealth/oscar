function refreshAllTabAlerts() {
	refreshTabAlerts("oscar_new_lab");
	refreshTabAlerts("oscar_new_msg");
	refreshTabAlerts("oscar_new_tickler");
	refreshTabAlerts("oscar_aged_consults");
	refreshTabAlerts("oscar_scratch");
}

function refreshTabAlerts(idval) {
	var url = "../provider/tabAlertsRefresh.jsp";
	$.get(url,{id:idval},function(data,textStatus){
		$("#"+idval).html(data);
	},"html");
}

$("document").ready(function(){
	refreshAllTabAlerts();
	window.setInterval(function() {
		refreshAllTabAlerts();
	},30000);
});

function popupInboxManager(varpage){
    var page = "" + varpage;
    var windowname="apptProviderSearch";
    windowprops = "height=700,width=1100,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=0";
    var popup = window.open(page, windowname, windowprops);
    if (popup != null) {
        if (popup.opener == null) {
            popup.opener = self;
        }
        popup.focus();
    }
}

function popupOscarRx(vheight,vwidth,varpage) {
	var page = varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
	var popup=window.open(varpage, "oscarRx_appt", windowprops);
	if (popup != null) {
		if (popup.opener == null) {
			popup.opener = self;
		}
		popup.focus();
	}
}

function popup(height, width, url, windowName) {
	  return popup2(height, width, 0, 0, url, windowName);
	}


function popup2(height, width, top, left, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}
