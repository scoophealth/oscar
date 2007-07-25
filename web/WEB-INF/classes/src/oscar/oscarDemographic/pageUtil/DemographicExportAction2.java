/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicExportAction
 *
 *
 * DemographicExportAction.java
 *
 * Created on June 29, 2005, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import cds.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import javax.servlet.http.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import noNamespace.*;
import org.apache.struts.action.*;
import org.apache.xmlbeans.*;
import org.jdom.*;
import org.jdom.output.*;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import oscar.entities.LabRequest;
import oscar.oscarDemographic.data.*;
import oscar.oscarLab.ca.on.*;
import oscar.oscarPrevention.*;
import oscar.oscarReport.data.*;
import oscar.util.*;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicExportAction2 extends Action {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    String setName = request.getParameter("patientSet");      

    DemographicSets dsets = new DemographicSets();
    ArrayList list = dsets.getDemographicSet(setName);
    ArrayList list2 = dsets.getDemographicSet(setName);

    DemographicData d = new DemographicData();

    ArrayList inject = new ArrayList();

    PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();//new PreventionDisplayConfig();         
    ArrayList prevList  = pdc.getPreventions();
    for (int k =0 ; k < prevList.size(); k++){
	Hashtable a = (Hashtable) prevList.get(k);   
	if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
	    inject.add((String) a.get("name"));
	}	     	
    }
