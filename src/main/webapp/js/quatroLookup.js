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
function void1()
{
	alert("To developer: this should never be called, please return false in your click event handler");
	// this function do nothing but was used in the href attribute of a <a> tag, cause the hand be displayed when mouse over
}
function showLookup(tableId, grandParentName, parentName, openerFormName, codeFieldName, descFieldName, displayCode, appRoot) {
    if(readOnly == true) return false;
    
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
        
	var lookupURL = "/" + appRoot.replace("/", "") + "/Lookup/LookupList.do" + queryString; 

	delaySearch =false;
	top.childWin = window.open(lookupURL,"_blank","resizable=yes,scrollbars=yes,status=yes,width=600,height=450,top=120, left=200");
	top.childWin.focus();
	return false;
}
function showLookup2(id, tableId, grandParentName, parentName, openerFormName, codeFieldName, descFieldName, displayCode, appRoot){
	// lookup with parents
    if(readOnly == true) return false;
	var orgFld = document.getElementById(id);
	
	if(orgFld.value.length >0){
		showLookup(tableId, grandParentName, parentName, openerFormName, codeFieldName, descFieldName, displayCode, appRoot);
	}else{
		alert("Please select ORG first!");
	}
	return false; 
}


function selectMe(code, desc, form_name, code_element_name, desc_element_name) {
   if(desc_element_name!=""){  //for both lookup tag and html dropdown
      var codeElement = opener.document.getElementsByName(code_element_name)[0];
      var descElement = opener.document.getElementsByName(desc_element_name)[0];
      
      codeElement.value = code;
      descElement.value = replaceQuote(desc);
	  if(false) {
     	var myexpr = "opener.document." + form_name + ".elements['" + code_element_name +"'].value='" + code +"'";
    	 eval(myexpr);
	     myexpr = "opener.document." + form_name + ".elements['" + desc_element_name +"'].value='" + desc + "'";
    	 eval(myexpr);
      }
   }else{
     AddtoDropdown(code, replaceQuote(desc), code_element_name);
   }
   self.close();
}

//for lookup tag java code use
function clearLookupValue(form_name, code_element_name, desc_element_name) {
   if(readOnly == true) return false;

   var myexpr = "document." + form_name + ".elements['" + code_element_name +"'].value=''";
   eval(myexpr);
   myexpr = "document." + form_name + ".elements['" + desc_element_name +"'].value=''";
   eval(myexpr);
	return false;
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
function replaceQuote(str)
{
	return str.replace(/&#34;/g,"\"");
}
