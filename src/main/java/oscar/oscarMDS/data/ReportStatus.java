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


package oscar.oscarMDS.data;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ReportStatus {

	private String providerName;

	private String providerNo;

	private String status;

	private String comment;

	private String timestamp;

	private String segmentID;
	
	private String oscarProviderNo;

	public ReportStatus() {
		// allow empty constructor
	}
	
	public ReportStatus(String pName, String pNo, String oscarProviderNo, String s, String c, String t, String sID) {
		this.oscarProviderNo = oscarProviderNo;
		providerName = pName;
		providerNo = pNo;
		status = s;
		comment = c;
		segmentID = sID;

		GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);

		// boneheaded calendar numbers months from 0
		if (t.length() > 18) {
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(5, 7)) - 1, Integer.parseInt(t.substring(8, 10)), Integer.parseInt(t.substring(11, 13)), Integer.parseInt(t.substring(14, 16)), Integer.parseInt(t.substring(17, 19)));
		} else if(t.length() == 10 ){
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(5, 7)) - 1, Integer.parseInt(t.substring(8, 10) ) );
		} else {
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(4, 6)) - 1, Integer.parseInt(t.substring(6, 8)), Integer.parseInt(t.substring(8, 10)), Integer.parseInt(t.substring(10, 12)), Integer.parseInt(t.substring(12, 14)));
		}
		timestamp = dateFormat.format(cal.getTime());
		
	}

	public ReportStatus(String pName, String pNo, String s, String c, String t, String sID) {
		providerName = pName;
		providerNo = pNo;
		status = s;
		comment = c;
		segmentID = sID;

		GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);

		// boneheaded calendar numbers months from 0
		if (t.length() > 18) {
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(5, 7)) - 1, Integer.parseInt(t.substring(8, 10)), Integer.parseInt(t.substring(11, 13)), Integer.parseInt(t.substring(14, 16)), Integer.parseInt(t.substring(17, 19)));
		} else if(t.length() == 10 ){
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(5, 7)) - 1, Integer.parseInt(t.substring(8, 10) ) );
		} else {
			cal.set(Integer.parseInt(t.substring(0, 4)), Integer.parseInt(t.substring(4, 6)) - 1, Integer.parseInt(t.substring(6, 8)), Integer.parseInt(t.substring(8, 10)), Integer.parseInt(t.substring(10, 12)), Integer.parseInt(t.substring(12, 14)));
		}
		timestamp = dateFormat.format(cal.getTime());
	}

	public ReportStatus(String pName, String pNo, String s, String c, long t, String sID) {
		providerName = pName;
		providerNo = pNo;
		status = s;
		comment = c;
		segmentID = sID;

		GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);

		cal.setTimeInMillis(t);

		timestamp = dateFormat.format(cal.getTime());
	}

	public String getProviderName() {
		return providerName;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public String getStatus() {
		return status;
	}

	public String getComment() {
		return comment;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getID() {
		return segmentID;
	}

	public String getSegmentID() {
		return (segmentID);
	}

	public void setSegmentID(String segmentID) {
		this.segmentID = segmentID;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the oscarProviderNo
	 */
    public String getOscarProviderNo() {
	    return oscarProviderNo;
    }

	/**
	 * @param oscarProviderNo the oscarProviderNo to set
	 */
    public void setOscarProviderNo(String oscarProviderNo) {
	    this.oscarProviderNo = oscarProviderNo;
    }

}
