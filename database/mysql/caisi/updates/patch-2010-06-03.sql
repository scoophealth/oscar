update OcanFormOption set ocanDataCategory="Reason for OCAN" where id=635;
update OcanFormOption set ocanDataCategoryName="Initial OCAN" where id=635;
update OcanFormOption set ocanDataCategory="Reason for OCAN" where id=636;
update OcanFormOption set ocanDataCategoryName="Reassessment" where id=636;
update OcanFormOption set ocanDataCategory="Reason for OCAN" where id=637;
update OcanFormOption set ocanDataCategory="Reason for OCAN" where id=638;
update OcanFormOption set ocanDataCategoryName="Other (e.g. consumer request)" where id=638;


INSERT INTO OcanFormOption VALUES (1000,'1.2','Reason for OCAN','SC','Significant Change');
INSERT INTO OcanFormOption VALUES (1001,'1.2','Reason for OCAN','REV','Review');
INSERT INTO OcanFormOption VALUES (1002,'1.2','Reason for OCAN','REK','Re-key');

INSERT INTO OcanFormOption VALUES (1003,'1.2','OCAN Lead Assessment','TRUE','Yes');
INSERT INTO OcanFormOption VALUES (1004,'1.2','OCAN Lead Assessment','FALSE','No');

INSERT INTO OcanFormOption VALUES (1005,'1.2','Consumer Self-Assessment completed','TRUE','Yes');
INSERT INTO OcanFormOption VALUES (1006,'1.2','Consumer Self-Assessment completed','FALSE','No');

INSERT INTO OcanFormOption VALUES (1007,'1.2','Consumer Self-Assessment incompleted','CMFLVL','Comfort Level');
INSERT INTO OcanFormOption VALUES (1008,'1.2','Consumer Self-Assessment incompleted','LOA','Length of Assessment');
INSERT INTO OcanFormOption VALUES (1009,'1.2','Consumer Self-Assessment incompleted','LIT','Literacy');
INSERT INTO OcanFormOption VALUES (1010,'1.2','Consumer Self-Assessment incompleted','MHC','Mental Health Condition');
INSERT INTO OcanFormOption VALUES (1011,'1.2','Consumer Self-Assessment incompleted','PYSCON','Physical Condition');
INSERT INTO OcanFormOption VALUES (1012,'1.2','Consumer Self-Assessment incompleted','LANG','Language Barrier');
INSERT INTO OcanFormOption VALUES (1013,'1.2','Consumer Self-Assessment incompleted','OTH','Other');

INSERT INTO OcanFormOption VALUES (1014,'1.2','client DOB Type','EST','Estimate');
INSERT INTO OcanFormOption VALUES (1015,'1.2','client DOB Type','UNK','Unknown');

INSERT INTO OcanFormOption VALUES (1016,'1.2','LHIN code','010-30','Out Of Province');
INSERT INTO OcanFormOption VALUES (1017,'1.2','LHIN code','010-52','Out of Country');
INSERT INTO OcanFormOption VALUES (1018,'1.2','LHIN code','UNK','Unknown');

update OcanFormOption set ocanDataCategoryName="Consumer Declined to answer" WHERE id=358;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to answer" WHERE id=536;

INSERT INTO OcanFormOption VALUES (1019,'1.2','MIS Functional Centre List','725 107 699','OTH - Other MH services not elsewhere classified');

