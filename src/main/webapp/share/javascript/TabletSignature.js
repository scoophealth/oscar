/*
	Copyright (c) 2010 Alex Gibson, http://miniapps.co.uk/
	Released under MIT license, http://miniapps.co.uk/license/
	Modified 2010 by Noah Daley, indivica.com	
*/

var canvas; //canvas element.
var ctx; //drawing context.
var startX = 0; //starting X coordinate.
var startY = 0; //starting Y coordinate.

var moved = false; //has move occured.

var toolbarHeight = 41; //toolbar offset height (pixels).
var penSize = 2; //pen width (pixels).
var r = 0; //red
var g = 0; //green
var b = 0; //blue

var stage = 1; 

var bCX = 0;
var bCY = 0;
var bX = 0;
var bY = 0;

	
function init() {

	ctx = canvas.getContext('2d');
    	
	//set height and width to size of device window
	canvas.setAttribute("height", "100px");
	canvas.setAttribute("width", "500px");
    	
    ctx.fillStyle = 'rgb(255,255,255)';
	ctx.fillRect(0, 0, canvas.width, canvas.height);
	ctx.fill();
    	
	//add event listeners
	document.querySelector('#clear').addEventListener('click', clearCanvas, false);
	document.querySelector('#save').addEventListener('click', saveCanvas, false);
    	
	//finally, add touch and mouse event listener
	canvas.addEventListener('touchstart', onTouchStart, false);
	canvas.addEventListener('mousedown', onMouseDown, false);
	
}

function onTouchStart(e) {
	
	e.preventDefault();
		
	//we are dealing with a single touch event
	if (e.touches.length == 1) {	
		//set touch start defaults
		started = false;
		moved = false;
        	
		ctx.lineCap = 'round';
		ctx.lineJoin = 'round';
		ctx.lineWidth = penSize; 	
        
		//get touch start position
		var pos = jQuery(canvas).offset();
		
		startX = e.touches[0].pageX - pos.left;
		startY = e.touches[0].pageY - pos.top;
        	
		//add event listeners for touch move,end and canvel events
		canvas.addEventListener('touchmove', onTouchMove, false);
		canvas.addEventListener('touchend', onTouchEnd, false);
		canvas.addEventListener('touchcancel', onTouchCancel, false);
		
		OnSignEvent(false, true);
	}
}
	
function onTouchMove(e) {
	
	e.preventDefault();
		
	//value to flag that we have triggered a touch move event
	moved = true;
        
	//if we are dealing with a single touch event
	if (e.touches.length == 1) {
            
		var pos = jQuery(canvas).offset();
		
		//if this is the start of a series of touch move events
		if (stage == 1) {
            ctx.strokeStyle = 'rgb(' + r + ',' + g + ',' + b + ')';
			ctx.beginPath();
			ctx.moveTo(startX, startY);   
			stage = 2;             
		} else if (stage == 2) {
			bCX = e.touches[0].pageX - pos.left;
            bCY = e.touches[0].clientY - pos.top;
			stage = 3;
		} else if (stage == 3) {
            bX = e.touches[0].clientX - pos.left;
            bY = e.touches[0].clientY - pos.top;

            bCY = bCY - 0.9*(bY-bCY);
            bCX = bCX - 0.9*(bX-bCX);
            		
            ctx.quadraticCurveTo(bCX, bCY, bX, bY);		
            ctx.stroke();
            ctx.closePath();
            startX = e.touches[0].pageX - pos.left;
			startY = e.touches[0].clientY - pos.top;
            stage = 1;
		}
		
		OnSignEvent(false, true);
	}
}
	
function onTouchEnd(e) {
	
	e.preventDefault();
		
	//if we are dealing with a single touch event
	if (e.touches.length == 0) {
		
		//if a touch move event has not been triggered, we must be dealing with a tap
		if (!moved) {
			
			var pos = jQuery(canvas).offset();
			
			//in this case, we simply draw a shape in the spot the users finger leaves the screen
			ctx.beginPath();     
			ctx.strokeStyle = 'rgb(' + r + ',' + g + ',' + b + ')';
			ctx.beginPath();
			ctx.moveTo(e.changedTouches[0].pageX - pos.left, e.changedTouches[0].pageY - pos.top);
			ctx.lineTo(e.changedTouches[0].pageX - pos.left, e.changedTouches[0].pageY - pos.top);
			ctx.stroke();
			ctx.closePath();
			ctx.stroke();
			ctx.closePath();
		}
		
		if (stage != 1) {
			ctx.closePath();
			stage = 1;
		}
							
		//remove touchmove, touchend and touchcancel event listeners
		canvas.removeEventListener('touchmove', onTouchMove, false);
		canvas.removeEventListener('touchend', onTouchEnd, false);
		canvas.removeEventListener('touchcancel', onTouchCancel, false);
		
	}		
}
	
function onTouchCancel(e) {
					
	//remove touchmove, touchend and touchcancel event listeners
	canvas.removeEventListener('touchmove', onTouchMove, false);
	canvas.removeEventListener('touchend', onTouchEnd, false);
	canvas.removeEventListener('touchcancel', onTouchCancel, false);
		
}
	
