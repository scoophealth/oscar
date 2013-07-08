
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
<%@page import="org.oscarehr.medextract.MedicationExtractor"%>

<%
    MedicationExtractor me = new MedicationExtractor("Simon");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>MedExtract</title>
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
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
                              <li class='btn btn-small'><a href="#">Sign and Save</a></li>
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
                    <h5>Suggested Medications from Text</h5>
                    <table class='table table-striped'>
                        <tbody>
                        <tr>
                            <td><a href='#'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Aspirin 81 mg po bid.</p></td>
                            <td><a href='#'><i class='icon-remove-sign'></i></a></td>
                        </tr> 
                        <tr>
                            <td><a href='#'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Coumadin 10 mg po bid.</p></td>
                            <td><a href='#'><i class='icon-remove-sign'></i></a></td>
                        </tr> 
                        <tr>
                            <td><a href='#'><span class='btn btn-mini btn-success'>Add</span></a></td>
                            <td><p>Lisinopril 50 mg po bid.</p></td>
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
                            <td><a href='#'><span class='btn btn-mini btn-primary'>Edit</span></a></td>
                            <td><p>Aspirin 81 mg po bid.</p></td>
                        </tr> 
                        <tr>
                            <td><a href='#'><span class='btn btn-mini btn-primary'>Edit</span></a></td>
                            <td><p>Coumadin 10 mg po bid.</p></td>
                        </tr> 
                        <tr>
                            <td><a href='#'><span class='btn btn-mini btn-primary'>Edit</span></a></td>
                            <td><p>Lisinopril 50 mg po bid.</p></td>
                        </tr> 
                        </tbody>
                    </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
