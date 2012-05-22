/*
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
var _highlighton = false;
function _togglehighlight() {
	var ar = $("[oscardbinput]");
	i = 0;
	while ((e = ar[i]) != null) {
		if (!_highlighton) {
			e.style.border = "2px solid red";
			e.style.background = "yellow";
		} else {
			e.style.border = "";
			e.style.background = "";
		}
		i++;
	}
	_highlighton = !_highlighton;
}

function _refreshfields() {

	$("#_oscarrefreshfieldsbtn").attr("disabled", true);
	var demographic = $("#_oscardemographicno").attr("value");
	var provider = $("#_oscarproviderno").attr("value");
	var fields = $("#_oscarupdatefields").attr("value").replace("%", ",");

	$.ajax({
		url: 'FetchUpdatedData.do?method=ajaxFetchData&demographic=' + demographic +
				'&provider=' + provider + '&fields=' + fields,
		dataType: 'json',
		success: function(data) {
			for (var s in data) {
				var t = $("[name=" + s + "]");
				if (t.is("textarea"))
					t.innerHtml = data[s];
				else if (t.attr("type").toLowerCase() == "checkbox") {
					if (data[s].toLowerCase() == "on")
						t.value = "on";
					else
						t.value = "off";
				} else
					t.attr("value", data[s]);
			}
			$("#_oscarrefreshfieldsbtn").attr("disabled", false);
		}
	});
}