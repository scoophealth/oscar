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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>BMI</title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<script type="text/javascript">

	//Body Surface Area = 0.20247 x Height(m)0.725 x Weight(kg)0.425
            function getBodySurfaceArea(height,weight){
                 //console.log( height);
                 return 0.20247 * Math.pow(height,0.725) * Math.pow(weight,0.425);
            }
        
        //Lean Body Weight (men) = (1.10 x Weight(kg)) - 128 ( Weight2/(100 x Height(m))2)
        //Lean Body Weight (women) = (1.07 x Weight(kg)) - 148 ( Weight2/(100 x Height(m))2)
            function getLeanBodyWeight( height,weight,sex){
                var LEANCONST1 = 1.10;  //MALE VALUES
                var LEANCONST2 = 128;
                
                if ("F" == sex){                  
                  LEANCONST1 = 1.07;  //FEMALE VALUES
                  LEANCONST2 = 148;
                }
                return ( LEANCONST1 * weight ) - LEANCONST2 * ( Math.pow(weight,2) /  Math.pow(100 *height,2) );
            }
        
        
        //Ideal Body Weight (men) = 50 + 2.3 ( Height(in) - 60 )
        //Ideal Body Weight (women) = 45.5 + 2.3 ( Height(in) - 60 )
            function getIdealBodyWeight(height,sex){
                var CONST1 = 50;
                if ("F" == sex){
                  CONST1 = 45.5;  //FEMALE VALUES
                }
                return CONST1 + 2.3 * ( height - 60);
            }

        //Body Mass Index = Weight(kg) / Height(m)2
            function getBodyMassIndex(weight,height){
                return weight / ( height * height);
            }

	
            function calc(){
                var weight = document.getElementById('weight').value;          
                var height = document.getElementById('height').value;
                var sexM    = document.getElementById('sexM');
                var sex = "M";
                if (sexM.checked == false){
                   sex = "F";
                }
                
                //console.log("weight "+weight+" height "+height+" sex"+sex);
                
                weight_type = document.getElementById('weight_type').value;   //value="lbs"    value="kgs"
                height_type = document.getElementById('height_type').value;   //value="inches" value="cm"  value="m"
                
                
                //console.log("weight_type "+weight_type+" weight_type "+height_type);
                
                
                var height_in_meters = 0;
                var height_in_inches = 0;
                var weight_in_kgs    = 0;
                
                if (weight_type == "lbs"){
                   //console.log("weight entered in lbs");
                   weight_in_kgs = weight * 0.45359237;  
                }else{
                   //console.log("weight entered in kgs:"+weight_type);
                   weight_in_kgs = weight;
                }
                
                if (height_type == "m"){
                   //console.log("height entered in meters");
                   height_in_meters = height;
                   height_in_inches = height * 39.3700787;
                }else if (height_type == "cm"){
                   height_in_meters = height * 0.01;
                   height_in_inches = height * 0.393700787;
                }else{  //inches
                   //console.log("height entered in inches "+height);
                   height_in_meters = height * 0.0254;
                   height_in_inches = height ;
                }
                
                
                //console.log("height_in_meters "+height_in_meters+" height_in_inches "+height_in_inches+" weight_in_kgs"+weight_in_kgs);
              
                
                var bodySurfaceArea = getBodySurfaceArea(height_in_meters,weight_in_kgs);    
                var leanBodyWeight  = getLeanBodyWeight(height_in_meters,weight_in_kgs,sex);
                var idealBodyWeight = getIdealBodyWeight(height_in_inches,sex);
                var bodyMassIndex   = getBodyMassIndex(weight_in_kgs,height_in_meters);
                
                
                //console.log("bodySurfaceArea "+bodySurfaceArea+" leanBodyWeight " +leanBodyWeight + " idealBodyWeight "+ idealBodyWeight +"bodyMassIndex  "+bodyMassIndex );
                
                /// FILL THE VALUES IN THE FORM
                
                        document.getElementById('bodySurfaceArea').value = Math.round(bodySurfaceArea * 100 ) / 100;
                        document.getElementById('leanBodyWeight_kg').value = Math.round(leanBodyWeight);
                        document.getElementById('leanBodyWeight_lbs').value = Math.round(leanBodyWeight * 2.20462262);
                        document.getElementById('idealBodyWeight_kg').value = Math.round(idealBodyWeight);
                        document.getElementById('idealBodyWeight_lbs').value = Math.round(idealBodyWeight * 2.20462262);
                        document.getElementById('bodyMassIndex').value = Math.round(bodyMassIndex * 10)/10;
                
                /// END FILLING THE VALUES
            }
			
		
        </script>
<script Language="JavaScript">
//
// QueryString
//

var pastewin;

