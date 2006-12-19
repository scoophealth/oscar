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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */

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


function validDate(id) {
   //checks if the <input type="text"> has a valid date format yyyy/mm/dd
   var completeRawDate = document.getElementById(id).value;
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


//to get elements by custom attributes, everything but the 'attribute' field is optional
document.getElementsByAttribute=function(attrN,attrV,multi){
    attrV=attrV.replace(/\|/g,'\\|').replace(/\[/g,'\\[').replace(/\(/g,'\\(').replace(/\+/g,'\\+').replace(/\./g,'\\.').replace(/\*/g,'\\*').replace(/\?/g,'\\?').replace(/\//g,'\\/');
    var
        multi=typeof multi!='undefined'?
            multi:
            false,
        cIterate=document.getElementsByTagName('*'),
        aResponse=[],
        attr,
        re=new RegExp(multi?'\\b'+attrV+'\\b':'^'+attrV+'$'),
        i=0,
        elm;
    while((elm=cIterate.item(i++))){
        attr=elm.getAttributeNode(attrN);
        if(attr &&
            attr.specified &&
            re.test(attr.value)
        )
            aResponse.push(elm);
    }
    return aResponse;
}

/*
oElm - Mandatory. This is element in whose children you will look for the attribute.
strTagName - Mandatory. This is the name of the HTML elements you want to look in. Use wildcard (*) if you want to look in all elements.
strAttributeName - Mandatory. The name of the attribute you’re looking for.
strAttributeValue - Optional. If you want the attribute you’re looking for to have a certain value as well. 
*/
document.getElementsByAttribute2 = function(oElm, strTagName, strAttributeName, strAttributeValue){
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