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


package oscar.oscarTags;

import java.util.ArrayList;


public class TagObject {
    private ArrayList assignedTags;
    private String objectId;
    private String objectClass;
    
    public void assignTag(String tagName) {
        getAssignedTags().add(tagName);
    }

    public ArrayList getAssignedTags() {
        return assignedTags;
    }

    public void setAssignedTags(ArrayList assignedTags) {
        this.assignedTags = assignedTags;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignedTags == null) ? 0 : assignedTags.hashCode());
		result = prime * result + ((objectClass == null) ? 0 : objectClass.hashCode());
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TagObject other = (TagObject) obj;
		if (assignedTags == null) {
			if (other.assignedTags != null) return false;
		} else if (!assignedTags.equals(other.assignedTags)) return false;
		if (objectClass == null) {
			if (other.objectClass != null) return false;
		} else if (!objectClass.equals(other.objectClass)) return false;
		if (objectId == null) {
			if (other.objectId != null) return false;
		} else if (!objectId.equals(other.objectId)) return false;
		return true;
	}
    
    
    
}