update OcanFormOption set ocanDataCategory="OTH" where id=711;
INSERT INTO OcanFormOption VALUES (1020,'1.2','Referral Source','0AS','Abuse Services');
INSERT INTO OcanFormOption VALUES (1021,'1.2','Referral Source','0AB','Alternative Businesses');
INSERT INTO OcanFormOption VALUES (1022,'1.2','Referral Source','ACT','Assertive Community Treatment Teams');
INSERT INTO OcanFormOption VALUES (1023,'1.2','Referral Source','0CM','Case Management');
INSERT INTO OcanFormOption VALUES (1024,'1.2','Referral Source','CHI','Child/Adolescent');
INSERT INTO OcanFormOption VALUES (1025,'1.2','Referral Source','CLU','Clubhouses');
INSERT INTO OcanFormOption VALUES (1026,'1.2','Referral Source','0CD','Community Development');
INSERT INTO OcanFormOption VALUES (1027,'1.2','Referral Source','CMH ','Community Mental Health Clinic');
INSERT INTO OcanFormOption VALUES (1028,'1.2','Referral Source','0IR ','Community Service Information and Referral');
INSERT INTO OcanFormOption VALUES (1029,'1.2','Referral Source','CON','Concurrent Disorders');
INSERT INTO OcanFormOption VALUES (1030,'1.2','Referral Source','0CT','Counseling & Treatment');
INSERT INTO OcanFormOption VALUES (1031,'1.2','Referral Source','DCS','Diversion & Court Support');
INSERT INTO OcanFormOption VALUES (1032,'1.2','Referral Source','DDx ','Dual Diagnosis');
INSERT INTO OcanFormOption VALUES (1033,'1.2','Referral Source','EAR ','Early Intervention');
INSERT INTO OcanFormOption VALUES (1034,'1.2','Referral Source','EAT','Eating Disorder');
INSERT INTO OcanFormOption VALUES (1035,'1.2','Referral Source','0FI ','Family Initiatives');
INSERT INTO OcanFormOption VALUES (1036,'1.2','Referral Source','FOR','Forensic');
INSERT INTO OcanFormOption VALUES (1037,'1.2','Referral Source','HPA ','Health Promotion/Education - Awareness');
INSERT INTO OcanFormOption VALUES (1038,'1.2','Referral Source','HPW','Health Promotion/Education - Women\'s Health \(MH\)');
INSERT INTO OcanFormOption VALUES (1039,'1.2','Referral Source','HSC','Homes for Special Care');
INSERT INTO OcanFormOption VALUES (1040,'1.2','Referral Source','CRI ','Mental Health Crisis Intervention');
INSERT INTO OcanFormOption VALUES (1041,'1.2','Referral Source','PSH','Peer/Self-help Initiatives');
INSERT INTO OcanFormOption VALUES (1042,'1.2','Referral Source','0DN','Primary Day/Night Care');
INSERT INTO OcanFormOption VALUES (1043,'1.2','Referral Source','GER','Psycho-Geriatric');
INSERT INTO OcanFormOption VALUES (1044,'1.2','Referral Source','0SR','Social Rehabilitation/Recreation');
INSERT INTO OcanFormOption VALUES (1045,'1.2','Referral Source','0SH','Supports within Housing');
INSERT INTO OcanFormOption VALUES (1046,'1.2','Referral Source','EMP','Vocational/Employment');
INSERT INTO OcanFormOption VALUES (1047,'1.2','Referral Source','OMHS','Other Mental Health Services');
INSERT INTO OcanFormOption VALUES (1048,'1.2','Referral Source','OAS','Other Addiction Services');
INSERT INTO OcanFormOption VALUES (1049,'1.2','Referral Source','PSOR','Police');
INSERT INTO OcanFormOption VALUES (1050,'1.2','Referral Source','COLA','Courts (includes jails and detention centres)');
INSERT INTO OcanFormOption VALUES (1051,'1.2','Referral Source','CSOR','Correctional Facilities (includes jails and detention centres)');
INSERT INTO OcanFormOption VALUES (1052,'1.2','Referral Source','4155.1.PPOF','Probation/Parole Officers');
INSERT INTO OcanFormOption VALUES (1053,'1.2','Referral Source','CSB','Short Term Residential Crisis Support Beds');
INSERT INTO OcanFormOption VALUES (1054,'1.2','Referral Source','CJSS','Criminal Justice System Source breakdown not available (use this category if above detailed breakdown is not available)');


INSERT INTO OcanFormOption VALUES (1055,'1.2','Yes No','TRUE','Yes');
INSERT INTO OcanFormOption VALUES (1056,'1.2','Yes No','FALSE','No');

INSERT INTO OcanFormOption VALUES (1063,'1.2','Doctor Psychiatrist','CDA','Consumer declined to answer');
INSERT INTO OcanFormOption VALUES (1064,'1.2','Doctor Psychiatrist','UNK','Unknown');

INSERT INTO OcanFormOption VALUES (1065,'1.2','Other Contacts Agency','TRUE','Yes');
INSERT INTO OcanFormOption VALUES (1066,'1.2','Other Contacts Agency','FALSE','No');
INSERT INTO OcanFormOption VALUES (1067,'1.2','Other Contacts Agency','CDA','Consumer declined to answer');
INSERT INTO OcanFormOption VALUES (1068,'1.2','Other Contacts Agency','UNK','Unknown');

update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=133;

INSERT INTO OcanFormOption VALUES (1069,'1.2','Areas Of Concern','TRUE','Yes');
INSERT INTO OcanFormOption VALUES (1070,'1.2','Areas Of Concern','FALSE','No');
INSERT INTO OcanFormOption VALUES (1071,'1.2','Areas Of Concern','UNK','Unknown');

