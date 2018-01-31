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

import org.oscarehr.common.model.Favorite;
import org.oscarehr.managers.DrugLookUp;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.FavoriteTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converts between domain Favorite and FavoriteTo1 objects.
 *
 * This class represents the transformation between a the SQL schema
 * and the data model that is presented to a client.
 */
@Component
public class FavoriteConverter extends AbstractConverter<Favorite, FavoriteTo1> {

    @Autowired
    protected DrugLookUp drugLookUpManager;

    /**
     * Converts from a transfer object to a Drug domain object.
     *
     * @param loggedInInfo information regarding the current logged in user.
     * @param t            the transfer object to copy the data from
     * @return a Favorite domain object representing the a favorite drug.
     * @throws ConversionException if conversion did not complete properly.
     */
    @Override
    public Favorite getAsDomainObject(LoggedInInfo loggedInInfo, FavoriteTo1 t) throws ConversionException {

        Favorite f = new Favorite();

        // Copy fields from DrugTo1 object
        // over to the new Drug object.
        // This is not quite a one-to-one copy, some transformation
        // is done on types.

        try {

            if(f.getProviderNo() != null) {
                f.setProviderNo(t.getProviderNo().toString());
            }else{
                f.setProviderNo(loggedInInfo.getLoggedInProviderNo());
            }

            f.setName(t.getFavoriteName());
            f.setBn(t.getBrandName());
            f.setGn(t.getGenericName());
            f.setRegionalIdentifier(t.getRegionalIdentifier());
            f.setAtc(t.getAtc());
            f.setDrugForm(t.getForm());

            f.setSpecial(t.getInstructions());
            f.setMethod(t.getMethod());
            f.setTakeMax(t.getTakeMax());
            f.setTakeMin(t.getTakeMin());
            f.setRoute(t.getRoute());
            f.setFrequencyCode(t.getFrequency());
            f.setPrn(t.getPrn());

            f.setDuration(t.getDuration().toString());
            f.setDurationUnit(t.getDurationUnit());

            if(t.getRepeats() != null){
                f.setRepeat(t.getRepeats());
            }


            if(t.getQuantity() != null) {
                f.setQuantity(t.getQuantity().toString());
            }

        }catch(RuntimeException re){

            throw new ConversionException();

        }

        return f;

    }

    /**
     * Converts from the Drug domain model object to a serializable Drug transfer object.
     *
     * @param loggedInInfo information for the logged in user (unused).
     * @param f            the Favorite drug domain object to convert from.
     * @return a serializable transfer object that represents the Favorite object
     * @throws ConversionException if the conversion fails.
     */
    @Override
    public FavoriteTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Favorite f) throws ConversionException {

        FavoriteTo1 t = new FavoriteTo1();

        // Copy over the fields from the Drug to the new transfer object.
        // This is not a one-to-one mapping, some transformation is
        // done on types.

        // medication fields
        t.setId(f.getId());
        t.setProviderNo(Integer.parseInt(f.getProviderNo()));
        t.setFavoriteName(f.getName());
        t.setBrandName(f.getBn());
        t.setGenericName(f.getGn());
        t.setRegionalIdentifier(f.getRegionalIdentifier());
        t.setAtc(f.getAtc());
        t.setForm(f.getDrugForm());

        // instruction fields
        t.setInstructions(f.getSpecial());
        t.setMethod(f.getMethod());
        t.setTakeMax(f.getTakeMax());
        t.setTakeMin(f.getTakeMin());
        t.setRoute(f.getRoute());
        t.setFrequency(f.getFrequencyCode());
        t.setPrn(f.isPrn());

        // prescription fields
        t.setDuration(Integer.parseInt(f.getDuration()));
        t.setDurationUnit(f.getDurationUnit());
        t.setRepeats(f.getRepeat());

        if(f.getQuantity() != null) t.setQuantity(Integer.parseInt(f.getQuantity()));

        return t;

    }

}
