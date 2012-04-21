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
<title><bean:message
	key="oscarEncounter.calculators.OsteoporoticFracture.title" /></title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<script type="text/javascript">

var osteFactorMale    = new Array();
osteFactorMale[1] = new Array();
osteFactorMale[2] = new Array();
osteFactorMale[3] = new Array();
osteFactorMale[4] = new Array();
osteFactorMale[5] = new Array();
osteFactorMale[6] = new Array();
osteFactorMale[7] = new Array();
osteFactorMale[8] = new Array();

var osteFactorFemale    = new Array();
osteFactorFemale[1] = new Array();
osteFactorFemale[2] = new Array();
osteFactorFemale[3] = new Array();
osteFactorFemale[4] = new Array();
osteFactorFemale[5] = new Array();
osteFactorFemale[6] = new Array();
osteFactorFemale[7] = new Array();
osteFactorFemale[8] = new Array();



//MALE AGE
//50
osteFactorMale[1][0]  = 3.3; //over all
osteFactorMale[1][1]  = 1.8; //1
osteFactorMale[1][2]  = 2.7; //0
osteFactorMale[1][3]  = 4.2; //-1
osteFactorMale[1][4]  = 6.3; //-2
osteFactorMale[1][5]  = 9.2; // < -2

//55
osteFactorMale[2][0]  = 3.9; //over all
osteFactorMale[2][1]  = 1.9; //1
osteFactorMale[2][2]  = "3.0"; //0
osteFactorMale[2][3]  = 4.6; //-1
osteFactorMale[2][4]  = "7.0"; //-2
osteFactorMale[2][5]  = 10.4; // < -2

//60
osteFactorMale[3][0]  = 4.9; //over all
osteFactorMale[3][1]  = 2.5; //1
osteFactorMale[3][2]  = 3.6; //0
osteFactorMale[3][3]  = 5.4; //-1
osteFactorMale[3][4]  = 7.9; //-2
osteFactorMale[3][5]  = 11.6; // < -2

//65
osteFactorMale[4][0]  = 5.9; //over all
osteFactorMale[4][1]  = "3.0"; //1
osteFactorMale[4][2]  = 4.3; //0
osteFactorMale[4][3]  = 6.2; //-1
osteFactorMale[4][4]  = 8.8; //-2
osteFactorMale[4][5]  = "13.0"; // < -2

//70
osteFactorMale[5][0]  = 7.6; //over all
osteFactorMale[5][1]  = 3.4; //1
osteFactorMale[5][2]  = 5.1; //0
osteFactorMale[5][3]  = 7.4; //-1
osteFactorMale[5][4]  = 10.9; //-2
osteFactorMale[5][5]  = 16.2; // < -2

//75
osteFactorMale[6][0]  = 10.4; //over all
osteFactorMale[6][1]  = 4.1; //1
osteFactorMale[6][2]  = 6.3; //0
osteFactorMale[6][3]  = 9.6; //-1
osteFactorMale[6][4]  = 14.4; //-2
osteFactorMale[6][5]  = 21.5; // < -2

//80
osteFactorMale[7][0]  = 13.1; //over all
osteFactorMale[7][1]  = 5.3; //1
osteFactorMale[7][2]  = 7.7; //0
osteFactorMale[7][3]  = 11.1; //-1
osteFactorMale[7][4]  = 15.8; //-2
osteFactorMale[7][5]  = 23.2; // < -2

//85
osteFactorMale[8][0]  = 13.1; //over all
osteFactorMale[8][1]  = 5.3; //1
osteFactorMale[8][2]  = 7.5; //0
osteFactorMale[8][3]  = 10.4; //-1
osteFactorMale[8][4]  = 14.3; //-2
osteFactorMale[8][5]  = 21.4; // < -2

//FEMALE AGE-------------------==========================--------------------------
//50
osteFactorFemale[1][0]  = "6.0"; //over all
osteFactorFemale[1][1]  = 2.4; //1
osteFactorFemale[1][2]  = 3.8; //0
osteFactorFemale[1][3]  = 5.9; //-1
osteFactorFemale[1][4]  = 9.2; //-2
osteFactorFemale[1][5]  = 13.9; // < -2

