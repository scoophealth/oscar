<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ page import="oscar.OscarProperties"%>

<%@ include file="/casemgmt/taglibs.jsp" %>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@page import="org.oscarehr.eyeform.model.EyeformSpecsHistory"%>
<%@page import="oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*,java.util.List,org.oscarehr.eyeform.model.EyeformSpecsHistory"%>
<%@ page import="org.oscarehr.util.*"%>
<%@ page import="org.oscarehr.eyeform.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.eyeform.dao.EyeformSpecsHistoryDao" %>


<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<link rel="stylesheet" href="<c:out value="${ctx}"/>/oscarEncounter/encounterStyles.css" type="text/css">

<%
	oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
	boolean glasses_hx = props1.getBooleanProperty("eyeform_glasses_hx_disable", "true");
	boolean vision_assessment = props1.getBooleanProperty("eyeform_vision_assessment_disable", "true");
	boolean vision_measurement = props1.getBooleanProperty("eyeform_vision_measurement_disable", "true");
	boolean refractive = props1.getBooleanProperty("eyeform_refractive_disable", "true");
	boolean iop = props1.getBooleanProperty("eyeform_iop_disable", "true");
	boolean other_exam = props1.getBooleanProperty("eyeform_other_exam_disable", "true");
	boolean duction = props1.getBooleanProperty("eyeform_duction_disable", "true");
	boolean deviation_measurement = props1.getBooleanProperty("eyeform_deviation_measurement_disable", "true");
	boolean external_orbit = props1.getBooleanProperty("eyeform_external_orbit_disable", "true");
	boolean eyelid_nld = props1.getBooleanProperty("eyeform_eyelid_nld_disable", "true");
	boolean eyelid_measurement = props1.getBooleanProperty("eyeform_eyelid_measurement_disable", "true");
	boolean posterior_segment = props1.getBooleanProperty("eyeform_posterior_segment_disable", "true");
	boolean anterior_segment = props1.getBooleanProperty("eyeform_anterior_segment_disable", "true");
	boolean eyeform_v3 = props1.getBooleanProperty("eyeform3", "true");
	String eyeform = props1.getProperty("cme_js");    
	
	
	String demo = request.getParameter("demographic_no");
	String appo = request.getParameter("appointment_no");
	String type11="distance";
	String type22="bifocal";
	String type33="invis bfocal";
	String type44="reading";
    EyeformSpecsHistoryDao dao = (EyeformSpecsHistoryDao) SpringUtils.getBean("eyeformSpecsHistoryDao");
    List<EyeformSpecsHistory> specs = dao.getRecentRecord(Integer.parseInt(demo),type11);
	List<EyeformSpecsHistory> specs1 = dao.getRecentRecord(Integer.parseInt(demo),type22);
	List<EyeformSpecsHistory> specs2 = dao.getRecentRecord(Integer.parseInt(demo),type33);
	List<EyeformSpecsHistory> specs3 = dao.getRecentRecord(Integer.parseInt(demo),type44);
	String value1 = "";
	String value2 = "";
	String value3 = "";
	String value4 = "";
	String value5 = "";
	String value6 = "";
	String value7 = "";
	String value8 = "";
	String value9 = "";
	String value10 = "";
	String value11 = "";
	String value12 = "";
	String value13 = "";
	String value14 = "";
	String value15 = "";
	String value16 = "";
	String value17 = "";
	String value18 = "";
	String value19 = "";
	String value20 = "";
	String value21 = "";
	String value22 = "";
	String value23 = "";
	String value24 = "";
	String value25 = "";
	String value26 = "";
	String value27 = "";
	String value28 = "";
	String value29 = "";
	String value30 = "";
	String value31 = "";
	String value32 = "";
	String value33 = "";
	String value34 = "";
	String value35 = "";
	String value36 = "";
	String value37 = "";
	String value38 = "";
	String value39 = "";
	String value40 = "";
	String value41 = "";
	String value42 = "";
	String value43 = "";
	String value44 = "";
	String glass_show = "style=\"display:none\"";
	int num = 1;
	int num1 = 0;
	int num_tab = 33;
	int[] bg_status = new int[44];
	int count = 0;
	int count1 = 0;
	int appo_now = Integer.parseInt(appo);
	boolean iscomein = false;
	
	if(specs.size() > 0){
		value1 = specs.get(0).getDateStr();
		if(value1 == null){
			for(int i = 1;i < specs.size();i ++){
				value1 = specs.get(i).getDateStr();
				if(value1 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value1 == null){
				value1 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value2 = specs.get(0).getOdSph();
		if(value2 == null){
			for(int i = 1;i < specs.size();i ++){
				value2 = specs.get(i).getOdSph();
				if(value2 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value2 == null){
				value2 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value3 = specs.get(0).getOdCyl();
		if(value3 == null){
			for(int i = 1;i < specs.size();i ++){
				value3 = specs.get(i).getOdCyl();
				if(value3 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value3 == null){
				value3 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value4 = specs.get(0).getOdAxis();
		if(value4 == null){
			for(int i = 1;i < specs.size();i ++){
				value4 = specs.get(i).getOdAxis();
				if(value4 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value4 == null){
				value4 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value5 = specs.get(0).getOdAdd();
		if(value5 == null){
			for(int i = 1;i < specs.size();i ++){
				value5 = specs.get(i).getOdAdd();
				if(value5 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value5 == null){
				value5 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value6 = specs.get(0).getOdPrism();
		if(value6 == null){
			for(int i = 1;i < specs.size();i ++){
				value6 = specs.get(i).getOdPrism();
				if(value6 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value6 == null){
				value6 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value7 = specs.get(0).getOsSph();
		if(value7 == null){
			for(int i = 1;i < specs.size();i ++){
				value7 = specs.get(i).getOsSph();
				if(value7 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value7 == null){
				value7 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value8 = specs.get(0).getOsCyl();
		if(value8 == null){
			for(int i = 1;i < specs.size();i ++){
				value8 = specs.get(i).getOsCyl();
				if(value8 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value8 == null){
				value8 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value9 = specs.get(0).getOsAxis();
		if(value9 == null){
			for(int i = 1;i < specs.size();i ++){
				value9 = specs.get(i).getOsAxis();
				if(value9 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value9 == null){
				value9 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value10 = specs.get(0).getOsAdd();
		if(value10 == null){
			for(int i = 1;i < specs.size();i ++){
				value10 = specs.get(i).getOsAdd();
				if(value10 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value10 == null){
				value10 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value11 = specs.get(0).getOsPrism();
		if(value11 == null){
			for(int i = 1;i < specs.size();i ++){
				value11 = specs.get(i).getOsPrism();
				if(value11 != null){
					if(appo_now == specs.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value11 == null){
				value11 = "";
				count ++;
			}
		}else{
			if(appo_now == specs.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}

		glass_show = "";
		//num ++;
		iscomein = true;
	}
	if(specs1.size() > 0){
		value12 = specs1.get(0).getDateStr();
		if(value12 == null){
			for(int i = 1;i < specs1.size();i ++){
				value12 = specs1.get(i).getDateStr();
				if(value12 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value12 == null){
				value12 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value13 = specs1.get(0).getOdSph();
		if(value13 == null){
			for(int i = 1;i < specs1.size();i ++){
				value13 = specs1.get(i).getOdSph();
				if(value13 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value13 == null){
				value13 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value14 = specs1.get(0).getOdCyl();
		if(value14 == null){
			for(int i = 1;i < specs1.size();i ++){
				value14 = specs1.get(i).getOdCyl();
				if(value14 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value14 == null){
				value14 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value15 = specs1.get(0).getOdAxis();
		if(value15 == null){
			for(int i = 1;i < specs1.size();i ++){
				value15 = specs1.get(i).getOdAxis();
				if(value15 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value15 == null){
				value15 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value16 = specs1.get(0).getOdAdd();
		if(value16 == null){
			for(int i = 1;i < specs1.size();i ++){
				value16 = specs1.get(i).getOdAdd();
				if(value16 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value16 == null){
				value16 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value17 = specs1.get(0).getOdPrism();
		if(value17 == null){
			for(int i = 1;i < specs1.size();i ++){
				value17 = specs1.get(i).getOdPrism();
				if(value17 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value17 == null){
				value17 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value18 = specs1.get(0).getOsSph();
		if(value18 == null){
			for(int i = 1;i < specs1.size();i ++){
				value18 = specs1.get(i).getOsSph();
				if(value18 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value18 == null){
				value18 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value19 = specs1.get(0).getOsCyl();
		if(value19 == null){
			for(int i = 1;i < specs1.size();i ++){
				value19 = specs1.get(i).getOsCyl();
				if(value19 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value19 == null){
				value19 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value20 = specs1.get(0).getOsAxis();
		if(value20 == null){
			for(int i = 1;i < specs1.size();i ++){
				value20 = specs1.get(i).getOsAxis();
				if(value20 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value20 == null){
				value20 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value21 = specs1.get(0).getOsAdd();
		if(value21 == null){
			for(int i = 1;i < specs1.size();i ++){
				value21 = specs1.get(i).getOsAdd();
				if(value21 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value21 == null){
				value21 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value22 = specs1.get(0).getOsPrism();
		if(value22 == null){
			for(int i = 1;i < specs1.size();i ++){
				value22 = specs1.get(i).getOsPrism();
				if(value22 != null){
					if(appo_now == specs1.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value22 == null){
				value22 = "";
				count ++;
			}
		}else{
			if(appo_now == specs1.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		
		glass_show = "";
		if(iscomein){
			num ++;
		}else{
			iscomein = true;
		}
	}
	if(specs2.size() > 0){
		value23 = specs2.get(0).getDateStr();
		if(value23 == null){
			for(int i = 1;i < specs2.size();i ++){
				value23 = specs2.get(i).getDateStr();
				if(value23 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value23 == null){
				value23 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value24 = specs2.get(0).getOdSph();
		if(value24 == null){
			for(int i = 1;i < specs2.size();i ++){
				value24 = specs2.get(i).getOdSph();
				if(value24 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value24 == null){
				value24 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value25 = specs2.get(0).getOdCyl();
		if(value25 == null){
			for(int i = 1;i < specs2.size();i ++){
				value25 = specs2.get(i).getOdCyl();
				if(value25 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value25 == null){
				value25 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value26 = specs2.get(0).getOdAxis();
		if(value26 == null){
			for(int i = 1;i < specs2.size();i ++){
				value26 = specs2.get(i).getOdAxis();
				if(value26 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value26 == null){
				value26 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value27 = specs2.get(0).getOdAdd();
		if(value27 == null){
			for(int i = 1;i < specs2.size();i ++){
				value27 = specs2.get(i).getOdAdd();
				if(value27 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value27 == null){
				value27 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value28 = specs2.get(0).getOdPrism();
		if(value28 == null){
			for(int i = 1;i < specs2.size();i ++){
				value28 = specs2.get(i).getOdPrism();
				if(value28 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value28 == null){
				value28 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value29 = specs2.get(0).getOsSph();
		if(value29 == null){
			for(int i = 1;i < specs2.size();i ++){
				value29 = specs2.get(i).getOsSph();
				if(value29 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value29 == null){
				value29 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value30 = specs2.get(0).getOsCyl();
		if(value30 == null){
			for(int i = 1;i < specs2.size();i ++){
				value30 = specs2.get(i).getOsCyl();
				if(value30 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value30 == null){
				value30 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value31 = specs2.get(0).getOsAxis();
		if(value31 == null){
			for(int i = 1;i < specs2.size();i ++){
				value31 = specs2.get(i).getOsAxis();
				if(value31 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value31 == null){
				value31 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value32 = specs2.get(0).getOsAdd();
		if(value32 == null){
			for(int i = 1;i < specs2.size();i ++){
				value32 = specs2.get(i).getOsAdd();
				if(value32 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value32 == null){
				value32 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value33 = specs2.get(0).getOsPrism();
		if(value33 == null){
			for(int i = 1;i < specs2.size();i ++){
				value33 = specs2.get(i).getOsPrism();
				if(value33 != null){
					if(appo_now == specs2.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value33 == null){
				value33 = "";
				count ++;
			}
		}else{
			if(appo_now == specs2.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		
		glass_show = "";
		if(iscomein){
			num ++;
		}else{
			iscomein = true;
		}
	}
	if(specs3.size() > 0){
		value34 = specs3.get(0).getDateStr();
		if(value34 == null){
			for(int i = 1;i < specs3.size();i ++){
				value34 = specs3.get(i).getDateStr();
				if(value34 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value34 == null){
				value34 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value35 = specs3.get(0).getOdSph();
		if(value35 == null){
			for(int i = 1;i < specs3.size();i ++){
				value35 = specs3.get(i).getOdSph();
				if(value35 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value35 == null){
				value35 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value36 = specs3.get(0).getOdCyl();
		if(value36 == null){
			for(int i = 1;i < specs3.size();i ++){
				value36 = specs3.get(i).getOdCyl();
				if(value36 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value36 == null){
				value36 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value37 = specs3.get(0).getOdAxis();
		if(value37 == null){
			for(int i = 1;i < specs3.size();i ++){
				value37 = specs3.get(i).getOdAxis();
				if(value37 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value37 == null){
				value37 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value38 = specs3.get(0).getOdAdd();
		if(value38 == null){
			for(int i = 1;i < specs3.size();i ++){
				value38 = specs3.get(i).getOdAdd();
				if(value38 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value38 == null){
				value38 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value39 = specs3.get(0).getOdPrism();
		if(value39 == null){
			for(int i = 1;i < specs3.size();i ++){
				value39 = specs3.get(i).getOdPrism();
				if(value39 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value39 == null){
				value39 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value40 = specs3.get(0).getOsSph();
		if(value40 == null){
			for(int i = 1;i < specs3.size();i ++){
				value40 = specs3.get(i).getOsSph();
				if(value40 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value40 == null){
				value40 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value41 = specs3.get(0).getOsCyl();
		if(value41 == null){
			for(int i = 1;i < specs3.size();i ++){
				value41 = specs3.get(i).getOsCyl();
				if(value41 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value41 == null){
				value41 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value42 = specs3.get(0).getOsAxis();
		if(value42 == null){
			for(int i = 1;i < specs3.size();i ++){
				value42 = specs3.get(i).getOsAxis();
				if(value42 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value42 == null){
				value42 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value43 = specs3.get(0).getOsAdd();
		if(value43 == null){
			for(int i = 1;i < specs3.size();i ++){
				value43 = specs3.get(i).getOsAdd();
				if(value43 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value43 == null){
				value43 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		value44 = specs3.get(0).getOsPrism();
		if(value44 == null){
			for(int i = 1;i < specs3.size();i ++){
				value44 = specs3.get(i).getOsPrism();
				if(value44 != null){
					if(appo_now == specs3.get(i).getAppointmentNo()){
						bg_status[count] = 1;
					}else{
						bg_status[count] = 0;
					}
					count ++;
					break;
				}
			}
			if(value44 == null){
				value44 = "";
				count ++;
			}
		}else{
			if(appo_now == specs3.get(0).getAppointmentNo()){
				bg_status[count] = 1;
			}else{
				bg_status[count] = 0;
			}
			count ++;
		}
		
		glass_show = "";
		if(iscomein){
			num ++;
		}else{
			iscomein = true;
		}
	}

%>

<script>
//if have value under slidy block set the color to brown
function changeclass(number){
	var i = number;
	var num = document.getElementById("type00" + i).options.selectedIndex;
	if(num == 0){
		count = 1;
	}else if(num == 1){
		count = 12;
	}else if(num == 2){
		count = 23;
	}else if(num == 3){
		count = 34;
	}
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("gl_date"+ i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){				
		document.getElementById("gl_date"+ i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("odSph" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("odSph" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("odCyl" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("odCyl" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("odAxis" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("odAxis" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("odAdd" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("odAdd" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("odPrism" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("odPrism" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("osSph" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("osSph" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("osCyl" + i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("osCyl" + i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("osAxis"+ i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("osAxis"+ i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("osAdd"+ i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("osAdd"+ i).className = "examfieldwhite" ;
	}
	count ++;
	if(Number(document.getElementById("bg_status" + count).value) == 0){
		document.getElementById("osPrism"+ i).className = "examfieldgrey" ;
	}else if(Number(document.getElementById("bg_status" + count).value) == 1){
		document.getElementById("osPrism"+ i).className = "examfieldwhite" ;
	}
}

function demo1() {
	var type001=document.getElementById("type001").value;
	
	var gl_date5=document.getElementById("gl_date5").value;
	var odSph5=document.getElementById("odSph5").value;
	var odCyl5=document.getElementById("odCyl5").value;
	var odAxis5=document.getElementById("odAxis5").value;
	var odAdd5=document.getElementById("odAdd5").value;
	var odPrism5=document.getElementById("odPrism5").value;
	var osSph5=document.getElementById("osSph5").value;
	var osCyl5=document.getElementById("osCyl5").value;
	var osAxis5=document.getElementById("osAxis5").value;
	var osAdd5=document.getElementById("osAdd5").value;
	var osPrism5=document.getElementById("osPrism5").value;

	var gl_date6=document.getElementById("gl_date6").value;
	var odSph6=document.getElementById("odSph6").value;
	var odCyl6=document.getElementById("odCyl6").value;
	var odAxis6=document.getElementById("odAxis6").value;
	var odAdd6=document.getElementById("odAdd6").value;
	var odPrism6=document.getElementById("odPrism6").value;
	var osSph6=document.getElementById("osSph6").value;
	var osCyl6=document.getElementById("osCyl6").value;
	var osAxis6=document.getElementById("osAxis6").value;
	var osAdd6=document.getElementById("osAdd6").value;
	var osPrism6=document.getElementById("osPrism6").value;

	var gl_date7=document.getElementById("gl_date7").value;
	var odSph7=document.getElementById("odSph7").value;
	var odCyl7=document.getElementById("odCyl7").value;
	var odAxis7=document.getElementById("odAxis7").value;
	var odAdd7=document.getElementById("odAdd7").value;
	var odPrism7=document.getElementById("odPrism7").value;
	var osSph7=document.getElementById("osSph7").value;
	var osCyl7=document.getElementById("osCyl7").value;
	var osAxis7=document.getElementById("osAxis7").value;
	var osAdd7=document.getElementById("osAdd7").value;
	var osPrism7=document.getElementById("osPrism7").value;

	var gl_date8=document.getElementById("gl_date8").value;
	var odSph8=document.getElementById("odSph8").value;
	var odCyl8=document.getElementById("odCyl8").value;
	var odAxis8=document.getElementById("odAxis8").value;
	var odAdd8=document.getElementById("odAdd8").value;
	var odPrism8=document.getElementById("odPrism8").value;
	var osSph8=document.getElementById("osSph8").value;
	var osCyl8=document.getElementById("osCyl8").value;
	var osAxis8=document.getElementById("osAxis8").value;
	var osAdd8=document.getElementById("osAdd8").value;
	var osPrism8=document.getElementById("osPrism8").value;

	if(type001=="distance"){
		document.getElementById("gl_date1").value=gl_date8;
		document.getElementById("odSph1").value=odSph8;
		document.getElementById("odCyl1").value=odCyl8;
		document.getElementById("odAxis1").value=odAxis8;
		document.getElementById("odAdd1").value=odAdd8;
		document.getElementById("odPrism1").value=odPrism8;
		document.getElementById("osSph1").value=osSph8;
		document.getElementById("osCyl1").value=osCyl8;
		document.getElementById("osAxis1").value=osAxis8;
		document.getElementById("osAdd1").value=osAdd8;
		document.getElementById("osPrism1").value=osPrism8;
	}else if(type001=="bifocal"){
		document.getElementById("gl_date1").value=gl_date5;
		document.getElementById("odSph1").value=odSph5;
		document.getElementById("odCyl1").value=odCyl5;
		document.getElementById("odAxis1").value=odAxis5;
		document.getElementById("odAdd1").value=odAdd5;
		document.getElementById("odPrism1").value=odPrism5;
		document.getElementById("osSph1").value=osSph5;
		document.getElementById("osCyl1").value=osCyl5;
		document.getElementById("osAxis1").value=osAxis5;
		document.getElementById("osAdd1").value=osAdd5;
		document.getElementById("osPrism1").value=osPrism5;
	}else if(type001=="invis bfocal"){
		document.getElementById("gl_date1").value=gl_date6;
		document.getElementById("odSph1").value=odSph6;
		document.getElementById("odCyl1").value=odCyl6;
		document.getElementById("odAxis1").value=odAxis6;
		document.getElementById("odAdd1").value=odAdd6;
		document.getElementById("odPrism1").value=odPrism6;
		document.getElementById("osSph1").value=osSph6;
		document.getElementById("osCyl1").value=osCyl6;
		document.getElementById("osAxis1").value=osAxis6;
		document.getElementById("osAdd1").value=osAdd6;
		document.getElementById("osPrism1").value=osPrism6;
	}
	else if(type001=="reading"){
		document.getElementById("gl_date1").value=gl_date7;
		document.getElementById("odSph1").value=odSph7;
		document.getElementById("odCyl1").value=odCyl7;
		document.getElementById("odAxis1").value=odAxis7;
		document.getElementById("odAdd1").value=odAdd7;
		document.getElementById("odPrism1").value=odPrism7;
		document.getElementById("osSph1").value=osSph7;
		document.getElementById("osCyl1").value=osCyl7;
		document.getElementById("osAxis1").value=osAxis7;
		document.getElementById("osAdd1").value=osAdd7;
		document.getElementById("osPrism1").value=osPrism7;
	}
	changeclass(1);
}

function demo2() {
	var type002=document.getElementById("type002").value;	
	var gl_date5=document.getElementById("gl_date5").value;
	var odSph5=document.getElementById("odSph5").value;
	var odCyl5=document.getElementById("odCyl5").value;
	var odAxis5=document.getElementById("odAxis5").value;
	var odAdd5=document.getElementById("odAdd5").value;
	var odPrism5=document.getElementById("odPrism5").value;
	var osSph5=document.getElementById("osSph5").value;
	var osCyl5=document.getElementById("osCyl5").value;
	var osAxis5=document.getElementById("osAxis5").value;
	var osAdd5=document.getElementById("osAdd5").value;
	var osPrism5=document.getElementById("osPrism5").value;
	
	var gl_date6=document.getElementById("gl_date6").value;
	var odSph6=document.getElementById("odSph6").value;
	var odCyl6=document.getElementById("odCyl6").value;
	var odAxis6=document.getElementById("odAxis6").value;
	var odAdd6=document.getElementById("odAdd6").value;
	var odPrism6=document.getElementById("odPrism6").value;
	var osSph6=document.getElementById("osSph6").value;
	var osCyl6=document.getElementById("osCyl6").value;
	var osAxis6=document.getElementById("osAxis6").value;
	var osAdd6=document.getElementById("osAdd6").value;
	var osPrism6=document.getElementById("osPrism6").value;

	var gl_date7=document.getElementById("gl_date7").value;
	var odSph7=document.getElementById("odSph7").value;
	var odCyl7=document.getElementById("odCyl7").value;
	var odAxis7=document.getElementById("odAxis7").value;
	var odAdd7=document.getElementById("odAdd7").value;
	var odPrism7=document.getElementById("odPrism7").value;
	var osSph7=document.getElementById("osSph7").value;
	var osCyl7=document.getElementById("osCyl7").value;
	var osAxis7=document.getElementById("osAxis7").value;
	var osAdd7=document.getElementById("osAdd7").value;
	var osPrism7=document.getElementById("osPrism7").value;

	var gl_date8=document.getElementById("gl_date8").value;
	var odSph8=document.getElementById("odSph8").value;
	var odCyl8=document.getElementById("odCyl8").value;
	var odAxis8=document.getElementById("odAxis8").value;
	var odAdd8=document.getElementById("odAdd8").value;
	var odPrism8=document.getElementById("odPrism8").value;
	var osSph8=document.getElementById("osSph8").value;
	var osCyl8=document.getElementById("osCyl8").value;
	var osAxis8=document.getElementById("osAxis8").value;
	var osAdd8=document.getElementById("osAdd8").value;
	var osPrism8=document.getElementById("osPrism8").value;
	
	if(type002=="distance"){
		document.getElementById("gl_date2").value=gl_date8;
		document.getElementById("odSph2").value=odSph8;
		document.getElementById("odCyl2").value=odCyl8;
		document.getElementById("odAxis2").value=odAxis8;
		document.getElementById("odAdd2").value=odAdd8;
		document.getElementById("odPrism2").value=odPrism8;
		document.getElementById("osSph2").value=osSph8;
		document.getElementById("osCyl2").value=osCyl8;
		document.getElementById("osAxis2").value=osAxis8;
		document.getElementById("osAdd2").value=osAdd8;
		document.getElementById("osPrism2").value=osPrism8;
	}else if(type002=="bifocal"){
		document.getElementById("gl_date2").value=gl_date5;
		document.getElementById("odSph2").value=odSph5;
		document.getElementById("odCyl2").value=odCyl5;
		document.getElementById("odAxis2").value=odAxis5;
		document.getElementById("odAdd2").value=odAdd5;
		document.getElementById("odPrism2").value=odPrism5;
		document.getElementById("osSph2").value=osSph5;
		document.getElementById("osCyl2").value=osCyl5;
		document.getElementById("osAxis2").value=osAxis5;
		document.getElementById("osAdd2").value=osAdd5;
		document.getElementById("osPrism2").value=osPrism5;
	}else if(type002=="invis bfocal"){
		document.getElementById("gl_date2").value=gl_date6;
		document.getElementById("odSph2").value=odSph6;
		document.getElementById("odCyl2").value=odCyl6;
		document.getElementById("odAxis2").value=odAxis6;
		document.getElementById("odAdd2").value=odAdd6;
		document.getElementById("odPrism2").value=odPrism6;
		document.getElementById("osSph2").value=osSph6;
		document.getElementById("osCyl2").value=osCyl6;
		document.getElementById("osAxis2").value=osAxis6;
		document.getElementById("osAdd2").value=osAdd6;
		document.getElementById("osPrism2").value=osPrism6;
	}
	else if(type002=="reading"){
		document.getElementById("gl_date2").value=gl_date7;
		document.getElementById("odSph2").value=odSph7;
		document.getElementById("odCyl2").value=odCyl7;
		document.getElementById("odAxis2").value=odAxis7;
		document.getElementById("odAdd2").value=odAdd7;
		document.getElementById("odPrism2").value=odPrism7;
		document.getElementById("osSph2").value=osSph7;
		document.getElementById("osCyl2").value=osCyl7;
		document.getElementById("osAxis2").value=osAxis7;
		document.getElementById("osAdd2").value=osAdd7;
		document.getElementById("osPrism2").value=osPrism7;
	}
	changeclass(2);
}

function demo3() {
	var type003=document.getElementById("type003").value;
	var gl_date5=document.getElementById("gl_date5").value;
	var odSph5=document.getElementById("odSph5").value;
	var odCyl5=document.getElementById("odCyl5").value;
	var odAxis5=document.getElementById("odAxis5").value;
	var odAdd5=document.getElementById("odAdd5").value;
	var odPrism5=document.getElementById("odPrism5").value;
	var osSph5=document.getElementById("osSph5").value;
	var osCyl5=document.getElementById("osCyl5").value;
	var osAxis5=document.getElementById("osAxis5").value;
	var osAdd5=document.getElementById("osAdd5").value;
	var osPrism5=document.getElementById("osPrism5").value;

	var gl_date6=document.getElementById("gl_date6").value;
	var odSph6=document.getElementById("odSph6").value;
	var odCyl6=document.getElementById("odCyl6").value;
	var odAxis6=document.getElementById("odAxis6").value;
	var odAdd6=document.getElementById("odAdd6").value;
	var odPrism6=document.getElementById("odPrism6").value;
	var osSph6=document.getElementById("osSph6").value;
	var osCyl6=document.getElementById("osCyl6").value;
	var osAxis6=document.getElementById("osAxis6").value;
	var osAdd6=document.getElementById("osAdd6").value;
	var osPrism6=document.getElementById("osPrism6").value;

	var gl_date7=document.getElementById("gl_date7").value;
	var odSph7=document.getElementById("odSph7").value;
	var odCyl7=document.getElementById("odCyl7").value;
	var odAxis7=document.getElementById("odAxis7").value;
	var odAdd7=document.getElementById("odAdd7").value;
	var odPrism7=document.getElementById("odPrism7").value;
	var osSph7=document.getElementById("osSph7").value;
	var osCyl7=document.getElementById("osCyl7").value;
	var osAxis7=document.getElementById("osAxis7").value;
	var osAdd7=document.getElementById("osAdd7").value;
	var osPrism7=document.getElementById("osPrism7").value;

	var gl_date8=document.getElementById("gl_date8").value;
	var odSph8=document.getElementById("odSph8").value;
	var odCyl8=document.getElementById("odCyl8").value;
	var odAxis8=document.getElementById("odAxis8").value;
	var odAdd8=document.getElementById("odAdd8").value;
	var odPrism8=document.getElementById("odPrism8").value;
	var osSph8=document.getElementById("osSph8").value;
	var osCyl8=document.getElementById("osCyl8").value;
	var osAxis8=document.getElementById("osAxis8").value;
	var osAdd8=document.getElementById("osAdd8").value;
	var osPrism8=document.getElementById("osPrism8").value;
	
	if(type003=="distance"){
		document.getElementById("gl_date3").value=gl_date8;
		document.getElementById("odSph3").value=odSph8;
		document.getElementById("odCyl3").value=odCyl8;
		document.getElementById("odAxis3").value=odAxis8;
		document.getElementById("odAdd3").value=odAdd8;
		document.getElementById("odPrism3").value=odPrism8;
		document.getElementById("osSph3").value=osSph8;
		document.getElementById("osCyl3").value=osCyl8;
		document.getElementById("osAxis3").value=osAxis8;
		document.getElementById("osAdd3").value=osAdd8;
		document.getElementById("osPrism3").value=osPrism8;
	}else if(type003=="bifocal"){
		document.getElementById("gl_date3").value=gl_date5;
		document.getElementById("odSph3").value=odSph5;
		document.getElementById("odCyl3").value=odCyl5;
		document.getElementById("odAxis3").value=odAxis5;
		document.getElementById("odAdd3").value=odAdd5;
		document.getElementById("odPrism3").value=odPrism5;
		document.getElementById("osSph3").value=osSph5;
		document.getElementById("osCyl3").value=osCyl5;
		document.getElementById("osAxis3").value=osAxis5;
		document.getElementById("osAdd3").value=osAdd5;
		document.getElementById("osPrism3").value=osPrism5;
	}else if(type003=="invis bfocal"){
		document.getElementById("gl_date3").value=gl_date6;
		document.getElementById("odSph3").value=odSph6;
		document.getElementById("odCyl3").value=odCyl6;
		document.getElementById("odAxis3").value=odAxis6;
		document.getElementById("odAdd3").value=odAdd6;
		document.getElementById("odPrism3").value=odPrism6;
		document.getElementById("osSph3").value=osSph6;
		document.getElementById("osCyl3").value=osCyl6;
		document.getElementById("osAxis3").value=osAxis6;
		document.getElementById("osAdd3").value=osAdd6;
		document.getElementById("osPrism3").value=osPrism6;
	}
	else if(type003=="reading"){
		document.getElementById("gl_date3").value=gl_date7;
		document.getElementById("odSph3").value=odSph7;
		document.getElementById("odCyl3").value=odCyl7;
		document.getElementById("odAxis3").value=odAxis7;
		document.getElementById("odAdd3").value=odAdd7;
		document.getElementById("odPrism3").value=odPrism7;
		document.getElementById("osSph3").value=osSph7;
		document.getElementById("osCyl3").value=osCyl7;
		document.getElementById("osAxis3").value=osAxis7;
		document.getElementById("osAdd3").value=osAdd7;
		document.getElementById("osPrism3").value=osPrism7;
	}
	changeclass(3);
}
function demo4() {
	var type004=document.getElementById("type004").value;
	var gl_date5=document.getElementById("gl_date5").value;
	var odSph5=document.getElementById("odSph5").value;
	var odCyl5=document.getElementById("odCyl5").value;
	var odAxis5=document.getElementById("odAxis5").value;
	var odAdd5=document.getElementById("odAdd5").value;
	var odPrism5=document.getElementById("odPrism5").value;
	var osSph5=document.getElementById("osSph5").value;
	var osCyl5=document.getElementById("osCyl5").value;
	var osAxis5=document.getElementById("osAxis5").value;
	var osAdd5=document.getElementById("osAdd5").value;
	var osPrism5=document.getElementById("osPrism5").value;

	var gl_date6=document.getElementById("gl_date6").value;
	var odSph6=document.getElementById("odSph6").value;
	var odCyl6=document.getElementById("odCyl6").value;
	var odAxis6=document.getElementById("odAxis6").value;
	var odAdd6=document.getElementById("odAdd6").value;
	var odPrism6=document.getElementById("odPrism6").value;
	var osSph6=document.getElementById("osSph6").value;
	var osCyl6=document.getElementById("osCyl6").value;
	var osAxis6=document.getElementById("osAxis6").value;
	var osAdd6=document.getElementById("osAdd6").value;
	var osPrism6=document.getElementById("osPrism6").value;

	var gl_date7=document.getElementById("gl_date7").value;
	var odSph7=document.getElementById("odSph7").value;
	var odCyl7=document.getElementById("odCyl7").value;
	var odAxis7=document.getElementById("odAxis7").value;
	var odAdd7=document.getElementById("odAdd7").value;
	var odPrism7=document.getElementById("odPrism7").value;
	var osSph7=document.getElementById("osSph7").value;
	var osCyl7=document.getElementById("osCyl7").value;
	var osAxis7=document.getElementById("osAxis7").value;
	var osAdd7=document.getElementById("osAdd7").value;
	var osPrism7=document.getElementById("osPrism7").value;

	var gl_date8=document.getElementById("gl_date8").value;
	var odSph8=document.getElementById("odSph8").value;
	var odCyl8=document.getElementById("odCyl8").value;
	var odAxis8=document.getElementById("odAxis8").value;
	var odAdd8=document.getElementById("odAdd8").value;
	var odPrism8=document.getElementById("odPrism8").value;
	var osSph8=document.getElementById("osSph8").value;
	var osCyl8=document.getElementById("osCyl8").value;
	var osAxis8=document.getElementById("osAxis8").value;
	var osAdd8=document.getElementById("osAdd8").value;
	var osPrism8=document.getElementById("osPrism8").value;
	
	if(type004=="distance"){
		document.getElementById("gl_date4").value=gl_date8;
		document.getElementById("odSph4").value=odSph8;
		document.getElementById("odCyl4").value=odCyl8;
		document.getElementById("odAxis4").value=odAxis8;
		document.getElementById("odAdd4").value=odAdd8;
		document.getElementById("odPrism4").value=odPrism8;
		document.getElementById("osSph4").value=osSph8;
		document.getElementById("osCyl4").value=osCyl8;
		document.getElementById("osAxis4").value=osAxis8;
		document.getElementById("osAdd4").value=osAdd8;
		document.getElementById("osPrism4").value=osPrism8;
	}else if(type004=="bifocal"){
		document.getElementById("gl_date4").value=gl_date5;
		document.getElementById("odSph4").value=odSph5;
		document.getElementById("odCyl4").value=odCyl5;
		document.getElementById("odAxis4").value=odAxis5;
		document.getElementById("odAdd4").value=odAdd5;
		document.getElementById("odPrism4").value=odPrism5;
		document.getElementById("osSph4").value=osSph5;
		document.getElementById("osCyl4").value=osCyl5;
		document.getElementById("osAxis4").value=osAxis5;
		document.getElementById("osAdd4").value=osAdd5;
		document.getElementById("osPrism4").value=osPrism5;
	}else if(type004=="invis bfocal"){
		document.getElementById("gl_date4").value=gl_date6;
		document.getElementById("odSph4").value=odSph6;
		document.getElementById("odCyl4").value=odCyl6;
		document.getElementById("odAxis4").value=odAxis6;
		document.getElementById("odAdd4").value=odAdd6;
		document.getElementById("odPrism4").value=odPrism6;
		document.getElementById("osSph4").value=osSph6;
		document.getElementById("osCyl4").value=osCyl6;
		document.getElementById("osAxis4").value=osAxis6;
		document.getElementById("osAdd4").value=osAdd6;
		document.getElementById("osPrism4").value=osPrism6;
	}
	else if(type004=="reading"){
		document.getElementById("gl_date4").value=gl_date7;
		document.getElementById("odSph4").value=odSph7;
		document.getElementById("odCyl4").value=odCyl7;
		document.getElementById("odAxis4").value=odAxis7;
		document.getElementById("odAdd4").value=odAdd7;
		document.getElementById("odPrism4").value=odPrism7;
		document.getElementById("osSph4").value=osSph7;
		document.getElementById("osCyl4").value=osCyl7;
		document.getElementById("osAxis4").value=osAxis7;
		document.getElementById("osAdd4").value=osAdd7;
		document.getElementById("osPrism4").value=osPrism7;
	}
	changeclass(4);
}

function touchColor() {
		var divs = document.getElementsByClassName(document, "div", "slidey");
        for (var i=0; i<divs.length; i++) {
                var inputs = divs[i].getElementsByTagName("INPUT");
                var teinputs=divs[i].getElementsByTagName("TEXTAREA");
                for (var j=0; j<inputs.length; j++)
                        if (inputs[j].value.length>0 && inputs[j].className=="examfieldwhite")
                                break;
                for (var k=0; k<teinputs.length; k++)
                        if (teinputs[k].value.length>0 && teinputs[k].className=="examfieldwhite")
                                break;
                var color=(j<inputs.length || k<teinputs.length)?"brown":"black";
                divs[i].getElementsByTagName("A")[0].style.color=color;
        }
}
function setglasseshxclass(){
	var num = Number(document.getElementById("shownum").value);
	//var num1 = Number(document.getElementById("appointment_no").value);
	var count = 1;
	if(num > 0){
		for(var i = 1;i <= num; i++){
			if(i == 1){
				count = 1;
			}else if(i == 2){
				count = 12;
			}else if(i == 3){
				count = 23;
			}else if(i == 4){
				count = 34;
			}
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("gl_date"+ i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){				
				document.getElementById("gl_date"+ i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("odSph" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("odSph" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("odCyl" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("odCyl" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("odAxis" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("odAxis" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("odAdd" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("odAdd" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("odPrism" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("odPrism" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("osSph" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("osSph" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("osCyl" + i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("osCyl" + i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("osAxis"+ i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("osAxis"+ i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("osAdd"+ i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("osAdd"+ i).className = "examfieldwhite" ;
			}
			count ++;
			if(Number(document.getElementById("bg_status" + count).value) == 0){
				document.getElementById("osPrism"+ i).className = "examfieldgrey" ;
			}else if(Number(document.getElementById("bg_status" + count).value) == 1){
				document.getElementById("osPrism"+ i).className = "examfieldwhite" ;
			}
		}
	}
}
</script>

<script>
var effectMap = {};
//jQuery("document").ready(function() {
	
//});

function togglediv(el) {
	jQuery('#s_' + el.id.substring(el.id.indexOf('_')+1)).toggle();
}

function expanddiv(el) {
	jQuery('#s_' + el.id.substring(el.id.indexOf('_')+1)).show();
}

function collapsediv(el) {
	jQuery('#s_' + el.id.substring(el.id.indexOf('_')+1)).hide();
}

function whiteField(el){
  		el.className="examfieldwhite";
}

function setfieldvalue(name,value) {
	jQuery("input[measurement='"+name+"']").each(function() {
		jQuery(this).val(value);
		whiteField(this);
	});
}

function getfieldvalue(name) {
	var val = undefined;
	jQuery("input[measurement='"+name+"']").each(function() {
		val = jQuery(this).val();
	});
	return val;
}

function copyAR(){
	setfieldvalue("od_manifest_refraction_sph",getfieldvalue("od_ar_sph"));
	setfieldvalue("os_manifest_refraction_sph",getfieldvalue("os_ar_sph"));
	setfieldvalue("od_manifest_refraction_cyl",getfieldvalue("od_ar_cyl"));
	setfieldvalue("os_manifest_refraction_cyl",getfieldvalue("os_ar_cyl"));
	setfieldvalue("od_manifest_refraction_axis",getfieldvalue("od_ar_axis"));
	setfieldvalue("os_manifest_refraction_axis",getfieldvalue("os_ar_axis"));
}

var copy_gl_rs = "";
var copy_gl_rc = "";
var copy_gl_rx = "";
var copy_gl_ls = "";
var copy_gl_lc = "";
var copy_gl_lx = "";
var copy_from = true;
function getglasshxvalue(name){
	var val;
	val = document.getElementById(name).value;
	return val;
}

function copyglasses(){
	copy_form = true;
	copy_gl_rs = getglasshxvalue("odSph1");
	copy_gl_rc = getglasshxvalue("odCyl1");
	copy_gl_rx = getglasshxvalue("odAxis1");
	copy_gl_ls = getglasshxvalue("osSph1");
	copy_gl_lc = getglasshxvalue("osCyl1");	
	copy_gl_lx = getglasshxvalue("osAxis1");
}

var copy_v_rs = "";
var copy_v_rc = "";
var copy_v_rx = "";
var copy_v_ls = "";
var copy_v_lc = "";
var copy_v_lx = "";

function copyar(){
	copy_form = false;
	copy_v_rs = getfieldvalue("v_rs");
	copy_v_rc = getfieldvalue("v_rc");
	copy_v_rx = getfieldvalue("v_rx");
	copy_v_ls = getfieldvalue("v_ls");
	copy_v_lc = getfieldvalue("v_lc");	
	copy_v_lx = getfieldvalue("v_lx");
}

function paste_mdist(){
	if(!copy_form){
		setfieldvalue("v_rds",copy_v_rs);
		setfieldvalue("v_rdc",copy_v_rc);
		setfieldvalue("v_rdx",copy_v_rx);
		setfieldvalue("v_lds",copy_v_ls);
		setfieldvalue("v_ldc",copy_v_lc);
		setfieldvalue("v_ldx",copy_v_lx);
	}else{
		setfieldvalue("v_rds",copy_gl_rs);
		setfieldvalue("v_rdc",copy_gl_rc);
		setfieldvalue("v_rdx",copy_gl_rx);
		setfieldvalue("v_lds",copy_gl_ls);
		setfieldvalue("v_ldc",copy_gl_lc);
		setfieldvalue("v_ldx",copy_gl_lx);
	}
}

function paste_mnear(){
	if(!copy_form){
		setfieldvalue("v_rns",copy_v_rs);
		setfieldvalue("v_rnc",copy_v_rc);
		setfieldvalue("v_rnx",copy_v_rx);
		setfieldvalue("v_lns",copy_v_ls);
		setfieldvalue("v_lnc",copy_v_lc);
		setfieldvalue("v_lnx",copy_v_lx);
	}else{
		setfieldvalue("v_rns",copy_gl_rs);
		setfieldvalue("v_rnc",copy_gl_rc);
		setfieldvalue("v_rnx",copy_gl_rx);
		setfieldvalue("v_lns",copy_gl_ls);
		setfieldvalue("v_lnc",copy_gl_lc);
		setfieldvalue("v_lnx",copy_gl_lx);
	}
}

function paste_Cyclo(){
	if(!copy_form){
		setfieldvalue("v_rcs",copy_v_rs);
		setfieldvalue("v_rcc",copy_v_rc);
		setfieldvalue("v_rcx",copy_v_rx);
		setfieldvalue("v_lcs",copy_v_ls);
		setfieldvalue("v_lcc",copy_v_lc);
		setfieldvalue("v_lcx",copy_v_lx);
	}else{
		setfieldvalue("v_rcs",copy_gl_rs);
		setfieldvalue("v_rcc",copy_gl_rc);
		setfieldvalue("v_rcx",copy_gl_rx);
		setfieldvalue("v_lcs",copy_gl_ls);
		setfieldvalue("v_lcc",copy_gl_lc);
		setfieldvalue("v_lcx",copy_gl_lx);
	}
}

function copySpecs(){
	jQuery.ajax({dataType: "script", url:ctx+"/eyeform/SpecsHistory.do?demographicNo="+demographicNo+"&method=copySpecs"});
}

function setAnterior(){
	setfieldvalue("od_cornea","clear");
	setfieldvalue("os_cornea","clear");
	setfieldvalue("od_conjuctiva_sclera","white");
	setfieldvalue("os_conjuctiva_sclera","white");
	setfieldvalue("od_anterior_chamber","deep and quiet");
	setfieldvalue("os_anterior_chamber","deep and quiet");
	setfieldvalue("od_angle_middle1","open");
	setfieldvalue("os_angle_middle1","open");
	setfieldvalue("od_iris","normal");
	setfieldvalue("os_iris","normal");
	setfieldvalue("od_lens","clear");
	setfieldvalue("os_lens","clear");
	setfieldvalue("od_anterior_vitreous","clear");
	setfieldvalue("os_anterior_vitreous","clear");
}
function clearAnterior(){
	setfieldvalue("od_cornea","");
	setfieldvalue("os_cornea","");
	setfieldvalue("od_conjuctiva_sclera","");
	setfieldvalue("os_conjuctiva_sclera","");
	setfieldvalue("od_anterior_chamber","");
	setfieldvalue("os_anterior_chamber","");
	setfieldvalue("od_angle_middle0","");
	setfieldvalue("od_angle_middle1","");
	setfieldvalue("od_angle_middle2","");
	setfieldvalue("od_angle_up","");
	setfieldvalue("od_angle_down","");
	setfieldvalue("os_angle_middle0","");
	setfieldvalue("os_angle_middle1","");
	setfieldvalue("os_angle_middle2","");
	setfieldvalue("os_angle_up","");
	setfieldvalue("os_angle_down","");
	setfieldvalue("od_iris","");
	setfieldvalue("os_iris","");
	setfieldvalue("od_lens","");
	setfieldvalue("os_lens","");
	setfieldvalue("od_anterior_vitreous","");
	setfieldvalue("os_anterior_vitreous","");
}

function setPosterior(){
	setfieldvalue("od_disc","normal");
	setfieldvalue("os_disc","normal");
	setfieldvalue("od_macula","normal");
	setfieldvalue("os_macula","normal");
	setfieldvalue("od_retina","normal");
	setfieldvalue("os_retina","normal");
	setfieldvalue("od_vitreous","clear");
	setfieldvalue("os_vitreous","clear");
}
function clearPosterior(){
	setfieldvalue("od_disc","");
	setfieldvalue("os_disc","");
	setfieldvalue("od_macula","");
	setfieldvalue("os_macula","");
	setfieldvalue("od_retina","");
	setfieldvalue("os_retina","");
	setfieldvalue("od_vitreous","");
	setfieldvalue("os_vitreous","");
	setfieldvalue("od_cd_ratio_horizontal","");
	setfieldvalue("os_cd_ratio_horizontal","");
}

function clearMeasurement_od(){
	setfieldvalue("v_rk1","");
	setfieldvalue("v_rk2","");
	setfieldvalue("v_rkx","");
	setfieldvalue("v_rs","");
	setfieldvalue("v_rc","");
	setfieldvalue("v_rx","");
	setfieldvalue("v_rar","");
	setfieldvalue("v_rds","");
	setfieldvalue("v_rdc","");
	setfieldvalue("v_rdx","");
	setfieldvalue("v_rdv","");
	setfieldvalue("v_rns","");
	setfieldvalue("v_rnc","");
	setfieldvalue("v_rnx","");
	setfieldvalue("v_rnv","");
	setfieldvalue("v_rcs","");
	setfieldvalue("v_rcc","");
	setfieldvalue("v_rcx","");
	setfieldvalue("v_rcv","");
}

function clearMeasurement_os(){
	setfieldvalue("v_lk1","");
	setfieldvalue("v_lk2","");
	setfieldvalue("v_lkx","");
	setfieldvalue("v_ls","");
	setfieldvalue("v_lc","");
	setfieldvalue("v_lx","");
	setfieldvalue("v_lar","");
	setfieldvalue("v_lds","");
	setfieldvalue("v_ldc","");
	setfieldvalue("v_ldx","");
	setfieldvalue("v_ldv","");
	setfieldvalue("v_lns","");
	setfieldvalue("v_lnc","");
	setfieldvalue("v_lnx","");
	setfieldvalue("v_lnv","");
	setfieldvalue("v_lcs","");
	setfieldvalue("v_lcc","");
	setfieldvalue("v_lcx","");
	setfieldvalue("v_lcv","");
}

function setExternal(){
	setfieldvalue("od_face","normal");
	setfieldvalue("os_face","normal");
	setfieldvalue("od_upper_lid","normal");
	setfieldvalue("os_upper_lid","normal");
	setfieldvalue("od_lower_lid","normal");
	setfieldvalue("os_lower_lid","normal");
	setfieldvalue("od_punctum","normal");
	setfieldvalue("os_punctum","normal");
	setfieldvalue("od_lacrimal_lake","normal");
	setfieldvalue("os_lacrimal_lake","normal");
}

function clearExternal(){
	setfieldvalue("od_face","");
	setfieldvalue("os_face","");
	setfieldvalue("od_upper_lid","");
	setfieldvalue("os_upper_lid","");
	setfieldvalue("od_lower_lid","");
	setfieldvalue("os_lower_lid","");
	setfieldvalue("od_punctum","");
	setfieldvalue("os_punctum","");
	setfieldvalue("od_lacrimal_lake","");
	setfieldvalue("os_lacrimal_lake","");
}

function setDuc_od(){
	setfieldvalue("duc_rur","normal");
	setfieldvalue("duc_rul","normal");
	setfieldvalue("duc_rr","normal");
	setfieldvalue("duc_rl","normal");
	setfieldvalue("duc_rdr","normal");
	setfieldvalue("duc_rdl","normal");
}

function clearDuc_od(){
	setfieldvalue("duc_rur","");
	setfieldvalue("duc_rul","");
	setfieldvalue("duc_rr","");
	setfieldvalue("duc_rl","");
	setfieldvalue("duc_rdr","");
	setfieldvalue("duc_rdl","");
}

function setDuc_os(){
	setfieldvalue("duc_lur","normal");
	setfieldvalue("duc_lul","normal");
	setfieldvalue("duc_lr","normal");
	setfieldvalue("duc_ll","normal");
	setfieldvalue("duc_ldr","normal");
	setfieldvalue("duc_ldl","normal");
}

function clearDuc_os(){
	setfieldvalue("duc_lur","");
	setfieldvalue("duc_lul","");
	setfieldvalue("duc_lr","");
	setfieldvalue("duc_ll","");
	setfieldvalue("duc_ldr","");
	setfieldvalue("duc_ldl","");
}

function setDuc_ou(){
	setfieldvalue("dip_ur","normal");
	setfieldvalue("dip_u","normal");
	setfieldvalue("dip_r","normal");
	setfieldvalue("dip_p","normal");
	setfieldvalue("dip_dr","normal");
	setfieldvalue("dip_d","normal");
}

function clearDuc_ou(){
	setfieldvalue("dip_ur","");
	setfieldvalue("dip_u","");
	setfieldvalue("dip_r","");
	setfieldvalue("dip_p","");
	setfieldvalue("dip_dr","");
	setfieldvalue("dip_d","");
}

function setDeviation(){
	setfieldvalue("dev_u","normal");
	setfieldvalue("dev_near","normal");
	setfieldvalue("dev_r","normal");
	setfieldvalue("dev_p","normal");
	setfieldvalue("dev_l","normal");
	setfieldvalue("dev_plus3","normal");
	setfieldvalue("dev_rt","normal");
	setfieldvalue("dev_d","normal");
	setfieldvalue("dev_lt","normal");
	setfieldvalue("dev_far","normal");
}

function clearDeviation(){
	setfieldvalue("dev_u","");
	setfieldvalue("dev_near","");
	setfieldvalue("dev_r","");
	setfieldvalue("dev_p","");
	setfieldvalue("dev_l","");
	setfieldvalue("dev_plus3","");
	setfieldvalue("dev_rt","");
	setfieldvalue("dev_d","");
	setfieldvalue("dev_lt","");
	setfieldvalue("dev_far","");
}

function setExternal_od(){
	setfieldvalue("ext_rface","normal");
	setfieldvalue("ext_rretro","normal");
	setfieldvalue("ext_rhertel","normal");
}

function clearExternal_od(){
	setfieldvalue("ext_rface","");
	setfieldvalue("ext_rretro","");
	setfieldvalue("ext_rhertel","");
}

function setExternal_os(){
	setfieldvalue("ext_lface","normal");
	setfieldvalue("ext_lretro","normal");
	setfieldvalue("ext_lhertel","normal");
}

function clearExternal_os(){
	setfieldvalue("ext_lface","");
	setfieldvalue("ext_lretro","");
	setfieldvalue("ext_lhertel","");
}

function setEyelid_od(){
	setfieldvalue("ext_rul","normal");
	setfieldvalue("ext_rll","normal");
	setfieldvalue("ext_rlake","normal");
	setfieldvalue("ext_rirrig","normal");
	setfieldvalue("ext_rpunc","normal");
	setfieldvalue("ext_rnld","normal");
	setfieldvalue("ext_rdye","normal");
}

function clearEyelid_od(){
	setfieldvalue("ext_rul","");
	setfieldvalue("ext_rll","");
	setfieldvalue("ext_rlake","");
	setfieldvalue("ext_rirrig","");
	setfieldvalue("ext_rpunc","");
	setfieldvalue("ext_rnld","");
	setfieldvalue("ext_rdye","");
}

function setEyelid_os(){
	setfieldvalue("ext_lul","normal");
	setfieldvalue("ext_lll","normal");
	setfieldvalue("ext_llake","normal");
	setfieldvalue("ext_lirrig","normal");
	setfieldvalue("ext_lpunc","normal");
	setfieldvalue("ext_lnld","normal");
	setfieldvalue("ext_ldye","normal");
}

function clearEyelid_os(){
	setfieldvalue("ext_lul","");
	setfieldvalue("ext_lll","");
	setfieldvalue("ext_llake","");
	setfieldvalue("ext_lirrig","");
	setfieldvalue("ext_lpunc","");
	setfieldvalue("ext_lnld","");
	setfieldvalue("ext_ldye","");
}

function setAnterior_od(){
	setfieldvalue("a_rk","clear");
	setfieldvalue("a_rconj","white");
	setfieldvalue("a_rac","deep and quiet");
	//setfieldvalue("a_rangle_1","");
	//setfieldvalue("a_rangle_2","");
	setfieldvalue("a_rangle_3","open");
	//setfieldvalue("a_rangle_4","");
	//setfieldvalue("a_rangle_5","");
	setfieldvalue("a_riris","normal");
	setfieldvalue("a_rlens","clear");
}

function clearAnterior_od(){
	setfieldvalue("a_rk","");
	setfieldvalue("a_rconj","");
	setfieldvalue("a_rac","");
	setfieldvalue("a_rangle_1","");
	setfieldvalue("a_rangle_2","");
	setfieldvalue("a_rangle_3","");
	setfieldvalue("a_rangle_4","");
	setfieldvalue("a_rangle_5","");
	setfieldvalue("a_riris","");
	setfieldvalue("a_rlens","");
}

function setAnterior_os(){
	setfieldvalue("a_lk","clear");
	setfieldvalue("a_lconj","white");
	setfieldvalue("a_lac","deep and quiet");
	//setfieldvalue("a_langle_1","");
	//setfieldvalue("a_langle_2","");
	setfieldvalue("a_langle_3","open");
	//setfieldvalue("a_langle_4","");
	//setfieldvalue("a_langle_5","");
	setfieldvalue("a_liris","normal");
	setfieldvalue("a_llens","clear");
}

function clearAnterior_os(){
	setfieldvalue("a_lk","");
	setfieldvalue("a_lconj","");
	setfieldvalue("a_lac","");
	setfieldvalue("a_langle_1","");
	setfieldvalue("a_langle_2","");
	setfieldvalue("a_langle_3","");
	setfieldvalue("a_langle_4","");
	setfieldvalue("a_langle_5","");
	setfieldvalue("a_liris","");
	setfieldvalue("a_llens","");
}

function setPosterior_od(){
	setfieldvalue("p_rdisc","normal");
	//setfieldvalue("p_rcd","normal");
	setfieldvalue("p_rmac","normal");
	setfieldvalue("p_rret","normal");
	setfieldvalue("p_rvit","clear");
}

function clearPosterior_od(){
	setfieldvalue("p_rdisc","");
	setfieldvalue("p_rcd","");
	setfieldvalue("p_rmac","");
	setfieldvalue("p_rret","");
	setfieldvalue("p_rvit","");
}

function setPosterior_os(){
	setfieldvalue("p_ldisc","normal");
	//setfieldvalue("p_lcd","normal");
	setfieldvalue("p_lmac","normal");
	setfieldvalue("p_lret","normal");
	setfieldvalue("p_lvit","clear");
}

function clearPosterior_os(){
	setfieldvalue("p_ldisc","");
	setfieldvalue("p_lcd","");
	setfieldvalue("p_lmac","");
	setfieldvalue("p_lret","");
	setfieldvalue("p_lvit","");
}

function expandAll() {
	jQuery(".title a").each(function() {
		if(this.id.indexOf('a_')!=-1) {
			expanddiv(this);
		}
	});
}

function collapseAll() {
	jQuery(".title a").each(function() {
		if(this.id.indexOf('a_')!=-1) {
			collapsediv(this);
		}
	});
}
var sumbitform = false;
var rowID=1;
rowID = Number(document.getElementById("shownum").value) + 1;
//if(Number(document.getElementById("shownum").value) == 1){
//	rowID = 2;
//}

function insRow()
{
	sumbitform = true;
	var tab_num = 0;
	var row = rowID -1;
	tab_num = document.getElementById("osPrism"+row.toString()).tabIndex + 2;
	
	if(rowID<5){
		document.getElementById("shownum").value = rowID;
		var rowNo=rowID;
		var x=document.getElementById('myTable').insertRow(rowID+1);
		var h1=x.insertCell(0);
		var h2=x.insertCell(1);
		var h3=x.insertCell(2);
		var h4=x.insertCell(3);
		var h5=x.insertCell(4);
		var h6=x.insertCell(5);
		var h7=x.insertCell(6);
		var h8=x.insertCell(7);
		var h9=x.insertCell(8);
		var h10=x.insertCell(9);
		var h11=x.insertCell(10);
		var h12=x.insertCell(11);
		var h13=x.insertCell(12);
		var h14=x.insertCell(13);
		var h15=x.insertCell(14);
		var h16=x.insertCell(15);

		h1.innerHTML="<div><select style='width:100%' onchange='demo"+rowNo+"()' name='specs.type"+rowNo+"' id='type00"+rowNo+"'><option value='distance'>distance</option><option value='bifocal'>bifocal</option><option value='invis bfocal'>invis bfocal</option><option value='reading'>reading</option></select></div>";
		h2.innerHTML="<div><input name='specs.odSph"+rowNo+"' id='odSph"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h3.innerHTML="<div><input name='specs.odCyl"+rowNo+"' id='odCyl"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h4.innerHTML="<div><input name='specs.odAxis"+rowNo+"' id='odAxis"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h5.innerHTML="<div><input name='specs.odAdd"+rowNo+"' id='odAdd"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h6.innerHTML="<div><input name='specs.odPrism"+rowNo+"' id='odPrism"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h7.innerHTML="<div  width='4%'></div>";
		h8.innerHTML="<div><input name='specs.osSph"+rowNo+"' id='osSph"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h9.innerHTML="<div><input name='specs.osCyl"+rowNo+"' id='osCyl"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h10.innerHTML="<div><input name='specs.osAxis"+rowNo+"' id='osAxis"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h11.innerHTML="<div><input name='specs.osAdd"+rowNo+"' id='osAdd"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h12.innerHTML="<div><input name='specs.osPrism"+rowNo+"' id='osPrism"+rowNo+"' type='text' tabindex='"+tab_num+"' maxlength='6' class='examfieldgrey' style='width:95%'  onfocus='whiteField(this);'></div>";
		tab_num ++;
		h13.innerHTML="<div><input name='specs.dateStr"+rowNo+"'  type='text' tabindex='"+tab_num+"' class='examfieldgrey' style='width:115%'  id='gl_date"+rowNo+"' onfocus='whiteField(this);'></div>";
		h14.innerHTML="<div  width='1%'><img src='"+ctx+"/images/cal.gif' id='pdate_cal"+rowNo+"'></div>";
		h15.innerHTML="<div><a href='javascript:void(0)' onclick='hxOpen"+rowNo+"();'><img src='../images/notes.gif' /> </a></div>";
		h16.innerHTML="<div><input type='hidden' value='' name='specs.id"+rowNo+"' id='specs.id"+rowNo+"'></div>";


		Calendar.setup({ inputField : "gl_date"+rowNo, ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal"+rowNo, singleClick : true, step : 1 });
		 
		rowID=rowID+1;
	}
}

function hxOpen1()
{
        var id=document.getElementById("specs.id1").value;
        var type=document.getElementById("type001").value;  
		var date=document.getElementById("gl_date1").value;
		var odSph=document.getElementById("odSph1").value;
		var odCyl=document.getElementById("odCyl1").value;
        var odAxis=document.getElementById("odAxis1").value;
		var odAdd=document.getElementById("odAdd1").value;
    	var odPrism=document.getElementById("odPrism1").value;
		var osSph=document.getElementById("osSph1").value;
		var osCyl=document.getElementById("osCyl1").value;
		var osAxis=document.getElementById("osAxis1").value;
        var osAdd=document.getElementById("osAdd1").value;
		var osPrism=document.getElementById("osPrism1").value;  
		var appno=document.getElementById("appointment_no").value; 
		var demno=document.getElementById("demographic_no").value; 
 window.open("../eyeform/glassHX.jsp?specs.id="+id+"&type="+type+"&dateStr="+date+"&odSph="+odSph+"&odCyl="+odCyl+"&odAxis="+odAxis+"&odAdd="+odAdd+"&odPrism="+odPrism+"&osSph="+osSph+"&osCyl="+osCyl+"&osAxis="+osAxis+"&osAdd="+osAdd+"&osPrism="+osPrism+"&appointment_no="+appno+"&demographic_no="+demno+"",'anwin','width=400,height=300');

}
function hxOpen2()
{
        var id=document.getElementById("specs.id2").value;
        var type=document.getElementById("type002").value;  	
		var date=document.getElementById("gl_date2").value;
		
		var odSph=document.getElementById("odSph2").value;
		var odCyl=document.getElementById("odCyl2").value;
        var odAxis=document.getElementById("odAxis2").value;
		var odAdd=document.getElementById("odAdd2").value;
    	var odPrism=document.getElementById("odPrism2").value;
		
		var osSph=document.getElementById("osSph2").value;
		var osCyl=document.getElementById("osCyl2").value;
		var osAxis=document.getElementById("osAxis2").value;
        var osAdd=document.getElementById("osAdd2").value;
		var osPrism=document.getElementById("osPrism2").value;  
		
		var appno=document.getElementById("appointment_no").value; 
		var demno=document.getElementById("demographic_no").value; 
    	
 window.open("../eyeform/glassHX.jsp?specs.id="+id+"&type="+type+"&dateStr="+date+"&odSph="+odSph+"&odCyl="+odCyl+"&odAxis="+odAxis+"&odAdd="+odAdd+"&odPrism="+odPrism+"&osSph="+osSph+"&osCyl="+osCyl+"&osAxis="+osAxis+"&osAdd="+osAdd+"&osPrism="+osPrism+"&appointment_no="+appno+"&demographic_no="+demno+"",'anwin','width=400,height=300');

}
function hxOpen3()
{
        var id=document.getElementById("specs.id3").value;
        var type=document.getElementById("type003").value;  	
		var date=document.getElementById("gl_date3").value;
		
		var odSph=document.getElementById("odSph3").value;
		var odCyl=document.getElementById("odCyl3").value;
        var odAxis=document.getElementById("odAxis3").value;
		var odAdd=document.getElementById("odAdd3").value;
    	var odPrism=document.getElementById("odPrism3").value;
		
		var osSph=document.getElementById("osSph3").value;
		var osCyl=document.getElementById("osCyl3").value;
		var osAxis=document.getElementById("osAxis3").value;
        var osAdd=document.getElementById("osAdd3").value;
		var osPrism=document.getElementById("osPrism3").value;  
		
		var appno=document.getElementById("appointment_no").value; 
		var demno=document.getElementById("demographic_no").value; 
    	
 window.open("../eyeform/glassHX.jsp?specs.id="+id+"&type="+type+"&dateStr="+date+"&odSph="+odSph+"&odCyl="+odCyl+"&odAxis="+odAxis+"&odAdd="+odAdd+"&odPrism="+odPrism+"&osSph="+osSph+"&osCyl="+osCyl+"&osAxis="+osAxis+"&osAdd="+osAdd+"&osPrism="+osPrism+"&appointment_no="+appno+"&demographic_no="+demno+"",'anwin','width=400,height=300');

}
function hxOpen4()
{
        var id=document.getElementById("specs.id4").value;
        var type=document.getElementById("type004").value;  	
		var date=document.getElementById("gl_date4").value;
		
		var odSph=document.getElementById("odSph4").value;
		var odCyl=document.getElementById("odCyl4").value;
        var odAxis=document.getElementById("odAxis4").value;
		var odAdd=document.getElementById("odAdd4").value;
    	var odPrism=document.getElementById("odPrism4").value;
		
		var osSph=document.getElementById("osSph4").value;
		var osCyl=document.getElementById("osCyl4").value;
		var osAxis=document.getElementById("osAxis4").value;
        var osAdd=document.getElementById("osAdd4").value;
		var osPrism=document.getElementById("osPrism4").value;  
		
		var appno=document.getElementById("appointment_no").value; 
		var demno=document.getElementById("demographic_no").value; 
    	
 window.open("../eyeform/glassHX.jsp?specs.id="+id+"&type="+type+"&dateStr="+date+"&odSph="+odSph+"&odCyl="+odCyl+"&odAxis="+odAxis+"&odAdd="+odAdd+"&odPrism="+odPrism+"&osSph="+osSph+"&osCyl="+osCyl+"&osAxis="+osAxis+"&osAdd="+osAdd+"&osPrism="+osPrism+"&appointment_no="+appno+"&demographic_no="+demno+"",'anwin','width=400,height=300');

}

function hxForm_sumbit(){
	
	var num = Number(document.getElementById("shownum").value);

	var postData = "";
	
	if((num > 0)){
		for(var i = 1;i <= num; i++){
			if(postData.length > 0) {
				postData += "&";
				var name = jQuery("#type00" + i).attr("name");
				var value = jQuery("#type00" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}else{
				var name = jQuery("#type00" + i).attr("name");
				var value = jQuery("#type00" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#specs.id" + i).attr("class") != "examfieldwhite"){
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#specs.id" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;*/
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#specs.id" + i).attr("name");
				var value = jQuery("#specs.id" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#gl_date" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#gl_date" + i).attr("name");
				var value = null;
				var data = name + "=" + "null";
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#gl_date" + i).attr("name");
				var value = jQuery("#gl_date" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#odSph" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#odSph" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#odSph" + i).attr("name");
				var value = jQuery("#odSph" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#odCyl" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#odCyl" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#odCyl" + i).attr("name");
				var value = jQuery("#odCyl" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#odAxis" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#odAxis" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#odAxis" + i).attr("name");
				var value = jQuery("#odAxis" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#odAdd" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#odAdd" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#odAdd" + i).attr("name");
				var value = jQuery("#odAdd" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#odPrism" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#odPrism" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#odPrism" + i).attr("name");
				var value = jQuery("#odPrism" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#osSph" + i).attr("class") != "examfieldwhite") {
				/*if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#osSph" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#osSph" + i).attr("name");
				var value = jQuery("#osSph" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#osCyl" + i).attr("class") != "examfieldwhite") {
			/*	if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#osCyl" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#osCyl" + i).attr("name");
				var value = jQuery("#osCyl" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#osAxis" + i).attr("class") != "examfieldwhite") {
			/*	if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#osAxis" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#osAxis" + i).attr("name");
				var value = jQuery("#osAxis" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#osAdd" + i).attr("class") != "examfieldwhite") {
			/*	if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#osAdd" + i).attr("name");
				var value = null;
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#osAdd" + i).attr("name");
				var value = jQuery("#osAdd" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
			if(jQuery("#osPrism" + i).attr("class") != "examfieldwhite") {
			/*	if(postData.length > 0) {
					postData += "&";
				}
				var name = jQuery("#osPrism" + i).attr("name");
				var value = null
				var data = name + "=" + encodeURIComponent(value);
				postData += data;	*/	
			}else{
				if(postData.length > 0) {
					postData += "&";
				}
				sumbitform = true;
				var name = jQuery("#osPrism" + i).attr("name");
				var value = jQuery("#osPrism" + i).val();
				var data = name + "=" + encodeURIComponent(value);
				postData += data;
			}
		}
	}

	if(sumbitform){
		//document.getElementById('hxForm').submit();
		//alert(postData);
		var appointmentNo=document.getElementById("appointment_no").value; 
		var demographicNo=document.getElementById("demographic_no").value;
		//alert(ctx+'/eyeform/SpecsHistory.do?action=save&demographicNo='+demographicNo+'&appointmentNo='+appointmentNo);
		jQuery.ajax({type:'POST',url:ctx+'/eyeform/SpecsHistory.do?method=save&specs.demographicNo='+demographicNo+'&specs.appointmentNo='+appointmentNo,data:postData,success: function(){}});
	}
}
</script>

<style type="text/css">
.exam
{
	padding: 0px;
	border-spacing: 0px;
	margin: 0px;
	border: 0px;
}

.exam td
{
	text-align: center;
	vertical-align:middle;
	padding:1px !important;
	margin:0px;
	border: 0px;
	font-size: 9px;
}

td .label
{
	text-align: left;
}

.examfieldgrey
{
	background-color: #DDDDDD;
}

.examfieldwhite
{
	background-color: white;
}

.slidey { margin-bottom: 6px;font-size: 10pt;}
.slidey .title {
        margin-top: 1px;

        list-style-type: none;
       /* font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif; */

        overflow: hidden;
        background-color: #f6f6ff;
        text-align:left;
        padding: 0px;

         /*font-size: 1.0em;*/
        font-size: 9px;
        /*font-variant:small-caps;*/

        font-weight: bold;


       font-family: Verdana,Tahoma,Arial,sans-serif;
       font-style:normal;
       text-transform:none;
       text-decoration:none;
       letter-spacing:normal;
       word-spacing:0;
       line-height:15px;
       vertical-align:baseline;




  }

.section_title {
	color:black;
}

.slidey .title2 {
        margin-top: 1px;
        font-size:10pt;
        list-style-type: none;
        font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
        overflow: hidden;
        background-color: #ccccff;
        padding: 0px; }
/* the only noteworthy thing here is the overflow is hidden,
it's really a sleight-of-hand kind of thing, we're playing
with the height and that makes it 'slide' */
.slidey .slideblock { overflow: hidden; background-color:  #ccccff;  padding: 2px; font-size: 10pt; }

span.ge{
        margin-top: 1px;
        border-bottom: 1px solid #000;
        font-weight: bold;
        list-style-type: none;
        font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
        font-size: 12pt;
        overflow: hidden;
        background-color: #99bbee;
        padding: 0px;
        color: black;;
        width: 300px;
}


</style>
<span style="font-size:10px">
	<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
<!--	<a id="save_measurements" href="javascript:void(0)"  onclick="document.getElementById('hxForm').submit();">[Save Measurements]</a>-->
	<a id="save_measurements" href="javascript:void(0)"  onclick="hxForm_sumbit();">[Save Measurements]</a>
	<%}else{%>
	<a id="save_measurements" href="#">[Save Measurements]</a>
	<%}%>
</span>
<span style="float:right;font-size:10px">
	<a href="javascript:void(0);" onclick="expandAll();">[expand all sections]</a>&nbsp;
	<a href="javascript:void(0);" onclick="collapseAll();">[collapse all sections]</a>
</span>

<table border="0" width="100%">
<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
<% if (!glasses_hx) {%>
<tr>
<td>
<div class="slidey">
 <form action="<c:out value="${ctx}"/>/eyeform/SpecsHistory.do" method="post" name="specsHistoryForm" id="hxForm" target="ifFrame">
	<div class="title">
		<a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="31" id="a_20" onclick="togglediv(this);">Glasses Hx:</a>
		<span>&nbsp;&nbsp;</span>

             <a href="javascript:void(0)" tabindex="32" onclick="popupPage(500,900,'examinationhistory1','<c:out value="${ctx}"/>/eyeform/ExaminationHistory.do?demographicNo=<%=request.getParameter("demographic_no")%>&method=query&oldglasses=true&fromlist1=GLASSES HISTORY'); return false;">[old glasses]</a>
	          <span>&nbsp;&nbsp;</span>
              <a href="javascript:void(0)" onclick="insRow()">[add]</a>
			  
			  <span>&nbsp;&nbsp;</span>
			  
			  <a href="javascript:void(0)"  onclick="document.getElementById('hxForm').submit();" style="display:none">[save]</a>
	</div>
	
<div style="display:none">	 
	 <input type="hidden" name="specs.dateStr5"  id="gl_date5"  value="<%=value12%>"/>
	 <input type="hidden" name="specs.odSph5" id="odSph5"  value="<%=value13%>"/>
	 <input type="hidden" name="specs.odCyl5" id="odCyl5"  value="<%=value14%>"/>
	 <input type="hidden" name="specs.odAxis5" id="odAxis5"  value="<%=value15%>"/>
	 <input type="hidden" name="specs.odAdd5" id="odAdd5"  value="<%=value16%>"/>
	 <input type="hidden" name="specs.odPrism5" id="odPrism5"  value="<%=value17%>"/>		 
	 <input type="hidden" name="specs.osSph5" id="osSph5"  value="<%=value18%>"/>
	 <input type="hidden" name="specs.osCyl5" id="osCyl5"  value="<%=value19%>"/>
	 <input type="hidden" name="specs.osAxis5"id="osAxis5"  value="<%=value20%>"/>
	 <input type="hidden" name="specs.osAdd5" id="osAdd5"  value="<%=value21%>"/>
	 <input type="hidden" name="specs.osPrism5" id="osPrism5"  value="<%=value22%>"/>


	 <input type="hidden" name="specs.dateStr6"  id="gl_date6"  value="<%=value23%>"/>	
	 <input type="hidden" name="specs.odSph6" id="odSph6"  value="<%=value24%>"/>
	 <input type="hidden"  name="specs.odCyl6" id="odCyl6"  value="<%=value25%>"/>
	 <input type="hidden" name="specs.odAxis6" id="odAxis6"  value="<%=value26%>"/>
	 <input type="hidden" name="specs.odAdd6" id="odAdd6"  value="<%=value27%>"/>
	 <input type="hidden" name="specs.odPrism6" id="odPrism6"  value="<%=value28%>"/>		
	 <input type="hidden" name="specs.osSph6" id="osSph6"  value="<%=value29%>"/>
	 <input type="hidden" name="specs.osCyl6" id="osCyl6"  value="<%=value30%>"/>
	 <input type="hidden" name="specs.osAxis6"id="osAxis6"  value="<%=value31%>"/>
	 <input type="hidden" name="specs.osAdd6" id="osAdd6"  value="<%=value32%>"/>
	 <input type="hidden" name="specs.osPrism6" id="osPrism6"  value="<%=value33%>"/>
	 
	 
	 <input type="hidden" name="specs.dateStr7"  id="gl_date7"  value="<%=value34%>"/>			
	 <input type="hidden" name="specs.odSph7" id="odSph7"  value="<%=value35%>"/>
	 <input type="hidden" name="specs.odCyl7" id="odCyl7"  value="<%=value36%>"/>
	 <input type="hidden" name="specs.odAxis7" id="odAxis7"  value="<%=value37%>"/>
	 <input type="hidden" name="specs.odAdd7" id="odAdd7"  value="<%=value38%>"/>
	 <input type="hidden" name="specs.odPrism7" id="odPrism7"  value="<%=value39%>"/>		
	 <input type="hidden" name="specs.osSph7" id="osSph7"  value="<%=value40%>"/>
	 <input type="hidden" name="specs.osCyl7" id="osCyl7"  value="<%=value41%>"/>
	 <input type="hidden" name="specs.osAxis7"id="osAxis7"  value="<%=value42%>"/>
	 <input type="hidden" name="specs.osAdd7" id="osAdd7"  value="<%=value43%>"/>
	 <input type="hidden" name="specs.osPrism7" id="osPrism7"  value="<%=value44%>"/>

	 <input type="hidden" name="specs.dateStr8"  id="gl_date8" value="<%=value1%>"/>			
	 <input type="hidden" name="specs.odSph8" id="odSph8"  value="<%=value2%>"/>
	 <input type="hidden" name="specs.odCyl8" id="odCyl8"  value="<%=value3%>"/>
	 <input type="hidden" name="specs.odAxis8" id="odAxis8"  value="<%=value4%>"/>
	 <input type="hidden" name="specs.odAdd8" id="odAdd8"  value="<%=value5%>"/>
	 <input type="hidden" name="specs.odPrism8" id="odPrism8"  value="<%=value6%>"/>	
	 <input type="hidden" name="specs.osSph8" id="osSph8"  value="<%=value7%>"/>
	 <input type="hidden" name="specs.osCyl8" id="osCyl8"  value="<%=value8%>"/>
	 <input type="hidden" name="specs.osAxis8"id="osAxis8"  value="<%=value9%>"/>
	 <input type="hidden" name="specs.osAdd8" id="osAdd8"  value="<%=value10%>"/>
	 <input type="hidden" name="specs.osPrism8" id="osPrism8"  value="<%=value11%>"/>
	 <input type="hidden" id="shownum" value="<%=num%>" />
	 
	 <input type="hidden" value="save" name="method">
	 <input type="hidden" value="<%=request.getParameter("demographic_no")%>" name="specs.demographicNo" id="demographic_no">
     <input type="hidden" value="<%= request.getParameter("appointment_no")%>" name="specs.appointmentNo" id="appointment_no">
	 
	 <input type="hidden" value="<%=bg_status[0]%>" name="bg_status1" id="bg_status1">
	 <input type="hidden" value="<%=bg_status[1]%>" name="bg_status2" id="bg_status2">
	 <input type="hidden" value="<%=bg_status[2]%>" name="bg_status3" id="bg_status3">
	 <input type="hidden" value="<%=bg_status[3]%>" name="bg_status4" id="bg_status4">
	 <input type="hidden" value="<%=bg_status[4]%>" name="bg_status5" id="bg_status5">
	 <input type="hidden" value="<%=bg_status[5]%>" name="bg_status6" id="bg_status6">
	 <input type="hidden" value="<%=bg_status[6]%>" name="bg_status7" id="bg_status7">
	 <input type="hidden" value="<%=bg_status[7]%>" name="bg_status8" id="bg_status8">
	 <input type="hidden" value="<%=bg_status[8]%>" name="bg_status9" id="bg_status9">
	 <input type="hidden" value="<%=bg_status[9]%>" name="bg_status10" id="bg_status10">
	 <input type="hidden" value="<%=bg_status[10]%>" name="bg_status11" id="bg_status11">
	 <input type="hidden" value="<%=bg_status[11]%>" name="bg_status12" id="bg_status12">
	 <input type="hidden" value="<%=bg_status[12]%>" name="bg_status13" id="bg_status13">
	 <input type="hidden" value="<%=bg_status[13]%>" name="bg_status14" id="bg_status14">
	 <input type="hidden" value="<%=bg_status[14]%>" name="bg_status15" id="bg_status15">
	 <input type="hidden" value="<%=bg_status[15]%>" name="bg_status16" id="bg_status16">
	 <input type="hidden" value="<%=bg_status[16]%>" name="bg_status17" id="bg_status17">
	 <input type="hidden" value="<%=bg_status[17]%>" name="bg_status18" id="bg_status18">
	 <input type="hidden" value="<%=bg_status[18]%>" name="bg_status19" id="bg_status19">
	 <input type="hidden" value="<%=bg_status[19]%>" name="bg_status20" id="bg_status20">
	 <input type="hidden" value="<%=bg_status[20]%>" name="bg_status21" id="bg_status21">
	 <input type="hidden" value="<%=bg_status[21]%>" name="bg_status22" id="bg_status22">
	 <input type="hidden" value="<%=bg_status[22]%>" name="bg_status23" id="bg_status23">
	 <input type="hidden" value="<%=bg_status[23]%>" name="bg_status24" id="bg_status24">
	 <input type="hidden" value="<%=bg_status[24]%>" name="bg_status25" id="bg_status25">
	 <input type="hidden" value="<%=bg_status[25]%>" name="bg_status26" id="bg_status26">
	 <input type="hidden" value="<%=bg_status[26]%>" name="bg_status27" id="bg_status27">
	 <input type="hidden" value="<%=bg_status[27]%>" name="bg_status28" id="bg_status28">
	 <input type="hidden" value="<%=bg_status[28]%>" name="bg_status29" id="bg_status29">
	 <input type="hidden" value="<%=bg_status[29]%>" name="bg_status30" id="bg_status30">
	 <input type="hidden" value="<%=bg_status[30]%>" name="bg_status31" id="bg_status31">
	 <input type="hidden" value="<%=bg_status[31]%>" name="bg_status32" id="bg_status32">
	 <input type="hidden" value="<%=bg_status[32]%>" name="bg_status33" id="bg_status33">
	 <input type="hidden" value="<%=bg_status[33]%>" name="bg_status34" id="bg_status34">
	 <input type="hidden" value="<%=bg_status[34]%>" name="bg_status35" id="bg_status35">
	 <input type="hidden" value="<%=bg_status[35]%>" name="bg_status36" id="bg_status36">
	 <input type="hidden" value="<%=bg_status[36]%>" name="bg_status37" id="bg_status37">
	 <input type="hidden" value="<%=bg_status[37]%>" name="bg_status38" id="bg_status38">
	 <input type="hidden" value="<%=bg_status[38]%>" name="bg_status39" id="bg_status39">
	 <input type="hidden" value="<%=bg_status[39]%>" name="bg_status40" id="bg_status40">
	 <input type="hidden" value="<%=bg_status[40]%>" name="bg_status41" id="bg_status41">
	 <input type="hidden" value="<%=bg_status[41]%>" name="bg_status42" id="bg_status42">
	 <input type="hidden" value="<%=bg_status[42]%>" name="bg_status43" id="bg_status43">
	 <input type="hidden" value="<%=bg_status[43]%>" name="bg_status44" id="bg_status44">
</div>	
			
	
	<div id="s_20"  <%=glass_show%>>
	
		<table class="exam" width="100%" id="myTable">
		 
		<tr>
			<td width="10%"><a href="javascript:void(0)" tabindex="33" onclick="copyglasses();return false;">[copy]</a></td>
			
			<td colspan="5" width="36%">OD</td>
			<td width="4%"></td>
			<td colspan="5"width="36%">OS</td>
			<td width="8%"></td>
			<td width="1%"></td>
			<td width="1%"></td>
		</tr>
		<tr>
		    <td >Type</td>
			
			<td>s</td>
			<td>c</td>
			<td>x</td>
			<td>add</td>
			<td>prism</td>
			<td width="4%"></td>
			<td>s</td>
			<td>c</td>
			<td>x</td>
			<td>add</td>
			<td>prism</td>
			
			<td>date</td>				
			<td width="8%"></td>
			
			<td></td>
		</tr>
		<%if(num == 1){
		num1 ++;%>
			<tr>
			  <td>
	             <select name="specs.type<%=num1%>" style="width:100%" id="type00<%=num1%>"  onchange="demo<%=num1%>()">
	   	            <option value="distance">distance</option>
	            	<option value="bifocal">bifocal</option>
	            	<option value="invis bfocal">invis bfocal</option>
	                <option value="reading">reading</option>
	            </select>
	         </td>
             
			 </td>
			 <td><input name="specs.odSph<%=num1%>" id="odSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value2%>"/></td>
			 <td><input name="specs.odCyl<%=num1%>" id="odCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value3%>"/></td>
			 <td><input name="specs.odAxis<%=num1%>" id="odAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value4%>"/></td>
			 <td><input name="specs.odAdd<%=num1%>" id="odAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value5%>"/></td>
			 <td><input name="specs.odPrism<%=num1%>" id="odPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value6%>"/></td>		
			 <td width="4%"></td>
			 <td><input name="specs.osSph<%=num1%>" id="osSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value7%>"/></td>
			 <td><input name="specs.osCyl<%=num1%>" id="osCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value8%>"/></td>
			 <td><input name="specs.osAxis<%=num1%>"id="osAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value9%>"/></td>
			 <td><input name="specs.osAdd<%=num1%>" id="osAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value10%>"/></td>
			 <td><input name="specs.osPrism<%=num1%>" id="osPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value11%>"/></td>
			 <td><input name="specs.dateStr<%=num1%>"  type="text" tabindex="<%=num_tab ++%>" class="examfieldgrey" style="width:115%"  id="gl_date<%=num1%>" onfocus="whiteField(this);" value="<%=value1%>"/>		 </td>			 
			 <td width="8%"><img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal<%=num1%>">
			 <script type="text/javascript">
				Calendar.setup({ inputField : "gl_date<%=num1%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal<%=num1%>", singleClick : true, step : 1 });
			</script>
			 <td><a href="javascript:void(0)" onclick="hxOpen<%=num1%>();"><img src="../images/notes.gif"  /> </a></td>
             <td><input type="hidden" value="" name="specs.id<%=num1%>" id="specs.id<%=num1%>"></td>		 
		</tr>
		<%}else{%>
		<%
			if(specs.size() > 0){
			num1 ++;
		%>
		<tr>
			  <td>
	             <select name="specs.type<%=num1%>" style="width:100%" id="type00<%=num1%>"  onchange="demo<%=num1%>()">
	   	            <option value="distance">distance</option>
	            	<option value="bifocal">bifocal</option>
	            	<option value="invis bfocal">invis bfocal</option>
	                <option value="reading">reading</option>
	            </select>
	         </td>
             
			 <td><input name="specs.odSph<%=num1%>" id="odSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value2%>"/></td>
			 <td><input name="specs.odCyl<%=num1%>" id="odCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value3%>"/></td>
			 <td><input name="specs.odAxis<%=num1%>" id="odAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value4%>"/></td>
			 <td><input name="specs.odAdd<%=num1%>" id="odAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value5%>"/></td>
			 <td><input name="specs.odPrism<%=num1%>" id="odPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value6%>"/></td>		
			 <td width="4%"></td>
			 <td><input name="specs.osSph<%=num1%>" id="osSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value7%>"/></td>
			 <td><input name="specs.osCyl<%=num1%>" id="osCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value8%>"/></td>
			 <td><input name="specs.osAxis<%=num1%>"id="osAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value9%>"/></td>
			 <td><input name="specs.osAdd<%=num1%>" id="osAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value10%>"/></td>
			 <td><input name="specs.osPrism<%=num1%>" id="osPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value11%>"/></td>
			 <td><input name="specs.dateStr<%=num1%>"  type="text" tabindex="<%=num_tab ++%>" class="examfieldgrey" style="width:115%"  id="gl_date<%=num1%>" onfocus="whiteField(this);" value="<%=value1%>"/>		 </td>			 
			 <td width="8%"><img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal<%=num1%>">
			 <script type="text/javascript">
				Calendar.setup({ inputField : "gl_date<%=num1%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal<%=num1%>", singleClick : true, step : 1 });
			</script>
			 </td>
			 <td><a href="javascript:void(0)" onclick="hxOpen<%=num1%>();"><img src="../images/notes.gif"  /> </a></td>
             <td><input type="hidden" value="" name="specs.id<%=num1%>" id="specs.id<%=num1%>"></td>		 
		</tr>
		<%}%>
		<%if(specs1.size() > 0){
			num1 ++;
		%>
		<tr>
			  <td>
	             <select name="specs.type<%=num1%>" style="width:100%" id="type00<%=num1%>"  onchange="demo<%=num1%>()">
	   	            <option value="distance">distance</option>
	            	<option value="bifocal" selected>bifocal</option>
	            	<option value="invis bfocal">invis bfocal</option>
	                <option value="reading">reading</option>
	            </select>
	         </td>
             
			 <td><input name="specs.odSph<%=num1%>" id="odSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value13%>"/></td>
			 <td><input name="specs.odCyl<%=num1%>" id="odCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value14%>"/></td>
			 <td><input name="specs.odAxis<%=num1%>" id="odAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value15%>"/></td>
			 <td><input name="specs.odAdd<%=num1%>" id="odAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value16%>"/></td>
			 <td><input name="specs.odPrism<%=num1%>" id="odPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value17%>"/></td>		
			 <td width="4%"></td>
			 <td><input name="specs.osSph<%=num1%>" id="osSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value18%>"/></td>
			 <td><input name="specs.osCyl<%=num1%>" id="osCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value19%>"/></td>
			 <td><input name="specs.osAxis<%=num1%>"id="osAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value20%>"/></td>
			 <td><input name="specs.osAdd<%=num1%>" id="osAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value21%>"/></td>
			 <td><input name="specs.osPrism<%=num1%>" id="osPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value22%>"/></td>
			 <td><input name="specs.dateStr<%=num1%>"  type="text" tabindex="<%=num_tab ++%>" class="examfieldgrey" style="width:115%"  id="gl_date<%=num1%>" onfocus="whiteField(this);" value="<%=value12%>"/>		 </td>			 
			 <td width="8%"><img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal<%=num1%>">
			 <script type="text/javascript">
				Calendar.setup({ inputField : "gl_date<%=num1%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal<%=num1%>", singleClick : true, step : 1 });
			</script>
			 </td>
			 <td><a href="javascript:void(0)" onclick="hxOpen<%=num1%>();"><img src="../images/notes.gif"  /> </a></td>
             <td><input type="hidden" value="" name="specs.id<%=num1%>" id="specs.id<%=num1%>"></td>	
		</tr>
		<%}%>
		<%if(specs2.size() > 0){
			num1 ++;
		%>
		<tr>
			  <td>
	             <select name="specs.type<%=num1%>" style="width:100%" id="type00<%=num1%>"  onchange="demo<%=num1%>()">
	   	            <option value="distance">distance</option>
	            	<option value="bifocal">bifocal</option>
	            	<option value="invis bfocal" selected>invis bfocal</option>
	                <option value="reading">reading</option>
	            </select>
	         </td>
             
			 <td><input name="specs.odSph<%=num1%>" id="odSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value24%>"/></td>
			 <td><input name="specs.odCyl<%=num1%>" id="odCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value25%>"/></td>
			 <td><input name="specs.odAxis<%=num1%>" id="odAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value26%>"/></td>
			 <td><input name="specs.odAdd<%=num1%>" id="odAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value27%>"/></td>
			 <td><input name="specs.odPrism<%=num1%>" id="odPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value28%>"/></td>		
			 <td width="4%"></td>
			 <td><input name="specs.osSph<%=num1%>" id="osSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value29%>"/></td>
			 <td><input name="specs.osCyl<%=num1%>" id="osCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value30%>"/></td>
			 <td><input name="specs.osAxis<%=num1%>"id="osAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value31%>"/></td>
			 <td><input name="specs.osAdd<%=num1%>" id="osAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"   onfocus="whiteField(this);" value="<%=value32%>"/></td>
			 <td><input name="specs.osPrism<%=num1%>" id="osPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:95%"  onfocus="whiteField(this);" value="<%=value33%>"/></td>
			 <td><input name="specs.dateStr<%=num1%>"  type="text" tabindex="<%=num_tab ++%>" class="examfieldgrey" style="width:115%"  id="gl_date<%=num1%>" onfocus="whiteField(this);" value="<%=value23%>"/>		 </td>			 
			 <td width="8%"><img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal<%=num1%>">
			 <script type="text/javascript">
				Calendar.setup({ inputField : "gl_date<%=num1%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal<%=num1%>", singleClick : true, step : 1 });
			</script>
			 </td>
			 <td><a href="javascript:void(0)" onclick="hxOpen<%=num1%>();"><img src="../images/notes.gif"  /> </a></td>
             <td><input type="hidden" value="" name="specs.id<%=num1%>" id="specs.id<%=num1%>"></td>	
		</tr>
		<%}%>
		<%if(specs3.size() > 0){
			num1 ++;
		%>
		<tr>
			  <td>
	             <select name="specs.type<%=num1%>" style="width:100%" id="type00<%=num1%>"  onchange="demo<%=num1%>()">
	   	            <option value="distance">distance</option>
	            	<option value="bifocal">bifocal</option>
	            	<option value="invis bfocal">invis bfocal</option>
	                <option value="reading" selected>reading</option>
	            </select>
	         </td>
             
			 <td><input name="specs.odSph<%=num1%>" id="odSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value35%>"/></td>
			 <td><input name="specs.odCyl<%=num1%>" id="odCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value36%>"/></td>
			 <td><input name="specs.odAxis<%=num1%>" id="odAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"   onfocus="whiteField(this);" value="<%=value37%>"/></td>
			 <td><input name="specs.odAdd<%=num1%>" id="odAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value38%>"/></td>
			 <td><input name="specs.odPrism<%=num1%>" id="odPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value39%>"/></td>		
			 <td width="4%"></td>
			 <td><input name="specs.osSph<%=num1%>" id="osSph<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value40%>"/></td>
			 <td><input name="specs.osCyl<%=num1%>" id="osCyl<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value41%>"/></td>
			 <td><input name="specs.osAxis<%=num1%>"id="osAxis<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value42%>"/></td>
			 <td><input name="specs.osAdd<%=num1%>" id="osAdd<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"   onfocus="whiteField(this);" value="<%=value43%>"/></td>
			 <td><input name="specs.osPrism<%=num1%>" id="osPrism<%=num1%>" type="text" tabindex="<%=num_tab ++%>" maxlength="6" class="examfieldgrey" style="width:99%"  onfocus="whiteField(this);" value="<%=value44%>"/></td>
			 <td><input name="specs.dateStr<%=num1%>"  type="text" tabindex="<%=num_tab ++%>" class="examfieldgrey" style="width:115%"  id="gl_date<%=num1%>" onfocus="whiteField(this);" value="<%=value34%>"/>		 </td>			 
			 <td width="8%"><img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal<%=num1%>">
			 <script type="text/javascript">
				Calendar.setup({ inputField : "gl_date<%=num1%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal<%=num1%>", singleClick : true, step : 1 });
			</script>
			 </td>
			 <td><a href="javascript:void(0)" onclick="hxOpen<%=num1%>();"><img src="../images/notes.gif"  /> </a></td>
             <td><input type="hidden" value="" name="specs.id<%=num1%>" id="specs.id<%=num1%>"></td>	
		</tr>
		<%}}%>
		</table>
		
	</div>
	
	</form>
	<iframe style="display:none" id="ifFrame" name="ifFrame" src="about:blank">
	</iframe>
</div>
</td>
</tr>
<%}%>
<% if (!vision_assessment){%>
<tr>
<td>
<div class="slidey">
	 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="77" id="a_22" onclick="togglediv(this);">Vision Assessment:</a>
            <!--
            <span>&nbsp;&nbsp;</span>

            
            <a href="javascript:void(0)" tabindex="357" onclick="setPretest();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="358" onclick="clearPretest();return false;">[clear]</a>
            -->
        </div>
        <div id="s_22" class="slideblock">
            <table class="exam" width="100%">
            	<tr>
            		<td width="8%"></td>
            		<td width="24%" colspan="3">OD</td>
					<td width="8%"></td>
            		<td width="24%" colspan="3">OS</td>
					<td width="8%"></td>
            		<td width="24%" colspan="3">OU</td>
					<td width="8%"></td>
            	</tr>
            	<tr>
            		<td width="8%"></td>
            		<td width="8%">sc</td>
            		<td width="8%">cc</td>
            		<td width="8%">ph</td>
					<td width="8%"></td>
            		<td width="8%">sc</td>
            		<td width="8%">cc</td>
            		<td width="8%">ph</td>
            		<td width="8%"></td>
					<td width="8%"></td>
            		<td width="8%">sc</td>
            		<td width="8%">cc</td>
					<td width="8%"></td>
            	</tr>
            	<tr>
            		<td class="label">Dist</td>
            		<td><input type="text" tabindex="78" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rdsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="79" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_rdcc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="80" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_rph" onfocus="whiteField(this);"/></td>
					<td></td>
            		<td><input type="text" tabindex="81" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_ldsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="82" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_ldcc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="83" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_lph" onfocus="whiteField(this);"/></td>
					<td></td>
            		<td colspan="2">Dist <input type="text" tabindex="84" maxlength="8" class="examfieldgrey" style="width:50%" measurement="v_dsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="85" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_dcc" onfocus="whiteField(this);"/></td>
					<td></td>
            	</tr>
            	<tr>
            		<td class="label">Int</td>
            		<td><input type="text" tabindex="86" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_risc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="87" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_ricc" onfocus="whiteField(this);"/></td>
            		<td></td>
					<td></td>
            		<td><input type="text" tabindex="88" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_lisc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="89" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_licc" onfocus="whiteField(this);"/></td>
            		<td></td>
					<td></td>
            		<td colspan="2">Int &nbsp;&nbsp;<input type="text" tabindex="90" maxlength="8" class="examfieldgrey" style="width:50%" measurement="v_isc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="91" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_icc" onfocus="whiteField(this);"/></td>
					<td></td>
            	</tr>
            	<tr>
            		<td class="label">Near</td>
            		<td><input type="text" tabindex="92" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_rnsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="93" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_rncc" onfocus="whiteField(this);"/></td>
            		<td></td>
					<td></td>
            		<td><input type="text" tabindex="94" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_lnsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="95" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_lncc" onfocus="whiteField(this);"/></td>
            		<td></td>
					<td></td>
            		<td colspan="2">Near <input type="text" tabindex="96" maxlength="8" class="examfieldgrey" style="width:50%" measurement="v_nsc" onfocus="whiteField(this);"/></td>
            		<td><input type="text" tabindex="97" maxlength="8" class="examfieldgrey" style="width:94%" measurement="v_ncc" onfocus="whiteField(this);"/></td>
					<td></td>
            	</tr>
            </table>
        </div>
</div>
</td>
</tr>
<%}%>
<% if (!vision_measurement) {%>
<tr>
<td>
<div class="slidey">
	<div class="title">
        <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="98" href="javascript:void(0)" id="a_23" onclick="togglediv(this);">Vision Measurement:</a>
        <span>&nbsp;&nbsp;</span>
		<!--
    	<a href="javascript:void(0)" tabindex="66" onclick="clearPretest();return false;">[PRINT Rx]</a>   
		-->
    </div>
    <div id="s_23" class="slideblock">
    	<table class="exam" width="100%">
    		<tr>
    			<td width="8%"></td>
    			<td colspan="4" width="32%">OD
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="99" onclick="clearMeasurement_od();return false;">[clear]</a>
    			</td>
				<td width="6%"></td>
    			<td colspan="4" width="32%">OS
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="100" onclick="clearMeasurement_os();return false;">[clear]</a>
    			</td>
				<td width="6%"></td>
    			<td colspan="2" width="16%">OU</td>
    		</tr>
    		<tr>
    			<td></td>
    			<td>K1</td>
    			<td>K2</td>
    			<td>K2 axis</td>
    			<td rowspan="2"></td>
				<td></td>
    			<td>K1</td>
    			<td>K2</td>
    			<td>K2 axis</td>
    			<td rowspan="2"></td>
				<td></td>
    			<td colspan="2"></td>
    		</tr>
    		<tr>
    			<td>K</td>
    			<td><input type="text"  tabindex="101" maxlength="8" class="examfieldgrey" style="width:99%"  measurement="v_rk1" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="102" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rk2" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="103" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rkx" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td><input type="text"  tabindex="104" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lk1" onfocus="whiteField(this);"/></td>				
    			<td><input type="text"  tabindex="105" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lk2" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="106" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lkx" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td>Fly</td>
    			<td><input type="text"  tabindex="142" maxlength="8" class="examfieldgrey" style="width:80%" measurement="v_fly" onfocus="whiteField(this);"/></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td>S</td>
    			<td>C</td>
    			<td>x</td>
    			<td>VA</td>
				<td></td>
    			<td>S</td>
    			<td>C</td>
    			<td>x</td>
    			<td>VA</td>
				<td></td>
    			<td>Stereo</td>
    			<td><input type="text"  tabindex="143" maxlength="8" class="examfieldgrey" style="width:80%" measurement="v_stereo" onfocus="whiteField(this);"/></td>
    		</tr>
    		<tr>
    			<td>AR
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="115" onclick="copyar();return false;">[copy]</a>
    			</td>
    			<td><input type="text"  tabindex="107" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rs" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="108" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="109" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="110" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rar" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td><input type="text"  tabindex="111" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_ls" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="112" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="113" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="114" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lar" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td colspan="2"></td>
    		</tr>
    		<tr>
    			<td>M (dist)
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="115" onclick="paste_mdist();return false;">[paste]</a>
    			</td>
    			<td><input type="text"  tabindex="116" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rds" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="117" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rdc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="118" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rdx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="119" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rdv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td><input type="text"  tabindex="120" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lds" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="121" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_ldc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="122" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_ldx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="123" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_ldv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td>Dist</td>
    			<td><input type="text"  tabindex="144" maxlength="8" class="examfieldgrey" style="width:80%" measurement="v_dv" onfocus="whiteField(this);"/></td>
    		</tr>
			<tr>
    			<td>Cyclo
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="124" onclick="paste_Cyclo();return false;">[paste]</a>
    			</td>
    			<td><input type="text"  tabindex="125" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rcs" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="126" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rcc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="127" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rcx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="128" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rcv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td><input type="text"  tabindex="129" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lcs" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="130" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lcc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="131" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lcx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="132" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lcv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td colspan="2"></td>
    		</tr>
    		<tr>
    			<td>M (near)
    				<span>&nbsp;&nbsp;</span>
    				<a href="javascript:void(0)" tabindex="133" onclick="paste_mnear();return false;">[paste]</a>
    			</td>
    			<td><input type="text"  tabindex="134" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rns" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="135" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rnc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="136" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rnx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="137" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_rnv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td><input type="text"  tabindex="138" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lns" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="139" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lnc" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="140" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lnx" onfocus="whiteField(this);"/></td>
    			<td><input type="text"  tabindex="141" maxlength="8" class="examfieldgrey" style="width:99%" measurement="v_lnv" onfocus="whiteField(this);"/></td>
				<td></td>
    			<td>Near</td>
    			<td><input type="text"  tabindex="145" maxlength="8" class="examfieldgrey" style="width:80%" measurement="v_nv" onfocus="whiteField(this);"/></td>
    		</tr>
    		
    	</table>
    </div>
</div>
</td>
</tr>
<%}%>
<% if (!refractive) {%>
<tr>
<td>
<div class="slidey">
	 <div class="title">
		<a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="146" href="javascript:void(0)" id="a_24" onclick="togglediv(this);">Refractive:</a>
		
	</div>
	<div id="s_24" class="slideblock">       	
		<table class="exam" width="100%">
			<tr>
				<td width="8%"></td>
				<td width="8%">OD</td>
				<td width="8%"></td>
				<td width="8%">OS</td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
				<td width="8%"></td>
			</tr>
			<tr>
				<td class="label">Dominance</td>				
				<td><input type="text" tabindex="147" maxlength="10" style="width:100%" measurement="ref_rdom" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td><input type="text" tabindex="148" maxlength="10" style="width:100%" measurement="ref_ldom" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td class="label">Pupil dim</td>
				<td><input type="text" tabindex="149" maxlength="10" style="width:100%" measurement="ref_rpdim" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td><input type="text" tabindex="150" maxlength="10" style="width:100%" measurement="ref_lpdim" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td class="label">Kappa</td>
				<td><input type="text" tabindex="151" maxlength="10" style="width:100%" measurement="ref_rkappa" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td><input type="text" tabindex="152" maxlength="10" style="width:100%" measurement="ref_lkappa" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td class="label">W2W</td>
				<td><input type="text" tabindex="153" maxlength="10" style="width:100%" measurement="ref_rw2w" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td><input type="text" tabindex="154" maxlength="10" style="width:100%" measurement="ref_lw2w" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</div>
</div>
</td>
</tr>
<%}%>
<% if (!iop) {%>
<tr>
<td>
<div class="slidey">
	<div class="title">
		<a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="155" id="a_25" onclick="togglediv(this);">IOP:</a>

	</div>
	<div id="s_25" class="slideblock">
	<table class="exam" width="100%">
		<tr>
				<td width="8%"></td>
				<td width="44%">OD</td>
				<td width="2%"></td>
				<td width="44%">OS</td>
				<td width="2%"></td>
				
			</tr>
			<tr>
				<td width="8%" nowrap="nowrap" class="label">NCT(<span id="nct_ts"></span>)</td>
				<td width="44%"><input type="text" tabindex="156" maxlength="50" style="width:100%" measurement="iop_rn" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				<td width="44%"><input type="text" tabindex="157" maxlength="50" style="width:100%" measurement="iop_ln" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				
			</tr>
			<tr>
				<td width="8%" class="label">Applanation(<span id="applanation_ts"></span>)</td>
				<td width="44%"><input type="text" tabindex="158" maxlength="50" style="width:100%" measurement="iop_ra" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				<td width="44%"><input type="text" tabindex="159" maxlength="50" style="width:100%" measurement="iop_la" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				
			</tr>
			<tr>
				<td width="8%" class="label">CCT</td>
				<td width="44%"><input type="text" tabindex="160" maxlength="50" style="width:100%" measurement="cct_r" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				<td width="44%"><input type="text" tabindex="161" maxlength="50" style="width:100%" measurement="cct_l" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="2%"></td>
				
			</tr>
	</table>
	</div>
</div>
</td>
</tr>
<%}%>
<% if (!other_exam) {%>
<tr>
<td>
<div class="slidey">
	<div class="title">
		<a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="162" href="javascript:void(0)" id="a_26" onclick="togglediv(this);">Other Exam:</a>

	</div>
	<div id="s_26" class="slideblock">
	<table class="exam" width="100%">
		<tr>
			<td width="8%"></td>
			<td width="24%" colspan="3">OD</td>
			<td width="4%"></td>
			<td width="24%" colspan="3">OS</td>
			<td width="8%"></td>
			<td width="32%" colspan="4">OU</td>
			<td width="4%"></td>
		</tr>
		<tr>
			<td class="label">Colour vision</td>
			<td colspan="3"><input type="text" tabindex="163" maxlength="50" style="width:98%" measurement="o_rcolour" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td width="4%"></td>
			<td colspan="3"><input type="text" tabindex="164" maxlength="50" style="width:98%" measurement="o_lcolour" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td></td>
			<td colspan="4">Maddox &nbsp; &nbsp;<input type="text" tabindex="173" maxlength="50" style="width:71%" measurement="o_mad" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td></td>
		</tr>
		<tr>
			<td class="label">Pupil</td>
			<td colspan="3"><input type="text" tabindex="165" maxlength="50" style="width:98%" measurement="o_rpupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td width="4%"></td>
			<td colspan="3"><input type="text" tabindex="166" maxlength="50" style="width:98%" measurement="o_lpupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
			<td colspan="4">Bagolini &nbsp; &nbsp;<input type="text" tabindex="174" maxlength="50" style="width:71%" measurement="o_bag" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
		</tr>
		<tr>
			<td class="label">Amsler grid</td>
			<td colspan="3"><input type="text" tabindex="167" maxlength="50" style="width:98%" maxlength="50" measurement="o_ramsler" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td width="4%"></td>
			<td colspan="3"><input type="text" tabindex="168" maxlength="50" style="width:98%" measurement="o_lamsler" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
			<td colspan="4">W4D(dist)&nbsp;<input type="text" tabindex="175" maxlength="50" style="width:71%" maxlength="50" measurement="o_w4dd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
		</tr>
		<tr>
			<td class="label">PAM</td>
			<td colspan="3"><input type="text" tabindex="169" maxlength="50" style="width:98%" measurement="o_rpam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td width="4%"></td>
			<td colspan="3"><input type="text" tabindex="170" maxlength="50" style="width:98%" measurement="o_lpam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
			<td colspan="4">W4D(near)<input type="text" tabindex="176" maxlength="50" style="width:71%" measurement="o_w4dn" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
		</tr>
		<tr>
			<td class="label">Confrontation</td>
			<td colspan="3"><input type="text" tabindex="171" maxlength="50" style="width:98%" measurement="o_rconf" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td width="4%"></td>
			<td colspan="3"><input type="text" tabindex="172" maxlength="50" style="width:98%" measurement="o_lconf" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
			<td ></td>
			<td colspan="4"></td>
			<td ></td>
		</tr>
	</table>
	</div>
</div>
</td>
</tr>
<%}%>

<% if (!anterior_segment) {%>
<tr>
<td>

	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="173" href="javascript:void(0)" id="a_32" onclick="togglediv(this);">Anterior Segment:</a>
			
        </div>
        <div id="s_32" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        		<td width="8%"></td>
        		<td width="46%">OD
        			<a href="javascript:void(0)" tabindex="174" onclick="setAnterior_od();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="175" onclick="clearAnterior_od();return false;">[clear]</a>
        		</td>
        		<td width="46%">OS
        			<a href="javascript:void(0)" tabindex="176" onclick="setAnterior_os();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="177" onclick="clearAnterior_os();return false;">[clear]</a>
        		</td>
        	</tr>
        	<tr>
        		<td class="label">Cornea</td>
        		<td><input type="text" tabindex="178" style="width:93%" measurement="a_rk" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="179" style="width:93%" measurement="a_lk" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Conj/Sclera</td>
        		<td><input type="text" tabindex="180" style="width:93%" measurement="a_rconj" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="181" style="width:93%" measurement="a_lconj" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">AC</td>
        		<td><input type="text" tabindex="182" style="width:93%" measurement="a_rac" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="183" style="width:93%" measurement="a_lac" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
				<td class="label">Angle</td>
				<td>
        			<table class="exam" style="width:100%">
        				<tr>
        					<td width="100%" align="center"><input type="text" style="width:30%" tabindex="184" measurement="a_rangle_1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        				<tr>
        					<td style="text-align:center">
	        					<input style="width:30%" type="text" tabindex="185" measurement="a_rangle_4" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        						<input style="width:30%" type="text" tabindex="186" measurement="a_rangle_3" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input style="width:30%" type="text" tabindex="187" measurement="a_rangle_2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        					</td>
        				</tr>
        				<tr>
        					<td><input type="text" style="width:30%" tabindex="188" measurement="a_rangle_5" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        			</table>
        		</td>
        		
        		<td>
        			<table class="exam" style="width:100%">
        				<tr>
        					<td width="100%" align="center"><input style="width:30%" type="text" tabindex="189" measurement="a_langle_1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>

        				<tr>
        					<td style="text-align:center">
        						<input style="width:30%" type="text" tabindex="190" measurement="a_langle_4" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input style="width:30%" type="text" tabindex="191" measurement="a_langle_3" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input style="width:30%" type="text" tabindex="192" measurement="a_langle_2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        					</td>
        				</tr>

        				<tr>
        					<td><input type="text" style="width:30%" tabindex="193" measurement="a_langle_5" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        			</table>
        		</td>
        	</tr>
        	<tr>
        		<td class="label">Iris</td>
        		<td><input type="text" tabindex="194" style="width:93%" measurement="a_riris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="195" style="width:93%" measurement="a_liris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Lens</td>
        		<td><input type="text" tabindex="196" style="width:93%" measurement="a_rlens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="197" style="width:93%" measurement="a_llens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>
<%}%>

<% if (!posterior_segment) {%>
<tr>
<td>
	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="198" href="javascript:void(0)" id="a_33" onclick="togglediv(this);">Posterior Segment:</a>
			
        </div>
        <div id="s_33" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        		<td width="8%"></td>
        		<td width="46%">OD
        			<a href="javascript:void(0)" tabindex="199" onclick="setPosterior_od();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="200" onclick="clearPosterior_od();return false;">[clear]</a>
        		</td>
        		<td width="46%">OS
        			<a href="javascript:void(0)" tabindex="201" onclick="setPosterior_os();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="202" onclick="clearPosterior_os();return false;">[clear]</a>
        		</td>
        	</tr>
        	<tr>
        		<td class="label">Disc</td>
        		<td><input type="text" tabindex="203" style="width:93%" measurement="p_rdisc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="204" style="width:93%" measurement="p_ldisc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">C/D ratio</td>
        		<td><input type="text" tabindex="205" style="width:93%" measurement="p_rcd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="206" style="width:93%" measurement="p_lcd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Macula</td>
        		<td><input type="text" tabindex="207" style="width:93%" measurement="p_rmac" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="208" style="width:93%" measurement="p_lmac" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Retina</td>
        		<td><input type="text" tabindex="209" style="width:93%" measurement="p_rret" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="210" style="width:93%" measurement="p_lret" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Vitreous</td>
        		<td><input type="text" tabindex="211" style="width:93%" measurement="p_rvit" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="212" style="width:93%" measurement="p_lvit" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
     </div>
</td>
</tr>
<%}%>

<% if (!duction) {%>
<tr>
<td>
	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="213" href="javascript:void(0)" id="a_27" onclick="togglediv(this);">Duction/Diplopia:</a>
			
        </div>
        <div id="s_27" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
			<td width="4%"></td>
        	<td colspan="2" width="25%">OD
        		<a href="javascript:void(0)" tabindex="214" onclick="setDuc_od();return false;">[normal]</a>
            	<a href="javascript:void(0)" tabindex="215" onclick="clearDuc_od();return false;">[clear]</a>
        	</td>
			<td width="4%"></td>
        	<td colspan="2" width="25%">OS
        		<a href="javascript:void(0)" tabindex="216" onclick="setDuc_os();return false;">[normal]</a>
            	<a href="javascript:void(0)" tabindex="217" onclick="clearDuc_os();return false;">[clear]</a>
        	</td>
			<td ></td>
        	<td colspan="2" width="25%">OU
        		<a href="javascript:void(0)" tabindex="218" onclick="setDuc_ou();return false;">[normal]</a>
            	<a href="javascript:void(0)" tabindex="219" onclick="clearDuc_ou();return false;">[clear]</a>
        	</td>
        	</tr>
        	<tr>
				<td></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="220" measurement="duc_rur" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="221" measurement="duc_rul" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td width="4%"></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="222" measurement="duc_lur" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="223" measurement="duc_lul" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
				<td></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="224" measurement="dip_ur" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" maxlength="10" tabindex="225" measurement="dip_u" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>       	
        	</tr>
        	<tr>
				<td></td>
        		<td><input type="text" style="width:90%" tabindex="226" maxlength="10" measurement="duc_rr" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="227" maxlength="10" measurement="duc_rl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td width="4%"></td>
        		<td><input type="text" style="width:90%" tabindex="228" maxlength="10" measurement="duc_lr" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="229" maxlength="10" measurement="duc_ll" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td></td>
        		<td><input type="text" style="width:90%" tabindex="230" maxlength="10" measurement="dip_r" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="231" maxlength="10" measurement="dip_p" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>       	
        	</tr>
        	<tr>
				<td></td>
        		<td><input type="text" style="width:90%" tabindex="232" maxlength="10" measurement="duc_rdr" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="233" maxlength="10" measurement="duc_rdl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td width="4%"></td>
        		<td><input type="text" style="width:90%" tabindex="234" maxlength="10" measurement="duc_ldr" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="235" maxlength="10" measurement="duc_ldl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td></td>
        		<td><input type="text" style="width:90%" tabindex="236" maxlength="10" measurement="dip_dr" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" style="width:90%" tabindex="237" maxlength="10" measurement="dip_d" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>       	
        	</tr>
        </table>
        </div>
	</div>
</td>
</tr>
<%}%>

<% if (!deviation_measurement) {%>
<tr>
<td>

	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="238" href="javascript:void(0)" id="a_28" onclick="togglediv(this);">Deviation Measurement:</a>
			
        </div>
        <div id="s_28" class="slideblock">
        <table class="exam" width="100%">
			<tr>
			<td align="center" width="11%">
				<a href="javascript:void(0)" tabindex="239" onclick="setDeviation();return false;">[normal]</a>
            	<a href="javascript:void(0)" tabindex="240" onclick="clearDeviation();return false;">[clear]</a>
			</td>
			<td width="40%">
				<table>
					<tr>
						<td width="30%"></td>
						<td width="30%"><input type="text" tabindex="241" maxlength="30" measurement="dev_u" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
						<td width="30%"></td>
					</tr>
					<tr>
						<td width="30%"><input type="text" tabindex="242" maxlength="30" measurement="dev_r" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
						<td width="30%"><input type="text" tabindex="243" maxlength="30" measurement="dev_p" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
						<td width="30%"><input type="text" tabindex="244" maxlength="30" measurement="dev_l" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
					</tr>
					<tr>
						<td width="30%"><input type="text" tabindex="245" maxlength="30" measurement="dev_rt" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
						<td width="30%"><input type="text" tabindex="246" maxlength="30" measurement="dev_d" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
						<td width="30%"><input type="text" tabindex="247" maxlength="30" measurement="dev_lt" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
					</tr>
				</table>
			</td>
			<td width="38%">
				<table>
					<tr>
						<td class="label">Near</td>
						<td><input type="text" tabindex="248" maxlength="30" measurement="dev_near" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
					</tr>
					<tr>
						<td class="label">+3.00D</td>
						<td><input type="text" tabindex="249" maxlength="30" measurement="dev_plus3" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
					</tr>
					<tr>
						<td class="label">Far Distance</td>
						<td><input type="text" tabindex="250" maxlength="30" measurement="dev_far" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
					</tr>
				</table>
			</td>
			<td></td>
			</tr>
			
        </table>
        </div>
    </div>

</td>
</tr>
<%}%>

<% if (!external_orbit) {%>
<tr>
<td>

	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="251" href="javascript:void(0)" id="a_29" onclick="togglediv(this);">External/Orbit:</a>
			
        </div>
        <div id="s_29" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        		<td width="8%"></td>
        		<td width="46%">OD
        			<a href="javascript:void(0)" tabindex="252" onclick="setExternal_od();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="253" onclick="clearExternal_od();return false;">[clear]</a>
        		</td>
        		<td width="46%">OS
        			<a href="javascript:void(0)" tabindex="254" onclick="setExternal_os();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="255" onclick="clearExternal_os();return false;">[clear]</a>
        		</td>
        	</tr>
        	<tr>
        		<td class="label">Face</td>
        		<td><input type="text" tabindex="256" style="width:93%" measurement="ext_rface" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="257" style="width:93%" measurement="ext_lface" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Retropulsion</td>
        		<td><input type="text" tabindex="258" style="width:93%" measurement="ext_rretro" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="259" style="width:93%" measurement="ext_lretro" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Hertel</td>
        		<td><input type="text" tabindex="260" style="width:93%" measurement="ext_rhertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="261" style="width:93%" measurement="ext_lhertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>

</td>
</tr>
<%}%>

<% if (!eyelid_nld) {%>
<tr>
<td>

	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="262" href="javascript:void(0)" id="a_30" onclick="togglediv(this);">Eyelid/NLD:</a>
			
        </div>
        <div id="s_30" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        		<td width="8%"></td>
        		<td width="46%">OD
        			<a href="javascript:void(0)" tabindex="263" onclick="setEyelid_od();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="264" onclick="clearEyelid_od();return false;">[clear]</a>
        		</td>
        		<td width="46%">OS
        			<a href="javascript:void(0)" tabindex="265" onclick="setEyelid_os();return false;">[normal]</a>
            		<a href="javascript:void(0)" tabindex="266" onclick="clearEyelid_os();return false;">[clear]</a>
        		</td>
        	</tr>
        	<tr>
        		<td class="label">Upper lid</td>
        		<td><input type="text" tabindex="267" style="width:93%" measurement="ext_rul" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="268" style="width:93%" measurement="ext_lul" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Lower lid</td>
        		<td><input type="text" tabindex="269" style="width:93%" measurement="ext_rll" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="270" style="width:93%" measurement="ext_lll" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Lac lake</td>
        		<td><input type="text" tabindex="271" style="width:93%" measurement="ext_rlake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="272" style="width:93%" measurement="ext_llake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Lac Irrig</td>
        		<td><input type="text" tabindex="273" style="width:93%" measurement="ext_rirrig" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="274" style="width:93%" measurement="ext_lirrig" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Punctum</td>
        		<td><input type="text" tabindex="275" style="width:93%" measurement="ext_rpunc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="276" style="width:93%" measurement="ext_lpunc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">NLD</td>
        		<td><input type="text" tabindex="277" style="width:93%" measurement="ext_rnld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="278" style="width:93%" measurement="ext_lnld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        		<td class="label">Dye Disapp</td>
        		<td><input type="text" tabindex="279" style="width:93%" measurement="ext_rdye" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        		<td><input type="text" tabindex="280" style="width:93%" measurement="ext_ldye" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
	</div>

</td>
</tr>
<%}%>

<% if (!eyelid_measurement) {%>
<tr>
<td>

	<div class="slidey">
		 <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="281" href="javascript:void(0)" id="a_31" onclick="togglediv(this);">Eyelid Measurement:</a>
			
        </div>
        <div id="s_31" class="slideblock">
        <table class="exam" width="100%">
     		<tr>
     			<td width="8%"></td>
     			<td colspan="2" align="center" width="46%">OD</td>
     			<td colspan="2" align="center" width="46%">OS</td>
     		</tr>
     		<tr>
     			<td></td>
     			<td>MRD</td>
     			<td>ISS</td>
     			<td>MRD</td>
     			<td>ISS</td>
     		</tr>
     		<tr>
     			<td class="label">Lid margins</td>
     			<td colspan="2"><input type="text" tabindex="282" maxlength="50" style="width:45%" measurement="lid_rmrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>&nbsp&nbsp
								<input type="text" tabindex="283" maxlength="50" style="width:45%" measurement="lid_riss" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
				</td>
     			<td colspan="2"><input type="text" tabindex="284" maxlength="50" style="width:45%" measurement="lid_lmrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>&nbsp&nbsp
								<input type="text" tabindex="285" maxlength="50" style="width:45%" measurement="lid_liss" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
				</td>
     		</tr>
     		<tr>
     			<td class="label">Levator Fn</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="286" maxlength="50" measurement="lid_rlev" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="287" maxlength="50" measurement="lid_llev" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>
     		<tr>
     			<td class="label">Lag</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="288" maxlength="50" measurement="lid_rlag" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="289" maxlength="50" measurement="lid_llag" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>
     		<tr>
     			<td class="label">Blink</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="290" maxlength="50" measurement="lid_rblink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="291" maxlength="50" measurement="lid_lblink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>
     		<tr>
     			<td class="label">CN VII</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="292" maxlength="50" measurement="lid_rcn7" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="293" maxlength="50" measurement="lid_lcn7" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>
     		<tr>
     			<td class="label">Bells</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="294" maxlength="50" measurement="lid_rbell" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="295" maxlength="50" measurement="lid_lbell" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>
     		<tr>
     			<td class="label">Schirmer</td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="296" maxlength="50" measurement="lid_rschirm" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     			<td colspan="2" align="center"><input type="text" style="width:93%" tabindex="297" maxlength="50" measurement="lid_lschirm" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
     		</tr>		
     	</table>
     	</div>
   	</div>

</td>
</tr>
<%}%>

<%}else{%>
<tr>
<td>
<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="31" id="a_21" onclick="togglediv(this);">Vision Pretest:</a>
        </div>
        <div id="s_21" class="slideblock">
            <table class="exam" width="100%">
            <tr>
            <td colspan="3">OD</td>
            <td ></td>
            <td colspan="3">OS</td>
            </tr>

			<tr>
            <td >Sph</td>
            <td >Cyl</td>
            <td >Axis</td>
            <td></td>
            <td>Sph</td>
            <td >Cyl</td>
            <td >Axis</td>
            </tr>
			<tr>
            <td><input type="text" tabindex="32"  class="examfieldgrey" size="7" measurement="od_ar_sph" onfocus="whiteField(this);"/></td>
            <td><input type="text" tabindex="33"  class="examfieldgrey" size="7" measurement="od_ar_cyl" onfocus="whiteField(this);"/></td>
            <td><input type="text" tabindex="34"  class="examfieldgrey" size="7" measurement="od_ar_axis" onfocus="whiteField(this);"/></td>


            <td >AR</td>
            <td ><input type="text" tabindex="35" class="examfieldgrey" size="7" measurement="os_ar_sph" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="36" class="examfieldgrey" size="7" measurement="os_ar_cyl" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="37" class="examfieldgrey" size="7" measurement="os_ar_axis" onfocus="whiteField(this);"/></td>

			</tr>
			<tr>
            <td >K1</td>
            <td >K2</td>
            <td >K2-Axis</td>
            <td ></td>
            <td >K1</td>
            <td >K2</td>
            <td >K2-Axis</td>
            </tr>
			<tr>
            <td ><input type="text"  tabindex="38"  class="examfieldgrey" size="7" measurement="od_k1" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="39"  class="examfieldgrey" size="7" measurement="od_k2" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="40"  class="examfieldgrey" size="7"  measurement="od_k2_axis" onfocus="whiteField(this);"/></td>


            <td >K</td>
            <td ><input type="text"  tabindex="41"  class="examfieldgrey" size="7" measurement="os_k1" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="42"  class="examfieldgrey" size="7" measurement="os_k2" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="43"  class="examfieldgrey" size="7" measurement="os_k2_axis" onfocus="whiteField(this);"/></td>

			</tr>
			<tr>
            <td >sc</td>
            <td >cc</td>
            <td >ph</td>
            <td ></td>
            <td >sc</td>
            <td >cc</td>
            <td >ph</td>
            </tr>
            <tr>
            <td ><input type="text"  tabindex="44"  class="examfieldgrey" size="7" measurement="od_sc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="45"  class="examfieldgrey" size="7" measurement="od_cc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="46"  class="examfieldgrey" size="7" measurement="od_ph_distance" onfocus="whiteField(this);"/></td>
            <td >Distance</td>
            <td ><input type="text"  tabindex="47"  class="examfieldgrey" size="7" measurement="os_sc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="48"  class="examfieldgrey" size="7" measurement="os_cc_distance" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  tabindex="49"  class="examfieldgrey" size="7" measurement="os_ph_distance" onfocus="whiteField(this);"/></td>

            </tr>

            <tr>
            <td><input type="text"  tabindex="50"  class="examfieldgrey" size="7" measurement="od_sc_near" onfocus="whiteField(this);"/></td>
            <td><input type="text"  tabindex="51"  class="examfieldgrey" size="7" measurement="od_cc_near" onfocus="whiteField(this);"/></td>
            <td></td>
           <td>Near</td>
            <td><input type="text"  tabindex="52"  class="examfieldgrey" size="7" measurement="os_sc_near" onfocus="whiteField(this);"/></td>
            <td><input type="text"  tabindex="53"  class="examfieldgrey" size="7" measurement="os_cc_near" onfocus="whiteField(this);"/></td>
            <td></td>
            </tr>




            </table>
        </div>
</div>
</td>
</tr>


<tr>
<td>
<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="60" href="javascript:void(0)" id="a_22" onclick="togglediv(this);">Vision Manifest:</a>
            <span>&nbsp;&nbsp;</span>

            <a href="javascript:void(0)" tabindex="61" onclick="copyAR();"> [copy AR] </a>
            <span>&nbsp;&nbsp;</span>
            <a href="javascript:void(0)" tabindex="62" onclick="copySpecs();"> [copy Specs] </a>

        </div>
        <div id="s_22" class="slideblock">
            <table class="exam" width="100%">
            <tr>
            <td colspan="6">OD</td>
            <td width="10%"></td>
            <td colspan="6">OS</td>
            </tr>
            <tr>
            <td width="6%">Sph</td>
            <td width="6%">Cyl</td>
            <td width="5%">Axis</td>
            <td width="6%">Add</td>
            <td width="1%"></td>
             <td width="9%">VA</td>
             <td width="9%">N</td>
            <td width="14%"></td>
            <td width="6%">Sph</td>
            <td width="6%">Cyl</td>
            <td width="5%">Axis</td>
            <td width="6%">Add</td>
            <td width="1%"></td>
            <td width="9%">VA</td>
            <td width="9%">N</td>
            </tr>
            <tr>
            <td><input type="text" size="5" tabindex="63" measurement="od_manifest_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="64" measurement="od_manifest_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="65" measurement="od_manifest_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text"  size="5" tabindex="66" measurement="od_manifest_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td><input type="text"  size="5" tabindex="67" measurement="od_manifest_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="68" measurement="od_manifest_near" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td >Manifest</td>
            <td><input type="text" size="5" tabindex="69" measurement="os_manifest_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="70" measurement="os_manifest_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="71" measurement="os_manifest_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="72" measurement="os_manifest_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td><input type="text" size="5" tabindex="73" measurement="os_manifest_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="74" measurement="os_manifest_near" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>


            </tr>

            <tr>
            <td ><input type="text" size="5" tabindex="75" measurement="od_cycloplegic_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="76" measurement="od_cycloplegic_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="77" measurement="od_cycloplegic_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td><input type="text" size="5" tabindex="78" measurement="od_cycloplegic_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td ><input type="text" size="5" tabindex="79" measurement="od_cycloplegic_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td></td>
            <td >Cycloplegic</td>
            <td ><input type="text" size="5" tabindex="80" measurement="os_cycloplegic_refraction_sph" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="81" measurement="os_cycloplegic_refraction_cyl" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="82" measurement="os_cycloplegic_refraction_axis" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ><input type="text" size="5" tabindex="83" measurement="os_cycloplegic_refraction_add" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td ></td>
            <td ><input type="text" size="5" tabindex="84" measurement="os_cycloplegic_distance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
            <td></td>
            </tr>

            </table>
        </div>
</div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="90" id="a_23" onclick="togglediv(this);">IOP:</a>

        </div>
        <div id="s_23" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="17%"></td>
        	<td width="15%">OD</td>
        	<td width="1%"></td>
        	<td width="34%"></td>
        	<td width="1%"></td>
        	<td width="15%">OS</td>
        	<td width="17%"></td>
        	</tr>
        	<tr>
        	<td ></td>
        	<td ><input type="text" tabindex="91" measurement="od_iop_nct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	<td width="34%" nowrap="nowrap">NCT(<span id="nct_ts"></span>)</td>
        	<td ></td>
        	<td ><input type="text" tabindex="93" measurement="os_iop_nct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	</tr>
        	<tr>
        	<td ></td>
        	<td ><input type="text" tabindex="94" measurement="od_iop_applanation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	<td width="34%">Applanation(<span id="applanation_ts"></span>)</td>
        	<td ></td>
        	<td ><input type="text" tabindex="95" measurement="os_iop_applanation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	</tr>
        	<tr>
        	<td></td>
        	<td><input type="text" tabindex="96" measurement="od_cct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td></td>
        	<td width="34%">CCT</td>
        	<td ></td>
        	<td><input type="text" tabindex="97" measurement="os_cct" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td ></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="100" href="javascript:void(0)" id="a_24" onclick="togglediv(this);">Special Exam:</a>

        </div>
        <div id="s_24" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="33%">OD</td>
        	<td width="34%"></td>
        	<td width="33%">OS</td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="101" measurement="od_color_vision" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">color vision</td>
        	<td width="33%"><input type="text" tabindex="102" measurement="os_color_vision" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="103" measurement="od_pupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">Pupil</td>
        	<td width="33%"><input type="text" tabindex="104" measurement="os_pupil" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="105" measurement="od_amsler_grid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">Amsler Grid</td>
        	<td width="33%"><input type="text" tabindex="106" measurement="os_amsler_grid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%"><input type="text" tabindex="107" measurement="od_pam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="34%">PAM</td>
        	<td width="33%"><input type="text" tabindex="108" measurement="os_pam" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="33%">
        	<input type="text" tabindex="109" measurement="od_confrontation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>

        	<td width="34%">Confrontation</td>
        	<td width="33%">
        	<input type="text" tabindex="110" measurement="os_confrontation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>

        	<tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" href="javascript:void(0)" tabindex="120" id="a_25" onclick="togglediv(this);">
            EOM/Stereo:
            </a>

        </div>
        <div id="s_25" class="slideblock">
        <textarea measurement="EOM" tabindex="121" onchange="syncFields(this)" cols="100" rows="4" class="examfieldgrey" onfocus="whiteField(this);"></textarea>

        </div>
    </div>
</td>
</tr>



<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="125" href="javascript:void(0)" id="a_26" onclick="togglediv(this);">Anterior Segment:</a>
            <span>&nbsp;&nbsp;</span>

            <a href="javascript:void(0)" tabindex="126" onclick="setAnterior();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="127" onclick="clearAnterior();return false;">[clear]</a>

        </div>
        <div id="s_26" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS<td>
        	</tr>
        	<tr>
        	<td><input type="text" size="50" tabindex="130" measurement="od_cornea" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Cornea</td>
        	<td><input type="text" size="50" tabindex="131" measurement="os_cornea" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<tr>
        	<td><input type="text" size="50" tabindex="132" measurement="od_conjuctiva_sclera" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Conjuctiva/Sclera</td>
        	<td><input type="text" size="50" tabindex="133" measurement="os_conjuctiva_sclera" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<tr>
        	<td><input type="text" size="50"  tabindex="134" measurement="od_anterior_chamber" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Anterior Chamber</td>
        	<td><input type="text"  size="50" tabindex="135" measurement="os_anterior_chamber" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>
        	<!-- start of angles -->
        	<tr>
        		<td>
        			<table class="exam" style="width:100%">
        				<tr>
        					<td width="100%" align="center"><input type="text" size="12" tabindex="136" measurement="od_angle_up" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        				<tr>
        					<td style="text-align:center">
	        					<input size="12" type="text" tabindex="137" measurement="od_angle_middle0" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        						<input size="12" type="text" tabindex="138" measurement="od_angle_middle1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input size="12" type="text" tabindex="139" measurement="od_angle_middle2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        					</td>
        				</tr>
        				<tr>
        					<td><input type="text" size="12" tabindex="140" measurement="od_angle_down" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        			</table>
        		</td>
        		<td>Angle</td>
        		<td>
        			<table class="exam" style="width:100%">
        				<tr>
        					<td width="100%" align="center"><input size="12" type="text" tabindex="141" measurement="os_angle_up" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>

        				<tr>
        					<td style="text-align:center">
        						<input size="12" type="text" tabindex="142" measurement="os_angle_middle0" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input size="12" type="text" tabindex="143" measurement="os_angle_middle1" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
								<input size="12" type="text" tabindex="144" measurement="os_angle_middle2" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/>
        					</td>
        				</tr>

        				<tr>
        					<td><input type="text" size="12" tabindex="145" measurement="os_angle_down" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        				</tr>
        			</table>
        		</td>
        	</tr>

        	<!-- end of angles -->
        	<tr>
        	<td><input type="text" size="50" tabindex="146" measurement="od_iris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Iris</td>
        	<td><input type="text" size="50" tabindex="147" measurement="os_iris" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>

        	<tr>
        	<td><input type="text" size="50" tabindex="148" measurement="od_lens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lens</td>
        	<td><input type="text" size="50" tabindex="149" measurement="os_lens" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/><td>
        	</tr>


        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="160" href="javascript:void(0)" id="a_27" onclick="togglediv(this);">Posterior Segment:</a>
            <span>&nbsp;&nbsp;</span>

            <a href="javascript:void(0)" tabindex="161" onclick="setPosterior();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="162" onclick="clearPosterior();return false;">[clear]</a>

        </div>
        <div id="s_27" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td width="48%" colspan="2">OD</td>
        	<td width="4%"></td>
        	<td width="48%" colspan="2">OS</td>
        	</tr>
        	<tr>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="163" measurement="od_disc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="4%">Disc</td>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="164" measurement="os_disc" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="165" measurement="od_cd_ratio_horizontal" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="4%">c/d ratio</td>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="166" measurement="os_cd_ratio_horizontal" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="167" measurement="od_macula" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="4%">Macula</td>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="168" measurement="os_macula" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="169" measurement="od_retina" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="4%">Retina</td>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="170" measurement="os_retina" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="171" measurement="od_vitreous" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td width="4%">Vitreous</td>
        	<td width="48%" colspan="2"><input type="text" size="50" tabindex="172" measurement="os_vitreous" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="175" href="javascript:void(0)" id="a_28" onclick="togglediv(this);">External:</a>
            <span>&nbsp;&nbsp;</span>
            <a href="javascript:void(0)" tabindex="176" onclick="setExternal();return false;">[normal]</a>
            <a href="javascript:void(0)" tabindex="177" onclick="clearExternal();return false;">[clear]</a>

        </div>
        <div id="s_28" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="178" measurement="od_face" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Face</td>
        	<td><input type="text" tabindex="179" measurement="os_face" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="180" measurement="od_upper_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Upper Lid</td>
        	<td><input type="text" tabindex="181" measurement="os_upper_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="182" measurement="od_lower_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>lower Lid</td>
        	<td><input type="text" tabindex="183" measurement="os_lower_lid" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="184" measurement="od_punctum" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Punctum</td>
        	<td><input type="text" tabindex="185" measurement="os_punctum" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="186" measurement="od_lacrimal_lake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lacrimal Lake</td>
        	<td><input type="text" tabindex="187" measurement="os_lacrimal_lake" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="190" href="javascript:void(0)" id="a_29" onclick="togglediv(this);">NLD:</a>

        </div>
        <div id="s_29" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td>
        	<input type="text" tabindex="191" measurement="od_lacrimal_irrigation" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>

        	<td>Lacrimal Irrigation</td>

        	<td><input type="text" tabindex="192" measurement="os_lacrimal_irrigation"  onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>

        	</tr>
        	<tr>
        	<td><input type="text" tabindex="193" measurement="od_nld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>NLD</td>
        	<td><input type="text" tabindex="194" measurement="os_nld" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="195" measurement="od_dye_disappearance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Dye Disappearance</td>
        	<td><input type="text" tabindex="196" measurement="os_dye_disappearance" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	</table>
        </div>
    </div>
</td>
</tr>

<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="200" href="javascript:void(0)" id="a_30" onclick="togglediv(this);">Eyelid Measurements:</a>

        </div>
        <div id="s_30" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="201" measurement="od_mrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>MRD</td>
        	<td><input type="text" tabindex="202" measurement="os_mrd" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="203" measurement="od_levator_function" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Levator Function</td>
        	<td><input type="text" tabindex="204" measurement="os_levator_function" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="205" measurement="od_inferior_scleral_show" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Inferior Scleral Show</td>
        	<td><input type="text" tabindex="206" measurement="os_inferior_scleral_show" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="207" measurement="od_cn_vii" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>CN VII</td>
        	<td><input type="text" tabindex="208" measurement="os_cn_vii" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="209" measurement="od_blink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Blink</td>
        	<td><input type="text" tabindex="210" measurement="os_blink" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="211" measurement="od_bells" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Bells</td>
        	<td><input type="text" tabindex="212" measurement="os_bells" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="213" measurement="od_lagophthalmos" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Lagophthalmos</td>
        	<td><input type="text" tabindex="214" measurement="os_lagophthalmos" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>
<tr>
<td>
	<div class="slidey">
        <div class="title">
            <a style="font-weight: bold;color:black;text-decoration:none;font-size:12px" tabindex="220" href="javascript:void(0)" id="a_31" onclick="togglediv(this);">Orbit:</a>

        </div>
        <div id="s_31" class="slideblock">
        <table class="exam" width="100%">
        	<tr>
        	<td>OD</td>
        	<td></td>
        	<td>OS</td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="221" measurement="od_hertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Hertel</td>
        	<td><input type="text" tabindex="222" measurement="os_hertel" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        	<tr>
        	<td><input type="text" tabindex="223" measurement="od_retropulsion" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	<td>Retropulsion</td>
        	<td><input type="text" tabindex="224" measurement="os_retropulsion" onchange="syncFields(this)" class="examfieldgrey" onfocus="whiteField(this);"/></td>
        	</tr>
        </table>
        </div>
    </div>
</td>
</tr>
<%}%>
</table>
<span style="font-size:10px">
<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
<a id="save_measurements" href="javascript:void(0)"  onclick="document.getElementById('hxForm').submit();">[Save Measurements]</a>
<%}else{%>
<a id="save_measurements" href="#">[Save Measurements]</a>
<%}%>
</span>
<span style="float:right;font-size:10px">
	<a href="javascript:void(0);" onclick="expandAll();">[expand all sections]</a>&nbsp;
	<a href="javascript:void(0);" onclick="collapseAll();">[collapse all sections]</a>
</span>
<script type="text/javascript">
for(var x=21;x<34;x++) {
	var name = 's_'+x;
	var el = document.getElementById(name);
	if(el) {
		el.style.display='none';
		effectMap[name] = 'up';
	}
}
setglasseshxclass();
</script>
