/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.service.BillingONService;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.data.EctFormData.PatientForm;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;
/**
 * The echart seems to have non-note items in the note list. As a result this class will hold non-note items. A constructor can be made for each type of non-note item.
 */
public class NoteDisplayNonNote implements NoteDisplay {

	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Integer noteId;
	private Date date;
	private String note;
	private Provider provider;
	private boolean isFreeDraw = false;
	private boolean isEformData = false;
	private boolean isEncounterForm = false;
	private boolean isInvoice = false;
	private String linkInfo;
	
	@Override
    public Integer getAppointmentNo() {
		return null;
	}

	public NoteDisplayNonNote(Map<String, ? extends Object> eform) {
		try {
			date = (formatter.parse((String)eform.get("formDate") + " " + (String)eform.get("formTime")));
		}catch(ParseException e) {
			date = (Date) eform.get("formDateAsDate");			
		}
		
		note = eform.get("formName") + " : " + eform.get("formSubject");
		provider = providerDao.getProvider((String) eform.get("providerNo"));
		isEformData = true;
		noteId = new Integer((String) eform.get("fdid"));
	}

	public NoteDisplayNonNote(PatientForm patientForm) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		
		date = patientForm.edited;
		if( date == null ) {
			date = patientForm.created;
		}
		note = patientForm.formName;
		noteId = patientForm.formId;
		linkInfo = patientForm.jsp;
		isEncounterForm = true;
	}

	public NoteDisplayNonNote(BillingONCHeader1 h1) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		date = null;
		Date billDate = null;

                    
	   billDate = h1.getBillingDate();
                    
      if(  billDate != null ) {
			
	        cal1.setTime(h1.getBillingDate());
           
			
	        if( h1.getBillingTime() != null ) {
	            	cal2.setTime(h1.getBillingTime());
	            	cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
	            	cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
	            	cal1.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
	        }
            
			
			date = cal1.getTime();
      }                    
                
		StringBuilder tmpNote = new StringBuilder();
                BillingONService billingONService = (BillingONService) SpringUtils.getBean("billingONService");
		List<BillingONItem>items = billingONService.getNonDeletedInvoices(h1.getId());
		BillingONItem item;

		int size = items.size();
		for(int idx = 0; idx < size; ++idx) {
			item = items.get(idx);
			tmpNote.append(item.getServiceCode());
			if( idx < size - 1 ) {
				tmpNote.append("; ");
			}
		}
		
		String pname, creator;
		if( h1.getProviderNo() != null && h1.getProviderNo().length() > 0 ) {
			provider = providerDao.getProvider(h1.getProviderNo());
			pname = provider == null ? "Not Set" : provider.getFormattedName();
		}
		else {
			pname = "Not Set";
		}
		
		if( h1.getCreator() != null && h1.getCreator().length() > 0 ) {
			provider = providerDao.getProvider(h1.getCreator());
			creator = provider == null ? "Not Set" : provider.getFormattedName();
		}
		else {
			creator = "Not Set";
		}
		
		if( pname.equalsIgnoreCase(creator) ) {
			tmpNote.append(" billed by " + creator);
		}
		else {
			tmpNote.append(" billed by " + creator + " for " + pname);
		}
		
		note = tmpNote.toString();
		noteId = h1.getId();
		linkInfo = "/billing/CA/ON/billingONCorrection.jsp?billing_no=" + noteId.toString();
		isInvoice = true;
	}

	public ArrayList<String> getEditorNames() {
		return (new ArrayList<String>());
	}

	public String getEncounterType() {
		return "";
	}

	public boolean getHasHistory() {
		return false;
	}

	public ArrayList<String> getIssueDescriptions() {
		return (new ArrayList<String>());
	}

	public String getLocation() {
		return null;
	}

	public String getNote() {
		return note;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public CaseManagementNoteLink getNoteLink() {
		return null;
	}

	public Date getObservationDate() {
		return date;
	}

	public String getProgramName() {
		return null;
	}

	public String getProviderName() {
		if (provider != null) return (provider.getFormattedName());
		else return ("");
	}

	public String getProviderNo() {
		if (provider != null) return (provider.getProviderNo());
		else return ("");
	}

	public Integer getRemoteFacilityId() {
		return null;
	}

	public String getRevision() {
		return null;
	}

	public String getRoleName() {
		return null;
	}

	public Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl) {
		return null;
	}

	public String getStatus() {
		return null;
	}

	public Date getUpdateDate() {
		return date;
	}

	public String getUuid() {
		return null;
	}

	public boolean isCpp() {
		return false;
	}

	public boolean containsIssue(String issueCode) {
		return false;
	}

	public boolean isDocument() {
		return false;
	}

	public boolean isEditable() {
		return false;
	}

	public boolean isEformData() {
		return isEformData;
	}
	
	public boolean isFreeDraw() {
		return isFreeDraw;
	}

	public boolean isGroupNote() {
		return false;
	}

	public boolean isLocked() {
		return false;
	}

	public boolean isReadOnly() {
		return false;
	}

	public boolean isRxAnnotation() {
		return false;
	}

	public boolean isSigned() {
		return false;
	}

	public boolean isEncounterForm() {
		return isEncounterForm;
	}

	public String getLinkInfo() {
		return linkInfo;
	}

	@Override
    public String getEncounterTime() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getEncounterTransportationTime() {
	    // TODO Auto-generated method stub
	    return null;
    }

	public boolean isInvoice() {
    	return isInvoice;
    }

	public void setInvoice(boolean isInvoice) {
    	this.isInvoice = isInvoice;
    }
	
	public boolean isTicklerNote() {
		return false;
	}

}