//55
osteFactorFemale[2][0]  = 7.8; //over all
osteFactorFemale[2][1]  = 2.6; //1
osteFactorFemale[2][2]  = 4.1; //0
osteFactorFemale[2][3]  = 6.7; //-1
osteFactorFemale[2][4]  = 10.7; //-2
osteFactorFemale[2][5]  = 16.8; // < -2

//60
osteFactorFemale[3][0]  = 10.6; //over all
osteFactorFemale[3][1]  = 3.2; //1
osteFactorFemale[3][2]  = 5.1; //0
osteFactorFemale[3][3]  = 8.2; //-1
osteFactorFemale[3][4]  = "13.0"; //-2
osteFactorFemale[3][5]  = 20.5; // < -2

//65
osteFactorFemale[4][0]  = 14.3; //over all
osteFactorFemale[4][1]  = "4.0"; //1
osteFactorFemale[4][2]  = 6.3; //0
osteFactorFemale[4][3]  = "10.0"; //-1
osteFactorFemale[4][4]  = 15.6; //-2
osteFactorFemale[4][5]  = 24.9; // < -2

//70
osteFactorFemale[5][0]  = 18.9; //over all
osteFactorFemale[5][1]  = 4.3; //1
osteFactorFemale[5][2]  = 7.1; //0
osteFactorFemale[5][3]  = 11.5; //-1
osteFactorFemale[5][4]  = 18.3; //-2
osteFactorFemale[5][5]  = 29.8; // < -2

//75
osteFactorFemale[6][0]  = 22.9; //over all
osteFactorFemale[6][1]  = 4.2; //1
osteFactorFemale[6][2]  = "7.0"; //0
osteFactorFemale[6][3]  = 11.8; //-1
osteFactorFemale[6][4]  = 19.4; //-2
osteFactorFemale[6][5]  = 32.6; // < -2

//80
osteFactorFemale[7][0]  = 26.5; //over all
osteFactorFemale[7][1]  = 4.6; //1
osteFactorFemale[7][2]  = 7.7; //0
osteFactorFemale[7][3]  = 12.7; //-1
osteFactorFemale[7][4]  = 20.5; //-2
osteFactorFemale[7][5]  = 34.4; // < -2

//85
osteFactorFemale[8][0]  = "27.0"; //over all
osteFactorFemale[8][1]  = 4.5; //1
osteFactorFemale[8][2]  = 7.4; //0
osteFactorFemale[8][3]  = "12.0"; //-1
osteFactorFemale[8][4]  = 19.1; //-2
osteFactorFemale[8][5]  = 33.1; // < -2


	
	
	

	
	function calculate(){
				resetAverageChart();
				var ageGroup = 0;
				var retval = "";
				var total = "";
				var age = document.calCorArDi.age.value;
				if 		(age <=  54){ ageGroup = 1 }
				else if ( age <= 59){ ageGroup = 2 }
				else if ( age <= 64){ ageGroup = 3 }
				else if ( age <= 69){ ageGroup = 4 }
				else if ( age <= 74){ ageGroup = 5 }
				else if ( age <= 79){ ageGroup = 6 }
				else if ( age <= 84){ ageGroup = 7 }
				else 				{ ageGroup = 8 }
										
				if (document.calCorArDi.sex[0].checked){
					//alert (ageGroup+" "+document.calCorArDi.tScore.value);
					//alert (osteFactorFemale[1][1]);
					retval = osteFactorFemale[ageGroup][document.calCorArDi.tScore.value];
					total = osteFactorFemale[ageGroup][0];
					
				}else{
					//alert (ageGroup+" "+document.calCorArDi.tScore.value);
					//alert (osteFactorMale[1][1]);
					retval = osteFactorMale[ageGroup][document.calCorArDi.tScore.value];
					total = osteFactorMale[ageGroup][0];
					
				}
					//alert(retval);
				document.getElementById("cell["+ageGroup+"][0]").style.background = "#ccccff";
				document.getElementById("cell["+ageGroup+"][1]").style.background = "#ccccff";				
				document.getElementById("cell["+ageGroup+"][2]").style.background = "#ccccff";
				document.getElementById("cell["+ageGroup+"][3]").style.background = "#ccccff";
				document.getElementById("cell["+ageGroup+"][4]").style.background = "#ccccff";
				document.getElementById("cell["+ageGroup+"][5]").style.background = "#ccccff";
				document.getElementById("cell["+ageGroup+"][6]").style.background = "#ccccff";		
				document.getElementById("cell["+ageGroup+"]["+document.calCorArDi.tScore.value+"]").style.color = "#ff0000";
				
				
				document.second.prediction.value=
					                  "*****************************************\n"
									 +"*<bean:message key="oscarEncounter.calculators.OsteoporoticFracture.msgAverage"/>      \n"
									 +"*<bean:message key="oscarEncounter.calculators.OsteoporoticFracture.msgOsteoporoticFracture"/>                  \n"
									 +"*****************************************\n"	                                
									 +"* <bean:message key="oscarEncounter.calculators.OsteoporoticFracture.msg10YearProb"/>:         "+retval+" %  \n"
									 +"* <bean:message key="oscarEncounter.calculators.OsteoporoticFracture.msgOverall"/>: "+total+" %  \n"
									 +"*****************************************\n";
				
		
	}
	function resetAverageChart(){
	
		var i = 1;
		var j = 1;
		for ( j = 1 ; j < 9 ; j++){
			for ( i = 0 ; i < 7 ; i++){
				document.getElementById("cell["+j+"]["+i+"]").style.background = "#ffffff";			
				document.getElementById("cell["+j+"]["+i+"]").style.color = "#000000";							
			}
		}
		
	}
	
	function switchData(){
		var i = 1;
		var j = 1;
		for ( j = 1 ; j < 9 ; j++){
			for ( i = 0 ; i < 6 ; i++){
		
				//alert (osteFactorMale[j][i]);
				if (document.calCorArDi.sex[0].checked){
				document.getElementById("cell["+j+"]["+i+"]").innerHTML = osteFactorFemale[j][i];
				}else{
				document.getElementById("cell["+j+"]["+i+"]").innerHTML = osteFactorMale[j][i];
				}
			}
		}
//		document.getElementById("cell["+i+"][1]").innerHTML = "Blah";
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
			document.calCorArDi.sex[1].checked = true;
		}else if ( theSex == 'F'){
			document.calCorArDi.sex[0].checked = true;
		}
	}
	if ( theAge != null){		
			document.calCorArDi.age.value = theAge;		
	}
