/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.labs.alberta;

public class TestLabs {

	final static String LAB01 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203122426||ORU^R01|Q199816391T198313508|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439429^101LA|2921834^URINE MICROSCOPIC^L01N|||20101203122200|||||||20101203122200|^^|1001745^Test, Physician - p-Test Physician||||10-337-300046||20101203122422||LA|F||1^^^20101203122200^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|4669219^WBC^L01N||6-10|/HPF|0-5|A|||F|||20101203122419\r" + 
			"OBX|2|ST|4669186^RBC^L01N||6-10|/HPF|0-5|A|||F|||20101203122419\r" + 
			"OBX|3|ST|4676194^EPITHELIAL CELLS^L01N||Few|/HPF|||||F|||20101203122419\r" + 
			"OBX|4|ST|4668988^URINE BACTERIA^L01N||Few|/HPF|||||F|||20101203122419\r" + 
			"OBX|5|ST|4668928^HYALINE CASTS^L01N||0-4|/LPF|||||F|||20101203122419\r";
	
	final static String LAB02 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203122622||ORU^R01|Q199816397T198313514|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439443^101LA|4356952^URINALYSIS^L01N|||20101203122500|||||||20101203122500|^^|1001745^Test, Physician - p-Test Physician||||10-337-300047||20101203122619||LA|F||1^^^20101203122500^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|6810081^Urinalysis Comment^L01N||See Note||||||F|||20101203122617\r" + 
			"NTE|1|C|Microscopic examination not performed due to negative Blood, Nitrite,\r" + 
			"NTE|2|C|Leukocyte Esterase and Protein.\r";
	
	final static String LAB03 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203122634||ORU^R01|Q199816399T198313516|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439443^101LA|4356952^URINALYSIS^L01N|||20101203122500|||||||20101203122500|^^|1001745^Test, Physician - p-Test Physician||||10-337-300047||20101203122631||LA|F||1^^^20101203122500^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|4669021^COLOR^L01N||Yellow||||||F|||20101203122628\r" + 
			"OBX|2|ST|4668985^APPEARANCE^L01N||Clear||||||F|||20101203122628\r" + 
			"OBX|3|ST|4673824^SPECIFIC GRAVITY^L01N||1.010||<=1.030||||F|||20101203122628\r" + 
			"OBX|4|ST|4673257^PH^L01N||6.0||5.0-8.5||||F|||20101203122628\r" + 
			"OBX|5|ST|4669045^LEUKOCYTE^L01N||Negative||Negative||||F|||20101203122628\r" + 
			"OBX|6|ST|4669051^NITRITE^L01N||Negative||Negative||||F|||20101203122628\r" + 
			"OBX|7|ST|3316682^PROTEIN^L01N||Negative|g/L|Negative||||F|||20101203122628\r" + 
			"OBX|8|ST|4673845^GLUCOSE^L01N||Negative|mmol/L|Negative||||F|||20101203122628\r" + 
			"OBX|9|ST|4669276^KETONES^L01N||Negative||Negative||||F|||20101203122628\r" + 
			"OBX|10|ST|4669003^BLOOD^L01N||Negative||Negative||||F|||20101203122628\r" + 
			"OBX|11|TX|6810081^Urinalysis Comment^L01N||See Note||||||F|||20101203122617\r" + 
			"NTE|1|C|Microscopic examination not performed due to negative Blood, Nitrite,\r" + 
			"NTE|2|C|Leukocyte Esterase and Protein.\r";
	
	final static String LAB04 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203122801||ORU^R01|Q199816401T198313518|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439370^101LA|2921852^VITAMIN D (25-HYDROXY)^L01N|||20101203121800|||||||20101203121900|^^|1001745^Test, Physician - p-Test Physician||||10-337-300042||20101203122758||LA|F||1^^^20101203121800^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|4674013^VITAMIN D (25-HYDROXY)^L01N||<10.1|nmol/L|80.0-200.0|L|||F|||20101203122756\r" + 
			"NTE|1|C|Specimen slightly hemolyzed.\r" + 
			"NTE|2|I|Result Interpretation as Follows:\r" + 
			"NTE|3|I|Severe Deficiency               <25.0         nmol/L\r" + 
			"NTE|4|I|Moderate To Mild Deficiency    25.0 - 80.0   nmol/L\r" + 
			"NTE|5|I|Optimum Levels                  80.0 - 200.0  nmol/L\r" + 
			"NTE|6|I|Toxicity Possible               >250.0       nmol/L\r";
	
	final static String LAB05 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203122846||ORU^R01|Q199816407T198313524|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439455^101LA|2921495^D-DIMER (DVT/PE)^L01N|||20101203122800|||||||20101203122800|^^|1001745^Test, Physician - p-Test Physician||||10-337-300048||20101203122844||LA|F||1^^^20101203122800^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4669105^D-DIMER^L01N||0.55|mg/L FEU|<=0.50|H|||F|||20101203122841\r" + 
			"NTE|1|I|A D-Dimer level less than 0.51 mg/L FEU may be used with a standardized\r" + 
			"NTE|2|I|clinical assessment and/or imaging studies to help exclude venous\r" + 
			"NTE|3|I|thromboembolism.  Values above the cut-off are not diagnostically used for VTE\r" + 
			"NTE|4|I|assessment.\r" + 
			"";
	
	final static String LAB06 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101203123040||ORU^R01|Q199816411T198313528|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439373^101LA|2921802^DRUG SCREEN URINE - TOXICOLOGY^L01N|||20101203121800|||||||20101203122000|^^|1001745^Test, Physician - p-Test Physician||||10-337-300043||20101203123037||LA|F||1^^^20101203121800^^RT~^^^20101203121800^^RT|\r" + 
			"OBX|1|ST|4677619^URINE TOXICOLOGY REPORT^L01N||prelimiary findings show the presence of cannabiniods||||||F|||20101203123034\r" + 
			"NTE|1|I|Results are intended to be used for medical (i.e. treatment) purposes.\r" + 
			"";
	
	final static String LAB07 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209094914||ORU^R01|Q199820946T198318041|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439468^101LA|12104818^DRUG SCREEN URINE - FINAL^L01N|||20101203121800|||||||20101203122000|^^|1001745^Test, Physician - p-Test Physician||||10-337-300043||20101209094921||LA|F||1^^^20101203121800^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|12152702^Drug Screen Urine Final^L01N||The presence of Cannabiniods have been confirmed by GC-MS testing.||||||F|||20101209094915\r" + 
			"";
	
	final static String LAB08 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209104801||ORU^R01|Q199821192T198318291|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439373^101LA|2921802^DRUG SCREEN URINE - TOXICOLOGY^L01N|||20101203121800|||||||20101203122000|^^|1001745^Test, Physician - p-Test Physician||||10-337-300043||20101203123037||LA|F||1^^^20101203121800^^RT~^^^20101203121800^^RT|\r" + 
			"OBX|1|ST|4677619^URINE TOXICOLOGY REPORT^L01N||prelimiary findings show the presence of cannabiniods  *************************************** ERROR - Disregard above report ***************************************||||||C|||20101209104802\r" + 
			"NTE|1|I|Results are intended to be used for medical (i.e. treatment) purposes.\r" + 
			"NTE|2|I|Results are intended to be used for medical (i.e. treatment) purposes.\r" + 
			"NTE|3||Corrected from   [NA] on 12/03/2010 12:30:34 MDT by MLTGL.\r" + 
			"";
	
	final static String LAB09 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209104801||ORU^R01|Q199821194T198318293|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439468^101LA|12104818^DRUG SCREEN URINE - FINAL^L01N|||20101203121800|||||||20101203122000|^^|1001745^Test, Physician - p-Test Physician||||10-337-300043||20101209094921||LA|F||1^^^20101203121800^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|12152702^Drug Screen Urine Final^L01N||The presence of Cannabiniods have been confirmed by GC-MS testing||||||C|||20101209104802\r" + 
			"OBX|2|ST|12152702^Drug Screen Urine Final^L01N||||||||C|||20101209104802\r" + 
			"OBX|3|ST|12152702^Drug Screen Urine Final^L01N||**********************************************||||||C|||20101209104802\r" + 
			"OBX|4|ST|12152702^Drug Screen Urine Final^L01N||ERROR- Please disregard above report||||||C|||20101209104802\r" + 
			"OBX|5|ST|12152702^Drug Screen Urine Final^L01N||**********************************************||||||C|||20101209104802\r" + 
			"OBX|6|ST|12152702^Drug Screen Urine Final^L01N||GC-MS testing has confirmed the presence of Barbiturate metabolites||||||C|||20101209104802\r" + 
			"";
	
	final static String LAB10 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209121440||ORU^R01|Q199821404T198318506|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448776^101LA|2921521^FOLLICLE STIMULATING HORMONE^L01N|||20101209121300|||||||20101209121300|^^|1001745^Test, Physician - p-Test Physician||||10-343-300087||20101209121448||LA|F||1^^^20101209121300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673182^FSH^L01N||16|IU/L|||||F|||20101209121442\r" + 
			"";
	
	final static String LAB11 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209121441||ORU^R01|Q199821406T198318508|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448776^101LA|2921521^FOLLICLE STIMULATING HORMONE^L01N|||20101209121300|||||||20101209121300|^^|1001745^Test, Physician - p-Test Physician||||10-343-300087||20101209121448||LA|F||1^^^20101209121300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673182^FSH^L01N||16|IU/L|||||F|||20101209121442\r" + 
			"OBX|2|TX|6810151^FSH COMMENT^L01N||See Note||||||F|||20101209121443\r" + 
			"NTE|1|C|Female Reference Ranges:\r" + 
			"NTE|2|C|Follicular Phase:     2 - 10 IU/L\r" + 
			"NTE|3|C|Mid-Cycle Peak:     3 - 33 IU/L\r" + 
			"NTE|4|C|Luteal Phase:          1 - 9 IU/L\r" + 
			"NTE|5|C|Post-Menopausal:   23 - 116 IU/L\r" + 
			"NTE|6|C|                                           .\r" + 
			"";
	
	final static String LAB12 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209121937||ORU^R01|Q199821416T198318516|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448784^101LA|2921686^PHENYTOIN^L01N|||20101209121300|||||||20101209121800|^^|1001745^Test, Physician - p-Test Physician||||10-343-300088||20101209121944||LA|F||1^^^20101209121300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673215^PHENYTOIN^L01N||76|umol/L|40-80||||F|||20101209121938\r" + 
			"OBX|2|ST|4672225^DATE LAST PHENYTOIN^L01N||20101209||||||F|||20101209121938\r" + 
			"OBX|3|ST|4672915^TIME DOSE PHENYTOIN^L01N||0800||||||F|||20101209121938\r" + 
			"";
	
	final static String LAB13 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209122130||ORU^R01|Q199821425T198318525|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448796^101LA|2921259^POTASSIUM^L01N|||20101209121300|||||||20101209122100|^^|1001745^Test, Physician - p-Test Physician||||10-343-300089||20101209122138||LA|F||1^^^20101209121300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673635^POTASSIUM^L01N||8.7|mmol/L|3.3-5.1|C|||F|||20101209122131\r" + 
			"";
	
	final static String LAB14 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209130651||ORU^R01|Q199821475T198318570|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448827^101LA|8232642^HEMOGLOBIN AND HEMATOCRIT^L01N|||20101209130500|||||||20101209130500|^^|1001745^Test, Physician - p-Test Physician||||10-343-300090||20101209130658||LA|F||1^^^20101209130500^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|12152330^HEMOGLOBIN..^L01N||165|g/L|120-160|H|||F|||20101209130652\r" + 
			"OBX|2|NM|12332593^HEMATOCRIT..^L01N||0.42|L/L|0.36-0.48||||F|||20101209130652\r" + 
			"";
	
	final static String LAB15 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209131021||ORU^R01|Q199821483T198318577|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448835^101LA|4356874^IMMUNOGLOBULIN G^L01N|||20101209130500|||||||20101209130800|^^|1001745^Test, Physician - p-Test Physician||||10-343-300091||20101209131028||LA|F||1^^^20101209130500^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|4674253^IMMUNOGLOBULIN G^L01N||<0.52|g/L|6.80-18.00|L|||F|||20101209131022\r" + 
			"";
	
	final static String LAB16 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209134852||ORU^R01|Q199821620T198318714|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448887^101LA|9493207^COMPLETE BLOOD COUNT^L01N|||20101209133700|||||||20101209133700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300092||20101209134857||LA|F||1^^^20101209133700^^RT~^^^20101209133700^^RT|\r" + 
			"OBX|1|NM|4672972^HEMOGLOBIN^L01N||141|g/L|120 - 160||||F|||20101209134851\r" + 
			"OBX|2|NM|4673716^HEMATOCRIT^L01N||0.41|L/L|0.36 - 0.48||||F|||20101209134851\r" + 
			"OBX|3|NM|4673467^RBC^L01N||4.0|10E12/L|4.0 - 5.6||||F|||20101209134851\r" + 
			"OBX|4|NM|4673050^MCV^L01N||82|fL|82 - 100||||F|||20101209134851\r" + 
			"OBX|5|NM|4673047^MCHC^L01N||332|g/L|320 - 360||||F|||20101209134851\r" + 
			"OBX|6|NM|4673476^RDW^L01N||18.0|%|11.0 - 16.0|H|||F|||20101209134851\r" + 
			"OBX|7|NM|4673236^PLATELET COUNT^L01N||42|10E9/L|150 - 400|L|||F|||20101209134851\r" + 
			"OBX|8|NM|4673800^WBC^L01N||10.0|10E9/L|4.0 - 11.0||||F|||20101209134851\r" + 
			"";
	
	final static String LAB17 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209134853||ORU^R01|Q199821622T198318716|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448887^101LA|9493207^COMPLETE BLOOD COUNT^L01N|||20101209133700|||||||20101209133700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300092||20101209134858||LA|F||1^^^20101209133700^^RT~^^^20101209133700^^RT|\r" + 
			"OBX|1|NM|4672972^HEMOGLOBIN^L01N||141|g/L|120 - 160||||F|||20101209134851\r" + 
			"OBX|2|NM|4673716^HEMATOCRIT^L01N||0.41|L/L|0.36 - 0.48||||F|||20101209134851\r" + 
			"OBX|3|NM|4673467^RBC^L01N||4.0|10E12/L|4.0 - 5.6||||F|||20101209134851\r" + 
			"OBX|4|NM|4673050^MCV^L01N||82|fL|82 - 100||||F|||20101209134851\r" + 
			"OBX|5|NM|4673047^MCHC^L01N||332|g/L|320 - 360||||F|||20101209134851\r" + 
			"OBX|6|NM|4673476^RDW^L01N||18.0|%|11.0 - 16.0|H|||F|||20101209134851\r" + 
			"OBX|7|NM|4673236^PLATELET COUNT^L01N||42|10E9/L|150 - 400|L|||F|||20101209134851\r" + 
			"OBX|8|NM|4673800^WBC^L01N||10.0|10E9/L|4.0 - 11.0||||F|||20101209134851\r" + 
			"OBX|9|NM|4674889^NEUTROPHILS^L01N||6.0|10E9/L|2.0 - 9.0||||F|||20101209134851\r" + 
			"OBX|10|NM|4673014^LYMPHOCYTES^L01N||3.0|10E9/L|0.5 - 3.3||||F|||20101209134851\r" + 
			"OBX|11|NM|4673074^MONOCYTES^L01N||1.0|10E9/L|0.0 - 1.0||||F|||20101209134851\r" + 
			"OBX|12|NM|4674892^EOSINOPHILS^L01N||0.0|10E9/L|0.0 - 0.7||||F|||20101209134851\r" + 
			"OBX|13|NM|4673350^BASOPHILS^L01N||0.0|10E9/L|0.0 - 0.2||||F|||20101209134851\r" + 
			"";
	
	final static String LAB18 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209140004||ORU^R01|Q199821652T198318746|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448887^101LA|9493207^COMPLETE BLOOD COUNT^L01N|||20101209133700|||||||20101209133700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300092||20101209134857||LA|F||1^^^20101209133700^^RT~^^^20101209133700^^RT|\r" + 
			"OBX|1|NM|4672972^HEMOGLOBIN^L01N||141|g/L|120 - 160||||F|||20101209134851\r" + 
			"OBX|2|NM|4673716^HEMATOCRIT^L01N||0.41|L/L|0.36 - 0.48||||F|||20101209134851\r" + 
			"OBX|3|NM|4673467^RBC^L01N||3.9|10E12/L|4.0 - 5.6|L|||C|||20101209140005\r" + 
			"NTE|1||Corrected from 4.0 10E12/L on 12/09/2010 13:48:51 MDT by MLTRRL.\r" + 
			"OBX|4|NM|4673050^MCV^L01N||82|fL|82 - 100||||F|||20101209134851\r" + 
			"OBX|5|NM|4673047^MCHC^L01N||332|g/L|320 - 360||||F|||20101209134851\r" + 
			"OBX|6|NM|4673476^RDW^L01N||18.0|%|11.0 - 16.0|H|||F|||20101209134851\r" + 
			"OBX|7|TX|4673236^PLATELET COUNT^L01N||Clumped|10E9/L|150 - 400||||C|||20101209140005\r" + 
			"NTE|1|C|Platelets appear decreased on smear. Unable to make an accurate assessment of the platelets due to clumping. Suggest recollection of the patient's CBC using EDTA, sodium citrate and sodium heparin anticoagulant tubes.\r" + 
			"NTE|2||Corrected from 42 10E9/L [LOW] on 12/09/2010 13:48:51 MDT by MLTRRL.\r" + 
			"OBX|8|NM|4673800^WBC^L01N||10.0|10E9/L|4.0 - 11.0||||F|||20101209134851\r" + 
			"OBX|9|NM|4674889^NEUTROPHILS^L01N||6.0|10E9/L|2.0 - 9.0||||F|||20101209134851\r" + 
			"OBX|10|NM|4673014^LYMPHOCYTES^L01N||3.0|10E9/L|0.5 - 3.3||||F|||20101209134851\r" + 
			"OBX|11|NM|4673074^MONOCYTES^L01N||1.0|10E9/L|0.0 - 1.0||||F|||20101209134851\r" + 
			"OBX|12|NM|4674892^EOSINOPHILS^L01N||0.0|10E9/L|0.0 - 0.7||||F|||20101209134851\r" + 
			"OBX|13|NM|4673350^BASOPHILS^L01N||0.0|10E9/L|0.0 - 0.2||||F|||20101209134851\r" + 
			"";
	
