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

function showLookup(tableName, grandParentName, parentName, openerFormName, codeFieldName, descFieldName, displayCode, appRoot) {
    var grandParentCode = null;
    var parentCode = null;   

    if(grandParentName!='') grandParentCode = document.getElementsByName(grandParentName)[0]; 
    if(parentName!='') parentCode = document.getElementsByName(parentName)[0];    


    codeField = document.getElementsByName(codeFieldName)[0]; 
    descField = document.getElementsByName(descFieldName)[0];    

    codeField.focus();
	
	//open the lookup URL
	var queryString = "?openerForm=" + openerFormName;
	queryString = queryString + "&codeName=" + codeFieldName;
	queryString = queryString + "&descName=" + descFieldName;
	queryString = queryString + "&tableName=" + tableName;
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
        
	var lookupURL = "/" + appRoot + "/QuatroLookup/LookupList.do" + queryString; 

	delaySearch =false;
	top.childWin = window.open(lookupURL,"_blank","resizable=yes,scrollbars=yes,status=yes,width=600,height=450,top=120, left=200");
	top.childWin.focus();
	
}

function selectMe(code, Desc, form_name, code_element_name, Desc_element_name) {
   self.close();
   var myexpr = "opener.document." + form_name + ".elements['" + code_element_name +"'].value='" + code +"'";
   eval(myexpr);
   myexpr = "opener.document." + form_name + ".elements['" + Desc_element_name +"'].value='" + Desc + "'";
   eval(myexpr);
}
