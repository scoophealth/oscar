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

import org.marc.shic.core.configuration.consent.PolicyDefinition;
import org.oscarehr.sharingcenter.model.PatientDocument;
import org.oscarehr.sharingcenter.actions.DocumentPermissionStatus;

/**
 *
 * @author tylerg
 */
public class DocumentConsentWrapper
{

    private PatientDocument document;
    /**
     * Policies that require elevation to disclose.
     */
    private List<PolicyDefinition> discloseElevatePolicies;
    /**
     * Policies that require elevation to import.
     */
    private List<PolicyDefinition> importElevatePolicies;

    private DocumentPermissionStatus disclosePermission;
    private DocumentPermissionStatus importPermission;

    public DocumentPermissionStatus getDisclosePermission()
    {
        return disclosePermission;
    }

    public void setDisclosePermission(DocumentPermissionStatus disclosePermission)
    {
        this.disclosePermission = disclosePermission;
    }

    public DocumentPermissionStatus getImportPermission()
    {
        return importPermission;
    }

    public void setImportPermission(DocumentPermissionStatus importPermission)
    {
        this.importPermission = importPermission;
    }

    public List<PolicyDefinition> getDiscloseElevatePolicies()
    {
        return discloseElevatePolicies;
    }

    public List<PolicyDefinition> getImportElevatePolicies()
    {
        return importElevatePolicies;
    }

    public PatientDocument getDocument()
    {
        return document;
    }

    public DocumentConsentWrapper()
    {
        importElevatePolicies = new ArrayList<PolicyDefinition>();
        discloseElevatePolicies = new ArrayList<PolicyDefinition>();
        disclosePermission = DocumentPermissionStatus.Valid;
        importPermission = DocumentPermissionStatus.Valid;
    }

    public DocumentConsentWrapper(PatientDocument document)
    {
        this();
        this.document = document;
    }
    
    public boolean isDiscloseAllowed() {
        return disclosePermission == DocumentPermissionStatus.Valid;
    }
    
    public boolean isImportAllowed() {
        return importPermission == DocumentPermissionStatus.Valid;
    }
}
