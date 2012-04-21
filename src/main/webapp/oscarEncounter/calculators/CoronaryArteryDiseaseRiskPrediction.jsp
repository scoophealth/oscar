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

<html lang="en">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Coronary Artery Disease Risk Prediction</title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<script type="text/javascript">

        var ageM = new Array();
        var ageF = new Array();
        var HDL  = new Array();
        var TChM  = new Array(5);
        var TChF  = new Array(5);
        var BPuM   = new Array();
        var BPtM   = new Array();
        var BPuF   = new Array();
        var BPtF   = new Array();
        var smokerM = new Array();
        var smokerF = new Array();

        var ageGroup = 0;
        var Total     = 0;
        var i = 0;
        var riskLvl;
        var percent;
        var cellID = 0;
        var lipidLvl;
        
        for (i=0; i < 5; i++) {
        TChM[i] = new Array(5)
        }

        for (i=0; i < 5; i++) {
        TChF[i] = new Array(5)
        }



        //smokerM
        smokerM[0] = 8; //20-39
        smokerM[1] = 5; //40-49
        smokerM[2] = 3; //50-59
        smokerM[3] = 1; //60-69
        smokerM[4] = 1; //>69

        //smokerF
        smokerF[0] = 9; //20-39
        smokerF[1] = 7; //40-49
        smokerF[2] = 4; //50-59
        smokerF[3] = 2; //60-69
        smokerF[4] = 1; //>69

        //FEMALE AGE
        ageF[1]  = -7; //<=34
        ageF[2]  = -3; //35-39
        ageF[3]  = 0; //40-44
        ageF[4]  = 3;  //45-49
        ageF[5]  = 6;  //50-54
        ageF[6]  = 8;  //55-59
        ageF[7]  = 10;  //60-64
        ageF[8]  = 12;  //65-69
        ageF[9]  = 14;  //70-74
        ageF[10] = 16;  //>=75

        //MALE AGE

        ageM[1]  = -9; //<=34
        ageM[2]  = -4; //35-39
        ageM[3]  = 0; //40-44
        ageM[4]  = 3;  //45-49
        ageM[5]  = 6;  //50-54
        ageM[6]  = 8;  //55-59
        ageM[7]  = 10;  //60-64
        ageM[8]  = 11;  //65-69
        ageM[9]  = 12;  //70-74
        ageM[10] = 13;  //>=75

        //HDC
        HDL[1]  = 2;    //<1.04
        HDL[2]  = 1;    //1.04-1.29
        HDL[3]  = 0;    //1.30-1.54
        HDL[4]  = -1;    //>1.54

        //TChM - 20-39

        TChM[0][0]  = 0; //<4.15
        TChM[0][1]  = 4; //4.15-5.19
        TChM[0][2]  = 7; //5.20-6.19
        TChM[0][3]  = 9;  //6.20-7.20
        TChM[0][4]  = 11;  //>7.20

        //TChM - 40-49

        TChM[1][0]  = 0; //<4.15
        TChM[1][1]  = 3; //4.15-5.19
        TChM[1][2]  = 5; //5.20-6.19
        TChM[1][3]  = 6;  //6.20-7.20
        TChM[1][4]  = 8;  //>7.20

        //TChM - 50-59

        TChM[2][0]  = 0; //<4.15
        TChM[2][1]  = 2; //4.15-5.19
        TChM[2][2]  = 3; //5.20-6.19
        TChM[2][3]  = 4;  //6.20-7.20
        TChM[2][4]  = 5;  //>7.20

        //TChM - 60-69

        TChM[3][0]  = 0; //<4.15
        TChM[3][1]  = 1; //4.15-5.19
        TChM[3][2]  = 1; //5.20-6.19
        TChM[3][3]  = 2;  //6.20-7.20
        TChM[3][4]  = 3;  //>7.20

        //TChM - 70-79

        TChM[4][0]  = 0; //<4.15
        TChM[4][1]  = 0; //4.15-5.19
        TChM[4][2]  = 0; //5.20-6.19
        TChM[4][3]  = 1;  //6.20-7.20
        TChM[4][4]  = 1;  //>7.20


        //TChF - 20-29

        TChF[0][0]  = 0; //<4.15
        TChF[0][1]  = 4; //4.15-5.19
        TChF[0][2]  = 8; //5.20-6.19
        TChF[0][3]  = 11;  //6.20-7.20
        TChF[0][4]  = 13;  //>7.20

        //TChF - 40-49

        TChF[1][0]  = 0; //<4.15
        TChF[1][1]  = 3; //4.15-5.19
        TChF[1][2]  = 6; //5.20-6.19
        TChF[1][3]  = 8;  //6.20-7.20
        TChF[1][4]  = 10;  //>7.20

        //TChF - 50-59

        TChF[2][0]  = 0; //<4.15
        TChF[2][1]  = 2; //4.15-5.19
        TChF[2][2]  = 4; //5.20-6.19
        TChF[2][3]  = 5;  //6.20-7.20
        TChF[2][4]  = 7;  //>7.20

        //TChF - 60-69

        TChF[3][0]  = 0; //<4.15
        TChF[3][1]  = 1; //4.15-5.19
        TChF[3][2]  = 2; //5.20-6.19
        TChF[3][3]  = 3;  //6.20-7.20
        TChF[3][4]  = 4;  //>7.20

        //TChF - 70-79

        TChF[4][0]  = 0; //<4.15
        TChF[4][1]  = 1; //4.15-5.19
        TChF[4][2]  = 1; //5.20-6.19
        TChF[4][3]  = 2;  //6.20-7.20
        TChF[4][4]  = 2;  //>7.20



        //BPuM
        BPuM[1]  = 0; //<120
        BPuM[2]  = 0; //120-129
        BPuM[3]  = 1; //130-139
        BPuM[4]  = 1;  //140-159
        BPuM[5]  = 2;  //>159

        //BPtM
        BPtM[1]  = 0; //<120
        BPtM[2]  = 1; //120-129
        BPtM[3]  = 2; //130-139
        BPtM[4]  = 2;  //140-159
        BPtM[5]  = 3;  //>159

        //BPuF
        BPuF[1]  = 0; //<120
        BPuF[2]  = 1; //120-129
        BPuF[3]  = 2; //130-139
        BPuF[4]  = 3;  //140-159
        BPuF[5]  = 4;  //>159

        //BPtF
        BPtF[1]  = 0; //<120
        BPtF[2]  = 3; //120-129
        BPtF[3]  = 4; //130-139
        BPtF[4]  = 5;  //140-159
        BPtF[5]  = 6;  //>159


