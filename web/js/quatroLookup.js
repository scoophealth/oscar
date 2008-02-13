codeField = null;
descField = null;
gradeField = null;
stepField = null;
rateField = null;
listFlag = "";
getEditValue = true;
getEditValueTime = true;
getEditValueDate = true;
codeValue = "";
doPostBack = false;
delaySearch= false;
emptyDate = new Date("01/01/1901");

function showLookup(tableId, grandParentName, parentName, openerFormName, codeFieldName, descFieldName, displayCode, appRoot) {
    var grandParentCode = null;
    var parentCode = null;   

    if(grandParentName!='') grandParentCode = document.getElementsByName(grandParentName)[0]; 
    if(parentName!='') parentCode = document.getElementsByName(parentName)[0];    


    codeField = document.getElementsByName(codeFieldName)[0]; 

    codeField.focus();
	
	//open the lookup URL
	var queryString = "?openerForm=" + openerFormName;
	queryString = queryString + "&codeName=" + codeFieldName;
	queryString = queryString + "&descName=" + descFieldName;
	queryString = queryString + "&tableId=" + tableId;
	var noParent = false;
	if (parentCode != null) {	    
		if (grandParentCode != null) {
		    if(grandParentCode.value == "" ||  parentCode.value == "") {
		        alert ("Please enter values in the box before");
		        return;
		     }
			queryString += "&grandParentCode=" + grandParentCode.value + "&parentCode=" + parentCode.value;
		} else {
		    if(parentCode.value == "") {
		        alert ("Please enter values in the box before");
		        return;
		     }
			queryString += "&parentCode=" + parentCode.value;
		}
	}
    if(delaySearch){
        queryString += "&d=1";
    }    
    if(displayCode=="True"){
        queryString += "&dc=1";
    }
        
	var lookupURL = "/" + appRoot.replace("/", "") + "/QuatroLookup/LookupList.do" + queryString; 

	delaySearch =false;
	top.childWin = window.open(lookupURL,"_blank","resizable=yes,scrollbars=yes,status=yes,width=600,height=450,top=120, left=200");
	top.childWin.focus();
	
}

function selectMe(code, Desc, form_name, code_element_name, Desc_element_name) {
   if(Desc_element_name!=""){  //for both lookup tag and html dropdown
      var myexpr = "opener.document." + form_name + ".elements['" + code_element_name +"'].value='" + code +"'";
      eval(myexpr);
      myexpr = "opener.document." + form_name + ".elements['" + Desc_element_name +"'].value='" + Desc + "'";
      eval(myexpr);
   }else{
     AddtoDropdown(code, Desc, code_element_name);
   }
   self.close();
}

function AddtoDropdown(key, value, element_name){
 if (window.opener && !window.opener.closed){
    var elSel= window.opener.document.getElementsByName(element_name)[0]; 
    var bExist=false;
    for(var i=0;i<elSel.options.length;i++){
      if(key==elSel.options[i].value)
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
}
