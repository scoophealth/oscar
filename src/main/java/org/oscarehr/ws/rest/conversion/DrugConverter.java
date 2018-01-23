/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.DrugLookUp;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.DrugSearchTo1;
import org.oscarehr.ws.rest.to.model.DrugTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts between domain Drug object and transfer Drug objects.
 *
 * This class represents the transformation between a the SQL schema
 * and the data model that is presented to a client.
 */
@Component
public class DrugConverter extends AbstractConverter<Drug, DrugTo1> {

    @Autowired
    protected DrugLookUp drugLookUpManager;

    /**
     * Converts from a transfer object to a Drug domain object.
     *
     * @param loggedInInfo information regarding the current logged in user.
     * @param t            the transfer object to copy the data from
     * @return a Drug domain object representing this data.
     * @throws ConversionException if conversion did not complete properly.
     */
    @Override
    public Drug getAsDomainObject(LoggedInInfo loggedInInfo, DrugTo1 t) throws ConversionException {

        Drug d = new Drug();

        // Copy fields from DrugTo1 object
        // over to the new Drug object.
        // This is not quite a one-to-one copy, some transformation
        // is done on types.

        try {

            d.setId(t.getDrugId());
            d.setBrandName(t.getBrandName());
            d.setGenericName(t.getGenericName());
            d.setDemographicId(t.getDemographicNo());
            d.setProviderNo(t.getProviderNo().toString()); // Cast to string.
            d.setAtc(t.getAtc());
            d.setRegionalIdentifier(t.getRegionalIdentifier());
            d.setDosage(t.getStrength() + t.getStrengthUnit());
            d.setTakeMax(t.getTakeMax());
            d.setTakeMin(t.getTakeMin());
            d.setRxDate(t.getRxDate());
            d.setEndDate(t.getEndDate());
            d.setFreqCode(t.getFrequency());
            d.setDuration(t.getDuration().toString()); // Cast to string.
            d.setDurUnit(t.getDurationUnit());
            d.setRepeat(t.getRepeats());
            d.setSpecial(t.getInstructions());
            d.setArchived(t.isArchived());
            d.setArchivedReason(t.getArchivedReason());
            d.setArchivedDate(t.getArchivedDate());
            d.setRoute(t.getRoute());
            d.setDrugForm(t.getForm());
            d.setMethod(t.getMethod());
            d.setPrn(t.isPrn());
            d.setLongTerm(t.getLongTerm());
            d.setNoSubs(t.getNoSubstitutions());
            d.setPosition(1);
            d.setOutsideProviderName(t.getExternalProvider());
            d.setSpecialInstruction(t.getAdditionalInstructions());

            if(t.getQuantity() != null){
                d.setQuantity(t.getQuantity().toString());
            }


            populateDrugStrength(d, t);

        }catch(RuntimeException re){

            throw new ConversionException();

        }

        return d;

    }

    /**
     * Converts from the Drug domain model object to a serializable Drug transfer object.
     *
     * @param loggedInInfo information for the logged in user (unused).
     * @param d            the Drug domain object to convert from.
     * @return a serializable transfer object that represents the Drug object
     * @throws ConversionException if the conversion fails.
     */
    @Override
    public DrugTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Drug d) throws ConversionException {

        DrugTo1 t = new DrugTo1();

        // Copy over the fields from the Drug to the new transfer object.
        // This is not a one-to-one mapping, some transformation is
        // done on types.

        t.setDrugId(d.getId());
        t.setBrandName(d.getBrandName());
        t.setGenericName(d.getGenericName());
        t.setAtc(d.getAtc());
        t.setRegionalIdentifier(d.getRegionalIdentifier());
        t.setDemographicNo(d.getDemographicId());
        t.setProviderNo(Integer.parseInt(d.getProviderNo())); // Parse the providerNo string to an int.
        t.setTakeMin(d.getTakeMin());
        t.setTakeMax(d.getTakeMax());
        t.setRxDate(d.getRxDate());
        t.setEndDate(d.getEndDate());
        t.setFrequency(d.getFreqCode());
        t.setDuration(Integer.parseInt(d.getDuration()));   // Parse the duration string to an int.
        t.setDurationUnit(d.getDurUnit());
        t.setRoute(d.getRoute());
        t.setForm(d.getDrugForm());
        t.setPrn(d.isPrn());
        t.setMethod(d.getMethod());
        t.setRepeats(d.getRepeat());
        t.setInstructions(d.getSpecial());
        t.setPrn(d.isPrn());
        t.setNoSubstitutions(d.isNoSubs());
        t.setLongTerm(d.isLongTerm());
        t.setArchived(d.isArchived());
        t.setArchivedDate(d.getArchivedDate());
        t.setArchivedReason(d.getArchivedReason());
        t.setExternalProvider(d.getOutsideProviderName());
        t.setAdditionalInstructions(d.getSpecialInstruction());

        if(d.getQuantity() != null){
            t.setQuantity(Integer.parseInt(d.getQuantity()));
        }

        this.populateTo1Strength(t, d);


        return t;
    }

    /**
     * Attempts to populate the strength and strength unit fields of the transfer object
     * based on the info in the drug object. Will not overwrite the strength fields if they
     * are already set.
     *
     * @param t a transfer object to populated.
     * @param d a drug object to use as a reference.
     *
     * @return true if populated successfully, false otherwise.
     */
    protected Boolean populateTo1Strength(DrugTo1 t, Drug d) {

        if(t.getStrength() != null || (t.getStrengthUnit() != null && !t.getStrengthUnit().isEmpty())) {
            // check that the strength is not already set.
            return false;
        }

        if (d.getBrandName() == null || d.getBrandName().isEmpty()) return false;

        try {

            List<DrugSearchTo1> matchedDrugs = this.drugLookUpManager.search(d.getBrandName());
            DrugSearchTo1 details = null;

            if (matchedDrugs.size() >= 1) {

                // get the details for the drug in question
                details = this.drugLookUpManager.details(matchedDrugs.get(0).getId().toString());

                if(details != null){
                    t.setStrength(details.getComponents().get(0).getStrength().floatValue());
                    t.setStrengthUnit(details.getComponents().get(0).getUnit());
                    return true;
                }else{
                    return false;
                }

            } else {
                // if we did not find a matching drug ignore, not strength populated.
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Attempts to populate the dosage field of the drug object given a transfer object.
     *
     * @param d the drug to populate
     * @param t the transfer object to use as a reference.
     *
     * @return true if successful, false otherwise.
     */
    protected boolean populateDrugStrength(Drug d, DrugTo1 t) {

        if (t.getStrengthUnit() != null && t.getStrength() != null) {

            d.setDosage(t.getStrength() + " " + t.getStrengthUnit());
            d.setUnit(t.getStrengthUnit());
            return true;

        } else {

            // if there is no strength we have to look it up in drug ref
            // we use the
            if (this.populateTo1Strength(t, d)) {

                d.setDosage(t.getStrength() + " " + t.getStrengthUnit());
                d.setUnit(t.getStrengthUnit());
                return true;

            } else {

                return false;

            }

        }
    }

}
