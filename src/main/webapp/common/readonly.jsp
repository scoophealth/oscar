<!--  isReadOnly ?? --->
<script type="text/javascript">
	var hashVal0= "";
	var needToConfirm = false;
	
    function setReadOnly()
    {
        readOnly = true;
        var k = document.forms[0].elements.length;
        for(var i=0; i < k; i++) 
        {
           var elem = document.forms[0].elements[i];
           if (elem) {
               if (elem.type == 'hidden' ) continue;
               if (elem.type == 'checkbox'||elem.type=='option'|| elem.type == 'radio'||elem.type=='textarea'|| elem.type=='button' || elem.type=='select-one') {
                  elem.disabled = true;
               }  
               else
               {
                  elem.readOnly=true;
               }
           }
        }
    }
	function getListVal(elSel)
	{
		var str = "";
		for(var i=0; i < elSel.options.length; i++){
			if(str == ""){
			   str = elSel.options[i].value;
			}else{  
			   str  += elSel.options[i].value;
			}
		}
		return str;
	}

    function getHash()
    {
       var hashVal = "";
       var frm = document.forms[0];
       if (!frm) return "";
       var k = frm.elements.length;
       for(var i=0; i < k; i++) 
       {
          var elem = frm.elements[i];
          if (elem) {
              if (elem.type == 'hidden' ) continue;
              if (elem.type == 'checkbox' || elem.type == 'radio') {
                 hashVal += elem.checked;
              }  
              else if (elem.type == 'select-multiple') {
              	hashVal += getListVal(elem);
              }
              else
              {
                 hashVal += " " + elem.value;
              }
          }
       }
       return hashVal;
   	}
   	function noChanges()
   	{
		var pageChangedBox = document.forms[0].pageChanged;
		var pageChanged = "";
		if (pageChangedBox != null) 
		{
			pageChanged = pageChangedBox.value;
		}
		if (pageChanged == "") {
	        var hashVal1 = getHash();
	        if(hashVal1 != hashVal0) pageChanged = "1";
     	}
     	return pageChanged == "";
   	}
	function confirmClose() {
		if(!needToConfirm) return;
		var pageChangedBox = document.forms[0].pageChanged;
		var pageChanged = "";
		if (pageChangedBox != null) 
		{
			pageChanged = pageChangedBox.value;
		}
		setNoConfirm();
		if (pageChanged == "") {
	        var hashVal1 = getHash();
	        if( hashVal1 != hashVal0) {
	         		return "You have made changes. To save these changes, click Cancel, then Save."; 
	     	}
     	}
     	else
     	{
     		return "You have made changes. To save these changes, click Cancel, then Save."; 
     	}
	}
	function setNoConfirm()
	{
        needToConfirm = false; 
        setTimeout('resetFlag()', 750);
	}	
    function resetFlag() { needToConfirm = true; } 
	function initHash()
	{
		hashVal0 = getHash();
	}
</script>
	<logic:notPresent name="isReadOnly">
		<script type="text/javascript">
			readOnly=false;
			needToConfirm = true;
		    window.onbeforeunload = confirmClose; 
		</script>
	</logic:notPresent>
	<logic:present name="isReadOnly">
		<logic:equal name="isReadOnly" value="true">
		<script type="text/javascript">
			readOnly = true;
			needToConfirm = false;
			setReadOnly();
		</script>
	</logic:equal>
	<logic:equal name="isReadOnly" value="false">
		<script type="text/javascript">
			readOnly=false;
			needToConfirm = true;
		    window.onbeforeunload = confirmClose; 
		</script>
	</logic:equal>
</logic:present>
<input type="hidden" name="token" value="<c:out value="${token}"/>" />