function calculate(){

        Total = new Number(0);
        var ageFactor = 0
        var age = new Number(document.calCorArDi.age.value);
	
        if (age <= 39){ageGroup = 0; }
        else if (age <= 49){ageGroup = 1; }	
        else if (age <= 59){ageGroup = 2; }
        else if (age <= 69){ageGroup = 3; }
        else if (age > 69){ageGroup = 4; }
            
        if (document.calCorArDi.sex[0].checked){
        
        if (age <= 34){   ageFactor = ageF[1]; }
        else if (age <= 39){ageFactor = ageF[2]; }	
        else if (age <= 44){ageFactor = ageF[3]; }
        else if (age <= 49){ageFactor = ageF[4]; }
        else if (age <= 54){ageFactor = ageF[5]; }
        else if (age <= 59){ageFactor = ageF[6]; }				
        else if (age <= 64){ageFactor = ageF[7]; }
        else if (age <= 69){ageFactor = ageF[8]; }
        else if (age <= 74){ageFactor = ageF[9]; }
        else if (age > 74){ageFactor = ageF[10]; }
										
								
        }else{		
        if (age <= 34){   ageFactor = ageM[1]; }
        else if (age <= 39){ageFactor = ageM[2]; }	
        else if (age <= 44){ageFactor = ageM[3]; }
        else if (age <= 49){ageFactor = ageM[4]; }
        else if (age <= 54){ageFactor = ageM[5]; }
        else if (age <= 59){ageFactor = ageM[6]; }				
        else if (age <= 64){ageFactor = ageM[7]; }
        else if (age <= 69){ageFactor = ageM[8]; }
        else if (age <= 74){ageFactor = ageM[9]; }
        else if (age > 74){ageFactor = ageM[10];}							
																
					
        }
	
	
        //Age Factor
	Total = Total + ageFactor;
	
        //HDL
        Total = Total +  HDL[new Number(document.calCorArDi.HDL.value)];
                
        //TCh
        if (document.calCorArDi.sex[0].checked){
            Total = Total +  TChF[ageGroup][new Number(document.calCorArDi.TCh.value)];
        }else{
            Total = Total +  TChM[ageGroup][new Number(document.calCorArDi.TCh.value)];
        }
	
        if (document.calCorArDi.treated[0].checked){
            if (document.calCorArDi.sex[0].checked){
                Total = Total +  BPuF[new Number(document.calCorArDi.BP.value)];
            }else{
                Total = Total +  BPuM[new Number(document.calCorArDi.BP.value)];
            }
        }else{
            if (document.calCorArDi.sex[0].checked){
                Total = Total +  BPtF[new Number(document.calCorArDi.BP.value)];
            }else{
                Total = Total +  BPtM[new Number(document.calCorArDi.BP.value)];
            }
        }


        
        //alert(Total);
        if (document.calCorArDi.cigs.checked){
            if (document.calCorArDi.sex[0].checked){
                Total = Total +  smokerF[ageGroup];
            }else{
                Total = Total +  smokerM[ageGroup];
            }
        }
	
        
        
	 
    if (document.calCorArDi.sex[0].checked){
        if (Total > 22){
            riskLvl = "HIGH";
            lipidLvl = "LDL-C Level(mmol/L) - < 2.0           \n* TC/HDL-C Ratio - < 4.0\n";
        }
        else if (Total < 20){
            riskLvl = "LOW";
            lipidLvl = "LDL-C Level(mmol/L) - < 5.0           \n* TC/HDL-C Ratio - < 6.0\n";
        }
        else{
            riskLvl = "MODERATE";
            lipidLvl = "LDL-C Level(mmol/L) - < 3.5           \n* TC/HDL-C Ratio - < 5.0\n";
        }   
    }else{
        if (Total > 14){
            riskLvl = "HIGH";
            lipidLvl = "LDL-C Level(mmol/L) - < 2.0           \n* TC/HDL-C Ratio - < 4.0\n";
        }
        else if (Total < 13){
            riskLvl = "LOW";
            lipidLvl = "LDL-C Level(mmol/L) - < 5.0           \n* TC/HDL-C Ratio - < 6.0\n";
        }
        else{
            riskLvl = "MODERATE";
            lipidLvl = "LDL-C Level(mmol/L) - < 3.5           \n* TC/HDL-C Ratio - < 5.0\n";
        } 
    }
                
    setChart(Total);
    
    if (cellID == 1){
        percent = "< 1%";
    }
    else if (cellID == 14){
        percent = ">=30%";
    }
    else{

        if (document.calCorArDi.sex[0].checked){
            percent = document.getElementById('2cellR'+cellID).innerHTML;
        }
        else{
            percent = document.getElementById('cellR'+cellID).innerHTML;
        }
    }
        
        
        if (document.calCorArDi.diabetic.checked){
            resetAverageChart();
            document.second.prediction.value="*****************************************\n"
                                        +"*Coronary Artery Disease Risk Prediction\n"
                                        +"*****************************************\n"
                                        +"* Total Point Count: "+Total+"           \n"
                                        +"* 10-year Risk: HIGH >= 20%              \n"
					+"*****************************************\n"
					+"*Patients with established CVD, diabetes \n"
                                        +"*or any atherosclerotic disease are      \n"
                                        +"*automatically considered high risk      \n"
                                        +"*with a >=20% 10-year risk.              \n"
                                        +"*****************************************\n"
                                        +"* Based on risk, your target lipid       \n"
                                        +"* levels should be:                      \n"
                                        +"* LDL-C Level(mmol/L) - < 2.0            \n* TC/HDL-C Ratio - < 4.0\n"
                                        +"*****************************************";
	}else{
            document.second.prediction.value="*****************************************\n"
                                        +"*Coronary Artery Disease Risk Prediction\n"
                                        +"*****************************************\n"
                                        +"* Total Point Count:  "+Total+  "        \n"
                                        +"* 10-year Risk: "+riskLvl+" - "+percent+"\n"
					+"*****************************************\n"
					+"* Based on risk, your target lipid       \n"
                                        +"* levels should be:                      \n"
					+"* "+lipidLvl
					+"*****************************************";
                                        
	}
	
}
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

