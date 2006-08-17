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

function popup(height, width, url, windowName){   
  var page = url;  
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";  
  var popup=window.open(url, windowName, windowprops);  
  if (popup != null){  
    if (popup.opener == null){  
      popup.opener = self;  
    }  
  }  
  popup.focus();  
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