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


/*
 * AcknowledgementData.java
 *
 * Created on July 9, 2007, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarMDS.data.ReportStatus;

public class AcknowledgementData {

	private static Logger logger = MiscUtils.getLogger();

	/** Creates a new instance of AcknowledgementData */
	private AcknowledgementData() {
		// don't instantiate
	}

	public static ArrayList<ReportStatus> getAcknowledgements(String segmentID) {
		ArrayList<ReportStatus> acknowledgements = null;
		try {

			acknowledgements = new ArrayList<ReportStatus>();
			String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='" + segmentID + "' and providerLabRouting.lab_type='HL7'";
			ResultSet rs = DBHandler.GetSQL(sql);
			while (rs.next()) {
				acknowledgements.add(new ReportStatus(oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "provider_no"), oscar.Misc.getString(rs, "status"), oscar.Misc.getString(rs, "comment"), oscar.Misc.getString(rs, "timestamp"), segmentID));
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Could not retrieve acknowledgement data", e);
		}
		return acknowledgements;
	}

	public static ArrayList<ReportStatus> getAcknowledgements(String docType, String segmentID) {
		ArrayList<ReportStatus> acknowledgements = null;
		try {

			acknowledgements = new ArrayList<ReportStatus>();
			String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='" + segmentID + "' and providerLabRouting.lab_type='" + docType + "'";
			ResultSet rs = DBHandler.GetSQL(sql);
			while (rs.next()) {
				acknowledgements.add(new ReportStatus(oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "provider_no"), oscar.Misc.getString(rs, "status"), oscar.Misc.getString(rs, "comment"), oscar.Misc.getString(rs, "timestamp"), segmentID));
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Could not retrieve acknowledgement data", e);
		}

		return acknowledgements;
	}
}
