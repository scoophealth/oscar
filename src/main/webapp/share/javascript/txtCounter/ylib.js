/*
********************************************************
Copyright @ WebOnWebOff.com, by D. Miller
You may reuse this script, on condition that: 
	- this copyright text is kept
	- report improvements/changes to contact(at)WebOnWebOff.com
www.WebOnWebOff.com
********************************************************
*/
var ylib = function(){
	return {
		util: {},
		widget: {},
		namespace: function(sNameSpace){
			if(!xStr(sNameSpace)) return null;
			var levels = sNameSpace.split('.');
			var thisNameSpace = ylib;
			
			for(var i=(levels[0]=='ylib'? 1 : 0); i<levels.length; i++){
				thisNameSpace[levels[i]] = thisNameSpace[levels[i]] || {};
				thisNameSpace = thisNameSpace[levels[i]];
			} 
		}
	};
}();
