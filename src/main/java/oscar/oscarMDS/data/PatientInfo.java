package oscar.oscarMDS.data;	
	
public class PatientInfo implements Comparable<PatientInfo> {
	public String firstName = "",
		          lastName = "";
	public int    id,
		   		  docCount = 0,
		   		  labCount = 0;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return lastName + ("".equals(lastName) ? "" : ", ") + firstName;			
	}
	@Override
	public boolean equals(Object obj) {			
		return obj instanceof PatientInfo && ((PatientInfo)obj).id == this.id;
	}
	public PatientInfo(int id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public int getDocCount() {
		return docCount;
	}
	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}
	public int getLabCount() {
		return labCount;
	}
	public void setLabCount(int labCount) {
		this.labCount = labCount;
	}
	@Override
	public int compareTo(PatientInfo that) {
		return this.lastName.equals(that.lastName) ? this.firstName.compareTo(that.firstName) : this.lastName.compareTo(that.lastName);
	}		
}