function showReport(url){
//var url=str + "/PMmodule/Reports/quatroReportViewer.do";
	
top.childWin = window.open(url,"_blank","toolbar=yes,location=no,menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=650,height=400,top=50, left=50");
	top.childWin.focus();
}

function removeSel(str) {
    var elSel= document.getElementsByName(str)[0]; 
    if(elSel.selectedIndex>=0){
      elSel.remove(elSel.selectedIndex);
    }else{
      alert("Please select one item to remove.");
    } 
}
