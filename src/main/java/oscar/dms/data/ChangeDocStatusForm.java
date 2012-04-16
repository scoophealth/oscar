/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package oscar.dms.data;

import org.apache.struts.action.ActionForm;

public class ChangeDocStatusForm extends ActionForm {
	
	
	private String docTypeD = "";
	private String docTypeP = "";
	private String statusD = "";
	private String statusP = "";
	
	public ChangeDocStatusForm() {
    }
	
	public String getDocTypeD() {
        return docTypeD;
	}
	
	public String getDocTypeP() {
        return docTypeP;
	}
	
	
	public String getStatusD() {
		 return statusD;
	 }
	
	public String getStatusP() {
		 return statusP;
	 }
	
	public void setDocTypeD(String docTypeD) {
        this.docTypeD = docTypeD;
	}
	
	public void setDocTypeP(String docTypeP) {
        this.docTypeP = docTypeP;
	}
	
	public void setStatusD(String statusD) {
		 this.statusD = statusD;
	 }
	
	public void setStatusP(String statusP) {
		 this.statusP = statusP;
	 }
	
	 
	
}
