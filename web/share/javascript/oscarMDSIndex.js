/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of CommonLabResultData

 */


  function changeView(){
                                if($('summaryView').getStyle('display')=='none'){
                                     $('summaryView').show();
                                     $('readerViewTable').hide();

                                     $('documentCB').show();
                                     $('hl7CB').show();
                                     $('normalCB').show();
                                     $('abnormalCB').show();
                                     $('documentCB2').show();
                                     $('hl7CB2').show();
                                     $('normalCB2').show();
                                     $('abnormalCB2').show();
                                     var eles=document.getElementsByName('cbText');
                                     for(var i=0;i<eles.length;i++){
                                         var ele=eles[i];
                                         ele.show();
                                     }
                                }
                                else{
                                     $('summaryView').hide();
                                     $('readerViewTable').show();

                                     $('documentCB').hide();
                                     $('hl7CB').hide();
                                     $('normalCB').hide();
                                     $('abnormalCB').hide();
                                     $('documentCB2').hide();
                                     $('hl7CB2').hide();
                                     $('normalCB2').hide();
                                     $('abnormalCB2').hide();
                                     var eles=document.getElementsByName('cbText');
                                     for(var i=0;i<eles.length;i++){
                                         var ele=eles[i];
                                         ele.hide();
                                     }
                                 }


                            }

function popupStart(vheight,vwidth,varpage) {
    popupStart(vheight,vwidth,varpage,"helpwindow");
}

function popupStart(vheight,vwidth,varpage,windowname) {
        //console.log("in popupstart 4 args");
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    if(!windowname)
        windowname="helpwindow";
    //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}

function reportWindow(page,height,width) {
    //console.log(page);
    if(height && width){
        windowprops="height="+660+", width="+width+", location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0" ;
    }else{
        windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    }
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}


function submitFile(){
   aBoxIsChecked = false;
   submitLabs = true;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            if (document.reassignForm.ackStatus.value == "false"){
                aBoxIsChecked = confirm("The lab for "+document.reassignForm.patientName.value+" has not been attached to a demographic, would you like to file it anyways?");
            }else{
                aBoxIsChecked = true;
            }
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                if (document.reassignForm.ackStatus[i].value == "false"){
                    aBoxIsChecked = confirm("The lab for "+document.reassignForm.patientName[i].value+" has not been attached to a demographic, would you like to file it anyways?");
                    if(!aBoxIsChecked)
                        break;
                }else{
                    aBoxIsChecked = true;
                }
            }
        }
    }
    if (aBoxIsChecked) {
       document.reassignForm.action = '../oscarMDS/FileLabs.do';
       document.reassignForm.submit();
    }
}

function checkAll(formId){
   var f = document.getElementById(formId);
   var val = f.checkA.checked;
   for (i =0; i < f.flaggedLabs.length; i++){
      f.flaggedLabs[i].checked = val;
   }
}

