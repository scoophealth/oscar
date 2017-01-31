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
package org.oscarehr.jobs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.OnCallClinicDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.ScheduleDateDao;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;

public class OscarOnCallClinic implements OscarRunnable {
	private Provider provider = null;
	private static String SCHEDULE_TEMPLATE = "P:OnCallClinic";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE MMMM d, yyyy");
	private static String DOCUMENTDIR = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
	 
	@Override
	public void run() {
		MiscUtils.getLogger().info("Starting OSCAR ON CALL CLINIC Job");
		OnCallClinicDao onCallClinicDao = SpringUtils.getBean(OnCallClinicDao.class);
		Calendar yesterday = Calendar.getInstance();	
		MiscUtils.getLogger().info("DATE " + yesterday.getTime());
		yesterday.add(Calendar.DATE, -1);	
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);
		yesterday.set(Calendar.MILLISECOND, 0);
		MiscUtils.getLogger().info("DATE " + yesterday.getTime());
		Date d = yesterday.getTime();
		
		if( onCallClinicDao.findByDate(d) != null ) {
			MiscUtils.getLogger().info("YESTERDAY WAS ON CALL CLINIC");
			
			OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
			ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
			ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
			List<Object[]> results = appointmentDao.findAppointments(d, d);
			MiscUtils.getLogger().info("FOUND " + results.size() + " appointments");			
			
			for( Object[] result : results ) {
				Appointment appointment = (Appointment)result[0];
				if( appointment.getStatus().matches(".*C.*") ) {
					MiscUtils.getLogger().info("Skipping appointment as it is canceled");
				}
				else {
					ScheduleDate scheduleDate = scheduleDateDao.findByProviderNoAndDate(appointment.getProviderNo(), appointment.getAppointmentDate());
					if( scheduleDate != null && scheduleDate.getHour().equalsIgnoreCase(SCHEDULE_TEMPLATE) ) {
						Demographic demographic = (Demographic)result[1];
						if( demographic.getProviderNo() != null && !demographic.getProviderNo().equals(appointment.getProviderNo())) {
							ProviderData providerData = providerDataDao.find(appointment.getProviderNo());
													
							String filename = "OSCAROnCallClinic" + new Date().getTime() + ".pdf";
							
							if( appointment.getStatus().matches(".*N.*") ) {
								if( makeNoShowApptDocument(filename, appointment, demographic, providerData)) {
									SendDocument(filename, demographic);
								}
							}
							else {
								
								if( makeGoodApptDocument(filename, appointment, demographic, providerData) ) {
									SendDocument(filename, demographic);
								}
							}
						}
					}
					else {
						MiscUtils.getLogger().info("Skipping appointment as it does not belong to on call clinic schedule");
					}
				}
			}
			
		}
		MiscUtils.getLogger().info("Finished OSCAR ON CALL CLINIC Job");
	}
	
	
	private Boolean makeNoShowApptDocument(String filename, Appointment appointment, Demographic demographic, ProviderData providerData) {
		Document document = new Document();
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream(DOCUMENTDIR + filename));
			Rectangle pageSize = new Rectangle(PageSize.A5.getWidth(), PageSize.A5.getHeight());
			pageSize.setBackgroundColor(new BaseColor(0xCC, 0xCC, 0xFF));
			document.setPageSize(pageSize);
	        document.setMargins(36, 72, 108, 180);
	        document.setMarginMirroringTopBottom(true);
			document.open();
			Font headerFont = new Font(FontFamily.HELVETICA, 14);
			Chunk chunkHeader = new Chunk("OSCAR ON CALL CLINIC", headerFont);
			chunkHeader.setUnderline(2f, -2f);
			Paragraph header = new Paragraph(chunkHeader);
			header.setAlignment(Element.ALIGN_CENTER);
			header.setExtraParagraphSpace(24f);
			document.add(header);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
		
			Font bodyFont = new Font(FontFamily.TIMES_ROMAN, 12);
			Chunk chunkAttn = new Chunk("ATTN: " + demographic.getProvider().getFormattedName(), bodyFont);
			Paragraph attnParagraph = new Paragraph(chunkAttn);
			attnParagraph.setAlignment(Element.ALIGN_LEFT);
			attnParagraph.setExtraParagraphSpace(24f);
			document.add(attnParagraph);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			
			Font patientFont = new Font(FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.BLUE);
			Chunk patientChunk = new Chunk(demographic.getFormattedName(), patientFont);
			Paragraph body = new Paragraph();
			Chunk body1 = new Chunk("Your patient ", bodyFont);
			Chunk body2 = new Chunk(" did NOT show for an appointment on " + simpleDateFormat.format(appointment.getAppointmentDate()) + 
					".  The appointment was with " + providerData.getFirstName() + " " + providerData.getLastName());
			
			body.add(body1);
			body.add(patientChunk);
			body.add(body2);
			body.setAlignment(Element.ALIGN_LEFT);
			document.add(body);
			document.close();
					
		
		} catch (FileNotFoundException e) {
			MiscUtils.getLogger().error("ERROR", e);
			return false;
			
		} catch (DocumentException e) {
			MiscUtils.getLogger().error("ERROR", e);
			return false;
		}
		
		return true;
		
	}
	
	private Boolean makeGoodApptDocument(String filename, Appointment appointment, Demographic demographic, ProviderData providerData) {
		Document document = new Document();
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream(DOCUMENTDIR + filename));
			Rectangle pageSize = new Rectangle(PageSize.A5.getWidth(), PageSize.A5.getHeight());
			pageSize.setBackgroundColor(new BaseColor(0xCC, 0xCC, 0xFF));
			document.setPageSize(pageSize);
	        document.setMargins(36, 72, 108, 180);
	        document.setMarginMirroringTopBottom(true);
			document.open();
			Font headerFont = new Font(FontFamily.HELVETICA, 14);
			Chunk chunkHeader = new Chunk("OSCAR ON CALL CLINIC", headerFont);
			chunkHeader.setUnderline(2f, -2f);
			Paragraph header = new Paragraph(chunkHeader);
			header.setAlignment(Element.ALIGN_CENTER);
			header.setExtraParagraphSpace(24f);
			document.add(header);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			String reason = appointment.getReason() == null || "".equals(appointment.getReason())? "" : " for \"" + appointment.getReason() + "\"";
			Font bodyFont = new Font(FontFamily.TIMES_ROMAN, 12);
			Chunk chunkAttn = new Chunk("ATTN: " + demographic.getProvider().getFormattedName(), bodyFont);
			Paragraph attnParagraph = new Paragraph(chunkAttn);
			attnParagraph.setAlignment(Element.ALIGN_LEFT);
			attnParagraph.setExtraParagraphSpace(24f);
			document.add(attnParagraph);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			
			Font patientFont = new Font(FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.BLUE);
			Chunk patientChunk = new Chunk(demographic.getFormattedName(), patientFont);
			Paragraph body = new Paragraph();
			Chunk body1 = new Chunk("Your patient ", bodyFont);
			Chunk body2 = new Chunk(" was seen on " + simpleDateFormat.format(appointment.getAppointmentDate()) + " by " + 
			providerData.getFirstName() + " " + providerData.getLastName() + reason, bodyFont);
			
			body.add(body1);
			body.add(patientChunk);
			body.add(body2);
			body.setAlignment(Element.ALIGN_LEFT);
			document.add(body);
			document.close();
					
		
		} catch (FileNotFoundException e) {
			MiscUtils.getLogger().error("ERROR", e);
			return false;
			
		} catch (DocumentException e) {
			MiscUtils.getLogger().error("ERROR", e);
			return false;
		}
		
		return true;
	}

	private void SendDocument(String fileName, Demographic demographic) {		
		String user = "System";
		String mrp = demographic.getProviderNo();
		EDoc newDoc = new EDoc("", "", fileName, "", user, user, "", 'A',
							   oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", demographic.getDemographicNo().toString(), 0);
		newDoc.setDocPublic("0");
		
        // if the document was added in the context of a program
		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
		
		ProgramProvider pp = programManager.getCurrentProgramInDomain(null, provider.getProviderNo());
		if(pp != null && pp.getProgramId() != null) {
			newDoc.setProgramId(pp.getProgramId().intValue());
		}
		
		newDoc.setFileName(fileName);
		newDoc.setContentType("application/pdf");
		newDoc.setType("on-call clinic");
		newDoc.setDescription("On-Call Clinic");
		
		newDoc.setNumberOfPages(1);
		String doc_no = EDocUtil.addDocumentSQL(newDoc);
				
		ProviderInboxRoutingDao providerInboxRoutingDao = SpringUtils.getBean(ProviderInboxRoutingDao.class);
		providerInboxRoutingDao.addToProviderInbox(mrp, Integer.parseInt(doc_no), "DOC");
		
		
		PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
		
		PatientLabRouting patientLabRouting = new PatientLabRouting();
		patientLabRouting.setDemographicNo(demographic.getDemographicNo());
		patientLabRouting.setLabNo(Integer.parseInt(doc_no));
		patientLabRouting.setLabType("DOC");
		patientLabRoutingDao.persist(patientLabRouting);
		
		
		MiscUtils.getLogger().info("Sent Document");
	}
	
	

	@Override
	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;

	}

	@Override
	public void setLoggedInSecurity(Security security) {
		// TODO Auto-generated method stub

	}

}
