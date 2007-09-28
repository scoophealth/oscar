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
 * Ronnie Cheng
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicExportAction
 *
 *
 * DemographicExportAction2.java
 *
 * Created on July 23, 2007, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import cds.*;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
import java.util.zip.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.*;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarPrevention.*;
import oscar.oscarReport.data.DemographicSets;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicExportAction2 extends Action {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
    
    pdc = null;
    prevList = null;

    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = null;

    CommonLabTestValues comLab = new CommonLabTestValues();
    PreventionData pd = new PreventionData();
    DemographicExt ext = new DemographicExt();

    String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
    File[] files = null;
    Vector err = new Vector();
    if (tmpDir==null || tmpDir.trim().equals("")) {
	throw new Exception("Temporary Export Directory not set! Check oscar.properties.");
    } else {
	if (tmpDir.charAt(tmpDir.length()-1)!='/') tmpDir = tmpDir + '/';
	
	files = new File[list.size()];
	String data = null;
	for(int i = 0 ; i < list.size(); i++){	
	    String demoNo = (String) list.get(i);
	    if (demoNo==null || demoNo.trim().equals("")) err.add("Error: No Demographic Number");

	    // DEMOGRAPHICS
	    DemographicData.Demographic demographic = d.getDemographic(demoNo);
	    Hashtable demoExt = ext.getAllValuesForDemo(demoNo);

	    cds.OmdCdsDocument omdCdsDoc = cds.OmdCdsDocument.Factory.newInstance();
	    cds.OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
	    cds.PatientRecordDocument.PatientRecord patientRec = omdCds.addNewPatientRecord();
	    cds.DemographicsDocument.Demographics demo = patientRec.addNewDemographics();

	    demo.setUniqueVendorIdSequence(demoNo);
	    
	    cdsDt.PersonNameStandard.LegalName legalName = demo.addNewNames().addNewLegalName();
	    cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
	    cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
	    
	    data = demographic.getFirstName();
	    if (data==null || data.trim().equals("")) err.add("Error: No First Name for Patient "+demoNo);
	    else firstName.setPart(data);
	    
	    
	    data = demographic.getLastName();
	    if (data==null || data.trim().equals("")) err.add("Error: No Last Name for Patient "+demoNo);
	    else lastName.setPart(data);
	    
	    data = demographic.getSex();
	    if (data==null || data.trim().equals("")) err.add("Error: No Gender for Patient "+demoNo);
	    else demo.setGender(cdsDt.Gender.Enum.forString(data));
	    
	    data = demographic.getRosterStatus();
	    if (data==null || data.trim().equals("")) err.add("Error: No Enrollment Status for Patient "+demoNo);
	    else demo.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(data));
	    
	    data = demographic.getPatientStatus();
	    if (data==null || data.trim().equals("")) err.add("Error: No Person Status Code for Patient "+demoNo);
	    else demo.setPersonStatusCode(cdsDt.PersonStatus.Enum.forString(data));
	    
	    Calendar c = Calendar.getInstance();
	    data = demographic.getDob("-");
	    if (data==null || data.trim().equals("")) {
		err.add("Error: No Date Of Birth for Patient "+demoNo);
	    } else {
		c.setTime(UtilDateUtilities.StringToDate(data));
		demo.setDateOfBirth(c);
	    }	    
	    
	    if (demographic.getJustHIN()!=null && !demographic.getJustHIN().trim().equals("")) {
		cdsDt.HealthCard healthCard = demo.addNewHealthCard();
		healthCard.setNumber(demographic.getJustHIN());
		if (demographic.getVersionCode()!=null && !demographic.getVersionCode().trim().equals("")) healthCard.setVersion(demographic.getVersionCode());
	    }
	    if (  demographic.getAddress()!=null && !demographic.getAddress().trim().equals("") ||
		  demographic.getCity()!=null && !demographic.getCity().trim().equals("") ||
		  demographic.getProvince()!=null && !demographic.getProvince().trim().equals("") ||
		  demographic.getPostal()!=null && !demographic.getPostal().trim().equals(""))      {
		cdsDt.AddressStructured address = demo.addNewAddress().addNewStructured();
		if (demographic.getAddress()!=null && !demographic.getAddress().trim().equals("")) address.setLine1(demographic.getAddress());
		if (demographic.getCity()!=null && !demographic.getCity().trim().equals("")) address.setCity(demographic.getCity());
		if (demographic.getProvince()!=null && !demographic.getProvince().trim().equals("")) address.setCountrySubdivisionCode(demographic.getProvince());
		if (demographic.getPostal()!=null && !demographic.getPostal().trim().equals("")) address.addNewPostalZipCode().setPostalCode(demographic.getPostal());
	    }
	    if (demographic.getPhone()!=null && !demographic.getPhone().trim().equals("")) {
		cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
		phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
		phoneResident.setPhoneNumber(demographic.getPhone());
		if (demoExt.get("hPhoneExt")!=null) phoneResident.setExtension((String)demoExt.get("hPhoneExt"));
	    }
	    if (demographic.getPhone2()!=null && !demographic.getPhone2().trim().equals("")) {
		cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
		phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
		phoneWork.setPhoneNumber(demographic.getPhone2());
		if (demoExt.get("wPhoneExt")!=null) phoneWork.setExtension((String)demoExt.get("wPhoneExt"));
	    }
	    demoExt = null;

	    // IMMUNIZATIONS
	    ArrayList prevList2 = pd.getPreventionData(demoNo);
	    for (int k =0 ; k < prevList2.size(); k++){
		Hashtable a = (Hashtable) prevList2.get(k);  
		if (a != null && inject.contains((String) a.get("type")) ){
		    cds.ImmunizationsDocument.Immunizations immu = patientRec.addNewImmunizations();
		    data = (String) a.get("type");
		    if (data==null || data.trim().equals("")) err.add("Error: No Immunization Name for Patient "+demoNo);
		    else immu.setImmunizationName(data);
		    
		    data = (String) a.get("refused");
		    if (data==null || data.trim().equals("")) err.add("Error: No Refused Flag for Patient "+demoNo);
		    else immu.setRefusedFlag(convert10toboolean(data));
		    
		    Hashtable extraData = pd.getPreventionById((String) a.get("id"));
		    data = (String) extraData.get("summary");
		    if (data==null || data.trim().equals("")) err.add("Error: No Category Summary Line (Immunization) for Patient "+demoNo);
		    else immu.setCategorySummaryLine(data);
		    
		    data = (String) extraData.get("manufacture");
		    if (data!=null && !data.trim().equals("")) immu.setManufacturer(data);
		    data = (String) extraData.get("lot");
		    if (data!=null && !data.trim().equals("")) immu.setLotNumber(data);
		    data = (String) extraData.get("route");
		    if (data!=null && !data.trim().equals("")) immu.setRoute(data);
		    data = (String) extraData.get("location");
		    if (data!=null && !data.trim().equals("")) immu.setSite(data);
		    data = (String) extraData.get("comments");
		    if (data!=null && !data.trim().equals("")) immu.setNotes((String)extraData.get("comments"));
		    
		    data = (String) a.get("prevention_date");
		    if (data!=null && !data.trim().equals("")) {
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
		data = arr[p].getDrugName();
		if (data==null || data.trim().equals("")) err.add("Error: No Drug Name for Patient "+demoNo);
		else medi.setDrugName(data);
		data = arr[p].getSpecial();
		if (data==null || data.trim().equals("")) err.add("Error: No Category Summary Line (Medications & Treatments) for Patient "+demoNo);
		else medi.setCategorySummaryLine(data);
		
		if (arr[p].getDosageDisplay()!=null) medi.setDosage(arr[p].getDosageDisplay());
		if (arr[p].getFreqDisplay()!=null) medi.setFrequency(arr[p].getFreqDisplay());
		if (arr[p].getQuantity()!=null) medi.setQuantity(arr[p].getQuantity());		
		if (medi.getDrugName()!=null) medi.setNumberOfRefills(BigInteger.valueOf(arr[p].getRepeat()));
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

		labr.setTestName((String) h.get("testName"));
		data = (String) h.get("abn");
		if (data==null || data.trim().equals("")) err.add("Error: No Result Normal/Abnormal Flag for Patient "+demoNo);
		else labr.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Enum.forString(data));
		
		if (h.get("result")!=null) labResult.setValue((String) h.get("result"));
		if (h.get("unit")!=null) labResult.setUnitOfMeasure((String) h.get("unit"));
		if (h.get("range")!=null) labRef.setReferenceRangeText((String) h.get("range"));
		data = (String) h.get("collDate");
		if (data==null || data.trim().equals("")) err.add("Error: No Collection Date/Time for Patient "+demoNo);
		else c.setTime(UtilDateUtilities.StringToDate(data));
		
		labr.setCollectionDateTime(c);
		h = null;
	    }
	    labs = null;

	    //export file to temp directory
	    String inFiles = null;
	    try{
		File directory = new File(tmpDir);
		if(!directory.exists()){
		    throw new Exception("Temporary Export Directory (as set in oscar.properties) does not exist!");
		}
		inFiles = demoNo+"-"+lastName.getPart()+firstName.getPart()+"-"+UtilDateUtilities.getToday("yyyy-mm-dd.HH.mm.ss")+".xml";
		files[i] = new File(directory,inFiles);
	    }catch(Exception e){
		e.printStackTrace();
	    }
	    try {
		    omdCdsDoc.save(files[i]);
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
	    }
	}
	
	//create ReadMe.txt & ExportEvent.log
	File[] exportFiles = new File[files.length+2];	
	for (int i=0; i<files.length; i++) exportFiles[i] = files[i];
	exportFiles[exportFiles.length-2] = makeReadMe(files, err, request);
	exportFiles[exportFiles.length-1] = makeExportLog(files, err);
	
	//zip all export files
	String zipName = "export-"+setName.replace(" ","")+"-"+UtilDateUtilities.getToday("yyyy-mm-dd.HH.mm.ss")+".zip";
	if (!zipFiles(exportFiles, zipName)) {
	    throw new Exception("Error! Failed zipping export files");
	}
	
	//PGP encrypt zip file (source zip file will be deleted)
	PGPEncrypt pet = new PGPEncrypt(zipName);
	if (!pet.doEncrypt()) throw new Exception("Error encrypting export files!");
	
	//Remove the temporary export files
	for (int i=0; i<exportFiles.length; i++) {
	    exportFiles[i].delete();
	}
	
	//Download & remove .pgp file
	String pgpFile = zipName+".pgp";
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename=\""+pgpFile+"\"" );
	
	InputStream in = new FileInputStream(tmpDir+pgpFile);
	OutputStream out = response.getOutputStream();
	byte[] buf = new byte[1024];
	int len;
	while ((len=in.read(buf)) > 0) out.write(buf,0,len);
	in.close();
	out.close();
	
	File pgpF = new File(tmpDir+pgpFile);
	if (!pgpF.delete()) throw new Exception("Error! Cannot remove .pgp file from temporary directory");
    }
    return null;
}

    File makeReadMe(File[] f, Vector error, HttpServletRequest req) throws IOException {
	String mediaType = req.getParameter("mediaType");
	String noOfMedia = req.getParameter("noOfMedia");
	
	File readMe = new File(f[0].getParentFile(), "ReadMe.txt");
	BufferedWriter out = new BufferedWriter(new FileWriter(readMe));
	out.write("Physician Group                    : ");
	out.write(new ClinicData().getClinicName());
	out.newLine();
	out.write("CMS Vendor, Product & Version      : ");
	String vendor = oscar.OscarProperties.getInstance().getProperty("Vendor_Product");
	if (vendor==null || vendor.trim().equals("")) error.add("Error: Vendor_Product not defined in oscar.properties");
	else out.write(vendor);
	out.newLine();
	out.write("Application Support Contact        : ");
	String support = oscar.OscarProperties.getInstance().getProperty("Support_Contact");
	if (support==null || support.trim().equals("")) error.add("Error: Support_Contact not defined in oscar.properties");
	else out.write(support);
	out.newLine();
	out.write("Media type                         : ");
	out.write(mediaType);
	out.newLine();
	out.write("Number of media                    : ");
	out.write(noOfMedia);
	out.newLine();
	out.write("Date and Time stamp                : ");
	out.write(UtilDateUtilities.getToday("yyyy-mm-dd hh:mm:ss aa"));
	out.newLine();
	out.write("Total byte count of export files(s): ");
	int fileBytes=0;
	for (int i=0; i<f.length; i++) fileBytes += f[i].length();
	out.write(String.valueOf(fileBytes));
	out.newLine();
	out.write("Total patients files extracted     : ");
	out.write(String.valueOf(f.length));
	out.newLine();
	out.write("Number of errors                   : ");
	out.write(String.valueOf(error.size()));
	if (error.size()>0) out.write(" (See ExportEvent.log for detail)");
	out.newLine();
	out.write("Patient ID range                   : ");
	out.write(f[0].getName().substring(0,f[0].getName().indexOf("-")));
	out.write("-");
	out.write(f[f.length-1].getName().substring(0,f[f.length-1].getName().indexOf("-")));
	out.newLine();
	out.close();
	
	return readMe;
    }
    
    File makeExportLog(File[] f, Vector error) throws IOException {
	Hashtable content = null;
	String[] keyword = new String[4];
	keyword[0] = "Demographics";
	keyword[1] = "MedicationsAndTreatments";
	keyword[2] = "Immunizations";
	keyword[3] = "LaboratoryResults";
	String patientID = "Patient ID";
	String totalByte = "Total Bytes";
	String field = null;
	File exportLog = new File(f[0].getParentFile(), "ExportEvent.log");
	BufferedWriter out = new BufferedWriter(new FileWriter(exportLog));
	
	int tableWidth = patientID.length() + totalByte.length() + 5; //add 3+2 for left & right + PatientID delimiters
	for (int i=0; i<keyword.length; i++) tableWidth += keyword[i].length()+2; //add 3 for delimitors
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	out.write("|"+patientID+" |");
	for (int i=0; i<keyword.length; i++) out.write(keyword[i]+" |");
	out.write(totalByte+" |");
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	    
	for (int i=0; i<f.length; i++) {
	    content = countByte(f[i], keyword);

	    out.write("|");
	    field = f[i].getName().substring(0,f[i].getName().indexOf("-"));
	    out.write(fillUp(field,' ',patientID.length()));
	    out.write(" |");
	    int total=0;
	    for (int j=0; j<keyword.length; j++) {
		field = content.get(keyword[j]).toString();
		total += Integer.parseInt(field);
		out.write(fillUp(field,' ',keyword[j].length()));
		out.write(" |");
	    }
	    out.write(fillUp(String.valueOf(total),' ',totalByte.length()));
	    out.write(" |");
	    out.newLine();
	}
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	
	//write any error that has occurred
	for (int i=0; i<error.size(); i++) {
	    out.newLine();
	    out.write((String)error.get(i));
	}
	out.newLine();
	out.close();
	
	return exportLog;
    }

    boolean zipFiles(File[] exports, String zipFileName) throws Exception {
	byte[] buf = new byte[1024];
	ZipOutputStream zout = null;	
	try {		
		zout = new ZipOutputStream(new FileOutputStream(exports[0].getParent()+"/"+zipFileName));
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot create ZIP file");
	}

	// Compress the input files
	for (int i=0; i<exports.length; i++) {
	    String filePath = exports[i].getAbsolutePath();
	    String fileName = exports[i].getName();
	    FileInputStream fin = null;
	    try {
		    fin = new FileInputStream(filePath);
	    } catch (FileNotFoundException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: While zipping, Export File not found - " + fileName);
	    }
	    try {
		    // Add ZIP entry to output stream
		    zout.putNextEntry(new ZipEntry(fileName));
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot add file to ZIP - " + fileName);
	    }

	    // Transfer bytes from the input files to the ZIP file
	    int len;
	    try {
		    while ((len = fin.read(buf)) > 0) zout.write(buf, 0, len);
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot write data to ZIP - " + fileName);
	    }
	    try {
		    // Complete the entry
		    zout.closeEntry();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot complete ZIP entry - " + fileName);
	    }
	    try {
		    fin.close();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot close input file - " + fileName);
	    }
	}
	try {
		// Complete the ZIP file
		zout.close();
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot close ZIP file");
	}
	return true;
    }
    
    Hashtable countByte(File fin, String[] kwd) throws FileNotFoundException, IOException {
	Hashtable catcnt = new Hashtable();
	String[] tag = new String[4];
	
	FileInputStream fis = new FileInputStream(fin);
	BufferedInputStream bis = new BufferedInputStream(fis);
	DataInputStream dis = new DataInputStream(bis);
	
	int cnt=0, tcnt=0;
	for (int i=0; i<tag.length; i++) {
	    tag[i] = "cds:" + kwd[i];
	    boolean tagfnd=false;
	    while (!tagfnd && dis.available()!=0) {
		if ((char)dis.read()!='<') continue;
		cnt=0;
		for (int j=0; j<tag[i].length(); j++) {
		    if ((char)dis.read()!=tag[i].charAt(j)) break;
		    cnt++;
		}
		if (cnt<tag[i].length()) continue;
		cnt++;	//add 1 for the '<' character
		tagfnd=true;
	    }
	    while (tagfnd && dis.available()!=0) {
		boolean clstag=false;
		if ((char)dis.read()=='<') {
		    if ((char)dis.read()=='/') clstag=true;
		    cnt++;
		}
		cnt++;
		if (clstag) {
		    int cnt_tag=0;
		    for (int j=0; j<tag[i].length(); j++) {
			cnt++;
			cnt_tag++;
			if ((char)dis.read()!=tag[i].charAt(j)) break;
		    }
		    if (cnt_tag<tag[i].length()) continue;
		    cnt++;  //add 1 for the '>' character
		    tagfnd=false;
		}
	    }
	    catcnt.put(kwd[i], cnt);
	}
	fis.close();
	bis.close();
	dis.close();
	
	return catcnt;
    }
   
    boolean convert10toboolean(String s){
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
    
    String fillUp(String filled, char c, int size) {
	if (size>=filled.length()) {
	    int fill = size-filled.length();
	    for (int i=0; i<fill; i++) filled += c;
	}
	return filled;
    }
}
