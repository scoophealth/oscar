angular.module('oscarFilters', []).filter('ticklerLink', function() {
  return function(input) {
	  if(input != null && input.id != null) {
		  var url = "";
		  
		  if(input.tableName == 'CML') {
			  url = "../lab/CA/ON/CMLDisplay.jsp?segmentID=" + input.tableId;
		  } else if(input.tableName == 'MDS') {
			  url = "../oscarMDS/SegmentDisplay.jsp?segmentID=" + input.tableId;
		  } else if(input.tableName == 'HL7') {
			  url = "../lab/CA/ALL/labDisplay.jsp?segmentID=" + input.tableId;
		  } else if(input.tableName == 'DOC') {
			  url = "../dms/ManageDocument.do?method=display&doc_no=" + input.tableId;
		  } 
		  
		  return url;
	  }
    return input;
  };
});

angular.module('oscarFilters', []).filter('age', function() {
	  return function(input) {
		  if(input != null && input.years != null) {
			 var result = "";
			 
			 if(input.years < 1 && input.months < 1) {
				 return input.days + "d"
			 }
			 if(input.years < 2) {
				 return input.months + "m";
			 }
			 return input.years + " y"
		  }
	    return "";
	  };
	});
