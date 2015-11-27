/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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


package org.oscarehr.oscarRx.erx.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.oscarehr.common.model.Drug;
import org.oscarehr.oscarRx.erx.model.ERxPrescription;

import oscar.oscarRx.util.RxDrugRef;
import oscar.oscarRx.util.RxUtil;

/**
 * An object that translates between Prescription objects and ERxPrescription
 * objects.
 * 
 * FUTURE: org.oscarehr.oscarRx.erx should, in the future, support more than one
 * external prescription service, which is unlikely to use the current
 * prescription data model. When this support is added, this class should be
 * renamed to org.oscarehr.oscarRx.erx.PrescriptionTranslator,
 * which should implement a new interface named
 * org.oscarehr.oscarRx.erx.controller.ERxPrescriptionTranslator. This new
 * interface should define two methods: ERxPrescription
 * translateToExternal(Prescription) and Prescription
 * translateToInternal(ERxPrescription). Then,
 * org.oscarehr.oscarRx.erx.PrescriptionTranslator will need to be
 * re-factored as such. Likely at this point, any lookups will be moved
 * internally.
 */
public class ERxPrescriptionTranslator {
    /*
     * Generate an ERxPrescription given a Prescription
     * 
     * @param prescription The prescription data to translate
     * 
     * @return The translated prescription data
     * 
     * 
     * public static ERxPrescription translateToExternal(Prescription
     * prescription) {
     * 
     * // Translate OSCAR prescription to ERxPrescription
     * 
     * return null;
     * 
     * }
     */

    /**
     * Generate a Prescription given an ERxPrescription.
     * 
     * @param in
     *            The external prescription to translate.
     * @param providerId
     *            The ID number of the provider issuing the prescription.
     * @param patientId
     *            The ID number of the patient having the prescription issued to
     *            them.
     * @param drugAtcCode
     *            The ATC code of the drug being prescribed.
     * @return The translated prescription.
     */
/*    public static Prescription translateToInternal(ERxPrescription in,
            String providerId, String patientId, String drugAtcCode) {
        Prescription out;
        SimpleDateFormat df = new SimpleDateFormat();
        StringBuilder commentBuilder = new StringBuilder();
        StringBuilder specialInstructionsBuilder = new StringBuilder();

        // Prepare comment fields so we can log non-critical import failures to them
        commentBuilder.append(in.getFormattedPrescriptionToString());

        // Initialize the answer
        out = new RxPrescriptionData().newPrescription(providerId,
                Integer.parseInt(patientId));
        out.setAtcCode(drugAtcCode);

        // Set fields that will be the same for every imported prescription
        out.setNonAuthoritative(true);

        // Set fields that directly correspond to each other
        out.setDiscontinued(in.isDCPrescription());
        out.setGenericName(in.getProductName());
        out.setBrandName(in.getProductName());
        out.setCustomName(in.getProductName());
        out.setDrugForm(in.getProductForm());
        out.setRegionalIdentifier(Long.toString(in.getDrugCode()));
        out.setDosage(in.getProductStrength());
        out.setRepeat(in.getRefills());
        out.setDurationUnit(in.getRefillsDurationTimeUnit().toString());
        specialInstructionsBuilder.append(in.getSigManual());
        out.setTakeMin(in.getSigDosage());
        out.setTakeMax(in.getSigDosage());
        out.setFrequencyCode(in.getSigFrequencyToString());
        out.setRoute(in.getSigSpecifierToString());
        out.setPrn(in.isSigPRN());
        out.setDurationSpecifiedByUser(in.isSigPRN());
        specialInstructionsBuilder.append(in.getInstructions());
        out.setRxStatus(in.getStatus());
        out.setLastArchReason(in.getCeasingReason());
        out.setNosubs(in.isDispenseAsWritten());

        // Set more-complex fields
        if ((in.getManualPrescriptionContent() != null)
                && !in.getManualPrescriptionContent().trim().isEmpty()) {
            out.setCustomInstr(true);
            commentBuilder.append(in.getManualPrescriptionContent());
        } else {
            out.setCustomInstr(false);
        }
        out.setDosage(Float.toString(in.getSigDosage()) + " "
                + in.getSigUnitToString());

        // As specified by Jay, round up when converting the float to an int
        out.setRefillQuantity(new Double(StrictMath.ceil(in.getQuantity()))
                .intValue());

        /*
         * Set the prescription dates.
         * 
         * Since the import script is designed to run nightly, if something goes
         * horribly wrong when parsing the dates returned by the external prescriber, we'll set
         * them to the current date and append a note.
         *
        try {
            out.setRxCreatedDate(df.parse(in.getPrescriptionDateTime()));
            out.setRxDate(df.parse(in.getPrescriptionDateTime()));
            out.setWrittenDate(df.parse(in.getPrescriptionDateTime()));
            out.setWrittenDateFormat(in.getPrescriptionDateTime());
            out.setStartDateUnknown(false);
        } catch (ParseException e) {
            // Get the current date
            Date now = new Date();
            // Log the failure
            commentBuilder
                    .append("\n[import error]: Could not parse prescription date "
                            + in.getPrescriptionDateTime()
                            + ". Setting to "
                            + now.toString() + " (the date of the import).");
            // Set the dates to a sane default value
            out.setRxCreatedDate(now);
            out.setRxDate(now);
            out.setWrittenDate(now);
            out.setWrittenDateFormat(now.toString());
            out.setStartDateUnknown(true);
        }

        /*
         * Set the estimated prescription end date.
         * 
         * If something goes wrong, we'll just leave it blank and append a note.
         *
        try {
            out.setEndDate(df.parse(in.getEndingDate()));
        } catch (ParseException e) {
            commentBuilder.append("\n[import error]: Could not parse end date "
                    + in.getEndingDate() + ". Leaving it blank.");
        }

        /*
         * Set fields that have duplicates and note any mismatches.
         * 
         * This section sets the OSCAR prescription fields from the external prescriber
         * fields common to all prescriptions, and adds information to the note
         * where the narcotic fields have unexpected values.
         *
        out.setQuantity(Float.toString(in.getQuantity()));
        if ((in.getNarcoticTotalQuantity() >= 0)
                && (in.getNarcoticTotalQuantity() != in.getQuantity())) {
            commentBuilder.append("\n[import error]: Narcotic quantity "
                    + Float.toString(in.getNarcoticTotalQuantity())
                    + " doesn't match quantity "
                    + Float.toString(in.getQuantity()) + "!");
        }
        out.setDuration(Integer.toString(in.getTreatmentDuration()));
        if ((in.getNarcoticInterval() >= 0)
                && (in.getNarcoticInterval() != in.getTreatmentDuration())) {
            commentBuilder.append("\n[import error]: Narcotic interval "
                    + Integer.toString(in.getNarcoticInterval())
                    + " doesn't match treatment duration "
                    + Integer.toString(in.getTreatmentDuration()) + "!");
        }
        out.setDurationUnit(in.getTreatmentDurationTimeUnit().toString());
        if ((in.getNarcoticInterval() >= 0)
                && (in.getNarcoticIntervalTimeUnit() != in
                        .getTreatmentDurationTimeUnit())) {
            commentBuilder
                    .append("\n[import error]: Narcotic interval time unit "
                            + in.getNarcoticIntervalTimeUnit().toString()
                            + " doesn't match treatment duration time unit "
                            + in.getTreatmentDurationTimeUnit().toString()
                            + "!");
        }
        switch (in.getRefillsDurationTimeUnit()) {
        case DAY:
            out.setRefillDuration(in.getRefillsDuration());
            break;
        case WEEK:
            out.setRefillDuration(in.getRefillsDuration() * 7);
        case MONTH:
            out.setRefillDuration(in.getRefillsDuration() * 30);
        case YEAR:
            out.setRefillDuration(in.getRefillsDuration() * 365);
        }
        out.setRefillDuration(in.getRefillsDuration()); // !!

        out.setSpecialInstruction(specialInstructionsBuilder.toString());

        // Set the comment field
        out.setComment(commentBuilder.toString()
                + "\n(Prescription imported from the External Prescriber on "
                + new Date().toString() + ")");

        return out;
    }*/

