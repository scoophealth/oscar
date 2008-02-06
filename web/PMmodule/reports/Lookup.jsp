<%@ include file="/taglibs.jsp" %>

<html>
<head>
<link rel="stylesheet" href="../theme/Master.css" type="text/css">
<title>lookup</title>
</head>
<body>
<table width=100%">
<tr><td><a href="javascript:refreshParent('H001', 'Hydro One - H001')">Hydro One - H001</a></td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;<a href="javascript:refreshParent('0043','STF RED 2002 - 0043')">STF RED 2002 - 0043</a></td></tr>
<tr><td><a href="javascript:refreshParent('0235','HMN RSRCES - 0235')">HMN RSRCES - 0235<</a></td></tr>
<tr><td><a href="javascript:refreshParent('0436','PLNG+INTGRTN - 0436')">PLNG+INTGRTN - 0436</a></td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;<a href="javascript:refreshParent('0639','HMN OPTNS - 0639')">HMN OPTNS - 0639</a></td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;<a href="javascript:refreshParent('0670','COMP+BNFTS - 0670')">COMP+BNFTS - 0670</a></td></tr>
</table>
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
