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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

public class RxManagerTest extends RxManager {

    //Helper variables for testing.

    Drug old = null;
    int daoAddNewDrugCalled = 0;
    @Before
    public void before() {
        this.drugDao = new MockDrugDao();
        this.securityInfoManager = new MockSecurityInfoManager();
        this.prescriptionManager = new MockPrescriptionManager();
        this.old = null;
        this.daoAddNewDrugCalled = 0;
    }

    @After
    public void after() {
        this.drugDao = null;
        this.old = null;
    }

    @Test
    public void testReadCheckWithAllowedAccess() {

        LoggedInInfo info = new LoggedInInfo();

        // We know that MockSecurityInfoManager.hasPrivledge()
        // will return true if demographicNo = 1.

        try {
            this.readCheck(info, 1);
        } catch (RuntimeException rte) {
            fail();
        }

    }

    @Test(expected = AccessDeniedException.class)
    public void testReadCheckWithDeniedAccess() {

        LoggedInInfo info = new LoggedInInfo();

        // We know that MockSecurityInfoManager.hasPrivledge()
        // will return true if demographicNo > 5, false otherwise.

        this.readCheck(info, 10);

    }

    @Test
    public void testGetDrugsWithStatusAll() {

        LoggedInInfo info = new LoggedInInfo();

        List<Drug> drugs = this.getDrugs(info, 1, RxManager.ALL);

        assertEquals(drugs.size(), 2);
        assertEquals(drugs.get(0).getBrandName(), "Aspirin");
        assertEquals(drugs.get(1).getBrandName(), "Tylenol");
    }

    @Test
    public void testGetDrugsWithStatusArchived() {

        LoggedInInfo info = new LoggedInInfo();

        List<Drug> drugs = this.getDrugs(info, 1, RxManager.ARCHIVED);

        assertEquals(drugs.size(), 1);
        assertEquals(drugs.get(0).getBrandName(), "Aspirin");
    }

    @Test
    public void testGetDrugsWithStatusCurrent() {

        LoggedInInfo info = new LoggedInInfo();

        List<Drug> drugs = this.getDrugs(info, 1, RxManager.CURRENT);

        assertEquals(drugs.size(), 1);
        assertEquals(drugs.get(0).getBrandName(), "Tylenol");
    }

    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void testGetDrugsWithInvalidStatus() {

        LoggedInInfo info = new LoggedInInfo();

        List<Drug> drugs = this.getDrugs(info, 1, "FOOBAR");
    }

    @Test(expected = AccessDeniedException.class)
    public void testWriteCheckWithInvalidCreds() {

        LoggedInInfo info = new LoggedInInfo();

        // The MockSecurityInfoManager.hasPrivledge()
        // will return false for demographicNo > 5

        this.writeCheck(info, 6);

    }

    @Test
    public void testWriteCheckWithValidCred() {

        LoggedInInfo info = new LoggedInInfo();


        // MockSecurityInfoManager.hasPriviledge() will
        // return true for demographicNo <=5

        try {
            this.writeCheck(info, 1);
        } catch (RuntimeException re) {
            fail(); // should not throw a RuntimeException!
        }

    }

    @Test
    public void testAddDrugWithValidDrug() {

        LoggedInInfo info = new LoggedInInfo();

        Drug d = new Drug();

        // genericName == ASA will return true from MockDrugDao.addDrug()
        d.setGenericName("ASA");

        // demographicId == 1 will cause writeCheck() to return pass.
        d.setDemographicId(1);

        Drug result = this.addDrug(info, d);

        assertNotNull(result);
        assertEquals((Integer) 1, result.getId());

    }

    @Test
    public void testAddDrugWithInvalidDrug() {

        LoggedInInfo info = new LoggedInInfo();

        Drug d = new Drug();

        // genericName != ASA will return false from MockDrugDao.addDrug()
        d.setGenericName("Foobar");

        // demographicId == 1 will cause writeCheck() to return pass.
        d.setDemographicId(1);

        assertNull(this.addDrug(info, d));

    }

    @Test
    public void testUpdateDrugWithValidInput() {

        LoggedInInfo info = new LoggedInInfo();

        Drug d = new Drug();

        d.setDemographicId(1);
        d.setId(1);
        d.setGenericName("ASA");

        Drug result = this.updateDrug(info, d);

        assertNotNull(result);
        assertEquals(1, (int) d.getId()); //should take on id assigned by dao.addNewDrug
        assertEquals("ASA", d.getGenericName()); //should not change other fields.

        // merge() should have adjusted the this.old variable
        // to have archived status
        assertTrue(old.isArchived());
        assertEquals("represcribed", old.getArchivedReason());

    }

    @Test
    public void testUpdateDrugWithFailedAddNewDrug() {

        LoggedInInfo info = new LoggedInInfo();

        Drug d = new Drug();

        d.setDemographicId(1);
        d.setId(1);
        d.setGenericName("foobar"); // will fail in MockDrugDao

        Drug result = this.updateDrug(info, d);

        assertNull(result);

        // should not have created a new "old" drug.
        assertNull(old);


    }

