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
package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.SpringUtils;

public class BillingONCHeader1DaoTest extends DaoTestFixtures {

	protected BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);

	public BillingONCHeader1DaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_on_cheader1", "billing_on_item", "gstControl", "billingservice", "provider", 
				"demographic", "lst_gender", "admission", "demographic_merged", "program", 
				"health_safety", "provider", "providersite", "site", "program_team","log", "Facility","billing_on_payment");
	}

	@Test
	public void testCreate() throws Exception {
		BillingONCHeader1 entity = new BillingONCHeader1();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
         
    @Test
	public void testCountBillingVisitsByCreator() {
        assertNotNull(dao.countBillingVisitsByCreator("100", new Date(), new Date()));
    }
        
    @Test
	public void testCountBillingVisitsByProvider() {
        assertNotNull(dao.countBillingVisitsByProvider("100", new Date(), new Date()));
	}

    @Test
    public void testFindBillingsByManyThings() {
	    assertNotNull(dao.findBillingsByManyThings("STAT", null, null, null, null));
	    assertNotNull(dao.findBillingsByManyThings("STAT", null, null, null, 100));
	    assertNotNull(dao.findBillingsByManyThings("STAT", null , null, new Date(), 100));
	    assertNotNull(dao.findBillingsByManyThings("STAT", null , new Date(), new Date(), 100));
	    assertNotNull(dao.findBillingsByManyThings("STAT", "PROV" , new Date(), new Date(), 100));
    }

    @Test
    public void testFindByProviderStatusAndDateRange() {
    	Date date = new Date(0);
	    assertNotNull(dao.findByProviderStatusAndDateRange("100", Arrays.asList(new String[] {"A"}), new DateRange(date, new Date())));
	    assertNotNull(dao.findByProviderStatusAndDateRange("100", Arrays.asList(new String[] {"A"}), new DateRange(null, null)));
	    assertNotNull(dao.findByProviderStatusAndDateRange("100", Arrays.asList(new String[] {"A"}), new DateRange(null, new Date())));
	    assertNotNull(dao.findByProviderStatusAndDateRange("100", Arrays.asList(new String[] {"A"}), new DateRange(new Date(), null)));
    }
    
    @Test
    public void testFindBillingsAndDemographicsById() {
	    assertNotNull(dao.findBillingsAndDemographicsById(100));
    }

    @Test
    public void testFindByMagic() {
	    assertNotNull(dao.findByMagic(Arrays.asList(new String[] {"PP"}), "STS_TY", "PROV_NO", new Date(), new Date(), 100,null,null,null));
    }
    
    @Test
    public void testFindByMagic2() {
	    assertNotNull(dao.findByMagic2(Arrays.asList(new String[] {"PP"}), "STS_TY", "PROV_NO", 
	    		new Date(), new Date(), 100, Arrays.asList(new String[] {"SVN_CODE"}), "DX", "VIS_TYPE",null,null,null));
    }
    
    @Test
    public void testFindBillingsByDemoNoCh1HeaderServiceCodeAndDate() {
	    assertNotNull(dao.findBillingsByDemoNoCh1HeaderServiceCodeAndDate(100, Arrays.asList(new String[] {"PP"}), new Date(), new Date()));
    }
    
    @Override
    protected List<String> getSimpleExceptionTestExcludes() {
		List<String> excludes = super.getSimpleExceptionTestExcludes();
		// this is very JSP specific method that includes a mix of SQL fields, we will test it manuall in #findBillingData
		excludes.add("findBillingData");
		
	    return excludes;
    }
}
