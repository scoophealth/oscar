 /*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

function saveLinks(randNumber) {
    $('method_'+randNumber).onblur();
    $('route_'+randNumber).onblur();
    $('frequency_'+randNumber).onblur();
    $('minimum_'+randNumber).onblur();
    $('maximum_'+randNumber).onblur();
    $('duration_'+randNumber).onblur();
    $('durationUnit_'+randNumber).onblur();
}


function handleEnter(inField, ev){
    var charCode;
    if(ev && ev.which)
        charCode=ev.which;
    else if(window.event){
        ev=window.event;
        charCode=ev.keyCode;
    }
    var id=inField.id.split("_")[1];
    if(charCode==13)
        showHideSpecInst('siAutoComplete_'+id);
}

//has to be in here, not prescribe.jsp for it to work in IE 6/7 and probably 8.
function showHideSpecInst(elementId){
	alert(elementId);
    if( $(elementId).is(":visible") ){
    	$(elementId).hide( "fold", 1000 );
    }else{
    	$(elementId).show( "fold", 1000 );   
    }
}

function resetReRxDrugList(){
	var rand = Math.floor(Math.random()*10001);
    var url=ctx + "/oscarRx/deleteRx.do?parameterValue=clearReRxDrugList";
           var data = "rand="+rand;
           new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
            }});
}

function onPrint(cfgPage) {
    var docF = $('printFormDD');

    docF.action = "../form/createpdf?__title=Rx&__cfgfile=" + cfgPage + "&__template=a6blank";
    docF.target="_blank";
    docF.submit();
   return true;
}

function buildRoute() {

    pickRoute = "";
}



function popupRxSearchWindow(){
   var winX = (document.all)?window.screenLeft:window.screenX;
   var winY = (document.all)?window.screenTop:window.screenY;

   var top = winY+70;
   var left = winX+110;
   var url = "searchDrug.do?rx2=true&searchString="+$('searchString').value;
   popup2(600, 800, top, left, url, 'windowNameRxSearch<%=demoNo%>');

}


function popupRxReasonWindow(demographic,id){
   var winX = (document.all)?window.screenLeft:window.screenX;
   var winY = (document.all)?window.screenTop:window.screenY;

   var top = winY+70;
   var left = winX+110;
   var url = "SelectReason.jsp?demographicNo="+demographic+"&drugId="+id;
   popup2(575, 650, top, left, url, 'windowNameRxReason<%=demoNo%>');

}


var highlightMatch = function(full, snippet, matchindex) {
    return "<a title='"+full+"'>"+full.substring(0, matchindex) +
    "<span class=match>" +full.substr(matchindex, snippet.length) + "</span>" + full.substring(matchindex + snippet.length)+"</a>";
};

var highlightMatchInactiveMatchWord = function(full, snippet, matchindex) {
   //oscarLog(full+"--"+snippet+"--"+matchindex);
    return "<a title='"+full+"'>"+"<span class=matchInactive>"+full.substring(0, matchindex) +
    "<span class=match>" +full.substr(matchindex, snippet.length) +"</span>" + full.substring(matchindex + snippet.length)+"</span>"+"</a>";
};
var highlightMatchInactive = function(full, snippet, matchindex) {
   /* oscarLog(full+"--"+snippet+"--"+matchindex);
    oscarLog(" aa "+full.substring(0, matchindex) );
    oscarLog(" bb "+full.substr(matchindex, snippet.length) );
    oscarLog(" cc "+ full.substring(matchindex + snippet.length));*/
   /*return "<a title='"+full+"'>"+"<span class=matchInactive>"+full.substring(0, matchindex) +
    full.substr(matchindex, snippet.length) +full.substring(matchindex + snippet.length)+"</span>"+"</a>";*/
    return "<a title='"+full+"'>"+"<span class=matchInactive>"+full+"</span>"+"</a>";
};
var resultFormatter = function(oResultData, sQuery, sResultMatch) {
   //oscarLog("oResultData, sQuery, sResultMatch="+oResultData+"--"+sQuery+"--"+sResultMatch);
   //oscarLog("oResultData[0]="+oResultData[0]);
   //oscarLog("oResultData.name="+oResultData.name);
   //oscarLog("oResultData.name="+oResultData.id);
   var query = sQuery.toUpperCase();
   var drugName = oResultData[0];

   var mIndex = drugName.toUpperCase().indexOf(query);
   var display = '';

   if(mIndex > -1){
       display = highlightMatch(drugName,query,mIndex);
   }else{
       display = drugName;
   }
   return  display;
};

var resultFormatter2 = function(oResultData, sQuery, sResultMatch) {
   /*oscarLog("oResultData, sQuery, sResultMatch="+oResultData+"--"+sQuery+"--"+sResultMatch);
   oscarLog("oResultData[0]="+oResultData[0]);
   oscarLog("oResultData.name="+oResultData.name);
   oscarLog("oResultData.name="+oResultData.id);*/
   var query = sQuery.toUpperCase();
   var drugName = oResultData.name;
   var isInactive=oResultData.isInactive;
   //oscarLog("isInactive="+isInactive);

   var mIndex = drugName.toUpperCase().indexOf(query);
   var display = '';
   if(mIndex>-1 && (isInactive=='true'||isInactive==true)){ //match and inactive
       display=highlightMatchInactiveMatchWord(drugName,query,mIndex);
   }
   else if(mIndex > -1 && (isInactive=='false'||isInactive==false || isInactive==undefined || isInactive==null)){ //match and active
       display = highlightMatch(drugName,query,mIndex);
   }else if(mIndex<=-1 && (isInactive=='true'||isInactive==true)){//no match and inactive
       display=highlightMatchInactive(drugName,query,mIndex);
   }
   else{//active and no match
       display = drugName;
   }
   return  display;
};


addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable

    if (!document.getElementsByTagName) return;

    tbls = document.getElementsByTagName("table");

    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];

        if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    oscarLog('making '+table+' sortable');
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;
    oscarLog('Gets past here');

    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        var txt = ts_getInnerText(cell);
        cell.innerHTML = '<a href="#"  class="sortheader" '+
        'onclick="ts_resortTable(this, '+i+');return false;">' +
        txt+'<span class="sortarrow"></span></a>';
    }
}