function wrapUp() {
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_lab");
	setTimeout("window.close();",100);
    } else {
	window.close();
    }
}

       function showDocLab(childId,docNo,providerNo,searchProviderNo,status,demoName,showhide){//showhide is 0 = document currently hidden, 1=currently shown
                                //create child element in docViews
                                docNo=docNo.replace(' ','');//trim
                                var type=checkType(docNo);
                                //oscarLog('type'+type);
                                //var div=childId;

                                //var div=window.frames[0].document.getElementById(childId);
                                var div=$(childId);
                                //alert(div);
                                var url='';
                                if(type=='DOC')
                                    url="../dms/showDocument.jsp";
                                else if(type=='MDS')
                                    url="";
                                else if(type=='HL7')
                                    url="../lab/CA/ALL/labDisplayAjax.jsp";
                                else if(type=='CML')
                                    url="";
                                else
                                    url="";

                                        //oscarLog('url='+url);
                                        var data="segmentID="+docNo+"&providerNo="+providerNo+"&searchProviderNo="+searchProviderNo+"&status="+status+"&demoName="+demoName;
                                        //oscarLog('url='+url+'+-+ \n data='+data);
                                        new Ajax.Updater(div,url,{method:'get',parameters:data,insertion:Insertion.Bottom,evalScripts:true,onSuccess:function(transport){}});

                            }

                            function createNewElement(parent,child){
                                //oscarLog('11 create new leme');
                                var newdiv=document.createElement('div');
                                //oscarLog('22 after create new leme');
                                newdiv.setAttribute('id',child);
                               var parentdiv=$(parent);
                                parentdiv.appendChild(newdiv);
                                //oscarLog('55 after create new leme');
                            }

         function clearDocView(){
                            var docview=$('docViews');
                               //var docview=window.frames[0].document.getElementById('docViews');
                               docview.innerHTML='';
                            }
                                    function showhideSubCat(plus_minus,patientId){
                                if(plus_minus=='plus'){
                                    $('plus'+patientId).hide();
                                    $('minus'+patientId).show();
                                    $('labdoc'+patientId+'showSublist').show();
                                }else{
                                    $('minus'+patientId).hide();
                                    $('plus'+patientId).show();
                                    $('labdoc'+patientId+'showSublist').hide();
                                }
                            }
              function un_bold(ele){
                                //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                                if(currentBold==ele.id){
                                    ;
                                }else{
                                  if($(currentBold)!=null)
                                      $(currentBold).style.fontWeight='';
                                    ele.style.fontWeight='bold';
                                    currentBold=ele.id;
                                }
                                //oscarLog('currentbold='+currentBold+'---ele.id='+ele.id);
                            }
                function showPageNumber(page){
                                    var totalNoRow=$('totalNumberRow').value;
                                    var newStartIndex=number_of_row_per_page*(parseInt(page)-1);
                                    var newEndIndex=parseInt(newStartIndex)+19;
                                    var isLastPage=false;
                                    if(newEndIndex>totalNoRow){
                                        newEndIndex=totalNoRow;
                                        isLastPage=true;
                                    }
                                    //oscarLog("new start="+newStartIndex+";new end="+newEndIndex);
                                   for(var i=0;i<totalNoRow;i++){
                                       if($('row'+i) && parseInt(newStartIndex)<=i && i<=parseInt(newEndIndex)) {
                                           //oscarLog("show row-"+i);
                                           $('row'+i).show();
                                       }else if($('row'+i)){
                                           //oscarLog("hide row-"+i);
                                           $('row'+i).hide();
                                       }
                                   }
                               //update current page
                               $('currentPageNum').innerHTML=page;
                               if(page==1)
                               {
                                   $('msgPrevious').hide();
                               }else if(page>1){
                                   $('msgPrevious').show();
                               }
                               if(isLastPage)
                                   $('msgNext').hide();
                               else
                                   $('msgNext').show();
                           }
                           function showTypePageNumber(page,type){
                               var eles;
                               var numberPerPage=20;
                               if(type=='D'){
                                   eles=document.getElementsByName('scannedDoc');
                                   var length=eles.length;
                                   var startindex=(parseInt(page)-1)*numberPerPage;
                                   var endindex=startindex+numberPerPage-1;
                                   if(endindex>length-1){
                                       endindex=length-1;
                                   }
                                   //only display current page
                                   for(var i=startindex;i<endindex+1;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'table-row'});
                                   }
                                   //hide previous page
                                   for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all labs
                                   eles=document.getElementsByName('HL7lab');
                                   for(i=0;i<eles.length;i++){
                                        var ele=eles[i];
                                        ele.setStyle({display:'none'});
                                   }
                               }else if (type=='H'){
                                   eles=document.getElementsByName('HL7lab');
                                   var length=eles.length;
                                   var startindex=(parseInt(page)-1)*numberPerPage;
                                   var endindex=startindex+numberPerPage-1;
                                   if(endindex>length-1){
                                       endindex=length-1;
                                   }
                                   //only display current page
                                   for(var i=startindex;i<endindex+1;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'table-row'});
                                   }
                                   //hide previous page
                                   for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all labs
                                   eles=document.getElementsByName('scannedDoc');
                                   for(i=0;i<eles.length;i++){
                                        var ele=eles[i];
                                        ele.setStyle({display:'none'});
                                   }
                               }else if (type=='N'){
                                    var eles1=document.getElementsByClassName('NormalRes');
                                    var length=eles.length;
                                    var startindex=(parseInt(page)-1)*numberPerPage;
                                    var endindex=startindex+numberPerPage-1;
                                    if(endindex>length-1){
                                           endindex=length-1;
                                    }

                                    for(var i=startindex;i<endindex+1;i++){
                                        var ele=eles1[i];
                                        ele.setStyle({display:'table-row'});
                                    }
                                    //hide previous page
                                    for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all abnormals
                                    var eles2=document.getElementsByClassName('AbnormalRes');
                                    i=0;
                                    for(i=0;i<eles2.length;i++){
                                        var ele=eles2[i];
                                        ele.setStyle({display:'none'});
                                    }
                               }else if (type=='AB'){
                                    var eles1=document.getElementsByClassName('AbnormalRes');
                                    var length=eles.length;
                                    var startindex=(parseInt(page)-1)*numberPerPage;
                                    var endindex=startindex+numberPerPage-1;
                                    if(endindex>length-1){
                                           endindex=length-1;
                                    }
                                    for(var i=startindex;i<endindex+1;i++){
                                        var ele=eles1[i];
                                        ele.setStyle({display:'table-row'});
                                    }
                                    //hide previous page
                                    for(var i=0;i<startindex;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide later page
                                   for(var i=endindex;i<length;i++){
                                       var ele=eles[i];
                                       ele.setStyle({display:'none'});
                                   }
                                   //hide all normals
                                    var eles2=document.getElementsByClassName('NormalRes');
                                    for(var i=0;i<eles2.length;i++){
                                        var ele=eles2[i];
                                        ele.setStyle({display:'none'});
                                    }
                               }
                           }
                function setTotalRows(){
                               var ds=document.getElementsByName('scannedDoc');
                               var ls=document.getElementsByName('HL7lab');
                               for(var i=0;i<ds.length;i++){
                                   var ele=ds[i];
                                   total_rows.push(ele.id);
                               }
                               for(var i=0;i<ls.length;i++){
                                   var ele=ls[i];
                                   total_rows.push(ele.id);
                               }
                               total_rows=sortRowId(uniqueArray(total_rows));
                               current_category=new Array();
                                                        current_category[0]=document.getElementsByName('scannedDoc');
                                                        current_category[1]=document.getElementsByName('HL7lab');
                                                        current_category[2]=document.getElementsByClassName('NormalRes');
                                                        current_category[3]=document.getElementsByClassName('AbnormalRes');
                           }
                           function checkBox(){
                                                    //oscarLog("in checkBox");
                                                    var checkedArray=new Array();
                                                    if($('documentCB').checked==1){
                                                        checkedArray.push('document');
                                                    }
                                                    if($('hl7CB').checked==1){
                                                        checkedArray.push('hl7');
                                                    }
                                                    if($('normalCB').checked==1){
                                                        checkedArray.push('normal');
                                                    }
                                                    if($('abnormalCB').checked==1){
                                                        checkedArray.push('abnormal');
                                                    }
                                         if(checkedArray.length==0||checkedArray.length==4){
                                                        var endindex= number_of_row_per_page-1;
                                                        if(endindex>=total_rows.length)
                                                            endindex=total_rows.length-1;

                                                        //show all
                                                        for(var i=0;i<endindex+1;i++){
                                                            var id=total_rows[i];
                                                            if($(id)){
                                                                $(id).show();
                                                            }
                                                        }
                                                        for(var i=endindex+1;i<total_rows.length;i++){
                                                            var id=total_rows[i];
                                                            if($(id)){
                                                                $(id).hide();
                                                            }
                                                        }
                                                        current_numberofpages=Math.ceil(total_rows.length/number_of_row_per_page);
                                                        initializeNavigation();
                                                        current_category=new Array();
                                                        current_category[0]=document.getElementsByName('scannedDoc');
                                                        current_category[1]=document.getElementsByName('HL7lab');
                                                        current_category[2]=document.getElementsByClassName('NormalRes');
                                                        current_category[3]=document.getElementsByClassName('AbnormalRes');
                                           }
                                            else{
                                                        //oscarLog('checkedArray='+checkedArray);
                                                        var eles=new Array();
                                                    for(var i=0;i<checkedArray.length;i++){
                                                        var type=checkedArray[i];

                                                        if(type=='document'){
                                                            var docs=document.getElementsByName('scannedDoc');
                                                            eles.push(docs);
                                                        }
                                                        else if(type=='hl7'){
                                                            var labs=document.getElementsByName('HL7lab');
                                                            eles.push(labs);
                                                        }
                                                        else if(type=='normal'){
                                                            var normals=document.getElementsByClassName('NormalRes');
                                                            eles.push(normals);

                                                        }
                                                        else if(type=='abnormal'){
                                                            var abnormals=document.getElementsByClassName('AbnormalRes');
                                                            eles.push(abnormals);
                                                        }
                                                    }
                                                    current_category=eles;
                                                    displayCategoryPage(1);
                                                    initializeNavigation();
                                                }
                                            }

                                            function displayCategoryPage(page){
                                                //oscarLog('in displaycategorypage, page='+page);
                                                //write all row ids to an array
                                                var displayrowids=new Array();
                                                    for(var p=0;p<current_category.length;p++){
                                                        var elements=new Array();
                                                        elements=current_category[p];
                                                        //oscarLog("elements.lenght="+elements.length);
                                                        for(var j=0;j<elements.length;j++){
                                                            var e=elements[j];
                                                            var rowid=e.id;
                                                            displayrowids.push(rowid);
                                                        }
                                                    }
                                                    //make array unique
                                                    displayrowids=uniqueArray(displayrowids);
                                                    displayrowids=sortRowId(displayrowids);
                                                    //oscarLog('sort and unique displaywords='+displayrowids);

                                                    var numOfRows=displayrowids.length;
                                                    //oscarLog(numOfRows);
                                                    current_numberofpages=Math.ceil(numOfRows/number_of_row_per_page);
                                                    //oscarLog(current_numberofpages);
                                                    var startIndex=(parseInt(page)-1)*number_of_row_per_page;
                                                    var endIndex=startIndex+(number_of_row_per_page-1);
                                                    if(endIndex>displayrowids.length-1){
                                                        endIndex=displayrowids.length-1;
                                                    }
                                                    //set current displaying rows
                                                    current_rows=new Array();
                                                    for(var i=startIndex;i<endIndex+1;i++){
                                                        if($(displayrowids[i])){
                                                            current_rows.push(displayrowids[i]);
                                                        }
                                                    }
                                                    //loop through every thing,if it's in displayrowids, show it , if it's not hide it.
                                                    for(var i=0;i<total_rows.length;i++){
                                                        var rowid=total_rows[i];
                                                        if(a_contain_b(current_rows,rowid)){
                                                            $(rowid).show();
                                                        }else
                                                            $(rowid).hide();
                                                    }
                                            }

                                            function initializeNavigation(){
                                                   $('currentPageNum').innerHTML=1;
                                                    //update the page number shown and update previous and next words
                                                    if(current_numberofpages>1){
                                                        $('msgNext').show();
                                                        $('msgPrevious').hide();
                                                    }else if(current_numberofpages<1){
                                                        $('msgNext').hide();
                                                        $('msgPrevious').hide();
                                                    }else if(current_numberofpages==1){
                                                        $('msgNext').hide();
                                                        $('msgPrevious').hide();
                                                    }
                                                    //oscarLog("current_numberofpages "+current_numberofpages);
                                                    $('current_individual_pages').innerHTML="";
                                                   if(current_numberofpages>1){
                                                       for(var i=1;i<=current_numberofpages;i++){
                                                        $('current_individual_pages').innerHTML+='<a style="text-decoration:none;" href="javascript:void(0);" onclick="navigatePage('+i+')> [ '+i+' ] </a>';
                                                    }
                                                   }
                                            }
                                            function sortRowId(a){
                                                    var numArray=new Array();
                                                    //sort array
                                                    for(var i=0;i<a.length;i++){
                                                        var id=a[i];
                                                        var n=id.replace('row','');
                                                        numArray.push(parseInt(n));
                                                    }
                                                    numArray.sort(function(a,b){return a-b;});
                                                    a=new Array();
                                                    for(var i=0;i<numArray.length;i++){
                                                        a.push('row'+numArray[i]);
                                                    }
                                                    return a;
                                            }
                                            function a_contain_b(a,b){//a is an array, b maybe an element in a.
                                                for(var i=0;i<a.length;i++){
                                                    if(a[i]==b){
                                                        return true;
                                                    }
                                                }
                                                return false;
                                            }

                                            function uniqueArray(a){
                                                var r=new Array();
                                                o:for(var i=0,n=a.length;i<n;i++){
                                                    for(var x=0,y=r.length;x<y;x++){
                                                        if(r[x]==a[i]) continue o;
                                                    }
                                                    r[r.length]=a[i];
                                                }
                                                return r;
                                            }

                                            function navigatePage(p){
                                                var pagenum=parseInt($('currentPageNum').innerHTML);
                                                if(p=='Previous'){
                                                    displayCategoryPage(pagenum-1);
                                                    $('currentPageNum').innerHTML=pagenum-1
                                                }
                                                else if(p=='Next'){
                                                    displayCategoryPage(pagenum+1);
                                                    $('currentPageNum').innerHTML=pagenum+1
                                                }
                                                else if(parseInt(p)>0){
                                                    displayCategoryPage(parseInt(p));
                                                    $('currentPageNum').innerHTML=p;
                                                }
                                                changeNavigationBar();
                                            }
                                            function changeNavigationBar(){
                                                var pagenum=parseInt($('currentPageNum').innerHTML);
                                                if(current_numberofpages==1){
                                                    $('msgNext').hide();
                                                    $('msgPrevious').hide();
                                                }
                                                else if(current_numberofpages>1 && current_numberofpages==pagenum){
                                                    $('msgNext').hide();
                                                    $('msgPrevious').show();
                                                }
                                                else if(current_numberofpages>1 && pagenum==1){
                                                    $('msgNext').show();
                                                    $('msgPrevious').hide();
                                                }else if(pagenum<current_numberofpages && pagenum>1){
                                                    $('msgNext').show();
                                                    $('msgPrevious').show();
                                                }
                                            }
                                            function syncCB(ele){
                                                var id=ele.id;
                                                if(id=='documentCB'){
                                                    if(ele.checked==1)
                                                        $('documentCB2').checked=1;
                                                    else
                                                        $('documentCB2').checked=0;
                                                }
                                                else if(id=='documentCB2'){
                                                    if(ele.checked==1)
                                                        $('documentCB').checked=1;
                                                    else
                                                        $('documentCB').checked=0;
                                                }
                                                else if(id=='hl7CB'){
                                                    if(ele.checked==1)
                                                        $('hl7CB2').checked=1;
                                                    else
                                                        $('hl7CB2').checked=0;
                                                }
                                                else if(id=='hl7CB2'){
                                                    if(ele.checked==1)
                                                        $('hl7CB').checked=1;
                                                    else
                                                        $('hl7CB').checked=0;
                                                }
                                                else if(id=='normalCB'){
                                                    if(ele.checked==1)
                                                        $('normalCB2').checked=1;
                                                    else
                                                        $('normalCB2').checked=0;
                                                }
                                                else if(id=='normalCB2'){
                                                    if(ele.checked==1)
                                                        $('normalCB').checked=1;
                                                    else
                                                        $('normalCB').checked=0;
                                                }
                                                else if(id=='abnormalCB'){
                                                    if(ele.checked==1)
                                                        $('abnormalCB2').checked=1;
                                                    else
                                                        $('abnormalCB2').checked=0;
                                                }
                                                else if(id=='abnormalCB2'){
                                                    if(ele.checked==1)
                                                        $('abnormalCB').checked=1;
                                                    else
                                                        $('abnormalCB').checked=0;
                                                }
                                            }
