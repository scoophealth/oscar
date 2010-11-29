<%@ page import="oscar.OscarProperties"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%
	oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
	String fid = pros.getProperty("eyeform_diagram_eform");

%>

<script>
//if have value under slidy block set the color to brown
function touchColor() {
		var divs = document.getElementsByClassName(document, "div", "slidey");
        for (var i=0; i<divs.length; i++) {
                var inputs = divs[i].getElementsByTagName("INPUT");
                var teinputs=divs[i].getElementsByTagName("TEXTAREA");
                for (var j=0; j<inputs.length; j++)
                        if (inputs[j].value.length>0 && inputs[j].className=="examfieldwhite")
                                break;
                for (var k=0; k<teinputs.length; k++)
                        if (teinputs[k].value.length>0 && teinputs[k].className=="examfieldwhite")
                                break;
                var color=(j<inputs.length || k<teinputs.length)?"brown":"black";
                divs[i].getElementsByTagName("A")[0].style.color=color;
        }
}
</script>

<script>
var effectMap = {};
jQuery("document").ready(function() {
	for(var x=21;x<32;x++) {
		var name = 's_'+x;	
		var el = document.getElementById(name);		
		if(el) {
			el.style.display='none';
			effectMap[name] = 'up';
		}
		
	}
});

function togglediv(el) {
	var tmp = document.getElementById('s_' + el.id.substring(el.id.indexOf('_')+1));	
	if(effectMap[tmp.id] == 'down') {
		new Effect.BlindUp(tmp);
		effectMap[tmp.id] = 'up';
	} else {
		new Effect.BlindDown(tmp);
		effectMap[tmp.id] = 'down';
	}
}

function whiteField(el){	
  		el.className="examfieldwhite";
}

function setfieldvalue(name,value) {
	jQuery("input[measurement='"+name+"']").each(function() {
		jQuery(this).val(value);
		whiteField(this);		
	});
}

function getfieldvalue(name) {
	var val = undefined;
	jQuery("input[measurement='"+name+"']").each(function() {
		val = jQuery(this).val();		
	});
	return val;
}

function copyAR(){	
	setfieldvalue("od_manifest_refraction_sph",getfieldvalue("od_ar_sph"));	
	setfieldvalue("os_manifest_refraction_sph",getfieldvalue("os_ar_sph"));
	setfieldvalue("od_manifest_refraction_cyl",getfieldvalue("od_ar_cyl"));
	setfieldvalue("os_manifest_refraction_cyl",getfieldvalue("os_ar_cyl"));
	setfieldvalue("od_manifest_refraction_axis",getfieldvalue("od_ar_axis"));
	setfieldvalue("os_manifest_refraction_axis",getfieldvalue("os_ar_axis"));	
}

function copySpecs(){
	jQuery.ajax({dataType: "script", url:ctx+"/eyeform/SpecsHistory.do?demographicNo="+demographicNo+"&method=copySpecs"});			
}

function setAnterior(){
	setfieldvalue("od_cornea","clear");
	setfieldvalue("os_cornea","clear");
	setfieldvalue("od_conjuctiva_sclera","white");
	setfieldvalue("os_conjuctiva_sclera","white");
	setfieldvalue("od_anterior_chamber","deep and quiet");
	setfieldvalue("os_anterior_chamber","deep and quiet");
	setfieldvalue("od_angle_middle1","open");
	setfieldvalue("os_angle_middle1","open");
	setfieldvalue("od_iris","normal");
	setfieldvalue("os_iris","normal");
	setfieldvalue("od_lens","clear");
	setfieldvalue("os_lens","clear");
	setfieldvalue("od_anterior_vitreous","clear");
	setfieldvalue("os_anterior_vitreous","clear");
}
function clearAnterior(){
	setfieldvalue("od_cornea","");
	setfieldvalue("os_cornea","");
	setfieldvalue("od_conjuctiva_sclera","");
	setfieldvalue("os_conjuctiva_sclera","");
	setfieldvalue("od_anterior_chamber","");
	setfieldvalue("os_anterior_chamber","");
	setfieldvalue("od_angle_middle0","");
	setfieldvalue("od_angle_middle1","");
	setfieldvalue("od_angle_middle2","");
	setfieldvalue("od_angle_up","");
	setfieldvalue("od_angle_down","");
	setfieldvalue("os_angle_middle0","");
	setfieldvalue("os_angle_middle1","");
	setfieldvalue("os_angle_middle2","");
	setfieldvalue("os_angle_up","");
	setfieldvalue("os_angle_down","");
	setfieldvalue("od_iris","");
	setfieldvalue("os_iris","");
	setfieldvalue("od_lens","");
	setfieldvalue("os_lens","");
	setfieldvalue("od_anterior_vitreous","");
	setfieldvalue("os_anterior_vitreous","");
}