	final static String LAB19 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209144631||ORU^R01|Q199821750T198318838|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176448997^101LA|4382056^DIFFERENTIAL^L01N|||20101209133700|||||||20101209133700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300092||20101209144638||LA|F||1^^^20101209133700^^RT~^^^^^RT|\r" + 
			"OBX|1|ST|4670050^SMUDGE CELLS^L01N||Present||||||F|||20101209144632\r" + 
			"OBX|2|ST|4670053^ABNORMAL LYMPHOCYTES^L01N||Present||||||F|||20101209144632\r" + 
			"OBX|3|ST|4670056^AUER RODS^L01N||Present||||||F|||20101209144632\r" + 
			"";
	
	final static String LAB20 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209172228||ORU^R01|Q199821920T198318997|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449247^101BA|4389639^ABO RH TYPE^L01N|||20101209171000|||||||20101209171000|^^|1001745^Test, Physician - p-Test Physician||||10-343-300110||20101209172235||BA|F||1^^^20101209171000^^RT~^^^^^RT|\r" + 
			"NTE|1|C|Order level comment added to Type.\r" + 
			"OBX|1|TX|4600112^ABO RH TYPE^L01N||B Positive||||||F|||20101209172228\r" + 
			"";
	
	final static String LAB21 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209172420||ORU^R01|Q199821924T198319001|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449250^101BA|4655785^ANTIBODY SCREEN^L01N|||20101209171000|||||||20101209171000|^^|1001745^Test, Physician - p-Test Physician||||10-343-300110||20101209172427||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600115^ANTIBODY SCREEN^L01N||Positive|||A|||F|||20101209172425\r" + 
			"NTE|1|C|2010-12-09 17:24  JMARYKA\r" + 
			"NTE|2|C|Result level comment added to Antibody Screen.\r" + 
			"";
	
	final static String LAB22 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209172547||ORU^R01|Q199821930T198319007|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449294^101BA|4389615^ANTIBODY IDENTIFICATION^L01N|||20101209171000|||||||20101209171000|^^|1001745^Test, Physician - p-Test Physician||||10-343-300110||20101209172429||BA|F||1^^^20101209171000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4600143^ANTIBODY IDENTIFICATION^L01N||Anti-K||||||F|||20101209172551\r" + 
			"OBX|2|TX|4600143^ANTIBODY IDENTIFICATION^L01N||Anti-Fya||||||F|||20101209172551\r" + 
			"";
	
	final static String LAB23 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209173225||ORU^R01|Q199821960T198319035|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449303^101BA|4655769^TYPE AND SCREEN^L01N|||20101209171000|||||||20101209172700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300111||20101209173232||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600112^ABO RH TYPE^L01N||B Positive||||||F|||20101209173231\r" + 
			"";
	
	final static String LAB24 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209173225||ORU^R01|Q199821962T198319037|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449303^101BA|4655769^TYPE AND SCREEN^L01N|||20101209171000|||||||20101209172700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300111||20101209173232||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600112^ABO RH TYPE^L01N||B Positive||||||F|||20101209173231\r" + 
			"OBX|2|TX|4600115^ANTIBODY SCREEN^L01N||Negative||||||F|||20101209173231\r" + 
			"OBX|3|ST|5086280^Cancellation Date^L01N||20101215||||||F|||20101209173231\r" + 
			"";
	
	final static String LAB25 = 
			"MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209173226||ORU^R01|Q199821964T198319039|P|2.3\r" +
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449303^101BA|4655769^TYPE AND SCREEN^L01N|||20101209171000|||||||20101209172700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300111||20101209173232||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600112^ABO RH TYPE^L01N||B Positive||||||F|||20101209173231\r" + 
			"OBX|2|TX|4600115^ANTIBODY SCREEN^L01N||Negative||||||F|||20101209173231\r" + 
			"OBX|3|TX|5802574^RTSIS Number^L01N||TGG 0987||||||F|||20101209173231\r" + 
			"OBX|4|ST|5086280^Cancellation Date^L01N||20101215||||||F|||20101209173231\r" + 
			"";
	
	final static String LAB26 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209173226||ORU^R01|Q199821966T198319041|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449303^101BA|4655769^TYPE AND SCREEN^L01N|||20101209171000|||||||20101209172700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300111||20101209173232||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600112^ABO RH TYPE^L01N||B Positive||||||F|||20101209173231\r" + 
			"OBX|2|TX|4600115^ANTIBODY SCREEN^L01N||Negative||||||F|||20101209173231\r" + 
			"OBX|3|TX|5802574^RTSIS Number^L01N||TGG 0987||||||F|||20101209173231\r" + 
			"OBX|4|ST|5086280^Cancellation Date^L01N||20101215||||||F|||20101209173231\r" + 
			"";
	
	final static String LAB27 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209173228||ORU^R01|Q199821972T198319047|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||990176449303^101BA|4655769^TYPE AND SCREEN^L01N|||20101209171000|||||||20101209172700|^^|1001745^Test, Physician - p-Test Physician||||10-343-300111||20101209173236||BA|F||1^^0^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|ST|8030877^COMMENT^L01N||Antibody Screen Comment: Red Cells available if required.||||||F|||20101209173236\r" + 
			"";
	
	final static String LAB28 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101209174924||ORU^R01|Q199821974T198319049|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176449250^101BA|4655785^ANTIBODY SCREEN^L01N|||20101209171000|||||||20101209171000|^^|1001745^Test, Physician - p-Test Physician||||10-343-300110||20101209172427||BA|F||1^^^20101209171000^^RT~^^^20101209171000^^RT|\r" + 
			"OBX|1|TX|4600115^ANTIBODY SCREEN^L01N||Negative||||||C|||20101209174931\r" + 
			"NTE|1|C|2010-12-09 17:24  JMARYKA\r" + 
			"NTE|2|C|Result level comment added to Antibody Screen.\r" + 
			"NTE|3||Corrected from Positive  [ABN] on 12/09/2010 17:24:25 MDT by JMARYKA.\r" + 
			"";
	
	final static String LAB29 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210074718||ORU^R01|Q199822400T198319473|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176450368^101LA|12943991^OCCULT BLOOD X3^L01N|||20101210074200|||||||20101210074500|^^|1001745^Test, Physician - p-Test Physician||||10-344-300006||20101210074723||LA|F||1^^^20101210074200^^RT~^^^20101210074200^^RT|\r" + 
			"OBX|1|TX|4670188^OCCULT BLOOD 1^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"NTE|1|I|The Alberta Colorectal Screening Program recommends that the Fecal Occult Blood Test (FOBT) should primarily be used for annual colorectal cancer screening in asymptomatic 50-74 y individuals without risk factors for colorectal cancer. The use of the FOBT for the investigation of disease is of limited value. A Positive FOBT should be investigated by complete evaluation of the colon, preferably by colonoscopy. Repeating the FOBT is not recommended.\r" + 
			"OBX|2|ST|4672069^COLLECTION DATE^L01N||07-DEC-2010||||||F|||20101210074717\r" + 
			"OBX|3|TX|4669876^OCCULT BLOOD 2^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"OBX|4|ST|4672027^COLLECTION DATE^L01N||08-DEC-2010||||||F|||20101210074717\r" + 
			"";
	
	final static String LAB30 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210074718||ORU^R01|Q199822402T198319476|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176450368^101LA|12943991^OCCULT BLOOD X3^L01N|||20101210074200|||||||20101210074600|^^|1001745^Test, Physician - p-Test Physician||||10-344-300006||20101210074723||LA|F||1^^^20101210074200^^RT~^^^20101210074200^^RT|\r" + 
			"OBX|1|TX|4670188^OCCULT BLOOD 1^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"NTE|1|I|The Alberta Colorectal Screening Program recommends that the Fecal Occult Blood Test (FOBT) should primarily be used for annual colorectal cancer screening in asymptomatic 50-74 y individuals without risk factors for colorectal cancer. The use of the FOBT for the investigation of disease is of limited value. A Positive FOBT should be investigated by complete evaluation of the colon, preferably by colonoscopy. Repeating the FOBT is not recommended.\r" + 
			"OBX|2|ST|4672069^COLLECTION DATE^L01N||07-DEC-2010||||||F|||20101210074717\r" + 
			"OBX|3|TX|4669876^OCCULT BLOOD 2^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"OBX|4|ST|4672027^COLLECTION DATE^L01N||08-DEC-2010||||||F|||20101210074717\r" + 
			"";
	
	final static String LAB31 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210074719||ORU^R01|Q199822404T198319479|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176450368^101LA|12943991^OCCULT BLOOD X3^L01N|||20101210074200|||||||20101210074600|^^|1001745^Test, Physician - p-Test Physician||||10-344-300006||20101210074724||LA|F||1^^^20101210074200^^RT~^^^20101210074200^^RT|\r" + 
			"OBX|1|TX|4670188^OCCULT BLOOD 1^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"NTE|1|I|The Alberta Colorectal Screening Program recommends that the Fecal Occult Blood Test (FOBT) should primarily be used for annual colorectal cancer screening in asymptomatic 50-74 y individuals without risk factors for colorectal cancer. The use of the FOBT for the investigation of disease is of limited value. A Positive FOBT should be investigated by complete evaluation of the colon, preferably by colonoscopy. Repeating the FOBT is not recommended.\r" + 
			"OBX|2|ST|4672069^COLLECTION DATE^L01N||07-DEC-2010||||||F|||20101210074717\r" + 
			"OBX|3|TX|4669876^OCCULT BLOOD 2^L01N||Negative||Negative||||F|||20101210074717\r" + 
			"OBX|4|ST|4672027^COLLECTION DATE^L01N||08-DEC-2010||||||F|||20101210074717\r" + 
			"OBX|5|TX|4670191^OCCULT BLOOD 3^L01N||Positive||Negative|A|||F|||20101210074717\r" + 
			"OBX|6|ST|4672072^COLLECTION DATE^L01N||09-DEC-2010||||||F|||20101210074717\r" + 
			"";
	
	final static String LAB32 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210075154||ORU^R01|Q199822411T198319487|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176450392^101LA|2921210^GLUCOSE FASTING^L01N|||20101210074200|||||||20101210075000|^^|1001745^Test, Physician - p-Test Physician||||10-344-300007||20101210075201||LA|F||1^^^20101210074200^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673692^GLUCOSE, FASTING^L01N||5.9|mmol/L|3.9-6.1||||F|||20101210075155\r" + 
			"OBX|2|ST|13202828^Hours Fasting^L01N||11 hours||||||F|||20101210075155\r" + 
			"";
	
	final static String LAB33 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210140837||ORU^R01|Q199822847T198319920|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450798^101MA|4356713^THROAT BETA STREP TEST^L01N|||20101210140000|||||||20101210140400|SWAB^^|1001745^Test, Physician - p-Test Physician||||TH-10-3000007||20101210140834||MA|F||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384243^THROAT BETA STREP TEST^L01N||*****Microbiology E.N.T/EYE*****||||||F|||20101210140834\r" + 
			"OBX|2|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|3|TX|4384243^THROAT BETA STREP TEST^L01N||                     TEST: Throat Beta Strep Test||||||F|||20101210140834\r" + 
			"OBX|4|TX|4384243^THROAT BETA STREP TEST^L01N||            SPECIMEN TYPE: Swab                     COLLECTED: 12/10/2010 14:00 MST||||||F|||20101210140834\r" + 
			"OBX|5|TX|4384243^THROAT BETA STREP TEST^L01N||          SPECIMEN SOURCE: Throat                    RECEIVED: 12/10/2010 14:05 MST||||||F|||20101210140834\r" + 
			"OBX|6|TX|4384243^THROAT BETA STREP TEST^L01N||                                                    ACCESSION: TH-10-3000007||||||F|||20101210140834\r" + 
			"OBX|7|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|8|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|9|TX|4384243^THROAT BETA STREP TEST^L01N||     FINAL REPORT                                    Verified:12/10/2010 14:08 MST||||||F|||20101210140834\r" + 
			"OBX|10|TX|4384243^THROAT BETA STREP TEST^L01N||     Test for Group A Streptococcus: Negative||||||F|||20101210140834\r" + 
			"OBX|11|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|12|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|13|TX|4384243^THROAT BETA STREP TEST^L01N||||||||F|||20101210140834\r" + 
			"OBX|14|TX|4384243^THROAT BETA STREP TEST^L01N||     __________________________________________________________||||||F|||20101210140834\r" + 
			"";
	
	final static String LAB34 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141101||ORU^R01|Q199822850T198319923|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450792^101MA|2922080^WOUND BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140200|Superficial Wound Swab^^right hand|1001745^Test, Physician - p-Test Physician||||WN-10-3000020||20101210141056||MA|P||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384298^WOUND BACTERIAL CULTURE^L01N||*****Microbiology Wound/Abscess*****||||||P|||20101210141056\r" + 
			"OBX|2|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|3|TX|4384298^WOUND BACTERIAL CULTURE^L01N||                     TEST: Wound Culture||||||P|||20101210141056\r" + 
			"OBX|4|TX|4384298^WOUND BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Superficial Wound Swab   COLLECTED: 12/10/2010 14:00 MST||||||P|||20101210141056\r" + 
			"OBX|5|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Finger                    RECEIVED: 12/10/2010 14:05 MST||||||P|||20101210141056\r" + 
			"OBX|6|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: right hand               ACCESSION: WN-10-3000020||||||P|||20101210141056\r" + 
			"OBX|7|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|8|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|9|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|10|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     GRAM STAIN REPORT                               Verified:12/10/2010 14:10 MST||||||P|||20101210141056\r" + 
			"OBX|11|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No neutrophils seen||||||P|||20101210141056\r" + 
			"OBX|12|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No epithelial cells seen||||||P|||20101210141056\r" + 
			"OBX|13|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No organisms seen||||||P|||20101210141056\r" + 
			"OBX|14|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|15|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|16|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     Additional info / info provided on requisition:||||||P|||20101210141056\r" + 
			"OBX|17|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     D1||||||P|||20101210141056\r" + 
			"OBX|18|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|19|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|20|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|21|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141056\r" + 
			"OBX|22|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     __________________________________________________________||||||P|||20101210141056\r" + 
			"";
	
	final static String LAB35 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141124||ORU^R01|Q199822853T198319926|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450792^101MA|2922080^WOUND BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140200|Superficial Wound Swab^^right hand|1001745^Test, Physician - p-Test Physician||||WN-10-3000020||20101210141121||MA|P||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384298^WOUND BACTERIAL CULTURE^L01N||*****Microbiology Wound/Abscess*****||||||P|||20101210141121\r" + 
			"OBX|2|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|3|TX|4384298^WOUND BACTERIAL CULTURE^L01N||                     TEST: Wound Culture||||||P|||20101210141121\r" + 
			"OBX|4|TX|4384298^WOUND BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Superficial Wound Swab   COLLECTED: 12/10/2010 14:00 MST||||||P|||20101210141121\r" + 
			"OBX|5|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Finger                    RECEIVED: 12/10/2010 14:05 MST||||||P|||20101210141121\r" + 
			"OBX|6|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: right hand               ACCESSION: WN-10-3000020||||||P|||20101210141121\r" + 
			"OBX|7|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|8|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|9|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|10|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     GRAM STAIN REPORT                               Verified:12/10/2010 14:10 MST||||||P|||20101210141121\r" + 
			"OBX|11|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No neutrophils seen||||||P|||20101210141121\r" + 
			"OBX|12|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No epithelial cells seen||||||P|||20101210141121\r" + 
			"OBX|13|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No organisms seen||||||P|||20101210141121\r" + 
			"OBX|14|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|15|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|16|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     PRELIMINARY REPORT                              Verified:12/10/2010 14:11 MST||||||P|||20101210141121\r" + 
			"OBX|17|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No growth||||||P|||20101210141121\r" + 
			"OBX|18|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|19|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|20|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     Additional info / info provided on requisition:||||||P|||20101210141121\r" + 
			"OBX|21|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     D1||||||P|||20101210141121\r" + 
			"OBX|22|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|23|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|24|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|25|TX|4384298^WOUND BACTERIAL CULTURE^L01N||||||||P|||20101210141121\r" + 
			"OBX|26|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     __________________________________________________________||||||P|||20101210141121\r" + 
			"";
	
	final static String LAB36 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141424||ORU^R01|Q199822860T198319933|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450795^101MA|2922077^URINE BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140400|URINE^^R/O YEAST|1001745^Test, Physician - p-Test Physician||||UR-10-3000032||20101210141416||MA|P||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384297^URINE BACTERIAL CULTURE^L01N||*****Microbiology Urine*****||||||P|||20101210141416\r" + 
			"OBX|2|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|3|TX|4384297^URINE BACTERIAL CULTURE^L01N||                     TEST: Urine Culture||||||P|||20101210141416\r" + 
			"OBX|4|TX|4384297^URINE BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Urine                    COLLECTED: 12/10/2010 14:00 MST||||||P|||20101210141416\r" + 
			"OBX|5|TX|4384297^URINE BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Post Pros. Massage Urine  RECEIVED: 12/10/2010 14:05 MST||||||P|||20101210141416\r" + 
			"OBX|6|TX|4384297^URINE BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: R/O YEAST                ACCESSION: UR-10-3000032||||||P|||20101210141416\r" + 
			"OBX|7|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|8|TX|4384297^URINE BACTERIAL CULTURE^L01N||     SUSCEPTIBILITY RESULTS ***||||||P|||20101210141416\r" + 
			"OBX|9|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Escherichia||||||P|||20101210141416\r" + 
			"OBX|10|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             coli||||||P|||20101210141416\r" + 
			"OBX|11|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|12|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp||||||P|||20101210141416\r" + 
			"OBX|13|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________||||||P|||20101210141416\r" + 
			"OBX|14|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ampicillin              S||||||P|||20101210141416\r" + 
			"OBX|15|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalothin(1)          S||||||P|||20101210141416\r" + 
			"OBX|16|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefazolin(1)            S||||||P|||20101210141416\r" + 
			"OBX|17|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefepime(1)             R||||||P|||20101210141416\r" + 
			"OBX|18|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ceftriaxone(1)          R||||||P|||20101210141416\r" + 
			"OBX|19|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Gentamicin              S||||||P|||20101210141416\r" + 
			"OBX|20|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S||||||P|||20101210141416\r" + 
			"OBX|21|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Norfloxacin             S||||||P|||20101210141416\r" + 
			"OBX|22|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S||||||P|||20101210141416\r" + 
			"OBX|23|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|24|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|25|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|26|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|27|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|28|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|29|TX|4384297^URINE BACTERIAL CULTURE^L01N||     *** SUSCEPTIBILITY FOOTNOTES||||||P|||20101210141416\r" + 
			"OBX|30|TX|4384297^URINE BACTERIAL CULTURE^L01N||     (1)||||||P|||20101210141416\r" + 
			"OBX|31|TX|4384297^URINE BACTERIAL CULTURE^L01N||     This organism has been shown to produce an Extended Spectrum Beta||||||P|||20101210141416\r" + 
			"OBX|32|TX|4384297^URINE BACTERIAL CULTURE^L01N||     lactamase (ESBL) and should be considered resistant to all||||||P|||20101210141416\r" + 
			"OBX|33|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalosporins.||||||P|||20101210141416\r" + 
			"OBX|34|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|35|TX|4384297^URINE BACTERIAL CULTURE^L01N||||||||P|||20101210141416\r" + 
			"OBX|36|TX|4384297^URINE BACTERIAL CULTURE^L01N||     __________________________________________________________||||||P|||20101210141416\r" + 
			"";
	