INSERT INTO OcanFormOption VALUES (1073,'1.2','Age of Onset','EST','Estimate');
INSERT INTO OcanFormOption VALUES (1074,'1.2','Age of Onset','CDA','Consumer declined to answer');
INSERT INTO OcanFormOption VALUES (1075,'1.2','Age of Onset','UNK','Unknown');
INSERT INTO OcanFormOption VALUES (1076,'1.2','Age of Onset','NA','N/A');

INSERT INTO OcanFormOption VALUES (1077,'1.2','Age of Entry To Org','EST','Estimate');
INSERT INTO OcanFormOption VALUES (1078,'1.2','Age of Entry To Org','CDA','Consumer declined to answer');
INSERT INTO OcanFormOption VALUES (1079,'1.2','Age of Entry To Org','UNK','Unknown');
INSERT INTO OcanFormOption VALUES (1080,'1.2','Age of Entry To Org','NA','N/A');

INSERT INTO OcanFormOption VALUES (1081,'1.2','Ethniticity','CDA','Consumer Declined to Answer');
INSERT INTO OcanFormOption VALUES (1082,'1.2','Ethniticity','UNK','Unknown');

update OcanFormOption set ocanDataCategoryValue="UNK" where id=63;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=64;
update OcanFormOption set ocanDataCategoryValue="CDA" where id=64;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=129;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=851;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=182;

INSERT INTO OcanFormOption VALUES (1083,'1.2','Language','CDA','Consumer Declined to Answer');
INSERT INTO OcanFormOption VALUES (1084,'1.2','Language','UNK','Unknown');

update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=505;

# 
update OcanFormOption set ocanDataCategoryValue="UNK" where id=499;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=500;
update OcanFormOption set ocanDataCategoryValue="CDA" where id=500;

update OcanFormOption set ocanDataCategoryValue="OTH" where id=740;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=741;
update OcanFormOption set ocanDataCategoryValue="CDA" where id=742;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=742;

update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=528;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=224;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=223;

update OcanFormOption set ocanDataCategoryValue="OTH" where id=212;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=213;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=214;

INSERT INTO OcanFormOption VALUES (1085,'1.2','Barriers','ADD','Addictions');
INSERT INTO OcanFormOption VALUES (1086,'1.2','Barriers','CAB','Cognitive Abilities');
INSERT INTO OcanFormOption VALUES (1087,'1.2','Barriers','CON','Confidence');
INSERT INTO OcanFormOption VALUES (1088,'1.2','Barriers','CTM','Contemplative');
INSERT INTO OcanFormOption VALUES (1089,'1.2','Barriers','DIS','Disclosure');
INSERT INTO OcanFormOption VALUES (1090,'1.2','Barriers','FOC','Financial ODSP cut off');
INSERT INTO OcanFormOption VALUES (1091,'1.2','Barriers','FFT','Funding for Training');
INSERT INTO OcanFormOption VALUES (1092,'1.2','Barriers','LOR','Lack of Resume');
INSERT INTO OcanFormOption VALUES (1093,'1.2','Barriers','LGC','Language Comprehension');
INSERT INTO OcanFormOption VALUES (1094,'1.2','Barriers','LIT','Literacy');
INSERT INTO OcanFormOption VALUES (1095,'1.2','Barriers','MSE','Medication Side Effects');
INSERT INTO OcanFormOption VALUES (1096,'1.2','Barriers','PHY','Physical Health');
INSERT INTO OcanFormOption VALUES (1097,'1.2','Barriers','PCO','Pre-contemplative');
INSERT INTO OcanFormOption VALUES (1098,'1.2','Barriers','STG','Stigma');
INSERT INTO OcanFormOption VALUES (1099,'1.2','Barriers','SYM','Symptoms');
INSERT INTO OcanFormOption VALUES (1100,'1.2','Barriers','TRN','Transportation');
INSERT INTO OcanFormOption VALUES (1101,'1.2','Barriers','OTH','Other');
INSERT INTO OcanFormOption VALUES (1102,'1.2','Barriers','CDA','Consumer declined to answer');

delete from OcanFormOption where ocanDataCategory="Unemployed Education Risk";