function showAb_Normal(ab_normal,str){
                                    str=str.replace('[','');
                                    str=str.replace(']','');
                                    var ids=str.split(',');
                                    var childId;
                                if(ab_normal=='normal'){
                                    childId='normals';
                                }else if (ab_normal=='abnormal'){
                                    childId='abnormals';
                                }
                                //oscarLog(childId);
                                if(childId!=null && childId.length>0){
                                      clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<ids.length;i++){
                                            var docLabId=ids[i].replace(/\s/g,'');
                                            var ackStatus=getAckStatusFromDocLabId(docLabId);
                                            var patientId=getPatientIdFromDocLabId(docLabId);
                                            var patientName=getPatientNameFromPatientId(patientId);
                                            showDocLab(childId,docLabId,providerNo,searchProviderNo,ackStatus,patientName,ab_normal+'show');
                                        }
                                }
                           }

                                 function showSubType(patientId,subType){
                                    var labdocsArr=getLabDocFromPatientId(patientId);
                                    var childId='subType'+subType+patientId;
                                    if(labdocsArr.length>0){
                                        //if(toggleElement(childId));
                                     // else{
                                     clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<labdocsArr.length;i++){
                                            var labdoc=labdocsArr[i];
                                            labdoc=labdoc.replace(' ','');
                                            //oscarLog('check type input='+labdoc);
                                            var type=checkType(labdoc);
                                            var ackStatus=getAckStatusFromDocLabId(labdoc);
                                            var patientName=getPatientNameFromPatientId(patientId);
                                            //oscarLog("type="+type+"--subType="+subType);
                                            if(type==subType)
                                                showDocLab(childId,labdoc,providerNo,searchProviderNo,ackStatus,patientName,'subtype'+subType+patientId+'show');
                                            else;
                                        }
                                        //toggleMarker('subtype'+subType+patientId+'show');
                                    //}
                                  }
                            }
                            function getPatientNameFromPatientId(patientId){
                                var text2=patientIdNames;
                                var p2=new RegExp(patientId+"=\\w+,\\s*\\w+");
                                var patientName=null;
                                if(text2.match(p2)!=null&&(text2.match(p2)).length>0){
                                        var r2=(text2.match(p2))[0];
                                        patientName=r2.split("=")[1];
                                }
                                return patientName;
                            }
                            function getAckStatusFromDocLabId(docLabId){
                                var p3=new RegExp(docLabId+"=\\w");
                                var text3=docStatus;
                                var m=(text3.match(p3))[0];
                                var ackStatus=m.split("=")[1];
                                return ackStatus;
                            }
                            function showAllDocLabs(){
                                var patientids=patientIdStr;
                                var idsArr=patientids.split(',');
                                clearDocView();
                                for(var i=0;i<idsArr.length;i++){
                                    var id=idsArr[i];
                                    //oscarLog("ids in showalldoclabs="+id);
                                    if(id.length>0){
                                        showThisPatientDocs(id,true);

                                    }
                                }

                            }
                            function showCategory(cat){
                                //oscarLog('cat ='+cat);
                                var pattern=new RegExp(cat+"=\\[.*?\\]","g");
                                var text=typeDocLab;
                                var resultA=text.match(pattern);
                                var result;
                                if(resultA==null || resultA.length==0);
                                else
                                    result=resultA[0];
                                var r=result.split("=")[1];
                                 r=r.replace("[","");
                                 r=r.replace("]","");
                                 var sA=r.split(",");//array of doc ids belong to this category
                                 //oscarLog("sA="+sA);
                                 var childId="category"+cat;
                                 //if(toggleElement(childId));
                                // else{
                                clearDocView();
                                     createNewElement('docViews',childId);
                                     for(var i=0;i<sA.length;i++){
                                         var docLabId=sA[i];
                                         docLabId=docLabId.replace(/\s/g, "");
                                         //oscarLog("docLabId="+docLabId);
                                         var patientId=getPatientIdFromDocLabId(docLabId);
                                         //oscarLog("patientId="+patientId);
                                         var patientName=getPatientNameFromPatientId(patientId);
                                         var ackStatus=getAckStatusFromDocLabId(docLabId);
                                         //oscarLog("patientName="+patientName);
                                         //oscarLog("ackStatus="+ackStatus);

                                         if(patientName!=null) showDocLab(childId,docLabId,providerNo,searchProviderNo,ackStatus,patientName,cat+'show');
                                     }
                                    //toggleMarker(cat+'show');
                            }

                            function getPatientIdFromDocLabId(docLabId){
                                var pna=new RegExp("-1=\\[.*?"+docLabId+".*?\\]");
                                var p=new RegExp("[{\\s]\\d+=\\[.*?"+docLabId+".*?\\]",'g');
                                var text=patientDocs;
                                var rna=text.match(pna);
                                if(rna!=null && rna.length>0){
                                    return '-1';
                                }else{
                                        var r=text.match(p);
                                        if(r!=null && r.length>0){
                                            var s=r[0];
                                            s=(s.split("="))[0];
                                            return s;
                                        }
                                        return null;
                                }
                            }
                              function getLabDocFromPatientId(patientId){//return array of doc ids and lab ids from patient id.
                                var pattern=new RegExp(patientId+"=\\[.*?\\]","g");
                                var text=patientDocs;
                                //oscarLog(text);
                                var result=(text.match(pattern))[0];
                                //oscarLog(result);
                                result=result.replace(patientId+"=[","");
                                result=result.replace("]","");
                                result=result.replace(/\s/g,"");
                                //oscarLog(result);
                                var resultAr=result.split(",");
                                return resultAr;
                            }

                            function showThisPatientDocs(patientId,keepPrevious){
                                //oscarLog("patientId in show this patientdocs="+patientId);
                                var labDocsArr=getLabDocFromPatientId(patientId);
                                var patientName=getPatientNameFromPatientId(patientId);
                                if(patientName!=null&&patientName.length>0){
                                        //oscarLog(patientName);
                                        var childId='patient'+patientId;
                                      //if(toggleElement(childId));
                                      //else{
                                      if(keepPrevious);
                                      else clearDocView();
                                        createNewElement('docViews',childId);
                                        for(var i=0;i<labDocsArr.length;i++){
                                            var docId=labDocsArr[i].replace(' ', '');

                                            var ackStatus=getAckStatusFromDocLabId(docId);
                                            //oscarLog('childId='+childId+',docId='+docId+',ackStatus='+ackStatus);
                                            showDocLab(childId,docId,providerNo,searchProviderNo,ackStatus,patientName,'labdoc'+patientId+'show');
                                        }
                                }
                            }
                            function popupConsultation(segmentId) {
                            	  var page =contextpath+ '/oscarEncounter/ViewRequest.do?segmentId='+segmentId;
                            	  var windowprops = "height=960,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
                            	  var popup=window.open(page, msgConsReq, windowprops);
                            	  if (popup != null) {
                            	    if (popup.opener == null) {
                            	      popup.opener = self;
                            	    }
                            	  }
                           }

                           function checkType(docNo){
                                docNo=docNo.replace(' ','');
                                var docTypes=docType;
                                var s='[{\\s]'+docNo+'='+'\\w+';
                                var p= new RegExp(s,'g');
                                //oscarLog(docTypes+"-+-"+p);
                                if(docTypes.match(p)!=null && (docTypes.match(p)).length>0){
                                    var text=(docTypes.match(p))[0];
                                    //oscarLog('matched='+text);
                                    return text.split("=")[1];
                                }else
                                    return '';
                            }
                             function checkSelected() {
    aBoxIsChecked = false;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            aBoxIsChecked = true;
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                aBoxIsChecked = true;
            }
        }
    }
    if (aBoxIsChecked) {
        popupStart(300, 400, 'SelectProvider.jsp', 'providerselect');
    } else {
        alert(msgSelectOneLab);
    }
}