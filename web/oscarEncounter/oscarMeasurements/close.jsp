<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>close</title>
<script LANGUAGE="JavaScript">
function closeWin() {
      
      self.close();
      self.opener.location.reload();      
}
</script>

</head>
<body onload="closeWin();">

</body>
</html>
