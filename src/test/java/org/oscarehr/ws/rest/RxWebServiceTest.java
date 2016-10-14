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

package org.oscarehr.ws.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.managers.MockSecurityInfoManager;
import org.oscarehr.managers.RxManager;
import org.oscarehr.ws.MockRxWebService;
import org.oscarehr.ws.rest.to.DrugResponse;
import org.oscarehr.ws.rest.to.DrugSearchResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.PrescriptionResponse;
import org.oscarehr.ws.rest.to.model.DrugTo1;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;

public class RxWebServiceTest {

    private MockRxWebService service;

    @Before
    public void before() {

        this.service = new MockRxWebService();

    }

    @After
    public void after() {

        this.service = null;

    }

    @Test
    public void testAllDrugs() {

        DrugSearchResponse resp = null;
        try {
        	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
        	mockSecurityInfoManager.setAccessPatientRecord(true);
        	mockSecurityInfoManager.setPrivilege(true);
        	service.setSecurityInfoManager(mockSecurityInfoManager);
            resp = this.service.drugs(1, null);
        } catch (OperationNotSupportedException e) {
            fail();
        }
        List<DrugTo1> content = resp.getContent();
        assertEquals(content.size(), 2);

        assertEquals(content.get(0).getBrandName(), "Aspirin");
        assertEquals(content.get(0).getGenericName(), "ASA");
        assertEquals((int) content.get(0).getDrugId(), 1);

        assertEquals(content.get(1).getBrandName(), "Tylenol");
        assertEquals(content.get(1).getGenericName(), "Acetaminophen");
        assertEquals((int) content.get(1).getDrugId(), 2);

    }

    @Test
    public void testCurrentDrugs() {

        DrugSearchResponse resp = null;
        try {
        	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
        	mockSecurityInfoManager.setAccessPatientRecord(true);
        	mockSecurityInfoManager.setPrivilege(true);
        	service.setSecurityInfoManager(mockSecurityInfoManager);
            resp = this.service.drugs(1, RxManager.CURRENT);
        } catch (OperationNotSupportedException e) {
            fail();
        }
        List<DrugTo1> content = resp.getContent();

        assertEquals(content.size(), 1);
        assertEquals(content.get(0).getBrandName(), "Tylenol");
        assertEquals(content.get(0).getGenericName(), "Acetaminophen");
        assertEquals((int) content.get(0).getDrugId(), 2);

    }

    @Test
    public void testArchivedDrugs() {

        DrugSearchResponse resp = null;
        try {
        	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
        	mockSecurityInfoManager.setAccessPatientRecord(true);
        	mockSecurityInfoManager.setPrivilege(true);
        	service.setSecurityInfoManager(mockSecurityInfoManager);
            resp = this.service.drugs(1, RxManager.ARCHIVED);
        } catch (OperationNotSupportedException e) {
            fail();
        }
        List<DrugTo1> content = resp.getContent();

        assertEquals(content.size(), 1);
        assertEquals(content.get(0).getBrandName(), "Aspirin");
        assertEquals(content.get(0).getGenericName(), "ASA");
        assertEquals((int) content.get(0).getDrugId(), 1);

    }

    @Test(expected = OperationNotSupportedException.class)
    public void testInvalidStatus() throws OperationNotSupportedException {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
        this.service.drugs(1, "foobar"); // an unknown status

    }

    @Test(expected = RuntimeException.class)
    public void testFailedPrivilege() {

        try {
        	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
        	mockSecurityInfoManager.setAccessPatientRecord(true);
        	mockSecurityInfoManager.setPrivilege(false);
        	service.setSecurityInfoManager(mockSecurityInfoManager);
            this.service.drugs(6, null);
        } catch (OperationNotSupportedException e) {
            fail();
        }

    }

    @Test
    public void testAddDrugWithValidInput() {
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugTo1 t = this.getTestTransferObject();
        GenericRESTResponse resp = this.service.addDrug(t, 1);
        assertTrue(resp.isSuccess());

    }

    @Test(expected = AccessDeniedException.class)
    public void testAddDrugWithNoPrivledges() {

        DrugTo1 t = this.getTestTransferObject();

        // The MockSecurityInfoManager.hasPrivledge will return false
        // for any demographiNo > 5

        GenericRESTResponse resp = this.service.addDrug(t, 10);

    }

