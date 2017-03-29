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

<%@ page import="oscar.OscarProperties"%>
<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.eyeform.web.EyeformAction"%>

<%
	request.setAttribute("sections",EyeformAction.getMeasurementSections());
	request.setAttribute("headers",EyeformAction.getMeasurementHeaders());
	request.setAttribute("providers",EyeformAction.getActiveProviders());
	oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
	String eyeform = props1.getProperty("cme_js");
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
			function addSection1(fromList,toList) {
				//alert(fromList);
				//alert(toList);
				selectOption2(toList);
				for(var x=0;x<fromList.options.length;x++) {
					if(fromList.options[x].selected) {
						selectHeaderFromSection1(fromList.options[x].value,toList);
					}
				}
				
			}

			function selectHeaderFromSection1(section,toList) {
				if(section == 'GLASSES HISTORY') {
					selectOption1(toList,'Glasses Rx');
				}
				if(section == 'VISION ASSESSMENT') {
					selectOption1(toList,'Distance vision (sc)');
					selectOption1(toList,'Distance vision (cc)');
					selectOption1(toList,'Distance vision (ph)');
					selectOption1(toList,'Intermediate vision (sc)');
					selectOption1(toList,'Intermediate vision (cc)');
					selectOption1(toList,'Near vision (sc)');
					selectOption1(toList,'Near vision (cc)');
				}	
				if(section == 'STEREO VISION') {
					selectOption1(toList,'Fly test');
					selectOption1(toList,'Stereo-acuity');
				}
				if(section == 'VISION MEASUREMENT') {
					selectOption1(toList,'Keratometry');
					selectOption1(toList,'Auto-refraction');
					selectOption1(toList,'Manifest distance');
					selectOption1(toList,'Manifest near');
					selectOption1(toList,'Cycloplegic refraction');
				}
				if(section == 'INTRAOCULAR PRESSURE') {
					selectOption1(toList,'NCT');
					selectOption1(toList,'Applanation');
					selectOption1(toList,'Central corneal thickness');
				}
				if(section == 'REFRACTIVE') {
					selectOption1(toList,'Dominance');
					selectOption1(toList,'Mesopic pupil size');
					selectOption1(toList,'Angle Kappa');
				}
				if(section == 'OTHER EXAM') {
					selectOption1(toList,'Colour vision');
					selectOption1(toList,'Pupil');
					selectOption1(toList,'Amsler grid');
					selectOption1(toList,'Potential acuity meter');
					selectOption1(toList,'Confrontation fields');
					selectOption1(toList,'Maddox rod');
					selectOption1(toList,'Bagolini test');
					selectOption1(toList,'Worth 4 Dot (distance)');
					selectOption1(toList,'Worth 4 Dot (near)');
				}
				if(section == 'DUCTION/DIPLOPIA TESTING') {
					selectOption1(toList,'DUCTION/DIPLOPIA TESTING');
				}
				if(section == 'DEVIATION MEASUREMENT') {
					selectOption1(toList,'Primary gaze');
					selectOption1(toList,'Up gaze');
					selectOption1(toList,'Down gaze');
					selectOption1(toList,'Right gaze');
					selectOption1(toList,'Left gaze');
					selectOption1(toList,'Right head tilt');
					selectOption1(toList,'Left head tilt');
					selectOption1(toList,'Near');
					selectOption1(toList,'Near with +3D add');
					selectOption1(toList,'Far distance');
				}
				if(section == 'EXTERNAL/ORBIT') {
					selectOption1(toList,'Face');
					selectOption1(toList,'Retropulsion');
					selectOption1(toList,'Hertel');
				}
				if(section == 'EYELID/NASOLACRIMAL DUCT') {
					selectOption1(toList,'Upper lid');
					selectOption1(toList,'Lower lid');
					selectOption1(toList,'Lacrimal lake');
					selectOption1(toList,'Lacrimal irrigation');
					selectOption1(toList,'Punctum');
					selectOption1(toList,'Nasolacrimal duct');
					selectOption1(toList,'Dye disappearance');
				}
				if(section == 'EYELID MEASUREMENT') {
					selectOption1(toList,'Margin reflex distance');
					selectOption1(toList,'Inferior scleral show');
					selectOption1(toList,'Levator function');
					selectOption1(toList,'Lagophthalmos');
					selectOption1(toList,'Blink reflex');
					selectOption1(toList,'Cranial Nerve VII function');
					selectOption1(toList,'Bells phenomenon');
					selectOption1(toList,'Schirmer test');
				}
				if(section == 'ANTERIOR SEGMENT') {
					selectOption1(toList,'Cornea');
					selectOption1(toList,'Conjunctiva/Sclera');
					selectOption1(toList,'Anterior chamber');
					selectOption1(toList,'Angle');
					selectOption1(toList,'Iris');
					selectOption1(toList,'Lens');
				}
				if(section == 'POSTERIOR SEGMENT') {
					selectOption1(toList,'Optic disc');
					selectOption1(toList,'C/D ratio');
					selectOption1(toList,'Macula');
					selectOption1(toList,'Retina');
					selectOption1(toList,'Vitreous');
				}
				
			}

			function selectOption2(toList){
				for(var x=0;x<toList.options.length;x++) {
					if(toList.options[x].selected == true){
						toList.options[x].selected=false;
					}
				}
			}
			function selectOption1(toList,val) {
				for(var x=0;x<toList.options.length;x++) {
					if(toList.options[x].value == val) {
						toList.options[x].selected=true;
					}
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
					<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
						<tr>
               		<td>
							<select name="fromlist1" multiple="multiple" size="14" ondblclick="addSection1(document.inputForm.elements['fromlist1'],document.inputForm.elements['fromlist2']);">	
								<option value="GLASSES HISTORY">GLASSES HISTORY</option>
								<option value="VISION ASSESSMENT">VISION ASSESSMENT</option>
								<option value="VISION MEASUREMENT">VISION MEASUREMENT</option>
								<option value="STEREO VISION">STEREO VISION</option>
								<option value="INTRAOCULAR PRESSURE">INTRAOCULAR PRESSURE</option>
								<option value="REFRACTIVE">REFRACTIVE</option>
								<option value="OTHER EXAM">OTHER EXAM</option>
								<option value="DUCTION/DIPLOPIA TESTING">DUCTION/DIPLOPIA TESTING</option>
								<option value="DEVIATION MEASUREMENT">DEVIATION MEASUREMENT</option>
								<option value="EXTERNAL/ORBIT">EXTERNAL/ORBIT</option>
								<option value="EYELID/NASOLACRIMAL DUCT">EYELID/NASOLACRIMAL DUCT</option>
								<option value="EYELID MEASUREMENT">EYELID MEASUREMENT</option>
								<option value="ANTERIOR SEGMENT">ANTERIOR SEGMENT</option>
								<option value="POSTERIOR SEGMENT">POSTERIOR SEGMENT</option>							
                			</select>
                		</td>
                		<td valign="middle">
							<input type="button" value=">>" onclick="addSection1(document.inputForm.elements['fromlist1'],document.inputForm.elements['fromlist2']);"/>
						</td>
                		<td>
                			<select id="fromlist2" name="fromlist2" multiple="multiple" size="20">
								<option value="Glasses Rx">Glasses Rx</option>
								
								<option value="Distance vision (sc)">Distance vision (sc)</option>
								<option value="Distance vision (cc)">Distance vision (cc)</option> 
								<option value="Distance vision (ph)">Distance vision (ph)</option> 
								<option value="Intermediate vision (sc)">Intermediate vision (sc)</option> 
								<option value="Intermediate vision (cc)">Intermediate vision (cc)</option> 
								<option value="Near vision (sc)">Near vision (sc)</option> 
								<option value="Near vision (cc)">Near vision (cc)</option> 
								
								<option value="Fly test">Fly test</option> 
								<option value="Stereo-acuity">Stereo-acuity</option> 
								
								<option value="Keratometry">Keratometry</option> 
								<option value="Auto-refraction">Auto-refraction</option>
								<option value="Manifest distance">Manifest distance</option>
								<option value="Manifest near">Manifest near</option>
								<option value="Cycloplegic refraction">Cycloplegic refraction</option>
								
								<option value="NCT">NCT</option>
								<option value="Applanation">Applanation</option>
								<option value="Central corneal thickness">Central corneal thickness</option>
								
								<option value="Dominance">Dominance</option>
								<option value="Mesopic pupil size">Mesopic pupil size</option>
								<option value="Angle Kappa">Angle Kappa</option>
								
								<option value="Colour vision">Colour vision</option>
								<option value="Pupil">Pupil</option>
								<option value="Amsler grid">Amsler grid</option>
								<option value="Potential acuity meter">Potential acuity meter</option>
								<option value="Confrontation fields">Confrontation fields</option>
								<option value="Maddox rod">Maddox rod</option>
								<option value="Bagolini test">Bagolini test</option>
								<option value="Worth 4 Dot (distance)">Worth 4 Dot (distance)</option>
								<option value="Worth 4 Dot (near)">Worth 4 Dot (near)</option>
								
								<option value="DUCTION/DIPLOPIA TESTING">DUCTION/DIPLOPIA TESTING</option>
								
								<option value="Primary gaze">Primary gaze</option>
								<option value="Up gaze">Up gaze</option>
								<option value="Down gaze">Down gaze</option>
								<option value="Right gaze">Right gaze</option>
								<option value="Left gaze">Left gaze</option>
								<option value="Right head tilt">Right head tilt</option>
								<option value="Left head tilt">Left head tilt</option>
								<option value="Near">Near</option>
								<option value="Near with +3D add">Near with +3D add</option>
								<option value="Far distance">Far distance</option>
								
								<option value="Face">Face</option>
								<option value="Retropulsion">Retropulsion</option>
								<option value="Hertel">Hertel</option>
								
								<option value="Upper lid">Upper lid</option>
								<option value="Lower lid">Lower lid</option>
								<option value="Lacrimal lake">Lacrimal lake</option>
								<option value="Lacrimal irrigation">Lacrimal irrigation</option>
								<option value="Punctum">Punctum</option>
								<option value="Nasolacrimal duct">Nasolacrimal duct</option>
								<option value="Dye disappearance">Dye disappearance</option>
								
								<option value="Margin reflex distance">Margin reflex distance</option>
								<option value="Inferior scleral show">Inferior scleral show</option>
								<option value="Levator function">Levator function</option>
								<option value="Lagophthalmos">Lagophthalmos</option>
								<option value="Blink reflex">Blink reflex</option>
								<option value="Cranial Nerve VII function">Cranial Nerve VII function</option>
								<option value="Bells phenomenon">Bell's phenomenon</option>
								<option value="Schirmer test">Schirmer test</option>
								
								<option value="Cornea">Cornea</option>
								<option value="Conjunctiva/Sclera">Conjunctiva/Sclera</option>
								<option value="Anterior chamber">Anterior chamber</option>
								<option value="Angle">Angle</option>
								<option value="Iris">Iris</option>
								<option value="Lens">Lens</option>
								
								<option value="Optic disc">Optic disc</option>
								<option value="C/D ratio">C/D ratio</option>
								<option value="Macula">Macula</option>
								<option value="Retina">Retina</option>
								<option value="Vitreous">Vitreous</option>
								
                			</select>                			
						</td>              																				
						</tr>
						
					<%}else{%>
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
						<%}%>
					</table>
					</td>
				</tr>
				<tr>
				<td colspan="1"></td>
				<td>
					<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
					<input name="" value="Submit" onclick="document.inputForm.method.value='query';" type="submit">
					<%}else{%>
					<input name="" value="Submit" onclick="document.inputForm.method.value='query';selectList2();" type="submit">
					<%}%>
				</td>
	</tr>
	</tbody></table>
  
  </form>
</body>
</html>
