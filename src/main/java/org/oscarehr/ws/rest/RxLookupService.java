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

package org.oscarehr.ws.rest;

import org.apache.log4j.Logger;
import org.oscarehr.managers.DrugLookUp;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.DrugLookupResponse;
import org.oscarehr.ws.rest.to.DrugResponse;
import org.oscarehr.ws.rest.to.model.DrugSearchTo1;
import org.oscarehr.ws.rest.to.model.DrugTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that allows access to DrugRef, or other drug product
 * database.
 */
@Path("/rxlookup")
@Component("rxLookupService")
@Produces("application/xml")
public class RxLookupService extends AbstractServiceImpl {

    private static Logger logger = MiscUtils.getLogger();

    @Autowired
    protected SecurityInfoManager securityInfoManager;

    @Autowired
    protected DrugLookUp drugLookUpManager;

    /**
     * Performs a search in the drug product database for a drug that matches in input parameter.
     *
     * @param s a drug name to search in the drug product database.
     * @return response containing a list of DrugSearchTo1 objects representing the drugs that matched in the search.
     */
    @GET
    @Path("/search")
    @Produces("application/json")
    public DrugLookupResponse search(@QueryParam("string") String s) {

        DrugLookupResponse resp = new DrugLookupResponse();

        List<DrugSearchTo1> drugs;

        try {

            drugs = this.drugLookUpManager.search(s);

            if (drugs != null) {

                resp.setDrugs(drugs);
                resp.setSuccess(true);

            } else {

                resp.setMessage("Failed to find drugs that match: " + s);
                resp.setSuccess(false);

            }

        } catch (Exception e) {
            logger.error(e.getStackTrace());
            resp.setSuccess(false);
            resp.setMessage("Failed to complete lookup!");
        }

        return resp;

    }

    /**
     * Gets details of a specific drug product
     * @param s the id of the drug product
     * @return a response object with a single drug search object represented the details.
     */
    @GET
    @Path("/details")
    @Produces("application/json")
    public DrugLookupResponse details(@QueryParam("id") String s) {

        DrugLookupResponse resp = new DrugLookupResponse();

        DrugSearchTo1 drug;

        List<DrugSearchTo1> l = new ArrayList<DrugSearchTo1>();

        try {

            drug = this.drugLookUpManager.details(s);

            if (drug != null) {

                l.add(drug);
                resp.setDrugs(l);
                resp.setSuccess(true);

            } else {

                resp.setMessage("Failed to find drugs that have an id: " + s);
                resp.setSuccess(false);

            }

        } catch (Exception e) {
            logger.error(e.getStackTrace());
            resp.setSuccess(false);
            resp.setMessage("Failed to complete lookup!");
        }

        return resp;

    }

    /**
     * Extracts medication instruction details (frequency, dose, etc...) from an instruction string.
     *
     * Uses the same parser as the old Rx2 interface did.
     *
     * @param instructions the string to parse
     * @return a partially populated drug object with the extracted fields populated.
     */
    @POST
    @Path("/parse")
    @Produces
    public DrugResponse parseInstructions(@QueryParam("input")String instructions){

        // the provider no, demographic no, etc... for this drug do not matter.
        // all we care about are the instruction fields.

        RxPrescriptionData.Prescription rx = new RxPrescriptionData.Prescription(0, getLoggedInInfo().getLoggedInProviderNo(), 0);

        rx.setSpecial(instructions);

        RxUtil.instrucParser(rx);

        DrugResponse resp = new DrugResponse();

        DrugTo1 d = new DrugTo1();

        d.setTakeMax(rx.getTakeMax());
        d.setTakeMin(rx.getTakeMin());
        d.setMethod(rx.getMethod());
        d.setFrequency(rx.getFrequencyCode());
        d.setDuration(Integer.parseInt(rx.getDuration()));
        d.setDurationUnit(rx.getDurationUnit());
        d.setPrn(rx.isPrn());
        d.setRoute(rx.getRoute());

        resp.setDrug(d);

        return resp;

    }


}