    /**
     * Generate a Prescription given an ERxPrescription.
     * 
     * @param in
     *            The external prescription to translate.
     * @param providerId
     *            The ID number of the provider issuing the prescription.
     * @param patientId
     *            The ID number of the patient having the prescription issued to
     *            them.
     * @param drugAtcCode
     *            The ATC code of the drug being prescribed.
     * @return The translated prescription (drug).
     */
    public static Drug translateToInternal(ERxPrescription in,
            String providerId, String patientId, String drugAtcCode) {
        Drug out = new Drug();
        SimpleDateFormat df = new SimpleDateFormat();
        StringBuilder commentBuilder = new StringBuilder();
        StringBuilder specialInstructionsBuilder = new StringBuilder();
        RxDrugRef drugLookup = new RxDrugRef();

        // Prepare comment fields so we can log non-critical import failures to them
        commentBuilder.append(in.getPrescriptionToString());
        
        out.setOutsideProviderName("External Rx");

        // Initialize the answer
        //out = new RxPrescriptionData().newPrescription(providerId,
        //        Integer.parseInt(patientId));
        out.setDemographicId(Integer.parseInt(patientId.trim()));
        out.setProviderNo(providerId);
        
        out.setAtc(drugAtcCode);

        // Set fields that will be the same for every imported prescription
        out.setNonAuthoritative(true);

        // Set fields that directly correspond to each other
        out.setPastMed(in.isDCPrescription());
        out.setGenericName(in.getProductName());
        out.setBrandName(in.getProductName());
        out.setCustomName(in.getProductName());
        out.setDrugForm(in.getProductForm());
        out.setRegionalIdentifier(Long.toString(in.getDrugCode()));
        out.setDosage(in.getProductStrength());
        out.setRepeat(in.getRefills());
        out.setDurUnit(in.getRefillsDurationTimeUnit().toString());
        specialInstructionsBuilder.append(in.getSigManual());
        out.setTakeMin(in.getSigDosage());
        out.setTakeMax(in.getSigDosage());
        out.setFreqCode(in.getSigFrequencyToString());
        out.setRoute(in.getSigSpecifierToString());
        out.setPrn(in.isSigPRN());
        specialInstructionsBuilder.append(in.getInstructions());
        out.setRxStatus(in.getStatus());
        out.setArchivedReason(in.getCeasingReason());
        out.setNoSubs(in.isDispenseAsWritten());
        out.setSpecial(in.getProductName());

        // Set more-complex fields
        if ((in.getManualPrescriptionContent() != null)
                && !in.getManualPrescriptionContent().trim().isEmpty()) {
            out.setCustomInstructions(true);
            commentBuilder.append(in.getManualPrescriptionContent());
        } else {
            out.setCustomInstructions(false);
        }
        out.setDosage(Float.toString(in.getSigDosage()) + " "
                + in.getSigUnitToString());

        // As specified by Jay, round up when converting the float to an int
        out.setRefillQuantity(new Double(StrictMath.ceil(in.getQuantity()))
                .intValue());

        /*
         * Set the prescription dates.
         * 
         * Since the import script is designed to run nightly, if something goes
         * horribly wrong when parsing the dates returned by the external prescriber, we'll set
         * them to the current date and append a note.
         */
        try {
            out.setCreateDate(df.parse(in.getPrescriptionDateTime()));
            out.setRxDate(df.parse(in.getPrescriptionDateTime()));
            out.setWrittenDate(df.parse(in.getPrescriptionDateTime()));
            out.setStartDateUnknown(false);
        } catch (ParseException e) {
            // Get the current date
            Date now = new Date();
            // Log the failure
            commentBuilder
                    .append("\n[import error]: Could not parse prescription date "
                            + in.getPrescriptionDateTime()
                            + ". Setting to "
                            + now.toString() + " (the date of the import).");
            // Set the dates to a sane default value
            out.setCreateDate(now);
            out.setRxDate(now);
            out.setWrittenDate(now);          
            out.setStartDateUnknown(true);
        }

        /*
         * Set the estimated prescription end date.
         * 
         * If something goes wrong, we'll just set it to 0001-01-01 and append a note.
         */
        try {
            out.setEndDate(df.parse(in.getEndingDate()));
        } catch (ParseException e) {
        	out.setEndDate(RxUtil.StringToDate("0001-01-01", "yyyy-MM-dd"));
            commentBuilder.append("\n[import error]: Could not parse end date "
                    + in.getEndingDate() + ". Setting it to 0001-01-01.");
        }

        /*
         * Set fields that have duplicates and note any mismatches.
         * 
         * This section sets the OSCAR prescription fields from the external prescriber
         * fields common to all prescriptions, and adds information to the note
         * where the narcotic fields have unexpected values.
         */
        out.setQuantity(Float.toString(in.getQuantity()));
        if ((in.getNarcoticTotalQuantity() >= 0)
                && (!(Math.abs(in.getNarcoticTotalQuantity() -in.getQuantity()) < 0.00000001 ))) {
            commentBuilder.append("\n[import error]: Narcotic quantity "
                    + Float.toString(in.getNarcoticTotalQuantity())
                    + " doesn't match quantity "
                    + Float.toString(in.getQuantity()) + "!");
        }
        out.setDuration(Integer.toString(in.getTreatmentDuration()));
        if ((in.getNarcoticInterval() >= 0)
                && (in.getNarcoticInterval() != in.getTreatmentDuration())) {
            commentBuilder.append("\n[import error]: Narcotic interval "
                    + Integer.toString(in.getNarcoticInterval())
                    + " doesn't match treatment duration "
                    + Integer.toString(in.getTreatmentDuration()) + "!");
        }
        out.setDurUnit(in.getTreatmentDurationTimeUnit().toString());
        if ((in.getNarcoticInterval() >= 0)
                && (in.getNarcoticIntervalTimeUnit() != in
                        .getTreatmentDurationTimeUnit())) {
            commentBuilder
                    .append("\n[import error]: Narcotic interval time unit "
                            + in.getNarcoticIntervalTimeUnit().toString()
                            + " doesn't match treatment duration time unit "
                            + in.getTreatmentDurationTimeUnit().toString()
                            + "!");
        }
        switch (in.getRefillsDurationTimeUnit()) {
        case DAY:
            out.setRefillDuration(in.getRefillsDuration());
            break;
        case WEEK:
            out.setRefillDuration(in.getRefillsDuration() * 7);
        case MONTH:
            out.setRefillDuration(in.getRefillsDuration() * 30);
        case YEAR:
            out.setRefillDuration(in.getRefillsDuration() * 365);
        }
        out.setRefillDuration(in.getRefillsDuration()); // !!

        out.setSpecialInstruction(specialInstructionsBuilder.toString());

        // Set the comment field
        out.setComment(commentBuilder.toString()
                + "\n(Prescription imported from the External Prescriber on "
                + new Date().toString() + ")");

        return out;
    }
}
