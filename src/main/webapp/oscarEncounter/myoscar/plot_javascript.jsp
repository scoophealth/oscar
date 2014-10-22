<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript" language="javascript">
	// value="${param.series1}"

	jQuery(document).ready(function() {
		
		var line1 = [];
	<c:if test="${not empty param.series1}">
		line1 = [${param.series1}];
	</c:if>
		
		var line2 = [];
	<c:if test="${not empty param.series2}">
		line2 = [${param.series2}];
	</c:if>
		
		var series = new Array();
		var seriesLabels = new Array();
		if (line1 && line1.length != 0) {
			series[0] = line1;
			seriesLabels[0] = {
					label : '${param.series1Label}',
					showMarker:true
				};
		}
		
		if (line2 && line2.length != 0) {
			series[1] = line2;
			seriesLabels[1] = {
					label : '${param.series2Label}',
					showMarker:true
				};
		}
		
		if (series.length == 0) {
			return;
		}
		
		plot = jQuery.jqplot ('chart', series, {
			series: seriesLabels,
			legend: {
			      show: true,
			      location: 'ne',
			      xoffset: 12,
			      yoffset: 12,
			},
			axes : {
				xaxis : {
					label : 'Date',
					// tickInterval:'1 day',
					renderer : jQuery.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%b'
					},
				},
				yaxis : {
					label : '${param.yaxisLabel}'
				}
			}
		});
		var height = document.getElementsByTagName('body')[0].offsetHeight.toString();
		console.log('responsivechild ' + pymChild.id + ' '+ height);
		console.log(pymChild);
		        // Send the height to the parent.
		        window.parent.postMessage('responsivechild ' + pymChild.id + ' '+ height, '*');
	});
</script>