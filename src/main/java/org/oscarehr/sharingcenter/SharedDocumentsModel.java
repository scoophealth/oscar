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

package org.oscarehr.sharingcenter;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.CodeMappingDataObject;
import org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject;

/**
 *
 * @author tylerg
 */
public class SharedDocumentsModel
{
    private List<AffinityDomainDocuments> affinityDomainSharedDocuments;
    private List<AffinityDomainFolderMetaData> folders;
    private List<AffinityDomainDataObject> sharedAffinityDomains;
    private CodeMappingDataObject consentCodeMappings;
    private AffinityDomainDataObject selectedAffinityDomain;
    private List<PolicyDefinitionDataObject> unconsentedPolicies;
    private List<String> errors;
    private boolean consentGiven;    
    private List<PolicyDefinitionDataObject> elevatePolicies;
    private Demographic demographic;
    private List<Demographic> decisionMakers;    
    private List<String> downloadedDocumentNames;
    
    public List<AffinityDomainDocuments> getAffinityDomainSharedDocuments()
    {
        return affinityDomainSharedDocuments;
    }

    public void setAffinityDomainSharedDocuments(List<AffinityDomainDocuments> affinityDomainSharedDocuments)
    {
        this.affinityDomainSharedDocuments = affinityDomainSharedDocuments;
    }

    public List<AffinityDomainFolderMetaData> getFolders()
    {
        return folders;
    }

    public void setFolders(List<AffinityDomainFolderMetaData> folders)
    {
        this.folders = folders;
    }

    public List<AffinityDomainDataObject> getSharedAffinityDomains()
    {
        return sharedAffinityDomains;
    }

    public void setSharedAffinityDomains(List<AffinityDomainDataObject> sharedAffinityDomains)
    {
        this.sharedAffinityDomains = sharedAffinityDomains;
    }

    public CodeMappingDataObject getConsentCodeMappings()
    {
        return consentCodeMappings;
    }

    public void setConsentCodeMappings(CodeMappingDataObject consentCodeMappings)
    {
        this.consentCodeMappings = consentCodeMappings;
    }

    public AffinityDomainDataObject getSelectedAffinityDomain()
    {
        return selectedAffinityDomain;
    }

    public List<String> getErrors()
    {
        return errors;
    }
    public void setSelectedAffinityDomain(AffinityDomainDataObject selectedAffinityDomain)
    {
        this.selectedAffinityDomain = selectedAffinityDomain;
    }    

    public boolean isConsentGiven()
    {
        return consentGiven;
    }

    public void setConsentGiven(boolean consentGiven)
    {
        this.consentGiven = consentGiven;
    }

    public List<PolicyDefinitionDataObject> getElevatePolicies()
    {
        return elevatePolicies;
    }    

    public List<PolicyDefinitionDataObject> getUnconsentedPolicies()
    {
        return unconsentedPolicies;
    }    

    public Demographic getDemographic()
    {
        return demographic;
    }

    public void setDemographic(Demographic demographic)
    {
        this.demographic = demographic;
    }

    public List<Demographic> getDecisionMakers()
    {
        return decisionMakers;
    }

    public void setDecisionMakers(List<Demographic> decisionMakers)
    {
        this.decisionMakers = decisionMakers;
    }

    public List<String> getDownloadedDocumentNames()
    {
        return downloadedDocumentNames;
    }

    public SharedDocumentsModel()
    {
        this.affinityDomainSharedDocuments = new ArrayList<AffinityDomainDocuments>();
        this.folders = new ArrayList<AffinityDomainFolderMetaData>();
        this.affinityDomainSharedDocuments = new ArrayList<AffinityDomainDocuments>();
        this.errors = new ArrayList<String>();
        this.elevatePolicies = new ArrayList<PolicyDefinitionDataObject>();
        this.unconsentedPolicies = new ArrayList<PolicyDefinitionDataObject>();
        this.downloadedDocumentNames = new ArrayList<String>();
    }

    public SharedDocumentsModel(List<AffinityDomainDocuments> affintyDomainDocuments)
    {
        this();
        this.affinityDomainSharedDocuments = affintyDomainDocuments;
    }

    public SharedDocumentsModel(List<AffinityDomainDocuments> affintyDomainDocuments, List<AffinityDomainFolderMetaData> folders)
    {
        this(affintyDomainDocuments);
        this.folders = folders;
    }

    public SharedDocumentsModel(List<AffinityDomainDocuments> affintyDomainDocuments, List<AffinityDomainFolderMetaData> folders, List<AffinityDomainDataObject> sharedAffinityDomains)
    {
        this(affintyDomainDocuments, folders);
        this.sharedAffinityDomains = sharedAffinityDomains;
    }
    
     public SharedDocumentsModel(List<AffinityDomainDocuments> affintyDomainDocuments, List<AffinityDomainFolderMetaData> folders, List<AffinityDomainDataObject> sharedAffinityDomains, AffinityDomainDataObject selectedAffinityDomain)
    {
        this(affintyDomainDocuments, folders, sharedAffinityDomains);
        this.selectedAffinityDomain = selectedAffinityDomain;
    }
}
