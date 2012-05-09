var HealthCardHandler = function(handler) {

	this.keyBuffer = "";
	this.prevHc = {};
	this.lastReadSuccess = false;
	this.keyBufferHandler = handler;

	jQuery(window).keypress((function (context) {
		return function(e) {
			var charReceived = String.fromCharCode(e.charCode);
			if (charReceived == "%")
				context.keyBuffer = "%";
			else if (e.charCode != 0 && e.charCode != 13 && context.keyBuffer.length > 0)
				context.keyBuffer += charReceived;
			else if (e.charCode == 32 && context.keyBuffer.length > 0)
				e.preventDefault();
			else if (context.keyBuffer.length > 0 && ((e.charCode == 0 && e.keyCode == 13) || e.charCode == 13)) {
				e.preventDefault();
				context.readKeyBuffer();
				context.keyBuffer = "";
			}
			
			if ((e.charCode == 32 || e.keyCode == 13 || e.charCode == 13) && context.keyBuffer.length > 0)
				e.preventDefault();
	
			e.stopPropagation();
		};
	})(this));
	
};

HealthCardHandler.prototype.readKeyBuffer = function() {
	if (this.keyBuffer.substring(0, 3) == "%E?") {
		this.lastReadSuccess = false;
		this.keyBufferHandler({ error: "INVALID" });
		return;
	}
	
	var hcParts = {};
	if (this.keyBuffer.substring(2,8) == "610054") { // Ontario
		hcParts["issuer"] = "ON";
		hcParts["hin"] = this.keyBuffer.substring(8, 18);
		var namePos = this.keyBuffer.indexOf("^") + 1;
		var endNamePos = this.keyBuffer.indexOf("^", namePos);
		hcParts["fullName"] = this.keyBuffer.substring(namePos, endNamePos);
		hcParts["lastName"] = hcParts["fullName"].split("/")[0];
		hcParts["firstName"] = hcParts["fullName"].split("/")[1];
		hcParts["sex"] = this.keyBuffer.substring(endNamePos + 8, endNamePos + 9);
		hcParts["dob"] = this.keyBuffer.substring(endNamePos + 9, endNamePos + 17);
		hcParts["hinExp"] = this.keyBuffer.substring(endNamePos + 1, endNamePos + 5) + hcParts["dob"].substring(6,8);
		hcParts["hinVer"] = this.keyBuffer.substring(endNamePos + 17, endNamePos + 19);
		hcParts["firstNameShort"] = this.keyBuffer.substring(endNamePos + 19, endNamePos + 24);
		hcParts["issueDate"] = this.keyBuffer.substring(endNamePos + 24, endNamePos + 30);
		hcParts["lang"] = this.keyBuffer.substring(endNamePos + 30, endNamePos + 32);
	} else {
		this.lastReadSuccess = false;
		this.keyBufferHandler({ error: "ISSUER" });
		return;
	}
	
	this.lastReadSucess = true;
	this.prevHc = hcParts;
	this.keyBufferHandler(hcParts);
}