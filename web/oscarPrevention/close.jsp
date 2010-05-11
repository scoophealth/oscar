<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>close</title>
<script LANGUAGE="JavaScript">
function closeWin() {
      
      
      if ( self.opener.refreshInfo){
         self.opener.refreshInfo();
         self.setTimeout('closeThisWindow()', 5000)  
      }else{
         self.close();
         self.opener.location.reload();      
      }
}
function closeThisWindow(){
   self.close();
}
</script>

</head>
<body onload="closeWin();">
Click to
<a href="javascript: function myFunction() {return false; }"
	onclick="window.close();">Close</a>
window
</body>
</html>
