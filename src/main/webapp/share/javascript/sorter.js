function popup(vheight,vwidth,varpage,windowname) {
	if(!windowname)
		windowname="helpwindow";

	var page = varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
	var win=window.open(varpage, windowname, windowprops);
}


(function ($) {
// VERTICALLY ALIGN FUNCTION
$.fn.vAlign = function() {
	return this.each(function(i){
	var ah = $(this).height();
	var ph = $(this).parent().height();
	var mh = (ph - ah) / 2;
	$(this).css('margin-top', mh);
	});
};
})(jQuery);

function resizeUl() {
	var sum = 0;
	//$("#picker li").each(function() { sum += $(this).width(); } );
	//$("#picker").width(sum+3);
	
	sum = 0;
	$("#builder li").each(function() { sum += $(this).width(); } );
	if (sum == 0) $("#builder").width('99%');
	else $("#builder").width(sum+40);
}

var selected = [];
var selectedTop = [];
function clearPreviousSelection() {
	if ($(selected).length > 0)
		$(selected).removeClass("selected");
	selected = [];
}

function clearPreviousTopSelection() {
	if ($(selectedTop).length > 0)
		$(selectedTop).removeClass("selected");
	selectedTop = [];
}

function _rotater(e) {
	if ($(e).parent().find("img.page").length > 0)
		$(e).parent().find("img.page").rotateRight();
	else
		$(e).parent().find("canvas").rotateRight();

	var r = parseInt($(e).attr("rotate"));
	r = (r != "NaN" ? r : 0);
	
	if (90 + r >= 360) r = -90;
	$(e).attr("rotate", 90 + r);
	$(e).parent().addClass("rotated");
	$(e).parent().vAlign();
	resizeUl();
};

function _resizeui() {
	$("#buildercontainer").height($(window).height() - 80);
	$("#picker").height($(window).height());
}

function _zoom(d) {
	var img = $(d).find('img').attr('src');
	var modal = $('img[src$="' + img + '"]').clone();
	
	var t = new Image();
	t.src = img;
	
	var height = t.height;
	var width = t.width;
	
	modal.dialog({
		height: $(window).height() - 40,
		modal: true,
		draggable: false,
		resizable: false,
		width: width 
	});
}

function _remove(r) {
	
	if ($(r).parent().parent().parent().attr('id') == "builder") {
		var rem = $(r).parent();
		$("#picker").append($(rem).parent());
		
		clearPreviousSelection();
		_resetsortable();
	}
}

function _resetsortable() {
	/*
	$("#picker li").sortable("destroy");
	
	$("#picker li").sortable({
		helper: 'clone',
		connectWith: '#builder',
		revert: 'invalid',
		receive: function(e, ui) {
			resizeUl();
		},
		distance: 15,
		stop: function(e, ui) {
			if ($(ui.sender).parent().attr("id") == "picker")
				$(ui.sender).sortable("cancel");
		}
	});
	*/
}

var selectionMode = "single";
var temp;

