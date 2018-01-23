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
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Favorite;
import org.oscarehr.managers.RxManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.conversion.ConversionException;
import org.oscarehr.ws.rest.conversion.DrugConverter;
import org.oscarehr.ws.rest.conversion.FavoriteConverter;
import org.oscarehr.ws.rest.conversion.PrescriptionConverter;
import org.oscarehr.ws.rest.to.*;
import org.oscarehr.ws.rest.to.model.DrugTo1;
import org.oscarehr.ws.rest.to.model.FavoriteTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/rx")
@Component("rxWebService")
@Produces("application/xml")
public class RxWebService extends AbstractServiceImpl {

    private static Logger logger = MiscUtils.getLogger();

    @Autowired
    protected SecurityInfoManager securityInfoManager;

    @Autowired
    protected RxManager rxManager;

    @Autowired
    protected DrugConverter drugConverter;

    @Autowired
    protected PrescriptionConverter prescriptionConverter;

    @Autowired
    protected FavoriteConverter favoriteConverter;

    /**
     * Gets drugs for the demographic and filter based on their status.
     *
     * @param demographicNo the demographic identifier to look up drugs for.
     * @param status        the status to use to filter the results on {"", current, archived,}
     *
     * @return a response containing a list of drugs that meet the status criteria.
     *
     * @throws AccessDeniedException          if the current user does not have permission to access this data.
     * @throws OperationNotSupportedException if the requested status is unknown.
     */
    @GET
    @Path("/drugs{status : (/status)?}")
    @Produces("application/json")
    public DrugSearchResponse drugs(@QueryParam("demographicNo") int demographicNo, @PathParam("status") String status)
            throws OperationNotSupportedException {

        // determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_rx", "r", demographicNo)) {
            throw new AccessDeniedException("_rx", "r", demographicNo);
        }

        DrugSearchResponse response = new DrugSearchResponse();

        List<Drug> drugList;

        if (status == null) {

            drugList = rxManager.getDrugs(getLoggedInInfo(), demographicNo, RxManager.ALL);

        } else if (status.equals(RxManager.CURRENT) || status.equals(RxManager.ARCHIVED)) {

            drugList = rxManager.getDrugs(getLoggedInInfo(), demographicNo, status);

        } else {

            // Throw an exception because we do not know what status they are requesting.
            throw new OperationNotSupportedException();

        }

        response.setContent(this.drugConverter.getAllAsTransferObjects(getLoggedInInfo(), drugList));

