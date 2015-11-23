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
package org.oscarehr.ws.rest.conversion.summary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.stereotype.Component;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.EDocUtil.EDocSort;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.StringUtils;

@Component
public class LabsDocsSummary implements Summary {
	private static Logger logger = MiscUtils.getLogger();
	protected static final String ELLIPSES = "...";
	protected static final int MAX_LEN_TITLE = 48;
	protected static final int CROP_LEN_TITLE = 45;
	protected static final int MAX_LEN_KEY = 12;
	protected static final int CROP_LEN_KEY = 9;
	
	public  SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("Incoming",0,SummaryTo1.INCOMING_CODE);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		int count = 0;
		
		// Labs
		
		 //_newCasemgmt.documents
		 //_newCasemgmt.labResult
		
		CommonLabResultData comLab = new CommonLabResultData();
        ArrayList<LabResultData> labs = comLab.populateLabResultsData(loggedInInfo, "",""+demographicNo, "", "","","U");
                
		LinkedHashMap<String,LabResultData> accessionMap = new LinkedHashMap<String,LabResultData>();
		for (int i = 0; i < labs.size(); i++) {
			LabResultData result = labs.get(i);
			if (result.accessionNumber == null || result.accessionNumber.equals("")) {
				accessionMap.put("noAccessionNum" + i + result.labType, result);
			} else {
				if (!accessionMap.containsKey(result.accessionNumber + result.labType)) accessionMap.put(result.accessionNumber + result.labType, result);
			}
		}
		labs = new ArrayList<LabResultData>(accessionMap.values());
		
        //now we add individual module items
        String url = null;
        for( int idx = 0; idx < labs.size(); ++idx ) {
            LabResultData result =  labs.get(idx);
            Date date = result.getDateObj();
            String label = result.getLabel();
            String labDisplayName;
            
            if ( result.isMDS() ){ 
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label;                
                url = "../oscarMDS/SegmentDisplay.jsp?providerNo="+loggedInInfo.getLoggedInProvider().getProviderNo()+"&segmentID="+result.segmentID+"&status="+result.getReportStatus();
            }else if (result.isCML()){ 
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label; 
                url = "../lab/CA/ON/CMLDisplay.jsp?providerNo="+loggedInInfo.getLoggedInProvider().getProviderNo()+"&segmentID="+result.segmentID;                 
            }else if (result.isHL7TEXT()){
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label; 
                url = "../lab/CA/ALL/labDisplay.jsp?providerNo="+loggedInInfo.getLoggedInProvider().getProviderNo()+"&segmentID="+result.segmentID;
            }else {
            	if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
            	else labDisplayName = label;
                url = "../lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+loggedInInfo.getLoggedInProvider().getProviderNo();
            }
            
            SummaryItemTo1 summaryItem = new SummaryItemTo1(Integer.parseInt(result.segmentID), labDisplayName,"action","lab");//+result.labType);
            summaryItem.setDate(date);
            summaryItem.setAction(url);
            if(result.isAbnormal()){
            	summaryItem.setAbnormalFlag(true);
            }
            list.add(summaryItem);
            count++;
        } 
        
        //Docs
        ArrayList<EDoc> docList = EDocUtil.listDocs(loggedInInfo, "demographic", ""+demographicNo, null, EDocUtil.PRIVATE, EDocSort.OBSERVATIONDATE, "active");
		String dbFormat = "yyyy-MM-dd";

		String key;
		String title;
	
		

		// --- add remote documents ---
		
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			try {
				ArrayList<EDoc> remoteDocuments = EDocUtil.getRemoteDocuments(loggedInInfo, demographicNo);
				docList.addAll(remoteDocuments);
			} catch (Exception e) {
				logger.error("error getting remote documents", e);
			}
		}


		for (int i = 0; i < docList.size(); i++) {
			EDoc curDoc = docList.get(i);
			String dispFilename = org.apache.commons.lang.StringUtils.trimToEmpty(curDoc.getFileName());
			String dispStatus = String.valueOf(curDoc.getStatus());

			if (dispStatus.equals("A")) dispStatus = "active";
			else if (dispStatus.equals("H")) dispStatus = "html";

			String dispDocNo = curDoc.getDocId();
			title = StringUtils.maxLenString(curDoc.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);

			if (EDocUtil.getDocUrgentFlag(dispDocNo)) title = StringUtils.maxLenString("!" + curDoc.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
			
			SummaryItemTo1 summaryItem = new SummaryItemTo1(Integer.parseInt(curDoc.getDocId()), title,"action","document");
			

			DateFormat formatter = new SimpleDateFormat(dbFormat);
			String dateStr = curDoc.getObservationDate();
			
			try {
				Date date = formatter.parse(dateStr);
				summaryItem.setDate(date);
			} catch (ParseException ex) {
				MiscUtils.getLogger().debug("EctDisplayDocsAction: Error creating date " + ex.getMessage());
			}

			
	
			
			if( curDoc.getRemoteFacilityId()==null && curDoc.isPDF() ) {
				url = "../dms/MultiPageDocDisplay.jsp?segmentID=" + dispDocNo + "&providerNo=" + loggedInInfo.getLoggedInProviderNo() + "&searchProviderNo=" + loggedInInfo.getLoggedInProviderNo() + "&status=A&demoName=";//'); return false;";
			}
			else {
				url = "";// "../dms/ManageDocument.do?method=display&doc_no=" + dispDocNo + "&providerNo=" + loggedInInfo.getLoggedInProviderNo() + (curDoc.getRemoteFacilityId()!=null?"&remoteFacilityId="+curDoc.getRemoteFacilityId();
			}
			summaryItem.setAction(url);
			if(summaryItem.getDisplayName().trim().equals("")){
				summaryItem.setDisplayName("N/A");
			}
			//item.setLinkTitle(title + serviceDateStr);
			//item.setTitle(title);
			//key = StringUtils.maxLenString(curDoc.getDescription(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
			///key = StringEscapeUtils.escapeJavaScript(key);

			 list.add(summaryItem);
             count++;


		}

		
		Collections.sort(list, Collections.reverseOrder(new Comparator<SummaryItemTo1>() {
			  public int compare(SummaryItemTo1 o1, SummaryItemTo1 o2) {
			      return o1.getDate().compareTo(o2.getDate());
			  }
		}));
		
		
		
		return summary;
	}
}