function ts_getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;	//Not needed but it is faster
	var str = "";

	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += ts_getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function ts_resortTable(lnk,clid) {
    // get the span
    var span;
    for (var ci=0;ci<lnk.childNodes.length;ci++) {
        if (lnk.childNodes[ci].tagName && lnk.childNodes[ci].tagName.toLowerCase() == 'span') span = lnk.childNodes[ci];
    }
    var spantext = ts_getInnerText(span);
    var td = lnk.parentNode;
    var column = clid;
    var table = getParent(td,'TABLE');

    // Work out a type for the column
    if (table.rows.length <= 1) return;


    var itm = ts_getInnerText(table.rows[1].cells[column]).trim();
    sortfn = ts_sort_caseinsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^[Â£$]/)) sortfn = ts_sort_currency;
    if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    SORT_COLUMN_INDEX = column;
    var firstRow = new Array();
    var newRows = new Array();
    for (i=0;i<table.rows[0].length;i++) { firstRow[i] = table.rows[0][i]; }
    for (j=1;j<table.rows.length;j++) { newRows[j-1] = table.rows[j]; }

    newRows.sort(sortfn);

    if (span.getAttribute("sortdir") == 'down') {
        ARROW = '&nbsp;&nbsp;&uarr;';
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
        ARROW = '&nbsp;&nbsp;&darr;';
        span.setAttribute('sortdir','down');
    }

    // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    // don't do sortbottom rows
    for (i=0;i<newRows.length;i++) { if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);}
    // do sortbottom rows only
    for (i=0;i<newRows.length;i++) { if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);}

    // Delete any other arrows there may be showing
    var allspans = document.getElementsByTagName("span");
    for (var ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className == 'sortarrow') {
            if (getParent(allspans[ci],"table") == getParent(lnk,"table")) { // in the same table as us?
                allspans[ci].innerHTML = '';
            }
        }
    }

    span.innerHTML = ARROW;
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
		return el;
	else
		return getParent(el.parentNode, pTagName);
}
function ts_sort_date(a,b) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function ts_sort_currency(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
}