        return response;
    }


    /**
     * Adds a new drug to the drugs table.
     *
     * @param transferObject the drug information
     * @param demographicNo  the identifier for the demographic this drug is for.
     *
     * @return a drug transfer object that reflects the new drug in the database.
     */
    @POST
    @Path("/new")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DrugResponse addDrug(DrugTo1 transferObject, @QueryParam("demographicNo") int demographicNo) {

        LoggedInInfo info = getLoggedInInfo();

        // determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(info, "_rx", "w", demographicNo)) {
            throw new AccessDeniedException("_rx", "w", demographicNo);
        }

        DrugResponse resp = new DrugResponse();

        Drug d;
        Drug outgoingDrug;

        try {

            d = this.drugConverter.getAsDomainObject(info, transferObject);
            outgoingDrug = this.rxManager.addDrug(info, d);

            if (outgoingDrug != null) {

                resp.setSuccess(true);
                resp.setDrug(this.drugConverter.getAsTransferObject(info, outgoingDrug));

            } else {

                resp.setMessage("Could not add the drug, request rejected.");
                resp.setSuccess(false);

            }

        } catch (ConversionException ce) {

            logger.error(ce);
            resp.setMessage("Could not convert provided JSON to Drug domain object, addDrug request rejected.");
            resp.setSuccess(false);

        }

        return resp;

    }

    /**
     * Updates a drug in the database identified by the drugId to reflect
     * the data provided in the incoming transferObject. In the database this
     * has the effect of:
     * a) setting the drug with transferObject.drugId to archived;
     * b) making a new entry that contains data from the transferObject.
     *
     * @param transferObject the data to make the update based on.
     * @param demographicNo  the demographic this drug is for.
     *
     * @return a response object containing a drug transfer object
     * that reflects updated version in the database.
     *
     * @throws AccessDeniedException if the current user is not allowed to write
     *                               prescription information to this demographic.
     */
    @Path("/update")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DrugResponse updateDrug(DrugTo1 transferObject, @QueryParam("demographicNo") int demographicNo) {

        LoggedInInfo info = getLoggedInInfo();

        DrugResponse resp = new DrugResponse();

        // determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(info, "_rx", "w", demographicNo)) {
            throw new AccessDeniedException("_rx", "w", demographicNo);
        }

        Drug incomingDrug;
        Drug outgoingDrug;

        try {

            incomingDrug = this.drugConverter.getAsDomainObject(info, transferObject);
            outgoingDrug = this.rxManager.updateDrug(info, incomingDrug);

            if (outgoingDrug != null) {

                resp.setSuccess(true);
                resp.setDrug(this.drugConverter.getAsTransferObject(info, outgoingDrug));

            } else {

                resp.setSuccess(false);
                resp.setDrug(null);
                resp.setMessage("Unable to update drug, request to updateDrug() failed.");

            }

        } catch (ConversionException ce) {
            logger.info("Failed to convert from transfer object to domain object: " + ce.getMessage());
            logger.error(ce);
            resp.setMessage("Could not convert provided JSON to Drug domain object, updateDrug request rejected.");
            resp.setDrug(null);
            resp.setSuccess(false);

        }

        return resp;

    }

    /**
     * Marks a drug in the database as discontinued. Requires a reason for
     * discontinuing be provided.
     *
     * @param drugId        the id of the drug to discontinue
     * @param reason        the reason for discontinuing the drug, one of:
     *                      {"adverseReaction","allergy","discontinuedByAnotherPhysician",
     *                      "increasedRiskBenefitRatio", "newScientificEvidence", "noLongerNecessary"
     *                      "ineffectiveTreatment", "other", "cost", "drugInteraction",
     *                      "patientRequest", "unknown", "deleted", "simplifyingTreatment"}
     * @param demographicNo the demographic the drug is associated with.
     *
     * @return a generic response indicating success or failure.
     */
    @Path("/discontinue")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public GenericRESTResponse discontinueDrug(@QueryParam("drugId") int drugId,
                                               @QueryParam("reason") String reason,
                                               @QueryParam("demographicNo") int demographicNo
    ) {

        LoggedInInfo info = getLoggedInInfo();

        // Determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(info, "_rx", "w", demographicNo)) {
            throw new AccessDeniedException("_rx", "w", demographicNo);
        }

        GenericRESTResponse resp = new GenericRESTResponse();

        if (rxManager.discontinue(info, drugId, demographicNo, reason)) {

            resp.setSuccess(true);
            resp.setMessage("Successfully discontinued drug.");

        } else {

            resp.setSuccess(false);
            resp.setMessage("Failed to discontinue drug.");
        }

        return resp;

    }

    /**
     * Creates a prescription for the drugs that are provided.
     *
     * @param drugTransferObjects a non-empty list of drugs to include on the prescription.
     * @param demographicNo the demographic this prescription is for.
     * @return the completed prescription or an indication of failure.
     */
    @Path("/prescribe")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PrescriptionResponse prescribe(
            List<DrugTo1> drugTransferObjects,
            @QueryParam("demographicNo")int demographicNo
    ) {


        LoggedInInfo info = getLoggedInInfo();

        // Determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(info, "_rx", "w", demographicNo)) {
            throw new AccessDeniedException("_rx", "w", demographicNo);
        }

        PrescriptionResponse resp = new PrescriptionResponse();

        // sanity check for input parameters
        if(drugTransferObjects == null || demographicNo < 0 || drugTransferObjects.size() < 1){
            resp.setSuccess(false);
            resp.setMessage("Invalid parameters passed to prescribe");
            return resp;
        }

        List<Drug> drugs = new ArrayList<Drug>();

        RxManager.PrescriptionDrugs pd;

        try {

            //attempt to convert to domain object so that we can
            // work with the objects.

            for (DrugTo1 to : drugTransferObjects) {
                drugs.add(this.drugConverter.getAsDomainObject(info, to));
            }

        } catch (ConversionException ce) {

            logger.info("Failed to convert from transfer object to domain object: " + ce.getMessage());
            logger.error(ce);
            resp.setMessage("Could not convert provided drugs to domain object, prescribe failed.");
            resp.setSuccess(false);

            return resp;
        }

        // attempt to prescribe the drugs.
        pd = rxManager.prescribe(info, drugs, demographicNo);

        if(pd != null){

            // prescribe was success. We can now prepare the response.
            resp.setDrugs(this.drugConverter.getAllAsTransferObjects(info, pd.drugs));
            resp.setPrescription(this.prescriptionConverter.getAsTransferObject(info, pd.prescription));
            resp.setSuccess(true);

            return resp;

        }else{

            logger.error("Failed to prescribe drugs: " + drugs.toString());
            resp.setMessage("Failed to prescribe drugs");
            resp.setSuccess(false);
            return resp;

        }

    }

    /**
     * Looks up the history of a particular drug.
     *
     * @param id the drug identifier to get history for.
     * @param demographicNo the id of the demographic associated with the drug.
     * @return a response containing the drugs in the history.
     */
    @Path("/history")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DrugSearchResponse history(
            @QueryParam("id") int id,
            @QueryParam("demographicNo") int demographicNo
    ){

        LoggedInInfo info = getLoggedInInfo();

        // Determine if the user has privileges to view this data.
        if (!securityInfoManager.hasPrivilege(info, "_rx", "r", demographicNo)) {
            throw new AccessDeniedException("_rx", "r", demographicNo);
        }

        DrugSearchResponse resp = new DrugSearchResponse();

        List<Drug> drugs = rxManager.getHistory(id, info, demographicNo);
        resp.setContent(drugConverter.getAllAsTransferObjects(info, drugs));

        return resp;
    }

    @Path("/favorites")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public FavoriteResponse favourites(){

        // No access control check required, we are not accessing a patient record.
        // TODO: Revise access control policies and re-evalute this to see if it requires access control check.

        LoggedInInfo info = getLoggedInInfo();

        FavoriteResponse resp = new FavoriteResponse();

        List<Favorite> favs = this.rxManager.getFavorites(info.getLoggedInProviderNo());

        resp.setDrugs(this.favoriteConverter.getAllAsTransferObjects(info, favs));

        return resp;

    }

    @Path("/favorites")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public GenericRESTResponse addFavorite(FavoriteTo1 newFavorite){

        // No access control check required, we are not accessing a patient record.
        // TODO: Revise access control policies and re-evalute this to see if it requires access control check.

        LoggedInInfo info = getLoggedInInfo();

        GenericRESTResponse resp = new GenericRESTResponse();

        try{

            Favorite f = this.favoriteConverter.getAsDomainObject(info, newFavorite);

            if(this.rxManager.addFavorite(f)){
                resp.setSuccess(true);
                resp.setMessage("added favorite");
            }else{
                resp.setSuccess(false);
                resp.setMessage("failed to add new favorite");
            }

        }catch(ConversionException e){
            logger.error(e.getStackTrace());
            resp.setSuccess(false);
            resp.setMessage("Failed to add favorite.");
        }

        return resp;

    }


}