    @Test
    public void testDiscontinueWithInvalidDrugId() {

        LoggedInInfo info = new LoggedInInfo();

        // pass 2nd argument as drug id that is not
        // in the test data
        boolean r = this.discontinue(info, 20, 1, "allergy");

        assertFalse(r);

    }

    @Test
    public void testDiscontinueWithDrugIdNotMatchingDemographicId() {

        LoggedInInfo info = new LoggedInInfo();

        // pass 2nd argument as drug id that is not
        // in the test data
        boolean r = this.discontinue(info, 1, 2, "allergy");

        assertFalse(r);

    }

    @Test
    public void testDiscontinueWithValidReasonAdverseReaction() {
        executeValidDiscontinueByReason("adverseReaction");
    }

    @Test
    public void testDiscontinueWithValidReasonAllergy() {
        executeValidDiscontinueByReason("allergy");
    }

    @Test
    public void testDiscontinueWithValidReasonAnotherPhysician() {
        executeValidDiscontinueByReason("discontinuedByAnotherPhysician");
    }

    @Test
    public void testDiscontinueWithValidReasonDrugInteraction() {
        executeValidDiscontinueByReason("drugInteraction");
    }

    @Test
    public void testDiscontinueWithValidReasonCost() {
        executeValidDiscontinueByReason("cost");
    }

    @Test
    public void testDiscontinueWithValidReasonDeleted() {
        executeValidDiscontinueByReason("deleted");
    }

    @Test
    public void testDiscontinueWithValidReasonBenefitRisk() {
        executeValidDiscontinueByReason("increasedRiskBenefitRatio");
    }

    @Test
    public void testDiscontinueWithValidReasonEvidence() {
        executeValidDiscontinueByReason("newScientificEvidence");
    }

    @Test
    public void testDiscontinueWithValidReasonTreatment() {
        executeValidDiscontinueByReason("ineffectiveTreatment");
    }

    @Test
    public void testDiscontinueWithValidReasonNecessary() {
        executeValidDiscontinueByReason("noLongerNecessary");
    }

    @Test
    public void testDiscontinueWithValidReasonRequest() {
        executeValidDiscontinueByReason("patientRequest");
    }

    @Test
    public void testDiscontinueWithValidReasonSimplify() {
        executeValidDiscontinueByReason("simplifyingTreatment");
    }

    @Test
    public void testDiscontinueWithValidReasonUnknown() {
        executeValidDiscontinueByReason("unknown");
    }

    @Test
    public void testDiscontinueWithValidReasonOther() {
        executeValidDiscontinueByReason("other");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDiscontinueWithUnsupportedReason() {
        executeValidDiscontinueByReason("foobar");
    }


    @Test
    public void testPrescribeBasic() {

        List<Drug> drugs = new ArrayList<Drug>();
        drugs.add(getTestDrug());

        LoggedInInfo info = new LoggedInInfo();

        PrescriptionDrugs pd = prescribe(info, drugs, 1);

        assertNotNull(pd);
        assertEquals(pd.drugs.size(), 1);
        assertNotNull(pd.prescription);

    }

    @Test
    public void testPrescribeBasicMultiple(){

        List<Drug> drugs = new ArrayList<Drug>();
        drugs.add(getTestDrug());
        drugs.add(getTestDrug());
        drugs.add(getTestDrug());

        LoggedInInfo info = new LoggedInInfo();

        PrescriptionDrugs pd = prescribe(info, drugs, 1);

        assertNotNull(pd);
        assertEquals(pd.drugs.size(), 3);
        assertNotNull(pd.prescription);

    }

    @Test
    public void testShouldReturnNullOnNullInfo(){

        List<Drug> drugs = new ArrayList<Drug>();
        drugs.add(getTestDrug());
        PrescriptionDrugs pd = prescribe(null, drugs, 1);
        assertNull(pd);

    }

    @Test
    public void testShouldReturnNullOnNullDrugs(){

        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, null, 1);
        assertNull(pd);

    }

