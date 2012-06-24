/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.dms.data;

import org.apache.struts.action.ActionForm;

public class AddDocumentTypeForm extends ActionForm  {
	
	private String function = "";
	private String docType = "";
	
	
	public AddDocumentTypeForm() {
    }
	
	 public String getFunction() {
	        return function;
	 }
	 
	 public String getDocType() {
	        return docType;
	 }
	 
	
	 public void  setFunction(String function) {
	        this.function = function;
	 }
	 
	 public void setDocType(String docType) {
	        this.docType = docType;
	 }
	 
	
}
