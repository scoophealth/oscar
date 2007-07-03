/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * 
 *
 * Created on April 27, 2007, 4:24 PM
 *

package org.oscarehr.phr.service;

import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRMessage;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author jay
 */
public interface PHRService {
    
    PHRAuthentication authenticate(String userId,String password);
    boolean validAuthentication(PHRAuthentication auth);
    void retrieveDocuments(PHRAuthentication auth,String providerNo) throws Exception; 
    void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO);
 
    //public void sendAddMedication(EctProviderData.Provider prov, String demographicNo, String demographicPhrId, RxPrescriptionData.Prescription drug) throws Exception;
    //public void sendUpdateMedication(EctProviderData.Provider prov, String demographicNo, String demographicPhrId, RxPrescriptionData.Prescription drug, String oldPhrDrugIndex) throws Exception;
    void sendQueuedDocuments(PHRAuthentication auth,String providerNo) throws Exception ;
    void setPhrActionDAO(PHRActionDAO phrActionDAO);
    public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId) throws Exception;
    public void sendUpdateMessage(PHRMessage msg) throws Exception;
    public boolean isIndivoRegistered(String classification, String oscarId);
   
    
}
