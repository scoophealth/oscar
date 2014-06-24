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
package org.oscarehr.renal;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.oscarehr.common.dao.ORNCkdScreeningReportLogDao;
import org.oscarehr.common.model.ORNCkdScreeningReportLog;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ORNCkdScreeningReportThread extends Thread {

	private String providerNo = null;
	
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	
	public void run() {
		try {
			CkdScreenerReportHandler report = new CkdScreenerReportHandler();
			List<CKDReportContainer> ckds = report.generateReport();

			CkdScreeningReportContainer container = new CkdScreeningReportContainer();
			container.setItems(ckds);
		
			ORNCkdScreeningReportLogDao logDao = SpringUtils.getBean(ORNCkdScreeningReportLogDao.class);
			JAXBContext context = JAXBContext.newInstance(CkdScreeningReportContainer.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
		    StringWriter sw = new StringWriter();
		    m.marshal(container, sw);
		    
		    ORNCkdScreeningReportLog loggedReport = new ORNCkdScreeningReportLog();
		    loggedReport.setProviderNo(providerNo);
		    loggedReport.setReportData(sw.toString());
		    logDao.persist(loggedReport);
		    
		}catch(JAXBException e) {
			MiscUtils.getLogger().error("Error",e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
	
	
}
