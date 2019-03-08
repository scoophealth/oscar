package org.oscarehr.prevention.reports;
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

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.AuthUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.PreventionSearchTo1;

public class PreventionReportBuilderTest extends DaoTestFixtures {
	private static Logger logger = MiscUtils.getLogger();
	
	@Before
 	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[] { "demographic", "demographicArchive" });
	}
	
	@Test
	public void reportRunner()  {
		String providerNo = "-1";
		PreventionSearchTo1 preventionSearchTo1 = new PreventionSearchTo1();
//		 {"id":null,"reportName":null,"ageStyle":1,"age1":"2","age2":null,"ageCalc":0,"ageAsOf":null,"rosterStat":"RO","rosterAsOf":null,"sex":1}
		preventionSearchTo1.setAge1("2");
		preventionSearchTo1.setAgeStyle(2);
		preventionSearchTo1.setAgeCalc(0);
		preventionSearchTo1.setRosterStat("RO");
		preventionSearchTo1.setSex(1);
		
		
		
		/*
		    <select class="form-control" ng-model="newReport.ageStyle">
			   <option value="0">---NO AGE SPECIFIED---</option>
                <option value="1">younger than</option>
                <option value="2">older than</option>
                <option value="3">equal too</option>
                <option value="4">ages between</option>
			</select>

		    <label for="provider">Age <small>in years, add m for months</small></label>
		    <input type="text" class="form-control" id="lower" placeholder="age" ng-model="newReport.age1">
		    <input type="text" class="form-control" id="high" placeholder="age" ng-show="newReport.agestyle == 4" ng-model="newReport.age2">
		  
			<label for="provider">Age Calculated</label>
		        		<input type="radio" ng-model="newReport.ageCalc" id="optionsRadios1" value="0" >
			    		When report is run
			    		<input type="radio" ng-model="newReport.ageCalc" id="optionsRadios2" value="1">
			   		as of : <input type="date" class="form-control" id="lower" placeholder="2020-03-31">
			  
			 <label for="provider">Roster Status</label>
		    		    <input type="radio" ng-model="newReport.rosterStat" value="{{rs.name}}">
				    {{rs.name}}
					as of : <input type="date" class="form-control" id="lower" ng-model="newReport.rosterAsOf" placeholder="2020-03-31">
		  	
	  		<select class="form-control" ng-model="newReport.sex">
		   		<option value="0">---NO SEX SPECIFIED---</option>
            		<option value="1">Female</option>
            		<option value="2">Male</option>
			</select>
		 */
		
		
		
		LoggedInInfo loggedInInfo = AuthUtils.initLoginContext();
		ReportBuilder reportBuilder = new ReportBuilder();
		Report report = reportBuilder.runReport(loggedInInfo, providerNo,preventionSearchTo1);
		logger.error(" number of items "+report.getItems().size());
		assertEquals(1,1);
	}
	
}
