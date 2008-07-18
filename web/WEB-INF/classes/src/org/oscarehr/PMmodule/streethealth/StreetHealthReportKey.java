package org.oscarehr.PMmodule.streethealth;


public class StreetHealthReportKey {
	String cohort;
	String label;
	String answer;
	
	public StreetHealthReportKey() {
		
	}
	
	public StreetHealthReportKey(int cohort, String label, String answer) {
		setCohort(String.valueOf(cohort));
		setLabel(label);
		setAnswer(answer);
	}
	
	public String getCohort() {
		return cohort;
	}
	public void setCohort(String cohort) {
		this.cohort = cohort;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean equals(Object obj) {
		StreetHealthReportKey o;
        if (null == obj)
            return false;
        if (!(obj instanceof StreetHealthReportKey))
            return false;
        else {
        	o = (StreetHealthReportKey)obj;
        	return (this.getCohort().equals(o.getCohort()) && this.getAnswer().equals(o.getAnswer()) && this.getLabel().equals(o.getLabel()));
        }
	}
	
    public int hashCode() {
    	String hashStr = this.getClass().getName() + ":" + this.getCohort() + ":" + this.getAnswer() + ":" + this.getLabel();
    	return hashStr.hashCode();
    }

}
