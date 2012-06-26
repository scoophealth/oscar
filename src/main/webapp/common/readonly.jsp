<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
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