INSERT INTO OcanFormOption VALUES (1103,'1.2','Medical Conditions','26929004','Alzheimer\'s');
INSERT INTO OcanFormOption VALUES (1104,'1.2','Medical Conditions','424460009','Hepatitis D');
INSERT INTO OcanFormOption VALUES (1105,'1.2','Medical Conditions','44186003','Sleep Problems (e.g. Insomnia)');
update OcanFormOption set ocanDataCategoryName="Communicable disease" where id=543;
update OcanFormOption set ocanDataCategoryName="Sexually Transmitted Infection (STI)" where id=565;
delete from OcanFormOption where id=560;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=572;
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where id=898;

delete from OcanFormOption where ocanDataCategory="Side Effects Reported Ability";
delete from OcanFormOption where ocanDataCategory="Side Effects Description List";

INSERT INTO OcanFormOption VALUES (1106,'1.2','Source of Information','116154003','Consumer');
INSERT INTO OcanFormOption VALUES (1107,'1.2','Source of Information','HLTHP','Health Provider');
INSERT INTO OcanFormOption VALUES (1108,'1.2','Source of Information','42665001','Homes of the Aged');
INSERT INTO OcanFormOption VALUES (1109,'1.2','Source of Information','22232009','Hospital (at discharge or hospital record)');
INSERT INTO OcanFormOption VALUES (1110,'1.2','Source of Information','JUST','Justice (e.g. Probation Order)');
INSERT INTO OcanFormOption VALUES (1111,'1.2','Source of Information','LTCH','Long Term Care Home');
INSERT INTO OcanFormOption VALUES (1112,'1.2','Source of Information','264372000','Pharmacy');
INSERT INTO OcanFormOption VALUES (1113,'1.2','Source of Information','309343006','Physician');
INSERT INTO OcanFormOption VALUES (1114,'1.2','Source of Information','42120006','Significant Other');

INSERT INTO OcanFormOption VALUES (1115,'1.2','Emergency Department','None','None');
INSERT INTO OcanFormOption VALUES (1116,'1.2','Emergency Department','ED-1','1');
INSERT INTO OcanFormOption VALUES (1117,'1.2','Emergency Department','ED-2','2-5');
INSERT INTO OcanFormOption VALUES (1118,'1.2','Emergency Department','ED-6','6+');
INSERT INTO OcanFormOption VALUES (1119,'1.2','Emergency Department','CDA','Consumer Declined to Answer');
INSERT INTO OcanFormOption VALUES (1120,'1.2','Emergency Department','UNK','Unknown');

INSERT INTO OcanFormOption VALUES (1121,'1.2','Symptoms Checklist','24199005','Agitation');
INSERT INTO OcanFormOption VALUES (1122,'1.2','Symptoms Checklist','20602000','Apathy');
INSERT INTO OcanFormOption VALUES (1123,'1.2','Symptoms Checklist','424100000','Difficulty in Abstract Thinking');
INSERT INTO OcanFormOption VALUES (1124,'1.2','Symptoms Checklist','247640008','Disorganized Thinking');
INSERT INTO OcanFormOption VALUES (1125,'1.2','Symptoms Checklist','225652009','Emotional Unresponsiveness');
INSERT INTO OcanFormOption VALUES (1126,'1.2','Symptoms Checklist','79351003','Hostility');
INSERT INTO OcanFormOption VALUES (1127,'1.2','Symptoms Checklist','276306002','Lack of Drive or Initiative');
INSERT INTO OcanFormOption VALUES (1128,'1.2','Symptoms Checklist','247905005','Lack of Spontaneity');
INSERT INTO OcanFormOption VALUES (1129,'1.2','Symptoms Checklist','43994002','Physical Symptoms ');
INSERT INTO OcanFormOption VALUES (1130,'1.2','Symptoms Checklist','422517004','Poor Communication Skills');
INSERT INTO OcanFormOption VALUES (1131,'1.2','Symptoms Checklist','105411000','Social Withdrawal');
INSERT INTO OcanFormOption VALUES (1132,'1.2','Symptoms Checklist','78205008','Stereotype Thinking');
INSERT INTO OcanFormOption VALUES (1133,'1.2','Symptoms Checklist','22927000','Suspiciousness');


