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

<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.eyeform.web.EyeformAction"%>

<%
	request.setAttribute("sections",EyeformAction.getMeasurementSections());
	request.setAttribute("headers",EyeformAction.getMeasurementHeaders());
	request.setAttribute("providers",EyeformAction.getActiveProviders());
%>
<html>
	<head>
    	<title>Choose Fields for Examination History</title>
    
   		<style type="text/css">
		table td{
			width:80%
			padding:0px;
			cell-spacing:0;
			margin:0;
			border: 0;
			font-size: 10pt;
		}
		.full input{border:1px solid gray;}
		select{border:1px solid gray;}		
		</style>

		<oscar:customInterface/>
  
  		<script>
  			function moveAllToList(fromList,toList) {
  				var removePos=new Array();
  				for(var x=0;x<fromList.options.length;x++) {
  					var val = fromList.options[x].value;
					var text = fromList.options[x].text;
					toList.options[toList.options.length] = new Option(text,val);
					removePos.push(val);
  				}
  				for(var x=0;x<removePos.length;x++) {
  					removeOption(fromList,removePos[x]);  					  				
  				}  	
  			}
  			
  			function moveToList(fromList,toList) {
  				var removePos=new Array();
  				for(var x=0;x<fromList.options.length;x++) {
  					if(fromList.options[x].selected) {
  						var val = fromList.options[x].value;
  						var text = fromList.options[x].text;
  						toList.options[toList.options.length] = new Option(text,val);
  						removePos.push(val);
  					}
  				}
  				
  				for(var x=0;x<removePos.length;x++) {
  					removeOption(fromList,removePos[x]);  					  				
  				}  				
  				  				
  			}
  			
  			function removeOption(list,val) {
  				for(var x=0;x<list.options.length;x++) {
  					if(list.options[x].value == val) {
  						list.remove(x);
  						break;
  					}
  				}
  			}
  			
  			function selectList2() {
  				var list = document.inputForm.elements['fromlist2'];
  				for(var x=0;x<list.options.length;x++) {
  					list.options[x].selected=true;
  				}
  			}
  		</script>
  </head><body>

  <form name="inputForm" id="inputForm" method="post" action="<%=request.getContextPath()%>/eyeform/ExaminationHistory.do">
  <input name="method" value="query" type="hidden">
  <input name="demographicNo" value="<c:out value="${demographic.demographicNo }"/>" type="hidden">

	<table>
	<tbody>
	<tr>
	<td colspan="2">Please choose the examination field for <c:out value="${demographic.formattedName }"/></td>
	</tr>
					<tr>
					<td colspan=2 style="width: 100%">
					<table>
						<tr>
               		<td>
                			<select name="fromlist1" multiple="multiple" size="20" ondblclick="moveToList(document.inputForm.elements['fromlist1'],document.inputForm.elements['fromlist2']);">                				
								<option value="ar">AR</option>
																
								<option value="k">K</option>
								<option value="sc_distance">sc distance</option>
								<option value="ph_distance">ph distance</option>
								<option value="cc_distance">cc distance</option>
								<option value="cc_near">cc near</option>
								<option value="sc_near">sc near</option>
								<option value="manifest_refraction">Manifest refraction</option>
								<option value="cycloplegic_refraction">Cycloplegic refraction</option>
								<option value="manifest_distance">manifest distance</option>
								
								<option value="manifest_near">manifest near</option>
								<option value="cycloplegic_distance">cycloplegic distance</option>
								<option value="iop_nct">IOP_nct</option>
								<option value="iop_applanation">IOP_applanation</option>
								<option value="cct">CCT</option>
								<option value="color_vision">color vision</option>
								<option value="pupil">pupil</option>
								<option value="amsler_grid">amsler grid</option>
								<option value="pam">PAM</option>
								
								<option value="confrontation">confrontation</option>
								<option value="EOM">EOM</option>
								<option value="cornea">cornea</option>
								<option value="conjuctiva_sclera">conjuctiva/sclera</option>
								<option value="anterior_chamber">anterior chamber</option>
								<option value="angle">angle</option>
								<option value="iris">iris</option>
								<option value="lens">lens</option>
								<option value="disc">disc</option>
								
								<option value="cd_ratio_horizontal">c/d ratio</option>
								<option value="macula">macula</option>
								<option value="retina">retina</option>
								<option value="vitreous">vitreous</option>
								<option value="face">face</option>
								<option value="upper_lid">upper lid</option>
								<option value="lower_lid">lower lid</option>
								<option value="punctum">punctum</option>
								<option value="lacrimal_lake">lacrimal lake</option>
								
								<option value="lacrimal_irrigation">lacrimal irrigation</option>
								<option value="nld">NLD</option>
								<option value="dye_disappearance">dye disappearance</option>
								<option value="mrd">MRD</option>
								<option value="levator_function">levator function</option>
								<option value="inferior_scleral_show">inferior scleral show</option>
								<option value="cn_vii">CN VII</option>
								<option value="blink">blink</option>
								<option value="bells">bells</option>
								
								<option value="lagophthalmos">lagophthalmos</option>
								<option value="hertel">Hertel</option>
								<option value="retropulsion">retropulsion</option>                				
                			</select>
                		</td>
                		<td valign="middle">
                			<input type="button" value=">>" onclick="moveToList(document.inputForm.elements['fromlist1'],document.inputForm.elements['fromlist2']);"/>
                			<br/><br/>
                			<input type="button" value="All >>" onclick="moveAllToList(document.inputForm.elements['fromlist1'],document.inputForm.elements['fromlist2']);"/>
                			<br/><br/>
                			<input type="button" value="<<" onclick="moveToList(document.inputForm.elements['fromlist2'],document.inputForm.elements['fromlist1']);"/>
                			<br/><br/>
                			<input type="button" value="All <<" onclick="moveAllToList(document.inputForm.elements['fromlist2'],document.inputForm.elements['fromlist1']);"/>                			                			
                		</td>
                		<td>
                			<select id="fromlist2" name="fromlist2" multiple="multiple" size="20">                				
                			</select>                			
						</td>              																				
						</tr>
					</table>
					</td>
				</tr>
				<tr>
				<td colspan="1"></td>
				<td>
					<input name="" value="Submit" onclick="document.inputForm.method.value='query';selectList2();" type="submit">
				</td>
	</tr>
	</tbody></table>
  
  </form>
</body>
</html>
