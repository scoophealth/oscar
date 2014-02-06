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

package oscar.util;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.util.DbConnectionFilter;

public class CBIFormDataSubmissionJob  extends TimerTask
{
	private static Logger logger = Logger.getLogger(CBIFormDataSubmissionJob.class);
	private static CBIUtil cbiUtil = new CBIUtil();

	@Override
	public void run()
	{
		logger.debug("CBI form data submission job starts now");
		try {
			doJob();
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
		logger.debug("CBI form data submission job finished");
	}

	public void doJob()
	{
		//List<Admission> admissionList = cbiUtil.getAdmissionDetailsToBeSubmittedToCBI();
		
		List<OcanStaffForm> cbiFormList = cbiUtil.getUnsubmittedCbiForms();
		//Only submit the latest cbi form for each admission_id
		logger.info("Found <"+(cbiFormList!=null?cbiFormList.size():"0")+"> CBI forms to be submitted to CBI");

		if (cbiFormList != null)
		{
			for (final OcanStaffForm cbiForm : cbiFormList)
			{
				processCbiFormSubmission(cbiForm);
			}
		}
	}

	public void processCbiFormSubmission(final OcanStaffForm cbiForm)
	{		
		try
		{
			if(cbiForm!=null) {
				//Maybe will use this method again. It depends on if the form should be signed or not.
				//Bboolean isSubmitted = cbiUtil.isFormDataAlreadySubmitted(cbiForm);
				logger.info("cbi form data not submitted. The ocan staff form id is : <"+(cbiForm!=null?cbiForm.getId():"null")+">");
					
				try
				{
					cbiUtil.submitCBIData(cbiForm);
					logger.debug("cbi form data submitted successfully. The ocan staff form id is : <"+(cbiForm!=null?cbiForm.getId():"null")+">");
				}
				catch (Exception e)
				{
					logger.error("Error in submission thread. The ocan staff form id is : <"+(cbiForm!=null?cbiForm.getId():"null")+">", e);
				}
				
			}
		}
		catch (Exception e)
		{
			logger.error("Error while processing admission", e);
		}
	}
}