INSERT INTO OcanFormOption VALUES (1134,'1.2','Diagnostic Categories','228156007','Intellectual Disability or Impairment');
update OcanFormOption set ocanDataCategoryValue="SRD" where id=165;
update OcanFormOption set ocanDataCategoryValue="0DH" where id=166;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=167;
update OcanFormOption set ocanDataCategoryName="Physical disabilities" where id=171;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=747;
update OcanFormOption set ocanDataCategoryName="once a week" where id=350;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=194;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=119;
update OcanFormOption set ocanDataCategoryValue="224294005" where id=195;
update OcanFormOption set ocanDataCategoryValue="224295006" where id=197;
update OcanFormOption set ocanDataCategoryValue="224297003" where id=199;
update OcanFormOption set ocanDataCategoryValue="224302000" where id=201;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=202;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=374;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=375;
update OcanFormOption set ocanDataCategoryValue="CDA" where id=376;
INSERT INTO OcanFormOption VALUES (1135,'1.2','Presenting Issues','TTS','Threat to Self');
update OcanFormOption set ocanDataCategoryValue="TTO" where id=610;
INSERT INTO OcanFormOption VALUES (1136,'1.2','Presenting Issues','ATMPS','Attempted Suicide');
update OcanFormOption set ocanDataCategoryValue="SEXAB" where id=612;	
update OcanFormOption set ocanDataCategoryName="Sexual Abuse" where id=612;
INSERT INTO OcanFormOption VALUES (1137,'1.2','Presenting Issues','PHYSAB','Physical Abuse');
update OcanFormOption set ocanDataCategoryValue="PWSA" where id=619;
update OcanFormOption set ocanDataCategoryName="Problems with Substance Abuse" where id=619;
INSERT INTO OcanFormOption VALUES (1138,'1.2','Presenting Issues','PWA','Problems with Addictions');
update OcanFormOption set ocanDataCategoryValue="OTH" where id=621;

INSERT INTO OcanFormOption VALUES (1139,'1.2','Year of First Entry Date','2000','2000');
INSERT INTO OcanFormOption VALUES (1140,'1.2','Year of First Entry Date','2001','2001');
INSERT INTO OcanFormOption VALUES (1141,'1.2','Year of First Entry Date','2002','2002');
INSERT INTO OcanFormOption VALUES (1142,'1.2','Year of First Entry Date','2003','2003');
INSERT INTO OcanFormOption VALUES (1143,'1.2','Year of First Entry Date','2004','2004');
INSERT INTO OcanFormOption VALUES (1144,'1.2','Year of First Entry Date','2005','2005');
INSERT INTO OcanFormOption VALUES (1145,'1.2','Year of First Entry Date','2006','2006');
INSERT INTO OcanFormOption VALUES (1146,'1.2','Year of First Entry Date','2007','2007');
INSERT INTO OcanFormOption VALUES (1147,'1.2','Year of First Entry Date','2008','2008');
INSERT INTO OcanFormOption VALUES (1148,'1.2','Year of First Entry Date','2009','2009');
INSERT INTO OcanFormOption VALUES (1149,'1.2','Year of First Entry Date','2010','2010');
INSERT INTO OcanFormOption VALUES (1150,'1.2','Year of First Entry Date','2011','2011');
INSERT INTO OcanFormOption VALUES (1151,'1.2','Year of First Entry Date','2012','2012');
INSERT INTO OcanFormOption VALUES (1152,'1.2','Year of First Entry Date','2013','2013');
INSERT INTO OcanFormOption VALUES (1153,'1.2','Year of First Entry Date','2014','2014');
INSERT INTO OcanFormOption VALUES (1154,'1.2','Year of First Entry Date','2015','2015');
INSERT INTO OcanFormOption VALUES (1155,'1.2','Year of First Entry Date','2016','2016');
INSERT INTO OcanFormOption VALUES (1156,'1.2','Year of First Entry Date','2017','2017');
INSERT INTO OcanFormOption VALUES (1157,'1.2','Year of First Entry Date','2018','2018');
INSERT INTO OcanFormOption VALUES (1158,'1.2','Year of First Entry Date','2019','2019');
INSERT INTO OcanFormOption VALUES (1159,'1.2','Year of First Entry Date','2020','2020');
INSERT INTO OcanFormOption VALUES (1160,'1.2','Year of First Entry Date','2021','2021');
INSERT INTO OcanFormOption VALUES (1161,'1.2','Year of First Entry Date','2022','2022');
INSERT INTO OcanFormOption VALUES (1162,'1.2','Year of First Entry Date','2023','2023');
INSERT INTO OcanFormOption VALUES (1163,'1.2','Year of First Entry Date','2024','2024');
INSERT INTO OcanFormOption VALUES (1164,'1.2','Year of First Entry Date','2025','2025');
INSERT INTO OcanFormOption VALUES (1165,'1.2','Year of First Entry Date','2026','2026');
INSERT INTO OcanFormOption VALUES (1166,'1.2','Year of First Entry Date','2027','2027');
INSERT INTO OcanFormOption VALUES (1167,'1.2','Year of First Entry Date','2028','2028');
INSERT INTO OcanFormOption VALUES (1168,'1.2','Year of First Entry Date','2020','2029');
INSERT INTO OcanFormOption VALUES (1169,'1.2','Year of First Entry Date','2030','2030');

