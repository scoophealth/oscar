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
package org.oscarehr.sharingcenter.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.oscarehr.sharingcenter.SharingCenterUtil;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.PatientSharingNetworkDataObject;
import org.oscarehr.util.SpringUtils;

public class ShareDomainServlet extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // grab the referring_page to send back to
        String referer = request.getParameter("referring_page");
        if (referer.isEmpty()) {
            referer = request.getHeader("referer"); // default
        }

        int patientId = Integer.parseInt(request.getParameter("demographic_no"));

        int networkId = Integer.parseInt(request.getParameter("network_id"));
        AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
        AffinityDomainDataObject network = null;
        network = affDao.getAffinityDomain(networkId);

        PatientSharingNetworkDao patientSharingNetworkDao = SpringUtils.getBean(PatientSharingNetworkDao.class);

        // is patient already sharing?
        boolean sharingEnabled = patientSharingNetworkDao.isSharingEnabled(network.getId(), patientId);

        // actions
        if (request.getParameter("sharing_action").equalsIgnoreCase("register-patient") && sharingEnabled == false) {
            // register
            boolean result = SharingCenterUtil.registerPatient(patientId, AffinityDomainDataObject.createIheAffinityDomainConfiguration(network));

            if (result) {
                // registration successful
                PatientSharingNetworkDataObject sharedNetwork = new PatientSharingNetworkDataObject();
                sharedNetwork.setDemographicNo(patientId);
                sharedNetwork.setAffinityDomain(networkId);
                sharedNetwork.setSharingEnabled(true);
                sharedNetwork.setSharingKey(null);
                patientSharingNetworkDao.saveEntity(sharedNetwork);
            }

            response.sendRedirect(referer);

        } else if (request.getParameter("sharing_action").equalsIgnoreCase("update-patient") && sharingEnabled == true) {
            // update
            boolean result = SharingCenterUtil.updatePatient(patientId, AffinityDomainDataObject.createIheAffinityDomainConfiguration(network));

            response.sendRedirect(referer);

        } else if (request.getParameter("sharing_action").equalsIgnoreCase("disable-sharing") && sharingEnabled == true) {
            // disable
            PatientSharingNetworkDataObject sharedNetwork = patientSharingNetworkDao.findPatientSharingNetworkDataObject(network.getId(), patientId);
            patientSharingNetworkDao.remove(sharedNetwork.getId());

            response.sendRedirect(referer);

        } else if (request.getParameter("sharing_action").equalsIgnoreCase("register-sharing-key") && sharingEnabled == false) {
            // grab the sharing key
            String sharingKey = request.getParameter("sharingKey");

            if (sharingKey != null) {
                // registration successful
                PatientSharingNetworkDataObject sharedNetwork = new PatientSharingNetworkDataObject();
                sharedNetwork.setDemographicNo(patientId);
                sharedNetwork.setAffinityDomain(networkId);
                sharedNetwork.setSharingEnabled(true);
                sharedNetwork.setSharingKey(sharingKey);
                patientSharingNetworkDao.saveEntity(sharedNetwork);
            }

            response.sendRedirect(referer);

        } else if (request.getParameter("sharing_action").equalsIgnoreCase("update-sharing-key") && sharingEnabled == true) {
            // grab the sharing key
            String sharingKey = request.getParameter("sharingKey");

            if (sharingKey != null) {
                // grab the shared network association object
                PatientSharingNetworkDataObject sharedNetwork = patientSharingNetworkDao.findPatientSharingNetworkDataObject(networkId, patientId);
                if (sharedNetwork != null) {
                    sharedNetwork.setSharingKey(sharingKey);
                    patientSharingNetworkDao.merge(sharedNetwork);
                }
            }

            response.sendRedirect(referer);

        } else {
            // unimplemented action
            response.sendRedirect(request.getHeader("referer"));
        }

        return null;
    }

}
