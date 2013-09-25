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
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.OcanStaffForm;

public class CBIFormDataSubmissionJob  extends TimerTask
{
	private static Logger logger = Logger.getLogger(CBIFormDataSubmissionJob.class);
	private static CBIUtil cbiUtil = new CBIUtil();

	@Override
	public void run()
	{
		logger.info("### cbi form data submission job started ###");

		doJob();

		logger.info("### cbi form data submission job finished ###");
	}

	public void doJob()
	{
		List<Admission> admissionList = cbiUtil.getAdmissionDetailsToBeSubmittedToCBI();
		logger.info("## found <"+(admissionList!=null?admissionList.size():"0")+"> admissions detail to be submitted to CBI");

		if (admissionList != null)
		{
			for (final Admission admission : admissionList)
			{
				processAdmission(admission);
			}
		}
	}

	public void processAdmission(final Admission admission)
	{
		logger.info("## processing admission id : "+(admission!=null?admission.getId():"null"));
		try
		{
			final OcanStaffForm ocanStaffForm = cbiUtil.getCBIFormData(admission.getClientId());
			boolean isSubmitted = cbiUtil.isFormDataAlreadySubmitted(ocanStaffForm);
			if (!isSubmitted)
			{
				logger.info("## cbi form data not submitted for admission id : <"+(admission!=null?admission.getId():"null")+">. submitting now. ocan staff form id is : <"+(ocanStaffForm!=null?ocanStaffForm.getId():"null")+">");
				
				try
				{
					cbiUtil.submitCBIData(admission, ocanStaffForm);
					logger.info("## cbi form data submitted successfully. admission id : <"+(admission!=null?admission.getId():"null")+">. ocan staff form id is : <"+(ocanStaffForm!=null?ocanStaffForm.getId():"null")+">");
				}
				catch (Exception e)
				{
					logger.error("## Error in submission thread. admission id : <"+(admission!=null?admission.getId():"null")+">. ocan staff form id is : <"+(ocanStaffForm!=null?ocanStaffForm.getId():"null")+">", e);
				}
			}
			else
			{
				logger.info("## cbi form data already submitted for admission id : <"+(admission!=null?admission.getId():"null")+">. skipping it. ocan staff form id is : <"+(ocanStaffForm!=null?ocanStaffForm.getId():"null")+">");
			}
		}
		catch (Exception e)
		{
			logger.error("Error while processing admission", e);
		}
	}
}
