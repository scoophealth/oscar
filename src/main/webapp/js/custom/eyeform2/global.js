var X       = 10;
var pBSmall = 30;
var small   = 60;
var normal  = 166;
var medium  = 272;
var large   = 378;
var full    = 649;

var rowOne = ['cpp_currentHis','cpp_familyHis','cpp_diagnostics'];

function rowOneX(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",X);
	}	
}

function rowOneSmall(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",small);
	}	
}

function rowOneNormal(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",normal);
	}	
}

function rowOneLarge(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",large);
	}	
}

function rowOneFull(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",full);
	}	
}

function reset() {
	rowOneNormal();
}