$(document).ready(function() {
	$(document).disableSelection();

	$("#picker img").wrap("<div rotate=0 />");
	//$("#picker img").mouseover(function() {
	//	this.style.height = "400px";
	//}).mouseout(function() {
	//	this.style.height = "";
	//});
	
	$("#picker div").append(function(index, html) {
		return "<span class='num'>" + ++index + "</span>" +
				"<span class='jog-control'><img style='height: 16px; -box-shadow: none; " +
				"-webkit-box-shadow: none; -moz-box-shadow: none' src='../images/icons/132.png' />" +
				"<img style='height: 16px; -box-shadow: none; -webkit-box-shadow: none; " +
				"-moz-box-shadow: none' src='../images/icons/131.png' />" +
				"<img style='height: 16px; -box-shadow: none; -webkit-box-shadow: none; " +
				"-moz-box-shadow: none' src='../images/icons/114.png' /></span>";
	});
	
	//$("#picker div span.rotate_r").click(function() { _rotater(this) });
	
	$(document).bind("contextmenu", function(e) { return false; } );
	
	$(window).resize(function() {
		_resizeui();
	});
	
	_resizeui();
	//_resetsortable();
	
	$("#picker div").click(function(e) {
		$(this).addClass("selected");
		if ($(this).parent().attr('id') == "builder")
			clearPreviousSelection();
		else if ($(this).parent().attr('id') == "picker")
			clearPreviousTopSelection();
			
		if (e.shiftKey) {
			selected.push(this);
		} else {
			clearPreviousSelection();
			selected = [this];
		}
	}).hover(function(e) {
		
	});
	
	$("#picker div").dblclick(function(e) {
		_zoom(this);
	});
	
	
	/*$("#builder").sortable({
		helper: 'clone',
		revert: 'invalid',
		receive: function(e, ui) {
			$("#builder").children('div').wrap("<li />");
			resizeUl();
		},
		start: function(e, ui) {
			if (ui.offset) {
			ui.originalPosition.left = 0;
			ui.offset.left = 0;
			ui.position.left = 0;
			}
		},
		sort: function(e, ui) {
			var curposition = $(ui.helper).position;
			temp = ui;
			ui.offset.left = 20;
			ui.position.left = -80;
			$(ui.helper).css('left', curposition-800);
		}
	}).droppable({
		activeClass: 'accept-drop'
	});*/
	
	$("#tool_add").click(function(e) {
		if ($(selected).length > 0) {
			var selectedp = $(selected).parent();
			for (var s = 0; s < $(selectedp).length; s++)
				$("#builder").append(selectedp[s]);
			
			$("#picker").remove($(selected).parent());
			selected = [];
			$(".selected").removeClass("selected");
			
			resizeUl();
		}
	});
	
	$("#tool_remove").click(function(e) {
		var selectedr = $(selected).find("img, canvas");
		if ($(selectedr).length > 0) {
			for (var s = 0; s < $(selectedr).length; s++)
				_remove(selectedr[s]);
		}
	});
	
	$("#tool_rotate").click(function(e) {
		if ($(selected).length > 0) {
			for (var s = 0; s < $(selected).length; s++)
				_rotater(selected[s]);
		}
	});
	
	$("#tool_savecontinue").click(function(e) {
		$("#builder").children().each(function() {
			var num = $(this).find("span").html();
			var rotate = $(this).find("div").attr("rotate");
			$(this).attr("id", "page_" + num + "," + rotate);
		});
		
		$("#builder").sortable();
		var serialized = $("#builder").sortable('serialize');
		$("#builder").sortable("destroy");
		$("#builder").empty();
		
		var docnum = $("#document_no").attr('value');
		var queueId = $("#queueID").attr('value');
		var demoName = $("#demoName").attr('value');
		
		$("#tool_savecontinue span").html("Wait...");
		
		$.getJSON('../dms/SplitDocument.do?method=split&document=' + docnum + '&' + serialized + '&queueID=' + queueId,			
				function(data) {
					$("#tool_savecontinue span").html("Save &amp; Continue");
					popup(screen.height,screen.width,"../dms/showDocument.jsp?segmentID="+data["newDocNum"] + '&demoName=' + encodeURIComponent(demoName) + "&inWindow=true", "assignDoc");			
				return false;
			}
		);
	});
	
	$("#tool_done").click(function(e) {
		
		if( confirm("Are you sure want to exit?")) {
		
			var docnum = $("#document_no").attr('value');
			
			opener.refreshAndFile(docnum);
			window.close();
		}
		
	});
	
	$(".jog-control").find(">:first-child").click(function(e) {
		// "prev" button
		var li = $(this).parent().parent().parent();
		var index = $(li).index();
		if (index != 0) {
			$("#builder li:eq(" + (index - 1) + ")").before(li);
		}
	});
	
	$(".jog-control").find(">:first-child").next().click(function(e) {
		// "next" button
		var li = $(this).parent().parent().parent();
		var index = $(li).index();
		$("#builder li:eq(" + (index + 1) + ")").after(li);
	});
	
	$(".jog-control").find(">:first-child").next().next().click(function(e) {
		// "rotate" button
		var div = $(this).parent().parent();
		_rotater(div);
	});
	
	$(".jog-control").find("img").hover(function(e) {
		$(this).css("opacity", "1.0");
	}, function(e) {
		$(this).css("opacity", "0.5");
	});
});

$(window).load(resizeUl);