function ts_sort_numeric(a,b) {
    aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX]));
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function ts_sort_caseinsensitive(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function ts_sort_default(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}


function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
{
  if (elm.addEventListener){
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
    return r;
  } else {
    alert("Handler could not be removed");
  }
}
function checkFav(){
    //oscarLog("****** in checkFav");
    var usefav='<%=usefav%>';
    var favid='<%=favid%>';
    if(usefav=="true" && favid!=null && favid!='null'){
        //oscarLog("****** favid "+favid);
        useFav2(favid);
    }else{}
}

 //not used , represcribe a drug
function represcribeOnLoad(drugId){
    var data="drugId="+drugId + "&rand=" + Math.floor(Math.random()*10001);
    var url= ctx + "/oscarRx/rePrescribe2.do?method=saveReRxDrugIdToStash";
    new Ajax.Updater('rxText',url, {method:'get',parameters:data,evalScripts:true,insertion: Insertion.Bottom,
        onSuccess:function(transport){
        }});

}


function moveDrugDown(drugId,swapDrugId,demographicNo) {
	new Ajax.Request(ctx + '/oscarRx/reorderDrug.do?method=update&direction=down&drugId='+drugId + '&swapDrugId='+swapDrugId+'&demographicNo=' + demographicNo + "&rand="+ Math.floor(Math.random()*10001) , {
		  method: 'get',
		  onSuccess: function(transport) {
			callReplacementWebService("ListDrugs.jsp",'drugProfile');
        resetReRxDrugList();
        resetStash();
		  }
		});
}

function moveDrugUp(drugId,swapDrugId,demographicNo) {
	new Ajax.Request(ctx + '/oscarRx/reorderDrug.do?method=update&direction=up&drugId='+drugId  + '&swapDrugId='+swapDrugId+'&demographicNo=' + demographicNo +"&rand=" + Math.floor(Math.random()*10001), {
		  method: 'get',
		  onSuccess: function(transport) {
			  callReplacementWebService("ListDrugs.jsp",'drugProfile');
              resetReRxDrugList();
              resetStash();
		  }
		});
}

function showPreviousPrints(scriptNo) {
	popupWindow(720,700,'ShowPreviousPrints.jsp?scriptNo='+scriptNo,'ShowPreviousPrints')
}


/*<![CDATA[*/
var Lst;

function CngClass(obj){
	document.getElementById("selected_default").removeAttribute("style");
 if (Lst) Lst.className='';
 obj.className='selected';
 Lst=obj;
}

/*]]>*/

function toggleStartDateUnknown(rand) {
	var cb = document.getElementById('startDateUnknown_'+rand);
	var txt = document.getElementById('rxDate_'+rand);
	if(cb.checked) {

		txt.disabled=true;
		txt.value=new Date();
	} else {
		txt.disabled=false;
	}
}

function emptyWrittenDate(rand){
	var cb = document.getElementById('pastMed_'+rand);
	var txt = document.getElementById('writtenDate_'+rand);

	if(cb.checked){
		txt.value='0001-01-01';
		txt.disabled=true;
	}else{
		txt.disabled=false;

	}

}

//this is a SJHH specific feature
function completeMedRec() {
	 var ok = confirm("Are you sure you would like to mark the Med Rec as complete?");
	 if(ok) {
		 var url = ctx + "/oscarRx/completeMedRec.jsp?demographicNo=<%=bean.getDemographicNo()%>";
		 var data;
		 new Ajax.Request(url,{method: 'get',parameters:data,onSuccess:function(transport){
            alert('Completed.')
        }});
	 }
}



function changeLt(drugId){
    if (confirm('<bean:message key="oscarRx.Prescription.changeDrugLongTermConfirm" />')==true) {
           var data="ltDrugId="+drugId+"&rand="+Math.floor(Math.random()*10001);
           var url= ctx + "/oscarRx/WriteScript.do?parameterValue=changeToLongTerm";
           new Ajax.Request(url,{method: 'post',parameters:data,onSuccess:function(transport){
                   var json=transport.responseText.evalJSON();
                   if(json!=null && (json.success=='true'||json.success==true) ){
                        $("notLongTermDrug_"+drugId).innerHTML="*";
                        $("notLongTermDrug_"+drugId).setStyle({
                            textDecoration: 'none',
                            color: 'red'
                        });
                        $("notLongTermDrug_"+drugId).setAttribute("onclick","");
                        $("notLongTermDrug_"+drugId).setAttribute("href","");
                    }else{
                    }
               }});
       }
}
    function checkReRxLongTerm(){
        var url=window.location.href;
        var match=url.indexOf('ltm=true');
        if(match>-1){
            RePrescribeLongTerm();
        }
    }
    function changeContainerHeight(ele){
        var ss=$('searchString').value;
        ss=trim(ss);
        if(ss.length==0)
            $('autocomplete_choices').setStyle({height:'0%'});
        else
            $('autocomplete_choices').setStyle({height:'100%'});
    }
    function addInstruction(content,randomId){
        $('instructions_'+randomId).value=content;
        parseIntr($('instructions_'+randomId));
    }
    function addSpecialInstruction(content,randomId){
                if($('siAutoComplete_'+randomId).getStyle('display')=='none'){
                  Effect.BlindDown('siAutoComplete_'+randomId);
                }else{}
                $('siInput_'+randomId).value=content;
                $('siInput_'+randomId).setStyle({color:'black'});
   }
   function hideMedHistory(){
       mb.hide();
   }
   var modalBox=function(){
       this.show=function(randomId, displaySRC, H){
           if(!document.getElementById("xmaskframe")){
               var divFram=document.createElement('iframe');
               divFram.setAttribute("id","xmaskframe");
               divFram.setAttribute("name","xmaskframe");
               //divFram.setAttribute("src","displayMedHistory.jsp?randomId="+randomId);
               divFram.setAttribute("allowtransparency","false");
               document.body.appendChild(divFram);
               var divSty=document.getElementById("xmaskframe").style;
               divSty.position="fixed";
               divSty.top="0px";
               divSty.right="0px";
               divSty.width="390px"
               //divSty.border="solid";
               divSty.backgroundColor="#F5F5F5";
               divSty.zIndex="45";
               //divSty.cursor="move";
           }
           this.waitifrm=document.getElementById("xmaskframe");

           this.waitifrm.setAttribute("src",displaySRC+".jsp?randomId="+randomId);
           this.waitifrm.style.display="block";
           this.waitifrm.style.height=H;

           $("dragifm").appendChild(this.waitifrm);
           Effect.Appear('xmaskframe');
       };
        this.hide=function()
            {
                Effect.Fade('xmaskframe');

            };
    }
    var mb=new modalBox();
    function displayMedHistory(randomId){
           var data="randomId="+randomId;
           new Ajax.Request(ctx + "/oscarRx/WriteScript.do?parameterValue=listPreviousInstructions",
           {method: 'post',parameters:data,asynchronous:false,onSuccess:function(transport){
                 mb.show(randomId,'displayMedHistory', '200px');
                }});
    }

    function displayInstructions(randomId){
    	var data="randomId="+randomId;
    	mb.show(randomId,'displayInstructions', '600px');

	}

    function updateProperty(elementId){
         var randomId=elementId.split("_")[1];
         if(randomId!=null){
             var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateProperty";
             var data="";
             if(elementId.match("prnVal_")!=null)
                 data="elementId="+elementId+"&propertyValue="+$(elementId).value;
             else
                 data="elementId="+elementId+"&propertyValue="+$(elementId).innerHTML;
             data = data + "&rand="+Math.floor(Math.random()*10001);
             new Ajax.Request(url, {method: 'post',parameters:data});
         }
    }
    function lookNonEdittable(elementId){
        $(elementId).className='';
    }
    function lookEdittable(elementId){
        $(elementId).className='highlight';
    }
    function setPrn(randomId){
        var prnStr=$('prn_'+randomId).innerHTML;
        prnStr=prnStr.strip();
        var prnStyle=$('prn_'+randomId).getStyle('textDecoration');
        if(prnStr=='prn' || prnStr=='PRN'|| prnStr=='Prn'){
            if(prnStyle.match("line-through")!=null){
                $('prn_'+randomId).setStyle({textDecoration:'none'});
                $('prnVal_'+randomId).value=true;
            }else{
                $('prn_'+randomId).setStyle({textDecoration:'line-through'});
                $('prnVal_'+randomId).value=false;
            }
        }
    }
     function focusTo(elementId){
         $(elementId).contentEditable='true';
         $(elementId).focus();
         //IE 6/7 bug..will this call onfocus twice?? may need to do browser check.
		 document.getElementById(elementId).onfocus();

     }

     function updateSpecialInstruction(elementId){
         var randomId=elementId.split("_")[1];
         var url=ctx +  "/oscarRx/WriteScript.do?parameterValue=updateSpecialInstruction";
         var data="randomId="+randomId+"&specialInstruction="+$(elementId).value;
         data = data + "&rand="+Math.floor(Math.random()*10001);
         new Ajax.Request(url, {method: 'post',parameters:data});
     }

    function changeText(elementId){
        if($(elementId).value=='Enter Special Instruction'){
            $(elementId).value="";
            $(elementId).setStyle({color:'black'});
        }else if ($(elementId).value==''){
            $(elementId).value='Enter Special Instruction';
            $(elementId).setStyle({color:'gray'});
        }

    }
    function updateMoreLess(elementId){
        if($(elementId).innerHTML=='more')
            $(elementId).innerHTML='less';
        else
            $(elementId).innerHTML='more';
    }

    function changeDrugName(randomId,origDrugName){
            if (confirm('If you change the drug name and write your own drug, you will lose the following functionality:'
            + '\n  *  Known Dosage Forms / Routes'
            + '\n  *  Drug Allergy Information'
            + '\n  *  Drug-Drug Interaction Information'
            + '\n  *  Drug Information'
            + '\n\nAre you sure you wish to use this feature?')==true) {

            //call another function to bring up prescribe.jsp
            var url=ctx + "/oscarRx/WriteScript.do?parameterValue=normalDrugSetCustom";
            var customDrugName=$("drugName_"+randomId).getValue();
            var data="randomId="+randomId+"&customDrugName="+customDrugName;
            new Ajax.Updater('rxText',url,{method:'get',parameters:data,asynchronous:true,insertion: Insertion.Bottom,onSuccess:function(transport){
                    $('set_'+randomId).remove();

                }});
            
            if( MYDRUGREF_DS == "yes") {
                      callReplacementWebService("GetmyDrugrefInfo.do?method=view",'interactionsRxMyD');
            }
        }else{
            $("drugName_"+randomId).value=origDrugName;
        }
    }
    function resetStash(){
               var url=ctx +  "/oscarRx/deleteRx.do?parameterValue=clearStash";
               var data = "rand=" + Math.floor(Math.random()*10001);
               new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                            updateCurrentInteractions();
            }});
               $('rxText').innerHTML="";//make pending prescriptions disappear.
               $("searchString").focus();
    }
    function iterateStash(){
        var url=ctx +  "/oscarRx/WriteScript.do?parameterValue=iterateStash";
        var data="rand="+ Math.floor(Math.random()*10001);
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,
            insertion: Insertion.Bottom,onSuccess:function(transport){
                updateCurrentInteractions();
        }});

    }
    function rxPageSizeSelect(){
               var ran_number=Math.round(Math.random()*1000000);
               var url="GetRxPageSizeInfo.do?method=view";
               var params = "demographicNo=<%=demoNo%>&rand="+ran_number;  //hack to get around ie caching the page
               new Ajax.Request(url, {method: 'post',parameters:params});
    }

    function reprint2(scriptNo){
        var data="scriptNo="+scriptNo + "&rand=" + Math.floor(Math.random()*10001);
        var url= ctx +  "/oscarRx/rePrescribe2.do?method=reprint2";
       new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                popForm2(scriptNo);

            }});
        return false;
    }


    function deletePrescribe(randomId){
        var data="randomId="+randomId;
        var url=ctx +  "/oscarRx/rxStashDelete.do?parameterValue=deletePrescribe";
        new Ajax.Request(url, {method: 'get',parameters:data,onSuccess:function(transport){
                updateCurrentInteractions();
                if($('deleteOnCloseRxBox').value=='true'){
                    deleteRxOnCloseRxBox(randomId);
                }
        }});
    }

    function deleteRxOnCloseRxBox(randomId){

            var data="randomId="+randomId;
            var url=ctx +  "/oscarRx/deleteRx.do?parameterValue=DeleteRxOnCloseRxBox";
            new Ajax.Request(url, {method: 'get',parameters:data,onSuccess:function(transport){
                     var json=transport.responseText.evalJSON();
                     if(json!=null){
                             var id=json.drugId;
                             var rxDate="rxDate_"+ id;
                             var reRx="reRx_"+ id;
                             var del="del_"+ id;
                             var discont="discont_"+ id;
                             var prescrip="prescrip_"+id;
                             $(rxDate).style.textDecoration='line-through';
                             $(reRx).style.textDecoration='line-through';
                             $(del).style.textDecoration='line-through';
                             $(discont).style.textDecoration='line-through';
                             $(prescrip).style.textDecoration='line-through';
                    }
                }});

    }

    function ThemeViewer(){

       var xy = Position.page($('drugProfile'));
       var x = (xy[0]+200)+'px';
       var y = xy[1]+'px';
       var wid = ($('drugProfile').getWidth()-300)+'px';
       var styleStr= {left: x, top: y,width: wid};

       $('themeLegend').setStyle(styleStr);
       $('themeLegend').show();
    }

    function useFav2(favoriteId){
        var randomId=Math.round(Math.random()*1000000);
        var data="favoriteId="+favoriteId+"&randomId="+randomId;
        var url= ctx + "/oscarRx/useFavorite.do?parameterValue=useFav2";
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
    }
    function calculateRxData(randomId){
        var dummie=parseIntr($('instructions_'+randomId));
        if(dummie)
            updateQty($('quantity_'+randomId));
    }
   function Delete2(element){

        if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
             var id_str=(element.id).split("_");
             var id=id_str[1];
             //var id=element.id;
             var rxDate="rxDate_"+ id;
             var reRx="reRx_"+ id;
             var del="del_"+ id;
             var discont="discont_"+ id;
             var prescrip="prescrip_"+id;

             var url=ctx +  "/oscarRx/deleteRx.do?parameterValue=Delete2"  ;
             var data="deleteRxId="+element.id + "&rand=" +  Math.floor(Math.random()*10001);
            new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                  $(rxDate).style.textDecoration='line-through';
                  $(reRx).style.textDecoration='line-through';
                  $(del).style.textDecoration='line-through';
                  $(discont).style.textDecoration='line-through';
                  $(prescrip).style.textDecoration='line-through';
            }});
        }
        return false;
    }

   function checkAllergy(id,atcCode){
         var url=ctx +  "/oscarRx/getAllergyData.jsp"  ;
         var data="atcCode="+atcCode+"&id="+id +"&rand="+ Math.floor(Math.random()*10001);
         new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                 var json=transport.responseText.evalJSON();
                 if(json!=null&&json.DESCRIPTION!=null&&json.reaction!=null){
                      var str = "<font color='red'>Allergy:</font> "+ json.DESCRIPTION + " <font color='red'>Reaction:</font> "+json.reaction;
                      $('alleg_'+json.id).innerHTML = str;
                      document.getElementById('alleg_tbl_'+json.id).style.display='block';
                 }
            }});
   }
   function checkIfInactive(id,dinNumber){
        var url=ctx + "/oscarRx/getInactiveDate.jsp"  ;
         var data="din="+dinNumber+"&id="+id +"&rand=" +  Math.floor(Math.random()*10001);
         new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                 var json=transport.responseText.evalJSON();
                if(json!=null){
                    var str = "Inactive Drug Since: "+new Date(json.vec[0].time).toDateString();
                    $('inactive_'+json.id).innerHTML = str;
                }
            }});
   }


    function Discontinue(event,element){
       var id_str=(element.id).split("_");
       var id=id_str[1];
       var widVal = ($('drugProfile').getWidth()-300);
       var widStr=widVal+'px';
       var heightDrugProfile=$('discontinueUI').getHeight();
       var posx=0,posy=0;
       if(event.pageX||event.pageY){
           posx=event.pageX;
           posx=posx-widVal;
           posy=event.pageY-heightDrugProfile/2;
           posx = posx+'px';
           posy = posy+'px';
       }else if(event.clientX||event.clientY){
           posx = event.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
           posx=posx-widVal;
	   posy = event.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop-heightDrugProfile/2;
           posx = posx+'px';
           posy = posy+'px';
       }else{
           var xy = Position.page($('drugProfile'));
           posx = (xy[0]+200)+'px';
           if(xy[1]>=0)
               posy = xy[1]+'px';
           else
               posy=0+'px';
       }
       var styleStr= {left: posx, top: posy,width: widStr};

        var drugName = $('prescrip_'+id).innerHTML;
       $('discontinueUI').setStyle(styleStr);
       $('disDrug').innerHTML = drugName;
       $('discontinueUI').show();
       $('disDrugId').value=id;


    }

    function Discontinue2(id,reason,comment,drugSpecial){
        var url=ctx +  "/oscarRx/deleteRx.do?parameterValue=Discontinue"  ;
        var demoNo='<%=patient.getDemographicNo()%>';
        var data="drugId="+id+"&reason="+reason+"&comment="+comment+"&demoNo="+demoNo+"&drugSpecial="+drugSpecial+"&rand="+ Math.floor(Math.random()*10001);
            new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                  var json=transport.responseText.evalJSON();
                  $('discontinueUI').hide();
                  $('rxDate_'+json.id).style.textDecoration='line-through';
                  $('reRx_'+json.id).style.textDecoration='line-through';
                  $('del_'+json.id).style.textDecoration='line-through';
                  $('discont_'+json.id).innerHTML = json.reason;
                  $('prescrip_'+json.id).style.textDecoration='line-through';
            }});

    }

    function updateCurrentInteractions(){
        new Ajax.Request("GetmyDrugrefInfo.do?method=findInteractingDrugList&rand="+ Math.floor(Math.random()*10001), {method:'get',onSuccess:function(transport){
                            new Ajax.Request("UpdateInteractingDrugs.jsp?rand="+ Math.floor(Math.random()*10001), {method:'get',onSuccess:function(transport){
                                            var str=transport.responseText;
                                            str=str.replace('<script type="text/javascript">','');
                                            str=str.replace(/<\/script>/,'');
                                            eval(str);
                                            if( MYDRUGREF_DS == "yes") {
                                 
                                              callReplacementWebService("GetmyDrugrefInfo.do?method=view&rand="+  Math.floor(Math.random()*10001),'interactionsRxMyD');
                                            }
                                        }});
                            }});
    }
