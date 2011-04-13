
var selectedDemos=new Array();

var highlightMatch = function(full, snippet, matchindex) {
    return full.substring(0, matchindex) +
    "<span class='match'>" +
    full.substr(matchindex, snippet.length) +
    "</span>" +
    full.substring(matchindex + snippet.length);
};
var resultFormatter2 = function(oResultData, sQuery, sResultMatch) {
    //console.log(oResultData);
    var query = sQuery.toLowerCase(),
    fname = oResultData[0],
    dob = oResultData[1],
    status = oResultData[3],
    fnameMatchIndex = fname.toLowerCase().indexOf(query),
    displayfname= '';
    //oscarLog("in resultFormatter2");
    //oscarLog(oResultData);
    if(fnameMatchIndex > -1) {
        displayfname = highlightMatch(fname, query, fnameMatchIndex);
    //oscarLog("displayfname in if="+displayfname);
    }
    else {
        displayfname = fname;
    }
    return displayfname + " (" + dob+ ") - "+status ;

};

var resultFormatter = function(oResultData, sQuery, sResultMatch) {
   //console.log(oResultData);
    var query = sQuery.toLowerCase(),
    fname = oResultData[1],
    lname = oResultData[2],
    fnameMatchIndex = fname.toLowerCase().indexOf(query),
    lnameMatchIndex = lname.toLowerCase().indexOf(query),

    displayfname, displaylname ;

    if(fnameMatchIndex > -1) {
        displayfname = highlightMatch(fname, query, fnameMatchIndex);
    }
    else {
        displayfname = fname;
    }

    if(lnameMatchIndex > -1) {
        displaylname = highlightMatch(lname, query, lnameMatchIndex);
    }
    else {
        displaylname = lname;
    }

    return displayfname + " " + displaylname ;
        

};
var resultFormatter3 = function(oResultData, sQuery, sResultMatch) {
    //console.log(oResultData);
    var query = sQuery.toLowerCase(),
    fname = oResultData[1],
    lname = oResultData[2],
    fnameMatchIndex = fname.toLowerCase().indexOf(query),
    lnameMatchIndex = lname.toLowerCase().indexOf(query),

    displayfname, displaylname ;

    if(fnameMatchIndex > -1) {
        displayfname = highlightMatch(fname, query, fnameMatchIndex);
    }
    else {
        displayfname = fname;
    }

    if(lnameMatchIndex > -1) {
        displaylname = highlightMatch(lname, query, lnameMatchIndex);
    }
    else {
        displaylname = lname;
    }

    return  displaylname+","+displayfname;

};

function checkSave(elementId){
    var curVal=$('autocompletedemo'+elementId).value;
    var isCurValValid=false;
    for(var i=0;i<selectedDemos.length;i++){
        if(curVal==selectedDemos[i]){
            isCurValValid=true;
            break;
        }
    }
    if(isCurValValid)
        $('save'+elementId).enable();
    else
        $('save'+elementId).disable();
}
function removeProv(th){
    var ele = th.up();
    ele.remove();

}