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
import org.oscarehr.common.dao.DemographicQueryFavouritesDao;
import org.oscarehr.common.model.DemographicQueryFavourite;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import oscar.oscarMessenger.docxfer.util.MsgCommxml;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;


public class RptDemographicQueryLoader {

	private DemographicQueryFavouritesDao demographicQueryFavouritesDao = SpringUtils.getBean(DemographicQueryFavouritesDao.class);

    public RptDemographicQueryLoader() {
    }

    public RptDemographicReportForm queryLoader(RptDemographicReportForm frm){
        String qId = frm.getSavedQuery();
        RptDemographicReportForm dRF = new RptDemographicReportForm();

        String mSelect         = null;
        String mAge           = null;
		String mStartYear = null;
		String mEndYear   = null;
		String mFirstName = null;
		String mLastName  = null;
		String mRosterStatus  = null;
		String mSex   = null;
		String mProviderNo    = null;
		String mPatientStatus = null;
		String mDemoIds = null;


		  DemographicQueryFavourite dqf = demographicQueryFavouritesDao.find(Integer.parseInt(qId));
		  if(dqf != null) {
			  mSelect = dqf.getSelects();
			  mAge = dqf.getAge();
			  mStartYear = dqf.getStartYear();
			  mEndYear = dqf.getEndYear();
			  mFirstName = dqf.getFirstName();
			  mLastName = dqf.getLastName();
			  mRosterStatus = dqf.getRosterStatus();
			  mSex = dqf.getSex();
			  mProviderNo = dqf.getProviderNo();
			  mPatientStatus = dqf.getPatientStatus();
			  mDemoIds = dqf.getDemoIds();
		  }


          if (mSelect != null && mSelect.length() != 0){
            String[] t = fromXMLtoArray(mSelect);
            dRF.setSelect(t);
          }
          if (mAge != null && mAge.length() != 0){
            dRF.setAge(mAge);
          }
          if (mStartYear != null && mStartYear.length() != 0){
            dRF.setStartYear(mStartYear);
          }
          if (mEndYear != null && mEndYear.length() != 0){
            dRF.setEndYear(mEndYear);
          }
          if (mFirstName != null && mFirstName.length() != 0){
            dRF.setFirstName(mFirstName);
          }
          if (mLastName != null && mLastName.length() != 0){
            dRF.setLastName(mLastName);
          }


          if (mRosterStatus != null && mRosterStatus.length() != 0){
            String[] t = fromXMLtoArray(mRosterStatus);
            dRF.setRosterStatus(t);
          }
          if (mSex != null && mSex.length() != 0){
            dRF.setSex(mSex);
          }
          if (mRosterStatus != null && mRosterStatus.length() != 0){
            String[] t = fromXMLtoArray(mRosterStatus);
            dRF.setRosterStatus(t);
          }
          if (mProviderNo != null && mProviderNo.length() != 0){
            String[] t = fromXMLtoArray(mProviderNo);
            dRF.setProviderNo(t);
          }
          if (mPatientStatus != null && mPatientStatus.length() != 0){
            String[] t = fromXMLtoArray(mPatientStatus);
            dRF.setPatientStatus(t);
          }
          if (mDemoIds != null && !mDemoIds.isEmpty()) {
        	  dRF.setDemoIds(mDemoIds);
          }

          return dRF;
    }

     String[] fromXMLtoArray(String xStr){

        Document doc = MsgCommxml.parseXML(xStr);

        Element docRoot = doc.getDocumentElement();

        NodeList nlst = docRoot.getChildNodes();

          String[] retval ;
         retval = new String [nlst.getLength()];
        for (int i = 0; i < nlst.getLength(); i++){


           Element temp = (Element) nlst.item(i);
           retval[i] = temp.getAttribute("value");
        }

        return retval;
    }
}
