/*
********************************************************
Copyright @ WebOnWebOff.com, by D. Miller
You may reuse this script, on condition that: 
	- this copyright text is kept
	- report improvements/changes to contact(at)WebOnWebOff.com
        - Added New Line tracking - Oscar Dev Team
www.WebOnWebOff.com
********************************************************
*/
ylib.namespace('ylib.widget');

ylib.widget.TextCounter=function(textT, numChars, numLines, textBefore, textAfter){	
	this.ready=false;
	this.ta=xGetElementById(textT); //text area/textbox	
	if(!isNaN(numChars)){
		this.lim=numChars;
	} else {
		this.lim=0;
	}
      	if(!isNaN(numLines)){
		this.maxLines=numLines;
	} else {
		this.maxLines=1;
	}

	
        this.tAfter  = xStr(textAfter) ? textAfter : '';
	this.tBefore = xStr(textBefore) ? textBefore : 'Chars left: ';
	this.ta.onchange=this.update;
	this.ta.onkeyup=this.update;
	this.ta.onkeydown=this.prevent;
	this.ta.onkeypress=this.prevent;
        this.ta.onblur=this.hide;
        this.ta.onfocus=this.show;
	//insert counter element
	var parent = xParent(this.ta, true);	
	var counter = document.createElement('span');
	//add counter	
            if(!parent || !counter){
		return;
	}
	counter.id=textT+'-Counter';
	xDisplay(counter,'none');
	counter.className='TextCounter-Counter';        
  
	xAppendChild(parent,counter);
	
	this.cntr=counter;         
	this.ready=true;
	this.ta.TextCounterObj=this;
	this.update.call(this.ta);	
}
ylib.widget.TextCounter.prototype.hide=function() {
    var obj = this.TextCounterObj;
    
    xDisplay(obj.cntr,'none');
}

ylib.widget.TextCounter.prototype.show=function() {
    var obj = this.TextCounterObj;
    
    xDisplay(obj.cntr,'block');
}

ylib.widget.TextCounter.prototype.update=function(){        
	var obj=this.TextCounterObj;	
	if(!obj || !obj.ready) return;
	
	var tVal=obj.ta.value;	
	var lenT=tVal.length;	
	if(lenT>obj.lim){
		obj.ta.value = tVal.substr(0,obj.lim);
		lenT=obj.lim;
	}

        var newlinepos = 0;
        var numlines = 1;  
        var deltalines = 0;
        var fraction = 0;
        var error = false;        
        var maxline = Math.floor(obj.lim/obj.maxLines);
        var linelength = 0;
        var endofline = 0;        

        //check for new lines in user input
        while( (newlinepos = obj.ta.value.indexOf("\n",newlinepos)) > -1 ) {
            ++numlines;

            //next we have to find how many lines current series of chars can be divided into
            endofline = obj.ta.value.indexOf("\n",newlinepos+1);
            endofline = endofline > -1 ? endofline : obj.ta.value.length;
            linelength = endofline - newlinepos;
            if( linelength > maxline ) {                
                deltalines = linelength/maxline;
                fraction = numlines + deltalines;
                numlines += Math.floor(deltalines);
                
                if( numlines > obj.maxLines ) {
                    newlinepos = tVal.length - Math.floor((fraction - obj.maxLines - 1) * maxline);                                                       
                }                   
            }

            if( numlines > obj.maxLines ) {
                obj.ta.value = tVal.substr(0,newlinepos);
                error = true;                
                break;
            }
            
            ++newlinepos;
        }
        
        if( error )
            obj.cntr.innerHTML="Max number lines " + obj.maxLines + " exceeded";
        else
            obj.cntr.innerHTML=obj.tBefore+(obj.lim-lenT)+obj.tAfter;
		
}
ylib.widget.TextCounter.prototype.prevent=function(event){

	var obj=this.TextCounterObj;
	if(!obj || !obj.ready) return;
	var T=obj.ta.value;	
	var lenT=T.length;
	var ev=new xEvent(event);

        var newlinepos = 0;
        var numlines = 1;        
        var error = false;        
        var maxline = Math.floor(obj.lim/obj.maxLines);
        var linelength = 0;
        var endofline = 0;  
        
        while( (newlinepos = obj.ta.value.indexOf("\n",newlinepos)) > -1 ) {
            ++numlines;

            endofline = obj.ta.value.indexOf("\n",newlinepos+1);
            endofline = endofline > -1 ? endofline : obj.ta.value.length;
            linelength = endofline - newlinepos;
            if( linelength > maxline )
                numlines += Math.floor(linelength/maxline);

            if( numlines > obj.maxLines ) {                
                error = true;                
                break;
            }
            
            ++newlinepos;
        }
				
	if(ylib.util.isTextChar(ev.keyCode)&&(lenT>=obj.lim||error)){
		return false;
	}
	
}