	final static String LAB37 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141648||ORU^R01|Q199822868T198319941|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450795^101MA|2922077^URINE BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140400|URINE^^R/O YEAST|1001745^Test, Physician - p-Test Physician||||UR-10-3000032||20101210141652||MA|F||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384297^URINE BACTERIAL CULTURE^L01N||*****Microbiology Urine*****|||A|||F|||20101210141652\r" + 
			"OBX|2|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|3|TX|4384297^URINE BACTERIAL CULTURE^L01N||                     TEST: Urine Culture|||A|||F|||20101210141652\r" + 
			"OBX|4|TX|4384297^URINE BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Urine                    COLLECTED: 12/10/2010 14:00 MST|||A|||F|||20101210141652\r" + 
			"OBX|5|TX|4384297^URINE BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Post Pros. Massage Urine  RECEIVED: 12/10/2010 14:05 MST|||A|||F|||20101210141652\r" + 
			"OBX|6|TX|4384297^URINE BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: R/O YEAST                ACCESSION: UR-10-3000032|||A|||F|||20101210141652\r" + 
			"OBX|7|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|8|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|9|TX|4384297^URINE BACTERIAL CULTURE^L01N||     FINAL REPORT                                    Verified:12/10/2010 14:16 MST|||A|||F|||20101210141652\r" + 
			"OBX|10|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count greater than 1X10E8 cfu/L Escherichia coli|||A|||F|||20101210141652\r" + 
			"OBX|11|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count 1X10E7 - 1X10E8 cfu/L Staphylococcus aureus|||A|||F|||20101210141652\r" + 
			"OBX|12|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|13|TX|4384297^URINE BACTERIAL CULTURE^L01N||     SUSCEPTIBILITY RESULTS ***|||A|||F|||20101210141652\r" + 
			"OBX|14|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Escherichia|||A|||F|||20101210141652\r" + 
			"OBX|15|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             coli|||A|||F|||20101210141652\r" + 
			"OBX|16|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|17|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||F|||20101210141652\r" + 
			"OBX|18|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||F|||20101210141652\r" + 
			"OBX|19|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ampicillin              S|||A|||F|||20101210141652\r" + 
			"OBX|20|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalothin(1)          S|||A|||F|||20101210141652\r" + 
			"OBX|21|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefazolin(1)            S|||A|||F|||20101210141652\r" + 
			"OBX|22|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefepime(1)             R|||A|||F|||20101210141652\r" + 
			"OBX|23|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ceftriaxone(1)          R|||A|||F|||20101210141652\r" + 
			"OBX|24|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Gentamicin              S|||A|||F|||20101210141652\r" + 
			"OBX|25|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||F|||20101210141652\r" + 
			"OBX|26|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Norfloxacin             S|||A|||F|||20101210141652\r" + 
			"OBX|27|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||F|||20101210141652\r" + 
			"OBX|28|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|29|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|30|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|31|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|32|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Staphylococcus|||A|||F|||20101210141652\r" + 
			"OBX|33|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             aureus|||A|||F|||20101210141652\r" + 
			"OBX|34|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|35|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||F|||20101210141652\r" + 
			"OBX|36|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||F|||20101210141652\r" + 
			"OBX|37|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||F|||20101210141652\r" + 
			"OBX|38|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||F|||20101210141652\r" + 
			"OBX|39|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ciprofloxacin           S|||A|||F|||20101210141652\r" + 
			"OBX|40|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|41|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|42|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|43|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|44|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|45|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|46|TX|4384297^URINE BACTERIAL CULTURE^L01N||     *** SUSCEPTIBILITY FOOTNOTES|||A|||F|||20101210141652\r" + 
			"OBX|47|TX|4384297^URINE BACTERIAL CULTURE^L01N||     (1)|||A|||F|||20101210141652\r" + 
			"OBX|48|TX|4384297^URINE BACTERIAL CULTURE^L01N||     This organism has been shown to produce an Extended Spectrum Beta|||A|||F|||20101210141652\r" + 
			"OBX|49|TX|4384297^URINE BACTERIAL CULTURE^L01N||     lactamase (ESBL) and should be considered resistant to all|||A|||F|||20101210141652\r" + 
			"OBX|50|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalosporins.|||A|||F|||20101210141652\r" + 
			"OBX|51|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|52|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|53|TX|4384297^URINE BACTERIAL CULTURE^L01N||     __________________________________________________________|||A|||F|||20101210141652\r" + 
			"";
	
	final static String LAB38 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141652||ORU^R01|Q199822870T198319943|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450795^101MA|2922077^URINE BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140400|URINE^^R/O YEAST|1001745^Test, Physician - p-Test Physician||||UR-10-3000032||20101210141652||MA|F||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384297^URINE BACTERIAL CULTURE^L01N||*****Microbiology Urine*****|||A|||F|||20101210141652\r" + 
			"OBX|2|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|3|TX|4384297^URINE BACTERIAL CULTURE^L01N||                     TEST: Urine Culture|||A|||F|||20101210141652\r" + 
			"OBX|4|TX|4384297^URINE BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Urine                    COLLECTED: 12/10/2010 14:00 MST|||A|||F|||20101210141652\r" + 
			"OBX|5|TX|4384297^URINE BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Post Pros. Massage Urine  RECEIVED: 12/10/2010 14:05 MST|||A|||F|||20101210141652\r" + 
			"OBX|6|TX|4384297^URINE BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: R/O YEAST                ACCESSION: UR-10-3000032|||A|||F|||20101210141652\r" + 
			"OBX|7|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|8|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|9|TX|4384297^URINE BACTERIAL CULTURE^L01N||     FINAL REPORT                                    Verified:12/10/2010 14:16 MST|||A|||F|||20101210141652\r" + 
			"OBX|10|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count greater than 1X10E8 cfu/L Escherichia coli|||A|||F|||20101210141652\r" + 
			"OBX|11|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count 1X10E7 - 1X10E8 cfu/L Staphylococcus aureus|||A|||F|||20101210141652\r" + 
			"OBX|12|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|13|TX|4384297^URINE BACTERIAL CULTURE^L01N||     SUSCEPTIBILITY RESULTS ***|||A|||F|||20101210141652\r" + 
			"OBX|14|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Escherichia|||A|||F|||20101210141652\r" + 
			"OBX|15|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             coli|||A|||F|||20101210141652\r" + 
			"OBX|16|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|17|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||F|||20101210141652\r" + 
			"OBX|18|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||F|||20101210141652\r" + 
			"OBX|19|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ampicillin              S|||A|||F|||20101210141652\r" + 
			"OBX|20|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalothin(1)          S|||A|||F|||20101210141652\r" + 
			"OBX|21|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefazolin(1)            S|||A|||F|||20101210141652\r" + 
			"OBX|22|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefepime(1)             R|||A|||F|||20101210141652\r" + 
			"OBX|23|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ceftriaxone(1)          R|||A|||F|||20101210141652\r" + 
			"OBX|24|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Gentamicin              S|||A|||F|||20101210141652\r" + 
			"OBX|25|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||F|||20101210141652\r" + 
			"OBX|26|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Norfloxacin             S|||A|||F|||20101210141652\r" + 
			"OBX|27|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||F|||20101210141652\r" + 
			"OBX|28|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|29|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|30|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|31|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|32|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Staphylococcus|||A|||F|||20101210141652\r" + 
			"OBX|33|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             aureus|||A|||F|||20101210141652\r" + 
			"OBX|34|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|35|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||F|||20101210141652\r" + 
			"OBX|36|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||F|||20101210141652\r" + 
			"OBX|37|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||F|||20101210141652\r" + 
			"OBX|38|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||F|||20101210141652\r" + 
			"OBX|39|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ciprofloxacin           S|||A|||F|||20101210141652\r" + 
			"OBX|40|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|41|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|42|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|43|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|44|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|45|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|46|TX|4384297^URINE BACTERIAL CULTURE^L01N||     *** SUSCEPTIBILITY FOOTNOTES|||A|||F|||20101210141652\r" + 
			"OBX|47|TX|4384297^URINE BACTERIAL CULTURE^L01N||     (1)|||A|||F|||20101210141652\r" + 
			"OBX|48|TX|4384297^URINE BACTERIAL CULTURE^L01N||     This organism has been shown to produce an Extended Spectrum Beta|||A|||F|||20101210141652\r" + 
			"OBX|49|TX|4384297^URINE BACTERIAL CULTURE^L01N||     lactamase (ESBL) and should be considered resistant to all|||A|||F|||20101210141652\r" + 
			"OBX|50|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalosporins.|||A|||F|||20101210141652\r" + 
			"OBX|51|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|52|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||F|||20101210141652\r" + 
			"OBX|53|TX|4384297^URINE BACTERIAL CULTURE^L01N||     __________________________________________________________|||A|||F|||20101210141652\r" + 
			"";
	
	final static String LAB39 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210141804||ORU^R01|Q199822873T198319946|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450792^101MA|2922080^WOUND BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140200|Superficial Wound Swab^^right hand|1001745^Test, Physician - p-Test Physician||||WN-10-3000020||20101210141746||MA|P||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384298^WOUND BACTERIAL CULTURE^L01N||*****Microbiology Wound/Abscess*****|||A|||P|||20101210141746\r" + 
			"OBX|2|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|3|TX|4384298^WOUND BACTERIAL CULTURE^L01N||                     TEST: Wound Culture|||A|||P|||20101210141746\r" + 
			"OBX|4|TX|4384298^WOUND BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Superficial Wound Swab   COLLECTED: 12/10/2010 14:00 MST|||A|||P|||20101210141746\r" + 
			"OBX|5|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Finger                    RECEIVED: 12/10/2010 14:05 MST|||A|||P|||20101210141746\r" + 
			"OBX|6|TX|4384298^WOUND BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: right hand               ACCESSION: WN-10-3000020|||A|||P|||20101210141746\r" + 
			"OBX|7|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|8|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|9|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|10|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     GRAM STAIN REPORT                               Verified:12/10/2010 14:10 MST|||A|||P|||20101210141746\r" + 
			"OBX|11|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No neutrophils seen|||A|||P|||20101210141746\r" + 
			"OBX|12|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No epithelial cells seen|||A|||P|||20101210141746\r" + 
			"OBX|13|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     No organisms seen|||A|||P|||20101210141746\r" + 
			"OBX|14|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|15|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|16|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     PRELIMINARY REPORT                              Verified:12/10/2010 14:17 MST|||A|||P|||20101210141746\r" + 
			"OBX|17|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     Coagulase negative Staphylococcus, not S.saprophyticus|||A|||P|||20101210141746\r" + 
			"OBX|18|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|19|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|20|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     Additional info / info provided on requisition:|||A|||P|||20101210141746\r" + 
			"OBX|21|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     D1|||A|||P|||20101210141746\r" + 
			"OBX|22|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|23|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|24|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|25|TX|4384298^WOUND BACTERIAL CULTURE^L01N|||||A|||P|||20101210141746\r" + 
			"OBX|26|TX|4384298^WOUND BACTERIAL CULTURE^L01N||     __________________________________________________________|||A|||P|||20101210141746\r" + 
			"";
	
	final static String LAB40 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210142056||ORU^R01|Q199822876T198319949|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450795^101MA|2922077^URINE BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140400|URINE^^R/O YEAST|1001745^Test, Physician - p-Test Physician||||UR-10-3000032||20101210142045||MA|C||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384297^URINE BACTERIAL CULTURE^L01N||*****Microbiology Urine*****|||A|||C|||20101210142045\r" + 
			"OBX|2|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|3|TX|4384297^URINE BACTERIAL CULTURE^L01N||                     TEST: Urine Culture|||A|||C|||20101210142045\r" + 
			"OBX|4|TX|4384297^URINE BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Urine                    COLLECTED: 12/10/2010 14:00 MST|||A|||C|||20101210142045\r" + 
			"OBX|5|TX|4384297^URINE BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Post Pros. Massage Urine  RECEIVED: 12/10/2010 14:05 MST|||A|||C|||20101210142045\r" + 
			"OBX|6|TX|4384297^URINE BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: R/O YEAST                ACCESSION: UR-10-3000032|||A|||C|||20101210142045\r" + 
			"OBX|7|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|8|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|9|TX|4384297^URINE BACTERIAL CULTURE^L01N||     FINAL REPORT                                    Verified:12/10/2010 14:16 MST|||A|||C|||20101210142045\r" + 
			"OBX|10|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count greater than 1X10E8 cfu/L Escherichia coli|||A|||C|||20101210142045\r" + 
			"OBX|11|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count 1X10E7 - 1X10E8 cfu/L Staphylococcus aureus|||A|||C|||20101210142045\r" + 
			"OBX|12|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|13|TX|4384297^URINE BACTERIAL CULTURE^L01N||     SUSCEPTIBILITY RESULTS ***|||A|||C|||20101210142045\r" + 
			"OBX|14|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Escherichia|||A|||C|||20101210142045\r" + 
			"OBX|15|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             coli|||A|||C|||20101210142045\r" + 
			"OBX|16|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|17|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||C|||20101210142045\r" + 
			"OBX|18|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||C|||20101210142045\r" + 
			"OBX|19|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ampicillin              S|||A|||C|||20101210142045\r" + 
			"OBX|20|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalothin(1)          S|||A|||C|||20101210142045\r" + 
			"OBX|21|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefazolin(1)            S|||A|||C|||20101210142045\r" + 
			"OBX|22|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefepime(1)             R|||A|||C|||20101210142045\r" + 
			"OBX|23|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ceftriaxone(1)          R|||A|||C|||20101210142045\r" + 
			"OBX|24|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Gentamicin              S|||A|||C|||20101210142045\r" + 
			"OBX|25|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||C|||20101210142045\r" + 
			"OBX|26|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Norfloxacin             S|||A|||C|||20101210142045\r" + 
			"OBX|27|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||C|||20101210142045\r" + 
			"OBX|28|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|29|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|30|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|31|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|32|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Staphylococcus|||A|||C|||20101210142045\r" + 
			"OBX|33|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             aureus|||A|||C|||20101210142045\r" + 
			"OBX|34|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|35|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||C|||20101210142045\r" + 
			"OBX|36|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||C|||20101210142045\r" + 
			"OBX|37|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||C|||20101210142045\r" + 
			"OBX|38|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      R#|||A|||C|||20101210142045\r" + 
			"OBX|39|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ciprofloxacin           S|||A|||C|||20101210142045\r" + 
			"OBX|40|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|41|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|42|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|43|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Staphylococcus aureus|||A|||C|||20101210142045\r" + 
			"OBX|44|TX|4384297^URINE BACTERIAL CULTURE^L01N||     _____________________|||A|||C|||20101210142045\r" + 
			"OBX|45|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa Interp corrected from S on 12/10/2010 14:20 MST|||A|||C|||20101210142045\r" + 
			"OBX|46|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|47|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|48|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|49|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|50|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|51|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|52|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|53|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|54|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|55|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|56|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|57|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|58|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|59|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|60|TX|4384297^URINE BACTERIAL CULTURE^L01N||     *** SUSCEPTIBILITY FOOTNOTES|||A|||C|||20101210142045\r" + 
			"OBX|61|TX|4384297^URINE BACTERIAL CULTURE^L01N||     (1)|||A|||C|||20101210142045\r" + 
			"OBX|62|TX|4384297^URINE BACTERIAL CULTURE^L01N||     This organism has been shown to produce an Extended Spectrum Beta|||A|||C|||20101210142045\r" + 
			"OBX|63|TX|4384297^URINE BACTERIAL CULTURE^L01N||     lactamase (ESBL) and should be considered resistant to all|||A|||C|||20101210142045\r" + 
			"OBX|64|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalosporins.|||A|||C|||20101210142045\r" + 
			"OBX|65|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|66|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142045\r" + 
			"OBX|67|TX|4384297^URINE BACTERIAL CULTURE^L01N||     __________________________________________________________|||A|||C|||20101210142045\r" + 
			"";
	
