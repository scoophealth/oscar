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

package org.oscarehr.ws.conversion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.DrugLookUpManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.conversion.DrugConverter;
import org.oscarehr.ws.rest.to.model.DrugSearchTo1;
import org.oscarehr.ws.rest.to.model.DrugTo1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DrugConverterTest extends DrugConverter {

    @Before
    public void before(){
        this.drugLookUpManager = new MockDrugLookUpManager();
    }

    @After
    public void after(){
        this.drugLookUpManager = null;
    }

    @Test
    public void testValidDomainToTransfer() throws Exception {

        Drug d = new Drug();

        LoggedInInfo info = new LoggedInInfo();

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

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

        DrugTo1 t = this.getAsTransferObject(info, d);

        assertEquals(1, (int)t.getDrugId());
        assertEquals(1, (int) t.getDemographicNo());
        assertEquals(1, (int) t.getProviderNo());
        assertEquals("Foobar", t.getBrandName());
        assertEquals("Barbang", t.getGenericName());
        assertEquals("12345", t.getRegionalIdentifier());
        assertEquals("abcde", t.getAtc());
        assertEquals(1.0, t.getTakeMin(), 0.01);
        assertEquals(2.0, t.getTakeMax(), 0.01);
        assertEquals(startDate.toString(), t.getRxDate().toString());
        assertEquals(endDate.toString(), t.getEndDate().toString());
        assertEquals("BID", t.getFrequency());
        assertEquals(28, (int) t.getDuration());
        assertEquals("D", t.getDurationUnit());
        assertEquals("PO", t.getRoute());
        assertEquals("TAB", t.getForm());
        assertTrue(t.isPrn());
        assertEquals("Take", t.getMethod());
        assertEquals(5, (int) t.getRepeats());
        assertEquals("some string", t.getInstructions());
        assertEquals(archivedDate.toString(), t.getArchivedDate().toString());
        assertEquals("reason", t.getArchivedReason());
        assertFalse(t.isArchived());

    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidDurationNumberStrings() {
        Drug d = new Drug();

        LoggedInInfo info = new LoggedInInfo();

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

        // SHOULD CAUSE THE EXCEPTION
        d.setDuration("NOT A NUMBER");

        // Other fields
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

        DrugTo1 t = this.getAsTransferObject(info, d);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidProviderNumberString() {

        Drug d = new Drug();

        LoggedInInfo info = new LoggedInInfo();

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

        // SHOULD CAUSE THE EXCEPTION
        d.setProviderNo("NOT A NUMBER");

        // Other fields
        d.setId(1);
        d.setDemographicId(1);
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

        DrugTo1 t = this.getAsTransferObject(info, d);
    }

    @Test
    public void testTransferToDomainObject() {

        DrugTo1 t = new DrugTo1();

        LoggedInInfo info = new LoggedInInfo();

        Date startDate = new Date();
        Date endDate = new Date();
        Date archivedDate = new Date();

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
        t.setStrength(new Float(10.0));
        t.setStrengthUnit("MG");
        t.setExternalProvider("foo");
        t.setLongTerm(false);
        t.setNoSubstitutions(false);

        Drug d = this.getAsDomainObject(info, t);

        assertEquals(1, (int) d.getDemographicId());
        assertEquals("1", d.getProviderNo());
        assertEquals("bangbar", d.getGenericName());
        assertEquals("foobar", d.getBrandName());
        assertEquals("12345", d.getRegionalIdentifier());
        assertEquals("abcde", d.getAtc());
        assertEquals(1.0, d.getTakeMin(), 0.01);
        assertEquals(2.0, d.getTakeMax(), 0.01);
        assertEquals(startDate.toString(), d.getRxDate().toString());
        assertEquals(endDate.toString(), d.getEndDate().toString());
        assertEquals("BID", d.getFreqCode());
        assertEquals("28", d.getDuration());
        assertEquals("D", d.getDurUnit());
        assertEquals("PO", d.getRoute());
        assertEquals("TAB", d.getDrugForm());
        assertEquals("take", d.getMethod());
        assertEquals("some string", d.getSpecial());
        assertFalse(d.isPrn());
        assertEquals("reason", d.getArchivedReason());
        assertFalse(d.isArchived());
        assertEquals(archivedDate.toString(), d.getArchivedDate().toString());
        assertFalse(d.isLongTerm());
        assertFalse(d.isNoSubs());
        assertEquals("foo", d.getOutsideProviderName());

    }

    @Test
    public void testPopulateDrugStrengthNormalInput(){
        Drug d = new Drug();
        DrugTo1 t = new DrugTo1();
        t.setStrengthUnit("mg");
        t.setStrength((float) 100);
        Boolean result = this.populateDrugStrength(d, t);
        assertEquals("100.0 mg", d.getDosage());
        assertEquals("mg", d.getUnit());
        assertTrue(result);
    }

    @Test
    public void testPopulateDrugStrengthNoStrengthInfo(){
        Drug d = new Drug();
        d.setBrandName("aspirin");

        DrugTo1 t = new DrugTo1();

        Boolean result = this.populateDrugStrength(d, t);

        assertEquals("1.0 mg", d.getDosage());
        assertEquals("mg", d.getUnit());
        assertTrue(result);
    }

    @Test
    public void testPopulateDrugStrengthNoValidStrength(){
        Drug d = new Drug();
        DrugTo1 t = new DrugTo1();
        t.setStrengthUnit("mg");
        t.setStrength(null);

        Boolean result = this.populateDrugStrength(d, t);

        assertFalse(result);
    }

    @Test
    public void testPopulateDrugStrengthNoValidStrengthUnit(){
        Drug d = new Drug();
        DrugTo1 t = new DrugTo1();
        t.setStrengthUnit(null);
        t.setStrength((float) 100);

        Boolean result = this.populateDrugStrength(d, t);

        assertFalse(result);
    }

    @Test
    public void testPopulateTo1StrengthNormal(){

        DrugTo1 t = new DrugTo1();
        Drug d = new Drug();

        // aspirin will trigger response from MockDrugLookUpManager
        d.setBrandName("aspirin");

        Boolean resp = this.populateTo1Strength(t, d);

        assertTrue(resp);
        assertEquals((float) 1.0, t.getStrength());
        assertEquals("mg", t.getStrengthUnit());

    }

    @Test
    public void testPopulateTo1StrengthNonNullStrength(){

        DrugTo1 t = new DrugTo1();
        Drug d = new Drug();

        t.setStrength((float) 100.0);

        // aspirin will trigger response from MockDrugLookUpManager
        d.setBrandName("aspirin");

        Boolean resp = this.populateTo1Strength(t, d);

        assertFalse(resp);
        assertEquals((float) 100.0, t.getStrength());

    }

    @Test
    public void testPopulateTo1StrengthNonNullStrengthUnit(){

        DrugTo1 t = new DrugTo1();
        Drug d = new Drug();

        t.setStrengthUnit("mg");

        // aspirin will trigger response from MockDrugLookUpManager
        d.setBrandName("aspirin");

        Boolean resp = this.populateTo1Strength(t, d);

        assertFalse(resp);
        assertEquals("mg", t.getStrengthUnit());

    }

    @Test
    public void testPopulateTo1StrengthNullBrandName(){

        DrugTo1 t = new DrugTo1();
        Drug d = new Drug();
        Boolean resp = this.populateTo1Strength(t, d);
        assertFalse(resp);

    }

    @Test
    public void testPopulateTo1StrengthNonRecognizedDrug() {

        DrugTo1 t = new DrugTo1();
        Drug d = new Drug();

        d.setBrandName("not aspirin");

        Boolean resp = this.populateTo1Strength(t, d);

        assertFalse(resp);

    }


    private class MockDrugLookUpManager extends DrugLookUpManager{

        public MockDrugLookUpManager(){

        }

        public List<DrugSearchTo1> search(String name){

            List<DrugSearchTo1> toReturn = new ArrayList<DrugSearchTo1>();

            // we treat aspirin as a special case for testing purposes.
            if(name.equals("aspirin")){
                DrugSearchTo1 d = new DrugSearchTo1();
                d.setName("aspirin");
                d.setId(1);
                toReturn.add(d);
            }

            return toReturn;

        }

        public DrugSearchTo1 details(String id){

            DrugSearchTo1 s = new DrugSearchTo1();

            if(id.equals("1")){
                List<DrugSearchTo1.DrugComponentTo1> components = new ArrayList<DrugSearchTo1.DrugComponentTo1>();
                DrugSearchTo1.DrugComponentTo1 c = new DrugSearchTo1.DrugComponentTo1();
                c.setStrength(1.0);
                c.setUnit("mg");
                components.add(c);
                s.setComponents(components);
                return s;
            }

            return null;

        }

    }

}
