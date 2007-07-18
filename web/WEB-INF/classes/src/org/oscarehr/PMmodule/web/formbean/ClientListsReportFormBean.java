package org.oscarehr.PMmodule.web.formbean;

import java.lang.reflect.Method;

public class ClientListsReportFormBean {

	private int providerId;
	private String seenStartDate;
	private String seenEndDate;
	private int programId;
	private String enrolledStartDate;
	private String enrolledEndDate;
	
	public int getProviderId() {
    
    	return providerId;
    }
	public void setProviderId(int providerId) {
    
    	this.providerId = providerId;
    }
	public String getSeenEndDate() {
    
    	return seenEndDate;
    }
	public void setSeenEndDate(String seenEndDate) {
    
    	this.seenEndDate = seenEndDate;
    }
	public String getSeenStartDate() {
    
    	return seenStartDate;
    }
	public void setSeenStartDate(String seenStartDate) {
    
    	this.seenStartDate = seenStartDate;
    }
	
	
	public int getProgramId() {
    
    	return programId;
    }
	public void setProgramId(int programId) {
    
    	this.programId = programId;
    }
	
	public String getEnrolledEndDate() {
    
    	return enrolledEndDate;
    }
	public void setEnrolledEndDate(String enrolledEndDate) {
    
    	this.enrolledEndDate = enrolledEndDate;
    }
	public String getEnrolledStartDate() {
    
    	return enrolledStartDate;
    }
	public void setEnrolledStartDate(String enrolledStartDate) {
    
    	this.enrolledStartDate = enrolledStartDate;
    }
	
	/**
	 * This is a rather inefficient but convenient toString method which
	 * prints out all the variables of the object (or is suppose to).
	 * It achieves this by calling all the get* is* has* methods of the class.
	 * Becareful not to use this if the object can contain itself in recursive loops.
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();

		try {
			for (Method method : getClass().getDeclaredMethods()) {
				String methodName = method.getName();
				if (method.getParameterTypes().length == 0 && (methodName.startsWith("get") || methodName.startsWith("is") || methodName.startsWith("has"))) {
					if (sb.length() > 0) sb.append(", ");
					sb.append(methodName);
					sb.append("()=");
					sb.append(method.invoke(this));
				}
			}
		}
		catch (Exception e) {
			sb.append(e.getMessage());
		}

		return(sb.toString());
	}
}