	final static String LAB41 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101210142320||ORU^R01|Q199822879T198319952|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176450795^101MA|2922077^URINE BACTERIAL CULTURE^L01N|||20101210140000|||||||20101210140400|URINE^^R/O YEAST|1001745^Test, Physician - p-Test Physician||||UR-10-3000032||20101210142315||MA|C||1^^^20101210140000^^RT~^^^^^RT|\r" + 
			"OBX|1|TX|4384297^URINE BACTERIAL CULTURE^L01N||*****Microbiology Urine*****|||A|||C|||20101210142315\r" + 
			"OBX|2|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|3|TX|4384297^URINE BACTERIAL CULTURE^L01N||                     TEST: Urine Culture|||A|||C|||20101210142315\r" + 
			"OBX|4|TX|4384297^URINE BACTERIAL CULTURE^L01N||            SPECIMEN TYPE: Urine                    COLLECTED: 12/10/2010 14:00 MST|||A|||C|||20101210142315\r" + 
			"OBX|5|TX|4384297^URINE BACTERIAL CULTURE^L01N||          SPECIMEN SOURCE: Post Pros. Massage Urine  RECEIVED: 12/10/2010 14:05 MST|||A|||C|||20101210142315\r" + 
			"OBX|6|TX|4384297^URINE BACTERIAL CULTURE^L01N||          ADDITIONAL INFO: R/O YEAST                ACCESSION: UR-10-3000032|||A|||C|||20101210142315\r" + 
			"OBX|7|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|8|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|9|TX|4384297^URINE BACTERIAL CULTURE^L01N||     ADDITIONAL/AMENDED REPORT                       Verified:12/10/2010 14:23 MST|||A|||C|||20101210142315\r" + 
			"OBX|10|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count greater than 1X10E8 cfu/L Escherichia coli|||A|||C|||20101210142315\r" + 
			"OBX|11|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Colony count 1X10E7 - 1X10E8 cfu/L Staphylococcus aureus|||A|||C|||20101210142315\r" + 
			"OBX|12|TX|4384297^URINE BACTERIAL CULTURE^L01N||     **Note amended susceptibility result**|||A|||C|||20101210142315\r" + 
			"OBX|13|TX|4384297^URINE BACTERIAL CULTURE^L01N||     ***Add Freetext*** resistant susceptible to resistant|||A|||C|||20101210142315\r" + 
			"OBX|14|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|15|TX|4384297^URINE BACTERIAL CULTURE^L01N||     SUSCEPTIBILITY RESULTS ***|||A|||C|||20101210142315\r" + 
			"OBX|16|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Escherichia|||A|||C|||20101210142315\r" + 
			"OBX|17|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             coli|||A|||C|||20101210142315\r" + 
			"OBX|18|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|19|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||C|||20101210142315\r" + 
			"OBX|20|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||C|||20101210142315\r" + 
			"OBX|21|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ampicillin              S|||A|||C|||20101210142315\r" + 
			"OBX|22|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalothin(1)          S|||A|||C|||20101210142315\r" + 
			"OBX|23|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefazolin(1)            S|||A|||C|||20101210142315\r" + 
			"OBX|24|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cefepime(1)             R|||A|||C|||20101210142315\r" + 
			"OBX|25|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ceftriaxone(1)          R|||A|||C|||20101210142315\r" + 
			"OBX|26|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Gentamicin              S|||A|||C|||20101210142315\r" + 
			"OBX|27|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||C|||20101210142315\r" + 
			"OBX|28|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Norfloxacin             S|||A|||C|||20101210142315\r" + 
			"OBX|29|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      S|||A|||C|||20101210142315\r" + 
			"OBX|30|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|31|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|32|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|33|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|34|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Staphylococcus|||A|||C|||20101210142315\r" + 
			"OBX|35|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             aureus|||A|||C|||20101210142315\r" + 
			"OBX|36|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|37|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             Interp|||A|||C|||20101210142315\r" + 
			"OBX|38|TX|4384297^URINE BACTERIAL CULTURE^L01N||                             _________|||A|||C|||20101210142315\r" + 
			"OBX|39|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Nitrofurantoin          S|||A|||C|||20101210142315\r" + 
			"OBX|40|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa      R#|||A|||C|||20101210142315\r" + 
			"OBX|41|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Ciprofloxacin           S|||A|||C|||20101210142315\r" + 
			"OBX|42|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|43|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|44|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|45|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Staphylococcus aureus|||A|||C|||20101210142315\r" + 
			"OBX|46|TX|4384297^URINE BACTERIAL CULTURE^L01N||     _____________________|||A|||C|||20101210142315\r" + 
			"OBX|47|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Trimethoprim/Sulfa Interp corrected from S on 12/10/2010 14:20 MST|||A|||C|||20101210142315\r" + 
			"OBX|48|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|49|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|50|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|51|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|52|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|53|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|54|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|55|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|56|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|57|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|58|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|59|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|60|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|61|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|62|TX|4384297^URINE BACTERIAL CULTURE^L01N||     *** SUSCEPTIBILITY FOOTNOTES|||A|||C|||20101210142315\r" + 
			"OBX|63|TX|4384297^URINE BACTERIAL CULTURE^L01N||     (1)|||A|||C|||20101210142315\r" + 
			"OBX|64|TX|4384297^URINE BACTERIAL CULTURE^L01N||     This organism has been shown to produce an Extended Spectrum Beta|||A|||C|||20101210142315\r" + 
			"OBX|65|TX|4384297^URINE BACTERIAL CULTURE^L01N||     lactamase (ESBL) and should be considered resistant to all|||A|||C|||20101210142315\r" + 
			"OBX|66|TX|4384297^URINE BACTERIAL CULTURE^L01N||     Cephalosporins.|||A|||C|||20101210142315\r" + 
			"OBX|67|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|68|TX|4384297^URINE BACTERIAL CULTURE^L01N|||||A|||C|||20101210142315\r" + 
			"OBX|69|TX|4384297^URINE BACTERIAL CULTURE^L01N||     __________________________________________________________|||A|||C|||20101210142315\r" + 
			"";
	
	final static String LAB42 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214100421||ORU^R01|Q199823599T198320671|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452155^101AP|313048^NON-GYN CYTOLOGY REPORT^L01N|||20101214095100|||||||20101214095100|^^Liver FNA|1001745^Test, Physician - p-Test Physician||||NF-10-0007545||20101214100357||AP|F||1^^^20101214095100^^~^^^^^RT|\r" + 
			"OBX|1|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||*****Surgical Pathology Report*****||||||F|||20101214100357\r" + 
			"OBX|2|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|3|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Accession Number     NF-10-0007545||||||F|||20101214100357\r" + 
			"OBX|4|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Collected Date/Time  2010-12-14 09:51 MST||||||F|||20101214100357\r" + 
			"OBX|5|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Received Date/Time   2010-12-14 09:51 MST||||||F|||20101214100357\r" + 
			"OBX|6|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214100357\r" + 
			"OBX|7|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|8|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|9|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Clinical Information||||||F|||20101214100357\r" + 
			"OBX|10|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      None||||||F|||20101214100357\r" + 
			"OBX|11|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|12|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|13|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Specimen||||||F|||20101214100357\r" + 
			"OBX|14|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Liver FNA||||||F|||20101214100357\r" + 
			"OBX|15|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|16|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|17|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Gross Description||||||F|||20101214100357\r" + 
			"OBX|18|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      2 ml of yellow fluid is received.||||||F|||20101214100357\r" + 
			"OBX|19|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|20|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|21|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Diagnosis||||||F|||20101214100357\r" + 
			"OBX|22|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      MALIGNANT CELLS PRESENT. Features are consistent with a metastatic adenocarcinoma.||||||F|||20101214100357\r" + 
			"OBX|23|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|24|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Screened by: CG||||||F|||20101214100357\r" + 
			"OBX|25|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|26|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Reported by: Gates, Colleen||||||F|||20101214100357\r" + 
			"OBX|27|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||                            (Electronically signed by)||||||F|||20101214100357\r" + 
			"OBX|28|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||                            2010/12/14 10:03||||||F|||20101214100357\r" + 
			"OBX|29|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|30|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|31|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Comment||||||F|||20101214100357\r" + 
			"OBX|32|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      Metastatic adenocarcinoma consistemt with Lung Primary.||||||F|||20101214100357\r" + 
			"OBX|33|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|34|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100357\r" + 
			"OBX|35|TX|4187233^NON-GYN CYTOLOGY REPORT^L01N||      *****Microbiology Wound/Abscess*****||||||F|||20101214100357\r" + 
			"";
	
	final static String LAB43 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214100752||ORU^R01|Q199823608T198320680|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452185^101AP|313050^CYTOPATHOLOGY REPORT^L01N|||20101214100500|||||||20101214100500|^^Cervical Sample LBC|1001745^Test, Physician - p-Test Physician||||GH-10-0152011||20101214100749||AP|F||1^^^20101214100500^^~^^^^^RT|\r" + 
			"OBX|1|TX|4187232^GYN CYTOLOGY REPORT^L01N||*****Gyn Cytopathology Report*****||||||F|||20101214100749\r" + 
			"OBX|2|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|3|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Accession Number     GH-10-0152011||||||F|||20101214100749\r" + 
			"OBX|4|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Collected Date/Time  2010-12-14 10:05 MST||||||F|||20101214100749\r" + 
			"OBX|5|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Received Date/Time   2010-12-14 10:05 MST||||||F|||20101214100749\r" + 
			"OBX|6|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214100749\r" + 
			"OBX|7|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|8|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|9|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Clinical Information||||||F|||20101214100749\r" + 
			"OBX|10|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Date of LMP: n/a||||||F|||20101214100749\r" + 
			"OBX|11|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|12|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|13|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Specimen||||||F|||20101214100749\r" + 
			"OBX|14|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Cervical Sample LBC||||||F|||20101214100749\r" + 
			"OBX|15|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|16|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|17|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Specimen Adequacy:||||||F|||20101214100749\r" + 
			"OBX|18|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Satisfactory for Evaluation.  Transformation Zone Present.||||||F|||20101214100749\r" + 
			"OBX|19|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|20|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|21|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Diagnosis:||||||F|||20101214100749\r" + 
			"OBX|22|TX|4187232^GYN CYTOLOGY REPORT^L01N||      EPITHELIAL CELL ABNORMALITY - Low-Grade Squamous Intraepithelial Lesion (LSIL).||||||F|||20101214100749\r" + 
			"OBX|23|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|24|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|25|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Recommendation:||||||F|||20101214100749\r" + 
			"OBX|26|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Repeat the smear in 6 months.||||||F|||20101214100749\r" + 
			"OBX|27|TX|4187232^GYN CYTOLOGY REPORT^L01N||      You may need to modify the recommendation based on your clinical assessment and||||||F|||20101214100749\r" + 
			"OBX|28|TX|4187232^GYN CYTOLOGY REPORT^L01N||      judgement.  Please refer to the Alberta Cervical Cancer Screening Program (ACCSP)||||||F|||20101214100749\r" + 
			"OBX|29|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Clinical Practice Guidelines for patient management at www.topalbertadoctors.org.||||||F|||20101214100749\r" + 
			"OBX|30|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|31|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Screened by: CG||||||F|||20101214100749\r" + 
			"OBX|32|TX|4187232^GYN CYTOLOGY REPORT^L01N||||||||F|||20101214100749\r" + 
			"OBX|33|TX|4187232^GYN CYTOLOGY REPORT^L01N||      Reported by: Gates, Colleen||||||F|||20101214100749\r" + 
			"OBX|34|TX|4187232^GYN CYTOLOGY REPORT^L01N||                            (Electronically signed by)||||||F|||20101214100749\r" + 
			"OBX|35|TX|4187232^GYN CYTOLOGY REPORT^L01N||                            2010/12/14 10:07||||||F|||20101214100749\r" + 
			"";
	