//represcribe long term meds
    function RePrescribeLongTerm(){
       var demoNo='<%=patient.getDemographicNo()%>';
        var data="demoNo="+demoNo+"&showall=<%=showall%>&rand=" +  Math.floor(Math.random()*10001);
        var url= ctx +  "/oscarRx/rePrescribe2.do?method=repcbAllLongTerm";
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,insertion: Insertion.Bottom,onSuccess:function(transport){
                            updateCurrentInteractions();
            }});
        return false;
    }

function customNoteWarning(){
    if (confirm('This feature will allow you to manually enter a prescription.'
	+ '\nWarning: you will lose the following functionality:'
        + '\n  *  Quantity and Repeats'
	+ '\n  *  Known Dosage Forms / Routes'
	+ '\n  *  Drug Allergy Information'
	+ '\n  *  Drug-Drug Interaction Information'
	+ '\n  *  Drug Information'
	+ '\n\nAre you sure you wish to use this feature?')==true) {
        var randomId=Math.round(Math.random()*1000000);
        var url=ctx +  "/oscarRx/WriteScript.do?parameterValue=newCustomNote";
        var data="randomId="+randomId;
        new Ajax.Updater('rxText',url,{method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
    }
}

function customWarning2(){
    if (confirm('This feature will allow you to manually enter a drug.'
	+ '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
	+ '\n  *  Known Dosage Forms / Routes'
	+ '\n  *  Drug Allergy Information'
	+ '\n  *  Drug-Drug Interaction Information'
	+ '\n  *  Drug Information'
	+ '\n\nAre you sure you wish to use this feature?')==true) {
	//call another function to bring up prescribe.jsp
        var randomId=Math.round(Math.random()*1000000);
        var url=ctx + "/oscarRx/WriteScript.do?parameterValue=newCustomDrug";
        var data="randomId="+randomId;
        new Ajax.Updater('rxText',url,{method:'get',parameters:data,asynchronous:true,evalScripts:true,
            insertion: Insertion.Bottom, onComplete:function(transport){
                updateQty($('quantity_'+randomId));
            }});

    }

}
function saveCustomName(element){
    var elemId=element.id;
    var ar=elemId.split("_");
    var rand=ar[1];
    var url=ctx + "/oscarRx/WriteScript.do?parameterValue=saveCustomName";
    var data="customName="+encodeURIComponent(element.value)+"&randomId="+rand;
    var instruction="instructions_"+rand;
    var quantity="quantity_"+rand;
    var repeat="repeats_"+rand;
    new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){

            }});
}
function updateDeleteOnCloseRxBox(){
    $('deleteOnCloseRxBox').value='true';
}
function popForm2(scriptId){
        try{
            //oscarLog("popForm2 called");
            var url1=ctx + "/oscarRx/WriteScript.do?parameterValue=checkNoStashItem&rand="+ Math.floor(Math.random()*10001);
            var data="";
            var h=900;
            new Ajax.Request(url1, {method: 'get',parameters:data, onSuccess:function(transport){
                //output default instructions
                var json=transport.responseText.evalJSON();
                var n=json.NoStashItem;
                if(n>4){
                    h=h+(n-4)*100;
                }
                //oscarLog("h="+h+"--n="+n);
                var url;
                var json = jQuery("#Calcs").val();
                //oscarLog(json);
                if( json != null && json != "" ) {
                	
                	var pharmacy = JSON.parse(json);
                    
                    if( pharmacy != null ) {
                    	url= ctx +  "/oscarRx/ViewScript2.jsp?scriptId="+scriptId+"&pharmacyId="+pharmacy.id;
                    }
                    else {
                    	url= ctx +  "/oscarRx/ViewScript2.jsp?scriptId="+scriptId;
                    }	
                }
                else {
                	url=ctx +  "/oscarRx/ViewScript2.jsp?scriptId="+scriptId;
                }
                
                //oscarLog( "preview2 done");
                myLightWindow.activateWindow({
                    href: url,
                    width: 1000,
                    height: h
                });
                var editRxMsg='<bean:message key="oscarRx.Preview.EditRx"/>';
                $('lightwindow_title_bar_close_link').update(editRxMsg);
                $('lightwindow_title_bar_close_link').onclick=updateDeleteOnCloseRxBox;
            }});

        }
        catch(er){
            oscarLog(er);
        }
        //oscarLog("bottom of popForm");
    }

     function callTreatments(textId,id){
         var ele = $(textId);
         var url = "TreatmentMyD.jsp"
         var ran_number=Math.round(Math.random()*1000000);
         var params = "demographicNo=<%=demoNo%>&cond="+ele.value+"&rand="+ran_number;  //hack to get around ie caching the page
         new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
         $('treatmentsMyD').toggle();
     }

     function callAdditionWebService(url,id){
         var ran_number=Math.round(Math.random()*1000000);
         var params = "demographicNo=<%=demoNo%>&rand="+ran_number;  //hack to get around ie caching the page
         var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,insertion: Insertion.Bottom,evalScripts:true});
     }

     function callReplacementWebService(url,id){
              var ran_number=Math.round(Math.random()*1000000);
              var params = "demographicNo=<%=demoNo%>&rand="+ran_number;  //hack to get around ie caching the page
              var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,evalScripts:true});
         }
          //callReplacementWebService("InteractionDisplay.jsp",'interactionsRx');
     if( MYDRUGREF_DS == "yes") {
          callReplacementWebService("GetmyDrugrefInfo.do?method=view",'interactionsRxMyD');
          callReplacementWebService("ListDrugs.jsp",'drugProfile');
     }
          
