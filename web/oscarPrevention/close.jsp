<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head><title>close</title>
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
