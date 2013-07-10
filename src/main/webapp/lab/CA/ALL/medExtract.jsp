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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>MedExtract</title>
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
        <script type="text/javascript" src="bootstrap/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
        <style type='text/css'>
            .well-bordered {
                border: 1px solid black;    
            }
        </style>
    </head>
    <body style='background-color:#e5e5e5;'>
        <div class="navbar">
            <div class="navbar-inner">
                <a class="brand" href="#">HART, Gordon</a>
                <ul class="nav">
                    <li class=""><a href="#"></a></li>
                    <li><a href="#">Health #:  1234-567-890</a></li>
                    <li><a href="#">DOB:  July 5th 2000 (13)</a></li>
                    <li><a href="#">Gender: Male</a></li>
                    <li><a href="#">Address:  2110 Wenman Dr, Victoria BC</a></li>
                </ul>
                <ul class='nav pull-right'>
                    <li><a href="#">Next Visit: July 7th 2013 (2 days)</a></li>
                    <li><a href="#">MRP: OscarDoc</a></li>
                </ul>
          </div>
        </div>
        <div style=''class="container">
            <div class='row-fluid'>
                <div class='span8'>
                    <div class='row-fluid'>
                        <div class='span6' style='margin-top:10px;'><span class='lead'>Discharge Summary</span></div>
                        <div class='span6' style='margin-top:10px;'>
                            <ul class="nav pull-right">
                              <li class='btn btn-small'><a href="#">Forward</a></li>
                              <li class='btn btn-small'><a href="#">Notes</a></li>
                              <li class='btn btn-small'><a href="#">Call Patient</a></li>
                              <li class='btn btn-small'><a href="#SignAndSaveModal" data-toggle='modal' role='button'>Sign and Save</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class='well well-bordered'>
                        <p>
