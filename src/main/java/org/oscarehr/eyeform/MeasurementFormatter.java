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


package org.oscarehr.eyeform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.model.Measurement;

public class MeasurementFormatter {

	Map<String,Measurement> mmap=new HashMap<String,Measurement>();
	
	
	public MeasurementFormatter(List<Measurement> measurements) {
		for(Measurement m:measurements) {
			mmap.put(m.getType(), m);
		}
	}
		
	public String getVisionAssessment(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Auto-refraction") != null && this.getVisionAssessmentAutoRefraction().length()>0) {
			sb.append("Auto-refraction ");
			sb.append(this.getVisionAssessmentAutoRefraction());
		}
		if(includeMap.get("Keratometry") != null && this.getVisionAssessmentKeratometry().length()>0) {
			sb.append("Keratometry ");
			sb.append(this.getVisionAssessmentKeratometry());
		}
		if(includeMap.get("Distance vision (sc)") != null && this.getVisionAssessmentVision("distance", "sc").length()>0) {
			sb.append("Distance vision (sc) ");
			sb.append(this.getVisionAssessmentVision("distance", "sc"));
		}
		if(includeMap.get("Distance vision (cc)") != null && this.getVisionAssessmentVision("distance", "cc").length()>0) {
			sb.append("Distance vision (cc) ");
			sb.append(this.getVisionAssessmentVision("distance", "cc"));
		}
		if(includeMap.get("Distance vision (ph)") != null && this.getVisionAssessmentVision("distance", "ph").length()>0) {
			sb.append("Distance vision (ph)");
			sb.append(this.getVisionAssessmentVision("distance", "ph"));
		}
		if(includeMap.get("Near vision (sc)") != null && this.getVisionAssessmentVision("near", "sc").length()>0) {
			sb.append("Near vision (sc) ");
			sb.append(this.getVisionAssessmentVision("near", "sc"));
		}
		if(includeMap.get("Near vision (cc)") != null && this.getVisionAssessmentVision("near", "cc").length()>0) {
			sb.append("Near vision (cc) ");
			sb.append(this.getVisionAssessmentVision("near", "cc"));
		}
		if(sb.length()>0) {
			sb.insert(0, "VISION ASSESSMENT:");
		}
		return sb.toString();
	}
	
	public String getManifestVision(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Manifest distance") != null && getManifestDistance().length()>0) {
			sb.append("Manifest distance ");
			sb.append(getManifestDistance());
		}
		if(includeMap.get("Manifest near") != null && getManifestNear().length()>0) {
			sb.append("Manifest near ");
			sb.append(getManifestNear());
		}
		if(includeMap.get("Cycloplegic refraction") != null && this.getCycloplegicRefraction().length()>0) {
			sb.append("Cycloplegic refraction ");
			sb.append(this.getCycloplegicRefraction());
		}
		if(sb.length()>0) {
			sb.insert(0,"MANIFEST VISION:");
		}
		return sb.toString();
	}
	
	public String getIntraocularPressure(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		
		if(includeMap.get("NCT") != null && this.getNCT().length()>0) {
			sb.append("NCT ");
			sb.append(this.getNCT());
		}
		if(includeMap.get("Applanation") != null && this.getApplanation().length()>0) {
			sb.append("Applanation ");
			sb.append(this.getApplanation());
		}
		if(includeMap.get("Central corneal thickness") != null && this.getCCT().length()>0) {
			sb.append("Central corneal thickness ");
			sb.append(this.getCCT());
		}
		if(sb.length()>0) {
			sb.insert(0,"INTRAOCULAR PRESSURE:");
		}
		return sb.toString();
	}
	
	public String getOtherExam(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		
		if(includeMap.get("Colour vision") != null && this.getColourVision().length()>0) {
			sb.append("Colour vision ");
			sb.append(this.getColourVision());
		}
		if(includeMap.get("Pupil") != null && this.getPupil().length()>0) {
			sb.append("Pupil ");
			sb.append(this.getPupil());
		}
		if(includeMap.get("Amsler grid") != null && this.getAmslerGrid().length()>0) {
			sb.append("Amsler grid ");
			sb.append(this.getAmslerGrid());
		}
		if(includeMap.get("Potential acuity meter") != null && this.getPAM().length()>0) {
			sb.append("Potential acuity meter ");
			sb.append(this.getPAM());
		}
		if(includeMap.get("Confrontation fields") != null && this.getConfrontation().length()>0) {
			sb.append("Confrontation fields ");
			sb.append(this.getConfrontation());
		}
		
		if(sb.length()>0) {
			sb.insert(0,"OTHER EXAM:");
		}
		return sb.toString();
	}

	public String getEOMStereo(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("EOM") != null && this.getEomStereo().length()>0) {
			sb.append("EOM/Stereo ");
			sb.append(this.getEomStereo());
		}
				
		return sb.toString();
	}
	
	public String getAnteriorSegment(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Cornea") != null && this.getCornea().length()>0) {
			sb.append("Cornea ");
			sb.append(this.getCornea());
		}		
		if(includeMap.get("Conjunctiva/Sclera") != null && this.getConjuctivaSclera().length()>0) {
			sb.append("Conjunctiva/Sclera ");
			sb.append(this.getConjuctivaSclera());
		}
		if(includeMap.get("Anterior chamber") != null && this.getAnteriorChamber().length()>0) {
			sb.append("Anterior chamber ");
			sb.append(this.getAnteriorChamber());
		}
		if(includeMap.get("Angle") != null && this.getAngle().length()>0) {
			sb.append("Angle ");
			sb.append(this.getAngle());
		}
		if(includeMap.get("Iris") != null && this.getIris().length()>0) {
			sb.append("Iris ");
			sb.append(this.getIris());
		}
		if(includeMap.get("Lens") != null && this.getLens().length()>0) {
			sb.append("Lens ");
			sb.append(this.getLens());
		}
		
		if(sb.length()>0) {
			sb.insert(0,"ANTERIOR SEGMENT:");
		}
		return sb.toString();
	}
	
	public String getPosteriorSegment(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Optic disc") != null && this.getDisc().length()>0) {
			sb.append("Optic disc ");
			sb.append(this.getDisc());
		}
		if(includeMap.get("C/D ratio") != null && this.getCdRatio().length()>0) {
			sb.append("C/D ratio ");
			sb.append(this.getCdRatio());
		}	
		if(includeMap.get("Macula") != null && this.getMacula().length()>0) {
			sb.append("Macula ");
			sb.append(this.getMacula());
		}	
		if(includeMap.get("Retina") != null && this.getRetina().length()>0) {
			sb.append("Retina ");
			sb.append(this.getRetina());
		}	
		if(includeMap.get("Vitreous") != null && this.getVitreous().length()>0) {
			sb.append("Vitreous ");
			sb.append(this.getVitreous());
		}	
		if(sb.length()>0) {
			sb.insert(0,"POSTERIOR SEGMENT:");
		}
		return sb.toString();
	}
	
	public String getExternalOrbit(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Face") != null && this.getFace().length()>0) {
			sb.append("Face ");
			sb.append(this.getFace());
		}
		if(includeMap.get("Upper lid") != null && this.getUpperLid().length()>0) {
			sb.append("Upper lid ");
			sb.append(this.getUpperLid());
		}
		if(includeMap.get("Lower lid") != null && this.getLowerLid().length()>0) {
			sb.append("Lower lid ");
			sb.append(this.getLowerLid());
		}
		if(includeMap.get("Punctum") != null && this.getPunctum().length()>0) {
			sb.append("Punctum ");
			sb.append(this.getPunctum());
		}
		if(includeMap.get("Lacrimal lake") != null && this.getLacrimalLake().length()>0) {
			sb.append("Lacrimal lake ");
			sb.append(this.getLacrimalLake());
		}
		if(includeMap.get("Retropulsion") != null && this.getRetropulsion().length()>0) {
			sb.append("Retropulsion ");
			sb.append(this.getRetropulsion());
		}
		if(includeMap.get("Hertel") != null && this.getHertel().length()>0) {
			sb.append("Hertel ");
			sb.append(this.getHertel());
		}
		
		if(sb.length()>0) {
			sb.insert(0,"EXTERNAL/ORBIT:");
		}
		return sb.toString();
	}
	
	public String getNasalacrimalDuct(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Lacrimal irrigation") != null && this.getLacrimalIrrigation().length()>0) {
			sb.append("Lacrimal irrigation ");
			sb.append(this.getLacrimalIrrigation());
		}
		if(includeMap.get("Nasolacrimal duct") != null && this.getNLD().length()>0) {
			sb.append("Nasolacrimal duct ");
			sb.append(this.getNLD());
		}
		if(includeMap.get("Dye disappearance") != null && this.getDyeDisappearance().length()>0) {
			sb.append("Dye disappearance ");
			sb.append(this.getDyeDisappearance());
		}
		
		if(sb.length()>0) {
			sb.insert(0,"NASOLACRIMAL DUCT:");
		}
		return sb.toString();
	}
	
	public String getEyelidMeasurement(Map<String,Boolean> includeMap) {
		StringBuilder sb = new StringBuilder();
		if(includeMap.get("Margin reflex distance") != null && this.getMarginReflexDistance().length()>0) {
			sb.append("Margin reflex distance ");
			sb.append(this.getMarginReflexDistance());
		}
		if(includeMap.get("Levator function") != null && this.getLevatorFunction().length()>0) {
			sb.append("Levator function ");
			sb.append(this.getLevatorFunction());
		}
		if(includeMap.get("Inferior scleral show") != null && this.getInferiorScleralShow().length()>0) {
			sb.append("Inferior scleral show ");
			sb.append(this.getInferiorScleralShow());
		}
		if(includeMap.get("Lagophthalmos") != null && this.getLagophthalmos().length()>0) {
			sb.append("Lagophthalmos ");
			sb.append(this.getLagophthalmos());
		}
		if(includeMap.get("Blink reflex") != null && this.getBlink().length()>0) {
			sb.append("Blink ");
			sb.append(this.getBlink());
		}
		if(includeMap.get("Cranial nerve VII function") != null && this.getCNVii().length()>0) {
			sb.append("Cranial nerve VII function ");
			sb.append(this.getCNVii());
		}
		if(includeMap.get("Bells phenomenon") != null && this.getBells().length()>0) {
			sb.append("Bell's phenomenon ");
			sb.append(this.getBells());
		}
		if(sb.length()>0) {
			sb.insert(0,"EYELID MEASUREMENT:");
		}
		return sb.toString();
	}
	
	public String getVisionAssessmentAutoRefraction() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_ar_sph")) {
			sb.append("OD ");
			sb.append(getValue("od_ar_sph"));
			sb.append(getValue("od_ar_cyl"));
			if(isPresent("od_ar_axis")) {
				sb.append("x" + getValue("od_ar_axis"));
			}
			sb.append("; ");
		}
		if(isPresent("os_ar_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_ar_sph"));
			sb.append(getValue("os_ar_cyl"));
			if(isPresent("os_ar_axis")) {
				sb.append("x" + getValue("os_ar_axis"));
			}
			sb.append(".");
		}
		return sb.toString();
	
	}
	
	public String getVisionAssessmentKeratometry() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_k1")) {
			sb.append("OD ");
			sb.append(getValue("od_k1"));
			sb.append("x" + getValue("od_k2"));
			sb.append("@" + getValue("od_k2_axis"));
			sb.append("; ");
		}
		if(isPresent("os_k1")) {
			sb.append("OS ");
			sb.append(getValue("os_k1"));
			sb.append("x" + getValue("os_k2"));
			sb.append("@" + getValue("os_k2_axis"));
			sb.append(".");
		}
		return sb.toString();
	
	}
	
	public String getVisionAssessmentVision(String distNear, String type) {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_"+type+"_"+ distNear))  {
			sb.append("OD ");
			sb.append(getValue("od_"+type+"_"+ distNear));
			//if(distNear.equals("near"))
			//	sb.append("+");
			sb.append("; ");
		}
		if(isPresent("os_"+type+"_"+ distNear)) {
			sb.append("OS ");
			sb.append(getValue("os_"+type+"_"+distNear));
			//if(distNear.equals("near"))
			//	sb.append("+");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getManifestDistance() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_manifest_refraction_sph")) {
			sb.append("OD ");		
			sb.append(getValue("od_manifest_refraction_sph"));
			sb.append(getValue("od_manifest_refraction_cyl"));
			if(isPresent("od_manifest_refraction_axis")) {
				sb.append("x" + getValue("od_manifest_refraction_axis"));
			}
			sb.append(" (" + getValue("od_manifest_distance") + ")");
			sb.append("; ");
		}
		if(isPresent("os_manifest_refraction_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_manifest_refraction_sph"));
			sb.append(getValue("os_manifest_refraction_cyl"));
			if(isPresent("os_manifest_refraction_axis")) {
				sb.append("x" + getValue("os_manifest_refraction_axis"));
			}
			sb.append(" (" + getValue("os_manifest_distance") + ")");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getManifestNear() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_manifest_refraction_add")) {
			sb.append("OD ");		
			sb.append("add ");
			sb.append(getValue("od_manifest_refraction_add"));
			sb.append(" ("+getValue("od_manifest_near")+")");
			sb.append("; ");
		}
		if(isPresent("os_manifest_refraction_add")) {
			sb.append("OS ");
			sb.append("add ");
			sb.append(getValue("os_manifest_refraction_add"));
			sb.append(" ("+getValue("os_manifest_near")+")");
			sb.append(".");
		}
		return sb.toString();
	}
	
	//TODO: No ADD HERE?
	public String getCycloplegicRefraction() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_cycloplegic_refraction_sph")) {
			sb.append("OD ");		
			sb.append(getValue("od_cycloplegic_refraction_sph"));
			sb.append(getValue("od_cycloplegic_refraction_cyl"));
			if(isPresent("od_cycloplegic_refraction_axis")) {
				sb.append("x" + getValue("od_cycloplegic_refraction_axis"));
			}
			if(isPresent("od_cycloplegic_distance")) {
				sb.append(" (" + getValue("od_cycloplegic_distance") + ")");
			}
			sb.append("; ");
		}
		if(isPresent("os_cycloplegic_refraction_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_cycloplegic_refraction_sph"));
			sb.append(getValue("os_cycloplegic_refraction_cyl"));
			if(isPresent("os_cycloplegic_refraction_axis")) {
				sb.append("x" + getValue("os_cycloplegic_refraction_axis"));
			}
			if(isPresent("os_cycloplegic_distance")) {
				sb.append(" (" + getValue("os_cycloplegic_distance") + ")");
			}
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getNCT() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_iop_nct") && isPresent("os_iop_nct")) {
			sb.append("OD ");		
			sb.append(getValue("od_iop_nct"));
			
			sb.append("; ");
			sb.append("OS ");
			sb.append(getValue("os_iop_nct"));
			sb.append(" ");
			Date d1 = mmap.get("od_iop_nct").getDateObserved();
			Date d2 = mmap.get("os_iop_nct").getDateObserved();
			Date d = d2;
			if(d1.after(d2))
				d=d1;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			sb.append("[" + sdf.format(d)  + "]");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getApplanation() {		
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_iop_applanation") && isPresent("os_iop_applanation")) {
			sb.append("OD ");		
			sb.append(getValue("od_iop_applanation"));
			
			sb.append("; ");
			sb.append("OS ");
			sb.append(getValue("os_iop_applanation"));
			sb.append(" ");
			Date d1 = mmap.get("od_iop_applanation").getDateObserved();
			Date d2 = mmap.get("os_iop_applanation").getDateObserved();
			Date d = d2;
			if(d1.after(d2))
				d=d1;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			sb.append("[" + sdf.format(d)  + "]");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getCCT() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_cct")) {
			sb.append("OD ");		
			sb.append(getValue("od_cct"));
			sb.append(" microns");
			sb.append("; ");
		}
		if(isPresent("os_cct")) {
			sb.append("OS ");
			sb.append(getValue("os_cct"));
			sb.append(" microns");
			sb.append(".");
		}
		return sb.toString();
	}	
	
	public String getColourVision() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_color_vision")) {
			sb.append("OD ");		
			sb.append(getValue("od_color_vision"));		
			sb.append("; ");
		}
		if(isPresent("os_color_vision")) {
			sb.append("OS ");
			sb.append(getValue("os_color_vision"));	
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getPupil() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_pupil")) {
			sb.append("OD ");		
			sb.append(getValue("od_pupil"));		
			sb.append("; ");
		}
		if(isPresent("os_pupil")) {
			sb.append("OS ");
			sb.append(getValue("os_pupil"));	
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getAmslerGrid() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_amsler_grid")) {
			sb.append("OD ");		
			sb.append(getValue("od_amsler_grid"));		
			sb.append("; ");
		}
		if(isPresent("os_amsler_grid")) {
			sb.append("OS ");
			sb.append(getValue("os_amsler_grid"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getPAM() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_pam")) {
			sb.append("OD ");		
			sb.append(getValue("od_pam"));		
			sb.append("; ");
		}
		if(isPresent("os_pam")) {
			sb.append("OS ");
			sb.append(getValue("os_pam"));	
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getConfrontation() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_confrontation")) {
			sb.append("OD ");		
			sb.append(getValue("od_confrontation"));		
			sb.append("; ");
		}
		if(isPresent("os_confrontation")) {
			sb.append("OS ");
			sb.append(getValue("os_confrontation"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getEomStereo() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("EOM")) {
			sb.append(getValue("EOM"));				
			sb.append(".");
		}
		return sb.toString();
	}	
	
	
	public String getCornea() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_cornea")) {
			sb.append("OD ");		
			sb.append(getValue("od_cornea"));		
			sb.append("; ");
		}
		if(isPresent("os_cornea")) {
			sb.append("OS ");
			sb.append(getValue("os_cornea"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getConjuctivaSclera() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_conjuctiva_sclera")) {
			sb.append("OD ");		
			sb.append(getValue("od_conjuctiva_sclera"));		
			sb.append("; ");
		}
		if(isPresent("os_conjuctiva_sclera")) {
			sb.append("OS ");
			sb.append(getValue("os_conjuctiva_sclera"));	
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getAnteriorChamber() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_anterior_chamber")) {
			sb.append("OD ");		
			sb.append(getValue("od_anterior_chamber"));		
			sb.append("; ");
		}
		if(isPresent("os_anterior_chamber")) {
			sb.append("OS ");
			sb.append(getValue("os_anterior_chamber"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getAngle() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_angle_middle1") || isPresent("od_angle_up") || isPresent("od_angle_middle2") || isPresent("od_angle_down") || isPresent("od_angle_middle0")) {			
			sb.append("OD ");		
			sb.append(getValue("od_angle_middle1"));
			if(isPresent("od_angle_up") || isPresent("od_angle_middle2") || isPresent("od_angle_down") || isPresent("od_angle_middle0")) {
				sb.append(" (");
			}
			boolean flag=false;
			if(isPresent("od_angle_up")) {
				sb.append("superior "+getValue("od_angle_up"));
				if(!flag) flag=true;
			}
			if(isPresent("od_angle_middle2")) {
				if(flag) {					
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("nasal "+getValue("od_angle_middle2"));
			}
			if(isPresent("od_angle_down")) {
				if(flag) {
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("inferior "+getValue("od_angle_down"));
			}
			if(isPresent("od_angle_middle0")) {
				if(flag) {
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("temporal " + getValue("od_angle_middle0"));
			}
			
			if(isPresent("od_angle_up") || isPresent("od_angle_middle2") || isPresent("od_angle_down") || isPresent("od_angle_middle0")) {
				sb.append(")");
			}
			sb.append("; ");
		}
		
		
		
		
		
		
		
		if(isPresent("os_angle_middle1") || isPresent("os_angle_up") || isPresent("os_angle_middle2") || isPresent("os_angle_down") || isPresent("os_angle_middle0")) {			
			sb.append("OS ");		
			sb.append(getValue("os_angle_middle1"));
			
			if(isPresent("os_angle_up") || isPresent("os_angle_middle2") || isPresent("os_angle_down") || isPresent("os_angle_middle0")) {
				sb.append(" (");
			}
			boolean flag=false;
			if(isPresent("os_angle_up")) {
				sb.append("superior "+getValue("os_angle_up"));
				if(!flag) flag=true;
			}
			if(isPresent("os_angle_middle0")) {
				if(flag) {					
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("nasal "+getValue("os_angle_middle0"));
			}
			if(isPresent("os_angle_down")) {
				if(flag) {
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("inferior "+getValue("os_angle_down"));
			}
			if(isPresent("os_angle_middle2")) {
				if(flag) {
					sb.append(", ");
				}
				if(!flag) flag=true;
				sb.append("temporal " + getValue("os_angle_middle2"));
			}
			
			if(isPresent("os_angle_up") || isPresent("os_angle_middle2") || isPresent("os_angle_down") || isPresent("os_angle_middle0")) {
				sb.append(")");
			}
			sb.append("; ");
		}	
				
		return sb.toString();
	}

	public String getIris() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_iris")) {
			sb.append("OD ");		
			sb.append(getValue("od_iris"));		
			sb.append("; ");
		}
		if(isPresent("os_iris")) {
			sb.append("OS ");
			sb.append(getValue("os_iris"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLens() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_lens")) {
			sb.append("OD ");		
			sb.append(getValue("od_lens"));		
			sb.append("; ");
		}
		if(isPresent("os_lens")) {
			sb.append("OS ");
			sb.append(getValue("os_lens"));	
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getDisc() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_disc")) {
			sb.append("OD ");		
			sb.append(getValue("od_disc"));		
			sb.append("; ");
		}
		if(isPresent("os_disc")) {
			sb.append("OS ");
			sb.append(getValue("os_disc"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getCdRatio() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_cd_ratio_horizontal")) {
			sb.append("OD ");		
			sb.append(getValue("od_cd_ratio_horizontal"));		
			sb.append("; ");
		}
		if(isPresent("os_cd_ratio_horizontal")) {
			sb.append("OS ");
			sb.append(getValue("os_cd_ratio_horizontal"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getMacula() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_macula")) {
			sb.append("OD ");		
			sb.append(getValue("od_macula"));		
			sb.append("; ");
		}
		if(isPresent("os_macula")) {
			sb.append("OS ");
			sb.append(getValue("os_macula"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getRetina() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_retina")) {
			sb.append("OD ");		
			sb.append(getValue("od_retina"));		
			sb.append("; ");
		}
		if(isPresent("os_retina")) {
			sb.append("OS ");
			sb.append(getValue("os_retina"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getVitreous() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_vitreous")) {
			sb.append("OD ");		
			sb.append(getValue("od_vitreous"));		
			sb.append("; ");
		}
		if(isPresent("os_vitreous")) {
			sb.append("OS ");
			sb.append(getValue("os_vitreous"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getFace() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_face")) {
			sb.append("OD ");		
			sb.append(getValue("od_face"));		
			sb.append("; ");
		}
		if(isPresent("os_face")) {
			sb.append("OS ");
			sb.append(getValue("os_face"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getUpperLid() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_upper_lid")) {
			sb.append("OD ");		
			sb.append(getValue("od_upper_lid"));		
			sb.append("; ");
		}
		if(isPresent("os_upper_lid")) {
			sb.append("OS ");
			sb.append(getValue("os_upper_lid"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLowerLid() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_lower_lid")) {
			sb.append("OD ");		
			sb.append(getValue("od_lower_lid"));		
			sb.append("; ");
		}
		if(isPresent("os_lower_lid")) {
			sb.append("OS ");
			sb.append(getValue("os_lower_lid"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getPunctum() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_punctum")) {
			sb.append("OD ");		
			sb.append(getValue("od_punctum"));		
			sb.append("; ");
		}
		if(isPresent("os_punctum")) {
			sb.append("OS ");
			sb.append(getValue("os_punctum"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLacrimalLake() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_lacrimal_lake")) {
			sb.append("OD ");		
			sb.append(getValue("od_lacrimal_lake"));		
			sb.append("; ");
		}
		if(isPresent("os_lacrimal_lake")) {
			sb.append("OS ");
			sb.append(getValue("os_lacrimal_lake"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLacrimalIrrigation() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_lacrimal_irrigation")) {
			sb.append("OD ");		
			sb.append(getValue("od_lacrimal_irrigation"));		
			sb.append("; ");
		}
		if(isPresent("os_lacrimal_irrigation")) {
			sb.append("OS ");
			sb.append(getValue("os_lacrimal_irrigation"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getNLD() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_nld")) {
			sb.append("OD ");		
			sb.append(getValue("od_nld"));		
			sb.append("; ");
		}
		if(isPresent("os_nld")) {
			sb.append("OS ");
			sb.append(getValue("os_nld"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getDyeDisappearance() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_dye_disappearance")) {
			sb.append("OD ");		
			sb.append(getValue("od_dye_disappearance"));		
			sb.append("; ");
		}
		if(isPresent("os_dye_disappearance")) {
			sb.append("OS ");
			sb.append(getValue("os_dye_disappearance"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getMarginReflexDistance() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_mrd")) {
			sb.append("OD ");		
			sb.append(getValue("od_mrd"));		
			sb.append("; ");
		}
		if(isPresent("os_mrd")) {
			sb.append("OS ");
			sb.append(getValue("os_mrd"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLevatorFunction() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_levator_function")) {
			sb.append("OD ");		
			sb.append(getValue("od_levator_function"));		
			sb.append("; ");
		}
		if(isPresent("os_levator_function")) {
			sb.append("OS ");
			sb.append(getValue("os_levator_function"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getInferiorScleralShow() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_inferior_scleral_show")) {
			sb.append("OD ");		
			sb.append(getValue("od_inferior_scleral_show"));		
			sb.append("; ");
		}
		if(isPresent("os_inferior_scleral_show")) {
			sb.append("OS ");
			sb.append(getValue("os_inferior_scleral_show"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getCNVii() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_cn_vii")) {
			sb.append("OD ");		
			sb.append(getValue("od_cn_vii"));		
			sb.append("; ");
		}
		if(isPresent("os_cn_vii")) {
			sb.append("OS ");
			sb.append(getValue("os_cn_vii"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getBlink() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_blink")) {
			sb.append("OD ");		
			sb.append(getValue("od_blink"));		
			sb.append("; ");
		}
		if(isPresent("os_blink")) {
			sb.append("OS ");
			sb.append(getValue("os_blink"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getBells() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_bells")) {
			sb.append("OD ");		
			sb.append(getValue("od_bells"));		
			sb.append("; ");
		}
		if(isPresent("os_bells")) {
			sb.append("OS ");
			sb.append(getValue("os_bells"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getLagophthalmos() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_lagophthalmos")) {
			sb.append("OD ");		
			sb.append(getValue("od_lagophthalmos"));		
			sb.append("; ");
		}
		if(isPresent("os_lagophthalmos")) {
			sb.append("OS ");
			sb.append(getValue("os_lagophthalmos"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getHertel() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_hertel")) {
			sb.append("OD ");		
			sb.append(getValue("od_hertel"));		
			sb.append("; ");
		}
		if(isPresent("os_hertel")) {
			sb.append("OS ");
			sb.append(getValue("os_hertel"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getRetropulsion() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_retropulsion")) {
			sb.append("OD ");		
			sb.append(getValue("od_retropulsion"));		
			sb.append("; ");
		}
		if(isPresent("os_retropulsion")) {
			sb.append("OS ");
			sb.append(getValue("os_retropulsion"));	
			sb.append(".");	
		}
		return sb.toString();
	}
	
	
	
	
	private String getValue(String key) {
		if(mmap.get(key)!=null)
			return mmap.get(key).getDataField();
		return  "";
	}
	
	private boolean isPresent(String key) {
		if(mmap.get(key)!=null)
			return true;
		return false;
	}
}