System.out.println("Ronnie here in DemographicExportAction2");
    pdc = null;
    prevList = null;

    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = null;

    CommonLabTestValues comLab = new CommonLabTestValues();
    PreventionData pd = new PreventionData();
    DemographicExt ext = new DemographicExt();

    cds.OmdCdsDocument omdCdsDoc = cds.OmdCdsDocument.Factory.newInstance();

    for(int i = 0 ; i < list.size(); i++){	
	String demoNo = (String) list.get(i);
	
	// DEMOGRAPHICS
	DemographicData.Demographic demographic = d.getDemographic(demoNo);
	Hashtable demoExt = ext.getAllValuesForDemo(demoNo);

	cds.OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
	cds.PatientRecordDocument.PatientRecord patientRec = omdCds.addNewPatientRecord();
	cds.DemographicsDocument.Demographics demo = patientRec.addNewDemographics();
	
	cdsDt.PersonNameStandard.LegalName legalName = demo.addNewNames().addNewLegalName();
	cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
	cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
	
	cdsDt.HealthCard healthCard = demo.addNewHealthCard();
	cdsDt.AddressStructured address = demo.addNewAddress().addNewStructured();
	cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
	cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
	
	firstName.setPart(demographic.getFirstName());
	lastName.setPart(demographic.getLastName());
	demo.setGender(cdsDt.Gender.Enum.forString(demographic.getSex()));
	healthCard.setNumber(demographic.getJustHIN());
	healthCard.setVersion(demographic.getVersionCode());
	address.setLine1(demographic.getAddress());
	address.setCity(demographic.getCity());
	address.setCountrySubdivisionCode(demographic.getProvince());
	address.addNewPostalZipCode().setPostalCode(demographic.getPostal());
	phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
	phoneResident.setPhoneNumber(demographic.getPhone());
	if (demoExt.get("hPhoneExt")!=null) phoneResident.setExtension((String)demoExt.get("hPhoneExt"));
	phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
	phoneWork.setPhoneNumber(demographic.getPhone());
	if (demoExt.get("wPhoneExt")!=null) phoneWork.setExtension((String)demoExt.get("wPhoneExt"));
	demoExt = null;
	
	Calendar c = Calendar.getInstance();
	c.setTime(UtilDateUtilities.StringToDate(demographic.getDob("-")));
	demo.setDateOfBirth(c);
         
	// IMUNIZATIONS
	ArrayList prevList2 = pd.getPreventionData(demoNo);                           
	for (int k =0 ; k < prevList2.size(); k++){
	    Hashtable a = (Hashtable) prevList2.get(k);  
	    if (a != null && inject.contains((String) a.get("type")) ){
		cds.ImmunizationsDocument.Immunizations immu = patientRec.addNewImmunizations();
		if (a.get("type")!=null) immu.setImmunizationName((String) a.get("type"));
		if (a.get("refused")!=null) immu.setRefusedFlag(convert10toboolean((String) a.get("refused")));
		Hashtable extraData = pd.getPreventionKeyValues((String) a.get("id"));
		if (extraData.get("comments")!=null) immu.setNotes((String)extraData.get("comments"));

		if (a.get("prevention_date")!=null) {
		    c.setTime(UtilDateUtilities.StringToDate((String)a.get("prevention_date")));
		    immu.setDate(c);
		}
		extraData = null;
	    }                                                       
	    a = null;
	}
	prevList2 = null;

	// MEDICATIONS & TREATMENTS
	arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
	for (int p = 0; p < arr.length; p++){
	    cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
	    if (arr[p].getDrugName()!=null) medi.setDrugName(arr[p].getDrugName());
	    if (arr[p].getDosageDisplay()!=null) medi.setDosage(arr[p].getDosageDisplay());
	    if (arr[p].getFreqDisplay()!=null) medi.setFrequency(arr[p].getFreqDisplay());
	    if (arr[p].getQuantity()!=null) medi.setQuantity(arr[p].getQuantity());
	    if (arr[p].getSpecial()!=null) medi.setNotes(arr[p].getSpecial());
	    if (arr[p].getDrugName()!=null) medi.setNumberOfRefills(BigInteger.valueOf(arr[p].getRepeat()));
	    
	    if (arr[p].getRxDate()!=null) {
		c.setTime(arr[p].getRxDate());
		medi.setStartDate(c);
	    }
	    if (arr[p].getRxCreatedDate()!=null) {
		cdsDt.DateFullOrPartial writtenDate = medi.addNewPrescriptionWrittenDate();
		c.setTime(arr[p].getRxCreatedDate());
		writtenDate.setFullDate(c);
	    }
	}
	arr = null;
	
	// LABORATORY RESULTS
	ArrayList labs = comLab.findValuesForDemographic(demoNo);         
	for (int l = 0 ; l < labs.size(); l++){
	    Hashtable h = (Hashtable) labs.get(l);

	    cds.LaboratoryResultsDocument.LaboratoryResults labr = patientRec.addNewLaboratoryResults();
	    cds.LaboratoryResultsDocument.LaboratoryResults.Result labResult = labr.addNewResult();
	    cds.LaboratoryResultsDocument.LaboratoryResults.ReferenceRange labRef = labr.addNewReferenceRange();
	    
	    if (h.get("testName")!=null) labr.setTestName((String) h.get("testName"));
	    if (h.get("abn")!=null) labr.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Enum.forString((String) h.get("abn")));
	    if (h.get("result")!=null) labResult.setValue((String) h.get("result"));
	    if (h.get("unit")!=null) labResult.setUnitOfMeasure((String) h.get("unit"));
	    if (h.get("range")!=null) labRef.setReferenceRangeText((String) h.get("range"));
	    
	    if (h.get("testDate")!=null) {
		c.setTime(UtilDateUtilities.StringToDate((String) h.get("testDate")));
		labr.setCollectionDateTime(c);
	    }
	    h = null;
	}
	labs = null;
    }
	
    try{
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename=\"export-"+setName+"-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".xml\"" );
	omdCdsDoc.save(response.getOutputStream());

    }catch(Exception e2){e2.printStackTrace();}

    return null;
    //return mapping.findForward("success");
}  
   
   Boolean convert10toboolean(String s){
      Boolean ret = false;
      if ( s!= null && s.trim().equals("1") ){
        ret = true; 
      }
            
      return ret;
   }
   
   public DemographicExportAction2() {
   }
   
   public void getMembers(Object obj){
      Class cls = obj.getClass();
      Method[] methods = cls.getMethods();
      for (int i=0; i < methods.length; i++){
         Class ret = methods[i].getReturnType();
         Class[] params = methods[i].getParameterTypes();
         System.out.print(ret.getName()+" ");
         System.out.print(methods[i].getName());
         System.out.print("(");
         for (int j=0; j<params.length; j++){
            System.out.print(" "+params[j].getName());
         }
         System.out.println(")");
      }
   }
   
   public String currentMem(){        
       long total = Runtime.getRuntime().totalMemory();
       long free  = Runtime.getRuntime().freeMemory();
       long Used = total -  free;
       return "Total "+total+" Free "+free+" USED "+Used;
    }
}
