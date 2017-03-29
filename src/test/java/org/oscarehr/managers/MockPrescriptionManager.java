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

package org.oscarehr.managers;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;

public class MockPrescriptionManager extends PrescriptionManager{

    public MockPrescriptionManager(){
        super();
    }

    public Prescription createNewPrescription(LoggedInInfo info, List<Drug> drugs, Integer demographicNo) {

        if(demographicNo > 10) return null;
        else return getTestPrescription();

    }
    
    public Prescription getTestPrescription(){

        Prescription p = new Prescription();

        p.setDemographicId(1);
        p.setProviderNo("1");
        p.setTextView("PRESCRIPTION TEXT");
        p.setDatePrescribed( new Date());
        p.setComments("COMMENT TEXT");

        return p;

    }
}