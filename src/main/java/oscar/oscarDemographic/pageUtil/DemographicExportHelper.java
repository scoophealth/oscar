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
package oscar.oscarDemographic.pageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cds.PatientRecordDocument.PatientRecord;

/**
 * Manager class for handling demographic exports. This component retains state for each export, and therefore must not be 
 * created as a field, but instead looked up for each individual export. 
 *
 */
@Component
@Scope("prototype")
public class DemographicExportHelper {

	private static Logger logger = MiscUtils.getLogger();

	@Autowired
	private PartialDateDao partialDateDao;
	
	@Autowired
	private CaseManagementManager cmm;
	
	@Autowired
	private ProviderDataDao providerDataDao;
	
	private List<String> exportErrors = new ArrayList<String>();

	public static XmlOptions getDefaultOptions() {
		XmlOptions options = new XmlOptions();
		options.put(XmlOptions.SAVE_PRETTY_PRINT);
		options.put(XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3);
		options.put(XmlOptions.SAVE_AGGRESSIVE_NAMESPACES);

		HashMap<String, String> suggestedPrefix = new HashMap<String, String>();
		suggestedPrefix.put("cds_dt", "cdsd");
		options.setSaveSuggestedPrefixes(suggestedPrefix);
		options.setSaveOuter();
		return options;
	}
	
	public void addMedications(String demoNo, PatientRecord patientRec) {
		addMedications(demoNo, patientRec, null);
	}

