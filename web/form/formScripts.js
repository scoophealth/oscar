  
    function reset() {
        document.forms[0].target = "apptProviderSearch";
        document.forms[0].action = action ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "../form/createpdf?__title=&__cfgfile=&__cfgfile=&__template=";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";                
        
        var ret = is1CheckboxChecked(0, choiceFormat) && allAreNumeric(0, allNumericField) && areInRange(0, allMatch);                       

        if(ret==true) {                        
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true) {
            window.close();
        }
        return(false);
    }

    function onSaveExit() {
        document.forms[0].submit.value="exit";        
        var ret = is1CheckboxChecked(0, choiceFormat) && allAreNumeric(0, allNumericField) && areInRange(0, allMatch);                       
        if(ret == true) {            
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }
    function popupFixedPage(vheight,vwidth,varpage) { 
       var page = "" + varpage;
       windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
       var popup=window.open(page, "planner", windowprop);
    }


    /*
        nbcheckboxes is an array list which stores the start and end element nb of each question
        need to include css class checkboxError
    */
    function is1CheckboxChecked(formNb, nbcheckboxes){        
        var isValid=true;
        if(nbcheckboxes!=null){
            for(var i=0; i < nbcheckboxes.length; i=i+2)
            {                        
                if (numCheckboxChecked(formNb,nbcheckboxes[i],nbcheckboxes[i+1])>1){                
                    isValid=false;              
                    for(var j=nbcheckboxes[i]; j<=nbcheckboxes[i+1]; j++){
                        if (document.forms[formNb].elements[j].checked==true)
                            document.forms[formNb].elements[j].className = 'checkboxError';                        
                    }
                }
            }
            if(isValid==false)
                alert("Please select one item only for each question");        
        }
        return isValid;
    }
               
    //Need to include css class checkbox in the jsp file
    function numCheckboxChecked(formNb, startElement, endElements){
        var numCheck = 0;

        for(var element = startElement; element <= endElements; element++){            
            document.forms[formNb].elements[element].className = 'checkbox';
            //alert("element: " + element + " is " + document.forms[formNb].elements[element].checked);
            if (document.forms[formNb].elements[element].checked == true){                
                numCheck++;
            }
        }                
        return numCheck;
    }

    function isNumeric(s){        
        if(s!=null){
            for(var i=0; i<s.length; ++i){
                if("0123456789.".indexOf(s.charAt(i))<0){
                    return false;
                }
            }
        }
        return true;

    }

    function allAreNumeric(formNb,allNumericField){
        var isValid = true;
        var needAlert = false;
        if(allNumericField!=null){
            for(var i=0; i<allNumericField.length; i++){
                document.forms[formNb].elements[allNumericField[i]].style.backgroundColor = 'white';  
                isValid = isNumeric(document.forms[formNb].elements[allNumericField[i]].value);
                if(isValid == false){
                    document.forms[formNb].elements[allNumericField[i]].style.backgroundColor = 'red';                    
                    needAlert = true;
                }                    
            }
            if(needAlert == true){
                alert("Only numeric value is valid in the highlighted field(s)");    
                return false;
            }
        }
        return true; 
    }

    function oneFieldIsNumeric(formNb,id){
        var isValid = true;
        isValid = isNumeric(document.forms[formNb].elements[id].value);
        document.forms[formNb].elements[id].style.backgroundColor = 'white'; 
        if(isValid == false){
            document.forms[formNb].elements[id].style.backgroundColor = 'red'; 
            alert("Only numeric value is valid in the highlighted field(s)");  
            return false;
        }
        return true;
    }

    function maxLength(formNb, id){
        var input = document.forms[formNb].elements[id].value;
        if((input.length)<=document.forms[formNb].elements[id].maxLength)
            return true;
        return false;
    }

    function allMaxLength(formNb, allId){
        var isValid = true;
        for(var i=0; i<allId.length; i++){
            document.forms[formNb].elements[allId[i]].style.backgroundColor='white';
            if (!maxLength(formNb, allId[i])){
                isValid = false;
                document.forms[formNb].elements[allId[i]].style.backgroundColor='red';
                alert("The maximum length of the highlighted field is " + document.forms[formNb].elements[id].maxLength);
            }
        }
        
        return isValid;                   
    }

    //the string s can only include character shown in match
    function isMatched(s, match){
        if(s!=null){
            for(var i=0; i<s.length; ++i){
                if(match.indexOf(s.charAt(i))<0)
                    return false;                
            }
        }
        return true;
    }

    function areInRange(formNb, allId){
        var isValid = true;
        if (allId !=null){
            for(var i = 0; i<allId.length; i++){
                var lower = allId[i][0];  
                var upper = allId[i][1]; 
                for(var j = 2; j<allId[i].length; j++){
                    document.forms[formNb].elements[allId[i][j]].style.backgroundColor='white';
                    var s = document.forms[formNb].elements[allId[i][j]].value;
                    if(s!=""){
                        if(isNumeric(s)==false || s<lower || s>upper){
                            isValid = false;
                            document.forms[formNb].elements[allId[i][j]].style.backgroundColor='red';
                        }
                    }
                }
            }
            if (isValid==false){
                alert("Invalid entry!");
                return false;
            } 
        }
        return true;
    }