	final static String LAB44 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214104054||ORU^R01|Q199823626T198320698|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452202^101AP|313046^SURGICAL PATHOLOGY REPORT^L01N|||20101214102300|||||||20101214102300|^^LN, Regional Resection(5)|1001745^Test, Physician - p-Test Physician||||SF-10-0020326||20101214104043||AP|F||1^^^20101214102300^^~^^^^^RT|\r" + 
			"OBX|1|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||*****Surgical Pathology Report*****||||||F|||20101214104043\r" + 
			"OBX|2|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|3|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Accession Number     SF-10-0020326||||||F|||20101214104043\r" + 
			"OBX|4|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Collected Date/Time  2010-12-14 10:23 MST||||||F|||20101214104043\r" + 
			"OBX|5|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Received Date/Time   2010-12-14 10:23 MST||||||F|||20101214104043\r" + 
			"OBX|6|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214104043\r" + 
			"OBX|7|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|8|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|9|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Specimen Description||||||F|||20101214104043\r" + 
			"OBX|10|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      A.  Inferior pulmonary ligament node||||||F|||20101214104043\r" + 
			"OBX|11|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B.  Right lower lobe superior segment||||||F|||20101214104043\r" + 
			"OBX|12|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      C.  Right middle lobe wedge resection||||||F|||20101214104043\r" + 
			"OBX|13|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      D.  #10R lymph node||||||F|||20101214104043\r" + 
			"OBX|14|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      E.  Subcarinal #7 lymph node||||||F|||20101214104043\r" + 
			"OBX|15|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      F.  #12 right upper lobe node||||||F|||20101214104043\r" + 
			"OBX|16|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G.  Right upper lobe posterior segment *please note 2 nodules*||||||F|||20101214104043\r" + 
			"OBX|17|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|18|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|19|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Clinical Information||||||F|||20101214104043\r" + 
			"OBX|20|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Smoker, enlarging RUL nodule, incidental RML nodule - query metastatic lung Ca.||||||F|||20101214104043\r" + 
			"OBX|21|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|22|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214104043\r" + 
			"OBX|23|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      __||||||F|||20101214104043\r" + 
			"OBX|24|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|25|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|26|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Diagnosis||||||F|||20101214104043\r" + 
			"OBX|27|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      A.  Inferior Pulmonary Ligament Node:||||||F|||20101214104043\r" + 
			"OBX|28|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214104043\r" + 
			"OBX|29|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|30|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B.  Right Lower Lobe Superior Segment:||||||F|||20101214104043\r" + 
			"OBX|31|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Moderately differentiated adenocarcinoma (1.4 cm),with areas of||||||F|||20101214104043\r" + 
			"OBX|32|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  bronchiolo-alveolar carcinoma like growth pattern.||||||F|||20101214104043\r" + 
			"OBX|33|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Arises in a background of respiratory bronchiolitis, congestion and||||||F|||20101214104043\r" + 
			"OBX|34|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                   mild emphysematous change.||||||F|||20101214104043\r" + 
			"OBX|35|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Resection margins negative for malignancy and dysplasia||||||F|||20101214104043\r" + 
			"OBX|36|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Pleura free of tumor.||||||F|||20101214104043\r" + 
			"OBX|37|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|38|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      C.  Right Middle Lobe Wedge Resection:||||||F|||20101214104043\r" + 
			"OBX|39|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Hyalinized fibrous nodule with chronic inflammatory cells.||||||F|||20101214104043\r" + 
			"OBX|40|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Negative for Malignancy and Dysplasia.||||||F|||20101214104043\r" + 
			"OBX|41|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|42|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      D.  #10 R Lymph Node:||||||F|||20101214104043\r" + 
			"OBX|43|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214104043\r" + 
			"OBX|44|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|45|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      E.  Subcarinal #7 Lymph Node:||||||F|||20101214104043\r" + 
			"OBX|46|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214104043\r" + 
			"OBX|47|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|48|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      F.  #12 Right Upper Lobe Node:||||||F|||20101214104043\r" + 
			"OBX|49|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214104043\r" + 
			"OBX|50|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|51|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G.  Right Upper Lobe, Posterior Segment:||||||F|||20101214104043\r" + 
			"OBX|52|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            -     1)Moderately differentiated adenocarcinoma (1.7 cm) in a||||||F|||20101214104043\r" + 
			"OBX|53|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  background of respiratory bronchiolitis and emphysematous change||||||F|||20101214104043\r" + 
			"OBX|54|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|55|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|56|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|57|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|58|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|59|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|60|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|61|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Tumor is morphologically different than that in specimen B and thus||||||F|||20101214104043\r" + 
			"OBX|62|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  is reflective of a second primary site.||||||F|||20101214104043\r" + 
			"OBX|63|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|64|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  2)Moderately differentiated adenocarcinoma (2.0 cm),with areas of||||||F|||20101214104043\r" + 
			"OBX|65|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  bronchiolo-alveolar carcinoma like growth pattern.||||||F|||20101214104043\r" + 
			"OBX|66|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Arises in a background of respiratory bronchiolitis, congestion and||||||F|||20101214104043\r" + 
			"OBX|67|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                   mild emphysematous change.||||||F|||20101214104043\r" + 
			"OBX|68|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|69|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  Tumor is morphologically the same as that seen in specimen B but||||||F|||20101214104043\r" + 
			"OBX|70|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  different from the first tumor in specimen G. Thus reflective of||||||F|||20101214104043\r" + 
			"OBX|71|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  pulmonary metastases.||||||F|||20101214104043\r" + 
			"OBX|72|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|73|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  3) One microscopic area of Bronchioalveolar Carcinoma (0.6 cm)||||||F|||20101214104043\r" + 
			"OBX|74|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|75|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  4)  Atypical adenomatous hyperplasia (AAH)||||||F|||20101214104043\r" + 
			"OBX|76|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|77|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                  5) Resection margins negative for malignancy and dysplasia||||||F|||20101214104043\r" + 
			"OBX|78|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||                      Pleura free of tumor.||||||F|||20101214104043\r" + 
			"OBX|79|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|80|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214104043\r" + 
			"OBX|81|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|82|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214104043\r" + 
			"OBX|83|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Verified: 2010/12/14 10:40 AM||||||F|||20101214104043\r" + 
			"OBX|84|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      CG/CG||||||F|||20101214104043\r" + 
			"OBX|85|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|86|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214104043\r" + 
			"OBX|87|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      ________||||||F|||20101214104043\r" + 
			"OBX|88|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|89|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|90|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Synoptic Report||||||F|||20101214104043\r" + 
			"OBX|91|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G: CLS Lung Resection||||||F|||20101214104043\r" + 
			"OBX|92|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Specimen Type:||||||F|||20101214104043\r" + 
			"OBX|93|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Wedge resection||||||F|||20101214104043\r" + 
			"OBX|94|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Laterality:||||||F|||20101214104043\r" + 
			"OBX|95|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Right||||||F|||20101214104043\r" + 
			"OBX|96|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor Site:||||||F|||20101214104043\r" + 
			"OBX|97|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Upper lobe||||||F|||20101214104043\r" + 
			"OBX|98|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor type:||||||F|||20101214104043\r" + 
			"OBX|99|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Adenocarcinoma||||||F|||20101214104043\r" + 
			"OBX|100|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Grade:||||||F|||20101214104043\r" + 
			"OBX|101|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Moderately differentiated||||||F|||20101214104043\r" + 
			"OBX|102|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Location:||||||F|||20101214104043\r" + 
			"OBX|103|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Peripheral||||||F|||20101214104043\r" + 
			"OBX|104|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Satellite nodules in same lobe:||||||F|||20101214104043\r" + 
			"OBX|105|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Positive||||||F|||20101214104043\r" + 
			"OBX|106|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Intrapulmonary metastases:||||||F|||20101214104043\r" + 
			"OBX|107|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Positive||||||F|||20101214104043\r" + 
			"OBX|108|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Vascular invasion:||||||F|||20101214104043\r" + 
			"OBX|109|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|110|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Perineural invasion:||||||F|||20101214104043\r" + 
			"OBX|111|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|112|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Margins: - Bronchial:||||||F|||20101214104043\r" + 
			"OBX|113|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor 0.5 cm away from stapled resection margin||||||F|||20101214104043\r" + 
			"OBX|114|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Margins: - Medistinal:||||||F|||20101214104043\r" + 
			"OBX|115|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|116|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|117|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|118|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|119|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|120|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|121|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|122|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Pleural invasion:||||||F|||20101214104043\r" + 
			"OBX|123|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|124|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Atelectasis/Obstructive Pneumonia:||||||F|||20101214104043\r" + 
			"OBX|125|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|126|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Lymph nodes||||||F|||20101214104043\r" + 
			"OBX|127|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|128|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B: CLS Lung Resection||||||F|||20101214104043\r" + 
			"OBX|129|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Specimen Type:||||||F|||20101214104043\r" + 
			"OBX|130|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Wedge resection||||||F|||20101214104043\r" + 
			"OBX|131|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Laterality:||||||F|||20101214104043\r" + 
			"OBX|132|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Right||||||F|||20101214104043\r" + 
			"OBX|133|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor Site:||||||F|||20101214104043\r" + 
			"OBX|134|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Lower lobe||||||F|||20101214104043\r" + 
			"OBX|135|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor type:||||||F|||20101214104043\r" + 
			"OBX|136|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Adenocarcinoma||||||F|||20101214104043\r" + 
			"OBX|137|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Grade:||||||F|||20101214104043\r" + 
			"OBX|138|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Moderately differentiated||||||F|||20101214104043\r" + 
			"OBX|139|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Location:||||||F|||20101214104043\r" + 
			"OBX|140|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Peripheral||||||F|||20101214104043\r" + 
			"OBX|141|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Size:||||||F|||20101214104043\r" + 
			"OBX|142|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Maximum dimension  1.4  cm||||||F|||20101214104043\r" + 
			"OBX|143|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Satellite nodules in same lobe:||||||F|||20101214104043\r" + 
			"OBX|144|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|145|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Vascular invasion:||||||F|||20101214104043\r" + 
			"OBX|146|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|147|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Perineural invasion:||||||F|||20101214104043\r" + 
			"OBX|148|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|149|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Margins: - Bronchial:||||||F|||20101214104043\r" + 
			"OBX|150|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Tumor 1.5 cm away from stapled resection margin||||||F|||20101214104043\r" + 
			"OBX|151|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Pleural invasion:||||||F|||20101214104043\r" + 
			"OBX|152|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|153|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Atelectasis/Obstructive Pneumonia:||||||F|||20101214104043\r" + 
			"OBX|154|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|155|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      In situ component:||||||F|||20101214104043\r" + 
			"OBX|156|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|157|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Lymph nodes:||||||F|||20101214104043\r" + 
			"OBX|158|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Negative||||||F|||20101214104043\r" + 
			"OBX|159|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|160|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|161|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Gross Description||||||F|||20101214104043\r" + 
			"OBX|162|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Received are specimens A to G.  All requisitions and specimen containers are labelled||||||F|||20101214104043\r" + 
			"OBX|163|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      with the patient's name.  The cassettes and AP identifiers are labelled with the Surgical||||||F|||20101214104043\r" + 
			"OBX|164|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Number SF10-19864.||||||F|||20101214104043\r" + 
			"OBX|165|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|166|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      A.  Received is a single grey/tan irregular soft tissue nodule measuring 1.0 x 0.8 x 0.2||||||F|||20101214104043\r" + 
			"OBX|167|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      cm.  It is trisected and submitted in one block.  ARH/ct||||||F|||20101214104043\r" + 
			"OBX|168|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|169|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B.  The specimen consists of a lung wedge (10 x 6 x 2.5 cm) with a single yellow||||||F|||20101214104043\r" + 
			"OBX|170|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      spiculated nodule (1.0 cm) located 1.5 cm from the painted (black) staple margin.||||||F|||20101214104043\r" + 
			"OBX|171|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Representative section submitted for QS.  IC/ct||||||F|||20101214104043\r" + 
			"OBX|172|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|173|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Another stapled margin is identified opposite to the first.  The staples are cut and the||||||F|||20101214104043\r" + 
			"OBX|174|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      resection surface is painted blue.  The lung wedge is sectioned serially. The primary||||||F|||20101214104043\r" + 
			"OBX|175|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      tumor mass identified during the frozen section is seen.  No other tumor masses, areas of||||||F|||20101214104043\r" + 
			"OBX|176|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      consolidation or fibrosis are identified.  Sections are submitted as follows:||||||F|||20101214104043\r" + 
			"OBX|177|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|178|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|179|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|180|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|181|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|182|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|183|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|184|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B2-B3 sections of the primary tumor mass, submitted in toto||||||F|||20101214104043\r" + 
			"OBX|185|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B4-B6 representative sections of the lung parenchyma adjacent to the tumor mass||||||F|||20101214104043\r" + 
			"OBX|186|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B7-B10 random representative sections of the grossly normal-appearing lung parenchyma||||||F|||20101214104043\r" + 
			"OBX|187|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      ARH/ct||||||F|||20101214104043\r" + 
			"OBX|188|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|189|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      C.  The specimen consists of a lung wedge (3.5 x 2.5 x 0.5 cm) containing a single||||||F|||20101214104043\r" + 
			"OBX|190|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      subpleural nodule (0.1 cm and ? circled).  Staple resection margin painted black.  Nodule||||||F|||20101214104043\r" + 
			"OBX|191|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      submitted in toto for QS.  IC/ct||||||F|||20101214104043\r" + 
			"OBX|192|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|193|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      The remaining portion of the wedge was sectioned serially and appears to be grossly||||||F|||20101214104043\r" + 
			"OBX|194|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      normal.  Random representative sections will be submitted in blocks C2-C4.  ARH/ct||||||F|||20101214104043\r" + 
			"OBX|195|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|196|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      D.  Received is a single grey/tan irregular portion of soft tissue measuring 1.5 x 0.6 x||||||F|||20101214104043\r" + 
			"OBX|197|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      0.2 cm.  It is bisected longitudinally and submitted totally in one block.||||||F|||20101214104043\r" + 
			"OBX|198|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|199|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      E.  Received are two portions of irregular grey/tan tissue measuring in aggregate 1.2 x||||||F|||20101214104043\r" + 
			"OBX|200|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      0.8 x 0.3 cm.  Each of the portions are bisected longitudinally and submitted totally in||||||F|||20101214104043\r" + 
			"OBX|201|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      blocks E1 and E2.||||||F|||20101214104043\r" + 
			"OBX|202|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|203|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      F.  Received are multiple portions of grey/tan and irregular soft tissue measuring in||||||F|||20101214104043\r" + 
			"OBX|204|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      aggregate 1.5 x 1.3 x 0.2 cm.  The largest portion is bisected and submitted totally in||||||F|||20101214104043\r" + 
			"OBX|205|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      block F1.  The remaining portions are submitted totally in block F2.||||||F|||20101214104043\r" + 
			"OBX|206|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|207|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G.  Received is a wedge of lung tissue measuring 13.0 x 7.5 x 1.3 cm.  A stapled||||||F|||20101214104043\r" + 
			"OBX|208|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      resection margin is identified.  The stapled resection margin is painted blue.  The||||||F|||20101214104043\r" + 
			"OBX|209|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      pleural surface shows puckering, at a distance of 2.5 cm from the stapled margin.||||||F|||20101214104043\r" + 
			"OBX|210|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Underneath this puckered pleural surface, an irregular firm-to-hard nodule can be||||||F|||20101214104043\r" + 
			"OBX|211|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      palpated.  On serial sectioning of the lung wedge, a yellow/tan firm-to-hard irregular||||||F|||20101214104043\r" + 
			"OBX|212|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      nodule is identified coming within 0.2 cm of the pleural surface and 0.5 cm from the||||||F|||20101214104043\r" + 
			"OBX|213|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      stapled resection margin.  The mass itself measures 1.7 x 1.1 x 1.2 cm.  Another||||||F|||20101214104043\r" + 
			"OBX|214|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      irregular firm-to-hard yellow/tan mass is identified coming within 0.2 cm of the puckered||||||F|||20101214104043\r" + 
			"OBX|215|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      pleural surface.  This mass measures 2.0 x 1.1 x 1.1 cm.  This mass is located at a||||||F|||20101214104043\r" + 
			"OBX|216|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      distance of 1.7 cm from the first tumor mass (where the depth of the lung wedge is 4.5||||||F|||20101214104043\r" + 
			"OBX|217|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      cm).  The rest of the lung parenchyma appears to be grossly normal with no areas of||||||F|||20101214104043\r" + 
			"OBX|218|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      fibrosis or consolidation identified.  Sections are submitted as follows:||||||F|||20101214104043\r" + 
			"OBX|219|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|220|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G1-G3 sections of the primary tumor mass, submitted in toto||||||F|||20101214104043\r" + 
			"OBX|221|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G4 sections of the primary and secondary tumor mass to show the relationship between the||||||F|||20101214104043\r" + 
			"OBX|222|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      two (contiguous sections)||||||F|||20101214104043\r" + 
			"OBX|223|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G5-G7 representative sections of the secondary tumor mass to show its relationship with||||||F|||20101214104043\r" + 
			"OBX|224|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      puckered pleural surface||||||F|||20101214104043\r" + 
			"OBX|225|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      G8-G12 random representative sections of the grossly normal-appearing lung parenchyma||||||F|||20101214104043\r" + 
			"OBX|226|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      ARH/ct||||||F|||20101214104043\r" + 
			"OBX|227|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|228|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|229|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Frozen Section Diagnosis||||||F|||20101214104043\r" + 
			"OBX|230|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      B.  Right Lower Lobe Superior Segment:||||||F|||20101214104043\r" + 
			"OBX|231|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            - Well-differentiated adenocarcinoma with BAC features  /IC  Conf #65||||||F|||20101214104043\r" + 
			"OBX|232|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|233|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      C.  Right Middle Lobe Wedge:||||||F|||20101214104043\r" + 
			"OBX|234|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||            - Benign fibrous nodule, negative for malignancy  IC/ct  Conf #66||||||F|||20101214104043\r" + 
			"OBX|235|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|236|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Reported by: V V Falck||||||F|||20101214104043\r" + 
			"OBX|237|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|238|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|239|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|240|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|241|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|242|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|243|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||||||||F|||20101214104043\r" + 
			"OBX|244|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Pathologist Comment||||||F|||20101214104043\r" + 
			"OBX|245|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      Sections show insitu and invasive adenocarcinomas. One of the adenocarcinomas is distinct||||||F|||20101214104043\r" + 
			"OBX|246|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      from the rest and thus indicative of multiple primary tumors, however the remaining||||||F|||20101214104043\r" + 
			"OBX|247|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      tumors in the two separate lobes are histologically similar and consistent with pulmonary||||||F|||20101214104043\r" + 
			"OBX|248|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      metastases. As the lower lobe tumor is smaller than the upper lobe tumor and insitu||||||F|||20101214104043\r" + 
			"OBX|249|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      disease is noted in the upper lobe, the upper lobe would be regarded as the primary tumor||||||F|||20101214104043\r" + 
			"OBX|250|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      in this case. This means two separate primary tumors are present in the upper lobe with||||||F|||20101214104043\r" + 
			"OBX|251|TX|4187235^SURGICAL PATHOLOGY REPORT^L01N||      upper lobe and lower lobe metastases from one of the two primary tumors.||||||F|||20101214104043\r" + 
			"";
	
	final static String LAB45 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214104724||ORU^R01|Q199823629T198320701|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452155^101AP|313048^NON-GYN CYTOLOGY REPORT^L01N|||20101214095100|||||||20101214095100|^^Liver FNA|1001745^Test, Physician - p-Test Physician||||NF-10-0007545||20101214104725||AP|F||1^^^20101214095100^^~^^^^^RT|\r" + 
			"OBX|1|TX|4187228^ADDENDUM REPORT^L01N||*****Addendum Report*****||||||F|||20101214104725\r" + 
			"OBX|2|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|3|TX|4187228^ADDENDUM REPORT^L01N||      Accession Number     NF-10-0007545||||||F|||20101214104725\r" + 
			"OBX|4|TX|4187228^ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 09:51 MST||||||F|||20101214104725\r" + 
			"OBX|5|TX|4187228^ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 09:51 MST||||||F|||20101214104725\r" + 
			"OBX|6|TX|4187228^ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214104725\r" + 
			"OBX|7|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|8|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|9|TX|4187228^ADDENDUM REPORT^L01N||      Addendum Reason||||||F|||20101214104725\r" + 
			"OBX|10|TX|4187228^ADDENDUM REPORT^L01N||      Testing Netcare and POS.||||||F|||20101214104725\r" + 
			"OBX|11|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|12|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|13|TX|4187228^ADDENDUM REPORT^L01N||      Addendum Diagnosis||||||F|||20101214104725\r" + 
			"OBX|14|TX|4187228^ADDENDUM REPORT^L01N||      No change in original diagnosis.||||||F|||20101214104725\r" + 
			"OBX|15|TX|4187228^ADDENDUM REPORT^L01N||            FNA, showing metastatic adenocarcinoma.||||||F|||20101214104725\r" + 
			"OBX|16|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|17|TX|4187228^ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214104725\r" + 
			"OBX|18|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|19|TX|4187228^ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214104725\r" + 
			"OBX|20|TX|4187228^ADDENDUM REPORT^L01N||      Verified: 2010/12/14 10:47 AM||||||F|||20101214104725\r" + 
			"OBX|21|TX|4187228^ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214104725\r" + 
			"OBX|22|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|23|TX|4187228^ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214104725\r" + 
			"OBX|24|TX|4187228^ADDENDUM REPORT^L01N||      ________||||||F|||20101214104725\r" + 
			"OBX|25|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|26|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|27|TX|4187228^ADDENDUM REPORT^L01N||      Addendum Discussion||||||F|||20101214104725\r" + 
			"OBX|28|TX|4187228^ADDENDUM REPORT^L01N||      No discussion.||||||F|||20101214104725\r" + 
			"OBX|29|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|30|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|31|TX|4187228^ADDENDUM REPORT^L01N||      *****Non-Gyn Cytopathology Report*****||||||F|||20101214104725\r" + 
			"OBX|32|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|33|TX|4187228^ADDENDUM REPORT^L01N||      Accession Number     NF-10-0007545||||||F|||20101214104725\r" + 
			"OBX|34|TX|4187228^ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 09:51 MST||||||F|||20101214104725\r" + 
			"OBX|35|TX|4187228^ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 09:51 MST||||||F|||20101214104725\r" + 
			"OBX|36|TX|4187228^ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214104725\r" + 
			"OBX|37|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|38|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|39|TX|4187228^ADDENDUM REPORT^L01N||      Clinical Information||||||F|||20101214104725\r" + 
			"OBX|40|TX|4187228^ADDENDUM REPORT^L01N||      None||||||F|||20101214104725\r" + 
			"OBX|41|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|42|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|43|TX|4187228^ADDENDUM REPORT^L01N||      Specimen||||||F|||20101214104725\r" + 
			"OBX|44|TX|4187228^ADDENDUM REPORT^L01N||      Liver FNA||||||F|||20101214104725\r" + 
			"OBX|45|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|46|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|47|TX|4187228^ADDENDUM REPORT^L01N||      Gross Description||||||F|||20101214104725\r" + 
			"OBX|48|TX|4187228^ADDENDUM REPORT^L01N||      2 ml of yellow fluid is received.||||||F|||20101214104725\r" + 
			"OBX|49|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|50|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|51|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|52|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|53|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|54|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|55|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|56|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|57|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|58|TX|4187228^ADDENDUM REPORT^L01N||      Diagnosis||||||F|||20101214104725\r" + 
			"OBX|59|TX|4187228^ADDENDUM REPORT^L01N||      MALIGNANT CELLS PRESENT. Features are consistent with a metastatic adenocarcinoma.||||||F|||20101214104725\r" + 
			"OBX|60|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|61|TX|4187228^ADDENDUM REPORT^L01N||      Screened by: CG||||||F|||20101214104725\r" + 
			"OBX|62|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|63|TX|4187228^ADDENDUM REPORT^L01N||      Reported by: Gates, Colleen||||||F|||20101214104725\r" + 
			"OBX|64|TX|4187228^ADDENDUM REPORT^L01N||                            (Electronically signed by)||||||F|||20101214104725\r" + 
			"OBX|65|TX|4187228^ADDENDUM REPORT^L01N||                            2010/12/14 10:03||||||F|||20101214104725\r" + 
			"OBX|66|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|67|TX|4187228^ADDENDUM REPORT^L01N||||||||F|||20101214104725\r" + 
			"OBX|68|TX|4187228^ADDENDUM REPORT^L01N||      Comment||||||F|||20101214104725\r" + 
			"OBX|69|TX|4187228^ADDENDUM REPORT^L01N||      Metastatic adenocarcinoma consistemt with Lung Primary.||||||F|||20101214104725\r" + 
			"";
	
