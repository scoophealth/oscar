-- MySQL dump 9.07
--
-- Host: localhost    Database: oscar_mcmaster
---------------------------------------------------------
-- Server version	4.0.12-standard

--
-- Dumping data for table 'FaxClientLog'
--

--
-- Dumping data for table 'allergies'
--

--
-- Dumping data for table 'billactivity'
--

--
-- Dumping data for table 'billing'
--

--
-- Dumping data for table 'billingdetail'
--

--
-- Dumping data for table 'billinginr'
--
--
-- Dumping data for table 'billingservice'
--

--
-- Dumping data for table 'consultationRequests'
--

--
-- Dumping data for table 'consultationServices'
--

INSERT INTO consultationServices VALUES (53,'Cardiologia','1');
INSERT INTO consultationServices VALUES (54,'Dermatologia','1');
INSERT INTO consultationServices VALUES (55,'Neurologia','1');
INSERT INTO consultationServices VALUES (56,'Radiologia','1');
INSERT INTO consultationServices VALUES (57,'Veja anotações','1');

--
-- Dumping data for table 'ctl_billingservice'
--

--
-- Dumping data for table 'ctl_billingservice_premium'
--

--
-- Dumping data for table 'ctl_diagcode'
--


--
-- Dumping data for table 'ctl_doctype'
--

INSERT INTO ctl_doctype VALUES ('demographic','lab','A');
INSERT INTO ctl_doctype VALUES ('demographic','consult','A');
INSERT INTO ctl_doctype VALUES ('demographic','insurance','A');
INSERT INTO ctl_doctype VALUES ('demographic','legal','A');
INSERT INTO ctl_doctype VALUES ('demographic','oldchart','A');
INSERT INTO ctl_doctype VALUES ('demographic','others','A');
INSERT INTO ctl_doctype VALUES ('provider','resource','A');
INSERT INTO ctl_doctype VALUES ('provider','desktop','A');
INSERT INTO ctl_doctype VALUES ('provider','handout','A');
INSERT INTO ctl_doctype VALUES ('provider','forms','A');
INSERT INTO ctl_doctype VALUES ('provider','others','A');
INSERT INTO ctl_doctype VALUES ('provider','share','A');

--
-- Dumping data for table 'ctl_document'
--

INSERT INTO ctl_document VALUES ('provider',999998,4953,'A');
INSERT INTO ctl_document VALUES ('provider',999998,4954,'H');
INSERT INTO ctl_document VALUES ('demographic',2147483647,4955,'A');

--
-- Dumping data for table 'ctl_frequency'
--


--
-- Dumping data for table 'ctl_provider'
--

--
-- Dumping data for table 'ctl_specialinstructions'
--

--
-- Dumping data for table 'demographic'
--

--
-- Dumping data for table 'demographicaccessory'
--


--
-- Dumping data for table 'demographiccust'
--


--
-- Dumping data for table 'demographicstudy'
--


--
-- Dumping data for table 'desannualreviewplan'
--


--
-- Dumping data for table 'desaprisk'
--


--
-- Dumping data for table 'diagnosticcode'
--


--
-- Dumping data for table 'diseases'
--


--
-- Dumping data for table 'document'
--

--
-- Dumping data for table 'drugs'
--


--
-- Dumping data for table 'dxresearch'
--


--
-- Dumping data for table 'eChart'
--


--
-- Dumping data for table 'eform'
--


--
-- Dumping data for table 'eform_data'
--


--
-- Dumping data for table 'eforms'
--


--
-- Dumping data for table 'eforms_data'
--


--
-- Dumping data for table 'encounter'
--


--
-- Dumping data for table 'encounterForm'
--

INSERT INTO encounterForm VALUES ('Annual','formAnnual.jsp?demographic_no=','formAnnual');
INSERT INTO encounterForm VALUES ('ALPHA','formAlpha.jsp?demographic_no=','formAlpha');
INSERT INTO encounterForm VALUES ('Rourke','formRourke.jsp?demographic_no=','formRourke');
INSERT INTO encounterForm VALUES ('T2Diabetes','formType2Diabetes.jsp?demographic_no=','formType2Diabetes');
INSERT INTO encounterForm VALUES ('Mental Health','formMentalHealth.jsp?demographic_no=','formMentalHealth');
INSERT INTO encounterForm VALUES ('PeriMenopausal','formPeriMenopausal.jsp?demographic_no=','formPeriMenopausal');
INSERT INTO encounterForm VALUES ('Lab Req','formLabReq.jsp?demographic_no=','formLabReq');
INSERT INTO encounterForm VALUES ('MMSE','formMMSE.jsp?demographic_no=','formMMSE');
INSERT INTO encounterForm VALUES ('Pall. Care','formPalliativeCare.jsp?demographic_no=','formPalliativeCare');
INSERT INTO encounterForm VALUES ('AR','formAR.jsp?demographic_no=','formAR');

--
-- Dumping data for table 'encountertemplate'
--