	void addMedications(String demoNo, PatientRecord patientRec, DemographicExportHelperCallback callback) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exporting medications");
		}
		
		// MEDICATIONS & TREATMENTS
		RxPrescriptionData prescriptData = new RxPrescriptionData();
		RxPrescriptionData.Prescription[] arr = null;
		String annotation = null;
		arr = prescriptData.getPrescriptionsByPatient(Integer.parseInt(demoNo));
		for (int p = 0; p < arr.length; p++) {
			MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
			String mSummary = "";
			if (arr[p].getWrittenDate() != null) {
				String dateFormat = partialDateDao.getFormat(PartialDate.DRUGS, arr[p].getDrugId(), PartialDate.DRUGS_WRITTENDATE);
				Util.putPartialDate(medi.addNewPrescriptionWrittenDate(), arr[p].getWrittenDate(), dateFormat);
				mSummary = Util.addSummary("Prescription Written Date", partialDateDao.getDatePartial(arr[p].getWrittenDate(), dateFormat));
			}
			if (arr[p].getRxDate() != null) {
				medi.addNewStartDate().setFullDate(Util.calDate(arr[p].getRxDate()));
				mSummary = Util.addSummary(mSummary, "Start Date", UtilDateUtilities.DateToString(arr[p].getRxDate(), "yyyy-MM-dd"));
			}
			String regionalId = arr[p].getRegionalIdentifier();
			if (StringUtils.filled(regionalId)) {
				medi.setDrugIdentificationNumber(regionalId);
				mSummary = Util.addSummary(mSummary, "DIN", regionalId);
			}
			String drugName = arr[p].getBrandName();
			if (StringUtils.filled(drugName)) {
				medi.setDrugName(drugName);
				mSummary = Util.addSummary(mSummary, "Drug Name", drugName);
			} else {
				drugName = arr[p].getCustomName();
				if (StringUtils.filled(drugName)) {
					medi.setDrugDescription(drugName);
					mSummary = Util.addSummary(mSummary, "Drug Description", drugName);
				} else {
					exportErrors.add("Error! No medication name for Patient " + demoNo + " (" + (p + 1) + ")");
				}
			}

			if (callback != null) {
				if (logger.isInfoEnabled()) {
					logger.info("Invoking " + callback);
				}
				callback.callback();
				if (logger.isInfoEnabled()) {
					logger.info("Completed invocation successfully for " + callback);
				}
			}

			if (StringUtils.filled(arr[p].getDosage())) {
				String[] strength = arr[p].getDosage().split(" ");

				cdsDt.DrugMeasure drugM = medi.addNewStrength();
				if (Util.leadingNum(strength[0]).equals(strength[0])) {//amount & unit separated by space
					drugM.setAmount(strength[0]);
					if (strength.length > 1) drugM.setUnitOfMeasure(strength[1]);
					else drugM.setUnitOfMeasure("unit"); //UnitOfMeasure cannot be null

				} else {//amount & unit not separated, probably e.g. 50mg / 2tablet
					if (strength.length > 1 && strength[1].equals("/")) {
						if (strength.length > 2) {
							String unit1 = Util.leadingNum(strength[2]).equals("") ? "1" : Util.leadingNum(strength[2]);
							String unit2 = Util.trailingTxt(strength[2]).equals("") ? "unit" : Util.trailingTxt(strength[2]);

							drugM.setAmount(Util.leadingNum(strength[0]) + "/" + unit1);
							drugM.setUnitOfMeasure(Util.trailingTxt(strength[0]) + "/" + unit2);
						}
					} else {
						drugM.setAmount(Util.leadingNum(strength[0]));
						drugM.setUnitOfMeasure(Util.trailingTxt(strength[0]));
					}
				}
				mSummary = Util.addSummary(mSummary, "Strength", drugM.getAmount() + " " + drugM.getUnitOfMeasure());
			}

			String drugForm = arr[p].getDrugForm();
			if (StringUtils.filled(drugForm)) {
				medi.setForm(drugForm);
				mSummary = Util.addSummary(mSummary, "Form", drugForm);
			}

			//Process dosage export
			Float dosageValue = arr[p].getTakeMin();
			if (dosageValue == 0) { //takemin=0, try takemax
				dosageValue = arr[p].getTakeMax();
			}
			String drugUnit = StringUtils.noNull(arr[p].getUnit());

			if (drugUnit.equalsIgnoreCase(getDosageUnit(arr[p].getDosage()))) {
				//drug unit should not be same as dosage unit
				//check drug form to see if it matches the following list

				if (StringUtils.containsIgnoreCase(drugForm, "capsule")) drugUnit = "capsule";
				else if (StringUtils.containsIgnoreCase(drugForm, "drop")) drugUnit = "drop";
				else if (StringUtils.containsIgnoreCase(drugForm, "dosing")) drugUnit = "dosing";
				else if (StringUtils.containsIgnoreCase(drugForm, "grobule")) drugUnit = "grobule";
				else if (StringUtils.containsIgnoreCase(drugForm, "granule")) drugUnit = "granule";
				else if (StringUtils.containsIgnoreCase(drugForm, "patch")) drugUnit = "patch";
				else if (StringUtils.containsIgnoreCase(drugForm, "pellet")) drugUnit = "pellet";
				else if (StringUtils.containsIgnoreCase(drugForm, "pill")) drugUnit = "pill";
				else if (StringUtils.containsIgnoreCase(drugForm, "tablet")) drugUnit = "tablet";

				if (drugUnit.equals(arr[p].getUnit())) {
					//drugUnit not changed by the above
					//export dosage as "take * dosageValue"
					dosageValue *= getDosageValue(arr[p].getDosage());
				}
			}

			//export dosage
			medi.setDosage(dosageValue.toString());
			if (StringUtils.filled(drugUnit)) medi.setDosageUnitOfMeasure(drugUnit);
			mSummary = Util.addSummary(mSummary, "Dosage", dosageValue + " " + drugUnit);

			if (StringUtils.filled(arr[p].getSpecialInstruction())) {
				medi.setPrescriptionInstructions(arr[p].getSpecialInstruction());
				mSummary = Util.addSummary(mSummary, "Prescription Instructions", arr[p].getSpecialInstruction());
			}

			if (StringUtils.filled(arr[p].getRoute())) {
				medi.setRoute(arr[p].getRoute());
				mSummary = Util.addSummary(mSummary, "Route", arr[p].getRoute());
			}
			if (StringUtils.filled(arr[p].getFreqDisplay())) {
				medi.setFrequency(arr[p].getFreqDisplay());
				mSummary = Util.addSummary(mSummary, "Frequency", arr[p].getFreqDisplay());
			}
			String duration = arr[p].getDuration();
			if (StringUtils.filled(duration)) {
				String durunit = StringUtils.noNull(arr[p].getDurationUnit());
				Integer fctr = 1;
				if (durunit.equals("W")) fctr = 7;
				else if (durunit.equals("M")) fctr = 30;

				if (NumberUtils.isDigits(duration)) {
					duration = String.valueOf(Integer.parseInt(duration) * fctr);
					medi.setDuration(duration);
					mSummary = Util.addSummary(mSummary, "Duration", duration + " Day(s)");
				}
			}
			if (StringUtils.filled(arr[p].getQuantity())) {
				medi.setQuantity(arr[p].getQuantity());
				mSummary = Util.addSummary(mSummary, "Quantity", arr[p].getQuantity());
			}
			if (StringUtils.filled(medi.getDrugName()) || StringUtils.filled(medi.getDrugIdentificationNumber())) {
				medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
				mSummary = Util.addSummary(mSummary, "Number of Refills", String.valueOf(arr[p].getRepeat()));
			}
			if (StringUtils.filled(arr[p].getETreatmentType())) {
				medi.setTreatmentType(arr[p].getETreatmentType());
				mSummary = Util.addSummary(mSummary, "Treatment Type", arr[p].getETreatmentType());
			}
			if (arr[p].getRefillDuration() != null) {
				medi.setRefillDuration(String.valueOf(arr[p].getRefillDuration()));
				mSummary = Util.addSummary(mSummary, "Refill Duration", arr[p].getRefillDuration().toString());
			}
			if (arr[p].getRefillQuantity() != null) {
				medi.setRefillQuantity(String.valueOf(arr[p].getRefillQuantity()));
				mSummary = Util.addSummary(mSummary, "Refill Quantity", arr[p].getRefillQuantity().toString());
			}

			medi.addNewLongTermMedication().setBoolean(arr[p].getLongTerm());
			mSummary = Util.addSummary(mSummary, "Long Term Medication", arr[p].getLongTerm() ? "Yes" : "No");

			medi.addNewPastMedications().setBoolean(arr[p].getPastMed());
			mSummary = Util.addSummary(mSummary, "Past Medcation", arr[p].getPastMed() ? "Yes" : "No");

			cdsDt.YnIndicatorAndBlank pc = medi.addNewPatientCompliance();
			if (arr[p].getPatientCompliance() == null) {
				pc.setBlank(cdsDt.Blank.X);
			} else {
				String patientCompliance = arr[p].getPatientCompliance() ? "Yes" : "No";
				pc.setBoolean(arr[p].getPatientCompliance());
				mSummary = Util.addSummary(mSummary, "Patient Compliance", patientCompliance);
			}

			String outsideProviderName = arr[p].getOutsideProviderName();
			if (StringUtils.filled(outsideProviderName)) {
				MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
				String ohip = arr[p].getOutsideProviderOhip();
				if (ohip != null && ohip.trim().length() <= 6) pcb.setOHIPPhysicianId(ohip.trim());
				Util.writeNameSimple(pcb.addNewName(), outsideProviderName);
				mSummary = Util.addSummary(mSummary, "Prescribed by", StringUtils.noNull(outsideProviderName));
			} else {
				String prescribeProvider = arr[p].getProviderNo();
				if (StringUtils.filled(prescribeProvider)) {
					MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
					ProviderData prvd = providerDataDao.findByProviderNo(prescribeProvider);
					String ohip = prvd.getOhipNo();
					if (ohip != null && ohip.trim().length() <= 6) {
						pcb.setOHIPPhysicianId(ohip.trim());
					}
					Util.writeNameSimple(pcb.addNewName(), prvd.getFirstName(), prvd.getLastName());
					mSummary = Util.addSummary(mSummary, "Prescribed by", StringUtils.noNull(prvd.getFirstName()) + " " + StringUtils.noNull(prvd.getLastName()));
				}
			}

			annotation = getNonDumpNote(CaseManagementNoteLink.DRUGS, (long) arr[p].getDrugId(), null);
			if (StringUtils.filled(annotation)) {
				medi.setNotes(annotation);
				mSummary = Util.addSummary(mSummary, "Notes", annotation);
			}

			if (StringUtils.empty(mSummary)) exportErrors.add("Error! No Category Summary Line (Medications & Treatments) for Patient " + demoNo + " (" + (p + 1) + ")");
			medi.setCategorySummaryLine(mSummary);
		}
	}

	private String getDosageUnit(String dosage) {
		String[] dosageBreak = getDosageMultiple1st(dosage).split(" ");

		if (dosageBreak.length == 2) return dosageBreak[1].trim();
		else return null;
	}

	private Float getDosageValue(String dosage) {
		String[] dosageBreak = getDosageMultiple1st(dosage).split(" ");

		if (NumberUtils.isNumber(dosageBreak[0])) return Float.parseFloat(dosageBreak[0]);
		else return 0f;
	}

	private String getNonDumpNote(Integer tableName, Long tableId, String otherId) {
		String note = null;

		List<CaseManagementNoteLink> cmll;
		if (StringUtils.empty(otherId)) cmll = cmm.getLinkByTableIdDesc(tableName, tableId);
		else cmll = cmm.getLinkByTableIdDesc(tableName, tableId, otherId);

		for (CaseManagementNoteLink cml : cmll) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			if (n.getNote() != null && !n.getNote().startsWith("imported.cms4.2011.06")) {//not from dumpsite
				note = n.getNote();
				break;
			}
		}
		return note;
	}

	private String getDosageMultiple1st(String dosage) {
		if (StringUtils.empty(dosage)) return "";

		String[] dosageMultiple = dosage.split("/");
		return dosageMultiple[0].trim();
	}

	public PartialDateDao getPartialDateDao() {
		return partialDateDao;
	}

	public void setPartialDateDao(PartialDateDao partialDateDao) {
		this.partialDateDao = partialDateDao;
	}

	public CaseManagementManager getCmm() {
		return cmm;
	}

	public void setCmm(CaseManagementManager cmm) {
		this.cmm = cmm;
	}

	public List<String> getExportErrors() {
		return exportErrors;
	}

	public void setExportErrors(List<String> exportErrors) {
		this.exportErrors = exportErrors;
	}

}