	final static String LAB46 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214104908||ORU^R01|Q199823634T198320706|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452185^101AP|313050^CYTOPATHOLOGY REPORT^L01N|||20101214100500|||||||20101214100500|^^Cervical Sample LBC|1001745^Test, Physician - p-Test Physician||||GH-10-0152011||20101214104912||AP|F||1^^^20101214100500^^~^^^^^RT|\r" + 
			"OBX|1|TX|4788381^GYN ADDENDUM REPORT^L01N||*****Gyn Addendum Report*****||||||F|||20101214104912\r" + 
			"OBX|2|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|3|TX|4788381^GYN ADDENDUM REPORT^L01N||      Accession Number    GH-10-0152011||||||F|||20101214104912\r" + 
			"OBX|4|TX|4788381^GYN ADDENDUM REPORT^L01N||      Collected Date/Time 2010-12-14 10:05 MST||||||F|||20101214104912\r" + 
			"OBX|5|TX|4788381^GYN ADDENDUM REPORT^L01N||      Received Date/Time  2010-12-14 10:05 MST||||||F|||20101214104912\r" + 
			"OBX|6|TX|4788381^GYN ADDENDUM REPORT^L01N||      Pathologist         Gates, Colleen||||||F|||20101214104912\r" + 
			"OBX|7|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|8|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|9|TX|4788381^GYN ADDENDUM REPORT^L01N||      Addendum Reason||||||F|||20101214104912\r" + 
			"OBX|10|TX|4788381^GYN ADDENDUM REPORT^L01N||      Revision of diagnosis!||||||F|||20101214104912\r" + 
			"OBX|11|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|12|TX|4788381^GYN ADDENDUM REPORT^L01N||      PLEASE NOTE: This Addendum Report introduces a potentially significant change in the||||||F|||20101214104912\r" + 
			"OBX|13|TX|4788381^GYN ADDENDUM REPORT^L01N||      diagnosis or a change in patient information. Please clearly mark the Original Report as||||||F|||20101214104912\r" + 
			"OBX|14|TX|4788381^GYN ADDENDUM REPORT^L01N||      having been changed, with a clear reference to this Addendum Report (e.g. \"Please see||||||F|||20101214104912\r" + 
			"OBX|15|TX|4788381^GYN ADDENDUM REPORT^L01N||      additional report\").||||||F|||20101214104912\r" + 
			"OBX|16|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|17|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|18|TX|4788381^GYN ADDENDUM REPORT^L01N||      Addendum Diagnosis||||||F|||20101214104912\r" + 
			"OBX|19|TX|4788381^GYN ADDENDUM REPORT^L01N||      A: CLS GYN Addendum||||||F|||20101214104912\r" + 
			"OBX|20|TX|4788381^GYN ADDENDUM REPORT^L01N||      Specimen:||||||F|||20101214104912\r" + 
			"OBX|21|TX|4788381^GYN ADDENDUM REPORT^L01N||      Cervical Sample LBC.||||||F|||20101214104912\r" + 
			"OBX|22|TX|4788381^GYN ADDENDUM REPORT^L01N||      Specimen Adequacy:||||||F|||20101214104912\r" + 
			"OBX|23|TX|4788381^GYN ADDENDUM REPORT^L01N||      Satisfactory for Evaluation.||||||F|||20101214104912\r" + 
			"OBX|24|TX|4788381^GYN ADDENDUM REPORT^L01N||      Transformation Zone:||||||F|||20101214104912\r" + 
			"OBX|25|TX|4788381^GYN ADDENDUM REPORT^L01N||      Present.||||||F|||20101214104912\r" + 
			"OBX|26|TX|4788381^GYN ADDENDUM REPORT^L01N||      Diagnosis:||||||F|||20101214104912\r" + 
			"OBX|27|TX|4788381^GYN ADDENDUM REPORT^L01N||      Atypical Glandular Cells of Undetermined Significance (AGUS).||||||F|||20101214104912\r" + 
			"OBX|28|TX|4788381^GYN ADDENDUM REPORT^L01N||      Recommendation:||||||F|||20101214104912\r" + 
			"OBX|29|TX|4788381^GYN ADDENDUM REPORT^L01N||      Repeat the smear in 6 months.||||||F|||20101214104912\r" + 
			"OBX|30|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|31|TX|4788381^GYN ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214104912\r" + 
			"OBX|32|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|33|TX|4788381^GYN ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214104912\r" + 
			"OBX|34|TX|4788381^GYN ADDENDUM REPORT^L01N||      Verified: 2010/12/14 10:49 AM||||||F|||20101214104912\r" + 
			"OBX|35|TX|4788381^GYN ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214104912\r" + 
			"OBX|36|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|37|TX|4788381^GYN ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214104912\r" + 
			"OBX|38|TX|4788381^GYN ADDENDUM REPORT^L01N||      ________||||||F|||20101214104912\r" + 
			"OBX|39|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|40|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|41|TX|4788381^GYN ADDENDUM REPORT^L01N||      *****Gyn Cytopathology Report*****||||||F|||20101214104912\r" + 
			"OBX|42|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|43|TX|4788381^GYN ADDENDUM REPORT^L01N||      Accession Number     GH-10-0152011||||||F|||20101214104912\r" + 
			"OBX|44|TX|4788381^GYN ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 10:05 MST||||||F|||20101214104912\r" + 
			"OBX|45|TX|4788381^GYN ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 10:05 MST||||||F|||20101214104912\r" + 
			"OBX|46|TX|4788381^GYN ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214104912\r" + 
			"OBX|47|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|48|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|49|TX|4788381^GYN ADDENDUM REPORT^L01N||      Clinical Information||||||F|||20101214104912\r" + 
			"OBX|50|TX|4788381^GYN ADDENDUM REPORT^L01N||      Date of LMP: n/a||||||F|||20101214104912\r" + 
			"OBX|51|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|52|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|53|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|54|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|55|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|56|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|57|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|58|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|59|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|60|TX|4788381^GYN ADDENDUM REPORT^L01N||      Specimen||||||F|||20101214104912\r" + 
			"OBX|61|TX|4788381^GYN ADDENDUM REPORT^L01N||      Cervical Sample LBC||||||F|||20101214104912\r" + 
			"OBX|62|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|63|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|64|TX|4788381^GYN ADDENDUM REPORT^L01N||      Specimen Adequacy:||||||F|||20101214104912\r" + 
			"OBX|65|TX|4788381^GYN ADDENDUM REPORT^L01N||      Satisfactory for Evaluation.  Transformation Zone Present.||||||F|||20101214104912\r" + 
			"OBX|66|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|67|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|68|TX|4788381^GYN ADDENDUM REPORT^L01N||      Diagnosis:||||||F|||20101214104912\r" + 
			"OBX|69|TX|4788381^GYN ADDENDUM REPORT^L01N||      EPITHELIAL CELL ABNORMALITY - Low-Grade Squamous Intraepithelial Lesion (LSIL).||||||F|||20101214104912\r" + 
			"OBX|70|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|71|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|72|TX|4788381^GYN ADDENDUM REPORT^L01N||      Recommendation:||||||F|||20101214104912\r" + 
			"OBX|73|TX|4788381^GYN ADDENDUM REPORT^L01N||      Repeat the smear in 6 months.||||||F|||20101214104912\r" + 
			"OBX|74|TX|4788381^GYN ADDENDUM REPORT^L01N||      You may need to modify the recommendation based on your clinical assessment and||||||F|||20101214104912\r" + 
			"OBX|75|TX|4788381^GYN ADDENDUM REPORT^L01N||      judgement.  Please refer to the Alberta Cervical Cancer Screening Program (ACCSP)||||||F|||20101214104912\r" + 
			"OBX|76|TX|4788381^GYN ADDENDUM REPORT^L01N||      Clinical Practice Guidelines for patient management at www.topalbertadoctors.org.||||||F|||20101214104912\r" + 
			"OBX|77|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|78|TX|4788381^GYN ADDENDUM REPORT^L01N||      Screened by: CG||||||F|||20101214104912\r" + 
			"OBX|79|TX|4788381^GYN ADDENDUM REPORT^L01N||||||||F|||20101214104912\r" + 
			"OBX|80|TX|4788381^GYN ADDENDUM REPORT^L01N||      Reported by: Gates, Colleen||||||F|||20101214104912\r" + 
			"OBX|81|TX|4788381^GYN ADDENDUM REPORT^L01N||                            (Electronically signed by)||||||F|||20101214104912\r" + 
			"OBX|82|TX|4788381^GYN ADDENDUM REPORT^L01N||                            2010/12/14 10:07||||||F|||20101214104912\r" + 
			"";
	
	final static String LAB47 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214105653||ORU^R01|Q199823641T198320713|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452258^101AP|4388088^CONSULT REPORT^L01N|||20101214105300|||||||20101214105300|^^Outside Block/Slide|1000000^Unknown1, Physician, MD||||RF-10-0000900||20101214105651||AP|F||1^^^20101214105300^^~^^^^^RT|1001745^Test, Physician - p-Test Physician\r" + 
			"OBX|1|TX|4788372^CONSULT REPORT^L01N||*****Consultation Report*****||||||F|||20101214105651\r" + 
			"OBX|2|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|3|TX|4788372^CONSULT REPORT^L01N||      Accession Number     RF-10-0000900||||||F|||20101214105651\r" + 
			"OBX|4|TX|4788372^CONSULT REPORT^L01N||      Collected Date/Time  2010-12-14 10:53 MST||||||F|||20101214105651\r" + 
			"OBX|5|TX|4788372^CONSULT REPORT^L01N||      Received Date/Time   2010-12-14 10:53 MST||||||F|||20101214105651\r" + 
			"OBX|6|TX|4788372^CONSULT REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214105651\r" + 
			"OBX|7|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|8|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|9|TX|4788372^CONSULT REPORT^L01N||      Consult Description||||||F|||20101214105651\r" + 
			"OBX|10|TX|4788372^CONSULT REPORT^L01N||      Consult Specimen||||||F|||20101214105651\r" + 
			"OBX|11|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|12|TX|4788372^CONSULT REPORT^L01N||      Consult Type: TBCC Consult||||||F|||20101214105651\r" + 
			"OBX|13|TX|4788372^CONSULT REPORT^L01N||      Materials received:  21 Block  45 Slides||||||F|||20101214105651\r" + 
			"OBX|14|TX|4788372^CONSULT REPORT^L01N||      Received from:  PLC  Received by:  CG||||||F|||20101214105651\r" + 
			"OBX|15|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|16|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|17|TX|4788372^CONSULT REPORT^L01N||      Diagnosis||||||F|||20101214105651\r" + 
			"OBX|18|TX|4788372^CONSULT REPORT^L01N||      Abdominal Midline Mass, Core Biopsies:||||||F|||20101214105651\r" + 
			"OBX|19|TX|4788372^CONSULT REPORT^L01N||            -     High grade neuroendocrine carcinoma||||||F|||20101214105651\r" + 
			"OBX|20|TX|4788372^CONSULT REPORT^L01N||            -     (Extensive necrosis, prominent mitotic activity, Ki67 index greater||||||F|||20101214105651\r" + 
			"OBX|21|TX|4788372^CONSULT REPORT^L01N||                  than 50%)||||||F|||20101214105651\r" + 
			"OBX|22|TX|4788372^CONSULT REPORT^L01N||            -     Site of primary origin cannot be determined on histology (see micro||||||F|||20101214105651\r" + 
			"OBX|23|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|24|TX|4788372^CONSULT REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214105651\r" + 
			"OBX|25|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|26|TX|4788372^CONSULT REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214105651\r" + 
			"OBX|27|TX|4788372^CONSULT REPORT^L01N||      Verified: 2010/12/14 10:56 AM||||||F|||20101214105651\r" + 
			"OBX|28|TX|4788372^CONSULT REPORT^L01N||      CG/CG||||||F|||20101214105651\r" + 
			"OBX|29|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|30|TX|4788372^CONSULT REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214105651\r" + 
			"OBX|31|TX|4788372^CONSULT REPORT^L01N||      ________||||||F|||20101214105651\r" + 
			"OBX|32|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|33|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|34|TX|4788372^CONSULT REPORT^L01N||      Microscopic Description||||||F|||20101214105651\r" + 
			"OBX|35|TX|4788372^CONSULT REPORT^L01N||      This case was reviewed at the request of Dr. M. V, GI Oncology Group at the Tom Baker||||||F|||20101214105651\r" + 
			"OBX|36|TX|4788372^CONSULT REPORT^L01N||      Cancer Centre.  This patient has a midline upper abdominal mass which clinically would be||||||F|||20101214105651\r" + 
			"OBX|37|TX|4788372^CONSULT REPORT^L01N||      most consistent with a gallbladder carcinoma. Specific question in this review is whether||||||F|||20101214105651\r" + 
			"OBX|38|TX|4788372^CONSULT REPORT^L01N||      the possibility of a pancreatic primary as suggested originally, or other site of primary||||||F|||20101214105651\r" + 
			"OBX|39|TX|4788372^CONSULT REPORT^L01N||      is more likely.||||||F|||20101214105651\r" + 
			"OBX|40|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|41|TX|4788372^CONSULT REPORT^L01N||      This biopsy specimen consisted of two cores of tissue that consist of fibrous tissue and||||||F|||20101214105651\r" + 
			"OBX|42|TX|4788372^CONSULT REPORT^L01N||      necrotic areas with only small foci of residual viable neoplastic epithelium.  This||||||F|||20101214105651\r" + 
			"OBX|43|TX|4788372^CONSULT REPORT^L01N||      appears to be arranged into vague nests focally showing small intercellular lumina. There||||||F|||20101214105651\r" + 
			"OBX|44|TX|4788372^CONSULT REPORT^L01N||      appears to be a peripheral arrangement of nuclei and many of the nuclei have a granular||||||F|||20101214105651\r" + 
			"OBX|45|TX|4788372^CONSULT REPORT^L01N||      chromatin in a pattern raising the possibility of neuroendocrine tumor.  However, there||||||F|||20101214105651\r" + 
			"OBX|46|TX|4788372^CONSULT REPORT^L01N||      is prominent mitotic activity (three or more per high power field on the limited||||||F|||20101214105651\r" + 
			"OBX|47|TX|4788372^CONSULT REPORT^L01N||      material) and moderate nuclear pleomorphism. A positive synaptophysin immunostain||||||F|||20101214105651\r" + 
			"OBX|48|TX|4788372^CONSULT REPORT^L01N||      supports neuroendocrine differentiation.  I have also done a Ki67 which shows nuclear||||||F|||20101214105651\r" + 
			"OBX|49|TX|4788372^CONSULT REPORT^L01N||      positivity in approximately 50% of the viable tumor nuclei.||||||F|||20101214105651\r" + 
			"OBX|50|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|51|TX|4788372^CONSULT REPORT^L01N||      TTF1 positivity is not specific; Although a lung primary cannot be excluded, up to 50% of||||||F|||20101214105651\r" + 
			"OBX|52|TX|4788372^CONSULT REPORT^L01N||      high grade neuroendocrine carcinomas from sites other than the lung, may show TTF1||||||F|||20101214105651\r" + 
			"OBX|53|TX|4788372^CONSULT REPORT^L01N||      positivity.  I also did CDX2 immunostains which show patchy positivity further raising||||||F|||20101214105651\r" + 
			"OBX|54|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|55|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|56|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|57|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|58|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|59|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|60|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|61|TX|4788372^CONSULT REPORT^L01N||      the suspicion of a possible gastrointestinal origin, although serotonin immunostains are||||||F|||20101214105651\r" + 
			"OBX|62|TX|4788372^CONSULT REPORT^L01N||      negative.||||||F|||20101214105651\r" + 
			"OBX|63|TX|4788372^CONSULT REPORT^L01N||||||||F|||20101214105651\r" + 
			"OBX|64|TX|4788372^CONSULT REPORT^L01N||      In summary, the sample is limited, but the tumor that is viable has the morphology and||||||F|||20101214105651\r" + 
			"OBX|65|TX|4788372^CONSULT REPORT^L01N||      immunostaining pattern of a high grade neuroendocrine carcinoma (high mitotic activity,||||||F|||20101214105651\r" + 
			"OBX|66|TX|4788372^CONSULT REPORT^L01N||      Ki67 index of 50%). No other carcinoma component is present.  As originally reported, it||||||F|||20101214105651\r" + 
			"OBX|67|TX|4788372^CONSULT REPORT^L01N||      is difficult to determine site of origin in this small biopsy fragment.  A pancreatic||||||F|||20101214105651\r" + 
			"OBX|68|TX|4788372^CONSULT REPORT^L01N||      origin is a possibility, but a primary in other areas of the gastrointestinal tract||||||F|||20101214105651\r" + 
			"OBX|69|TX|4788372^CONSULT REPORT^L01N||      (including gallbladder), lung, or other organs with metastasis to the upper abdomen||||||F|||20101214105651\r" + 
			"OBX|70|TX|4788372^CONSULT REPORT^L01N||      (liver, gallbladder) cannot be excluded.||||||F|||20101214105651\r" + 
			"";
	