INSERT INTO encountertemplate VALUES ('SOAP','0001-01-01 00:00:00','');
INSERT INTO encountertemplate VALUES ('Cólica biliar','0001-01-01 00:00:00','Subjetivo:\r\nObjetivo:\r\nAnálise:\r\nPlano:\r\n');
INSERT INTO encountertemplate VALUES ('Enfisema pulmonar','0001-01-01 00:00:00','1. interrogatório sobre pelo menos 3 dos seguintes:\r\ndor, descrição\r\nE\r\nlocalização\r\nintolerância alimentar\r\nrecorrência\r\nfebre\r\n2. Exame abdominal?\r\n3. Exame do Tórax?\r\n4. Freqüência cardíaca e ritmo?\r\n5. Pressão arterial?\r\n6. Hemograma?\r\n7. TGO, bilirrubinas séricas e fosfatase alcalina?\r\n8. RX ou ultrassom da vesícula biliar\r\n9. Orientação sobre baixa ingesta de alimentos gordurosos\r\n10. Retorno dentro de 1 mês?\r\n11. SE recorrente');
INSERT INTO encountertemplate VALUES ('otite externa','0001-01-01 00:00:00','1. Interrogatório sobre sintomas torácicos na consulta, pelo menos 2 dos seguintes:\r\ntosse\r\nexpectoração\r\nsibilos\r\ndispnéia\r\n2. Exame Cardio-pulmonar pelo menos uma vez em dois anos?\r\n3. Eletrocardiograma no prontuário');
INSERT INTO encountertemplate VALUES ('IVAS','0001-01-01 00:00:00','1. Interrogatório sobre sintomas?\r\n2. Otoscopia?\r\n3. Evidências de "membrana timpânica normal"?');
INSERT INTO encountertemplate VALUES ('gripe','0001-01-01 00:00:00','1. Queixa de pelo menos um dos seguintes?\r\ndescarga nasal\r\ninflamação da garganta\r\nastenia\r\nfrio\r\n2. Duração dos sintomas?\r\n3. SE existir tosse na história, exame do tórax?\r\n4. SE paciente\r\n5. SE existir dor de garganta na história, oroscopia?\r\n6. SE antitussígenos prescritos, há tosse na história?\r\n7. SE antibióticos prescritos, há história de infecção secundária?');
INSERT INTO encountertemplate VALUES ('úlcera péptica','0001-01-01 00:00:00','1. Interrogatório sobre 3 dos seguintes:\r\nmialgia\r\nfebre\r\ntosse\r\ntipo de secreção\r\nastenia\r\n2. Interrogatório sobre duração dos sintomas?\r\n3. Exame de ouvido, nariz e garganta?\r\n4. SE tosse, ausculta pulmonar?\r\n5. SE antibióticos prescritos, há história de infecção secundária?');
INSERT INTO encountertemplate VALUES ('reação alérgica','0001-01-01 00:00:00','1. Interrogatório sobre dor epigástrica?\r\n2. Interrogatório sobre história de sintomas semelhantes?\r\n3. Interrogatório sobre alívio dos sintomas com antiácidos ou leite?\r\n4. Exame abdominal?\r\n5. Esofagogastroduodenografia\r\nOU\r\nendoscopia digestiva alta feita?\r\n6.Esofagogastroduodenografia\r\nOU\r\nendoscopia demonstrando lesão ulcerosa?\r\nE/OU em cicatrização?\r\n7. SE úlcera gástrica demonstrada no D1248\r\nOU\r\nna endoscopia, procedimento repetido dentro de 6 semanas?\r\n8. Instrução sobre dieta?\r\n9. SE tabagista, aconselhamento sobre o fumo?\r\n10. Instrução sobre bebidas alcoólicas?\r\n11. Aconselhamento sobre fatores estressantes?\r\n12. Há uso de alguma das seguintes drogas:\r\nesteroides orais\r\nanti-inflamatórios não esteroidais\r\nAAS\r\ncolchicina\r\n13. Retorno pelo menos de 6/6 semanas até paciente assintomático\r\nOU\r\nCura/cicatrização demonstrada pelo esofagograma seriado\r\nOU\r\npela endoscopia?');
INSERT INTO encountertemplate VALUES ('traumatismo crânio-encefálico','0001-01-01 00:00:00','1. Interrogatório sobre tipo OU descrição da reação?\r\n2. Interrogatório sobre localizacão da reação?\r\n3. Interrogatório sobre gravidade da reação?\r\n4. Interrogatório sobre possíveis causas?');
INSERT INTO encountertemplate VALUES ('orquite e epididimite','0001-01-01 00:00:00','Estas questões são aplicadas apenas para\r\no primeiro atendimento do TCE\r\n1. Descrição da lesão?\r\n2. Nível de consciência desde a ocorrência do trauma?\r\n3. Causa do trauma?\r\n4. Mecanismo do trauma?\r\n5. Exame de cabeça e pescoço?\r\n6. Exame de ouvido, nariz e garganta?\r\n7. Nervos cranianos?\r\n8. Exame neurológico?\r\n9. Pulso e pressão arterial?\r\n10. Nível de consciência e orientação no momento do exame?\r\n11. SE rebaixamento do nível de consciência, RX de crânio\r\nOU TC OU encaminhamento?\r\n12. SE dor no pescoço\r\nOU sensibilidade, RX de coluna cervical?\r\n13. Analgésicos narcóticos prescritos?');
INSERT INTO encountertemplate VALUES ('dermatite por fralda','0001-01-01 00:00:00','1. Interrogatório sobre localização da dor?\r\n2. Interrogatório sobre edema dos testículos?\r\n3. Exame dos testículos?\r\n4. Acometimento de sensibilidade?\r\n5. Leucograma?\r\n6. Análise de Urina?\r\n7. Urocultura?\r\n8. Suporte para o escroto?\r\n9. SE epididimite, antibióticos usados?\r\n10. Seantibióticos usados, dosagem e duração do tratamento?\r\n11. Retorno dentro de uma semana?');
INSERT INTO encountertemplate VALUES ('prostatite crônica','0001-01-01 00:00:00','1. Interrogatório sobre duração?\r\n2. Descrição da lesão/rash ?\r\n3. SE monilíase, acometimento da boca?\r\n4. SE monilíase, antifúngico tópico usado?\r\n5. Orientação sobre higiene na troca das fraldas?\r\n6. SE "grave", retorno dentro de 1 mês?\r\n7. Esteróides fluorinados usados?\r\n8. SE monilíase oral também presente, uso de antifúngicos orais?');
INSERT INTO encountertemplate VALUES ('dismenorréia','0001-01-01 00:00:00','1. Interrogatório sobre pelo menos 3 dos seguintes?\r\ndisúria\r\nfreqüência\r\ndor perineal\r\ndispareunia\r\nsecreção uretral\r\ndor lombar\r\nnictúria\r\n2. Exame abdominal?\r\n3. Toque retal?\r\n4. Descrição da próstata');
INSERT INTO encountertemplate VALUES ('ITU','0001-01-01 00:00:00','1. História menstrual?\r\n2. Interrogatório sobre sintomas urinários?\r\n3. Interrogatório sobre períodos da dor?\r\n4. SE sexualmente ativa, exame pélvico com dor à mobilização do colo?\r\n5. Exame abdominal?\r\n6. SE presença de secreção vaginal, urocultura?\r\n7. Retorno dentro de 4 meses?');
INSERT INTO encountertemplate VALUES ('lesão no joelho','0001-01-01 00:00:00','1. Interrogatório sobre duração dos sintomas?\r\n2. Interrogatório sobre primeiro episódio ou episódios recorrentes?\r\n3. Interrogatório sobre pelo menos 2 dos seguintes:\r\nfreqüência\r\ndisúria\r\nhematúria\r\nfebre\r\n4. Exame abdominal?\r\n5. Presença/ausência de sensibilidade em flanco OU CVA?\r\n6. SE mais de 2 infecções dentro de 1 ano em mulher, exame vaginal?\r\n7. Análise de urina AND bacterioscopia?\r\n8. Urocultura?\r\n9. SE três ou mais episódios');
INSERT INTO encountertemplate VALUES ('surdez','0001-01-01 00:00:00','1. Descrição de como a lesão ocorreu?\r\n2. Duração do desconforto?\r\n3. Presença/ausência de colapso ou limitação ao movimento?\r\n4. Presença ou ausência de tumefação?\r\n5. Dor à mobilização?\r\n6. Dor no exame de estabilidade ligamentar?\r\n7. SE derrame persistir por mais de 72 horas, aspiração ou encaminhamento?\r\n8. SE limitação do movimento ou instabilidade, encaminhamento?');
INSERT INTO encountertemplate VALUES ('sífilis','0001-01-01 00:00:00','1. Interrogatório sobre duração da perda auditiva?\r\n2. Interrogatório sobre trauma ou infecção ou exposição ocupacional');
INSERT INTO encountertemplate VALUES ('hérnia inguinal','0001-01-01 00:00:00','1. Interrogatório sobre exposição?\r\n2. SE presença de lesões de pele, interrogatório sobre duração?\r\n3. SE sífilis primária, presença/ausência de cancro?\r\n4. SE sífilis secundária, presença/ausência de rash cutâneo?\r\n5. Presença/ausência de linfadenopatia?\r\n6. VDRL ou FTA abs?\r\n7. SE VDRL ou FTA abs negativo, repetir dentro de 2 meses?\r\n8. Swab para cultura e antibiograma para gonorréia?\r\n9. SE uso de antibióticos, foi penicilina, eritromicina, tetraciclina ou estreptomicina?\r\n10. Notificação para as autoridades de saúde pública?\r\n11. Retorno dentro de 2 meses?');
INSERT INTO encountertemplate VALUES ('depressão','0001-01-01 00:00:00','1. Interrogatório sobre presença/ausência de vômitos?\r\n2. Interrogatório sobre pelo menos 2 dos seguintes:\r\nabaulamento inguinal\r\nduração\r\ndor\r\n3. Descrição da massa inguinal incluindo o lado?\r\n4. possibilidade ou não de redução?\r\n5. SE não redutível E presença de dor, encaminhamento para cirurgia dentro de 24 horas?');
INSERT INTO encountertemplate VALUES ('laringite ou traqueíte','0001-01-01 00:00:00','1. Interrogatório sobre medicamentos/drogas usadas?\r\n2. Interrogatório sobre duração do problema?\r\n3. Interrogatório sobre ideação suicida ou situação leve da depressão?\r\n4. SE queixas físicas, evidências no exame fÍsico ?\r\n5. Exame Cárdio-pulmonar dentro de dois anos?\r\n6. Alteração de humor OU aparência OU afeto?\r\n7. SE prescrito antidepressivo, retorno dentro de 2 semanas?\r\n8. SE primeira prescrição de antidepressivos, duração E\r\n9. SE medicação não prescrita, retorno dentro de 1 mês?\r\n10. SE ideação suicida, encaminhamento ou hospitalização?\r\n11. Discussão sobre fatores estressantes?\r\n12. Barbitúricos foram prescritos?');
INSERT INTO encountertemplate VALUES ('gonorréia','0001-01-01 00:00:00','1. Duração dos sintomas?\r\n2. Presença/ausência de tosse?\r\n3. Oroscopia?\r\n4. Ausculta pulmonar?');
INSERT INTO encountertemplate VALUES ('doença coronariana','0001-01-01 00:00:00','1. Interrogatório sobre tempo desde a exposição?\r\n2. Interrogatório sobre contatos sexuais?\r\n3. Interrogatório sobre sintomas');
INSERT INTO encountertemplate VALUES ('osteoporose','0001-01-01 00:00:00','1. Queixa sobre um dos seguintes em cada consulta?\r\nangina\r\ndispnéia\r\nedema de tornozelo\r\n2. Qeuixa de dor OU uso de nitroglicerina anualmente?\r\n3. Queixa de intolerância para exercícios anualmente? \r\n4. Pressão arterial em cada consulta para este diagnóstico');
INSERT INTO encountertemplate VALUES ('alcoolismo','0001-01-01 00:00:00','\r\n1. Interrogatório sobre presença/ausência de dor?\r\n2. Interrogatório sobre história dietética?\r\n3. Interrogatório sobre data da menopausa?\r\n4. SE presença de dor, exame da região?\r\n5. Queixa de cifose?\r\n6. RX ou densidometria óssea OU espessura cortical?\r\n7. Confirmação de osteoporose por um dos testes da questão 6?\r\n8. Aumento da ingesta de cálcio?');
INSERT INTO encountertemplate VALUES ('laceração','0001-01-01 00:00:00','1. Ingesta de álcool, quantidade por dia?');
INSERT INTO encountertemplate VALUES ('faringite estreptocócica','0001-01-01 00:00:00','1. Interrogatório sobre como a laceração ocorreu?\r\n2. Tempo entre a injúria e a consulta?\r\n3. Descrição do ferimento?\r\n4. SE mão ou punho, acometimento da função?\r\n5. SE acometimento de tendões, encaminhamento?\r\n6. Debridamento?');
INSERT INTO encountertemplate VALUES ('otite média serosa','0001-01-01 00:00:00','1. Interrogatório sobre inflamação da garganta?\r\n2. Interrogatório sobre presença ou ausência de febre?\r\n3. Interrogatório sobre tosse?\r\n4. Oroscopia?\r\n5. Presença/ausência de linfadenopatia?\r\n6. Presença/ausência de exsudato em faringe?\r\n7. SE febre OU linfadenopatia OU exsudato OU hipertrofia de amígdalas, swab de orofaringe com cultura e antibiograma?\r\n8. Uso de antibióticos, foi prescrito penicilina, eritromicina ou cefalosporina?\r\n9. SE uso de antibiótico, adesão do paciente por pelo menos 7 dias?\r\n10. Retorno dentro de 2 semanas?\r\n11. SE cultura estreptocócica positiva, antibióticos usados?');
INSERT INTO encountertemplate VALUES ('hemorróida','0001-01-01 00:00:00','1. Interrogatório sobre no mínimo dois dos seguintes: acuidade auditiva, dor, recorrência de infecções?\r\n2. Queixa de líquido no ouvido médio ou retração do tímpano?\r\n3. Queixa sobre nariz e garganta?\r\n4. SE terceiro episódio ou mais, audiometria ou encaminhamento?\r\n5. SE alterações físicas OU testes de audição alterados, retorno até resolução do caso ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('mononucleose','0001-01-01 00:00:00','1. Interrogatório sobre dor?\r\n2. Interrogatório sobre sangramento?\r\n3. Toque retal?\r\n4. SE sangramento retal, retosigmoidoscopia?');
INSERT INTO encountertemplate VALUES ('hipertireoidismo','0001-01-01 00:00:00','1. Interrogatório sobre pelo menos 2 dos seguintes:\r\ninflamação da garganta\r\nfebre\r\nastenia\r\nlinfadenopatia\r\ndor abdominal\r\n2. Presença/ausência de febre?\r\n3.Oroscopia?\r\n4. Presença/ausência de linfadenopatia?\r\n5. Presença/ausência de hepatoesplenomegalia?\r\n6. leucograma e diferenciais?\r\n7. Sorologia para mononucleose?\r\n8. Teste positivo para mono OU alteração de leucograma?\r\n9. Orientação sobre redução das atividades?\r\n10. Uso de ampicilina?\r\n11. SE presença de esplenomegalia, retorno dentro de 2 semanas?\r\n12. SE ausência de esplenomegalia, retorno dentro de 4 semanas?\r\n13. SE presença de esplenomegalia, orientação para evitar esportes ou atividades de contato?');
INSERT INTO encountertemplate VALUES ('isquemia cerebral transitória','0001-01-01 00:00:00','1. Interrogatório sobre um ou mais dos seguintes:\r\nperda de peso, palpitações\r\ntremores de extremidades, insônia\r\nfraqueza muscular\r\nfadiga\r\n2. exame de tireóide?\r\n3. pulso?\r\n4. Inspeção dos olhos?\r\n5. T4 OU T3 OU resina de captação OU outros testes de tireóide?\r\n6. T4 E T3 E captação elevados?\r\n7. Retorno de 6/6 meses?\r\n8. Eutiroideu dentro de 6 meses ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('impetigo e piodermite','0001-01-01 00:00:00','1. Interrogatório sobre freqüência?\r\n2. Interrogatório sobre duração de cada episódio?\r\n3. Descrição dos sintomas?\r\n4. Exame neurológico com descrição do deficit?\r\n5. Pressão arterial?\r\n6. Ausculta cardiovascular?\r\n7. Presença/ausência de sopro carotídeo?\r\n8. ECG?\r\n9. SE masculino >55 anos, prescrito AAS como medicação inicial ou causa identificada?\r\n10. SE uso de AAS, duração e dosagem registrados?\r\n11. SE 2 ou mais episódios, encaminhamento ou internação hospitalar?\r\n12. SE tabagista, aconselhamento sobre o fumo?\r\n13. Retorno dentro de 1 mês?\r\n14. Causa');
INSERT INTO encountertemplate VALUES ('carcinoma baso e espinocelular','0001-01-01 00:00:00','1. Local de acometimento?\r\n2. SE uso de antibiótico oral, este era penicilina, eritromicina, sulfonamida, tetraciclina ou cefalosporina?\r\n3. SE tetraciclina usada, era paciente');
INSERT INTO encountertemplate VALUES ('conjuntivite','0001-01-01 00:00:00','1. Interrogatório sobre duração da lesão?\r\n2. Localização da lesão?\r\n3. Tamanho da lesão?\r\n4. Biópsia e anatomopatológico ou encaminhamento?\r\n5. SE não foi encaminhado, anatomopatológico positivo?\r\n6. Excisão OU dissecação OU criocirurgia OU encaminhamento?\r\n7. SE não foi encaminhado, retorno dentro de 1 mês?');
INSERT INTO encountertemplate VALUES ('neuralgia do trigêmeo','0001-01-01 00:00:00','1. Interrogatório sobre prurido ou secreção?\r\n2. Interrogatório sobre duração?\r\n3. Descrição da conjuntiva?\r\n4. SE esteróides oftálmicos usados, a córnea foi tingida com colírio fluorescente?');
INSERT INTO encountertemplate VALUES ('glomerulonefrite','0001-01-01 00:00:00','1. Interrogatório sobre gravidade da dor?\r\n2. Interrogatório sobre duração da dor?\r\n3. Interrogatório sobre dor facial?\r\n4. Interrogatório sobre fatores desencadeantes?\r\n5. Exame neurológico?\r\n6. SE uso de tegretol, exame de função hepática dentro de 6 semanas?\r\n7. Orientação sobre história natural da doença ou tranquilização?\r\n8. Retorno dentro de 3 meses?');
INSERT INTO encountertemplate VALUES ('aborto espontâneo','0001-01-01 00:00:00','1. Interrogatório sobre freqüência da urina (oligúria/anúria?)\r\n2. Pressão arterial?\r\n3. Peso?\r\n4. Análise da urina, de rotina E microscopia anualmente?\r\n5. Creatinina anulamente?\r\n6. Uréia sérica anualmente?\r\n7. Clearance de creatinina anualmente?\r\n8. Proteínas séricas anulamente?\r\n9. Hemoglobina anualmente?\r\n10. Um dos seguintes?\r\nproteinemia registrada no prontuário\r\ndismorfismo eritrocitário\r\nbiópsia renal registrada no prontuário\r\n11. Retorno pelo menos anualmente?');
INSERT INTO encountertemplate VALUES ('carcinoma de colo','0001-01-01 00:00:00','1. Data da última menstruação?\r\n2. Duração da última menstruação?\r\n3. Contrações uterinas presentes?\r\n4. Volume e duração do sangramento vaginal?\r\n5. Passagem de tecido?\r\n6. Exame pélvico e observação se colo uterino aberto ou fechado?\r\n7. Pressão arterial?\r\n8.Pulso?\r\n9. Presença/ausência de febre?\r\n10. Teste de gravidez?\r\n11. Hemoglobina?\r\n12. Hematócrito?\r\n13. Fator Rh?\r\n14. SE tecido disponível, amostra encaminhada para laboratório?\r\n15. SE indicado pelo fator Rh, imunoglobulina RHOGAM/RH?\r\n16. Retorno dentro de 1 mês?');
INSERT INTO encountertemplate VALUES ('gravidez ectópica','0001-01-01 00:00:00','1. Interrogatório sobre secreção vaginal no último mês?\r\n2. Interrogatório sobre presença/ausência de spotting no último mês?\r\n3. Descrição do colo, dentro de um mês?\r\n4. Exame de Papanicolau positivo?\r\n5. SE classe IV no esfregaço ou pior, encaminhamento dentro de 1 mês?');
INSERT INTO encountertemplate VALUES ('vertigem','0001-01-01 00:00:00','1. Interrogatório sobre presença/ausência de dor abdominal baixa?\r\n2. Interrogatório sobre data da última menstruação?\r\n3. Interrogatório sobre presença /ausência de sangramento vaginal?\r\n4. Exame pélvico?\r\n5. Pressão arterial E pulso?\r\n6. SE secreção vaginal purulenta, cultura e antibiograma?\r\n7. Exame abdominal?\r\n8. Teste de gravidez?\r\n9. Teste de gravidez negativo, Beta-HCG?\r\n10. SE ausência de encaminhamento ou internação, ultrassonografia pélvica?\r\n11. Teste de gravidez positivo ou Beta-HCG positivo?\r\n12. Encaminhamento ou internação?');
INSERT INTO encountertemplate VALUES ('hernia umbilical','0001-01-01 00:00:00','1. Interrogatório sobre detalhes do episódio?\r\n2. Interrogatório sobre duração do episódio?\r\n3. Interrogatório sobre presença/ausência de fatores desencadeantes?\r\n4. Interrogatório sobre presença/ausência de medicações?\r\n5. Pressão arterial?\r\n6. Freqüência cardíaca e ritmo?\r\n7. Otoscopia?\r\n8. Rhomberg positivo OU reflexos OU nistagmo?\r\nse uso de diuréticos, dosagem de eletrólitos?\r\n10. SE o problema persistir, na segunda consulta solicitar glicemia E hemograma?\r\n11. SE arritmia cardíaca, ECG OU Holter?\r\n12. SE permanecer por mais de 3 meses e diagnóstico específico realizado, encaminhamento?');
INSERT INTO encountertemplate VALUES ('diabetes melitus ?','0001-01-01 00:00:00','***Paciente abaixo de 1 ano de idade\r\n1. Consultas para orientação dos cuidados para o bebê?\r\n2. SE realizado cirurgia, existia indicação?');
INSERT INTO encountertemplate VALUES ('febre do feno','0001-01-01 00:00:00','1. Interrogatório se história familiar de diabetes?');
INSERT INTO encountertemplate VALUES ('fibromiosite','0001-01-01 00:00:00','1. Interrogatório sobre acometimento sazonal?\r\n2. Interrogatório sobre fatores desencadeantes?');
INSERT INTO encountertemplate VALUES ('diverticulite','0001-01-01 00:00:00','1. Interrogatório sobre dor, descrição e localização?\r\n2. Interrogatório sobre fatores de melhora E/OU piora?\r\n3. Interrogatório sobre duração?\r\n4. Interrogatório sobre o padrão do sono?\r\n5. Interrogatório sobre sintomas de fadiga E/OU possível depressão?\r\n6. Descrição dos locais de dor?\r\n7. Teste trigger point');
INSERT INTO encountertemplate VALUES ('síncope','0001-01-01 00:00:00','1. Interrogatório sobre dor abdominal?\r\n2. Interrogatório sobre pelo menos um dos seguintes?\r\nconstipação\r\ndiarréia\r\nsangramento retal\r\nhábito intestinal\r\n3. Interrogatório sobre intolerância alimentar?\r\n4. Exame abdominal?\r\n5. Toque retal?\r\n6. Sangue oculto nas fezes OU dentro de um ano?\r\n7. Enema opaco registrado no prontuário?');
INSERT INTO encountertemplate VALUES ('impotência','0001-01-01 00:00:00','1. Interrogatório sobre 3 dos seguintes:\r\nrecorrência ou primeiro episódio ou fatores predisponentes');
INSERT INTO encountertemplate VALUES ('ansiedade','0001-01-01 00:00:00','1. Interrogatório sobre impotência, constante ou intermitente?\r\n2. Interrogatório sobre uso de álcool\r\n3. Interrogatório sobre DM ou doença sistêmica?\r\n4. Interrogatório sobre problemas emocionais?\r\n5. Interrogatório sobre ereções noturnas\r\n6. Interrogatório sobre medicações?\r\n7. Exame genital?\r\n8. Pressão arterial?\r\n9. Exame abdominal?\r\n10. Exame neurológico?\r\n11. Pulsos?\r\n12. Análise de urina?\r\n13. Glicemia capilar?\r\n14. Um seguimento?\r\n15. SE problema persistir por mais de 3 meses, encaminhamento?\r\n16. Aconselhamento sexual com o parceiro?');
INSERT INTO encountertemplate VALUES ('hematuria','0001-01-01 00:00:00','1. Declaração dos sintomas?\r\n2. Interrogatório sobre duração dos sintomas?\r\n3. Interrogatório sobre fatores desencadeantes?\r\n4. SE queixas físicas, evidência no exame da área afetada?\r\n5. SE uso de ansiolíticos, dosagem e duração registrados?\r\n6. SE primeira prescrição de medicação, retorno dentro de 2 semanas?\r\n7. Aconselhamento ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('menorragia','0001-01-01 00:00:00','1. Interrogatório sobre primeiro episódio ou episódios recorrentes?\r\n2. Interrogatório sobre freqüência da hematúria?\r\n3. Presença/ausência de dor em flanco?\r\n4. presença/ausência de disúria OU freqüência?\r\n5. hematúria visível ou microscópica?\r\n6. presença/ausência de cólica?\r\n7. exame abdominal?\r\n8. percussão dos flancos, achados documentados?\r\n9. SE masculino, exame retal e genital?\r\n10. SE feminino e dois episódios ou mais no último ano, exame pélvico?\r\n11. Análise de Urina E bacterioscopia?\r\n12. Urocultura?\r\n13. Uréia E/OU Creatinina?\r\n14. SE segundo episódio dentro de dois anos, IVP OU encaminhamento?\r\nse > 60 anos e causa não identificada, encaminhamento?');
INSERT INTO encountertemplate VALUES ('câncer de próstata','0001-01-01 00:00:00','1. Interrogatório sobre padrão do sangramento,duração e quantidade?\r\n2. SE\r\n3. Exame pélvico na primeira consulta ou quando sangramento parar?\r\n4. Hb?\r\n5. Exame Papanicolau na primeira consulta ou quando sangramento parar?\r\n6. Causa estabelicida OU encaminhamento dentro de 3 meses da primeira consulta pelo problema?');
INSERT INTO encountertemplate VALUES ('vulvites e vaginites','0001-01-01 00:00:00','1. interrogatório sobre sintomas urinários?\r\n2. Toque retal pelo menos anualmente?\r\n3. PSA anualmente?\r\n4. SE sintomas novos ou mudança dos sintomas, análise de urina e urocultura?\r\n5. anatomopatológico positivo?\r\n6. Retorno de 6/6 meses?');
INSERT INTO encountertemplate VALUES ('infertilidade feminina','0001-01-01 00:00:00','1. interrogatório sobre pelo menos um dos seguintes: prurido vaginal');
INSERT INTO encountertemplate VALUES ('laringite','0001-01-01 00:00:00','1. paridade?\r\n2. infertilidade por mais de 2 anos?\r\n3. interrogatório sobre história medicamentosa?\r\n4. interrogatório sobre história menstrual?\r\n5. exame pélvico?\r\n6. exame das mamas?\r\n7. Exame cárdio-pulmonar dentro de dois anos após diagnóstico inicial?\r\n8. Exame Papanicolau E/OU encaminhamento?\r\n9. análise do sêmem?');
INSERT INTO encountertemplate VALUES ('dor cervical','0001-01-01 00:00:00','1. interrogatório sobre duração?\r\n2. interrogatório sobre tabagismo?');
INSERT INTO encountertemplate VALUES ('dor cervical ','0001-01-01 00:00:00','1. interrogatório sobre dor cervical?\r\n2. interrogatório sobre presença/ausência de trauma?');
INSERT INTO encountertemplate VALUES ('dor cervical','0001-01-01 00:00:00','3.interrogatório sobre um dos seguintes:\r\ndor referida para ombro e parestesia de antebraço com fraqueza dos membros');
INSERT INTO encountertemplate VALUES ('dor cervical ','0001-01-01 00:00:00','4. cometimento de reflexos nos membros superiores?');
INSERT INTO encountertemplate VALUES ('fraturas','0001-01-01 00:00:00','5. presença/ausência de fraqueza de extremidades?\r\n6. Queixa de crepitação ao movimentar o pescoço OU sentro de 1 ano?\r\n7. RX coluna cervical registrado no prontuário?');
INSERT INTO encountertemplate VALUES ('artrite','0001-01-01 00:00:00','1. descrição do acidente?\r\n2. tempo desde o cidente?\r\n3. local do traumatismo?');
INSERT INTO encountertemplate VALUES ('dermatite de estase','0001-01-01 00:00:00','');
INSERT INTO encountertemplate VALUES ('piolho e escabiose','0001-01-01 00:00:00','1. queixa sobre o local?\r\n2. interrogatório sobre duração?\r\n3. presença/ausência de veias varicosas?\r\n4. descrição das lesões?\r\n5. retorno dentro de 1 mês?');
INSERT INTO encountertemplate VALUES ('blefarite','0001-01-01 00:00:00','1. interrogatório sobre prurido?\r\n2. localização?\r\n3. SE escabiose, descrição das lesões de pele');
INSERT INTO encountertemplate VALUES ('uretrite','0001-01-01 00:00:00','1. interrogatório sobre sintomas?\r\n2. duração dos sintomas?\r\n3. Uso de esteróides fluorinados?\r\n4.orientação sobre cuidados com o olho?');
INSERT INTO encountertemplate VALUES ('amigdalite aguda','0001-01-01 00:00:00','1. interrogatório sobre sintomas urinários?\r\n2.interrogatório sobre contato sexual ou traumatismo?\r\n3. presença/ausência de secreção uretral?\r\n4. exame genital?\r\n5. Análise de Urina e bacterioscopia?\r\n6. Urocultura?\r\n7. SE presença de secreção, swab uretral com cultura e antibiograma?\r\n8. VDRL ou FTA abs?\r\n9. SE uso de antibiótico, foi prescrito penicilina, eritromicina, cefalosporina, sulfonamida ou tetraciclina?\r\n10. retorno dentro de 2 semanas?\r\n11. SE cultura de urina ou do swab uretral positiva após tratamento, mudança de antibiótico?\r\n12. SE cultura de urina ou do swab uretral positiva, discussão sobre notificação do parceiro sexual?');
INSERT INTO encountertemplate VALUES ('cistite recorrente','0001-01-01 00:00:00','1. interrogatório sobre inflamação da garganta?\r\n2. descrição das amígdalas?\r\n3. SE prescrito eritromicina, cefalosporina ou sulfa?\r\n4. se > 4 anos, foi prescrito cefalosporina ou sulfa?\r\n5. SE >= 13 anos, uso de penicilina, eritromicina\r\ncefalosporina, sulfa ou tetraciclina?\r\n6. SE prescrito tetraciclina, era paciente');
INSERT INTO encountertemplate VALUES ('broncopneumonia','0001-01-01 00:00:00','1. queixa de algum sintoma urinário?\r\n2. Análise de urina ou por fita?\r\n3.Urocultura?\r\n4. Uréia registrada no prontuário?');
INSERT INTO encountertemplate VALUES ('hematoma','0001-01-01 00:00:00','1. um ou mais dos seguintes:\r\ntosse\r\ndispnéia');
INSERT INTO encountertemplate VALUES ('obstrução renal','0001-01-01 00:00:00','1. interrogatório sobre história e tipo do trauma\r\n2. espontâneo ou traumático?\r\n3. SE espontâneo, interrogatório sobre episódios anteriores\r\n4. SE espontâneo, interrogatório sobre história familiar de sangramento\r\n5. descrição do tamanho?\r\n6. descrição do local?\r\n7. SE espontâneo, hemograma, plaquetas, tempo de protrombina, tempo de tromboplastina feitos?\r\n8. SE atendido nas primeiras 48 horas do acontecimento, gelo recomendado?\r\n9. SE espontâneo OU recorrente, um retorno?\r\n10. SE criança');
INSERT INTO encountertemplate VALUES ('nasofaringite ou ?','0001-01-01 00:00:00','1. interrogatório sobre a dor?\r\n2. exame abdominal?\r\n3. pressão arterial?\r\n4. Uréia e creatinina?\r\n5. Análise de Urina, cultura e antibiograma?\r\n6. Pielografia intravenosa mostra bloqueio?\r\n7. encaminhamento dentro de 1 semana?');
INSERT INTO encountertemplate VALUES ('migrânea','0001-01-01 00:00:00','1. queixa de pelo menos um dos seguintes?\r\ndescarga nasal\r\ninflamação da garganta\r\nastenia\r\nfrio\r\n2. duração dos sintomas?\r\n3. SE tosse na história, exame do tórax?\r\n4. SE paciente\r\n5. SE inflamação de garganta na história, exame de orofaringe?\r\n6. SE prescrição de antitussígenos, tosse na história?\r\n7. SE prescrição de antibióticos, história de infecção secundária?');
INSERT INTO encountertemplate VALUES ('prostatite crônica','0001-01-01 00:00:00','1. interrogatório sobre presença/ausência de aura?\r\n2. SE aura presente, interrogatório sobre tipo de aura?\r\n3. interrogatório sobre localização da dor?\r\n4. interrogatório sobre mudanças na dor de cabeça?\r\n5. exame neurológico no último ano\r\n6. pressão arterial no último ano?\r\n7. SE prescrição de medicação, dosagem registrada?\r\n8. SE prescrição de medicação, duração registrada?');
INSERT INTO encountertemplate VALUES ('infecção do trato urinário','0001-01-01 00:00:00','1. interrogatório sobre pelo menos 3 dos seguintes:\r\ndisúria\r\nfreqüência\r\ndor perineal\r\ndispareunia\r\nsecreção uretral\r\ndor lombar\r\nnictúria\r\n2. exame abdominal?\r\n3. Toque retal?\r\n4. descrição da próstata');
INSERT INTO encountertemplate VALUES ('insuficiência cardíaca congestiva','0001-01-01 00:00:00','1. interrogatório sobre duração dos sintomas?\r\n2. interrogatório sobre primeiro episódio ou episódios recorrentes?\r\n3. interrogatório sobre pelo menos 2 dos seguintes:\r\nfreqüência\r\ndisúria\r\nhematúria\r\nfebre\r\n4. exame abdominal?\r\n5. Presença/ ausência de sensibilidade em flanco OU CVA?\r\n6. SE mais de 2 infecções dentro de 1 ano em mulher, exame vaginal?\r\n7. Análise de Urina e bacterioscopia?\r\n8. Urocultura?\r\n9. SE três ou mais ocorrência');
INSERT INTO encountertemplate VALUES ('cefaléia em salva','0001-01-01 00:00:00','1. interrogatório sobre pelo menos 2 dos seguintes:\r\ndispnéia\r\nedema de membros inferiores\r\ndispnéia paroxística noturna\r\nintolerância a exercícios\r\n2. Lista de medicamentos atuais?\r\n3. exame do tórax?\r\n4. medida de peso em pelo menos 50% das consultas?\r\n5. pressão arterial?\r\n6. queixa sobre tornozelos ou pressão venosa jugular?');
INSERT INTO encountertemplate VALUES ('displasia do colo uterino','0001-01-01 00:00:00','1. história de crises em salvas?\r\n2. crise aguda e de pequena duração e recorrente várias vezes em 24 horas? \r\n3. descrição da dor incluindo 2 dos seguintes:\r\nrubor facial ou sudorese\r\nlacrimejamento unilateral\r\ncongestão nasal\r\n4. pressão arterial?\r\n5. exame neurológico, incluindo anotação sobre pares cranianos?\r\n6. retorno dentro de 6 meses\r\n7. SE medicação, retorno dentro de 1 mês');
INSERT INTO encountertemplate VALUES ('cefaléia','0001-01-01 00:00:00','1. exame de papanicolau pelo menos anualmente?\r\n2. SE útero presente, seguimento anual?\r\n3. SE prsente em menos de um ano, resgatar o período de seguimento?');
INSERT INTO encountertemplate VALUES ('úlcera vulvar herpética','0001-01-01 00:00:00','1. interrogatório sobre pelo menos 6 dos seguintes:\r\ngravidade\r\nfreqüência\r\nlocalização\r\nfatores desencadeantes\r\nhistória medicamentosa\r\nduração dos sintomas\r\nsintomas associados');
INSERT INTO encountertemplate VALUES ('ameaça de aborto','0001-01-01 00:00:00','1. interrogatório sobre história de lesão vaginal?\r\n2. descrição da lesão?\r\n3. localização da lesão?\r\n4. cultura viral?');
INSERT INTO encountertemplate VALUES ('dermatofitose','0001-01-01 00:00:00','1. data da última menstruação?\r\n2. volume de sangramento vaginal?\r\n3. duração do sangramento vaginal?\r\n4. contrações uterinas presentes?\r\n5. SE sangramento intenso ou spotting contínuo por uma semana, exame pélvico?\r\n6. teste de gravidez?\r\n7. Hb?\r\n8. Ht?\r\n9. fator Rh?\r\n10. tipagem sanguínea?\r\n11. teste de gravidez positivo?\r\n12. orientação de repouso?\r\n13.uso de progesterona ou estrogênio?\r\n14. seguimento pelo menos uma vez por semana durante o sangramento?');
INSERT INTO encountertemplate VALUES ('amenorréia primária','0001-01-01 00:00:00','1. localização\r\n2. extensão\r\n3. se griseofulvina prescrita, raspado de pele para cultura e antibiograma?\r\n4. antifúngicos tópicos usados?\r\n5. SE griseofulvina prescrita, antifúngicos tópicos foram usados por um mês primeiramente?\r\n6. retorno dentro de 3 meses?\r\n7. SE griseofulvina prescrita, hemograma dentro de 3 meses?');
INSERT INTO encountertemplate VALUES ('fimose','0001-01-01 00:00:00','1. história familiar?\r\n2. história do crescimento?\r\n3. história do desenvolvimento sexual?');
INSERT INTO encountertemplate VALUES ('lombalgia','0001-01-01 00:00:00','1. interrogatório sobre dor?\r\n2. prepúcio não redutível pelo paciente?\r\n3. descrição do pênis?\r\n4. tentativa de redução pelo médico?\r\n5. aconselhamento sobre cuidados com o pênis?\r\n6. SE médico não consegue reduzir, acompanhamento ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('epistaxe','0001-01-01 00:00:00','1. interrogatório sobre duração e localização da dor?\r\n2. interrogatório sobre presença ou ausência de um dos seguintes:\r\nparestesia\r\nalteração sensorial\r\nirradiação da dor\r\n3. interrogatório sobre presença/ausência de trauma?\r\n4. interrogatório sobre episódio anterior');
INSERT INTO encountertemplate VALUES ('caxumba','0001-01-01 00:00:00','1. interrogatório sobre freqüência?\r\n2. interrogatório sobre duração?\r\n3. quantidade de sangue perdido estimado?\r\n4. inspeção do nariz\r\n5. SE sangramento ativo na hora da consulta, pressão arterial aferida?\r\n6. SE > 60 anos, hemoglobina?\r\n7. SE sangramento nasal recorrente');
INSERT INTO encountertemplate VALUES ('hiperlipidemia','0001-01-01 00:00:00','1. interrogatório sobre duração dos sintomas?\r\n2. tumefação na área da parótida?\r\n3. SE masculino > 11 anos, exame dos testículos?\r\n4. SE uso de analgésicos, são eles AAS ou acetaminofeno?');
INSERT INTO encountertemplate VALUES ('bronquite aguda','0001-01-01 00:00:00','1. história familiar ou registro no prontuário?');
INSERT INTO encountertemplate VALUES ('CÓLICA NA INFÂNCIA','0001-01-01 00:00:00','1. Queixa de tosse?\r\n2. Queixa de expectoração?\r\n3. exame do tórax?\r\n4. temperatura?\r\n5. SE uso de antibióticos, dose e duração');
INSERT INTO encountertemplate VALUES ('DIABETES MELITUS, J','0001-01-01 00:00:00','1. Interrogatório sobre pelo menos dois dos seguintes\r\nvômito\r\nperistalse\r\neructação\r\nflatulência\r\ningesta de líquido\r\n2. Interrogatório sobre tempo de choro\r\n3. Peso registrado com o diagnóstico inicial\r\n4. Queixa sobre a aparência do bebê\r\n5. Evidência de choro logo após alimentar-se\r\n6. Pelo menos um retorno com queixa da situação da cólica?\r\n7. Evidência de algum apoio para os pais');
INSERT INTO encountertemplate VALUES ('PSORÍASE','0001-01-01 00:00:00','1. Pelo menos a cada 6 meses, queixa de um dos seguintes?\r\npoliúria\r\npolidipsia\r\nperda de peso\r\n2. Descrição de fundo (de olho) desde o último ano ou evidência no prontuário');
INSERT INTO encountertemplate VALUES ('ARTRITE DEGENERATIVA','0001-01-01 00:00:00','1. Interrogatório sobre a duração das lesões ou registro no prontuário');
INSERT INTO encountertemplate VALUES ('ACNE VULGAR','0001-01-01 00:00:00','1. Uso de corticóide oral');
INSERT INTO encountertemplate VALUES ('GASTROENTERITE','0001-01-01 00:00:00','1. Se uso de antibióticos ');
INSERT INTO encountertemplate VALUES ('EPICONDILITE','0001-01-01 00:00:00','1. Interrogatório sobre presença ou ausência de vômito\r\n2. Se vômito, qual a freqüência e volume estimado?\r\n3. Interrogatório sobre freqüência, consistência e presença de muco nas fezes\r\n4. Interrogatório sobre presença ou ausência de sangue nas fezes?\r\n5. Interrogatório sobre duração dos sintomas?\r\n6. Interrogatório sobre história de viagem?\r\n7. Exame abdominal?\r\n8. Se criança\r\n9. Comentário sobre presença/ausência de desidratação\r\n10. Se não há resposta em 2 dias, hemograma?\r\n11. Se não há resposta em 2 dias, coprocultura?\r\n12. Se não há resposta em 2 dias, eletrólitos?\r\n13. Se não há resposta em 2 dias, sangue oculto nas fezes?\r\n14. Se gastroenterite, presença de diarréia e vômitos?\r\n15. Uso de anti-espasmódicos, anti-diarréicos, ou antibióticos?\r\n16. Orientação sobre evitar sucos ácidos e leite?');
INSERT INTO encountertemplate VALUES ('TUMORAÇÃO MAMÁRIA','0001-01-01 00:00:00','1. Interrogatório sobre duração?\r\n2. Interrogatório sobre causas?\r\n3. Registro dos achados na palpação?\r\n4. Presença de dor localizada?\r\n5. Presença de sensibilidade à palpação do local afetado?\r\n6. Conselho sobre evitar atividades que causam ou precipitam o problema?');
INSERT INTO encountertemplate VALUES ('ANEMIA FERROPRIVA','0001-01-01 00:00:00','1. Interrogatório sobre duração\r\n2. Interrogatório sobre presença/ausência de dor?\r\n3. Interrogatório sobre mudanças relativas ao ciclo menstrual?\r\n4. Tamanho da tumoração?\r\n5. Localização, descrição específica ou diagrama?\r\n6. Presença/ausência de gânglios axilares?\r\n7. Referência ou consulta de seguimento dentro de quatro semanas?\r\n8. Se não houve encaminhamento previamente e não houve mudança na tumoração ou ele é grande, um dos seguintes?\r\nencaminhamento\r\naspiração\r\nmamografia\r\nexcisão');
INSERT INTO encountertemplate VALUES ('ESCARLATINA','0001-01-01 00:00:00','**NOTA**\r\nnão gestante, nova consulta\r\n1. Interrogatório sobre sangramento intestinal?\r\n2. Interrogatório sobre sangramento de outras origens');
INSERT INTO encountertemplate VALUES ('ATROFIA VAGINAL','0001-01-01 00:00:00','');
INSERT INTO encountertemplate VALUES ('CÓLON IRRITÁVEL','0001-01-01 00:00:00','1. Interrogatório sobre pelo menos um dos seguintes\r\ndispareunia\r\ndisúria\r\nsangramento vaginal\r\nprurido vaginal\r\n2. Descrição de vulva e/ou vagina\r\n3. Papanicolau para índice de cariopicnose\r\n4. Se disúria, análise de urina e bacterioscopia? \r\n5. Cultura e antibiograma de secreçãovaginal?\r\n6. Se uso de agente tópico, premarim/estrógeno conjugado ou dienesterol creme?\r\n7. Se em uso de terapia oral com estrógeno, retorno dentro de um ano?\r\n8. Se disfunção sexual ou dispareunia identificada, aconselhamento?');
INSERT INTO encountertemplate VALUES ('ESPONDILITE ANQUILOSANTE','0001-01-01 00:00:00','1. Interrogatório sobre funcionamento intestinal ou cãimbras, nos últimos 6 meses?\r\n2. Exame abdominal, uma vez por ano?\r\n3. Sangue oculto nas fezes no último ano?\r\n4. Exame sigmoidoscópico ou registro no prontuário');
INSERT INTO encountertemplate VALUES ('OTITE MÉDIA AGUDA','0001-01-01 00:00:00','1. Interrogatório sobre presença/ausência de dor?\r\n2. Interrogatório sobre história de rigidez?\r\n3. Queixa sobre rigidez anualmente?\r\n4. Queixa sobre variação (limitação) do movimento anualmente?\r\n5. Queixa sobre presença/ausência de deformidade?\r\n6. HLA-B27 positivo ou registro de RX positivo ou consulta prévia confirmatório?\r\n7. Uso de corticóide oral iniciado no primeiro atendimento?\r\n8. Se uso de alguma medicação, retorno anualmente?');
INSERT INTO encountertemplate VALUES ('MORDEDURA DE ANIMAL','0001-01-01 00:00:00','1. Descrição dos sintomas?\r\n2. Duração dos sintomas?\r\n3. Otoscopia?\r\n4. Queixa de um dos seguintes\r\nhiperemia do tímpano\r\nabaulamento do tímpano\r\ndiminuição do reflexo de luz\r\n5. Se uso de tetraciclina ou cloranfenicol, era paciente\r\n6. Antibióticos prescritos por no mínimo 10 dias?\r\n7. Se > 4 anos, o antibiótico usado foi penicilina, eritromicina ou ampicilina?\r\n8. SE, o antibiótico usado foi penicilina, amoxicilina, sulfa ou eritromicina?\r\n9. Retorno em 04 semanas do episódio para estadiamento da condição do paciente?');
INSERT INTO encountertemplate VALUES ('HORDÉOLO','0001-01-01 00:00:00','1. Interrogatório sobre o tipo do animal?\r\n2. Interrogatório sobre se o animal foi provocado ou não?\r\n3. Descrição da ferida?\r\n4. Se não fez dose de vacina anti-tetânica há mais de 10 anos, vacinação foi feita?\r\n5. SE animal não provocado, queixa sobre o risco de raiva? ');
INSERT INTO encountertemplate VALUES ('INFARTO MIOCÁRDICO','0001-01-01 00:00:00','1. Pálpebra dolorosa ou inchada? ');
INSERT INTO encountertemplate VALUES ('CARDITE REUMÁTICA','0001-01-01 00:00:00','**NOTA**\r\nAS PERGUNTAS DE 1 A 6 DEVEM ESTAR PRESENTES AO MENOS EM 75% DAS VISITAS. \r\n1. Interrogatório sobre a relação da dor no peito com atividades?\r\n2. Interrogatório sobre palpitações? \r\n3. Interrogatório sobre dispnéia?\r\n4. PA? \r\n5. Ausculta torácica? \r\n6. Ausculta cardiaca? ');
INSERT INTO encountertemplate VALUES ('PROSTATITE','0001-01-01 00:00:00','1. Interrogatório sobre ao menos um dos seguintes\r\ndispnéia aos esforços\r\nintolerância a esforço\r\ndor torácica\r\nfadiga\r\n2. Descrição de sons cardíacos, ritmo, murmúrios?\r\n3. PA?\r\n4. SE disponível na comunidade, ecocardiograma? \r\n5. Raio X de tórax?\r\n6. SE antibioticoprofilaxia, foi penicilina, cefalosporina, sulfonamida, eritromicina? \r\n7. Conselho sobre cobertura com antibiótico para procedimentos cirúrgicos');
INSERT INTO encountertemplate VALUES ('CERATITE','0001-01-01 00:00:00','1. Interrogatório sobre disúria? \r\n2. Interrogatório sobre dor?\r\n3. Próstata sensível?\r\n4. Sumário de urina?\r\n5. Cultura e antibiograma urina?\r\n6. Antibióticos usados e dose?\r\n7. Antibióticos usados e duração?\r\n8. Aconselhamento sobre abandono de pelo menos um dos seguintes: café, álcool, fumo, condimentos?\r\n9. Retorno dentro de 2 semanas? ');
INSERT INTO encountertemplate VALUES ('ARTRITE MONOARTICULAR','0001-01-01 00:00:00','1. Interrogatório sobre ao menos um dos seguintes\r\nfotofobia\r\ndor nos olhos\r\nsecreção ocular\r\nlacrimejamento\r\n2. Duração dos sintomas?\r\n3. Descrição da córnea? \r\n4. Coloração fluorescente?\r\n5. Uso de colírio de corticosteróide?\r\n6. Retorno dentro de 48 horas? \r\n7. Se não há melhora em 48h, encaminhamento?');
INSERT INTO encountertemplate VALUES ('DEGENERAÇÃO DISCAL','0001-01-01 00:00:00','**NOTA**\r\nUma das grandes articulações: tornozelo, joelho, quadril, punho, cotovelo, ombro. \r\n1. Interrogatório sobre dor?\r\n2. Localização?\r\n3. Interrogatório sobre duração dos sintomas? \r\n4. Interrogatório sobre presença/ausência de trauma?\r\n5. Descrição da articulação? \r\n6. Registro da temperatura OU história de febre?\r\n7. Presença de articulação bastante dolorosa com anormalidade ao exame?\r\n8. Diagnóstico definitivo em 3 dias OU encaminhamento? ');
INSERT INTO encountertemplate VALUES ('PERFURAÇÃO TIMPÂNICA','0001-01-01 00:00:00','1. Interrogatório sobre dor lombar, ao menos um dos seguintes\r\nduração\r\nlocalização\r\nirradiação\r\n2. Queixa sobre movimentação da coluna, ao menos um dos seguintes? \r\nflexão\r\nextesão\r\nflexão lateral\r\nrotação\r\n3. RX de coluna lombar');
INSERT INTO encountertemplate VALUES ('ARTRITE REUMATÓIDE','0001-01-01 00:00:00','1. Interrogatório sobre a causa?\r\n2. Interrogatório sobre a dor?\r\n3. Interrogatório sobre descarga de secreção?\r\n4. Localização da perfuração?\r\n5. Tamanho da perfuração? \r\n6. Seguimento até a resolução ou referência? ');
INSERT INTO encountertemplate VALUES ('OXIURÍASE','0001-01-01 00:00:00','**NOTA**\r\nEstas questões aplicam-se somente para artrite reumatóide com diagnóstico prévio\r\n1. Interrogatório sobre dor?\r\n2. Interrogatório sobre rigidez? \r\n3. Interrogatório sobre fadiga?\r\n4. Comentário sobre edema de articulações durante o ano?\r\n5. Comentário sobre limitação de movimento durante o ano?\r\n6. Retorno ao menos uma vez por ano?\r\n7. Se o paciente faz uso de AINES OU cloroquina OU penicilamina OU metotrexate OU ouro ');
INSERT INTO encountertemplate VALUES ('RUBÉOLA','0001-01-01 00:00:00','1. Interrogatório sobre prurido anal ou vulvar?\r\n2. Exame para detectar ovos E/OU vermes no ânus? \r\n3. Teste de oxiúrus?\r\n4. Uso de pamoato de pirantel ou pamoato de pirvinium\r\n5. A família inteira tratou-se simultaneamente?\r\n6. Parasitológico de fezes positivo OU teste do oxiurus positivo? ');
INSERT INTO encountertemplate VALUES ('CELULITE','0001-01-01 00:00:00','1. Interrogatório sobre ao menos um dos seguintes? \r\ncrescimento de gânglios enfartados\r\ncongestão nasal\r\nmialgia, conjuntivite\r\nfebre, dor abdominal\r\notalgia ou dor de garganta\r\n2. Interrogatório sobre a duração dos sintomas?\r\n3. Exantema notável? \r\n4. Presença dos nódulos retro-auriculares palpáveis?');
INSERT INTO encountertemplate VALUES ('LINFADENOPATIA','0001-01-01 00:00:00','1. Interrogatório sobre duração?\r\n2. Localização da lesão?\r\n3. Extensão/tamanho da lesão?\r\n4. Temperatura? \r\n ***NOTA***\r\nAs questões de 5 a 8 aplicam-se se a lesão tem mais de 5 polegadas de diâmetro ou se este é o terceito episódio (ou mais)\r\n5. Leucograma registrado no prontuário?\r\n6. Sumário de urina registrado no prontuário?\r\n7. Cultura e antibiograma da lesão?\r\n8. Registro de glicemia no último ano?\r\n9. Uso de antibióticos por pelo menos 7 dias? \r\n10. Se uso de antibiótico, qual o tipo?\r\n11. Se uso de antibiótico, qual a dose?\r\n12. Retorno em 7 dias? ');
INSERT INTO encountertemplate VALUES ('BURSITE','0001-01-01 00:00:00','1. Interrogatório sobre a localização dos gânglios?\r\n2. Interrogatório sobre a duração? \r\n3. Descrição do nódulo');
INSERT INTO encountertemplate VALUES ('GRAVIDEZ E PARTO','0001-01-01 00:00:00','1. Interrogatório sobre dor OU edema?\r\n2. Interrogatório sobre a localização?\r\n3. Interrogatório sobre duração?\r\n4. Descrição do local da lesão');
INSERT INTO encountertemplate VALUES ('SANGRAMENTO RETAL','0001-01-01 00:00:00','1. Registros de Pré-Natal?\r\n2. Sumário de Urina em cada consulta? \r\n3. Hemoglobina a cada trimestre?\r\n4. Se glicosúria positiva repetida em 2 testes, glicemia, TTGO ou encaminhamento?\r\n5. Se análise de urina por fita positiva, relatório do laboratório da análise de urina e microscopia? \r\n6. Se hemoglobina \r\n7. Se uso de drogas');
INSERT INTO encountertemplate VALUES ('URTICARIA','0001-01-01 00:00:00','1. Interrogatório sobre ao menos 2 dos seguintes: \r\nvolume\r\ntipo de sangramento\r\nduração do sangramento\r\n2. Interrogatório sobre hábitos intestinais?\r\n3. Exame abdominal? \r\n4. Exame retal?\r\n5. Hemoglobina dentro de 1 semana? \r\n6. Proctoscopia dentro de 1 semana?\r\n7. Sigmoidoscopia ou encaminhamento dentro de 2 semanas?\r\n8. SE > 30, o enema baritado E contraste aéreo dentro de 1 mês?\r\n9. SE 30 anos ou menos E nenhuma causa foi encontrada na sigmoidoscopia, enema baritado E contraste aéreo dentro de 1 mês?\r\n10. Enema baritado E/OU sigmoidoscopia dentro de 1 mês?\r\n11. SE nenhum diagnóstico foi estabelecido após 1 mês, encaminhar/consulta OU indicar a justificativa?\r\n12. Se não for hemorróida, retorno? ');
INSERT INTO encountertemplate VALUES ('FOLICULITE ','0001-01-01 00:00:00','1. Interrogatório sobre a duração do rash?\r\n2. Interrogatório sobre a localização do rash?\r\n3. Interrogatório sobre possível causa.');
INSERT INTO encountertemplate VALUES ('CORPO ESTRANHO EM CAVIDADE NASAL','0001-01-01 00:00:00','1. Questionar se é o primeiro episódio ou se é recorrente\r\n2. Descrição da erupção?\r\n3. Localização?\r\n4. Se recorrente');
INSERT INTO encountertemplate VALUES ('ABSCESSO CUTÂNEO','0001-01-01 00:00:00','1. Interrogatório sobre como o corpo estranho entrou no nariz\r\n2. Interrogatório sobre qual o lado afetado? \r\n3. SE removido, descrição do corpo estranho? \r\n4. SE não removido, refenciar ao otorrinolaringologista dentro de 24 horas?');
INSERT INTO encountertemplate VALUES ('ESTOMATITE, MONILÍASE','0001-01-01 00:00:00','1. Interrogatório sobre a posição?\r\n2. Interrogatório sobre se é o primeiro episódio ou se é recorrente? \r\n3. Descrição do tamanho?\r\n4. Presença/ausência de flutuação?\r\n5. Presença/ausência de linfangite?\r\n6. Cultura e antibiograma do pus?\r\n7. SE recorrente, glicemia capilar?\r\n8. Demonstração do pus?\r\n9. Dose infectante?\r\n10. Retorno dentro de 10 dias? ');
INSERT INTO encountertemplate VALUES ('BRONQUITE CRÔNICA','0001-01-01 00:00:00','1. Interrogatório sobre localização e duração das lesões orais? \r\n2.  SE adulto, inquérito sobre causa subjacente ');
INSERT INTO encountertemplate VALUES ('HIPERTENSÃO EM MENORES DE 75 ANOS','0001-01-01 00:00:00','1. Interrogatório sobre ocupação');
INSERT INTO encountertemplate VALUES ('HIPERTROFIA PROSTÁTICA BENIGNA','0001-01-01 00:00:00','1. Interrogatório sobre história familiar de AVC e/ou IAM, ou registro no prontuário?');
INSERT INTO encountertemplate VALUES ('PIELONEFRITE CRÔNICA','0001-01-01 00:00:00','1. Interrogatório sobre sintomas urinários, ao menos um dos seguintes\r\nnictúria\r\nfrequencia\r\nfluxo\r\nurgência\r\n2. Descrição da próstata?\r\n3. Sumário de Urina?\r\n4. Cultura e antibiograma?\r\n5. Se bexiga distendida, drenar lentamente?\r\n6. Se cateterizado ou obstruído, encaminhar?');
INSERT INTO encountertemplate VALUES ('IRITE','0001-01-01 00:00:00','1. Interrogatório sobre 3 dos seguintes ou registro no prontuário');
INSERT INTO encountertemplate VALUES ('REFLUXO ESOFÁGICO','0001-01-01 00:00:00','1. Interrogatório sobre ao menos um dos seguintes: visão borrada, olho doloroso, olho vermelho, fotofobia\r\n2. Descrição dos olhos\r\n3. Encaminhar ou consulta pelo telefone?');
INSERT INTO encountertemplate VALUES ('ESTOMATITE HERPÉTICA','0001-01-01 00:00:00','1. Interrogatório sobre duração dos sintomas?\r\n2. Ao menos dois dos seguintes está presente?\r\npirose quando reclinado\r\nasia\r\nintolerância a condimentos \r\nintolerância a álcool\r\ndisfagia\r\nEructação\r\n3. Exame abdominal? \r\n4. Se há disfagia, endoscopia?\r\n5. Se realizada Esofagogastroduodenografia, demonstrado refluxo?\r\n6. Aconselhar sobre elevação da cabeceira da cama?\r\n7. Aconselhar sobre dieta');
INSERT INTO encountertemplate VALUES ('ASMA','0001-01-01 00:00:00','1. Interrogatório sobre dor em cavidade oral\r\n2. Há ulcerações?');
INSERT INTO encountertemplate VALUES ('INFLAMAÇÃO PÉLVICA','0001-01-01 00:00:00','1. Interrogatório sobre ocorrência do quadro previamente\r\n2. Interrogatório sobre história familiar ou registro no prontuário');
INSERT INTO encountertemplate VALUES ('HERPES ZOSTER','0001-01-01 00:00:00','1. Interrogatório sobre dor pélvica e secreção vaginal? \r\n2. Interrogatório sobre antecedente de DIP OU de doença venérea? \r\n3. Interrogatório sobre o história menstrual? \r\n4 Exame pélvico com queixa de secreção cervical? \r\n5. Queixa ao exame de anexos?\r\n6. Queixa de sensibilidade (dor) pélvica');
INSERT INTO encountertemplate VALUES ('NÓDULO TIREÓIDEO','0001-01-01 00:00:00','1. Descrição da lesão\r\n2. Localização da lesão\r\n3. Se lesões em face OU médico percebe "distribuição oftálmica", exame de córnea OU encaminhamento?');
INSERT INTO encountertemplate VALUES ('ABSCESSO PERIAMIDALIANO','0001-01-01 00:00:00','1. Interrogatório sobre localização?\r\n2. Interrogatório sobre duração?\r\n3. Interrogatório sobre um dos seguintes?\r\npalpitações\r\ntremor\r\nperda de peso\r\n4. Descrição do tamanho da lesão\r\n5. Queixa na localização');
INSERT INTO encountertemplate VALUES ('CONVULSÃO FEBRIL','0001-01-01 00:00:00','1. Interrogatório sobre dor na garganta?\r\n2.Interrogatório sobre dificuldade de engolir ?\r\n3. Exame da garganta?\r\n4. Descrição da massa?\r\n5. Encaminhamento ou hospitalização?');
INSERT INTO encountertemplate VALUES ('ENURESE NOTURNA','0001-01-01 00:00:00','1. Descrição da convulsão\r\n2. Tempo total de duração da convulsão\r\n3. Interrogatório sobre história prévia de convulsões?\r\n4. Interrogatório sobre febre nas 24h precedentes?\r\n5. Interrogatório sobre adoecimento das 24h precedentes?\r\n6. Temperatura?\r\n7. Presença/ausência de rigidez de nuca?\r\n8. Exame de ouvido, nariz e garaganta (ONT)?\r\n9. Exame torácico?\r\n10 Febre em 24 hora?\r\n11. Se temperatura maior que 38graus ');
INSERT INTO encountertemplate VALUES ('CEFALÉIA TENSIONAL','0001-01-01 00:00:00','**NOTA**\r\nSomente para menores de 4 anos\r\n1. Interrogatório sobre história familiar de enurese? \r\n2. Interrogatório sobre freqüência de "cama molhada"?\r\n3. Interrogatório sobre remissões e exacerbações?\r\n4. Exame genital descrito alguma vez no prontuário');
INSERT INTO encountertemplate VALUES ('PLANEJAMENTO FAMILIAR','0001-01-01 00:00:00','1. Interrogatório sobre ao menos um dos 5 seguintes?\r\nlocalização\r\nduração\r\ntempo de início\r\nfreqüência\r\nsintomas associados');
INSERT INTO encountertemplate VALUES ('CLAUDICAÇÃO INTERMITENTE','0001-01-01 00:00:00','1. Interrogatório sobre 3 dos seguintes?\r\ngravidezes\r\nabortos\r\nhistória menstrual?\r\ncirurgia ginecológica\r\nhistória de DIP\r\nhistória de tabagismo');
INSERT INTO encountertemplate VALUES ('CISTITE','0001-01-01 00:00:00','1. Interrogatório sobre duração da dor?\r\n2. Interrogatório sobre uso de tabaco atualmente (quantificar)?\r\n3. Presença/ausência de pulsos em membros inferiores?\r\n4. PA? \r\n5. Queixa abdominal ou aneurisma?\r\n6. Queixa dos membros inferiores, temperatura, crescimento de pêlos e coloração (de MMII)\r\n7. Exame cárdio-pulmonar em 12 meses antes ou 5 meses após apresentação?\r\n8. Colesterol ou triglicérides?\r\n9. Glicemia?\r\n10. Dor em MMII com exercícios, ou ao andar, que aliviam com o descanso?\r\n11. Se fuma, aconselhamento sobre parada?\r\n12. Discussão sobre o cuidado com os pés?');
INSERT INTO encountertemplate VALUES ('HIPOTIREOIDISMO','0001-01-01 00:00:00','1. Interrogatório sobre um ou mais dos seguintes\r\nurgência\r\nfreqüência\r\ndisúria\r\nhematúria\r\n2. Interrogatório sobre duração dos sintomas?\r\n3. Fitas de urina para análise de Proteinúria E hematúria OU sumário de urina OU urocultura? \r\n4. Urocultura positiva ou dois dos seguintes:\r\nurgência\r\nfreqüência\r\ndisúria\r\nhematúria\r\n5. ATB usado: sulfa, ampicilina, sulfa/trimetropim (bactrim), tetraciclina?\r\n6. Um retorno E repetir sumário de urina OU\r\nurocultura?\r\n7. Urocultura negativa no prontuário no fim do tratamento?\r\n8. Uso de estreptomicina ou cloromicetina?');
INSERT INTO encountertemplate VALUES ('DOR ABDOMINAL','0001-01-01 00:00:00','1. Interrogatório sobre tratamento prévio para tireóide?\r\n2. Interrogatório sobre pelo menos um dos seguintes\r\nintolerância ao frio\r\n retardo mental, fluxo menstrual \r\nfraqueza generalizada, constipação\r\n3. Exame da tireóide\r\n4. Exame dos reflexos\r\n5. Queixa de ao menos um dos seguintes: \r\npele seca, mudança da voz\r\nmixedema, letargia\r\n6. T4?\r\n7. Repetir T4 a cada segunda mudança da dose\r\n8. T4 baixo, T3 baixo, ou baixa captação, ou TSH alto?\r\n9. Se recente diagnóstico? ');
INSERT INTO encountertemplate VALUES ('NASOFARINGITE','0001-01-01 00:00:00','1. Interrogatório sobre tipo da dor?\r\n2. Interrogatório sobre duração da dor?\r\n3. Interrogatório localização da dor?\r\n4. Interrogatório sobre presença/ausência de intolerância a algum alimento específico?\r\n5. Interrogatório sobre presença/ausência de sintomas gastro-intestinais?\r\n6. Interrogatório sobre presença/ausência de febre?\r\n7. Se mulher, interrogatório sobre história menstrual?\r\n8. Exame torácico?\r\n9. Exame abdominal?\r\n10. Interrogatório sobre presença/ausência de ponto doloroso?\r\n11. Se mulher, com dor pélvica, dor em quadrante inferior esquerdo ou direito: exame pélvico?\r\n12. Se homem E dor pelvica ou dor em quadrante inferior esquerdo ou direito: exame retal? \r\n13. Sumário de Urina e urocultura?\r\n14. Se urina anormal, urocultura e antibiograma?\r\n15. Se segundo episódio NOS, cultura e antibiograma?');
INSERT INTO encountertemplate VALUES ('EPILEPSIA','0001-01-01 00:00:00','1. História de um dos seguintes?\r\nspray nasal\r\ncongestão nasal\r\ngotejamento nasal posterior\r\nuso de cigarro\r\nexposição a poeira ou fumaça \r\n2. Descrição da mucosa nasal\r\n3. Aconselhamento sobre irritantes da mucosa nasal');
INSERT INTO encountertemplate VALUES ('ABORTO, TERAPÊUTICA','0001-01-01 00:00:00','1. Tipo e descrição das crises\r\n2. Freqüência das crises\r\n3. Tempo duração das crises ');
INSERT INTO encountertemplate VALUES ('MENOPAUSA','0001-01-01 00:00:00','1. História obstétrica\r\n2. Data da última menstruação?\r\n3. Gravidez percebida?\r\n4. Exame pélvico, achados ao exame ou encaminhamento?\r\n5. Estimativa de tamanho do útero, ou número de semanas da gestação ou encaminhamento?\r\n6. Fator Rh?\r\n7. Teste de gravidez realizado?\r\n8. Teste de gravidez positivo?\r\n9. Se for indicado fator Rh, RHOGAM/Rh imunoglobulina?\r\n10. Admissão para dilatação e curetagem ou encaminhamento?\r\n11. Planejamento familiar OU conselho sobre acompanhamento até o nascimento?\r\n12. Retorno em 06 semanas após o aborto?');
INSERT INTO encountertemplate VALUES ('HIPERTIREOIDISMO, TRE?????','0001-01-01 00:00:00','1. Interrogatório sobre história menstrual');
INSERT INTO encountertemplate VALUES ('ANEMIA, NYD','0001-01-01 00:00:00','1. Interrogatório sobre um dos seguintes em cada consulta:\r\nenergia\r\npeso\r\nsensibilidade ao calor \r\n2. Se nova consulta nos últimos 2 anos, questionar sobre duração da doença\r\n3. Se nova consulta nos últimos 2 anos, examinar tireóide e olhos?\r\n4. Ritmo cardíaco e pulsos em cada consulta?\r\n5. T3 RIA OU TSH anulamente?\r\n6. Ao menos um teste de tireóide anormal no prontuário');
INSERT INTO encountertemplate VALUES ('PIELONEFRITE AGUDA','0001-01-01 00:00:00','**NOTA**\r\nNÃO GESTANTE\r\n1. Interrogatório sobre perda sanguínea?\r\n2. Interrogatório sobre a dieta?\r\n3. Exame cárdio-pulmonar dentro de 6 meses?\r\n4. Na consulta, pelo menos 03 dos seguintes\r\nPA\r\npulso\r\nexame abdominal\r\nexame retal\r\n5. Hemoglobina ou hematócrito?\r\n6. Se não há história de perda de sangue como causa, esfregaço de sangue para índice?\r\n7. Se paciente negro, pesquisa de deformidades celulares\r\n8. Hemoglobina para homens maiores de 19 anos e para mulheres maiores de 17 anos\r\n9. Se macrocitose ou esfregaço');
INSERT INTO encountertemplate VALUES ('PTIRÍASE RÓSEA','0001-01-01 00:00:00','1. Interrogatório sobre pelo menos um dos seguintes:\r\nfreqüência urinária\r\nurgência miccional\r\ndisúria');
INSERT INTO encountertemplate VALUES ('HERPANGINA ','0001-01-01 00:00:00','1. Interrogatório sobre duração de rash?\r\n2. Interrogatório sobre presença de placa?\r\n3. Descrição da distribuição?\r\n4. VDRL?\r\n5. Uso de esteróide oral\r\n6. Aconselhamento sobre duração');
INSERT INTO encountertemplate VALUES ('GASTRITE - HIPERACIDEZ','0001-01-01 00:00:00','1 Interrogatório sobre dor na garganta?\r\n2. Exame da garganta?\r\n3. Antibióticos usados?');
INSERT INTO encountertemplate VALUES ('CONSTIPAÇÃO RECORRENTE','0001-01-01 00:00:00','1. Interrogatório sobre localização da dor abdominal\r\n1. Interrogatório sobre duração da dor abdominal\r\n3. Interrogatório sobre tipo de dor abdominal\r\n4. Interrogatório sobre fatores agravantes');
INSERT INTO encountertemplate VALUES ('OBESIDADE','0001-01-01 00:00:00','**NOTA**\r\nPacientes com mais de 30 anos com história prévia de constipação\r\n1. Interrogatório sobre mudança no funcionamento intestinal\r\n2. Interrogatório sobre dieta\r\n3. Interrogatório sobre drogas\r\n4. Exame abdominal\r\n5. Exame retal\r\n6. Sangue oculto nas fezes?\r\n7. Se menos de 3 meses de duração: enema baritado\r\n8. Movimento intestinal infrequente e/ou difícil\r\n9. Instruções sobre o aumento da ingesta de fibras, farelo e substâncias que ajudam na digestão \r\n10. Retorno ou diagnóstico especificado em 03 meses ');
INSERT INTO encountertemplate VALUES ('ENTORSE OU DISTENSÃO, NY','0001-01-01 00:00:00','1. Interrogatório sobre duração da obesidade\r\n2. Peso\r\n3. Altura\r\n4. Índice de Massa Corpórea (IMC)?\r\n5. Uso de anorexiantes ou drogas tireóideas');
INSERT INTO encountertemplate VALUES ('CONCUSSÃO CEREBRAL','0001-01-01 00:00:00','1. Interrogatório sobre como ocorreu a lesão\r\n2. Interrogatório sobre localização da lesão\r\n3. Quando occoreu a lesão\r\n4. Presença/ausência de edema\r\n5. Presença/ausência de dor\r\n6. Presença/ausência de hematoma\r\n7. Se relacionado a esportes, recomendações sobre prevenção de futuros episódios');
INSERT INTO encountertemplate VALUES ('VARICELA','0001-01-01 00:00:00','1. Descrição do tipo de trauma\r\n2. Comentário sobre a gravidade do trauma\r\n3. Tempo decorrido desde a lesão\r\n4. Presença/ausência de alteração de consciência desde a lesão\r\n5. História de diminuição de consciência\r\n6. Exame neurológico\r\n7. Exame do local da lesão\r\n8. RX de crânio\r\n9. Se paciente não internado, enfaixamento de rotina da lesão em cabeça, OU instruções?\r\n10. Uso de narcóticos ou sedativos?\r\n11. Interação no hospital ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('PLEURITE','0001-01-01 00:00:00','1. Interrogatório sobre duração dos sintomas?\r\n2. Descrição do rash\r\n3. Presença de bolha, pápula ou vesícula\r\n4. Uso de AAS?');
INSERT INTO encountertemplate VALUES ('PNEUMONIA LOBAR','0001-01-01 00:00:00','1. Interrogatório sobre duração dos sintomas?\r\n2. Interrogatório sobre localização da dor\r\n3. Presença/ausência de febre\r\n4. Presença/ausência de tosse\r\n5. Interrogatório sobre intensificação da dor à respiração profunda');
INSERT INTO encountertemplate VALUES ('LACERAÇÕES DE PELE','0001-01-01 00:00:00','1. Interrogatório sobre os seguintes:\r\ntosse\r\ndispnéia\r\ndor no peito\r\nfebre\r\n2. Descrição da ausculta pulmonar\r\n3. Macicez à percussão ou condensações\r\n4. Cultura e antibiograma de escarro?\r\n5. Leucograma?\r\n6. RX de tórax em duas posições?\r\n7. Se RX confirmatório, retorno em 30 dias\r\n8. Cultura positiva e RX positivo ou consolidação ao exame\r\n9. Penicilina oral, ou eritromicina ou cefalosporina?\r\n10. Dose?\r\n11. Quantidade (dias)?\r\n12. Retorno em 01 semana');
INSERT INTO encountertemplate VALUES ('OTITE MÉDIA SEROSA','0001-01-01 00:00:00','** NOTA**\r\nPara as questões de 1-7 o médico deverá cumprir as condições para cada episódio de laceração\r\n1. Interrogatório sobre como ocorreu a laceração\r\n2. Tempo decorrido da lesão até o atendimento\r\n3. Descrição da lesão\r\n4. Se mão ou pulso, queixa sobre a função\r\n5. Se tendões foram atingidos: encaminhamento?\r\n6. Desbridamento');
INSERT INTO encountertemplate VALUES ('DIABETES MELITUS, A ???','0001-01-01 00:00:00','1. Interrogatório sobre dois dos seguintes: audição, dor, infecções recorrentes?\r\n2. Queixa de secreção em ouvido médio ou retração de membrana?\r\n3. Queixa sobre nariz e garganta\r\n4. Se é o terceiro episódio ou mais, audiometria ou encaminhamento?\r\n5. Se o exame físico ou o teste de ouvido estão anormais, acompanhamento até a cura ou encaminhamento?');
INSERT INTO encountertemplate VALUES ('CÂNCER DE PRÓSTATA','0001-01-01 00:00:00','1. Interrogatório sobre história de diabetes familiar');
INSERT INTO encountertemplate VALUES ('PUERICULTURA','0001-01-01 00:00:00','1. Interrogatório sobre sintomas urinários?\r\n2. Exame retal ao menos anualmente?\r\n3. PSA anualmente?\r\n4. Se sintomas urinários novos ou alterados, cultura e antibiograma e sumário de urina?\r\n5. Laudo patológico positivo?\r\n6. Retorno a cada 6 meses?');
INSERT INTO encountertemplate VALUES ('ARTRITE, NYD OU NÃO','0001-01-01 00:00:00','1. Interrogatório sobre a alimentação/dieta\r\n2. Interrogatório aos pais sobre habilidades? \r\n3. Peso em cada consulta\r\n4. Comentários sobre marcos normais/anormais no desenvolvimento? \r\n5. Medida de comprimento três vezes ou mais por ano?\r\n6. Registro do perímetro cefálico ao menos 3 vezes no primeiro ano de vida? \r\n7. Três doses de DPT até os 8 meses ou justificativa para mudança no curso\r\n8. Se 1-2 anos, MMR entre 12-15 meses; DPT entre 17-19 meses ou justificativa para alteração no curso?\r\n9. Se maior de 1 ano, ao menos 3 visitas no primeiro ano?\r\n10. Se maior de 2 anos, ao menos 3 visitas no segundo ano?\r\n11. Se os pais identificaram problemas: aconselhamento ou encaminhamento?\r\n12. Administrado MMR antes dos 12 anos?');
INSERT INTO encountertemplate VALUES ('HEMATOMA SUBCUTÂNEO','0001-01-01 00:00:00','**NOTA**\r\nmenos de um mês - múltiplas articulações\r\n1. Interrogatório sobre duração dos sintomas?\r\n2. Localização da dor articular?\r\n3. Descrição da natureza ou gravidade da dor?\r\n4. Interrogatório sobre fatores agravantes ou precipitantes?\r\n5. Descrição da inflamação ou edema?\r\n6. Descrição da variação de movimento?\r\n7. Antes da ou na segunda consulta para o mesmo problema, hemograma?\r\n8. Antes da ou na segunda visita para o mesmo problema, taxa de sedimentação de hemácias?\r\n9. Antes da ou na segunda visita para o mesmo problema, FAN?');
INSERT INTO encountertemplate VALUES ('DERMATITE DE CONTATO','0001-01-01 00:00:00','1. Interrogatório sobre história de trauma e tipo?\r\n2. Interrogatório sobre: espontâneo ou trauma?\r\n3. Se espontâneo, questionar episódios prévios?\r\n4. Se espontâneo, história familiar de sangramentos?\r\n5. Descrição do tamanho?\r\n6. Descrição da localização?\r\n7. Se espontâneo, feitos TTP, TP, hemograma, PLAQUETAS?\r\n8. Se foi atendido dentro de 48h, foi recomendado gelo?\r\n9. Se espontâneo ou recorrente, um retorno?\r\n10. Se criança');
INSERT INTO encountertemplate VALUES ('AMENORRÉIA SECUNDÁRIA','0001-01-01 00:00:00','**NOTA**\r\nIncluindo veneno de planta\r\n1. Interrogatório sobre duração?\r\n2. Interrogatório sobre prurido\r\n3. Interrogatório sobre exposição a irritantes?\r\n4. Localização do rash?\r\n5. Se uso de prednisona oral, não mais que sete dias?\r\n6. Se uso de prednisona oral, retorno ou contato pelo telefone?');
INSERT INTO encountertemplate VALUES ('DIARRÉIA DE REPETIÇÃO','0001-01-01 00:00:00','1. História menstrual?\r\n2. Duração do problema?\r\n3. Descrição do início do problema?\r\n4. História de medicações?');
INSERT INTO encountertemplate VALUES ('PÓLIPO NASAL','0001-01-01 00:00:00','1. Interrogatório sobre a freqüência?\r\n2. Interrogatório sobre a duração?\r\n3. Interrogatório sobre a dieta?\r\n4. Interrogatório sobre uso de medicações?\r\n5. Interrogatório sobre viagens?\r\n6. Interrogatório sobre sangue nas fezes?\r\n7. Interrogatório sobre febre?\r\n8. Interrogatório sobre perda de peso?\r\n9. Interrogatório sobre náusea, dor abdominal tipo cólica?\r\n10. Exame abdominal?\r\n11. Exame retal?\r\n12. Peso ao menos uma vez?\r\n13. Fezes para cultura e antibiograma?\r\n14. Fezes com ovos ou parasitas?\r\n15. Hemograma?\r\n16. Taxa de sedimentação de hemácias?\r\n17. Sigmoidoscopia, ou colonoscopia, ou encaminhamento?\r\n18. Enema baritado?\r\n19. Se enema baritado negativo, Esofagogastroduodenografia com estudo de intestino delgado\r\n20. Se não houve melhora em seis meses, ou não houve diagnóstico específico, encaminhamento?');
INSERT INTO encountertemplate VALUES ('ESCOLIOSE','0001-01-01 00:00:00','1. Interrogatório sobre sintomas nasais?\r\n2. Interrogatório sobre história de asma ou alergia AAS?');
INSERT INTO encountertemplate VALUES ('LESÃO EM CAVIDADE ORAL','0001-01-01 00:00:00','**NOTA**\r\nApenas com o paciente presente\r\n1. Interrogatório sobre como foi percebida a condição?\r\n2. Descrição da localização?');
INSERT INTO encountertemplate VALUES ('GLAUCOMA','0001-01-01 00:00:00','1. Interrogatório sobre a localização?\r\n2. Interrogatório sobre a duração?\r\n3. Descrição da lesão?\r\n4. Se lesão descrita for úlcera, ou placa e não cicatrizada em dois meses, investigação ou encaminhamento? ');
INSERT INTO encountertemplate VALUES ('AMIDALITE CRÔNICA','0001-01-01 00:00:00','1. Interrogatório sobre a visão em cada visita\r\n**NOTA**\r\nSe o paciente faz seguimento com oftalmologista, as questões de 2-8 NÃO SE APLICAM\r\n2. Interrogatório sobre o correto uso dos medicamentos?\r\n3. Fundoscopia anual para estadiamento de cúpula óptica?\r\n4. Exame de campos visuais anualmente?\r\n5. Pressão intra-ocular anualmente?\r\n6. Medicações e dosagens administradas?\r\n7. Aumento da pressão intra-ocular?');
INSERT INTO encountertemplate VALUES ('DERMATITE DE CONTATO, ECZEMA','0001-01-01 00:00:00','1. Interrogatório sobre recorrência de dor de garganta?\r\n2. Descrição das amídalas?\r\n3. Presença/ausência de gânglios cervicais palpáveis?\r\n4. Se antibiótico: penicilina, eritromicina, cefalosporina, tetraciclina?\r\n5. Se amidalectomia ou encaminhamento, houve mais de 4 episódios em 02 anos ou abscesso periamidaliano?');
INSERT INTO encountertemplate VALUES ('DOENÇA FIBROCÍSTICA','0001-01-01 00:00:00','1. Interrogatório sobre duração?\r\n2. Presença/ausência de história familiar de eczema ou registro no prontuário?');
INSERT INTO encountertemplate VALUES ('FARINGITE','0001-01-01 00:00:00','1. Interrogatório sobre ao menos dois dos seguintes:\r\ndor na mama\r\ncomportamento periódico da protuberância \r\nrecorrência do problema\r\nlocalização da protuberância\r\n2. Descrição de ambas mamas?\r\n3. Indicação da posição e do tamanho das protuberâncias?\r\n4. Exame axilar?\r\n5. Se diagnosticado como não cístico, mamografia?\r\n6. Se suspeita de cisto, aspiração ou referência?\r\n7. Exame das mamas pelo médico anualmente após o diagnóstico inicial?\r\n8. Cisto diagnosticado pelo exame ou mamografia?\r\n9. Ultrassom mamário a cada dois anos?\r\n10. Se persiste uma discreta protuberância após a aspiração, encaminhamento?');
INSERT INTO encountertemplate VALUES ('SINUSITE','0001-01-01 00:00:00','1. Interrogatório sobre dor na garganta?\r\n2. Interrogatório sobre duração?\r\n3. Exame da faringe?\r\n4. Se membrana esbranquiçada ou exsudato visível,sorologia para mononucleose e cultura e antibiograma?\r\n5. Hiperemia?');
INSERT INTO encountertemplate VALUES ('GOTA','0001-01-01 00:00:00','1. Interrogatório sobre dor na face ou cefaléia?\r\n2. Interrogatório sobre congestão nasal?\r\n3. Presença/ausência de febre?\r\n4. Presença/ausência de pontos dolorosos sobre os seios?\r\n5. Se recorrente?');
INSERT INTO encountertemplate VALUES ('LESÃO NASAL','0001-01-01 00:00:00','1. Interrogatório sobre os seguintes:\r\nvárias articulações dolorosas?\r\nhistória de edema\r\nhistória de inflamação\r\nacometimento de UMA SÓ articulação?\r\n2. Lista das drogas em uso ou registrado no prontuário?');
--
-- Dumping data for table 'favorites'
--


--
-- Dumping data for table 'form'
--


--
-- Dumping data for table 'formAR'
--


--
-- Dumping data for table 'formAlpha'
--


--
-- Dumping data for table 'formAnnual'
--


--
-- Dumping data for table 'formLabReq'
--


--
-- Dumping data for table 'formMMSE'
--


--
-- Dumping data for table 'formMentalHealth'
--


--
-- Dumping data for table 'formPalliativeCare'
--


--
-- Dumping data for table 'formPeriMenopausal'
--


--
-- Dumping data for table 'formRourke'
--


--
-- Dumping data for table 'formType2Diabetes'
--


--
-- Dumping data for table 'groupMembers_tbl'
--

INSERT INTO groupMembers_tbl VALUES (0,'88888');
INSERT INTO groupMembers_tbl VALUES (0,'999999');
INSERT INTO groupMembers_tbl VALUES (0,'999998');
INSERT INTO groupMembers_tbl VALUES (0,'999997');
INSERT INTO groupMembers_tbl VALUES (0,'174');
INSERT INTO groupMembers_tbl VALUES (17,'174');
INSERT INTO groupMembers_tbl VALUES (17,'999998');
INSERT INTO groupMembers_tbl VALUES (19,'999997');
INSERT INTO groupMembers_tbl VALUES (18,'999999');
INSERT INTO groupMembers_tbl VALUES (18,'88888');

--
-- Dumping data for table 'groups_tbl'
--

INSERT INTO groups_tbl VALUES (17,0,'doc');
INSERT INTO groups_tbl VALUES (18,0,'receptionist');
INSERT INTO groups_tbl VALUES (19,0,'admin');

--
-- Dumping data for table 'ichppccode'
--

--
-- Dumping data for table 'immunizations'
--

--
-- Dumping data for table 'messagelisttbl'
--



--
-- Dumping data for table 'messagetbl'
--


--
-- Dumping data for table 'mygroup'
--

INSERT INTO mygroup VALUES ('Docs','174','Chan','David','a1');
INSERT INTO mygroup VALUES ('IT Support','88888','Support','IT',NULL);

--
-- Dumping data for table 'oscarcommlocations'
--

INSERT INTO oscarcommlocations VALUES (145,'Oscar Users',NULL,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<addressBook><group><group desc=\"doc\" id=\"17\"><address desc=\"Chan, David\" id=\"174\"/><address desc=\"oscardoc, doctor\" id=\"999998\"/></group><group desc=\"receptionist\" id=\"18\"><address desc=\"oscarrep, receptionist\" id=\"999999\"/><address desc=\"Support, IT\" id=\"88888\"/></group><group desc=\"admin\" id=\"19\"><address desc=\"oscaradmin, admin\" id=\"999997\"/></group><address desc=\"Chan, David\" id=\"174\"/><address desc=\"oscaradmin, admin\" id=\"999997\"/><address desc=\"oscardoc, doctor\" id=\"999998\"/><address desc=\"oscarrep, receptionist\" id=\"999999\"/><address desc=\"Support, IT\" id=\"88888\"/></group></addressBook>',NULL);

--
-- Dumping data for table 'preference'
--

INSERT INTO preference VALUES (138,'999998','8','18','15','.default','deepblue');

--
-- Dumping data for table 'prescribe'
--


--
-- Dumping data for table 'professionalSpecialists'
--


--
-- Dumping data for table 'property'
--


--
-- Dumping data for table 'provider'
--

INSERT INTO provider VALUES ('999997','oscaradmin','admin','admin','','','','0001-01-01','','','','','','','','1','','');
INSERT INTO provider VALUES ('999998','oscardoc','doctor','doctor','','','','0001-01-01','','','','','','','','1','','');
INSERT INTO provider VALUES ('999999','oscarrep','receptionist','receptionist','','','','0001-01-01','','','','','','','','1','','');
INSERT INTO provider VALUES ('88888','Support','IT','receptionist','','Admin','','0001-01-01','','','','','','','','1','','');
INSERT INTO provider VALUES ('174','Chan','David','doctor','Family Medicine','TEAM A','M','0001-01-01','','H 905-','058081','','','BAAP','1','1','','');

--
-- Dumping data for table 'providerExt'
--


--
-- Dumping data for table 'radetail'
--


--
-- Dumping data for table 'raheader'
--


--
-- Dumping data for table 'recycle_bin'
--


--
-- Dumping data for table 'recyclebin'
--


--
-- Dumping data for table 'remoteAttachments'
--


--
-- Dumping data for table 'reportagesex'
--


--
-- Dumping data for table 'reportprovider'
--

INSERT INTO reportprovider VALUES ('174','Docs','billingreport','A');

--
-- Dumping data for table 'reporttemp'
--


--
-- Dumping data for table 'rschedule'
--


--
-- Dumping data for table 'scheduledate'
--


--
-- Dumping data for table 'scheduledaytemplate'
--


--
-- Dumping data for table 'scheduleholiday'
--

INSERT INTO scheduleholiday VALUES ('2003-12-25','Natal');
INSERT INTO scheduleholiday VALUES ('2004-01-01','Ano novo');

--
-- Dumping data for table 'scheduletemplate'
--


--
-- Dumping data for table 'scheduletemplatecode'
--

INSERT INTO scheduletemplatecode VALUES ('1','Consulta de 15 minutos','15','#BFEFFF');
INSERT INTO scheduletemplatecode VALUES ('2','Consulta de 30 minutos','30','#BFEFFF');
INSERT INTO scheduletemplatecode VALUES ('3','Consulta de 45 minutos','45','#BFEFFF');

--
-- Dumping data for table 'security'
--

INSERT INTO security VALUES (127,'oscarrep','-51-282443-97-5-9410489-60-1021-45-127-12435464-32','999999','1117');
INSERT INTO security VALUES (128,'oscardoc','-51-282443-97-5-9410489-60-1021-45-127-12435464-32','999998','1117');
INSERT INTO security VALUES (129,'oscaradmin','-51-282443-97-5-9410489-60-1021-45-127-12435464-32','999997','1117');

--
-- Dumping data for table 'serviceSpecialists'
--

INSERT INTO serviceSpecialists VALUES (53,297);

--
-- Dumping data for table 'specialistsJavascript'
--

INSERT INTO specialistsJavascript VALUES ('1','function makeSpecialistslist(dec){\n if(dec==\'1\') \n{K(-1,\"----Choose a Service-------\");D(-1,\"--------Choose a Specialist-----\");}\nelse\n{K(-1,\"----All Services-------\");D(-1,\"--------All Specialists-----\");}\nK(53,\"Cardiology\");\nD(53,\"297\",\"ss4444\",\"ssss, sss ssss\",\"sss\",\"sssss\");\n\nK(54,\"Dermatology\");\n\nK(55,\"Neurology\");\n\nK(56,\"Radiology\");\n\nK(57,\"SEE NOTES\");\n\n\n}\n');

--
-- Dumping data for table 'study'
--


--
-- Dumping data for table 'studydata'
--


--
-- Dumping data for table 'tickler'
--


--
-- Dumping data for table 'tmpdiagnosticcode'
--


