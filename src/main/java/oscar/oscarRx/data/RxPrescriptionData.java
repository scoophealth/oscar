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

package oscar.oscarRx.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.FavoriteDao;
import org.oscarehr.common.dao.IndivoDocsDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.IndivoDocs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.util.RxUtil;
import oscar.util.ConversionUtils;
import oscar.util.DateUtils;

public class RxPrescriptionData {

	private static final Logger logger = MiscUtils.getLogger();

	public static String getFullOutLine(String special) {
		String ret = "";
		if (special != null) {
			if (special.length() > 0) {
				int i;
				String[] arr = special.split("\n");
				for (i = 0; i < arr.length; i++) {
					ret += arr[i].trim();
					if (i < arr.length - 1) {
						ret += "; ";
					}
				}
			}
		} else {
			logger.warn("Drugs special field was null, this means nothing will print.");
		}

		return ret;
	}

	public Prescription getPrescription(int drugId) {

		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		Drug drug = drugDao.find(drugId);

		Prescription prescription = new Prescription(drugId, drug.getProviderNo(), drug.getDemographicId());
		prescription.setRxCreatedDate(drug.getCreateDate());
		prescription.setRxDate(drug.getRxDate());
		prescription.setEndDate(drug.getEndDate());
		prescription.setWrittenDate(drug.getWrittenDate());
		prescription.setBrandName(drug.getBrandName());
		prescription.setGCN_SEQNO(drug.getGcnSeqNo());
		prescription.setCustomName(drug.getCustomName());
		prescription.setTakeMin(drug.getTakeMin());
		prescription.setTakeMax(drug.getTakeMax());
		prescription.setFrequencyCode(drug.getFreqCode());
		String dur = drug.getDuration();
		if (StringUtils.isBlank(dur) || dur.equalsIgnoreCase("null")) dur = "";
		prescription.setDuration(dur);
		prescription.setDurationUnit(drug.getDurUnit());
		prescription.setQuantity(drug.getQuantity());
		prescription.setDispensingUnits(drug.getDispensingUnits());
		prescription.setRepeat(drug.getRepeat());
		prescription.setLastRefillDate(drug.getLastRefillDate());
		prescription.setNosubs(drug.isNoSubs());
		prescription.setPrn(drug.isPrn());
		prescription.setSpecial(drug.getSpecial());
		prescription.setGenericName(drug.getGenericName());
		prescription.setAtcCode(drug.getAtc());
		prescription.setScript_no(String.valueOf(drug.getScriptNo()));
		prescription.setRegionalIdentifier(drug.getRegionalIdentifier());
		prescription.setUnit(drug.getUnit());
		prescription.setUnitName(drug.getUnitName());
		prescription.setMethod(drug.getMethod());
		prescription.setRoute(drug.getRoute());
		prescription.setDrugForm(drug.getDrugForm());
		prescription.setCustomInstr(drug.isCustomInstructions());
		prescription.setDosage(drug.getDosage());
		prescription.setLongTerm(drug.isLongTerm());
		prescription.setShortTerm(drug.getShortTerm());
		prescription.setCustomNote(drug.isCustomNote());
		prescription.setPastMed(drug.getPastMed());
		prescription.setDispenseInternal(drug.getDispenseInternal());
		prescription.setStartDateUnknown(drug.getStartDateUnknown());
		prescription.setComment(drug.getComment());
		if (drug.getPatientCompliance() == null) prescription.setPatientCompliance(null);
		else prescription.setPatientCompliance(drug.getPatientCompliance());
		prescription.setOutsideProviderName(drug.getOutsideProviderName());
		prescription.setOutsideProviderOhip(drug.getOutsideProviderOhip());
		prescription.setSpecialInstruction(drug.getSpecialInstruction());
		prescription.setPickupDate(drug.getPickUpDateTime());
		prescription.setPickupTime(drug.getPickUpDateTime());
		prescription.setETreatmentType(drug.getETreatmentType());
		prescription.setRxStatus(drug.getRxStatus());
		if (drug.getDispenseInterval() != null) prescription.setDispenseInterval(drug.getDispenseInterval());
		if (drug.getRefillDuration() != null) prescription.setRefillDuration(drug.getRefillDuration());
		if (drug.getRefillQuantity() != null) prescription.setRefillQuantity(drug.getRefillQuantity());

		if (prescription.getSpecial() == null || prescription.getSpecial().length() <= 6) {
			logger.warn("I strongly suspect something is wrong, either special is null or it appears to not contain anything useful. drugId=" + drugId + ", drug.special=" + prescription.getSpecial());
			logger.warn("data from db is : " + drug.getSpecial());
		}
		prescription.setDispenseInternal(drug.getDispenseInternal());
		return prescription;
	}

	public Prescription newPrescription(String providerNo, int demographicNo) {
		// Create new prescription (only in memory)
		return new Prescription(0, providerNo, demographicNo);
	}

	public Prescription newPrescription(String providerNo, int demographicNo, Favorite favorite) {
		// Create new prescription from favorite (only in memory)
		Prescription prescription = new Prescription(0, providerNo, demographicNo);

		prescription.setRxDate(RxUtil.Today());
		prescription.setWrittenDate(RxUtil.Today());
		prescription.setEndDate(null);
		prescription.setBrandName(favorite.getBN());
		prescription.setGCN_SEQNO(favorite.getGCN_SEQNO());
		prescription.setCustomName(favorite.getCustomName());
		prescription.setTakeMin(favorite.getTakeMin());
		prescription.setTakeMax(favorite.getTakeMax());
		prescription.setFrequencyCode(favorite.getFrequencyCode());
		prescription.setDuration(favorite.getDuration());
		prescription.setDurationUnit(favorite.getDurationUnit());
		prescription.setDispensingUnits(favorite.getDispensingUnits());
		prescription.setQuantity(favorite.getQuantity());
		prescription.setRepeat(favorite.getRepeat());
		prescription.setNosubs(favorite.getNosubs());
		prescription.setPrn(favorite.getPrn());
		prescription.setSpecial(favorite.getSpecial());
		prescription.setGenericName(favorite.getGN());
		prescription.setAtcCode(favorite.getAtcCode());
		prescription.setRegionalIdentifier(favorite.getRegionalIdentifier());
		prescription.setUnit(favorite.getUnit());
		prescription.setUnitName(favorite.getUnitName());
		prescription.setMethod(favorite.getMethod());
		prescription.setRoute(favorite.getRoute());
		prescription.setDrugForm(favorite.getDrugForm());
		prescription.setCustomInstr(favorite.getCustomInstr());
		prescription.setDosage(favorite.getDosage());

		return prescription;
	}

	public Prescription newPrescription(String providerNo, int demographicNo, Prescription rePrescribe) {
		// Create new prescription
		Prescription prescription = new Prescription(0, providerNo, demographicNo);

		prescription.setRxDate(RxUtil.Today());
		prescription.setWrittenDate(RxUtil.Today());
		prescription.setEndDate(null);
		prescription.setBrandName(rePrescribe.getBrandName());
		prescription.setGCN_SEQNO(rePrescribe.getGCN_SEQNO());
		prescription.setCustomName(rePrescribe.getCustomName());
		prescription.setTakeMin(rePrescribe.getTakeMin());
		prescription.setTakeMax(rePrescribe.getTakeMax());
		prescription.setFrequencyCode(rePrescribe.getFrequencyCode());
		prescription.setDuration(rePrescribe.getDuration());
		prescription.setDurationUnit(rePrescribe.getDurationUnit());
		prescription.setDispensingUnits(rePrescribe.getDispensingUnits());
		prescription.setQuantity(rePrescribe.getQuantity());
		prescription.setRepeat(rePrescribe.getRepeat());
		prescription.setLastRefillDate(rePrescribe.getLastRefillDate());
		prescription.setNosubs(rePrescribe.getNosubs());
		prescription.setPrn(rePrescribe.getPrn());
		prescription.setSpecial(rePrescribe.getSpecial());
		prescription.setGenericName(rePrescribe.getGenericName());
		prescription.setAtcCode(rePrescribe.getAtcCode());
		prescription.setScript_no(rePrescribe.getScript_no());
		prescription.setRegionalIdentifier(rePrescribe.getRegionalIdentifier());
		prescription.setUnit(rePrescribe.getUnit());
		prescription.setUnitName(rePrescribe.getUnitName());
		prescription.setMethod(rePrescribe.getMethod());
		prescription.setRoute(rePrescribe.getRoute());
		prescription.setDrugForm(rePrescribe.getDrugForm());
		prescription.setCustomInstr(rePrescribe.getCustomInstr());
		prescription.setDosage(rePrescribe.getDosage());
		prescription.setLongTerm(rePrescribe.getLongTerm());
		prescription.setShortTerm(rePrescribe.getShortTerm());
		prescription.setCustomNote(rePrescribe.isCustomNote());
		prescription.setPastMed(rePrescribe.getPastMed());
		prescription.setDispenseInternal(rePrescribe.isDispenseInternal());
		prescription.setPatientCompliance(rePrescribe.getPatientCompliance());
		prescription.setOutsideProviderName(rePrescribe.getOutsideProviderName());
		prescription.setOutsideProviderOhip(rePrescribe.getOutsideProviderOhip());
		prescription.setSpecialInstruction(rePrescribe.getSpecialInstruction());
		prescription.setPickupDate(rePrescribe.getPickupDate());
		prescription.setPickupTime(rePrescribe.getPickupTime());
		prescription.setETreatmentType(rePrescribe.getETreatmentType());
		prescription.setRxStatus(rePrescribe.getRxStatus());
		if (rePrescribe.getDispenseInterval() != null) prescription.setDispenseInterval(rePrescribe.getDispenseInterval());
		if (rePrescribe.getRefillDuration() != null) prescription.setRefillDuration(rePrescribe.getRefillDuration());
		if (rePrescribe.getRefillQuantity() != null) prescription.setRefillQuantity(rePrescribe.getRefillQuantity());
		prescription.setDrugReferenceId(rePrescribe.getDrugId());
		prescription.setDispenseInternal(rePrescribe.getDispenseInternal());
		return prescription;
	}

