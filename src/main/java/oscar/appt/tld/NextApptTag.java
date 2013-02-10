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

package oscar.appt.tld;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author jay
 */
public class NextApptTag extends TagSupport {

	private String demoNo = null;
	private String date = null;
	private String format = null;

	/** Creates a new instance of NextApptTag */
	public NextApptTag() {
	}

	public void setDemographicNo(String demoNo1) {
		demoNo = demoNo1;
	}

	public String getDemographicNo() {
		return demoNo;
	}

	public int doStartTag() throws JspException {
		Date nextApptDate = null;
		if (demoNo != null && !demoNo.equalsIgnoreCase("") && !demoNo.equalsIgnoreCase("null")) {
			Integer demographicId = Integer.parseInt(demoNo);
			OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);
			Appointment appt = dao.findNextAppointment(demographicId);
			if(appt != null)
				nextApptDate = appt.getAppointmentDate();
		}

		String s = "";
		try {
			if (nextApptDate != null) {
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				s = formatter.format(nextApptDate);
			}
			JspWriter out = super.pageContext.getOut();
			out.print(s);
		} catch (Exception p) {
			MiscUtils.getLogger().error("Error", p);
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
