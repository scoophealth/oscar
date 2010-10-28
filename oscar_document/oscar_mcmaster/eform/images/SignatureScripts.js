var pvcnv = document.getElementById("preview");
var pv = new jsGraphics(pvcnv);
var cnv = document.getElementById("SignCanvas");
var jg = new jsGraphics(cnv);
var cnvLeft = parseInt(cnv.style.left);
var cnvTop = parseInt(cnv.style.top);
var StrokeColor = "black";
var StrokeThickness = 2;
var x0 = 0;
var y0 = 0;
jg.setPrintable(true);
var SubmitData = new Array();
var DrawData  = new Array();
var TempData = new Array();
var MouseDown = false;
function SetMouseDown(){
	MouseDown = true;
}
function SetMouseUp(){
	MouseDown = false;
}
var DrawSwitch = false;
function SetDrawOn(){
	DrawSwitch = true;
}
function SetDrawOff(){
	DrawSwitch  = false;
}
var FreehandSwitch = true;
var DrawTool = "Freehand";
function RoundTo(n, d){
	//rounds n to the nearest d
	var i = Math.round(n/d) * d;
	return i;
}
function SetStart(){
	x0 = (mousex - cnvLeft);
	y0 = (mousey - cnvTop);
}
var Xposition = new Array();
var Yposition = new Array();
function GetXY(x,y){
var t = StrokeThickness;
var l = Xposition.length - 1;	//l = last position
var h = Math.sqrt(Math.pow((Xposition[l] - x),2)+Math.pow((Yposition[l] - y),2)) //calc hypotenuse
	if(Xposition.length<2){
		Xposition.push(x);
		Yposition.push(y);
	}
	else {
		if (h>t){
			Xposition.push(x);
			Yposition.push(y);
		}
	}
}
function ClearXY(){
	Xposition = [];
	Yposition = [];
}
function ArrToStr(Arr,s){
	//convert array values to string
	var Str = "";
	for (n = 0; (n < Arr.length); n++)
	 {
		if (n > 0)
		{
			Str += s; // each set of data separated by s
		}
		Str += Arr[n];
	}
 	return Str;
}
function StrToArr(Str,s){
	//converts string to an array
	var Arr  = Str.split(s);
	for (n=0;n<Arr.length;n++){
		Arr[n] = parseInt(Arr[n]);
	}
	return Arr;
}
function AddFreehand(canvas,x,y,StrokeColor){
		var X = Xposition;
		var Y = Yposition;
		jg.setColor(StrokeColor);
		jg.setStroke(StrokeThickness);
		if (X.length>1){
			var a = X.length - 2;
			var b = a + 1;
			var x1 = parseInt(X[a]);
			var y1 = parseInt(Y[a]);
			//var x2 = parseInt(X[b]);
			//var y2 = parseInt(Y[b]);
			jg.drawLine(x1,y1,x,y);
			jg.paint();
		}
}
function DrawFreehand(canvas,X,Y,StrokeColor){
		canvas.setColor(StrokeColor);
		canvas.setStroke(StrokeThickness);
		canvas.drawPolyline(X,Y);
		canvas.paint();
		//store parameters in an array
		var StrX = ArrToStr(X,':');
		var StrY = ArrToStr(Y,':');
		var Parameter = "Freehand" + "|" +  StrX + "|" + StrY + "|" + StrokeColor;
		DrawData.push(Parameter);
		document.getElementById("DrawData").value = DrawData;
}
function DrawMarker(){
	if(DrawSwitch){
		var x = parseInt(mousex - cnvLeft);
		var y = parseInt(mousey - cnvTop);
		if(FreehandSwitch){
			DrawFreehand(jg,Xposition,Yposition,StrokeColor);
			ClearXY();
		}
	}
}
function DrawPreview(){
	var x = parseInt(mousex-cnvLeft);
	var y = parseInt(mousey-cnvTop);
	if (MouseDown){
	if(FreehandSwitch){
			GetXY(x,y);
			AddFreehand(pv,x,y,StrokeColor);
		}
	}
}
function RedrawImage(RedrawParameter){
		var DrawingType = RedrawParameter[0];
		if(DrawingType == "Freehand"){
			var X = StrToArr(RedrawParameter[1], ':');
			var Y = StrToArr(RedrawParameter[2], ':');
			StrokeColor = RedrawParameter[3];
			DrawFreehand(jg,X,Y,StrokeColor);
		}
}
function Undo(){
	jg.clear();
	TempData = DrawData;
	DrawData = new Array();
	document.getElementById("DrawData").value = "";
	for (i=0; (i < (TempData.length - 1) ); i++){
		var Parameters = TempData[i].split("|");
		RedrawImage(Parameters);
	}
}
function RecallImage(){
	for (i=0; (i < TempData.length);i++){
		var Parameters = new Array();
		Parameters =  TempData[i].split("|");
		RedrawImage(Parameters);
	}
}
function Clear(){
	jg.clear();
	TempData = new Array();
	DrawData = new Array();
	SubmitData = new Array();
	document.getElementById("TempData").value = "";
	document.getElementById("DrawData").value = "";
	document.getElementById("SubmitData").value = "";
	Xposition = new Array();
	Yposition = new Array();
}
function ClearExceptSubmit(){
	jg.clear();
	TempData = new Array();
	DrawData = new Array();
	document.getElementById("TempData").value = "";
	document.getElementById("DrawData").value = "";
	Xposition = new Array();
	Yposition = new Array();
}
function SubmitImage(){
	EncodeData();
}
function EncodeData(){
	var packed = "";  // Initialize packed or we get the word 'undefined'
	//Converting image data in array into a string
	for (i = 0; (i < DrawData.length); i++){
		if (i > 0){
			packed += ","; // each set of data separated by comma
		}
		packed += escape(DrawData[i]); 	//'escape' encodes dataset into unicode
	}
	document.getElementById("SubmitData").value = packed;  //stores image data into hidden form field
}
function DecodeData(){
	var query = document.getElementById("SubmitData").value;
	var data = query.split(',');
	for (i = 0; (i < data.length); i++){
		data[i] = unescape(data[i]);
	}
	TempData = data;
	DrawData = new Array();
	document.getElementById("DrawData").value = document.getElementById("SubmitData").value;
	document.getElementById("SubmitData").value = "";
}
function ReloadImage(){
	DecodeData();
	RecallImage();
}