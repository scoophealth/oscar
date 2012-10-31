create table frm_labreq_preset (
preset_id int (10) NOT NULL auto_increment primary key,
lab_type varchar(255)  NOT NULL,
prop_name varchar(255) NOT NULL,
prop_value varchar(255) NOT NULL,
status int (1) NOT NULL
);


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_glucose_fasting","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_hba1c","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_uricAcid","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("DM","aci","\n Fast for 12 hours",'1');


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","i_prenatal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests1","Hep B s Ag",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests2","VDRL",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests3","HIV",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests4","Varicella titre",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AnteNatal","o_otherTests5","Ferritin",'1');


insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_bloodFilmExam","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_hemoglobin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_wcbCount","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","h_hematocrit","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_heterophile","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_prenatal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_prenatalHepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","i_vdrl","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AR","otherTest","HIV \n\nVaricella titre",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Acne","../form/formlabreq07.jsp?labType=Acne&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Acne","../form/formlabreq10.jsp?labType=Acne&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Acne","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AHEf","../form/formlabreq07.jsp?labType=AHEf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AHEf","../form/formlabreq10.jsp?labType=AHEf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","o_otherTests1","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_chlamydia","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEf","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AHEm","../form/formlabreq07.jsp?labType=AHEm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AHEm","../form/formlabreq10.jsp?labType=AHEm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEm","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AHEm","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Anemia","../form/formlabreq07.jsp?labType=Anemia&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Anemia","../form/formlabreq10.jsp?labType=Anemia&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","b_ferritin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Anemia","o_otherTests1","reticulocyte ct",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Bp","../form/formlabreq07.jsp?labType=Bp&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Bp","../form/formlabreq10.jsp?labType=Bp&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_sodium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_potassium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_chloride","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Bp","o_otherTests2","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Chol","../form/formlabreq07.jsp?labType=Chol&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Chol","../form/formlabreq10.jsp?labType=Chol&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Chol","h_cbc","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Ed","../form/formlabreq07.jsp?labType=Ed&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Ed","../form/formlabreq10.jsp?labType=Ed&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests1","lh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests2","fsh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests3","prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests4","testosterone",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ed","o_otherTests5","testosterone,free",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fatigue","../form/formlabreq07.jsp?labType=Fatigue&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fatigue","../form/formlabreq10.jsp?labType=Fatigue&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_glucose","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_vitaminB12","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","b_ferritin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","i_mononucleosisScreen","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fatigue","o_otherTests1","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fulstdf","../form/formlabreq07.jsp?labType=Fulstdf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fulstdf","../form/formlabreq10.jsp?labType=Fulstdf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_vaginal","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_chlamydiaSource","cervical",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests2","hep b surf antigen",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdf","o_otherTests3","VDRL",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Fulstdm","../form/formlabreq07.jsp?labType=Fulstdm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Fulstdm","../form/formlabreq10.jsp?labType=Fulstdm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests2","hep b surf antigen",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Fulstdm","o_otherTests3","vdrl",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gastro","../form/formlabreq07.jsp?labType=Gastro&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gastro","../form/formlabreq10.jsp?labType=Gastro&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gastro","o_otherTests1","stool c+s x1",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gastro","o_otherTests2","stool c+p x1",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gstdm","../form/formlabreq07.jsp?labType=Gstdm&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gstdm","../form/formlabreq10.jsp?labType=Gstdm&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immuneStatus","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisA","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisB","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","v_immune_HepatitisC","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests1","hiv-ab",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests2","vdrl",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gstdm","o_otherTests3","rectal - gc/chlam",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Gyne","../form/formlabreq07.jsp?labType=Gyne&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Gyne","../form/formlabreq10.jsp?labType=Gyne&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","o_otherTests1","pap",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Gyne","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 HIV","../form/formlabreq07.jsp?labType=HIV&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 HIV","../form/formlabreq10.jsp?labType=HIV&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("HIV","o_otherTests1","hiv-ab",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Ibs","../form/formlabreq07.jsp?labType=Ibs&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Ibs","../form/formlabreq10.jsp?labType=Ibs&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","o_otherTests1","stool c+s x1",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Ibs","o_otherTests2","stool c+s x1",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Mono","../form/formlabreq07.jsp?labType=Mono&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Mono","../form/formlabreq10.jsp?labType=Mono&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Mono","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Mono","i_mononucleosisScreen","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Pcod","../form/formlabreq07.jsp?labType=Pcod&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Pcod","../form/formlabreq10.jsp?labType=Pcod&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests1","prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests2","lh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests3","fsh",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests4","17-hydroxy-progest",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Pcod","o_otherTests5","testosterone",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdf","../form/formlabreq07.jsp?labType=Stdf&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdf","../form/formlabreq10.jsp?labType=Stdf&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_chlamydiaSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_gcSource","cerv",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdf","m_vaginal","checked",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdms","../form/formlabreq07.jsp?labType=Stdms&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdms","../form/formlabreq10.jsp?labType=Stdms&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_gcSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdms","o_otherTests1","urine - microscopic",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Stdma","../form/formlabreq07.jsp?labType=Stdma&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Stdma","../form/formlabreq10.jsp?labType=Stdma&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_chlamydia","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_chlamydiaSource","urine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_gc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Stdma","m_gcSource","urine",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Thyroid","../form/formlabreq07.jsp?labType=Thyroid&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Thyroid","../form/formlabreq10.jsp?labType=Thyroid&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Thyroid","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Thyroid","o_otherTests1","free t4",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Urticaria","../form/formlabreq07.jsp?labType=Urticaria&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Urticaria","../form/formlabreq10.jsp?labType=Urticaria&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","i_mononucleosisScreen","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","i_rubella","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","v_acuteHepatitis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Urticaria","o_otherTests1","Urinalysis (routine)",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Uti","../form/formlabreq07.jsp?labType=Uti&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Uti","../form/formlabreq10.jsp?labType=Uti&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","m_urine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","o_otherTests1","urine, routine",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Uti","o_otherTests2","urine, micro",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 Vaginitis","../form/formlabreq07.jsp?labType=Vaginitis&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 Vaginitis","../form/formlabreq10.jsp?labType=Vaginitis&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("Vaginitis","o_otherTests1","swab vaginal c+s",'1');

insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 AntiPsychotic","../form/formlabreq07.jsp?labType=AntiPsychotic&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 AntiPsychotic","../form/formlabreq10.jsp?labType=AntiPsychotic&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_sodium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_potassium","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","b_lipidAssessment","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests1","AST",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests2","Prolactin",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("AntiPsychotic","o_otherTests3","ECG",'1');


insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq07 PreLithium","../form/formlabreq07.jsp?labType=PreLithium&demographic_no=","",'0');
insert into encounterForm (form_name,form_value,form_table,hidden) values("LabReq10 PreLithium","../form/formlabreq10.jsp?labType=PreLithium&demographic_no=","",'0');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_tsh","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_creatinine","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_alt","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_alkPhosphatase","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_bilirubin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_albumin","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","b_urinalysis","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","h_cbc","checked",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","o_otherTests1","GGT",'1');
insert into frm_labreq_preset (lab_type,prop_name,prop_value,status) values ("PreLithium","o_otherTests2","ECG",'1');