/*
YAHOO.example.FnMultipleFields = function(){
    var url = ctx + "/oscarRx/searchDrug.do?method=jsonSearch";
    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ingoreStaleResponse'});
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
    // Define the schema of the delimited results
    oDS.responseSchema = {
        resultsList : "results",
        fields : ["name", "id","isInactive"]
    };
    // Enable caching
    oDS.maxCacheEntries =0;
    oDS.connXhrMode ="cancelStaleRequests";
    // Instantiate AutoComplete
    var oAC = new YAHOO.widget.AutoComplete("searchString", "autocomplete_choices", oDS);
    oAC.useShadow = true;
    oAC.resultTypeList = false;
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 50;
    oAC.formatResult = resultFormatter2;

    // Define an event handler to populate a hidden form field
    // when an item gets selected and populate the input field
    //var myHiddenField = YAHOO.util.Dom.get("myHidden");
    var myHandler = function(type, args) {
                    var arr = args[2];
                    var url = ctx + "/oscarRx/WriteScript.do?parameterValue=createNewRx"; //"prescribe.jsp";
                    var ran_number=Math.round(Math.random()*1000000);
                    var name=encodeURIComponent(arr.name);
                    var params = "demographicNo=<%=demoNo%>&drugId="+arr.id+"&text="+name+"&randomId="+ran_number;  //hack to get around ie caching the page
                   new Ajax.Updater('rxText',url, {method:'get',parameters:params,evalScripts:true,
                        insertion: Insertion.Bottom,onSuccess:function(transport){
                            updateCurrentInteractions();
                        }});

                    $('searchString').value = "";

   };
    oAC.itemSelectEvent.subscribe(myHandler);
    var collapseFn=function(){
        $('autocomplete_choices').hide();
    }
    oAC.containerCollapseEvent.subscribe(collapseFn);
    var expandFn=function(){
        $('autocomplete_choices').show();
    }
    oAC. dataRequestEvent.subscribe(expandFn);
    return {
        oDS: oDS,
        oAC: oAC
    };
}();
*/
function addFav(randomId,brandName){
    var favoriteName = window.prompt('Please enter a name for the Favorite:',  brandName);
    favoriteName=encodeURIComponent(favoriteName);
   if (favoriteName.length > 0){
        var url= ctx +  "/oscarRx/addFavorite2.do?parameterValue=addFav2";
        var data="randomId="+randomId+"&favoriteName="+favoriteName;
        new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
              window.location.href="SearchDrug3.jsp";
        }})
   }
}
    var resHidden2 = 0;//not used
    //not used
    function showHiddenRes(){
        var list = $$('div.hiddenResource');
        if(resHidden2 == 0){
          list.invoke('show');
          resHidden2 = 1;
          $('showHiddenResWord').update('hide');
          var url = "updateHiddenResources.jsp";
          var params="hiddenResources=&rand="+ Math.floor(Math.random()*10001);
          new Ajax.Request(url, {method: 'post',parameters:params});
        }else{
            $('showHiddenResWord').update('show');
            list.invoke('hide');
            resHidden2 = 0;
        }
    }
    var showOrHide=0;
    function showOrHideRes(hiddenRes){
        hiddenRes=hiddenRes.replace(/\{/g,"");
        hiddenRes=hiddenRes.replace(/\}/g,"");
        hiddenRes=hiddenRes.replace(/\s/g,"");
        var arr=hiddenRes.split(",");
        var numberOfHiddenResources=0;
        if(showOrHide==0){
            numberOfHiddenResources=0;
            for(var i=0;i<arr.length;i++){
                var element=arr[i];
                element=element.replace("mydrugref","");
                var elementArr=element.split("=");
                var resId=elementArr[0];
                var resUpdated=elementArr[1];
                var id=resId+"."+resUpdated;
                $(id).show();
                $('show_'+id).hide();
                $('showHideWord').update('hide');

                showOrHide=1;
                numberOfHiddenResources++;
            }
        }else{
            numberOfHiddenResources=0
            for(var i=0;i<arr.length;i++){
                var element=arr[i];
                element=element.replace("mydrugref","");
                var elementArr=element.split("=");
                var resId=elementArr[0];
                var resUpdated=elementArr[1];
                var id=resId+"."+resUpdated;
                oscarLog("id="+id);
                $(id).hide();
                $('show_'+id).show();
                $('showHideWord').update('show');
                showOrHide=0;
                numberOfHiddenResources++;
            }
        }
        $('showHideNumber').update(numberOfHiddenResources);

    }
   // var totalHiddenResources=0;


    var addTextView=0;
    function showAddText(randId){
        var addTextId="addText_"+randId;
        var addTextWordId="addTextWord_"+randId;
        if(addTextView==0){
            $(addTextId).show();
            addTextView=1;
            $(addTextWordId).update("less")
        }
        else{
            $(addTextId).hide();
            addTextView=0;
            $(addTextWordId).update("more")
        }
    }

    function ShowW(id,resourceId,updated){

        var params = "resId="+resourceId+"&updatedat="+updated
        var url='GetmyDrugrefInfo.do?method=setWarningToShow&rand='+ Math.floor(Math.random()*10001);
        new Ajax.Updater('showHideTotal',url,{method:'get',parameters:params,asynchronous:true,evalScripts:true,onSuccess:function(transport){

                $(id).show();
                $('show_'+id).hide();

            }});
    }

   function HideW(id,resourceId,updated){
        var url = 'GetmyDrugrefInfo.do?method=setWarningToHide';
        var ran_number=Math.round(Math.random()*1000000);
        var params = "resId="+resourceId+"&updatedat="+updated+"&rand="+ran_number;  //hack to get around ie caching the page
        //totalHiddenResources++;
        new Ajax.Updater('showHideTotal',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,onSuccess:function(transport){

                $(id).hide();
                $("show_"+id).show();

            }});
    }