function write2Parent(){
    var text = document.second.prediction.value;
    
    if( window.opener != null )
        pastewin = window.opener;

    if( pastewin.document.forms["caseManagementEntryForm"] == undefined ) {
        pastewin.document.encForm.enTextarea.value = pastewin.document.encForm.enTextarea.value + "\n\n" + text;
        pastewin.setTimeout("document.encForm.enTextarea.scrollTop=document.encForm.enTextarea.scrollHeight", 0);  // setTimeout is needed to allow browser to realize that text field has been updated 
        pastewin.document.encForm.enTextarea.focus();
    }
    else {
        pastewin.pasteToEncounterNote(text);
    }        
    window.close();
 }


function QueryString(key)
{
	var value = null;
	for (var i=0;i<QueryString.keys.length;i++)
	{
		if (QueryString.keys[i]==key)
		{
			value = QueryString.values[i];
			break;
		}
	}
	return value;
}
QueryString.keys = new Array();
QueryString.values = new Array();

function QueryString_Parse()
{
	var query = window.location.search.substring(1);
	var pairs = query.split("&");
	
	for (var i=0;i<pairs.length;i++)
	{
		
		var pos = pairs[i].indexOf('=');
		if (pos >= 0)
		{
			var argname = pairs[i].substring(0,pos);
			var value = pairs[i].substring(pos+1);
			QueryString.keys[QueryString.keys.length] = argname;
			QueryString.values[QueryString.values.length] = value;		
		}
	}

}

QueryString_Parse();
function setValues(){
	var theSex = QueryString('sex');
	var theAge = QueryString('age');
	if ( theSex != null){
		if ( theSex == 'M'){
			//document.calCorArDi.sex[1].checked = true;
                        document.getElementById('sexM').checked = true;
		}else if ( theSex == 'F'){
			//document.calCorArDi.sex[0].checked = true;
                        document.getElementById('sexF').checked = true;
		}
	}
	if ( theAge != null){		
			//document.calCorArDi.age.value = theAge;	
                        
	}
//	alert(QueryString('sex'));
	
//	alert(QueryString('age'));
	//}
	//switchData();
        window.focus();
}
                            </script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">BMI</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>BMI</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="calculator" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<fieldset><legend>BMI</legend> <label>Sex:</label> <input
			type="radio" name="sex" id="sexM" value="M">Male</input> <input
			type="radio" name="sex" id="sexF" value="F">Female</input> <br />

		<label>Weight</label> <input type="text" class="text" name="weight"
			id="weight"> <select name="weight_type" id="weight_type">
			<option value="lbs">lbs</option>
			<option value="kgs">kgs</option>
		</select> <br />

		<label>Height</label> <input type="text" class="text" name="height"
			id="height"> <select name="height_type" id="height_type">
			<option value="inches">inches</option>
			<option value="cm">cm</option>
			<option value="m">m</option>
		</select> <br />
		<input type="submit" value="Calculate" onclick="calc();" /></fieldset>
		<fieldset class="inputs"><legend>BMI</legend> <label>Body
		Surface Area</label> <input type="text" name="bodySurfaceArea"
			id="bodySurfaceArea"> m<sup>2</sup> </input> <br />
		<label>Lean Body Weight</label> <input type="text"
			name="leanBodyWeight_kg" id="leanBodyWeight_kg"> kg</input> <input
			type="text" name="leanBodyWeight_lbs" id="leanBodyWeight_lbs">
		lbs</input> <br />
		<label>Ideal Body Weight</label> <input type="text"
			name="idealBodyWeight_kg" id="idealBodyWeight_kg"> kg</input> <input
			type="text" name="idealBodyWeight_lbs" id="idealBodyWeight_lbs">
		lbs</input> <br />

		<label>Body Mass Index</label> <input type="text" name="bodyMassIndex"
			id="bodyMassIndex"> kg/m<sup>2</sup> </input> <br />
		</fieldset>
		<div class="defs"><span>Body Surface Area</span> = 0.20247 x
		Height(m)<sup>0.725</sup> x Weight(kg)<sup>0.425</sup> <br />
		<span>Lean Body Weight (men)</span> = (1.10 x Weight(kg)) - 128 (
		Weight<sup>2</sup>/(100 x Height(m)<sup>2</sup>) <br />
		<span>Lean Body Weight (women)</span> = (1.07 x Weight(kg)) - 148 (
		Weight<sup>2</sup>/(100 x Height(m)<sup>2</sup>) <br />
		<span>Ideal Body Weight (men)</span> = 50 + 2.3 ( Height(in) - 60 ) <br />
		<span>Ideal Body Weight (women)</span> = 45.5 + 2.3 ( Height(in) - 60
		) <br />
		<span>Body Mass Index</span> = Weight(kg) / Height(m)<sup>2</sup> <br />
		</div>

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