RECORD #10364
899267981 | G | 38407554 | | 3238805 | 6/22/2005 12:00:00 AM | CHRONIC OBSTRUCTIVE PULMONARY DISEASE | Signed | DIS | Admission Date: 6/22/2005 Report Status: Signed
Discharge Date: 6/5/2005
ATTENDING: EHLI , STANLEY M.D.
PRIMARY CARE PHYSICIAN: Eli Miltenberger , MD
PRINCIPAL DIAGNOSIS: Tracheostomy tube change.
DIAGNOSES: Redundant oropharyngeal tissue , aspiration.
HISTORY OF PRESENT ILLNESS: The patient is a 60-year-old woman
with severe COPD , insulin dependent diabetes , and morbid obesity
who presents from acute inpatient rehab with recurrent trach tube
dislocations and need for a trach tube change. The
patient has had numerous intubations and an ICU stay from March
to August 2005 due to her severe COPD and poor respiratory status.
On admission , she did report tight breathing and shortness of
breath. She denies any chest pain , abdominal pain , diarrhea ,
constipation , dysuria , fevers , or chills.
PAST MEDICAL HISTORY: Severe COPD with numerous admissions
including an ICU admission from March to August 2005. The patient
also has schizoaffective disorder , adult-onset type 2 diabetes ,
requiring insulin therapy , morbid obesity , coronary artery
disease , and left ventricular ejection fraction of 30%.
MEDICATIONS ON ADMISSION: Combivent four puffs q.i.d. ,
prednisone 20 mg per GT daily , Novolin insulin SC 40 units q.a.m.
and 18 units q.p.m. , transdermal nitro one patch topical daily ,
lactulose 45 mL GT b.i.d. , Lasix 80 mg GT q.a.m. , Mepron
suspension 750 mg GT daily , Colace liquid 200 mg GT b.i.d. ,
Ultram 50 mg GT q.i.d. p.r.n. , Cardizem 30 mg q.i.d. GT , vitamin
C 500 mg GT b.i.d. , Depakene syrup 250 mg GT q.h.s. , Genasyme 80
mg GT b.i.d. , Celexa 20 mg GT q.a.m. , Lopressor 6.25 mg GT
b.i.d. , aspirin 81 mg GT daily , Novolin insulin sliding scale SC
q.a.c. , Captopril 25 mg GT t.i.d. , Clozaril 100 mg GT q.h.s. and
50 mg GT q.a.m. , Reglan 15 mg GT q.i.d. , Feosol 300 mg GT t.i.d. ,
Protonix 40 mg GT daily , vitamin B1 100 mg GT daily , Singulair 10
mg GT daily , fleets enema , one enema PR daily , p.r.n.
constipation , Tylenol elixir 1000 mg p.o. q.i.d. p.r.n. pain or
fever , and Flovent 220 mcg two puffs b.i.d.
                        </p>
                    </div>
                </div>
                <div class='span4'>
                    <div class='well well-bordered'>
                        <a href='#addNewMedModal' data-toggle='modal' role='button' class='btn btn-block btn-primary'>Add New Medication</a>
                    </div>
                    <div class='well well-bordered'>
                    <h5>Suggested Medications from Text</h5>
                    <table class='table table-striped'>
                        <tbody>
                        <tr>
                            <td><a href='#addMed1' data-toggle='modal' role='button'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Atenolol 25 mg tablet oral daily</p></td>
                            <td><a href='#'><i class='icon-remove-sign'></i></a></td>
                        </tr> 
                        <tr>
                            <td><a href='#addMed2' data-toggle='modal' role='button'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Omeprazole 20 mg tablet oral daily</p></td>
                            <td><a href='#'><i class='icon-remove-sign'></i></a></td>
                        </tr> 
                        <tr>
                            <td><a href='#addMed3' data-toggle='modal' role='button'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Nitroglycerin spray sublingual p.r.n<br>take as instructed</p></td>
                            <td><a href='#'><i class='icon-remove-sign'></i></a></td>
                        </tr> 
                        </tbody>
                    </table>
                    </div>
                    <div class='well well-bordered'>
                    <h5>Current Medications in OSCAR</h5>
                    <table class='table table-striped'>
                        <tbody>
                        <tr>
                            <td><a href="#editMed1" role="button" data-toggle="modal" class='btn btn-mini btn-primary'>Edit</a></td>
                            <td><p>Aspirin (ASA) 81 mg po bid.</p></td>
                        </tr> 
                        <tr>
                            <td><a href="#editMed2" role="button" data-toggle="modal" class='btn btn-mini btn-primary'>Edit</a></td>
                            <td><p>Coumadin (WARFARIN) 10 mg tablet oral twice daily</p></td>
                        </tr> 
                        <tr>
                            <td><a href="#editMed3" role="button" data-toggle="modal" class='btn btn-mini btn-primary'>Edit</a></td>
                            <td><p>Glucophage (METFORMIN) 500 mg tablet oral twice a day.</p></td>
                        </tr> 
                        </tbody>
                    </table>
                    </div>
                </div>
            </div>
        </div>
 
        <!--Medication Modals -->

        <!----------------------------------------------- ADD MED DEFAULT--------------------------------------------------------->
        <div id="addNewMedModal" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Add Medication:  </h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med1_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med1_brandname" id="med1_brandname" value=""/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med1_ingredientname" id="med1_ingredientname" value=""/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med1_dose" id="med1_dose" maxlength="6" size="6"value=""/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='none'>-</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='po'>oral</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='q.d'>daily</option>
                                <option value='b.i.d'>twice daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med1_longterm" id="med1_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med1_externalprovider" id="med1_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med1_instructions' rows="3"></textarea> 
                        </div>
                    </div>
            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-primary">Add and Prescribe</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>

        <!----------------------------------------------- END ADD MED DEFUALT--------------------------------------------------------->
        <!----------------------------------------------- ADD MED 1--------------------------------------------------------->
        <div id="addMed1" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Add Medication: Atenolol 25 mg tablet oral daily </h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med1_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med1_brandname" id="med1_brandname" value=""/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med1_ingredientname" id="med1_ingredientname" value="Atenolol"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med1_dose" id="med1_dose" maxlength="6" size="6"value="25"/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='tablet'>tablet</option>
                                <option value='po'>oral</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='none'>-</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='q.d'>daily</option>
                                <option value='void'>-</option>
                                <option value='b.i.d'>twice daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med1_longterm" id="med1_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med1_externalprovider" id="med1_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med1_instructions' rows="3"></textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-primary">Add and Prescribe</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>

        <!----------------------------------------------- END ADD MED 1--------------------------------------------------------->
        <!----------------------------------------------- ADD MED 2--------------------------------------------------------->
        <div id="addMed2" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Add Medication: Omeprazole 20 mg tabel oral daily</h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med2_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med2_brandname" id="med2_brandname" value=""/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med2_ingredientname" id="med2_ingredientname" value="Omeprazole"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med2_dose" id="med2_dose" maxlength="6" size="6"value="20"/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='tablet'>tablet</option>
                                <option value='po'>oral</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='none'>-</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='q.d'>daily</option>
                                <option value='void'>-</option>
                                <option value='b.i.d'>twice daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med2_longterm" id="med2_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med2_externalprovider" id="med2_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med2_instructions' rows="3"></textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-primary">Add and Prescribe</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>

        <!----------------------------------------------- END ADD MED 2--------------------------------------------------------->
        <!----------------------------------------------- ADD MED 3--------------------------------------------------------->
        <div id="addMed3" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Add Medication: Nitroglycerin spray sublingual p.r.n</h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med3_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med3_brandname" id="med3_brandname" value=""/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med3_ingredientname" id="med3_ingredientname" value="Nitroglycerin"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med3_dose" id="med3_dose" maxlength="6" size="6"value=""/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='spray'>spray</option>
                                <option value='tablet'>tablet</option>
                                <option value='po'>oral</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='sublingual'>sublingual</option>
                                <option value='po'>oral</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='other'>other</option>
                                <option value='none'>-</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='p.r.n'>as needed (p.r.n)</option>
                                <option value='q.d'>daily</option>
                                <option value='b.i.d'>twice daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                                <option value='void'>-</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med3_longterm" id="med3_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med3_externalprovider" id="med3_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med3_instructions' rows="3">Take as instructed</textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-primary">Add and Prescribe</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>

        <!----------------------------------------------- END ADD MED 1--------------------------------------------------------->
        <!----------------------------------------------- EDIT MED 1 --------------------------------------------------------->

        <div id="editMed1" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Edit Medication:  Aspirin (ASA) 81 mg po bid</h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med1_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med1_brandname" id="med1_brandname" value="Aspirin"/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med1_ingredientname" id="med1_ingredientname" value="ASA"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med1_dose" id="med1_dose" maxlength="6" size="6"value="81"/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='none'>-</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='po'>oral</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='q.d'>daily</option>
                                <option value='b.i.d'>twice daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med1_longterm" id="med1_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med1_externalprovider" id="med1_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med1_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med1_instructions' rows="3"></textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-danger">Remove from OSCAR</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>
        <!----------------------------------------------- END EDIT MED 1 --------------------------------------------------------->
        <!-----------------------------------------------  EDIT MED 2 --------------------------------------------------------->

        <div id="editMed2" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Edit Medication: Coumadin 10 mg tablet oral twice daily</h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med2_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med2_brandname" id="med2_brandname" value="Coumadin"/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med2_ingredientname" id="med2_ingredientname" value="WARFARIN"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med2_dose" id="med2_dose" maxlength="6" size="6"value="10"/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='tablet'>tablet</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='tablet'>tablet</option>
                                <option value='none'>-</option>
                                <option value='spray'>spray</option>
                                <option value='po'>oral</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='b.i.d'>twice daily</option>
                                <option value='q.d'>daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med2_longterm" id="med2_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med2_externalprovider" id="med2_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med2_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med2_instructions' rows="3"></textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-danger">Remove from OSCAR</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>
        <!----------------------------------------------- END EDIT MED 2 --------------------------------------------------------->
        <!----------------------------------------------- EDIT MED 3 --------------------------------------------------------->

        <div id="editMed3" class="modal fade hide">
          <div class="modal-header">
            <h4 id="myModalLabel">Edit Medication: Glucophage (METFORMIN) 500 mg tablet oral twice daily.</h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="med3_name">Medication:</label>
                        <div class="controls">
                            <input type="text" class='input-medium' name="med3_brandname" id="med3_brandname" value="Glucophage"/>
                            <span>(&nbsp</span><input type="text" class='input-medium' name="med3_ingredientname" id="med3_ingredientname" value="METFORMIN"/><span>&nbsp)</span>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_dose">Dose:</label>
                        <div class="controls">
                            <input type="text" class='input-mini' name="med3_dose" id="med3_dose" maxlength="6" size="6"value="500"/>
                            <select class='input-mini'>
                                <option>mg</option>
                                <option>mL</option>
                                <option>mEq</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_route">Route:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='tablet'>tablet</option>
                                <option value='po'>oral</option>
                                <option value='spray'>spray</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='po'>oral</option>
                                <option value='none'>-</option>
                                <option value='spray'>spray</option>
                                <option value='tablet'>tablet</option>
                                <option value='subq'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='other'>other</option>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_freq">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium'>
                                <option value='b.i.d'>twice daily</option>
                                <option value='q.d'>daily</option>
                                <option value='t.i.d'>three times daily</option>
                                <option value='q.i.d'>four times daily</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            <select class='input-medium'>
                                <option value='void'>-</option>
                                <option value='p.r.n_pain'>as needed; pain</option>
                                <option value='p.r.n'>as needed</option>
                                <option value='q.4h'>every 4 hours</option>
                                <option value='q.8h'>every 8 hours</option>
                                <option value='q.12h'>every 12 hours</option>
                                <option value='other'>other</option>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="med3_longterm" id="med3_longterm" value='true'>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="med3_externalprovider" id="med3_externalprovider" value='true'>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="med3_instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='med3_instructions' rows="3"></textarea> 
                        </div>
                    </div>

            </form>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success">Add Medication to OSACAR</button>
            <button class="btn btn-danger">Remove from OSCAR</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
          </div>
        </div>
        <!----------------------------------------------- END EDIT MED 3 --------------------------------------------------------->
        <!----------------------------------------------- SIGN AND SAVE --------------------------------------------------------->
       <div id='SignAndSaveModal'class="modal hide fade">
          <div class="modal-header">
            <h4>Summary</h4>
          </div>
          <div class="modal-body">
            <p>HART, Gordon's profile will now have the following medications: </p>
            <ul>
                <li>Aspirin (ASA) 81 mg po bid.</li>
                <li>Coumadin (WARFARIN) 10 mg tablet oral twice daily</li> 
                <li>Glucophage (METFORMIN) 500 mg tablet oral twice a day.</li> 
            </ul>
            <p>NOTE:  Alternativly, we could show which medications where removed and which were added.</p>
          </div>
          <div class="modal-footer">
            <a href="#" data-dismiss='modal' class="btn btn-danger">Cancel</a>
            <a href="#" data-dismiss='modal' class="btn btn-success">Sign and Save</a>
          </div>
        </div> 
        <!----------------------------------------------- END SIGN AND SAVE --------------------------------------------------------->
            
        <!-- End Modals -->
    </body>
</html>
