package oscar.oscarRx.util;

import java.util.Comparator;

import org.oscarehr.common.model.Drug;

public class ShowAllSorter implements Comparator<Drug> {

	//current, current but will expire, expired, discontinued
	public int compare(Drug a,Drug b) {
		
		long now = System.currentTimeMillis();
        long month = 1000L * 60L * 60L * 24L * 30L;
        
        
		//current
		if (!a.isExpired() && !a.isArchived()) {
			if(!b.isExpired() && b.getEndDate()!=null && (b.getEndDate().getTime() - now <= month)) {
				return -1;
			}
			if(b.isExpired() || b.isArchived() || b.isDiscontinued()) {
				return -1;
			}
		}
		
		//current but will expire
		if (!a.isExpired() && a.getEndDate()!=null && (a.getEndDate().getTime() - now <= month)) { 
            if(b.isExpired() || b.isDiscontinued()) {
            	return -1;
            }
        }
		
		//expired
		if(a.isExpired() && b.isDiscontinued()) {
			return -1;
		}
		
		//discontinued
		
		
		if(a.isDiscontinued() && !b.isDiscontinued()) {
			return 1;
		}
		
		if(a.isExpired()) {
			if(!b.isExpired() && b.getEndDate()!=null && (b.getEndDate().getTime() - now <= month)) {
				return 1;
			}
			if(!b.isExpired() && !b.isArchived()) {
				return 1;
			}			
		}
		
		if(!a.isExpired() && a.getEndDate()!=null && (a.getEndDate().getTime() - now <= month)) {
			if(!b.isExpired() && !b.isArchived()) {
				return 1;
			}
		}				
		
		//all other are the same	
		return 0;
	}
}