	final static String LAB48 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214110750||ORU^R01|Q199823648T198320720|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452258^101AP|4388088^CONSULT REPORT^L01N|||20101214105300|||||||20101214105300|^^Outside Block/Slide|1000000^Unknown1, Physician, MD||||RF-10-0000900||20101214110752||AP|F||1^^^20101214105300^^~^^^^^RT|1001745^Test, Physician - p-Test Physician\r" + 
			"OBX|1|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||*****Consultation Addendum Report*****||||||F|||20101214110752\r" + 
			"OBX|2|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|3|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Accession Number     RF-10-0000900||||||F|||20101214110752\r" + 
			"OBX|4|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 10:53 MST||||||F|||20101214110752\r" + 
			"OBX|5|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 10:53 MST||||||F|||20101214110752\r" + 
			"OBX|6|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214110752\r" + 
			"OBX|7|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|8|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|9|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Consult Addendum Diagnosis||||||F|||20101214110752\r" + 
			"OBX|10|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Slides and blocks reviewed by Lung specialty group.  Diagnosis remains unchanged.||||||F|||20101214110752\r" + 
			"OBX|11|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|12|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214110752\r" + 
			"OBX|13|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|14|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214110752\r" + 
			"OBX|15|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Verified: 2010/12/14 11:07 AM||||||F|||20101214110752\r" + 
			"OBX|16|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214110752\r" + 
			"OBX|17|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|18|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214110752\r" + 
			"OBX|19|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      ________||||||F|||20101214110752\r" + 
			"OBX|20|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|21|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|22|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Consult Addendum Discussion||||||F|||20101214110752\r" + 
			"OBX|23|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      No discussion.||||||F|||20101214110752\r" + 
			"OBX|24|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|25|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|26|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      *****Consultation Report*****||||||F|||20101214110752\r" + 
			"OBX|27|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|28|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Accession Number     RF-10-0000900||||||F|||20101214110752\r" + 
			"OBX|29|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 10:53 MST||||||F|||20101214110752\r" + 
			"OBX|30|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 10:53 MST||||||F|||20101214110752\r" + 
			"OBX|31|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214110752\r" + 
			"OBX|32|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|33|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|34|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Consult Description||||||F|||20101214110752\r" + 
			"OBX|35|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Consult Specimen||||||F|||20101214110752\r" + 
			"OBX|36|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|37|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Consult Type: TBCC Consult||||||F|||20101214110752\r" + 
			"OBX|38|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Materials received:  21 Block  45 Slides||||||F|||20101214110752\r" + 
			"OBX|39|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Received from:  PLC  Received by:  CG||||||F|||20101214110752\r" + 
			"OBX|40|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|41|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|42|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Diagnosis||||||F|||20101214110752\r" + 
			"OBX|43|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Abdominal Midline Mass, Core Biopsies:||||||F|||20101214110752\r" + 
			"OBX|44|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||            -     High grade neuroendocrine carcinoma||||||F|||20101214110752\r" + 
			"OBX|45|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||            -     (Extensive necrosis, prominent mitotic activity, Ki67 index greater||||||F|||20101214110752\r" + 
			"OBX|46|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||                  than 50%)||||||F|||20101214110752\r" + 
			"OBX|47|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|48|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|49|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|50|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|51|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|52|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|53|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|54|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|55|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||            -     Site of primary origin cannot be determined on histology (see micro||||||F|||20101214110752\r" + 
			"OBX|56|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|57|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214110752\r" + 
			"OBX|58|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|59|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214110752\r" + 
			"OBX|60|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Verified: 2010/12/14 10:56 AM||||||F|||20101214110752\r" + 
			"OBX|61|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214110752\r" + 
			"OBX|62|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|63|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214110752\r" + 
			"OBX|64|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      ________||||||F|||20101214110752\r" + 
			"OBX|65|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|66|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|67|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Microscopic Description||||||F|||20101214110752\r" + 
			"OBX|68|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      This case was reviewed at the request of Dr. M. V, GI Oncology Group at the Tom Baker||||||F|||20101214110752\r" + 
			"OBX|69|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Cancer Centre.  This patient has a midline upper abdominal mass which clinically would be||||||F|||20101214110752\r" + 
			"OBX|70|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      most consistent with a gallbladder carcinoma. Specific question in this review is whether||||||F|||20101214110752\r" + 
			"OBX|71|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      the possibility of a pancreatic primary as suggested originally, or other site of primary||||||F|||20101214110752\r" + 
			"OBX|72|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      is more likely.||||||F|||20101214110752\r" + 
			"OBX|73|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|74|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      This biopsy specimen consisted of two cores of tissue that consist of fibrous tissue and||||||F|||20101214110752\r" + 
			"OBX|75|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      necrotic areas with only small foci of residual viable neoplastic epithelium.  This||||||F|||20101214110752\r" + 
			"OBX|76|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      appears to be arranged into vague nests focally showing small intercellular lumina. There||||||F|||20101214110752\r" + 
			"OBX|77|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      appears to be a peripheral arrangement of nuclei and many of the nuclei have a granular||||||F|||20101214110752\r" + 
			"OBX|78|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      chromatin in a pattern raising the possibility of neuroendocrine tumor.  However, there||||||F|||20101214110752\r" + 
			"OBX|79|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      is prominent mitotic activity (three or more per high power field on the limited||||||F|||20101214110752\r" + 
			"OBX|80|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      material) and moderate nuclear pleomorphism. A positive synaptophysin immunostain||||||F|||20101214110752\r" + 
			"OBX|81|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      supports neuroendocrine differentiation.  I have also done a Ki67 which shows nuclear||||||F|||20101214110752\r" + 
			"OBX|82|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      positivity in approximately 50% of the viable tumor nuclei.||||||F|||20101214110752\r" + 
			"OBX|83|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|84|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      TTF1 positivity is not specific; Although a lung primary cannot be excluded, up to 50% of||||||F|||20101214110752\r" + 
			"OBX|85|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      high grade neuroendocrine carcinomas from sites other than the lung, may show TTF1||||||F|||20101214110752\r" + 
			"OBX|86|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      positivity.  I also did CDX2 immunostains which show patchy positivity further raising||||||F|||20101214110752\r" + 
			"OBX|87|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      the suspicion of a possible gastrointestinal origin, although serotonin immunostains are||||||F|||20101214110752\r" + 
			"OBX|88|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      negative.||||||F|||20101214110752\r" + 
			"OBX|89|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||||||||F|||20101214110752\r" + 
			"OBX|90|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      In summary, the sample is limited, but the tumor that is viable has the morphology and||||||F|||20101214110752\r" + 
			"OBX|91|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      immunostaining pattern of a high grade neuroendocrine carcinoma (high mitotic activity,||||||F|||20101214110752\r" + 
			"OBX|92|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      Ki67 index of 50%). No other carcinoma component is present.  As originally reported, it||||||F|||20101214110752\r" + 
			"OBX|93|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      is difficult to determine site of origin in this small biopsy fragment.  A pancreatic||||||F|||20101214110752\r" + 
			"OBX|94|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      origin is a possibility, but a primary in other areas of the gastrointestinal tract||||||F|||20101214110752\r" + 
			"OBX|95|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      (including gallbladder), lung, or other organs with metastasis to the upper abdomen||||||F|||20101214110752\r" + 
			"OBX|96|TX|4788369^CONSULTATION ADDENDUM REPORT^L01N||      (liver, gallbladder) cannot be excluded.||||||F|||20101214110752\r" + 
			"";
	
	final static String LAB49 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20101214111339||ORU^R01|Q199823654T198320726|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^80016||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|A|61^636^2^80016|||||||||||||||A\r" + 
			"OBR|1||0176452202^101AP|313046^SURGICAL PATHOLOGY REPORT^L01N|||20101214102300|||||||20101214102300|^^LN, Regional Resection(5)|1001745^Test, Physician - p-Test Physician||||SF-10-0020326||20101214111336||AP|F||1^^^20101214102300^^~^^^^^RT|\r" + 
			"OBX|1|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||*****Consultative Addendum Report*****||||||F|||20101214111336\r" + 
			"OBX|2|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|3|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Accession Number     SF-10-0020326||||||F|||20101214111336\r" + 
			"OBX|4|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 10:23 MST||||||F|||20101214111336\r" + 
			"OBX|5|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 10:23 MST||||||F|||20101214111336\r" + 
			"OBX|6|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214111336\r" + 
			"OBX|7|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|8|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|9|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Consultative Add Reason||||||F|||20101214111336\r" + 
			"OBX|10|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Addendum to report outside consult.||||||F|||20101214111336\r" + 
			"OBX|11|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|12|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|13|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Addendum Diagnosis||||||F|||20101214111336\r" + 
			"OBX|14|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Case sent to Mayo Clinic for consultation and they agree with original diagnosis.||||||F|||20101214111336\r" + 
			"OBX|15|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|16|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214111336\r" + 
			"OBX|17|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|18|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214111336\r" + 
			"OBX|19|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Verified: 2010/12/14 11:13 AM||||||F|||20101214111336\r" + 
			"OBX|20|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214111336\r" + 
			"OBX|21|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|22|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214111336\r" + 
			"OBX|23|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      ________||||||F|||20101214111336\r" + 
			"OBX|24|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|25|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|26|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Addendum Discussion||||||F|||20101214111336\r" + 
			"OBX|27|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Testing Netcare and POS addendums.||||||F|||20101214111336\r" + 
			"OBX|28|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|29|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|30|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      *****Surgical Pathology Report*****||||||F|||20101214111336\r" + 
			"OBX|31|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|32|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Accession Number     SF-10-0020326||||||F|||20101214111336\r" + 
			"OBX|33|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Collected Date/Time  2010-12-14 10:23 MST||||||F|||20101214111336\r" + 
			"OBX|34|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Received Date/Time   2010-12-14 10:23 MST||||||F|||20101214111336\r" + 
			"OBX|35|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Pathologist          Gates, Colleen||||||F|||20101214111336\r" + 
			"OBX|36|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|37|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|38|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Specimen Description||||||F|||20101214111336\r" + 
			"OBX|39|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      A.  Inferior pulmonary ligament node||||||F|||20101214111336\r" + 
			"OBX|40|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B.  Right lower lobe superior segment||||||F|||20101214111336\r" + 
			"OBX|41|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      C.  Right middle lobe wedge resection||||||F|||20101214111336\r" + 
			"OBX|42|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      D.  #10R lymph node||||||F|||20101214111336\r" + 
			"OBX|43|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      E.  Subcarinal #7 lymph node||||||F|||20101214111336\r" + 
			"OBX|44|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      F.  #12 right upper lobe node||||||F|||20101214111336\r" + 
			"OBX|45|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G.  Right upper lobe posterior segment *please note 2 nodules*||||||F|||20101214111336\r" + 
			"OBX|46|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|47|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|48|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Clinical Information||||||F|||20101214111336\r" + 
			"OBX|49|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Smoker, enlarging RUL nodule, incidental RML nodule - query metastatic lung Ca.||||||F|||20101214111336\r" + 
			"OBX|50|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|51|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214111336\r" + 
			"OBX|52|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      __||||||F|||20101214111336\r" + 
			"OBX|53|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|54|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|55|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|56|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|57|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|58|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|59|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|60|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Diagnosis||||||F|||20101214111336\r" + 
			"OBX|61|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      A.  Inferior Pulmonary Ligament Node:||||||F|||20101214111336\r" + 
			"OBX|62|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214111336\r" + 
			"OBX|63|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|64|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B.  Right Lower Lobe Superior Segment:||||||F|||20101214111336\r" + 
			"OBX|65|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Moderately differentiated adenocarcinoma (1.4 cm),with areas of||||||F|||20101214111336\r" + 
			"OBX|66|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  bronchiolo-alveolar carcinoma like growth pattern.||||||F|||20101214111336\r" + 
			"OBX|67|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Arises in a background of respiratory bronchiolitis, congestion and||||||F|||20101214111336\r" + 
			"OBX|68|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                   mild emphysematous change.||||||F|||20101214111336\r" + 
			"OBX|69|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Resection margins negative for malignancy and dysplasia||||||F|||20101214111336\r" + 
			"OBX|70|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Pleura free of tumor.||||||F|||20101214111336\r" + 
			"OBX|71|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|72|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      C.  Right Middle Lobe Wedge Resection:||||||F|||20101214111336\r" + 
			"OBX|73|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Hyalinized fibrous nodule with chronic inflammatory cells.||||||F|||20101214111336\r" + 
			"OBX|74|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Negative for Malignancy and Dysplasia.||||||F|||20101214111336\r" + 
			"OBX|75|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|76|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      D.  #10 R Lymph Node:||||||F|||20101214111336\r" + 
			"OBX|77|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214111336\r" + 
			"OBX|78|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|79|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      E.  Subcarinal #7 Lymph Node:||||||F|||20101214111336\r" + 
			"OBX|80|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214111336\r" + 
			"OBX|81|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|82|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      F.  #12 Right Upper Lobe Node:||||||F|||20101214111336\r" + 
			"OBX|83|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     Negative for malignancy.||||||F|||20101214111336\r" + 
			"OBX|84|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|85|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G.  Right Upper Lobe, Posterior Segment:||||||F|||20101214111336\r" + 
			"OBX|86|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            -     1)Moderately differentiated adenocarcinoma (1.7 cm) in a||||||F|||20101214111336\r" + 
			"OBX|87|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  background of respiratory bronchiolitis and emphysematous change||||||F|||20101214111336\r" + 
			"OBX|88|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|89|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Tumor is morphologically different than that in specimen B and thus||||||F|||20101214111336\r" + 
			"OBX|90|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  is reflective of a second primary site.||||||F|||20101214111336\r" + 
			"OBX|91|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|92|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  2)Moderately differentiated adenocarcinoma (2.0 cm),with areas of||||||F|||20101214111336\r" + 
			"OBX|93|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  bronchiolo-alveolar carcinoma like growth pattern.||||||F|||20101214111336\r" + 
			"OBX|94|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Arises in a background of respiratory bronchiolitis, congestion and||||||F|||20101214111336\r" + 
			"OBX|95|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                   mild emphysematous change.||||||F|||20101214111336\r" + 
			"OBX|96|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|97|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  Tumor is morphologically the same as that seen in specimen B but||||||F|||20101214111336\r" + 
			"OBX|98|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  different from the first tumor in specimen G. Thus reflective of||||||F|||20101214111336\r" + 
			"OBX|99|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  pulmonary metastases.||||||F|||20101214111336\r" + 
			"OBX|100|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|101|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  3) One microscopic area of Bronchioalveolar Carcinoma (0.6 cm)||||||F|||20101214111336\r" + 
			"OBX|102|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|103|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  4)  Atypical adenomatous hyperplasia (AAH)||||||F|||20101214111336\r" + 
			"OBX|104|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|105|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                  5) Resection margins negative for malignancy and dysplasia||||||F|||20101214111336\r" + 
			"OBX|106|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|107|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|108|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|109|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|110|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|111|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|112|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|113|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|114|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||                      Pleura free of tumor.||||||F|||20101214111336\r" + 
			"OBX|115|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|116|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Reported by: Gates, C.||||||F|||20101214111336\r" + 
			"OBX|117|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|118|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Electronically signed by: Gates, Colleen||||||F|||20101214111336\r" + 
			"OBX|119|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Verified: 2010/12/14 10:40 AM||||||F|||20101214111336\r" + 
			"OBX|120|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      CG/CG||||||F|||20101214111336\r" + 
			"OBX|121|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|122|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      _________________________________________________________________________________________||||||F|||20101214111336\r" + 
			"OBX|123|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      ________||||||F|||20101214111336\r" + 
			"OBX|124|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|125|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|126|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Synoptic Report||||||F|||20101214111336\r" + 
			"OBX|127|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G: CLS Lung Resection||||||F|||20101214111336\r" + 
			"OBX|128|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Specimen Type:||||||F|||20101214111336\r" + 
			"OBX|129|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Wedge resection||||||F|||20101214111336\r" + 
			"OBX|130|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Laterality:||||||F|||20101214111336\r" + 
			"OBX|131|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Right||||||F|||20101214111336\r" + 
			"OBX|132|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor Site:||||||F|||20101214111336\r" + 
			"OBX|133|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Upper lobe||||||F|||20101214111336\r" + 
			"OBX|134|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor type:||||||F|||20101214111336\r" + 
			"OBX|135|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Adenocarcinoma||||||F|||20101214111336\r" + 
			"OBX|136|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Grade:||||||F|||20101214111336\r" + 
			"OBX|137|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Moderately differentiated||||||F|||20101214111336\r" + 
			"OBX|138|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Location:||||||F|||20101214111336\r" + 
			"OBX|139|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Peripheral||||||F|||20101214111336\r" + 
			"OBX|140|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Satellite nodules in same lobe:||||||F|||20101214111336\r" + 
			"OBX|141|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Positive||||||F|||20101214111336\r" + 
			"OBX|142|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Intrapulmonary metastases:||||||F|||20101214111336\r" + 
			"OBX|143|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Positive||||||F|||20101214111336\r" + 
			"OBX|144|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Vascular invasion:||||||F|||20101214111336\r" + 
			"OBX|145|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|146|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Perineural invasion:||||||F|||20101214111336\r" + 
			"OBX|147|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|148|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Margins: - Bronchial:||||||F|||20101214111336\r" + 
			"OBX|149|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor 0.5 cm away from stapled resection margin||||||F|||20101214111336\r" + 
			"OBX|150|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Margins: - Medistinal:||||||F|||20101214111336\r" + 
			"OBX|151|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|152|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Pleural invasion:||||||F|||20101214111336\r" + 
			"OBX|153|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|154|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Atelectasis/Obstructive Pneumonia:||||||F|||20101214111336\r" + 
			"OBX|155|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|156|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Lymph nodes||||||F|||20101214111336\r" + 
			"OBX|157|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|158|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B: CLS Lung Resection||||||F|||20101214111336\r" + 
			"OBX|159|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Specimen Type:||||||F|||20101214111336\r" + 
			"OBX|160|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Wedge resection||||||F|||20101214111336\r" + 
			"OBX|161|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Laterality:||||||F|||20101214111336\r" + 
			"OBX|162|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Right||||||F|||20101214111336\r" + 
			"OBX|163|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor Site:||||||F|||20101214111336\r" + 
			"OBX|164|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Lower lobe||||||F|||20101214111336\r" + 
			"OBX|165|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor type:||||||F|||20101214111336\r" + 
			"OBX|166|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Adenocarcinoma||||||F|||20101214111336\r" + 
			"OBX|167|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Grade:||||||F|||20101214111336\r" + 
			"OBX|168|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Moderately differentiated||||||F|||20101214111336\r" + 
			"OBX|169|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|170|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|171|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|172|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|173|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|174|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|175|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Location:||||||F|||20101214111336\r" + 
			"OBX|176|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Peripheral||||||F|||20101214111336\r" + 
			"OBX|177|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Size:||||||F|||20101214111336\r" + 
			"OBX|178|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Maximum dimension  1.4  cm||||||F|||20101214111336\r" + 
			"OBX|179|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Satellite nodules in same lobe:||||||F|||20101214111336\r" + 
			"OBX|180|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|181|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Vascular invasion:||||||F|||20101214111336\r" + 
			"OBX|182|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|183|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Perineural invasion:||||||F|||20101214111336\r" + 
			"OBX|184|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|185|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Margins: - Bronchial:||||||F|||20101214111336\r" + 
			"OBX|186|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Tumor 1.5 cm away from stapled resection margin||||||F|||20101214111336\r" + 
			"OBX|187|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Pleural invasion:||||||F|||20101214111336\r" + 
			"OBX|188|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|189|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Atelectasis/Obstructive Pneumonia:||||||F|||20101214111336\r" + 
			"OBX|190|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|191|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      In situ component:||||||F|||20101214111336\r" + 
			"OBX|192|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|193|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Lymph nodes:||||||F|||20101214111336\r" + 
			"OBX|194|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Negative||||||F|||20101214111336\r" + 
			"OBX|195|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|196|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|197|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Gross Description||||||F|||20101214111336\r" + 
			"OBX|198|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Received are specimens A to G.  All requisitions and specimen containers are labelled||||||F|||20101214111336\r" + 
			"OBX|199|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      with the patient's name.  The cassettes and AP identifiers are labelled with the Surgical||||||F|||20101214111336\r" + 
			"OBX|200|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Number SF10-19864.||||||F|||20101214111336\r" + 
			"OBX|201|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|202|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      A.  Received is a single grey/tan irregular soft tissue nodule measuring 1.0 x 0.8 x 0.2||||||F|||20101214111336\r" + 
			"OBX|203|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      cm.  It is trisected and submitted in one block.  ARH/ct||||||F|||20101214111336\r" + 
			"OBX|204|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|205|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B.  The specimen consists of a lung wedge (10 x 6 x 2.5 cm) with a single yellow||||||F|||20101214111336\r" + 
			"OBX|206|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      spiculated nodule (1.0 cm) located 1.5 cm from the painted (black) staple margin.||||||F|||20101214111336\r" + 
			"OBX|207|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Representative section submitted for QS.  IC/ct||||||F|||20101214111336\r" + 
			"OBX|208|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|209|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Another stapled margin is identified opposite to the first.  The staples are cut and the||||||F|||20101214111336\r" + 
			"OBX|210|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      resection surface is painted blue.  The lung wedge is sectioned serially. The primary||||||F|||20101214111336\r" + 
			"OBX|211|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      tumor mass identified during the frozen section is seen.  No other tumor masses, areas of||||||F|||20101214111336\r" + 
			"OBX|212|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      consolidation or fibrosis are identified.  Sections are submitted as follows:||||||F|||20101214111336\r" + 
			"OBX|213|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|214|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B2-B3 sections of the primary tumor mass, submitted in toto||||||F|||20101214111336\r" + 
			"OBX|215|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B4-B6 representative sections of the lung parenchyma adjacent to the tumor mass||||||F|||20101214111336\r" + 
			"OBX|216|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B7-B10 random representative sections of the grossly normal-appearing lung parenchyma||||||F|||20101214111336\r" + 
			"OBX|217|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      ARH/ct||||||F|||20101214111336\r" + 
			"OBX|218|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|219|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      C.  The specimen consists of a lung wedge (3.5 x 2.5 x 0.5 cm) containing a single||||||F|||20101214111336\r" + 
			"OBX|220|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      subpleural nodule (0.1 cm and ? circled).  Staple resection margin painted black.  Nodule||||||F|||20101214111336\r" + 
			"OBX|221|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      submitted in toto for QS.  IC/ct||||||F|||20101214111336\r" + 
			"OBX|222|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|223|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      The remaining portion of the wedge was sectioned serially and appears to be grossly||||||F|||20101214111336\r" + 
			"OBX|224|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      normal.  Random representative sections will be submitted in blocks C2-C4.  ARH/ct||||||F|||20101214111336\r" + 
			"OBX|225|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|226|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      D.  Received is a single grey/tan irregular portion of soft tissue measuring 1.5 x 0.6 x||||||F|||20101214111336\r" + 
			"OBX|227|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      0.2 cm.  It is bisected longitudinally and submitted totally in one block.||||||F|||20101214111336\r" + 
			"OBX|228|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|229|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|230|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|231|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|232|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|233|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|234|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|235|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|236|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      E.  Received are two portions of irregular grey/tan tissue measuring in aggregate 1.2 x||||||F|||20101214111336\r" + 
			"OBX|237|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      0.8 x 0.3 cm.  Each of the portions are bisected longitudinally and submitted totally in||||||F|||20101214111336\r" + 
			"OBX|238|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      blocks E1 and E2.||||||F|||20101214111336\r" + 
			"OBX|239|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|240|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      F.  Received are multiple portions of grey/tan and irregular soft tissue measuring in||||||F|||20101214111336\r" + 
			"OBX|241|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      aggregate 1.5 x 1.3 x 0.2 cm.  The largest portion is bisected and submitted totally in||||||F|||20101214111336\r" + 
			"OBX|242|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      block F1.  The remaining portions are submitted totally in block F2.||||||F|||20101214111336\r" + 
			"OBX|243|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|244|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G.  Received is a wedge of lung tissue measuring 13.0 x 7.5 x 1.3 cm.  A stapled||||||F|||20101214111336\r" + 
			"OBX|245|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      resection margin is identified.  The stapled resection margin is painted blue.  The||||||F|||20101214111336\r" + 
			"OBX|246|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      pleural surface shows puckering, at a distance of 2.5 cm from the stapled margin.||||||F|||20101214111336\r" + 
			"OBX|247|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Underneath this puckered pleural surface, an irregular firm-to-hard nodule can be||||||F|||20101214111336\r" + 
			"OBX|248|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      palpated.  On serial sectioning of the lung wedge, a yellow/tan firm-to-hard irregular||||||F|||20101214111336\r" + 
			"OBX|249|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      nodule is identified coming within 0.2 cm of the pleural surface and 0.5 cm from the||||||F|||20101214111336\r" + 
			"OBX|250|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      stapled resection margin.  The mass itself measures 1.7 x 1.1 x 1.2 cm.  Another||||||F|||20101214111336\r" + 
			"OBX|251|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      irregular firm-to-hard yellow/tan mass is identified coming within 0.2 cm of the puckered||||||F|||20101214111336\r" + 
			"OBX|252|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      pleural surface.  This mass measures 2.0 x 1.1 x 1.1 cm.  This mass is located at a||||||F|||20101214111336\r" + 
			"OBX|253|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      distance of 1.7 cm from the first tumor mass (where the depth of the lung wedge is 4.5||||||F|||20101214111336\r" + 
			"OBX|254|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      cm).  The rest of the lung parenchyma appears to be grossly normal with no areas of||||||F|||20101214111336\r" + 
			"OBX|255|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      fibrosis or consolidation identified.  Sections are submitted as follows:||||||F|||20101214111336\r" + 
			"OBX|256|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|257|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G1-G3 sections of the primary tumor mass, submitted in toto||||||F|||20101214111336\r" + 
			"OBX|258|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G4 sections of the primary and secondary tumor mass to show the relationship between the||||||F|||20101214111336\r" + 
			"OBX|259|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      two (contiguous sections)||||||F|||20101214111336\r" + 
			"OBX|260|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G5-G7 representative sections of the secondary tumor mass to show its relationship with||||||F|||20101214111336\r" + 
			"OBX|261|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      puckered pleural surface||||||F|||20101214111336\r" + 
			"OBX|262|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      G8-G12 random representative sections of the grossly normal-appearing lung parenchyma||||||F|||20101214111336\r" + 
			"OBX|263|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      ARH/ct||||||F|||20101214111336\r" + 
			"OBX|264|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|265|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|266|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Frozen Section Diagnosis||||||F|||20101214111336\r" + 
			"OBX|267|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      B.  Right Lower Lobe Superior Segment:||||||F|||20101214111336\r" + 
			"OBX|268|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            - Well-differentiated adenocarcinoma with BAC features  /IC  Conf #65||||||F|||20101214111336\r" + 
			"OBX|269|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|270|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      C.  Right Middle Lobe Wedge:||||||F|||20101214111336\r" + 
			"OBX|271|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||            - Benign fibrous nodule, negative for malignancy  IC/ct  Conf #66||||||F|||20101214111336\r" + 
			"OBX|272|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|273|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Reported by: V V Falck||||||F|||20101214111336\r" + 
			"OBX|274|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|275|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||||||||F|||20101214111336\r" + 
			"OBX|276|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Pathologist Comment||||||F|||20101214111336\r" + 
			"OBX|277|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      Sections show insitu and invasive adenocarcinomas. One of the adenocarcinomas is distinct||||||F|||20101214111336\r" + 
			"OBX|278|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      from the rest and thus indicative of multiple primary tumors, however the remaining||||||F|||20101214111336\r" + 
			"OBX|279|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      tumors in the two separate lobes are histologically similar and consistent with pulmonary||||||F|||20101214111336\r" + 
			"OBX|280|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      metastases. As the lower lobe tumor is smaller than the upper lobe tumor and insitu||||||F|||20101214111336\r" + 
			"OBX|281|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      disease is noted in the upper lobe, the upper lobe would be regarded as the primary tumor||||||F|||20101214111336\r" + 
			"OBX|282|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      in this case. This means two separate primary tumors are present in the upper lobe with||||||F|||20101214111336\r" + 
			"OBX|283|TX|5925225^CONSULTATIVE ADDENDUM REPORT^L01N||      upper lobe and lower lobe metastases from one of the two primary tumors.||||||F|||20101214111336\r" + 
			"";
	