//	alert(QueryString('sex'));
	
//	alert(QueryString('age'));
	//}
	switchData();
        window.focus();
}
</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarEncounter.calculators.OsteoporoticFracture.msgCalculators" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="oscarEncounter.calculators.OsteoporoticFracture.title" /></td>
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
		<table>
			<tr>
				<td>
				<form name="calCorArDi">
				<table>
					<tr>
						<td><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgFemale" />:</td>
						<td><input type="radio" name="sex" value="F" checked
							onClick="switchData();" /></td>
					</tr>
					<tr>
						<td><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgMale" />:</td>
						<td><input type="radio" name="sex" value="M"
							onClick="switchData();" /></td>
					</tr>
					<tr>
						<td><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgAge" />:</td>
						<td><input type="text" name="age" size="4" /></td>
					</tr>
					<tr>
						<td><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgTScore" />:</td>
						<td><select name="tScore">
							<option value="1">1</option>
							<option value="2">0</option>
							<option value="3">-1</option>
							<option value="4">-2</option>
							<option value="5">&lt; -2.5</option>
						</select></td>
					</tr>

					<tr>
						<td colspan="2"><input type="button"
							value="<bean:message key="oscarEncounter.calculators.OsteoporoticFracture.btnCalculate"/>"
							onClick="calculate();" /></td>
					</tr>
					<!--<tr>
										<td><input type="text" name="<bean:message key="oscarEncounter.calculators.OsteoporoticFracture.btnTotalVal"/>" visible="false"/></td>
									</tr>-->
				</table>
				</form>

				<form name="second"><textarea name="prediction" rows="10"
					cols="42" style="overflow: hidden"></textarea></form>
				<input type="button" value="Paste" onClick="write2Parent();" /></td>
				<td>&nbsp;</td>
				<td align="center" valign="top">
				<table class="outline">
					<tr>
						<td><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgCompare" />
						</td>
					</tr>
					<tr>
						<td align="center"><bean:message
							key="oscarEncounter.calculators.OsteoporoticFracture.msgProbability" />
						</td>
					</tr>
					<tr>
						<td>
						<table class="grid" width="100%" align="center" cellspacing="0">
							<tr>
								<td rowspan=2 class="gridTitles"><bean:message
									key="oscarEncounter.calculators.OsteoporoticFracture.msgAge" /></td>
								<td class="gridTitlesWOBottom"><bean:message
									key="oscarEncounter.calculators.OsteoporoticFracture.msgOverall" /></td>
								<td colspan=5 class="gridTitlesWOBottom"><bean:message
									key="oscarEncounter.calculators.OsteoporoticFracture.msgTScore" /></td>

							</tr>
							<tr>
								<td align="center" class="gridTitles"><bean:message
									key="oscarEncounter.calculators.OsteoporoticFracture.msgAvg" /></td>
								<td class="gridTitles">1</td>
								<td class="gridTitles">0</td>
								<td class="gridTitles">-1</td>
								<td class="gridTitles">-2</td>
								<td class="gridTitles"><-5</td>
							</tr>
							<tr>
								<td id="cell[1][6]">50</td>
								<td class="middleGrid" id="cell[1][0]">3.3</td>
								<td class="middleGrid" id="cell[1][1]">1.8</td>
								<td class="middleGrid" id="cell[1][2]">2.7</td>
								<td class="middleGrid" id="cell[1][3]">4.2</td>
								<td class="middleGrid" id="cell[1][4]">6.3</td>
								<td id="cell[1][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[2][6]">55</td>
								<td class="middleGrid" id="cell[2][0]">3.3</td>
								<td class="middleGrid" id="cell[2][1]">1.8</td>
								<td class="middleGrid" id="cell[2][2]">2.7</td>
								<td class="middleGrid" id="cell[2][3]">4.2</td>
								<td class="middleGrid" id="cell[2][4]">6.3</td>
								<td id="cell[2][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[3][6]">60</td>
								<td class="middleGrid" id="cell[3][0]">3.3</td>
								<td class="middleGrid" id="cell[3][1]">1.8</td>
								<td class="middleGrid" id="cell[3][2]">2.7</td>
								<td class="middleGrid" id="cell[3][3]">4.2</td>
								<td class="middleGrid" id="cell[3][4]">6.3</td>
								<td id="cell[3][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[4][6]">65</td>
								<td class="middleGrid" id="cell[4][0]">3.3</td>
								<td class="middleGrid" id="cell[4][1]">1.8</td>
								<td class="middleGrid" id="cell[4][2]">2.7</td>
								<td class="middleGrid" id="cell[4][3]">4.2</td>
								<td class="middleGrid" id="cell[4][4]">6.3</td>
								<td id="cell[4][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[5][6]">70</td>
								<td class="middleGrid" id="cell[5][0]">3.3</td>
								<td class="middleGrid" id="cell[5][1]">1.8</td>
								<td class="middleGrid" id="cell[5][2]">2.7</td>
								<td class="middleGrid" id="cell[5][3]">4.2</td>
								<td class="middleGrid" id="cell[5][4]">6.3</td>
								<td id="cell[5][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[6][6]">75</td>
								<td class="middleGrid" id="cell[6][0]">3.3</td>
								<td class="middleGrid" id="cell[6][1]">1.8</td>
								<td class="middleGrid" id="cell[6][2]">2.7</td>
								<td class="middleGrid" id="cell[6][3]">4.2</td>
								<td class="middleGrid" id="cell[6][4]">6.3</td>
								<td id="cell[6][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[7][6]">80</td>
								<td class="middleGrid" id="cell[7][0]">3.3</td>
								<td class="middleGrid" id="cell[7][1]">1.8</td>
								<td class="middleGrid" id="cell[7][2]">2.7</td>
								<td class="middleGrid" id="cell[7][3]">4.2</td>
								<td class="middleGrid" id="cell[7][4]">6.3</td>
								<td id="cell[7][5]">9.2</td>
							</tr>
							<tr>
								<td id="cell[8][6]">85</td>
								<td class="middleGrid" id="cell[8][0]">3.3</td>
								<td class="middleGrid" id="cell[8][1]">1.8</td>
								<td class="middleGrid" id="cell[8][2]">2.7</td>
								<td class="middleGrid" id="cell[8][3]">4.2</td>
								<td class="middleGrid" id="cell[8][4]">6.3</td>
								<td id="cell[8][5]">9.2</td>
							</tr>
						</table>

						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
