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
 CREATE TABLE `demographicQueryFavourites` (
  `favId` int(9) NOT NULL auto_increment,
  `selects` text,
  `age` varchar(255) default NULL,
  `startYear` varchar(8) default NULL,
  `endYear` varchar(8) default NULL,
  `firstName` varchar(255) default NULL,
  `lastName` varchar(255) default NULL,
  `rosterStatus` text,
  `sex` varchar(10) default NULL,
  `providerNo` text,
  `patientStatus` text,
  `queryName` varchar(255) default NULL,
  `archived` char(1) default NULL,
  PRIMARY KEY  (`favId`)
)
 */

package oscar.oscarReport.data;
import org.oscarehr.common.dao.DemographicQueryFavouritesDao;
import org.oscarehr.common.model.DemographicQueryFavourite;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarMessenger.docxfer.util.MsgCommxml;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;

public class RptDemographicQuerySaver {

	private DemographicQueryFavouritesDao demographicQueryFavouritesDao = SpringUtils.getBean(DemographicQueryFavouritesDao.class);

    public RptDemographicQuerySaver() {
    }

    public void saveQuery(RptDemographicReportForm frm){

        String[] select = frm.getSelect();
        String yearStyle        = frm.getAge();
        String startYear        = frm.getStartYear();
        String endYear          = frm.getEndYear();
        String[] rosterStatus   = frm.getRosterStatus();
        String[] patientStatus  = frm.getPatientStatus();
        String[] providers      = frm.getProviderNo();

        String firstName        = frm.getFirstName();
        String lastName         = frm.getLastName();
        String sex              = frm.getSex();
        String queryName        = frm.getQueryName();
        String demoIds        	= frm.getDemoIds();


        if (firstName != null ){
            firstName = firstName.trim();
        }

        if (lastName != null ){
            lastName = lastName.trim();
        }

        if (sex != null){
            sex = sex.trim();
        }

        String sqSelects = new String();
        String sqAge = yearStyle;
        String sqStartYear  = startYear   ;
        String sqEndYear  = endYear;
        String sqFirstName  = firstName;

        String sqLastName  = lastName;
        String sqRosterStatus  = new String();
        String sqSex  = sex;
        String sqProviderNo  = new String();
        String sqPatientStatus  = new String();
        String sqQueryName  = queryName;



        Document doc = MsgCommxml.newDocument();
        Element docRoot = MsgCommxml.addNode(doc, "root");

        if (select != null){
            for (int i=0; i < select.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",select[i]);
                docRoot.appendChild(table);
            }
        sqSelects = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (rosterStatus != null){
            for (int i=0; i < rosterStatus.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",rosterStatus[i]);
                docRoot.appendChild(table);
            }
        sqRosterStatus = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (patientStatus != null){
            for (int i=0; i < patientStatus.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",patientStatus[i]);
                docRoot.appendChild(table);
            }
        sqPatientStatus = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (providers != null){
            for (int i=0; i < providers.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",providers[i]);
                docRoot.appendChild(table);
            }
        sqProviderNo = MsgCommxml.toXML(docRoot);
        }

    	DemographicQueryFavourite dqf = new DemographicQueryFavourite();
    	dqf.setSelects(sqSelects);
    	dqf.setAge(sqAge);
    	dqf.setStartYear(sqStartYear);
    	dqf.setEndYear(sqEndYear);
    	dqf.setFirstName(sqFirstName);
    	dqf.setLastName(sqLastName);
    	dqf.setRosterStatus(sqRosterStatus);
    	dqf.setSex(sqSex);
    	dqf.setProviderNo(sqProviderNo);
    	dqf.setPatientStatus(sqPatientStatus);
    	dqf.setQueryName(sqQueryName);
    	dqf.setArchived("1");
    	dqf.setDemoIds(demoIds);

    	demographicQueryFavouritesDao.persist(dqf);

    }
}
