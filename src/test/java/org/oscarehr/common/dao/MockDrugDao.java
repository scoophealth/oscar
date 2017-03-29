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

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Drug;

public class MockDrugDao extends DrugDao {

    List<Drug> drugs;
    static public Drug old = null;
    static public int daoAddNewDrugCalled = 0;
    
    public MockDrugDao() {

        Drug d;

        drugs = new ArrayList<Drug>();

        d = new Drug();
        d.setId(1);
        d.setDemographicId(1);
        d.setGenericName("ASA");
        d.setBrandName("Aspirin");
        d.setArchived(true);
        d.setArchivedDate(new Date());
        d.setArchivedReason("allergy");
        drugs.add(d);

        d = new Drug();
        d.setId(2);
        d.setDemographicId(1);
        d.setGenericName("Acetaminophen");
        d.setBrandName("Tylenol");
        d.setArchived(false);
        drugs.add(d);

    }

    public List<Drug> findByDemographicId(Integer demographicId) {
        return this.drugs;
    }

    public List<Drug> findByDemographicId(Integer demographicId, Boolean archived) {

        List<Drug> toReturn = new ArrayList<Drug>();

        for (Drug d : this.drugs) {

            if (d.isArchived() == archived) {
                toReturn.add(d);
            }

        }

        return toReturn;

    }

    public List<Drug> findByDemographicIdAndDrugId(int demographicNo, Integer drugId) {

        if(drugId > 5) {
            return new ArrayList<Drug>();
        } else{
            List<Drug> drugs = new ArrayList<Drug>();
            Drug d = getTestDrug();
            d.setId(drugId);
            d.setDemographicId(demographicNo);
            drugs.add(d);
            return drugs;
        }

    }

    public List<Drug> findByAtc(String atc) {

        if(atc.equals("BAD_ATC")){
            return new ArrayList<Drug>();
        }else{
            List<Drug> drugs = new ArrayList<Drug>();
            Drug d = getTestDrug();
            drugs.add(d);
            return drugs;
        }
    }


    public boolean addNewDrug(Drug d) {

        // For testing purposes we only return
        // a drug if the drug matches out ASA drug in the test data.
        // Write tests to take advantage of this fact...

        daoAddNewDrugCalled++;

        if (d.getGenericName().equals("ASA")) {
            d.setId(1);
            return true;
        }

        return false;

    }

    public void persist(Drug d) {
        return;
    }

    /**
     * Mock find() method that searches that test data
     * for a drug with appropriate ID.
     *
     * @param i
     *
     * @return
     */
    public Drug find(Object i) {

        int j = (Integer) i;

        for (Drug d : this.drugs) {
            if (j == d.getId()) return d;
        }

        return null;
    }

    /**
     * Override method for testing purposes.
     */
    public void merge(AbstractModel<?> o) {

        // Sets this in the parent class so that we
        // can check it after the test.
        old = (Drug) o;

        return;

    }
    
    public static Drug getTestDrug() {

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

        Drug d = new Drug();

        d.setId(1);
        d.setDemographicId(1);
        d.setProviderNo("1");
        d.setBrandName("Aspirin");
        d.setGenericName("ASA");
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
