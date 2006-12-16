package org.oscarehr.casemgmt.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.oscarehr.casemgmt.dao.CaseManagementCPPDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementCPPDAOHibernate extends HibernateDaoSupport
		implements CaseManagementCPPDAO {

	public CaseManagementCPP getCPP(String demographic_no) {
		List results = this.getHibernateTemplate()
		.find("from CaseManagementCPP cpp where cpp.demographic_no = ?",new Object[] {demographic_no});
		return (results.size()!=0)?(CaseManagementCPP)results.get(0):null;
	}

	public void saveCPP(CaseManagementCPP cpp) {
		/*find the old cpp record for client*/
		String demoNo=cpp.getDemographic_no();
		CaseManagementCPP tempcpp=getCPP(demoNo);
				
		String fhist=cpp.getFamilyHistory()==null?"":cpp.getFamilyHistory();
		String ongoing=cpp.getOngoingConcerns()==null?"":cpp.getOngoingConcerns();
		String shist=cpp.getSocialHistory()==null?"":cpp.getSocialHistory();
		String rem=cpp.getReminders()==null?"":cpp.getReminders();
		String mhist=cpp.getMedicalHistory()==null?"":cpp.getMedicalHistory();
		if (tempcpp==null) tempcpp=cpp;
		tempcpp.setFamilyHistory(fhist);
		tempcpp.setMedicalHistory(mhist);
		tempcpp.setOngoingConcerns(ongoing);
		tempcpp.setReminders(rem);
		tempcpp.setSocialHistory(shist);
		tempcpp.setUpdate_date(new Date());
		tempcpp.setPrimaryPhysician(cpp.getPrimaryPhysician());
		tempcpp.setPrimaryCounsellor(cpp.getPrimaryCounsellor());
		this.getHibernateTemplate().saveOrUpdate(tempcpp);
	}

}
