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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.util.RxUtil;

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
            logger.error("Drugs special field was null, this means nothing will print.", new IllegalStateException("Drugs special field was null."));
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
		if(StringUtils.isBlank(dur) || dur.equalsIgnoreCase("null") ) dur = "";
		prescription.setDuration(dur);
		prescription.setDurationUnit(drug.getDurUnit());
		prescription.setQuantity(drug.getQuantity());
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
		prescription.setCustomNote(drug.isCustomNote());
		prescription.setPastMed(drug.getPastMed());
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
			logger.error("I strongly suspect something is wrong, either special is null or it appears to not contain anything useful. drugId=" + drugId + ", drug.special=" + prescription.getSpecial(), new IllegalStateException("Drug special is blank or invalid"));
			logger.error("data from db is : " + drug.getSpecial());
		}

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
        prescription.setCustomNote(rePrescribe.isCustomNote());
        prescription.setPastMed(rePrescribe.getPastMed());
        prescription.setPatientCompliance(rePrescribe.getPatientCompliance());
        prescription.setOutsideProviderName(rePrescribe.getOutsideProviderName());
        prescription.setOutsideProviderOhip(rePrescribe.getOutsideProviderOhip());
        prescription.setSpecialInstruction(rePrescribe.getSpecialInstruction());
        prescription.setPickupDate(rePrescribe.getPickupDate());
        prescription.setPickupTime(rePrescribe.getPickupTime());
        prescription.setETreatmentType(rePrescribe.getETreatmentType());
        prescription.setRxStatus(rePrescribe.getRxStatus());
        if (rePrescribe.getDispenseInterval()!=null) prescription.setDispenseInterval(rePrescribe.getDispenseInterval());
        if (rePrescribe.getRefillDuration()!=null) prescription.setRefillDuration(rePrescribe.getRefillDuration());
        if (rePrescribe.getRefillQuantity()!=null) prescription.setRefillQuantity(rePrescribe.getRefillQuantity());
        prescription.setDrugReferenceId(rePrescribe.getDrugId());

        return prescription;
    }

    // JAY CHANGED THIS FUNCTION on desc 3 2002.
    // I changed the sql query from select * from drugs where archived = 0 and demographic_no ....
    // to
    // select * from drugs where demographic_no ...
    // I also added the function getPrescriptionByPatientHideDeleted
    public Prescription[] getPrescriptionsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT * FROM drugs WHERE  " + "demographic_no = " + demographicNo + " " + "ORDER BY position, rx_date DESC, drugId DESC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setRxDate(rs.getDate("rx_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setWrittenDate(rs.getDate("written_date"));
                p.setBrandName(oscar.Misc.getString(rs, "BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(oscar.Misc.getString(rs, "customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
                p.setDuration(oscar.Misc.getString(rs, "duration"));
                p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
                p.setQuantity(oscar.Misc.getString(rs, "quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setLastRefillDate(rs.getDate("last_refill_date"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(oscar.Misc.getString(rs, "special"));
                p.setSpecialInstruction(oscar.Misc.getString(rs, "special_instruction"));
                p.setArchived(oscar.Misc.getString(rs, "archived"));
                p.setGenericName(oscar.Misc.getString(rs, "GN"));
                p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
                p.setScript_no(oscar.Misc.getString(rs, "script_no"));
                p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
                p.setUnit(oscar.Misc.getString(rs, "unit"));
                p.setUnitName(oscar.Misc.getString(rs, "unitName"));
                p.setMethod(oscar.Misc.getString(rs, "method"));
                p.setRoute(oscar.Misc.getString(rs, "route"));
                p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(oscar.Misc.getString(rs, "dosage"));
                p.setLongTerm(rs.getBoolean("long_term"));
                p.setCustomNote(rs.getBoolean("custom_note"));
                p.setPastMed(rs.getBoolean("past_med"));
                p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
                p.setComment(rs.getString("comment"));
                if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
                else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
                p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
                p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
                p.setPickupDate(rs.getDate("pickup_datetime"));
                p.setPickupTime(rs.getDate("pickup_datetime"));
                p.setETreatmentType(rs.getString("eTreatmentType"));
                p.setRxStatus(rs.getString("rxStatus"));
                if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
                if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
                if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));
                lst.add(p);
            }

            rs.close();

            arr = (Prescription[]) lst.toArray(arr);

        } catch (SQLException e) {
            logger.error(sql, e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return arr;
    }

    // ////
    private Prescription getPrescriptionFromRS(ResultSet rs, int demographicNo) throws SQLException {
        Prescription p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
        p.setRxCreatedDate(rs.getDate("create_date"));
        p.setRxDate(rs.getDate("rx_date"));
        p.setEndDate(rs.getDate("end_date"));
        p.setWrittenDate(rs.getDate("written_date"));
        p.setBrandName(oscar.Misc.getString(rs, "BN"));
        p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
        p.setCustomName(oscar.Misc.getString(rs, "customName"));
        p.setTakeMin(rs.getFloat("takemin"));
        p.setTakeMax(rs.getFloat("takemax"));
        p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
        p.setDuration(oscar.Misc.getString(rs, "duration"));
        p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
        p.setQuantity(oscar.Misc.getString(rs, "quantity"));
        p.setRepeat(rs.getInt("repeat"));
        p.setLastRefillDate(rs.getDate("last_refill_date"));
        p.setNosubs(rs.getInt("nosubs"));
        p.setPrn(rs.getInt("prn"));
        p.setSpecial(oscar.Misc.getString(rs, "special"));
        p.setArchived(oscar.Misc.getString(rs, "archived"));
        p.setGenericName(oscar.Misc.getString(rs, "GN"));
        p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
        p.setScript_no(oscar.Misc.getString(rs, "script_no"));
        p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
        p.setUnit(oscar.Misc.getString(rs, "unit"));
        p.setUnitName(oscar.Misc.getString(rs, "unitName"));
        p.setMethod(oscar.Misc.getString(rs, "method"));
        p.setRoute(oscar.Misc.getString(rs, "route"));
        p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
        p.setCustomInstr(rs.getBoolean("custom_instructions"));
        p.setDosage(oscar.Misc.getString(rs, "dosage"));
        p.setLongTerm(rs.getBoolean("long_term"));
        p.setCustomNote(rs.getBoolean("custom_note"));
        p.setPastMed(rs.getBoolean("past_med"));
        p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
        p.setComment(rs.getString("comment"));
        if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
        else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
        p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
        p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
        p.setPickupDate(rs.getDate("pickup_datetime"));
        p.setPickupTime(rs.getDate("pickup_datetime"));
        p.setETreatmentType(rs.getString("eTreatmentType"));
        p.setRxStatus(rs.getString("rxStatus"));
        if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
        if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
        if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));

        // String datesRePrinted = oscar.Misc.getString(rs,"dates_reprinted");
        // if( datesRePrinted != null && datesRePrinted.length() > 0 ) {
        // p.setNumPrints(datesRePrinted.split(",").length + 1);
        // }
        // else {
        // p.setNumPrints(1);
        // }
        return p;

    }

    /*
     * Limit returned prescriptions to those which have an entry in both drugs and prescription table
     */
    public Prescription[] getPrescriptionScriptsByPatientATC(int demographicNo, String atc) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT d.*FROM drugs d WHERE  " + "d.demographic_no = " + demographicNo + " and d.ATC = '" + atc + "' " + "ORDER BY position, rx_date DESC, drugId DESC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;
            String datesRePrinted;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                lst.add(getPrescriptionFromRS(rs, demographicNo));
            }
            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

            arr = (Prescription[]) lst.toArray(arr);
        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return arr;
    }
    //do not return customed drugs
    public Prescription[] getPrescriptionScriptsByPatientRegionalIdentifier(int demographicNo, String regionalIdentifier) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT d.* FROM drugs d WHERE  " + "d.demographic_no = " + demographicNo + " and d.regional_identifier = '" + regionalIdentifier + "' " + "ORDER BY position, rx_date DESC, drugId DESC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                lst.add(getPrescriptionFromRS(rs, demographicNo));
            }
            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

            arr = (Prescription[]) lst.toArray(arr);
        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return arr;
    }

    //maybe needed later.
    public Prescription[] getPrescriptionScriptsByPatientDrugId(int demographicNo, String drugId) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT d.* FROM drugs d WHERE  " + "d.demographic_no = " + demographicNo + " and d.drugId = '" + drugId + "' " + "ORDER BY position, rx_date DESC, drugId DESC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                lst.add(getPrescriptionFromRS(rs, demographicNo));
            }
            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

            arr = (Prescription[]) lst.toArray(arr);
        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return arr;
    }

    public Prescription getLatestPrescriptionScriptByPatientDrugId(int demographicNo, String drugId) {
        Prescription prescription = null;

        String sql = "SELECT d.* FROM drugs d WHERE  d.drugId = " + drugId;

        try {
            // Get Prescription from database

            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                prescription = getPrescriptionFromRS(rs, demographicNo);
            }
            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return prescription;
    }

    // ////

    /*
     * Limit returned prescriptions to those which have an entry in both drugs and prescription table
     */
    public Prescription[] getPrescriptionScriptsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT d.*, p.date_printed, p.dates_reprinted FROM drugs d, prescription p WHERE  " + "d.demographic_no = " + demographicNo + " and d.script_no = p.script_no " + "ORDER BY position DESC" +
        		", rx_date DESC, drugId ASC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;
            String datesRePrinted;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setRxDate(rs.getDate("rx_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setWrittenDate(rs.getDate("written_date"));
                p.setBrandName(oscar.Misc.getString(rs, "BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(oscar.Misc.getString(rs, "customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
                p.setDuration(oscar.Misc.getString(rs, "duration"));
                p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
                p.setQuantity(oscar.Misc.getString(rs, "quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setLastRefillDate(rs.getDate("last_refill_date"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(oscar.Misc.getString(rs, "special"));
                p.setArchived(oscar.Misc.getString(rs, "archived"));
                p.setGenericName(oscar.Misc.getString(rs, "GN"));
                p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
                p.setScript_no(oscar.Misc.getString(rs, "script_no"));
                p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
                p.setUnit(oscar.Misc.getString(rs, "unit"));
                p.setUnitName(oscar.Misc.getString(rs, "unitName"));
                p.setMethod(oscar.Misc.getString(rs, "method"));
                p.setRoute(oscar.Misc.getString(rs, "route"));
                p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(oscar.Misc.getString(rs, "dosage"));
                p.setLongTerm(rs.getBoolean("long_term"));
                p.setCustomNote(rs.getBoolean("custom_note"));
                p.setPastMed(rs.getBoolean("past_med"));
                p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
                p.setComment(rs.getString("comment"));
                if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
                else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
                p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
                p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
                p.setPickupDate(rs.getDate("pickup_datetime"));
                p.setPickupTime(rs.getDate("pickup_datetime"));
                p.setETreatmentType(rs.getString("eTreatmentType"));
                p.setRxStatus(rs.getString("rxStatus"));
                if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
                if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
                if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));

                datesRePrinted = oscar.Misc.getString(rs, "dates_reprinted");
                if (datesRePrinted != null && datesRePrinted.length() > 0) {
                    p.setNumPrints(datesRePrinted.split(",").length + 1);
                } else {
                    p.setNumPrints(1);
                }
                p.setPrintDate(rs.getDate("date_printed"));
                p.setDatesReprinted(datesRePrinted);
                lst.add(p);
            }

            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

            arr = (Prescription[]) lst.toArray(arr);

        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return arr;
    }

    public ArrayList<Prescription> getPrescriptionsByScriptNo(int script_no, int demographicNo) {
        ArrayList<Prescription> lst = new ArrayList<Prescription>();

        String sql = "select drugs.*, p.date_printed, p.dates_reprinted from drugs, prescription p where p.script_no = drugs.script_no and " + "drugs.script_no = " + script_no;

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;
            String datesRePrinted;

            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setRxDate(rs.getDate("rx_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setWrittenDate(rs.getDate("written_date"));
                p.setBrandName(oscar.Misc.getString(rs, "BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(oscar.Misc.getString(rs, "customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
                p.setDuration(oscar.Misc.getString(rs, "duration"));
                p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
                p.setQuantity(oscar.Misc.getString(rs, "quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setLastRefillDate(rs.getDate("last_refill_date"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(oscar.Misc.getString(rs, "special"));
                p.setGenericName(oscar.Misc.getString(rs, "GN"));
                p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
                p.setScript_no(oscar.Misc.getString(rs, "script_no"));
                p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
                p.setUnit(oscar.Misc.getString(rs, "unit"));
                p.setUnitName(oscar.Misc.getString(rs, "unitName"));
                p.setMethod(oscar.Misc.getString(rs, "method"));
                p.setRoute(oscar.Misc.getString(rs, "route"));
                p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(oscar.Misc.getString(rs, "dosage"));
                p.setLongTerm(rs.getBoolean("long_term"));
                p.setCustomNote(rs.getBoolean("custom_note"));
                p.setPastMed(rs.getBoolean("past_med"));
                p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
                p.setComment(rs.getString("comment"));
                if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
                else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
                p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
                p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
                p.setPrintDate(rs.getDate("date_printed"));
                p.setPickupDate(rs.getDate("pickup_datetime"));
                p.setPickupTime(rs.getDate("pickup_datetime"));
                p.setETreatmentType(rs.getString("eTreatmentType"));
                p.setRxStatus(rs.getString("rxStatus"));
                if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
                if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
                if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));

                datesRePrinted = oscar.Misc.getString(rs, "dates_reprinted");
                if (datesRePrinted != null && datesRePrinted.length() > 0) p.setNumPrints(datesRePrinted.split(",").length + 1);
                else p.setNumPrints(1);

                lst.add(p);
            }

            rs.close();
            DbConnectionFilter.getThreadLocalDbConnection().close();

        } catch (SQLException e) {
            logger.error(sql, e);
        }

        return lst;
    }

    // ///
    public Prescription[] getPrescriptionsByPatientHideDeleted(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        String sql = "SELECT * FROM drugs WHERE archived = 0 AND " + "demographic_no = " + demographicNo + " " + "ORDER BY position, rx_date DESC, drugId DESC";

        try {
            // Get Prescription from database

            ResultSet rs;

            Prescription p;

            rs = DBHandler.GetSQL(sql);


            while (rs.next()) {
                p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setRxDate(rs.getDate("rx_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setWrittenDate(rs.getDate("written_date"));
                p.setBrandName(oscar.Misc.getString(rs, "BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(oscar.Misc.getString(rs, "customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
                p.setDuration(oscar.Misc.getString(rs, "duration"));
                p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
                p.setQuantity(oscar.Misc.getString(rs, "quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setLastRefillDate(rs.getDate("last_refill_date"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(oscar.Misc.getString(rs, "special"));
                p.setGenericName(oscar.Misc.getString(rs, "GN"));
                p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
                p.setScript_no(oscar.Misc.getString(rs, "script_no"));
                p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
                p.setUnit(oscar.Misc.getString(rs, "unit"));
                p.setUnitName(oscar.Misc.getString(rs, "unitName"));
                p.setMethod(oscar.Misc.getString(rs, "method"));
                p.setRoute(oscar.Misc.getString(rs, "route"));
                p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(oscar.Misc.getString(rs, "dosage"));
                p.setLongTerm(rs.getBoolean("long_term"));
                p.setCustomNote(rs.getBoolean("custom_note"));
                p.setPastMed(rs.getBoolean("past_med"));
                p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
                p.setComment(rs.getString("comment"));
                if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
                else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
                p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
                p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
                p.setPickupDate(rs.getDate("pickup_datetime"));
                p.setPickupTime(rs.getDate("pickup_datetime"));
                p.setETreatmentType(rs.getString("eTreatmentType"));
                p.setRxStatus(rs.getString("rxStatus"));
                if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
                if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
                if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));
                lst.add(p);
            }

            rs.close();

            arr = (Prescription[]) lst.toArray(arr);

        } catch (SQLException e) {
            logger.error(sql, e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return arr;
    }

    public Vector getCurrentATCCodesByPatient(int demographicNo) {
        Vector vec = new Vector();
        Prescription[] p = getPrescriptionsByPatientHideDeleted(demographicNo);
        for (int i = 0; i < p.length; i++) {
            if (p[i].isCurrent()) {
                logger.debug(p[i].getAtcCode() + " " + p[i].getBrandName());
                if (!vec.contains(p[i].getAtcCode())) {
                    logger.debug("Actually Adding " + p[i].getAtcCode() + " " + p[i].getBrandName());
                    if (p[i].isValidAtcCode()) {
                        vec.add(p[i].getAtcCode());
                    }
                }
            }
        }
        return vec;
    }

    // /////////////////////
    public Prescription[] getUniquePrescriptionsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();

        try {
            // Get Prescription from database

            ResultSet rs, rs2;
            String sql = "SELECT * FROM drugs d WHERE d.archived = 0 AND d.demographic_no = " + demographicNo + " ORDER BY position DESC,rx_date DESC, drugId DESC";
            String indivoSql = "SELECT indivoDocIdx FROM indivoDocs i WHERE i.oscarDocNo = ? and docType = 'Rx' limit 1";
            boolean myOscarEnabled = OscarProperties.getInstance().getProperty("MY_OSCAR", "").trim().equalsIgnoreCase("YES");
            Prescription p;
            logger.info(sql + "   RETURNS");
            rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                boolean b = true;

                for (int i = 0; i < lst.size(); i++) {
                    Prescription p2 = (Prescription) lst.get(i);

                    if (p2.getGCN_SEQNO() == rs.getInt("GCN_SEQNO")) {
                        if (p2.getGCN_SEQNO() != 0) // not custom - safe GCN
                        {
                            b = false;
                        } else // custom
                        {
                            if (p2.getCustomName() != null && oscar.Misc.getString(rs, "customName") != null) {
                                if (p2.getCustomName().equals(oscar.Misc.getString(rs, "customName"))) // same custom
                                {
                                    b = false;
                                }
                            }
                        }
                    }
                }

                if (b) {
                	logger.info("ADDING PRESCRIPTION");
                    p = new Prescription(rs.getInt("drugid"), oscar.Misc.getString(rs, "provider_no"), demographicNo);
                    p.setRxCreatedDate(rs.getDate("create_date"));
                    p.setRxDate(rs.getDate("rx_date"));
                    p.setEndDate(rs.getDate("end_date"));
                    p.setWrittenDate(rs.getDate("written_date"));
                    p.setBrandName(oscar.Misc.getString(rs, "BN"));
                    p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                    p.setCustomName(oscar.Misc.getString(rs, "customName"));
                    p.setTakeMin(rs.getFloat("takemin"));
                    p.setTakeMax(rs.getFloat("takemax"));
                    p.setFrequencyCode(oscar.Misc.getString(rs, "freqcode"));
                    p.setDuration(oscar.Misc.getString(rs, "duration"));
                    p.setDurationUnit(oscar.Misc.getString(rs, "durunit"));
                    p.setQuantity(oscar.Misc.getString(rs, "quantity"));
                    p.setRepeat(rs.getInt("repeat"));
                    p.setLastRefillDate(rs.getDate("last_refill_date"));
                    p.setNosubs(rs.getInt("nosubs"));
                    p.setPrn(rs.getInt("prn"));
                    p.setSpecial(oscar.Misc.getString(rs, "special"));
                    p.setGenericName(oscar.Misc.getString(rs, "GN"));
                    p.setAtcCode(oscar.Misc.getString(rs, "ATC"));
                    p.setScript_no(oscar.Misc.getString(rs, "script_no"));
                    p.setRegionalIdentifier(oscar.Misc.getString(rs, "regional_identifier"));
                    p.setUnit(oscar.Misc.getString(rs, "unit"));
                    p.setUnitName(oscar.Misc.getString(rs, "unitName"));
                    p.setMethod(oscar.Misc.getString(rs, "method"));
                    p.setRoute(oscar.Misc.getString(rs, "route"));
                    p.setDrugForm(oscar.Misc.getString(rs, "drug_form"));
                    p.setCustomInstr(rs.getBoolean("custom_instructions"));
                    p.setDosage(oscar.Misc.getString(rs, "dosage"));
                    p.setLongTerm(rs.getBoolean("long_term"));
                    p.setCustomNote(rs.getBoolean("custom_note"));
                    p.setPastMed(rs.getBoolean("past_med"));
                    p.setStartDateUnknown(rs.getBoolean("start_date_unknown"));
                    p.setComment(rs.getString("comment"));
                    if (rs.getObject("patient_compliance")==null) p.setPatientCompliance(null);
                    else p.setPatientCompliance(rs.getBoolean("patient_compliance"));
                    p.setOutsideProviderName(oscar.Misc.getString(rs, "outside_provider_name"));
                    p.setOutsideProviderOhip(oscar.Misc.getString(rs, "outside_provider_ohip"));
                    p.setHideCpp(rs.getBoolean("hide_cpp"));
                    p.setPickupDate(rs.getDate("pickup_datetime"));
                    p.setPickupTime(rs.getDate("pickup_datetime"));
                    p.setETreatmentType(rs.getString("eTreatmentType"));
                    p.setRxStatus(rs.getString("rxStatus"));
                    if (rs.getObject("dispense_interval")!=null) p.setDispenseInterval(rs.getInt("dispense_interval"));
                    if (rs.getObject("refill_duration")!=null) p.setRefillDuration(rs.getInt("refill_duration"));
                    if (rs.getObject("refill_quantity")!=null) p.setRefillQuantity(rs.getInt("refill_quantity"));
                    if (myOscarEnabled) {
                        String tmp = indivoSql.replaceFirst("\\?", oscar.Misc.getString(rs, "drugid"));
                        rs2 = DBHandler.GetSQL(tmp);

                        if (rs2.next()) {
                            p.setIndivoIdx(rs2.getString("indivoDocIdx"));
                            if (p.getIndivoIdx() != null && p.getIndivoIdx().length() > 0) p.setRegisterIndivo();
                            }
                        rs2.close();
                    }
                    p.setPosition(rs.getInt("position"));
                    lst.add(p);
                }
            }

            rs.close();

            arr = (Prescription[]) lst.toArray(arr);

        } catch (SQLException e) {
            logger.error("unexpected error", e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return arr;
    }

    public Favorite[] getFavorites(String providerNo) {
        Favorite[] arr = {};
        LinkedList lst = new LinkedList();

        try {

            ResultSet rs;
            Favorite favorite;

            rs = DBHandler.GetSQL("SELECT * FROM favorites WHERE provider_no = '" + providerNo + "' ORDER BY favoritename");

            while (rs.next()) {
                favorite = new Favorite(rs.getInt("favoriteid"), oscar.Misc.getString(rs, "provider_no"), oscar.Misc.getString(rs, "favoritename"), oscar.Misc.getString(rs, "BN"), rs.getInt("GCN_SEQNO"), oscar.Misc.getString(rs, "customName"), rs.getFloat("takemin"), rs.getFloat("takemax"), oscar.Misc.getString(rs, "freqcode"), oscar.Misc.getString(rs, "duration"), oscar.Misc.getString(rs, "durunit"), oscar.Misc.getString(rs, "quantity"), rs.getInt("repeat"), rs.getInt("nosubs"), rs.getInt("prn"), oscar.Misc.getString(rs, "special"),
                        oscar.Misc.getString(rs, "GN"), oscar.Misc.getString(rs, "ATC"), oscar.Misc.getString(rs, "regional_identifier"), oscar.Misc.getString(rs, "unit"), oscar.Misc.getString(rs, "unitName"), oscar.Misc.getString(rs, "method"), oscar.Misc.getString(rs, "route"), oscar.Misc.getString(rs, "drug_form"),
                        rs.getBoolean("custom_instructions"), oscar.Misc.getString(rs, "dosage"));

                lst.add(favorite);
            }

            rs.close();

            arr = (Favorite[]) lst.toArray(arr);

        } catch (SQLException e) {
            logger.error("unexpected error", e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return arr;
    }

    public Favorite getFavorite(int favoriteId) {
        Favorite favorite = null;

        try {

            ResultSet rs;

            rs = DBHandler.GetSQL("SELECT * FROM favorites WHERE favoriteid = " + favoriteId);

            if (rs.next()) {
                favorite = new Favorite(rs.getInt("favoriteid"), oscar.Misc.getString(rs, "provider_no"), oscar.Misc.getString(rs, "favoritename"), oscar.Misc.getString(rs, "BN"), rs.getInt("GCN_SEQNO"), oscar.Misc.getString(rs, "customName"), rs.getFloat("takemin"), rs.getFloat("takemax"), oscar.Misc.getString(rs, "freqcode"), oscar.Misc.getString(rs, "duration"), oscar.Misc.getString(rs, "durunit"), oscar.Misc.getString(rs, "quantity"), rs.getInt("repeat"), rs.getInt("nosubs"), rs.getInt("prn"), oscar.Misc.getString(rs, "special"),
                        oscar.Misc.getString(rs, "GN"), oscar.Misc.getString(rs, "ATC"), oscar.Misc.getString(rs, "regional_identifier"), oscar.Misc.getString(rs, "unit"), oscar.Misc.getString(rs, "unitName"), oscar.Misc.getString(rs, "method"), oscar.Misc.getString(rs, "route"), oscar.Misc.getString(rs, "drug_form"),
                        rs.getBoolean("custom_instructions"), oscar.Misc.getString(rs, "dosage"));
            }

            rs.close();

        } catch (SQLException e) {
            logger.error("unexpected error", e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return favorite;
    }

    public boolean deleteFavorite(int favoriteId) {
        boolean ret = false;

        try {

            String sql = "DELETE FROM favorites WHERE favoriteid = " + favoriteId;

            DBHandler.RunSQL(sql);
            DbConnectionFilter.getThreadLocalDbConnection().close();
            ret = true;
        } catch (SQLException e) {
            logger.error("unexpected error", e);
        }

        return ret;
    }

    /**
     * This function is used to save a set of prescribed drugs to as one prescription. This is for historical purposes
     *
     * @param bean This is the oscarRx session bean
     * @return This returns the insert id of the script to be included the drugs table
     */
    public String saveScript(oscar.oscarRx.pageUtil.RxSessionBean bean) {
        /*
         * create table prescription ( script_no int(10) auto_increment primary key, provider_no varchar(6), demographic_no int(10), date_prescribed date, date_printed date, dates_reprinted text, textView text);
         */
        String provider_no = bean.getProviderNo();
        int demographic_no = bean.getDemographicNo();
        String date_prescribed = oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "yyyy/MM/dd");
        String date_printed = date_prescribed;

        StringBuilder textView = new StringBuilder();
        String retval = null;

        // ///create full text view
        oscar.oscarRx.data.RxPatientData.Patient patient = null;
        oscar.oscarRx.data.RxProviderData.Provider provider = null;
        try {
            patient = RxPatientData.getPatient(demographic_no);
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
        textView.append(oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy") + "\n");

        String txt;
        for (int i = 0; i < bean.getStashSize(); i++) {
            Prescription rx = bean.getStashItem(i);

            String fullOutLine = rx.getFullOutLine();
            if (fullOutLine == null || fullOutLine.length() < 6) {
                logger.error("Drug full outline appears to be null or empty : " + fullOutLine, new IllegalStateException("full out line appears wrong"));
            }

            txt = fullOutLine.replaceAll(";", "\n");
            textView.append("\n" + txt);
        }
        // textView.append();

        String sql = " insert into prescription " + " (provider_no,demographic_no,date_prescribed,date_printed,textView,lastUpdateDate) " + " values " + " ( '" + provider_no + "', " + "   '" + demographic_no + "', " + "   '" + date_prescribed + "', " + "   '" + date_printed + "', " + "   '" + StringEscapeUtils.escapeSql(textView.toString()) + "', now()) ";
        try {



            DBHandler.RunSQL(sql);

            ResultSet rs;
            // NEEDS TO BE UPDATED FOR POSTGRES
            rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID() ");

            if (rs.next()) {
                retval = Integer.toString(rs.getInt(1));
            }
            DbConnectionFilter.getThreadLocalDbConnection().close();
        } catch (SQLException e) {
            logger.error("unexpected error", e);
        }

        logger.debug("INSERT INTO PRESCRIPTION " + retval);
        return retval;
    }

    public void setScriptComment(String scriptNo, String comment) {
        String sql = "update prescription set rx_comments = '" + StringEscapeUtils.escapeSql(comment) + "' where script_no = " + scriptNo;
        try {

            DBHandler.RunSQL(sql);
        } catch (SQLException e) {
            logger.error("unexpected error", e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
    }

    public String getScriptComment(String scriptNo) {
        String ret = null;
        String sql = "select rx_comments from  prescription where script_no = " + scriptNo;
        logger.debug("SQL " + sql);
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                ret = rs.getString("rx_comments");
            }
        } catch (SQLException e) {
            logger.error("unexpected error", e);
        } finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        return ret;
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
        int repeat = 0;
        java.util.Date lastRefillDate = null;
        boolean nosubs = false;
        boolean prn = false;
        boolean longTerm = false;
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
        private boolean discontinuedLatest=false;
        String special_instruction=null;
        private boolean durationSpecifiedByUser=false;
        private boolean customNote=false;
        boolean nonAuthoritative = false;
        String eTreatmentType = null;
        private boolean hideCpp=false;
        String rxStatus = null;
        private Integer refillDuration = 0;
        private Integer refillQuantity = 0;
        private Integer dispenseInterval = 0;
        private int position = 0;
        private String comment = null;

        private String drugFormList = "";
        private String datesReprinted = "";

        private List<String> policyViolations = new ArrayList<String>();

        private int drugReferenceId;
        
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

		public String getETreatmentType(){
        	return eTreatmentType;
        }

        public void setETreatmentType(String treatmentType){
        	eTreatmentType = treatmentType;
        }

        public String getRxStatus(){
        	return rxStatus;
        }

        public void setRxStatus(String status){
        	rxStatus = status;
        }

        public boolean isCustomNote(){
            return customNote;
        }
        public void setCustomNote(boolean b){
            customNote=b;
        }
        public boolean isMitte(){
            if(unitName!=null &&
                    (unitName.equalsIgnoreCase("D")||unitName.equalsIgnoreCase("W")||unitName.equalsIgnoreCase("M")||
                    unitName.equalsIgnoreCase("day")||unitName.equalsIgnoreCase("week")||unitName.equalsIgnoreCase("month")||
                    unitName.equalsIgnoreCase("days")||unitName.equalsIgnoreCase("weeks")||unitName.equalsIgnoreCase("months")||
                    unitName.equalsIgnoreCase("mo")))
                return true;
            else
                return false;
        }
        public boolean isDurationSpecifiedByUser(){
            return durationSpecifiedByUser;
        }
        public void setDurationSpecifiedByUser(boolean b){
            this.durationSpecifiedByUser=b;
        }
        public String getSpecialInstruction(){
            return special_instruction;
        }
        public void setSpecialInstruction(String s){
            special_instruction=s;
        }


        public boolean isLongTerm(){
            return longTerm;
        }



        public boolean isDiscontinuedLatest(){
            return this.discontinuedLatest;
        }
        public void setDiscontinuedLatest(boolean dl){
            this.discontinuedLatest=dl;
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

        public void setArchivedDate(Date ad){
            this.archivedDate=ad;
        }
        public Date getArchivedDate(){
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
            if (!tf.equals("0")) {
                this.archived = true;
            }
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
            if(this.isDiscontinued()) return this.archivedDate;
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

        public boolean isCurrent() {
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

        public void p(String s) {

        }

        public void p(String s, String s1) {

        }

        public void calcEndDate() {
            try{
            GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
            int days = 0;
                        p("in calcEndDate");
            //          p("this.getRxDate()",this.getRxDate().toString());
            cal.setTime(this.getRxDate());

            if (this.getDuration()!=null && this.getDuration().length() > 0) {
                if (Integer.parseInt(this.getDuration()) > 0) {
                    int i = Integer.parseInt(this.getDuration());
                    //      p("i",Integer.toString(i));
                    //      p("this.getDurationUnit()",this.getDurationUnit());
                        if (this.getDurationUnit()!=null && this.getDurationUnit().equalsIgnoreCase("D")) {
                        days = i;
                    }
                        if (this.getDurationUnit()!=null && this.getDurationUnit().equalsIgnoreCase("W")) {
                        days = i * 7;
                    }
                        if (this.getDurationUnit()!=null && this.getDurationUnit().equalsIgnoreCase("M")) {
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
            }catch(Exception e){
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
        public boolean isPrn(){//conventional name for getter of boolean variable
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

        public Boolean getPatientCompliance() {
            return this.patientCompliance;
        }

        public void setPatientCompliance(Boolean pc) {
            this.patientCompliance = pc;
        }

        public boolean getPatientCompliance(String YN) {
            if (YN==null || getPatientCompliance()==null) return false;

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
                logger.error("Some one is retrieving the drug special but it appears to be blank : " + special, new IllegalStateException());
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
                  logger.error("Some one is setting the drug special but it appears to be blank : " + special, new IllegalStateException());
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
                  logger.error("after processing the drug special but it appears to be blank : " + special, new IllegalStateException());
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
            return (RxPrescriptionData.getFullOutLine(getSpecial()));
        }

        public String getDosageDisplay() {
            String ret = "";
            if (this.getTakeMin() != this.getTakeMax()) {
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

                if (this.getTakeMin() != this.getTakeMax()) {
                    ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
                } else {
                    ret += this.getTakeMinString();
                }

                ret += " " + this.getFrequencyCode();

                if (this.getPrn()) {
                    ret += " PRN ";
                }
                ret += " " + this.getDuration() + " ";

                if (this.getDurationUnit().equals("D")) {
                    ret += "Day";
                }
                if (this.getDurationUnit().equals("W")) {
                    ret += "Week";
                }
                if (this.getDurationUnit().equals("M")) {
                    ret += "Month";
                }

                try{
                    if(this.getDuration()!=null&&this.getDuration().trim().length()==0){
                        this.setDuration("0");
                    }
                    if (this.getDuration()!=null && !this.getDuration().equalsIgnoreCase("null") && Integer.parseInt(this.getDuration()) > 1) {
                        ret += "s";
                    }
                }catch(Exception durationCalcException){
                    logger.error("Error with duration:",durationCalcException);
                }
                ret += "  ";
                ret += this.getQuantity();
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
            if (this.getTakeMin() != this.getTakeMax()) {
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
            	DrugDao drugDao=(DrugDao) SpringUtils.getBean("drugDao");
            	Drug drug=drugDao.find(getDrugId());
            	if (drug!=null)
            	{
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
            boolean ret = false;
            try {
                String update;
                if (isRegisteredIndivo()) {
                    update = "U";
                } else {
                    update = "I";
                }


                String sql = "INSERT INTO indivoDocs (oscarDocNo, indivoDocIdx, docType, dateSent, `update`)" + " VALUES(" + String.valueOf(getDrugId()) + ",'" + getIndivoIdx() + "','" + docType + "',now(),'" + update + "')";

                DBHandler.RunSQL(sql);
                ret = true;
            } catch (SQLException e) {
                logger.error("DATABASE ERROR: " + e.getMessage());
            }

            return ret;
        }

        public boolean Print() {
            boolean ret = false;
            try {

                ResultSet rs;
                String sql = "SELECT dates_reprinted, now() FROM prescription WHERE script_no = " + this.getScript_no();
                String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();

                rs = DBHandler.GetSQL(sql);

                if (rs.next()) {
                    String dates_reprinted = oscar.Misc.getString(rs, 1);
                    String now = oscar.Misc.getString(rs, 2);
                    if (dates_reprinted != null && dates_reprinted.length() > 0) {
                        dates_reprinted += "," + now + ";" + providerNo;
                    } else {
                        dates_reprinted = now + ";" + providerNo;
                    }

                    sql = "UPDATE prescription SET dates_reprinted = '" + dates_reprinted + "' WHERE script_no = " + this.getScript_no();
                    DBHandler.RunSQL(sql);
                    ret = true;

                    this.setNumPrints(this.getNumPrints() + 1);

                }

            } catch (SQLException e) {
                logger.error("unexpected error", e);
            }
            return ret;
        }

        public int getNextPosition() throws SQLException {
       	 String sql = "SELECT Max(position) FROM drugs WHERE demographic_no=" + this.getDemographicNo();
            ResultSet rs = DBHandler.GetSQL(sql);

            int position = 0;
            if (rs.next()) {
               position = rs.getInt(1);
            }
       	 return (position+1);
        }

        public boolean Save(String scriptId) {
            boolean b = false;
            //     p("inside Save now");
            //        p(RxUtil.DateToString(this.getEndDate()));
            // calculate end date
            this.calcEndDate();

            // clean up fields
            if (this.takeMin > this.takeMax) {
                this.takeMax = this.takeMin;
            }

            if (getSpecial() == null || getSpecial().length() < 6) {
                logger.error("drug special appears to be null or empty : " + getSpecial(), new IllegalStateException("Drug special is invalid."));
            }
          //  String parsedSpecial = RxUtil.replace(this.getSpecial(), "'", "");//a bug?
             String escapedSpecial = StringEscapeUtils.escapeSql(this.getSpecial());
          /*  if (parsedSpecial == null || parsedSpecial.length() < 6) {
                logger.error("drug special after parsing appears to be null or empty : " + parsedSpecial, new IllegalStateException("Drug special is invalid after parsing."));
            }*/
            if (escapedSpecial == null || escapedSpecial.length() < 6) {
                logger.error("drug special after escaping appears to be null or empty : " + escapedSpecial, new IllegalStateException("Drug special is invalid after escaping."));
            }
            try {

                ResultSet rs;
                String sql;

                // if drugid = 0 this is an add, else update
                if (this.getDrugId() == 0) {

                    // check to see if there is an identitical prescription in
                    // the database. If there is we'll return that drugid instead
                    // of adding a new prescription.

                	String endDate;
                	if (this.getEndDate() == null) {
                		endDate = "0001-01-01";
                	} else {
                		endDate = RxUtil.DateToString(this.getEndDate());
                	}

                    sql = "SELECT drugid FROM drugs WHERE archived = 0 AND " + "provider_no = '" + this.getProviderNo() + "' AND " + "demographic_no = " +
                            this.getDemographicNo() + " AND " + "rx_date = '" + RxUtil.DateToString(this.getRxDate()) + "' AND " + "end_date = '" +
                            RxUtil.DateToString(this.getEndDate()) + "' AND " + "written_date = '" + RxUtil.DateToString(this.getWrittenDate()) + "' AND " + "BN = '" +
                            StringEscapeUtils.escapeSql(this.getBrandName()) + "' AND " + "GCN_SEQNO = " + this.getGCN_SEQNO() + " AND " + "customName = '" + this.getCustomName() +
                            "' AND " + "takemin = " + this.getTakeMin() + " AND " + "takemax = " + this.getTakeMax() + " AND " + "freqcode = '" + this.getFrequencyCode() +
                            "' AND " + "duration = '" + this.getDuration() + "' AND " + "durunit = '" + this.getDurationUnit() + "' AND " + "quantity = '" + this.getQuantity() +
                            "' AND " + "unitName = '" + this.getUnitName() + "' AND " + "`repeat` = " + this.getRepeat() + " AND " + "last_refill_date = '" +
                            RxUtil.DateToString(this.getLastRefillDate()) + "' AND " + "nosubs = " + this.getNosubsInt() + " AND " + "prn = " + this.getPrnInt() + " AND " +
                            "special = '" +escapedSpecial+ "' AND " + "outside_provider_name = '" + this.getOutsideProviderName() + "' AND " +
                            "outside_provider_ohip = '" + this.getOutsideProviderOhip() + "' AND " + "custom_instructions = " + this.getCustomInstr() + " AND " + "long_term = " +
                            this.getLongTerm() +" AND " + "custom_note = " + this.isCustomNote() + " AND " + "past_med = " + this.getPastMed() + " AND " + "patient_compliance = " + this.getPatientCompliance()
                            +" AND "+" special_instruction = '"+this.getSpecialInstruction()+"' AND comment = '" + this.getComment() + "' AND start_date_unknown = " + this.getStartDateUnknown();
                    MiscUtils.getLogger().debug(sql);
                    rs = DBHandler.GetSQL(sql);

                    if (rs.next()) {
                        this.drugId = rs.getInt("drugid");
                    }

                    rs.close();

                    b = true;

                    // if it doesn't already exist add it.
                    if (this.getDrugId() == 0) {
                    	int position = this.getNextPosition();

                    	DrugDao drugDao=(DrugDao) SpringUtils.getBean("drugDao");
                    	Drug drug=new Drug();

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
                    	drug.setRepeat(getRepeat());
                    	drug.setLastRefillDate(getLastRefillDate());
                    	drug.setNoSubs(getNosubs());
                    	drug.setPrn(getPrn());
                    	drug.setSpecial(getSpecial());
                    	drug.setGenericName(getGenericName());
                    	drug.setScriptNo(Integer.parseInt(scriptId));
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
                    	drug.setCustomNote(isCustomNote());
                    	drug.setPastMed(getPastMed());
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

                    	drugDao.persist(drug);

                        drugId = drug.getId();

                        b = true;
                    }

                } else { // update the database

                	DrugDao drugDao=(DrugDao) SpringUtils.getBean("drugDao");
                	Drug drug=drugDao.find(getDrugId());

                    // create_date is not updated
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
                	drug.setRepeat(getRepeat());
                	drug.setLastRefillDate(getLastRefillDate());
                	drug.setNoSubs(getNosubs());
                	drug.setPrn(getPrn());
                	drug.setSpecial(getSpecial());
                	drug.setAtc(atcCode);
                	drug.setRegionalIdentifier(regionalIdentifier);
                	drug.setUnit(getUnit());
                	drug.setMethod(getMethod());
                	drug.setRoute(getRoute());
                	drug.setDrugForm(getDrugForm());
                	drug.setDosage(getDosage());
                	drug.setOutsideProviderName(getOutsideProviderName());
                	drug.setOutsideProviderOhip(getOutsideProviderOhip());
                	drug.setCustomInstructions(getCustomInstr());
                	drug.setUnitName(getUnitName());
                	drug.setLongTerm(getLongTerm());
                	drug.setCustomNote(isCustomNote());
                	drug.setPastMed(getPastMed());
                	drug.setSpecialInstruction(getSpecialInstruction());
                	drug.setPatientCompliance(getPatientCompliance());
                	drug.setETreatmentType(getETreatmentType());
                	drug.setRxStatus(getRxStatus());
                	drug.setDispenseInterval(getDispenseInterval());
                	drug.setRefillQuantity(getRefillQuantity());
                	drug.setRefillDuration(getRefillDuration());
                	drug.setHideFromCpp(isHideCpp());
                	drug.setPosition(getPosition());
                	drug.setComment(comment);
                	drug.setStartDateUnknown(getStartDateUnknown());

                	drugDao.merge(drug);

                    b = true;
                }

                // close by conn
                DbConnectionFilter.getThreadLocalDbConnection().close();

            } catch (SQLException e) {
                logger.error("unexpected error", e);
            }

            return b;
        }

        public boolean AddToFavorites(String providerNo, String favoriteName) {
            Favorite fav = new Favorite(0, providerNo, favoriteName, this.getBrandName(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getRepeat(), this.getNosubsInt(), this.getPrnInt(), this.getSpecial(), this.getGenericName(), this.getAtcCode(), this.getRegionalIdentifier(), this.getUnit(), this.getUnitName(), this.getMethod(), this.getRoute(),
                    this.getDrugForm(), this.getCustomInstr(), this.getDosage());

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
         * @param unit New value of property unitName.
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
         * @param boolean value for custom
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

        public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, int repeat, int nosubs,
                int prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
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

        public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, int repeat, boolean nosubs,
                boolean prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
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

        public boolean Save() {
            boolean b = false;

            // clean up fields
            if (this.takeMin > this.takeMax) {
                this.takeMax = this.takeMin;
            }
if (getSpecial() == null || getSpecial().length() < 4) {
            //if (getSpecial() == null || getSpecial().length() < 6) {
                logger.error("drug special appears to be null or empty : " + getSpecial(), new IllegalStateException("Drug special is invalid."));
            }
            String parsedSpecial = RxUtil.replace(this.getSpecial(), "'", "");
            //if (parsedSpecial == null || parsedSpecial.length() < 6) {
                if (parsedSpecial == null || parsedSpecial.length() < 4) {
                logger.error("drug special after parsing appears to be null or empty : " + parsedSpecial, new IllegalStateException("Drug special is invalid after parsing."));
            }

            try {

                ResultSet rs;
                String sql;

                if (this.getFavoriteId() == 0) {
                    sql = "SELECT favoriteid FROM favorites WHERE " + "provider_no = '" + this.getProviderNo() + "' AND " + "favoritename = '" + StringEscapeUtils.escapeSql(this.getFavoriteName()) + "' AND " + "BN = '" + StringEscapeUtils.escapeSql(this.getBN()) + "' AND " + "GCN_SEQNO = " + this.getGCN_SEQNO() + " AND " + "customName = '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "' AND " + "takemin = " + this.getTakeMin() + " AND " + "takemax = " + this.getTakeMax() + " AND " + "freqcode = '" + this.getFrequencyCode() + "' AND " + "duration = '" + this.getDuration() + "' AND " + "durunit = '" + this.getDurationUnit() + "' AND " + "quantity = '" + this.getQuantity() + "' AND " + "`repeat` = " + this.getRepeat() + " AND " + "nosubs = " + this.getNosubsInt() + " AND " + "prn = " + this.getPrnInt() + " AND " + "special = '" + StringEscapeUtils.escapeSql(parsedSpecial) + "' AND " + "GN = '" + this.getGN() + "' AND " + "unitName = '" + this.getUnitName() + "' AND " + "custom_instructions = " + this.getCustomInstr();

                    rs = DBHandler.GetSQL(sql);

                    if (rs.next()) {
                        this.favoriteId = rs.getInt("favoriteid");
                    }

                    rs.close();

                    b = true;

                    if (this.getFavoriteId() == 0) {
                        sql = "INSERT INTO favorites (provider_no, favoritename, " + "BN, GCN_SEQNO, customName, takemin, takemax, " + "freqcode, duration, durunit, quantity, " + "`repeat`, nosubs, prn, special,GN,ATC,regional_identifier,unit,unitName,method,route,drug_form,custom_instructions,dosage) " + "VALUES ('" + this.getProviderNo() + "', '" + StringEscapeUtils.escapeSql(this.getFavoriteName()) + "', '" + StringEscapeUtils.escapeSql(this.getBN()) + "', " + this.getGCN_SEQNO() + ", '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "', " + this.getTakeMin() + ", " + this.getTakeMax() + ", '" + this.getFrequencyCode() + "', '" + this.getDuration() + "', '" + this.getDurationUnit() + "', '" + this.getQuantity() + "', " + this.getRepeat() + ", " + this.getNosubsInt() + ", " + this.getPrnInt() + ", '" + parsedSpecial + "', '" + this.getGN() + "', ' " + this.getAtcCode() + "', '" + this.getRegionalIdentifier() + "', '" + this.getUnit() + "', '" + this.getUnitName() + "', '" + this.getMethod() + "', '" + this.getRoute() + "', '" + this.getDrugForm() + "', " + this.getCustomInstr() + ", '" + this.getDosage() + "')";

                        DBHandler.RunSQL(sql);

                        sql = "SELECT Max(favoriteid) FROM favorites";

                        rs = DBHandler.GetSQL(sql);

                        if (rs.next()) {
                            this.favoriteId = rs.getInt(1);
                        }

                        rs.close();

                        b = true;
                    }

                } else {
                    sql = "UPDATE favorites SET " + "provider_no = '" + this.getProviderNo() + "', " + "favoritename = '" + this.getFavoriteName() + "', " + "BN = '" + StringEscapeUtils.escapeSql(this.getBN()) + "', " + "GCN_SEQNO = " + this.getGCN_SEQNO() + ", " + "customName = '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "', " + "takemin = " + this.getTakeMin() + ", " + "takemax = " + this.getTakeMax() + ", " + "freqcode = '" + this.getFrequencyCode() + "', " + "duration = '" + this.getDuration() + "', " + "durunit = '" + this.getDurationUnit() + "', " + "quantity = '" + this.getQuantity() + "', " + "`repeat` = " + this.getRepeat() + ", " + "nosubs = " + this.getNosubsInt() + ", " + "prn = " + this.getPrnInt() + ", " + "special = '" + parsedSpecial + "', " + "GN = '" + this.getGN() + "', " + "ATC = '" + this.getAtcCode() + "', " + "regional_identifier = '" + this.getRegionalIdentifier() + "', " + "unit = '" + this.getUnit() + "', " + "unitName = '" + this.getUnitName() + "', " + "method = '" + this.getMethod() + "', " + "route = '" + this.getRoute() + "', " + "drug_form = '" + this.getDrugForm() + "', " + "custom_instructions = " + this.getCustomInstr() + ", " + "dosage = '" + this.getDosage() + "' " + "WHERE favoriteid = " + this.getFavoriteId();

                    DBHandler.RunSQL(sql);

                    b = true;
                }

            } catch (SQLException e) {
                logger.error("unexpected error", e);
            } finally {
                DbConnectionFilter.releaseThreadLocalDbConnection();
            }

            return b;
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
        Favorite fav = new Favorite(0, providerNo, favoriteName, drug.getBrandName(), drug.getGcnSeqNo(), drug.getCustomName(), drug.getTakeMin(), drug.getTakeMax(), drug.getFreqCode(), drug.getDuration(), drug.getDurUnit(), drug.getQuantity(), drug.getRepeat(), drug.isNoSubs(), drug.isPrn(), drug.getSpecial(), drug.getGenericName(), drug.getAtc(), drug.getRegionalIdentifier(), drug.getUnit(), drug.getUnitName(), drug.getMethod(), drug.getRoute(),
        		drug.getDrugForm(), drug.isCustomInstructions(), drug.getDosage());

        return fav.Save();
    }

    @Override
    public String toString()
	{
		return(ReflectionToStringBuilder.toString(this));
	}

}
