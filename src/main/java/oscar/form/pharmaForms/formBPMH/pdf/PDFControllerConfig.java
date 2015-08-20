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
package oscar.form.pharmaForms.formBPMH.pdf;

import java.util.HashMap;

public class PDFControllerConfig {

	private String dateFormat;
	private String[] targetBeans;
	private String regexStringFilter;
	private String[] javaScript;
	private HashMap<String, Integer[]> textLengthLimits;
	private HashMap<String, Integer[]> textBoxLineLimits;
	private HashMap<String, Integer[]> tableRowLimits;
	private Boolean freeTextFlattening;
	private Boolean formFlattening;
	
	public PDFControllerConfig() {
		// default
		setDateFormat("MM-dd-yyyy");
		setRegexStringFilter("[^a-zA-Z0-9_' '!.!#]");
		setFreeTextFlattening(Boolean.TRUE);
		setFormFlattening(Boolean.TRUE);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set the global date format. 
	 * Parameter is a String using a standard Simple Date Format
	 * ie: MM-dd-yyyy generates 05-13-1902
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	
	public String[] getTargetBeans() {
		return targetBeans;
	}

	/**
	 * Set the specific Beans or Bean packages which are targeted for
	 * parsing. Precise focus is better. This will help the PDFController avoid
	 * errors from Beans or methods it should not be using Reflection on.
	 * ie: org.oscarehr.common.model and/or oscar.form.pharmaForms.formBPMH.bean
	 * @param String[] targetBeans
	 */
	public void setTargetBeans(String[] targetBeans) {
		this.targetBeans = targetBeans;
	}

	public String getRegexStringFilter() {
		return regexStringFilter;
	}

	/**
	 * Set any Regex that can be used to filter out any undesired characters 
	 * in the Bean data as it is being parsed. 
	 * The default value is usually enough. 
	 * @param regexStringFilter
	 */
	public void setRegexStringFilter(String regexStringFilter) {
		this.regexStringFilter = regexStringFilter;
	}

	public String[] getJavaScript() {
		return javaScript;
	}

	/**
	 * Set an Array of javaScripts which can be used in the PDF. 
	 * ie: trigger local printing when pdf is opened: this.print({bUI: true, bSilent: true, bShrinkToFit:true});
	 * @param String[] javaScript [ array of scripts ]
	 */
	public void setJavaScript(String[] javaScript) {
		this.javaScript = javaScript;
	}

	public HashMap<String, Integer[]> getTextLengthLimits() {
		return textLengthLimits;
	}

	private void setTextLengthLimits(HashMap<String, Integer[]> textLengthLimits) {
		this.textLengthLimits = textLengthLimits;
	}
	
	/**
	 * Add character limits by text box or by line. 
	 * ie: set the character width for a line:  textBoxName.line
	 * for an entire single textbox: textBoxName
	 * The supporting method will add the page number to the textBoxName to 
	 * redirect overflow text.  ie: textBoxName2
	 * @param textBoxName
	 * @param characterLimit
	 * @param pageNumber
	 */
	public void addTextLengthLimits( String textBoxName, Integer characterLimit, Integer pageNumber ) {
		if( getTextLengthLimits() == null ) {
			setTextLengthLimits( new HashMap<String, Integer[]>() );
		}
		
		if( ! getTextLengthLimits().containsKey( textBoxName ) ) {
			Integer[] action = new Integer[]{ characterLimit, pageNumber };
			getTextLengthLimits().put( textBoxName, action );
		}
	}

	public HashMap<String, Integer[]> getTextBoxLineLimits() {
		return textBoxLineLimits;
	}

	private void setTextBoxLineLimits( HashMap<String, Integer[]> textBoxLineLimits ) {
		this.textBoxLineLimits = textBoxLineLimits;
	}
	
	public void addTextBoxLineLimits( String textBoxName, Integer lineLimit, Integer pageNumber ) {
		if( getTextBoxLineLimits() == null) {
			setTextBoxLineLimits( new HashMap<String, Integer[]>() );
		}
		
		if( ! getTextBoxLineLimits().containsKey( textBoxName ) ) {
			Integer[] action = new Integer[]{ lineLimit, pageNumber };
			getTextBoxLineLimits().put(textBoxName, action);
		}
	}

	public HashMap<String, Integer[]> getTableRowLimits() {
		return tableRowLimits;
	}

	private void setTableRowLimits(HashMap<String, Integer[]> tableRowLimits) {
		this.tableRowLimits = tableRowLimits;
	}
	
	/**
	 * Set configuration to trigger a new table for rows that exceed the default table 
	 * row limits.
	 * @param String [ table name ] usually a List name inside a form bean.
	 * @param Integer [ row limit ] limit of rows per table.
	 * @param Integer [ page number ] page number that holds the overflow table.
	 */
	public void addTableRowLimit( String tableName, Integer rowLimit, Integer pageNumber ) {
		if( getTableRowLimits() == null ) {
			setTableRowLimits( new HashMap<String, Integer[]>() );
		}
		
		if( ! getTableRowLimits().containsKey( tableName ) ) {
			Integer[] action = new Integer[]{ rowLimit, pageNumber };
			getTableRowLimits().put(tableName, action);
		}
	}

	public Boolean getFreeTextFlattening() {
		return freeTextFlattening;
	}

	/**
	 * Default is TRUE
	 * @param freeTextFlattening
	 */
	public void setFreeTextFlattening(Boolean freeTextFlattening) {
		this.freeTextFlattening = freeTextFlattening;
	}

	public Boolean getFormFlattening() {
		return formFlattening;
	}

	/**
	 * Default is TRUE
	 * @param formFlattening
	 */
	public void setFormFlattening(Boolean formFlattening) {
		this.formFlattening = formFlattening;
	}

}