function setSearchedDrug(drugId,name){

    var url = ctx +  "/oscarRx/WriteScript.do?parameterValue=createNewRx";
    var ran_number=Math.round(Math.random()*1000000);
    name=encodeURIComponent(name);
    var params = "demographicNo=<%=demoNo%>&drugId="+drugId+"&text="+name+"&randomId="+ran_number;
    new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom,onSuccess:function(transport){
                            updateCurrentInteractions();
            }});
    $('searchString').value = "";
}
var counterRx=0;
function updateReRxDrugId(elementId){
        var ar=elementId.split("_");
        var drugId=ar[1];
   if(drugId!=null && $(elementId).checked==true){
       var data="reRxDrugId="+drugId+"&action=addToReRxDrugIdList&rand="+ Math.floor(Math.random()*10001);
       var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
       new Ajax.Request(url, {method: 'get',parameters:data});
   }else if(drugId!=null){
       var data="reRxDrugId="+drugId+"&action=removeFromReRxDrugIdList&rand="+ Math.floor(Math.random()*10001);
       var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
       new Ajax.Request(url, {method: 'get',parameters:data});
   }
}


function removeReRxDrugId(drugId){
	 if(drugId!=null){
	   var data="reRxDrugId="+drugId+"&action=removeFromReRxDrugIdList&rand="+Math.floor(Math.random()*10001);
	   var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
	   new Ajax.Request(url, {method: 'get',parameters:data});
	}
	}

