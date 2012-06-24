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

package org.oscarehr.web;

public enum Cds4FunctionCode {
    code0AS("0AS","Abuse Services"),
    code0AB("0AB","Alternative Businesses"),
    codeACT("ACT", "Assertive Community Treatment Teams"),
    codeCHI("CHI","Child/Adolescent"),
    codeCLU("CLU","Clubhouses"),
    code0CD("0CD"," Community Development"),
    codeCMH("CMH"," Community Mental Health Clinic"),
    code0IR("0IR"," Community Service Information and Referral"),
    codeCON("CON"," Concurrent Disorders"),
    code0CT("0CT"," Counseling & Treatment"),
    codeCSB("CSB"," Crisis Support Beds"),
    codeDCS("DCS"," Diversion & Court Support"),
    codeDDx("DDx"," Dual Diagnosis"),
    codeEAR("EAR"," Early Interventions"),
    codeEAT("EAT"," Eating Disorder"),
    code0FI("0FI"," Family Initiatives"),
    codeFOR("FOR"," Forensic"),
    codeHPA("HPA"," Health Promotion and Education -  General Awareness"),
    codeHPW("HPW"," Health Promotion and Education - Women's Mental Health"),
    codeHSC("HSC"," Homes for Special Care"),
    code0CM("0CM"," Mental Health Case Management"),
    codeCRI("CRI"," Mental Health Crisis Intervention"),
    codeOTH("OTH"," Other MH Services Not Elsewhere Identified"),
    codePSH("PSH"," Peer/Self-help Initiatives"),
    code0DN("0DN"," Primary Day/night Care"),
    codeGER("GER"," Psycho-geriatric"),
    code0SR("0SR"," Social Rehabilitation/Recreation"),
    code0SH("0SH"," Supportive Housing"),
    codeEMP("EMP"," Vocational/Employment");
    
    private String functionCode=null;
    private String functionName=null;
    
    Cds4FunctionCode(String functionCode, String functionName)
    {
    	this.functionCode=functionCode;
    	this.functionName=functionName;
    }

	public String getFunctionCode() {
    	return functionCode;
    }

	public String getFunctionName() {
    	return functionName;
    }

}
