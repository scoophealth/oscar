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

package org.oscarehr.casemgmt.service;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.util.ExtPrint;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

public class MeasurementPrint implements ExtPrint {

	private static Logger logger = MiscUtils.getLogger();
	
	
	@Override
	public void printExt(CaseManagementPrintPdf engine,HttpServletRequest request) throws IOException, DocumentException{
		logger.info("measurement print!!!!");
		MeasurementDao measurementsDao = SpringUtils.getBean(MeasurementDao.class);
		String startDate = request.getParameter("pStartDate");
		String endDate = request.getParameter("pEndDate");
		String demographicNo = request.getParameter("demographicNo");
		
		logger.info("startDate = "+startDate);
		logger.info("endDate = "+endDate);
		logger.info("demographicNo = "+demographicNo);
		
		List<Measurement> measurements = null;
		
		if(startDate.equals("") && endDate.equals("")) {
			measurements = measurementsDao.findByDemographicId(Integer.parseInt(demographicNo));
		} else {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date dStartDate = formatter.parse(startDate);
				Date dEndDate = formatter.parse(endDate);
				measurements = measurementsDao.findByDemographicIdObservedDate(Integer.parseInt(demographicNo),dStartDate,dEndDate);
			}catch(Exception e){logger.error(e);}
			
		}
		
		if( engine.getNewPage() )
            engine.getDocument().newPage();
        else
            engine.setNewPage(true);
        
        Font obsfont = new Font(engine.getBaseFont(), engine.FONTSIZE, Font.UNDERLINE);                
       
        
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Phrase phrase = new Phrase(engine.LEADING, "\n\n", engine.getFont());
        p.add(phrase);
        phrase = new Phrase(engine.LEADING, "Measurements", obsfont);        
        p.add(phrase);
        engine.getDocument().add(p);
        
