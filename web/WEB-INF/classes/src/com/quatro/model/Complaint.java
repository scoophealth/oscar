package com.quatro.model;

//import java.util.Date;
import java.util.Calendar;

import com.quatro.common.KeyConstants;

import oscar.MyDateFormat;


/**
 * Complaint entity. @author JZhang
 */

public class Complaint  implements java.io.Serializable {

    // Fields    

     private Integer id;
     private String source;
     private String method;
     private String firstname;
     private String lastname;
     private String standards;
     private boolean standardsRelated;
     private String[] standards1 = {};
     private String description;
     private String satisfiedWithOutcome;
     private String standardsBreached;
     private String outstandingIssues;
     private String status;
     private Calendar completedDate;
     private Calendar createdDate;
     private String duration;
     private String person1;
     private String title1;
     private Calendar date1;
     private String person2;
     private String title2;
     private Calendar date2;
     private String person3;
     private String title3;
     private Calendar date3;
     private String person4;
     private String title4;
     private Calendar date4;
     private Integer clientId;
     private Integer programId;
     
     

     private String date1x;
     private String date2x;
     private String date3x;
     private String date4x;
     private String completedDatex;
     private String createdDatex;
     private String lastUpdateUser;
     private Calendar lastUpdateDate;
     
    // Constructors
/*
    public String getCreatedDateStr() {
		String str = "Unknown";
    	if(createdDate != null)
    		str = createdDate.toString().substring(0, 10);
    	return str;
	}
*/



	/** default constructor */
    public Complaint() {
    }

  
   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }

    public String getMethod() {
        return this.method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }

    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


	public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSatisfiedWithOutcome() {
        return this.satisfiedWithOutcome;
    }
    
    public void setSatisfiedWithOutcome(String satisfiedWithOutcome) {
        this.satisfiedWithOutcome = satisfiedWithOutcome;
    }

    public String getStandardsBreached() {
        return this.standardsBreached;
    }
    
    public void setStandardsBreached(String standardsBreached) {
        this.standardsBreached = standardsBreached;
    }

    public String getOutstandingIssues() {
        return this.outstandingIssues;
    }
    
    public void setOutstandingIssues(String outstandingIssues) {
        this.outstandingIssues = outstandingIssues;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Calendar getCompletedDate() {
        return this.completedDate;
    }
    
    public void setCompletedDate(Calendar completedDate) {
        this.completedDate = completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = MyDateFormat.getCalendar(completedDate);
    }

    public Calendar getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getDuration() {
        return this.duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPerson1() {
        return this.person1;
    }
    
    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getTitle1() {
        return this.title1;
    }
    
    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public Calendar getDate1() {
        return this.date1;
    }
    
    public void setDate1(Calendar date1) {
        this.date1 = date1;
    }

    public void setDate1(String date1) {
        this.date1 = MyDateFormat.getCalendar(date1);
    }

    public String getPerson2() {
        return this.person2;
    }
    
    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    public String getTitle2() {
        return this.title2;
    }
    
    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public Calendar getDate2() {
        return this.date2;
    }
    
    public void setDate2(Calendar date2) {
        this.date2 = date2;
    }

    public void setDate2(String date2) {
        this.date2 = MyDateFormat.getCalendar(date2);
    }
    
    public String getPerson3() {
        return this.person3;
    }
    
    public void setPerson3(String person3) {
        this.person3 = person3;
    }

    public String getTitle3() {
        return this.title3;
    }
    
    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public Calendar getDate3() {
        return this.date3;
    }
    
    public void setDate3(Calendar date3) {
        this.date3 = date3;
    }

    public void setDate3(String date3) {
        this.date3 = MyDateFormat.getCalendar(date3);
    }
    
    public String getPerson4() {
        return this.person4;
    }
    
    public void setPerson4(String person4) {
        this.person4 = person4;
    }

    public String getTitle4() {
        return this.title4;
    }
    
    public void setTitle4(String title4) {
        this.title4 = title4;
    }

    public Calendar getDate4() {
        return this.date4;
    }
    
    public void setDate4(Calendar date4) {
        this.date4 = date4;
    }

    public void setDate4(String date4) {
        this.date4 = MyDateFormat.getCalendar(date4);
    }

    public Integer getClientId() {
        return this.clientId;
    }
    
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

	public String getStandards() {
		return standards;
	}


	public void setStandards(String standards) {
		this.standards = standards;
	}


	public String[] getStandards1() {
		return standards1;
	}


	public void setStandards1(String[] standards1) {
		this.standards1 = standards1;
	}


	public String getCompletedDatex() {
		return completedDatex;
	}


	public void setCompletedDatex(String completedDatex) {
		this.completedDatex = completedDatex;
	}


	public String getDate1x() {
		return date1x;
	}


	public void setDate1x(String date1x) {
		this.date1x = date1x;
	}


	public String getDate2x() {
		return date2x;
	}


	public void setDate2x(String date2x) {
		this.date2x = date2x;
	}


	public String getDate3x() {
		return date3x;
	}


	public void setDate3x(String date3x) {
		this.date3x = date3x;
	}


	public String getDate4x() {
		return date4x;
	}


	public void setDate4x(String date4x) {
		this.date4x = date4x;
	}


	public String getCreatedDatex() {
		String str = "Unknown";
    	if(createdDate != null)
    		str = MyDateFormat.getStandardDate(createdDate);
    		
		return str;
	}
	public String getCreatedDatexFromPage() {
		   		
		return createdDatex;
	}

	public void setCreatedDatex(String createdDatex) {
		this.createdDatex = createdDatex;
	}



	public boolean isStandardsRelated() {
		return standardsRelated;
	}



	public void setStandardsRelated(boolean standardsRelated) {
		this.standardsRelated = standardsRelated;
	}



	public Calendar getLastUpdateDate() {
		return lastUpdateDate;
	}



	public void setLastUpdateDate(Calendar lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}



	public String getLastUpdateUser() {
		return lastUpdateUser;
	}



	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	 public String getComplaintStatus(){
	    	String str =KeyConstants.STATUS_ACTIVE;
	    	if( "1".equals(getStatus())){
	    		str=KeyConstants.STATUS_READONLY;
	    	}
	    	return str;
	    }

}