function onMouseDown(e) {
	
	e.preventDefault();
	
	started = false;
	moved = false;
    	
	ctx.lineCap = 'round';
	ctx.lineJoin = 'round';
	ctx.lineWidth = penSize; 	
    
	var pos = jQuery(canvas).offset();
	
	startX = e.pageX - pos.left;
	startY = e.pageY - pos.top;
	
	canvas.addEventListener('mousemove', onMouseMove, false);
	canvas.addEventListener('mouseup', onMouseUp, false);
	canvas.addEventListener('mousecancel', onMouseCancel, false);
	
	OnSignEvent(false, true);
}
	
function onMouseMove(e) {
	e.preventDefault();
	
	moved = true;
	
	var pos = jQuery(canvas).offset();
	
	//if this is the start of a series of touch move events
	if (stage == 1) {
        ctx.strokeStyle = 'rgb(' + r + ',' + g + ',' + b + ')';
		ctx.beginPath();
		ctx.moveTo(startX, startY);   
		stage = 2;             
	} else if (stage == 2) {
		bCX = e.pageX - pos.left;
        bCY = e.clientY - pos.top;
		stage = 3;
	} else if (stage == 3) {
        bX = e.clientX - pos.left;
        bY = e.clientY - pos.top;

        bCY = bCY - 0.9*(bY-bCY);
        bCX = bCX - 0.9*(bX-bCX);
        		
        ctx.quadraticCurveTo(bCX, bCY, bX, bY);		
        ctx.stroke();
        ctx.closePath();
        startX = e.pageX - pos.left;
		startY = e.clientY - pos.top;
        stage = 1;
	}
	
}
	
function onMouseUp(e) {
		
	e.preventDefault();
			
	var pos = jQuery(canvas).offset();
	
	//in this case, we simply draw a shape in the spot the users finger leaves the screen
	ctx.beginPath();     
	ctx.strokeStyle = 'rgb(' + r + ',' + g + ',' + b + ')';
	ctx.beginPath();
	ctx.moveTo(e.pageX - pos.left, e.pageY - pos.top);
	ctx.lineTo(e.pageX - pos.left, e.pageY - pos.top);
	ctx.stroke();
	ctx.closePath();
	ctx.stroke();
	ctx.closePath();
	
	if (stage != 1) {
		ctx.closePath();
		stage = 1;
	}
		
	canvas.removeEventListener('mousemove', onMouseMove, false);
	canvas.removeEventListener('mouseup', onMouseUp, false);		
}

function onMouseCancel(e) {
	
	//remove touchmove, touchend and touchcancel event listeners
	canvas.removeEventListener('mousemove', onMouseMove, false);
	canvas.removeEventListener('mouseend', onMouseEnd, false);
	canvas.removeEventListener('mousecancel', onMouseCancel, false);
		
}

function clearCanvas() {
    
	ctx.fillStyle = 'rgb(255,255,255)';
	ctx.fillRect(0, 0, canvas.width, canvas.height);
	ctx.fill();
	
	OnSignEvent(false, false);
}
    
function saveCanvas() {
    var strDataURI = canvas.toDataURL("image/png");
    
    document.getElementById("signatureImage").value = strDataURI;
    // Used to submit the form using Ajax.
    if (_in_window) {
    	jQuery.ajax({
        	type: "POST",
    		url: contextPath + "/signature_pad/uploadSignature.jsp",
    		data: jQuery("#signatureForm").formSerialize(),
    		success: function(data) {    
    			var savedId = jQuery(jQuery(data.trim())[0]).val();
    			OnSignEvent(true, false, savedId);
    		}
    	});
    	return false;
    }
    else {
    	document.getElementById("signatureForm").submit();
    }
    
}


//function that runs once the document has loaded
function loaded() {
	
	//prevent default scrolling on document window
	document.addEventListener('touchmove', function(e) {
		e.preventDefault()
	}, false);
    
	canvas = document.querySelector('canvas');
    
	//if the browser supports canvas context
	if (canvas.getContext) {
        
		//initialize the app
		init();
        
	}
	//else alert the user and do nothing more
	else {
		alert('Your browser does not support Canvas 2D drawing, sorry!');
	}
	
	if (_in_window) { 
		if (parent.signatureHandler) { addSignatureListener(parent, parent.signatureHandler); }
	}
}

var caller = null, listener = null;

// Add a listener for signature events with parameters (save, dirty) 
// NOTE: Only supports one listener at a time.
function addSignatureListener(element,listener) {
	caller = element;
	this.listener = listener; 
}

function OnSignEvent(save,dirty,savedId) {
	document.getElementById("save").style.display = dirty ? "inline" : "none";
	document.getElementById("clear").style.display = dirty || save ? "inline" : "none";
	document.getElementById("signMessage").style.display = !dirty && !save ? "inline" : "none";
	if (listener != null) {
		listener({ target: caller, 
			       isSave: save, 
			       isDirty: dirty,
			       requestIdKey: requestIdKey,
			       previewImageUrl: previewImageUrl,
			       storedImageUrl: storedImageUrl + savedId
		});
	}
}

window.addEventListener("load", loaded, true);