        //go through each appt in reverge chronological order, and print the measurements
        String lastDate = null;       
        PdfPTable table = null;
        for(Measurement measurement:measurements) {
        	boolean newDate=false;
        	String date = engine.getFormatter().format(measurement.getDateObserved());
        	if(lastDate==null) {
        		lastDate = date;
        		newDate = true;
        	} else {
        		if(!lastDate.equals(date)) {
        			newDate=true;
        			lastDate = date;
        		}
        	}
        	if(newDate) {        		        		
        		p = new Paragraph();
        		phrase = new Phrase(engine.LEADING, "", engine.getFont());              
        		Chunk chunk = new Chunk("Documentation Date: " + engine.getFormatter().format(measurement.getDateObserved()) + "\n\n", obsfont);
        		phrase.add(chunk);                    
        		p.add(phrase);
        		table = new PdfPTable(2);
        		printMeasurementEntries(date,measurements,table);        		
        		engine.getDocument().add(p);
        		engine.getDocument().add(table);
        	}
	        
	        //
	       // engine.getDocument().add(table);	       	      	        
        }
        //engine.getDocument().add(p);
	}

	private void printMeasurementEntries(String date, List<Measurement> measurements, PdfPTable table) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Map<String, Measurement> map = new HashMap<String,Measurement>();
		
		for(Measurement measurement:measurements) {
			String d = formatter.format(measurement.getDateObserved());
			if(d.equals(date)) {
				map.put(measurement.getType(), measurement);
			}
		}
		
		printMeasurementEntry("od_ar_sph","OD AR SPH",map,table);
		printMeasurementEntry("od_ar_cyl","OD AR CYL",map,table);
		printMeasurementEntry("od_ar_axis","OD AR AXIS",map,table);
		printMeasurementEntry("od_k1","OD k1",map,table);
		printMeasurementEntry("od_k2","OD k2",map,table);
		printMeasurementEntry("od_k2_axis","k2 axis",map,table);
		printMeasurementEntry("od_sc_distance","OD sc distance",map,table);
		printMeasurementEntry("od_cc_distance","OD cc distance",map,table);
		printMeasurementEntry("od_ph_distance","OD ph distance",map,table);
		printMeasurementEntry("od_sc_near","OD sc near",map,table);
		printMeasurementEntry("od_cc_near","OD cc near",map,table);
		
		printMeasurementEntry("os_ar_sph","OS AR SPH",map,table);
		printMeasurementEntry("os_ar_cyl","OS AR CYL",map,table);
		printMeasurementEntry("os_ar_axis","OS AR AXIS",map,table);
		printMeasurementEntry("os_k1","OS k1",map,table);
		printMeasurementEntry("os_k2","OS k2",map,table);
		printMeasurementEntry("os_k2_axis","OS k2 axis",map,table);
		printMeasurementEntry("os_sc_distance","OS sc distance",map,table);
		printMeasurementEntry("os_cc_distance","OS cc distance",map,table);
		printMeasurementEntry("os_ph_distance","OS ph distance",map,table);
		printMeasurementEntry("os_sc_near","OS sc near",map,table);
		printMeasurementEntry("os_cc_near","OS cc near",map,table);			
		
		
		printMeasurementEntry("od_manifest_refraction_sph","OD Manifest SPH",map,table);
		printMeasurementEntry("od_manifest_refraction_cyl","OD Manifest CYL",map,table);
		printMeasurementEntry("od_manifest_refraction_axis","OD Manifest AXIS",map,table);
		printMeasurementEntry("od_manifest_refraction_add","OD Manifest ADD",map,table);
		printMeasurementEntry("od_manifest_refraction_distance","OD Manifest VA",map,table);
		printMeasurementEntry("od_manifest_refraction_near","OD Manifest N",map,table);		
		printMeasurementEntry("od_cycloplegic_refraction_sph","OD Cycloplegic SPH",map,table);
		printMeasurementEntry("od_cycloplegic_refraction_cyl","OD Cycloplegic CYL",map,table);
		printMeasurementEntry("od_cycloplegic_refraction_axis","OD Cycloplegic AXIS",map,table);
		printMeasurementEntry("od_cycloplegic_refraction_add","OD Cycloplegic ADD",map,table);
		printMeasurementEntry("od_cycloplegic_refraction_distance","OD Cycloplegic VA",map,table);
		
		printMeasurementEntry("os_manifest_refraction_sph","OS Manifest SPH",map,table);
		printMeasurementEntry("os_manifest_refraction_cyl","OS Manifest CYL",map,table);
		printMeasurementEntry("os_manifest_refraction_axis","OS Manifest AXIS",map,table);
		printMeasurementEntry("os_manifest_refraction_add","OS Manifest ADD",map,table);
		printMeasurementEntry("os_manifest_refraction_distance","OS Manifest VA",map,table);
		printMeasurementEntry("os_manifest_refraction_near","OS Manifest N",map,table);		
		printMeasurementEntry("os_cycloplegic_refraction_sph","OS Cycloplegic SPH",map,table);
		printMeasurementEntry("os_cycloplegic_refraction_cyl","OS Cycloplegic CYL",map,table);
		printMeasurementEntry("os_cycloplegic_refraction_axis","OS Cycloplegic AXIS",map,table);
		printMeasurementEntry("os_cycloplegic_refraction_add","OS Cycloplegic ADD",map,table);
		printMeasurementEntry("os_cycloplegic_refraction_distance","OS Cycloplegic VA",map,table);
		
		printMeasurementEntry("od_iop_nct","OD NCT",map,table);
		printMeasurementEntry("od_iop_applanation","OD Applanation",map,table);
		printMeasurementEntry("od_cct","OD CCT",map,table);
		
		printMeasurementEntry("os_iop_nct","OS NCT",map,table);
		printMeasurementEntry("os_iop_applanation","OS Applanation",map,table);
		printMeasurementEntry("os_cct","OS CCT",map,table);
		
		printMeasurementEntry("od_color_vision","OD Color Vision",map,table);
		printMeasurementEntry("od_pupil","OD pupil",map,table);
		printMeasurementEntry("od_amsler_grid","OD Amsler Grid",map,table);
		printMeasurementEntry("od_pam","OD Pam",map,table);
		printMeasurementEntry("od_confrontation","OD Confrontation",map,table);
		printMeasurementEntry("os_color_vision","OS Color Vision",map,table);
		printMeasurementEntry("os_pupil","OS pupil",map,table);
		printMeasurementEntry("os_amsler_grid","OS Amsler Grid",map,table);
		printMeasurementEntry("os_pam","OS Pam",map,table);
		printMeasurementEntry("os_confrontation","OS Confrontation",map,table);
		
		printMeasurementEntry("EOM","EOM Stereo",map,table);
		
		printMeasurementEntry("od_cornea","OD Cornea",map,table);
		printMeasurementEntry("od_conjuctiva_sclera","OD Cojuctiva/Sclera",map,table);
		printMeasurementEntry("od_anterior_chamber","OD Anterior Chamber",map,table);
		printMeasurementEntry("od_angle_up","OD Angle Up",map,table);
		printMeasurementEntry("od_angle_middle0","OD Angle Middle 0",map,table);
		printMeasurementEntry("od_angle_middle1","OD Angle Middle 1",map,table);
		printMeasurementEntry("od_angle_middle2","OD Angle Middle 2",map,table);
		printMeasurementEntry("od_angle_down","OD Angle Down",map,table);
		printMeasurementEntry("od_iris","OD Iris",map,table);
		printMeasurementEntry("od_lens","OD Lens",map,table);		
		printMeasurementEntry("os_cornea","OS Cornea",map,table);
		printMeasurementEntry("os_conjuctiva_sclera","OS Cojuctiva/Sclera",map,table);
		printMeasurementEntry("os_anterior_chamber","OS Anterior Chamber",map,table);
		printMeasurementEntry("os_angle_up","OS Angle Up",map,table);
		printMeasurementEntry("os_angle_middle0","OS Angle Middle 0",map,table);
		printMeasurementEntry("os_angle_middle1","OS Angle Middle 1",map,table);
		printMeasurementEntry("os_angle_middle2","OS Angle Middle 2",map,table);
		printMeasurementEntry("os_angle_down","OS Angle Down",map,table);
		printMeasurementEntry("os_iris","OS Iris",map,table);
		printMeasurementEntry("os_lens","OS Lens",map,table);
		
		printMeasurementEntry("od_disc","OD disc",map,table);
		printMeasurementEntry("od_cd_ratio_horizontal","OD c/d ratio",map,table);
		printMeasurementEntry("od_macula","OD Macula",map,table);
		printMeasurementEntry("od_retina","OD Retina",map,table);
		printMeasurementEntry("od_vitreous","OD Vitreous",map,table);
		printMeasurementEntry("os_disc","OS disc",map,table);
		printMeasurementEntry("os_cd_ratio_horizontal","OS c/d ratio",map,table);
		printMeasurementEntry("os_macula","OS Macula",map,table);
		printMeasurementEntry("os_retina","OS Retina",map,table);
		printMeasurementEntry("os_vitreous","OS Vitreous",map,table);
		
		printMeasurementEntry("od_face","OD Face",map,table);
		printMeasurementEntry("od_upper_lid","OD Upper Lid",map,table);
		printMeasurementEntry("od_lower_lid","OD Lower Lid",map,table);
		printMeasurementEntry("od_punctum","OD Punctum",map,table);
		printMeasurementEntry("od_lacrimal_lake","OD Lacrimal Lake",map,table);
		printMeasurementEntry("os_face","OS Face",map,table);
		printMeasurementEntry("os_upper_lid","OS Upper Lid",map,table);
		printMeasurementEntry("os_lower_lid","OS Lower Lid",map,table);
		printMeasurementEntry("os_punctum","OS Punctum",map,table);
		printMeasurementEntry("os_lacrimal_lake","OS Lacrimal Lake",map,table);
		
		printMeasurementEntry("od_lacrimal_irrigation","OD Lacrimal Irrigation",map,table);
		printMeasurementEntry("od_nld","OD NLD",map,table);
		printMeasurementEntry("od_dye_disappearance","OD Dye Disappearance",map,table);
		printMeasurementEntry("os_lacrimal_irrigation","OS Lacrimal Irrigation",map,table);
		printMeasurementEntry("os_nld","OS NLD",map,table);
		printMeasurementEntry("os_dye_disappearance","OS Dye Disappearance",map,table);
		
		printMeasurementEntry("od_mrd","OD MRD",map,table);
		printMeasurementEntry("od_levator_function","OD Levator Function",map,table);
		printMeasurementEntry("od_inferior_scleral_show","OD Inferior Scleral Show",map,table);
		printMeasurementEntry("od_cn_vii","OD CN VII",map,table);
		printMeasurementEntry("od_blink","OD Blink",map,table);
		printMeasurementEntry("od_bells","OD Bells",map,table);
		printMeasurementEntry("od_lagophthalmos","OD Lagophthalmos",map,table);
		printMeasurementEntry("os_mrd","OS MRD",map,table);
		printMeasurementEntry("os_levator_function","OS Levator Function",map,table);
		printMeasurementEntry("os_inferior_scleral_show","OS Inferior Scleral Show",map,table);
		printMeasurementEntry("os_cn_vii","OS CN VII",map,table);
		printMeasurementEntry("os_blink","OS Blink",map,table);
		printMeasurementEntry("os_bells","OS Bells",map,table);
		printMeasurementEntry("os_lagophthalmos","OS Lagophthalmos",map,table);
		
		printMeasurementEntry("od_hertel","OD Hertel",map,table);
		printMeasurementEntry("od_retropulsion","OD Retropulsion",map,table);
		printMeasurementEntry("os_hertel","OS Hertel",map,table);
		printMeasurementEntry("os_retropulsion","OS Retropulsion",map,table);
		
	}
	
	private void printMeasurementEntry(String type,String label, Map<String,Measurement> measurementMap, PdfPTable table) {
		if(measurementMap.get(type) == null) {
			return;
		}
		
		table.addCell(label);
		table.addCell(measurementMap.get(type).getDataField());
	}
}