    @Test
    public void testShouldReturnNullOnEmptyDrugs(){

        List<Drug> l = new ArrayList<Drug>();
        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, l, 1);
        assertNull(pd);

    }

    @Test
    public void testShouldReturnNullOnInvalidDemoNo(){

        List<Drug> drugs = new ArrayList<Drug>();
        drugs.add(getTestDrug());
        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, drugs, -1);
        assertNull(pd);

    }

    @Test
    public void testShouldReturnNullOnDrugThatCannotBePrescribed(){

        List<Drug> drugs = new ArrayList<Drug>();
        Drug d = getTestDrug();
        d.setProviderNo(""); // will cause check to fail.
        drugs.add(d);

        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, drugs, 1);
        assertNull(pd);

    }

    @Test(expected = AccessDeniedException.class)
    public void testShouldDenyAcess(){
        List<Drug> drugs = new ArrayList<Drug>();
        Drug d = getTestDrug();
        drugs.add(d);
        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, drugs, 10);
    }

    @Test
    public void testShouldAttemptToAddDrugIfDoesNotExist(){

        List<Drug> drugs = new ArrayList<Drug>();
        Drug d = getTestDrug();
        d.setId(3); //result in MockDrugDao.find() failing.
        d.setGenericName("ASA"); // allowed to add in test MockDrugDao.addNewDrug
        drugs.add(d);
        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, drugs, 1);

        assertNotNull(pd);
        assertEquals(daoAddNewDrugCalled, 1);
        assertEquals(pd.drugs.get(0).getGenericName(), "ASA");

    }

    @Test
    public void testShouldReturnNullIfAddingANewDrugFails(){

        List<Drug> drugs = new ArrayList<Drug>();
        Drug d = getTestDrug();
        d.setId(3); //result in MockDrugDao.find() failing.
        d.setGenericName("NOT ASA"); // fail to add in test MockDrugDao.addNewDrug
        drugs.add(d);
        LoggedInInfo info = new LoggedInInfo();
        PrescriptionDrugs pd = prescribe(info, drugs, 1);

        assertNull(pd);
        assertEquals(daoAddNewDrugCalled, 1);

    }

    @Test
    public void testCanPrescribeBasic(){
        Drug d = getTestDrug();
        assertTrue(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnNull(){
        assertFalse(canPrescribe(null));
    }

    @Test
    public void testCanPrescribeIsFalseNullProvider(){
        Drug d = getTestDrug();
        d.setProviderNo(null);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseEmptyStringProvider(){
        Drug d = getTestDrug();
        d.setProviderNo("");
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseNullDemographic(){
        Drug d = getTestDrug();
        d.setDemographicId(null);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseInvalidDemographic(){
        Drug d = getTestDrug();
        d.setDemographicId(-1);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnNullStartDate(){
        Drug d = getTestDrug();
        d.setRxDate(null);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnNullEndDate(){
        Drug d = getTestDrug();
        d.setEndDate(null);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnBadDateSequence(){
        Drug d = getTestDrug();

        // start after end date.
        d.setEndDate(new Date(100000000));
        d.setRxDate(new Date(200000000));

        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnNullInstructions(){
        Drug d = getTestDrug();
        d.setSpecial(null);
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testCanPrescribeIsFalseOnEmptyStringInstructions(){
        Drug d = getTestDrug();
        d.setSpecial("");
        assertFalse(canPrescribe(d));
    }

    @Test
    public void testGetHistoryBasic(){

        LoggedInInfo info = new LoggedInInfo();

        List<Drug> drugs = getHistory(1, info, 1);

        assertNotNull(drugs);
        assertEquals(drugs.size(), 1);

    }

    @Test
    public void testGetHistoryReturnsEmptyListOnInvalidDrug(){

        LoggedInInfo info = new LoggedInInfo();

        // drug id > 5 results in not finding drug in MockDrugDao.findByDemographicIdAndDrugId()
        List<Drug> drugs = getHistory(6, info, 1);

        assertNotNull(drugs);
        assertEquals(drugs.size(), 0);
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetHistoryCanDenyAccess(){

        LoggedInInfo info = new LoggedInInfo();

        // MockSecurityManager will fail the access check for demo > 5
        List<Drug> drugs = getHistory(1, info, 6);

    }


    // =========== TEST HELPER METHODS =================

    public Drug getTestDrug() {

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

    public Prescription getTestPrescription(){

        Prescription p = new Prescription();

        p.setDemographicId(1);
        p.setProviderNo("1");
        p.setTextView("PRESCRIPTION TEXT");
        p.setDatePrescribed( new Date());
        p.setComments("COMMENT TEXT");

        return p;

    }


    protected void executeValidDiscontinueByReason(String reason) {

        LoggedInInfo info = new LoggedInInfo();

        boolean r = this.discontinue(info, 1, 1, reason);

        assertTrue(r);
        assertEquals(reason, old.getArchivedReason());
        assertTrue(old.isArchived());
        Assert.assertNotNull(old.getArchivedDate());

    }

    // =========== TESTING SUPPORT CLASSES =============
    // Uses to mock objects to get around dependancy injection
    // allows for more control over exactly what is tested.

    public class MockPrescriptionManager extends PrescriptionManager{

        public MockPrescriptionManager(){
            super();
        }

        public Prescription createNewPrescription(LoggedInInfo info, List<Drug> drugs, Integer demographicNo) {

            if(demographicNo > 10) return null;
            else return getTestPrescription();

        }

    }


    public class MockDrugDao extends DrugDao {

        List<Drug> drugs;

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

        
        public List<Drug> findByDemographicIdAndAtc(int demographicNo, String atc) {
        		return new ArrayList<Drug>();
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



    }

}
