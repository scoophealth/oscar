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
    codeHPA("HPA"," Health Promotion and Education – General Awareness"),
    codeHPW("HPW"," Health Promotion and Education – Women’s Mental Health"),
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