INSERT INTO OcanFormOption VALUES (1170,'1.2','Month of First Entry Date','01','01');
INSERT INTO OcanFormOption VALUES (1171,'1.2','Month of First Entry Date','02','02');
INSERT INTO OcanFormOption VALUES (1172,'1.2','Month of First Entry Date','03','03');
INSERT INTO OcanFormOption VALUES (1173,'1.2','Month of First Entry Date','04','04');
INSERT INTO OcanFormOption VALUES (1174,'1.2','Month of First Entry Date','05','05');
INSERT INTO OcanFormOption VALUES (1175,'1.2','Month of First Entry Date','06','06');
INSERT INTO OcanFormOption VALUES (1176,'1.2','Month of First Entry Date','07','07');
INSERT INTO OcanFormOption VALUES (1177,'1.2','Month of First Entry Date','08','08');
INSERT INTO OcanFormOption VALUES (1178,'1.2','Month of First Entry Date','09','09');
INSERT INTO OcanFormOption VALUES (1179,'1.2','Month of First Entry Date','10','10');
INSERT INTO OcanFormOption VALUES (1180,'1.2','Month of First Entry Date','11','11');
INSERT INTO OcanFormOption VALUES (1181,'1.2','Month of First Entry Date','12','12');

-- June 1, 2010
INSERT INTO OcanFormOption VALUES (1182,'1.2','Frequency of Drug Use','5','Past 6 months');
INSERT INTO OcanFormOption VALUES (1183,'1.2','Frequency of Drug Use','6','Ever');
INSERT INTO OcanFormOption VALUES (1184,'1.2','Number Of Centres','1','1');
INSERT INTO OcanFormOption VALUES (1185,'1.2','Number Of Centres','2','2');
INSERT INTO OcanFormOption VALUES (1186,'1.2','Number Of Centres','3','3');
INSERT INTO OcanFormOption VALUES (1187,'1.2','Number Of Centres','4','4');

INSERT INTO OcanFormOption VALUES (1188,'1.2','Age in Months','0','0');
INSERT INTO OcanFormOption VALUES (1189,'1.2','Age in Months','1','1');
INSERT INTO OcanFormOption VALUES (1190,'1.2','Age in Months','2','2');
INSERT INTO OcanFormOption VALUES (1191,'1.2','Age in Months','3','3');
INSERT INTO OcanFormOption VALUES (1192,'1.2','Age in Months','4','4');
INSERT INTO OcanFormOption VALUES (1193,'1.2','Age in Months','5','5');
INSERT INTO OcanFormOption VALUES (1194,'1.2','Age in Months','6','6');
INSERT INTO OcanFormOption VALUES (1195,'1.2','Age in Months','7','7');
INSERT INTO OcanFormOption VALUES (1196,'1.2','Age in Months','8','8');
INSERT INTO OcanFormOption VALUES (1197,'1.2','Age in Months','9','9');
INSERT INTO OcanFormOption VALUES (1198,'1.2','Age in Months','10','10');
INSERT INTO OcanFormOption VALUES (1199,'1.2','Age in Months','11','11');

INSERT INTO OcanFormOption VALUES (1200,'1.2','OCAN Type','CORE','CORE OCAN');
INSERT INTO OcanFormOption VALUES (1201,'1.2','OCAN Type','SELF','CORE + SELF OCAN');
INSERT INTO OcanFormOption VALUES (1202,'1.2','OCAN Type','FULL','FULL OCAN');

update OcanFormOption set ocanDataCategoryValue="OTH" where id=570;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=711;
update OcanFormOption set ocanDataCategory="Referral Source" where id=711;
update OcanFormOption set ocanDataCategoryValue="OTH" where id=180;
update OcanFormOption set ocanDataCategoryValue="UNK" where id=181;

update OcanFormOption set ocanDataCategoryValue="OTH" where ocanDataCategoryName="Other";
update OcanFormOption set ocanDataCategoryValue="UNK" where ocanDataCategoryName="Unknown";


update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where ocanDataCategoryName="Client declined to answer";
update OcanFormOption set ocanDataCategoryName="Consumer Declined to Answer" where ocanDataCategoryName="Client Declined to Answer";