	static final String LAB50 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110105150337||ORU^R01|Q199836608T198333711|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439032^101LA|2921248^SODIUM^L01N|||20101203111100|||||||20101203111100|^^|1001745^Test, Physician - p-Test Physician||||10-337-300034||20101203114337||LA|F||1^^^20101203111100^^RT~^^^^^RT|\r" + 
			"NTE|1|C|patient is feeling blue today\r" + 
			"NTE|2|C|But it is friday\r" + 
			"OBX|1|NM|4673500^SODIUM^L01N||135|mmol/L|133-145||||C|||20101203114908\r" + 
			"NTE|1||Corrected from 140 mmol/L on 12/03/10 11:49:08 MST by CDREWS02.\r" + 
			"";
	
	static final String LAB51 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110105150927||ORU^R01|Q199836610T198333713|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176439032^101LA|2921248^SODIUM^L01N|||20101203111100|||||||20101203111100|^^|1001745^Test, Physician - p-Test Physician||||10-337-300034||20101203114337||LA|F||1^^^20101203111100^^RT~^^^^^RT|\r" + 
			"NTE|1|C|patient is feeling blue today\r" + 
			"NTE|2|C|But it is friday\r" + 
			"OBX|1|NM|4673500^SODIUM^L01N||135|mmol/L|133-145||||C|||20110105150922\r" + 
			"NTE|1|C|patient is red today\r" + 
			"NTE|2||Corrected from 135 mmol/L on 01/05/11 15:09:22 MST by DATKINSON03.\r" + 
			"NTE|3||Corrected from 140 mmol/L on 12/03/10 11:49:08 MST by CDREWS02.\r" + 
			"";
	
	// Chart 17
	static final String LAB52 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110107124858||ORU^R01|Q199837792T198334885|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176479899^101LA|4387636^LIPID PANEL^L01N|||20110107124300|||||||20110107124400|^^|1001745^Test, Physician - p-Test Physician||||11-007-300003||20110107124855||LA|F||1^^^20110107124300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4673389^CHOLESTEROL^L01N||5.00|mmol/L|3.80-5.20||||F|||20110107124852\r" + 
			"OBX|2|NM|4673578^TRIGLYCERIDES^L01N||5.00|mmol/L|0.60-2.30|H|||F|||20110107124852\r" + 
			"OBX|3|NM|4673719^HDL CHOLESTEROL^L01N||1.00|mmol/L|>=0.91||||F|||20110107124852\r" + 
			"OBX|4|NM|4671640^TOTAL: HDL CHOLESTEROL RATIO^L01N||1.0||||||F|||20110107124852\r" + 
			"NTE|1|I|10-YR Risk Of CAD (Death or Non-fatal MI) and Target Lipid Values\r" + 
			"NTE|2|I|RISK LEVEL                                                   TARGETS: LDL-C\r" + 
			"NTE|3|I|TC/HDL-C\r" + 
			"NTE|4|I|High (>= 20%): or  Diabetes, or CAD/PAD/                       < 2.0    and\r" + 
			"NTE|5|I|< 4.0\r" + 
			"NTE|6|I|     CVD/CRF\r" + 
			"NTE|7|I|Moderate  (10-19%)\r" + 
			"NTE|8|I|< 3.5    or         < 5.0\r" + 
			"NTE|9|I|Low     (< 10%)\r" + 
			"NTE|10|I|< 5.0    or         < 6.0\r" + 
			"NTE|11|I|GUIDELINE: CAN J CARDIOL 2006;  22:  913-927\r" + 
			"OBX|5|NM|4671331^LDL, CALCULATED^L01N||5.00|mmol/L|2.00-3.40|H|||F|||20110107124852\r" + 
			"OBX|6|ST|6188004^HOURS  FASTING^L01N||10 hours||||||F|||20110107124852\r" + 
			"";
	
	static final String LAB53 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110107125544||ORU^R01|Q199837806T198334898|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176479912^101LA|2921814^TROPONIN T^L01N|||20110107124300|||||||20110107124400|^^|1001745^Test, Physician - p-Test Physician||||11-007-300003||20110107125543||LA|F||1^^^20110107124300^^RT~^^^^^RT|\r" + 
			"OBX|1|NM|4674004^TROPONIN T^L01N||10.00|ug/L|0.00-0.02|C|||F|||20110107125540\r" + 
			"";
	
	static final String LAB54 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110107143252||ORU^R01|Q199838016T198335107|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176480449^101LA|5879562^TROPONIN I (VIDAS)^L01N|||20110107142300|||||||20110107143100|^^|1001745^Test, Physician - p-Test Physician||||11-007-300015||20110107143252||LA|F||1^^^20110107142300^^ST~^^^^^ST|\r" + 
			"OBX|1|NM|4674001^TROPONIN I (VIDAS)^L01N||0.06|ug/L|<=0.01|H|||F|||20110107143248\r" + 
			"NTE|1|C|Troponin I value is inconclusive for acute MI and may be due to myocardial\r" + 
			"NTE|2|C|injury.  Repeat ordering may be warranted in some clinical situations.\r" + 
			"";
	
	static final String LAB55 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110112095002||ORU^R01|Q199839856T198336943|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176485077^101LA|4655308^GLUCOSE TOLERANCE PREG^L01N|||20110112070000|||||||20110112094900|^^|1001745^Test, Physician - p-Test Physician||||11-012-300002||20110112094957||LA|F||1^^^20110112070000^^TM~^^^20110112070000^^RT|\r" + 
			"OBX|1|NM|6188134^GLUCOSE TOLERANCE FASTING.^L01N||4.8|mmol/L|||||F|||20110112094955\r" + 
			"NTE|1|I|Interpretive criteria:\r" + 
			"NTE|2|I|Fasting     1 Hr      2 Hr\r" + 
			"NTE|3|I|5.3            10.6      8.9 mmol/L\r" + 
			"NTE|4|I|If one of three values is met or exceeded, the diagnosis is impaired glucose tolerance of pregnancy.  If two or three values are met or exceeded, the diagnosis of gestational diabetes mellitus has been established. Please see Canadian Diabetes Association 2008 practice guidelines.\r" + 
			"OBX|2|NM|6187984^GLUCOSE DOSAGE.(G)^L01N||100|g|||||F|||20110112094955\r" + 
			"OBX|3|TX|6187989^HOURS FASTING.^L01N||10 hours||||||F|||20110112094955\r" + 
			"OBX|4|NM|6388149^WEEKS GESTATION.^L01N||28|week(s)|||||F|||20110112094955\r" + 
			"";
	
	static final String LAB56 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110112095019||ORU^R01|Q199839858T198336945|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176485077^101LA|4655308^GLUCOSE TOLERANCE PREG^L01N|||20110112080000|||||||20110112094900|^^|1001745^Test, Physician - p-Test Physician||||11-012-300003||20110112095016||LA|F||1^^^20110112080000^^TM~^^^20110112070000^^RT|\r" + 
			"OBX|1|NM|6188134^GLUCOSE TOLERANCE FASTING.^L01N||4.8|mmol/L|||||F|||20110112094955\r" + 
			"NTE|1|I|Interpretive criteria:\r" + 
			"NTE|2|I|Fasting     1 Hr      2 Hr\r" + 
			"NTE|3|I|5.3            10.6      8.9 mmol/L\r" + 
			"NTE|4|I|If one of three values is met or exceeded, the diagnosis is impaired glucose tolerance of pregnancy.  If two or three values are met or exceeded, the diagnosis of gestational diabetes mellitus has been established. Please see Canadian Diabetes Association 2008 practice guidelines.\r" + 
			"OBX|2|NM|6187984^GLUCOSE DOSAGE.(G)^L01N||100|g|||||F|||20110112094955\r" + 
			"OBX|3|TX|6187989^HOURS FASTING.^L01N||10 hours||||||F|||20110112094955\r" + 
			"OBX|4|NM|6388149^WEEKS GESTATION.^L01N||28|week(s)|||||F|||20110112094955\r" + 
			"OBX|5|NM|4673668^GLUCOSE TOLERANCE 1.0H.^L01N||5.2|mmol/L|||||F|||20110112095015\r" + 
			"";
	
	static final String LAB57 = "MSH|^~\\&|OPEN ENGINE|CLS|Egate|POSP|20110112095039||ORU^R01|Q199839860T198336947|P|2.3\r" + 
			"PID|1|798274114^^^AB|2250008675^^^88000||MillMCK CB FSI, Karla||19701027|F||||83||\r" + 
			"PV1|1|E|05031^^^88000|||||||||||||||E\r" + 
			"OBR|1||0176485077^101LA|4655308^GLUCOSE TOLERANCE PREG^L01N|||20110112090000|||||||20110112094900|^^|1001745^Test, Physician - p-Test Physician||||11-012-300004||20110112095036||LA|F||1^^^20110112090000^^TM~^^^20110112070000^^RT|\r" + 
			"OBX|1|NM|6188134^GLUCOSE TOLERANCE FASTING.^L01N||4.8|mmol/L|||||F|||20110112094955\r" + 
			"NTE|1|I|Interpretive criteria:\r" + 
			"NTE|2|I|Fasting     1 Hr      2 Hr\r" + 
			"NTE|3|I|5.3            10.6      8.9 mmol/L\r" + 
			"NTE|4|I|If one of three values is met or exceeded, the diagnosis is impaired glucose tolerance of pregnancy.  If two or three values are met or exceeded, the diagnosis of gestational diabetes mellitus has been established. Please see Canadian Diabetes Association 2008 practice guidelines.\r" + 
			"OBX|2|NM|6187984^GLUCOSE DOSAGE.(G)^L01N||100|g|||||F|||20110112094955\r" + 
			"OBX|3|TX|6187989^HOURS FASTING.^L01N||10 hours||||||F|||20110112094955\r" + 
			"OBX|4|NM|6388149^WEEKS GESTATION.^L01N||28|week(s)|||||F|||20110112094955\r" + 
			"OBX|5|NM|4673668^GLUCOSE TOLERANCE 1.0H.^L01N||5.2|mmol/L|||||F|||20110112095015\r" + 
			"OBX|6|NM|4676143^GLUCOSE TOLERANCE 2.0H.^L01N||5.9|mmol/L|||||F|||20110112095035";
	
	static final int LAB_COUNT = 57;
	
	public static String[] ALL_LABS = new String[LAB_COUNT];
	
	public static String[] LAB_NAMES = new String[LAB_COUNT];
	
	static {
		for(int i = 1; i <= LAB_COUNT; i++) {
			String fieldName = String.format("LAB%02d", i);
			String fieldValue;
            try {
	            fieldValue = (String) TestLabs.class.getDeclaredField(fieldName).get(null);
            } catch (Exception e) {
            	throw new RuntimeException("Unable to initialize test", e);
            }
			
			ALL_LABS[i - 1] = fieldValue;
			LAB_NAMES[i - 1] = fieldName;
		}
		
	}
	
//	
//	static final String LAB0 = "";	
//	static final String LAB1 = "";
//	static final String LAB2 = "";
//	static final String LAB3 = "";
//	static final String LAB4 = "";
//	static final String LAB5 = "";
//	static final String LAB6 = "";
//	static final String LAB7 = "";
//	static final String LAB8 = "";
//	static final String LAB9 = "";
//	
}
