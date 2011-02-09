<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>close</title>
<script LANGUAGE="JavaScript">
    
     if( opener.opener.document.forms["caseManagementEntryForm"] != undefined ){        
        opener.opener.pasteToEncounterNote('<%=session.getAttribute("textOnEncounter")%>');
     }else if( opener.document.forms["caseManagementEntryForm"] != undefined ){        
        opener.pasteToEncounterNote('<%=session.getAttribute("textOnEncounter")%>');
     }
     //TODO:// add a section here that uses an ajax call to add a note.  Or should this check be made on the submitting (check to see if the form is defined) for and let the action add the note?
  
    
    
function closeWin() {
      
      self.close();
      self.opener.location.reload();      
}
</script>

</head>
<body onload="closeWin();">

</body>
</html>
