function popupPatient(height, width, url, windowName, docId) {
	  d = document.getElementById('demofind'+ docId).value; //demog  //attachedDemoNo
	  urlNew = url + d;
	
	  return popup2(height, width, 0, 0, urlNew, windowName);
}

function popupPatientTicklerPlus(height, width, url, windowName,docId) {
  d = document.getElementById('demofind'+ docId).value; //demog  //attachedDemoNo
  n = document.getElementById('demofindName' + docId).value;
  urlNew = url + "method=edit&tickler.demographic_webName=" + n + "&tickler.demographicNo=" +  d + "&docType=DOC&docId="+docId;
  	
  	  return popup2(height, width, 0, 0, urlNew, windowName);
}

function popupPatientTickler(height, width, url, windowName,docId) {
	  d = document.getElementById('demofind'+ docId).value; //demog  //attachedDemoNo
	  n = document.getElementById('demofindName' + docId).value;
	  urlNew = url + "demographic_no="+d+"&name="+n+"&chart_no=&bFirstDisp=false&messageID=null&remoteFacilityId=";
	  
	  	  return popup2(height, width, 0, 0, urlNew, windowName);
	}