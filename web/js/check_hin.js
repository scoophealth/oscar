function mod10CheckCalDig(dig) {
	var ret = dig*2 + "";
	if (ret.length==2) ret = eval(ret.charAt(0)*1+ret.charAt(1)*1);

	return ret;
}

function mod10Check(hinNum) {
	var typeInOK = false;
	var hChar = new Array();
	var sum = 0;
	for (i=0; i<hinNum.length; i++) {
		hChar[i] = hinNum.charAt(i);
	}

	for (i=0; i<hinNum.length; i=i+2) {
		hChar[i] = mod10CheckCalDig(hChar[i]);
	}

	for (i=0; i<hinNum.length-1; i++) {
		sum = eval(sum*1 + hChar[i]*1);
	}

	var calDigit = 10-(""+sum).charAt((""+sum).length-1) ;
	if (hChar[hinNum.length-1] == ( (""+calDigit).charAt((""+calDigit).length-1) )) typeInOK = true;

	return typeInOK;
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		  ch = typeIn.substring(i, i+1);
		  if (ch == "-") { i++; continue; }
		  if ((ch < "0") || (ch > "9") ) {
			  typeInOK = false;
			  break;
		  }
	    i++;
    }
	} else typeInOK = false;
	return typeInOK;
}

function isValidHin(hin, province) {
	if (province!="ON") return(true);
	if (hin.length==0) return(true);
	
	if (hin.length!=10) return(false);
	if (!checkTypeNum(hin)) return(false);
	
	return(mod10Check(hin));
}