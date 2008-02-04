var IE_PRINT_CONTROL_PRODUCTVERSION = "11,5,0,253"
var NS_PRINT_CONTROL_PRODUCTVERSION = "11.5.0.253"

function blockEvents() {
    var deadend;
    opener.captureEvents(Event.CLICK, Event.MOUSEDOWN, Event.MOUSEUP, Event.FOCUS);
    opener.onclick = deadend;
    opener.onmousedown = deadend;
    opener.onmouseup = deadend;
    opener.focus = deadend;
}

function unblockEvents() {
    opener.releaseEvents(Event.CLICK, Event.MOUSEDOWN, Event.MOUSEUP, Event.FOCUS);
    opener.onclick = null;
    opener.mousedown = null;
    opener.mouseup = null;
    opener.onfocus = null;
}

function finished() {
    setTimeout("close()", 1000);
}

function installNsPlugin(pluginUrl, clientVersionRegistry) {
    var err = InstallTrigger.compareVersion(clientVersionRegistry, NS_PRINT_CONTROL_PRODUCTVERSION);

    if (err < 0)
    {
        var xpi = new Object();
        xpi[L_PrintControlPlugin] = pluginUrl;
        InstallTrigger.install(xpi, callback);
    }
}

function callback(url, status) {
    if (status) {
        alert(L_PrintControlInstallError + status);
    }
}

function checkModal(dlgWindow) {
    if (dlgWindow && !dlgWindow.closed)
    dlgWindow.focus();
}

function cancelPrinting(printControl) {
    if (printControl && printControl.IsBusy) {
        printControl.CancelPrinting();
    }
}
