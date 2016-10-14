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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;

import com.sun.istack.NotNull;

public class MockRxManager extends RxManager {

    protected List<Drug> drugs;
    protected Drug d;
    
    public MockRxManager() {

        drugs = new ArrayList<Drug>();

        d = new Drug();
        d.setId(1);
        d.setGenericName("ASA");
        d.setBrandName("Aspirin");
        d.setProviderNo("1");
        d.setDuration("28");
        d.setArchived(true);
        d.setArchivedDate(new Date());
        d.setArchivedReason("allergy");
        drugs.add(d);

        d = new Drug();
        d.setId(2);
        d.setGenericName("Acetaminophen");
        d.setBrandName("Tylenol");
        d.setProviderNo("1");
        d.setDuration("28");
        d.setArchived(false);
        drugs.add(d);

    }

    public void setSecurityInfoManager(SecurityInfoManager securityInfoManager) {
    	this.securityInfoManager = securityInfoManager;
    }
    
    public List<Drug> getDrugs(@NotNull LoggedInInfo info, @NotNull int demographicNo, @NotNull String status)
            throws UnsupportedOperationException {
        if (status.equals(RxManager.ALL)) return this.getAllDrugs(info, demographicNo);
        else if (status.equals(RxManager.CURRENT)) return this.getCurrentDrugs(info, demographicNo);
        else if (status.equals(RxManager.ARCHIVED)) return this.getArchivedDrugs(info, demographicNo);
        else return null;

    }

    private List<Drug> getAllDrugs(LoggedInInfo info, int id) {

        return this.drugs;

    }

    private List<Drug> getCurrentDrugs(LoggedInInfo info, int id) {

        List<Drug> toReturn = new ArrayList<Drug>();

        for (Drug d : this.drugs) {

            if (!d.isArchived()) toReturn.add(d);

        }

        return toReturn;
    }

    private List<Drug> getArchivedDrugs(LoggedInInfo info, int id) {

        List<Drug> toReturn = new ArrayList<Drug>();

        for (Drug d : this.drugs) {

            if (d.isArchived()) toReturn.add(d);

        }

        return toReturn;
    }

    public Drug addDrug(LoggedInInfo info, Drug d) {

        // only return a drug if the ID is 1
        // others we return null. This is useful for testing.

        if (d.getId() == 1) {
            return d;
        }

        return null;

    }

    public Drug updateDrug(LoggedInInfo info, Drug d) {

        if (d.getId() == 1) return d;

        return null;

    }

    public boolean discontinue(LoggedInInfo i,
                               int drugId,
                               int demo,
                               String reason
    ) {

        // mock discontinue method

        return drugId == 1 && demo == 1;
    }

    public PrescriptionDrugs prescribe(LoggedInInfo info, List<Drug> drugs, Integer demoNo){

        if(drugs.get(0).getId() > 1) return null;

        Prescription p = new Prescription();

        p.setProviderNo("1");
        p.setDemographicId(1);
        p.setTextView("SOME TEXT");

        PrescriptionDrugs pd = new PrescriptionDrugs(p, drugs);
        return pd;

    }

    public List<Drug> getHistory(Integer id, LoggedInInfo info, Integer demographicNo){

        // case for supporting tests, only return an empty list if demo > 5.

        if(id > 5) {

            return new ArrayList<Drug>();

        } else{

            List<Drug> toReturn = new ArrayList<Drug>();

            Drug d = getTestDrug();
            toReturn.add(d);
            return toReturn;

        }
    }
    
    public Drug getTestDrug() {

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

        Drug d = new Drug();

        d.setId(1);
        d.setDemographicId(1);
        d.setProviderNo("1");
        d.setBrandName("Foobar");
        d.setGenericName("Barbang");
        d.setRegionalIdentifier("12345");
        d.setAtc("abcde");
        d.setTakeMax(2);
        d.setTakeMin(1);
        d.setRxDate((Date) startDate.clone());
        d.setEndDate((Date) endDate.clone());
        d.setFreqCode("BID");
        d.setDuration("28");
        d.setDurUnit("D");
        d.setRoute("PO");
        d.setDrugForm("TAB");
        d.setPrn(true);
        d.setMethod("Take");
        d.setRepeat(5);
        d.setSpecial("some string");
        d.setArchived(false);
        d.setArchivedDate((Date) archivedDate.clone());
        d.setArchivedReason("reason");

        return d;

    }
}
