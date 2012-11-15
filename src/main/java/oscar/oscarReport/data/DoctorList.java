/*
 * DoctorList.java
 *
 * Created on August 27, 2007, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarReport.data;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.bean.ProviderNameBean;

public class DoctorList {
	
	public ArrayList<ProviderNameBean> getDoctorNameList() {

		ArrayList<ProviderNameBean> dnl = new ArrayList<ProviderNameBean>();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> docs = dao.getProvidersByType("doctor");

		for (Provider doc : docs) {
			ProviderNameBean pb = new ProviderNameBean();
			pb.setProviderID(doc.getProviderNo());
			pb.setProviderName(doc.getFullName());
			dnl.add(pb);
		}
		return dnl;
	}
}
