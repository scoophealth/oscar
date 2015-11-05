/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.util.LocaleUtils;

@Entity
@Table(name="tickler")
public class Tickler extends AbstractModel<Integer> {

	//These fields can be phased out in favor for the enums
	public static final String ACTIVE  = "A";
	public static final String COMPLETED = "C";
	public static final String DELETED = "D";
	      
	public static final String HIGH = "High";
	public static final String NORMAL = "Normal";
	public static final String LOW = "Low";
        
        public static final String NOT_APPLICABLE = "N/A";
	
	public static enum STATUS {
        A, C, D
	}
	
	public static enum PRIORITY {
        High, Normal, Low
	}
	
	public static final String DATE_FORMAT = "MM-dd-yyyy";
	public static final String TIME_FORMAT = "hh:mm a";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="tickler_no")
	private Integer id;
	
	@Column(name="demographic_no")
	private Integer demographicNo;
	
	@Column(name="program_id")
	private Integer programId;
	
	private String message;
	
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private STATUS status = STATUS.A;
	
	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate = new Date();
	
	@Column(name="service_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date serviceDate = new Date();
	
	@Column(length=6)
	private String creator;
	
	@Column(length=6)
	@Enumerated(EnumType.STRING)
	private PRIORITY priority = PRIORITY.Normal;
	
	@Column(name="task_assigned_to")
	private String taskAssignedTo;

	@Column(name="category_id")
	private Integer categoryId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="category_id", referencedColumnName="id", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private TicklerCategory ticklerCategory;
	
	@OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="tickler_no", referencedColumnName="tickler_no")
	@OrderBy("updateDate ASC")
	private Set<TicklerUpdate> updates = new HashSet<TicklerUpdate>();
	
	@OneToMany( fetch=FetchType.EAGER)
    @JoinColumn(name="tickler_no", referencedColumnName="tickler_no")
	@OrderBy("updateDate ASC")
	private Set<TicklerComment> comments = new HashSet<TicklerComment>();
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="demographic_no", referencedColumnName="demographic_no", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Demographic demographic;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="creator", referencedColumnName="provider_no", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Provider provider;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="task_assigned_to", referencedColumnName="provider_no", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Provider assignee;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="program_id", referencedColumnName="id", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Program program;

	
	@Transient
	private String demographic_webName;
	
	@Transient
	private String taskAssignedToName;
	
	public Tickler() {
		setUpdateDate(new Date());
		setServiceDate(new Date());
		setStatus(STATUS.A);
		setPriority(PRIORITY.Normal);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public PRIORITY getPriority() {
		return priority;
	}

	public void setPriority(PRIORITY priority) {
		this.priority = priority;
	}

	public String getTaskAssignedTo() {
		return taskAssignedTo;
	}

	public void setTaskAssignedTo(String taskAssignedTo) {
		this.taskAssignedTo = taskAssignedTo;
	}

	public Set<TicklerUpdate> getUpdates() {
		return updates;
	}

	public void setUpdates(Set<TicklerUpdate> updates) {
		this.updates = updates;
	}

	public Set<TicklerComment> getComments() {
		return comments;
	}

	public void setComments(Set<TicklerComment> comments) {
		this.comments = comments;
	}

	public Demographic getDemographic() {
		return demographic;
	}

	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Provider getAssignee() {
		return assignee;
	}

	public void setAssignee(Provider assignee) {
		this.assignee = assignee;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	//web stuff

    public String getStatusDesc(Locale locale) {
        String statusStr = "";            
        if (status.equals(Tickler.STATUS.A)){
            statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stActive");
        }
        else if (status.equals(Tickler.STATUS.C)) {               
            statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stComplete");
        }
        else if (status.equals(Tickler.STATUS.D)) {                
            statusStr = LocaleUtils.getMessage(locale,"tickler.ticklerMain.stDeleted");
        }
        return statusStr;
    }
    
    public void setStatusAsChar(char s) {
    	if(s == 'A' || s == 'a')
    		setStatus(Tickler.STATUS.A);
    	else if(s == 'C' || s == 'c')
    		setStatus(Tickler.STATUS.C);
    	else if(s == 'D' || s == 'd')
    		setStatus(Tickler.STATUS.D);
    	else
    		throw new IllegalArgumentException("Invalid status");
    }
    
    public void setPriorityAsString(String p) {
    	if(p != null && p.equalsIgnoreCase("Normal"))
    		setPriority(Tickler.PRIORITY.Normal);
    	else if(p != null && p.equalsIgnoreCase("High"))
    		setPriority(Tickler.PRIORITY.High);
    	else if(p != null &&  p.equalsIgnoreCase("Low"))
    		setPriority(Tickler.PRIORITY.Low);
    	else 
    		throw new IllegalArgumentException("Invalid priority");
    }
    
	public String getServiceDateWeb() {
		if(getServiceDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getServiceDate());
		}
		return "";
	}
	
	public void setServiceTime(String time) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
		Date d = formatter.parse(getServiceDateWeb() + " " + time);
		setServiceDate(d);
	}

	public String getDemographic_webName() {
		return demographic_webName;
	}

	public void setDemographic_webName(String demographic_webName) {
		this.demographic_webName = demographic_webName;
	}

	public String getTaskAssignedToName() {
		return taskAssignedToName;
	}

	public void setTaskAssignedToName(String taskAssignedToName) {
		this.taskAssignedToName = taskAssignedToName;
	}

	public void setStatusWeb(String s) {
		if(s != null && s.equals("C")) {
			setStatus(Tickler.STATUS.C);
		} else if(s != null && s.equals("D")) {
			setStatus(Tickler.STATUS.D);
		} else if(s != null && s.equals("A")){
			setStatus(Tickler.STATUS.A);
		}
	}
	
	public String getStatusWeb() {
		return getStatus().toString();
	}
	
	public void setPriorityWeb(String s) {
		if(s != null && s.equals("Normal")) {
			setPriority(Tickler.PRIORITY.Normal);
		} else if(s != null && s.equals("High")) {
			setPriority(Tickler.PRIORITY.High);
		} else if(s != null && s.equals("Low")){
			setPriority(Tickler.PRIORITY.Low);
		}
	}
	
	public String getPriorityWeb() {
		return getPriority().toString();
	}
	
         public static final Comparator<Tickler> DemographicNameAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            int compareVal = (Demographic.FormattedNameComparator.compare(t1.getDemographic(),t2.getDemographic()));
            
            //if there are more than one ticklers for a given demographic name, order them in ascending order by service date
            if (compareVal == 0) {
                compareVal = t1.getServiceDate().compareTo(t2.getServiceDate());
            }
            
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> DemographicNameDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            int compareVal = (Demographic.FormattedNameComparator.compare(t2.getDemographic(),t1.getDemographic()));
            if (compareVal == 0) {
                compareVal = t2.getServiceDate().compareTo(t1.getServiceDate());
            }
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> CreatorAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {  
            String creator1 = NOT_APPLICABLE;           
            if (t1.getProvider() != null) {
                creator1 = t1.getProvider().getFormattedName();
            }
            
            String creator2 = NOT_APPLICABLE;
            if (t2.getProvider() != null) {
                creator2 = t2.getProvider().getFormattedName();
            }
            
            int compareVal = (creator1.compareToIgnoreCase(creator2));
            if (compareVal == 0){
                compareVal = t1.getServiceDate().compareTo(t2.getServiceDate());
            }
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> CreatorDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {    
            String creator1 = NOT_APPLICABLE;           
            if (t1.getProvider() != null) {
                creator1 = t1.getProvider().getFormattedName();
            }
            
            String creator2 = NOT_APPLICABLE;
            if (t2.getProvider() != null) {
                creator2 = t2.getProvider().getFormattedName();
            }
            
            int compareVal = creator2.compareToIgnoreCase(creator1);
            if (compareVal == 0){
                compareVal = t2.getServiceDate().compareTo(t1.getServiceDate());
            }
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> ServiceDateAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            return (t1.getServiceDate().compareTo(t2.getServiceDate()));
	}
    };
    
    public static final Comparator<Tickler> ServiceDateDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            return (t2.getServiceDate().compareTo(t1.getServiceDate()));
	}
    };
    
    public static final Comparator<Tickler> CreationDateAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            return (t1.getUpdateDate().compareTo(t2.getUpdateDate()));
	}
    };
    
    public static final Comparator<Tickler> CreationDateDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            return (t2.getUpdateDate().compareTo(t1.getUpdateDate()));
	}
    };
    
    public static final Comparator<Tickler> PriorityAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {       
            int compareVal = 0;
            if (t1.getPriority().equals(PRIORITY.High) && !t2.getPriority().equals(PRIORITY.High)) {
                compareVal = 1;
            } else if (t2.getPriority().equals(PRIORITY.High)) {
                compareVal = -1;
            } else if (t1.getPriority().equals(PRIORITY.Normal) && !t2.getPriority().equals(PRIORITY.Normal)) {
                compareVal = 1;
            } else if (t2.getPriority().equals(PRIORITY.Normal)) {
                compareVal = -1;
            } else if (t1.getPriority().equals(PRIORITY.Low) && !t2.getPriority().equals(PRIORITY.Low)) {
                compareVal = 1;
            } else if (t2.getPriority().equals(PRIORITY.Low)) {
                compareVal = -1;
            } else { 
                compareVal = 0;
            }
            
            if (compareVal == 0) {
                compareVal = t1.getServiceDate().compareTo(t2.getServiceDate());
            }
            
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> PriorityDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {       
            int compareVal = 0;
            if (t2.getPriority().equals(PRIORITY.High) && !t1.getPriority().equals(PRIORITY.High)) {
                compareVal = 1;
            } else if (t1.getPriority().equals(PRIORITY.High)) {
                compareVal = -1;
            } else if (t2.getPriority().equals(PRIORITY.Normal) && !t1.getPriority().equals(PRIORITY.Normal)) {
                compareVal = 1;
            } else if (t1.getPriority().equals(PRIORITY.Normal)) {
                compareVal = -1;
            } else if (t2.getPriority().equals(PRIORITY.Low) && !t1.getPriority().equals(PRIORITY.Low)) {
                compareVal = 1;
            } else if (t1.getPriority().equals(PRIORITY.Low)) {
                compareVal = -1;
            } else { 
                compareVal = 0;
            }
            
            if (compareVal == 0) {
                compareVal = t2.getServiceDate().compareTo(t1.getServiceDate());
            }
            
            return compareVal;
	}
    };
    
    public static final Comparator<Tickler> TaskAssignedToAscComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {
            String assignee1 = t1.getAssignee().getLastName() + ", " + t1.getAssignee().getFirstName();
            String assignee2 = t2.getAssignee().getLastName() + ", " + t2.getAssignee().getFirstName();
            int compareVal = (assignee1.compareToIgnoreCase(assignee2));
            if (compareVal == 0) {
                compareVal = t1.getServiceDate().compareTo(t2.getServiceDate());
            }
            return compareVal;
	}
    };	
    
    public static final Comparator<Tickler> TaskAssignedToDescComparator = new Comparator<Tickler>() {
        @Override
	public int compare(Tickler t1, Tickler t2) {          
            String assignee1 = t1.getAssignee().getLastName() + ", " + t1.getAssignee().getFirstName();
            String assignee2 = t2.getAssignee().getLastName() + ", " + t2.getAssignee().getFirstName();
            int compareVal = (assignee2.compareToIgnoreCase(assignee1));
            if (compareVal == 0) {
                compareVal = t2.getServiceDate().compareTo(t1.getServiceDate());
            }
            return compareVal;
	}
    };

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public TicklerCategory getTicklerCategory() {
		return ticklerCategory;
	}

	public void setTicklerCategory(TicklerCategory ticklerCategory) {
		this.ticklerCategory = ticklerCategory;
	}

}

