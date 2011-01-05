
// setfocus to the current window
function setfocus() {
    this.focus();
}

// refresh this page
function refresh() {
	  history.go(0);
}
function showHideItem(id){
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = 'block';
    else
        document.getElementById(id).style.display = 'none';
}
var currentWidth;
// This function is useful to simulate min-width for the body in IE6 (for popup windows)
function setMinWidth(width){
    if (currentWidth != document.documentElement.clientWidth) {
        document.body.style.width = (document.documentElement.clientWidth < (width+10))? width+"px" : "auto";
    }
    currentWidth = document.documentElement.clientWidth;
}

// open a new popup window
function popupPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
//popup and focus
//allow to specify pageTitle
function popupFocusPage(vheight,vwidth,varpage,pageTitle){
            var page = "" + varpage;
            var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,"+
                "screenX=50,screenY=50,top=0,left=0";
            var popup=window.open(page, pageTitle, windowprops);
            if (popup != null) {
                        if (popup.opener == null) {
                            popup.opener = self;
                        }
                        popup.focus();
            }
}
// Prompt user if exiting without saving
function onExit() {
    if(confirm("Are you sure you wish to exit without saving your changes?")==true)
    {
        window.close();
    }
    return(false);
}

// Check if the entered type is a number
function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if ((ch < "0") || (ch > "9")) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}

// print the current window
function onPrint() {
	windows.print();
}

// change the case of current control's text to upper case
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}


function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}

function Check(e) {
	e.checked = true;
}

function Clear(e) {
	e.checked = false;
}
    
function CheckAll(ml) {
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
			Check(e);
	    }
	}
}

function ClearAll(ml) {
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Clear(e);
	    }
	}
}

//set the focus to this window
function start(){
	  this.focus();
}

//close the current window
function closeit() {
  self.opener.refresh();
  close();
}   

//ajax update of the current window
function updateAjax() {
    var parentAjaxId = "<%=parentAjaxId%>";    
    if( parentAjaxId != "null" ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
        window.opener.updateNeeded = true;    
    }
}

function setEnabledAll(form, enabled){
	for (var i = 0; i < form.elements.length; i++) {
		form.elements[i].disabled=!enabled;
	}
}

function getSelectedRadioValue(radioGroup)
{
	for (var i=0; i<radioGroup.length; i++)
	{
		if (radioGroup[i].checked == true) return(radioGroup[i].value);
	}
}


function selectSelectListOption(selectList, optionValue)
{
	 for (var i=0; i<selectList.options.length; i++)
	 {
		 if (selectList.options[i].value==optionValue)
		 {
			 selectList.selectedIndex=i;
			 return;
		 }
	 } 
}

function getSelectListValue(selectList)
{
	return(selectList.options[selectList.selectedIndex].value);
}