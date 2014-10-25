function popupDrugOfChoice(vheight,vwidth,varpage) { //open a new popup window
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
    var popup=window.open(varpage, "oscarDoc", windowprops);
    if (popup != null) {
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
}

function goDOC(){
    if (document.RxSearchDrugForm.searchString.value.length == 0){
        popupDrugOfChoice(720,700,'http://doc.oscartools.org/')
    }else{
        //var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
        var docURL = "http://doc.oscartools.org/search?SearchableText="+document.RxSearchDrugForm.searchString.value;
        popupDrugOfChoice(720,700,docURL);
    }
}


function goOMD(){
    var docURL = "../common/OntarioMDRedirect.jsp?keyword=eCPS&params="+document.RxSearchDrugForm.searchString.value;
    popupDrugOfChoice(743,817,docURL);
}


function popupWindow(vheight,vwidth,varpage,varPageName) { //open a new popup window
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
    var popup=window.open(varpage,varPageName, windowprops);
    if (popup != null) {
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
}

function customWarning(){
    if (confirm('This feature will allow you to manually enter a drug.'
        + '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
        + '\n  *  Known Dosage Forms / Routes'
        + '\n  *  Drug Allergy Information'
        + '\n  *  Drug-Drug Interaction Information'
        + '\n  *  Drug Information'
        + '\n\nAre you sure you wish to use this feature?')==true) {
        window.location.href = 'chooseDrug.do?demographicNo=<%=response.encodeURL(Integer.toString(bean.getDemographicNo()))%>';
    }
}

function populatePharmacy(data) {
	
	var json = JSON.parse(data);
	
	document.getElementById("pharmacyName").innerHTML = json["name"];
    document.getElementById("pharmacyAddress").innerHTML = json["address"];
    document.getElementById("pharmacyCity").innerHTML = json["city"];
    document.getElementById("pharmacyPostalCode").innerHTML = json["postalCode"];
    document.getElementById("pharmacyProvince").innerHTML = json["province"];
    document.getElementById("pharmacyPhone1").innerHTML = json["phone1"];
    document.getElementById("pharmacyPhone2").innerHTML = json["phone2"];
    document.getElementById("pharmacyFax").innerHTML = json["fax"];
    document.getElementById("pharmacyNotes").innerHTML = json["notes"];
}

function showpic(picture){
    if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        var bal = document.getElementById("Calcs");

        var offsetTrail = document.getElementById("Calcs");
        var offsetLeft = 0;
        var offsetTop = 0;
        while (offsetTrail) {
            offsetLeft += offsetTrail.offsetLeft;
            offsetTop += offsetTrail.offsetTop;
            offsetTrail = offsetTrail.offsetParent;
        }
        if (navigator.userAgent.indexOf("Mac") != -1 &&
            typeof document.body.leftMargin != "undefined") {
            offsetLeft += document.body.leftMargin;
            offsetTop += document.body.topMargin;
        }
        
        targetElement.style.left = offsetLeft +bal.offsetWidth;
        targetElement.style.top = offsetTop;
        targetElement.style.visibility = 'visible';
    }
}

function hidepic(picture){
    if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        targetElement.style.visibility = 'hidden';
    }
}

function isEmpty(){
    if (document.RxSearchDrugForm.searchString.value.length == 0){
        alert("Search Field is Empty in rx.js");
        document.RxSearchDrugForm.searchString.focus();
        return false;
    }
    return true;
}


function processData() {

    if (isEmpty())
        buildRoute();
    else
        return false;

    return true;
}

//make sure form is in viewport
function load() {
    window.scrollTo(0,0);
}

function submitPending(stashId, action){
    var frm = document.forms.RxStashForm;
    frm.stashId.value = stashId;
    frm.action.value = action;
    frm.submit();
}



function ShowDrugInfo(GN){
    window.open("drugInfo.do?GN=" + escape(GN), "_blank",
        "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
}


function reprint(drug) {
    document.forms[0].drugList.value = drug;
    document.forms[0].method.value = "reprint";
    document.forms[0].submit();

}

function RePrescribe(){

    if(document.getElementsByName('chkRePrescribe')!=null){
        var checks = document.getElementsByName('chkRePrescribe');
        var s='';
        var i;

        for(i=0; i<checks.length; i++){
            if(checks[i].checked==true){
                s += checks[i].getAttribute("drugId") + ',';
            }
        }

        if(s.length>1){
            s = s.substring(0, s.length - 1);

            document.forms[0].drugList.value = s;
            document.forms[0].method.value = "represcribe";
            document.forms[0].submit();
        }
    }
}

function Delete(){
    if(document.getElementsByName('chkDelete')!=null){
        var checks = document.getElementsByName('chkDelete');
        var s='';
        var i;

        for(i=0; i<checks.length; i++){
            if(checks[i].checked==true){
                s += checks[i].getAttribute("drugId") + ',';
            }
        }

        if(s.length>1){
            if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
                s = s.substring(0, s.length - 1);

                document.forms[1].drugList.value = s;
                document.forms[1].submit();
            }
        }
    }
}