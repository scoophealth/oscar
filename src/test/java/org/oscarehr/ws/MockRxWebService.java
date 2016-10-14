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


package org.oscarehr.ws;

import org.oscarehr.managers.MockRxManager;
import org.oscarehr.managers.MockSecurityInfoManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.RxWebService;
import org.oscarehr.ws.rest.conversion.MockDrugConverter;
import org.oscarehr.ws.rest.conversion.PrescriptionConverter;

public class MockRxWebService extends RxWebService {

    public MockRxWebService() {
        super();
        this.rxManager = new MockRxManager();
        this.drugConverter = new MockDrugConverter();
        this.securityInfoManager = new MockSecurityInfoManager();
        this.prescriptionConverter = new PrescriptionConverter();
    }
    
    public void setSecurityInfoManager(SecurityInfoManager securityInfoManager) {
    	this.securityInfoManager = securityInfoManager;
    }
    
    protected LoggedInInfo getLoggedInInfo() {

        return null;

    }
}