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

package oscar.oscarReport.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarPrevention.reports.PreventionReportUtil;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.util.DateUtils;
import oscar.util.UtilDateUtilities;

public class RptDemographicQueryBuilder {

	int theWhereFlag;
	int theFirstFlag;
	StringBuilder stringBuffer = null;

	public void whereClause() {
		if (stringBuffer != null) {
			if (theWhereFlag == 0) {
				stringBuffer.append(" where ");
				theWhereFlag = 1;
			}
		}
	}

	public void firstClause() {
		if (theFirstFlag != 0) {
			stringBuffer.append(" and ");
			theFirstFlag = 1;
		}
	}

	public RptDemographicQueryBuilder() {
	}

	public java.util.ArrayList<ArrayList<String>> buildQuery(LoggedInInfo loggedInInfo, RptDemographicReportForm frm) {
		return buildQuery(loggedInInfo, frm, null);
	}

	public java.util.ArrayList<ArrayList<String>> buildQuery(LoggedInInfo loggedInInfo, RptDemographicReportForm frm, String asofRosterDate) {
		MiscUtils.getLogger().debug("in buildQuery");

		String[] select = frm.getSelect();
		stringBuffer = new StringBuilder("select ");

		String ageStyle = frm.getAgeStyle();
		String yearStyle = frm.getAge();
		String startYear = frm.getStartYear();
		String endYear = frm.getEndYear();
		String[] rosterStatus = frm.getRosterStatus();
		String[] patientStatus = frm.getPatientStatus();
		String[] providers = frm.getProviderNo();

		String firstName = frm.getFirstName();
		String lastName = frm.getLastName();
		String sex = frm.getSex();

		String orderBy = frm.getOrderBy();
		String limit = frm.getResultNum();

		String asofDate = frm.getAsofDate();

		if (asofDate == null || asofDate.trim().isEmpty() 
				|| UtilDateUtilities.getDateFromString(asofDate, "yyyy-MM-dd") == null) {
			asofDate = "CURRENT_DATE";
		} else {
			asofDate = "'" + asofDate + "'";
		}

		RptDemographicColumnNames demoCols = new RptDemographicColumnNames();

		oscar.oscarMessenger.util.MsgStringQuote s = new oscar.oscarMessenger.util.MsgStringQuote();
		if (firstName != null) {
			firstName = firstName.trim();
		}

		if (lastName != null) {
			lastName = lastName.trim();
		}

		if (sex != null) {
			sex = sex.trim();
		}

		theWhereFlag = 0;
		theFirstFlag = 0;

		boolean getprovider = false;
		for (int i = 0; i < select.length; i++) {
			if (select[i].equalsIgnoreCase("provider_name")) {
				stringBuffer.append(" concat(p.last_name,', ',p.first_name) " + select[i] + " ");
				getprovider = true;
				if (i < (select.length - 1)) {
					stringBuffer.append(", ");
				}
				continue;
			}
			if (i == (select.length - 1)) {
				
				if(select[i].equalsIgnoreCase("ver")){
					stringBuffer.append(" CAST(d." + select[i] + " as CHAR) ");
				}else{
					stringBuffer.append(" d." + select[i] + " ");
				}
				
			} else {
				
				
				if(select[i].equalsIgnoreCase("ver")){
					stringBuffer.append(" CAST(d." + select[i] + " as CHAR) " + ", ");
				}else{
					stringBuffer.append(" d." + select[i] + ", ");
				}
			}

		}

		stringBuffer.append(" from demographic d ");
		if (getprovider) {
			stringBuffer.append(", provider p");
		}
		int yStyle = 0;
		try {
			yStyle = Integer.parseInt(yearStyle);
		} catch (Exception e) {
			//empty
		}

		// value="0"> nothing specified
		// value="1">born before
		// value="2">born after
		// value="3">born in
		// value="4">born between

		/*switch (yStyle){
		    case 1:
		        whereClause();
		        stringBuffer.append(" ( year_of_birth < "+startYear+"  )");
		        theFirstFlag = 1;
		        break;
		    case 2:
		        whereClause();
		        stringBuffer.append(" ( year_of_birth > "+startYear+"  )");
		        theFirstFlag = 1;
		        break;
		    case 3:
		        whereClause();
		        stringBuffer.append(" ( year_of_birth = "+startYear+"  )");
		        theFirstFlag = 1;
		        break;
		    case 4:
		        whereClause();
		        stringBuffer.append(" ( year_of_birth > "+startYear+" and year_of_birth < "+endYear+" ) ");
		        theFirstFlag = 1;
		        break;
		}*/
		// value="0"> nothing specified
		// value="1">born before
		// value="2">born after
		// value="3">born in
		// value="4">born between

		MiscUtils.getLogger().debug("date style" + yStyle);
		switch (yStyle) {
		case 1:
			whereClause();
			if (ageStyle.equals("1")) {
				stringBuffer.append(" ( ( YEAR(" + asofDate + ") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(" + asofDate + ",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) <  " + startYear + " ) ");
			} else {
				stringBuffer.append(" ( YEAR(" + asofDate + ") - d.year_of_birth < " + startYear + "  ) ");
			}
			theFirstFlag = 1;
			break;
		case 2:
			whereClause();
			//if (ageStyle.equals("1")){
			stringBuffer.append(" ( ( YEAR(" + asofDate + ") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(" + asofDate + ",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) >  " + startYear + " ) ");
			//}else{
			//   stringBuffer.append(" ( YEAR("+asofDate+") - year_of_birth > "+startYear+"  ) ");
			//}
			theFirstFlag = 1;
			break;
		case 3:
			whereClause();
			if (ageStyle.equals("1")) {
				stringBuffer.append(" ( ( YEAR(" + asofDate + ") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(" + asofDate + ",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) =  " + startYear + " ) ");
			} else {
				stringBuffer.append(" ( YEAR(" + asofDate + ") - d.year_of_birth = " + startYear + "  ) ");
			}
			theFirstFlag = 1;
			break;
		case 4:
			whereClause();
			MiscUtils.getLogger().debug("age style " + ageStyle);
			if (!ageStyle.equals("2")) {
				// stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth),'-',(month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >  "+startYear+" and ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth),'-',(month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) <  "+endYear+"  ) ");
				MiscUtils.getLogger().debug("VERIFYING INT" + startYear);
				//check to see if its a number
				if (verifyInt(startYear)) {
					stringBuffer.append(" ( ( YEAR(" + asofDate + ") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(" + asofDate + ",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) >  " + startYear + " ) ");
				} else {
					String interval = getInterval(startYear);
					stringBuffer.append(" ( date_sub(" + asofDate + ",interval " + interval + ") >= DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d')   ) ");
				}
				stringBuffer.append(" and ");
				if (verifyInt(endYear)) {
					stringBuffer.append(" ( ( YEAR(" + asofDate + ") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(" + asofDate + ",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) <  " + endYear + "  ) ");
				} else {
					///
					String interval = getInterval(endYear);
					stringBuffer.append(" ( date_sub(" + asofDate + ",interval " + interval + ") < DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d')   ) ");
				}
			} else {
				stringBuffer.append(" ( YEAR(" + asofDate + ") - d.year_of_birth > " + startYear + "  and YEAR(" + asofDate + ") - d.year_of_birth < " + endYear + "  ) ");
			}
			theFirstFlag = 1;
			break;
		}

		if (rosterStatus != null) {
			whereClause();
			firstClause();
			stringBuffer.append(" ( ");
			for (int i = 0; i < rosterStatus.length; i++) {
				theFirstFlag = 1;
				if (i == (rosterStatus.length - 1)) {
					stringBuffer.append(" d.roster_status = '" + rosterStatus[i] + "' )");
				} else {
					stringBuffer.append(" d.roster_status = '" + rosterStatus[i] + "' or  ");
				}
			}
		}

		if (patientStatus != null) {
			whereClause();
			firstClause();
			stringBuffer.append(" ( ");
			for (int i = 0; i < patientStatus.length; i++) {
				theFirstFlag = 1;
				if (i == (patientStatus.length - 1)) {
					stringBuffer.append(" d.patient_status = '" + patientStatus[i] + "' )");
				} else {
					stringBuffer.append(" d.patient_status = '" + patientStatus[i] + "' or  ");
				}
			}
		}

		if (providers != null) {
			whereClause();
			firstClause();
			stringBuffer.append(" ( ");
			for (int i = 0; i < providers.length; i++) {
				theFirstFlag = 1;
				if (i == (providers.length - 1)) {
					stringBuffer.append(" d.provider_no = '" + providers[i] + "' )");
				} else {
					stringBuffer.append(" d.provider_no = '" + providers[i] + "' or  ");
				}
			}
		}

		if (lastName != null && lastName.length() != 0) {
			MiscUtils.getLogger().debug("last name = " + lastName + "<size = " + lastName.length());
			whereClause();
			firstClause();
			theFirstFlag = 1;
			stringBuffer.append(" ( ");
			stringBuffer.append(" d.last_name like '" + s.q(lastName) + "%'");
			stringBuffer.append(" ) ");
		}

		if (firstName != null && firstName.length() != 0) {
			whereClause();
			firstClause();
			theFirstFlag = 1;
			stringBuffer.append(" ( ");
			stringBuffer.append(" d.first_name like '" + s.q(firstName) + "%'");
			stringBuffer.append(" ) ");
		}

		yStyle = 0;
		try {
			yStyle = Integer.parseInt(sex);
		} catch (Exception e) {
			//empty
		}
		switch (yStyle) {
		case 1:
			whereClause();
			firstClause();
			stringBuffer.append(" ( d.sex =  'F'  )");
			theFirstFlag = 1;
			break;
		case 2:
			whereClause();
			firstClause();
			stringBuffer.append(" ( d.sex = 'M' )");
			theFirstFlag = 1;
			break;

		}

		//removed roster_status condition in place more complex check below

		if (getprovider) {
			whereClause();
			firstClause();
			stringBuffer.append(" ( d.provider_no = p.provider_no )");
		}
		
		List<Integer> demoIds = frm.getDemographicIds();
		if (!demoIds.isEmpty()) {
			whereClause();
			firstClause();

			stringBuffer.append("(");
			boolean isFirst = true;
			for (Integer i : demoIds) {
				if (isFirst) {
					isFirst = false;
				} else {
					stringBuffer.append(" OR ");
				}
				stringBuffer.append("d.demographic_no = " + i);
			}
			stringBuffer.append(")");
		}

		if (orderBy != null && orderBy.length() != 0) {
			if (!orderBy.equals("0")) {
				stringBuffer.append(" order by " + demoCols.getColumnName(orderBy) + " ");
			}
		}

		if (limit != null && limit.length() != 0) {
			if (!limit.equals("0")) {
				try {
					Integer.parseInt(limit);
					stringBuffer.append(" limit " + limit + " ");
				} catch (Exception u) {
					MiscUtils.getLogger().debug("limit was not numeric >" + limit + "<");
				}
			}
		}

		MiscUtils.getLogger().debug("SEARCH SQL STATEMENT \n" + stringBuffer.toString());
		java.util.ArrayList<ArrayList<String>> searchedArray = new java.util.ArrayList<ArrayList<String>>();
		try {
			MiscUtils.getLogger().info(stringBuffer.toString());

			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			for (Object[] o : dao.runNativeQuery(stringBuffer.toString())) {
				if (o == null) {
					continue;
				}
				
				String demoNo = null;
				java.util.ArrayList<String> tempArr = new java.util.ArrayList<String>();
				for (int i = 0; i < select.length; i++) {
					String fieldName = select[i];
					String fieldValue = o[i] == null ? null : String.valueOf(o[i]);

					tempArr.add(fieldValue);
					
					if ("demographic_no".equals(fieldName)) {
						demoNo = fieldValue;
						MiscUtils.getLogger().debug("Demographic :" + demoNo + " is in the list");
					}
				}

				// need to check if they were rostered at this point to this provider  (asofRosterDate is only set if this is being called from prevention reports)
				if (demoNo != null && asofRosterDate != null && providers != null && providers.length > 0) {
					//Only checking the first doc.  Only one should be included for finding the cumulative bonus
					try {
						if (!PreventionReportUtil.wasRosteredToThisProvider(loggedInInfo, Integer.parseInt(demoNo), DateUtils.parseDate(asofRosterDate, null), providers[0])) {
							MiscUtils.getLogger().info("Demographic :" + demoNo + " was not included in returned array because they were not rostered to " + providers[0] + " on " + asofRosterDate);
							continue;
						} else {
							MiscUtils.getLogger().info("Demographic :" + demoNo + " was included in returned array because they were not rostered to " + providers[0] + " on " + asofRosterDate);
						}
					} catch (NumberFormatException e) {
						MiscUtils.getLogger().error("Error", e);
					} catch (ParseException e) {
						MiscUtils.getLogger().error("Error", e);
					}
				}

				searchedArray.add(tempArr);

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return searchedArray;
	}

	boolean verifyInt(String str) {
		boolean verify = true;
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			verify = false;
		}
		return verify;
	}

	String getInterval(String startYear) {
		MiscUtils.getLogger().debug("in getInterval startYear " + startYear);
		String str = "";
		if (startYear.charAt(startYear.length() - 1) == 'm') {
			str = startYear.substring(0, (startYear.length() - 1)) + " month";
		}
		MiscUtils.getLogger().debug(str);
		return str;
	}
}
