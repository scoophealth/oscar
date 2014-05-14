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

package org.oscarehr.sharingcenter.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.marc.shic.cda.datatypes.Code;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DrugReasonDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.util.SpringUtils;

public class MedicationUtil {

    private static final DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
    private static final DrugReasonDao drugReasonDao = SpringUtils.getBean(DrugReasonDao.class);

    public static final String ATC = "ATC";
    public static final String BRAND_NAME = "Brand Name";
    public static final String DOSAGE = "Dosage";
    public static final String GENERIC_NAME = "Generic Name";
    public static final String ORDER_DATE = "Order Date";
    public static final String QUANTITY = "Quantity";
    public static final String REPEAT = "Repeat";
    public static final String ROUTE = "Route of Administration";
    public static final String SPECIAL = "Special";

    public static final String[] COLUMNS = {ORDER_DATE, GENERIC_NAME, BRAND_NAME, DOSAGE, QUANTITY, REPEAT, ROUTE, SPECIAL, ATC};

    private MedicationUtil() {
    }

    public static Code getRouteCodeForMedication(Drug med) {

        return new Code(med.getRoute(), "2.16.840.1.113883.5.112", null, "RouteOfAdministration");
    }

    public static Code getDrugCode(Drug drug) {

        return new Code(drug.getAtc(), "2.16.840.1.113883.6.77", drug.getBrandName(), "ATC");
    }

    /**
     * This method returns all the Drugs as a List associated with the
     * demographicId given.
     *
     * @param demographicId
     * @return
     */
    public static List<Drug> getMedications(int demographicId) {
        return getMedications(demographicId, false);
    }

    /**
     * This method returns all the Drugs as a List associated with the
     * demographicId given and whether it's been archived or not.
     *
     * @param demographicId	Not Null
     * @param archived TRUE = not active, FALSE = active, null = all
     * @return
     */
    public static List<Drug> getMedications(int demographicId, boolean archived) {
        return drugDao.findByDemographicId(demographicId, archived);
    }

    /**
     * This method returns all the Drugs as a List associated with the
     * demographicId given, whether it's been archived or not, and in descending
     * order by rxDate.
     *
     * @param demographicId	Not Null
     * @param archived TRUE = not active, FALSE = active, null = all
     * @return
     */
    public static List<Drug> getMedicationsOrderByDate(int demographicId, boolean archived) {
        //Gets the list of drugs
        List<Drug> drugList = getMedications(demographicId, archived);
        //Sorts the list of drugs by rxDate in descending order. 
        List<Drug> drugSortedList = new ArrayList<Drug>();
        for (int i = 0; i < drugList.size(); i++) {
            Drug toBeInserted = new Drug();
            toBeInserted.setRxDate(new Date(0));
            for (Drug aDrug : drugList) {
                if (aDrug.getRxDate().after(toBeInserted.getRxDate())) {
                    toBeInserted = aDrug;
                }
            }
            drugSortedList.add(i, toBeInserted);
            drugList.remove(toBeInserted);
        }
        return drugSortedList;
    }

    /**
     * This method returns all the Drugs as a List associated with the ATC
     * given.
     *
     * @param ATC
     * @return
     */
    public static List<Drug> getMedicationsByATC(String ATC) {
        return drugDao.findByAtc(ATC);
    }

    /**
     * This method returns all the Drugs as a List associated with the
     * demographicId given and the ATC given.
     *
     * @param demographicId Not Null
     * @param ATC
     * @return
     */
    public static List<Drug> getMedicationsByATC(int demographicId, String ATC) {
        return drugDao.findByDemographicIdAndAtc(demographicId, ATC);
    }

    /**
     * This method returns all the Drugs as a List associated with the
     * demographicId given and the drugId given.
     *
     * @param demographicId Not Null
     * @param drugId
     * @return
     */
    public static List<Drug> getMedicationsByDrug(int demographicId, int drugId) {
        return drugDao.findByDemographicIdAndDrugId(demographicId, drugId);
    }

    /**
     * This method returns all the Drugs as a List associated with demographicId
     * given and the region identifier given.
     *
     * @param demographicId Not Null
     * @param regionalIdentifier
     * @return
     */
    public static List<Drug> getMedicationsByRegion(int demographicId, String regionalIdentifier) {
        return drugDao.findByDemographicIdAndRegion(demographicId, regionalIdentifier);
    }

    /**
     * This method returns all the Drugs as a List associated with
     * prescriptionID given.
     *
     * @param prescriptionId
     * @return
     */
    public static List<Drug> getMedicationsByPrescription(int prescriptionId) {
        return drugDao.findByPrescriptionId(prescriptionId);
    }

    /**
     * This method returns all the Drugs as a List associated with demographicId
     * given and the prescriptionID given.
     *
     * @param demographicId
     * @param prescriptionId
     * @return
     */
    public static List<Drug> getMedicationsByPrescription(int demographicId, int prescriptionId) {
        List<Drug> drugList = getMedicationsByPrescription(prescriptionId);
        for (Drug a : drugList) {
            if (a.getDemographicId() == demographicId) {
                drugList.remove(a);
            }
        }
        return drugList;
    }

    /**
     * This method returns all the DrugReasons as a List associated with drugId
     * given.
     *
     * @param drugId
     * @return
     */
    public static List<DrugReason> getMedicationReasons(int drugId) {
        return drugReasonDao.getReasonsForDrugID(drugId, true);
    }

}
