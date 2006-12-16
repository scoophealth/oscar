var isAlreadySubmitted = false;

function setSubmitState(state)
{
	isAlreadySubmitted = state;
}

function checkSubmitState(href, target)
{
	if (isAlreadySubmitted)
	{
		alert("Please wait; your request is already being processed");
	}
	else
	{		
		if (target)
		{
			window.open(href, target);
		}
		else
		{
			window.location = href;
		}
	}
	return false;
}

function dontCheckSubmitState(href, target)
{
	if (target)
	{
		window.open(href, target);
	}
	else
	{
		window.location = href;
	}
}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);


function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_nbGroup(event, grpName) { //v6.0
  var i,img,nbArr,args=MM_nbGroup.arguments;
  if (event == "init" && args.length > 2) {
    if ((img = MM_findObj(args[2])) != null && !img.MM_init) {
      img.MM_init = true; img.MM_up = args[3]; img.MM_dn = img.src;
      if ((nbArr = document[grpName]) == null) nbArr = document[grpName] = new Array();
      nbArr[nbArr.length] = img;
      for (i=4; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
        if (!img.MM_up) img.MM_up = img.src;
        img.src = img.MM_dn = args[i+1];
        nbArr[nbArr.length] = img;
    } }
  } else if (event == "over") {
    document.MM_nbOver = nbArr = new Array();
    for (i=1; i < args.length-1; i+=3) if ((img = MM_findObj(args[i])) != null) {
      if (!img.MM_up) img.MM_up = img.src;
      img.src = (img.MM_dn && args[i+2]) ? args[i+2] : ((args[i+1])? args[i+1] : img.MM_up);
      nbArr[nbArr.length] = img;
    }
  } else if (event == "out" ) {
    for (i=0; i < document.MM_nbOver.length; i++) {
      img = document.MM_nbOver[i]; img.src = (img.MM_dn) ? img.MM_dn : img.MM_up; }
  } else if (event == "down") {
    nbArr = document[grpName];
    if (nbArr)
      for (i=0; i < nbArr.length; i++) { img=nbArr[i]; img.src = img.MM_up; img.MM_dn = 0; }
    document[grpName] = nbArr = new Array();
    for (i=2; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
      if (!img.MM_up) img.MM_up = img.src;
      img.src = img.MM_dn = (args[i+1])? args[i+1] : img.MM_up;
      nbArr[nbArr.length] = img;
  } }
}


function getElement(id, component)
{
    return document.getElementById(id + '_' + component);
}

function doExpandCollapse(id)
{
    var content = getElement(id, 'content');
    if (content.style.display == 'none')
    {
        expand(id);
    }
    else
    {
        collapse(id);
    }
}

function expand(id)
{
    var content = getElement(id, 'content');
    var summary = getElement(id, 'summary');
    var expand = getElement(id, 'expand');

    content.style.display = 'block';
    expand.src = collapseImage;

    if (summary != null)
    {
        summary.style.display = 'none';
    }
}

function collapse(id)
{
    var content = getElement(id, 'content');
    var summary = getElement(id, 'summary');
    var expand = getElement(id, 'expand');

    content.style.display = 'none';
    expand.src = expandImage;

    if (summary != null)
    {
        summary.style.display = 'block';
    }
}


function doExpandCollapseHTML(id, imgCol, imgExp, title) {

    var content = getElement(id, 'content');
    if (content.style.display == 'none')
    {
        expandHTML(id, imgCol, title);
    }
    else
    {
        collapseHTML(id, imgExp, title);
    }
	
}

function expandHTML(id, img, title)
{
    var content = getElement(id, 'content');
    var summary = getElement(id, 'summary');
    var expand = getElement(id, 'expand');

    content.style.display = 'block';
    if(img!=null) {
    	expand.src = img;
    }
    else {
	    expand.src = collapseImage;
    }

    if(title!=null) {
    	expand.title = "Collapse " + title;
    	expand.alt = "Collapse " + title;
    }

    if (summary != null)
    {
        summary.style.display = 'none';
    }
}

function collapseHTML(id, img, title)
{
    var content = getElement(id, 'content');
    var summary = getElement(id, 'summary');
    var expand = getElement(id, 'expand');

    content.style.display = 'none';
    if(img!=null) {
    	expand.src = img;
    }
    else {
	    expand.src = expandImage;
    }

    if(title!=null) {
    	expand.title = "Expand " + title;
    	expand.alt = "Expand " + title;
    }

    if (summary != null)
    {
        summary.style.display = 'block';
    }
}


function openSelectionLink(selection, windowName) 
{
	var URL;
	var win = "";
	
	if (typeof(selection) == "string") {
		URL = selection;
	} else if (selection.selectedIndex) {
		// Select list option with url and page title
	    URL = selection.options[selection.selectedIndex].value;
	    win = selection.options[selection.selectedIndex].text.replace(" ", "");
	    selection.selectedIndex = 0;
	} else {
		// Field where value is url
		URL = sel.value;
	}
	
	//Make sure calls to BIND stay in the same window
	if(win==windowName) {
		win="_self";
	}
    
    if (URL != "") window.open(URL, win);
}

function openLink(URL)
{
    if(URL!="") location = URL;
}

/**
 * Opens a location in the current window.
 * @param location  url to open
 * @param params    parameters (must include '?')
 */
function openLocation(form, location, params)
{
	if (params != null) {
		location = location + params;
	}
	window.location = location;
	return false;
}

function submitEnter(myfield, e)
{
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return true;

	if (keycode == 13) {
		myfield.form.submit();
		return false;
	} else {
		return true;
	}
}

function xmlhttpPost(requestURL, changeFunc, queryString) {
    var xmlHttpReq = false;
    var self = this;
    // Mozilla/Safari
    if (window.XMLHttpRequest) {
        self.xmlHttpReq = new XMLHttpRequest();
        //self.xmlHttpReq.overrideMimeType("text/xml");
    }
    // IE
    else if (window.ActiveXObject) {
        self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
    self.xmlHttpReq.open('POST', requestURL, true);
    self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    self.xmlHttpReq.onreadystatechange = changeFunc;
    self.xmlHttpReq.send(queryString);
}