	public Prescription[] getPrescriptionsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Drug drug : dao.findByDemographicIdOrderByPosition(demographicNo, null)) {
			Prescription p = toPrescription(drug, demographicNo);
			lst.add(p);
		}

		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription toPrescription(Drug drug, int demographicNo) {
		Prescription p = new Prescription(drug.getId(), drug.getProviderNo(), demographicNo);
		p.setRxCreatedDate(drug.getCreateDate());
		p.setRxDate(drug.getRxDate());
		p.setEndDate(drug.getEndDate());
		p.setWrittenDate(drug.getWrittenDate());
		p.setBrandName(drug.getBrandName());
		p.setGCN_SEQNO(drug.getGcnSeqNo());
		p.setCustomName(drug.getCustomName());
		p.setTakeMin(drug.getTakeMin());
		p.setTakeMax(drug.getTakeMax());
		p.setFrequencyCode(drug.getFreqCode());
		p.setDuration(drug.getDuration());
		p.setDurationUnit(drug.getDuration());
		p.setQuantity(drug.getQuantity());
		p.setDispensingUnits(drug.getDispensingUnits());
		p.setRepeat(drug.getRepeat());
		p.setLastRefillDate(drug.getLastRefillDate());
		p.setNosubs(drug.isNoSubs());
		p.setPrn(drug.isPrn());
		p.setSpecial(drug.getSpecial());
		p.setSpecialInstruction(drug.getSpecialInstruction());
		p.setArchived(String.valueOf(drug.isArchived()));
		p.setGenericName(drug.getGenericName());
		p.setAtcCode(drug.getAtc());
		p.setScript_no(ConversionUtils.toIntString(drug.getScriptNo()));
		p.setRegionalIdentifier(drug.getRegionalIdentifier());
		p.setUnit(drug.getUnit());
		p.setUnitName(drug.getUnitName());
		p.setMethod(drug.getMethod());
		p.setRoute(drug.getRoute());
		p.setDrugForm(drug.getDrugForm());
		p.setCustomInstr(drug.isCustomInstructions());
		p.setDosage(drug.getDosage());
		p.setLongTerm(drug.getLongTerm());
		p.setShortTerm(drug.getShortTerm());
		p.setCustomNote(drug.isCustomNote());
		p.setPastMed(drug.getPastMed());
		p.setStartDateUnknown(drug.getStartDateUnknown());
		p.setComment(drug.getComment());
		if (drug.getPatientCompliance() == null) p.setPatientCompliance(null);
		else p.setPatientCompliance(drug.getPatientCompliance());
		p.setOutsideProviderName(drug.getOutsideProviderName());
		p.setOutsideProviderOhip(drug.getOutsideProviderOhip());
		p.setPickupDate(drug.getPickUpDateTime());
		p.setPickupTime(drug.getPickUpDateTime());
		p.setETreatmentType(drug.getETreatmentType());
		p.setRxStatus(drug.getRxStatus());
		if (drug.getDispenseInterval() != null) p.setDispenseInterval(drug.getDispenseInterval());
		if (drug.getRefillDuration() != null) p.setRefillDuration(drug.getRefillDuration());
		if (drug.getRefillQuantity() != null) p.setRefillQuantity(drug.getRefillQuantity());
		p.setHideCpp(drug.getHideFromCpp());
		return p;
	}

	public Prescription[] getPrescriptionScriptsByPatientATC(int demographicNo, String atc) {
		List<Prescription> lst = new ArrayList<Prescription>();

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Drug drug : dao.findByDemographicIdAndAtc(demographicNo, atc))
			lst.add(toPrescription(drug, demographicNo));

		return lst.toArray(new Prescription[lst.size()]);
	}

	// do not return customed drugs
	public Prescription[] getPrescriptionScriptsByPatientRegionalIdentifier(int demographicNo, String regionalIdentifier) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicIdAndRegion(demographicNo, regionalIdentifier))
			lst.add(toPrescription(drug, demographicNo));
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription getLatestPrescriptionScriptByPatientDrugId(int demographicNo, String drugId) {
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		List<Drug> drugs = dao.findByDemographicIdAndDrugId(demographicNo, Integer.parseInt(drugId));
		if (drugs.isEmpty()) return null;
		return toPrescription(drugs.get(0), demographicNo);
	}

	/*
	 * Limit returned prescriptions to those which have an entry in both drugs and prescription table
	 */
	public Prescription[] getPrescriptionScriptsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Object[] pair : dao.findDrugsAndPrescriptions(demographicNo)) {
			Drug drug = (Drug) pair[0];
			org.oscarehr.common.model.Prescription rx = (org.oscarehr.common.model.Prescription) pair[1];
			MiscUtils.getLogger().debug("Looking at drug " + drug + " and rx " + rx);
			lst.add(toPrescription(demographicNo, drug, rx));
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	private Prescription toPrescription(int demographicNo, Drug drug, org.oscarehr.common.model.Prescription rx) {
		Prescription prescription = toPrescription(drug, demographicNo);
		if (!rx.isReprinted()) prescription.setNumPrints(1);
		else prescription.setNumPrints(rx.getReprintCount() + 1);

		prescription.setPrintDate(rx.getDatePrinted());
		prescription.setDatesReprinted(rx.getDatesReprinted());
		return prescription;
	}

	public List<Prescription> getPrescriptionsByScriptNo(int script_no, int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Object[] pair : dao.findDrugsAndPrescriptionsByScriptNumber(script_no)) {
			Drug drug = (Drug) pair[0];
			org.oscarehr.common.model.Prescription rx = (org.oscarehr.common.model.Prescription) pair[1];

			lst.add(toPrescription(demographicNo, drug, rx));
		}
		return lst;
	}

	public Prescription[] getPrescriptionsByPatientHideDeleted(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicId(demographicNo)) {
			if ((drug.isCurrent() && !drug.isArchived() && !drug.isDeleted() && !drug.isDiscontinued()) || drug.isLongTerm()) {
				lst.add(toPrescription(drug, demographicNo));
			}
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription[] getActivePrescriptionsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicId(demographicNo)) {
			Prescription p = toPrescription(drug, demographicNo);
			if (!p.isArchived() && !p.isDiscontinued() && p.isCurrent()) {
				lst.add(p);
			}
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Vector getCurrentATCCodesByPatient(int demographicNo) {
		List<String> result = new ArrayList<String>();

		Prescription[] p = getPrescriptionsByPatientHideDeleted(demographicNo);
		for (int i = 0; i < p.length; i++) {
			if (p[i].isCurrent()) {
				if (!result.contains(p[i].getAtcCode())) {
					if (p[i].isValidAtcCode()) result.add(p[i].getAtcCode());
				}
			}
		}
		return new Vector(result);
	}

	public List<String> getCurrentRegionalIdentifiersCodesByPatient(int demographicNo) {
		List<String> result = new ArrayList<String>();

		Prescription[] p = getPrescriptionsByPatientHideDeleted(demographicNo);
		for (int i = 0; i < p.length; i++) {
			if (p[i].isCurrent()) {
				if (!result.contains(p[i].getRegionalIdentifier())) {
					if (p[i].getRegionalIdentifier() != null && p[i].getRegionalIdentifier().trim().length() != 0) {
						result.add(p[i].getRegionalIdentifier());
					}
				}
			}
		}
		return result;
	}

	public Prescription[] getUniquePrescriptionsByPatient(int demographicNo) {
		List<Prescription> result = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		List<Drug> drugList = dao.findByDemographicId(demographicNo);

		Collections.sort(drugList, new Drug.ComparatorIdDesc());

		for (Drug drug : drugList) {

			boolean isCustomName = true;

			for (Prescription p : result) {
				if (p.getGCN_SEQNO() == drug.getGcnSeqNo()) {
					if (p.getGCN_SEQNO() != 0) // not custom - safe GCN
					isCustomName = false;
					else if (p.getCustomName() != null && drug.getCustomName() != null) // custom
					    isCustomName = !p.getCustomName().equals(drug.getCustomName());

				}
			}

			if (isCustomName) {
				logger.debug("ADDING PRESCRIPTION " + drug.getId());
				Prescription p = toPrescription(drug, demographicNo);

				IndivoDocsDao iDao = SpringUtils.getBean(IndivoDocsDao.class);
				IndivoDocs iDoc = iDao.findByOscarDocNo(drug.getId());
				if (iDoc != null) {
					p.setIndivoIdx(iDoc.getIndivoDocIdx());
					if (p.getIndivoIdx() != null && p.getIndivoIdx().length() > 0) p.setRegisterIndivo();
				}
				
				p.setPosition(drug.getPosition());
				result.add(p);
			}
		}
		return result.toArray(new Prescription[result.size()]);
	}

	public Favorite[] getFavorites(String providerNo) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);

		List<Favorite> result = new ArrayList<Favorite>();

		for (org.oscarehr.common.model.Favorite f : dao.findByProviderNo(providerNo))
			result.add(toFavorite(f));

		return result.toArray(new Favorite[result.size()]);
	}

	private Favorite toFavorite(org.oscarehr.common.model.Favorite f) {
		Favorite result = new Favorite(f.getId(), f.getProviderNo(), f.getName(), f.getBn(), (int) f.getGcnSeqno(), f.getCustomName(), f.getTakeMin(), f.getTakeMax(), f.getFrequencyCode(), f.getDuration(), f.getDurationUnit(), f.getQuantity(), f.getDispensingUnits(), f.getRepeat(), f.isNosubs(), f.isPrn(), f.getSpecial(), f.getGn(), f.getAtc(), f.getRegionalIdentifier(), f.getUnit(), f.getUnitName(), f.getMethod(), f.getRoute(), f.getDrugForm(), f.isCustomInstructions(), f.getDosage());
		return result;
	}

	public Favorite getFavorite(int favoriteId) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
		org.oscarehr.common.model.Favorite result = dao.find(favoriteId);
		if (result == null) return null;
		return toFavorite(result);
	}

	public boolean deleteFavorite(int favoriteId) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
		return dao.remove(favoriteId);
	}

	/**
	 * This function is used to save a set of prescribed drugs to as one prescription. This is for historical purposes
	 *
	 * @param bean This is the oscarRx session bean
	 * @return This returns the insert id of the script to be included the drugs table
	 */
	public String saveScript(LoggedInInfo loggedInInfo, oscar.oscarRx.pageUtil.RxSessionBean bean) {
		/*
		 * create table prescription ( script_no int(10) auto_increment primary key, provider_no varchar(6), demographic_no int(10), date_prescribed date, date_printed date, dates_reprinted text, textView text);
		 */
		String provider_no = bean.getProviderNo();
		int demographic_no = bean.getDemographicNo();

		Date today = oscar.oscarRx.util.RxUtil.Today();
		//String date_prescribed = oscar.oscarRx.util.RxUtil.DateToString(today, "yyyy/MM/dd");
		//String date_printed = date_prescribed;

		StringBuilder textView = new StringBuilder();

		// ///create full text view
		oscar.oscarRx.data.RxPatientData.Patient patient = null;
		oscar.oscarRx.data.RxProviderData.Provider provider = null;
		try {
			patient = RxPatientData.getPatient(loggedInInfo, demographic_no);
			provider = new oscar.oscarRx.data.RxProviderData().getProvider(provider_no);
		} catch (Exception e) {
			logger.error("unexpected error", e);
		}
		ProSignatureData sig = new ProSignatureData();
		boolean hasSig = sig.hasSignature(bean.getProviderNo());
		String doctorName = "";
		if (hasSig) {
			doctorName = sig.getSignature(bean.getProviderNo());
		} else {
			doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
		}

		textView.append(doctorName + "\n");
		textView.append(provider.getClinicName() + "\n");
		textView.append(provider.getClinicAddress() + "\n");
		textView.append(provider.getClinicCity() + "\n");
		textView.append(provider.getClinicPostal() + "\n");
		textView.append(provider.getClinicPhone() + "\n");
		textView.append(provider.getClinicFax() + "\n");
		textView.append(patient.getFirstName() + " " + patient.getSurname() + "\n");
		textView.append(patient.getAddress() + "\n");
		textView.append(patient.getCity() + " " + patient.getPostal() + "\n");
		textView.append(patient.getPhone() + "\n");
		textView.append(oscar.oscarRx.util.RxUtil.DateToString(today, "MMMM d, yyyy") + "\n");

		String txt;
		for (int i = 0; i < bean.getStashSize(); i++) {
			Prescription rx = bean.getStashItem(i);

			String fullOutLine = rx.getFullOutLine();
			if (fullOutLine == null || fullOutLine.length() < 6) {
				logger.warn("Drug full outline appears to be null or empty : " + fullOutLine);
			}

			txt = fullOutLine.replaceAll(";", "\n");
			textView.append("\n" + txt);
		}
		// textView.append();

		org.oscarehr.common.model.Prescription rx = new org.oscarehr.common.model.Prescription();
		rx.setProviderNo(provider_no);
		rx.setDemographicId(demographic_no);
		rx.setDatePrescribed(today);
		rx.setDatePrinted(today);
		rx.setTextView(textView.toString());

		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		dao.persist(rx);
		return rx.getId().toString();
	}

	public int setScriptComment(String scriptNo, String comment) {
		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		return dao.updatePrescriptionsByScriptNo(Integer.parseInt(scriptNo), comment);
	}

	public String getScriptComment(String scriptNo) {
		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		org.oscarehr.common.model.Prescription p = dao.find(ConversionUtils.fromIntString(scriptNo));
		if (p == null) return null;

		return p.getComments();
	}

	// erased an orfin }
	public static class Prescription {

		int drugId;
		String providerNo;
		int demographicNo;
		long randomId = 0;
		java.util.Date rxCreatedDate = null;
		java.util.Date rxDate = null;
		java.util.Date endDate = null;
		java.util.Date pickupDate = null;
		java.util.Date pickupTime = null;
		java.util.Date writtenDate = null;
		String writtenDateFormat = null;
		java.util.Date printDate = null;
		int numPrints = 0;
		String BN = null; // regular
		int GCN_SEQNO = 0; // regular
		String customName = null; // custom
		float takeMin = 0;
		float takeMax = 0;
		String frequencyCode = null;
		String duration = null;
		String durationUnit = null;
		String quantity = null;
		String dispensingUnits = null;
		int repeat = 0;
		java.util.Date lastRefillDate = null;
		boolean nosubs = false;
		boolean prn = false;
		boolean longTerm = false;
		boolean shortTerm = false;
		boolean pastMed = false;
		boolean startDateUnknown = false;
		Boolean patientCompliance = null;
		String special = null;
		String genericName = null;
		boolean archived = false; // ADDED BY JAY DEC 3 2002
		String atcCode = null;
		String script_no = null;
		String regionalIdentifier = null;
		String method = null;
		String unit = null;
		String unitName = null;
		String route = null;
		String drugForm = null;
		String dosage = null;
		String outsideProviderName = null;
		String outsideProviderOhip = null;
		boolean custom = false;
		private String indivoIdx = null; // indivo document index for this prescription
		private boolean registerIndivo = false;
		private final String docType = "Rx";
		private boolean discontinued = false;//indicate if the rx has isDisontinued before.
		private String lastArchDate = null;
		private String lastArchReason = null;
		private Date archivedDate;
		private boolean discontinuedLatest = false;
		String special_instruction = null;
		private boolean durationSpecifiedByUser = false;
		private boolean customNote = false;
		boolean nonAuthoritative = false;
		String eTreatmentType = null;
		private boolean hideCpp = false;
		String rxStatus = null;
		private Integer refillDuration = 0;
		private Integer refillQuantity = 0;
		private Integer dispenseInterval = 0;
		private int position = 0;
		private String comment = null;

		private String drugFormList = "";
		private String datesReprinted = "";
		private boolean dispenseInternal = false;

		private List<String> policyViolations = new ArrayList<String>();

		private int drugReferenceId;
		
		private String drugReasonCode;
		private String drugReasonCodeSystem;

		public String getDrugReasonCode() {
			return drugReasonCode;
		}

		public void setDrugReasonCode(String drugReasonCode) {
			this.drugReasonCode = drugReasonCode;
		}

		public String getDrugReasonCodeSystem() {
			return drugReasonCodeSystem;
		}

		public void setDrugReasonCodeSystem(String drugReasonCodeSystem) {
			this.drugReasonCodeSystem = drugReasonCodeSystem;
		}

		public List<String> getPolicyViolations() {
			return policyViolations;
		}

		public void setPolicyViolations(List<String> policyViolations) {
			this.policyViolations = policyViolations;
		}

		public void setDrugReferenceId(int drugId2) {
			this.drugReferenceId = drugId2;

		}

		public int getDrugReferenceId() {
			return drugReferenceId;
		}

		public boolean getStartDateUnknown() {
			return startDateUnknown;
		}

		public void setStartDateUnknown(boolean startDateUnknown) {
			this.startDateUnknown = startDateUnknown;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getDatesReprinted() {
			return datesReprinted;
		}

		public void setDatesReprinted(String datesReprinted) {
			this.datesReprinted = datesReprinted;
		}

		public String getDrugFormList() {
			return drugFormList;
		}

		public void setDrugFormList(String drugFormList) {
			this.drugFormList = drugFormList;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public boolean isHideCpp() {
			return hideCpp;
		}

		public void setHideCpp(boolean hideCpp) {
			this.hideCpp = hideCpp;
		}

		public String getETreatmentType() {
			return eTreatmentType;
		}

		public void setETreatmentType(String treatmentType) {
			eTreatmentType = treatmentType;
		}

		public String getRxStatus() {
			return rxStatus;
		}

		public void setRxStatus(String status) {
			rxStatus = status;
		}

		public boolean isCustomNote() {
			return customNote;
		}

		public void setCustomNote(boolean b) {
			customNote = b;
		}

		public boolean isMitte() {
			if (unitName != null && (unitName.equalsIgnoreCase("D") || unitName.equalsIgnoreCase("W") || unitName.equalsIgnoreCase("M") || unitName.equalsIgnoreCase("day") || unitName.equalsIgnoreCase("week") || unitName.equalsIgnoreCase("month") || unitName.equalsIgnoreCase("days") || unitName.equalsIgnoreCase("weeks") || unitName.equalsIgnoreCase("months") || unitName.equalsIgnoreCase("mo"))) return true;
			else return false;
		}

		public boolean isDurationSpecifiedByUser() {
			return durationSpecifiedByUser;
		}

		public void setDurationSpecifiedByUser(boolean b) {
			this.durationSpecifiedByUser = b;
		}

		public String getSpecialInstruction() {
			return special_instruction;
		}

		public void setSpecialInstruction(String s) {
			special_instruction = s;
		}

		public boolean isLongTerm() {
			return longTerm;
		}

		public boolean isDiscontinuedLatest() {
			return this.discontinuedLatest;
		}

		public void setDiscontinuedLatest(boolean dl) {
			this.discontinuedLatest = dl;
		}

		public String getLastArchDate() {
			return this.lastArchDate;
		}

		public void setLastArchDate(String lastArchDate) {
			this.lastArchDate = lastArchDate;
		}

		public String getLastArchReason() {
			return this.lastArchReason;
		}

		public void setLastArchReason(String lastArchReason) {
			this.lastArchReason = lastArchReason;
		}

		public boolean isDiscontinued() {
			return this.discontinued;
		}

		public void setDiscontinued(boolean discon) {
			this.discontinued = discon;
		}

		public void setArchivedDate(Date ad) {
			this.archivedDate = ad;
		}

		public Date getArchivedDate() {
			return this.archivedDate;
		}

		// RxDrugData.GCN gcn = null;
		public Prescription(int drugId, String providerNo, int demographicNo) {
			this.drugId = drugId;
			this.providerNo = providerNo;
			this.demographicNo = demographicNo;
		}

		public long getRandomId() {
			return this.randomId;
		}

		public void setRandomId(long randomId) {
			this.randomId = randomId;
		}

		public int getNumPrints() {
			return this.numPrints;
		}

		public void setNumPrints(int numPrints) {
			this.numPrints = numPrints;
		}

		public java.util.Date getPrintDate() {
			return this.printDate;
		}

		public void setPrintDate(java.util.Date printDate) {
			this.printDate = printDate;
		}

		public void setScript_no(String script_no) {
			this.script_no = script_no;
		}

		public String getScript_no() {
			return this.script_no;
		}

		public void setIndivoIdx(String idx) {
			indivoIdx = idx;
		}

		public String getIndivoIdx() {
			return indivoIdx;
		}

		public void setRegisterIndivo() {
			registerIndivo = true;
		}

		public boolean isRegisteredIndivo() {
			return registerIndivo;
		}

		public String getGenericName() {
			return genericName;
		}

		public void setGenericName(String genericName) {
			this.genericName = genericName;
		}

		// ADDED BY JAY DEC 03 2002
		public boolean isArchived() {
			return this.archived;
		}

		public void setArchived(String tf) {
			this.archived = Boolean.parseBoolean(tf);
		}

		//////////////////////////////
		public int getDrugId() {
			return this.drugId;
		}

		public String getProviderNo() {
			return this.providerNo;
		}

		public int getDemographicNo() {
			return this.demographicNo;
		}

		public java.util.Date getRxDate() {
			return this.rxDate;
		}

		public void setRxDate(java.util.Date RHS) {
			this.rxDate = RHS;
		}

		public java.util.Date getPickupDate() {
			return this.pickupDate;
		}

		public void setPickupDate(java.util.Date RHS) {
			this.pickupDate = RHS;
		}

		public java.util.Date getPickupTime() {
			return this.pickupTime;
		}

		public void setPickupTime(java.util.Date RHS) {
			this.pickupTime = RHS;
		}

		public java.util.Date getEndDate() {
			if (this.isDiscontinued()) return this.archivedDate;
			else return this.endDate;
		}

		public void setEndDate(java.util.Date RHS) {
			this.endDate = RHS;
		}

		public java.util.Date getWrittenDate() {
			return this.writtenDate;
		}

		public void setWrittenDate(java.util.Date RHS) {
			this.writtenDate = RHS;
		}

		public String getWrittenDateFormat() {
			return this.writtenDateFormat;
		}

		public void setWrittenDateFormat(String RHS) {
			this.writtenDateFormat = RHS;
		}

		/*
		 * Current should contain non-expired drugs, as well as long terms drugs that are not deleted/discontinued
		 */
		public boolean isCurrent() {
			if(isLongTerm() && !isDiscontinued() && !isArchived()) {
				return true;
			}
			boolean b = false;

			try {
				GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
				cal.add(GregorianCalendar.DATE, -1);

				if (this.getEndDate().after(cal.getTime())) {
					b = true;
				}
			} catch (Exception e) {
				b = false;
			}

			return b;
		}
		
		private boolean isMethadoneOrSuboxone() {
        	if (customName != null && (customName.toLowerCase().contains("methadone")
        			||customName.toLowerCase().contains("suboxone")
        			||customName.toLowerCase().contains("buprenorphine"))) {
        		return true;
        	}
        	
        	if (BN != null && (BN.toLowerCase().contains("methadone")
        			|| BN.toLowerCase().contains("suboxone")
        			|| BN.toLowerCase().contains("buprenorphine"))) {
        		return true;
        	}
        	
        	return false;
        }
		
		public void calcEndDate() {
			
			if (oscar.OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")
        			&& (isMethadoneOrSuboxone())) {
        		if (this.endDate == null) {
        			this.endDate = this.rxDate;
        		}
        		return;
        	}
			
			try {
				GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
				int days = 0;

				//          p("this.getRxDate()",this.getRxDate().toString());
				cal.setTime(this.getRxDate());

				if (this.getDuration() != null && this.getDuration().length() > 0) {
					if (Integer.parseInt(this.getDuration()) > 0) {
						int i = Integer.parseInt(this.getDuration());
						//      p("i",Integer.toString(i));
						//      p("this.getDurationUnit()",this.getDurationUnit());
						if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("D")) {
							days = i;
						}
						if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("W")) {
							days = i * 7;
						}
						if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("M")) {
							days = i * 30;
						}

						if (this.getRepeat() > 0) {
							int r = this.getRepeat();

							r++; // if we have a repeat of 1, multiply days by 2

							days = days * r;
						}
						//    p("days",Integer.toString(days));
						if (days > 0) {
							cal.add(GregorianCalendar.DATE, days);
						}
					}
				}

				this.endDate = cal.getTime();
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
			//     p("endDate",RxUtil.DateToString(this.endDate));
		}

		public String getBrandName() {
			return this.BN;
		}

		public void setBrandName(String RHS) {
			this.BN = RHS;
			// this.gcn=null;
		}

		public int getGCN_SEQNO() {
			return this.GCN_SEQNO;
		}

		public void setGCN_SEQNO(int RHS) {
			this.GCN_SEQNO = RHS;
			// this.gcn=null;
		}

		/*
		 * public RxDrugData.GCN getGCN() { if (this.gcn==null) { this.gcn = new RxDrugData().getGCN(this.BN, this.GCN_SEQNO); }
		 *
		 * return gcn; }
		 */
		public boolean isCustom() {
			boolean b = false;

			if (this.customName != null) {
				b = true;
			} else if (this.GCN_SEQNO == 0) {
				b = true;
			}
			return b;
		}

		public String getCustomName() {
			return this.customName;
		}

		public void setCustomName(String RHS) {
			this.customName = RHS;
			if (this.customName != null) {
				if (this.customName.equalsIgnoreCase("null") || this.customName.equalsIgnoreCase("")) {
					this.customName = null;
				}
			}
		}

		public float getTakeMin() {
			return this.takeMin;
		}

		public void setTakeMin(float RHS) {
			this.takeMin = RHS;
		}

		public String getTakeMinString() {
			return RxUtil.FloatToString(this.takeMin);
		}

		public float getTakeMax() {
			return this.takeMax;
		}

		public void setTakeMax(float RHS) {
			this.takeMax = RHS;
		}

		public String getTakeMaxString() {
			return RxUtil.FloatToString(this.takeMax);
		}

		public String getFrequencyCode() {
			return this.frequencyCode;
		}

		public void setFrequencyCode(String RHS) {
			this.frequencyCode = RHS;
		}

		public String getDuration() {
			return this.duration;
		}

		public void setDuration(String RHS) {
			this.duration = RHS;
		}

		public String getDurationUnit() {
			return this.durationUnit;
		}

		public void setDurationUnit(String RHS) {
			this.durationUnit = RHS;
		}

		public String getQuantity() {
			if (this.quantity == null) {
				this.quantity = "";
			}
			return this.quantity;
		}

		public void setQuantity(String RHS) {
			if (RHS == null || RHS.equals("null") || RHS.length() < 1) {
				this.quantity = "0";
			} else {
				this.quantity = RHS;
			}
		}

		public String getDispensingUnits() {
			if (this.dispensingUnits == null) {
				this.dispensingUnits = "";
			}
			return this.dispensingUnits;
		}

		public void setDispensingUnits(String RHS) {
			this.dispensingUnits = RHS;
		}

		public int getRepeat() {
			return this.repeat;
		}

		public void setRepeat(int RHS) {
			this.repeat = RHS;
		}

		public java.util.Date getLastRefillDate() {
			return this.lastRefillDate;
		}

		public void setLastRefillDate(java.util.Date RHS) {
			this.lastRefillDate = RHS;
		}

		public boolean getNosubs() {
			return this.nosubs;
		}

		public int getNosubsInt() {
			if (this.getNosubs() == true) {
				return 1;
			} else {
				return 0;
			}
		}

		public void setNosubs(boolean RHS) {
			this.nosubs = RHS;
		}

		public void setNosubs(int RHS) {
			if (RHS == 0) {
				this.setNosubs(false);
			} else {
				this.setNosubs(true);
			}
		}

		public boolean isPrn() {//conventional name for getter of boolean variable
			return this.prn;
		}

		public boolean getPrn() {
			return this.prn;
		}

		public int getPrnInt() {
			if (this.getPrn() == true) {
				return 1;
			} else {
				return 0;
			}
		}

		public void setPrn(boolean RHS) {
			this.prn = RHS;
		}

		public void setPrn(int RHS) {
			if (RHS == 0) {
				this.setPrn(false);
			} else {
				this.setPrn(true);
			}
		}

		public boolean getLongTerm() {
			return this.longTerm;
		}

		public void setLongTerm(boolean lt) {
			this.longTerm = lt;
		}
		
		public boolean getShortTerm() {
			return this.shortTerm;
		}

		public void setShortTerm(boolean st) {
			this.shortTerm = st;
		}

		public void setNonAuthoritative(boolean nonAuthoritative) {
			this.nonAuthoritative = nonAuthoritative;
		}

		public boolean isNonAuthoritative() {
			return this.nonAuthoritative;
		}

		public boolean isPastMed() {
			return this.pastMed;
		}

		public boolean getPastMed() {
			return this.pastMed;
		}

		public void setPastMed(boolean pm) {
			this.pastMed = pm;
		}

		public boolean getDispenseInternal() {
			return isDispenseInternal();
		}

		public boolean isDispenseInternal() {
			return dispenseInternal;
		}

		public void setDispenseInternal(boolean dispenseInternal) {
			this.dispenseInternal = dispenseInternal;
		}

		public Boolean getPatientCompliance() {
			return this.patientCompliance;
		}

		public void setPatientCompliance(Boolean pc) {
			this.patientCompliance = pc;
		}

		public boolean getPatientCompliance(String YN) {
			if (YN == null || getPatientCompliance() == null) return false;

			if (YN.equalsIgnoreCase("Y")) return getPatientCompliance().equals(true);
			if (YN.equalsIgnoreCase("N")) return getPatientCompliance().equals(false);
			return false;
		}

		public void setPatientCompliance(boolean Y, boolean N) {
			if (Y) setPatientCompliance(true);
			else if (N) setPatientCompliance(false);
			else setPatientCompliance(null);
		}

		public String getSpecial() {

			//if (special == null || special.length() < 6) {
			if (special == null || special.length() < 4) {
				// the reason this is here is because Tomislav/Caisi was having massive problems tracking down
				// drugs that randomly go missing in prescriptions, like a list of 20 drugs and 3 would be missing on the prescription.
				// it was tracked down to some code which required a special, but we couldn't figure out why a special was required or missing.
				// so now we have code to log an error when a drug is missing a special, we still don't know why it's required or missing
				// but at least we know which drug does it.
				logger.warn("Some one is retrieving the drug special but it appears to be blank : " + special);
			}

			return special;
		}

		public String getOutsideProviderName() {
			return this.outsideProviderName;
		}

		public void setOutsideProviderName(String outsideProviderName) {
			this.outsideProviderName = outsideProviderName;
		}

		public String getOutsideProviderOhip() {
			return this.outsideProviderOhip;
		}

		public void setOutsideProviderOhip(String outsideProviderOhip) {
			this.outsideProviderOhip = outsideProviderOhip;
		}

		public void setSpecial(String RHS) {

			//if (RHS == null || RHS.length() < 6) {
			if (RHS == null || RHS.length() < 4) {
				logger.warn("Some one is setting the drug special but it appears to be blank : " + special);
			}

			if (RHS != null) {
				if (!RHS.equals("null")) {
					special = RHS;
				} else {
					special = null;
				}
			} else {
				special = null;
			}

			//if (special == null || special.length() < 6) {
			if (special == null || special.length() < 4) {
				logger.warn("after processing the drug special but it appears to be blank : " + special);
			}
		}

		public String getSpecialDisplay() {
			String ret = "";

			String s = this.getSpecial();

			if (s != null) {
				if (s.length() > 0) {
					ret = "<br>";

					int i;
					String[] arr = s.split("\n");

					for (i = 0; i < arr.length; i++) {
						ret += arr[i].trim();
						if (i < arr.length - 1) {
							ret += "; ";
						}
					}
				}
			}

			return ret;
		}

		//function used for passing data to LogAction; maps to data column in log table
		public String getAuditString() {
			return getFullOutLine();
		}

		public String getFullOutLine() {
			String fullOutLine = (RxPrescriptionData.getFullOutLine(getSpecial()));
        	if (oscar.OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")) {
        		// parse out comment
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		String custName = getCustomName();
        		String brandName = getBrandName();
        		StringBuilder sb = new StringBuilder();
        		int flag = 1;
        		if ((custName != null && custName.toLowerCase().contains("methadone")) ||
       				 (brandName != null && brandName.toLowerCase().contains("methadone"))) {
        			flag = 2;
        		} else if ((custName != null && (custName.toLowerCase().contains("suboxone") || custName.toLowerCase().contains("buprenorphine"))) ||
	       				 (brandName != null && (brandName.toLowerCase().contains("suboxone")  || brandName.toLowerCase().contains("buprenorphine")))){
        			flag = 3;
        		}
        		
        		if (flag != 1) {
        			sb.append(";;Start Date:" + sdf.format(getRxDate()));
        			sb.append("    ");
        			sb.append("End Date:" + sdf.format(getEndDate()==null?RxUtil.Today():getEndDate()));
        			sb.append(";");
        			
        			String cmt = getComment();
        			if (cmt == null) {
        				cmt = "";
        			}
        			String cmtArr[] = cmt.split(";");
        			if (flag == 2) {
        				sb.append("Drink observed in the pharmacy on days:;");
        			} else {
        				sb.append("Dose observed in the pharmacy on days:;");
        			}
        			if (cmtArr.length > 0 && cmtArr[0] != null) {
        				sb.append(cmtArr[0]);
        			}
        			sb.append(";");
        			sb.append("The following days are to be dispensed as take home doses:;");
        			if (cmtArr.length > 1 && cmtArr[1] != null) {
        				sb.append(cmtArr[1]);
        			}
        			sb.append(";");
        			sb.append("Hold prescription if 3 consecutive doses missed or ");
        			/*
        			String dose = getDosage();
        			if (dose == null) {
        				dose = "";
        			}
        			sb.append("if dosage change exceeds " + dose + " from previous prescription.;");
        			*/
        			if(flag == 2) {
        				sb.append("if dosage change exceeds 15mg from previous prescription.;");
        			} else {
        				sb.append("if dosage change exceeds 4mg from previous prescription.;");
        			}
        			sb.append("Notify physician if dose is missed.");
        			fullOutLine += sb.toString();
        		}
        	}
       		return fullOutLine;
		}

		public String getDosageDisplay() {
			String ret = "";
			if(!(Math.abs(this.getTakeMin() - this.getTakeMax()) <  0.00000001 )) {
				ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
			} else {
				ret += this.getTakeMinString();
			}
			return ret;
		}

		public String getFreqDisplay() {
			String ret = this.getFrequencyCode();
			if (this.getPrn()) {
				ret += " PRN ";
			}
			return ret;
		}

		public String getRxDisplay() {
			try {
				String ret;

				if (this.isCustom()) {
					if (this.customName != null) {
						ret = this.customName + " ";
					} else {
						ret = "Unknown ";
					}
				} else {
					// RxDrugData.GCN gcn = this.getGCN();

					ret = this.getBrandName() + " "; // gcn.getBrandName() + " ";
					// + gcn.getStrength() + " "
					// + gcn.getDoseForm() + " "
					// + gcn.getRoute() + " ";
				}

				if(!(Math.abs(this.getTakeMin() - this.getTakeMax()) <  0.00000001 )) {
					ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
				} else {
					ret += this.getTakeMinString();
				}

				ret += " " + this.getFrequencyCode();

				if (this.getPrn()) {
					ret += " PRN ";
				}
				ret += " " + this.getDuration() + " ";

				if (getDurationUnit() != null && this.getDurationUnit().equals("D")) {
					ret += "Day";
				}
				if (getDurationUnit() != null && this.getDurationUnit().equals("W")) {
					ret += "Week";
				}
				if (getDurationUnit() != null && this.getDurationUnit().equals("M")) {
					ret += "Month";
				}

				try {
					if (this.getDuration() != null && this.getDuration().trim().length() == 0) {
						this.setDuration("0");
					}
					if (this.getDuration() != null && !this.getDuration().equalsIgnoreCase("null") && Integer.parseInt(this.getDuration()) > 1) {
						ret += "s";
					}
				} catch (Exception durationCalcException) {
					logger.error("Error with duration:", durationCalcException);
				}
				ret += "  ";
				ret += this.getQuantity();
				ret += " " + this.getDispensingUnits();
				ret += " Qty  Repeats: ";
				ret += String.valueOf(this.getRepeat());

				if (this.getNosubs()) {
					ret += " No subs ";
				}

				return ret;
			} catch (Exception e) {
				logger.error("unexpected error", e);
				return null;
			}
		}

		public String getDrugName() {
			String ret;
			if (this.isCustom()) {
				if (this.customName != null) {
					ret = this.customName + " ";
				} else {
					ret = "Unknown ";
				}
			} else {
				ret = this.getBrandName() + " ";
			}
			return ret;
		}

		public String getFullFrequency() {
			String ret = "";
			if(!(Math.abs(this.getTakeMin() - this.getTakeMax()) <  0.00000001 )) {
				ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
			} else {
				ret += this.getTakeMinString();
			}

			ret += " " + this.getFrequencyCode();
			return ret;
		}

		public String getFullDuration() {
			String ret = this.getDuration() + " ";
			if (this.getDurationUnit().equals("D")) {
				ret += "Day";
			}
			if (this.getDurationUnit().equals("W")) {
				ret += "Week";
			}
			if (this.getDurationUnit().equals("M")) {
				ret += "Month";
			}

			if (Integer.parseInt(this.getDuration()) > 1) {
				ret += "s";
			}
			return ret;
		}

		public void Delete() {
			try {
				DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
				Drug drug = drugDao.find(getDrugId());
				if (drug != null) {
					drug.setArchived(true);
					drugDao.merge(drug);
				}
			} catch (Exception e) {
				logger.error("unexpected error", e);
			}
		}

		public boolean Save() {
			return Save(null);
		}

		public boolean registerIndivo() {
			IndivoDocs doc = new IndivoDocs();
			doc.setOscarDocNo(getDrugId());
			doc.setIndivoDocIdx(getIndivoIdx());
			doc.setDocType(docType);
			doc.setDateSent(new Date());
			doc.setUpdate(isRegisteredIndivo() ? "U" : "I");

			IndivoDocsDao dao = SpringUtils.getBean(IndivoDocsDao.class);
			dao.persist(doc);
			return true;
		}

		public boolean Print(LoggedInInfo loggedInInfo) {
			PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
			org.oscarehr.common.model.Prescription p = dao.find(ConversionUtils.fromIntString(getScript_no()));
			String providerNo = loggedInInfo.getLoggedInProviderNo();

			if (p == null) return false;

			String dates_reprinted = p.getDatesReprinted();
			String now = DateUtils.format("yyyy-MM-dd HH:mm:ss", new Date());
			if (dates_reprinted != null && dates_reprinted.length() > 0) {
				dates_reprinted += "," + now + ";" + providerNo;
			} else {
				dates_reprinted = now + ";" + providerNo;
			}
			p.setDatesReprinted(dates_reprinted);
			dao.merge(p);
			this.setNumPrints(this.getNumPrints() + 1);

			return true;

		}

		public int getNextPosition() {
			DrugDao dao = SpringUtils.getBean(DrugDao.class);
			int position = dao.getMaxPosition(this.getDemographicNo());
			return (position + 1);
		}

		public boolean Save(String scriptId) {

			this.calcEndDate();

			// clean up fields
			if (this.takeMin > this.takeMax) this.takeMax = this.takeMin;

			if (getSpecial() == null || getSpecial().length() < 6) logger.warn("drug special appears to be null or empty : " + getSpecial());

			//  String parsedSpecial = RxUtil.replace(this.getSpecial(), "'", "");//a bug?
			String escapedSpecial = StringEscapeUtils.escapeSql(this.getSpecial());

			if (escapedSpecial == null || escapedSpecial.length() < 6) logger.warn("drug special after escaping appears to be null or empty : " + escapedSpecial);

			// check to see if there is an identitical prescription in
			// the database. If there is we'll return that drugid instead
			// of adding a new prescription.
			/*
						String endDate;
						if (this.getEndDate() == null) {
							endDate = "0001-01-01";
						} else {
							endDate = RxUtil.DateToString(this.getEndDate());
						}
			*/
			DrugDao dao = SpringUtils.getBean(DrugDao.class);
			// double check if we don't h
			Drug drug = dao.findByEverything(this.getProviderNo(), this.getDemographicNo(), this.getRxDate(), this.getEndDate(), this.getWrittenDate(), this.getBrandName(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getUnitName(), this.getRepeat(), this.getLastRefillDate(), this.getNosubs(), this.getPrn(), escapedSpecial, this.getOutsideProviderName(),
			        this.getOutsideProviderOhip(), this.getCustomInstr(), this.getLongTerm(), this.isCustomNote(), this.getPastMed(), this.getPatientCompliance(), this.getSpecialInstruction(), this.getComment(), this.getStartDateUnknown());

			drug = new Drug();

			int position = this.getNextPosition();
			this.position = position;
			syncDrug(drug, ConversionUtils.fromIntString(scriptId));
			dao.persist(drug);
			drugId = drug.getId();

			return true;
		}

		private void syncDrug(Drug drug, Integer scriptId) {
			// the fields set are based on previous code, I don't know the details of why which are and are not set and can not audit it at this point in time.
			drug.setProviderNo(getProviderNo());
			drug.setDemographicId(getDemographicNo());
			drug.setRxDate(getRxDate());
			drug.setEndDate(getEndDate());
			drug.setWrittenDate(getWrittenDate());
			drug.setBrandName(getBrandName());
			drug.setGcnSeqNo(getGCN_SEQNO());
			drug.setCustomName(getCustomName());
			drug.setTakeMin(getTakeMin());
			drug.setTakeMax(getTakeMax());
			drug.setFreqCode(getFrequencyCode());
			drug.setDuration(getDuration());
			drug.setDurUnit(getDurationUnit());
			drug.setQuantity(getQuantity());
			drug.setDispensingUnits(getDispensingUnits());
			drug.setRepeat(getRepeat());
			drug.setLastRefillDate(getLastRefillDate());
			drug.setNoSubs(getNosubs());
			drug.setPrn(getPrn());
			drug.setSpecial(getSpecial());
			drug.setGenericName(getGenericName());
			drug.setScriptNo(scriptId);
			drug.setAtc(atcCode);
			drug.setRegionalIdentifier(regionalIdentifier);
			drug.setUnit(getUnit());
			drug.setMethod(getMethod());
			drug.setRoute(getRoute());
			drug.setDrugForm(getDrugForm());
			drug.setOutsideProviderName(getOutsideProviderName());
			drug.setOutsideProviderOhip(getOutsideProviderOhip());
			drug.setCustomInstructions(getCustomInstr());
			drug.setDosage(getDosage());
			drug.setUnitName(getUnitName());
			drug.setLongTerm(getLongTerm());
			drug.setShortTerm(getShortTerm());
			drug.setCustomNote(isCustomNote());
			drug.setPastMed(getPastMed());
			drug.setDispenseInternal(getDispenseInternal());
			drug.setSpecialInstruction(getSpecialInstruction());
			drug.setPatientCompliance(getPatientCompliance());
			drug.setNonAuthoritative(isNonAuthoritative());
			drug.setPickUpDateTime(getPickupDate());
			drug.setETreatmentType(getETreatmentType());
			drug.setRxStatus(getRxStatus());
			drug.setDispenseInterval(getDispenseInterval());
			drug.setRefillQuantity(getRefillQuantity());
			drug.setRefillDuration(getRefillDuration());
			drug.setHideFromCpp(false);
			drug.setPosition(position);
			drug.setComment(getComment());
			drug.setStartDateUnknown(getStartDateUnknown());
			drug.setDispenseInternal(getDispenseInternal());
		}

		public boolean AddToFavorites(String providerNo, String favoriteName) {
			Favorite fav = new Favorite(0, providerNo, favoriteName, this.getBrandName(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getDispensingUnits(), this.getRepeat(), this.getNosubsInt(), this.getPrnInt(), this.getSpecial(), this.getGenericName(), this.getAtcCode(), this.getRegionalIdentifier(), this.getUnit(), this.getUnitName(), this.getMethod(), this.getRoute(), this.getDrugForm(),
			        this.getCustomInstr(), this.getDosage());
			fav.setDispenseInternal(this.getDispenseInternal());

			return fav.Save();
		}

		/**
		 * Getter for property atcCode.
		 *
		 * @return Value of property atcCode.
		 */
		public java.lang.String getAtcCode() {
			return atcCode;
		}

		/**
		 * Checks to see if atcCode is not null or an emtpy string
		 */
		public boolean isValidAtcCode() {
			if (atcCode != null && !atcCode.trim().equals("")) {
				return true;
			}
			return false;
		}

		/**
		 * Setter for property atcCode.
		 *
		 * @param atcCode New value of property atcCode.
		 */
		public void setAtcCode(java.lang.String atcCode) {
			this.atcCode = atcCode;
		}

		/**
		 * Getter for property regionalIdentifier.
		 *
		 * @return Value of property regionalIdentifier.
		 */
		public java.lang.String getRegionalIdentifier() {
			return regionalIdentifier;
		}

		/**
		 * Setter for property regionalIdentifier.
		 *
		 * @param regionalIdentifier New value of property regionalIdentifier.
		 */
		public void setRegionalIdentifier(java.lang.String regionalIdentifier) {
			this.regionalIdentifier = regionalIdentifier;
		}

		/**
		 * Getter for property method.
		 *
		 * @return Value of property method.
		 */
		public java.lang.String getMethod() {
			return method;
		}

		/**
		 * Setter for property method.
		 *
		 * @param method New value of property method.
		 */
		public void setMethod(java.lang.String method) {
			this.method = method;
		}

		/**
		 * Getter for property unit.
		 *
		 * @return Value of property unit.
		 */
		public java.lang.String getUnit() {
			return unit;
		}

		/**
		 * Setter for property unit.
		 *
		 * @param unit New value of property unit.
		 */
		public void setUnit(java.lang.String unit) {
			this.unit = unit;
		}

		/**
		 * Getter for property unitName
		 *
		 * @return Value of property unitName.
		 */
		public java.lang.String getUnitName() {
			return unitName;
		}

		/**
		 * Setter for property unitName.
		 *
		 * @param unitName New value of property unitName.
		 */
		public void setUnitName(java.lang.String unitName) {
			this.unitName = unitName;
		}

		/**
		 * Getter for property route.
		 *
		 * @return Value of property route.
		 */
		public java.lang.String getRoute() {
			return route;
		}

		/**
		 * Setter for property route.
		 *
		 * @param route New value of property route.
		 */
		public void setRoute(java.lang.String route) {
			this.route = route;
		}

		public java.lang.String getDrugForm() {
			return drugForm;
		}

		public void setDrugForm(java.lang.String drugForm) {
			this.drugForm = drugForm;
		}

		/**
		 *Setter for property custom (does it have customized directions)
		 *
		 * @param custom value for custom
		 */
		public void setCustomInstr(boolean custom) {
			this.custom = custom;
		}

		public boolean getCustomInstr() {
			return this.custom;
		}

		/**
		 * Getter for property rxCreatedDate.
		 *
		 * @return Value of property rxCreatedDate.
		 */
		public java.util.Date getRxCreatedDate() {
			return rxCreatedDate;
		}

		/**
		 * Setter for property rxCreatedDate.
		 *
		 * @param rxCreatedDate New value of property rxCreatedDate.
		 */
		public void setRxCreatedDate(java.util.Date rxCreatedDate) {
			this.rxCreatedDate = rxCreatedDate;
		}

		/**
		 * Getter for property dosage.
		 *
		 * @return Value of property dosage.
		 */
		public java.lang.String getDosage() {
			return dosage;
		}

		/**
		 * Setter for property dosage.
		 *
		 * @param dosage New value of property dosage.
		 */
		public void setDosage(java.lang.String dosage) {
			this.dosage = dosage;
		}

		public Integer getRefillDuration() {
			return refillDuration;
		}

		public void setRefillDuration(int refillDuration) {
			this.refillDuration = refillDuration;
		}

		public Integer getRefillQuantity() {
			return refillQuantity;
		}

		public void setRefillQuantity(int refillQuantity) {
			this.refillQuantity = refillQuantity;
		}

		public Integer getDispenseInterval() {
			return dispenseInterval;
		}

		public void setDispenseInterval(int dispenseInterval) {
			this.dispenseInterval = dispenseInterval;
		}

	}

	public static class Favorite {

		int favoriteId;
		String providerNo;
		String favoriteName;
		String BN;
		int GCN_SEQNO;
		String customName;
		float takeMin;
		float takeMax;
		String frequencyCode;
		String duration;
		String durationUnit;
		String quantity;
		String dispensingUnits;
		int repeat;
		boolean nosubs;
		boolean prn;
		boolean customInstr;
		String special;
		String GN;
		String atcCode;
		String regionalIdentifier;
		String unit;
		String unitName;
		String method;
		String route;
		String drugForm;
		String dosage;
		Boolean dispenseInternal;

		public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, String dispensingUnits, int repeat, int nosubs, int prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
			this.favoriteId = favoriteId;
			this.providerNo = providerNo;
			this.favoriteName = favoriteName;
			this.BN = BN;
			this.GCN_SEQNO = GCN_SEQNO;
			this.customName = customName;
			this.takeMin = takeMin;
			this.takeMax = takeMax;
			this.frequencyCode = frequencyCode;
			this.duration = duration;
			this.durationUnit = durationUnit;
			this.quantity = quantity;
			this.dispensingUnits = dispensingUnits;
			this.repeat = repeat;
			this.nosubs = RxUtil.IntToBool(nosubs);
			this.prn = RxUtil.IntToBool(prn);
			this.special = special;
			this.GN = GN;
			this.atcCode = atc;
			this.regionalIdentifier = regionalIdentifier;
			this.unit = unit;
			this.unitName = unitName;
			this.method = method;
			this.route = route;
			this.drugForm = drugForm;
			this.customInstr = customInstr;
			this.dosage = dosage;
		}

		public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, String dispensingUnits, int repeat, boolean nosubs, boolean prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
			this.favoriteId = favoriteId;
			this.providerNo = providerNo;
			this.favoriteName = favoriteName;
			this.BN = BN;
			this.GCN_SEQNO = GCN_SEQNO;
			this.customName = customName;
			this.takeMin = takeMin;
			this.takeMax = takeMax;
			this.frequencyCode = frequencyCode;
			this.duration = duration;
			this.durationUnit = durationUnit;
			this.quantity = quantity;
			this.dispensingUnits = dispensingUnits;
			this.repeat = repeat;
			this.nosubs = nosubs;
			this.prn = prn;
			this.special = special;
			this.GN = GN;
			this.atcCode = atc;
			this.regionalIdentifier = regionalIdentifier;
			this.unit = unit;
			this.unitName = unitName;
			this.method = method;
			this.route = route;
			this.drugForm = drugForm;
			this.customInstr = customInstr;
			this.dosage = dosage;
		}

		public String getGN() {
			return this.GN;
		}

		public void setGN(String RHS) {
			this.GN = RHS;
		}

		public int getFavoriteId() {
			return this.favoriteId;
		}

		public String getProviderNo() {
			return this.providerNo;
		}

		public String getFavoriteName() {
			return this.favoriteName;
		}

		public void setFavoriteName(String RHS) {
			this.favoriteName = RHS;
		}

		public String getBN() {
			return this.BN;
		}

		public void setBN(String RHS) {
			this.BN = RHS;
		}

		public int getGCN_SEQNO() {
			return this.GCN_SEQNO;
		}

		public void setGCN_SEQNO(int RHS) {
			this.GCN_SEQNO = RHS;
		}

		public String getCustomName() {
			return this.customName;
		}

		public void setCustomName(String RHS) {
			this.customName = RHS;
		}

		public float getTakeMin() {
			return this.takeMin;
		}

		public void setTakeMin(float RHS) {
			this.takeMin = RHS;
		}

		public String getTakeMinString() {
			return RxUtil.FloatToString(this.takeMin);
		}

		public float getTakeMax() {
			return this.takeMax;
		}

		public void setTakeMax(float RHS) {
			this.takeMax = RHS;
		}

		public String getTakeMaxString() {
			return RxUtil.FloatToString(this.takeMax);
		}

		public String getFrequencyCode() {
			return this.frequencyCode;
		}

		public void setFrequencyCode(String RHS) {
			this.frequencyCode = RHS;
		}

		public String getDuration() {
			return this.duration;
		}

		public void setDuration(String RHS) {
			this.duration = RHS;
		}

		public String getDurationUnit() {
			return this.durationUnit;
		}

		public void setDurationUnit(String RHS) {
			this.durationUnit = RHS;
		}

		public String getQuantity() {
			return this.quantity;
		}

		public void setQuantity(String RHS) {
			this.quantity = RHS;
		}

		public String getDispensingUnits() {
			return this.dispensingUnits;
		}

		public void setDispensingUnits(String RHS) {
			this.dispensingUnits = RHS;
		}

		public int getRepeat() {
			return this.repeat;
		}

		public void setRepeat(int RHS) {
			this.repeat = RHS;
		}

		public boolean getNosubs() {
			return this.nosubs;
		}

		public void setNosubs(boolean RHS) {
			this.nosubs = RHS;
		}

		public int getNosubsInt() {
			if (this.getNosubs() == true) {
				return 1;
			} else {
				return 0;
			}
		}

		public boolean getPrn() {
			return this.prn;
		}

		public void setPrn(boolean RHS) {
			this.prn = RHS;
		}

		public int getPrnInt() {
			if (this.getPrn() == true) {
				return 1;
			} else {
				return 0;
			}
		}

		public String getSpecial() {
			return this.special;
		}

		public void setSpecial(String RHS) {
			this.special = RHS;
		}

		public boolean getCustomInstr() {
			return this.customInstr;
		}

		public void setCustomInstr(boolean customInstr) {
			this.customInstr = customInstr;
		}

		public Boolean getDispenseInternal() {
			return dispenseInternal;
		}

		public void setDispenseInternal(Boolean dispenseInternal) {
			this.dispenseInternal = dispenseInternal;
		}

		public boolean Save() {
			boolean b = false;

			// clean up fields
			if (this.takeMin > this.takeMax) {
				this.takeMax = this.takeMin;
			}
			if (getSpecial() == null || getSpecial().length() < 4) {
				//if (getSpecial() == null || getSpecial().length() < 6) {
				logger.warn("drug special appears to be null or empty : " + getSpecial());
			}
			String parsedSpecial = RxUtil.replace(this.getSpecial(), "'", "");
			//if (parsedSpecial == null || parsedSpecial.length() < 6) {
			if (parsedSpecial == null || parsedSpecial.length() < 4) {
				logger.warn("drug special after parsing appears to be null or empty : " + parsedSpecial);
			}

			FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
			org.oscarehr.common.model.Favorite favorite = dao.findByEverything(this.getProviderNo(), this.getFavoriteName(), this.getBN(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getRepeat(), this.getNosubs(), this.getPrn(), parsedSpecial, this.getGN(), this.getUnitName(), this.getCustomInstr());

			if (this.getFavoriteId() == 0) {

				if (favorite != null) this.favoriteId = favorite.getId();

				b = true;

				if (this.getFavoriteId() == 0) {
					favorite = new org.oscarehr.common.model.Favorite();
					favorite = syncFavorite(favorite);

					dao.persist(favorite);
					this.favoriteId = favorite.getId();

					b = true;
				}

			} else {
				favorite = syncFavorite(favorite);
				dao.merge(favorite);

				b = true;
			}

			return b;
		}

		private org.oscarehr.common.model.Favorite syncFavorite(org.oscarehr.common.model.Favorite f) {
			f.setProviderNo(this.getProviderNo());
			f.setName(this.getFavoriteName());
			f.setBn(this.getBN());
			f.setGcnSeqno(this.getGCN_SEQNO());
			f.setCustomName(this.getCustomName());
			f.setTakeMin(this.getTakeMin());
			f.setTakeMax(this.getTakeMax());
			f.setFrequencyCode(this.getFrequencyCode());
			f.setDuration(this.getDuration());
			f.setDurationUnit(this.getDurationUnit());
			f.setQuantity(this.getQuantity());
			f.setRepeat(this.getRepeat());
			f.setNosubs(this.getNosubsInt() != 0);
			f.setPrn(this.getPrnInt() != 0);
			f.setSpecial(this.getSpecial());
			f.setGn(this.getGN());
			f.setAtc(this.getAtcCode());
			f.setRegionalIdentifier(this.getRegionalIdentifier());
			f.setUnit(this.getUnit());
			f.setUnitName(this.getUnitName());
			f.setMethod(this.getMethod());
			f.setRoute(this.getRoute());
			f.setDrugForm(this.getDrugForm());
			f.setCustomInstructions(this.getCustomInstr());
			f.setDosage(this.getDosage());
			return f;
		}

		/**
		 * Getter for property atcCode.
		 *
		 * @return Value of property atcCode.
		 */
		public java.lang.String getAtcCode() {
			return atcCode;
		}

		/**
		 * Setter for property atcCode.
		 *
		 * @param atcCode New value of property atcCode.
		 */
		public void setAtcCode(java.lang.String atcCode) {
			this.atcCode = atcCode;
		}

		/**
		 * Getter for property regionalIdentifier.
		 *
		 * @return Value of property regionalIdentifier.
		 */
		public java.lang.String getRegionalIdentifier() {
			return regionalIdentifier;
		}

		/**
		 * Setter for property regionalIdentifier.
		 *
		 * @param regionalIdentifier New value of property regionalIdentifier.
		 */
		public void setRegionalIdentifier(java.lang.String regionalIdentifier) {
			this.regionalIdentifier = regionalIdentifier;
		}

		/**
		 * Getter for property unit.
		 *
		 * @return Value of property unit.
		 */
		public java.lang.String getUnit() {
			return unit;
		}

		/**
		 * Setter for property unit.
		 *
		 * @param unit New value of property unit.
		 */
		public void setUnit(java.lang.String unit) {
			this.unit = unit;
		}

		/**
		 * Getter for property unitName.
		 *
		 * @return Value of property unitName.
		 */
		public java.lang.String getUnitName() {
			return unitName;
		}

		/**
		 * Setter for property unitName.
		 *
		 * @param unitName New value of property unitName.
		 */
		public void setUnitName(java.lang.String unitName) {
			this.unitName = unitName;
		}

		/**
		 * Getter for property method.
		 *
		 * @return Value of property method.
		 */
		public java.lang.String getMethod() {
			return method;
		}

		/**
		 * Setter for property method.
		 *
		 * @param method New value of property method.
		 */
		public void setMethod(java.lang.String method) {
			this.method = method;
		}

		/**
		 * Getter for property route.
		 *
		 * @return Value of property route.
		 */
		public java.lang.String getRoute() {
			return route;
		}

		/**
		 * Setter for property route.
		 *
		 * @param route New value of property route.
		 */
		public void setRoute(java.lang.String route) {
			this.route = route;
		}

		public java.lang.String getDrugForm() {
			return drugForm;
		}

		public void setDrugForm(java.lang.String drugForm) {
			this.drugForm = drugForm;
		}

		/**
		 * Getter for property dosage.
		 *
		 * @return Value of property dosage.
		 */
		public java.lang.String getDosage() {
			return dosage;
		}

		/**
		 * Setter for property dosage.
		 *
		 * @param dosage New value of property dosage.
		 */
		public void setDosage(java.lang.String dosage) {
			this.dosage = dosage;
		}

	}

	public static boolean addToFavorites(String providerNo, String favoriteName, Drug drug) {
		Favorite fav = new Favorite(0, providerNo, favoriteName, drug.getBrandName(), drug.getGcnSeqNo(), drug.getCustomName(), drug.getTakeMin(), drug.getTakeMax(), drug.getFreqCode(), drug.getDuration(), drug.getDurUnit(), drug.getQuantity(), drug.getDispensingUnits(), drug.getRepeat(), drug.isNoSubs(), drug.isPrn(), drug.getSpecial(), drug.getGenericName(), drug.getAtc(), drug.getRegionalIdentifier(), drug.getUnit(), drug.getUnitName(), drug.getMethod(), drug.getRoute(), drug.getDrugForm(), drug.isCustomInstructions(),
		        drug.getDosage());
		fav.setDispenseInternal(drug.getDispenseInternal());
		return fav.Save();
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}

}
