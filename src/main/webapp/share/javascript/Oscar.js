/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

function popup(height, width, url, windowName) {
  return popup2(height, width, 0, 0, url, windowName);
}

function newWindow(url,windowName){
    //this way the w&d works with older browsers as well
    var w = document.getElementsByTagName('body')[0].clientWidth;//window.innerWidth;
    var h = document.getElementsByTagName('body')[0].clientHeight;//window.innerHeight;
    w = Math.max(w, window.innerWidth);
    h = Math.max(h, window.innerHeight);
    
    return popup2(h, w, 0, 0, url, windowName);
 }


function popup2(height, width, top, left, url, windowName){
  if ( typeof popup2.winRefs == 'undefined' ) {	  
	    popup2.winRefs = {};
  }
  if ( typeof popup2.winRefs[windowName] == 'undefined' || popup2.winRefs[windowName].closed ) {  
	  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=" + top + ",left=" + left;
	  popup2.winRefs[windowName]=window.open(url, windowName, windowprops);
  }
  else {
	  popup2.winRefs[windowName].location.href = url;
	  popup2.winRefs[windowName].resizeTo(width,height);
	  popup2.winRefs[windowName].focus();
  }
  
  return popup2.winRefs[windowName];  
}

function confirmNGo(url, message) {
    if (confirm(message)) {
        location.href = url;
    }
    return false;
}


function showHideItem(id){ 
    if(document.getElementById(id).style.display == 'none')
        showItem(id);        
    else
        hideItem(id);        
}

function showItem(id){
        document.getElementById(id).style.display = ''; 
}

function hideItem(id){
        document.getElementById(id).style.display = 'none'; 
}

/*
oElm - Mandatory. This is element in whose children you will look for the attribute.
strTagName - Mandatory. This is the name of the HTML elements you want to look in. Use wildcard (*) if you want to look in all elements.
strAttributeName - Mandatory. The name of the attribute you’re looking for.
strAttributeValue - Optional. If you want the attribute you’re looking for to have a certain value as well. (don't specify it if you don't know it)
*/
document.getElementsByAttribute = function(oElm, strTagName, strAttributeName, strAttributeValue){
    var arrElements = (strTagName == "*" && oElm.all)? oElm.all : oElm.getElementsByTagName(strTagName);
    var arrReturnElements = new Array();
    var oAttributeValue = (typeof strAttributeValue != "undefined")? new RegExp("(^|\\s)" + strAttributeValue + "(\\s|$)") : null;
    var oCurrent;
    var oAttribute;
    for(var i=0; i<arrElements.length; i++){
        oCurrent = arrElements[i];
        oAttribute = oCurrent.getAttribute && oCurrent.getAttribute(strAttributeName);
        if(typeof oAttribute == "string" && oAttribute.length > 0){
            if(typeof strAttributeValue == "undefined" || (oAttributeValue && oAttributeValue.test(oAttribute))){
                arrReturnElements.push(oCurrent);
            }
        }
    }
    return arrReturnElements;
}

function validDate(id) {
   //checks if the <input type="text"> has a valid date format yyyy/mm/dd
   var completeRawDate = document.getElementById(id).value;
   return validDateText(completeRawDate);
}

function validDateText(completeRawDate) {
//Just another method that validates date in a string, useful if you want to make your own
//date checking script.
   var delimiter = '/';
   if (completeRawDate.indexOf('/') == -1)
       delimiter = '-';
   var dateArray = completeRawDate.split(delimiter);
   if (dateArray.length != 3) return false;
   year = (dateArray[0] - 0);
   month = (dateArray[1] - 1);
   day = (dateArray[2] - 0);
   //alert("year: " + year + ", " + month + ", " + day);
   dateObject = new Date(year,month,day);
   return ((day==dateObject.getDate()) && (month==dateObject.getMonth()) && (year==dateObject.getFullYear()));
}

function validDateFieldsByClass(className, parentEle) {
//Checks if the dates are valid, identifies date fields by class name; datefield preferrably type="text"
// parentEle is any object that encloses the fields, usually the form element
   var datefields = document.getElementsByClassName(className, parentEle)
   for (var i=0; i<datefields.length; i++) {
      if (!validDateText(datefields[i].value)) {
        datefields[i].focus();
        return false;
      }
   }
   return true;
}

//returns mouse coordinates
function getMouseCoords(e) {
        var array = new Array(2);
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) 	{
        posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	}
        array[0] = posx;
        array[1] = posy;
        return array;
}

//example  <input type="checkbox" onclick="checkAll(this, 'parentEle', 'checkclass')">
function checkAll(master, parentEle, className){
   var val = master.checked;
   var chkList = document.getElementsByClassName(className, parentEle);
   for (i =0; i < chkList.length; i++){
      chkList[i].checked = val;
   }
}

//Calls firebugs console log if it's available.  A safer way to use the console because if firebug is not enabled the javascript will error on the console.log line.
function oscarLog(str){
    if(window.console){
       console.log(str);
    }
    
}


//Can be use the enter key in a text box and call javacript function
//example:  <itput type="text" onkeypress="return grabEnter(event,'ReferralScriptAttach1()')"/>
function grabEnter(event,callb){
  if( (window.event && window.event.keyCode == 13) || (event && event.which == 13) )  {
     eval(callb);
     return false;
  }
}



//Checks to see if a String is numeric integer ie. No decimal aloud
function isNumericInt(sText){
   var validNumChars = "0123456789";
   var isNumber=true;
   var chara;
   for (i = 0; i < sText.length && isNumber == true; i++) {
      chara = sText.charAt(i);
      if (validNumChars.indexOf(chara) == -1) {
         isNumber = false;
      }
   }
   return isNumber;
}

//Checks to see if a String is numeric
function isNumeric(sText){
   var validNumChars = "0123456789.";
   var isNumber=true;
   var chara;
   for (i = 0; i < sText.length && isNumber == true; i++) {
      chara = sText.charAt(i);
      if (validNumChars.indexOf(chara) == -1) {
         isNumber = false;
      }
   }
   return isNumber;
}
//remove leading and trailing white space
function trim(str){
   str=str.replace(/^\s+/g,"");
   str=str.replace(/\s+$/g,"");//trim str
   return str;
}