    @Test
    public void testFailedAddNewDrug() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugTo1 t = this.getTestTransferObject();

        // The MockRxManager.addDrug() will return false if
        // a drugId != 1

        t.setDrugId(2);

        GenericRESTResponse resp = this.service.addDrug(t, 1);
        assertFalse(resp.isSuccess());

    }

    @Test
    public void testInvalidTransferObject() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugTo1 t = this.getTestTransferObject();

        // MockDrugConverter will throw ConversionException
        // if the Drug id > 2.
        // We expect this to be handled by the RxWebService

        t.setDrugId(3);

        GenericRESTResponse resp = this.service.addDrug(t, 1);
        assertFalse(resp.isSuccess());


    }

    @Test
    public void testValidUpdateDrugCall() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugTo1 t = this.getTestTransferObject();

        // MockRxManager.updateDrug() will return a Drug
        // if drugid == 1

        t.setDrugId(1);

        DrugResponse r = this.service.updateDrug(t, 1);

        assertNotNull(r);

        assertTrue(r.isSuccess());
        assertNotNull(r.getDrug());
        assertEquals("bangbar", r.getDrug().getGenericName());

    }

    @Test(expected = AccessDeniedException.class)
    public void testShouldThrowAccessDeniedException() {

        DrugTo1 t = this.getTestTransferObject();

        //demographicId > 5 will cause access denied
        // in MockSecurityInfoManager.

        DrugResponse r = this.service.updateDrug(t, 6);

    }

    @Test
    public void testShouldReturnFalseForUnsuccessfulUpdate() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugTo1 t = this.getTestTransferObject();

        t.setDrugId(2); // MockRxManager will return null for drugid != 1

        DrugResponse r = this.service.updateDrug(t, 1);

        assertNotNull(r); //response should be valid

        assertFalse(r.isSuccess());
        assertNull(r.getDrug());
    }

    @Test
    public void testShouldHandlePoorlyFormedTransferObject() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        // Should catch any ConversionExceptions...

        DrugTo1 t = this.getTestTransferObject();

        t.setDuration(null); //this should fail the conversion.

        DrugResponse r = this.service.updateDrug(t, 1);

        assertNotNull(r); //valid response.
        assertNull(r.getDrug()); //no drug returned.
        assertFalse(r.isSuccess()); //failed update

    }

    @Test
    public void testValidDiscontinueReason() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        GenericRESTResponse r = this.service.discontinueDrug(1, "deleted", 1);
        assertNotNull(r);
        assertTrue(r.isSuccess());

    }

    @Test
    public void testFailedToDiscontinue() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        GenericRESTResponse r = this.service.discontinueDrug(2, "delete", 1);
        assertNotNull(r);
        assertFalse(r.isSuccess());

    }

    @Test(expected = AccessDeniedException.class)
    public void testDiscontinueShouldDenyAccess() {

        this.service.discontinueDrug(1, "delete", 20);

    }

    @Test
    public void testPrescribeBasic() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        toPrescribe.add(this.getTestTransferObject());
        PrescriptionResponse resp = this.service.prescribe(toPrescribe, 1);

        assertTrue(resp.isSuccess());
        assertNotNull(resp.getDrugs());
        assertEquals(resp.getDrugs().size(), 1);
        assertEquals((int) resp.getDrugs().get(0).getDemographicNo(), 1);
        assertEquals(resp.getDrugs().get(0).getBrandName(), "foobar");

        assertNotNull(resp.getPrescription());
        assertEquals((int)resp.getPrescription().getProviderNo(), 1);
        assertEquals((int) resp.getPrescription().getDemographicNo(), 1);
        assertEquals(resp.getPrescription().getTextView(), "SOME TEXT");
    }

    @Test
    public void testPrescribeMultiple() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        toPrescribe.add(this.getTestTransferObject());
        toPrescribe.add(this.getTestTransferObject());
        PrescriptionResponse resp = this.service.prescribe(toPrescribe, 1);

        assertTrue(resp.isSuccess());
        assertNotNull(resp.getDrugs());
        assertEquals(resp.getDrugs().size(), 2);

        assertNotNull(resp.getPrescription());
        assertEquals((int)resp.getPrescription().getProviderNo(), 1);
        assertEquals((int) resp.getPrescription().getDemographicNo(), 1);
        assertEquals(resp.getPrescription().getTextView(), "SOME TEXT");
    }

    @Test
    public void testItShouldFailToPrescribeEmptyDrugList() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        PrescriptionResponse resp = this.service.prescribe(toPrescribe, 1);

        assertFalse(resp.isSuccess());
        assertNotNull(resp.getMessage());
    }

    @Test
    public void testItShouldHandleAFailedConversion() {

    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        toPrescribe.add(this.getTestTransferObject());
        toPrescribe.get(0).setDrugId(5); //will cause exception in mock test converter.

        PrescriptionResponse resp = this.service.prescribe(toPrescribe, 1);

        assertFalse(resp.isSuccess());
        assertNotNull(resp.getMessage());

    }

    @Test(expected = AccessDeniedException.class)
    public void testShouldDenyAccess(){

        // demographic no of 20 will cause access denied in test classes
        this.service.prescribe(null, 20);

    }

    @Test
    public void testItShouldHandleNullDrugListParameter(){
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        PrescriptionResponse resp = this.service.prescribe(null, 1);

        assertFalse(resp.isSuccess());
        assertNotNull(resp.getMessage());
    }

    @Test
    public void testItShouldHandleInvalidDemographicParametert(){
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        toPrescribe.add(this.getTestTransferObject());

        PrescriptionResponse resp = this.service.prescribe(toPrescribe, -1);

        assertFalse(resp.isSuccess());
        assertNotNull(resp.getMessage());
    }

    @Test
    public void testItShouldHandeFailureToCreatePrescription(){
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        List<DrugTo1> toPrescribe = new ArrayList<DrugTo1>();
        toPrescribe.add(this.getTestTransferObject());
        toPrescribe.get(0).setDrugId(2); // will trigger failure in test code.

        PrescriptionResponse resp = this.service.prescribe(toPrescribe, 1);

        assertFalse(resp.isSuccess());
        assertNotNull(resp.getMessage());

    }

    @Test
    public void testBasicHistory(){
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugSearchResponse resp = this.service.history(1, 1);
        assertNotNull(resp);
        assertEquals(resp.getContent().size(), 1);

    }

    @Test
    public void testEmptyHistory(){
    	
    	MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    	mockSecurityInfoManager.setAccessPatientRecord(true);
    	mockSecurityInfoManager.setPrivilege(true);
    	service.setSecurityInfoManager(mockSecurityInfoManager);
    	
        DrugSearchResponse resp = this.service.history(6, 1); //id > 5 will trigger empty list.

        assertNotNull(resp);
        assertEquals(resp.getContent().size(), 0);

    }

    @Test(expected = AccessDeniedException.class)
    public void testShouldHandleAccessDenied(){

        DrugSearchResponse resp = this.service.history(1, 6); //demo > 5 will trigger access denied

    }



    // ============ HELPER TEST METHODS ==============

    public DrugTo1 getTestTransferObject() {

        DrugTo1 t = new DrugTo1();

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

        t.setDrugId(1);
        t.setDemographicNo(1);
        t.setProviderNo(1);
        t.setGenericName("bangbar");
        t.setBrandName("foobar");
        t.setRegionalIdentifier("12345");
        t.setAtc("abcde");
        t.setTakeMax((float) 2.0);
        t.setTakeMin((float) 1.0);
        t.setRxDate((Date) startDate.clone());
        t.setEndDate((Date) endDate.clone());
        t.setFrequency("BID");
        t.setDuration(28);
        t.setDurationUnit("D");
        t.setRoute("PO");
        t.setForm("TAB");
        t.setPrn(false);
        t.setMethod("take");
        t.setRepeats(5);
        t.setInstructions("some string");
        t.setArchived(false);
        t.setArchivedReason("reason");
        t.setArchivedDate((Date) archivedDate.clone());
        t.setNoSubstitutions(false);
        t.setLongTerm(false);
        t.setExternalProvider(null);

        return t;
    }
}
