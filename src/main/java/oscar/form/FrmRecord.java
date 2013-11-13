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

package oscar.form;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Properties;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.util.UtilDateUtilities;

/**
 * 
 *
 */
public abstract class FrmRecord {

	protected Demographic demographic;
	protected DemographicExt demographicExt;
	protected Map<String, String> demographicExtMap;
	
	protected DemographicDao demographicDao;
	protected DemographicExtDao demographicExtDao;
	
	protected java.util.Date date;
	protected String dateFormat;

	public abstract Properties getFormRecord(int demographicNo, int existingID) throws SQLException;

	public abstract int saveFormRecord(Properties props) throws SQLException;

	public abstract String findActionValue(String submit) throws SQLException;

	public abstract String createActionURL(String where, String action, String demoId, String formId) throws SQLException;


	public Properties getGraph(int demographicNo, int existingID) throws SQLException {
		return new Properties();
	}

	public Properties getCaisiFormRecord(int demographicNo, int existingID, int providerNo, int programNo) throws SQLException {
		return new Properties();
	}

	public void setGraphType(String graphType) { /*Rourke needs to know whether plotting head circ or height*/
	}

	
	
	public FrmRecord() {	
		this.demographicDao = SpringUtils.getBean(DemographicDao.class);
		this.demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	} 
	
	
	protected void setDemoProperties(int demographicNo, Properties demoProps) {
		
		this.setDemographic(demographicNo);
		
        date = UtilDateUtilities.calcDate(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth());
        demoProps.setProperty("demographic_no", demographic.getDemographicNo().toString());

        demoProps.setProperty("c_surname", demographic.getLastName());
        demoProps.setProperty("c_givenName", demographic.getFirstName());
        demoProps.setProperty("c_address", demographic.getAddress());
        demoProps.setProperty("c_city", demographic.getCity());
        demoProps.setProperty("c_province", demographic.getProvince());
        demoProps.setProperty("c_postal", demographic.getPostal());
        demoProps.setProperty("c_phn", demographic.getHin());
        demoProps.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date, dateFormat));
        demoProps.setProperty("pg1_age", String.valueOf(UtilDateUtilities.getNumYears(date, GregorianCalendar.getInstance().getTime())));
        demoProps.setProperty("c_phone", demographic.getPhone());
        demoProps.setProperty("c_phoneAlt1", demographic.getPhone2());
        
        String rd = SxmlMisc.getXmlContent(demographic.getFamilyDoctor(), "rd");
        rd = rd != null ? rd : "";
        demoProps.setProperty("pg1_famPhy", rd);

        Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographicNo);
        String cell = demoExt.get("demo_cell");
        if ( cell != null ){
        	demoProps.setProperty("c_phoneAlt2",cell );
        }
	}
	
	protected void setDemoCurProperties(int demographicNo, Properties demoProps) {
		
		this.setDemographic(demographicNo);

		demoProps.setProperty("c_surname_cur", demographic.getLastName());
		demoProps.setProperty("c_givenName_cur", demographic.getFirstName());
		demoProps.setProperty("c_address_cur", demographic.getAddress());
		demoProps.setProperty("c_city_cur", demographic.getCity());
		demoProps.setProperty("c_province_cur", demographic.getProvince());
		demoProps.setProperty("c_postal_cur", demographic.getPostal());
		demoProps.setProperty("c_phn_cur", demographic.getHin());
		demoProps.setProperty("c_phone_cur", demographic.getPhone());
		demoProps.setProperty("c_phoneAlt1_cur", demographic.getPhone2());
		
        Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographicNo);
        String cell = demoExt.get("demo_cell");
        if ( cell != null ){
        	demoProps.setProperty("c_phoneAlt2_cur",cell );
        }
	}
	
	protected void setDemographic(int demographicNo) {
		if (this.demographicDao != null) {
			this.demographic = demographicDao.getClientByDemographicNo(demographicNo);
			this.setDemographicExt(demographicNo);
		}
	}

	protected void setDemographicExt(int demographicNo) {
		if (this.demographicExtDao != null) {
			this.demographicExt = demographicExtDao.getDemographicExt(demographicNo);
			this.demographicExtMap = demographicExtDao.getAllValuesForDemo(demographicNo);
		}
	}
}
