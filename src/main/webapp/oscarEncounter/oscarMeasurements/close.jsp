<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>close</title>
<script LANGUAGE="JavaScript">

//get query strings for opener url to match on submitting form
function getParameterByName(url, name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(url);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

<%if (session.getAttribute("textOnEncounter")!=null) {%>

if(opener.opener!=null || opener!=null){
	
if( opener.opener.document.forms["caseManagementEntryForm"] != undefined){        
				//from Templateflowsheet
	    	    opener.opener.pasteToEncounterNote('<%=session.getAttribute("textOnEncounter")%>');
				closeWin();
}else if( opener.document.forms["caseManagementEntryForm"] != undefined){ 
                
        if( getParameterByName(opener.location.href, 'demographicNo')==<%=request.getParameter("demographic_no")%> ){ 
				opener.pasteToEncounterNote('<%=session.getAttribute("textOnEncounter")%>');
				closeWin();
        }else{
				//display error
            	document.write("<h3>Sorry, there was an error so progress note will not be inserted into chart.</h3>");
        }
}         
 

}else{
		 //incase of unseen reason the above two conditions are not met then close window
    	 self.close();
}
     
<%}else{%>
		closeWin();
<%}%>

//TODO:// add a section here that uses an ajax call to add a note.  Or should this check be made on the submitting (check to see if the form is defined) for and let the action add the note?

function closeWin() {
      self.close();
      self.opener.location.reload();      
}
</script>

</head>
<body>

</body>
</html>
