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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.indivo.service.accesspolicies;

import java.util.Date;
import java.util.List;

import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;

import oscar.util.StringUtils;

/**
 *
 * @author apavel
 */
public class IndivoAPService extends PHRService {

    /*Setting a provider access policy occurs in 4 stages:
     * 1.  ProposeAccessPolicy - Adds proposal to the actions
     * 2.  AcceptAccessPolicy/DenyAccessPolicy - provider accepts AP, gets marked as "ON HOLD" or rejects it, and it gets marked as "deleted"
     * 3.  PackageAccessPolicy - Called by indivoService just before documents are sent to indivo.  This retrieves existing policies, makes an
     *                          indivo document with the new policies, puts it in actions and marks it as an UPDATE action.
     * 4.  SendAccessPolicy - IndivoService sends the action policy via UPDATE action along with other documents.

     * All this occurs in one stage for sending demographic access policies
     */

    public static final String LEVEL_NOT_SET = "";
    public static final String LEVEL_PATIENT = "org:indivo:profile:patient";
    public static final String LEVEL_PROVIDER = "org:indivo:profile:primary-care-provider";
    //if you want to add more of these, see indivo-security-profiles.xml in oscarehr/phr/indivo/accesspolicies

    private static final String DOC_CONTENT_DELIMITER = ", ";

    public IndivoAPService() {

    }

    public IndivoAPService(PHRService phrService) {
        this.phrActionDAO = phrService.getPhrActionDao();
        this.phrDocumentDAO = phrService.getPhrDocumentDAO();
    }

    /*adds the access policy to the PHRActions table.  The action waits for approval from the provider
    //before it is queued and sent*/
    public void proposeAccessPolicy(String providerOscarId, Long permissionRecipientPhrId, String newPolicy, String providerIdPerformingAction) {
        PHRAction action = new PHRAction();
        action.setActionType(PHRAction.ACTION_UPDATE);
        action.setStatus(PHRAction.STATUS_APPROVAL_PENDING);
        action.setPhrClassification("RELATIONSHIP");
        action.setDateQueued(new Date());
        action.setReceiverOscar(providerOscarId);
        //TOTHINK: might have to get Id later
        action.setReceiverType(PHRDocument.TYPE_PROVIDER);
        action.setSenderType(PHRDocument.TYPE_PROVIDER);
        action.setSenderOscar(providerIdPerformingAction);
        //make a csv store permissionRecipientPhrId and newPolicy
        action.setDocContent(permissionRecipientPhrId + DOC_CONTENT_DELIMITER + newPolicy);
        //User receiving permissions is stored as senderPhr
        action.setSenderMyOscarUserId(permissionRecipientPhrId);
        phrActionDAO.save(action);
    }

    public void approveAccessPolicy(PHRAction action) {
        action.setStatus(PHRAction.STATUS_SEND_PENDING);
        phrActionDAO.update(action);
    }

    public void denyAccessPolicy(PHRAction action) {
        this.denyAction(action);
    }


    public static List<String> getProposalIdAndPermission(PHRAction action) {
        return StringUtils.split(action.getDocContent(), DOC_CONTENT_DELIMITER);
    }
}
