// our global vars
var heightnow;
var targetheight;
var block;
var slideinterval;
var divheights = new Array();

// the delay between the slide in/out, and a little inertia
var inertiabase = 1;
var inertiainc = 1;
var slideintervalinc = 1;
var inertiabaseoriginal = inertiabase;

var closedSections = [];
function getFormField() {
	return document.getElementById("closedSections");
}

function initSections() {
	var ssv=getFormField().value;
	closedSections = ssv.split(",");
	for (var i=0; i<closedSections.length; i++) {
		var s = document.getElementById(closedSections[i]);
		if (s!=null) document.getElementById(closedSections[i]).style.display="none";
	}
}

function setup_slidey()
{
	
    // detect whether the user has ie or not, how we get the height is different 
    var useragent = navigator.userAgent.toLowerCase();
    var ie = ((useragent.indexOf('msie') != -1) && (useragent.indexOf('opera') == -1) && (useragent.indexOf('webtv') == -1));
    var divs = getElementsByClassName(document, "div", "slideblock");

    for(var i=0; i<divs.length; i++)
    {
        // get the original height
        var baseheight = (ie) ? divs[i].offsetHeight + "px" : document.defaultView.getComputedStyle(divs[i], null).getPropertyValue('height', null);

        // explicitly display it (optional, you could use cookies to toggle whether to display it or not)
        divs[i].style.display = "block";

        // "save" our div height, because once it's display is set to none we can't get the original height again
        var d = new div();
        d.el = divs[i];
        d.ht = baseheight.substring(0, baseheight.indexOf("p"));

        // store our saved versoin
        divheights[i] = d;        
    }
	
	initSections();
	
	touchColor();
}

// this is one of our divs, it just has a DOM reference to the element and the original height
function div(_el, _ht)
{
    this.el = _el;
    this.ht = _ht;
}

// this is our slidein function the interval uses, it keeps subtracting
// from the height till it's 1px then it hides it
function slidein()
{
    if(heightnow > targetheight)
    {
        // reduce the height by intertiabase * inertiainc
        heightnow -= inertiabase;

        // increase the intertiabase by the amount to keep it changing
        inertiabase += inertiainc;

        // it's possible to exceed the height we want so we use a ternary - (condition) ? when true : when false;
        block.style.height = (heightnow > 1) ? heightnow + "px" : targetheight + "px";
    }
    else
    {
        // finished, so hide the div properly and kill the interval
        clearInterval(slideinterval);
        block.style.display = "none";
    }
}

// this is the function our slideout interval uses, it keeps adding
// to the height till it's fully displayed
function slideout()
{
    if(heightnow < targetheight)
    {
        // increases the height by the inertia stuff
        heightnow += inertiabase;

        // increase the inertia stuff
        inertiabase += inertiainc;

        // it's possible to exceed the height we want so we use a ternary - (condition) ? when true : when false;
        block.style.height = (heightnow < targetheight) ? heightnow + "px" : targetheight + "px";
        
    }
    else
    {
        // finished, so make sure the height is what it's meant to be (inertia can make it off a little)
        // then kill the interval
        clearInterval(slideinterval);
        block.style.height = targetheight + "px";
    }
}

// returns the height of the div from our array of such things
function divheight(d)
{
    for(var i=0; i<divheights.length; i++)
    {
        if(divheights[i].el == d)
        {
            return divheights[i].ht;
        }
    }
}

/*
    the getElementsByClassName function I pilfered from this guy.  It's
    a useful function that'll return any/all tags with a specific css class.

        Written by Jonathan Snook, http://www.snook.ca/jonathan
        Add-ons by Robert Nyman, http://www.robertnyman.com
*/
function getElementsByClassName(oElm, strTagName, strClassName)
{
    // first it gets all of the specified tags
    var arrElements = (strTagName == "*" && document.all) ? document.all : oElm.getElementsByTagName(strTagName);
    
    // then it sets up an array that'll hold the results
    var arrReturnElements = new Array();

    // some regex stuff you don't need to worry about
    strClassName = strClassName.replace(/\-/g, "\\-");

    var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
    var oElement;

    // now it iterates through the elements it grabbed above
    for(var i=0; i<arrElements.length; i++)
    {
        oElement = arrElements[i];

        // if the class matches what we're looking for it ads to the results array
        if(oRegExp.test(oElement.className))
        {
            arrReturnElements.push(oElement);
        }   
    }

    // then it kicks the results back to us
    return (arrReturnElements)
}

function togglediv(t)
{

    // reset our inertia base and interval
    inertiabase = inertiabaseoriginal;
    clearInterval(slideinterval);

    // get our block
    block = t.parentNode.nextSibling;

    // for mozilla, it doesn't like whitespace between elements
    if(block.className == undefined)
        block = t.parentNode.nextSibling.nextSibling;
	
    if(block.style.display == "none")
    {
        // link text
        // t.innerHTML = "Hide";
		for (var i=0; i<closedSections.length; i++)
			if (closedSections[i]==block.id)
				closedSections.splice(i,1);
		getFormField().value=closedSections.join(",");	
		
        block.style.display = "block";
        block.style.height = "1px";

        // our goal and current height
        targetheight = divheight(block);
        heightnow = 1;

        // our interval
        slideinterval = setInterval(slideout, slideintervalinc);
    }
    else
    {
        // linkstext
        // t.innerHTML = "Show";
		closedSections.push(block.id);
		getFormField().value=closedSections.join(",");			

        // our goal and current height
        targetheight = 1;
        heightnow = divheight(block);

        // our interval
        slideinterval = setInterval(slidein, slideintervalinc);
    }
}

function expandAll() {
	var divs = getElementsByClassName(document, "div", "slideblock");
	for (var i=0; i<divs.length; i++) {
		divs[i].style.height=divheight(divs[i]);
		divs[i].style.display="block";
	}
	getFormField().value="";
}

function collapseAll(){
	var divs = getElementsByClassName(document, "div", "slideblock");
	getFormField().value="";
	for (var i=0; i<divs.length; i++) { 
		divs[i].style.display="none";
		getFormField().value+=divs[i].id+",";
	}
}