//represcribe a drug
function represcribe(element, toArchive){
  
    var elemId=element.id;
    var ar=elemId.split("_");
    var drugId=ar[1];
    if(drugId!=null && $("reRxCheckBox_"+drugId).checked==true){
    	        	
        var url= ctx +  "/oscarRx/rePrescribe2.do?method=represcribeMultiple&rand="+Math.floor(Math.random()*10001);
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:false,evalScripts:true,
            insertion: Insertion.Bottom,onSuccess:function(transport){
                updateCurrentInteractions();
            }});
    }else if(drugId!=null){
        var dataUpdateId="reRxDrugId="+toArchive+"&action=addToReRxDrugIdList&rand="+Math.floor(Math.random()*10001);
        var urlUpdateId=ctx +  "/oscarRx/WriteScript.do?parameterValue=updateReRxDrug";
        new Ajax.Request(urlUpdateId, {method: 'get',parameters:dataUpdateId});
                	
        var data="drugId="+drugId;
        var url= ctx +  "/oscarRx/rePrescribe2.do?method=represcribe2&rand="+Math.floor(Math.random()*10001);
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,evalScripts:true,
            insertion: Insertion.Bottom,onSuccess:function(transport){
                updateCurrentInteractions();
            }});

   }
}

function updateQty(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var data="randomId="+rand+"&action=updateQty&quantity="+element.value;
        var url= ctx + "/oscarRx/WriteScript.do?parameterValue=updateDrug";

        var rxMethod="rxMethod_"+rand;
        var rxRoute="rxRoute_"+rand;
        var rxFreq="rxFreq_"+rand;
        var rxDrugForm="rxDrugForm_"+rand;
        var rxDuration="rxDuration_"+rand;
        var rxDurationUnit="rxDurationUnit_"+rand;
        var rxAmt="rxAmount_"+rand;
        var str;
       // var rxString="rxString_"+rand;
       var methodStr="method_"+rand;
       var routeStr="route_"+rand;
       var frequencyStr="frequency_"+rand;
       var minimumStr="minimum_"+rand;
       var maximumStr="maximum_"+rand;
       var durationStr="duration_"+rand;
       var durationUnitStr="durationUnit_"+rand;
       var quantityStr="quantityStr_"+rand;
       var unitNameStr="unitName_"+rand;
       var prnStr="prn_"+rand;
       var prnVal="prnVal_"+rand;
        new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                $(methodStr).innerHTML=json.method;
                $(routeStr).innerHTML=json.route;
                $(frequencyStr).innerHTML=json.frequency;
                $(minimumStr).innerHTML=json.takeMin;
                $(maximumStr).innerHTML=json.takeMax;
                if(json.duration==null || json.duration=="null"){
                    $(durationStr).innerHTML='';
                }else{
                    $(durationStr).innerHTML=json.duration;
                }
                $(durationUnitStr).innerHTML=json.durationUnit;
                $(quantityStr).innerHTML=json.calQuantity;
                if(json.unitName!=null && json.unitName!="null" && json.unitName!="NULL" && json.unitName!="Null"){
                    $(unitNameStr).innerHTML=json.unitName;
                }else{
                    $(unitNameStr).innerHTML='';
                }
                if(json.prn){
                    $(prnStr).innerHTML="prn";
                    $(prnVal).value=true;
                } else{
                    $(prnStr).innerHTML="";$(prnVal).value=false;
                }

            }});
        return true;
}
    function parseIntr(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var instruction="instruction="+element.value+"&action=parseInstructions&randomId="+rand;
        var url= ctx + "/oscarRx/UpdateScript.do?parameterValue=updateDrug";
        var quantity="quantity_"+rand;
        var str;
       var methodStr="method_"+rand;
       var routeStr="route_"+rand;
       var frequencyStr="frequency_"+rand;
       var minimumStr="minimum_"+rand;
       var maximumStr="maximum_"+rand;
       var durationStr="duration_"+rand;
       var durationUnitStr="durationUnit_"+rand;
       var quantityStr="quantityStr_"+rand;
       var unitNameStr="unitName_"+rand;
       var prnStr="prn_"+rand;
       var prnVal="prnVal_"+rand;
        new Ajax.Request(url, {method: 'get',parameters:instruction,asynchronous:false, onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                if(json.policyViolations != null && json.policyViolations.length>0) {
                       for(var x=0;x<json.policyViolations.length;x++) {
                               alert(json.policyViolations[x]);
                       }
                       $("saveButton").disabled=true;
                       $("saveOnlyButton").disabled=true;
                } else {
                       $("saveButton").disabled=false;
                       $("saveOnlyButton").disabled=false;
                }

                $(methodStr).innerHTML=json.method;
                $(routeStr).innerHTML=json.route;
                $(frequencyStr).innerHTML=json.frequency;
                $(minimumStr).innerHTML=json.takeMin;
                $(maximumStr).innerHTML=json.takeMax;
                if(json.duration==null || json.duration=="null"){
                    $(durationStr).innerHTML='';
                }else{
                    $(durationStr).innerHTML=json.duration;
                }
                $(durationUnitStr).innerHTML=json.durationUnit;
                $(quantityStr).innerHTML=json.calQuantity;
                if(json.unitName!=null && json.unitName!="null" && json.unitName!="NULL" && json.unitName!="Null"){
                    $(unitNameStr).innerHTML=json.unitName;
                }else{
                    $(unitNameStr).innerHTML='';
                }
                if(json.prn){
                    $(prnStr).innerHTML="prn";$(prnVal).value=true;
                } else{
                    $(prnStr).innerHTML="";$(prnVal).value=false;
                }
            if($(unitNameStr).innerHTML!='')
                $(quantity).value=$(quantityStr).innerHTML+" "+$(unitNameStr).innerHTML;
            else
                $(quantity).value=$(quantityStr).innerHTML;
            }});
        return true;
    }

    function addLuCode(eleId,luCode){
        $(eleId).value = $(eleId).value +" LU Code: "+luCode;
    }

         function getRenalDosingInformation(divId,atcCode){
               var url = "RenalDosing.jsp";
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=demoNo%>&atcCode="+atcCode+"&divId="+divId+"&rand="+ran_number;
               new Ajax.Updater(divId,url, {method:'get',parameters:params,insertion: Insertion.Bottom,asynchronous:true});
         }
         function getLUC(divId,randomId,din){
             var url="LimitedUseCode.jsp";
             var params="randomId="+randomId+"&din="+din;
             new Ajax.Updater(divId,url,{method:'get',parameters:params,insertion:Insertion.Bottom,asynchronous:true});
         }

      function validateRxDate() {
          	var rx=true;
          	jQuery('input[name^="rxDate_"]').each(function(){
          		var strRx  = jQuery(this).val();

          		if(!checkAndValidateDate(strRx,null)) {
          			jQuery(this).focus();
          			rx=false;
          			return false;
          		}

          	});
          	return rx;
     }

    function validateWrittenDate() {
    	var x = true;
        jQuery('input[name^="writtenDate_"]').each(function(){
            var str1  = jQuery(this).val();

            var dt = str1.split("-");
            if (dt.length>3) {
            	jQuery(this).focus();
                alert('Written Date wrong format! Must be yyyy or yyyy-mm or yyyy-mm-dd');
                x = false;
                return;
            }

            var dt1=1, mon1=0, yr1=parseInt(dt[0],10);
            if (isNaN(yr1) || yr1<0 || yr1>9999) {
            	jQuery(this).focus();
                alert('Invalid Written Date! Please check the year');
                x = false;
                return;
            }
            if (dt.length>1) {
            	mon1 = parseInt(dt[1],10)-1;
            	if (isNaN(mon1) || mon1<0 || mon1>11) {
            		jQuery(this).focus();
            		alert('Invalid Written Date! Please check the month');
                    x = false;
                    return;
            	}
            }
            if (dt.length>2) {
            	dt1 = parseInt(dt[2],10);
                if (isNaN(dt1) || dt1<1 || dt1>31) {
                	jQuery(this).focus();
                    alert('Invalid Written Date! Please check the day');
                    x = false;
                    return;
                }
            }
            var date1 = new Date(yr1, mon1, dt1);
            var now  = new Date();

            if(date1 > now) {
            	jQuery(this).focus();
                alert('Written Date cannot be in the future. (' + str1 +')');
                x = false;
                return;
	        }
        });
        return x;
    }


    function updateSaveAllDrugsPrintContinue(){
    	if(!validateWrittenDate()) {
    		return false;
    	}
		if(!validateRxDate()) {
    		return false;
    	}
		
        var data=Form.serialize($('drugForm'));
        var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateSaveAllDrugs&rand="+ Math.floor(Math.random()*10001);
        new Ajax.Request(url,
        {method: 'post',postBody:data,asynchronous:false,
            onSuccess:function(transport){
            	
                callReplacementWebService("ListDrugs.jsp",'drugProfile');
                popForm2(null);
                resetReRxDrugList();
            }});
        return false;
    }
    
    function updateSaveAllDrugsContinue(){
    	if(!validateWrittenDate()) {
    		return false;
    	}
		if(!validateRxDate()) {
    		return false;
    	}
		
		
        var data=Form.serialize($('drugForm'));
        var url= ctx +  "/oscarRx/WriteScript.do?parameterValue=updateSaveAllDrugs&rand="+ Math.floor(Math.random()*10001);
        new Ajax.Request(url,
        {method: 'post',postBody:data,asynchronous:false,
            onSuccess:function(transport){
                callReplacementWebService("ListDrugs.jsp",'drugProfile');
                resetReRxDrugList();
                resetStash();
            }});
        return false;
    }

function checkEnterSendRx(){
        popupRxSearchWindow();
        return false;
}



$("searchString").focus();



  