function setChart(Total){
        resetAverageChart();
        
    
        if (document.calCorArDi.sex[0].checked){
            if (Total <  9){ cellID = 1 }
            else if ( Total <= 12){ cellID = 2 }
            else if ( Total <= 14){ cellID = 3 }
            else if ( Total <= 15){ cellID = 4 }
            else if ( Total <= 16){ cellID = 5 }
            else if ( Total <= 17){ cellID = 6 }
            else if ( Total <= 18){ cellID = 7 }
            else if ( Total <= 19){ cellID = 8 }
            else if ( Total <= 20){ cellID = 9 }
            else if ( Total <= 21){ cellID = 10 }
            else if ( Total <= 22){ cellID = 11 }
            else if ( Total <= 23){ cellID = 12 }
            else if ( Total <= 24){ cellID = 13 }
            else if ( Total >= 25){ cellID = 14 }
        
        }else{ 				  
            if (Total <  0){ cellID = 1 }
            else if ( Total <= 4){ cellID = 2 }
            else if ( Total <= 6){ cellID = 3 }
            else if ( Total <= 7){ cellID = 4 }
            else if ( Total <= 8){ cellID = 5 }
            else if ( Total <= 9){ cellID = 6 }
            else if ( Total <= 10){ cellID = 7 }
            else if ( Total <= 11){ cellID = 8 }
            else if ( Total <= 12){ cellID = 9 }
            else if ( Total <= 13){ cellID = 10 }
            else if ( Total <= 14){ cellID = 11 }
            else if ( Total <= 15){ cellID = 12 }
            else if ( Total <= 16){ cellID = 13 }
            else if ( Total >= 17){ cellID = 14 }
        }
        
        
if (document.calCorArDi.sex[0].checked){
    if (Total > 22){
        var CellStyle = document.getElementById('2cellL'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellC'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellR'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
    }
    else if (Total < 20){
        var CellStyle = document.getElementById('2cellL'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellC'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellR'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
    }
    else{
        var CellStyle = document.getElementById('2cellL'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellC'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('2cellR'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
    }   
}
else{
    if (Total > 14){
        var CellStyle = document.getElementById('cellL'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellC'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellR'+cellID).style;
        CellStyle.background = "#ff0000"; // otherwise, color it blue
        CellStyle.color = "#000000";
    }
    else if (Total < 13){
        var CellStyle = document.getElementById('cellL'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellC'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellR'+cellID).style;
        CellStyle.background = "#99FF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
    }
    else{
        var CellStyle = document.getElementById('cellL'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellC'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
        var CellStyle = document.getElementById('cellR'+cellID).style;
        CellStyle.background = "#FFFF00"; // otherwise, color it blue
        CellStyle.color = "#000000";
    } 
}

        
}
    
function resetAverageChart(){
        document.getElementById('cellL1').style.background = "#ffffff";
        document.getElementById('cellC1').style.background = "#ffffff";
        document.getElementById('cellR1').style.background = "#ffffff";
        document.getElementById('cellL1').style.color = "#000000";
        document.getElementById('cellC1').style.color = "#000000";
        document.getElementById('cellR1').style.color = "#000000";
		
        document.getElementById('cellL2').style.background = "#ffffff";
        document.getElementById('cellC2').style.background = "#ffffff";
        document.getElementById('cellR2').style.background = "#ffffff";
        document.getElementById('cellL2').style.color = "#000000";
        document.getElementById('cellC2').style.color = "#000000";
        document.getElementById('cellR2').style.color = "#000000";
	 
        document.getElementById('cellL3').style.background = "#ffffff";
        document.getElementById('cellC3').style.background = "#ffffff";
        document.getElementById('cellR3').style.background = "#ffffff";
        document.getElementById('cellL3').style.color = "#000000";
        document.getElementById('cellC3').style.color = "#000000";
        document.getElementById('cellR3').style.color = "#000000";
    												 
        document.getElementById('cellL4').style.background = "#ffffff";
        document.getElementById('cellC4').style.background = "#ffffff";
        document.getElementById('cellR4').style.background = "#ffffff";	
        document.getElementById('cellL4').style.color = "#000000";
        document.getElementById('cellC4').style.color = "#000000";
        document.getElementById('cellR4').style.color = "#000000";
												 
        document.getElementById('cellL5').style.background = "#ffffff";
        document.getElementById('cellC5').style.background = "#ffffff";
        document.getElementById('cellR5').style.background = "#ffffff";
        document.getElementById('cellL5').style.color = "#000000";
        document.getElementById('cellC5').style.color = "#000000";
        document.getElementById('cellR5').style.color = "#000000";
		
        document.getElementById('cellL6').style.background = "#ffffff";
        document.getElementById('cellC6').style.background = "#ffffff";
        document.getElementById('cellR6').style.background = "#ffffff";				
        document.getElementById('cellL6').style.color = "#000000";
        document.getElementById('cellC6').style.color = "#000000";
        document.getElementById('cellR6').style.color = "#000000";
											
        document.getElementById('cellL7').style.background = "#ffffff";
        document.getElementById('cellC7').style.background = "#ffffff";
        document.getElementById('cellR7').style.background = "#ffffff";	
        document.getElementById('cellL7').style.color = "#000000";
        document.getElementById('cellC7').style.color = "#000000";
        document.getElementById('cellR7').style.color = "#000000";
												 
        document.getElementById('cellL8').style.background = "#ffffff";
        document.getElementById('cellC8').style.background = "#ffffff";
        document.getElementById('cellR8').style.background = "#ffffff";				
        document.getElementById('cellL8').style.color = "#000000";
        document.getElementById('cellC8').style.color = "#000000";
        document.getElementById('cellR8').style.color = "#000000";
											
        document.getElementById('cellL9').style.background = "#ffffff";
        document.getElementById('cellC9').style.background = "#ffffff";
        document.getElementById('cellR9').style.background = "#ffffff";				
        document.getElementById('cellL9').style.color = "#000000";
        document.getElementById('cellC9').style.color = "#000000";
        document.getElementById('cellR9').style.color = "#000000";
        
        document.getElementById('cellL10').style.background = "#ffffff";
        document.getElementById('cellC10').style.background = "#ffffff";
        document.getElementById('cellR10').style.background = "#ffffff";			
        document.getElementById('cellL10').style.color = "#000000";
        document.getElementById('cellC10').style.color = "#000000";
        document.getElementById('cellR10').style.color = "#000000";
        
        document.getElementById('cellL11').style.background = "#ffffff";
        document.getElementById('cellC11').style.background = "#ffffff";
        document.getElementById('cellR11').style.background = "#ffffff";			
        document.getElementById('cellL11').style.color = "#000000";
        document.getElementById('cellC11').style.color = "#000000";
        document.getElementById('cellR11').style.color = "#000000";
        
        document.getElementById('cellL12').style.background = "#ffffff";
        document.getElementById('cellC12').style.background = "#ffffff";
        document.getElementById('cellR12').style.background = "#ffffff";			
        document.getElementById('cellL12').style.color = "#000000";
        document.getElementById('cellC12').style.color = "#000000";
        document.getElementById('cellR12').style.color = "#000000";
        
        document.getElementById('cellL13').style.background = "#ffffff";
        document.getElementById('cellC13').style.background = "#ffffff";
        document.getElementById('cellR13').style.background = "#ffffff";		
        document.getElementById('cellL13').style.color = "#000000";
        document.getElementById('cellC13').style.color = "#000000";
        document.getElementById('cellR13').style.color = "#000000";
        
        document.getElementById('cellL14').style.background = "#ffffff";
        document.getElementById('cellC14').style.background = "#ffffff";
        document.getElementById('cellR14').style.background = "#ffffff";			
        document.getElementById('cellL14').style.color = "#000000";
        document.getElementById('cellC14').style.color = "#000000";
        document.getElementById('cellR14').style.color = "#000000";
        
        
        
        
        document.getElementById('2cellL1').style.background = "#ffffff";
        document.getElementById('2cellC1').style.background = "#ffffff";
        document.getElementById('2cellR1').style.background = "#ffffff";
        document.getElementById('2cellL1').style.color = "#000000";
        document.getElementById('2cellC1').style.color = "#000000";
        document.getElementById('2cellR1').style.color = "#000000";
		
        document.getElementById('2cellL2').style.background = "#ffffff";
        document.getElementById('2cellC2').style.background = "#ffffff";
        document.getElementById('2cellR2').style.background = "#ffffff";
        document.getElementById('2cellL2').style.color = "#000000";
        document.getElementById('2cellC2').style.color = "#000000";
        document.getElementById('2cellR2').style.color = "#000000";
	 
        document.getElementById('2cellL3').style.background = "#ffffff";
        document.getElementById('2cellC3').style.background = "#ffffff";
        document.getElementById('2cellR3').style.background = "#ffffff";
        document.getElementById('2cellL3').style.color = "#000000";
        document.getElementById('2cellC3').style.color = "#000000";
        document.getElementById('2cellR3').style.color = "#000000";
    												 
        document.getElementById('2cellL4').style.background = "#ffffff";
        document.getElementById('2cellC4').style.background = "#ffffff";
        document.getElementById('2cellR4').style.background = "#ffffff";
        document.getElementById('cellL4').style.color = "#000000";
        document.getElementById('cellC4').style.color = "#000000";
        document.getElementById('cellR4').style.color = "#000000";
												 
        document.getElementById('2cellL5').style.background = "#ffffff";
        document.getElementById('2cellC5').style.background = "#ffffff";
        document.getElementById('2cellR5').style.background = "#ffffff";
        document.getElementById('2cellL5').style.color = "#000000";
        document.getElementById('2cellC5').style.color = "#000000";
        document.getElementById('2cellR5').style.color = "#000000";
		
        document.getElementById('2cellL6').style.background = "#ffffff";
        document.getElementById('2cellC6').style.background = "#ffffff";
        document.getElementById('2cellR6').style.background = "#ffffff";			
        document.getElementById('2cellL6').style.color = "#000000";
        document.getElementById('2cellC6').style.color = "#000000";
        document.getElementById('2cellR6').style.color = "#000000";
											
        document.getElementById('2cellL7').style.background = "#ffffff";
        document.getElementById('2cellC7').style.background = "#ffffff";
        document.getElementById('2cellR7').style.background = "#ffffff";
        document.getElementById('2cellL7').style.color = "#000000";
        document.getElementById('2cellC7').style.color = "#000000";
        document.getElementById('2cellR7').style.color = "#000000";
												 
        document.getElementById('2cellL8').style.background = "#ffffff";
        document.getElementById('2cellC8').style.background = "#ffffff";
        document.getElementById('2cellR8').style.background = "#ffffff";		
        document.getElementById('2cellL8').style.color = "#000000";
        document.getElementById('2cellC8').style.color = "#000000";
        document.getElementById('2cellR8').style.color = "#000000";
											
        document.getElementById('2cellL9').style.background = "#ffffff";
        document.getElementById('2cellC9').style.background = "#ffffff";
        document.getElementById('2cellR9').style.background = "#ffffff";			
        document.getElementById('2cellL9').style.color = "#000000";
        document.getElementById('2cellC9').style.color = "#000000";
        document.getElementById('2cellR9').style.color = "#000000";
        
        document.getElementById('2cellL10').style.background = "#ffffff";
        document.getElementById('2cellC10').style.background = "#ffffff";
        document.getElementById('2cellR10').style.background = "#ffffff";			
        document.getElementById('2cellL10').style.color = "#000000";
        document.getElementById('2cellC10').style.color = "#000000";
        document.getElementById('2cellR10').style.color = "#000000";
        
        document.getElementById('2cellL11').style.background = "#ffffff";
        document.getElementById('2cellC11').style.background = "#ffffff";
        document.getElementById('2cellR11').style.background = "#ffffff";			
        document.getElementById('2cellL11').style.color = "#000000";
        document.getElementById('2cellC11').style.color = "#000000";
        document.getElementById('2cellR11').style.color = "#000000";
        
        document.getElementById('2cellL12').style.background = "#ffffff";
        document.getElementById('2cellC12').style.background = "#ffffff";
        document.getElementById('2cellR12').style.background = "#ffffff";			
        document.getElementById('2cellL12').style.color = "#000000";
        document.getElementById('2cellC12').style.color = "#000000";
        document.getElementById('2cellR12').style.color = "#000000";
        
        document.getElementById('2cellL13').style.background = "#ffffff";
        document.getElementById('2cellC13').style.background = "#ffffff";
        document.getElementById('2cellR13').style.background = "#ffffff";		
        document.getElementById('2cellL13').style.color = "#000000";
        document.getElementById('2cellC13').style.color = "#000000";
        document.getElementById('2cellR13').style.color = "#000000";
        
        document.getElementById('2cellL14').style.background = "#ffffff";
        document.getElementById('2cellC14').style.background = "#ffffff";
        document.getElementById('2cellR14').style.background = "#ffffff";			
        document.getElementById('2cellL14').style.color = "#000000";
        document.getElementById('2cellC14').style.color = "#000000";
        document.getElementById('2cellR14').style.color = "#000000";
        
}
        </script>
<script Language="JavaScript">
            
            // QueryString
            
    function QueryString(key){
            var value = null;
            for (var i=0;i<QueryString.keys.length;i++){
                if (QueryString.keys[i]==key){
                    value = QueryString.values[i];
                    break;
                }
            }
            return value;
    }
            QueryString.keys = new Array();
            QueryString.values = new Array();

    function QueryString_Parse(){
            var query = window.location.search.substring(1);
            var pairs = query.split("&");
	
            for (var i=0;i<pairs.length;i++){
		
                var pos = pairs[i].indexOf('=');
                if (pos >= 0){
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
            
            window.focus();
}
        </script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Calculators: Coronary
		Artery Disease Risk Prediction</td>
		<td style="text-align: right"><oscar:help keywords="calculator" key="app.top1"/> | <a
			href="javascript:
                    popupStart(300,400,'About.jsp')">About</a>
		| <a href="javascript:popupStart(300,400,'License.jsp')">License</a>
		</td>
	</tr>
	<tr>

		<td class="MainTableRightColumn" colspan="2">
		<table>
			<tr>
				<td>
				<form name="calCorArDi">
				<table>
					<tr>
						<td>Female:</td>
						<td><input type="radio" name="sex" value="F" checked /></td>
					</tr>
					<tr>
						<td>Male:</td>
						<td><input type="radio" name="sex" value="M" /></td>
					</tr>
					<tr>
						<td>Age:</td>
						<td><input type="text" name="age" size="4" /></td>
					</tr>
					<tr>
						<td>HDL-Cholesterol:</td>

						<td><select name="HDL">
							<option value="1"><1.04</option>
							<option value="2">1.04-1.29</option>
							<option value="3">1.30-1.54</option>
							<option value="4">>1.54</option>
						</select></td>
					</tr>
					<tr>
						<td>Total-Cholesterol:</td>
						<td><select name="TCh">
							<option value="0"><4.15</option>
							<option value="1">4.15-5.19</option>
							<option value="2">5.20-6.19</option>
							<option value="3">6.20-7.20</option>
							<option value="4">>7.20</option>
						</select></td>
					</tr>
					<tr>
						<td>Systolic BP:</td>
						<td><select name="BP">
							<option value="1"><120</option>
							<option value="2">120-129</option>
							<option value="3">130-139</option>
							<option value="4">140-159</option>
							<option value="5">>159</option>
						</select></td>
						<td>Treated:</td>
						<td>No</td>
						<td><input type="radio" name="treated" value="no" checked /></td>
						<td>Yes</td>
						<td><input type="radio" name="treated" value="yes" /></td>

					</tr>
					<tr>
						<td>Smoker:</td>
						<td><input type="checkbox" name="cigs" /></td>
					</tr>
					<tr>
						<td>Diabetic:</td>
						<td><input type="checkbox" name="diabetic" /></td>
					</tr>
					<tr>

					</tr>
					<tr>
						<td colspan="2"><input type="button" value="Calculate"
							onClick="calculate();" /></td>
					</tr>
					<!--<tr>
                        <td><input type="text" name="totalVal" 
                        visible="false"/></td>
                        </tr>-->
				</table>
				</form>

				<form name="second"><textarea name="prediction" rows="12"
					cols="43" style="overflow: auto"></textarea></form>

				<input type="button" value="Paste" onClick="write2Parent();" /></td>
				<td>&nbsp;</td>
				<td align="center" valign="top">
				<table class="outline">
					<tr>
						<td align="center">10-year Risk (%)</td>
					</tr>
					<tr>
						<td align="center">Male</td>
					</tr>
					<tr>
						<td>
						<table class="grid" width="100%" align="center" cellspacing="0">
							<tr>
								<td align="center" class="gridTitles">Total Risk Points</td>
								<td align="center" class="gridTitles">-</td>
								<td align="center" class="gridTitles">10-year Risk</td>
							</tr>
							<tr>
								<td id="cellL1" align="center">< 0</td>
								<td id="cellC1" align="center" class="middleGrid">-</td>
								<td id="cellR1" align="center">< 1%</td>
							</tr>
							<tr>
								<td id="cellL2" align="center">0 - 4</td>
								<td id="cellC2" align="center" class="middleGrid">-</td>
								<td id="cellR2" align="center">1%</td>
							</tr>
							<tr>
								<td id="cellL3" align="center">5 - 6</td>
								<td id="cellC3" align="center" class="middleGrid">-</td>
								<td id="cellR3" align="center">2%</td>
							</tr>
							<tr>
								<td id="cellL4" align="center">7</td>
								<td id="cellC4" align="center" class="middleGrid">-</td>
								<td id="cellR4" align="center">3%</td>
							</tr>
							<tr>
								<td id="cellL5" align="center">8</td>
								<td id="cellC5" align="center" class="middleGrid">-</td>
								<td id="cellR5" align="center">4%</td>
							</tr>
							<tr>
								<td id="cellL6" align="center">9</td>
								<td id="cellC6" align="center" class="middleGrid">-</td>
								<td id="cellR6" align="center">5%</td>
							</tr>
							<tr>
								<td id="cellL7" align="center">10</td>
								<td id="cellC7" align="center" class="middleGrid">-</td>
								<td id="cellR7" align="center">6%</td>
							</tr>
							<tr>
								<td id="cellL8" align="center">11</td>
								<td id="cellC8" align="center" class="middleGrid">-</td>
								<td id="cellR8" align="center">8%</td>
							</tr>
							<tr>
								<td id="cellL9" align="center">12</td>
								<td id="cellC9" align="center" class="middleGrid">-</td>
								<td id="cellR9" align="center">10%</td>
							</tr>
							<tr>
								<td id="cellL10" align="center">13</td>
								<td id="cellC10" align="center" class="middleGrid">-</td>
								<td id="cellR10" align="center">12%</td>
							</tr>
							<tr>
								<td id="cellL11" align="center">14</td>
								<td id="cellC11" align="center" class="middleGrid">-</td>
								<td id="cellR11" align="center">16%</td>
							</tr>
							<tr>
								<td id="cellL12" align="center">15</td>
								<td id="cellC12" align="center" class="middleGrid">-</td>
								<td id="cellR12" align="center">20%</td>
							</tr>
							<tr>
								<td id="cellL13" align="center">16</td>
								<td id="cellC13" align="center" class="middleGrid">-</td>
								<td id="cellR13" align="center">25%</td>
							</tr>
							<tr>
								<td id="cellL14" align="center">>=17</td>
								<td id="cellC14" align="center" class="middleGrid">-</td>
								<td id="cellR14" align="center">>=30%</td>
							</tr>

						</table>
						</td>
					</tr>
				</table>
				</td>
				<td valign="top">
				<table class="outline">
					<tr>
						<td align="center">10-year Risk (%)</td>
					</tr>
					<tr>
						<td align="center">Female</td>
					</tr>
					<tr>
						<td>
						<table class="grid" width="100%" align="center" cellspacing="0">
							<tr>
								<td align="center" class="gridTitles">Total Risk Points</td>
								<td align="center" class="gridTitles">-</td>
								<td align="center" class="gridTitles">10-year Risk</td>
							</tr>
							<tr>
								<td id="2cellL1" align="center">< 9</td>
								<td id="2cellC1" align="center" class="middleGrid">-</td>
								<td id="2cellR1" align="center">< 1%</td>
							</tr>
							<tr>
								<td id="2cellL2" align="center">9 - 12</td>
								<td id="2cellC2" align="center" class="middleGrid">-</td>
								<td id="2cellR2" align="center">1%</td>
							</tr>
							<tr>
								<td id="2cellL3" align="center">13 - 14</td>
								<td id="2cellC3" align="center" class="middleGrid">-</td>
								<td id="2cellR3" align="center">2%</td>
							</tr>
							<tr>
								<td id="2cellL4" align="center">15</td>
								<td id="2cellC4" align="center" class="middleGrid">-</td>
								<td id="2cellR4" align="center">3%</td>
							</tr>
							<tr>
								<td id="2cellL5" align="center">16</td>
								<td id="2cellC5" align="center" class="middleGrid">-</td>
								<td id="2cellR5" align="center">4%</td>
							</tr>
							<tr>
								<td id="2cellL6" align="center">17</td>
								<td id="2cellC6" align="center" class="middleGrid">-</td>
								<td id="2cellR6" align="center">5%</td>
							</tr>
							<tr>
								<td id="2cellL7" align="center">18</td>
								<td id="2cellC7" align="center" class="middleGrid">-</td>
								<td id="2cellR7" align="center">6%</td>
							</tr>
							<tr>
								<td id="2cellL8" align="center">19</td>
								<td id="2cellC8" align="center" class="middleGrid">-</td>
								<td id="2cellR8" align="center">8%</td>
							</tr>
							<tr>
								<td id="2cellL9" align="center">20</td>
								<td id="2cellC9" align="center" class="middleGrid">-</td>
								<td id="2cellR9" align="center">11%</td>
							</tr>
							<tr>
								<td id="2cellL10" align="center">21</td>
								<td id="2cellC10" align="center" class="middleGrid">-</td>
								<td id="2cellR10" align="center">14%</td>
							</tr>
							<tr>
								<td id="2cellL11" align="center">22</td>
								<td id="2cellC11" align="center" class="middleGrid">-</td>
								<td id="2cellR11" align="center">17%</td>
							</tr>
							<tr>
								<td id="2cellL12" align="center">23</td>
								<td id="2cellC12" align="center" class="middleGrid">-</td>
								<td id="2cellR12" align="center">22%</td>
							</tr>
							<tr>
								<td id="2cellL13" align="center">24</td>
								<td id="2cellC13" align="center" class="middleGrid">-</td>
								<td id="2cellR13" align="center">27%</td>
							</tr>
							<tr>
								<td id="2cellL14" align="center">>=25</td>
								<td id="2cellC14" align="center" class="middleGrid">-</td>
								<td id="2cellR14" align="center">>=30%</td>
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
</html>
