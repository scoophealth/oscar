/*
********************************************************
Copyright @ WebOnWebOff.com, by D. Miller
You may reuse this script, on condition that: 
	- this copyright text is kept
	- report improvements/changes to contact(at)WebOnWebOff.com
www.WebOnWebOff.com
********************************************************
*/
ylib.namespace('ylib.util');

ylib.util.isTextChar = function(keyCode){
	if(isNaN(keyCode)) return false;
	switch(keyCode){
		case 10,13: //carriage return
			return true;
		case 127:	//DEL
			return false;
		default:
			if(keyCode>=0&&keyCode<=47) return false;
			if(keyCode>=91&&keyCode<=95) return false;
			if(keyCode>=112&&keyCode<=187) return false;
	}
	return true;
}

ylib.util.FindValueInList = function(sValue, sList){
	var tValue;
	sValue = sValue.replace(/^\s+|\s+$/, '');
	if(sList.indexOf(",")==-1){
		return (sList.replace(/^\s+|\s+$/, '') == sValue);
	}
	var sArr;	
	//find in list
	eval("sArr = [" + sList + "];");
	for(var i=0; i<sArr.length; i++){		
		tValue = sArr[i].toString().replace(/^\s+|\s+$/, '');
		if(tValue==sValue) return true;
	}
	return false;
}