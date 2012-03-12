package oscar.oscarRx.pageUtil;

import org.oscarehr.common.model.Allergy;

public final class AllergyDisplay {
	private Integer id;
	private Integer remoteFacilityId;
	private String entryDate;
	private String description;
	private int typeCode;
	private String severityCode;
	private String onSetCode;
	private String reaction;
	private String startDate;
	private String archived;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRemoteFacilityId() {
		return (remoteFacilityId);
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}

	public String getEntryDate() {
		return (entryDate);
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTypeCode() {
		return (typeCode);
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public String getSeverityCode() {
		return (severityCode);
	}

	public void setSeverityCode(String severityCode) {
		this.severityCode = severityCode;
	}

	public String getOnSetCode() {
		return (onSetCode);
	}

	public void setOnSetCode(String onSetCode) {
		this.onSetCode = onSetCode;
	}

	public String getReaction() {
		return (reaction);
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public String getStartDate() {
		return (startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getOnSetDesc() {
		return (Allergy.getOnSetOfReactionDesc(onSetCode));
	}

	public String getSeverityDesc() {
		return (Allergy.getSeverityOfReactionDesc(severityCode));
	}

	public String getTypeDesc() {
		return (Allergy.getTypeDesc(typeCode));
	}

	public String getArchived() {
    	return archived;
    }

	public void setArchived(String archived) {
    	this.archived = archived;
    }

}
