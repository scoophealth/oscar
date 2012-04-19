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


package org.oscarehr.phr.dao;

import java.util.List;

import org.oscarehr.phr.model.PHRDocument;

/**
 *
 * @author jay
 */
public interface PHRDocumentDAO {
    public boolean hasIndex(String index);
    public void save(PHRDocument doc);
    public void update(PHRDocument doc);
    public List getDocumentsReceived(String docType,String providerNo);
    public List getDocumentsSent(String docType,String providerNo);
    public List getDocumentsArchived(String docType,String providerNo);
    public List<PHRDocument> getDocumentsByReceiverSenderStatusClassification(Integer receiverType, Integer senderType, String phrClassification, String receiverOscar,Integer status);
    public PHRDocument getDocumentById(String id);
    public PHRDocument getDocumentByIndex(String idx);
    
    
    public List getReferencedMessages(PHRDocument doc);
    public List getReferencedMessagesById(String id);
    public int countUnreadDocuments(String classification, String providerNo);
    
}
