<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="struts-logic" prefix="logic"%>
<%@ taglib uri="struts-bean" prefix="bean"%>
<%@ taglib uri="struts-html" prefix="html"%>
<%@ taglib uri="struts-layout" prefix="layout"%>

<html>
<head>
<link rel="stylesheet" href="../theme/Master.css" type="text/css">
<title>lookup</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Application Developer">
</head>
<body>
<layout:menu styleClass="FORM" align="left">
	<layout:menuItem key="Hydro One - H001" link="javascript:refreshParent('H001', 'Hydro One - H001')">
		<layout:menuItem key="STF RED 2002 - 0043" link="javascript:refreshParent('0043','STF RED 2002 - 0043')">
		</layout:menuItem>
		<layout:menuItem key="HMN RSRCES - 0235" link="javascript:refreshParent('0235','HMN RSRCES - 0235')">
			<layout:menuItem key="PLNG+INTGRTN - 0436" link="javascript:refreshParent('0436','PLNG+INTGRTN - 0436')"  />
			<layout:menuItem key="HMN OPTNS - 0639" link="javascript:refreshParent('0639','HMN OPTNS - 0639')" />
			<layout:menuItem key="COMP+BNFTS - 0670" link="javascript:refreshParent('0670','COMP+BNFTS - 0670')" />
		</layout:menuItem>
	</layout:menuItem>
</layout:menu>
<script>
function refreshParent(key, value){
 if (window.opener && !window.opener.closed){
    var elSel= window.opener.document.getElementsByName("lstOrg")[0]; 
    var bExist=false;
    for(var i=0;i<elSel.options.length;i++){
      if(value==elSel.options[i].value)
      {
         bExist=true;
         break;
      }
    }

    if(bExist==false){
       var elOptNew = window.opener.document.createElement("option");
       elOptNew.text = value;
       elOptNew.value = key;
       try {
         elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
       }
       catch(ex) {
         elSel.add(elOptNew); // IE only
       }
    }

  }
  window.close();
}
</script>
</body>
</html>
