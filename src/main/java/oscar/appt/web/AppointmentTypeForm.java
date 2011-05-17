package oscar.appt.web;
import org.apache.struts.action.ActionForm;

public class AppointmentTypeForm  extends ActionForm {
    private int typeNo;
    private Integer id;
    private String name;
    private String notes;
    private String reason;
    private String location;
    private String resources;    
    private int duration;
        
    public int getTypeNo() {
		return typeNo;
	}
	public void setTypeNo(int typeNo) {
		this.typeNo = typeNo;
	}
	
	public Integer getId() {
    	return id;
    }
	public void setId(Integer id) {
    	this.id = id;
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}