function setPosterior(){
	setfieldvalue("od_disc","normal");
	setfieldvalue("os_disc","normal");
	setfieldvalue("od_macula","normal");
	setfieldvalue("os_macula","normal");
	setfieldvalue("od_retina","normal");
	setfieldvalue("os_retina","normal");
	setfieldvalue("od_vitreous","clear");
	setfieldvalue("os_vitreous","clear");
}
function clearPosterior(){
	setfieldvalue("od_disc","");
	setfieldvalue("os_disc","");
	setfieldvalue("od_macula","");
	setfieldvalue("os_macula","");
	setfieldvalue("od_retina","");
	setfieldvalue("os_retina","");
	setfieldvalue("od_vitreous","");
	setfieldvalue("os_vitreous","");
	setfieldvalue("od_cd_ratio_horizontal","");
	setfieldvalue("os_cd_ratio_horizontal","");
}

</script>

<style type="text/css">
.exam 
{
	padding: 0px;
	border-spacing: 0px;
	margin: 0px;
	border: 0px;
}

.exam td
{
	text-align: center;
	vertical-align:middle;
	padding:1px !important;
	margin:0px;
	border: 0px;
	font-size: 8pt;
}

.examfieldgrey
{
	background-color: #DDDDDD;
}

.examfieldwhite
{
	background-color: white;
}

.slidey { margin-bottom: 6px;font-size: 10pt;}
.slidey .title {
        margin-top: 1px;
        font-size:10pt;
        list-style-type: none;
        font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
        overflow: hidden;
        background-color: #f6f6ff;
        text-align:left;
        padding: 0px; }

.slidey .title2 {
        margin-top: 1px;
        font-size:10pt;
        list-style-type: none;
        font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
        overflow: hidden;
        background-color: #ccccff;
        padding: 0px; }
