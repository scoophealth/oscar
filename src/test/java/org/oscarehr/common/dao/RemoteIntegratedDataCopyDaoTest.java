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

/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. 
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.MessageDigest;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RemoteIntegratedDataCopy;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.ObjectMarshalUtil;
import org.oscarehr.util.SpringUtils;


public class RemoteIntegratedDataCopyDaoTest extends DaoTestFixtures {
	
	RemoteIntegratedDataCopyDao  remoteIntegratedDataCopyDao = SpringUtils.getBean(RemoteIntegratedDataCopyDao.class);  

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"RemoteIntegratedDataCopy"});
	}
	
	@Test
	public void testCreate() throws Exception{
		Provider drw = getProvider("Marcus","Welby","111112","111112","doctor","00","M");
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.save(1,drw,drw.getProviderNo(),1);
		
		assertEquals("111112",remoteIntegratedDataCopy.getProviderNo());
		assertEquals(""+1,""+remoteIntegratedDataCopy.getFacilityId());
		assertEquals(Provider.class.getName(),remoteIntegratedDataCopy.getDataType());

		assertNotNull(remoteIntegratedDataCopy.getId());
		assertNotNull(remoteIntegratedDataCopyDao.find(remoteIntegratedDataCopy.getId()));
				
		Provider drwCopy = remoteIntegratedDataCopyDao.getObjectFrom(Provider.class, remoteIntegratedDataCopy);
		RemoteIntegratedDataCopy remoteIntegratedDataCopy2 =  remoteIntegratedDataCopyDao.findByDemoType(1,1, Provider.class.getName());
		
		if(remoteIntegratedDataCopy2 == null){
			MiscUtils.getLogger().error("it's null");
			assertNotNull(remoteIntegratedDataCopy2);
		}
		Provider drwCopy2 = remoteIntegratedDataCopyDao.getObjectFrom(Provider.class,remoteIntegratedDataCopy2 );
						
		compareProvider(drw,drwCopy);
		compareProvider(drw,drwCopy2);
		
		assertNull(remoteIntegratedDataCopyDao.save(1, drwCopy,drwCopy.getProviderNo(),1));
		assertNull(remoteIntegratedDataCopyDao.save(1, drwCopy2,drwCopy2.getProviderNo(),1));
		
	}
	
	
	@Test
	public void testCreateWithType() throws Exception{
		Provider drw = getProvider("Jaymus","Melby","111113","111113","doctor","00","M");
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.save(1,drw,drw.getProviderNo(),1,"provider");
		
		assertEquals("111113",remoteIntegratedDataCopy.getProviderNo());
		assertEquals(""+1,""+remoteIntegratedDataCopy.getFacilityId());
		assertEquals(Provider.class.getName()+"+provider",remoteIntegratedDataCopy.getDataType());

		assertNotNull(remoteIntegratedDataCopy.getId());
		assertNotNull(remoteIntegratedDataCopyDao.find(remoteIntegratedDataCopy.getId()));
				
		Provider drwCopy = remoteIntegratedDataCopyDao.getObjectFrom(Provider.class, remoteIntegratedDataCopy);
		RemoteIntegratedDataCopy remoteIntegratedDataCopy2 =  remoteIntegratedDataCopyDao.findByDemoType(1,1, Provider.class.getName()+"+provider");
		
		if(remoteIntegratedDataCopy2 == null){
			MiscUtils.getLogger().error("it's null");
			assertNotNull(remoteIntegratedDataCopy2);
		}
		Provider drwCopy2 = remoteIntegratedDataCopyDao.getObjectFrom(Provider.class,remoteIntegratedDataCopy2 );
						
		compareProvider(drw,drwCopy);
		compareProvider(drw,drwCopy2);
		
		assertNull(remoteIntegratedDataCopyDao.save(1, drwCopy,drwCopy.getProviderNo(),1,"provider"));		
		assertNull(remoteIntegratedDataCopyDao.save(1, drwCopy2,drwCopy2.getProviderNo(),1,"provider"));
		
	}
	
	
	@Test
	public void testCreateArray() throws Exception{
		
		Provider[] providers = new Provider[4];
		providers[0] = getProvider("Livingstone","drl","100",null,"doctor","00","M");
		providers[1] = getProvider("Kimble","drk","101",null,"doctor","00","M");
		providers[2] = getProvider("Trainee","drt","102",null,"doctor","00","M");
		providers[3] = getProvider("Samson","drs","103",null,"doctor","00","M");
		
		String marshalledObject = ObjectMarshalUtil.marshalToString(providers); 
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.digest(marshalledObject.getBytes());

		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.save(1, providers,"1",1);
		
		assertNotNull(remoteIntegratedDataCopy.getId());
		assertNotNull(remoteIntegratedDataCopyDao.find(remoteIntegratedDataCopy.getId()));
		
		Provider[] providersCopy = remoteIntegratedDataCopyDao.getObjectFrom(Provider[].class, remoteIntegratedDataCopy);
		
		for (int i = 0; i < providers.length;i++){
			compareProvider(providers[i],providersCopy[i]);
		}
		
		
	}

	
	void compareProvider(Provider p1, Provider p2){
		assertEquals(p1.getFirstName(),p2.getFirstName());
		assertEquals(p1.getLastName(),p2.getLastName());
		assertEquals(p1.getProviderNo(),p2.getProviderNo());
		assertEquals(p1.getOhipNo(),p2.getOhipNo());
		assertEquals(p1.getProviderType(),p2.getProviderType());
		assertEquals(p1.getSpecialty(),p2.getSpecialty());
		assertEquals(p1.getSex(),p2.getSex());
		assertEquals(p1.getStatus(),p2.getStatus());
	}
	
	
	Provider getProvider(String firstName,String lastName,String providerNo,String ohipNo,String providerType,String specialty,String sex){
		Provider provider = new Provider();
		provider.setFirstName(firstName);
		provider.setLastName(lastName);
		provider.setProviderNo(providerNo);
		provider.setOhipNo(ohipNo);
		provider.setSpecialty(specialty);
		provider.setProviderType(providerType);
		provider.setSex(sex);
		provider.setSignedConfidentiality(new Date());
		provider.setStatus("1");
		return provider;
	}
	
	
	
}
