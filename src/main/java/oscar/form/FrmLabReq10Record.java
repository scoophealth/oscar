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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.caisi_integrator.ws.CachedDemographicForm;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;

public class FrmLabReq10Record extends FrmRecord {
	private static Logger logger=MiscUtils.getLogger();

	private DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");

	public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
        	Demographic demographic=demographicDao.getDemographicById(demographicNo);

            if (demographic!=null) {
                props.setProperty("demographic_no", String.valueOf(demographic.getDemographicNo()));
                props.setProperty("patientName", demographic.getLastName()+", "+ demographic.getFirstName());
                props.setProperty("healthNumber", StringUtils.trimToEmpty(demographic.getHin()));
                props.setProperty("version", StringUtils.trimToEmpty(demographic.getVer()));
                props.setProperty("hcType", StringUtils.trimToEmpty(demographic.getHcType()));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));

                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                java.util.Date dob = UtilDateUtilities.calcDate(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth());
                props.setProperty("birthDate", StringUtils.trimToEmpty(UtilDateUtilities.DateToString(dob, "yyyy/MM/dd")));

                props.setProperty("phoneNumber", StringUtils.trimToEmpty(demographic.getPhone()));
                props.setProperty("patientAddress", StringUtils.trimToEmpty(demographic.getAddress()));
                props.setProperty("patientCity", StringUtils.trimToEmpty(demographic.getCity()));
                props.setProperty("patientPC", StringUtils.trimToEmpty(demographic.getPostal()));
                props.setProperty("province", StringUtils.trimToEmpty(demographic.getProvince()));
                String sex = StringUtils.trimToEmpty(demographic.getSex());
                if( sex.startsWith("F") ) {
                	props.setProperty("female", "on");
                }
                else {
                	props.setProperty("male", "on");
                }
                props.setProperty("sex",sex);
                props.setProperty("demoProvider", StringUtils.trimToEmpty(demographic.getProviderNo()));
            }

            //get local clinic information
        	ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
        	Clinic clinic = clinicDao.getClinic();

            	props.setProperty("clinicName",clinic.getClinicName()==null?"":clinic.getClinicName());
            	props.setProperty("clinicProvince",clinic.getClinicProvince()==null?"":clinic.getClinicProvince());
                props.setProperty("clinicAddress", clinic.getClinicAddress()==null?"":clinic.getClinicAddress());
                props.setProperty("clinicCity", clinic.getClinicCity()==null?"":clinic.getClinicCity());
                props.setProperty("clinicPC", clinic.getClinicPostal()==null?"":clinic.getClinicPostal());


        } else {
            String sql = "SELECT * FROM formLabReq10 WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public Properties getFormCustRecord(Properties props, String provNo) {
                             
        ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");                
        Provider mrp = null;
        String demoProvider = props.getProperty("demoProvider","");   
        
        if (!demoProvider.isEmpty()) {
            mrp =  providerDao.getProvider(demoProvider);

            if (mrp != null) {
                props.setProperty("provName", "MRP: " + mrp.getFormattedName());            
            }
        }
                        
        OscarProperties oscarProps = OscarProperties.getInstance();
        
        if ((oscarProps.getProperty("lab_req_provider") != null) && (oscarProps.getProperty("lab_req_billing_no") != null)) {
            
            props.setProperty("reqProvName", oscarProps.getProperty("lab_req_provider"));
            props.setProperty("practitionerNo", oscarProps.getProperty("lab_req_billing_no"));
            
    	} else {             
            Provider provider = providerDao.getProvider(provNo);
                
            String ohipNo = provider.getOhipNo();
            if (ohipNo == null || ohipNo.isEmpty()) {                
                provider = mrp;            
            }  
            
            if (provider != null) {
                
                String xmlSpecialtyCode = "<xml_p_specialty_code>";
                String xmlSpecialtyCode2 = "</xml_p_specialty_code>";
                String strSpecialtyCode = "00";
                String comments = provider.getComments();     

                if( comments.indexOf(xmlSpecialtyCode) != -1 ) {                                    
                    String specialtyCode = comments.substring(comments.indexOf(xmlSpecialtyCode)+xmlSpecialtyCode.length(), comments.indexOf(xmlSpecialtyCode2));
                    specialtyCode = specialtyCode.trim();
                    if(!specialtyCode.isEmpty()) {
                        strSpecialtyCode = specialtyCode;
                    }
                }                                            
                
                ohipNo = provider.getOhipNo();
            
                if (ohipNo != null && !ohipNo.isEmpty()) {                                        
                    props.setProperty("reqProvName", provider.getFormattedName());
                    props.setProperty("practitionerNo", "0000-" + ohipNo + "-" + strSpecialtyCode);
                }
            }
        }
                                                 	    	    	    	
        //get local clinic information
        ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
    	Clinic clinic = clinicDao.getClinic();

    	props.setProperty("clinicName",clinic.getClinicName()==null?"":clinic.getClinicName());
    	props.setProperty("clinicProvince",clinic.getClinicProvince()==null?"":clinic.getClinicProvince());
        props.setProperty("clinicAddress", clinic.getClinicAddress()==null?"":clinic.getClinicAddress());
        props.setProperty("clinicCity", clinic.getClinicCity()==null?"":clinic.getClinicCity());
        props.setProperty("clinicPC", clinic.getClinicPostal()==null?"":clinic.getClinicPostal());

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formLabReq10 WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formLabReq10 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public static List<Properties> getPrintRecords(int demographicNo) throws SQLException {
        String sql = "SELECT * FROM formLabReq10 WHERE demographic_no = " + demographicNo;
        return ((new FrmRecordHelp()).getPrintRecords(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }


    public static Properties getRemoteRecordProperties(Integer remoteFacilityId, Integer formId) throws IOException
    {
    	FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
    	pk.setIntegratorFacilityId(remoteFacilityId);
    	pk.setCaisiItemId(formId);

    	DemographicWs demographicWs=CaisiIntegratorManager.getDemographicWs();
    	CachedDemographicForm form=demographicWs.getCachedDemographicForm(pk);

    	ByteArrayInputStream bais=new ByteArrayInputStream(form.getFormData().getBytes());

    	Properties p=new Properties();
    	p.load(bais);

    	// missing
        // props.setProperty("hcType", demographic.getHcType());
    	// props.setProperty("demoProvider", demographic.getProviderNo());
    	// props.setProperty("clinicProvince",oscar.Misc.getString(rs, "clinic_province"));

    	logger.debug("Remote properties : "+p);

    	return(p);
    }
}