/* the only noteworthy thing here is the overflow is hidden,
it's really a sleight-of-hand kind of thing, we're playing
with the height and that makes it 'slide' */
.slidey .slideblock { overflow: hidden; background-color:  #ccccff;  padding: 2px; font-size: 10pt; }

span.ge{
        margin-top: 1px;
        border-bottom: 1px solid #000;
        font-weight: bold;
        list-style-type: none;
        font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
        font-size: 12pt;
        overflow: hidden;
        background-color: #99bbee;
        padding: 0px;
        color: black;;
        width: 300px;
}


</style>
<a id="save_measurements" href="">[save]</a>
<table>
<tr><td><span class="ge">Photos and Diagrams:</span></td></tr>
<tr>
<td>
<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" href="javascript:void(0)" tabindex="31" id="a_19" onclick="togglediv(this);">Digital Photos:</a>            
        </div>
        <div id="s_19" class="slideblock">
	        <a href="#" onclick="window.open('<html:rewrite page="/dms/documentReport.jsp"/>?function=demographic&doctype=lab&functionid=14&curUser=<%=session.getAttribute("user")%>&mode=add&parentAjaxId=docs&appointment_no='+appointmentNo);return false;">Add Photos</a>
        </div>
        
        <div class="title">
            <a style="font-weight: bold;" href="javascript:void(0)" tabindex="31" id="a_20" onclick="alert('Not yet implemented');">Diagrams:</a>            
        </div>
        <div id="s_20" class="slideblock">
            <a href="#" onclick="window.open('<html:rewrite page="/eform/efmformadd_data.jsp"/>?fid=<%=fid %>&demographic_no=<%=request.getParameter("demographic_no") %>&appointment=<%=request.getParameter("appointment_no") %>');return false;">Add Diagram</a>
        </div>
</div>
</td>
</tr>
<tr><td><span class="ge">Examination:</span></td></tr>
<tr>
<td>
<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" href="javascript:void(0)" tabindex="31" id="a_21" onclick="togglediv(this);">Vision Pretest:</a>            
        </div>
        <div id="s_21" class="slideblock">
            <table class="exam" width="100%">
            <tr>
            <td colspan="3">OD</td>
            <td ></td>
            <td colspan="3">OS</td>
            </tr>
            
			<tr>
            <td >Sph</td>
            <td >Cyl</td>
            <td >Axis</td>
            <td></td>
            <td>Sph</td>
            <td >Cyl</td>
            <td >Axis</td>
            </tr>
			<tr>
            <td><input type="text" tabindex="32"  class="examfieldgrey" size="6" measurement="od_ar_sph" onfocus="whiteField(this);"/></td>
            <td><input type="text" tabindex="33"  class="examfieldgrey" size="6" measurement="od_ar_cyl" onfocus="whiteField(this);"/></td>
            <td><input type="text" tabindex="34"  class="examfieldgrey" size="6" measurement="od_ar_axis" onfocus="whiteField(this);"/></td>
            
            
            <td >AR</td>
            <td ><input type="text" tabindex="35" class="examfieldgrey" size="6" measurement="os_ar_sph" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="36" class="examfieldgrey" size="6" measurement="os_ar_cyl" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="37" class="examfieldgrey" size="6" measurement="os_ar_axis" onfocus="whiteField(this);"/></td>
            
			</tr>
			<tr>
            <td >K1</td>
            <td >K2</td>
            <td >K2-Axis</td>
            <td ></td>
            <td >K1</td>
            <td >K2</td>
            <td >K2-Axis</td>
            </tr>
			<tr>
            <td ><input type="text"  tabindex="38"  class="examfieldgrey" size="6" measurement="od_k1" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="39"  class="examfieldgrey" size="6" measurement="od_k2" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="40"  class="examfieldgrey" size="6"  measurement="od_k2_axis" onfocus="whiteField(this);"/></td>
            
            
            <td >K</td>
            <td ><input type="text"  tabindex="41"  class="examfieldgrey" size="6" measurement="os_k1" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="42"  class="examfieldgrey" size="6" measurement="os_k2" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="43"  class="examfieldgrey" size="6" measurement="os_k2_axis" onfocus="whiteField(this);"/></td>
            
			</tr>
			<tr>
            <td >sc</td>
            <td >cc</td>
            <td >ph</td>
            <td ></td>
            <td >sc</td>
            <td >cc</td>
            <td >ph</td>
            </tr>
            <tr>
            <td ><input type="text"  tabindex="44"  class="examfieldgrey" size="6" measurement="od_sc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="45"  class="examfieldgrey" size="6" measurement="od_cc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="46"  class="examfieldgrey" size="6" measurement="od_ph_distance" onfocus="whiteField(this);"/></td>
            <td >Distance</td>
            <td ><input type="text"  tabindex="47"  class="examfieldgrey" size="6" measurement="os_sc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="48"  class="examfieldgrey" size="6" measurement="os_cc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="49"  class="examfieldgrey" size="6" measurement="os_ph_distance" onfocus="whiteField(this);"/></td>
            
            </tr>
            
            <tr>
            <td><input type="text"  tabindex="50"  class="examfieldgrey" size="6" measurement="od_sc_near" onfocus="whiteField(this);"/></td>
            <td><input type="text"  tabindex="51"  class="examfieldgrey" size="6" measurement="od_cc_near" onfocus="whiteField(this);"/></td>
            <td></td>
           <td>Near</td>
            <td><input type="text"  tabindex="52"  class="examfieldgrey" size="6" measurement="os_sc_near" onfocus="whiteField(this);"/></td>
            <td><input type="text"  tabindex="53"  class="examfieldgrey" size="6" measurement="os_cc_near" onfocus="whiteField(this);"/></td>
            <td></td>
            </tr>
			 
			
			
			
            </table>
        </div>
</div>
</td>
</tr>


<tr>
<td>
<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="60" href="javascript:void(0)" id="a_22" onclick="togglediv(this);">Vision Manifest:</a>
            <span>&nbsp;&nbsp;</span>
            <c:if test="${requestScope.hisFlag=='false'}">
            <a href="javascript:void(0)" tabindex="61" onclick="copyAR();"> [copy AR] </a>
            <span>&nbsp;&nbsp;</span>
            <a href="javascript:void(0)" tabindex="62" onclick="copySpecs();"> [copy Specs] </a>
            </c:if>
        </div>
        <div id="s_22" class="slideblock">
            <table class="exam" width="100%">
            <tr>
            <td colspan="6">OD</td>
            <td width="10%"></td>
            <td colspan="6">OS</td>
            </tr>
            <tr>
            <td width="6%">Sph</td>
            <td width="6%">Cyl</td>
            <td width="5%">Axis</td>
            <td width="6%">Add</td>
            <td width="1%"></td>
             <td width="9%">VA</td>
             <td width="9%">N</td>
            <td width="14%"></td>
            <td width="6%">Sph</td>
            <td width="6%">Cyl</td>
            <td width="5%">Axis</td>
            <td width="6%">Add</td>
            <td width="1%"></td>
            <td width="9%">VA</td>
            <td width="9%">N</td>
            </tr>
            <tr>
            <td><input type="text" size="5" tabindex="63" measurement="od_manifest_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="64" measurement="od_manifest_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="65" measurement="od_manifest_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  size="5" tabindex="66" measurement="od_manifest_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td><input type="text"  size="5" tabindex="67" measurement="od_manifest_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="68" measurement="od_manifest_near" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td >Manifest</td>
            <td><input type="text" size="5" tabindex="69" measurement="os_manifest_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="70" measurement="os_manifest_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="71" measurement="os_manifest_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="72" measurement="os_manifest_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td><input type="text" size="5" tabindex="73" measurement="os_manifest_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="74" measurement="os_manifest_near" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            

            </tr>
                       
            <tr>
            <td ><input type="text" size="5" tabindex="75" measurement="od_cycloplegic_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="76" measurement="od_cycloplegic_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="77" measurement="od_cycloplegic_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="78" measurement="od_cycloplegic_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td ><input type="text" size="5" tabindex="79" measurement="od_cycloplegic_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td></td>
            <td >Cycloplegic</td>
            <td ><input type="text" size="5" tabindex="80" measurement="os_cycloplegic_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="81" measurement="os_cycloplegic_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="82" measurement="os_cycloplegic_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="83" measurement="os_cycloplegic_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td ><input type="text" size="5" tabindex="84" measurement="os_cycloplegic_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td></td>
            </tr>
            
            </table>
        </div>
</div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" href="javascript:void(0)" tabindex="90" id="a_23" onclick="togglediv(this);">IOP:</a>
            
        </div>
        <div id="s_23" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="17%"></td>
        	<td width="15%">OD</td>
        	<td width="1%"></td>
        	<td width="34%"></td>
        	<td width="1%"></td>
        	<td width="15%">OS</td>
        	<td width="17%"></td>
        	</tr>
        	<tr>
        	<td ></td>
        	<td ><input type="text" tabindex="91" name="od_iop_nct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	<td width="34%" nowrap="nowrap">NCT()</td>
        	<td ></td>
        	<td ><input type="text" tabindex="93" name="os_iop_nct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	</tr>
        	<tr>
        	<td ></td>
        	<td ><input type="text" tabindex="94" name="od_iop_applanation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	<td width="34%">Applanation()</td>
        	<td ></td>
        	<td ><input type="text" tabindex="95" name="os_iop_applanation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</tr>
        	<tr>
        	<td></td>
        	<td><input type="text" tabindex="96" name="od_cct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	<td width="34%">CCT</td>
        	<td ></td>
        	<td><input type="text" tabindex="97" name="os_cct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="100" href="javascript:void(0)" id="a_24" onclick="togglediv(this);">Special Exam:</a>
            
        </div>
        <div id="s_24" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="33%">OD</td>
        	<td width="34%"></td>
        	<td width="33%">OS</td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="101" name="od_color_vision" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">color vision</td>
        	<td width="33%"><input type="text" tabindex="102" name="os_color_vision" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="103" name="od_pupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">Pupil</td>
        	<td width="33%"><input type="text" tabindex="104" name="os_pupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="105" name="od_amsler_grid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">Amsler Grid</td>
        	<td width="33%"><input type="text" tabindex="106" name="os_amsler_grid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="107" name="od_pam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">PAM</td>
        	<td width="33%"><input type="text" tabindex="108" name="os_pam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%">
        	<input type="text" tabindex="109" name="od_confrontation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	
        	<td width="34%">Confrontation</td>
        	<td width="33%">
        	<input type="text" tabindex="110" name="os_confrontation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	
        	<tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" href="javascript:void(0)" tabindex="120" id="a_25" onclick="togglediv(this);">
            EOM/Stereo:
            </a>
            
        </div>
        <div id="s_25" class="slideblock">
        <input type="text"area name="EOM" tabindex="121" onchange="syncFields(this)" cols="100" rows="2" class="examfieldgrey" onfocus="whiteField(this);"></input type="text"area>
        
        </div>
    </div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="125" href="javascript:void(0)" id="a_26" onclick="togglediv(this);">Anterior Segment:</a>
            <span>&nbsp;&nbsp;</span>
            <c:if test="${requestScope.hisFlag=='false'}">
            <a href="javascript:void(0)" tabindex="126" onclick="setAnterior();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="127" onclick="clearAnterior();return false;">[clear]</a>            
            </c:if>
        </div>
        <div id="s_26" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS<td>
        	</tr>
        	<tr>
        	<td><input type="text" size="6" tabindex="130" measurement="od_cornea" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Cornea</td>
        	<td><input type="text" size="6" tabindex="131" measurement="os_cornea" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<tr>
        	<td><input type="text" size="6" tabindex="132" measurement="od_conjuctiva_sclera" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Conjuctiva/Sclera</td>
        	<td><input type="text" size="6" tabindex="133" measurement="os_conjuctiva_sclera" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<tr>
        	<td><input type="text" size="6"  tabindex="134" measurement="od_anterior_chamber" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Anterior Chamber</td>
        	<td><input type="text"  size="6" tabindex="135" measurement="os_anterior_chamber" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<tr>
        	<td>
        	<table class="exam">
        	<tr>
        	<td></td>
        	<td width="20%"><input type="text" size="6" tabindex="136" measurement="od_angle_up" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</tr>
        	<tr>
        	<td colspan="3">
        	<table class="exam">
        	<tr>
        	<td width="20%"><input size="6" type="text" tabindex="137" measurement="od_angle_middle0" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="60%"><input size="6" type="text" tabindex="138" measurement="od_angle_middle1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%"><input size="6" type="text" tabindex="139" measurement="od_angle_middle2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	</table>
        	</td>
        	</tr>
        	<tr>
        	<td></td>
        	<td><input type="text" size="6" tabindex="140" measurement="od_angle_down" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</tr>
        	</table>
        	</td>
        	<td>Angle</td>
        	<td>
        	<table class="exam">
        	<tr>
        	<td></td>
        	<td width="20%"><input size="6" type="text" tabindex="141" measurement="os_angle_up" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</tr>
        	<tr>
        	<td colspan="3">
        	<table class="exam">
        	<tr>
        	<td width="20%"><input size="6" type="text" tabindex="142" measurement="os_angle_middle0" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="60%"><input size="6" type="text" tabindex="143" measurement="os_angle_middle1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%"><input size="6" type="text" tabindex="144" measurement="os_angle_middle2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	</table>
        	</td>
        	</tr>
        	<tr>
        	<td></td>
        	<td><input type="text" size="6" tabindex="145" measurement="os_angle_down" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</table>
        	</td>
        	</tr>
        	
        	<tr>
        	<td><input type="text" size="6" tabindex="146" measurement="od_iris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Iris</td>
        	<td><input type="text" size="6" tabindex="147" measurement="os_iris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	
        	<tr>
        	<td><input type="text" size="6" tabindex="148" measurement="od_lens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lens</td>
        	<td><input type="text" size="6" tabindex="149" measurement="os_lens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	
        	
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="160" href="javascript:void(0)" id="a_27" onclick="togglediv(this);">Posterior Segment:</a>
            <span>&nbsp;&nbsp;</span>
            <c:if test="${requestScope.hisFlag=='false'}">
            <a href="javascript:void(0)" tabindex="161" onclick="setPosterior();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="162" onclick="clearPosterior();return false;">[clear]</a>
            </c:if>
        </div>
        <div id="s_27" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="40%" colspan="2">OD</td>
        	<td width="20%"></td>
        	<td width="40%" colspan="2">OS</td>
        	</tr>
        	<tr>
        	<td width="40%" colspan="2"><input type="text" tabindex="163" measurement="od_disc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%">Disc</td>
        	<td width="40%" colspan="2"><input type="text" tabindex="164" measurement="os_disc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td colspan="2"><input type="text" tabindex="165" measurement="od_cd_ratio_horizontal" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>c/d ratio</td>
        	<td colspan="2"><input type="text" tabindex="166" measurement="os_cd_ratio_horizontal" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="40%" colspan="2"><input type="text" tabindex="167" measurement="od_macula" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%">Macula</td>
        	<td width="40%" colspan="2"><input type="text" tabindex="168" measurement="os_macula" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="40%" colspan="2"><input type="text" tabindex="169" measurement="od_retina" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%">Retina</td>
        	<td width="40%" colspan="2"><input type="text" tabindex="170" measurement="os_retina" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="40%" colspan="2"><input type="text" tabindex="171" measurement="od_vitreous" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="20%">Vitreous</td>
        	<td width="40%" colspan="2"><input type="text" tabindex="172" measurement="os_vitreous" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="175" href="javascript:void(0)" id="a_28" onclick="togglediv(this);">External:</a>
            <span>&nbsp;&nbsp;</span>
            <c:if test="${requestScope.hisFlag=='false'}">
            <a href="javascript:void(0)" tabindex="176" onclick="setExternal();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="177" onclick="clearExternal();return false;">[clear]</a>
            </c:if>
        </div>
        <div id="s_28" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="178" measurement="od_face" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Face</td>
        	<td><input type="text" tabindex="179" measurement="os_face" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="180" measurement="od_upper_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Upper Lid</td>
        	<td><input type="text" tabindex="181" measurement="os_upper_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="182" measurement="od_lower_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>lower Lid</td>
        	<td><input type="text" tabindex="183" measurement="os_lower_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="184" measurement="od_punctum" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Punctum</td>
        	<td><input type="text" tabindex="185" measurement="os_punctum" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="186" measurement="od_lacrimal_lake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lacrimal Lake</td>
        	<td><input type="text" tabindex="187" measurement="os_lacrimal_lake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="190" href="javascript:void(0)" id="a_29" onclick="togglediv(this);">NLD:</a>
            
        </div>
        <div id="s_29" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td>
        	<input type="text" tabindex="191" measurement="od_lacrimal_irrigation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	
        	<td>Lacrimal Irrigation</td>
        	
        	<td><input type="text" tabindex="192" measurement="os_lacrimal_irrigation"  onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="193" measurement="od_nld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>NLD</td>
        	<td><input type="text" tabindex="194" measurement="os_nld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="195" measurement="od_dye_disappearance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Dye Disappearance</td>
        	<td><input type="text" tabindex="196" measurement="os_dye_disappearance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	</table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="200" href="javascript:void(0)" id="a_30" onclick="togglediv(this);">Eyelid Measurements:</a>
            
        </div>
        <div id="s_30" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="201" measurement="od_mrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>MRD</td>
        	<td><input type="text" tabindex="202" measurement="os_mrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="203" measurement="od_levator_function" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Levator Function</td>
        	<td><input type="text" tabindex="204" measurement="os_levator_function" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="205" measurement="od_inferior_scleral_show" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Inferior Scleral Show</td>
        	<td><input type="text" tabindex="206" measurement="os_inferior_scleral_show" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="207" measurement="od_cn_vii" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>CN VII</td>
        	<td><input type="text" tabindex="208" measurement="os_cn_vii" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="209" measurement="od_blink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Blink</td>
        	<td><input type="text" tabindex="210" measurement="os_blink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="211" measurement="od_bells" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Bells</td>
        	<td><input type="text" tabindex="212" measurement="os_bells" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="213" measurement="od_lagophthalmos" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lagophthalmos</td>
        	<td><input type="text" tabindex="214" measurement="os_lagophthalmos" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>
<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;" tabindex="220" href="javascript:void(0)" id="a_31" onclick="togglediv(this);">Orbit:</a>
            
        </div>
        <div id="s_31" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="221" measurement="od_hertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Hertel</td>
        	<td><input type="text" tabindex="222" measurement="os_hertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="223" measurement="od_retropulsion" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Retropulsion</td>
        	<td><input type="text" tabindex="224" measurement="os_retropulsion" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>



</table>