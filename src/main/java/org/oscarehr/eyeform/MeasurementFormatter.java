package org.oscarehr.eyeform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

public class MeasurementFormatter {

	Map<String,Measurements> mmap=new HashMap<String,Measurements>();
	
	
	public MeasurementFormatter(List<Measurements> measurements) {
		for(Measurements m:measurements) {
			mmap.put(m.getType(), m);
		}
	}
	
	public String getVisionAssessmentAutoRefraction() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_ar_sph")) {
			sb.append("OD ");
			sb.append(getValue("od_ar_sph"));
			sb.append((isNegative("od_ar_cyl")?"":"+") + getValue("od_ar_cyl"));
			sb.append("x" + getValue("od_ar_axis"));
			sb.append(";");
		}
		if(isPresent("os_ar_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_ar_sph"));
			sb.append((isNegative("os_ar_cyl")?"":"+") + getValue("os_ar_cyl"));
			sb.append("x" + getValue("os_ar_axis"));
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
			sb.append(";");
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
			if(distNear.equals("near"))
				sb.append("+");
			sb.append(";");
		}
		if(isPresent("os_"+type+"_"+ distNear)) {
			sb.append("OS ");
			sb.append(getValue("os_"+type+"_"+distNear));
			if(distNear.equals("near"))
				sb.append("+");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getManifestDistance() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_manifest_refraction_sph")) {
			sb.append("OD ");		
			sb.append(getValue("od_manifest_refraction_sph"));
			sb.append((isNegative("od_manifest_refraction_cyl")?"":"+") + getValue("od_manifest_refraction_cyl"));
			sb.append("x" + getValue("od_manifest_refraction_axis"));
			sb.append(" (" + getValue("od_manifest_distance") + ")");
			sb.append(";");
		}
		if(isPresent("os_manifest_refraction_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_manifest_refraction_sph"));
			sb.append((isNegative("os_manifest_refraction_cyl")?"":"+") + getValue("os_manifest_refraction_cyl"));
			sb.append("x" + getValue("os_manifest_refraction_axis"));
			sb.append(" (" + getValue("os_manifest_distance") + ")");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getManifestNear() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_manifest_refraction_add")) {
			sb.append("OD ");		
			sb.append("add +");
			sb.append(getValue("od_manifest_refraction_add"));
			sb.append(" ("+getValue("od_manifest_near")+"+)");
			sb.append(";");
		}
		if(isPresent("os_manifest_refraction_add")) {
			sb.append("OS ");
			sb.append("add +");
			sb.append(getValue("os_manifest_refraction_add"));
			sb.append(" ("+getValue("os_manifest_near")+"+)");
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getCycloplegicRefraction() {
		StringBuilder sb = new StringBuilder();
		if(isPresent("od_cycloplegic_refraction_sph")) {
			sb.append("OD ");		
			sb.append(getValue("od_cycloplegic_refraction_sph"));
			sb.append((isNegative("od_cycloplegic_refraction_cyl")?"":"+") + getValue("od_cycloplegic_refraction_cyl"));
			sb.append("x" + getValue("od_cycloplegic_refraction_axis"));
			sb.append(" (" + getValue("od_cycloplegic_distance") + ")");
			sb.append(";");
		}
		if(isPresent("os_cycloplegic_refraction_sph")) {
			sb.append("OS ");
			sb.append(getValue("os_cycloplegic_refraction_sph"));
			sb.append((isNegative("os_cycloplegic_refraction_cyl")?"":"+") + getValue("os_cycloplegic_refraction_cyl"));
			sb.append("x" + getValue("os_cycloplegic_refraction_axis"));
			sb.append(" (" + getValue("os_cycloplegic_distance") + ")");
			sb.append(".");	
		}
		return sb.toString();
	}
	
	public String getNCT() {
		StringBuilder sb = new StringBuilder();		
		if(isPresent("od_iop_nct") && isPresent("os_iop_nct")) {
			sb.append("OD ");		
			sb.append(getValue("od_iop_nct"));
			
			sb.append(";");
			sb.append("OS ");
			sb.append(getValue("os_iop_nct"));
			sb.append(" ");
			Date d1 = mmap.get("od_iop_nct").getDateObserved();
			Date d2 = mmap.get("os_iop_nct").getDateObserved();
			Date d = d2;
			if(d1.after(d2))
				d=d1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
			
			sb.append(";");
			sb.append("OS ");
			sb.append(getValue("os_iop_applanation"));
			sb.append(" ");
			Date d1 = mmap.get("od_iop_applanation").getDateObserved();
			Date d2 = mmap.get("os_iop_applanation").getDateObserved();
			Date d = d2;
			if(d1.after(d2))
				d=d1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
		sb.append(getValue("EOM"));				
		sb.append(".");	
		return sb.toString();
	}	
	
	
	public String getCornea() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_cornea")) {
			sb.append("OD ");		
			sb.append(getValue("od_cornea"));		
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
		if(isPresent("od_angle_middle1")) {
			sb.append("OD ");		
			sb.append(getValue("od_angle_middle1"));
			sb.append(" ");
			sb.append("(superior "+getValue("od_angle_up")+", nasal "+getValue("od_angle_middle2")+", inferior "+getValue("od_angle_down")+", temporal " + getValue("od_angle_middle0") +")");
			sb.append(";");
		}
		if(isPresent("os_angle_middle1")) {
			sb.append("OS ");
			sb.append(getValue("os_angle_middle1"));
			sb.append(" ");
			sb.append("(superior "+getValue("od_angle_up")+", nasal "+getValue("od_angle_middle0")+", inferior "+getValue("od_angle_down")+", temporal " + getValue("od_angle_middle2") +")");		
			sb.append(".");
		}
		return sb.toString();
	}

	public String getIris() {
		StringBuilder sb = new StringBuilder();	
		if(isPresent("od_iris")) {
			sb.append("OD ");		
			sb.append(getValue("od_iris"));		
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
			sb.append(";");
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
	
	private boolean isNegative(String key) {
		if(mmap.get(key) != null && mmap.get(key).getDataField().startsWith("-"))
			return true;
		return false;
	}
}
